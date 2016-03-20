package com.xyl.mmall.cart.delete;

import com.xyl.mmall.constant.NkvConstant;

public class CartDeleteKeyUtil {

	private static final String CART_DEL_CACHE_PREFIX = "CART_DEL_CACHE_";
	
	public static String getOutKeyByParam(String type,long areaId){
		if(type==null){
			//不分类型
			return CART_DEL_CACHE_PREFIX+areaId;
		}
		
		if(type.equals(NkvConstant.NKV_CART_OVERTIME)){
			return getOverTimeOutKey(areaId);
		}else if(type.equals(NkvConstant.NKV_CART_DELETE_OVERTIME)){
			return getDeleteOfOverTimeOutKey(areaId);
		}else if(type.equals(NkvConstant.NKV_CART_DELETE)){
			return getDeleteOutKey(areaId);
		}else if(type.equals(NkvConstant.NKV_CART_INVALID)){
			return getInvalidOutKey(areaId);
		}
		return null;
	}
	
	public static String getDeleteFieldKey(long userId){
		return Long.toString(userId);
	}

	public static String getOverTimeOutKey(long areaId) {
		return CART_DEL_CACHE_PREFIX + NkvConstant.NKV_CART_OVERTIME;
	}

	public static String getDeleteOfOverTimeOutKey(long areaId) {
		return CART_DEL_CACHE_PREFIX + NkvConstant.NKV_CART_DELETE_OVERTIME + "_" + areaId;
	}

	public static String getDeleteOutKey(long areaId) {
		return CART_DEL_CACHE_PREFIX + NkvConstant.NKV_CART_DELETE + "_" + areaId;
	}

	public static String getInvalidOutKey(long areaId) {
		return CART_DEL_CACHE_PREFIX + NkvConstant.NKV_CART_INVALID + "_" + areaId;
	}

}
