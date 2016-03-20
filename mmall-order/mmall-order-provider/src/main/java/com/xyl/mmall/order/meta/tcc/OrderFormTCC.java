package com.xyl.mmall.order.meta.tcc;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.order.meta.OrderForm;

/**
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "订单-TCC", tableName = "Mmall_Order_OrderFormTCC")
public class OrderFormTCC extends OrderForm implements Serializable, TCCMetaInterface {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "用户Id")
	private long userId;

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