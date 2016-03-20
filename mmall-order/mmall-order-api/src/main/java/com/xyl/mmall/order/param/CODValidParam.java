package com.xyl.mmall.order.param;

import java.io.Serializable;

import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;

/**
 * 判断是否允许COD(货到付款)的参数
 * 
 * @author dingmingliang
 * 
 */
public class CODValidParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 订单的收货地址
	 */
	private OrderExpInfoDTO orderExpInfoDTO;

	/**
	 * 用户的收货地址簿
	 */
	private ConsigneeAddressDTO ConsigneeAddressDTO;

	/**
	 * 用户Id
	 */
	private long userId;

	public OrderExpInfoDTO getOrderExpInfoDTO() {
		return orderExpInfoDTO;
	}

	public void setOrderExpInfoDTO(OrderExpInfoDTO orderExpInfoDTO) {
		this.orderExpInfoDTO = orderExpInfoDTO;
	}

	public ConsigneeAddressDTO getConsigneeAddressDTO() {
		return ConsigneeAddressDTO;
	}

	public void setConsigneeAddressDTO(ConsigneeAddressDTO consigneeAddressDTO) {
		ConsigneeAddressDTO = consigneeAddressDTO;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
