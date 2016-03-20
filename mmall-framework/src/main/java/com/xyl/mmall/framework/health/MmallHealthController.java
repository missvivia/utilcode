package com.xyl.mmall.framework.health;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * 不停服发布接口.
 * 
 * @author wangfeng
 *
 */
@Controller
public class MmallHealthController {

	private final static Logger LOGGER = LoggerFactory.getLogger(MmallHealthController.class);

	// ip白名单.
	private final static String[] WHITE_IP_ARRAY = { "192.168.25.2", "127.0.0.1", "192.168.24.10", "10.164.120.2",
			"10.120.152.63", "10.164.132.92" };

	@Autowired
	private MmallHealthIndicator mmallHealthIndicator;

	/**
	 * 查询状态接口.
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/health/status", method = RequestMethod.GET)
	public void status(HttpServletResponse response) throws IOException {
		Status status = mmallHealthIndicator.status();
		// SA约定的返回状态.
		if (status == Status.UP)
			response.setStatus(HttpServletResponse.SC_OK);
		else
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		printJsonResult(response, JsonUtils.toJson(status));
	}

	/**
	 * 上线接口.
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/health/active", method = RequestMethod.GET)
	public void active(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ipAddr = getIpAddr(request);
		LOGGER.info("active, ip=" + ipAddr);
		boolean isWhiteIP = CollectionUtil.isInArray(WHITE_IP_ARRAY, ipAddr);
		if (isWhiteIP) {
			boolean isSucc = mmallHealthIndicator.active();
			printResult(isSucc, response);
		} else {
			printNotSupportResult(response);
		}
	}

	/**
	 * 下线接口.
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/health/offline", method = RequestMethod.GET)
	public void offline(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ipAddr = getIpAddr(request);
		LOGGER.info("offline, ip=" + ipAddr);
		boolean isWhiteIP = CollectionUtil.isInArray(WHITE_IP_ARRAY, ipAddr);
		if (isWhiteIP) {
			boolean isSucc = mmallHealthIndicator.offline();
			printResult(isSucc, response);
		} else {
			printNotSupportResult(response);
		}
	}

	private void printResult(boolean isSucc, HttpServletResponse response) throws IOException {
		BaseJsonVO vo = new BaseJsonVO();
		if (isSucc) {
			vo.setCode(ErrorCode.SUCCESS);
			vo.setMessage(ErrorCode.SUCCESS.getDesc());
		} else {
			vo.setCode(ErrorCode.SERVICE_EXEC_FAIL);
			vo.setMessage(ErrorCode.SERVICE_EXEC_FAIL.getDesc());
		}
		printJsonResult(response, JsonUtils.toJson(vo));
	}

	private void printNotSupportResult(HttpServletResponse response) throws IOException {
		BaseJsonVO vo = new BaseJsonVO();
		vo.setCode(ErrorCode.NOT_SUPPORT);
		vo.setMessage(ErrorCode.NOT_SUPPORT.getDesc());
		printJsonResult(response, JsonUtils.toJson(vo));
	}

	private void printJsonResult(HttpServletResponse response, String result) throws IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(result);
	}

	/**
	 * 获取请求包里的真实IP地址(跳过前端代理)
	 * 
	 * @param request
	 *            请求包对象
	 * @return
	 */
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}

		if (StringUtils.isBlank(ip)) {
			return null;
		}

		Pattern pattern = Pattern.compile("((2[0-4]\\d|25[0-5]|1?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|1?\\d?\\d)");
		Matcher matcher = pattern.matcher(ip);
		if (!matcher.matches()) {
			return null;
		}
		return ip;
	}

}
