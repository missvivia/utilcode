package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderPackageRefundTaskState;

/**
 * 包裹退款定时任务
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderPackageRefundTask", desc = "包裹退款定时任务")
public class OrderPackageRefundTask implements Serializable {

	private static final long serialVersionUID = 20150113L;

	@AnnonOfField(desc = "id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "包裹id")
	private long packageId;

	@AnnonOfField(desc = "订单id")
	private long orderId;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "总金额")
	private BigDecimal wybCash = BigDecimal.ZERO;

	@AnnonOfField(desc = "红包金额")
	private BigDecimal redCash = BigDecimal.ZERO;

	@AnnonOfField(desc = "退款方式")
	private OrderCancelRType rtype;

	@AnnonOfField(desc = "记录创建时间戳")
	private long ctime;

	@AnnonOfField(desc = "取消逻辑的执行结果")
	private long retryFlag;

	@AnnonOfField(desc = "取消状态(0:创建完毕;1:交易处理完毕)")
	private OrderPackageRefundTaskState state = OrderPackageRefundTaskState.CREATE;

	@AnnonOfField(desc = "取消逻辑的执行次数")
	private int retryCount = 0;

	// ------------------------------------------

	/**
	 * retryFlag位定义: 网易宝退款
	 */
	public static final int IDX_REFUND_WYB = 0;

	/**
	 * retryFlag位定义: 红包退款
	 */
	public static final int IDX_REFUND_HB = 1;

	// -----------------------------------------

	public long getCtime() {
		return ctime;
	}

	public OrderCancelRType getRtype() {
		return rtype;
	}

	public void setRtype(OrderCancelRType rtype) {
		this.rtype = rtype;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public OrderPackageRefundTaskState getState() {
		return state;
	}

	public void setState(OrderPackageRefundTaskState state) {
		this.state = state;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public long getRetryFlag() {
		return retryFlag;
	}

	public void setRetryFlag(long retryFlag) {
		this.retryFlag = retryFlag;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public BigDecimal getRedCash() {
		return redCash;
	}

	public void setRedCash(BigDecimal redCash) {
		this.redCash = redCash;
	}

	public BigDecimal getWybCash() {
		return wybCash;
	}

	public void setWybCash(BigDecimal wybCash) {
		this.wybCash = wybCash;
	}
}
