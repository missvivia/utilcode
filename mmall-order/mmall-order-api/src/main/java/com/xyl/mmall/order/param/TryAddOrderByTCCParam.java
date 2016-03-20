package com.xyl.mmall.order.param;

import java.io.Serializable;

import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.result.TryAddOrderByTCCResult;

/**
 * 以TCC模型,向DB添加订单数据需要的参数
 * 
 * @author dingmingliang
 * 
 */
public class TryAddOrderByTCCParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;
	
	/**
	 * 用户Id
	 */
	private long userId;

	/**
	 * 内存组单数据
	 */
	private OrderFormCalDTO orderFormCalDTO;

	/**
	 * 方法执行结果
	 */
	private TryAddOrderByTCCResult result;

	public void setOrderFormCalDTO(OrderFormCalDTO orderFormCalDTO) {
		this.orderFormCalDTO = orderFormCalDTO;
	}

	public TryAddOrderByTCCResult getResult() {
		return result;
	}

	public void setResult(TryAddOrderByTCCResult result) {
		this.result = result;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public OrderFormCalDTO getOrderFormCalDTO() {
		return orderFormCalDTO;
	}
}
