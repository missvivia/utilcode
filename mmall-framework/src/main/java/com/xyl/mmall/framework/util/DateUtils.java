package com.xyl.mmall.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * DateUtils.java
 * 
 */
public class DateUtils {
	
	private static Logger logger = Logger.getLogger(DateUtils.class);
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATETIMEFORMAT = "yyyyMMddHHmmss";
	
	public static final  String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

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
		if (time <= 0) {
			return "";
		}
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
			logger.error(e);
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
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * 添加天数
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
	
	/** * 获取指定日期是星期几
	  * 参数为null时表示获取当前日期是星期几
	  * @param date
	  * @return
	*/
	public static String getWeekOfDate(Date date) {      
	    Calendar calendar = Calendar.getInstance();      
	    if(date != null){        
	         calendar.setTime(date);      
	    }        
	    int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;      
	    if (w < 0){        
	        w = 0;      
	    }      
	    return weekOfDays[w];    
	}
}
