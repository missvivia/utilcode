package com.xyl.mmall.order.param;

import java.math.BigDecimal;

/**
 * 客服退款操作信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月23日 下午5:00:05
 *
 */
public class PassReturnOperationParam extends ReturnOperationParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1856230280861294342L;
	
	// 商品退款
	@Deprecated
	private BigDecimal payedReturnCashPrice = BigDecimal.ZERO;
	
	// 快递补偿
	@Deprecated
	private BigDecimal expCompensatedPrice = BigDecimal.ZERO;
	
	// 退给用户的红包退款金额
	@Deprecated
	private BigDecimal hbPriceToUser = BigDecimal.ZERO;
	
	// 退给用户的商品退款金额（原路/网易宝/到付银行卡）
	private BigDecimal cashPriceToUser = BigDecimal.ZERO;

	@Deprecated
	public BigDecimal getPayedReturnCashPrice() {
		return payedReturnCashPrice;
	}

	@Deprecated
	public void setPayedReturnCashPrice(BigDecimal payedCashPrice) {
		this.payedReturnCashPrice = payedCashPrice;
	}

	@Deprecated
	public BigDecimal getExpCompensatedPrice() {
		return expCompensatedPrice;
	}

	@Deprecated
	public void setExpCompensatedPrice(BigDecimal expSubsidyPrice) {
		this.expCompensatedPrice = expSubsidyPrice;
	}

	@Deprecated
	public BigDecimal getHbPriceToUser() {
		return hbPriceToUser;
	}

	@Deprecated
	public void setHbPriceToUser(BigDecimal hbPriceToUser) {
		this.hbPriceToUser = hbPriceToUser;
	}

	public BigDecimal getCashPriceToUser() {
		return cashPriceToUser;
	}

	public void setCashPriceToUser(BigDecimal cashPriceToUser) {
		this.cashPriceToUser = cashPriceToUser;
	}
	
}
