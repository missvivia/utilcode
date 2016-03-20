package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.order.enums.CancelOmsOrderTaskState;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 取消Oms订单的任务
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_CancelOmsOrderTask", desc = "取消Oms订单的任务")
public class CancelOmsOrderTask implements Serializable {

	private static final long serialVersionUID = 20150206L;

	@AnnonOfField(desc = "OrderId", primary = true)
	private long orderId;

	@AnnonOfField(desc = "UserId", policy = true)
	private long userId;

	@AnnonOfField(desc = "记录创建时间戳")
	private long ctime;

	@AnnonOfField(desc = "取消状态(0:创建完毕,1:oms成功,2:oms成功,3:oms失败,6:order成功,7:order失败)")
	private CancelOmsOrderTaskState taskState = CancelOmsOrderTaskState.CREATE;

	@AnnonOfField(desc = "订单状态")
	private OrderFormState oldOrderFormState;

	@AnnonOfField(desc = "取消原因", safeHtml = true)
	private String reason;

	@AnnonOfField(desc = "取消后的金额返回方式(0:原路退回,1:网易宝)")
	private OrderCancelRType rtype;

	@AnnonOfField(desc = "取消来源(0:用户取消;1:客服取消;2:超时系统取消)")
	private OrderCancelSource cancelSource;

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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public OrderFormState getOldOrderFormState() {
		return oldOrderFormState;
	}

	public void setOldOrderFormState(OrderFormState oldOrderFormState) {
		this.oldOrderFormState = oldOrderFormState;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public CancelOmsOrderTaskState getTaskState() {
		return taskState;
	}

	public void setTaskState(CancelOmsOrderTaskState taskState) {
		this.taskState = taskState;
	}
}
