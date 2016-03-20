package com.xyl.mmall.itemcenter.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年8月21日下午1:44:03
 */
public class ProductPriceParam {
	
	private BigDecimal sprice;
	
	private BigDecimal eprice;
	
	/**
	 * 按price排序
	 */
	private boolean isAsc = true;
	
	/**
	 * 商品Ids
	 */
	private List<Long>skuIds;
	
	/**
	 * 商品状态  4上架
	 */
	private int status = -1;
	
	public ProductPriceParam(){
		
	}

	public ProductPriceParam(BigDecimal sprice, BigDecimal eprice, boolean isAsc,int status) {
		this.sprice = sprice;
		this.eprice = eprice;
		this.isAsc = isAsc;
		this.status = status;
	}

	public BigDecimal getSprice() {
		return sprice;
	}

	public void setSprice(BigDecimal sprice) {
		this.sprice = sprice;
	}

	public BigDecimal getEprice() {
		return eprice;
	}

	public void setEprice(BigDecimal eprice) {
		this.eprice = eprice;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

	public List<Long> getSkuIds() {
		return skuIds;
	}

	public void setSkuIds(List<Long> skuIds) {
		this.skuIds = skuIds;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	

}
