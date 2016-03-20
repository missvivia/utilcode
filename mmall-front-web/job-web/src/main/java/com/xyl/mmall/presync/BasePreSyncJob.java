package com.xyl.mmall.presync;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.base.BaseJobWithOtherFunction;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.util.DateUtil;
import com.xyl.mmall.util.HttpClientUtil;
import com.xyl.mmall.util.ResourceTextUtil;

/**
 * 数据预热父类
 * <p>
 * 主要提供
 * 
 * @author hzzhaozhenzuo
 * 
 */
public abstract class BasePreSyncJob extends BaseJobWithOtherFunction {

	protected static final ResourceBundle presyncBundle = ResourceTextUtil.getResourceBundleByName("content.presync");

	private static final Logger logger = LoggerFactory.getLogger(BasePreSyncJob.class);

	private String bizDesc;

	/**
	 * 预热job业务描述
	 */
	protected abstract void fillBizDesc();

	@Override
	public boolean entrance(JobParam param){
		
		//设置预执业务描述
		this.fillBizDesc();
		
		//执行业务预热
		boolean flag=execute(param);
		
		//发送邮件
		long time=param.getCommonParam().getTimestamp();
		String dateShow=DateUtil.parseLongToString(DateUtil.DATE_TIME_FORMAT, time);
		String mailTitle=ResourceTextUtil.getTextFromResourceByKey(presyncBundle, "mail.title", bizDesc,dateShow);
		StringBuilder builder=new StringBuilder(32);
		builder.append(mailTitle+"<br/>");
		String desc="成功";
		if(!flag){
			desc="失败";
		}
		builder.append(desc);
		boolean mailFlag=super.sendMail(param, mailTitle, builder.toString());
		if(!mailFlag){
			logger.error("send mail fail for presync:"+bizDesc);
		}
		
		return flag;
	}
	
	public boolean invokeUrl(String url, int invokeNum) {
		boolean successFlag = true;
		for (int i = 0; i < invokeNum; i++) {
			if (!HttpClientUtil.executeSimple(url)) {
				logger.error("===error when presync ,url:"+url);
				successFlag = false;
			}
		}
		return successFlag;
	}
	
	public boolean invokeUrlWithJsonParam(String url, int invokeNum,String paramWithJson) {
		boolean successFlag = true;
		for (int i = 0; i < invokeNum; i++) {
			if (!HttpClientUtil.executePostByJsonParam(url, paramWithJson)) {
				logger.error("===error when presync ,url:"+url);
				successFlag = false;
			}
		}
		return successFlag;
	}

	public String getBizDesc() {
		return bizDesc;
	}

	public void setBizDesc(String bizDesc) {
		this.bizDesc = bizDesc;
	}

}
