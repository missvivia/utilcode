/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * RegexUtils.java created by yydx811 at 2015年5月6日 下午8:50:52
 * 正则表达式工具类
 *
 * @author yydx811
 */
public class RegexUtils {
	
	/** n位数字. */
	private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+$");
	
	/** 密码数字和字母组合. */
	private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,20}$");
	
	/** 字母或数字组合. */
	private static final Pattern LETTER_NUMBER_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
	
	/** IP地址 */
	private static final Pattern IP_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}$");

	public static boolean isAllNumber(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		Matcher m = NUMBER_PATTERN.matcher(str);
		return m.find();
	}
	
	public static boolean isValidPassword(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		Matcher m = PASSWORD_PATTERN.matcher(str);
		return m.find();
	}
	
	public static boolean isLetterOrNumber(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		Matcher m = LETTER_NUMBER_PATTERN.matcher(str);
		return m.find();
	}
	
	public static boolean isValidIP(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		Matcher m = IP_PATTERN.matcher(str);
		return m.find();
	}
	
	
}
