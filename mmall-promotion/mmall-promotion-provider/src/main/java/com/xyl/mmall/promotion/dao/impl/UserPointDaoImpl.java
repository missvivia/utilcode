/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.UserPointDao;
import com.xyl.mmall.promotion.meta.UserPoint;

/**
 * UserPointDaoImpl.java created by yydx811 at 2015年12月23日 下午12:53:14
 * 用户积分dao接口实现
 *
 * @author yydx811
 */
@Repository("UserPointDao")
public class UserPointDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<UserPoint> implements UserPointDao {

	@Override
	public UserPoint saveObject(UserPoint obj) {
		// 先更新
		if (updateUserPoint(obj)) {
			return obj;
		}
		// 更新失败，添加
		long id = this.allocateRecordId();
		obj.setId(id);
		return addObject(obj);
	}

	@Override
	public boolean updateUserPoint(UserPoint userPoint) {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.getTableName()).append(" SET ");
		sql.append(" Point = Point + ").append(userPoint.getPoint());
		sql.append(" WHERE ").append("UserId = ").append(userPoint.getUserId());
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public UserPoint getUserPointByUserId(long userId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		return queryObject(sql.toString());
	}
}
