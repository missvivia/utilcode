/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * PointRule.java created by yydx811 at 2015年12月23日 上午10:19:02
 * 积分兑换规则
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "积分兑换规则表", tableName = "Mmall_Promotion_PointRule", dbCreateTimeName = "CreateTime")
public class PointRule implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -1855042485091032262L;

	@AnnonOfField(desc = "积分兑换规则id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "站点id", policy = true)
	private long siteId;

	@AnnonOfField(desc = "区间最小积分值")
	private int minPoint;

	@AnnonOfField(desc = "积分兑换抵扣金额")
	private BigDecimal exchangeCash = BigDecimal.ZERO;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public int getMinPoint() {
		return minPoint;
	}

	public void setMinPoint(int minPoint) {
		this.minPoint = minPoint;
	}

	public BigDecimal getExchangeCash() {
		return exchangeCash;
	}

	public void setExchangeCash(BigDecimal exchangeCash) {
		this.exchangeCash = exchangeCash;
	}
}
