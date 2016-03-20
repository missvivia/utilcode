package com.xyl.mmall.task.bean;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

public abstract class BaseDataFilter<T> implements DataFilter<T>,InitializingBean{
	
	//白名单
	protected final Set<T> dataSet=new HashSet<T>();
	
	public abstract void initData();
	
	public BaseDataFilter<T> addDataToPassFilter(T data){
		dataSet.add(data);
		return this;
	}
	
	public void afterPropertiesSet() throws Exception{
		initData();
	}
	
	public boolean canAccess(T targetData){
		return dataSet.contains(targetData);
	}
}
