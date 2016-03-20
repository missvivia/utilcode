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
public class MobileOrderVO implements Serializable{
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = 3732981682247140145L;
	//订单号
	protected long orderId;
	protected int status;
	protected int expressType;
	protected String expressName;
	//剩余支付时间
	protected long countDownSeconds;
	//截止支付时间
	protected long endTime;
	//下单时间
	protected long orderTime;
	//支付方式，1：货到付款，0：网易宝
	protected int payChannel;
	//可支付的方式列表
	protected int[] payChannelList;
	//是否使用红包，0 否 1是
	protected int useGiftMoney;
	//使用红包金额
	protected double giftMoneyInvolved;
	//红包余额
	protected double giftMoneyRemain	;
	//优惠券ID
	protected long couponId;
	//优惠代码
	protected String couponCode;
	//优惠券名字
	protected String couponName;
	//价格信息
	protected MobileOrderPriceVO priceInfo;
	//购买商品列表 同 购物车 用于未发货之前
	//protected List<MobileCartPoVO> cartPOList;
	//包裹列表和上面商品2选1 用于已经发货
	protected List<MobilePackageVO> packageList;
	//网易宝支付连接
	protected String payURL;
	//收货地址
	protected MobileConsigneeAddressVO shipAddress;
	//发票类型
	protected int invoiceType;
	//发票标题
	protected String invoiceTitle;
	
	private String platformInfo;
	
	private MobileShareVO shareGift;
	
	private String deliverURL;
	
	private List<String> allInfo;
	
	private int cancelFail;
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
	public long getCountDownSeconds() {
		return countDownSeconds;
	}
	public void setCountDownSeconds(long countDownSeconds) {
		this.countDownSeconds = countDownSeconds;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public long getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}
	public int getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(int payChannel) {
		this.payChannel = payChannel;
	}
	public int[] getPayChannelList() {
		return payChannelList;
	}
	public void setPayChannelList(int[] payChannelList) {
		this.payChannelList = payChannelList;
	}
	public int getUseGiftMoney() {
		return useGiftMoney;
	}
	public void setUseGiftMoney(int useGiftMoney) {
		this.useGiftMoney = useGiftMoney;
	}
	public double getGiftMoneyInvolved() {
		return giftMoneyInvolved;
	}
	public void setGiftMoneyInvolved(double giftMoneyInvolved) {
		this.giftMoneyInvolved = giftMoneyInvolved;
	}
	public double getGiftMoneyRemain() {
		return giftMoneyRemain;
	}
	public void setGiftMoneyRemain(double giftMoneyRemain) {
		this.giftMoneyRemain = giftMoneyRemain;
	}
	public long getCouponId() {
		return couponId;
	}
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public MobileOrderPriceVO getPriceInfo() {
		return priceInfo;
	}
	public void setPriceInfo(MobileOrderPriceVO priceInfo) {
		this.priceInfo = priceInfo;
	}
	
	public List<MobilePackageVO> getPackageList() {
		return packageList;
	}
	public void setPackageList(List<MobilePackageVO> packageList) {
		this.packageList = packageList;
	}
	public String getPayURL() {
		return payURL;
	}
	public void setPayURL(String payURL) {
		this.payURL = payURL;
	}
	public MobileConsigneeAddressVO getShipAddress() {
		return shipAddress;
	}
	public void setShipAddress(MobileConsigneeAddressVO shipAddress) {
		this.shipAddress = shipAddress;
	}
	public int getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoiceTitle() {
		return invoiceTitle;
	}
	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}
	public int getExpressType() {
		return expressType;
	}
	public void setExpressType(int expressType) {
		this.expressType = expressType;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public String getPlatformInfo() {
		return platformInfo;
	}
	public void setPlatformInfo(String platformInfo) {
		this.platformInfo = platformInfo;
	}
	public MobileShareVO getShareGift() {
		return shareGift;
	}
	public void setShareGift(MobileShareVO shareGift) {
		this.shareGift = shareGift;
	}
	public String getDeliverURL() {
		return deliverURL;
	}
	public void setDeliverURL(String deliverURL) {
		this.deliverURL = deliverURL;
	}
	public List<String> getAllInfo() {
		return allInfo;
	}
	public void setAllInfo(List<String> allInfo) {
		this.allInfo = allInfo;
	}
	public int getCancelFail() {
		return cancelFail;
	}
	public void setCancelFail(int cancelFail) {
		this.cancelFail = cancelFail;
	}

	
}
