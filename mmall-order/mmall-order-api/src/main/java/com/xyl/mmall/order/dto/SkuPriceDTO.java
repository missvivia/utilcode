package com.xyl.mmall.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SkuPriceDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8255863259071911576L;

	/** 最小数目. */
	private int prodMinNumber;

	/** 最大数目. */
	private int prodMaxNumber;

	/** 价格. */
	private BigDecimal prodPrice;

	public int getProdMinNumber() {
		return prodMinNumber;
	}

	public void setProdMinNumber(int prodMinNumber) {
		this.prodMinNumber = prodMinNumber;
	}

	public int getProdMaxNumber() {
		return prodMaxNumber;
	}

	public void setProdMaxNumber(int prodMaxNumber) {
		this.prodMaxNumber = prodMaxNumber;
	}

	public BigDecimal getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(BigDecimal prodPrice) {
		this.prodPrice = prodPrice;
	}
	
	
}
