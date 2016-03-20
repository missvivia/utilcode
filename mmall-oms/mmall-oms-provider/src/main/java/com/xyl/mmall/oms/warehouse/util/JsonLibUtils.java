package com.xyl.mmall.oms.warehouse.util;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hzzengchengyuan
 *
 */
public class JsonLibUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonLibUtils.class);

	public static String object2json(Object obj) {
		try {
			return com.alibaba.fastjson.JSON.toJSONString(obj);
		} catch (Throwable e) {
			String str = obj == null ? "" : obj.toString();
			LOGGER.error(str, e);
			return str;
		}
		
	}

	public static String xml2json(String xmlString) {
		try {
			XMLSerializer xmlSerializer = new XMLSerializer();
			JSON json = xmlSerializer.read(xmlString);
			return json.toString();
		} catch (Throwable e) {
			LOGGER.error(xmlString, e);
			return xmlString;
		}
	}
}
