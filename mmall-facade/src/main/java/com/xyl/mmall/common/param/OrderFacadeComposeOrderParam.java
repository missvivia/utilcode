package com.xyl.mmall.common.param;

import java.math.BigDecimal;

import com.xyl.mmall.framework.enums.SpSource;
import com.xyl.mmall.order.enums.OrderFormSource;

/**
 * 组单参数
 * 
 * @author dingmingliang
 * 
 */
public class OrderFacadeComposeOrderParam {

	/**
	 * 用户Id
	 */
	private long userId;

	/**
	 * 要组单的购物车参数
	 */
	private String cartIds;

	/**
	 * 购物车skuId对应的价格， 格式：例如 11|11.12,12|23.23
	 */
	private String skusPrice;

	/**
	 * 收货地址Id
	 */
	private long caId;

	/**
	 * 省份Id ---- 实际值是区Id
	 */
	private int provinceId;

	/**
	 * 城市Id
	 */
	private int cityId;

	/**
	 * 用户选择的优惠券Id
	 */
	private long userCouponId;

	/**
	 * 支付方式
	 */
	private int payMethodInt = -1;

	/**
	 * 订单来源
	 */
	private OrderFormSource source;

	/**
	 * 发票抬头
	 */
	private String invoiceTitle;

	/**
	 * 红包使用金额(null表示不使用,-1表示自动使用)
	 */
	private BigDecimal hbCash;

	/**
	 * 订单渠道
	 */
	private SpSource spSource;

	/**
	 * 代客下单 运营agentId
	 */
	private long agentId;

	public BigDecimal getHbCash() {
		return hbCash;
	}

	/**
	 * 红包使用金额(null表示不使用,-1表示自动使用)
	 * 
	 * @param hbCash
	 */
	public void setHbCash(BigDecimal hbCash) {
		this.hbCash = hbCash;
	}

	public long getUserCouponId() {
		return userCouponId;
	}

	public void setUserCouponId(long userCouponId) {
		this.userCouponId = userCouponId;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCartIds() {
		return cartIds;
	}

	public void setCartIds(String cartIds) {
		this.cartIds = cartIds;
	}

	public long getCaId() {
		return caId;
	}

	public void setCaId(long caId) {
		this.caId = caId;
	}

	public int getPayMethodInt() {
		return payMethodInt;
	}

	public void setPayMethodInt(int payMethodInt) {
		this.payMethodInt = payMethodInt;
	}

	public OrderFormSource getSource() {
		return source;
	}

	public void setSource(OrderFormSource source) {
		this.source = source;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getSkusPrice() {
		return skusPrice;
	}

	public void setSkusPrice(String skusPrice) {
		this.skusPrice = skusPrice;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public SpSource getSpSource() {
		return spSource;
	}

	public void setSpSource(SpSource spSource) {
		this.spSource = spSource;
	}

}
