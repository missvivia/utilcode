package com.xyl.mmall.order.result;

import java.io.Serializable;
import java.util.Map;

import com.xyl.mmall.framework.enums.ComposeOrderErrorCodeEnum;
import com.xyl.mmall.order.dto.OrderFormCalDTO;

/**
 * @author dingmingliang
 * 
 */
public class OrderCalServiceGenOrderResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 下单过程中的订单DTO
	 */
	private OrderFormCalDTO orderFormCalDTO;

	/**
	 * 组单错误code
	 */
	private ComposeOrderErrorCodeEnum resultCode;

	/**
	 * 库存不足的Sku信息<br>
	 * Key: skuId, Value: 缺少的库存数
	 */
	private Map<Long, Integer> outStockSkuMap;

	/**
	 * 库存不足的Sku信息<br>
	 * Key: skuId, Value: 缺少的库存数
	 * 
	 * @return
	 */
	public Map<Long, Integer> getOutStockSkuMap() {
		return outStockSkuMap;
	}

	public void setOutStockSkuMap(Map<Long, Integer> outStockSkuMap) {
		this.outStockSkuMap = outStockSkuMap;
	}

	public OrderFormCalDTO getOrderFormCalDTO() {
		return orderFormCalDTO;
	}

	public void setOrderFormCalDTO(OrderFormCalDTO orderFormCalDTO) {
		this.orderFormCalDTO = orderFormCalDTO;
	}

	public ComposeOrderErrorCodeEnum getResultCode() {
		return resultCode;
	}

	public void setResultCode(ComposeOrderErrorCodeEnum resultCode) {
		this.resultCode = resultCode;
	}

}
