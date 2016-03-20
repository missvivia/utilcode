package com.xyl.mmall.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

public class SignUtil {
	public static String genSig(String secretkey, String timestamp, String nonce) {
		List<String> elements = new ArrayList<String>();
		elements.add(timestamp);
		elements.add(nonce);
		elements.add(secretkey);
		Collections.sort(elements);
		StringBuilder seed = new StringBuilder();
		for (String link : elements) {
			seed.append(link);
		}
		String signature = DigestUtils.sha1Hex(seed.toString());
		return signature;
	}
}