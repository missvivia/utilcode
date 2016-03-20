package com.xyl.mmall.mobile.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

public class MobileSignUtilOfCommon {
	public static String genSig(String secretkey, String timestamp) {
		List<String> elements = new ArrayList<String>();
		elements.add(timestamp);
		elements.add(secretkey);
		Collections.sort(elements);
		StringBuilder seed = new StringBuilder();
		for (String link : elements) {
			seed.append(link);
		}
		String signature = DigestUtils.sha256Hex(seed.toString());
		return signature;
	}
	
	public static boolean checkSign(String secretkey, String timestamp,String signInput) {
		List<String> elements = new ArrayList<String>();
		elements.add(timestamp);
		elements.add(secretkey);
		Collections.sort(elements);
		StringBuilder seed = new StringBuilder();
		for (String link : elements) {
			seed.append(link);
		}
		String signature = DigestUtils.sha256Hex(seed.toString());
		return signature.equalsIgnoreCase(signInput);
	}
}