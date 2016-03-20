package com.xyl.mmall.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * ================================================================== 
 * PhoneNumberUtil.java created by yydx811 at 2014年2月11日 上午11:02:03 电话号码常规检测工具
 * 
 * @author yydx811
 * @version 1.0
 * @mail yydx811@163.com 
 * ==================================================================
 */

public class PhoneNumberUtil {
	/**
	 * 固定电话
	 */
	static final Pattern fixedPhonePattern = Pattern.compile("^(0[0-9]{2,3}(/////-)?)?([2-9][0-9]{7,8})+$");
	/**
	 * 手机
	 */
	static final Pattern mobilePhonePattern = Pattern.compile("^(13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\\d{8}$");
	/**
	 * 特殊号码
	 */
	static final String[] usefulNumber = { "10010", "10000", "10086" };

	/**
	 * 座机号码匹配
	 * 
	 * @param phone
	 * @return boolean
	 */
	public static boolean isFixedPhone(String phone) {
		if (phone == null) {
			return false;
		}
		Matcher m = fixedPhonePattern.matcher(phone);
		return m.find();
	}

	/**
	 * 手机号码匹配
	 * 
	 * @param phone
	 * @return boolean
	 */
	public static boolean isMobilePhone(String phone) {
		if (phone == null) {
			return false;
		}
		Matcher m = mobilePhonePattern.matcher(phone);
		return m.find();
	}

	/**
	 * 特殊号码匹配
	 * 
	 * @param phone
	 * @return boolean
	 */
	public static boolean isUsefulNumber(String phone) {
		boolean b = false;
		if (phone == null) {
			return b;
		}
		for (int i = 0; i < usefulNumber.length; i++) {
			if (StringUtils.equals(phone, usefulNumber[i])) {
				b = true;
				break;
			}
		}
		return b;
	}

	/**
	 * 全号码匹配
	 * 
	 * @param str_number
	 *            电话号码串以,分隔
	 * @return boolean
	 */
	public static boolean isNumberOfAll(String str_number) {
		if (str_number == null) {
			return false;
		}
		boolean b = true;
		String[] numbers = str_number.split(",");
		for (String number : numbers) {
			if (!isFixedPhone(number) && !isMobilePhone(number) && !isUsefulNumber(number)) {
				return false;
			}
		}
		return b;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(isMobilePhone(null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(isMobilePhone(""));
	}

}
