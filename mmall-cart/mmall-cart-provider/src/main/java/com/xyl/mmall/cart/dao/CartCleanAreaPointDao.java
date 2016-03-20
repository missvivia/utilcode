package com.xyl.mmall.cart.dao;

import com.xyl.mmall.cart.clean.meta.CartCleanAreaPoint;

/**
 * 区域清除指针dao
 * 将指针相关信息放入nkv cache中
 * @author hzzhaozhenzuo
 *
 */
public interface CartCleanAreaPointDao{
	
	/**
	 * 根据分布式处理key，获取对应那组处理区域集
	 * @param distributeKey
	 * @return
	 */
	public CartCleanAreaPoint queryPointByDistributeKey(String distributeKey);
	
	/**
	 * 更新对应的point记录为:pointToUpdate
	 * @param distributeKey
	 * @param pointToUpdate 当前要被处理的区域位置
	 * @param processStatusToUpdate 希望更新的状态
	 * @param oldPoint 上一个已经处理过的区域位置
	 * @return
	 */
	public boolean updatePointAndStatus(String distributeKey,int pointToUpdate,int processStatusToUpdate,int oldPoint);
}
