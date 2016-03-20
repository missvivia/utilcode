/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.CouponConfigDao;
import com.xyl.mmall.promotion.meta.CouponConfig;

/**
 * CouponConfigDaoImpl.java created by yydx811 at 2015年12月31日 下午2:07:00
 * 优惠券配置dao接口实现
 *
 * @author yydx811
 */
@Repository("couponConfigDao")
public class CouponConfigDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CouponConfig> 
	implements CouponConfigDao {

	@Override
	public CouponConfig getCouponConfigByType(long siteId, int type) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "SiteId", siteId);
		SqlGenUtil.appendExtParamObject(sql, "Type", type);
		return queryObject(sql.toString());
	}

	@Override
	public int updateCouponConfig(CouponConfig couponConfig) {
		StringBuilder sql = new StringBuilder(255);
		sql.append("UPDATE ").append(this.getTableName()).append(" SET ");
		sql.append("CouponCodes = ?, IsRelativeTime = ?, ValidFlag = ?");
		sql.append(" WHERE SiteId = ").append(couponConfig.getSiteId());
		SqlGenUtil.appendExtParamObject(sql, "Type", couponConfig.getType());
		if (couponConfig.getId() > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "Id", couponConfig.getId());
		}
		return this.getSqlSupport().excuteUpdate(sql.toString(), 
				couponConfig.getCouponCodes(), couponConfig.getIsRelativeTime(),
				couponConfig.getValidFlag());
	}
}
