/*
 * @(#) 2014-10-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xyl.mmall.promotion.constants.ParameterConstants;

/**
 * ApplicationUtils.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-22
 * @since      1.0
 */
public class ParametersUtils {
	private static Logger log = Logger.getLogger(ParametersUtils.class);
	
	private static long lastModified = 0;
	
	private static Map<Integer, String> parametersMaps = new HashMap<>();
	
	public static void init() {
		try {
			File f = new File(ParametersUtils.class.getClassLoader().getResource("").toURI());
			File resourceFile = new File(f.getAbsolutePath() + File.separator + "config/parameters.properties");
			long modified = resourceFile.lastModified();
			//检查文件最后修改时间，如果最后修改时间发生改变，重新加载文件
			if (modified > lastModified) {
				load(resourceFile);
				lastModified = modified;
			}
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private static void load(File resourceFile) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(resourceFile.getAbsolutePath());
			Properties properties = new Properties();
			properties.load(is);
			parametersMaps.put(ParameterConstants.REFUND_COUPON_CODE, properties.getProperty("refund.coupon.code"));
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	public static String getValue(int key) {
		init();
		
		return parametersMaps.get(key);
	}
}
