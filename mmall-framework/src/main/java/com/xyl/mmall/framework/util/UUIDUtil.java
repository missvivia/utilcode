package com.xyl.mmall.framework.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * ================================================================== 
 * UUIDUtil.java created by yydx811 at 2014年1月20日 下午12:47:54 uuid工具类
 * 
 * @author yydx811
 * @version 1.0
 * ==================================================================
 */

public class UUIDUtil {
	private static final Set<String> finalSet = new HashSet<String>();

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
			"u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public static String generateShortUUID() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();

	}

	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int id = 0; id < 100; id++) {
			new Thread(new Runnable() {
				public void run() {
					Set<String> set = new HashSet<String>();
					for (int i = 0; i < 100000; i++) {
						set.add(generateShortUUID());
					}
					// System.out.println(set.size());
					setSet(set);
				}
			}).start();
		}
	}

	public static synchronized void setSet(Set<String> set) {
		finalSet.addAll(set);
		System.out.println(finalSet.size());
	}

}
