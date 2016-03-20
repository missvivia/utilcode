/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter;

/**
 * 基于HTTP的adapter抽象类
 * 
 * @author hzzengchengyuan
 */
public abstract class AbstractHttpWarehouseAdapter extends AbstractWarehouseAdapter {

	/**
	 * 向仓库接口发出HTTP POST请求，并返回结果
	 * 
	 * @param p_variables
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	protected String doPost(Map<String, String> p_variables) throws ClientProtocolException, IOException {
		List<NameValuePair> variables = new ArrayList<>();
		for (String key : p_variables.keySet()) {
			variables.add(new BasicNameValuePair(key, p_variables.get(key)));
		}
		HttpClient httpClient = null;
		httpClient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(getUrl());
		post.setEntity(new UrlEncodedFormEntity(variables, getCharSet()));
		HttpResponse response = httpClient.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), getCharSet()));
		StringBuffer result = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		String xml = result.toString();
		xml = replace("&lt;", '<' + "", xml);
		xml = replace("&gt;", '>' + "", xml);
		return xml;
	}

	protected String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer str = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			str.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		str.append(source);
		return str.toString();
	}

}
