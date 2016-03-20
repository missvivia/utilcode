package com.xyl.mmall.mainsite.vo.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.netease.print.common.util.DateFormatEnum;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 订单列表VO
 * 
 * @author author:lhp
 *
 * @version date:2015年7月13日下午3:20:51
 */
public class OrderFormListVO {

	/**
	 * 订单Id
	 */
	private long orderId;

	/**
	 * 订单parentId
	 */
	private long parentId;

	/**
	 * 订单结算价
	 */
	private BigDecimal cartRPrice = BigDecimal.ZERO;

	/**
	 * 下单时间
	 */
	private String orderTime;

	/**
	 * 交易关闭倒计时
	 */
	private long payCloseCD = 0;

	/**
	 * 店铺名字
	 */
	private String storeName;

	/**
	 * 店铺Url
	 */
	private String storeUrl;

	/**
	 * 是否在线付款
	 */
	private boolean isOnpay;

	/**
	 * 订单状态
	 */
	private OrderFormState orderFormState;

	/**
	 * 明细
	 */
	private List<OrderSkuVO> orderSkuList;

	/**
	 * 子订单列表
	 */
	private List<OrderFormListVO> subOrderFormListVO;

	/**
	 * 订单渠道
	 */
	private int spSource;

	public OrderFormListVO() {

	}

	public OrderFormListVO(OrderFormDTO orderFormDTO) {
		this.orderId = orderFormDTO.getOrderId();
		this.parentId = orderFormDTO.getParentId();
		this.cartRPrice = orderFormDTO.getCartRPrice();
		this.orderTime = DateFormatEnum.TYPE5.getFormatDate(orderFormDTO.getOrderTime());
		this.orderFormState = orderFormDTO.getOrderFormState();
		this.isOnpay = OrderFormPayMethod.isOnlinePayMethod(orderFormDTO.getOrderFormPayMethod());
		this.orderSkuList = MainsiteVOConvertUtil.convertToOrderSkuVOList(orderFormDTO.getOrderSkuDTOListOfOrdGift(),
				true);
		setPayCloseCDAndOrderForm(orderFormDTO.getOrderTime());
		this.spSource = orderFormDTO.getSpSource().getIntValue();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public BigDecimal getCartRPrice() {
		return cartRPrice;
	}

	public void setCartRPrice(BigDecimal cartRPrice) {
		this.cartRPrice = cartRPrice;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public OrderFormState getOrderFormState() {
		return orderFormState;
	}

	public void setOrderFormState(OrderFormState orderFormState) {
		this.orderFormState = orderFormState;
	}

	public List<OrderSkuVO> getOrderSkuList() {
		return orderSkuList;
	}

	public void setOrderSkuList(List<OrderSkuVO> orderSkuList) {
		this.orderSkuList = orderSkuList;
	}

	public List<OrderFormListVO> getSubOrderFormListVO() {
		return subOrderFormListVO;
	}

	public void setSubOrderFormListVO(List<OrderFormListVO> subOrderFormListVO) {
		this.subOrderFormListVO = subOrderFormListVO;
	}

	public void addSubOrderFormListVO(OrderFormListVO orderFormListVO) {
		if (subOrderFormListVO == null) {
			subOrderFormListVO = new ArrayList<OrderFormListVO>();
		}
		subOrderFormListVO.add(orderFormListVO);
	}

	public long getPayCloseCD() {
		return payCloseCD;
	}

	public void setPayCloseCD(long payCloseCD) {
		this.payCloseCD = payCloseCD;
	}

	public boolean isOnpay() {
		return isOnpay;
	}

	public void setOnpay(boolean isOnpay) {
		this.isOnpay = isOnpay;
	}

	public void setPayCloseCDAndOrderForm(long orderTime) {
		long currTime = System.currentTimeMillis();
		if (orderTime + ConstValueOfOrder.MAX_PAY_TIME > currTime
				&& this.getOrderFormState() == OrderFormState.WAITING_PAY) {
			payCloseCD = orderTime + ConstValueOfOrder.MAX_PAY_TIME - currTime;
			payCloseCD = payCloseCD <= 0 ? 0 : payCloseCD;
		}
		// 6.设置订单状态
		if (payCloseCD <= 0 && this.orderFormState == OrderFormState.WAITING_PAY) {
			this.orderFormState = OrderFormState.CANCEL_ED;
		}
	}

	public int getSpSource() {
		return spSource;
	}

	public void setSpSource(int spSource) {
		this.spSource = spSource;
	}

}
