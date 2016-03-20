package com.xyl.mmall.order.service;

import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.OrderFormPayMethodDTO;
import com.xyl.mmall.order.param.OrderCalServiceGenExpPriceParam;
import com.xyl.mmall.order.param.OrderCalServiceGenOrderParam;
import com.xyl.mmall.order.param.SkuParam;
import com.xyl.mmall.order.result.OrderCalServiceGenOrderResult;

/**
 * 下单过程中,OrderFormCalDTO的相关服务
 * 
 * @author dingmingliang
 * 
 */
public interface OrderCalService {

	/**
	 * 内存组单
	 * 
	 * @param param
	 * @return
	 */
	public OrderCalServiceGenOrderResult genOrder(OrderCalServiceGenOrderParam param);
	
	/**
	 * 计算用户需要支付的邮费金额
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal genExpUserPrice(OrderCalServiceGenExpPriceParam param);

	/**
	 * 生成可选的支付方式列表
	 * 
	 * @param userId
	 * @param orderExpInfo
	 * @param totalRPrice
	 * @return
	 */
	public OrderFormPayMethodDTO[] genOrderFormPayMethodDTOArray(long userId, OrderExpInfoDTO orderExpInfo,
			BigDecimal totalRPrice);
}
