package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileCartDetailVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5776354317897115842L;
	
	//购物车信息
	private MobileCartInfoVO cartInfo;
	//平台活动信息
	private List<String> platformInfo;
	//原总价(商品原总价)
	private double originTotalPrice;
	//最后总价格(商品折后总价)
	private double poTotalPrice;
	//专场列表
	private List<MobileCartPoVO>  cartPOList;
	//失效商品列表
	private List<MobileSkuVO> invalidList;
	public MobileCartInfoVO getCartInfo() {
		return cartInfo;
	}
	public void setCartInfo(MobileCartInfoVO cartInfo) {
		this.cartInfo = cartInfo;
	}
	public List<String> getPlatformInfo() {
		return platformInfo;
	}
	public void setPlatformInfo(String platformInfo) {
		List<String> ps = new ArrayList<String>();
		ps.add(platformInfo);
		this.platformInfo = ps;
	}
	public void setPlatformInfo(List<String> platformInfo) {
		this.platformInfo = platformInfo;
	}
	public double getOriginTotalPrice() {
		return originTotalPrice;
	}
	public void setOriginTotalPrice(double originTotalPrice) {
		this.originTotalPrice = originTotalPrice;
	}
	public double getPoTotalPrice() {
		return poTotalPrice;
	}
	public void setPoTotalPrice(double poTotalPrice) {
		this.poTotalPrice = poTotalPrice;
	}

	public List<MobileCartPoVO> getCartPOList() {
		return cartPOList;
	}
	public void setCartPOList(List<MobileCartPoVO> cartPOList) {
		this.cartPOList = cartPOList;
	}
	public List<MobileSkuVO> getInvalidList() {
		return invalidList;
	}
	public void setInvalidList(List<MobileSkuVO> invalidList) {
		this.invalidList = invalidList;
	}

	
}
