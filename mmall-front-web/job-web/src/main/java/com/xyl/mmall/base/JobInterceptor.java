package com.xyl.mmall.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xyl.mmall.JobPropertyConfiguration;
import com.xyl.mmall.dao.JobHistoryDao;
import com.xyl.mmall.jms.service.util.ResourceTextUtil;
import com.xyl.mmall.meta.JobHistory;
import com.xyl.mmall.util.HttpClientUtil;
import com.xyl.mmall.util.ParamAnalyzer;
import com.xyl.mmall.util.SignUtil;

public class JobInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(JobInterceptor.class);

	private ApplicationContext applicationContext;

	private static final String DEFAULT_RESULT = "ok";

	@Autowired
	private JobHistoryDao jobHistoryDao;

	@Autowired
	private JobPropertyConfiguration jobPropertyConfiguration;

	private static final boolean NOT_EXECUTE_CONTROLLER = false;

	private static final boolean checkDuplicate = true;

	private static final ResourceBundle whiteListResouce = ResourceTextUtil
			.getResourceBundleByName("content.whitelist");

	private static final String HTTP_WHITELIST = ResourceTextUtil.getTextFromResourceByKey(whiteListResouce,
			"http.whitelist");

	private static final Pattern whiteListPattern = Pattern.compile(HTTP_WHITELIST);
	
	private boolean inHttpWhiteList(String url){
		Matcher matcher = whiteListPattern.matcher(url);
		return matcher.matches();
	}

	/**
	 * 拦截job任务请求
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("before process job:" + request.getRequestURI());

		long oldTime = System.currentTimeMillis();

		String jobPath = request.getRequestURI();
		if (this.inHttpWhiteList(jobPath)) {
			//在http执行白名单中
			return true;
		}

		// 参数认证
		boolean shouldExecute = this.checkJobParamFromRequest(request);
		if (!shouldExecute) {
			logger.error("can not execute job,param is null");
			this.sendResultToJobServer(null, JobCodeInfo.FAIL_CODE);
			return NOT_EXECUTE_CONTROLLER;
		}

		JobParam jobParam = this.getJobParamFromRequest(request);

		// 签名认证
		if (Boolean.TRUE.toString().equalsIgnoreCase(jobPropertyConfiguration.getCheckSign())) {
			boolean signIsValid = this.checkSign(jobParam);
			if (!signIsValid) {
				logger.error("sign is not valid,params:" + jobParam.toString());
				return false;
			}
		}

		// 取出对应url的job实例进行处理
		logger.info("jobPath invoke is:" + jobPath + ",params:" + jobParam.toString());
		Class jobClass = JobMaapingStore.getMappingClassByKey(jobPath);
		if (jobClass == null) {
			logger.error("can not find the jobPath:" + jobPath + ",mapping error");
			this.sendResultToJobServer(jobParam, JobCodeInfo.NOT_FOUND_CODE);
			return NOT_EXECUTE_CONTROLLER;
		}
		BaseJob job = (BaseJob) applicationContext.getBean(jobClass);

		if (checkDuplicate) {
			// 检查是否当前任务是否已经执行过（含执行中，执行失败，执行成功）
			if (this.jobHasExecuted(jobParam)) {
				logger.error("job has already executed," + jobParam.toString());
				this.sendResultToJobServer(jobParam, JobCodeInfo.FAIL_CODE);
				return NOT_EXECUTE_CONTROLLER;
			}
		}

		// 保存历史记录
		if (!this.saveJobHistory(jobParam, job)) {
			logger.error("cannot save history," + jobParam.toString());
			this.sendResultToJobServer(jobParam, JobCodeInfo.FAIL_CODE);
			return NOT_EXECUTE_CONTROLLER;
		}

		// 执行job
		boolean executeFlag = true;
		String errorDesc = "";
		try {
			executeFlag = job.entrance(jobParam);
		} catch (Exception e) {
			logger.error("execute job error,params:" + jobParam.toString(), e);
			executeFlag = false;
			errorDesc = "execute job error";
		}

		long costTime = System.currentTimeMillis() - oldTime;

		// 更新历史记录状态
		boolean historyUpdateFlag = jobHistoryDao.updateStatusByJobUniqueId(jobParam.getCommonParam().getUuid(),
				executeFlag ? JobCodeInfo.JOB_STATUS_SUCCESS : JobCodeInfo.JOB_STATUS_FAIL, costTime, errorDesc);
		if (!historyUpdateFlag) {
			// job历史状态未更新时，只记录log，后续将执行业务job的执行状态返回给job服务器
			logger.error("fail to update status for job history," + jobParam.toString());
		}

		// 回调
		this.sendResultToJobServer(jobParam, executeFlag ? JobCodeInfo.SUCCESS_CODE : JobCodeInfo.FAIL_CODE);
		return NOT_EXECUTE_CONTROLLER;
	}

	/**
	 * job是否已经执行过
	 * 
	 * @param jobParam
	 * @return
	 */
	private boolean jobHasExecuted(JobParam jobParam) {
		JobHistory jobHistory = jobHistoryDao.queryByJobUniqueId(jobParam.getCommonParam().getUuid());
		return jobHistory != null ? true : false;
	}

	private boolean saveJobHistory(JobParam jobParam, BaseJob job) {
		JobHistory jobHistory = new JobHistory();
		jobHistory.setJobTypeId(jobParam.getCommonParam().getId());
		jobHistory.setJobUniqueId(jobParam.getCommonParam().getUuid());
		jobHistory.setGenerateTime(jobParam.getCommonParam().getTimestamp());
		jobHistory.setProcessStatus(JobCodeInfo.JOB_STATUS_PROCESSING);
		jobHistory.setProcessTime(System.currentTimeMillis());
		jobHistory.setCreateTime(System.currentTimeMillis());
		return jobHistoryDao.addObject(jobHistory) != null ? true : false;
	}

	private void sendResultToJobServer(JobParam jobParam, String code) {
		boolean flag = HttpClientUtil.execute(jobPropertyConfiguration.getBackUrl(),
				this.componseJobResult(jobParam, code));
		if (!flag) {
			logger.error("response to job server error");
		}
	}

	/**
	 * 指定结果具体的code
	 * 
	 * @param jobParam
	 * @param code
	 * @return
	 */
	private JobResult componseJobResult(JobParam jobParam, String code) {
		JobResult result = new JobResult();
		result.setCode(code);
		result.setResult(DEFAULT_RESULT);

		if (jobParam != null && jobParam.getCommonParam() != null) {
			result.setId(jobParam.getCommonParam().getId());
			result.setNonce(jobParam.getCommonParam().getNonce());
			result.setSignature(jobParam.getCommonParam().getSignature());
			result.setTimestamp(jobParam.getCommonParam().getTimestamp());
			result.setUuid(jobParam.getCommonParam().getUuid());
		}
		return result;
	}

	private boolean checkJobParamFromRequest(HttpServletRequest request) {
		if (request.getParameter(JobCodeInfo.ID_PREFIX) == null
				|| request.getParameter(JobCodeInfo.UUID_PREFIX) == null
				|| request.getParameter(JobCodeInfo.SIGNATURE_PREFIX) == null
				|| request.getParameter(JobCodeInfo.TIMESTAMP_PREFIX) == null
				|| request.getParameter(JobCodeInfo.NONCE_PREFIX) == null) {
			logger.error("job server pass param error");
			return false;
		}
		return true;
	}

	private JobParam getJobParamFromRequest(HttpServletRequest request) {
		JobParam jobParam = new JobParam();
		JobCommonParam commonParam = new JobCommonParam();
		jobParam.setCommonParam(commonParam);

		commonParam.setId(Long.valueOf(request.getParameter(JobCodeInfo.ID_PREFIX)));
		commonParam.setUuid(request.getParameter(JobCodeInfo.UUID_PREFIX).toString());
		commonParam.setSignature(request.getParameter(JobCodeInfo.SIGNATURE_PREFIX).toString());
		commonParam.setTimestamp(Long.valueOf(request.getParameter(JobCodeInfo.TIMESTAMP_PREFIX)));
		commonParam.setNonce(request.getParameter(JobCodeInfo.NONCE_PREFIX).toString());
		this.analyzeParam(request, jobParam);
		return jobParam;
	}

	private void analyzeParam(HttpServletRequest request, JobParam jobParam) {
		// all param
		Map<String, String[]> paramsMap = request.getParameterMap();
		Map<String, Object> paramAllMap = new HashMap<String, Object>();
		jobParam.setParamMap(paramAllMap);
		for (Entry<String, String[]> entry : paramsMap.entrySet()) {
			Object valueRes = this.getParamKeyValueArr(entry.getKey(), entry.getValue()[0], jobParam);
			paramAllMap.put(entry.getKey(), valueRes);
		}
	}

	private Object getParamKeyValueArr(String key, Object value, JobParam jobParam) {
		Object res = value;
		if (JobCodeInfo.START_TIME_PARAM.equals(key) || JobCodeInfo.END_TIME_PARAM.equals(key)) {
			res = ParamAnalyzer.getDateByAnalyzeTimeParam(jobParam.getCommonParam().getTimestamp(),
					String.valueOf(value));
		}
		return res;
	}

	private boolean checkSign(JobParam jobParam) {
		// 验证签名
		String sig = SignUtil.genSig(jobPropertyConfiguration.getSecretKey(),
				String.valueOf(jobParam.getCommonParam().getTimestamp()), jobParam.getCommonParam().getNonce());
		return sig.equals(jobParam.getCommonParam().getSignature());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
