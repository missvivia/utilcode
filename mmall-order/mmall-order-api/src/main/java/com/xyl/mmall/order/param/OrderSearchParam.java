package com.xyl.mmall.order.param;

import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;

public class OrderSearchParam extends DDBParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5288070667260317779L;
	
	/**
	 * 订单号
	 */
	private long orderId;
	
	/**
	 * 订单状态
	 */
	private int orderStatus = -1;
	
	/**
	 * 支付方式
	 */
	private int payMethod;
	
	/**
	 * 支付状态
	 */
	private int payStatus = -1;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 卖家账号
	 */
	private String businessAccount;
	
	/**
	 * 下单时间搜索：起始时间
	 */
	private long stime;
	
	/**
	 * 下单时间搜索：结束时间
	 */
	private long etime;
	
	/**
	 * 商家Id
	 */
	private long businessId;
	
	/**
	 * 用户Id
	 */
	private long userId;
	
	/**
	 * 搜索条件类型  5 订单Id 6 用户名 7 订单时间 8.卖家账号
	 */
	private int queryType;
	
	/**
	 * 订单是否可见
	 */
	private boolean isVisible;
	
	
	/**
	 * 是否使用优惠券
	 */
	private boolean isUseCoupon;
	
	/**
	 * 站点Id
	 */
	private long siteId;
	
	/**
	 * 用户Id List
	 */
	private List<Long>userIdList;
	
	/**
	 * 站点区域list
	 */
	private List<Long>siteAreaList;
	
	/**
	 * 商家Id List
	 */
	private List<Long> businessIdList;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public List<Long> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}

	public List<Long> getSiteAreaList() {
		return siteAreaList;
	}

	public void setSiteAreaList(List<Long> siteAreaList) {
		this.siteAreaList = siteAreaList;
	}

	public boolean isUseCoupon() {
		return isUseCoupon;
	}

	public void setUseCoupon(boolean isUseCoupon) {
		this.isUseCoupon = isUseCoupon;
	}

	public String getBusinessAccount() {
		return businessAccount;
	}

	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
	}

	public List<Long> getBusinessIdList() {
		return businessIdList;
	}

	public void setBusinessIdList(List<Long> businessIdList) {
		this.businessIdList = businessIdList;
	}

	
	
}
