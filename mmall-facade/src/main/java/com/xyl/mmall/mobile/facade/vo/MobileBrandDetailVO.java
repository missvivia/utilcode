package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 第一级类
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileBrandDetailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4221593642995534894L;

	private MobileBrandVO brand;
	
	private List<MobilePOGroupVO> poGroupList;
	
	private List<MobileShopVO> shopList;
	
	private List<MobileAreaListVO> areaList;

	public MobileBrandVO getBrand() {
		return brand;
	}

	public void setBrand(MobileBrandVO brand) {
		this.brand = brand;
	}

	public List<MobileShopVO> getShopList() {
		return shopList;
	}

	public void setShopList(List<MobileShopVO> shopList) {
		this.shopList = shopList;
	}

	public List<MobileAreaListVO> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<MobileAreaListVO> areaList) {
		this.areaList = areaList;
	}

	public List<MobilePOGroupVO> getPoGroupList() {
		return poGroupList;
	} 

	public void setPoGroupList(List<MobilePOGroupVO> poGroupList) {
		this.poGroupList = poGroupList;
	}
	
	
}
