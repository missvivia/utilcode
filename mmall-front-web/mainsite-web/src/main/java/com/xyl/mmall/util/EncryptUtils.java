/*
 * @(#)EncryptUtils.java 2013-10-18
 *
 * Copyright 2013 mircobuy, Inc. All rights reserved.
 */
package com.xyl.mmall.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * EncryptUtils.java
 *
 * @author     <A HREF="mailto:ruan635@163.com">Roy</A>
 * @version    1.0 2014-2-15
 * @since      1.0
 */
public class EncryptUtils {
	
	private static Logger log = Logger.getLogger(EncryptUtils.class);
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 对字符串进行加密 指出md5和sha
	 * @param value
	 * @param algorithm
	 * @return
	 */
	public static String encrypt(String value, String algorithm) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			digest.update(value.getBytes());
			
			byte[] bytes = digest.digest();
			
			int length = bytes.length;
			
			StringBuffer sb = new StringBuffer();
			
			for (int i = 0; i < length; i++) {
				sb.append(HEX_DIGITS[(bytes[i] >>> 4) & 0x0f]);
				sb.append(HEX_DIGITS[bytes[i] & 0xf]);
			}
			
			return sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		}
		
		return null;
	}
}
