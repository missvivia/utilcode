package com.xyl.mmall.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 计算订单快递价格的参数
 * 
 * @author dingmingliang
 * 
 */
public class OmsExpPriceParam implements Serializable {

	private static final long serialVersionUID = 20140909L;

	/**
	 * 购买的产品列表
	 */
	private List<OmsSkuParam> skuParamList;

	/**
	 * 收货地址
	 */
	private OmsConsigneeAddressParam ca;

	/**
	 * 站点id(即用户所在省份ID)
	 */
	private long provinceId;

	/**
	 * 是否是货到付款
	 */
	private boolean isCOD;
	
	/**
	 * 收件地址是否是偏远地址
	 */
	private boolean acceptAddressRemote;
	
	/**
	 * 选择的物流公司code
	 */
	private String expressCompanyCodeSelected;
	
	/**
	 * 偏远地区价格
	 */
	private BigDecimal remoteAreaPrice;

	public List<OmsSkuParam> getSkuParamList() {
		return skuParamList;
	}

	public void setSkuParamList(List<OmsSkuParam> skuParamList) {
		this.skuParamList = skuParamList;
	}

	public OmsConsigneeAddressParam getCa() {
		return ca;
	}

	public void setCa(OmsConsigneeAddressParam ca) {
		this.ca = ca;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public boolean isCOD() {
		return isCOD;
	}

	public void setCOD(boolean isCOD) {
		this.isCOD = isCOD;
	}

	public boolean isAcceptAddressRemote() {
		return acceptAddressRemote;
	}

	public void setAcceptAddressRemote(boolean acceptAddressRemote) {
		this.acceptAddressRemote = acceptAddressRemote;
	}

	public String getExpressCompanyCodeSelected() {
		return expressCompanyCodeSelected;
	}

	public void setExpressCompanyCodeSelected(String expressCompanyCodeSelected) {
		this.expressCompanyCodeSelected = expressCompanyCodeSelected;
	}

	public BigDecimal getRemoteAreaPrice() {
		return remoteAreaPrice;
	}

	public void setRemoteAreaPrice(BigDecimal remoteAreaPrice) {
		this.remoteAreaPrice = remoteAreaPrice;
	}

}
