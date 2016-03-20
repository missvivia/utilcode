/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.meta.CouponConfig;

/**
 * CouponConfigDao.java created by yydx811 at 2015年12月31日 下午2:05:45
 * 优惠券配置dao
 *
 * @author yydx811
 */
public interface CouponConfigDao extends AbstractDao<CouponConfig> {

	/**
	 * 根据站点id，配置类型，获取优惠券配置
	 * @param siteId
	 * @param type
	 * @return
	 */
	public CouponConfig getCouponConfigByType(long siteId, int type);
	
	/**
	 * 更新优惠券配置
	 * @param couponConfig
	 * @return
	 */
	public int updateCouponConfig(CouponConfig couponConfig);
}
