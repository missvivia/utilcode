package com.xyl.mmall.task.bean;

/**
 * 数据过滤接口
 * @author hzzhaozhenzuo
 *
 */
public interface DataFilter<T> {
	
	/**
	 * 对应数据是否能够访问
	 * @param targetData
	 * @return
	 */
	public boolean canAccess(T targetData);

}
