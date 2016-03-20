package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.DeprecatedReturnOrderSku;
import com.xyl.mmall.order.param.RetOrdSkuPriceCalParam;

/**
 * 退回的商品
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午12:26:14
 *
 */
@Deprecated
public class DeprecatedReturnOrderSkuDTO extends DeprecatedReturnOrderSku implements RetOrdSkuPriceCalParam {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -468890115148919716L;
	
	/**
	 * 构造函数
	 */
	public DeprecatedReturnOrderSkuDTO() {
	}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public DeprecatedReturnOrderSkuDTO(DeprecatedReturnOrderSku obj) {
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

	@Override
	public long idOfOrderSku() {
		return getOrderSkuId();
	}

	@Override
	public long countOfReturn(boolean applySituation) {
		return applySituation ? getReturnCount() : getConfirmCount();
	}
	
}
