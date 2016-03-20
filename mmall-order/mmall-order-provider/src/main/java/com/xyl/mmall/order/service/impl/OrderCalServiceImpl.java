package com.xyl.mmall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.enums.CartItemType;
import com.xyl.mmall.framework.enums.ComposeOrderErrorCodeEnum;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.api.util.OrderApiUtil;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderFormPayMethodDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderSkuCartItemDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.tcc.SkuOrderStockTCC;
import com.xyl.mmall.order.param.OrderCalServiceGenExpPriceParam;
import com.xyl.mmall.order.param.OrderCalServiceGenOrderParam;
import com.xyl.mmall.order.param.SkuParam;
import com.xyl.mmall.order.result.OrderCalServiceGenOrderResult;
import com.xyl.mmall.order.service.CODAuditService;
import com.xyl.mmall.order.service.ConsigneeAddressService;
import com.xyl.mmall.order.service.OrderCalService;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * @author dingmingliang
 * 
 */
@Service("orderCalService")
public class OrderCalServiceImpl implements OrderCalService {

	@Autowired
	ConsigneeAddressService consigneeAddressService;

	@Autowired
	SkuOrderStockService skuOrderStockService;

	@Autowired
	CODAuditService codAuditService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderCalService#genOrder(com.xyl.mmall.order.param.OrderCalServiceGenOrderParam)
	 */
	@SuppressWarnings("unchecked")
	public OrderCalServiceGenOrderResult genOrder(OrderCalServiceGenOrderParam param) {
		OrderCalServiceGenOrderResult result = new OrderCalServiceGenOrderResult();
		// 输入参数判断
		if (param == null) {
			result.setResultCode(ComposeOrderErrorCodeEnum.ORDER_PARAM_ERROR);
			return result;
		}

		try {
			long userId = param.getUserId(), currTime = System.currentTimeMillis();
			// 1.设置订单的基本属性
			OrderFormCalDTO orderFormCalDTO = new OrderFormCalDTO();
			// orderFormCalDTO.setCanCODBySku(param.isCanCODBySku());
			// 目前全部支持货到付款
			orderFormCalDTO.setCanCODBySku(true);
			orderFormCalDTO.setUserId(userId);
			orderFormCalDTO.setOrderTime(currTime);
			orderFormCalDTO.setOrderFormSource(param.getOrderFormSource());
			orderFormCalDTO.setOrderFormPayMethod(param.getOrderFormPayMethod());
			orderFormCalDTO.setInvoiceInOrdDTO(genInvoiceInOrd(param));
			orderFormCalDTO.setAgentId(param.getAgentId());
			orderFormCalDTO.setSpSource(param.getSpSource());
			if (orderFormCalDTO.getInvoiceInOrdDTO() != null)
				orderFormCalDTO.getInvoiceInOrdDTO().setOrderTime(currTime);
			orderFormCalDTO.setProvinceId(param.getProvinceId());
			if (orderFormCalDTO.getOrderFormSource() == null) {
				result.setResultCode(ComposeOrderErrorCodeEnum.ORDER_PARAM_ERROR);
				return result;
			}

			// 2.生成OrderExpInfoDTO
			// 读取用户的收货地址
			ConsigneeAddressDTO caDTO = param.getCaDTO();
			if (caDTO == null) {
				result.setResultCode(ComposeOrderErrorCodeEnum.ORDER_ADDRESS_NULL);
				return result;
			}
			OrderExpInfoDTO orderExpInfo = convertToOrderExpInfoDTO(caDTO);
			orderExpInfo.setUserId(userId);
			orderFormCalDTO.setOrderExpInfoDTO(orderExpInfo);

			// 3.根据SkuParam,生成OrderCartItem
			List<OrderCartItemDTO> cartList = genCartList(userId, param.getSkuParamList());
			if (CollectionUtil.isEmptyOfCollection(cartList)) {
				result.setResultCode(ComposeOrderErrorCodeEnum.ORDER_BTACH_DISSTISFY);
				return result;
			}
			orderFormCalDTO.setOrderCartItemDTOList(cartList);
			// 通过OrderCartItemDTO,计算orderFormCalDTO上的价格信息
			OrderApiUtil.resetCartPrice(orderFormCalDTO);

			// 4.生成OrderPackage
			// List<OrderPackageDTO> orderPackageDTOList =
			// composeOrderPackageList(param, orderFormCalDTO);
			// orderFormCalDTO.setOrderPackageDTOList(orderPackageDTOList);

			// 5.根据订单包裹里的产品信息,重新设置快递方式+快递费(包含全场包邮)
			// resetExpPriceInfo(orderFormCalDTO, param);

			// 6.计算需要付款的金额
			BigDecimal totalCash = orderFormCalDTO.getCartRPrice().add(orderFormCalDTO.getExpUserPrice());
			orderFormCalDTO.setTotalCash(totalCash);
			if (totalCash.compareTo(new BigDecimal(100000000l)) > 0) {
				result.setResultCode(ComposeOrderErrorCodeEnum.ORDER_CASH_EXCEED);// 总价最大1亿
				return result;
			}
			orderFormCalDTO.setRedCash(param.getHbSOrderPrice());
			orderFormCalDTO.setRealCash(orderFormCalDTO.getTotalCash().subtract(orderFormCalDTO.getRedCash()));

			// 7.设置订单可选的支付方式(同时重置用户选择无效的支付方式)
			setPayMethodInfo(param, orderFormCalDTO, orderExpInfo);
			param.setOrderFormPayMethod(orderFormCalDTO.getOrderFormPayMethod());

			// 8.设置订单的状态+订单的支付状态
			setOrderFormStateInfo(orderFormCalDTO);

			// 9.校验库存
			RetArg retArgOfCS = checkStock(orderFormCalDTO);
			if (!RetArgUtil.get(retArgOfCS, Boolean.class)) {
				result.setResultCode(ComposeOrderErrorCodeEnum.ORDER_STOCK_LACK);
				result.setOutStockSkuMap(RetArgUtil.get(retArgOfCS, TreeMap.class));
				return result;
			}

			// 设置返回结果
			result.setOrderFormCalDTO(orderFormCalDTO);
			result.setResultCode(ComposeOrderErrorCodeEnum.ORDER_SUCCESS);
		} catch (Exception ex) {
			logger.error(param.toString(), ex);
			result.setOrderFormCalDTO(null);
			result.setResultCode(ComposeOrderErrorCodeEnum.ORDER_FAIL);
		}
		return result;
	}

