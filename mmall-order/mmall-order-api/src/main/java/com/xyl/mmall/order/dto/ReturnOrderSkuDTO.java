package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.ReturnOrderSku;

/**
 * 退回的商品
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午12:26:14
 *
 */
public class ReturnOrderSkuDTO extends ReturnOrderSku {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -468890115148919716L;
	
	/**
	 * 构造函数
	 */
	public ReturnOrderSkuDTO() {
	}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public ReturnOrderSkuDTO(ReturnOrderSku obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 与所退商品关联的原始OrderSku
	 */
	private OrderSkuDTO ordSkuDTO;

	public OrderSkuDTO getOrdSkuDTO() {
		return ordSkuDTO;
	}

	public void setOrdSkuDTO(OrderSkuDTO ordSkuDTO) {
		this.ordSkuDTO = ordSkuDTO;
	}

}
