/*
 * @(#)PayController.java 2014-3-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.out.facade.PayFacade;
import com.xyl.mmall.common.param.PayNoticeParam;
import com.xyl.mmall.common.param.PayOrderParam;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.OrderFormSource;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.TradeItem;
import com.xyl.mmall.order.param.OrderServiceSetStateToEPayedParam;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.promotion.service.RebateService;

/**
 * PayController.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-3-28
 * @since 1.0
 */

@Controller
public class PayController extends BaseController {

	private static Logger logger = Logger.getLogger(PayController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private RebateService rebateService;
	
	@Autowired
	private OnlineActivityFacade onlineActivityFacade;
	
	@Autowired
	private OrderBriefService orderBriefService;
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private PayFacade payFacade;

    @Deprecated
	//@RequestMapping("/req/gentrade")
	public ModelAndView genTrade(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("pay/tradeitem");
		
		long tradeSerial = ServletRequestUtils.getLongParameter(request, "tradeSerialId", 0);
		if (tradeSerial <= 0) {
			view.addObject("error", "tradeSerial must not be null!");
			return view;
		}
		
		TradeItem item = tradeService.getTradeItemDTO(tradeSerial);
		if (item == null) {
			view.addObject("tradeItem", "");
			return view;
		}

		OrderForm form = orderService.queryOrderForm(item.getUserId(), item.getOrderId(), null);
		if (form == null) {
			view.addObject("tradeItem", "");
			return view;
		}

		UserProfileDTO profile = userProfileService.findUserProfileById(item.getUserId());

		String userName = profile.getUserName();

		Map<String, Object> object = new HashMap<>();
		object.put("cash", item.getCash().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		object.put("ctime", item.getCtime());
		object.put("orderId", item.getOrderId());
		object.put("payState", item.getPayState().getIntValue());
		object.put("spSource", item.getSpSource().getIntValue());
		object.put("tradeId", item.getTradeId());
		object.put("userId", item.getUserId());
		object.put("orderSource", form.getOrderFormSource().getIntValue());
		object.put("orderSn", item.getOrderSn());
		object.put("payOrderSn", item.getPayOrderSn());
		object.put("userName", userName);
		object.put("payMethod", item.getTradeItemPayMethod().getIntValue());
		if (logger.isDebugEnabled()) {
			logger.info(object.toString());
		}
		view.addObject("tradeItem", JsonUtils.toJson(object));

		return view;
	}
	
	/**
	 * 更新交易，支付和退款处理
	 * @param request
	 * @param response
	 * @return
	 */
    @Deprecated
	@SuppressWarnings("unchecked")
	//@RequestMapping("/req/updatetrade")
	public ModelAndView updateTradeItem(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("pay/succ.ret");

		String jsonData = StringUtils.trim(ServletRequestUtils.getStringParameter(request, "jsonData", "{}"));

		Map<String, Object> object = JsonUtils.fromJson(jsonData, Map.class);

		Map<String, Object> retObject = new HashMap<>();
		if (object == null) {
			retObject.put("succRet", false);
			retObject.put("status", -6);
			view.addObject("retObject", retObject.toString());
			return view;
		}

		BigDecimal cash = new BigDecimal(String.valueOf(object.get("cash"))).setScale(2, BigDecimal.ROUND_HALF_UP);
		// long orderId = Long.valueOf(String.valueOf(object.get("orderId")));
		long tradeId = Long.valueOf(String.valueOf(object.get("tradeId")));
		long userId = Long.valueOf(String.valueOf(object.get("userId")));
		int spSource = Integer.valueOf(String.valueOf(object.get("spSource")));
		int payState = Integer.valueOf(String.valueOf(object.get("payState")));
		String orderSn = String.valueOf(object.get("orderSn"));
		String payOrderSn = String.valueOf(object.get("payOrderSn"));
		
		//获取交易对象
		TradeItem tradeItem = tradeService.getTradeItemDTO(tradeId, userId);

		// 交易是否存在
		if (tradeItem == null) {
			retObject.put("succRet", false);
			retObject.put("status", -2);

			view.addObject("retObject", JsonUtils.toJson(retObject));
			return view;
		}
		
		if (tradeItem.getPayState() != PayState.ONLINE_NOT_PAY) {
			retObject.put("succRet", false);
			retObject.put("status", -3);

			view.addObject("retObject", JsonUtils.toJson(retObject));
			return view;
		}
		
		// 金额是否符合要求
		if (cash.compareTo(tradeItem.getCash()) > 0) {
			retObject.put("succRet", false);
			retObject.put("status", -4);

			view.addObject("retObject", JsonUtils.toJson(retObject));
			return view;
		}

		OrderForm orderForm = orderService.queryOrderForm(tradeItem.getUserId(), tradeItem.getOrderId(), null);
		// 主站订单才判断渠道是否一致
		if (orderForm.getOrderFormSource().getIntValue() == OrderFormSource.PC.getIntValue()) {
			if (spSource != tradeItem.getSpSource().getIntValue()) {
				retObject.put("succRet", false);
				retObject.put("status", -5);

				view.addObject("retObject", JsonUtils.toJson(retObject));
				return view;
			}
		}
		
		//如果订单已经支付，不需要重复更新
		if (orderForm.getPayState() == PayState.ONLINE_PAYED || orderForm.getPayState() == PayState.COD_PAYED) {
			retObject.put("succRet", true);
			retObject.put("status", 1);

			view.addObject("retObject", JsonUtils.toJson(retObject));
			return view;
		}
		
		// 支付状态是否为已经支付
		if (payState != PayState.ONLINE_PAYED.getIntValue()) {
			retObject.put("succRet", false);
			retObject.put("status", -6);

			view.addObject("retObject", JsonUtils.toJson(retObject));
			return view;
		}
		
		OrderServiceSetStateToEPayedParam param = new OrderServiceSetStateToEPayedParam();
		param.setOrderId(orderForm.getOrderId());
		param.setOrderSn(orderSn);
		param.setPayOrderSn(payOrderSn);
		param.setTradeId(tradeId);
		param.setUserId(userId);
		int ret = orderService.setStateToEPayed(param);
		
		if (ret < 1) {
			retObject.put("succRet", false);
			retObject.put("status", 0);
		} else {
			// 发送返券邮件+短信
			retObject.put("succRet", true);
			retObject.put("status", 1);
			//返券
			rebateService.rebate(userId, orderForm.getOrderId());
		}
		
		view.addObject("retObject", JsonUtils.toJson(retObject));
		doPaySuccessActivity(payState, cash, orderForm);
		return view;
	}
	
	
	/**
	 * 支付通知接口  为了支付平台
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/pay/notice")
	public void updateOrderStateByOrderParentId(PayNoticeParam payNoticeParam,HttpServletResponse response) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("o_order_no=").append(payNoticeParam.getO_order_no()).append("&");
		sb.append("result_code=").append(payNoticeParam.getResult_code()).append("&");
		sb.append("state_code=").append(payNoticeParam.getState_code()).append("&");
		sb.append("order_amount=").append(payNoticeParam.getOrder_amount()).append("&");
		sb.append("pay_amount=").append(payNoticeParam.getPay_amount()).append("&");
		sb.append("acquiring_time=").append(payNoticeParam.getAcquiring_time()).append("&");
		sb.append("complete_time=").append(payNoticeParam.getComplete_time()).append("&");
		sb.append("order_no=").append(payNoticeParam.getOrder_no()).append("&");
		sb.append("partner_id=").append(payNoticeParam.getPartner_id()).append("&");
		sb.append("remark=").append(payNoticeParam.getRemark()).append("&");
		sb.append("charset=").append(payNoticeParam.getCharset()).append("&");
		sb.append("sign_type=").append(payNoticeParam.getSign_type());
	
		logger.info("pay notice request:"+sb.toString());
		String parentIdStr =  payNoticeParam.getO_order_no();
		
		if(!payFacade.validSignMsg(sb.toString(),payNoticeParam.getSign_msg())){
			logger.error("valid sign msg failed:"+sb.toString());
			printResutlt(false, response,sb.toString());
			return;
		}
		Map<Long, Long> orderIdMap = new HashMap<Long, Long>();
		//获取交易对象
		List<TradeItemDTO> tradeItems = tradeService.getTradeItemDTOListByParentId(Long.parseLong(parentIdStr), -1);
		// 交易是否存在
		if (CollectionUtil.isEmptyOfList(tradeItems)) {
			logger.error("tradeItem is not exist:"+sb.toString());
			printResutlt(false, response,sb.toString());
			return;
		}
		boolean isPayed = true;
		long userId = 0l;
		for (TradeItemDTO tradeItemDTO : tradeItems) {
			userId = tradeItemDTO.getUserId();
			orderIdMap.put(tradeItemDTO.getOrderId(),tradeItemDTO.getTradeId());
			if (tradeItemDTO.getPayState().equals(PayState.ONLINE_NOT_PAY)||StringUtils.isEmpty(tradeItemDTO.getPayOrderSn())) {
				isPayed = false;
			}
		}
		if(isPayed){
			printResutlt(true, response,sb.toString());
			return;
		}
		OrderServiceSetStateToEPayedParam param = new OrderServiceSetStateToEPayedParam();
		param.setOrderIdMap(orderIdMap);
		//param.setOrderSn(orderSn);
		param.setPayOrderSn(payNoticeParam.getOrder_no());
		param.setUserId(userId);
		int result = orderService.setStateToEPayed(param);
		if(result==1){
			printResutlt(true, response,sb.toString());
			return;
		}
		logger.error("StateToEPayed failed By notice pay:"+sb.toString());
		printResutlt(false, response,sb.toString());
		return;
	}
	
	private void printResutlt(boolean isSuccess,HttpServletResponse response,String payNoticeParam){
		 PrintWriter writer;
		try {
			writer = response.getWriter();
			if(isSuccess){
				writer.print("success");
			}else{
				writer.print("failure");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("pay notice print error: "+e.getMessage()+"pay notice param:"+payNoticeParam);
		}
	}
	
	//支付回调地址
	@RequestMapping(value = "/pay/callback")
	public ModelAndView paysucc(HttpServletRequest request,HttpServletResponse response) throws IOException {
		ModelAndView failView = new ModelAndView("pages/order/payfail");
		Map<String,String> paramMap = new HashMap<String, String>();
		String signMsg = "";
		StringBuffer sb = new StringBuffer();
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()){
			String name = (String)paramNames.nextElement();
			String value = request.getParameter(name);
			if(!name.equals("sign_msg")){
				sb.append(name).append("=").append(value).append("&");
			}else{
				signMsg = value;
			}
			paramMap.put(name, value);
		}
		sb.deleteCharAt(sb.length()-1);
		//构建支付结果信息
		String parentIdStr =  paramMap.get("o_order_no");
		long parentId = StringUtils.isEmpty(parentIdStr)?0l:Long.parseLong(parentIdStr),userId = SecurityContextUtils.getUserId();
		List<OrderFormDTO> orderDTOList = orderFacade.queryOrderFormList(userId,  Long.parseLong(paramMap.get("o_order_no")), null);
        List<OrderForm2VO> order2VOList = new ArrayList<OrderForm2VO>();
        for(OrderFormDTO orderDTO : orderDTOList)
        {
            OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(orderDTO,
                    null);
            order2VOList.add(order2VO);
        }
		// 返回结果
		Map<String, Object> modelMap = new HashMap<>();
		modelMap.put("hasHB", false);
		modelMap.put("hbValue", 0);
		modelMap.put("order", order2VOList);
		modelMap.put("type", 0);
	    failView.addAllObjects(modelMap);
		
	    //记录支付平台回调
		logger.info("pay callback info:"+sb.toString()+"signMsg:"+signMsg);
		if(!payFacade.validSignMsg(sb.toString(),signMsg)){
			logger.error("pay valid sign error; callback info:"+sb.toString()+"signMsg:"+signMsg);
			return failView;
		}
		
		if(!paramMap.get("result_code").equals("0000")||!paramMap.get("state_code").equals("2")){
			logger.error("pay failed;callback info:"+sb.toString()+"signMsg:"+signMsg);
			return failView;
		}
		
		Map<Long, Long>tradeMap = new HashMap<Long, Long>();
		List<TradeItemDTO> tradeItemDTOs = tradeService.getTradeItemDTOListByParentId(parentId,userId);
		for (TradeItemDTO tradeItemDTO : tradeItemDTOs) {
			if(tradeItemDTO.getPayState().equals(PayState.ONLINE_NOT_PAY)||StringUtils.isEmpty(tradeItemDTO.getPayOrderSn())){
				tradeMap.put(tradeItemDTO.getOrderId(), tradeItemDTO.getTradeId());
			}
		}
		if (tradeMap.size()>0) {
			OrderServiceSetStateToEPayedParam param = new OrderServiceSetStateToEPayedParam();
				param.setOrderIdMap(tradeMap);
			//param.setOrderSn(orderSn);
			param.setPayOrderSn(paramMap.get("order_no"));
			param.setUserId(userId);
			int result = orderService.setStateToEPayed(param);
			if(result<=0){
				//支付成功更新订单状态失败时
				logger.error("pay success set form state error; callback info:"+sb.toString()+"signMsg:"+signMsg);
				return failView;
			}
		}
		ModelAndView mv = new ModelAndView("pages/order/paysucc");
		mv.addAllObjects(modelMap);
		return mv;
	}
	
	
	/**
	 * 组装请求支付接口参数
	 * @param payConfirmParam
	 * @return
	 */
	@RequestMapping(value = "/pay/getPayRequestParam", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getPayRequestParam(@RequestParam(value="parentId") long parentId){
		BaseJsonVO baseJsonVO = new BaseJsonVO(); 
		
		long userId = SecurityContextUtils.getUserId();
	        // 1.查询订单数据
	    List<OrderFormBriefDTO> orderBDTOList = parentId <= 0 ? null : orderFacade
	                .queryOrderFormBriefDTOList(userId, parentId, true);
	        
	    PayOrderParam payOrderParam = payFacade.buildPayOrderParam(orderBDTOList);
		if(payOrderParam!=null){
			baseJsonVO.setCode(200);
			baseJsonVO.setResult(payOrderParam);
		}else{
			baseJsonVO.setCode(201);
			baseJsonVO.setMessage("组装请求支付参数失败!");
		}
		return baseJsonVO;
	}
	

	/**
	 * 处理上线活动中支付成功的逻辑。
	 * 
	 * @param orderForm
	 * @param cash
	 * @param payState
	 * 
	 */
    @Deprecated
	private void doPaySuccessActivity(int payState, BigDecimal cash, OrderForm orderForm) {
		long currentTime = System.currentTimeMillis();
		if (currentTime > OnlineActivityConstants.ACTIVITY_END_TIME
				|| currentTime < OnlineActivityConstants.ACTIVITY_START_TIME) {
			return;
		}
		if (payState == PayState.ONLINE_PAYED.getIntValue()) {
			// 给用户获取一次抽奖机会
			onlineActivityFacade.giveUserOneLettory(orderForm.getUserId());
			if (cash.compareTo(OnlineActivityConstants.SUIT_CASE_GIFT_ORDER_AMOUNT) >= 0) {
				//给用户一个旅行箱
				onlineActivityFacade.giveUserOneSuitcase(orderForm.getUserId(), orderForm.getOrderId());
			}
		}
	}

}
