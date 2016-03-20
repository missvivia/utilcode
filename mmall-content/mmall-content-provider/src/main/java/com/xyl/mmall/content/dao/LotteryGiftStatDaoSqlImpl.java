/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.content.meta.LotteryGiftStat;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * @author hzzhanghui
 * 
 */
@Repository
public class LotteryGiftStatDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<LotteryGiftStat> implements
		LotteryGiftStatDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<LotteryGiftStat> getGiftStatList() {
		logger.info("getGiftStatList() called.");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		logger.info("SQL: " + sql.toString());
		return queryObjects(sql);
	}

	public boolean updateGiftStat(int type, long date, int cnt) {
		logger.info("updateGiftStat(" + type + "," + date + ")");
		String sql = " UPDATE Mmall_Content_LotteryGiftStat SET lotteryCnt=" + cnt + " WHERE type=" + type
				+ " AND lotteryDate=" + date;
		logger.info("SQL: " + sql);
		try {
			getSqlSupport().excuteUpdate(sql);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public LotteryGiftStat getGiftStat(int type, long date) {
		logger.info("getGiftStat(" + type + "," + date + ") ");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "type", type);
		SqlGenUtil.appendExtParamObject(sql, "lotteryDate", date);
		logger.info("SQL: " + sql.toString());
		return queryObject(sql);
	}
}
