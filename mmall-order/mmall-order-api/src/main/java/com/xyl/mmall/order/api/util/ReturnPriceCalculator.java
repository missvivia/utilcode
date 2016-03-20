package com.xyl.mmall.order.api.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam.RetOrdSkuPriceParam;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.param.ReturnConfirmParam;
import com.xyl.mmall.order.param.ReturnOrderSkuParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public class ReturnPriceCalculator {

	private static final Logger logger = Logger.getLogger(ReturnPriceCalculator.class);
	
	private ReturnPriceCalculator() {
	}
	
	/**
	 * 根据退货请求计算退款价格
	 * 
	 * @param ordPkgDTO
	 * @param retParamList
	 * @return
	 */
	public static _ReturnPackagePriceParam compute(OrderPackageSimpleDTO ordPkgDTO, List<ReturnOrderSkuParam> retParamList) {
		if(null == ordPkgDTO || CollectionUtil.isEmptyOfMap(ordPkgDTO.getOrderSkuMap()) || CollectionUtil.isEmptyOfList(retParamList)) {
			logger.warn("null ordPkgDTO or empty ordPkgDTO.orderSkuMap or empty retParamList");
			return null;
		}
		
		_ReturnPackagePriceParam retPkgPrice = new _ReturnPackagePriceParam();
		retPkgPrice.ordPkgDTO = ordPkgDTO;
		retPkgPrice.expSubsidyPrice = BigDecimal.TEN;
		
		long ordPkgId = ordPkgDTO.getPackageId();
		Map<Long, OrderSkuDTO> pkgOrdSkuMap = ordPkgDTO.getOrderSkuMap();
		
		for(ReturnOrderSkuParam retParam : retParamList) {
			if(null == retParam) {
				logger.warn("null retParam in retParamList");
				continue;
			}
			
			long orderSkuId = retParam.getOrderSkuId();
			OrderSkuDTO ordSku = pkgOrdSkuMap.get(orderSkuId);
			if(null == ordSku) {
				logger.warn("no OrderSku for orderSkuId " + orderSkuId + " in OrderPackage ordPkgId " + ordPkgId);
				continue;
			}
			
			int retCount = retParam.getRetCount() <= ordSku.getTotalCount() ? retParam.getRetCount() : ordSku.getTotalCount();
			// @AnnonOfField(desc = "最终零售价格[包含红包优惠](单位)")
			BigDecimal rprice = ordSku.getRprice();
			// @AnnonOfField(desc = "红包支付的差额(单位)")
			BigDecimal hbSPrice = ordSku.getRedSPrice();
			// 单个sku实际退款金额
			BigDecimal realReturnCashPrice = BigDecimal.ZERO;
			if(rprice.doubleValue() > hbSPrice.doubleValue()) {
				realReturnCashPrice = new BigDecimal(rprice.doubleValue() - hbSPrice.doubleValue());
			}
			
			RetOrdSkuPriceParam retOrdSkuPrice = new RetOrdSkuPriceParam();
			retOrdSkuPrice.ordSku = ordSku;
			retOrdSkuPrice.count = retCount;
			retPkgPrice.retOrdSkuPriceMap.put(orderSkuId, retOrdSkuPrice);
			
			retPkgPrice.returnHbPrice = retPkgPrice.returnHbPrice.add(hbSPrice.multiply(new BigDecimal(retCount)));
			retPkgPrice.returnCashPrice = retPkgPrice.returnCashPrice.add(realReturnCashPrice.multiply(new BigDecimal(retCount)));
		}
		
		retPkgPrice.returnTotalPrice = retPkgPrice.returnHbPrice.add(retPkgPrice.returnCashPrice);
		return retPkgPrice;
	}
	
	/**
	 * 根据退货请求计算退款价格
	 * 
	 * @param ordPkgDTO
	 * @param receivedRetOrdSku: orderSkuId->ReturnConfirmParam
	 * @return
	 */
	public static _ReturnPackagePriceParam compute(OrderPackageSimpleDTO ordPkgDTO, Map<Long, ReturnConfirmParam> receivedRetOrdSku) {
		if(null == ordPkgDTO || CollectionUtil.isEmptyOfMap(ordPkgDTO.getOrderSkuMap()) || CollectionUtil.isEmptyOfMap(receivedRetOrdSku)) {
			logger.warn("null ordPkgDTO or empty ordPkgDTO.orderSkuMap or empty receivedRetOrdSku");
			return null;
		}
		List<ReturnOrderSkuParam> retParamList = new ArrayList<ReturnOrderSkuParam>();
		Map<Long, OrderSkuDTO> ordSkuMap = ordPkgDTO.getOrderSkuMap();
		for(Entry<Long, ReturnConfirmParam> entry : receivedRetOrdSku.entrySet()) {
			long ordSkuId = entry.getKey();
			ReturnConfirmParam p = entry.getValue();
			OrderSkuDTO ordSku = ordSkuMap.get(ordSkuId);
			if(null == p || null == ordSku) {
				logger.warn("illegal orderSkuId " + ordSkuId + " in receivedRetOrdSku(Map<Long, ReturnConfirmParam>)");
				continue;
			}
			ReturnOrderSkuParam rosParam = new ReturnOrderSkuParam();
			rosParam.setOrderSkuId(ordSkuId);
			rosParam.setSkuId(ordSku.getSkuId());
			int retCount = p.getConfirmCount() <= ordSku.getTotalCount() ? p.getConfirmCount() : ordSku.getTotalCount();
			rosParam.setRetCount(retCount);
			rosParam.setReason("");
			retParamList.add(rosParam);
		}
		return compute(ordPkgDTO, retParamList);
	}
	
	/**
	 * ReturnPackage退款价格参数
	 *
	 */
	public static class _ReturnPackagePriceParam {

		/**
		 * ReturnOrderSku退款价格参数
		 *
		 */
		public static class RetOrdSkuPriceParam {
			// 退货数量
			private int count;
			
			// OrderSkuDTO
			private OrderSkuDTO ordSku;
			
			private RetOrdSkuPriceParam() {
			}

			public int getCount() {
				return count;
			}

			public OrderSkuDTO getOrdSku() {
				return ordSku;
			}

		}
		
		// 退款商品总金额
		private BigDecimal returnTotalPrice = BigDecimal.ZERO;
		
		// 红包退款金额
		private BigDecimal returnHbPrice = BigDecimal.ZERO;
		
		// 用户商品金额
		private BigDecimal returnCashPrice = BigDecimal.ZERO;
		
		// "红包（回寄运费补帖）"
		private BigDecimal expSubsidyPrice = BigDecimal.TEN;
		
		// 申请退货的包裹
		private OrderPackageSimpleDTO ordPkgDTO;
		
		/**
		 * ReturnOrderSku退款价格参数
		 * key: orderSkuId, value: RetOrdSkuPriceParam
		 */
		private Map<Long, RetOrdSkuPriceParam> retOrdSkuPriceMap = new HashMap<Long, RetOrdSkuPriceParam>();
		
		public _ReturnPackagePriceParam() {
		}

		public BigDecimal getReturnTotalPrice() {
			return returnTotalPrice;
		}

		public BigDecimal getReturnHbPrice() {
			return returnHbPrice;
		}

		public BigDecimal getReturnCashPrice() {
			return returnCashPrice;
		}

		public BigDecimal getExpSubsidyPrice() {
			return expSubsidyPrice;
		}

		public OrderPackageSimpleDTO getOrdPkgDTO() {
			return ordPkgDTO;
		}

		public Map<Long, RetOrdSkuPriceParam> getRetOrdSkuPriceMap() {
			return retOrdSkuPriceMap;
		}

	}
}
