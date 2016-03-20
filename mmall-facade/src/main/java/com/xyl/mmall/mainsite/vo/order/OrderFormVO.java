package com.xyl.mmall.mainsite.vo.order;

import java.math.BigDecimal;

import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年5月20日下午3:20:58
 */
public class OrderFormVO {

	/**
	 * 订单Id
	 */
	private long orderId;

	/**
	 * 订单状态
	 */
	private int orderFormState;

	/**
	 * 支付方式
	 */
	private int orderFormPayMethod;

	/**
	 * 支付状态
	 */
	private int payState;

	/**
	 * 实付金额
	 */
	private BigDecimal cartRPrice;

	/**
	 * 订单金额
	 */
	private BigDecimal cartOriRPrice;

	/**
	 * 买家Id
	 */
	private long userId;

	/**
	 * 下单支付时间
	 */
	private long payTime;

	/**
	 * 下单时间
	 */
	private String orderCreateTime;

	/**
	 * 买家账号
	 */
	private String userName;

	/**
	 * 是否使用优惠券
	 */
	private boolean isUseCoupon;

	/**
	 * 订单渠道 4普通订单 7 代客订单
	 */
	private int spSource;

	public OrderFormVO() {

	};

	public OrderFormVO(OrderFormDTO orderFormDTO) {
		this.orderId = orderFormDTO.getOrderId();
		this.orderFormState = orderFormDTO.getOrderFormState().getIntValue();
		this.orderFormPayMethod = orderFormDTO.getOrderFormPayMethod().getIntValue();
		this.payState = orderFormDTO.getPayState().getIntValue();
		this.cartRPrice = orderFormDTO.getCartRPrice();
		this.cartOriRPrice = orderFormDTO.getCartOriRPrice();
		this.userId = orderFormDTO.getUserId();
		this.payTime = orderFormDTO.getPayTime();
		this.orderCreateTime = DateUtil.dateToString(orderFormDTO.getCreateTime(), DateUtil.LONG_PATTERN);
		this.userName = orderFormDTO.getUserName();
		// 订单超过2个小时未付款设为取消
		if (orderFormDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME < System.currentTimeMillis()
				&& orderFormDTO.getOrderFormState().equals(OrderFormState.WAITING_PAY)) {
			this.setOrderFormState(OrderFormState.CANCEL_ED.getIntValue());
		}
		if (orderFormDTO.getCouponDiscount().compareTo(BigDecimal.ZERO) > 0) {
			this.setUseCoupon(true);
		}
		this.spSource = orderFormDTO.getSpSource().getIntValue();
	}

	public OrderFormVO(OrderFormBriefDTO orderFormBriefDTO) {
		this.orderId = orderFormBriefDTO.getOrderId();
		this.orderFormState = orderFormBriefDTO.getOrderFormState().getIntValue();
		this.orderFormPayMethod = orderFormBriefDTO.getOrderFormPayMethod().getIntValue();
		this.payState = orderFormBriefDTO.getPayState().getIntValue();
		this.cartRPrice = orderFormBriefDTO.getCartRPrice();
		this.cartOriRPrice = orderFormBriefDTO.getCartOriRPrice();
		this.userId = orderFormBriefDTO.getUserId();
		this.payTime = orderFormBriefDTO.getPayTime();
		this.orderCreateTime = DateUtil.dateToString(orderFormBriefDTO.getCreateTime(), DateUtil.LONG_PATTERN);
		this.userName = orderFormBriefDTO.getUserName();
		// 订单超过2个小时未付款设为取消
		if (orderFormBriefDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME < System.currentTimeMillis()
				&& orderFormBriefDTO.getOrderFormState().equals(OrderFormState.WAITING_PAY)) {
			this.setOrderFormState(OrderFormState.CANCEL_ED.getIntValue());
		}
		if (orderFormBriefDTO.getCouponDiscount().compareTo(BigDecimal.ZERO) > 0) {
			this.setUseCoupon(true);
		}
		this.spSource = orderFormBriefDTO.getSpSource().getIntValue();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getOrderFormState() {
		return orderFormState;
	}

	public void setOrderFormState(int orderFormState) {
		this.orderFormState = orderFormState;
	}

	public int getOrderFormPayMethod() {
		return orderFormPayMethod;
	}

	public void setOrderFormPayMethod(int orderFormPayMethod) {
		this.orderFormPayMethod = orderFormPayMethod;
	}

	public int getPayState() {
		return payState;
	}

	public void setPayState(int payState) {
		this.payState = payState;
	}

	public BigDecimal getCartRPrice() {
		return cartRPrice;
	}

	public void setCartRPrice(BigDecimal cartRPrice) {
		this.cartRPrice = cartRPrice;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public BigDecimal getCartOriRPrice() {
		return cartOriRPrice;
	}

	public void setCartOriRPrice(BigDecimal cartOriRPrice) {
		this.cartOriRPrice = cartOriRPrice;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isUseCoupon() {
		return isUseCoupon;
	}

	public void setUseCoupon(boolean isUseCoupon) {
		this.isUseCoupon = isUseCoupon;
	}

	public int getSpSource() {
		return spSource;
	}

	public void setSpSource(int spSource) {
		this.spSource = spSource;
	}

}
