package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

public class BatchGetPoSkuParam implements Serializable {
	private long skuId;
	private String barCode;
	private long poId;

	private String goodsNo;

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
}
