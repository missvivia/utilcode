package com.xyl.mmall.mainsite.vo.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月30日 下午2:27:13
 *
 */
public class ReturnPriceVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5809974772958854416L;

	private ReturnPrice returnPrice = new ReturnPrice();
	
	/**
	 * 退款方式：红包 + 原路/网易宝/到付银行卡
	 */
	private List<ReturnWay> returnWay = new ArrayList<ReturnWay>();
	
	/**
	 * 回寄运费补贴红包
	 */
	private List<ReturnWay> returnExpCompensation = new ArrayList<ReturnWay>();
	
	/**
	 * 订单的优惠券（整个订单退货时退回优惠券）
	 */
	private List<ReturnCoupon> returnCoupon = new ArrayList<ReturnCoupon>();
	
	public void fillWithPriceParam(_ReturnPackagePriceParam priceParam, RefundType rt, 
			String bankCardNOInfo, List<ReturnCoupon> returnCoupon) {
		if(null == priceParam || null == rt) {
			return;
		}
// 1. 填充returnPrice
		setReturnPrice(new ReturnPrice(priceParam.getReturnTotalPrice(), priceParam.getReturnHbPrice(), priceParam.getReturnCashPrice()));		
// 2. 填充returnWay
		String bankCardNO = null;
		if(rt == RefundType.BANKCARD) {
			bankCardNO = bankCardNOInfo;
		}
		returnWay.add(new ReturnWay(rt.getDesc(), priceParam.getReturnCashPrice(), bankCardNO));
		returnWay.add(new ReturnWay("红包", priceParam.getReturnHbPrice(), null));
// 3. 填充returnExpCompensation
		returnExpCompensation.add(new ReturnWay("红包", priceParam.getExpSubsidyPrice(), null));
// 4. 填充returnCoupon
		setReturnCoupon(returnCoupon);
	}
	
	public void fillWithReturnPackage(ReturnPackageDTO retPkgDTO, boolean applySituation, 
			String bankCardNOInfo, List<ReturnCoupon> returnCoupon) {
		if(null == retPkgDTO) {
			return;
		}
// 1. 填充returnPrice
		if(applySituation) {
			setReturnPrice(new ReturnPrice(retPkgDTO.getApplyedReturnTotalPrice(), 
					retPkgDTO.getApplyedReturnHbPrice(), retPkgDTO.getApplyedReturnCashPrice()));
		} else {
			setReturnPrice(new ReturnPrice(retPkgDTO.getPayedTotalPriceToUser(), 
					retPkgDTO.getPayedHbPriceToUser(), retPkgDTO.getPayedCashPriceToUser()));
		}		
// 2. 填充returnWay
		String bankCardNO = null;
		RefundType rt = retPkgDTO.getRefundType();
		if(rt == RefundType.BANKCARD) {
			bankCardNO = bankCardNOInfo;
		}
		if(applySituation) {
			returnWay.add(new ReturnWay(rt.getDesc(), retPkgDTO.getApplyedReturnCashPrice(), bankCardNO));
			returnWay.add(new ReturnWay("红包", retPkgDTO.getApplyedReturnHbPrice(), null));
		} else {
			returnWay.add(new ReturnWay(rt.getDesc(), retPkgDTO.getPayedCashPriceToUser(), bankCardNO));
			returnWay.add(new ReturnWay("红包", retPkgDTO.getPayedHbPriceToUser(), null));
		}
// 3. 填充returnExpCompensation
		returnExpCompensation.add(new ReturnWay("红包", retPkgDTO.getExpSubsidyPrice(), null));
// 4. 填充showReturnCoupon + returnCoupon
		setReturnCoupon(returnCoupon);
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

	public List<ReturnWay> getReturnExpCompensation() {
		return returnExpCompensation;
	}

	public void setReturnExpCompensation(List<ReturnWay> returnExpCompensation) {
		this.returnExpCompensation = returnExpCompensation;
	}

	public List<ReturnCoupon> getReturnCoupon() {
		return returnCoupon;
	}

	public void setReturnCoupon(List<ReturnCoupon> returnCoupon) {
		this.returnCoupon = returnCoupon;
	}

	public static class ReturnPrice implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3609874708432888401L;
		
		/**
		 * 2.2 实际退款商品金额 = 退给用户的红包退款金额 + 退给用户的商品退款金额（原路/网易宝/到付银行卡）
		 */	
		// @AnnonOfField(desc = "实际退款商品金额")
		private BigDecimal payedTotalPriceToUser = BigDecimal.ZERO;
		
		// @AnnonOfField(desc = "实际退给用户的红包退款金额")
		private BigDecimal payedHbPriceToUser = BigDecimal.ZERO;
		
		// @AnnonOfField(desc = "实际退给用户的商品退款金额（原路/网易宝/到付银行卡）")
		private BigDecimal payedCashPriceToUser = BigDecimal.ZERO;
	
		public ReturnPrice() {
		}

		public ReturnPrice(BigDecimal payedTotalPriceToUser, BigDecimal payedHbPriceToUser, BigDecimal payedCashPriceToUser) {
			if(null != payedTotalPriceToUser) {
				this.payedTotalPriceToUser = payedTotalPriceToUser.setScale(2, RoundingMode.HALF_UP);
			}
			if(null != payedHbPriceToUser) {
				this.payedHbPriceToUser = payedHbPriceToUser.setScale(2, RoundingMode.HALF_UP);
			}
			if(null != payedCashPriceToUser) {
				this.payedCashPriceToUser = payedCashPriceToUser.setScale(2, RoundingMode.HALF_UP);
			}
		}

		public BigDecimal getPayedTotalPriceToUser() {
			return payedTotalPriceToUser;
		}

		public void setPayedTotalPriceToUser(BigDecimal payedTotalPriceToUser) {
			this.payedTotalPriceToUser = payedTotalPriceToUser;
		}

		public BigDecimal getPayedHbPriceToUser() {
			return payedHbPriceToUser;
		}

		public void setPayedHbPriceToUser(BigDecimal payedHbPriceToUser) {
			this.payedHbPriceToUser = payedHbPriceToUser;
		}

		public BigDecimal getPayedCashPriceToUser() {
			return payedCashPriceToUser;
		}

		public void setPayedCashPriceToUser(BigDecimal payedCashPriceToUser) {
			this.payedCashPriceToUser = payedCashPriceToUser;
		}

	}
	
	public static class ReturnWay implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5451513398332815557L;
		
		private String returnType;
		
		private BigDecimal sum = BigDecimal.ZERO;
		
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

	public static class ReturnCoupon implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5067748751277995709L;
		
		private boolean returned = false;	// 定时器是否已经返回
		
		private String couponId;
		
		private String couponCode;

		public ReturnCoupon fillWithCouponOrder(CouponOrder couponOrder, Coupon coupon, boolean recycled) {
			if(null != couponOrder) {
				this.couponId = String.valueOf(couponOrder.getId());
				if(null != coupon) {
					this.couponCode = coupon.getName();
				} else {
					this.couponCode = couponOrder.getCouponCode();
				}
				this.returned = recycled;
			}
			return this;
		}
		
		public boolean isReturned() {
			return returned;
		}

		public void setReturned(boolean returned) {
			this.returned = returned;
		}

		public String getCouponCode() {
			return couponCode;
		}

		public void setCouponCode(String couponName) {
			this.couponCode = couponName;
		}

		public String getCouponId() {
			return couponId;
		}

		public void setCouponId(String couponId) {
			this.couponId = couponId;
		}
		
	}
	
}
