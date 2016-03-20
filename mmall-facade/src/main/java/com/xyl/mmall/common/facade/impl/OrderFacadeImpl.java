package com.xyl.mmall.common.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.dubbo.rpc.RpcContext;
import com.netease.backend.tcc.TccActivity;
import com.netease.backend.tcc.TccManager;
import com.netease.backend.tcc.Transaction;
import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.print.common.constant.CalendarConst;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatEnum;
import com.netease.print.common.util.ExtInfoFieldUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.backend.vo.ProdPicVO;
import com.xyl.mmall.backend.vo.ProdSpeciBackendVO;
import com.xyl.mmall.backend.vo.ProductPriceVO;
import com.xyl.mmall.backend.vo.ProductSKUBackendVO;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.enums.OrderQueryType;
import com.xyl.mmall.common.facade.BrandQueryFacade;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.facade.POProductQueryFacade;
import com.xyl.mmall.common.facade.TradeFacade;
import com.xyl.mmall.common.param.OrderFacadeComposeOrderParam;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.util.UrlBaseUtil;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ItemSPUService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.mainsite.facade.CalCenterFacade;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mainsite.vo.CartInnervVO;
import com.xyl.mmall.mainsite.vo.order.OrderFormListVO;
import com.xyl.mmall.oms.dto.OmsConsigneeAddressParam;
import com.xyl.mmall.oms.dto.OmsExpPriceParam;
import com.xyl.mmall.oms.dto.OmsSkuParam;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.facade.OmsFeeFacade;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.order.api.util.OrderApiUtil;
import com.xyl.mmall.order.api.util.TradeApiUtil;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderCartItemBriefDTO;
import com.xyl.mmall.order.dto.OrderCartItemDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderLogisticsDTO;
import com.xyl.mmall.order.dto.OrderPackageBriefDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.dto.SkuPriceDTO;
import com.xyl.mmall.order.dto.SkuSPDTO;
import com.xyl.mmall.order.dto.SkuSpeciDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.CancelOmsOrderTaskState;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.OrderCancelInfo;
import com.xyl.mmall.order.param.OrderCalServiceGenExpPriceParam;
import com.xyl.mmall.order.param.OrderCalServiceGenOrderParam;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.param.SkuParam;
import com.xyl.mmall.order.param.TryAddOrderByTCCParam;
import com.xyl.mmall.order.result.OrderCalServiceGenOrderResult;
import com.xyl.mmall.order.result.TryAddOrderByTCCResult;
import com.xyl.mmall.order.service.CODAuditService;
import com.xyl.mmall.order.service.InvoiceService;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderCalService;
import com.xyl.mmall.order.service.OrderMiscService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.OrderTCCService;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.dto.RedPacketParam;
import com.xyl.mmall.promotion.dto.TCCParamDTO;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.RebateService;
import com.xyl.mmall.promotion.service.tcc.ActivityTCCService;
import com.xyl.mmall.promotion.service.tcc.RecycleTCCService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.service.ProductSKULimitService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * @author dingmingliang
 * 
 */
@Facade
public class OrderFacadeImpl implements OrderFacade {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderMiscService orderMiscService;

	@Autowired
	private OrderCalService orderCalService;

	@Autowired
	private OrderTCCService orderTCCService;

	@Autowired
	private ActivityTCCService activityTCCService;

	@Autowired
	private RecycleTCCService recycleTCCService;

	@Autowired
	private OmsOrderFormService omsOrderFormService;

	@Autowired
	private BrandQueryFacade brandQueryFacade;

	@Resource(name = "poProductQueryFacadeXHCacheImpl")
	private POProductQueryFacade poProductQueryFacadeXHCacheImpl;

	@Autowired
	private SkuOrderStockService skuOrderStockService;

	@Autowired
	private CouponOrderService couponOrderService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private POProductService poProductService;

	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private OrderBriefService orderBriefService;

	@Autowired
	private PromotionFacade promotionFacade;

	@Autowired
	private TradeFacade tradeFacade;

	@Autowired
	private OmsFeeFacade omsFeeFacade;

	@Autowired
	private CODAuditService codAuditService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private RebateService rebateService;

	@Autowired
	private CalCenterFacade calCenterFacade;

	@Autowired
	private ItemProductService itemProductService;

	@Autowired
	private ProductFacade productFacade;

	@Autowired
	private ItemSPUService itemSPUService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private CategoryService categoryService;

	// TCC----------------
	@Autowired
	private TccManager tccManager;

	@Resource(name = "addOrderTCCActivity")
	private TccActivity addOrderTCCActivity;

	@Resource(name = "callOffOrderTCCActivity")
	private TccActivity callOffOrderTCCActivity;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private BigDecimal negativeOne = new BigDecimal("-1");

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductSKULimitService productSKULimitService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#composeOrder(com.xyl.mmall.common.param.OrderFacadeComposeOrderParam)
	 */
	public RetArg composeOrder(OrderFacadeComposeOrderParam param, PlatformType platformtype) {
		// 0.参数初始化
		long userId = param.getUserId();
		int provinceId = param.getProvinceId();
		OrderFormPayMethod orderFormPayMethod = OrderFormPayMethod.genEnumByIntValueSt(param.getPayMethodInt());

		// 1.调用购物车计算促销的接口
		FavorCaculateResultDTO fcResultDTO = genFavorCaculateResultDTOForComposeOrder(param, platformtype);

		// 2.准备组单用的参数
		// 收获地址
		ConsigneeAddressDTO caDTO = consigneeAddressFacade.getAddressById(param.getCaId(), param.getUserId());
		// 生成skuParamList
		List<SkuParam> skuParamList = genSkuParamList(fcResultDTO);
		// 活动免邮标记
		boolean isFreeExpPrice = fcResultDTO != null ? fcResultDTO.isFreeExpPrice() : false;
		// 计算订单的基本邮费金额
		// BigDecimal expPrice = genExpPriceForComposeOrder(param);

		// 3.尝试调用红包的计算逻辑
		// RedPacketParam redPacketParam =
		// genRedPacketParamForComposeOrder(param, fcResultDTO, skuParamList,
		// isFreeExpPrice, expPrice);

		// 4.调用订单服务进行组单
		// 4.1 生成OrderCalServiceGenOrderParam对象
		OrderCalServiceGenOrderParam genOrderParam = new OrderCalServiceGenOrderParam();
		// if (redPacketParam != null) {
		// skuParamList = convertSkuParamList2(skuParamList,
		// redPacketParam.getSkuParams());
		// genOrderParam.setHbSExpPrice(redPacketParam.getExpressDiscountPrice()
		// != null ? redPacketParam
		// .getExpressDiscountPrice() : BigDecimal.ZERO);
		// genOrderParam.setHbSOrderPrice(redPacketParam.getRealUsedRedPrice()
		// != null ? redPacketParam
		// .getRealUsedRedPrice() : BigDecimal.ZERO);
		// if (fcResultDTO != null) {
		// fcResultDTO.getRedPacketOrderTCCList().addAll(redPacketParam.getRedPacketOrderTCCList());
		// }
		// }
		genOrderParam.setCaDTO(caDTO);
		genOrderParam.setOrderFormPayMethod(orderFormPayMethod);
		genOrderParam.setInvoiceTitle(param.getInvoiceTitle());
		genOrderParam.setOrderFormSource(param.getSource());
		genOrderParam.setSkuParamList(skuParamList);
		genOrderParam.setUserId(userId);
		genOrderParam.setProvinceId(provinceId);
		genOrderParam.setFreeExpPrice(isFreeExpPrice);
		genOrderParam.setAgentId(param.getAgentId());
		genOrderParam.setSpSource(param.getSpSource());
		// genOrderParam.setExpPrice(expPrice);
		// 4.2 组单
		OrderCalServiceGenOrderResult genOrderResult = composeOrder(genOrderParam);

		// 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, genOrderResult);
		RetArgUtil.put(retArg, fcResultDTO);
		// RetArgUtil.put(retArg, relationResult);
		return retArg;
	}

	/**
	 * 调用购物车计算促销的接口
	 * 
	 * @param param
	 * @return
	 */
	private FavorCaculateResultDTO genFavorCaculateResultDTOForComposeOrder(OrderFacadeComposeOrderParam param,
			PlatformType platformtype) {
		// 0.参数初始化
		long userId = param.getUserId();
		Long userCounponId = param.getUserCouponId();
		int provinceId = param.getProvinceId();
		String cartIds = param.getCartIds();

		// 1.调用购物车计算促销的接口
		FavorCaculateResultDTO fcResultDTO = null;

		if (StringUtils.isNotBlank(cartIds)) {
			List<Long> filterList = new ArrayList<>();
			for (String cartId : cartIds.split(",")) {
				if (NumberUtils.isNumber(cartId))
					filterList.add(Long.valueOf(cartId));
			}
			boolean caculateCoupon = true;
			CartInnervVO cartInnervVO = cartFacade.getFavorCaculateResultDTOBySelected(userId, provinceId, filterList,
					userCounponId, caculateCoupon, platformtype);
			fcResultDTO = cartInnervVO != null && cartInnervVO.isCartIdsValid() ? cartInnervVO
					.getFavorCaculateResultDTO() : null;
		}
		return fcResultDTO;
	}

	/**
	 * 尝试调用红包的计算逻辑
	 * 
	 * @param param
	 * @param fcResultDTO
	 * @param skuParamList
	 * @param isFreeExpPrice
	 * @param expPrice
	 * @return
	 */
	private RedPacketParam genRedPacketParamForComposeOrder(OrderFacadeComposeOrderParam param,
			FavorCaculateResultDTO fcResultDTO, List<SkuParam> skuParamList, boolean isFreeExpPrice, BigDecimal expPrice) {
		BigDecimal redPackets = fcResultDTO == null ? BigDecimal.ZERO : fcResultDTO.getCanUseRedPackets();
		// 0.参数初始化
		long userId = param.getUserId();

		// 1.尝试调用红包的计算逻辑
		boolean isAutoUseRedPackets = param.getHbCash() != null && param.getHbCash().compareTo(negativeOne) == 0;
		boolean isUseRedPackets = isAutoUseRedPackets
				|| (param.getHbCash() != null && param.getHbCash().compareTo(BigDecimal.ZERO) > 0);
		boolean hasRedPackets = redPackets != null && redPackets.compareTo(BigDecimal.ZERO) > 0;
		boolean isCalRedPackets = isUseRedPackets && hasRedPackets;
		RedPacketParam redPacketParam = null;
		if (isCalRedPackets) {
			// 1.1 读取订单的邮费
			OrderCalServiceGenExpPriceParam orderCalServiceGenExpPriceParam = new OrderCalServiceGenExpPriceParam();
			orderCalServiceGenExpPriceParam.setExpPrice(expPrice);
			orderCalServiceGenExpPriceParam.setFreeExpPrice(isFreeExpPrice);
			orderCalServiceGenExpPriceParam.setSkuParamList(skuParamList);
			orderCalServiceGenExpPriceParam.setUserId(userId);

			BigDecimal expUserPrice = orderCalService.genExpUserPrice(orderCalServiceGenExpPriceParam);
			// 1.2 调用红包的计算逻辑
			redPacketParam = new RedPacketParam();
			redPacketParam.setExpressPrice(expUserPrice);
			redPacketParam.setSkuParams(convertSkuParamList1(skuParamList));
			redPacketParam.setUsedRedPrice(param.getHbCash() == null || isAutoUseRedPackets ? redPackets : param
					.getHbCash());
			redPacketParam.setUserId(userId);
			redPacketParam = calCenterFacade.caculateRedPackets(redPacketParam);
		}
		return redPacketParam;
	}

