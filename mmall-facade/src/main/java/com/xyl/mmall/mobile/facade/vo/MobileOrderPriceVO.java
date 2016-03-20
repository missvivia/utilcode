package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 第一級類
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileOrderPriceVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8762600962976722522L;
	//原价
	private double originPrice	;
	//优惠的价格
	private double reducedPrice;
	//红包价格
	private double giftPrice;
	//红包价格
	private double couponPrice;
	//活动优惠
	private double hdPrice;
	//运费
	private double carriageFee;
	//总价
	private double finalprice;
	
	private double giftCanUse;
	public double getOriginPrice() {
		return originPrice;
	}
	public void setOriginPrice(double originPrice) {
		this.originPrice = originPrice;
	}
	public double getReducedPrice() {
		return reducedPrice;
	}
	public void setReducedPrice(double reducedPrice) {
		this.reducedPrice = reducedPrice;
	}
	public double getCarriageFee() {
		return carriageFee;
	}
	public void setCarriageFee(double carriageFee) {
		this.carriageFee = carriageFee;
	}
	public double getFinalprice() {
		return finalprice;
	}
	public void setFinalprice(double finalprice) {
		this.finalprice = finalprice;
	}
	public double getGiftPrice() {
		return giftPrice;
	}
	public void setGiftPrice(double giftPrice) {
		this.giftPrice = giftPrice;
	}
	public double getCouponPrice() {
		return couponPrice;
	}
	public void setCouponPrice(double couponPrice) {
		this.couponPrice = couponPrice;
	}
	public double getHdPrice() {
		return hdPrice;
	}
	public void setHdPrice(double hdPrice) {
		this.hdPrice = hdPrice;
	}
	public double getGiftCanUse() {
		return giftCanUse;
	}
	public void setGiftCanUse(double giftCanUse) {
		this.giftCanUse = giftCanUse;
	}
	
	
}
