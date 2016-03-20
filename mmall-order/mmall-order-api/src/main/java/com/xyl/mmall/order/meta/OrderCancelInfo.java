package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderCancelState;

/**
 * 订单取消信息
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderCancelInfo", desc = "订单取消原因")
public class OrderCancelInfo implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "OrderId", primary = true)
	private long orderId;

	@AnnonOfField(desc = "UserId", policy = true)
	private long userId;

	@AnnonOfField(desc = "记录创建时间戳")
	private long ctime;

	@AnnonOfField(desc = "取消原因", safeHtml = true)
	private String reason;

	@AnnonOfField(desc = "取消后的金额返回方式(0:原路退回,1:网易宝)")
	private OrderCancelRType rtype;

	@AnnonOfField(desc = "取消来源(0:用户取消;1:客服取消;2:超时系统取消)")
	private OrderCancelSource cancelSource;

	@AnnonOfField(desc = "取消状态(0:创建完毕;1:交易处理完毕)")
	private OrderCancelState cancelState = OrderCancelState.CREATE;

	@AnnonOfField(desc = "取消逻辑的执行次数")
	private int retryCount;

	@AnnonOfField(desc = "取消逻辑的执行结果")
	private long retryFlag;

	// ------------------------------------------

	/**
	 * retryFlag位定义: 取消交易的结果
	 */
	public static final int IDX_CANCEL_TRADE = 0;

	/**
	 * retryFlag位定义: 取消OMS订单的结果
	 */
	public static final int IDX_CANCEL_OMSORDER = 1;

	// -----------------------------------------

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

	public OrderCancelState getCancelState() {
		return cancelState;
	}

	public void setCancelState(OrderCancelState cancelState) {
		this.cancelState = cancelState;
	}

	public OrderCancelRType getRtype() {
		return rtype;
	}

	public void setRtype(OrderCancelRType rtype) {
		this.rtype = rtype;
	}

	public OrderCancelSource getCancelSource() {
		return cancelSource;
	}

	public void setCancelSource(OrderCancelSource cancelSource) {
		this.cancelSource = cancelSource;
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

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
