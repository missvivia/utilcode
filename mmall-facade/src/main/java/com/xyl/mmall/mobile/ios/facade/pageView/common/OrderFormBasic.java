package com.xyl.mmall.mobile.ios.facade.pageView.common;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

public class OrderFormBasic {

	@AnnonOfField(desc = "购物车商品零售总价-结算价(商品总额)")
	private BigDecimal cartRPrice = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "购物车商品零售总价-原价")
	private BigDecimal cartOriRPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-原价")
	@JsonIgnore
	private BigDecimal expOriPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-用户支付价")
	@JsonIgnore
	private BigDecimal expUserPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "快递费-系统垫付金额")
	@JsonIgnore
	private BigDecimal expSysPayPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "活动优惠的差额(总和)")
	@JsonIgnore
	private BigDecimal hdSPrice = BigDecimal.ZERO;

	@AnnonOfField(desc = "优惠券优惠的差额(总和)")
	private BigDecimal couponSPrice = BigDecimal.ZERO;
	public BigDecimal getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(BigDecimal totalCash) {
		this.totalCash = totalCash;
	}

	public BigDecimal getRealCash() {
		return realCash;
	}

	public void setRealCash(BigDecimal realCash) {
		this.realCash = realCash;
	}

	/**
	 * 用户需要支付的总金额(红包+现金)
	 */
	@JsonIgnore
	private BigDecimal totalCash = BigDecimal.ZERO;

	/**
	 * 用户需要支付的金额(现金:允许EPAY或者COD)
	 */
	private BigDecimal realCash = BigDecimal.ZERO;	

	public BigDecimal getCartRPrice() {
		return cartRPrice;
	}

	public void setCartRPrice(BigDecimal cartRPrice) {
		this.cartRPrice = cartRPrice;
	}

	public BigDecimal getCartOriRPrice() {
		return cartOriRPrice;
	}

	public void setCartOriRPrice(BigDecimal cartOriRPrice) {
		this.cartOriRPrice = cartOriRPrice;
	}

	public BigDecimal getExpOriPrice() {
		return expOriPrice;
	}

	public void setExpOriPrice(BigDecimal expOriPrice) {
		this.expOriPrice = expOriPrice;
	}

	public BigDecimal getExpUserPrice() {
		return expUserPrice;
	}

	public void setExpUserPrice(BigDecimal expUserPrice) {
		this.expUserPrice = expUserPrice;
	}

	public BigDecimal getExpSysPayPrice() {
		return expSysPayPrice;
	}

	public void setExpSysPayPrice(BigDecimal expSysPayPrice) {
		this.expSysPayPrice = expSysPayPrice;
	}

	public BigDecimal getHdSPrice() {
		return hdSPrice;
	}

	public void setHdSPrice(BigDecimal hdSPrice) {
		this.hdSPrice = hdSPrice;
	}

	public BigDecimal getCouponSPrice() {
		return couponSPrice;
	}

	public void setCouponSPrice(BigDecimal couponSPrice) {
		this.couponSPrice = couponSPrice;
	}
	
	
}
