package com.xyl.mmall.cart.dto;

import java.io.Serializable;

public class SkuInventoryAddDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int areaId;
	
	private long skuId;
	
	//增加数量
	private int num;

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
