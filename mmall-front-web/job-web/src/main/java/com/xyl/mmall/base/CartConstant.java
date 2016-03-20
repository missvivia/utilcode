package com.xyl.mmall.base;

import com.netease.backend.nkv.client.NkvClient.NkvOption;

/**
 * @author Yang,Nan
 *
 */
public final class CartConstant {

	public final static int CART_ITEM_MAX_COUNT = 2;

	public final static int MAX_SKU_COUNT = 10;
	/**
	 * name space
	 */

	/**
	 * nkv option
	 */
	public final static NkvOption NKV_OPTION = new NkvOption(5000);

	public final static NkvOption NKV_OPTION_1DAY_EXPIRE = new NkvOption(5000, (short)0, 24 * 60 * 60);

	/**
	 * 有效的nkv key
	 */
	public final static String NKV_CART_VALID = "NKV_CART_VALID";

	/**
	 * 超时的key
	 */
	public final static String NKV_CART_OVERTIME = "NKV_CART_OVERTIME";

	/**
	 * 无效的key
	 */
	public final static String NKV_CART_INVALID = "NKV_CART_INVALID";

	/**
	 * 被删除的有效的nkv key
	 */
	public final static String NKV_CART_DELETE = "NKV_CART_DELETE";
	
	/**
	 * 被删除的超时的nkv key
	 */
	public final static String NKV_CART_DELETE_OVERTIME = "NKV_CART_DELETE_OVERTIME";

	/**
	 * 有效的nkv key
	 */
	public final static String NKV_CART_INVENTORY = "NKV_CART_INVENTORY";

	/**
	 * 购物车更新时间 key
	 */
	public final static String NKV_CART_UPDATETIME = "NKV_CART_UPDATETIME";

	/**
	 * 当某个sku无库存时，作为存储用户订阅了关注这个sku功能的key
	 */
	public final static String NKV_CART_REMIND_WHEN_STORAGE = "NKV_CART_REMIND";

	public final static boolean DISTRIBUTE_FOR_CART_CLEAN = false;
}

