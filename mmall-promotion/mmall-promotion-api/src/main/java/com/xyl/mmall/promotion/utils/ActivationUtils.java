/*
 * @(#) 2015-2-4
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.utils;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.promotion.activity.Activation;

/**
 * ActivationUtils.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2015-2-4
 * @since      1.0
 */
public class ActivationUtils {
	
	public static List<Activation> containActivations(String items) {
		if (StringUtils.isEmpty(items)) {
			return null;
		}
		List<Activation> activations = JsonUtils.parseArray(items, Activation.class);
		//按照condition排序
		Collections.sort(activations);
		return activations;
	}
	
}
