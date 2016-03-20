package com.xyl.mmall.oms.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.common.constant.ConstValue;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.dto.OrderFormOMSDTO;
import com.xyl.mmall.oms.dto.OrderSkuOMSDTO;

/**
 * @author dingmingliang
 * 
 */
public class OmsOrderFormPriceUtil {

	private static Logger logger = LoggerFactory.getLogger(OmsOrderFormPriceUtil.class);

	/**
	 * 拆分订单
	 * 
	 * @param orderDTO
	 * @param orderSkuDTOGroupColl
	 * @return
	 */
	public static List<OrderFormOMSDTO> caculatePrice(OrderFormOMSDTO orderDTO,
			Collection<Collection<OrderSkuOMSDTO>> orderSkuDTOGroupColl) {
		// 0.参数准备
		// 是否是货到付款订单
		boolean isCOD = orderDTO.isCashOnDelivery();
		int scale = isCOD ? 0 : 2;
		// 订单的邮费金额(现金)
		BigDecimal expUserPriceOfRealCash = orderDTO.getExpUserPrice().subtract(orderDTO.getExpUserPriceOfRed());
		BigDecimal cartRPriceOfRealCash = orderDTO.getCartRPrice().subtract(orderDTO.getRedCash())
				.subtract(orderDTO.getExpUserPriceOfRed());
		BigDecimal redCashOfTotal = orderDTO.getRedCash();
		BigDecimal cashOfTrade = orderDTO.getCartRPrice().add(orderDTO.getExpUserPrice()).subtract(redCashOfTotal)
				.setScale(scale, RoundingMode.DOWN);
		// 1.skuIdGroup按默认方式排序(将skuId最小的放在第一位)
		List<Collection<OrderSkuOMSDTO>> sortSkuIdGroupColl = sortSkuIdGroupColl(orderSkuDTOGroupColl);
		// 2.根据sortSkuIdGroup进行分组,并生成新的OrderFormOMSDTO对象(计算新的cartRPrice,expUserPrice)
		List<OrderFormOMSDTO> resultList = new ArrayList<>();
		for (Collection<OrderSkuOMSDTO> sortSkuIdGroup : sortSkuIdGroupColl) {
			// 2.1 设置订单的sku明细列表
			OrderFormOMSDTO orderDTOOfTmp = ReflectUtil.cloneObj(orderDTO);
			List<OrderSkuOMSDTO> orderSkuDTOListOfTmp = CollectionUtil.addAllOfList(null, sortSkuIdGroup);
			orderDTOOfTmp.setOrderSkuOMSDTOList(orderSkuDTOListOfTmp);
			// 2.2 计算商品零售价
			BigDecimal cartRPriceOfCashTmp = BigDecimal.ZERO;
			for (OrderSkuOMSDTO orderSkuDTOOfTmp : sortSkuIdGroup) {
				BigDecimal rpriceOfCashTmp = orderSkuDTOOfTmp.getRprice().subtract(orderSkuDTOOfTmp.getRedSPrice());
				cartRPriceOfCashTmp = cartRPriceOfCashTmp.add(rpriceOfCashTmp.multiply(new BigDecimal(orderSkuDTOOfTmp
						.getTotalCount())));
			}
			// 2.3 计算用户支付的邮费
			BigDecimal expUserPriceOfCashTmp = null;
			if (orderDTO.getCartRPrice().compareTo(BigDecimal.ZERO) == 0) {
				// 订单商品金额为0的特殊处理(快递金额按照分组均分)
				expUserPriceOfCashTmp = expUserPriceOfRealCash.divide(new BigDecimal(sortSkuIdGroupColl.size()),
						ConstValue.MC).setScale(2, RoundingMode.DOWN);
			} else {
				expUserPriceOfCashTmp = expUserPriceOfRealCash.multiply(cartRPriceOfCashTmp)
						.divide(cartRPriceOfRealCash, ConstValue.MC).setScale(2, RoundingMode.DOWN);
			}
			// 2.4 货到付款的特殊处理(抹掉元以下的金额)
			if (isCOD) {
				cartRPriceOfCashTmp = cartRPriceOfCashTmp.setScale(0, RoundingMode.DOWN);
				expUserPriceOfCashTmp = expUserPriceOfCashTmp.setScale(0, RoundingMode.DOWN);
			}
			// 2.5 设置订单的属性
			orderDTOOfTmp.setCartRPrice(cartRPriceOfCashTmp);
			orderDTOOfTmp.setExpUserPrice(expUserPriceOfCashTmp);
			resultList.add(orderDTOOfTmp);
		}

		// 3.额外的金额平摊(商品金额和快递费)
		OrderFormOMSDTO orderDTOOfChange = resultList.get(0);
		BigDecimal cartRPriceOfCashTotal = BigDecimal.ZERO, expUserPriceOfCashTotal = BigDecimal.ZERO;
		for (OrderFormOMSDTO orderDTOOfTmp : resultList) {
			if (orderDTOOfTmp == orderDTOOfChange)
				continue;
			cartRPriceOfCashTotal = cartRPriceOfCashTotal.add(orderDTOOfTmp.getCartRPrice());
			expUserPriceOfCashTotal = expUserPriceOfCashTotal.add(orderDTOOfTmp.getExpUserPrice());
		}
		orderDTOOfChange.setExpUserPrice(expUserPriceOfRealCash.subtract(expUserPriceOfCashTotal, ConstValue.MC)
				.setScale(scale, RoundingMode.DOWN));
		orderDTOOfChange.setCartRPrice(cashOfTrade.subtract(cartRPriceOfCashTotal).subtract(expUserPriceOfCashTotal)
				.subtract(orderDTOOfChange.getExpUserPrice()).setScale(scale, RoundingMode.DOWN));
		// 防止出现负数
		if (orderDTOOfChange.getCartRPrice().compareTo(BigDecimal.ZERO) < 1)
			orderDTOOfChange.setCartRPrice(BigDecimal.ZERO);
		if (orderDTOOfChange.getExpUserPrice().compareTo(BigDecimal.ZERO) < 1)
			orderDTOOfChange.setExpUserPrice(BigDecimal.ZERO);
		return resultList;
	}

	/**
	 * skuIdGroup按默认方式排序(将skuId最小的放在第一位)
	 * 
	 * @param skuIdGroupColl
	 * @return
	 */
	private static List<Collection<OrderSkuOMSDTO>> sortSkuIdGroupColl(
			Collection<Collection<OrderSkuOMSDTO>> orderSkuOMSDTOGroupColl) {
		Map<Collection<OrderSkuOMSDTO>, Long> map1 = new HashMap<>();
		for (Collection<OrderSkuOMSDTO> orderSkuOMSDTOGroup : orderSkuOMSDTOGroupColl) {
			long skuId = -1L;
			Map<Long, OrderSkuOMSDTO> map11 = new TreeMap<>();
			for (OrderSkuOMSDTO orderSkuOMSDTO : orderSkuOMSDTOGroup) {
				map11.put(orderSkuOMSDTO.getSkuId(), orderSkuOMSDTO);
			}
			skuId = CollectionUtil.getFirstKeyOfMap(map11);
			map1.put(orderSkuOMSDTOGroup, skuId);
		}

		Map<Long, Collection<OrderSkuOMSDTO>> map2 = new TreeMap<>();
		for (Entry<Collection<OrderSkuOMSDTO>, Long> entry : map1.entrySet()) {
			map2.put(entry.getValue(), entry.getKey());
		}

		List<Collection<OrderSkuOMSDTO>> sortColl = new ArrayList<>();
		sortColl.addAll(map2.values());
		return sortColl;
	}

}
