package com.xyl.mmall.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobMaapingStore {
	
	@SuppressWarnings("rawtypes")
	private static final Map<String, Class> jobMappingStore=new ConcurrentHashMap<String, Class>(); 
	
	@SuppressWarnings("rawtypes")
	public static Class putMapping(String key,Class claz){
		return jobMappingStore.put(key, claz);
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getMappingClassByKey(String key){
		return jobMappingStore.get(key);
	}
}
