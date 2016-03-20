/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.content.meta.SuitcaseGift;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * @author hzlihui2014
 *
 */
@Repository
public class SuitcaseGiftDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<SuitcaseGift> implements SuitcaseGiftDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.SuitcaseGiftDao#findByUserIdAndOrderId(long,
	 *      long)
	 */
	@Override
	public List<SuitcaseGift> findByUserIdAndOrderId(long userId, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return queryObjects(sql);
	}

}
