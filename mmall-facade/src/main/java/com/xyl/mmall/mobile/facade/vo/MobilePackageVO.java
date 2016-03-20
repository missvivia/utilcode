package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 第一級類
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobilePackageVO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4808039141599910349L;
	private long packageId;
	
	private long packageIndex;
	//快递单号
	private String deliverCode;
	//快递详情查询URL
	private String deliverURL;
	//下单时间
	private long orderTime;
	//Sku对象
	private List<MobileSkuVO> skuList;
	//商品件数
	private int prdtCount;
	//1.1新增(包裹状态)
	private int packageStatus;

	private String rejectInfo;
	public String getDeliverCode() {
		return deliverCode;
	}
	public void setDeliverCode(String deliverCode) {
		this.deliverCode = deliverCode;
	}
	public String getDeliverURL() {
		return deliverURL;
	}
	public void setDeliverURL(String deliverURL) {
		this.deliverURL = deliverURL;
	}
	public long getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public List<MobileSkuVO> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<MobileSkuVO> skuList) {
		this.skuList = skuList;
	}
	public long getPackageId() {
		return packageId;
	}
	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}
	public long getPackageIndex() {
		return packageIndex;
	}
	public void setPackageIndex(long packageIndex) {
		this.packageIndex = packageIndex;
	}
	public int getPackageStatus() {
		return packageStatus;
	}
	public void setPackageStatus(int packageStatus) {
		this.packageStatus = packageStatus;
	}
	public String getRejectInfo() {
		return rejectInfo;
	}
	public void setRejectInfo(String rejectInfo) {
		this.rejectInfo = rejectInfo;
	}
	public int getPrdtCount() {
		return prdtCount;
	}
	public void setPrdtCount(int prdtCount) {
		this.prdtCount = prdtCount;
	}
	
	
}
