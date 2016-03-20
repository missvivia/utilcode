package com.xyl.mmall.order.meta.tcc;

import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.order.meta.OrderSku;

/**
 * @author dingmingliang
 *
 */
@AnnonOfClass(desc = "OrderCartItem上显示用的基本单位-TCC", tableName = "Mmall_Order_OrderSkuTCC")
public class OrderSkuTCC extends OrderSku implements TCCMetaInterface {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;
	
	@AnnonOfField(desc = "创建时间", inDB = false)
	private Date createTime;

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