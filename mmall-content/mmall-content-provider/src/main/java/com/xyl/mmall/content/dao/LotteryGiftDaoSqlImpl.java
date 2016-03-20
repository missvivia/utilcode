/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.content.enums.GiftType;
import com.xyl.mmall.content.meta.LotteryGift;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * @author hzlihui2014
 * 
 */
@Repository
public class LotteryGiftDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<LotteryGift> implements LotteryGiftDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean saveGiftHit(LotteryGift gift) {
		logger.info("saveGiftHit: " + gift);
		addObject(gift);
		return true;
	}

	@Override
	public List<LotteryGift> getGiftHitByUserId(long userId) {
		logger.info("getGiftHitByUserId(" + userId + ")");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		logger.info("SQL: " + sql.toString());
		return queryObjects(sql);
	}

	@Override
	public List<LotteryGift> getGiftHitByUserIdAndTime(long userId, long hitTime) {
		logger.info("getGiftHitByUserIdAndTime(" + userId + ", " + hitTime + ")");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "hitTime", hitTime);
		logger.info("SQL: " + sql.toString());
		return queryObjects(sql);
	}

	@Override
	public List<LotteryGift> getGiftHitByUserIdAndTimeRange(long userId, long hitTimeStart, long hitTimeEnd) {
		logger.info("getGiftHitByUserIdAndTimeRange(" + userId + ", " + hitTimeStart + ", " + hitTimeEnd + ")");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		sql.append(" AND hitTime>=" + hitTimeStart);
		sql.append(" AND hitTime<=" + hitTimeEnd);
		logger.info("SQL: " + sql.toString());
		return queryObjects(sql);
	}

	@Override
	public List<LotteryGift> getGiftHitList() {
		logger.info("getGiftHitList() called.");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		logger.info("SQL: " + sql.toString());
		return queryObjects(sql);
	}

	@Override
	public List<LotteryGift> getGiftHitListByTime(long hitTime) {
		logger.info("getGiftHitListByTime(" + hitTime + ")");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "hitTime", hitTime);
		logger.info("SQL: " + sql.toString());
		return queryObjects(sql);
	}

	@Override
	public List<LotteryGift> getGiftHitListByTimeRange(long hitTimeStart, long hitTimeEnd) {
		logger.info("getGiftHitListByTimeRange(" + hitTimeStart + ", " + hitTimeEnd + ")");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		sql.append(" AND hitTime>=" + hitTimeStart);
		sql.append(" AND hitTime<=" + hitTimeEnd);
		logger.info("SQL: " + sql.toString());
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.LotteryGiftDao#getListByGiftTypeList(java.util.List,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	@Cacheable(value = "latestLotteryGiftList", key = "#typeList")
	public List<LotteryGift> getListByGiftTypeList(List<GiftType> typeList, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "type", typeList);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}

}
