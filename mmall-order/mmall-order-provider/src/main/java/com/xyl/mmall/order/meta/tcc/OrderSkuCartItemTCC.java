package com.xyl.mmall.order.meta.tcc;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.order.meta.OrderSkuCartItem;

/**
 * @author dingmingliang
 *
 */
@AnnonOfClass(desc = "订单明细-普通商品Sku-TCC", tableName = "Mmall_Order_OrderSkuCartItemTCC")
public class OrderSkuCartItemTCC extends OrderSkuCartItem implements TCCMetaInterface {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

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