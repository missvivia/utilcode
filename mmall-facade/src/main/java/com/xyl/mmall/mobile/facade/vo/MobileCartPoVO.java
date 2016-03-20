package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileCartPoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5776354317897115842L;
	//包括一般PO 和 聯合 PO
	private long cartPOId;
	//购物车列表 PO 名字
	private String cartPOName;
	//购物车 PO活动信息
	private List<String> cartPOInfo;
	//Sku对象
	private List<MobileSkuVO> skulist;
	//0:否1:已经失效(超时)
	private int isValid;
	public long getCartPOId() {
		return cartPOId;
	}
	public void setCartPOId(long cartPOId) {
		this.cartPOId = cartPOId;
	}
	public String getCartPOName() {
		return cartPOName;
	}
	public void setCartPOName(String cartPOName) {
		this.cartPOName = cartPOName;
	}
	public List<String> getCartPOInfo() {
		return cartPOInfo;
	}
	public void setCartPOInfo(List<String> cartPOInfo) {
		this.cartPOInfo = cartPOInfo;
	}
	public List<MobileSkuVO> getSkulist() {
		return skulist;
	}
	public void setSkulist(List<MobileSkuVO> skulist) {
		this.skulist = skulist;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	
	
	
	
}
