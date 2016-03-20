/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.bi.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderSku;
import com.xyl.mmall.order.service.OrderService;

/**
 * SkuStatisticsServiceImpl.java created by yydx811 at 2015年11月6日 下午5:25:55
 * 订单商品统计
 *
 * @author yydx811
 */
@Service("skuStatisticsService")
public class SkuStatisticsServiceImpl implements BILogService {
	
	private static Logger logger = LoggerFactory.getLogger(SkuStatisticsServiceImpl.class);

	@Resource
	private OrderService orderService;
	
	@Resource
	private ItemProductService itemProductService;

	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		// 判断订单id
		if (StringUtils.isBlank(otherKey) || !RegexUtils.isAllNumber(otherKey)) {
			logger.warn("Wrong otherKey : {}!", otherKey);
		} else {
			// 获取订单状态
			long orderId = Long.parseLong(otherKey);
			OrderForm orderForm = orderService.getOrderForm(orderId);
			if (orderForm == null || orderForm.getOrderFormState() != OrderFormState.FINISH_TRADE) {
				logger.warn("Order is not finished! OrderId : {}.", orderId);
			} else {
				// 获取订单sku
				List<OrderSku> orderSkuList = 
						orderService.getOrderSKUListByOrderIdAndUserId(orderId, orderForm.getUserId());
				if (CollectionUtils.isEmpty(orderSkuList)) {
					logger.warn("OrderSkuList is empty! OrderId : {}, UserId : {}.", orderId, orderForm.getUserId());
				} else {
					for (OrderSku orderSku : orderSkuList) {
						try {
							int res = itemProductService.updateProductSKUSaleNum(orderSku.getSkuId(), 
									orderSku.getTotalCount());
							if (res > 0) {
								logger.info("Update productSKU saleNum successful! SkuId :　{}, saleNum : {}.", 
										orderSku.getSkuId(), orderSku.getTotalCount());
							} else {
								logger.error("Update productSKU saleNum error! SkuId :　{}, saleNum : {}.", 
										orderSku.getSkuId(), orderSku.getTotalCount());
							}
						} catch (Exception e) {
							logger.error("Update productSKU saleNum error! SkuId :　{}, saleNum : {}.", 
									orderSku.getSkuId(), orderSku.getTotalCount());
						}
					}
				}
			}
		}
	}

}
