package com.xyl.mmall.order.meta.tcc;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.order.meta.OrderPOInfo;

/**
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "订单和PO的关系-TCC", tableName = "Mmall_Order_OrderPOInfoTCC")
public class OrderPOInfoTCC extends OrderPOInfo implements Serializable, TCCMetaInterface {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "poId")
	private long poId;

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
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