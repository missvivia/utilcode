package com.xyl.mmall.order.dto;

import java.util.ArrayList;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderForm;

/**
 * 成功下单后的订单DTO(简洁版)
 * 
 * @author dingmingliang
 * 
 */
public class OrderFormBrief2DTO extends OrderForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 是否可以取消订单
	 */
	public boolean canCancel;
	
	public List<OrderSkuDTO> ordSkuList = new ArrayList<OrderSkuDTO>();

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderFormBrief2DTO(OrderForm obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderFormBrief2DTO() {
	}

	public boolean isCanCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

	public List<OrderSkuDTO> getOrdSkuList() {
		return ordSkuList;
	}

	public void setOrdSkuList(List<OrderSkuDTO> ordSkuList) {
		this.ordSkuList = ordSkuList;
	}
}
