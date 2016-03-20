/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.logger.CommonLogger;
import com.xyl.mmall.mobile.facade.MobileCartFacade;
import com.xyl.mmall.mobile.facade.MobileOrderFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;
import com.xyl.mmall.mobile.facade.param.MobileOrderCommitAO;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.service.MobileHeaderProcess;

/**
 * @author lihui
 *
 */
@RestController
@RequestMapping("/m")
public class MobileTradeController {
	@Autowired
	private CommonLogger logger;

	@Resource(name="mCartFacade")
	private MobileCartFacade mCartFacade;

	@Autowired
	private MobileOrderFacade mobileOrderFacade;

	private static final String DEFAULT_CANCEL_ORDER_REASON = " ";

	// 商品详情增加
	@RequestMapping(value = "/addCart", method = RequestMethod.GET)
	public BaseJsonVO addCart(HttpServletRequest request, @RequestParam(value = "skuId", required = false) Long skuId,
			@RequestParam(value = "count", required = false) Integer count) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("skuId", skuId);
		logger.logger(ao, "click", "addToCart", userId, map);
		return mCartFacade.addToCart(userId, skuId, areaCode, count, 0);
	}

	@RequestMapping(value = "/cartRebuy", method = RequestMethod.GET)
	public BaseJsonVO cartRebuy(HttpServletRequest request,
			@RequestParam(value = "skuId", required = false) Long skuId,
			@RequestParam(value = "count", required = false) Integer count) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		return mCartFacade.addToCart(userId, skuId, areaCode, count, 1);
	}

	// 购物车结算
	@RequestMapping(value = "/cartConfirm", method = RequestMethod.GET)
	public BaseJsonVO cartConfirm(HttpServletRequest request,
			@RequestParam(value = "hash", required = false) String hash,@RequestParam(value = "useGiftMoney", required = false) Integer useGiftMoney) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		int os = -1;
		if(useGiftMoney == null)
			useGiftMoney = 1;
		BaseJsonVO vo = mobileOrderFacade.genOrder(userId, areaCode, os,Converter.protocolVersion(ao.getProtocolVersion()),useGiftMoney);
		logger.insertResult(vo, new String[] { "skuId" }, new String[] { "skuList" }, ao, "page", "orderConfirmPage",
				userId);
		logger.insertResult(vo, new String[] { "orderId" }, new String[] { "orderId", "cartOriRPrice","cartRPrice" }, ao, "orderOP", "orderSubmit",
				userId);
		return vo;
	}

	// 购物车信息
	@RequestMapping(value = "/cartStatus", method = RequestMethod.GET)
	public BaseJsonVO cartStatus(HttpServletRequest request) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		return mCartFacade.getCartInfo(userId, areaCode);
	}

	// 删除sku
	@RequestMapping(value = "/deleteCartSku", method = RequestMethod.GET)
	public BaseJsonVO deleteOrder(HttpServletRequest request,
			@RequestParam(value = "skuId", required = false) List<Long> skuId) {
		Long userId = SecurityContextUtils.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaId = MobileHeaderProcess.getAreaCode(ao);
		return mCartFacade.deleteInCartPage(userId, areaId, skuId);
	}

	@RequestMapping(value = "/getOrderPrice", method = RequestMethod.GET)
	public BaseJsonVO getOrderPrice(HttpServletRequest request, MobileOrderCommitAO pager) {
		Long userId = MobileHeaderProcess.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		int os = MobileHeaderProcess.getOS(ao);
		return mobileOrderFacade.genOrderChange(userId, areaCode, os, pager,Converter.protocolVersion(ao.getProtocolVersion()));
	}

	@RequestMapping(value = "/cartDetail", method = RequestMethod.GET)
	public BaseJsonVO cartDetail(HttpServletRequest request) {
		Long userId = SecurityContextUtils.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		logger.logger(ao, "page", "cartPage", userId);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		return mCartFacade.getCartDetail(userId, areaCode);
	}

	// 添加删除
	@RequestMapping(value = "/updateCartSkuAmount", method = RequestMethod.GET)
	public BaseJsonVO updateCartSkuAmount(HttpServletRequest request,
			@RequestParam(value = "skuId", required = false) Long skuId,
			@RequestParam(value = "add", required = false) Integer add,
			@RequestParam(value = "del", required = false) Integer del) {
		Long userId = SecurityContextUtils.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		return mCartFacade.updateInCartPage(userId, areaCode, skuId, add, del);
	}

	@RequestMapping(value = "/postOrder", method = RequestMethod.POST)
	public BaseJsonVO postOrder(HttpServletRequest request, MobileOrderCommitAO pager) {
		Long userId = SecurityContextUtils.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		int os = MobileHeaderProcess.getOS(ao);
		String token = MobileHeaderProcess.getToken(request);
		BaseJsonVO vo = mobileOrderFacade.postOrder(userId, areaCode, os, pager,token,Converter.protocolVersion(ao.getProtocolVersion()));
		logger.insertResult(vo, new String[] { "orderId", "skuId" }, new String[] { "orderId", "skuList" }, ao, "page",
				"orderSubmitPage", userId);
		return vo;
	}

	@RequestMapping(value = "/getOrderList", method = RequestMethod.GET)
	public BaseJsonVO getOrderList(MobilePageCommonAO ao, @RequestParam(value = "type", required = false) Integer type) {
		Long userId = SecurityContextUtils.getUserId();
		return mobileOrderFacade.getOrderList(userId, type, ao);
	}

	@RequestMapping(value = "/getOrderDetail", method = RequestMethod.GET)
	public BaseJsonVO getOrderDetail(HttpServletRequest request,
			@RequestParam(value = "orderId", required = false) Long orderId) {
		Long userId = SecurityContextUtils.getUserId();
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		BaseJsonVO vo = mobileOrderFacade.getOrderDetail(userId, orderId,Converter.protocolVersion(ao.getProtocolVersion()));
		logger.insertResult(vo, new String[] { "status" }, new String[] { "status" }, ao, "page", "orderDetailPage",
				userId);
		return vo;
	}

	@RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
	public BaseJsonVO cancelOrder(HttpServletRequest request,@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "rType", required = false) int type,
			@RequestParam(value = "reason", required = false) String reason) {
		Long userId = SecurityContextUtils.getUserId();
		if (reason == null || reason.isEmpty()) {
			reason = DEFAULT_CANCEL_ORDER_REASON;
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		logger.logger(ao, "click", "cancelOrder", userId, map);

		return mobileOrderFacade.cancelOrder(userId, orderId, type, reason);
	}

	@RequestMapping(value = "/deleteOrder", method = RequestMethod.GET)
	public BaseJsonVO deleteOrder(@RequestParam(value = "orderId", required = false) Long orderId) {
		Long userId = SecurityContextUtils.getUserId();
		return mobileOrderFacade.deleteOrder(userId, orderId);
	}

	@RequestMapping(value = "/getUnreadCount", method = RequestMethod.GET)
	public BaseJsonVO getUnreadCount(@RequestParam(value = "lastReadUnPay", required = false) Long lastReadUnPay,
			@RequestParam(value = "lastReadUnSend", required = false) Long lastReadUnSend,
			@RequestParam(value = "lastReadSend", required = false) Long lastReadSend) {
		Long userId = SecurityContextUtils.getUserId();
		return mobileOrderFacade.unReadOrder(userId);
	}

	/**
	 * 
	 * 0---------------------------
	 */

	@RequestMapping(value = "/changeOrderPayChannel", method = RequestMethod.GET)
	public BaseJsonVO changeOrderPayChannel(HttpServletRequest request,@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "payChannel", required = false) Integer payChannel) {
		Long userId = SecurityContextUtils.getUserId();
		String token = MobileHeaderProcess.getToken(request);
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		BaseJsonVO vo = mobileOrderFacade.changePay(userId, payChannel, orderId,token,Converter.protocolVersion(ao.getProtocolVersion()));
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("changeFrom", payChannel == 1?0:1);
		map.put("changeTo", payChannel);
		logger.logger(ao, "click", "changePayment", userId, map);
		return vo;
	}
}
