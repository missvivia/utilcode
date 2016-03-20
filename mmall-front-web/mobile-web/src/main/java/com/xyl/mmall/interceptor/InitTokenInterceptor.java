package com.xyl.mmall.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.xyl.mmall.common.annotation.InitToken;
import com.xyl.mmall.framework.config.NkvConfiguration;

/**
 * 初始化token
 * 
 * 
 */
@Component("initToken")
public class InitTokenInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(InitTokenInterceptor.class);
	
	@Resource
	private DefaultExtendNkvClient defaultExtendNkvClient;

	@Resource
	private NkvConfiguration nkvConfiguration;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			InitToken annotation = method.getMethodAnnotation(InitToken.class);
			if (annotation != null) {
				String token = createtToken();
				setTokenToCache(token);
				response.addHeader("Form-Token", token);
			}
		}
		return true;

	}

	private synchronized String createtToken() {
		String token = UUID.randomUUID().toString();
		return token;
	}

	@SuppressWarnings("deprecation")
	private void setTokenToCache(String token) {
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		map.put(token.getBytes(), token.getBytes());
		try {
			defaultExtendNkvClient.hmset(nkvConfiguration.rdb_common_namespace, token.getBytes(), map,
					new NkvOption(5000, (short) 0, 1 * 60 * 60));
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("put token in cache error:", e);
			e.printStackTrace();
		}
	}

}
