package com.xyl.mmall.itemcenter.param;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;

public class ProductSKUSearchParam extends DDBParam{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2595601489562946834L;

	/** 商家id */
	private long businessId;

	/** 商品名称 */
	private String productName;

	/** 货号即skuId */
	private String goodsNo;

	/** 上次查询的最后一个id值 */
	private long lastId;

	/** 开始时间 */
	private long stime;

	/** 结束时间 */
	private long etime;

	/**
	 * 商品状态 : 1未审核，2审核中，3审核未通过，4已上架，5已下架
	 */
	private int status = 0;
	
	/**
	 * 区域Id
	 */
	private long areaId;
	
	/** 第一级类目   记住搜索条件类目使用 */
	private long firstCategoryId;
	
	/** 第二级类目   记住搜索条件类目使用 */
	private long secondCategoryId;
	
	/** 最低类目id  商家后台根据类目搜索搜索商品用*/
	private long lowCategoryId;
	
	
	/**
	 * 内容分类 rootId
	 */
	private long rootId;

	/**
	 * 类目等级
	 */
	private int level=0;
	
	/**
	 * 库存状态: 1 库存不足 ，2库存足
	 */
	private int stockStatus = 0;
	
	private BigDecimal sprice;
	
	private BigDecimal eprice;
	
	/**
	 * 商品分类Ids: 店铺里根据类目搜索用
	 */
	private String categoryNormalIds;
	
	/**
	 * 单品库Id List
	 */
	private List<Long> spuIdList;
	
	/**
	 * 商品Id List
	 */
	private List<Long> skuIdList;
	
	/**
	 * 条形码
	 */
	private String barCode;
	
	/**
	 * 搜索类型  1:商家平台商品搜索  2：mainsite店铺中商品搜索  默认1
	 */
    private int searchType = 1;	
    
    /**
     * 品牌Ids
     */
    private List<Long> brandIdList;
    
    /**
     * 1限购  ;0不限购
     */
    private int isLimited = -1;
    
    /**
     * 用户Id
     */
    private long userId;
    
	public int getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(int stockStatus) {
		this.stockStatus = stockStatus;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public BigDecimal getSprice() {
		return sprice;
	}

	public void setSprice(BigDecimal sprice) {
		this.sprice = sprice;
	}

	public BigDecimal getEprice() {
		return eprice;
	}

	public void setEprice(BigDecimal eprice) {
		this.eprice = eprice;
	}

	public String getCategoryNormalIds() {
		return categoryNormalIds;
	}

	public void setCategoryNormalIds(String categoryNormalIds) {
		this.categoryNormalIds = categoryNormalIds;
	}

	public List<Long> getSpuIdList() {
		return spuIdList;
	}

	public void setSpuIdList(List<Long> spuIdList) {
		this.spuIdList = spuIdList;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public List<Long> getSkuIdList() {
		return skuIdList;
	}

	public void setSkuIdList(List<Long> skuIdList) {
		this.skuIdList = skuIdList;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public List<Long> getBrandIdList() {
		return brandIdList;
	}

	public void setBrandIdList(List<Long> brandIdList) {
		this.brandIdList = brandIdList;
	}

	public long getRootId() {
		return rootId;
	}

	public void setRootId(long rootId) {
		this.rootId = rootId;
	}

	public long getFirstCategoryId() {
		return firstCategoryId;
	}

	public void setFirstCategoryId(long firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}

	public long getSecondCategoryId() {
		return secondCategoryId;
	}

	public void setSecondCategoryId(long secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}

	public int getIsLimited() {
		return isLimited;
	}

	public void setIsLimited(int isLimited) {
		this.isLimited = isLimited;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}


    
    
	
	
	
	
}
