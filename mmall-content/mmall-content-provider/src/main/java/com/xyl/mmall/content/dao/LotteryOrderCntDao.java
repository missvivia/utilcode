/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.content.meta.LotteryOrderCnt;

/**
 * @author hzzhanghui
 * 
 */
public interface LotteryOrderCntDao extends AbstractDao<LotteryOrderCnt> {

	/**
	 * 查询所有记录
	 * 
	 * @return
	 */
	List<LotteryOrderCnt> getGiftOrderCntList();

}
