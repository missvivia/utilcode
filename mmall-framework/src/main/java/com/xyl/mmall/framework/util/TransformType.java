/**
 * 
 */
package com.xyl.mmall.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author jmy
 *
 */
public class TransformType {
	
	public static Long[] transform(String[] strArray, boolean keepNull) {
		if (strArray == null) return null;
		List<Long> result = new ArrayList<>(strArray.length);
		for (int i = 0; i < strArray.length; i ++) {
			String s = strArray[i].trim();
			if (s == null || s.length() == 0) {
				if (keepNull) result.add(null);
			} else {
				result.add(Long.valueOf(s));
			}
		}
		return result.toArray(new Long[0]);
	}
	
	public static void main(String[] args) {
		String str = " 1 , 2 ,3     , 4, 5,  ,,";
		String[] arr1 = str.trim().split("\\s*,\\s*");
		System.out.println(Arrays.toString(transform(arr1, true)));
		System.out.println(Arrays.toString(transform(arr1, false)));
		String[] arr2 = str.split(",");
		System.out.println(Arrays.toString(transform(arr2, true)));
		System.out.println(Arrays.toString(transform(arr2, false)));
		String[] arr3 = StringUtils.splitByWholeSeparatorPreserveAllTokens(str, ",");
		System.out.println(Arrays.toString(transform(arr3, true)));
		System.out.println(Arrays.toString(transform(arr3, false)));

	}
}
