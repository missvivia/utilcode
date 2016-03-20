package com.xyl.mmall.framework.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtil {

	public static Logger logger = Logger.getLogger(HttpUtil.class);
	
	/**
	 * Http请求超时设置
	 */
	public static int HTTPCLIENT_CONNECT_TIMEOUT = 3 * 1000;

	/**
	 * Http读取超时设置
	 */
	public static int HTTPCLIENT_SO_TIMEOUT = 3 * 1000;

	/**
	 * @param uri
	 * @return
	 */
	public static String getContent(String uri) {
		try {
			HttpClient client = genDefaultHttpClient();

			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpGet get = new HttpGet(uri);
			get.setConfig(genDefaultRequestConfig());
			HttpResponse response = client.execute(get);
			StatusLine line = response.getStatusLine();
			if (line.getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (IOException e) {
			logger.error(e);
		}

		return null;
	}

	public static HttpResponse sendPost(String uri, NameValuePair[] param) {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpPost post = new HttpPost(uri);
			post.setConfig(genDefaultRequestConfig());
			post.setEntity(new UrlEncodedFormEntity(Arrays.asList(param), Charset.forName("UTF-8")));
			HttpResponse response = client.execute(post);
			return response;
		} catch (Exception e) {
			logger.error("Send post error! URI : " + uri + ", param : " + JsonUtils.toJson(param), e);
		}
		return null;
	}

	public static HttpResponse sendHttpPostJson(String uri, String json) {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpPost post = new HttpPost(uri);
			post.setConfig(genDefaultRequestConfig());
			post.setHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(json));
			HttpResponse response = client.execute(post);
			return response;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static HttpResponse sendHttpsPostJson(String uri, String json) {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("https") ? uri : "https://" + uri;
			HttpPost post = new HttpPost(uri);
			post.setConfig(genDefaultRequestConfig());
			post.setHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(json));
			HttpResponse response = client.execute(post);
			return response;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	public static HttpResponse sendGet(String uri) {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpGet get = new HttpGet(uri);
			get.setConfig(genDefaultRequestConfig());
			HttpResponse response = client.execute(get);
			return response;
		} catch (IOException e) {
			logger.error(e);
		}

		return null;
	}
	
	/**
	 * @return
	 */
	private static HttpClient genDefaultHttpClient() {
		HttpClient httpClient = HttpClients.createDefault();
		return httpClient;
	}
	
	private static RequestConfig genDefaultRequestConfig() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(HTTPCLIENT_SO_TIMEOUT)
				.setConnectTimeout(HTTPCLIENT_CONNECT_TIMEOUT)
				.build();
		return requestConfig;
	}
}
