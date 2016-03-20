package com.xyl.mmall.order.meta.tcc;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;

/**
 * 订单服务的Sku库存数据-TCC
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "订单服务的Sku库存数据-TCC", tableName = "Mmall_Order_SkuOrderStockTCC")
public class SkuOrderStockTCC implements TCCMetaInterface {

	@AnnonOfField(desc = "skuId", primary = true, primaryIndex = 1)
	private long skuId;

	@AnnonOfField(desc = "库存量")
	private int stockCount;

	@AnnonOfField(desc = "poId")
	private long poId;

	@AnnonOfField(desc = "是否是增加库存")
	private boolean isAddStockCount;

	@AnnonOfField(desc = "TCC事务Id", primary = true, primaryIndex = 2, policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	public boolean isAddStockCount() {
		return isAddStockCount;
	}

	public void setAddStockCount(boolean isAddStockCount) {
		this.isAddStockCount = isAddStockCount;
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
