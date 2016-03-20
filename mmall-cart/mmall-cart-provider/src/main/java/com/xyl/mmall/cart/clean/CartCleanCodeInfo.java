package com.xyl.mmall.cart.clean;

public class CartCleanCodeInfo {
	public static final String SPLIT="-";
	
	/**
	 * 【清除区域】
	 * 用于清除超时购物车时，存储在n个区域桶中每个桶的key前缀
	 */
	public static final String NKV_CART_CLEAN_AREA_TIME_PREFIX="NKV_CART_CLEAN_AREA_TIME_PREFIX";

	/**
	 * 【清除区域处理指针】，最外层key
	 */
	public static final String NKV_CART_CLEAN_POINT_OUT="NKV_CART_CLEAN_POINT_OUT";
	
	/**
	 * 【清除区域处理指针】，代表当前指针值的field名称
	 */
	public static final String NKV_CART_CLEAN_POINT_COUNT="NKV_CART_CLEAN_POINT_COUNT";
	
	/**
	 * 【清除区域处理指针】，代表当前清除区域处理状态
	 * -1:未处理
	 * 0:处理中
	 * 1:处理成功
	 * 2:处理失败
	 */
	public static final String NKV_CART_CLEAN_POINT_STATUS="NKV_CART_CLEAN_POINT_STATUS";
	
	public static final String UTF8_PREFIX="UTF-8";
	
	//清除区域未被处理
	public static final int AREA_CLEAN_NO_PROCESSING=-1;
	
	//清除区域处理中
	public static final int AREA_CLEAN_PROCESSING=0;
	
	//清除区域成功
	public static final int AREA_CLEAN_SUCCESS=1;
	
	//清除区域失败
	public static final int AREA_CLEAN_FAIL=2;
	
	public static final int INIT_DEFAULT_POINT=-1;

}
