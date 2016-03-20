package com.xyl.mmall.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.excelparse.ExcelParse;

public class ControllerUtils {
	private static final Logger logger = LoggerFactory.getLogger(ExcelParse.class);

	public static void operaSQLParam(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Class fieldType = field.getType();
			if (fieldType.getName().equals("java.lang.String")) {
				try {
					Method getMethod = ReflectUtil.genMethod(obj.getClass(), field, true);
					String value = (String) getMethod.invoke(obj);
					if (!StringUtils.isBlank(value)) {
						value = value.replaceAll("\\\\", "\\\\\\\\");
						Method setMethod = ReflectUtil.genMethod(obj.getClass(), field, false);
						setMethod.invoke(obj, value);
					}

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
}
