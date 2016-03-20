/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.enums.GiftType;
import com.xyl.mmall.content.meta.LotteryGift;

/**
 * @author hzlihui2014
 *
 */
public interface LotteryGiftDao extends AbstractDao<LotteryGift> {
	
	/**
	 * 
	 * @param gift
	 * @return
	 */
	boolean saveGiftHit(LotteryGift gift);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	List<LotteryGift> getGiftHitByUserId(long userId);

	/**
	 * 
	 * @param userId
	 * @param hitTime
	 * @return
	 */
	List<LotteryGift> getGiftHitByUserIdAndTime(long userId, long hitTime);

	/**
	 * 
	 * @param userId
	 * @param hitTimeStart
	 * @param hitTimeEnd
	 * @return
	 */
	List<LotteryGift> getGiftHitByUserIdAndTimeRange(long userId, long hitTimeStart, long hitTimeEnd);

	/**
	 * 
	 * @return
	 */
	List<LotteryGift> getGiftHitList();

	/**
	 * 
	 * @param hitTime
	 * @return
	 */
	List<LotteryGift> getGiftHitListByTime(long hitTime);

	/**
	 * 
	 * @param hitTimeStart
	 * @param hitTimeEnd
	 * @return
	 */
	List<LotteryGift> getGiftHitListByTimeRange(long hitTimeStart, long hitTimeEnd);


	/**
	 * 根据奖品类型列表获取中奖名单。
	 * 
	 * @param typeList
	 * @param param
	 * @return
	 */
	List<LotteryGift> getListByGiftTypeList(List<GiftType> typeList, DDBParam param);

}
