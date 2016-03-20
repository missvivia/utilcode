package com.xyl.mmall.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.bi.core.meta.ChangePaymentParam;
import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.facade.TradeFacade;
import com.xyl.mmall.common.out.facade.PayFacade;
import com.xyl.mmall.common.param.OrderFacadeComposeOrderParam;
import com.xyl.mmall.content.annotation.PaySuccessActivity;
import com.xyl.mmall.framework.annotation.CheckFormToken;
import com.xyl.mmall.framework.enums.ComposeOrderErrorCodeEnum;
import com.xyl.mmall.framework.enums.SpSource;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.util.DateUtils;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.mainsite.facade.CalCenterFacade;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.CartInnervVO;
import com.xyl.mmall.mainsite.vo.order.OrderForm1VO;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;
import com.xyl.mmall.mainsite.vo.order.OrderFormPayMethod1VO;
import com.xyl.mmall.order.api.util.TradeApiUtil;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.TradeItem;
import com.xyl.mmall.order.result.OrderCalServiceGenOrderResult;
import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionCartDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.meta.RedPacketOrder;
import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.promotion.service.RedPacketShareRecordService;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.utils.ActivationUtils;
import com.xyl.mmall.saleschedule.service.ProductSKULimitService;
import com.xyl.mmall.util.AreaUtils;

/**
 * 下单页面
 * 
 */