	/**
	 * 设置订单可选的支付方式
	 * 
	 * @param orderFormCalDTO
	 * @param orderExpInfo
	 */
	private void setPayMethodInfo(OrderCalServiceGenOrderParam param, OrderFormCalDTO orderFormCalDTO,
			OrderExpInfoDTO orderExpInfo) {
		long userId = orderFormCalDTO.getUserId();
		// 1.计算全部的商品金额
		BigDecimal totalRPrice = OrderApiUtil.calPriceForCanCOD(orderFormCalDTO);
		// 2.设置支付方式列表
		orderFormCalDTO.setPaymethodArray(genOrderFormPayMethodDTOArray(userId, orderExpInfo, totalRPrice));
		// 产品配送方式不支持货到付款的特殊处理
		// 目前全部支持货到
		// if (!param.isCanCODBySku()) {
		// for (OrderFormPayMethodDTO payMethodDTOOfTmp :
		// orderFormCalDTO.getPaymethodArray()) {
		// if (payMethodDTOOfTmp.getPayMethod() == OrderFormPayMethod.COD) {
		// payMethodDTOOfTmp.setValid(false);
		// payMethodDTOOfTmp.setInvalidMess(ConstValueOfOrder.COD_INVALIDMESS_SKU);
		// }
		// }
		// }
		// 3.设置选中的支付方式
		OrderFormPayMethodDTO payMethodDTOOfDefault = null, payMethodDTOOfSelected = null;
		for (OrderFormPayMethodDTO payMethodDTOOfTmp : orderFormCalDTO.getPaymethodArray()) {
			if (!payMethodDTOOfTmp.isValid())
				continue;
			if (payMethodDTOOfDefault == null) {
				payMethodDTOOfDefault = payMethodDTOOfTmp;
			}
			if (payMethodDTOOfTmp.getPayMethod() == orderFormCalDTO.getOrderFormPayMethod()) {
				payMethodDTOOfSelected = payMethodDTOOfTmp;
			}
		}
		orderFormCalDTO.setOrderFormPayMethod(payMethodDTOOfSelected != null ? payMethodDTOOfSelected.getPayMethod()
				: payMethodDTOOfDefault.getPayMethod());

	}

