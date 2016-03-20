package com.xyl.mmall.controller;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.config.ExPropertyConfiguration;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.HttpUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.util.IPUtils;
import com.xyl.mmall.jms.service.util.ResourceTextUtil;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;
import com.xyl.mmall.obj.ExCustomeServiceStatus;
import com.xyl.mmall.obj.ExResponse;
import com.xyl.mmall.task.enums.PlatformType;

/**
 * 易信对接controller
 * 
 * @author hzzhaozhenzuo
 * 
 */
@Controller
public class ExController {

	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(ExController.class);

	@Autowired
	private ExPropertyConfiguration exPropertyConfiguration;

	private static final String newMessageContent = "您有新的回复";

	private static final String messageHeader = "exService";

	private static final String split = "|";

	@Autowired
	private MessagePushFacade messagePushFacade;

	private static final ResourceBundle exResourceBundle = ResourceTextUtil
			.getResourceBundleByName("config.exWhiteList");

	private static final String exWhiteList = ResourceTextUtil.getTextFromResourceByKey(exResourceBundle,
			"ex.whitelist");

	private static final Pattern exWhiteListPattern = Pattern.compile(exWhiteList);

	/**
	 * 提供给易信提示有新消息（针对留言反馈部分）
	 * 
	 * @param account
	 * @return
	 */
	@RequestMapping(value = "/m/ex/remindNewMessage")
	@ResponseBody
	public BaseJsonVO remindNewMessage(@RequestParam String account, HttpServletRequest request) {
		BaseJsonVO resVo = new BaseJsonVO();
		String ip = IPUtils.getIpAddr(request);
		String ipFromOrigin=request.getRemoteAddr();
		if (!this.canAccessRemindNewMessage(ip) && !this.canAccessRemindNewMessage(ipFromOrigin)) {
			logger.error("can not access remindNewMessage,ip:" + ip+",ipFromOrigin:"+ipFromOrigin);
			resVo.setResult(false);
			return resVo;
		}

		boolean flag = messagePushFacade.pushMessageForPrivate(account, PlatformType.ALL_PLATFORM, newMessageContent
				+ split + messageHeader);
		resVo.setResult(flag);
		return resVo;
	}

	private boolean canAccessRemindNewMessage(String ip) {
		Matcher matcher = exWhiteListPattern.matcher(ip);
		return matcher.matches();
	}

	/**
	 * 是否有新的消息接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ex/hasNewMessage", method = RequestMethod.GET)
	@ResponseBody
	private BaseJsonVO hasNewMessage() {
		BaseJsonVO resVo = new BaseJsonVO();

		long userId = SecurityContextUtils.getUserId();

		String url = exPropertyConfiguration.getNewLeaveMessageUrl() + userId;
		String res = HttpUtil.getContent(url);
		ExResponse response = JSON.parseObject(res, ExResponse.class);
		if (response != null && response.getHaveNewLeaveMessage() != null
				&& Boolean.TRUE.toString().equals(response.getHaveNewLeaveMessage())) {
			resVo.setResult(true);
			resVo.setCode(ErrorCode.SUCCESS);
		} else {
			resVo.setCode(ErrorCode.EMPTY);
			resVo.setResult(false);
		}
		return resVo;
	}

	@RequestMapping(value = "/ex/CustomeServiceStatus", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getCustomeServiceStatus() {
		BaseJsonVO resVo = new BaseJsonVO();

		ExCustomeServiceStatus exCustomeServiceStatus = this.getExCustomeServiceStatus();

		if (exCustomeServiceStatus == null || StringUtils.isEmpty(exCustomeServiceStatus.getOnline())
				|| exCustomeServiceStatus.getOnline().equalsIgnoreCase(Boolean.FALSE.toString())) {
			resVo.setCode(ErrorCode.DATA_NOT_MATCH);
			return resVo;
		}

		resVo.setCode(ErrorCode.SUCCESS);
		return resVo;

	}

	/**
	 * 客服在线或留言反馈请求
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 */
	@RequestMapping(value = "/ex/entrance", method = RequestMethod.GET)
	public void entrance(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

		long userId = SecurityContextUtils.getUserId();

		ExResponse exResponse = this.getExResponse(userId);
		if (exResponse == null) {
			logger.error("===can not got token,ex,userId:" + userId);
			return;
		}

		// 调用init
		this.invokeInit(exResponse, httpServletRequest, httpServletResponse);
	}

	/**
	 * 历史记录请求
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 */
	@RequestMapping(value = "/ex/history", method = RequestMethod.GET)
	public void history(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

		long userId = SecurityContextUtils.getUserId();

		ExResponse exResponse = this.getExResponse(userId);
		if (exResponse == null) {
			logger.error("===can not got token,ex,userId:" + userId);
			return;
		}

		// 调用init
		this.invokeInitForHistory(exResponse, httpServletRequest, httpServletResponse);
	}

	private void invokeInitForHistory(ExResponse exResponse, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		String url = exPropertyConfiguration.getInitUrl() + exResponse.getAccess_token() + "&openid="
				+ exResponse.getOpenid() + "&target=history";
		try {
			WebUtils.issueRedirect(httpServletRequest, httpServletResponse, url);
		} catch (IOException e) {
			logger.error("redirect error");
		}
	}

	private void invokeInit(ExResponse exResponse, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		String url = exPropertyConfiguration.getInitUrl() + exResponse.getAccess_token() + "&openid="
				+ exResponse.getOpenid() + "&target=thread";
		try {
			WebUtils.issueRedirect(httpServletRequest, httpServletResponse, url);
		} catch (IOException e) {
			logger.error("redirect error");
		}
	}

	private ExResponse getExResponse(long userId) {
		String url = exPropertyConfiguration.getTokenUrl() + userId;
		String res = HttpUtil.getContent(url);
		if (StringUtils.isEmpty(res)) {
			return null;
		}
		ExResponse response = JSON.parseObject(res, ExResponse.class);
		return response;
	}

	private ExCustomeServiceStatus getExCustomeServiceStatus() {
		String url = exPropertyConfiguration.getCustomeServiceStatusUrl();
		String res = HttpUtil.getContent(url);
		logger.info("===get customeservice:" + res);
		if (StringUtils.isEmpty(res)) {
			return null;
		}
		ExCustomeServiceStatus response = JSON.parseObject(res, ExCustomeServiceStatus.class);
		return response;
	}

}
