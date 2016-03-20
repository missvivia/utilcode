/*
 * @(#)PromotionConstants.java 2014-4-18
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.constants;

/**
 * ActivationConstants.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-4-18
 * @since      1.0
 */
public interface ActivationConstants {
	/**
	 * 订单促销
	 */
	public static final int ACTIVATION_TYPE_ORDER = 1;
	
	/**
	 * 优惠券
	 */
	public static final int ACTIVATION_TYPE_COUPON = 2;
	
	/**
	 * 红包
	 */
	public static final int ACTIVATION_TYPE_REDPACKETS = 3;
	
	/**
	 * 可用
	 */
	public static final int STATE_CAN_USE = 0;
	
	/**
	 * 不存在
	 */
	public static final int STATE_NOT_EXISTS = 1;
	
	/**
	 * 已过期
	 */
	public static final int STATE_EXPIRED = 2;
	
	/**
	 * 已被使用
	 */
	public static final int STATE_HAS_BEAN_USED = 3;
	
	/**
	 * 不匹配
	 */
	public static final int STATE_NOT_MATCH = 4;
	
	/**
	 * 已失效（有效期内但已被取消）
	 */
	public static final int STATE_INACTIVE = 5;
	
	/**
	 * 使用次数为0
	 */
	public static final int STATE_LEFT_ZERO_COUNT = 6;
	
	/**
	 * 已被激活绑定
	 */
	public static final int STATE_HAS_BIND_OTHERS = 7;
	
	/**
	 * 未生效
	 */
	public static final int STATE_NOT_TAKE_EFFECT = 8;
	
	/**
	 * 全部通配符
	 */
	public static final String OVERALL = "*";
}