	/**
	 * 设置订单的状态+订单的支付状态
	 * 
	 * @param orderFormCalDTO
	 */
	private void setOrderFormStateInfo(OrderFormCalDTO orderFormCalDTO) {
		// 1.读取支付方式
		OrderFormPayMethod payMethod = orderFormCalDTO.getOrderFormPayMethod();
		// 2.判断订单是否是0元
		boolean isRealCashBigZero = orderFormCalDTO.getRealCash().compareTo(BigDecimal.ZERO) <= 0;
		// 3.设置订单的状态
		OrderFormState orderFormState = payMethod == OrderFormPayMethod.COD ? OrderFormState.WAITING_DELIVE
				: isRealCashBigZero ? OrderFormState.WAITING_DELIVE : OrderFormState.WAITING_PAY;
		orderFormCalDTO.setOrderFormState(orderFormState);
		// 4.设置订单的支付状态
		long payTime = 0;
		PayState payState = null;
		if (isRealCashBigZero) {
			payState = PayState.ONLINE_PAYED;
			payTime = orderFormCalDTO.getOrderTime();
		} else if (orderFormState == OrderFormState.WAITING_DELIVE) {
			payState = PayState.COD_NOT_PAY;
		} else {
			payState = PayState.ONLINE_NOT_PAY;
		}
		orderFormCalDTO.setPayState(payState);
		orderFormCalDTO.setPayTime(payTime);
	}

	/**
	 * 生成发票
	 * 
	 * @param param
	 * @return
	 */
	private InvoiceInOrdDTO genInvoiceInOrd(OrderCalServiceGenOrderParam param) {
		String title = param.getInvoiceTitle();
		if (StringUtils.isBlank(title))
			return null;

		InvoiceInOrdDTO obj = new InvoiceInOrdDTO();
		obj.setState(InvoiceInOrdState.INIT);
		obj.setTitle(title);
		obj.setUserId(param.getUserId());
		return obj;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderCalService#genOrderFormPayMethodDTOArray(long,
	 *      com.xyl.mmall.order.dto.OrderExpInfoDTO, java.math.BigDecimal)
	 */
	public OrderFormPayMethodDTO[] genOrderFormPayMethodDTOArray(long userId, OrderExpInfoDTO orderExpInfo,
			BigDecimal totalRPrice) {
		// 1.生成全部的支付方式
		OrderFormPayMethodDTO payMethodDTOOfCOD = null;
		List<OrderFormPayMethodDTO> payMethodList = new ArrayList<>();
		for (OrderFormPayMethod payMethod : OrderFormPayMethod.values()) {
			OrderFormPayMethodDTO payMethodDTO = new OrderFormPayMethodDTO();
			payMethodDTO.setPayMethod(payMethod);
			payMethodList.add(payMethodDTO);

			if (payMethod == OrderFormPayMethod.COD)
				payMethodDTOOfCOD = payMethodDTO;
		}

		// 2.判断是否可以货到付款
		// CODValidParam codValidParam = new CODValidParam();
		// codValidParam.setOrderExpInfoDTO(orderExpInfo);
		// codValidParam.setUserId(userId);
		// ConsigneeAddressDTO caDTO =
		// ReflectUtil.convertObj(ConsigneeAddressDTO.class, orderExpInfo,
		// false);
		// boolean isValidOfCOD = !codAuditService.isInBlacklist(userId, caDTO);
		// if (!isValidOfCOD) {
		// payMethodDTOOfCOD.setValid(false);
		// payMethodDTOOfCOD.setInvalidMess(ConstValueOfOrder.COD_INVALIDMESS_INBLACK);
		// }
		// if (totalRPrice.compareTo(new BigDecimal(10)) < 0 ||
		// totalRPrice.compareTo(new BigDecimal(2000)) >= 0) {
		// payMethodDTOOfCOD.setValid(false);
		// payMethodDTOOfCOD.setInvalidMess(ConstValueOfOrder.COD_INVALIDMESS_AMOUNT);
		// }

		// 返回结果
		OrderFormPayMethodDTO[] payMethodArray = payMethodList.toArray(new OrderFormPayMethodDTO[payMethodList.size()]);
		return payMethodArray;
	}

