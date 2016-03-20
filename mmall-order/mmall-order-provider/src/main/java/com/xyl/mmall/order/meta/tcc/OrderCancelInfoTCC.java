package com.xyl.mmall.order.meta.tcc;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.order.meta.OrderCancelInfo;

/**
 * 订单取消信息
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderCancelInfoTCC", desc = "订单取消原因-TCC")
public class OrderCancelInfoTCC extends OrderCancelInfo implements TCCMetaInterface {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	@AnnonOfField(desc = "UserId")
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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
