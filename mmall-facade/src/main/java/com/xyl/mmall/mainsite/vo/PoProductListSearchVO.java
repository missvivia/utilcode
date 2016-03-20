package com.xyl.mmall.mainsite.vo;

import java.math.BigDecimal;

public class PoProductListSearchVO {

	/** 档期id */
	private long scheduleId;

	/** 类目id */
	private long categoryId;

	/** 是否降序 */
	private boolean desc = true;

	/** 起始价格 */
	private BigDecimal priceTo;

	/** 终止价格 */
	private BigDecimal priceFrom;

	/** 排序字段，0-默认、1-价格、2-销量、3-折扣四类，不传或者空则表示默认排序 */
	private int order;

	/** 列表偏移量 */
	private int offset;

	/** 是否要返回总数 */
	private boolean total;

	/** 列表数量 */
	private int limit;

	private long lastId;

	/** 商品名包含的关键字 */
	private String productName;

	private boolean preview;
	
	private String brandIds;
	
	/** 条形码 */
	private String barcode;

	/** 进货日期 */
	private long replenishTime;
	
	/** 商品Ids */
	private String productIds;
	
	
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public long getReplenishTime() {
		return replenishTime;
	}

	public void setReplenishTime(long replenishTime) {
		this.replenishTime = replenishTime;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

	public BigDecimal getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(BigDecimal priceTo) {
		this.priceTo = priceTo;
	}

	public BigDecimal getPriceFrom() {
		return priceFrom;
	}

	public void setPriceFrom(BigDecimal priceFrom) {
		this.priceFrom = priceFrom;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
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

	public long getLastId() {
		return lastId;
	}

	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}

	public String getBrandIds() {
		return brandIds;
	}

	public void setBrandIds(String brandIds) {
		this.brandIds = brandIds;
	}
	
	public String getProductIds() {
		return productIds;
	}

	public void setPoProductIds(String productIds) {
		this.productIds = productIds;
	}

}
