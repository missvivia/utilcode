package com.xyl.mmall.order.meta.tcc;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 更新订单状态的TCC
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "更新订单状态的TCC", tableName = "Mmall_Order_UpdateOrderStateTCC")
public class UpdateOrderStateTCC implements TCCMetaInterface {

	@AnnonOfField(desc = "订单Id", primary = true, primaryIndex = 1)
	private long orderId;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	@AnnonOfField(desc = "订单状态-更新前")
	private OrderFormState oriOState;

	@AnnonOfField(desc = "订单状态-更新后")
	private OrderFormState newOState;

	@AnnonOfField(desc = "支付状态-更新前")
	private PayState oriPState;

	@AnnonOfField(desc = "支付状态-更新后")
	private PayState newPState;

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public OrderFormState getOriOState() {
		return oriOState;
	}

	public void setOriOState(OrderFormState oriOState) {
		this.oriOState = oriOState;
	}

	public OrderFormState getNewOState() {
		return newOState;
	}

	public void setNewOState(OrderFormState newOState) {
		this.newOState = newOState;
	}

	public PayState getOriPState() {
		return oriPState;
	}

	public void setOriPState(PayState oriPState) {
		this.oriPState = oriPState;
	}

	public PayState getNewPState() {
		return newPState;
	}

	public void setNewPState(PayState newPState) {
		this.newPState = newPState;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getCtimeOfTCC() {
		return ctimeOfTCC;
	}

	public void setCtimeOfTCC(long ctimeOfTCC) {
		this.ctimeOfTCC = ctimeOfTCC;
	}

	public long getTranId() {
		return tranId;
	}

	public void setTranId(long tranId) {
		this.tranId = tranId;
	}
}
