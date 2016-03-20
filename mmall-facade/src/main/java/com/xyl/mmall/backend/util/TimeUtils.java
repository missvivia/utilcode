/**
 * 
 */
package com.xyl.mmall.backend.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hzzengdan
 * 
 */

public class TimeUtils {

	public static String DATE_FORMAT = "yyyy-MM-dd";

	public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static SimpleDateFormat sdf = new SimpleDateFormat();

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(TimeUtils.class);

	public static long stringToLong(String dataStr) {
		if (StringUtils.isBlank(dataStr)) {
			return 0;
		}
		sdf.applyPattern(DATE_TIME_FORMAT);
		Date time = null;
		try {
			time = sdf.parse(dataStr);
		} catch (ParseException e) {
		}
		if (time == null) {
			sdf.applyPattern(DATE_FORMAT);
			try {
				time = sdf.parse(dataStr);
			} catch (ParseException e) {
				// 尝试直接以long类型转换
				try {
					time = new Date(Long.parseLong(dataStr));
				} catch (Exception e1) {
					e.printStackTrace();
				}
			}
		}
		return time == null ? 0 : time.getTime();
	}

	public static long stringToLong(String sl, String format) {
		if (StringUtils.isBlank(sl)) {
			return 0;
		}

		sdf.applyPattern(format);

		Date time = null;
		try {
			time = sdf.parse(sl);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (time == null) {
			return 0;
		}

		if (logger.isInfoEnabled()) {
			logger.info("字符串型时间：" + sl + "转化为long型时间：" + time);
		}
		return time.getTime();
	}

	public static long addDay(Date date, int amount) {
		if (date == null) {
			return 0;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.DAY_OF_MONTH, amount);

		return calendar.getTimeInMillis();
	}

	public static Date stringToDate(String dateTime, String format) {
		if (StringUtils.isEmpty(dateTime)) {
			return null;
		}

		sdf.applyPattern(format);

		try {
			return sdf.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
}
