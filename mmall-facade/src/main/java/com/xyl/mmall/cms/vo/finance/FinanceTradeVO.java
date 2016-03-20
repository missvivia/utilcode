package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.common.util.DateFormatUtil;
import com.xyl.mmall.order.dto.TradeItemDTO;

/**
 * 
 * @author wangfeng
 *
 */
public class FinanceTradeVO implements Serializable {

	private static final long serialVersionUID = -7722638869617756399L;

	/** 交易号. */
	private long tradeId;

	/** 交易平台. */
	private String payMethod;

	/** 付款时间. */
	private String payDate;

	/** 交易金额. */
	private BigDecimal cash;

	/** 订单号. */
	private long orderId;

	public FinanceTradeVO() {
		super();
	}

	public FinanceTradeVO(TradeItemDTO trdeItemDTO) {
		super();
		this.tradeId = trdeItemDTO.getTradeId();
		this.payMethod = trdeItemDTO.getTradeItemPayMethod().getDesc();
		this.payDate = DateFormatUtil.getFormatDateType5(trdeItemDTO.getPayTime());
		this.cash = trdeItemDTO.getCash();
		this.orderId = trdeItemDTO.getOrderId();
	}

	public long getTradeId() {
		return tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

}
