package com.xyl.mmall.oms.util;

import java.util.Calendar;
import java.util.Date;

public class ReportTimeUtil {
	/**
	 * 一天的微秒数
	 */
	public static final long ONEDAY = 24 * 60 * 60 * 1000L;
	/**
	 * 下午四点
	 */
	public static final long HOUR16 = 16 * 60 * 60 * 1000L;
	
	/**
	 * 下午四点半
	 */
	public static final long HOUR16ANDHALF = (16 * 60 + 30) * 60 * 1000L;
	
	public static long getLastDayTimeZeroMillis() {
		long time = getDayTimeZeroMillis();
		return time - ONEDAY;
	}

	public static long getDayTimeZeroMillis() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

}
