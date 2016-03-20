package com.xyl.mmall.mobile.facade.param;

import java.io.Serializable;

import com.xyl.mmall.mobile.facade.vo.MobileConsigneeAddressVO;

public class MobileOrderCommitAO extends MobileConsigneeAddressVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3917896830108530705L;


	private String hash;
	private int expressType;

	// 是否使用红包，0 否 1是
	protected int useGiftMoney;

	// 使用红包金额
	protected double giftMoneyInvolved;

	// 优惠券ID
	protected long couponId;


	// 支付方式，1：货到付款，0：网易宝
	protected int payChannel = -1;

	// 发票类型
	protected int invoiceType;

	// 发票标题
	protected String invoiceTitle;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
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

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public int getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(int payChannel) {
		this.payChannel = payChannel;
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


}
