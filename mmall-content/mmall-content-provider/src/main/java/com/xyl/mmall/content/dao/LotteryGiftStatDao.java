/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.content.meta.LotteryGiftStat;

/**
 * @author hzzhanghui
 * 
 */
public interface LotteryGiftStatDao extends AbstractDao<LotteryGiftStat> {

	/**
	 * 查询所有记录
	 * 
	 * @return
	 */
	List<LotteryGiftStat> getGiftStatList();

	/**
	 * 查询某类奖品在某天的分配情况。
	 * 
	 * @param type
	 * @param date
	 * @return
	 */
	LotteryGiftStat getGiftStat(int type, long date);
	
	/**
	 * 更新某类奖品在某天的数量。
	 * 
	 * @param type
	 * @param date
	 * @param cnt
	 * @return
	 */
	boolean updateGiftStat(int type, long date, int cnt);
}
