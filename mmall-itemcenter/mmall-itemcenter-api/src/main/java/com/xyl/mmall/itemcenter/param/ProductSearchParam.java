package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

/**
 * 商品筛选条件接收类
 * 
 * @author hzhuangluqian
 *
 */
public class ProductSearchParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 724203501445028767L;

	/** 供应商id */
	private long supplierId;

	/** 最低类目id */
	private long lowCategoryId;

	/** 基本信息是否录入，0：未选择，1未录入，2已录入 */
	private int isBaseInfo;

	/** 规格是否录入，0：未选择，1未录入，2已录入 */
	private int isPicInfo;

	/** 详情模块是否录入，0：未选择，1未录入，2已录入 */
	private int isDetailInfo;

	private int isSizeSet;
	
	/** 商品名称 */
	private String productName;

	/** 货号 */
	private String goodsNo;

	/** 上次查询的最后一个id值 */
	private long lastId;

	private int offset;
	/** 查询数量 */
	private int limit;

	/** 添加时间1 */
	private long stime;

	/** 添加时间2 */
	private long etime;

	/** 条形码 */
	private String barCode;

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

	public long getStime() {
		return stime;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public long getEtime() {
		return etime;
	}

	public void setEtime(long etime) {
		this.etime = etime;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getIsBaseInfo() {
		return isBaseInfo;
	}

	public void setIsBaseInfo(int isBaseInfo) {
		this.isBaseInfo = isBaseInfo;
	}

	public int getIsPicInfo() {
		return isPicInfo;
	}

	public void setIsPicInfo(int isPicInfo) {
		this.isPicInfo = isPicInfo;
	}

	public int getIsDetailInfo() {
		return isDetailInfo;
	}

	public void setIsDetailInfo(int isDetailInfo) {
		this.isDetailInfo = isDetailInfo;
	}

	public int getIsSizeSet() {
		return isSizeSet;
	}

	public void setIsSizeSet(int isSizeSet) {
		this.isSizeSet = isSizeSet;
	}

}
