/**
 * 
 */
package com.xyl.mmall.oms.warehouse.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换工具类：long -> Date;String ->
 * Date;long->Date->String;String->Date->long</br> 默认时间格式为：YYYY-MM-dd hh:mm:ss
 * 
 * @author hzzengchengyuan
 * 
 */
public class DateUtils {
	public static final DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

	public static final DateFormat format3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static final DateFormat format4 = new SimpleDateFormat("yyyy/MM/dd");

	public static final DateFormat format5 = new SimpleDateFormat("yyyyMMddHHmmss");

	public static final DateFormat format6 = new SimpleDateFormat("yyyyMMdd");

	public static final DateFormat[] allFormat = new DateFormat[] { format1, format2, format3, format4, format5,
			format6 };

	private static final long defaultZeroTime = new Date(0).getTime();

	public static Date parseToDate(long time) {
		return time <= defaultZeroTime ? null : new Date(time);
	}

	/**
	 * 尝试按以下顺序对字符串进行格式化："yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","yyyy/MM/dd HH:mm:ss"
	 * ,"yyyy/MM/dd","yyyyMMddHHmmss","yyyyMMdd"<br>
	 * 所以类型尝试完后还是无法转换则返回null
	 * 
	 * @param time
	 * @return
	 */
	public static Date parseToDate(String time) {
		Date date = null;
		for (DateFormat format : allFormat) {
			try {
				date = format.parse(time);
			} catch (ParseException e) {
			}
			if (date != null) {
				return date;
			}
		}
		return null;
	}

	public static long parseToLongtime(String time) {
		Date date = parseToDate(time);
		return date == null ? 0 : date.getTime();
	}

	public static String parseToStringtime(long time) {
		Date date = parseToDate(time);
		return date == null ? null : format(date);
	}

	/**
	 * 按默认格式"yyyy-MM-dd HH:mm:ss"格式化
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format1.format(date);
	}

}
