package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

import com.xyl.mmall.itemcenter.enums.SizeType;

public class BatchSizePrimaryValueParam implements Serializable {
	private long skuId;

	private long templatekey;

	private long sizeId;

	private SizeType sizeType;

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getTemplatekey() {
		return templatekey;
	}

	public void setTemplatekey(long templatekey) {
		this.templatekey = templatekey;
	}

	public long getSizeId() {
		return sizeId;
	}

	public void setSizeId(long sizeId) {
		this.sizeId = sizeId;
	}

	public SizeType getSizeType() {
		return sizeType;
	}

	public void setSizeType(SizeType sizeType) {
		this.sizeType = sizeType;
	}

}
