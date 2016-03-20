package com.xyl.mmall.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对日期进行处理的工具类.
 * 
 * @author luojian,yydx811
 */
public class DateUtil {
	
	/** The Constant LONG_PATTERN. */
	public final static String LONG_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/** The Constant MINUTE_PATTERN. */
	public final static String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	
	/** The Constant SHORT_PATTERN. */
	public final static String SHORT_PATTERN = "yyyyMMddHHmmss";
	
	/** The Constant PATTERN_THREE. */
	public final static String PATTERN_THREE = "yyyy-MM-dd";
	
	/** The Constant PATTERN_FOUR. */
	public final static String PATTERN_FOUR = "yyyyMM";

	/** The Constant PATTERN_THREE 2014年1月1日. */
	public final static String PATTERN_FIVE = "yyyy年MM月dd日";
	
	/** 一天毫秒数. */
	public final static long ONE_DAY_MILLISECONDS = 60l * 60l * 24l * 1000l;

	/** 一天秒数. */
	public final static long ONE_DAY_SECONDS = 60l * 60l * 24l;

	/** yyyy.MM.dd HH:mm:ss. */
	public final static String LONG_PATTERN_SPLIT_POINT = "yyyy.MM.dd HH:mm:ss";
	
	/**
	 * pattern 日期的格式，如：yyyy/MM/dd yyyy-MM-dd yyyy-MM-dd HH:mm:ss.
	 * 
	 * @param strDate the str date
	 * @param pattern the pattern
	 * @return the java.util. date
	 */
	public static Date stringToDate(String strDate, String pattern) {
		if (strDate == null || strDate.trim().length() <= 0)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			return null;
		}

	}

	/**
	 * 不指定格式的转换函数.
	 * 
	 * @param strDate the str date
	 * @return the  date
	 */
	public static Date stringToDate(String strDate) {
		if (strDate == null || strDate.trim().length() <= 0)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			return null;
		}

	}

	/**
	 * Check date.
	 * 
	 * @param strDate the str date
	 * @return true, if successful
	 */
	public static boolean checkDate(String strDate) {
		if (strDate == null || strDate.length() == 0)
			return true;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.parse(strDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * Date to string.
	 * 
	 * @param date the date
	 * @param pattern the pattern
	 * @return the string
	 */
	public static String dateToString(Date date, String pattern) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * Date add days.
	 * 
	 * @param date the date
	 * @param addDays the add days
	 * @return the date
	 */
	public static Date dateAddDays(Date date, int addDays) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDays);
		return cal.getTime();
	}

	/**
	 * Date add months.
	 * 
	 * @param date the date
	 * @param addMonths the add months
	 * @return the date
	 */
	public static Date dateAddMonths(Date date, int addMonths) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, addMonths);
		return cal.getTime();
	}

	/**
	 * Date add weeks.
	 * 
	 * @param date the date
	 * @param addWeeks the add weeks
	 * @return the date
	 */
	public static Date dateAddWeeks(Date date, int addWeeks) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, addWeeks);
		return cal.getTime();
	}

	/**
	 * Date add years.
	 * 
	 * @param date the date
	 * @param addYears the add years
	 * @return the date
	 */
	public static Date dateAddYears(Date date, int addYears) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, addYears);
		return cal.getTime();
	}

	/**
	 * Gets the date length.
	 * 
	 * @param beginDate the begin date
	 * @param endDate the end date
	 * @return the date length
	 */
	public static int getDateLength(Date beginDate, Date endDate) {
		int length = 0;
		if (beginDate == null || endDate == null)
			return length;

		length = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24));
		length++;
		return length;
	}

	/**
	 * Gets the date begin.
	 * 
	 * @param strDateSegment the str date segment
	 * @return the date begin
	 */
	public static String getDateBegin(String strDateSegment) {
		if (strDateSegment == null || strDateSegment.length() <= 0)
			return null;
		return strDateSegment + " 00:00:00";
	}

	/**
	 * Gets the date end.
	 * 
	 * @param strDateSegment the str date segment
	 * @return the date end
	 */
	public static String getDateEnd(String strDateSegment) {
		if (strDateSegment == null || strDateSegment.length() <= 0)
			return null;
		return strDateSegment + " 23:59:59";
	}

	/**
	 * 检查录入的日期格式（yyyyMMdd-yyyyMMdd或yyyyMMdd）.
	 * 
	 * @param strDate the str date
	 * @return true, if successful
	 */
	public static boolean checkLongDatePattern(String strDate) {
		String ps = "^\\d{4}\\d{1,2}\\d{1,2}-\\d{4}\\d{1,2}\\d{1,2}$|^\\d{4}\\d{1,2}\\d{1,2}$";
		Pattern p = Pattern.compile(ps);
		Matcher m = p.matcher(strDate);
		if (!m.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 检查录入的日期格式（yyyy-MM-dd）.
	 * 
	 * @param strDate the str date
	 * @return true, if successful
	 */
	public static boolean checkDatePattern(String strDate) {
		String ps = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
		Pattern p = Pattern.compile(ps);
		Matcher m = p.matcher(strDate);
		if (!m.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 检查和格式化日期 param:strDate
	 * 如:2007-06-01--2007-12-01,2007-06-18,20070601--20071201,20070718.
	 * return:HashMap 格式化以后的date对象：beginDate和endDate throws ParseException
	 * 输入的日期格式错误
	 * 
	 * @param strDate the str date
	 * @return the list
	 * @throws ParseException the parse exception
	 */
	public static List<Date> transformStrDateToListDate(String strDate) throws ParseException {
		List<Date> dateList = new ArrayList<Date>();
		String[] tempStrDate = strDate.split(",");
		for (int i = 0; i < tempStrDate.length; i++) {
			// 将字符串格式化为“yyyy-MM-dd--yyyy-MM-dd”
			Date tempDate = stringToDate(tempStrDate[i], "yyyy-MM-dd");
			dateList.add(tempDate);
		}

		return dateList;
	}

	/**
	 * 检查和格式化日期 param:strDate
	 * 如:2007-06-01--2007-12-01,2007-06-18,20070601--20071201,20070718
	 * return:HashMap 格式化以后String型的日期：beginDate和endDate throws ParseException
	 * 输入的日期格式错误
	 * 
	 * @param strDate the str date
	 * @return the hash map
	 * @throws ParseException the parse exception
	 */
	public static HashMap<String, String> formatStrDateToDateMap(String strDate) throws ParseException {
		HashMap<String, String> dateMap = new HashMap<String, String>();

		Date beginDate = null;
		Date endDate = null;
		if (strDate == null || strDate.trim().length() == 0)
			return null;

		if (strDate.indexOf("--") > 0) {
			String[] tempDate = strDate.split("--");
			if (tempDate.length == 2) {
				if (tempDate[0].indexOf("-") > 0) {
					beginDate = stringToDateThrowsExe(tempDate[0], "yyyy-MM-dd");
				} else {
					beginDate = stringToDateThrowsExe(tempDate[0], "yyyyMMdd");
				}
				if (tempDate[1].indexOf("-") > 0) {
					endDate = stringToDateThrowsExe(tempDate[1], "yyyy-MM-dd");
				} else {
					endDate = stringToDateThrowsExe(tempDate[1], "yyyyMMdd");
				}
			} else if (tempDate.length == 1) {
				if (tempDate[0].indexOf("-") > 0) {
					beginDate = stringToDateThrowsExe(tempDate[0], "yyyy-MM-dd");
					endDate = stringToDateThrowsExe(tempDate[0], "yyyy-MM-dd");
				} else {
					beginDate = stringToDateThrowsExe(tempDate[0], "yyyyMMdd");
					endDate = stringToDateThrowsExe(tempDate[0], "yyyyMMdd");
				}
			}
		} else {
			if (strDate.indexOf("-") > 0) {
				beginDate = stringToDateThrowsExe(strDate, "yyyy-MM-dd");
				endDate = stringToDateThrowsExe(strDate, "yyyy-MM-dd");
			} else {
				beginDate = stringToDateThrowsExe(strDate, "yyyyMMdd");
				endDate = stringToDateThrowsExe(strDate, "yyyyMMdd");
			}
		}
		dateMap.put("beginDate", dateToString(beginDate, "yyyy-MM-dd"));
		dateMap.put("endDate", dateToString(endDate, "yyyy-MM-dd"));
		return dateMap;
	}

	/**
	 * String to date throws exe.
	 * 
	 * @param strDate the str date
	 * @param pattern the pattern
	 * @return the  date
	 * @throws ParseException the parse exception
	 */
	public static Date stringToDateThrowsExe(String strDate, String pattern) throws ParseException {
		if (strDate == null || strDate.trim().length() <= 0)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(strDate);
	}

	/**
	 * 检查日期的先后顺序 param:beginDate endDate return:boolean boolean
	 * beginDate<=endDate 返回true；beginDate>endDate返回false
	 * 
	 * @param beginDate the begin date
	 * @param endDate the end date
	 * @return true, if successful
	 */
	public static boolean compareDate(String beginDate, String endDate) {
		if (endDate == null || endDate.trim().length() == 0)
			endDate = beginDate;

		Date bDate = stringToDate(beginDate, "yyyy-MM-dd");
		Date eDate = stringToDate(endDate, "yyyy-MM-dd");

		if (bDate == null || eDate == null)
			return true;
		if (!bDate.after(eDate))
			return true;
		return false;
	}

	/**
	 * 检查输入的日期字符串中的重复日期 param:strDate
	 * 格式如：2007-06-01--2007-12-01,2007-06-18,20070601--20071201,20070618
	 * return:list 字符串日期列表
	 * 
	 * @param strDate the str date
	 * @return the list
	 * @throws ParseException the parse exception
	 */
	public static List<String> checkRepeatDate(String strDate) throws ParseException {
		List<String> repeatDateList = new ArrayList<String>();
		Map<Date, String> mediaPlanDateMap = new HashMap<Date, String>();

		String[] tempStrDate = strDate.split(",");

		for (int k = 0; k < tempStrDate.length; k++) {
			HashMap<String, String> dateMap = new HashMap<String, String>();
			dateMap = DateUtil.formatStrDateToDateMap(tempStrDate[k]);
			if (dateMap == null)
				continue;

			String strBeginDate = (String) dateMap.get("beginDate");
			String strEndDate = (String) dateMap.get("endDate");

			if (strEndDate == null || strEndDate.length() == 0){
				strEndDate = strBeginDate;
			}
			// 检查同一个媒介计划有没有重复日期
			Date tmpBeginDate = null;
			Date tmpEndDate = null;
			if (strBeginDate.indexOf("-") > 0) {
				tmpBeginDate = DateUtil.stringToDate(strBeginDate,
						"yyyy-MM-dd");
				tmpEndDate = DateUtil.stringToDate(strEndDate, "yyyy-MM-dd");
			} else {
				tmpBeginDate = DateUtil.stringToDate(strBeginDate, "yyyyMMdd");
				tmpEndDate = DateUtil.stringToDate(strEndDate, "yyyyMMdd");
			}

			int len = DateUtil.getDateLength(tmpBeginDate, tmpEndDate);
			Date tmpDate = null;
			for (int j = 0; j < len; j++) {
				tmpDate = DateUtil.dateAddDays(tmpBeginDate, j);
				if (mediaPlanDateMap.size() > 0) {
					if ("1".equals(mediaPlanDateMap.get(tmpDate))) {
						repeatDateList.add(DateUtil.dateToString(tmpDate,
								"yyyy-MM-dd"));
					} else {
						mediaPlanDateMap.put(tmpDate, "1");
					}
				} else {
					mediaPlanDateMap.put(tmpDate, "1");
				}
			}
		}
		return repeatDateList;
	}

	/**
	 * 获得给定日期中的 年.
	 * 
	 * @param date 日期
	 * @return 日期中的 年
	 */
	public static String parseYear(Date date) {
		return new SimpleDateFormat(SHORT_PATTERN, Locale.CHINA).format(date).substring(0, 4);
	}

	/**
	 * 获得当前日期中的 年.
	 * 
	 * @return 日期中的 年
	 */
	public static String parseYear() {
		Date date = Calendar.getInstance().getTime();
		return new SimpleDateFormat(SHORT_PATTERN, Locale.CHINA).format(date).substring(0, 4);
	}

	/**
	 * Date to simple string.
	 * 
	 * @param date the date
	 * @return the string
	 */
	public static String dateToSimpleString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	/**
	 * String simple to date.
	 * 
	 * @param date the date
	 * @return the  date
	 */
	public static Date stringSimpleToDate(String date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return (Date) format.parseObject(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the quarter by month.
	 * 
	 * @param month the month
	 * @return the quarter by month
	 */
	public static int getQuarterByMonth(int month) {
		if (month >= 1 && month <= 12) {
			return ((month + 2) / 3);
		} else {
			return -1;
		}
	}

	/**
	 * * 得到date所在周的周日（第一天）.
	 * 
	 * @param date
	 *            the date
	 * @return the sunday
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getSunday(Date date) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int intWeek = c.get(c.DAY_OF_WEEK);
			c.add(c.DATE, -(intWeek - 1));
			date = (c.getTime());
		} catch (Exception e) {
			System.err.println("ReportGatekeeper.java--getSunday:" + e);
		}

		return date;
	}

	/**
	 * * 得到date所在周，周六（最后一天）.
	 * 
	 * @param date
	 *            the date
	 * @return the saturday
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getSaturday(Date date) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(getSunday(date));
			c.add(c.DATE, 6);
			date = (c.getTime());
		} catch (Exception e) {
			System.err.println("ReportGatekeeper.java--getSunday:" + e);
		}

		return date;
	}

	/**
	 * Gets the first day in month.
	 * 
	 * @param date the date
	 * @return the first day in month
	 */
	public static Date getFirstDayInMonth(Date date) {
		String dateString = DateUtil.dateToSimpleString(date);
		dateString = dateString.substring(0, 8);
		dateString = dateString.concat("01");
		date = DateUtil.stringSimpleToDate(dateString);
		return date;
	}

	/**
	 * Gets the first day in quarter.
	 * 
	 * @param date the date
	 * @return the first day in quarter
	 */
	public static Date getFirstDayInQuarter(Date date) {
		String dateString = DateUtil.dateToSimpleString(date);
		String t = dateString.substring(5, 7);
		dateString = dateString.substring(0, 5);
		int i = new Integer(t).intValue();
		int q = getQuarterByMonth(i);
		int month = (q - 1) * 3 + 1;
		if (month < 10)
			dateString = dateString.concat("0" + month);
		else
			dateString = dateString.concat("" + month);
		dateString = dateString.concat("-01");
		date = DateUtil.stringSimpleToDate(dateString);
		return date;
	}

	/**
	 * Gets the first day in year.
	 * 
	 * @param date the date
	 * @return the first day in year
	 */
	public static Date getFirstDayInYear(Date date) {
		String dateString = DateUtil.dateToSimpleString(date);
		String t = dateString.substring(0, 4);
		dateString = t.concat("-01-01");
		date = DateUtil.stringSimpleToDate(dateString);
		return date;
	}

	/**
	 * Gets the last day in year.
	 * 
	 * @param date the date
	 * @return the last day in year
	 */
	public static Date getLastDayInYear(Date date) {
		Date tmp = DateUtil.getFirstDayInYear(date);
		tmp = DateUtil.dateAddYears(tmp, 1);
		tmp = DateUtil.dateAddDays(tmp, -1);
		return tmp;
	}

	/**
	 * Gets the last day in month.
	 * 
	 * @param firstDayInMonth the first day in month
	 * @return the last day in month
	 */
	public static Date getLastDayInMonth(Date firstDayInMonth) {
		Date tmp = DateUtil.dateAddMonths(firstDayInMonth, 1);
		tmp = DateUtil.getFirstDayInMonth(tmp);
		tmp = DateUtil.dateAddDays(tmp, -1);
		return tmp;
	}

	/**
	 * Gets the last day in quarter.
	 * 
	 * @param value the value
	 * @return the last day in quarter
	 */
	public static Date getLastDayInQuarter(Date value) {
		Date tmp = DateUtil.getFirstDayInQuarter(value);
		tmp = DateUtil.dateAddMonths(tmp, 3);
		tmp = DateUtil.dateAddDays(tmp, -1);
		return tmp;
	}

	/**
	 * 返回当前日期的格式化字符串.
	 * 
	 * @param tag 0:yyyy-MM-dd HH:mm:ss;1:yyyyMMddHHmmss;2:yyyy-MM-dd;3:yyyyMM
	 * @return 格式化成字符串的日期
	 */
	public static String formatDate(int tag) {
		Date date = Calendar.getInstance().getTime();
		String dateStr = null;
		switch (tag) {
		case 0:
			dateStr = new SimpleDateFormat(LONG_PATTERN, Locale.CHINA).format(date);
			break;
		case 1:
			dateStr = new SimpleDateFormat(SHORT_PATTERN, Locale.CHINA).format(date);
			break;
		case 2:
			dateStr = new SimpleDateFormat(PATTERN_THREE, Locale.CHINA).format(date);
			break;
		case 3:
			dateStr = new SimpleDateFormat(PATTERN_FOUR, Locale.CHINA).format(date);
			break;
		default:
			dateStr = new SimpleDateFormat(LONG_PATTERN, Locale.CHINA).format(date);
			break;
		}
		return dateStr;
	}

	/**
	 * 返回一天的起始 0am
	 * @param date
	 * @return Date
	 */
	public static Date getDateBegin(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 返回一天的起始 0am
	 * @param date
	 * @return long
	 */
	public static long getDateBegin(long millitime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millitime);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		String temp = DateUtil.dateToString(new Date(), "yyyyMM");
		System.out.println(temp);
		Date now = new Date();
		System.out.println(getDateBegin(dateToSimpleString(dateAddMonths(now, -1))));
	}
}
