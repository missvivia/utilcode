package com.xyl.mmall.mobile.web.vo.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.order.dto.DeprecatedReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.param.ReturnBankCardParam;
import com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam.PriceParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月30日 下午2:27:13
 *
 */
@Deprecated
public class DeprecatedReturnPriceVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5809974772958854416L;

	private ReturnPrice returnPrice = new ReturnPrice();
	
	private List<ReturnWay> returnWay = new ArrayList<ReturnWay>();
	
	private List<ReturnWay> returnCoupon = new ArrayList<ReturnWay>();
	
	public void fillWithPriceParam(PriceParam priceParam) {
		if(null == priceParam) {
			return;
		}
		BigDecimal price = null;;
// 1. 填充returnPrice
		//商品总金额
		price = priceParam.getGoodsTotalRPrice();
		if(null != price) {
			returnPrice.setGoodsTotalRPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
		//扣除的运费
		price = priceParam.getExpPrice();
		if(null != price) {
			returnPrice.setExpPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
		//扣除优惠券费用
		price = priceParam.getCouponYPrice();
		if(null != price) {
			returnPrice.setCouponPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
		//扣除的活动费用
		price = priceParam.getHdYPrice();
		if(null != price) {
			returnPrice.setPromotion(price.setScale(2, RoundingMode.HALF_UP));
		}
		//退款金额
		price = priceParam.getPayedCashPrice();
		if(null != price) {
			returnPrice.setFinalRetunPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
// 2. 填充returnWay
		RefundType rt = priceParam.getRefundType();
		if(null != rt) {
			ReturnWay rw = new ReturnWay(rt.getDesc(), priceParam.getPayedCashPrice(), null);
			if(rt == RefundType.BANKCARD) {
				ReturnBankCardParam bankCard = priceParam.getRetBankCard();
				if(null != bankCard) {
					rw.setAccount(bankCard.getBankCardNO());
				}
			}
			returnWay.add(rw);
		}
// 3. 填充returnCoupon
		ReturnWay couponRW = new ReturnWay();
		couponRW.setReturnType("优惠券（回寄运费补帖）");
		couponRW.setSum(priceParam.getExpSubsidyPrice());
		returnCoupon.add(couponRW);
	}
	
	public void fillWithReturnForm(DeprecatedReturnFormDTO retFormDTO, DeprecatedReturnCODBankCardInfoDTO bankCard) {
		BigDecimal price = null;
// 1. 填充returnPrice
		//商品总金额
		price = retFormDTO.getGoodsTotalRPrice();
		if(null != price) {
			returnPrice.setGoodsTotalRPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
		//扣除的运费
		price = retFormDTO.getExpPrice();
		if(null != price) {
			returnPrice.setExpPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
		//扣除优惠券费用
		price = retFormDTO.getCouponYPrice();
		if(null != price) {
			returnPrice.setCouponPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
		//扣除的活动费用
		price = retFormDTO.getHdYPrice();
		if(null != price) {
			returnPrice.setPromotion(price.setScale(2, RoundingMode.HALF_UP));
		}
		//退款金额
		price = retFormDTO.getPayedCashPrice();
		if(null != price) {
			returnPrice.setFinalRetunPrice(price.setScale(2, RoundingMode.HALF_UP));
		}
		
// 2. 填充returnWay
		RefundType rt = retFormDTO.getRefundType();
		if(null != rt) {
			ReturnWay rw = new ReturnWay(rt.getDesc(), retFormDTO.getPayedCashPrice(), null);
			if(rt == RefundType.BANKCARD && null != bankCard) {
				rw.setAccount(bankCard.getBankCardNO());
			}
			returnWay.add(rw);
		}
// 3. 填充returnCoupon
		ReturnWay couponRW = new ReturnWay();
		couponRW.setReturnType("优惠券（回寄运费补帖）");
		couponRW.setSum(retFormDTO.getExpSubsidyPrice());
		returnCoupon.add(couponRW);
	}
	
	public ReturnPrice getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(ReturnPrice returnPrice) {
		this.returnPrice = returnPrice;
	}

	public List<ReturnWay> getReturnWay() {
		return returnWay;
	}

	public void setReturnWay(List<ReturnWay> returnWay) {
		this.returnWay = returnWay;
	}

	public List<ReturnWay> getReturnCoupon() {
		return returnCoupon;
	}

	public void setReturnCoupon(List<ReturnWay> returnCoupon) {
		this.returnCoupon = returnCoupon;
	}

	public static class ReturnPrice implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3609874708432888401L;
		
		//商品总金额
		private BigDecimal goodsTotalRPrice = BigDecimal.ZERO;
		
		//扣除的运费
		private BigDecimal expPrice = BigDecimal.ZERO;
		
		//扣除优惠券费用
		private BigDecimal couponPrice = BigDecimal.ZERO;
		
		//扣除的活动费用
		private BigDecimal promotion = BigDecimal.ZERO;

		//退款金额
		private BigDecimal finalRetunPrice = BigDecimal.ZERO;
	
		public ReturnPrice() {
		}

		public ReturnPrice(BigDecimal goodsTotalRPrice, BigDecimal expPrice, BigDecimal couponPrice, 
				BigDecimal promotion, BigDecimal finalRetunPrice) {
			this.goodsTotalRPrice = goodsTotalRPrice;
			this.expPrice = expPrice;
			this.couponPrice = couponPrice;
			this.promotion = promotion;
			this.finalRetunPrice = finalRetunPrice;
		}

		public BigDecimal getExpPrice() {
			return expPrice;
		}
	
		public void setExpPrice(BigDecimal expPrice) {
			this.expPrice = expPrice;
		}
	
		public BigDecimal getCouponPrice() {
			return couponPrice;
		}
	
		public void setCouponPrice(BigDecimal couponPrice) {
			this.couponPrice = couponPrice;
		}
	
		public BigDecimal getPromotion() {
			return promotion;
		}
	
		public void setPromotion(BigDecimal promotion) {
			this.promotion = promotion;
		}
	
		public BigDecimal getGoodsTotalRPrice() {
			return goodsTotalRPrice;
		}
	
		public void setGoodsTotalRPrice(BigDecimal goodsTotalRPrice) {
			this.goodsTotalRPrice = goodsTotalRPrice;
		}
	
		public BigDecimal getFinalRetunPrice() {
			return finalRetunPrice;
		}
	
		public void setFinalRetunPrice(BigDecimal hbPrice) {
			this.finalRetunPrice = hbPrice;
		}
	}
	
	public static class ReturnWay implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5451513398332815557L;
		
		private String returnType;
		
		private BigDecimal sum;
		
		private String account;

		public ReturnWay() {
		}
		
		public ReturnWay(String returnType, BigDecimal sum, String account) {
			this.returnType = returnType;
			if(null != sum) {
				this.sum = sum.setScale(2, RoundingMode.HALF_UP);
			}
			this.account = account;
		}

		public String getReturnType() {
			return returnType;
		}

		public void setReturnType(String returnType) {
			this.returnType = returnType;
		}

		public BigDecimal getSum() {
			return sum;
		}

		public void setSum(BigDecimal sum) {
			this.sum = sum;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}
		
	}

}
