package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.order.enums.RefundType;

/**
 * 申请退货时提供的参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月20日 下午1:41:19
 *
 */
@Deprecated
public class DeprecatedReturnFormApplyParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4168527426990063036L;

	// 退款参数
	private PriceParam priceParam = new PriceParam();
	
	// 退货的OrderSku列表
	private List<DeprecatedReturnOrderSkuParam> retOrderSkuParamList = new ArrayList<DeprecatedReturnOrderSkuParam>();;
	
	public PriceParam getPriceParam() {
		return priceParam;
	}

	public void setPriceParam(PriceParam priceParam) {
		this.priceParam = priceParam;
	}

	public List<DeprecatedReturnOrderSkuParam> getRetOrderSkuParamList() {
		return retOrderSkuParamList;
	}

	public void setRetOrderSkuParamList(List<DeprecatedReturnOrderSkuParam> retOrderSkuParamList) {
		this.retOrderSkuParamList = retOrderSkuParamList;
	}

	@Deprecated
	public static class PriceParam implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2997132427042001257L;
		// 退货订单的原始信息明细(主站显示给用户看的数据)	
		//(desc = "商品总金额")
		private BigDecimal goodsTotalRPrice = BigDecimal.ZERO;
		
		//(desc = "扣除活动优惠")
		private BigDecimal hdYPrice = BigDecimal.ZERO;

		//(desc = "扣除优惠券抵用")
		private BigDecimal couponYPrice = BigDecimal.ZERO;
		
		//(desc = "扣除运费")
		private BigDecimal expPrice = BigDecimal.ZERO;
	//---------------

	// 实际要退款明细的组成(运维系统里用到的实际金额)
		//(desc = "实付金额")
		private BigDecimal payedCashPrice = BigDecimal.ZERO;
		
//		// 退款金额 hbPrice + returnCashPrice
//		//(desc = "退款金额 - 红包抵用金额 (需求未知)")
//		private BigDecimal hbPrice = BigDecimal.ZERO;
//		
//		//(desc = "退款金额 - 在线支付金额 (需求未知)")
//		private BigDecimal returnCashPrice = BigDecimal.ZERO;
		
		//(desc = "退货补贴-快递费")
		private BigDecimal expSubsidyPrice = BigDecimal.TEN;
	//---------------
		
		// 退款方式
		private RefundType refundType;
		
		// 货到付款退货场景下的银行卡信息(refundType=BANKCARD时有效)
		private ReturnBankCardParam retBankCard;
		
		// 是否回收优惠券+红包
		private boolean recycle = false;

		public BigDecimal getGoodsTotalRPrice() {
			return goodsTotalRPrice;
		}

		public void setGoodsTotalRPrice(BigDecimal goodsTotalRPrice) {
			this.goodsTotalRPrice = goodsTotalRPrice;
		}

		public BigDecimal getHdYPrice() {
			return hdYPrice;
		}

		public void setHdYPrice(BigDecimal hdYPrice) {
			this.hdYPrice = hdYPrice;
		}

		public BigDecimal getCouponYPrice() {
			return couponYPrice;
		}

		public void setCouponYPrice(BigDecimal couponYPrice) {
			this.couponYPrice = couponYPrice;
		}

		public BigDecimal getExpPrice() {
			return expPrice;
		}

		public void setExpPrice(BigDecimal expPrice) {
			this.expPrice = expPrice;
		}

		public BigDecimal getPayedCashPrice() {
			return payedCashPrice;
		}

		public void setPayedCashPrice(BigDecimal payedCashPrice) {
			this.payedCashPrice = payedCashPrice;
		}

//		public BigDecimal getHbPrice() {
//			return hbPrice;
//		}
//
//		public void setHbPrice(BigDecimal hbPrice) {
//			this.hbPrice = hbPrice;
//		}
//
//		public BigDecimal getReturnCashPrice() {
//			return returnCashPrice;
//		}
//
//		public void setReturnCashPrice(BigDecimal returnCashPrice) {
//			this.returnCashPrice = returnCashPrice;
//		}

		public BigDecimal getExpSubsidyPrice() {
			return expSubsidyPrice;
		}

		public void setExpSubsidyPrice(BigDecimal expSubsidyPrice) {
			this.expSubsidyPrice = expSubsidyPrice;
		}

		public RefundType getRefundType() {
			return refundType;
		}

		public void setRefundType(RefundType refundType) {
			this.refundType = refundType;
		}

		public ReturnBankCardParam getRetBankCard() {
			return retBankCard;
		}

		public void setRetBankCard(ReturnBankCardParam retBankCard) {
			this.retBankCard = retBankCard;
		}

		public boolean isRecycle() {
			return recycle;
		}

		public void setRecycle(boolean recycle) {
			this.recycle = recycle;
		}
	}
	
}
