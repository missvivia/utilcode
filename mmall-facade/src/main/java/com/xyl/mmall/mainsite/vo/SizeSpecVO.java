package com.xyl.mmall.mainsite.vo;

import java.util.List;

/**
 * 详情页尺寸规格VO
 * 
 * @author hzhuangluqian
 *
 */
public class SizeSpecVO {
	private long index;

	/** skuId */
	private String skuId;

	/** 尺码标签显示的文字，如L(155/190A) */
	private String size;

	/** sku数量 */
	private int num;

	/** sku原始总量 */
	private int total;

	private int stock;
	
	/** 类型 1：正常； 2：有机会 ；3：售罄 ； */
	private int type;

	/** 鼠标悬浮时候显示的尺码详情 */
	private List<BaseNameValueVO> sizeTips;

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<BaseNameValueVO> getSizeTips() {
		return sizeTips;
	}

	public void setSizeTips(List<BaseNameValueVO> sizeTips) {
		this.sizeTips = sizeTips;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
