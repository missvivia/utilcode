package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobilePrdtSummaryVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 676180817586626401L;
	//商品ID
	protected long prdtId;
	//商品名字
	protected String prdtName;
	//商品图片，取第一张
	protected String imageURL;
	//原价格
	protected double orignPrice;
	//专场价
	protected double poPrice;
	//折扣
	protected String discountDesc;
	//剩余货量(按最少尺码数量的数量值)
	protected int inventoryCount;
	//销量
	protected int saleCount;
	//结束日期
	protected long endTime;
	//剩余时间
	protected long countDownTime;
	//0:不在当期，1：在当期
	protected int status;
	//0:不在当期，1：在当期
	protected int isRecommend;
	//0:旧货，1：专柜同款
	protected List<String> tag;
	
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
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public double getOrignPrice() {
		return orignPrice;
	}
	public void setOrignPrice(double orignPrice) {
		this.orignPrice = orignPrice;
	}
	public double getPoPrice() {
		return poPrice;
	}
	public void setPoPrice(double poPrice) {
		this.poPrice = poPrice;
	}
	public String getDiscountDesc() {
		return discountDesc;
	}
	public void setDiscountDesc(String discountDesc) {
		this.discountDesc = discountDesc;
	}
	public int getInventoryCount() {
		return inventoryCount;
	}
	public void setInventoryCount(int inventoryCount) {
		this.inventoryCount = inventoryCount;
	}
	public int getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public long getCountDownTime() {
		return countDownTime;
	}
	public void setCountDownTime(long countDownTime) {
		this.countDownTime = countDownTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
	public void setTag(String name, String icon) {
		List<String> list = new ArrayList<String>();
		list.add(icon);
		this.tag = list;
	}

}
