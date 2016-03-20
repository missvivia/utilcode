package com.xyl.mmall.mobile.facade.param;

public class MobilePayOrderParam {
	private long userId;

	private int areaCode;

	private String cartIds;

	private int os;

	private long couponId;

	private String invoiceTitle;

	private boolean needInvoice;

	private boolean useGift;

	private int payMethodInt;

	private long addressId;

	private String hash;

	public MobilePayOrderParam() {

	}

	public MobilePayOrderParam(long userId, int areaId, String cartIds, int os, long addressId, String hash, int useGiftMoney) {
		this.userId = userId;
		this.areaCode = areaId;
		this.cartIds = cartIds;
		this.os = os;
		this.payMethodInt = -1;
		this.addressId = addressId;
		this.hash = hash;
		this.useGift = (useGiftMoney == 1);
	}

	public MobilePayOrderParam(long userId, int areaId, String cartIds, int os, String hash, MobileOrderCommitAO ao) {
		this.userId = userId;
		this.areaCode = areaId;
		this.cartIds = cartIds;
		this.os = os;
		this.hash = hash;
		if (ao.getInvoiceType() == 1)
			this.invoiceTitle = "个人";
		if (ao.getInvoiceType() == 2)
			this.invoiceTitle = ao.getInvoiceTitle();
		this.useGift = ao.getUseGiftMoney() == 1;
		this.couponId = ao.getCouponId();
		this.payMethodInt = ao.getPayChannel();
		this.addressId = ao.getAddressId();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(int areaCode) {
		this.areaCode = areaCode;
	}

	public String getCartIds() {
		return cartIds;
	}

	public void setCartIds(String cartIds) {
		this.cartIds = cartIds;
	}

	public int getOs() {
		return os;
	}

	public void setOs(int os) {
		this.os = os;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public boolean isNeedInvoice() {
		return needInvoice;
	}

	public void setNeedInvoice(boolean needInvoice) {
		this.needInvoice = needInvoice;
	}

	public boolean isUseGift() {
		return useGift;
	}

	public void setUseGift(boolean useGift) {
		this.useGift = useGift;
	}

	public int getPayMethodInt() {
		return payMethodInt;
	}

	public void setPayMethodInt(int payMethodInt) {
		this.payMethodInt = payMethodInt;
	}

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}