@Controller
@RequestMapping("/purchase")
public class PurchaseController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private CalCenterFacade calCenterFacade;

	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;

	@Autowired
	private TradeFacade tradeFacade;

	@Autowired
	private PromotionFacade promotionFacade;

	@Autowired
	private RedPacketShareRecordService redPacketShareRecordService;

	@Autowired
	private ReturnPackageFacade returnPackageFacade;

	@Autowired
	private RedPacketService redPacketService;

	@Autowired
	private RedPacketOrderService redPacketOrderService;

	@Autowired
	private ItemProductService itemProductService;

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ProductSKULimitService productSKULimitService;

	@Autowired
	private PayFacade payFacade;

	/**
	 * Session的参数名前缀: 和RequestId匹配的OrderId
	 */
	private static final String SESSION_NAME_PREFFIX_ORDERID = "orderId-";

	/**
	 * 确认订单页-展示页面
	 * 
	 * @param model
	 * @param cartIds
	 * @param cartEndTime
	 * @param response
	 * @return
	 */
	@BILog(action = "page", type = "orderConfirmPage", clientType = "wap")
	@RequestMapping(value = "/index")
	public ModelAndView index(Model model, @RequestParam(value = "cartIds") String cartIds,
			@RequestParam(value = "skusPrice", defaultValue = "") String skusPrice, HttpServletResponse response) {
		super.setNoCache(response);
		long currTime = System.currentTimeMillis(), userId = getUserId();

		// 0.先判断nvk缓存库存
		// if(!cartFacade.isInventoryEnough(userId, getProviceId(), cartIds)){
		// return orderfail(0);//库存不足
		// }

		// 1.读取用户任意一条地址信息
		ConsigneeAddressDTO caDTO = consigneeAddressFacade.getOneAddress(userId);

		// 2.尝试组单
		if (caDTO != null) {
			OrderFacadeComposeOrderParam param = new OrderFacadeComposeOrderParam();
			param.setUserId(userId);
			param.setCaId(caDTO.getId());
			param.setProvinceId(getProviceId());
			param.setCartIds(cartIds);
			param.setSkusPrice(skusPrice);
			param.setSource(OrderFormSource.PC);
			RetArg retArg = genOrderForm1VO(param);
			ModelAndView errorMV = genComposeErrorMV(retArg);
			if (errorMV != null)
				return errorMV;
		}

		// BI统计数据
		// OrderConfirmPageParam orderConfirmPageParam = new
		// OrderConfirmPageParam();
		// orderConfirmPageParam.setCartIds(cartIds);
		// orderConfirmPageParam.setHasCa(caDTO != null);
		// model.addAttribute("orderConfirmPageParam",
		// JsonUtils.toJson(orderConfirmPageParam));

		// 正常返回
		// Map<String, Object> modelMap = new HashMap<>();
		// modelMap.put("cartIds", StringUtils.isBlank(cartIds) ? "" : cartIds);
		// modelMap.put("currTime", currTime);
		// modelMap.put("requestId", currTime);
		// modelMap.put("weekOfDate", DateUtils.getWeekOfDate(null));
		ModelAndView mv = new ModelAndView("redirect:/purchase/orderInfo");
		model.addAttribute("cartIds", StringUtils.isBlank(cartIds) ? "" : cartIds);
		// mv.addAllObjects(modelMap);
		return mv;
	}

	/**
	 * 生成组单失败的MV
	 * 
	 * @param retArgOfComposeOrder
	 * @return
	 */
	private ModelAndView genComposeErrorMV(RetArg retArgOfComposeOrder) {
		ComposeOrderErrorCodeEnum orderErrorEnum = RetArgUtil
				.get(retArgOfComposeOrder, ComposeOrderErrorCodeEnum.class);
		// 如果组单失败,则跳转到出错页面
		if (orderErrorEnum.equals(ComposeOrderErrorCodeEnum.ORDER_SUCCESS))
			return null;
		else
			return orderfail(orderErrorEnum.getIntValue());
	}

	@CheckFormToken(isCheckRepeat = false)
	@RequestMapping(value = "/orderInfo", method = RequestMethod.GET)
	public ModelAndView getOrder(Model model, @RequestParam(value = "cartIds") String cartIds) {
		long currTime = System.currentTimeMillis();
		model.addAttribute("cartIds", StringUtils.isBlank(cartIds) ? "" : cartIds);
		model.addAttribute("currTime", currTime);
		model.addAttribute("requestId", currTime);
		model.addAttribute("weekOfDate", DateUtils.getWeekOfDate(null));
		return new ModelAndView("pages/cart/orderinfo", model.asMap());
	}

	// for test
	@RequestMapping(value = "/orderpay", method = RequestMethod.GET)
	public String getOrderPay(Model model) {
		return "pages/cart/orderpay";
	}

	/**
	 * 确认订单页-组单
	 * 
	 * @param model
	 * @param cartIds
	 * @param cartEndTime
	 * @param caId
	 * @param userCouponId
	 * @param hbCashStr
	 *            -1:自动使用
	 * @param payMethodStr
	 *            默认使用支付宝(2)
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/composeOrder")
	public ModelAndView composeOrder(Model model, @RequestParam(value = "cartIds") String cartIds,
			@RequestParam(value = "cartEndTime") long cartEndTime, @RequestParam(value = "addressId") long addressId,
			@RequestParam(value = "userCouponId", required = false, defaultValue = "0") long userCouponId,
			@RequestParam(value = "hbCash", required = false, defaultValue = "-1") String hbCashStr,
			@RequestParam(value = "payMethod", required = false, defaultValue = "2") String payMethodStr,
			@RequestParam(value = "receipt", required = false) String invoiceTitle, HttpServletResponse response) {
		// 0.参数准备
		super.setNoCache(response);

		long currTime = System.currentTimeMillis(), userId = getUserId();
		int payMethodInt = NumberUtils.isNumber(payMethodStr) ? Integer.valueOf(payMethodStr) : -1;
		OrderFormPayMethod orderFormPayMethod = OrderFormPayMethod.genEnumByIntValueSt(payMethodInt);

		// 1.组单
		OrderFacadeComposeOrderParam param = new OrderFacadeComposeOrderParam();
		param.setUserId(userId);
		param.setCaId(addressId);
		param.setProvinceId(getProviceId());
		param.setCartIds(cartIds);
		param.setPayMethodInt(payMethodInt);
		param.setUserCouponId(userCouponId);
		if (StringUtils.isNotBlank(hbCashStr) && NumberUtils.isNumber(hbCashStr))
			param.setHbCash(new BigDecimal(hbCashStr));
		else
			param.setHbCash(new BigDecimal("-1"));
		param.setSource(OrderFormSource.PC);
		RetArg retArg = genOrderForm1VO(param);
		OrderFormCalDTO orderCalDTO = RetArgUtil.get(retArg, OrderFormCalDTO.class);
		Integer retCodeOfOrderService = RetArgUtil.get(retArg, Integer.class, 0);
		OrderForm1VO order1VO = RetArgUtil.get(retArg, OrderForm1VO.class);

		// 2.修改用户选中的支付方式
		if (orderCalDTO != null) {
			if (orderFormPayMethod == null || orderFormPayMethod != orderCalDTO.getOrderFormPayMethod()) {
				// 设置可选的支付方式为不选中
				for (OrderFormPayMethod1VO payMethod1VO : order1VO.getPayMethodArray()) {
					payMethod1VO.setSelected(false);
				}
			}
		}
		// 3.生成返回结果JSON
		Map<String, Object> jsonMap2 = new LinkedHashMap<>();
		jsonMap2.put("orderForm1VO", order1VO);
		jsonMap2.put("cartIds", cartIds);
		jsonMap2.put("currTime", currTime);
		jsonMap2.put("cartEndTime", cartEndTime);

		Map<String, Object> jsonMap1 = new LinkedHashMap<>();
		int errorCode = genCodeForComposeOrder(retCodeOfOrderService);
		jsonMap1.put("code", errorCode == 200 ? errorCode : errorCode + 500);
		jsonMap1.put("result", jsonMap2);
		model.addAllAttributes(jsonMap1);

		return new ModelAndView("http://127.0.0.1:8095/purchase/orderpay");
	}

	/**
	 * 生成组单逻辑返回给页面的JSON结果Code值
	 * 
	 * @param retCodeOfOrderService
	 * @return 200: 组单成功<br>
	 *         0: 组单失败-库存不足<br>
	 *         1: 组单失败-优惠过期<br>
	 *         3: 组单失败-未知原因 4: 组单失败-订单总价金额超过1亿 5: 组单失败-特许经营商家不允许非指定用户下单 6:
	 *         组单失败-下单是商品价格发生变动 7: 组单失败-您选的商品包含不支持当前地区配送 8: 组单失败-订单非法参数
	 *         --orderfail 直接传值8 9: 组单失败-您选的商品包含超过限购数量或者时间
	 */
	private int genCodeForComposeOrder(Integer retCodeOfOrderService) {
		/**
		 * retCodeOfOrderService定义: <br>
		 * 0:失败, 1:成功, 2:库存不足, 3:收货地址为空<br>
		 * 4:购买明细为空或者不满足起批条件, 5:输入参数不对,6。订单总价金额超过1亿 7.特许经营商家不允许非指定用户下单
		 * 8.下单是商品价格发生变动 9.您选的商品包含不支持当前地区配送 10.您选的商品包含超过限购数量或者时间
		 */
		// CASE0: 组单失败-未知原因
		if (retCodeOfOrderService == null)
			return 3;
		// CASE1: 组单成功
		else if (retCodeOfOrderService == 1)
			return 200;
		// CASE2: 组单失败-库存不足
		else if (retCodeOfOrderService == 2)
			return 0;
		// CASE3: 组单失败-促销失效
		else if (retCodeOfOrderService == 4)
			return 1;
		// CASE4: 组单失败-订单总价金额超过1亿
		else if (retCodeOfOrderService == 6)
			return 4;
		// CASE5: 组单失败-特许经营商家不允许非指定用户下单
		else if (retCodeOfOrderService == 7)
			return 5;
		// CASE6: 组单失败-下单是商品价格发生变动
		else if (retCodeOfOrderService == 8)
			return 6;
		// CASE7: 组单失败-您选的商品包含不支持当前地区配送
		else if (retCodeOfOrderService == 9)
			return 7;
		// CASE8: 组单失败-您选的商品包含超过限购数量或者时间
		else if (retCodeOfOrderService == 10)
			return 9;
		// CASE-Default： 组单失败-未知原因
		else
			return 3;
	}

	// /**
	// * 确认支付页面-下单
	// *
	// * @param model
	// * @param request
	// * @param response
	// * @param cartIds
	// * @param caId
	// * 收货地址Id
	// * @param userCouponId
	// * @param payMethodInt
	// * @param invoiceTitle
	// * @param hbCashStr
	// * @param requestId
	// * @return
	// * @throws IOException
	// */
	// @BILog(action = "page", type = "orderSubmitPage", clientType="wap")
	// @RequestMapping(value = "/buy")
	// public ModelAndView buy(Model model, HttpServletRequest request,
	// HttpServletResponse response,
	// @RequestParam(value = "cartIds") String cartIds, @RequestParam(value =
	// "addressId") long caId,
	// @RequestParam(value = "userCouponId", required = false, defaultValue =
	// "0") long userCouponId,
	// @RequestParam(value = "payMethod", required = false, defaultValue = "-1")
	// int payMethodInt,
	// @RequestParam(value = "receipt", required = false) String invoiceTitle,
	// @RequestParam(value = "hbCash", required = false) String hbCashStr,
	// @RequestParam(value = "requestId") String requestId) throws IOException {
	// // 0.参数准备
	// super.setNoCache(response);
	// // List<CartItemDTO>cartItemDTOs = convertToValidCartItemDTO(cartIds);
	// // boolean result = cartFacade.decreaseInventory(cartItemDTOs);
	// // if(!result){
	// // return orderfail(400);
	// // }
	// long userId = getUserId();
	// // 1.组单
	// OrderFacadeComposeOrderParam param = new OrderFacadeComposeOrderParam();
	// param.setUserId(userId);
	// param.setCaId(caId);
	// param.setProvinceId(getProviceId());
	// param.setCartIds(cartIds);
	// param.setInvoiceTitle(invoiceTitle);
	// param.setPayMethodInt(payMethodInt);
	// param.setUserCouponId(userCouponId);
	// if (StringUtils.isNotBlank(hbCashStr) && NumberUtils.isNumber(hbCashStr))
	// param.setHbCash(new BigDecimal(hbCashStr));
	//
	// param.setSource(OrderFormSource.PC);
	// RetArg retArgOfGenOrder = genOrderForm1VO(param);
	// OrderFormCalDTO orderCalDTO = RetArgUtil.get(retArgOfGenOrder,
	// OrderFormCalDTO.class);
	// FavorCaculateResultDTO fcResultDTO = RetArgUtil.get(retArgOfGenOrder,
	// FavorCaculateResultDTO.class);
	// ModelAndView errorMV = genComposeErrorMV(retArgOfGenOrder);
	// if (errorMV != null){
	// //加回库存缓存
	// //cartFacade.addInventoryCount(cartItemDTOs);
	// return errorMV;
	// }
	//
	// // 2.添加订单
	// RetArg retArgOfMakeOrder = orderFacade.makeOrder(orderCalDTO,
	// fcResultDTO, cartIds);
	// Boolean isSucc = RetArgUtil.get(retArgOfMakeOrder, Boolean.class);
	// long orderId = orderCalDTO != null ? orderCalDTO.getOrderId() : -100L;
	// // 保存orderId到session里
	// String sessionNameOfOrderId = genSessionNameOfOrderId(requestId);
	// request.getSession().setAttribute(sessionNameOfOrderId, orderId);
	// // BI统计数据
	// model.addAttribute("orderId", orderId);
	//
	// // 3.跳转到支付页面
	// PurchaseController thisProxy = ((PurchaseController)
	// AopContext.currentProxy());
	// return thisProxy.firstPay(isSucc == Boolean.TRUE ? orderId : -100L,
	// response);
	// }

	/**
	 * 确认支付页面-下单
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param cartIds
	 * @param caId
	 *            收货地址Id
	 * @param userCouponId
	 * @param payMethodInt
	 * @param invoiceTitle
	 * @param hbCashStr
	 * @param requestId
	 * @return
	 * @throws IOException
	 */
	@BILog(action = "page", type = "orderSubmitPage", clientType = "wap")
	@CheckFormToken(isCheckRepeat = false)
	@RequestMapping(value = "/buy")
	public ModelAndView buy(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cartIds") String cartIds,
			@RequestParam(value = "skusPrice", defaultValue = "") String skusPrice,
			@RequestParam(value = "addressId") long caId,
			@RequestParam(value = "userCouponId", required = false, defaultValue = "0") long userCouponId,
			@RequestParam(value = "payMethod", required = false, defaultValue = "-1") int payMethodInt,
			@RequestParam(value = "receipt", required = false) String invoiceTitle,
			@RequestParam(value = "hbCash", required = false) String hbCashStr,
			@RequestParam(value = "requestId") String requestId) throws IOException {
		// 0.参数准备
		// userCouponId = 1000042;
		super.setNoCache(response);
		long userId = getUserId();

		// 验证参数
		if (StringUtils.isEmpty(cartIds) || caId == 0) {
			return orderfail(ComposeOrderErrorCodeEnum.ORDER_PARAM_ERROR.getIntValue());
		}

		// .先判断nvk缓存库存
		// if(!cartFacade.isInventoryEnough(userId, getProviceId(), cartIds)){
		// return orderfail(0);//库存不足
		// }
		// 1.0 根据cartIds中的cartId生成对应的businessIds
		List<String> cartIdList = Arrays.asList(cartIds.split(","));
		Map<Long, StringBuilder> businessIds = itemProductService.getProductBusinessIdsByIds(cartIdList);

		// 1.1 组单
		Map<Long, OrderFormCalDTO> orderFormCalDTOMap = new HashMap<Long, OrderFormCalDTO>();
		Map<Long, FavorCaculateResultDTO> fcResultDTOMap = new HashMap<Long, FavorCaculateResultDTO>();
		Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
		for (Map.Entry<Long, StringBuilder> entry : businessIds.entrySet()) {
			OrderFacadeComposeOrderParam param = new OrderFacadeComposeOrderParam();
			param.setUserId(userId);
			param.setCaId(caId);
			param.setProvinceId(getProviceId());
			param.setCartIds(entry.getValue().toString());
			param.setInvoiceTitle(invoiceTitle);
			param.setPayMethodInt(payMethodInt);
			param.setUserCouponId(userCouponId);
			param.setSkusPrice(skusPrice);
			if (StringUtils.isNotBlank(hbCashStr) && NumberUtils.isNumber(hbCashStr))
				param.setHbCash(new BigDecimal(hbCashStr));
			param.setSource(OrderFormSource.PC);
			param.setSpSource(SpSource.MMALL);// 正常订单，不同于web版
			RetArg retArgOfGenOrder = genOrderForm1VO(param);
			OrderFormCalDTO orderCalDTO = RetArgUtil.get(retArgOfGenOrder, OrderFormCalDTO.class);
			FavorCaculateResultDTO fcResultDTO = RetArgUtil.get(retArgOfGenOrder, FavorCaculateResultDTO.class);
			ModelAndView errorMV = genComposeErrorMV(retArgOfGenOrder);
			if (errorMV != null) {
				return errorMV;
			}
			for (OrderSkuDTO orderSkuDTO : orderCalDTO.getAllOrderSkuDTOList()) {
				skuCountMap.put(orderSkuDTO.getSkuId(), orderSkuDTO.getTotalCount());
			}
			orderFormCalDTOMap.put(entry.getKey(), orderCalDTO);
			fcResultDTOMap.put(entry.getKey(), fcResultDTO);
		}

		// 1.1 如果使用优惠券，计算优惠券
		if (userCouponId > 0) {
			CouponDTO couponDTO = userCouponService.getUserCouponDTO(userId, userCouponId,
					ActivationConstants.STATE_CAN_USE, true);
			if (couponDTO == null) {
				// 优惠券无效
				return orderfail(ComposeOrderErrorCodeEnum.ORDER_COUPON_INVALID.getIntValue());
			}
			Coupon coupon = couponService.getCouponByCode(couponDTO.getCouponCode(), true);
			if (coupon == null) {
				// 优惠券无效
				return orderfail(ComposeOrderErrorCodeEnum.ORDER_COUPON_INVALID.getIntValue());
			}
			setOrderCouponParam(orderFormCalDTOMap, fcResultDTOMap, userId, userCouponId, coupon);
		}

		ModelAndView resultView = null;
		try {
			// 更新限购记录表
			productSKULimitService.batchChangeProductSKULimitRecords(userId, skuCountMap);
			// 2.添加订单
			RetArg retArgOfMakeOrder = orderFacade.makeMmallOrder(orderFormCalDTOMap, fcResultDTOMap, businessIds);
			Boolean isSucc = RetArgUtil.get(retArgOfMakeOrder, Boolean.class);
			Long parentOrderId = RetArgUtil.get(retArgOfMakeOrder, Long.class);
			Integer resultCode = RetArgUtil.get(retArgOfMakeOrder, Integer.class);
			parentOrderId = parentOrderId != null ? parentOrderId : -100L;
			// 保存orderId到session里
			String sessionNameOfOrderId = genSessionNameOfOrderId(requestId);
			request.getSession().setAttribute(sessionNameOfOrderId, parentOrderId);
			// BI统计数据
			model.addAttribute("orderId", parentOrderId);

			// 3.跳转到支付页面
			PurchaseController thisProxy = ((PurchaseController) AopContext.currentProxy());

			if (resultCode.intValue() == 2) {
				// 表示库存不足
				resultView = thisProxy.firstPay(-200L, response);
			}
			resultView = thisProxy.firstPay(isSucc == Boolean.TRUE ? parentOrderId : -100L, response);
		} catch (ServiceException e) {
			logger.error("batch change product sku error by user :" + userId, e);
			resultView = new ModelAndView("pages/404");
		} catch (Exception e) {
			logger.error("buy error by user :" + userId, e);
			resultView = new ModelAndView("pages/404");
		}
		return resultView;
	}

	/**
	 * 构建订单优惠券信息
	 * 
	 * @param orderFormCalDTOMap
	 * @param fcResultDTOMap
	 * @param userId
	 * @param userCouponId
	 * @param coupon
	 */
	private void setOrderCouponParam(Map<Long, OrderFormCalDTO> orderFormCalDTOMap,
			Map<Long, FavorCaculateResultDTO> fcResultDTOMap, long userId, long userCouponId, Coupon coupon) {
		List<Activation> activations = ActivationUtils.containActivations(coupon.getItems());
		String value = activations.get(0).getResult().get(0).getValue();
		BigDecimal couponValue = new BigDecimal(value);
		BigDecimal grossPrice = new BigDecimal(0);
		Iterator<Map.Entry<Long, OrderFormCalDTO>> iterator = orderFormCalDTOMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Long, OrderFormCalDTO> entry = iterator.next();
			grossPrice = grossPrice.add(entry.getValue().getCartRPrice());
		}
		iterator = orderFormCalDTOMap.entrySet().iterator();
		BigDecimal last = new BigDecimal(0);
		while (iterator.hasNext()) {
			Map.Entry<Long, OrderFormCalDTO> entry = iterator.next();
			BigDecimal newTotal = BigDecimal.ZERO;
			if (iterator.hasNext()) {
				BigDecimal currentTotalPrice = entry.getValue().getCartRPrice();
				BigDecimal couponDiscount = currentTotalPrice.multiply(couponValue).divide(grossPrice, 2,
						BigDecimal.ROUND_HALF_UP);
				last = last.add(couponDiscount);
				entry.getValue().setCouponDiscount(couponDiscount);
				newTotal = currentTotalPrice.subtract(couponDiscount);
			} else {
				entry.getValue().setCouponDiscount(couponValue.subtract(last));
				newTotal = entry.getValue().getCartRPrice().subtract(entry.getValue().getCouponDiscount());
			}
			if (newTotal.compareTo(BigDecimal.ZERO) == -1) {
				newTotal = BigDecimal.ZERO;
			}
			entry.getValue().setCartRPrice(newTotal);
			entry.getValue().setRealCash(newTotal);

			CouponOrderTCC couponOrderTCC = new CouponOrderTCC();
			couponOrderTCC.setCouponCode(coupon.getCouponCode());
			couponOrderTCC.setCouponHandlerType(ActivationHandlerType.DEFAULT);
			couponOrderTCC.setCouponOrderType(CouponOrderType.USE_COUPON);
			couponOrderTCC.setUserId(userId);
			couponOrderTCC.setCouponIdx(0);
			couponOrderTCC.setUserCouponId(userCouponId);
			fcResultDTOMap.get(entry.getKey()).getCouponOrderTCCList().add(couponOrderTCC);
		}
	}

	/**
	 * Session的参数名: 和RequestId匹配的OrderId
	 * 
	 * @param requestId
	 * @return
	 */
	private String genSessionNameOfOrderId(String requestId) {
		return StringUtils.isNotBlank(requestId) ? SESSION_NAME_PREFFIX_ORDERID + requestId : null;
	}

	/**
	 * 下单完成后,弹出的DIV对应的链接处理
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param requestId
	 * @param type
	 *            0: 跳转到重新支付页面<br>
	 *            1: 跳转到修改支付方式为COD页面<br>
	 *            2: 已完成支付按钮<br>
	 *            3: 弹框超时跳转
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/buyResultDiv")
	public ModelAndView buyResultDiv(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "requestId") String requestId, @RequestParam(value = "type") int type)
			throws IOException {
		// 1.根据RequestId,读取保存在Session里的OrderId
		Long orderId = (Long) request.getSession().getAttribute(genSessionNameOfOrderId(requestId));
		PurchaseController thisProxy = ((PurchaseController) AopContext.currentProxy());
		if (orderId == null) {
			orderId = -1L;
		}

		// 2.查询 已支付+未支付的现金交易
		TradeItemDTO tradeDTOOfOnlineAndPayed = null;
		if (orderId > 0) {
			List<TradeItemDTO> tradeDTOList = tradeFacade.getTradeItemDTOList(orderId, getUserId());
			tradeDTOOfOnlineAndPayed = (TradeItemDTO) TradeApiUtil.getTradeOfOnlineAndPayed(tradeDTOList);
			tradeDTOOfOnlineAndPayed = tradeDTOOfOnlineAndPayed == null ? (TradeItemDTO) TradeApiUtil
					.getTradeOfZeroPayed(tradeDTOList) : tradeDTOOfOnlineAndPayed;
		}
		boolean isPayed = tradeDTOOfOnlineAndPayed != null;

		// CASE1: 跳转到重新支付页面 + 超时跳转
		if (type == 0 || type == 3 || (type == 2 && isPayed)) {
			return thisProxy.firstPay(orderId, response);
		}
		// CASE2: 跳转到修改支付方式为COD页面
		else if (type == 1) {
			return thisProxy.toCod(model, orderId);
		}
		// CASE3: 已完成支付按钮
		else if (type == 2) {
			response.sendRedirect("/myorder/");
			return null;
		}
		return null;
	}

	/**
	 * 绑定优惠券
	 * 
	 * @param model
	 * @param cartIds
	 * @param couponCode
	 */
	@RequestMapping("/bindcoupon")
	public ModelAndView ajaxCouponBinder(Model model, @RequestParam(value = "cartIds") String cartIds,
			@RequestParam(value = "couponCode") String couponCode) {
		long userId = SecurityContextUtils.getUserId();
		// 2.生成返回结果JSON
		Map<String, Object> jsonMap2 = new LinkedHashMap<>();
		Map<String, Object> jsonMap1 = new LinkedHashMap<>();

		if (StringUtils.isBlank(cartIds)) {
			jsonMap2.put("state", ActivationConstants.STATE_NOT_EXISTS);
			jsonMap1.put("code", 200);
			jsonMap1.put("result", jsonMap2);
			return new ModelAndView();
		}

		FavorCaculateParamDTO paramDTO = new FavorCaculateParamDTO();
		paramDTO.setCouponCode(couponCode);
		paramDTO.setUserId(userId);

		int provinceId = getProviceId();
		List<Long> filterList = new ArrayList<>();
		for (String cartId : cartIds.split(",")) {
			if (NumberUtils.isNumber(cartId))
				filterList.add(Long.valueOf(cartId));
		}
		// 获取购物车并计算活动
		CartInnervVO innervVO = cartFacade.getFavorCaculateResultDTOBySelected(userId, provinceId, filterList, 0,
				false, PlatformType.PC);
		FavorCaculateResultDTO resultDTO = innervVO.getFavorCaculateResultDTO();
		Map<Long, List<PromotionSkuItemDTO>> poSkuMap = new HashMap<>();

		if (resultDTO == null) {
			jsonMap2.put("state", ActivationConstants.STATE_NOT_MATCH);
			jsonMap1.put("code", 200);
			jsonMap1.put("result", jsonMap2);
			return new ModelAndView();
		}
		// 从计算结果中获取sku列表
		poSkuMap.putAll(resultDTO.getNotSatisfySkuList());
		List<PromotionCartDTO> cartDTOs = resultDTO.getActivations();
		if (!CollectionUtils.isEmpty(cartDTOs)) {
			for (PromotionCartDTO dto : cartDTOs) {
				poSkuMap.putAll(dto.getPoSkuMap());
			}
		}

		FavorCaculateResultDTO favorCaculateResultDTO = calCenterFacade.bindCoupon(poSkuMap, paramDTO);
		jsonMap2.put("state", favorCaculateResultDTO.getCouponState());
		if (favorCaculateResultDTO.getCouponState() == ActivationConstants.STATE_CAN_USE
				&& favorCaculateResultDTO.getCouponDTO() != null) {
			jsonMap2.put("userCouponId", favorCaculateResultDTO.getCouponDTO().getUserCouponId());
		}

		jsonMap1.put("code", 200);
		jsonMap1.put("result", jsonMap2);
		model.addAllAttributes(jsonMap1);

		return new ModelAndView();
	}

	/**
	 * 内存组单
	 * 
	 * @param param
	 * @return RetArg.OrderFormCalDTO<br>
	 *         RetArg.OrderForm1VO<br>
	 *         RetArg.FavorCaculateResultDTO<br>
	 *         RetArg.Integer[0]:
	 *         组单的结果Code(0:失败,1:成功,2:库存不足,3:收货地址为空,4:购买明细为空或者不满足起批条件,5:输入参数不对)<br>
	 *         RetArg.Boolean[0]: 购物车产品个数不对
	 */
	private RetArg genOrderForm1VO(OrderFacadeComposeOrderParam param) {
		// 1.组单
		RetArg retArgOfComposeOrder = orderFacade.composeOrder(param, PlatformType.PC);
		OrderCalServiceGenOrderResult genOrderResult = RetArgUtil.get(retArgOfComposeOrder,
				OrderCalServiceGenOrderResult.class);
		FavorCaculateResultDTO fcResultDTO = RetArgUtil.get(retArgOfComposeOrder, FavorCaculateResultDTO.class);
		// 2.生成VO对象
		OrderFormCalDTO orderCalDTO = genOrderResult.getOrderFormCalDTO();
		OrderForm1VO order1VO = MainsiteVOConvertUtil.convertToOrderForm1VO(orderCalDTO, fcResultDTO);

		if (fcResultDTO != null && CollectionUtil.isNotEmptyOfList(fcResultDTO.getSkuList())) {
			Map<Long, BigDecimal> skuPriceMap = new HashMap<Long, BigDecimal>();
			if (StringUtils.isNotEmpty(param.getSkusPrice())) {
				String[] skuArray = param.getSkusPrice().split(",");
				for (String str : skuArray) {
					String[] skuPrice = StringUtils.split(str, "|");
					skuPriceMap.put(Long.parseLong(skuPrice[0]), new BigDecimal(skuPrice[1]));
				}
			}

			Set<Long> businessIdSet = new HashSet<Long>();
			Long userId = 0l;
			// 3.验证是否特许经营
			for (PromotionSkuItemDTO promotionSkuItemDTO : fcResultDTO.getSkuList()) {
				if (userId == 0) {
					userId = promotionSkuItemDTO.getUserId();
				}
				businessIdSet.add(promotionSkuItemDTO.getBusinessId());
				// 购物车价格不等于db里商品价格时，即买家下单时商家改了价格
				if (skuPriceMap.get(promotionSkuItemDTO.getSkuId()) != null
						&& skuPriceMap.get(promotionSkuItemDTO.getSkuId()).compareTo(
								promotionSkuItemDTO.getOriRetailPrice()) != 0) {
					genOrderResult.setResultCode(ComposeOrderErrorCodeEnum.ORDER_SKUPRICE_CHANGED);
				}
			}
			int result = businessService.isPermitOrder(new ArrayList<Long>(businessIdSet), userId, getProviceId());
			if (result == -1) {
				genOrderResult.setResultCode(ComposeOrderErrorCodeEnum.ORDER_USER_NOTALLOW);
			} else if (result == -2) {
				genOrderResult.setResultCode(ComposeOrderErrorCodeEnum.ORDER_AREA_NOTSUPPORT);
			}
			;

		}

		// 3.生成返回参数
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, genOrderResult.getResultCode());
		if (genOrderResult != null && !genOrderResult.getResultCode().equals(ComposeOrderErrorCodeEnum.ORDER_SUCCESS)) {
			return retArg;
		}

		RetArgUtil.put(retArg, orderCalDTO);
		RetArgUtil.put(retArg, order1VO);
		RetArgUtil.put(retArg, fcResultDTO);
		return retArg;
	}

	/**
	 * 货到付款订单,提交成功页面
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/codsucc")
	public ModelAndView codsucc(@RequestParam(value = "orderId") long orderId) {
		long userId = getUserId();
		Boolean isVisible = null;
		OrderFormDTO orderDTO = orderFacade.queryOrderForm(userId, orderId, isVisible);
		List<OrderForm2VO> order2VOList = new ArrayList<OrderForm2VO>();
		OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(orderDTO, returnPackageFacade);
		order2VOList.add(order2VO);

		Map<String, Object> map = handleHB(userId, orderId);

		Map<String, Object> modelMap = new HashMap<>();
		modelMap.put("order", order2VOList);
		modelMap.put("hasHB", map.get("hasHB"));
		modelMap.put("hbValue", map.get("hbValue"));
		ModelAndView mv = new ModelAndView("pages/order/codsucc");
		mv.addAllObjects(modelMap);
		return mv;
	}

	public ModelAndView codsuccByParentId(long parentId) {
		long userId = getUserId();
		Boolean isVisible = null;
		List<OrderFormDTO> orderDTOList = orderFacade.queryOrderFormList(userId, parentId, isVisible);
		List<OrderForm2VO> order2VOList = new ArrayList<OrderForm2VO>();
		BigDecimal total = new BigDecimal(0);
		for (OrderFormDTO orderDTO : orderDTOList) {
			OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(orderDTO, returnPackageFacade);
			order2VOList.add(order2VO);
			total = total.add(order2VO.getRealCash());
		}
		// Map<String, Object> map = handleHB(userId, parentId);
		Map<String, Object> modelMap = new HashMap<>();
		// modelMap.put("order", order2VOList);
		// modelMap.put("hasHB", map.get("hasHB"));
		// modelMap.put("hbValue", map.get("hbValue"));
		modelMap.put("expInfo", order2VOList == null || order2VOList.isEmpty() ? null : order2VOList.get(0)
				.getExpInfo());
		modelMap.put("total", total);
		// System.out.println( JSON.toJSONString(modelMap));
		ModelAndView mv = new ModelAndView("pages/order/codsucc");
		mv.addAllObjects(modelMap);
		return mv;
	}

	/**
	 * 在线支付下单成功页面
	 * 
	 * @param orderId值传进来的是parentId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/ordersucc")
	public ModelAndView ordersucc(@RequestParam(value = "orderId") long orderId, HttpServletResponse response)
			throws IOException {
		ModelAndView gotoPay = new ModelAndView("pages/purchase/gotoPay");
		List<OrderFormBriefDTO> orderBDTOList = orderFacade.queryOrderFormBriefDTOList(getUserId(), orderId, true);
		List<TradeItem> tradeListOfOnlineAndUnpay = null;
		if (orderBDTOList != null) {
			List<TradeItemDTO> tradeDTOList = tradeFacade.getTradeItemDTOListByParentId(orderId, getUserId());
			tradeListOfOnlineAndUnpay = TradeApiUtil.getTradeListOfOnlineAndUnpay(tradeDTOList);
		}
		// 支付成功跳到我的订单页
		if (CollectionUtil.isEmptyOfList(tradeListOfOnlineAndUnpay)) {
			response.sendRedirect("/myorder/");
			return null;
		}
		BigDecimal totalCash = BigDecimal.ZERO;
		for (TradeItem tradeItem : tradeListOfOnlineAndUnpay) {
			totalCash = totalCash.add(tradeItem.getCash());
		}
		gotoPay.addObject("grossPrice", totalCash.toString());
		gotoPay.addObject("payOrderParam", payFacade.buildPayOrderParam(orderBDTOList));
		return gotoPay;
	}

	/**
	 * 下单失败页面
	 * 
	 * @param errorType
	 *            0: 库存不足<br>
	 *            1: 优惠过期<br>
	 *            2: 订单不存在<br>
	 *            3: 其他未知原因<br>
	 * @return
	 */
	@RequestMapping(value = "/orderfail")
	public ModelAndView orderfail(@RequestParam(value = "errorType") int errorType) {
		ModelAndView mv = new ModelAndView("pages/order/orderfail");
		mv.addObject("errorType", errorType);
		return mv;
	}

	/**
	 * 支付失败+支付超时页面
	 * 
	 * @param orderId
	 * @param failType
	 *            0: 支付失败<br>
	 *            1: 支付超时<br>
	 *            2: 支付失败-订单已取消<br>
	 *            3: 支付失败-订单未能完成支付
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/payfail")
	public ModelAndView payfail(@RequestParam(value = "orderId") long parentId,
			@RequestParam(value = "failType") int failType,
			@RequestParam(value = "idType", defaultValue = "order") String idType) {
		long userId = getUserId();
		Boolean isVisible = null;
		// if ("trade".equalsIgnoreCase(idType)) {
		// failType = 3;
		// TradeItemDTO dto = tradeFacade.getTradeItemDTO(parentId, userId);
		// if (dto != null) {
		// // orderId = dto.getOrderId();
		// }
		// }
		List<OrderFormDTO> orderDTOList = orderFacade.queryOrderFormList(userId, parentId, isVisible);

		List<OrderForm2VO> order2VOList = new ArrayList<OrderForm2VO>();
		for (OrderFormDTO orderDTO : orderDTOList) {
			OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(orderDTO, returnPackageFacade);
			order2VOList.add(order2VO);
		}

		Map<String, Object> modelMap = new LinkedHashMap<>();
		modelMap.put("order", order2VOList);
		modelMap.put("failType", failType);
		ModelAndView mv = new ModelAndView("pages/order/payfail");
		mv.addAllObjects(modelMap);
		return mv;
	}

	/**
	 * 支付成功页面
	 * 
	 * @param parentId
	 * @param type
	 *            0: 支付成功<br>
	 *            1: 订单已支付<br>
	 *            2: 支付失败-订单已支付
	 * @return
	 * @throws IOException
	 */
	@PaySuccessActivity
	@RequestMapping(value = "/paysucc")
	public ModelAndView paysucc(@RequestParam(value = "tradeSerialId") long parentId,
			@RequestParam(value = "type", defaultValue = "0") int type) throws IOException {
		Boolean isVisible = null;
		long userId = SecurityContextUtils.getUserId();
		List<OrderFormDTO> orderDTOList = orderFacade.queryOrderFormList(userId, parentId, isVisible);

		List<OrderForm2VO> order2VOList = new ArrayList<OrderForm2VO>();
		for (OrderFormDTO orderDTO : orderDTOList) {
			OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(orderDTO, returnPackageFacade);
			order2VOList.add(order2VO);
		}

		Map<String, Object> map = handleHB(userId, parentId);

		// 返回结果
		Map<String, Object> modelMap = new HashMap<>();
		modelMap.put("hasHB", map.get("hasHB"));
		modelMap.put("hbValue", map.get("hbValue"));
		modelMap.put("order", order2VOList);
		modelMap.put("type", type);
		ModelAndView mv = new ModelAndView("pages/order/paysucc");
		mv.addAllObjects(modelMap);
		return mv;
	}

	private Map<String, Object> handleHB(long userId, long orderId) {
		Map<String, Object> map = new HashMap<>();
		List<RedPacketOrder> redPacketOrders = redPacketOrderService.getRedPacketOrderList(userId, orderId);
		if (CollectionUtils.isEmpty(redPacketOrders)) {
			map.put("hasHB", false);
			map.put("hbValue", 0);

		}

		boolean isReturnHB = false;
		for (RedPacketOrder order : redPacketOrders) {
			if (isReturnHB) {
				break;
			}
			if (order.getRedPacketOrderType() == RedPacketOrderType.RETURN_RED_PACKET
					&& order.getRedPacketHandlerType() == ActivationHandlerType.GRANT) {
				RedPacket packet = redPacketService.getRedPacketById(Long.valueOf(order.getRedPacketId()));
				if (!packet.isShare()) {
					map.put("hbValue", packet.getCash());
					isReturnHB = true;
					break;
				}
			}
		}

		map.put("hasHB", isReturnHB);
		return map;
	}

	/**
	 * 修改订单为COD
	 * 
	 * @param model
	 * @param orderId
	 * @return
	 */
	@BILog(action = "click", type = "changePayment", clientType = "wap")
	@RequestMapping("toCod")
	public ModelAndView toCod(Model model, @ModelAttribute("orderId") long orderId) throws IOException {
		long userId = getUserId();
		// 1.修改支付方式为货到付款
		OrderFormBriefDTO orderBDTO = orderFacade.queryOrderFormBriefDTO(userId, orderId, null);
		if (orderBDTO == null)
			return orderfail(2);

		int retCode = orderFacade.changePaymethodToCOD(userId, orderId);
		boolean isSucc = retCode > 0;

		// 设置BI参数
		ChangePaymentParam changePaymentParam = new ChangePaymentParam();
		changePaymentParam.setChangeFrom(OrderFormPayMethod.COD.getIntValue());
		changePaymentParam.setChangeTo(orderBDTO.getOrderFormPayMethod().getIntValue());
		changePaymentParam.setOrderId(orderId);
		model.addAttribute("changePaymentParam", JsonUtils.toJson(changePaymentParam));

		// 2.返回结果页
		if (isSucc) {
			return codsucc(orderId);
		} else {
			// 额外处理订单已取消的情况
			if (retCode == -3)
				return payfail(orderId, 2, "order");
			// 额外处理订单已支付的情况
			if (retCode == -5) {
				List<TradeItemDTO> tradeDTOList = tradeFacade.getTradeItemDTOList(orderId, userId);
				TradeItemDTO tradeDTOOfOnlineAndPayed = (TradeItemDTO) TradeApiUtil
						.getTradeOfOnlineAndPayed(tradeDTOList);
				if (tradeDTOOfOnlineAndPayed != null)
					return paysucc(tradeDTOOfOnlineAndPayed.getTradeId(), 1);
			}
			// 默认返回支付失败
			return payfail(orderId, 0, "order");
		}
	}

	/**
	 * 重新支付
	 * 
	 * @param orderId
	 * @param orderFormPayMethodInt
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@BILog(action = "click", type = "toPay", clientType = "wap")
	@RequestMapping("repay")
	public ModelAndView repay(@ModelAttribute("orderId") long orderId,
			@RequestParam(value = "pm", required = false, defaultValue = "-1") int orderFormPayMethodInt,
			HttpServletResponse response) throws IOException {
		boolean isRepay = true;
		OrderFormPayMethod orderFormPayMethod = OrderFormPayMethod.genEnumByIntValueSt(orderFormPayMethodInt);
		return goToPay(isRepay, orderId, orderFormPayMethod != null ? orderFormPayMethod.getIntValue() : null, response);
	}

	/**
	 * 首次支付
	 * 
	 * @param orderId
	 * @param response
	 * @return
	 */
	@BILog(action = "click", type = "toPay", clientType = "wap")
	@RequestMapping("mypay")
	public ModelAndView firstPay(@ModelAttribute("orderId") long orderId, HttpServletResponse response)
			throws IOException {
		boolean isRepay = false;
		return goToPayByParentId(isRepay, orderId, null, response);
	}

	/**
	 * 跳转到支付页面
	 * 
	 * @param isRepay
	 * @param orderId
	 * @param orderFormPayMethodInt
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private ModelAndView goToPay(boolean isRepay, long orderId, Integer orderFormPayMethodInt,
			HttpServletResponse response) throws IOException {
		long userId = getUserId();
		boolean isVisible = true;
		// 1.查询订单数据
		OrderFormBriefDTO orderBDTO = orderId <= 0 ? null : orderFacade.queryOrderFormBriefDTO(userId, orderId,
				isVisible);
		// 2.查询 已支付+未支付的现金交易
		TradeItemDTO tradeDTOOfOnlineAndUnpay = null, tradeDTOOfOnlineAndpayed = null;
		if (orderBDTO != null) {
			List<TradeItemDTO> tradeDTOList = tradeFacade.getTradeItemDTOList(orderId, userId);
			tradeDTOOfOnlineAndUnpay = (TradeItemDTO) TradeApiUtil.getTradeOfOnlineAndUnpay(tradeDTOList);
			tradeDTOOfOnlineAndpayed = (TradeItemDTO) TradeApiUtil.getTradeOfOnlineAndPayed(tradeDTOList);
			tradeDTOOfOnlineAndpayed = tradeDTOOfOnlineAndpayed == null ? (TradeItemDTO) TradeApiUtil
					.getTradeOfZeroPayed(tradeDTOList) : tradeDTOOfOnlineAndpayed;
		}
		return goToPay(isRepay, orderId, tradeDTOOfOnlineAndUnpay, tradeDTOOfOnlineAndpayed, orderBDTO,
				orderFormPayMethodInt, response);
	}

	private ModelAndView goToPayByParentId(boolean isRepay, long parentId, Integer orderFormPayMethodInt,
			HttpServletResponse response) throws IOException {
		long userId = getUserId();
		boolean isVisible = true;
		// 1.查询订单数据
		List<OrderFormBriefDTO> orderBDTOList = parentId <= 0 ? null : orderFacade.queryOrderFormBriefDTOList(userId,
				parentId, isVisible);
		// 2.查询 已支付+未支付的现金交易
		List<TradeItem> tradeListOfOnlineAndUnpay = null, tradeListOfOnlineAndpayed = null;
		if (orderBDTOList != null) {
			List<TradeItemDTO> tradeDTOList = tradeFacade.getTradeItemDTOListByParentId(parentId, userId);
			tradeListOfOnlineAndUnpay = TradeApiUtil.getTradeListOfOnlineAndUnpay(tradeDTOList);
			tradeListOfOnlineAndpayed = TradeApiUtil.getTradeListOfOnlineAndPayed(tradeDTOList);
			tradeListOfOnlineAndpayed = tradeListOfOnlineAndpayed == null ? TradeApiUtil
					.getTradeListOfZeroPayed(tradeDTOList) : tradeListOfOnlineAndpayed;
		}
		return goToPayByParentId(isRepay, parentId, tradeListOfOnlineAndUnpay, tradeListOfOnlineAndpayed,
				orderBDTOList, orderFormPayMethodInt, response);
	}

	/**
	 * 跳转到支付页面
	 * 
	 * @param isRepay
	 * @param orderId
	 * @param tradeDTOOfOnlineAndUnpay
	 * @param tradeDTOOfOnlineAndPayed
	 * @param orderBDTO
	 * @param orderFormPayMethodInt
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	private ModelAndView goToPay(boolean isRepay, long orderId, TradeItemDTO tradeDTOOfOnlineAndUnpay,
			TradeItemDTO tradeDTOOfOnlineAndPayed, OrderFormBriefDTO orderBDTO, Integer orderFormPayMethodInt,
			HttpServletResponse response) throws IOException {
		setNoCache(response);
		long currTime = System.currentTimeMillis();
		ModelAndView mv = super.genMV("purchase/pay.state");
		mv.addObject("serialCode", orderId);
		mv.addObject("state", "erro");

		// 1.订单和交易的数据有效性判断
		// CASE0: 订单提交失败
		if (orderId == -100L) {
			return orderfail(3);
		}
		// CASE0: OrderId参数不对
		else if (orderId <= 0) {
			response.sendRedirect("/myorder/");
			return null;
		}
		// CASE1: 读取不到有效的订单(订单参数不对)
		else if (orderBDTO == null) {
			return orderfail(2);
		}
		// CASE2： 订单取消的情况
		else if (orderBDTO.getOrderFormState() == OrderFormState.CANCEL_ED
				|| orderBDTO.getOrderFormState() == OrderFormState.CANCEL_ING) {
			return payfail(orderId, 2, "order");
		}
		// CASE3: 订单已付款(支付成功页or支付失败页)
		else if (orderBDTO.getOrderFormState() != OrderFormState.WAITING_PAY) {
			// CASE3-1: 非重新支付-跳转到支付成功页
			if (!isRepay) {
				if (orderBDTO.getOrderFormPayMethod() == OrderFormPayMethod.COD)
					return codsucc(orderId);
				else
					return paysucc(tradeDTOOfOnlineAndPayed.getTradeId(), 0);
			}
			// CASE3-2: 重新支付-跳转到支付失败页（订单已支付）
			else {
				return paysucc(tradeDTOOfOnlineAndPayed.getTradeId(), 2);
			}
		}
		// CASE3: 读取不到未付款的现金交易+支付超时
		else if (tradeDTOOfOnlineAndUnpay == null
				|| orderBDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME < currTime) {
			return payfail(orderId, 1, "order");
		}

		// 2.跳转到支付页面
		// CASE1: 跳转到网易宝页面
		if (TradeItemPayMethod.isOnlinePayMethod(tradeDTOOfOnlineAndUnpay.getTradeItemPayMethod())) {
			// long tradeSerialId = tradeDTOOfOnlineAndUnpay.getTradeId();
			// StringBuilder url = new StringBuilder(64);
			// url.append(payMainsite);
			// url.append("/pay/userpay?tradeSerialId=").append(tradeSerialId);
			// url.append("&sp=").append(tradeDTOOfOnlineAndUnpay.getSpSource().getIntValue());
			// if (orderFormPayMethodInt != null)
			// {
			// OrderFormPayMethod orderFormPayMethod = OrderFormPayMethod
			// .genEnumByIntValueSt(orderFormPayMethodInt);
			// TradeItemPayMethod tradeItemPayMethod = OrderApiUtil
			// .convertToTradeItemPayMethod(orderFormPayMethod);
			// if (TradeItemPayMethod.isOnlinePayMethod(tradeItemPayMethod))
			// url.append("&payMethod=").append(tradeItemPayMethod.getIntValue());
			// }
			// return new ModelAndView(new RedirectView(url.toString()));
			ModelAndView gotoPay = new ModelAndView("pages/purchase/gotoPay");
			gotoPay.addObject("grossPrice", tradeDTOOfOnlineAndUnpay.getCash().toString());
			return gotoPay;
		} else
			// CASE2: 其他情况
			return null;
	}

	/**
	 * 跳转到支付页面
	 * 
	 * @param isRepay
	 * @param parentId
	 * @param tradeDTOOfOnlineAndUnpay
	 * @param tradeDTOOfOnlineAndPayed
	 * @param orderBDTOList
	 * @param orderFormPayMethodInt
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private ModelAndView goToPayByParentId(boolean isRepay, long parentId, List<TradeItem> tradeListOfOnlineAndUnpay,
			List<TradeItem> tradeListOfOnlineAndPayed, List<OrderFormBriefDTO> orderBDTOList,
			Integer orderFormPayMethodInt, HttpServletResponse response) throws IOException {
		setNoCache(response);
		long currTime = System.currentTimeMillis();
		ModelAndView mv = super.genMV("purchase/pay.state");
		mv.addObject("serialCode", parentId);
		mv.addObject("state", "erro");

		// 1.订单和交易的数据有效性判断
		// CASE0.1: 库存不足
		if (parentId == -200L) {
			return orderfail(0);
		}
		// CASE0.2: 订单提交失败
		if (parentId == -100L) {
			return orderfail(3);
		}
		// CASE0: OrderId参数不对
		else if (parentId <= 0) {
			response.sendRedirect("/myorder/");
			return null;
		}
		// CASE1: 读取不到有效的订单(订单参数不对)
		else if (orderBDTOList == null) {
			return orderfail(2);
		}
		// CASE2： 订单取消的情况
		else if (orderBDTOList.get(0).getOrderFormState() == OrderFormState.CANCEL_ED
				|| orderBDTOList.get(0).getOrderFormState() == OrderFormState.CANCEL_ING) {
			return payfail(parentId, 2, "order");
		}
		// CASE3: 订单已付款(支付成功页or支付失败页)
		else if (orderBDTOList.get(0).getOrderFormState() != OrderFormState.WAITING_PAY) {
			// CASE3-1: 非重新支付-跳转到支付成功页
			if (!isRepay) {
				if (orderBDTOList.get(0).getOrderFormPayMethod() == OrderFormPayMethod.COD) {
					return codsuccByParentId(parentId);
				} else {
					return paysucc(tradeListOfOnlineAndPayed.get(0).getParentId(), 0);
				}
			}
			// CASE3-2: 重新支付-跳转到支付失败页（订单已支付）
			else {
				return paysucc(tradeListOfOnlineAndPayed.get(0).getParentId(), 2);
			}
		}
		// CASE3: 读取不到未付款的现金交易+支付超时
		else if (tradeListOfOnlineAndUnpay == null
				|| orderBDTOList.get(0).getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME < currTime) {
			return payfail(parentId, 1, "order");
		}

		// 2.跳转到支付页面
		if (TradeItemPayMethod.isOnlinePayMethod(tradeListOfOnlineAndUnpay.get(0).getTradeItemPayMethod())) {
			// long tradeSerialId = tradeDTOOfOnlineAndUnpay.getTradeId();
			// StringBuilder url = new StringBuilder(64);
			// url.append(payMainsite);
			// url.append("/pay/userpay?tradeSerialId=").append(tradeSerialId);
			// url.append("&sp=").append(tradeDTOOfOnlineAndUnpay.getSpSource().getIntValue());
			// if (orderFormPayMethodInt != null)
			// {
			// OrderFormPayMethod orderFormPayMethod = OrderFormPayMethod
			// .genEnumByIntValueSt(orderFormPayMethodInt);
			// TradeItemPayMethod tradeItemPayMethod = OrderApiUtil
			// .convertToTradeItemPayMethod(orderFormPayMethod);
			// if (TradeItemPayMethod.isOnlinePayMethod(tradeItemPayMethod))
			// url.append("&payMethod=").append(tradeItemPayMethod.getIntValue());
			// }
			// return new ModelAndView(new RedirectView(url.toString()));
			ModelAndView gotoPay = new ModelAndView("redirect:/purchase/ordersucc");
			// BigDecimal totalCash = BigDecimal.ZERO;
			// for (TradeItem tradeItem: tradeListOfOnlineAndUnpay) {
			// totalCash = totalCash.add(tradeItem.getCash());
			// }
			// gotoPay.addObject("grossPrice", totalCash.toString());
			// gotoPay.addObject("payOrderParam",payFacade.buildPayOrderParam(orderBDTOList));
			return gotoPay;
		} else
			// CASE2: 其他情况
			return null;
	}

	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public ModelAndView buy(Model model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("orderId") long orderId) throws IOException {
		// 3.跳转到支付页面
		PurchaseController thisProxy = ((PurchaseController) AopContext.currentProxy());
		return thisProxy.firstPay(orderId, response);
	}

	/**
	 * @return
	 */
	private long getUserId() {
		return SecurityContextUtils.getUserId();
	}

	/**
	 * 从环境变量，得到用户的省信息
	 * 
	 * @return
	 */
	private int getProviceId() {
		return AreaUtils.getProvinceCode();
	}

	private List<CartItemDTO> convertToValidCartItemDTO(String cartIds) {
		CartDTO cartDTO = cartFacade.getCart(getUserId(), getProviceId());
		if (CollectionUtil.isEmptyOfList(cartDTO.getCartItemList()) || StringUtils.isEmpty(cartIds)) {
			return null;
		}
		List<CartItemDTO> cartItemDTOs = cartDTO.getSkuOfValid();
		// List<Long>skuids = cartDTO.getSkuidOfValid();
		String[] cartIdArray = StringUtils.split(cartIds, ",");
		Set<Long> idSet = new HashSet<Long>();
		for (String cartId : cartIdArray) {
			idSet.add(Long.parseLong(cartId));
		}

		List<CartItemDTO> filerList = new ArrayList<CartItemDTO>();
		for (CartItemDTO cartItemDTO : cartItemDTOs) {
			if (idSet.contains(cartItemDTO.getSkuid())) {
				filerList.add(cartItemDTO);
			}
		}
		return filerList;
	}

}
