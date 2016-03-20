/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.constant;

import com.netease.backend.nkv.client.NkvClient.NkvOption;

/**
 * NkvConstant.java created by yydx811 at 2015年11月16日 下午11:25:39
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class NkvConstant {
	
	/* 通用nkv常量 */
	
	/** nkv client option 超时3秒. */
	public final static NkvOption NKV_OPTION = new NkvOption(3000l);

	/** nkv client option 超时3秒. */
	public final static NkvOption NKV_OPTION_0 = new NkvOption(3000l, (short) 0);
	
	/** nkv client option 超时3秒，过期24小时. */
	public final static NkvOption NKV_OPTION_1DAY_EXPIRE = new NkvOption(3000l, (short)0, 24 * 60 * 60);

	/* 购物车nkv常量 */
	public final static int CART_ITEM_MAX_COUNT = 2;

	public final static int MAX_SKU_COUNT = 50;

	/** 有效的购物车nkv key. */
	public final static String NKV_CART_VALID = "NKV_CART_VALID";

	/** 超时的购物车nkv key. */
	public final static String NKV_CART_OVERTIME = "NKV_CART_OVERTIME";

	/** 无效的购物车nkv key. */
	public final static String NKV_CART_INVALID = "NKV_CART_INVALID";

	/** 被删除的有效的购物车nkv key. */
	public final static String NKV_CART_DELETE = "NKV_CART_DELETE";
	
	/** 被删除的超时的购物车nkv key. */
	public final static String NKV_CART_DELETE_OVERTIME = "NKV_CART_DELETE_OVERTIME";

	/** 有效的购物车nkv key. */
	public final static String NKV_CART_INVENTORY = "NKV_CART_INVENTORY";

	/** 购物车更新时间 key. */
	public final static String NKV_CART_UPDATETIME = "NKV_CART_UPDATETIME";

	/**
	 * 当某个sku无库存时，作为存储用户订阅了关注这个sku功能的key
	 */
	public final static String NKV_CART_REMIND_WHEN_STORAGE = "NKV_CART_REMIND";

	public final static boolean DISTRIBUTE_FOR_CART_CLEAN = false;
	
	/* 商品限购nkv常量 */
	/** 商品限购nkv key. */
	public final static String NKV_PRODUCTSKU_LIMIT = "NKV_PRODUCTSKU_LIMIT";
	
	/** 商家平台-商品列表 商家搜索key. */
	public final static String NKV_BUSINESSID_SEARCHPARAM = "NKV_BUSINESSID_SEARCHPARAM";
	
	/**购物车商品加入时间key*/
	public final static String NKV_CART_ITEM_CREATETIME="NKV_CART_ITEM_CREATETIME";
	
	/**购物车选中商品*/
	public final static String NKV_CART_ITEM_SELECTIONS="NKV_CART_ITEM_SELECTIONS";
	
	/**
	 * 组合商品限购key
	 * @param skuId
	 * @return
	 */
	public static String genProductLimitNkvKey(long skuId) {
		return NKV_PRODUCTSKU_LIMIT + "_" + skuId;
	}
}
