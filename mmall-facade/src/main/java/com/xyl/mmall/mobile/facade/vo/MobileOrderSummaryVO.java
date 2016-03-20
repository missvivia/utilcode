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
public class MobileOrderSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1044747052116914957L;
	//下单时间
	private long orderTime;
	//订单号
	private long orderId;
	//1，待付款，2待收货，3、已签收，4、退货中，5、超时未付款
	private int status;
	//如果待支付，有倒计时时间
	private long countdownTime;
	//如果待支付，有截止时间
	private long endTime;
	
	private int payChannel;
	
	private List<Integer> buttonList;
	//包裹列表
	private List<MobilePackageVO> packages;
	//订单中的第一个PO
	private MobileSkuVO firstSku;
	//运费
	private double carriageFee;
	//商品件数
	private int prdtCount;
	//订单总额
	private double totalPrice;
	
	private String codReject;

	private String deliverURL;
	
	private int cancelFail;
	public long getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCountdownTime() {
		return countdownTime;
	}
	public void setCountdownTime(long countdownTime) {
		this.countdownTime = countdownTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getPrdtCount() {
		return prdtCount;
	}
	public void setPrdtCount(int prdtCount) {
		this.prdtCount = prdtCount;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public MobileSkuVO getFirstSku() {
		return firstSku;
	}
	public void setFirstSku(MobileSkuVO firstSku) {
		this.firstSku = firstSku;
	}
	public double getCarriageFee() {
		return carriageFee;
	}
	public void setCarriageFee(double carriageFee) {
		this.carriageFee = carriageFee;
	}
	public List<MobilePackageVO> getPackages() {
		return packages;
	}
	public void setPackages(List<MobilePackageVO> packages) {
		this.packages = packages;
	}
	public int getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(int payChannel) {
		this.payChannel = payChannel;
	}
	public List<Integer> getButtonList() {
		return buttonList;
	}
	public void setButtonList(List<Integer> buttonList) {
		this.buttonList = buttonList;
	}
	public String getCodReject() {
		return codReject;
	}
	public void setCodReject(String codReject) {
		this.codReject = codReject;
	}
	public String getDeliverURL() {
		return deliverURL;
	}
	public void setDeliverURL(String deliverURL) {
		this.deliverURL = deliverURL;
	}
	public int getCancelFail() {
		return cancelFail;
	}
	public void setCancelFail(int cancelFail) {
		this.cancelFail = cancelFail;
	}

	
}
