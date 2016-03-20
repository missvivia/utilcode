/*
 * @(#) 2014-10-13
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.activity.Condition;
import com.xyl.mmall.promotion.activity.Effect;
import com.xyl.mmall.promotion.constants.CartTipConstants;
import com.xyl.mmall.promotion.enums.ConditionType;
import com.xyl.mmall.promotion.enums.EffectType;

/**
 * CartTipUtils.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-13
 * @since      1.0
 */
public class CartTipUtils {
	
	private static Logger log = Logger.getLogger(CartTipUtils.class);
	
	private static long lastModified = 0;
	
	private static Map<Integer, String> tipMaps = new HashMap<>();
	
	public static void init() {
		try {
			File f = new File(CartTipUtils.class.getClassLoader().getResource("").toURI());
			File resourceFile = new File(f.getAbsolutePath() + File.separator + "config/cart.tips.properties");
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
			tipMaps.put(CartTipConstants.ACTIVITY_PRICE_MINUS_TIP, properties.getProperty("activity.price.minus.tip"));
			tipMaps.put(CartTipConstants.ACTIVITY_PRICE_DISCOUNT_TIP, properties.getProperty("activity.price.discount.tip"));
			tipMaps.put(CartTipConstants.ACTIVITY_UNIT_MINUS_TIP, properties.getProperty("activity.unit.minus.tip"));
			tipMaps.put(CartTipConstants.ACTIVITY_UNIT_DISCOUNT_TIP, properties.getProperty("activity.unit.discount.tip"));
			tipMaps.put(CartTipConstants.ACTIVITY_STEP_PRICE_TIP, properties.getProperty("activity.step.price.tip"));
			tipMaps.put(CartTipConstants.ACTIVITY_STEP_UNIT_TIP, properties.getProperty("activity.step.unit.tip"));
			tipMaps.put(CartTipConstants.ACTIVITY_ORIGIN_PRICE_TIP, properties.getProperty("activity.origin.price.tip"));
			tipMaps.put(CartTipConstants.ACTIVITY_ORIGIN_UNIT_TIP, properties.getProperty("activity.origin.unit.tip"));
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	public static String getTip(int key, Object... values) {
		init();
		String value = tipMaps.get(key);
		
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		if (values == null) {
			return value;
		}
		
		int index = 0;
		for (Object obj : values) {
			if (obj == null) {
				continue;
			}
			value = value.replace("{" + index + "}", String.valueOf(obj));
			index++;
			if (value.indexOf("{" + index + "}") <= 0) {
				break;
			}
		}
		
		return value;
	}
	
	public static String buildTipFromCondition(Activation activation, BigDecimal price, int count, boolean step, String seperate) {
		Condition condition = activation.getCondition();
		
		List<Effect> effects = activation.getResult();
		String extraTip = "";
		boolean addSeparate = false;
		
		if (StringUtils.isBlank(seperate)) {
			seperate = ";";
		}
		
		for (Effect effect : effects) {
			if (effect.getType() == EffectType.ACTIVATION_EXPRESS_FREE.getIntValue()) {
				if (addSeparate) {
					extraTip = extraTip.concat(seperate);
				}
				extraTip = extraTip.concat(EffectType.ACTIVATION_EXPRESS_FREE.getDesc());
				addSeparate = true;
			} else if (effect.getType() == EffectType.ACTIVATION_COUPON_PRESENT.getIntValue()) {
				if (addSeparate) {
					extraTip = extraTip.concat(seperate);
				}
				extraTip = extraTip.concat(EffectType.ACTIVATION_COUPON_PRESENT.getDesc());
				addSeparate = true;
			} else if (effect.getType() == EffectType.ACTIVATION_RED_PACKETS_PRESENT.getIntValue()) {
				if (addSeparate) {
					extraTip = extraTip.concat(seperate);
				}
				extraTip = extraTip.concat(EffectType.ACTIVATION_RED_PACKETS_PRESENT.getDesc());
				addSeparate = true;
			} else if (effect.getType() == EffectType.ACTIVATION_DISCOUNT.getIntValue()) {
				if (!StringUtils.isBlank(effect.getValue()) && StringUtils.isNumeric(effect.getValue())) {
					if (addSeparate) {
						extraTip = extraTip.concat(seperate);
					}
					BigDecimal discount = new BigDecimal(effect.getValue()).divide(new BigDecimal(10)).setScale(1,
							BigDecimal.ROUND_HALF_UP);
					extraTip = extraTip.concat("享").concat(discount.toPlainString())
							.concat(EffectType.ACTIVATION_DISCOUNT.getDesc());
					addSeparate = true;
				}
			} else if (effect.getType() == EffectType.ACTIVATION_MINUS.getIntValue()) {
				if (addSeparate) {
					extraTip = extraTip.concat(seperate);
				}
				extraTip = extraTip.concat(EffectType.ACTIVATION_MINUS.getDesc()).concat(effect.getValue()).concat("元");
				addSeparate = true;
			}
		}

		String tips = null;
		if (condition.getType() == ConditionType.BASIC_PRICE_SATISFY.getIntValue()) {
			if (price.compareTo(BigDecimal.ZERO) <= 0) {
				return null;
			}
			if (step) {
				tips = CartTipUtils.getTip(CartTipConstants.ACTIVITY_STEP_PRICE_TIP, price, extraTip);
			} else {
				tips = CartTipUtils.getTip(CartTipConstants.ACTIVITY_ORIGIN_PRICE_TIP, price, extraTip);
			}
		} else if (condition.getType() == ConditionType.BASIC_COUNT_SATISFY.getIntValue()) {
			if (count <= 0) {
				return null;
			}
			if (step) {
				tips = CartTipUtils.getTip(CartTipConstants.ACTIVITY_STEP_UNIT_TIP, count, extraTip);
			} else {
				tips = CartTipUtils.getTip(CartTipConstants.ACTIVITY_ORIGIN_UNIT_TIP, count, extraTip);
			}
		}
		
		return tips;
	}
	
	/**
	 * 获取条件值的第一个
	 * 
	 * @param value
	 * @return
	 */
	public static String getFirstValue(String value) {
		if (StringUtils.isBlank(value)) {
			return "0";
		}

		String[] values = value.split("-");

		return StringUtils.isBlank(values[0]) ? "0" : values[0];
	}
	
	public static void main(String[] args) {
		System.out.println(getTip(CartTipConstants.ACTIVITY_UNIT_MINUS_TIP, 100, 10, 100, 25));
	}
}
