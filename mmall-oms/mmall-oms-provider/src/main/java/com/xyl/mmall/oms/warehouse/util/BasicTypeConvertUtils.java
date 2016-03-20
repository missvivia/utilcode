package com.xyl.mmall.oms.warehouse.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 转换为基本数据类型的工具类，转换都是通过String作为媒介进行转换，如:Object -> String -> int
 * 
 * @author hzzengchengyuan
 *
 */
public class BasicTypeConvertUtils {

	private static Map<Class<?>, BasicTypeConverter<?>> formatterMap = new ConcurrentHashMap<Class<?>, BasicTypeConverter<?>>();

	static {
		putFormatter(BasicTypeConverter.IntegerFormatter.INSTANCE);
		putFormatter(BasicTypeConverter.LongFormatter.INSTANCE);
		putFormatter(BasicTypeConverter.DoubleFormatter.INSTANCE);
		putFormatter(BasicTypeConverter.FloatFormatter.INSTANCE);
		putFormatter(BasicTypeConverter.ShortFormatter.INSTANCE);
		putFormatter(BasicTypeConverter.CharactorFormatter.INSTANCE);
		putFormatter(BasicTypeConverter.ByteFormatter.INSTANCE);
		putFormatter(BasicTypeConverter.BooleanFormatter.INSTANCE);
		putFormatter(BasicTypeConverter.StringFormatter.INSTANCE);
	}
	
	private static void putFormatter(BasicTypeConverter<?> formatter) {
		formatterMap.put(formatter.clazz(), formatter);
	}

	@SuppressWarnings("unchecked")
	private static <T> BasicTypeConverter<T> get(Class<?> clazz) {
		return (BasicTypeConverter<T>) formatterMap.get(clazz);
	}

	/**
	 * 转换目标数据为指定的基本数据类型，转换都是通过String作为媒介进行转换，如:Object -> String -> int。
	 * 如果目标对象为null则返回null，如果转换异常则返回默认值
	 * 
	 * @param obj
	 * @param clazz
	 */
	public static <T> T convert(Object obj, Class<T> clazz) {
		if (obj == null) {
			return null;
		}
		BasicTypeConverter<T> converter = get(detectBasicClass(clazz));
		if (converter == null) {
			return null;
		}
		String value = obj.toString();
		if (value.trim().length() == 0) {
			return converter.defaultVal();
		} else {
			try {
				return converter.convert(value);
			} catch (Throwable e) {
				return converter.defaultVal();
			}
		}
	}

	public static Class<?> detectBasicClass(Class<?> type) {
		if (type.equals(String.class)) {
			return String.class;
		} else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
			return Integer.class;
		} else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
			return Long.class;
		} else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
			return Double.class;
		} else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
			return Float.class;
		} else if (type.equals(Short.TYPE) || type.equals(Short.class)) {
			return Short.class;
		} else if (type.equals(Character.TYPE) || type.equals(Character.class)) {
			return Character.class;
		} else if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
			return Byte.class;
		} else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
			return Boolean.class;
		}
		return type;
	}

	public static boolean isWrapClass(Class<?> clz) {
		try {
			return clz == String.class || clz.isPrimitive() || ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isBaseClass(Class<?> clazz) {
		return BasicTypeConvertUtils.isWrapClass(clazz) || Object.class.equals(clazz);
	}

}