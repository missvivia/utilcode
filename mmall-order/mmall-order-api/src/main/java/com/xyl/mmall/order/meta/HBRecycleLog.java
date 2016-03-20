package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.HBRecycleState;

/**
 * 退货包裹红包回收记录
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:10:21
 * 
 */
@AnnonOfClass(desc = "退货包裹红包回收记录", tableName = "Mmall_Order_HBRecycleLog")
public class HBRecycleLog implements Serializable {

	private static final long serialVersionUID = -6927719922584400168L;

	@AnnonOfField(desc = "退货包裹Id(PK)", primary = true, autoAllocateId = true)
	private long retPkgId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;
	
	@AnnonOfField(desc = "要退的订单的Id")
	private long orderId;
	
	@AnnonOfField(desc = "要退的订单包裹的Id")
	private long orderPkgId;

	@AnnonOfField(desc = "标记红包回收状态")
	private HBRecycleState hbRecycleState = HBRecycleState.INIT;
	
	@AnnonOfField(desc = "申请的红包退款金额(从ReturnPackage中复制)")
	private BigDecimal applyedReturnHbPrice = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "实际退给用户的红包退款金额(从ReturnPackage中复制)")
	private BigDecimal payedHbPriceToUser = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "申请时间")
	private long createTime;
	
	@AnnonOfField(desc = "更新时间")
	private long updateTime;
	
	public long getRetPkgId() {
		return retPkgId;
	}

	public void setRetPkgId(long id) {
		this.retPkgId = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOrderPkgId() {
		return orderPkgId;
	}

	public void setOrderPkgId(long orderPkgId) {
		this.orderPkgId = orderPkgId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long ctime) {
		this.createTime = ctime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public HBRecycleState getHbRecycleState() {
		return hbRecycleState;
	}

	public void setHbRecycleState(HBRecycleState hbRecycleState) {
		this.hbRecycleState = hbRecycleState;
	}

	public BigDecimal getApplyedReturnHbPrice() {
		return applyedReturnHbPrice;
	}

	public void setApplyedReturnHbPrice(BigDecimal applyedReturnHbPrice) {
		this.applyedReturnHbPrice = applyedReturnHbPrice;
	}

	public BigDecimal getPayedHbPriceToUser() {
		return payedHbPriceToUser;
	}

	public void setPayedHbPriceToUser(BigDecimal payedHbPriceToUser) {
		this.payedHbPriceToUser = payedHbPriceToUser;
	}
	
}