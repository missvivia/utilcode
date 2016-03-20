/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.content.meta.LotteryOrderCnt;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * @author hzzhanghui
 * 
 */
@Repository
public class LotteryOrderCntDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<LotteryOrderCnt> implements
		LotteryOrderCntDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<LotteryOrderCnt> getGiftOrderCntList() {
		logger.info("getGiftOrderCntList() called.");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		logger.info("SQL: " + sql.toString());
		return queryObjects(sql);
	}

}
