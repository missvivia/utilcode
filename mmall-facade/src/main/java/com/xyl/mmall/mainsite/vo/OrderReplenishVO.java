/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.mainsite.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xyl.mmall.order.dto.OrderReplenishDTO;

/**
 * OrderReplenishVO.java created by yydx811 at 2015年6月5日 下午3:54:43
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class OrderReplenishVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 5281307711088592195L;
	
	/** 补货单id. */
	private long replenishId;

	/** 用户id. */
	private long uid;

	/** 商家id. */
	private long supplierId;

	/** 商品id. */
	private long productId;

	/** 商品快照. */
	private String snapshot;

	/** 补货数量. */
	private int lastBuyNum;

	/** 最后一次购买价格. */
	private BigDecimal lastBuyPrice;
	
	/** 更新时间. */
	private long updateTime;

	/** 商品状态，0不存在，1未审核，2审核中，3审核未通过，4已上架，5已下架. */
	private int skuStatus;

	/** 价格浮动. */
	private String priceUpsAndDowns;
	
	/** 库存量. */
	private int skuStockNum;

	public OrderReplenishVO() {
	}
	
	public OrderReplenishVO(OrderReplenishDTO obj) {
		this.replenishId = obj.getId();
		this.uid = obj.getUserId();
		this.productId = obj.getSkuId();
		this.supplierId = obj.getBusinessId();
		this.snapshot = obj.getSkuSnapshot();
		this.lastBuyNum = obj.getBuyNum();
		this.lastBuyPrice = obj.getBuyPrice();
		if (obj.getUpdateTime() != null) {
			this.updateTime = obj.getUpdateTime().getTime();
		}
	}

	public long getReplenishId() {
		return replenishId;
	}

	public void setReplenishId(long replenishId) {
		this.replenishId = replenishId;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getSkuStatus() {
		return skuStatus;
	}

	public void setSkuStatus(int skuStatus) {
		this.skuStatus = skuStatus;
	}

	public String getPriceUpsAndDowns() {
		return priceUpsAndDowns;
	}

	public void setPriceUpsAndDowns(String priceUpsAndDowns) {
		this.priceUpsAndDowns = priceUpsAndDowns;
	}

	public int getLastBuyNum() {
		return lastBuyNum;
	}

	public void setLastBuyNum(int lastBuyNum) {
		this.lastBuyNum = lastBuyNum;
	}

	public BigDecimal getLastBuyPrice() {
		return lastBuyPrice;
	}

	public void setLastBuyPrice(BigDecimal lastBuyPrice) {
		this.lastBuyPrice = lastBuyPrice;
	}

	public int getSkuStockNum() {
		return skuStockNum;
	}

	public void setSkuStockNum(int skuStockNum) {
		this.skuStockNum = skuStockNum;
	}
}
