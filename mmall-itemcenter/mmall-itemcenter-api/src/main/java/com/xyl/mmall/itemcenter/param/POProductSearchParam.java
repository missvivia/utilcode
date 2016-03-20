package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

import com.xyl.mmall.itemcenter.enums.StatusType;

/**
 * 添加到PO的商品筛选参数类
 * 
 * @author hzhuangluqian
 *
 */
public class POProductSearchParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2197308112687568933L;

	/** poId */
	private long poId;

	/** 供应商id */
	private long supplierId;

	/** 类目id */
	private long lowCategoryId;

	/** 商品名称 */
	private String productName;

	/** 货号 */
	private String goodsNo;

	/** 上次查询的最后一个id值 */
	private long lastId;

	/** 查询数量 */
	private int limit;

	/** 条形码 */
	private String barCode;

	private int offset;
	/** 商品审核状态 */
	private StatusType status;

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public long getLastId() {
		return lastId;
	}

	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
