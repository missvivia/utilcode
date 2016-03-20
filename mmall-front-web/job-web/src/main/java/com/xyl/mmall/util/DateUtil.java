package com.xyl.mmall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATETIMEFORMAT = "yyyyMMddHHmmss";

	/**
	 * @param date
	 * @param format
	 * @return
	 */
	public static String parseDateToString(String format, Date date) {
		if (date == null) {
			return null;
		}
		if (StringUtils.isBlank(format)) {
			format = DATE_TIME_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * @param format
	 * @param time
	 * @return
	 */
	public static String parseLongToString(String format, long time) {
		SimpleDateFormat mydate = new SimpleDateFormat(format);
		return mydate.format(new Date(time));
	}

	/**
	 * 
	 * @param format
	 * @param time
	 * @return
	 */
	public static long parseStringToTime(String format, String time) {
		SimpleDateFormat mydate = new SimpleDateFormat(format);
		try {
			Date date = mydate.parse(time);
			if (date != null) {
				return date.getTime();
			}
		} catch (ParseException e) {
			logger.error("parse error",e);
		}
		return 0;
	}

	/**
	 * 
	 * @param format
	 * @param time
	 * @return
	 */
	public static Date parseStringToDate(String format, String time) {
		SimpleDateFormat mydate = new SimpleDateFormat(format);
		try {
			Date date = mydate.parse(time);
			if (date != null) {
				return date;
			}
		} catch (ParseException e) {
			logger.error("parse error",e);
		}
		return null;
	}

	/**
	 * 添加天数
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addDay(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.DAY_OF_MONTH, amount);

		return calendar.getTime();
	}

	public static long formatDate(Date date, String dateFormat) {
		String dateStr = parseDateToString(dateFormat, date);
		return parseStringToTime(dateFormat, dateStr);
	}

	public static Date dateFormat(Date date, String dateFormat) {
		String time = parseDateToString(dateFormat, date);
		return parseStringToDate(dateFormat, time);
	}

	/**
	 * 得到某个日期的年月日形式的日期，小时，分钟，秒都是0 如： 2014-07-01 00:00:00
	 * 
	 * @param curDate
	 * @param dayNum
	 * @return
	 */
	public static Date getDateYMD(Date curDate, int dayNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, dayNum);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

}
