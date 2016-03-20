package com.xyl.mmall.mobile.ios.facade.pageView.cartList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xyl.mmall.mobile.ios.facade.pageView.common.IosProcPrice;

public class IosCartItem {

	private long skuId;
	private String name;
	private String thumb;
	private String unit;
	private long stockCount;
//	private boolean selected = true;
	private List<IosProcPrice> priceList;
	private int status;
	private int count;
	private MobileSKULimitVO skuLimitVO;
	@JsonIgnore
	private long createTime;
	
	
	
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public MobileSKULimitVO getSkuLimitVO() {
		return skuLimitVO;
	}
	public void setSkuLimitVO(MobileSKULimitVO skuLimitVO) {
		this.skuLimitVO = skuLimitVO;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getSkuId() {
		return skuId;
	}
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public long getStockCount() {
		return stockCount;
	}
	public void setStockCount(long stockCount) {
		this.stockCount = stockCount;
	}
//	public boolean isSelected() {
//		return selected;
//	}
//	public void setSelected(boolean selected) {
//		this.selected = selected;
//	}
	public List<IosProcPrice> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<IosProcPrice> priceList) {
		this.priceList = priceList;
	}
	
	
}