	/**
	 * 生成OrderPackage(暂时只实现单包裹的情况)
	 * 
	 * @param param
	 * @param orderFormCalDTO
	 * @return
	 */
	private List<OrderPackageDTO> composeOrderPackageList(OrderCalServiceGenOrderParam param,
			OrderFormCalDTO orderFormCalDTO) {
		// 0.参数准备
		long userId = orderFormCalDTO.getUserId();
		OrderExpInfoDTO orderExpInfoDTO = orderFormCalDTO.getOrderExpInfoDTO();
		// 1.计算用户选择的运费
		ExpressCompany expressCompany = ExpressCompany.DEFAULT_EXPRESS;

		// 2.生成OrderPackage
		OrderPackageDTO orderPackage = new OrderPackageDTO();
		orderPackage.setExpressCompany(expressCompany);
		orderPackage.setOrderCartItemDTOList(orderFormCalDTO.getOrderCartItemDTOList());
		orderPackage.setOrderExpInfoDTO(orderExpInfoDTO);
		orderPackage.setUserId(userId);

		return CollectionUtil.addOfList(null, orderPackage);
	}

	/**
	 * 校验库存
	 * 
	 * @param orderFormDTO
	 */
	private RetArg checkStock(OrderFormDTO orderFormDTO) {
		RetArg retArg = new RetArg();
		// 1.根据订单的OrderSku明细,生成Sku库存TCC数据
		long currTime = -1, tranId = -1;
		Map<Long, SkuOrderStockTCC> skuOrderStockTCCMap = OrderUtil.genSkuOrderStockTCCMap(orderFormDTO, currTime,
				tranId, true);
		if (CollectionUtil.isEmptyOfMap(skuOrderStockTCCMap)) {
			RetArgUtil.put(retArg, true);
			return retArg;
		}

		// 2.读取订单服务的Sku库存
		List<SkuOrderStockDTO> skuOrderStockDTOList = skuOrderStockService
				.getSkuOrderStockDTOListBySkuIds(skuOrderStockTCCMap.keySet());
		if (CollectionUtil.isEmptyOfCollection(skuOrderStockDTOList)) {
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 3.判断本次下单需要的订单库存是否充足
		Map<Long, Integer> outStockSkuMap = new TreeMap<>();
		Map<Long, SkuOrderStockDTO> skuOrderStockDTOMap = CollectionUtil
				.convertCollToMap(skuOrderStockDTOList, "skuId");
		for (Long skuId : skuOrderStockTCCMap.keySet()) {
			SkuOrderStockDTO skuOrderStockDTO = skuOrderStockDTOMap.get(skuId);
			SkuOrderStockTCC skuOrderStockTCC = skuOrderStockTCCMap.get(skuId);
			// 保存库存不足的结果
			if (skuOrderStockDTO.getStockCount() < skuOrderStockTCC.getStockCount())
				outStockSkuMap.put(skuId, skuOrderStockDTO.getStockCount());
		}
		// 保存返回结果
		if (CollectionUtil.isNotEmptyOfMap(outStockSkuMap)) {
			RetArgUtil.put(retArg, false);
			RetArgUtil.put(retArg, outStockSkuMap);
		} else
			RetArgUtil.put(retArg, true);
		return retArg;
	}

	/**
	 * 根据SkuParam,生成OrderCartItem
	 * 
	 * @param userId
	 * @param skuParamList
	 * @return
	 */
	private List<OrderCartItemDTO> genCartList(long userId, List<SkuParam> skuParamList) {
		if (CollectionUtil.isEmptyOfCollection(skuParamList))
			return null;

		// 1.生成OrderCartItemDTO集合
		List<OrderCartItemDTO> cartList = new ArrayList<>();
		Map<Long, List<SkuParam>> supplierSkuMap = new HashMap<Long, List<SkuParam>>();
		for (SkuParam skuParam : skuParamList) {
			Long supplierId = Long.valueOf(skuParam.getSupplierId());
			if (supplierSkuMap.containsKey(supplierId)) {
				supplierSkuMap.get(supplierId).add(skuParam);
			} else {
				List<SkuParam> list = new ArrayList<SkuParam>();
				list.add(skuParam);
				supplierSkuMap.put(supplierId, list);
			}
		}

		// 每家商店，分别进行两步验证是否能够下单
		// 如果第一步验证通过，验证第二步
		// 如果第二步验证失败，则返回null
		for (Map.Entry<Long, List<SkuParam>> entry : supplierSkuMap.entrySet()) {
			// 第一步：计算用户在每个商店的的消费总金额是否大于起批金额
			BigDecimal sum = new BigDecimal(0);
			for (SkuParam skuParam : entry.getValue()) {
				sum = sum.add(skuParam.getOriRPrice().multiply(new BigDecimal(skuParam.getCount())));
			}

			BigDecimal batchCash = entry.getValue().get(0).getBatchCash();
			if (batchCash == null || sum.compareTo(batchCash) == -1) {
				// 第一步验证失败：该商店的商品总额不满最少起批金额
				return null;
			}

			// 第二步：判断每种商品是否满足起批数量
			for (SkuParam skuParam : entry.getValue()) {
				// 销售价为0意味着不满足起批价格
				if (skuParam.getSalePrice().equals(BigDecimal.ZERO)) {
					return null;
				}
			}
			// else
			// {
			// // skuParam中SalePrice设置成0的原因是：单个商品不满足起批数量
			// // 第一步验证成功，满足商店的起批金额，将skuParam中的SalePrice重新设置为起批价格
			// for (SkuParam skuParam : entry.getValue())
			// {
			// skuParam.setSalePrice(skuParam.getOriRPrice());
			// }
			// }
		}

		// 验证通过
		for (SkuParam skuParam : skuParamList) {
			// 1.1 生成OrderSkuDTO
			List<OrderSkuDTO> orderSkuDTOList = new ArrayList<>();
			OrderSkuDTO orderSkuDTO = genOrderSkuDTOBySkuParam(userId, skuParam);
			orderSkuDTOList.add(orderSkuDTO);

			// 1.2 生成OrderCartItemDTO
			OrderCartItemDTO OrderCartItemDTO = genOrderCartItemDTO(userId, skuParam, orderSkuDTOList);
			cartList.add(OrderCartItemDTO);
		}

		return cartList;
	}

	/**
	 * 根据SkuParam,生成OrderSkuDTO
	 * 
	 * @param userId
	 * @param skuParam
	 * @return
	 */
	private OrderSkuDTO genOrderSkuDTOBySkuParam(long userId, SkuParam skuParam) {
		long skuId = skuParam.getSkuId();
		OrderSkuDTO orderSkuDTO = new OrderSkuDTO();
		orderSkuDTO.setRprice(skuParam.getSalePrice());
		orderSkuDTO.setOriRPrice(skuParam.getOriRPrice());
		orderSkuDTO.setProductId(skuParam.getProductId());
		orderSkuDTO.setMarketPrice(skuParam.getMarketPrice());
		orderSkuDTO.setSkuId(skuId);
		orderSkuDTO.setTotalCount(skuParam.getCount());
		orderSkuDTO.setPoId(skuParam.getPoId());
		orderSkuDTO.setSupplierId(skuParam.getSupplierId());
		orderSkuDTO.setUserId(userId);
		orderSkuDTO.setCouponSPrice(skuParam.getCouponSPrice());
		orderSkuDTO.setHdSPrice(skuParam.getHdSPrice());
		orderSkuDTO.setRedSPrice(skuParam.getRedSPrice());

		return orderSkuDTO;
	}

	/**
	 * 根据SkuParam+OrderSkuDTOList,生成OrderCartItemDTO
	 * 
	 * @param userId
	 * @param skuParam
	 * @param orderSkuDTOList
	 * @return
	 */
	private OrderCartItemDTO genOrderCartItemDTO(long userId, SkuParam skuParam, List<OrderSkuDTO> orderSkuDTOList) {
		// 生成OrderSkuCartItemDTO
		OrderSkuCartItemDTO orderSkuCartItemDTO = new OrderSkuCartItemDTO();
		orderSkuCartItemDTO.setOrderCartItemId(0);
		orderSkuCartItemDTO.setSkuId(skuParam.getSkuId());
		orderSkuCartItemDTO.setUserId(userId);

		// 计算价格
		BigDecimal originalPrice = BigDecimal.ZERO, retailPrice = BigDecimal.ZERO;
		for (OrderSkuDTO orderSkuDTO : orderSkuDTOList) {
			BigDecimal count = BigDecimal.valueOf(orderSkuDTO.getTotalCount());
			originalPrice = originalPrice.add(orderSkuDTO.getOriRPrice().multiply(count));
			retailPrice = retailPrice.add(orderSkuDTO.getRprice().multiply(count));
		}

		// 组装OrderCartItemDTO
		OrderCartItemDTO orderCartItemDTO = new OrderCartItemDTO();
		orderCartItemDTO.setCartType(CartItemType.CART_SKU);
		orderCartItemDTO.setCount(skuParam.getCount());
		orderCartItemDTO.setOrderSkuDTOList(orderSkuDTOList);
		orderCartItemDTO.setOriginalPrice(originalPrice);
		orderCartItemDTO.setRetailPrice(retailPrice);
		orderCartItemDTO.setUserId(userId);
		orderCartItemDTO.setOrderSkuCartItemDTO(orderSkuCartItemDTO);
		orderCartItemDTO.setPromotionId(skuParam.getPromotionId());
		orderCartItemDTO.setPromotionIdx(skuParam.getPromotionIdx());

		return orderCartItemDTO;
	}

	/**
	 * 将【收货地址】对象转换成【订单快递地址信息】对象
	 * 
	 * @param caDTO
	 * @return
	 */
	private OrderExpInfoDTO convertToOrderExpInfoDTO(ConsigneeAddressDTO caDTO) {
		OrderExpInfoDTO orderExpInfo = ReflectUtil.convertObj(OrderExpInfoDTO.class, caDTO, false);
		orderExpInfo.setOrderId(0);
		if (StringUtils.isBlank(orderExpInfo.getZipcode()))
			orderExpInfo.setZipcode("");
		if (StringUtils.isBlank(orderExpInfo.getConsigneeMobile()))
			orderExpInfo.setConsigneeMobile("");
		if (StringUtils.isBlank(orderExpInfo.getConsigneeTel()))
			orderExpInfo.setConsigneeTel("");
		return orderExpInfo;
	}

	/**
	 * 根据订单包裹里的产品信息,重新设置快递费(包含全场包邮)
	 * 
	 * @param orderFormCalDTO
	 * @param param
	 */
	private void resetExpPriceInfo(OrderFormCalDTO orderFormCalDTO, OrderCalServiceGenOrderParam param) {
		OrderPackageDTO orderPackageDTO = CollectionUtil.getFirstObjectOfCollection(orderFormCalDTO
				.getOrderPackageDTOList());
		// // 1.计算可选的快递方式
		// ExpressCompany[] expressArray = ExpressCompany.values();
		// orderFormCalDTO.setExpressArray(expressArray);
		// ExpressCompany expressCompany = orderPackageDTO.getExpressCompany();
		// if (!CollectionUtil.isInArray(expressArray, expressCompany))
		// expressCompany = expressArray[0];

		// 2.计算用户选择的运费
		BigDecimal[] expPriceArray = calExpPrice(orderFormCalDTO.getCartRPrice(), param.getExpPrice(),
				param.isFreeExpPrice());
		BigDecimal expOriPrice = expPriceArray[0], expUserPrice = expPriceArray[1], expSysPayPrice = expPriceArray[2];
		// 包邮标记
		if (expUserPrice.compareTo(BigDecimal.ZERO) <= 0) {
			orderFormCalDTO.setFreeExp(true);
		}

		// 3.更新订单上的邮费
		orderFormCalDTO.setExpOriPrice(expOriPrice);
		orderFormCalDTO.setExpUserPrice(expUserPrice);
		orderFormCalDTO.setExpSysPayPrice(expSysPayPrice);
		orderFormCalDTO.setExpUserPriceOfRed(param.getHbSExpPrice());
		orderPackageDTO.setExpressCompany(ExpressCompany.DEFAULT_EXPRESS);
		orderFormCalDTO.setExpressCompany(ExpressCompany.DEFAULT_EXPRESS);
	}

	/**
	 * 计算邮费
	 * 
	 * @param rprice
	 * @param expPrice
	 * @param isFreeExpPrice
	 * @return [0]: 原始邮费<br>
	 *         [1]: 用户支付的邮费<br>
	 *         [2]: 系统垫付的邮费
	 */
	private BigDecimal[] calExpPrice(BigDecimal rprice, BigDecimal expPrice, boolean isFreeExpPrice) {
		boolean isValidOfPrice = false;
		BigDecimal expOriPrice = expPrice, expUserPrice = expOriPrice;

		if (ConstValueOfOrder.FREE_EXP_RPRICE != null)
			isValidOfPrice = rprice.compareTo(ConstValueOfOrder.FREE_EXP_RPRICE) >= 0;
		if (isValidOfPrice || isFreeExpPrice) {
			expUserPrice = BigDecimal.ZERO;
		}
		BigDecimal expSysPayPrice = expOriPrice.subtract(expUserPrice);

		return new BigDecimal[] { expOriPrice, expUserPrice, expSysPayPrice };
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderCalService#genExpUserPrice(com.xyl.mmall.order.param.OrderCalServiceGenExpPriceParam)
	 */
	@Override
	public BigDecimal genExpUserPrice(OrderCalServiceGenExpPriceParam param) {
		List<SkuParam> skuParamList = param.getSkuParamList();
		// 1.计算商品总金额
		BigDecimal retailPrice = BigDecimal.ZERO;
		for (SkuParam skuParam : skuParamList) {
			BigDecimal count = BigDecimal.valueOf(skuParam.getCount());
			retailPrice = retailPrice.add(skuParam.getSalePrice().multiply(count));
		}
		// 2.计算邮费
		BigDecimal[] expPriceArray = calExpPrice(retailPrice, param.getExpPrice(), param.isFreeExpPrice());
		BigDecimal expUserPrice = expPriceArray[1];
		return expUserPrice;
	}
}
