package com.xyl.mmall.order.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.netease.print.common.constant.ConstValue;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.ExtInfoFieldUtil;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderPackageRefundTaskDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.OrderCancelInfo;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderPackageRefundTask;

/**
 * @author dingmingliang
 * 
 */
public class OrderApiUtil {

	/**
	 * OrderPackageDTO->OrderPackageSimpleDTO
	 * 
	 * @param ordPkg
	 * @return
	 */
	public static OrderPackageSimpleDTO convertToOrderPackageSimpleDTO(OrderPackageDTO ordPkg) {
		if (null == ordPkg) {
			return null;
		}
		OrderPackageSimpleDTO ret = new OrderPackageSimpleDTO(ordPkg);
		List<? extends OrderCartItemDTO> orderCartItemDTOList = ordPkg.getOrderCartItemDTOList();
		if (!CollectionUtil.isEmptyOfList(orderCartItemDTOList)) {
			for (OrderCartItemDTO oct : orderCartItemDTOList) {
				List<? extends OrderSkuDTO> ordSkuList = null;
				if (null == oct || CollectionUtil.isEmptyOfList(ordSkuList = oct.getOrderSkuDTOList())) {
					continue;
				}
				for (OrderSkuDTO ordSku : ordSkuList) {
					if (null == ordSku) {
						continue;
					}
					ret.getOrderSkuMap().put(ordSku.getId(), ordSku);
				}
			}
		}
		ret.setOrderExpInfo(ordPkg.getOrderExpInfoDTO());
		return ret;
	}

	/**
	 * 通过OrderCartItemDTO,计算OrderFormCalDTO上的价格信息
	 * 
	 * @param orderFormCalDTO
	 */
	public static void resetCartPrice(OrderFormCalDTO orderFormCalDTO) {
		BigDecimal cartRPrice = BigDecimal.ZERO, cartOriRPrice = BigDecimal.ZERO;
		for (OrderCartItemDTO orderCartItem : orderFormCalDTO.getOrderCartItemDTOList()) {
			cartRPrice = cartRPrice.add(orderCartItem.getRetailPrice());
			cartOriRPrice = cartOriRPrice.add(orderCartItem.getOriginalPrice());
		}
		orderFormCalDTO.setCartRPrice(cartRPrice);
		orderFormCalDTO.setCartOriRPrice(cartOriRPrice);
	}

	/**
	 * 尝试将OrderFormPayMethod转换成TradeItemPayMethod
	 * 
	 * @param orderFormPayMethod
	 * @return
	 */
	public static TradeItemPayMethod convertToTradeItemPayMethod(OrderFormPayMethod orderFormPayMethod) {
		if (orderFormPayMethod == OrderFormPayMethod.ALIPAY)
			return TradeItemPayMethod.ALIPAY;
		if (orderFormPayMethod == OrderFormPayMethod.EPAY)
			return TradeItemPayMethod.EPAY;
		if (orderFormPayMethod == OrderFormPayMethod.COD)
			return TradeItemPayMethod.COD;
		return null;
	}

	/**
	 * 计算允许货到付款的金额
	 * 
	 * @param order
	 * @return
	 */
	public static BigDecimal calPriceForCanCOD(OrderForm order) {
		BigDecimal price = BigDecimal.ZERO;
		price = order.getCartRPrice();
		return price;
	}

	/**
	 * 计算用户需要用现金支付的交易里,体现的商品总额
	 * 
	 * @param orderCalDTO
	 * @return
	 */
	public static BigDecimal calRealCashPayWithoutEPrice(OrderFormCalDTO orderCalDTO) {
		BigDecimal price = BigDecimal.ZERO;
		price = orderCalDTO.getRealCash().subtract(orderCalDTO.getExpUserPrice(), ConstValue.MC)
				.setScale(2, RoundingMode.HALF_UP);
		return price;
	}

