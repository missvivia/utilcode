package com.xyl.mmall.order.meta.tcc;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.enums.OrderTCCLockType;

/**
 * 订单服务-TCC事务锁
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_TCCLock", desc = "订单服务-TCC事务锁")
public class OrderTCCLock {

	@AnnonOfField(desc = "TCC事务Id", primary = true, policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	@AnnonOfField(desc = "OrderId")
	private long orderId;

	@AnnonOfField(desc = "TCC事务类型(0:添加订单;1:取消订单)")
	private OrderTCCLockType type;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public OrderTCCLockType getType() {
		return type;
	}

	public void setType(OrderTCCLockType type) {
		this.type = type;
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

	/**
	 * 生成OrderTCCLock对象
	 * 
	 * @param tranId
	 * @param currTime
	 * @param type
	 * @param orderId
	 * @return
	 */
	public static OrderTCCLock genOrderTCCLock(long tranId, long currTime, OrderTCCLockType type, long orderId) {
		OrderTCCLock orderTCCLock = new OrderTCCLock();
		orderTCCLock.setCtimeOfTCC(currTime);
		orderTCCLock.setTranId(tranId);
		orderTCCLock.setType(type);
		orderTCCLock.setOrderId(orderId);
		return orderTCCLock;
	}

	/**
	 * 生成OrderTCCLock对象<br>
	 * 只包含TranId字段(DB主键+均衡字段)
	 * 
	 * @param tranId
	 * @return
	 */
	public static OrderTCCLock genOrderTCCLock(long tranId) {
		OrderTCCLock orderTCCLock = new OrderTCCLock();
		orderTCCLock.setTranId(tranId);
		return orderTCCLock;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
