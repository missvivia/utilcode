package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.ReturnCouponRecycleState;
import com.xyl.mmall.order.enums.ReturnOrderSkuNumState;

/**
 * 退货记录(订单维度)
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:10:21
 * 
 */
@AnnonOfClass(desc = "退货记录(订单维度)", tableName = "Mmall_Order_ReturnForm_New", dbCreateTimeName = "CreateTime")
public class ReturnForm implements Serializable {

	private static final long serialVersionUID = -6927719922584400168L;

	@AnnonOfField(desc = "要退的订单的Id", primary = true)
	private long orderId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "申请时间")
	private long ctime;

	@AnnonOfField(desc = "用户申请退货时，与订单退货数量关联的的状态")
	private ReturnOrderSkuNumState applyedNumState = ReturnOrderSkuNumState.APPLY_INIT;
	
	@AnnonOfField(desc = "系统或客服退款操作时，与订单退货数量关联的的状态")
	private ReturnOrderSkuNumState confirmedNumState = ReturnOrderSkuNumState.CONFIRM_INIT;
	
	@AnnonOfField(desc = "优惠券+红包回收状态")
	private ReturnCouponRecycleState couponHbRecycleState = ReturnCouponRecycleState.NONE;
	
	@AnnonOfField(desc = "备注信息")
	private String extInfo = "";
	
	@AnnonOfField(desc = "版本：用于乐观锁控制")
	private long version = 0;
	
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

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public ReturnOrderSkuNumState getApplyedNumState() {
		return applyedNumState;
	}

	public void setApplyedNumState(ReturnOrderSkuNumState applyedNumState) {
		this.applyedNumState = applyedNumState;
	}

	public ReturnOrderSkuNumState getConfirmedNumState() {
		return confirmedNumState;
	}

	public void setConfirmedNumState(ReturnOrderSkuNumState confirmedNumState) {
		this.confirmedNumState = confirmedNumState;
	}

	public ReturnCouponRecycleState getCouponHbRecycleState() {
		return couponHbRecycleState;
	}

	public void setCouponHbRecycleState(
			ReturnCouponRecycleState couponHbRecycleState) {
		this.couponHbRecycleState = couponHbRecycleState;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}