	/**
	 * 判断订单是否属于未付款(订单取消逻辑专用)
	 * 
	 * @param order
	 * @return false: 根据支付方式判断(COD都算未付款);订单状态为等待付款
	 */
	public static boolean isUnpayForOrderCancel(OrderForm order) {
		// 1.根据支付方式判断(COD都算未付款)
		if (order.getOrderFormPayMethod() == OrderFormPayMethod.COD) {
			return false;
		}
		// 2.根据订单状态判断: 订单状态是未付款的
		OrderFormState ostate = order.getOrderFormState();
		if (ostate == OrderFormState.WAITING_PAY)
			return false;
		// 3.默认返回值
		return true;
	}

	/**
	 * 是否需要通知oms取消订单
	 * 
	 * @param orderFormState
	 * @return
	 */
	public static boolean needCancelOMSOrder(OrderFormState orderFormState) {
		return orderFormState == OrderFormState.WAITING_DELIVE
				|| orderFormState == OrderFormState.WAITING_CANCEL_OMSORDER;
	}

	/**
	 * 判断订单是否属于0元支付
	 * 
	 * @param order
	 * @return
	 */
	public static boolean isRealCashZero(OrderFormCalDTO order) {
		boolean isRealCashZero = order != null && order.getRealCash().compareTo(BigDecimal.ZERO) <= 0;
		return isRealCashZero;
	}

	/**
	 * 取消订单的时候,是否要验证OMS的状态
	 * 
	 * @param orderState
	 * @return
	 */
	public static boolean needCheckOMSWhenCancelOrder(OrderFormState orderState) {
		OrderFormState[] orderStateArrayOfCanCancel = new OrderFormState[] { OrderFormState.WAITING_DELIVE,
				OrderFormState.PART_DELIVE };
		return CollectionUtil.isInArray(orderStateArrayOfCanCancel, orderState);
	}

	/**
	 * 判断是否需要取消交易
	 * 
	 * @param cancelDTO
	 * @return
	 */
	public static boolean needCancelTrade(OrderCancelInfoDTO cancelDTO) {
		int cancelIdx = OrderCancelInfo.IDX_CANCEL_TRADE;
		return needCancelByRetryFlag(cancelDTO, cancelIdx);
	}

	/**
	 * 判断是否需要取消OMS订单
	 * 
	 * @param cancelDTO
	 * @return
	 */
	public static boolean needCancelOMSOrder(OrderCancelInfoDTO cancelDTO) {
		int cancelIdx = OrderCancelInfo.IDX_CANCEL_OMSORDER;
		return needCancelByRetryFlag(cancelDTO, cancelIdx);
	}

	/**
	 * 判断是否要调用网易宝的退款接口
	 * 
	 * @param taskDTO
	 * @return
	 */
	public static boolean needRefundWYBCash(OrderPackageRefundTaskDTO taskDTO) {
		int flagIdx = OrderPackageRefundTask.IDX_REFUND_WYB;
		return needDoByRetryFlag(taskDTO.getRetryFlag(), flagIdx);
	}

	/**
	 * 判断是否要调用红包的退款接口
	 * 
	 * @param taskDTO
	 * @return
	 */
	public static boolean needRefundRedCash(OrderPackageRefundTaskDTO taskDTO) {
		int flagIdx = OrderPackageRefundTask.IDX_REFUND_HB;
		return needDoByRetryFlag(taskDTO.getRetryFlag(), flagIdx);
	}

	/**
	 * @param cancelDTO
	 * @param cancelIdx
	 * @return
	 */
	private static boolean needCancelByRetryFlag(OrderCancelInfoDTO cancelDTO, int cancelIdx) {
		boolean needCancel = CollectionUtil.getValueOfMap(
				ExtInfoFieldUtil.parseValueOfType1ToMap(cancelDTO.getRetryFlag()), cancelIdx) == Boolean.TRUE;
		return needCancel;
	}

	/**
	 * @param retryFlag
	 * @param flagIdx
	 * @return
	 */
	private static boolean needDoByRetryFlag(long retryFlag, int flagIdx) {
		boolean needCancel = CollectionUtil.getValueOfMap(ExtInfoFieldUtil.parseValueOfType1ToMap(retryFlag), flagIdx) == Boolean.TRUE;
		return needCancel;
	}
}
