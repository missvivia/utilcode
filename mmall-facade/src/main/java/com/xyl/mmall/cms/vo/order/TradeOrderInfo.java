package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * CMS订单详情：交易详情关联的Order
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月27日 下午5:48:03
 *
 */
public class TradeOrderInfo {
	
	private String orderId;    //12,
	
	private OrderFormState status;    //订单状态1,
	
	private long buyTime;    //订购时间1410922176302,
	
	private long logisticsBegin;    //物流开始时间1410922176302,
	
	private BigDecimal price = BigDecimal.ZERO;    //零售价10.00,
	
	private BigDecimal pay = BigDecimal.ZERO;    //实付金额10.00,
	
	private BigDecimal expressPrice = BigDecimal.ZERO;    //快递价格12.00,
	
	private BigDecimal platformPay = BigDecimal.ZERO;    //女装支付快递2.00,
	
	private int number;    //商品总数1,
	
	private int weight;    //商品重量（克）"1200g",
	
	private boolean canCancel;

	public TradeOrderInfo fillOrder(OrderFormDTO ordDTO) {
		if(null == ordDTO) {
			return this;
		}
		this.orderId = String.valueOf(ordDTO.getOrderId());    //12,
		this.status = ordDTO.getOrderFormState();    //订单状态1,
		this.buyTime = ordDTO.getOrderTime();    //订购时间1410922176302,
		this.logisticsBegin = ordDTO.getOmsTime();    //物流开始时间1410922176302,
		this.price = ordDTO.getCartOriRPrice();    //零售价10.00,
		if(null != this.price) {
			this.price = this.price.setScale(2, RoundingMode.HALF_UP);
		}
		this.pay = ordDTO.getCartRPrice();    //实付金额10.00,
		if(null != this.pay && null != ordDTO.getExpUserPrice()) {
			this.pay = this.pay.add(ordDTO.getExpUserPrice());
		}
		if(null != this.pay) {
			this.pay = this.pay.setScale(2, RoundingMode.HALF_UP);
		}
		this.expressPrice = ordDTO.getExpOriPrice();    //快递价格12.00,
		if(null != this.expressPrice) {
			this.expressPrice = this.expressPrice.setScale(2, RoundingMode.HALF_UP);
		}
		this.platformPay = ordDTO.getExpSysPayPrice();    //女装支付快递2.00,
		if(null != this.platformPay) {
			this.platformPay = this.platformPay.setScale(2, RoundingMode.HALF_UP);
		}
		this.number = ordDTO.getSkuCount();    //商品总数1,
// to be continued：商品重量
		this.weight = 0;    //商品重量（克）"1200g",
		this.canCancel = OrderFormState.canCancel(ordDTO.getOrderFormState());
		return this;
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public OrderFormState getStatus() {
		return status;
	}
	
	public void setStatus(OrderFormState status) {
		this.status = status;
	}
	
	public long getBuyTime() {
		return buyTime;
	}
	
	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}
	
	public long getLogisticsBegin() {
		return logisticsBegin;
	}
	
	public void setLogisticsBegin(long logisticsBegin) {
		this.logisticsBegin = logisticsBegin;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getPay() {
		return pay;
	}
	
	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}
	
	public BigDecimal getExpressPrice() {
		return expressPrice;
	}
	
	public void setExpressPrice(BigDecimal expressPrice) {
		this.expressPrice = expressPrice;
	}
	
	public BigDecimal getPlatformPay() {
		return platformPay;
	}
	
	public void setPlatformPay(BigDecimal platformPay) {
		this.platformPay = platformPay;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean isCanCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

}