	/**
	 * 计算订单的基本邮费金额
	 * 
	 * @param param
	 * @return
	 */
	private BigDecimal genExpPriceForComposeOrder(OrderFacadeComposeOrderParam param) {
		// 3.计算邮费
		// 3.1 生成OmsExpPriceParam
		// OmsExpPriceParam omsExpPriceParam = genOmsExpPriceParam(param);
		// 3.2 调用邮费计算模块
		// ExpressFeeDTO expressFeeDTO =
		// omsFeeFacade.calExpressFee(omsExpPriceParam);
		// 现在面向用户的快递费只收10块钱
		BigDecimal expPrice = new BigDecimal("10");
		// if (expressFeeDTO != null && expressFeeDTO.getPrice() != null
		// && expressFeeDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0)
		// expPrice = expressFeeDTO.getPrice();
		return expPrice;
	}

	/**
	 * @param skuParamList
	 * @return
	 */
	private List<com.xyl.mmall.promotion.dto.SkuParam> convertSkuParamList1(List<SkuParam> skuParamList) {
		boolean isFilterEnum = false;
		List<com.xyl.mmall.promotion.dto.SkuParam> skuParamList2 = new ArrayList<>();
		for (SkuParam skuParam : skuParamList) {
			com.xyl.mmall.promotion.dto.SkuParam skuParam2 = ReflectUtil.convertObj(
					com.xyl.mmall.promotion.dto.SkuParam.class, skuParam, isFilterEnum);
			CollectionUtil.addOfList(skuParamList2, skuParam2);
		}
		return skuParamList2;
	}

	/**
	 * @param skuParamListOfOri
	 * @param skuParamList
	 * @return
	 */
	private List<SkuParam> convertSkuParamList2(List<SkuParam> skuParamList2,
			List<com.xyl.mmall.promotion.dto.SkuParam> skuParamList) {
		Map<Long, SkuParam> skuParamMap2 = CollectionUtil.convertCollToMap(skuParamList2, "skuId");
		for (com.xyl.mmall.promotion.dto.SkuParam skuParam : skuParamList) {
			SkuParam skuParam2 = skuParamMap2.get(skuParam.getSkuId());
			skuParam2.setCouponSPrice(skuParam.getCouponSPrice());
			skuParam2.setHdSPrice(skuParam.getHdSPrice());
			skuParam2.setRedSPrice(skuParam.getRedSPrice());
			skuParam2.setSalePrice(skuParam.getSalePrice());
		}
		return skuParamList2;
	}

