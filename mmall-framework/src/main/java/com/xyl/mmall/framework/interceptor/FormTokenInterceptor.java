package com.xyl.mmall.framework.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.xyl.mmall.framework.annotation.CheckFormToken;
import com.xyl.mmall.framework.config.NkvConfiguration;

/**
 * 表单重复提交拦截
 * 
 * @author author:lhp
 * @version date:2015年6月25日下午12:30:02
 */
@Component("tokenInterceptor")
public class FormTokenInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(FormTokenInterceptor.class);

	private static final String FORM_TOKEN = "formToken";

	@Resource
	private DefaultExtendNkvClient defaultExtendNkvClient;

	@Resource
	private NkvConfiguration nkvConfiguration;

	@SuppressWarnings("deprecation")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean valid = true;
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			CheckFormToken annotation = method.getMethodAnnotation(CheckFormToken.class);
			if (annotation != null && annotation.isCheckRepeat()) {
				String token = request.getParameter("formToken");// 买家平台放在paramter里
				boolean formflag = StringUtils.isEmpty(token) ? false : true;
				if (!formflag) {
					token = request.getHeader("Form-Token");// 运营平台和商家平台token放在headers里
				}
				if (StringUtils.isEmpty(token)) {
					valid = false;
				} else {
					Map<String, String> tokenMap = new HashMap<String, String>();
					Result<Map<byte[], byte[]>> formTokenMap = null;
					try {
						formTokenMap = defaultExtendNkvClient.hgetall(nkvConfiguration.rdb_common_namespace,
								token.getBytes(), new NkvOption(5000));
					} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
						logger.error("get from token error:", e);
						return true;
					}
					for (Entry<byte[], byte[]> entry : formTokenMap.getResult().entrySet()) {
						tokenMap.put(new String(entry.getKey(), "UTF-8"), new String(entry.getValue(), "UTF-8"));
					}
					if (tokenMap.get(token) == null) {
						valid = false;
					} else {
						defaultExtendNkvClient.remove(nkvConfiguration.rdb_common_namespace, token.getBytes(),
								new NkvOption(5000));
					}
				}
				if (!valid) {
					response.setStatus(205);// 重复提交
					return false;
					// throw new
					// FormDupSubmitException("duplicate form submission");
				}
				if (!formflag) {// 判断是否走header token
					token = createtToken();
					setTokenToCache(token);
					response.addHeader("Form-Token", token);
				}
			}

		}
		return valid;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			CheckFormToken annotation = method.getMethodAnnotation(CheckFormToken.class);
			if (annotation != null && modelAndView != null) {
				String token = createtToken();
				setTokenToCache(token);
				modelAndView.addObject(FORM_TOKEN, token);
			}
		}
	}

	private String createtToken() {
		String token = UUID.randomUUID().toString();
		return token;
	}

	@SuppressWarnings("deprecation")
	private void setTokenToCache(String token) {
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		map.put(token.getBytes(), token.getBytes());
		try {
			defaultExtendNkvClient.hmset(nkvConfiguration.rdb_common_namespace, token.getBytes(), map, new NkvOption(
					5000, (short) 0, 1 * 60 * 60));
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("put token in cache error:", e);
			e.printStackTrace();
		}
	}

}
