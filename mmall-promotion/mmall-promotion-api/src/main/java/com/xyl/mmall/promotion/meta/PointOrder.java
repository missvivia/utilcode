/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;

/**
 * PointOrder.java created by yydx811 at 2015年12月23日 上午10:01:39
 * 积分订单
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "订单积分表", tableName = "Mmall_Promotion_PointOrder", dbCreateTimeName = "CreateTime")
public class PointOrder implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 8669776981726303985L;

	@AnnonOfField(desc = "id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "订单ID")
	private long orderId;

	@AnnonOfField(desc = "用户ID")
	private long userId;

	@AnnonOfField(desc = "积分增减量", defa = "0")
	private int pointDelta;
	
	@AnnonOfField(desc = "积分兑换抵扣金额")
	private BigDecimal exchangeCash = BigDecimal.ZERO;

	@AnnonOfField(desc = "处理情况")
	private ActivationHandlerType pointHandlerType = ActivationHandlerType.DEFAULT;
	
	@AnnonOfField(desc = "使用时间")
	private long usedTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPointDelta() {
		return pointDelta;
	}

	public void setPointDelta(int pointDelta) {
		this.pointDelta = pointDelta;
	}

	public BigDecimal getExchangeCash() {
		return exchangeCash;
	}

	public void setExchangeCash(BigDecimal exchangeCash) {
		this.exchangeCash = exchangeCash;
	}

	public ActivationHandlerType getPointHandlerType() {
		return pointHandlerType;
	}

	public void setPointHandlerType(ActivationHandlerType pointHandlerType) {
		this.pointHandlerType = pointHandlerType;
	}

	public long getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(long usedTime) {
		this.usedTime = usedTime;
	}
	
	public PointOrder cloneObject() {
		PointOrder po = new PointOrder();
		BeanUtils.copyProperties(this, po);
		return po;
	}
}
