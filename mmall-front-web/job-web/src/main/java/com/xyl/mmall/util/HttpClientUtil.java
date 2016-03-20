package com.xyl.mmall.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.base.JobResult;

public class HttpClientUtil {

	private static final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

	private static final HttpClient client = new HttpClient(connectionManager);

	private static final int DEFAULT_SO_TIMEOUT = 3000;

	private static final int DEFAULT_CONNECT_TIMEOUT = 3000;

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	// init httpClient
	{
		client.getHttpConnectionManager().getParams().setStaleCheckingEnabled(true);
		client.getHttpConnectionManager().getParams().setSoTimeout(DEFAULT_SO_TIMEOUT);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_CONNECT_TIMEOUT);
	}

	private static final int DEFAULT_RETRY_NUMS = 3;

	public static boolean execute(String url, JobResult result) {
		boolean resultFlag = true;

		GetMethod method = new GetMethod(url);
		NameValuePair[] nvPairs = new NameValuePair[6];
		nvPairs[0] = new NameValuePair("id", String.valueOf(result.getId()));
		nvPairs[1] = new NameValuePair("code", String.valueOf(result.getCode()));
		nvPairs[2] = new NameValuePair("result", String.valueOf(result.getResult()));
		nvPairs[3] = new NameValuePair("signature", String.valueOf(result.getSignature()));
		nvPairs[4] = new NameValuePair("timestamp", String.valueOf(result.getTimestamp()));
		nvPairs[5] = new NameValuePair("nonce", String.valueOf(result.getNonce()));
		method.setQueryString(nvPairs);

		try {
			logger.info("==callback url:" + url + ",uuid:" + result.getUuid());
			// Provide custom retry handler is necessary
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(DEFAULT_RETRY_NUMS, true));

			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + method.getStatusLine());
			}
			resultFlag = statusCode == HttpStatus.SC_OK ? true : false;

		} catch (HttpException e) {
			logger.error("Fatal protocol violation: " + e.getMessage() + ",uuid:" + result.getUuid());
			resultFlag = false;
		} catch (IOException e) {
			logger.error("Fatal transport error: " + e.getMessage() + ",uuid:" + result.getUuid());
			resultFlag = false;
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		logger.info("==fin callback url:" + url + ",uuid:" + result.getUuid());
		return resultFlag;
	}

	public static boolean executeSimple(String url) {
		boolean resultFlag = true;

		GetMethod method = new GetMethod(url);

		try {
			logger.info("==invoke url:" + url);

			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + method.getStatusLine());
			}
			resultFlag = statusCode == HttpStatus.SC_OK ? true : false;

		} catch (HttpException e) {
			logger.error("Fatal protocol violation: " + e.getMessage(), e);
			resultFlag = false;
		} catch (IOException e) {
			logger.error("Fatal transport error: " + e.getMessage(), e);
			resultFlag = false;
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return resultFlag;
	}

	public static boolean executeSimpleByParam(String url, Map<String, String> paramMap) {
		boolean resultFlag = true;

		GetMethod method = new GetMethod(url);

		try {
			logger.info("==invoke url:" + url);

			if (paramMap != null) {
				NameValuePair[] nvPairs = new NameValuePair[paramMap.size()];
				int count = 0;
				for (Entry<String, String> entry : paramMap.entrySet()) {
					nvPairs[count++] = new NameValuePair(entry.getKey(), entry.getValue());
				}
				method.setQueryString(nvPairs);
			}

			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + method.getStatusLine());
			}
			resultFlag = statusCode == HttpStatus.SC_OK ? true : false;

		} catch (HttpException e) {
			logger.error("Fatal protocol violation: " + e.getMessage(), e);
			resultFlag = false;
		} catch (IOException e) {
			logger.error("Fatal transport error: " + e.getMessage(), e);
			resultFlag = false;
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return resultFlag;
	}

	public static boolean executePost(String url, Map<String, String> paramMap) {
		boolean resultFlag = true;
		PostMethod method = new PostMethod(url);
		try {
			if (paramMap != null) {
				for (Entry<String, String> entry : paramMap.entrySet()) {
					method.addParameter(entry.getKey(), entry.getValue());
				}
			}

			int returnCode = client.executeMethod(method);

			resultFlag = returnCode == HttpStatus.SC_OK ? true : false;

		} catch (Exception e) {
			logger.error("fatal post error,url:" + url, e);
		} finally {
			method.releaseConnection();
		}
		return resultFlag;
	}

	public static boolean executePostByJsonParam(String url, String paramWithJson) {
		boolean resultFlag = true;
		StringRequestEntity requestEntity;
		try {
			requestEntity = new StringRequestEntity(paramWithJson, "application/json", "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("UnsupportedEncodingException when invoke url by post with json param,url：" + url);
			return false;
		}
		PostMethod method = new PostMethod(url);
		method.setRequestEntity(requestEntity);

		try {
			int returnCode = client.executeMethod(method);

			resultFlag = returnCode == HttpStatus.SC_OK ? true : false;

		} catch (Exception e) {
			logger.error("fatal post error,url:" + url, e);
		} finally {
			method.releaseConnection();
		}
		return resultFlag;
	}
}