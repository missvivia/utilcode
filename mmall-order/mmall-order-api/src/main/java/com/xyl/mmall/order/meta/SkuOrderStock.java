package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 订单服务的Sku库存数据
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "订单服务的Sku库存数据", tableName = "Mmall_Order_SkuOrderStock", dbCreateTimeName = "CreateTime")
public class SkuOrderStock implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "skuId", primary = true, policy = true)
	private long skuId;

	@AnnonOfField(desc = "可售数量")
	private int stockCount;

	@AnnonOfField(desc = "创建时间")
	private long ctime;

	@AnnonOfField(desc = "更新时间")
	private long upTime;

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getUpTime() {
		return upTime;
	}

	public void setUpTime(long upTime) {
		this.upTime = upTime;
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
}
