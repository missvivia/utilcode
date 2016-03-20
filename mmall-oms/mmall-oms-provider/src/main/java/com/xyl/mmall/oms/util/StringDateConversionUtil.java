/**
 * 字符串日期转换工具
 */
package com.xyl.mmall.oms.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hzzengdan
 * @date 2014-09-09
 */
public class StringDateConversionUtil {
	
	/**
	 * Long字符串转换成yyyy-MM-dd HH:mm:ss格式输出
	 */
	public String longToString(String longString){
		Date date = new Date(Long.parseLong(longString));
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
	
	/**
	 * 字符串转换成时间
	 */
	public Date stringToDate(String time) throws Exception{
		Date date = null;
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = formatter.parse(time);
		return date;
	}
	
	/**
	 * 将20141025102503转换成2014-10-25 10:25:03
	 */
	public String convertedToLoose(String timeString) throws Exception{
		SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-HH-dd HH:mm:ss");  
	    SimpleDateFormat formatter2=new SimpleDateFormat("yyyyHHddHHmmss");
	    String time = formatter1.format(formatter2.parse(timeString));
	    return time;
	}
	
	/**
	 * 获取系统当前时间,转化为字符串格式
	 */
	public static String getStringDate(){
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 获取系统当前时间，转化为long时间
	 */
	public static long getLongDate(){
		Date currentTime =new Date();
		long lSystemTime = currentTime.getTime();
		return lSystemTime;
	}
}
