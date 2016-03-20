package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.TradeItemPayMethod;

/**
 * CMS订单详情：交易详情
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月27日 下午5:47:03
 *
 */
public class TradeBasicInfo {
	private String userName;    //用户名
	private String tradeId;    //交易ID
	private String userId; 	//用户ID
	private String orderId;	//订单ID
	private PayState status;    //支付状态
	private List<PayMethod> payMethod = new ArrayList<PayMethod>();    //交易支付方式
	private long startTime;    //交易发起时间
	private long endTime;    //交易结束时间
	
	public TradeBasicInfo fillTrade(String userName, 
			TradeItemDTO tradeItem, List<TradeItemDTO> allTradesInSameOrder) {
		this.userName = userName;
		if(null == tradeItem || null == allTradesInSameOrder) {
			return this;
		}
		this.tradeId = String.valueOf(tradeItem.getTradeId());
		this.userId = String.valueOf(tradeItem.getUserId());
		this.orderId = String.valueOf(tradeItem.getOrderId());
		this.status = tradeItem.getPayState();
		BigDecimal total = BigDecimal.ZERO;
		for(TradeItemDTO trade : allTradesInSameOrder) {
			if(null == trade || null == trade.getCash()) {
				continue;
			}
			total = total.add(trade.getCash());
		}
		double totalDouble = total.doubleValue();
		for(TradeItemDTO trade : allTradesInSameOrder) {
		// to be continued：ugly code	
			if(null == trade || 0 == totalDouble) {
				continue;
			}
			if(null == orderId || !orderId.equals(trade.getOrderId()) || null == userId || !this.userId.equals(trade.getUserId())) {
				continue;
			}
			BigDecimal cash = trade.getCash();
			if(null == cash) {
				continue;
			}
			double cashDouble = cash.doubleValue();
			PayMethod method = new PayMethod(trade.getTradeItemPayMethod(), cash, cashDouble / totalDouble);
			payMethod.add(method);
		}
		this.startTime = tradeItem.getCtime();
	// to be continued：交易结束时间
		this.endTime = tradeItem.getPayTime();
		return this;
	}
	
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public PayState getStatus() {
		return status;
	}
	public void setStatus(PayState status) {
		this.status = status;
	}
	public List<PayMethod> getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(List<PayMethod> payMethod) {
		this.payMethod = payMethod;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public static class PayMethod {
		private TradeItemPayMethod method;
		private BigDecimal pay = BigDecimal.ZERO;
		private String pricePercentage;
		public PayMethod() {
		}
		public PayMethod(TradeItemPayMethod method, BigDecimal pay, double pricePercentage) {
			this.method = method;
			if(null != pay) {
				this.pay = pay.setScale(2, RoundingMode.HALF_UP);
			}
			NumberFormat nf = NumberFormat.getPercentInstance(); 
			nf.setMinimumFractionDigits(1);	// 小数点后保留几位
			this.pricePercentage = nf.format(pricePercentage);
		}
		public TradeItemPayMethod getMethod() {
			return method;
		}
		public void setMethod(TradeItemPayMethod method) {
			this.method = method;
		}
		public BigDecimal getPay() {
			return pay;
		}
		public void setPay(BigDecimal pay) {
			this.pay = pay;
		}
		public String getPricePercentage() {
			return pricePercentage;
		}
		public void setPricePercentage(String pricePercentage) {
			this.pricePercentage = pricePercentage;
		}
	}

}
