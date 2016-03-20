/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.PointRuleDao;
import com.xyl.mmall.promotion.meta.PointRule;

/**
 * PointRuleDaoImpl.java created by yydx811 at 2015年12月23日 下午1:02:16
 * 积分规则dao接口实现
 *
 * @author yydx811
 */
@Repository("pointRuleDao")
public class PointRuleDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PointRule> implements PointRuleDao {

	@Override
	public List<PointRule> getRuleList(PointRule pointRule) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		genSqlWhere(sql, pointRule);
		this.appendOrderSql(sql, "MinPoint", true);
		return queryObjects(sql.toString());
	}

	private void genSqlWhere(StringBuilder sql, PointRule pointRule) {
		SqlGenUtil.appendExtParamObject(sql, "SiteId", pointRule.getSiteId());
		if (pointRule.getMinPoint() >= 0) {
			sql.append(" AND MinPoint <= ").append(pointRule.getMinPoint());
		}
	}
}
