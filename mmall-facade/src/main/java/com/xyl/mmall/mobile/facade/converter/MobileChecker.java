package com.xyl.mmall.mobile.facade.converter;

import java.util.List;

import com.xyl.mmall.framework.exception.ParamNullException;

public class MobileChecker {
	
	public static void checkNull(String name ,Object obj) throws ParamNullException{
		if(obj == null)
			throw new ParamNullException(name + " CAN NOT BE NULL!");
	}
	
	public static void checkZero(String name ,Number obj) throws ParamNullException{
		if(obj == null || obj.longValue() == 0l)
			throw new ParamNullException(name + " CAN NOT BE CAST TO NUMBER , IST ZERO!");
	}
	
	public static void checkSkuCount(List<Long> a ,List<Integer> b) throws ParamNullException{
		if(a == null || b == null)
			throw new ParamNullException(" SKU ID OR COUNT CAN NOT NULL!");
		if(a.size() == 0 || b.size() == 0)
			throw new ParamNullException(" SKU ID OR COUNT CAN NOT EMPTY!");
		if(a.size() !=  b.size())
			throw new ParamNullException(" SKU ID SIZE NOT MATCH OPERATION SIZE!");
	}
	
	public static boolean checkHasNext(List<?> list ,int size){
		if(list == null)
			return false;
		if(list.size() < size)
			return false;
		return true;
	}
}
