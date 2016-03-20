package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.enums.SpSource;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormSource;

/**
 * 组单需要的参数,生成
 * 
 * @author dingmingliang
 * 
 */
public class OrderCalServiceGenOrderParam implements Serializable {

	private static final long serialVersionUID = 20140909L;

	/**
	 * 用户Id
	 */
	private long userId;

	/**
	 * 用户购买的Sku信息
	 */
	private List<SkuParam> skuParamList;

	/**
	 * 收货地址
	 */
	private ConsigneeAddressDTO caDTO;

	/**
	 * 订单来源
	 */
	private OrderFormSource orderFormSource;

	/**
	 * 订单的支付方式
	 */
	private OrderFormPayMethod orderFormPayMethod;

	/**
	 * 发票抬头
	 */
	private String invoiceTitle;

	/**
	 * 省份Id
	 */
	private int provinceId;

	/**
	 * 邮费
	 */
	private BigDecimal expPrice;

	/**
	 * 活动是否包邮
	 */
	private boolean isFreeExpPrice;

	/**
	 * 是否允许选择货到付款(根据产品的配送方式+快递+地址判断)
	 */
	private boolean canCODBySku = false;

	/**
	 * 红包抵用快递的金额
	 */
	private BigDecimal hbSExpPrice = BigDecimal.ZERO;

	/**
	 * 红包抵用订单的金额(包含快递费)
	 */
	private BigDecimal hbSOrderPrice = BigDecimal.ZERO;

	/**
	 * 订单渠道
	 */
	private SpSource spSource;

	/**
	 * 代客下单 运营agentId
	 */
	private long agentId;

	public BigDecimal getHbSExpPrice() {
		return hbSExpPrice;
	}

	public void setHbSExpPrice(BigDecimal hbSExpPrice) {
		this.hbSExpPrice = hbSExpPrice;
	}

	public BigDecimal getHbSOrderPrice() {
		return hbSOrderPrice;
	}

	public void setHbSOrderPrice(BigDecimal hbSOrderPrice) {
		this.hbSOrderPrice = hbSOrderPrice;
	}

	public BigDecimal getExpPrice() {
		return expPrice;
	}

	public void setExpPrice(BigDecimal expPrice) {
		this.expPrice = expPrice;
	}

	public boolean isCanCODBySku() {
		return canCODBySku;
	}

	public void setCanCODBySku(boolean canCODBySku) {
		this.canCODBySku = canCODBySku;
	}

	public boolean isFreeExpPrice() {
		return isFreeExpPrice;
	}

	public void setFreeExpPrice(boolean isFreeExpPrice) {
		this.isFreeExpPrice = isFreeExpPrice;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public OrderFormPayMethod getOrderFormPayMethod() {
		return orderFormPayMethod;
	}

	public void setOrderFormPayMethod(OrderFormPayMethod orderFormPayMethod) {
		this.orderFormPayMethod = orderFormPayMethod;
	}

	public OrderFormSource getOrderFormSource() {
		return orderFormSource;
	}

	public void setOrderFormSource(OrderFormSource orderFormSource) {
		this.orderFormSource = orderFormSource;
	}

	public ConsigneeAddressDTO getCaDTO() {
		return caDTO;
	}

	public void setCaDTO(ConsigneeAddressDTO caDTO) {
		this.caDTO = caDTO;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<SkuParam> getSkuParamList() {
		return skuParamList;
	}

	public void setSkuParamList(List<SkuParam> skuParamList) {
		this.skuParamList = skuParamList;
	}

	public SpSource getSpSource() {
		return spSource;
	}

	public void setSpSource(SpSource spSource) {
		this.spSource = spSource;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
