package com.xyl.mmall.order.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderFormExtInfoDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.meta.tcc.SkuOrderStockTCC;

/**
 * @author dingmingliang
 * 
 */
public class OrderUtil {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(OrderUtil.class);

	/**
	 * 尝试初始化DDBParam<br>
	 * 如果param没有排序字段,则按orderId字段升序
	 * 
	 * @param param
	 * @return
	 */
	public static DDBParam initDDBParamWithMinOrderId(DDBParam param) {
		return initDDBParamWithMinId(param, "orderId");
	}

	/**
	 * 尝试初始化DDBParam<br>
	 * 如果param没有排序字段,则按fieldName字段升序
	 * 
	 * @param param
	 * @param fieldName
	 *            排序字段
	 * @return
	 */
	public static DDBParam initDDBParamWithMinId(DDBParam param, String fieldName) {
		if (param == null)
			param = DDBParam.genParamX(0);
		if (StringUtils.isBlank(param.getOrderColumn())) {
			param.setAsc(true);
			param.setOrderColumn(fieldName);
		}
		return param;
	}

	/**
	 * 添加下单时间范围的条件
	 * 
	 * @param sql
	 * @param orderTimeRange
	 */
	public static void appendOrderTimeRange(StringBuilder sql, long[] orderTimeRange) {
		if (orderTimeRange != null)
			sql.append(" AND orderTime>=").append(orderTimeRange[0]).append(" AND orderTime<=")
					.append(orderTimeRange[1]);
	}

	/**
	 * 根据订单的OrderSku明细,生成Sku库存TCC数据
	 * 
	 * @param orderFormDTO
	 * @param currTime
	 * @param tranId
	 * @param isAddStockCount
	 * @return Key: skuId
	 */
	public static Map<Long, SkuOrderStockTCC> genSkuOrderStockTCCMap(OrderFormDTO orderFormDTO, long currTime,
			long tranId, boolean isAddStockCount) {
		if (orderFormDTO == null)
			return null;

		Map<Long, SkuOrderStockTCC> skuOrderStockTCCMap = new TreeMap<>();
		for (OrderCartItemDTO orderCartItemDTO : orderFormDTO.getOrderCartItemDTOList()) {
			for (OrderSkuDTO orderSkuDTO : orderCartItemDTO.getOrderSkuDTOList()) {
				long skuId = orderSkuDTO.getSkuId();
				SkuOrderStockTCC skuOrderStockTCC = skuOrderStockTCCMap.get(skuId);
				// 初始化SkuOrderStockTCC
				if (skuOrderStockTCC == null) {
					skuOrderStockTCC = new SkuOrderStockTCC();
					skuOrderStockTCC.setAddStockCount(isAddStockCount);
					skuOrderStockTCC.setCtimeOfTCC(currTime);
					skuOrderStockTCC.setStockCount(0);
					skuOrderStockTCC.setSkuId(skuId);
					skuOrderStockTCC.setTranId(tranId);
					skuOrderStockTCC.setPoId(orderSkuDTO.getPoId());
				}
				skuOrderStockTCC.setStockCount(skuOrderStockTCC.getStockCount() + orderSkuDTO.getTotalCount());
				skuOrderStockTCCMap.put(skuId, skuOrderStockTCC);
			}
		}
		return skuOrderStockTCCMap;
	}

	/**
	 * 判断订单当前是否可以取消
	 * 
	 * @param ord
	 * @param packageList
	 * @return
	 */
	public static boolean canCancel(OrderForm ord, List<? extends OrderPackage> packageList) {
		// 1.订单状态为可以取消
		OrderFormState orderState = ord.getOrderFormState();
		boolean canCancelOfState = OrderFormState.canCancel(orderState);
		// 2.订单是否曾经取消过
		OrderFormExtInfoDTO extDTO = OrderFormExtInfoDTO.genOrderFormExtInfoDTOByOrder(ord);
		boolean canCancelOfExtInfo = !extDTO.isCancelFail();
		// 3.判断是否已经有真实包裹
		boolean canCancelOfPackage = true;
		if (CollectionUtil.isNotEmptyOfCollection(packageList)) {
			for (OrderPackage pacakgeDTO : packageList) {
				if (pacakgeDTO.getPackageId() > 0)
					canCancelOfPackage = false;
			}
		}
		// 4.返回结果
		return canCancelOfState && canCancelOfExtInfo && canCancelOfPackage;
	}

	/**
	 * 判断订单当前是否可以取消
	 * 
	 * @param ordDTO
	 * @return
	 */
	public static boolean canCancel(OrderFormDTO ordDTO) {
		return canCancel(ordDTO, ordDTO.getOrderPackageDTOList());
	}
}
