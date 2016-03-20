package com.xyl.mmall.framework.interfaces;

import java.util.List;

/**
 * 支持TCC服务的Dao需要实现的接口<br>
 * 
 * @author dingmingliang
 * 
 * @param <T>
 */
public interface TCCDaoInterface<T> {

	/**
	 * 根据tranId,获得相关的数据集合
	 * 
	 * @param tranId
	 * @return
	 */
	public List<T> getListByTranId(long tranId);

	/**
	 * 根据tranId,删除相关的数据集合
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean deleteByTranId(long tranId);
}
