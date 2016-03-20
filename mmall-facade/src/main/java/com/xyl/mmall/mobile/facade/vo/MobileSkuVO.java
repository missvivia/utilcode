package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileSkuVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7623107745664927966L;
	private long skuId	;
	//对应的商品id
	private long  prdtId;
	//商名字
	private String prdtName;
	//商名字
	private String brandName;
	//尺寸描述
	private int buyCount;
	//尺寸描述
	private String skuSizeDesc;
	//专场价
	private double poPrice;
	//专柜价
	private double originPrice;
	//商品预览，取第一张
	private String skuImageUrl;
	//可售数量
	private int count;
	//狀態：1.有效，2超时 3抢光了，4活动到期，5主动删除
	private int validStatus;
	//失效描述
	private String invalidDesc;			
	
	public long getSkuId() {
		return skuId;
	}
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}
	public long getPrdtId() {
		return prdtId;
	}
	public void setPrdtId(long prdtId) {
		this.prdtId = prdtId;
	}
	public String getPrdtName() {
		return prdtName;
	}
	public void setPrdtName(String prdtName) {
		this.prdtName = prdtName;
	}
	public String getSkuSizeDesc() {
		return skuSizeDesc;
	}
	public void setSkuSizeDesc(String skuSizeDesc) {
		this.skuSizeDesc = skuSizeDesc;
	}
	public double getPoPrice() {
		return poPrice;
	}
	public void setPoPrice(double poPrice) {
		this.poPrice = poPrice;
	}
	public double getOriginPrice() {
		return originPrice;
	}
	public void setOriginPrice(double originPrice) {
		this.originPrice = originPrice;
	}
	public String getSkuImageUrl() {
		return skuImageUrl;
	}
	public void setSkuImageUrl(String skuImageUrl) {
		this.skuImageUrl = skuImageUrl;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getValidStatus() {
		return validStatus;
	}
	public void setValidStatus(int validStatus) {
		this.validStatus = validStatus;
	}
	public String getInvalidDesc() {
		return invalidDesc;
	}
	public void setInvalidDesc(String invalidDesc) {
		this.invalidDesc = invalidDesc;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	

}
