package com.xyl.mmall.mainsite.vo.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * 确认订单页-订单对象
 * 
 * @author dingmingliang
 * 
 */
public class OrderForm1VO extends OrderFormBasicVO {

	/**
	 * 订单优惠Tag列表
	 */
	private List<OrderYHTagVO> yhTagList;

	/**
	 * 按活动显示的订单明细列表
	 */
	private List<OrderActionVO> actionList;

	/**
	 * 没有参加活动的订单明细列表
	 */
	private List<OrderNoActionVO> noActionList;

	/**
	 * 可选的优惠券列表
	 */
	private List<OrderCoupon1VO> couponList;

	/**
	 * 可选的支付方式(0:网易宝,1:货到付款)
	 */
	private OrderFormPayMethod1VO[] payMethodArray;

	/**
	 * 用户有效的红包总金额
	 */
	private BigDecimal canUseRedPackets = BigDecimal.ZERO;

	/**
	 * 订单上可以使用的红包金额
	 */
	private BigDecimal canOrderRedPackets = BigDecimal.ZERO;

	public BigDecimal getCanOrderRedPackets() {
		return canOrderRedPackets;
	}

	public void setCanOrderRedPackets(BigDecimal canOrderRedPackets) {
		this.canOrderRedPackets = canOrderRedPackets;
	}

	public BigDecimal getCanUseRedPackets() {
		return canUseRedPackets;
	}

	public void setCanUseRedPackets(BigDecimal canUseRedPackets) {
		this.canUseRedPackets = canUseRedPackets;
	}

	public List<OrderYHTagVO> getYhTagList() {
		return yhTagList;
	}

	public void setYhTagList(List<OrderYHTagVO> yhTagList) {
		this.yhTagList = yhTagList;
	}

	public OrderFormPayMethod1VO[] getPayMethodArray() {
		return payMethodArray;
	}

	public void setPayMethodArray(OrderFormPayMethod1VO[] payMethodArray) {
		this.payMethodArray = payMethodArray;
	}

	public List<OrderCoupon1VO> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<OrderCoupon1VO> couponList) {
		this.couponList = couponList;
	}

	public List<OrderNoActionVO> getNoActionList() {
		return noActionList;
	}

	public void setNoActionList(List<OrderNoActionVO> noActionList) {
		this.noActionList = noActionList;
	}

	public List<OrderActionVO> getActionList() {
		return actionList;
	}

	public void setActionList(List<OrderActionVO> actionList) {
		this.actionList = actionList;
	}
}
