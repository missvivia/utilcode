package com.xyl.mmall.framework.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * json工具类 - fastjson实现
 * 
 * @author wangfeng
 * 
 */
public final class JsonUtils {

	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	private JsonUtils() {
	}

	/**
	 * jakson参数
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 将Json的String数据,反序列化成对象
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		boolean isJackson = true;
		T t = null;
		try {
			if (isJackson
					|| (StringUtils.isNotBlank(jsonString) && clazz.isEnum() && clazz
							.getAnnotation(JsonDeserialize.class) != null))
				// CASE1:
				// 如果是枚举类,而且申明了JsonDeserialize,则用jakson反序列化(代码样本见:OrderFormState)
				t = mapper.readValue(jsonString, clazz);
			else
				// CASE2: 使用fastjson反序列化
				t = JSON.parseObject(jsonString, clazz);
		} catch (JSONException ex) {
			logger.error("JSONException", ex);
		} catch (IOException ex) {
			logger.error("IOException", ex);
		}
		return t;
	}

	/**
	 * 将Json的String数据,反序列化成对象
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object fromJson(String jsonString) {
		Object obj = null;
		try {
			obj = JSON.parse(jsonString);
		} catch (JSONException ex) {
			logger.error("JSONException", ex);
		}
		return obj;
	}

	/**
	 * 将Json的String数据,反序列化成对象
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
		List<T> list = null;
		try {
			list = JSON.parseArray(jsonString, clazz);
		} catch (JSONException ex) {
			logger.error("JSONException", ex);
		}
		return list;
	}

	/**
	 * 将对象,序列化成Json的String数据
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return toJson(obj, new SerializerFeature[0]);
	}
	
	/**
	 * 将对象,序列化成Json的String数据
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj, SerializerFeature... features) {
		boolean isJackson = true;
		String ret = null;
		try {
			if (isJackson
					|| (obj != null && obj.getClass().isEnum() && obj.getClass().getAnnotation(JsonFormat.class) != null))
				// CASE1:
				// 如果是枚举类,而且申明了JsonFormat,则用jakson序列化(代码样本见:OrderFormState)
				ret = mapper.writeValueAsString(obj);
			else
				// CASE2: 使用fastjson序列化
				ret = JSON.toJSONString(obj, features);
		} catch (JSONException ex) {
			logger.error("JSONException", ex);
		} catch (IOException ex) {
			logger.error("IOException", ex);
		}
		return ret;
	}

	/**
	 * @param obj
	 * @return
	 */
	public static String toJsonTabAsSpecial(Object obj) {
		String ret = null;
		try {
			ret = JSON.toJSONString(obj, SerializerFeature.WriteTabAsSpecial);
		} catch (JSONException ex) {
			logger.error("JSONException", ex);
		}
		return ret;
	}

	/**
	 * @param text
	 * @return
	 */
	public static JSONObject toJSONObject(String text) {
		JSONObject jsonObject = null;
		try {
			jsonObject = JSON.parseObject(text);
		} catch (JSONException ex) {
			logger.error("JSONException", ex);
		}
		return jsonObject;
	}

	public static void main(String args[]) {
	}

}