	/**
	 * 生成canCODBySku标记位
	 * 
	 * @param caDTO
	 * @param skuParamList
	 * @return
	 */
	private boolean genCanCODBySkuForComposeOrder(ConsigneeAddressDTO caDTO, List<SkuParam> skuParamList) {
		// 参数判断
		if (caDTO == null || CollectionUtil.isEmptyOfCollection(skuParamList))
			return false;

		// 1.判断收货地址是否可以
		boolean isValidOfAddress = false;
		try {
			long provinceCode = caDTO.getProvinceId(), cityCode = caDTO.getCityId(), districtCode = caDTO
					.getSectionId(), streetCode = caDTO.getStreetId();
			ExpressType expressType = ExpressType.TYPE_EMS;
			isValidOfAddress = locationService.isLocationCod(provinceCode, cityCode, districtCode, streetCode,
					expressType);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		if (!isValidOfAddress)
			return false;
		// 2.COD判断: 收货地址和仓库的关系
		long provinceId = caDTO.getProvinceId();
		Set<Long> poIdSet = new TreeSet<>();
		for (SkuParam skuParam : skuParamList) {
			poIdSet.add(skuParam.getPoId());
		}
		List<Long> poIdList = CollectionUtil.addAllOfList(null, poIdSet);
		WarehouseDTO[] warehouseDTOArray = warehouseService.getAllWarehouse();
		Map<Long, List<Long>> poIdAndWarehouseIdsMap = scheduleService.getWarehouseListByPOIdList(poIdList);
		boolean isValidOfProvince = isValidOfProvince(provinceId, warehouseDTOArray, poIdAndWarehouseIdsMap);

		if (!isValidOfProvince)
			return false;
		// 3.返回结果
		return true;
	}

	/**
	 * COD判断: 收货地址和仓库的关系
	 * 
	 * @param provinceId
	 * @param warehouseDTOArray
	 * @param poIdAndWarehouseIdsMap
	 * @return
	 */
	private boolean isValidOfProvince(long provinceId, WarehouseDTO[] warehouseDTOArray,
			Map<Long, List<Long>> poIdAndWarehouseIdsMap) {
		// 是否判断仓库个数
		boolean isCheckWarehouseCount = true;
		// 是否判断仓库省份id
		boolean isCheckProvinceIdOfWarehouse = false;
		if (!(CollectionUtil.isNotEmptyOfArray(warehouseDTOArray) && CollectionUtil
				.isNotEmptyOfMap(poIdAndWarehouseIdsMap)))
			return false;

		Map<Long, Long> warehouseIdAndProvinceIdMap = new TreeMap<>();
		for (WarehouseDTO warehouseDTO : warehouseDTOArray) {
			warehouseIdAndProvinceIdMap.put(warehouseDTO.getWarehouseId(), warehouseDTO.getProvinceId());
		}
		for (List<Long> warehouseIds : poIdAndWarehouseIdsMap.values()) {
			if (CollectionUtil.isEmptyOfCollection(warehouseIds)) {
				return false;
			}
			// 判断仓库是否有多个
			if (isCheckWarehouseCount) {
				if (warehouseIds.size() > 1) {
					return false;
				}
			}
			// 判断仓库是否和收货地址省份一致
			if (isCheckProvinceIdOfWarehouse) {
				for (Long warehouseId : warehouseIds) {
					Long provinceIdOfWarehouse = warehouseIdAndProvinceIdMap.get(warehouseId);
					if (provinceIdOfWarehouse == null || provinceIdOfWarehouse != provinceId) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 根据促销计算结果,生成组单需要的原始数据
	 * 
	 * @param fcResultDTO
	 * @return
	 */
	private List<SkuParam> genSkuParamList(FavorCaculateResultDTO fcResultDTO) {
		if (fcResultDTO == null)
			return null;
		// List<PromotionCartDTO> actionDTOList = fcResultDTO.getActivations();
		// Map<Long, List<PromotionSkuItemDTO>> noActionDTOMap =
		// fcResultDTO.getNotSatisfySkuList();
		List<SkuParam> skuParamList = new ArrayList<>();
		// CASE1: 参加活动的产品列表
		/*
		 * if (CollectionUtil.isNotEmptyOfCollection(actionDTOList)) { for
		 * (PromotionCartDTO actionDTO : actionDTOList) { List<SkuParam>
		 * newSkuParamList = appendSkuParam(skuParamList,
		 * actionDTO.getPoSkuMap()); // 设置活动的PromotionId和PromotionIdx if
		 * (CollectionUtil.isEmptyOfCollection(newSkuParamList)) continue; for
		 * (SkuParam skuParam : newSkuParamList) {
		 * skuParam.setPromotionId(actionDTO.getPromotionDTO().getId());
		 * skuParam.setPromotionIdx(actionDTO.getIndex()); } } }
		 */
		// CASE2: 不参加活动的产品列表
		appendSkuParam(skuParamList, fcResultDTO.getSkuList());
		// 返回结果
		return skuParamList;
	}

	/**
	 * 根据促销计算结果,生成组单需要的原始数据
	 * 
	 * @param skuParamList
	 * @param psiDTOListMap
	 */
	private List<SkuParam> appendSkuParam(List<SkuParam> skuParamList, List<PromotionSkuItemDTO> skuItemDTOs) {
		if (CollectionUtil.isEmptyOfList(skuItemDTOs))
			return null;

		for (PromotionSkuItemDTO psiDTO : skuItemDTOs) {
			skuParamList.add(genSkuParam(psiDTO));
		}
		return skuParamList;
	}

	/**
	 * 根据PromotionSkuItemDTO生成SkuParam
	 * 
	 * @param psiDTO
	 * @return
	 */
	private SkuParam genSkuParam(PromotionSkuItemDTO psiDTO) {
		int count = psiDTO.getCount();
		long skuId = psiDTO.getSkuId();
		BigDecimal salePrice = psiDTO.getCartPrice(), hdSPrice = psiDTO.getPromotionDiscountAmount(), couponSPrice = psiDTO
				.getCouponDiscountAmount(), hbPrice = psiDTO.getHbPrice();
		salePrice = salePrice != null ? salePrice : BigDecimal.ZERO;
		hdSPrice = hdSPrice != null ? hdSPrice : BigDecimal.ZERO;
		couponSPrice = couponSPrice != null ? couponSPrice : BigDecimal.ZERO;
		hbPrice = hbPrice != null ? hbPrice : BigDecimal.ZERO;
		SkuParam skuParam = SkuParam.genSkuParam(skuId, count, salePrice, hdSPrice, couponSPrice, hbPrice);
		skuParam.setOriRPrice(psiDTO.getOriRetailPrice());
		skuParam.setBatchCash(psiDTO.getBatchCash());
		skuParam.setMarketPrice(psiDTO.getCartPrice());
		skuParam.setSalePrice(psiDTO.getCartPrice());
		skuParam.setSupplierId(psiDTO.getBusinessId());
		return skuParam;
	}

	/**
	 * 组单
	 * 
	 * @param genOrderParam
	 *            SkuParam只需要填写skuId+count+salePrice(其他参数Facade里自己补齐)
	 * @return
	 */
	// 1.填充SkuParam里的商品信息
	private OrderCalServiceGenOrderResult composeOrder(OrderCalServiceGenOrderParam param) {
		// fillSkuParamList(param);

		// 2.生成canCODBySku标记位
		// boolean canCODBySku = genCanCODBySkuForComposeOrder(param.getCaDTO(),
		// param.getSkuParamList());
		param.setCanCODBySku(param.getCaDTO().getSectionId() == param.getProvinceId());

		// 4.组单
		OrderCalServiceGenOrderResult result = orderCalService.genOrder(param);
		// 5.填充OrderSkuDTO上的SkuSPVO和对应的快照
		if (result != null)
			fillSkuSPVO(result.getOrderFormCalDTO());
		return result;
	}

	/**
	 * 生成OmsExpPriceParam
	 * 
	 * @param param
	 * @return
	 */
	private OmsExpPriceParam genOmsExpPriceParam(OrderCalServiceGenOrderParam param) {
		OmsExpPriceParam omsExpPriceParam = new OmsExpPriceParam();
		OmsConsigneeAddressParam ca = ReflectUtil.convertObj(OmsConsigneeAddressParam.class, param.getCaDTO(), false);
		omsExpPriceParam.setCa(ca);
		omsExpPriceParam.setCOD(param.getOrderFormPayMethod() == OrderFormPayMethod.COD);
		omsExpPriceParam.setProvinceId(param.getProvinceId());
		List<OmsSkuParam> omsSkuParamList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(param.getSkuParamList())) {
			for (SkuParam skuParam : param.getSkuParamList()) {
				omsSkuParamList.add(ReflectUtil.convertObj(OmsSkuParam.class, skuParam, false));
			}
		}
		omsExpPriceParam.setSkuParamList(omsSkuParamList);
		return omsExpPriceParam;
	}

	/**
	 * 填充OrderSkuDTO上的SkuSPVO和对应的快照
	 * 
	 * @param orderCalDTO
	 */
	private void fillSkuSPVO(OrderFormCalDTO orderCalDTO) {
		if (orderCalDTO == null)
			return;
		Map<Long, OrderSkuDTO> orderSkuDTOMap = orderCalDTO.mapOrderSkusBySkuId();
		if (CollectionUtil.isEmptyOfMap(orderSkuDTOMap))
			return;

		// 填充OrderSkuDTO上的SkuSPVO和对应的快照
		List<ProductSKUBackendVO> productSKUVOs = productFacade.getProductSKUVO((new ArrayList<Long>(orderSkuDTOMap
				.keySet())));
		Map<String, List<ProductPriceDTO>> map = productService.getProductPriceDTOByProductIds((new ArrayList<Long>(
				orderSkuDTOMap.keySet())));

		Map<Long, ProductSKUBackendVO> productSKUMap = CollectionUtil.convertCollToMap(productSKUVOs, "skuId");
		List<ProductPriceDTO> productPriceDTOs = null;
		for (OrderSkuDTO orderSkuDTO : orderSkuDTOMap.values()) {
			// PODTO poDTO =
			// scheduleService.getScheduleById(poSkuDTO.getPoId());
			productPriceDTOs = map.get(String.valueOf(orderSkuDTO.getSkuId()));
			productSKUMap.get(orderSkuDTO.getSkuId()).setPriceList(convertProductPriceDTOToVO(productPriceDTOs));
			String skuSnapshot = genSkuSPDTOJson(productSKUMap.get(orderSkuDTO.getSkuId()));
			orderSkuDTO.setSkuSnapshot(skuSnapshot);
		}
	}

	private List<ProductPriceVO> convertProductPriceDTOToVO(List<ProductPriceDTO> productPriceDTOs) {
		if (!CollectionUtils.isEmpty(productPriceDTOs)) {
			List<ProductPriceVO> priceList = new ArrayList<ProductPriceVO>();
			priceList = new ArrayList<ProductPriceVO>(productPriceDTOs.size());
			for (int i = 0; i < productPriceDTOs.size(); i++) {
				ProductPriceVO priceVO = new ProductPriceVO(productPriceDTOs.get(i));
				if (i > 0) {
					priceVO.setProdMaxNumber(priceList.get(i - 1).getProdMinNumber());
				}
				priceList.add(priceVO);
			}
			return priceList;
		}
		return null;
	}

	/**
	 * 生成SkuSPDTO的JSON格式数据
	 * 
	 * @param poSkuDTO
	 * @param brandDTO
	 * @param poDTO
	 * @return
	 */
	private String genSkuSPDTOJson(ProductSKUBackendVO skuVO) {
		SkuSPDTO skuSPDTO = new SkuSPDTO();
		BrandDTO brandDTO = skuVO.getBrandId() > 0 ? brandQueryFacade.getBrandByBrandId(skuVO.getBrandId()) : null;
		if (brandDTO != null && brandDTO.getBrand() != null) {
			String brandNameEn = brandDTO.getBrand().getBrandNameEn(), brandNameZh = brandDTO.getBrand()
					.getBrandNameZh();
			brandNameEn = StringUtils.isBlank(brandNameEn) ? "" : brandNameEn.trim();
			brandNameZh = StringUtils.isBlank(brandNameZh) ? "" : brandNameZh.trim();
			skuSPDTO.setBrandName(brandNameEn + " " + brandNameZh);
			skuSPDTO.setBrandLinkUrl("/mainbrand/story?id=" + skuVO.getBrandId());
		}
		skuSPDTO.setLinkUrl("/product/detail?skuId=" + skuVO.getSkuId());
		skuSPDTO.setProductTitle(skuVO.getProductTitle());
		skuSPDTO.setPicUrl(skuVO.getShowPicPath());
		skuSPDTO.setSalePrice(skuVO.getSalePrice());
		skuSPDTO.setProductName(skuVO.getProductName());
		skuSPDTO.setBatchNum(skuVO.getBatchNum());
		skuSPDTO.setUnit(skuVO.getProdUnit());
		skuSPDTO.setCanReturn(skuVO.getCanReturn());
		skuSPDTO.setExpireDate(skuVO.getExpireDate());
		skuSPDTO.setProdBarCode(skuVO.getProdBarCode());
		skuSPDTO.setProdProduceDate(skuVO.getProdProduceDate());
		skuSPDTO.setCategoryFullName(categoryService.getFullCategoryNormalName(skuVO.getCategoryNormalId()));
		skuSPDTO.setPicPath(convertProdPicVOToPicPathList(skuVO.getPicList()));
		skuSPDTO.setCustomEditHTML(skuVO.getProdDetail().getCustomEditHTML());
		skuSPDTO.setProdParam(skuVO.getProdDetail().getProdParamJson());
		skuSPDTO.setPriceList(convertProductPriceVOTOSkuPriceDTO(skuVO.getPriceList()));
		if (skuVO.getSpeciList() != null) {
			List<SkuSpeciDTO> skuSpeciDTOs = new ArrayList<SkuSpeciDTO>();
			for (ProdSpeciBackendVO prodSpeciBackendVO : skuVO.getSpeciList()) {
				SkuSpeciDTO skuSpeciDTO = new SkuSpeciDTO();
				skuSpeciDTO.setSpecificationName(prodSpeciBackendVO.getSpecificationName());
				skuSpeciDTO.setSpeciOptionName(prodSpeciBackendVO.getSpeciOptionName());
				skuSpeciDTOs.add(skuSpeciDTO);
			}
			skuSPDTO.setSkuSpeciDTOs(skuSpeciDTOs);
		}
		return JsonUtils.toJson(skuSPDTO);
	}

	private List<SkuPriceDTO> convertProductPriceVOTOSkuPriceDTO(List<ProductPriceVO> priceList) {
		if (CollectionUtil.isEmptyOfList(priceList)) {
			return null;
		}
		List<SkuPriceDTO> skuPriceDTOs = new ArrayList<SkuPriceDTO>();
		for (ProductPriceVO productPriceVO : priceList) {
			SkuPriceDTO skuPriceDTO = new SkuPriceDTO();
			skuPriceDTO.setProdMinNumber(productPriceVO.getProdMinNumber());
			skuPriceDTO.setProdMaxNumber(productPriceVO.getProdMaxNumber());
			skuPriceDTO.setProdPrice(productPriceVO.getProdPrice());
			skuPriceDTOs.add(skuPriceDTO);
		}
		return skuPriceDTOs;
	}

	/**
	 * 将商品图片对像转为图片路径List
	 * 
	 * @param picList
	 * @return
	 */
	private List<String> convertProdPicVOToPicPathList(List<ProdPicVO> picList) {
		if (CollectionUtil.isEmptyOfList(picList)) {
			return null;
		}
		List<String> picPathList = new ArrayList<String>();
		for (ProdPicVO prodPicVO : picList) {
			picPathList.add(prodPicVO.getPicPath());
		}
		return picPathList;

	}

	/**
	 * 填充SkuParam里的商品信息
	 * 
	 * @param skuParamList
	 */
	private void fillSkuParamList(OrderCalServiceGenOrderParam param) {
		List<SkuParam> skuParamList = param.getSkuParamList();
		if (CollectionUtil.isEmptyOfCollection(skuParamList))
			return;

		Iterator<SkuParam> iter = skuParamList.iterator();
		while (iter.hasNext()) {
			SkuParam skuParam = iter.next();
			boolean isValid = fillSkuParam(skuParam);
			if (!isValid) {
				iter.remove();
				logger.error("iter.remove() ,skuId=" + skuParam.getSkuId() + " ,userId=" + param.getUserId());
			}
		}
	}

	/**
	 * 填充SkuParam里的商品信息
	 * 
	 * @param skuParam
	 */
	private boolean fillSkuParam(SkuParam skuParam) {
		// 1.读取商品的信息
		ProductSKUDTO productSKUDTO = new ProductSKUDTO();
		productSKUDTO.setId(skuParam.getSkuId());
		productSKUDTO = itemProductService.getProductSKUDTO(productSKUDTO, false);
		if (productSKUDTO == null)
			return false;
		// 2.填充SkuParam里的商品信息
		long productId = productSKUDTO.getId(), supplierId = productSKUDTO.getBusinessId();
		// BigDecimal marketPrice = productSKUDTO.getSalePrice(), oriRPrice =
		// productSKUDTO.getSalePrice();
		skuParam.setProductId(productId);
		skuParam.setSupplierId(supplierId);
		// skuParam.setMarketPrice(marketPrice);
		// skuParam.setOriRPrice(oriRPrice);
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#changePaymethodToCOD(long,
	 *      long)
	 */
	public int changePaymethodToCOD(long userId, long orderId) {
		return orderService.changePaymethodToCOD(userId, orderId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#cancelTrade(com.xyl.mmall.order.dto.OrderCancelInfoDTO)
	 */
	public boolean cancelTrade(OrderCancelInfoDTO cancelDTO) {
		try {
			// 1.判断是否需要取消交易
			boolean needCancel = OrderApiUtil.needCancelTrade(cancelDTO);
			if (!needCancel)
				return true;
			// 2.取消交易
			long orderId = cancelDTO.getOrderId();
			boolean isSuccOfCancelTrade = orderService.cancelTradeByOrderCancelInfo(cancelDTO);
			if (!isSuccOfCancelTrade) {
				logger.error("orderService.cancelTradeByOrderCancelInfo fail, orderId=" + orderId);
			}
			return isSuccOfCancelTrade;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#cancelOmsOrder(com.xyl.mmall.order.dto.OrderCancelInfoDTO)
	 */
	public boolean cancelOmsOrder(OrderCancelInfoDTO cancelDTO) {
		try {
			int cancelIdx = OrderCancelInfo.IDX_CANCEL_OMSORDER;
			// 1.判断是否需要取消OMS订单
			boolean needCancel = OrderApiUtil.needCancelOMSOrder(cancelDTO);
			if (!needCancel)
				return true;
			long orderId = cancelDTO.getOrderId(), userId = cancelDTO.getUserId();

			// 2.取消OMS订单
			boolean isSucc = omsOrderFormService.cancelOrderForm(orderId, userId) > 0;
			// 3.设置OrderCancelInfo的处理标记位
			boolean isUpdateRetryFlag = isSucc;
			Long retryFlagOfOld = cancelDTO.getRetryFlag();
			if (isUpdateRetryFlag) {
				long retryFlagOfNew = ExtInfoFieldUtil.setValueOfType1(cancelDTO.getRetryFlag(), cancelIdx, false);
				cancelDTO.setRetryFlag(retryFlagOfNew);
			}
			if (orderService.updateOrderCancelInfoOfRetryInfo(isUpdateRetryFlag, cancelDTO, retryFlagOfOld) != 1)
				logger.info("updateOrderCancelInfoOfRetryInfo fail");

			if (!isSucc) {
				logger.error("omsOrderFormService.cancelOrderForm fail, orderId=" + orderId);
			}
			return isSucc;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#cancelOmsOrder(com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO)
	 */
	public RetArg cancelOmsOrder(CancelOmsOrderTaskDTO cancelTaskDTO) {
		long orderId = cancelTaskDTO.getOrderId(), userId = cancelTaskDTO.getUserId();
		// 2.判断订单是否需要通知oms取消
		boolean needCancelOmsOrder = OrderApiUtil.needCancelOMSOrder(cancelTaskDTO.getOldOrderFormState());
		// 3.通知oms取消订单
		boolean isSucc = true;
		if (needCancelOmsOrder)
			isSucc = isSucc && omsOrderFormService.cancelOrderForm(orderId, userId) > 0;
		// 4.更新CancelOmsOrderTask字段(不管上述操作是否成功)
		CancelOmsOrderTaskState oldState = cancelTaskDTO.getTaskState();
		CancelOmsOrderTaskState newState = !needCancelOmsOrder ? CancelOmsOrderTaskState.CANCEL_OMS_UNNEED
				: (isSucc ? CancelOmsOrderTaskState.CANCEL_OMS_SUCC : CancelOmsOrderTaskState.CANCEL_OMS_FAIL);
		cancelTaskDTO.setTaskState(newState);
		orderMiscService.updateCancelOmsOrderTaskState(cancelTaskDTO, oldState);
		// 5.返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, isSucc);
		RetArgUtil.put(retArg, cancelTaskDTO);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#cancelOrder(com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO)
	 */
	public RetArg cancelOrder(CancelOmsOrderTaskDTO cancelTaskDTO) {
		// TODO.DML waiting test
		// 1.执行取消订单的操作
		boolean isPassCanCancelFlag = true;
		OrderCancelInfoDTO cancelDTO = ReflectUtil.convertObj(OrderCancelInfoDTO.class, cancelTaskDTO, false);
		RetArg retArg = cancelOrder(cancelDTO, isPassCanCancelFlag, cancelTaskDTO.getOldOrderFormState());
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		// 2.更新CancelOmsOrderTask.state
		CancelOmsOrderTaskState oldState = cancelTaskDTO.getTaskState();
		CancelOmsOrderTaskState newState = isSucc == Boolean.TRUE ? CancelOmsOrderTaskState.CANCEL_ORDER_SUCC
				: CancelOmsOrderTaskState.CANCEL_ORDER_FAIL;
		cancelTaskDTO.setTaskState(newState);
		orderMiscService.updateCancelOmsOrderTaskState(cancelTaskDTO, oldState);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#cancelOrder(com.xyl.mmall.order.dto.OrderCancelInfoDTO)
	 */
	@Deprecated
	public RetArg cancelOrder(OrderCancelInfoDTO cancelDTO) {
		boolean isPassCanCancelFlag = false;
		return cancelOrder(cancelDTO, isPassCanCancelFlag, null);
	}

	/**
	 * 取消订单(不通知oms取消订单)
	 * 
	 * @param cancelDTO
	 * @param isPassCanCancelFlag
	 *            是否忽略canCancel标记位的判断
	 * @param oldOrderFormState
	 * @return RetArg.OrderFormDTO<br>
	 *         RetArg.Boolean: 是否成功<br>
	 *         RetArg.BigDecimal: 在线支付退款金额
	 */
	@Deprecated
	private RetArg cancelOrder(OrderCancelInfoDTO cancelDTO, boolean isPassCanCancelFlag,
			OrderFormState oldOrderFormState) {
		long timeout = 60 * 1000;
		// 0.参数准备
		RetArg retArg = new RetArg();
		boolean isVisible = true, isSucc = true;
		long userId = cancelDTO.getUserId(), orderId = cancelDTO.getOrderId(), currTime = System.currentTimeMillis();
		cancelDTO.setCtime(currTime);
		if (userId <= 0 || orderId <= 0) {
			RetArgUtil.put(retArg, false);
			return retArg;
		}
		OrderFormBriefDTO orderBDTO = queryOrderFormBriefDTO(userId, orderId, null);
		if (orderBDTO == null) {
			RetArgUtil.put(retArg, false);
			return retArg;
		}
		// 商家关闭订单，先判断订单是否属于该商家
		if (cancelDTO.getBusinessId() > 0 && orderBDTO.getBusinessId() != cancelDTO.getBusinessId()) {
			RetArgUtil.put(retArg, false);
			return retArg;
		}
		// 判断订单是否是重复取消
		boolean isAlreadyCancel = orderBDTO.getOrderFormState() == OrderFormState.CANCEL_ED;
		// || orderBDTO.getOrderFormState() == OrderFormState.CANCEL_ING;
		// OrderFormState orderState = oldOrderFormState == null ?
		// orderBDTO.getOrderFormState() : oldOrderFormState;
		// 判断订单是否可以取消
		boolean isRecyleStock = true;
		// canCancel = isPassCanCancelFlag ? true : orderBDTO.isCanCancel(),
		// isRecyleStock = true;
		// if (canCancel &&
		// OrderApiUtil.needCheckOMSWhenCancelOrder(orderState)) {
		// // 仓库是否允许取消,以及是否允许回收库存
		// int retCode = omsOrderFormService.canCancelOrder(orderId, userId);
		// canCancel = isPassCanCancelFlag ? true : retCode > 0;
		// isRecyleStock = retCode == 1;
		// }
		//
		// if (!canCancel) {
		// logger.error("!canCancel==true, orderId=" + orderId);
		// isSucc = false;
		// } else
		if (isAlreadyCancel) {
			isSucc = true;
		} else {
			// 1.获得注册的TCC事务管理器
			Transaction tx = null;
			try {
				tx = tccManager.beginTransaction("callOffOrderTCCActivity");
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
			if (tx == null) {
				RetArgUtil.put(retArg, false);
				return retArg;
			}

			// 2.获得tranId
			long tranId = tx.getUUID();
			logger.debug("tranId=" + tranId);

			// 读取完整的订单数据
			OrderFormDTO orderDTO = queryOrderForm(userId, orderId, isVisible);

			// 3.按顺序执行try步骤
			try {
				// 3.1 调用取消订单服务
				// orderTCCService.tryCallOffOrderByTCC(tranId, cancelDTO,
				// isRecyleStock);
				Future<Boolean> tryCallOffOrderByTCCResultFuture = RpcContext.getContext().getFuture();

				// 3.2 调用回收促销信息服务
				TCCParamDTO tccParamDTO = new TCCParamDTO();
				tccParamDTO.setOrderId(orderId);
				tccParamDTO.setUserId(userId);
				recycleTCCService.tryAddRecycleTCC(tranId, tccParamDTO);
				Future<Boolean> tryAddRecycleTCCResultFuture = RpcContext.getContext().getFuture();

				// 3.3 获得返回结果
				Boolean tryCallOffOrderByTCCResult = tryCallOffOrderByTCCResultFuture.get(timeout,
						TimeUnit.MILLISECONDS);
				Boolean tryAddRecycleTCCResult = tryAddRecycleTCCResultFuture.get(timeout, TimeUnit.MILLISECONDS);
				isSucc = tryCallOffOrderByTCCResult == Boolean.TRUE && tryAddRecycleTCCResult == Boolean.TRUE;
				if (!isSucc)
					throw new ServiceException("cancelOrder.try fail! tryCallOffOrderByTCCResult="
							+ tryCallOffOrderByTCCResult);// +
															// " tryAddRecycleTCCResult="
															// +
															// tryAddRecycleTCCResult);
			} catch (Exception ex) {
				isSucc = false;
				logger.error(ex.getMessage(), ex);
				// 如果有异常,则执行cancel步骤
				try {
					tx.cancel();
				} catch (Exception ex2) {
					logger.error(ex2.getMessage(), ex2);
				}
			}

			// 4.执行Confirm步骤
			if (isSucc) {
				try {
					tx.confirm();
				} catch (Exception ex) {
					isSucc = false;
					logger.error(ex.getMessage(), ex);
				}
			}

			// 5.执行恢复购物车库存的操作
			if (isSucc && isRecyleStock) {
				addInventoryCountForCancelOrder(orderDTO);
			}

			// 6.尝试调用取消交易的逻辑
			if (isSucc) {
				OrderCancelInfoDTO cancelDTOOfDB = orderService.getOrderCancelInfo(userId, orderId);
				this.cancelTrade(cancelDTOOfDB);
				// this.cancelOmsOrder(cancelDTOOfDB);
			}
		}

		// 7.返回结果
		// 7.1 查询订单信息
		OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId, isVisible);
		// 7.2 查询在线支付退款金额
		if (isSucc) {
			List<TradeItemDTO> tradeDTOList = tradeFacade.getTradeItemDTOList(orderId, userId);
			TradeItemDTO tradeDTO = (TradeItemDTO) TradeApiUtil.getTradeOfOnlineAndRefund(tradeDTOList);
			if (tradeDTO != null)
				RetArgUtil.put(retArg, tradeDTO.getCash());
		}
		// 7.3 设置返回结果
		RetArgUtil.put(retArg, isSucc);
		RetArgUtil.put(retArg, orderDTO);
		return retArg;
	}

	/**
	 * 执行恢复购物车库存的操作
	 * 
	 * @param orderDTO
	 */
	private void addInventoryCountForCancelOrder(OrderFormDTO orderDTO) {
		try {
			if (!cartFacade.addInventoryCount(getCartItemDTOFromOrderFormDTO(orderDTO)))
				logger.info("addInventoryCount fail, orderId=" + orderDTO.getOrderId());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	private void decreaseInventoryCountForMakeOrder(OrderFormDTO orderDTO) {
		try {
			if (!cartFacade.decreaseInventory(getCartItemDTOFromOrderFormDTO(orderDTO)))
				logger.info("decreaseInventoryCount fail, orderId=" + orderDTO.getOrderId());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	private List<CartItemDTO> getCartItemDTOFromOrderFormDTO(OrderFormDTO orderDTO) {
		List<OrderSkuDTO> orderSkuDTOList = orderDTO.getAllOrderSkuDTOList();
		Map<Long, CartItemDTO> cartDTOMap = new TreeMap<Long, CartItemDTO>();
		for (OrderSkuDTO orderSkuDTO : orderSkuDTOList) {
			long skuId = orderSkuDTO.getSkuId();
			CartItemDTO cartDTO = cartDTOMap.containsKey(skuId) ? cartDTOMap.get(skuId) : new CartItemDTO();
			cartDTO.setSkuid(skuId);
			cartDTO.setCount(cartDTO.getCount() + orderSkuDTO.getTotalCount());
			cartDTOMap.put(skuId, cartDTO);
		}
		return CollectionUtil.addAllOfList(null, cartDTOMap.values());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#setOrderFormStateToCancelRevert(long,
	 *      long, com.xyl.mmall.order.enums.OrderFormState)
	 */
	public boolean setOrderFormToCancelRevert(CancelOmsOrderTaskDTO cancelTaskDTO) {
		boolean isSucc = true;
		// 1.更新订单状态
		long orderId = cancelTaskDTO.getOrderId(), userId = cancelTaskDTO.getUserId();
		OrderFormState newState = cancelTaskDTO.getOldOrderFormState();
		int retCode = orderService.setOrderFormStateToCancelRevert(userId, orderId, newState);
		isSucc = isSucc && retCode > 0;
		// 2.更新CancelOmsOrderTask.state
		if (isSucc) {
			CancelOmsOrderTaskState oldState = cancelTaskDTO.getTaskState();
			cancelTaskDTO.setTaskState(CancelOmsOrderTaskState.REVERT_CANCEL_ORDER);
			isSucc = orderMiscService.updateCancelOmsOrderTaskState(cancelTaskDTO, oldState);
		}
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#addCancelOmsOrderTask(com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO)
	 */
	public RetArg addCancelOmsOrderTask(CancelOmsOrderTaskDTO cancelTaskDTO) {
		// 1.创建取消oms订单的任务
		long ctime = System.currentTimeMillis(), userId = cancelTaskDTO.getUserId(), orderId = cancelTaskDTO
				.getOrderId();
		cancelTaskDTO.setCtime(ctime);
		cancelTaskDTO = orderMiscService.addCancelOmsOrderTask(cancelTaskDTO);

		// 2.返回结果
		// 2.1 是否处理成功
		boolean isSucc = cancelTaskDTO != null;
		// 2.2 订单内容
		boolean isVisible = true;
		OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId, isVisible);
		// 2.3 返回结果
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, isSucc);
		RetArgUtil.put(retArg, orderDTO);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#makeOrder(com.xyl.mmall.order.dto.OrderFormCalDTO,
	 *      com.xyl.mmall.promotion.dto.FavorCaculateResultDTO)
	 */
	public RetArg makeOrder(OrderFormCalDTO orderCalDTO, FavorCaculateResultDTO fcResultDTO, String cartIds) {
		long timeout = 60 * 1000;
		long userId = orderCalDTO.getUserId();

		RetArg retArg = new RetArg();
		// 1.获得注册的TCC事务管理器
		Transaction tx = null;
		try {
			tx = tccManager.beginTransaction("addOrderTCCActivity");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		if (tx == null) {
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 2.获得tranId
		long tranId = tx.getUUID();
		logger.debug("tranId=" + tranId);

		// 3.按顺序执行try步骤
		boolean isSucc = true;
		try {
			// 3.1 分配OrderId
			long orderId = orderService.allocateRecordId();
			if (orderId < 1l) {
				throw new DBSupportRuntimeException("Get generateId failed!");
			}
			orderCalDTO.setOrderId(orderId);// for dev

			// 3.2 调用添加订单服务
			TryAddOrderByTCCParam tryAddOrderByTCCParam = new TryAddOrderByTCCParam();
			tryAddOrderByTCCParam.setOrderFormCalDTO(orderCalDTO);
			tryAddOrderByTCCParam.setUserId(userId);
			// orderTCCService.tryAddOrderByTCC(tranId, tryAddOrderByTCCParam);
			Future<TryAddOrderByTCCResult> tryAddOrderByTCCResultFuture = RpcContext.getContext().getFuture();

			// 3.3 调用修改促销信息服务
			TCCParamDTO tccParamDTO = new TCCParamDTO();
			tccParamDTO.setUserId(userId);
			tccParamDTO.setOrderId(orderId);
			activityTCCService.tryAddActivityTCC(tranId, tccParamDTO, fcResultDTO);
			Future<Boolean> tryAddActivityTCCResultFuture = RpcContext.getContext().getFuture();

			// 3.4 获得返回结果
			TryAddOrderByTCCResult tryAddOrderByTCCResult = tryAddOrderByTCCResultFuture.get(timeout,
					TimeUnit.MILLISECONDS);
			// Boolean tryAddActivityTCCResult =
			// tryAddActivityTCCResultFuture.get(timeout,
			// TimeUnit.MILLISECONDS);
			isSucc = tryAddOrderByTCCResult != null && tryAddOrderByTCCResult.getResultCode() != null
					&& tryAddOrderByTCCResult.getResultCode() == 1;// &&
																	// tryAddActivityTCCResult
																	// ==
																	// Boolean.TRUE;
			if (!isSucc)
				throw new ServiceException("makeOrder.try fail! tryAddOrderByTCCResult=" + tryAddOrderByTCCResult);
			// + " ,tryAddActivityTCCResult=" + tryAddActivityTCCResult);
		} catch (Exception ex) {
			isSucc = false;
			logger.error(ex.getMessage(), ex);
			// 如果有异常,则执行cancel步骤
			try {
				tx.cancel();
			} catch (Exception ex2) {
				logger.error(ex2.getMessage(), ex2);
			}
		}

		// 4.执行Confirm步骤
		if (isSucc) {
			try {
				tx.confirm();
			} catch (Exception ex) {
				isSucc = false;
				logger.error(ex.getMessage(), ex);
			}
		}

		// 5.调用购物车删除服务
		if (isSucc) {
			try {
				int provinceId = orderCalDTO.getProvinceId();
				long orderId = orderCalDTO.getOrderId();
				if (!cartFacade.deleteCartForOrder(userId, provinceId, cartIds))
					logger.info("!cartFacade.deleteCartForOrder(userId, provinceId, cartIds) ,orderId=" + orderId
							+ " ,cartIds=" + cartIds);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

		// 6.0元订单的返券特殊处理
		if (isSucc) {
			try {
				boolean isRealCashZero = OrderApiUtil.isRealCashZero(orderCalDTO);
				if (isRealCashZero) {
					long orderId = orderCalDTO.getOrderId();
					rebateService.rebate(userId, orderId);
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

		RetArgUtil.put(retArg, isSucc);
		return retArg;
	}

	public RetArg makeMmallOrder(Map<Long, OrderFormCalDTO> orderCalDTOMap,
			Map<Long, FavorCaculateResultDTO> fcResultDTOMap, Map<Long, StringBuilder> businessIds) {
		long timeout = 60 * 1000;

		RetArg retArg = new RetArg();
		// 1.获得注册的TCC事务管理器
		Transaction tx = null;
		try {
			tx = tccManager.beginTransaction("addOrderTCCActivity");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		if (tx == null) {
			RetArgUtil.put(retArg, false);
			return retArg;
		}
		logger.info("####################TCC事务注册成功，makeMmallOrder开始执行####################");
		// 2.获得tranId
		long tranId = tx.getUUID();
		logger.debug("tranId=" + tranId);
		logger.info("####################TCC tranId获取成功####################");
		// 3.按顺序执行try步骤
		boolean isSucc = true;
		int returnResultCode = 1;
		long parentOrderId = -1;
		try {
			// 3.1 分配ParentOrderId，该id用于支付，以及作为组合订单的唯一id
			parentOrderId = orderService.allocateRecordId();
			if (parentOrderId < 1l) {
				throw new DBSupportRuntimeException("Get generateId failed!");
			}
			logger.info("####################分配parentOrderId : " + parentOrderId + "####################");
			Map<Long, TryAddOrderByTCCParam> tryAddOrderTCCParamMap = new HashMap<Long, TryAddOrderByTCCParam>();
			Map<Long, TCCParamDTO> tccParamDTOMap = new HashMap<Long, TCCParamDTO>();
			for (Long businessId : businessIds.keySet()) {
				long orderId = orderService.allocateRecordId();
				if (orderId < 1l) {
					throw new DBSupportRuntimeException("Get generateId failed!");
				}
				OrderFormCalDTO orderCalDTO = orderCalDTOMap.get(businessId);
				long userId = orderCalDTO.getUserId();
				orderCalDTO.setOrderId(orderId);
				orderCalDTO.setBusinessId(businessId);
				orderCalDTO.setParentId(parentOrderId);
				TryAddOrderByTCCParam tryAddOrderByTCCParam = new TryAddOrderByTCCParam();
				tryAddOrderByTCCParam.setOrderFormCalDTO(orderCalDTO);
				tryAddOrderByTCCParam.setUserId(userId);
				tryAddOrderTCCParamMap.put(businessId, tryAddOrderByTCCParam);

				TCCParamDTO tccParamDTO = new TCCParamDTO();
				tccParamDTO.setUserId(userId);
				tccParamDTO.setOrderId(orderId);
				tccParamDTOMap.put(businessId, tccParamDTO);
			}

			// 3.2 调用添加订单服务
			logger.info("####################开始调用tryAddOrderByTCC添加订单服务####################");
			orderTCCService.tryAddOrderByTCC(tranId, tryAddOrderTCCParamMap);
			Future<TryAddOrderByTCCResult> tryAddOrderByTCCResultFuture = RpcContext.getContext().getFuture();
			logger.info("####################tryAddOrderByTCC添加订单服务结束####################");

			// 3.3 调用修改促销信息服务
			activityTCCService.tryAddActivityTCC(tranId, tccParamDTOMap, fcResultDTOMap);
			Future<Boolean> tryAddActivityTCCResultFuture = RpcContext.getContext().getFuture();

			// 3.4 获得返回结果
			TryAddOrderByTCCResult tryAddOrderByTCCResult = tryAddOrderByTCCResultFuture.get(timeout,
					TimeUnit.MILLISECONDS);
			Boolean tryAddActivityTCCResult = tryAddActivityTCCResultFuture.get(timeout, TimeUnit.MILLISECONDS);
			// isSucc = tryAddOrderByTCCResult != null
			// && tryAddOrderByTCCResult.getResultCode() != null
			// && tryAddOrderByTCCResult.getResultCode() == 1;// &&
			isSucc = tryAddOrderByTCCResult != null && tryAddOrderByTCCResult.getResultCode() != null
					&& tryAddOrderByTCCResult.getResultCode() == 1 && tryAddActivityTCCResult == Boolean.TRUE;
			if (isSucc) {
				// 有值
				// resultCode定义：0，Unknown失败；1，成功；2，库存不足；3，参数不正确
				isSucc = tryAddOrderByTCCResult.getResultCode() == 1;
				if (!isSucc) {
					// 失败
					if (tryAddOrderByTCCResult.getResultCode() == 0) {
						returnResultCode = 0;
						throw new ServiceException("resuleCode===0，tryAddOrderByTCC执行异常");
					} else {
						try {
							returnResultCode = tryAddOrderByTCCResult.getResultCode();
							tx.cancel();
							logger.info("[2，库存不足；3，参数不正确] tryAddOrderByTCC执行失败，tryAddOrderByTCCResult="
									+ tryAddOrderByTCCResult);
						} catch (Exception ex2) {
							logger.error(ex2.getMessage(), ex2);
						}
					}
				}
			} else {
				// 没值
				returnResultCode = 0;
				throw new ServiceException(
						"tryAddOrderByTCCResult===null或者tryAddOrderByTCCResult.getResultCode()===null，tryAddOrderByTCC执行异常");
			}
		} catch (Exception ex) {
			isSucc = false;
			logger.error(ex.getMessage(), ex);
			// 如果有异常,则执行cancel步骤
			try {
				tx.cancel();
				logger.info("####################TCC cancel完成####################");
			} catch (Exception ex2) {
				logger.error(ex2.getMessage(), ex2);
			}
		}

		// 4.执行Confirm步骤
		if (isSucc) {
			try {
				tx.confirm();
				logger.info("####################TCC confirm执行完成####################");
			} catch (Exception ex) {
				isSucc = false;
				logger.error(ex.getMessage(), ex);
			}
		}

		// 5.调用购物车删除服务
		if (isSucc)

		{
			for (Long businessId : businessIds.keySet()) {
				decreaseInventoryCountForMakeOrder(orderCalDTOMap.get(businessId));
			}
			try {
				for (Long businessId : businessIds.keySet()) {
					OrderFormCalDTO orderCalDTO = orderCalDTOMap.get(businessId);
					int provinceId = orderCalDTO.getProvinceId();
					long orderId = orderCalDTO.getOrderId();
					if (!cartFacade.deleteCartForOrder(orderCalDTO.getUserId(), provinceId, businessIds.get(businessId)
							.toString()))
						logger.info("!cartFacade.deleteCartForOrder(userId, provinceId, cartIds) ,orderId=" + orderId
								+ " ,cartIds=" + businessIds.get(businessId).toString());
				}
				logger.info("####################清空购物车逻辑执行完成####################");
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

		// 6.0元订单的返券特殊处理
		// if (isSucc) {
		// try {
		// for (Long businessId : businessIds.keySet()) {
		// OrderFormCalDTO orderCalDTO = orderCalDTOMap.get(businessId);
		// boolean isRealCashZero = OrderApiUtil.isRealCashZero(orderCalDTO);
		// if (isRealCashZero) {
		// long orderId = orderCalDTO.getOrderId();
		// rebateService.rebate(orderCalDTO.getUserId(), orderId);
		// }
		// logger.info("####################清空返券逻辑完成####################");
		// }
		// } catch (Exception ex) {
		// logger.error(ex.getMessage(), ex);
		// }
		// }

		RetArgUtil.put(retArg, isSucc);
		RetArgUtil.put(retArg, parentOrderId);
		RetArgUtil.put(retArg, returnResultCode);
		logger.info("####################makeMmallOrder执行完成####################");
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#updateIsVisible(long, long,
	 *      java.lang.Boolean)
	 */
	public boolean updateIsVisible(long userId, long orderId, boolean isVisible) {
		return orderService.updateIsVisible(userId, orderId, isVisible);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#queryOrderForm(long, long,
	 *      java.lang.Boolean)
	 */
	public OrderFormDTO queryOrderForm(long userId, long orderId, Boolean isVisible) {
		OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId, isVisible);
		if (orderDTO != null && orderDTO.getOrderFormState().equals(OrderFormState.CANCEL_ED)) {
			OrderCancelInfoDTO orderCancelInfoDTO = orderService.getOrderCancelInfo(userId, orderId);
			if (orderCancelInfoDTO != null) {
				orderDTO.setCancelReason(orderCancelInfoDTO.getReason());
				orderDTO.setCancelTime(orderCancelInfoDTO.getCtime());
			}
		}
		return orderDTO;
	}

	public List<OrderFormDTO> queryOrderFormList(long userId, long parentId, Boolean isVisible) {
		List<OrderFormDTO> orderDTOList = orderService.queryOrderFormList(userId, parentId, isVisible);
		return orderDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#queryOrderListCount(long)
	 */
	public Map<OrderQueryType, Integer> queryOrderListCount(long userId) {
		Map<OrderQueryType, Integer> queryResultMap = new HashMap<>();
		boolean isVisible = true;
		long[] orderTimeRange = null;
		Integer countOfAll = null;

		OrderQueryType[] queryTypeArray = new OrderQueryType[] { OrderQueryType.ALL, OrderQueryType.WAITING_PAY,
				OrderQueryType.WAITING_DELIVE, OrderQueryType.ALREADY_DELIVE };
		for (OrderQueryType queryType : queryTypeArray) {
			orderTimeRange = genOrderTimeRangeByOrderQueryType(queryType, null);
			int count = 0;
			if (countOfAll != null && countOfAll <= 0) {
				// 特殊优化: 全部订单为0的时候,其他订单数目必定为0
				count = 0;
			} else if (queryType == OrderQueryType.WAITING_PAY || queryType == OrderQueryType.ALREADY_DELIVE
					|| queryType == OrderQueryType.WAITING_DELIVE) {
				// CASE1: WAITING_PAY || WAITING_DELIVE || ALREADY_DELIVE
				count = orderService.queryOrderFormListCountByState2(userId, isVisible, queryType.getOrderStateArray(),
						orderTimeRange, null);
			} else if (queryType == OrderQueryType.ALL) {
				// CASE2: ALL
				count = orderService.queryAllOrderFormListCount(userId, isVisible, null);
				countOfAll = count;
			}
			queryResultMap.put(queryType, count);
		}

		// 返回结果
		return queryResultMap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#queryOrderList(long,
	 *      com.xyl.mmall.common.enums.OrderQueryType,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@SuppressWarnings("unchecked")
	public RetArg queryOrderList(long userId, OrderQueryType queryType, String search, DDBParam param,
			boolean isGetStoreInfo) {
		long[] orderTimeRange = null;
		RetArg retArg = queryOrderList(userId, queryType, search, orderTimeRange, param);

		List<OrderFormDTO> orderFormDTOs = RetArgUtil.get(retArg, ArrayList.class);
		if (orderFormDTOs == null || !isGetStoreInfo) {
			return retArg;
		}
		for (OrderFormDTO orderFormDTO : orderFormDTOs) {
			buildStoreInfo(orderFormDTO.getOrderCartItemDTOList());

		}
		return retArg;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#queryOrderList(long,
	 *      com.xyl.mmall.common.enums.OrderQueryType, java.lang.String, long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg queryOrderList(long userId, OrderQueryType queryType, String search, long[] orderTimeRange,
			DDBParam param) {
		boolean isVisible = true;
		RetArg retArg = new RetArg();
		orderTimeRange = genOrderTimeRangeByOrderQueryType(queryType, orderTimeRange);

		if (queryType == OrderQueryType.WAITING_PAY || queryType == OrderQueryType.WAITING_DELIVE
				|| queryType == OrderQueryType.ALREADY_DELIVE) {
			// CASE1: 待付款+待发货+已发货
			retArg = orderService.queryOrderFormListByState2(userId, isVisible, queryType.getOrderStateArray(),
					orderTimeRange, param);
		} else if (queryType == OrderQueryType.SEARCH) {
			// CASE2: 查询
			if (StringUtils.isNotBlank(search) && NumberUtils.isNumber(search.trim())) {
				Long orderIdOfPart = Long.valueOf(search.trim());
				retArg = orderService.queryOrderFormListByLikeOrderId(userId, isVisible, orderIdOfPart, orderTimeRange,
						param);
			}
		} else
			// CASE3: 全部订单
			retArg = orderService.queryAllOrderFormList(userId, isVisible, orderTimeRange, param);
		return retArg;
	}

	/**
	 * @param queryType
	 * @param orderTimeRange
	 * @return
	 */
	private long[] genOrderTimeRangeByOrderQueryType(OrderQueryType queryType, long[] orderTimeRange) {
		long currTime = System.currentTimeMillis();
		if (queryType == OrderQueryType.WAITING_PAY)
			orderTimeRange = new long[] { currTime - ConstValueOfOrder.MAX_PAY_TIME,
					currTime + CalendarConst.MINUTE_TIME * 5 };
		return orderTimeRange;
	}

	public List<SkuOrderStockDTO> getSkuOrderStockDTOListBySkuIds(Collection<Long> skuIdColl) {
		return skuOrderStockService.getSkuOrderStockDTOListBySkuIds(skuIdColl);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#getCoupon(long, long)
	 */
	@Override
	public Coupon getCoupon(long userId, long orderId) {
		CouponOrder couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(userId, orderId);
		if (null == couponOrder) {
			return null;
		}
		Coupon coupon = null;
		coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), true);
		if (null == coupon) {
			coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
		}
		return coupon;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#getPOList(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public List<PODTO> getPOList(OrderFormDTO ordFormDTO) {
		if (null == ordFormDTO) {
			return new ArrayList<PODTO>();
		}
		List<Long> poIdList = ordFormDTO.extractPOIdList();
		if (null == poIdList || 0 == poIdList.size()) {
			return new ArrayList<PODTO>();
		}
		POListDTO poListDTO = scheduleService.batchGetScheduleListByIdList(poIdList);
		if (null == poListDTO || null == poListDTO.getPoList()) {
			return new ArrayList<PODTO>();
		}
		return poListDTO.getPoList();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#getSaleAreaIdList(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public List<Long> getSaleAreaIdList(OrderFormDTO ordFormDTO) {
		List<Long> areaIdList = new ArrayList<Long>();
		List<PODTO> poList = getPOList(ordFormDTO);
		Set<Long> areaIdSet = new HashSet<Long>();
		for (PODTO po : poList) {
			ScheduleDTO scheduleDTO = po.getScheduleDTO();
			List<ScheduleSiteRela> siteRelaList = null;
			if (null == scheduleDTO || null == (siteRelaList = scheduleDTO.getSiteRelaList())) {
				continue;
			}
			for (ScheduleSiteRela site : siteRelaList) {
				if (null == site) {
					continue;
				}
				areaIdSet.add(site.getSaleSiteId());
			}
		}
		for (long id : areaIdSet) {
			areaIdList.add(id);
		}
		return areaIdList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#getProducts(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public Map<Long, POProductDTO> getProducts(OrderFormDTO ordForm) {
		Map<Long, POProductDTO> productList = new HashMap<Long, POProductDTO>();
		if (null == ordForm) {
			return productList;
		}
		Map<Long, OrderSkuDTO> allOrdSku = ordForm.mapOrderSkusByTheirId();
		if (null == allOrdSku) {
			return productList;
		}
		for (Entry<Long, OrderSkuDTO> entry : allOrdSku.entrySet()) {
			OrderSkuDTO ordSku = entry.getValue();
			if (null == ordSku) {
				continue;
			}
			long productId = ordSku.getProductId();
			POProductDTO p = poProductService.getProductDTO(productId);
			if (null != p) {
				productList.put(productId, p);
			}
		}
		return productList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#queryOrderCartItemBriefDTOList(long,
	 *      long)
	 */
	public List<OrderCartItemBriefDTO> queryOrderCartItemBriefDTOList(long userId, long orderId) {
		return orderBriefService.queryOrderCartItemBriefDTOList(userId, orderId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#queryOrderPackageBriefDTO(long,
	 *      long)
	 */
	public OrderPackageBriefDTO queryOrderPackageBriefDTO(long userId, long packageId) {
		return orderBriefService.queryOrderPackageBriefDTO(userId, packageId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#queryOrderFormBriefDTO(long,
	 *      long, java.lang.Boolean)
	 */
	public OrderFormBriefDTO queryOrderFormBriefDTO(long userId, long orderId, Boolean isVisible) {
		return orderBriefService.queryOrderFormBrief(userId, orderId, isVisible);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#queryOrderFormBriefDTO(long,
	 *      long, java.lang.Boolean)
	 */
	public List<OrderFormBriefDTO> queryOrderFormBriefDTOList(long userId, long parentId, Boolean isVisible) {
		return orderBriefService.queryOrderFormBriefList(userId, parentId, isVisible);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.OrderFacade#extractPromotion(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public Map<Long, PromotionDTO> extractPromotion(OrderFormDTO ordFormDTO) {
		Map<Long, PromotionDTO> ret = new HashMap<Long, PromotionDTO>();
		if (null == ordFormDTO) {
			return ret;
		}
		Map<Long, Long> poPromotionIdMap = ordFormDTO.mapPromotionByPOId(false);
		Map<Long, Long> poPromotionIndexMap = ordFormDTO.mapPromotionByPOId(true);
		for (Entry<Long, Long> entry : poPromotionIdMap.entrySet()) {
			if (null == entry) {
				continue;
			}
			long poId = entry.getKey();
			long promotionId = entry.getValue();
			if (!poPromotionIndexMap.containsKey(poId)) {
				continue;
			}
			long promotionIndex = poPromotionIndexMap.get(poId);
			PromotionDTO promotion = promotionFacade.getPromotionById(promotionId);
			if (null == promotion) {
				continue;
			}
			// rhy说没关系...
			promotion.setIndex((int) promotionIndex);
			ret.put(poId, promotion);
		}
		return ret;
	}

	@Override
	public int confirmOrder(long orderId, long userId) {
		return orderService.setOrderFormStateToFinish(orderId, userId);
	}

	@Override
	public int updateInvoice(InvoiceDTO invoiceDTO) {
		return orderService.updateInvoice(invoiceDTO);
	}

	@Override
	public int addInvoice(InvoiceDTO invoiceDTO) {
		// 为空时,商家新增情景
		if (StringUtils.isEmpty(invoiceDTO.getTitle())) {
			String title = "";
			InvoiceInOrdDTO invoice = invoiceService.getInvoiceInOrdByOrderId(invoiceDTO.getOrderId(),
					invoiceDTO.getUserId());
			if (null == invoice) {// 情景:买家下单时没申请发票，运营新增发票，票头信息从Mmall_Order_InvoiceInOrd取而不是从TB_Order_InvoiceInOrd取
				List<InvoiceDTO> list = invoiceService.getInvoiceByOrderId(invoiceDTO.getOrderId());
				if (CollectionUtil.isEmptyOfList(list)) {
					return -1;
				}
				title = list.get(0).getTitle();
			} else {
				title = invoice.getTitle();
			}
			invoiceDTO.setTitle(title);

		}
		return orderService.addInvoice(invoiceDTO);
	}

	@Override
	public int addOrUpdateOrderLogistics(OrderLogisticsDTO orderLogisticsDTO) {
		return orderService.addOrUpdateOrderLogistics(orderLogisticsDTO);
	}

	@Override
	public int modifyOrderState(OrderOperateParam param) {
		return orderService.modifyOrderFormState(param);
	}

	@Override
	public int updateOrderExpInfo(FrontOrderExpInfoUpdateParam param) {
		if (null == param || null == param.getChgParam()) {
			return -1;
		}
		int result = orderService.updateOrderExpInfo(param.getOrderId(), param.getUserId(), param.getChgParam());
		return result;
	}

	@Override
	public int addOrUpdateOrderFormComment(OrderOperateParam param) {
		return orderService.addOrUpdateOrderCommnet(param);
	}

	@Override
	public RetArg queryOrderFormByOrderIdAndUserId(long userId, long orderId, Boolean isVisible) {
		RetArg retArg = new RetArg();
		OrderFormDTO orderFormDTO = orderService.queryOrderForm(userId, orderId, isVisible);
		if (orderFormDTO != null && orderFormDTO.getOrderFormState().equals(OrderFormState.CANCEL_ED)) {
			OrderCancelInfoDTO orderCancelInfoDTO = orderService.getOrderCancelInfo(userId, orderId);
			if (orderCancelInfoDTO != null) {
				orderFormDTO.setCancelReason(orderCancelInfoDTO.getReason());
				orderFormDTO.setCancelTime(orderCancelInfoDTO.getCtime());
			}
		}
		RetArgUtil.put(retArg, orderFormDTO);
		if (orderFormDTO == null) {
			return retArg;
		}
		List<TradeItemDTO> tradeItemDTOs = tradeFacade.getTradeItemDTOList(orderId, userId);
		if (CollectionUtil.isNotEmptyOfList(tradeItemDTOs)) {
			orderFormDTO.setPayOrderSn(tradeItemDTOs.get(0).getPayOrderSn());
		}
		buildStoreInfo(orderFormDTO.getOrderCartItemDTOList());
		CouponOrder couponOrder = couponOrderService
				.getCouponOrderByOrderIdOfUseType(orderFormDTO.getUserId(), orderId);
		// coupon：根据优惠券Code反查出优惠券
		Coupon coupon = null;
		if (null != couponOrder) {
			coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
		}
		RetArgUtil.put(retArg, new CouponDTO(coupon));
		return retArg;
	}

	// 设置店铺信息
	private void buildStoreInfo(List<? extends OrderCartItemDTO> orderCartItemDTOS) {
		if (orderCartItemDTOS == null) {
			return;
		}
		long businessId = 0l;
		for (OrderCartItemDTO orderCartItemDTO : orderCartItemDTOS) {
			if (CollectionUtil.isNotEmptyOfList(orderCartItemDTO.getOrderSkuDTOList())) {
				businessId = orderCartItemDTO.getOrderSkuDTOList().get(0).getSupplierId();
				orderCartItemDTO.setStoreName(businessService.getBusinessNameById(businessId, 0));
				orderCartItemDTO.setStoreUrl(UrlBaseUtil.buildStoreUrl(businessId));
			}
		}

	}

	@Override
	public List<Long> getSubOrderIds(long parentId) {
		return orderService.getSubOrderIdsByParentId(parentId);
	}

	public List<Long> getParentIds(long parentId, int count) {
		return orderService.getParentIds(parentId, count);
	}

	public List<OrderFormDTO> queryOrderFormListByParentId(long parentId, boolean isVisible) {
		return orderService.getOrderFormByParentId(parentId, isVisible);
	}

	@Override
	public RetArg cancelOrders(List<OrderCancelInfoDTO> cancelDTOs) {
		long timeout = 60 * 1000;
		// 0.参数准备
		RetArg retArg = new RetArg();
		boolean isVisible = true, isSucc = true, isRecyleStock = true;
		List<OrderCancelInfoDTO> filterDtos = new ArrayList<OrderCancelInfoDTO>();
		List<Long> orderIds = new ArrayList<Long>();
		long userId = 0;

		// 订单是否是货到付款
		boolean isOrderoffline = false;
		for (OrderCancelInfoDTO cancelDTO : cancelDTOs) {
			userId = cancelDTO.getUserId();
			long currTime = System.currentTimeMillis(), orderId = cancelDTO.getOrderId();
			cancelDTO.setCtime(currTime);
			if (userId <= 0 || orderId <= 0) {
				RetArgUtil.put(retArg, false);
				return retArg;
			}
			OrderFormBriefDTO orderBDTO = queryOrderFormBriefDTO(userId, orderId, null);
			if (orderBDTO == null) {
				RetArgUtil.put(retArg, false);
				return retArg;
			}
			isOrderoffline = !OrderFormPayMethod.isOnlinePayMethod(orderBDTO.getOrderFormPayMethod());
			// 货到付款目前只有运营允许取消订单
			if (!cancelDTO.getOperateUserType().equals(OperateUserType.CMSER)
					&& !cancelDTO.getOperateUserType().equals(OperateUserType.ERP)
					&& !OrderFormPayMethod.isOnlinePayMethod(orderBDTO.getOrderFormPayMethod())) {
				RetArgUtil.put(retArg, false);
				return retArg;
			}
			// 商家关闭订单，先判断订单是否属于该商家
			if (cancelDTO.getBusinessId() > 0 && orderBDTO.getBusinessId() != cancelDTO.getBusinessId()) {
				RetArgUtil.put(retArg, false);
				return retArg;
			}
			// 判断订单是否是重复取消
			if (orderBDTO.getOrderFormState() != OrderFormState.CANCEL_ED) {
				filterDtos.add(cancelDTO);
				orderIds.add(orderId);
			}
		}
		// 1.获得注册的TCC事务管理器
		Transaction tx = null;
		try {
			tx = tccManager.beginTransaction("callOffOrderTCCActivity");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		if (tx == null) {
			RetArgUtil.put(retArg, false);
			return retArg;
		}

		// 2.获得tranId
		long tranId = tx.getUUID();
		logger.debug("tranId=" + tranId);
		// 3.按顺序执行try步骤
		try {
			// 3.1 调用取消订单服务
			orderTCCService.tryCallOffOrderByTCC(tranId, filterDtos, isRecyleStock);
			Future<Boolean> tryCallOffOrderByTCCResultFuture = RpcContext.getContext().getFuture();

			Future<Boolean> tryAddRecycleTCCResultFuture = null;
			// 货到付款的不回收
			if (!isOrderoffline) {
				// 3.2 调用回收促销信息服务
				TCCParamDTO tccParamDTO = new TCCParamDTO();
				tccParamDTO.setUserId(userId);
				tccParamDTO.setOrderIdList(orderIds);
				recycleTCCService.tryAddRecycleTCC(tranId, tccParamDTO);
				tryAddRecycleTCCResultFuture = RpcContext.getContext().getFuture();
			}

			// 3.3 获得返回结果
			Boolean tryCallOffOrderByTCCResult = tryCallOffOrderByTCCResultFuture.get(timeout, TimeUnit.MILLISECONDS);
			Boolean tryAddRecycleTCCResult = tryAddRecycleTCCResultFuture == null ? true : tryAddRecycleTCCResultFuture
					.get(timeout, TimeUnit.MILLISECONDS);
			isSucc = tryCallOffOrderByTCCResult == Boolean.TRUE && tryAddRecycleTCCResult == Boolean.TRUE;
			if (!isSucc)
				throw new ServiceException("cancelOrder.try fail! tryCallOffOrderByTCCResult="
						+ tryCallOffOrderByTCCResult);// +
														// " tryAddRecycleTCCResult="
														// +
														// tryAddRecycleTCCResult);
		} catch (Exception ex) {
			isSucc = false;
			logger.error(ex.getMessage(), ex);
			// 如果有异常,则执行cancel步骤
			try {
				tx.cancel();
			} catch (Exception ex2) {
				logger.error(ex2.getMessage(), ex2);
			}
		}

		// 4.执行Confirm步骤
		if (isSucc) {
			try {
				tx.confirm();
			} catch (Exception ex) {
				isSucc = false;
				logger.error(ex.getMessage(), ex);
			}
		}
		// 限购
		Map<Long, OrderSkuDTO> orderSkuMap = new HashMap<Long, OrderSkuDTO>();
		// 5.执行恢复购物车库存的操作
		if (isSucc && isRecyleStock) {
			for (Long orderId : orderIds) {
				// 读取完整的订单数据
				OrderFormDTO orderDTO = queryOrderForm(userId, orderId, isVisible);
				addInventoryCountForCancelOrder(orderDTO);
				if (orderDTO != null) {
					for (OrderSkuDTO orderSkuDTO : orderDTO.getAllOrderSkuDTOList()) {
						orderSkuMap.put(orderSkuDTO.getSkuId(), orderSkuDTO);
					}
				}
			}
		}

		// 6.尝试调用取消交易的逻辑
		if (isSucc) {
			for (Long orderId : orderIds) {
				OrderCancelInfoDTO cancelDTOOfDB = orderService.getOrderCancelInfo(userId, orderId);
				this.cancelTrade(cancelDTOOfDB);
			}
			// this.cancelOmsOrder(cancelDTOOfDB);
		}

		// 7.尝试恢复限购数据的逻辑
		if (isSucc) {
			Map<Long, ProductSKULimitConfigDTO> skuLimitConfigMap = productSKULimitService
					.getProductSKULimitConfigDTOMap(userId, new ArrayList<Long>(orderSkuMap.keySet()));
			Map<Long, OrderSkuDTO> limitOrderSkuMap = new HashMap<Long, OrderSkuDTO>();
			for (Long skuId : skuLimitConfigMap.keySet()) {
				limitOrderSkuMap.put(skuId, orderSkuMap.get(skuId));
			}
			productFacade.recoverOrderSkuLimit(userId, limitOrderSkuMap);
		}

		// 7.返回结果
		// 7.1 查询订单信息
		// OrderFormDTO orderDTO = orderService.queryOrderForm(userId, orderId,
		// isVisible);
		// 7.2 查询在线支付退款金额
		// if (isSucc) {
		// List<TradeItemDTO> tradeDTOList =
		// tradeFacade.getTradeItemDTOList(orderId, userId);
		// TradeItemDTO tradeDTO = (TradeItemDTO)
		// TradeApiUtil.getTradeOfOnlineAndRefund(tradeDTOList);
		// if (tradeDTO != null)
		// RetArgUtil.put(retArg, tradeDTO.getCash());
		// }
		// 7.3 设置返回结果
		RetArgUtil.put(retArg, isSucc);
		return retArg;
	}

	@SuppressWarnings("unchecked")
	public RetArg queryNewOrderList(OrderSearchParam orderSearchParam) {
		RetArg retArg = orderService.queryOrderFormListByOrderSearchParam(orderSearchParam);
		List<OrderFormDTO> orderFormDTOs = RetArgUtil.get(retArg, ArrayList.class);
		if (orderFormDTOs == null) {
			return retArg;
		}
		RetArgUtil.put(retArg, convertToOrderFormListVO(orderFormDTOs));
		RetArgUtil.put(retArg, RetArgUtil.get(retArg, OrderSearchParam.class));
		return retArg;
	}

	private List<OrderFormListVO> convertToOrderFormListVO(List<OrderFormDTO> orderFormDTOs) {
		List<OrderFormListVO> orderFormListVOs = new ArrayList<OrderFormListVO>();
		long businessId = 0l;
		int countFlag = 0, countNum = 0;
		OrderFormListVO preOrderFormListVO = null, parentOrderFormListVO = null;
		for (OrderFormDTO orderFormDTO : orderFormDTOs) {
			OrderFormListVO orderFormListVO = new OrderFormListVO(orderFormDTO);
			businessId = orderFormDTO.getOrderSkuDTOListOfOrdGift().get(0).getSupplierId();
			orderFormListVO.setStoreName(businessService.getBusinessNameById(businessId, 0));
			orderFormListVO.setStoreUrl(UrlBaseUtil.buildStoreUrl(businessId));
			// 在线支付未付款时根据parentId讲多个关联订单合并成一个
			if (preOrderFormListVO != null
					&& orderFormListVO.getParentId() == preOrderFormListVO.getParentId()
					&& (orderFormListVO.getOrderFormState().equals(OrderFormState.WAITING_PAY) && orderFormListVO
							.isOnpay())) {
				if (countFlag == 0) {
					parentOrderFormListVO = new OrderFormListVO();
					parentOrderFormListVO.setParentId(orderFormListVO.getParentId());
					parentOrderFormListVO.setOrderTime(DateFormatEnum.TYPE5.getFormatDate(orderFormDTO.getOrderTime()));
					parentOrderFormListVO.setCartRPrice(preOrderFormListVO.getCartRPrice());
					parentOrderFormListVO.setOrderFormState(preOrderFormListVO.getOrderFormState());
					parentOrderFormListVO.setPayCloseCDAndOrderForm(orderFormDTO.getOrderTime());
					parentOrderFormListVO.addSubOrderFormListVO(preOrderFormListVO);
				}
				parentOrderFormListVO.setCartRPrice(orderFormListVO.getCartRPrice().add(
						parentOrderFormListVO.getCartRPrice()));
				parentOrderFormListVO.addSubOrderFormListVO(orderFormListVO);
				if (countNum == orderFormDTOs.size() - 1) {
					orderFormListVOs.add(parentOrderFormListVO);
				}
				++countFlag;
			} else {
				// 没有合并的订单
				if (countFlag == 0 && preOrderFormListVO != null) {
					orderFormListVOs.add(preOrderFormListVO);
					if (countNum == orderFormDTOs.size() - 1) {
						orderFormListVOs.add(orderFormListVO);
					}
				}
				// 有需要合并的订单
				if (countFlag != 0 && preOrderFormListVO != null) {
					orderFormListVOs.add(parentOrderFormListVO);
					countFlag = 0;
				}
			}

			preOrderFormListVO = orderFormListVO;
			countNum++;
		}
		// 只有一个订单的情况
		if (countNum == 1) {
			orderFormListVOs.add(preOrderFormListVO);
		}
		return orderFormListVOs;
	}

	@Override
	public OrderSkuDTO getSkuSnapShot(long userId, long orderId, long skuId) {
		return orderService.getOrderSku(userId, orderId, skuId);
	}
}
