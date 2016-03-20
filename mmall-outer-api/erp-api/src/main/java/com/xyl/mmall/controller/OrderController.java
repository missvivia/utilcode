/**
 * ==================================================================
 * Copyright (c) xinyunlian Co.ltd Hangzhou, 2014-2015
 * 
 * 浙大网新新云联技术有限公司拥有该文件的使用、复制、修改和分发的许可权
 * 如果你想得到更多信息，请访问 <http://www.xinyunlian.com>
 *
 * xinyunlian Co.ltd Hangzhou owns permission to use, copy, modify and
 * distribute this documentation.
 * For more information, please see <http://www.xinyunlian.com>
 * ==================================================================
 */

package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.erp.facade.ERPOrderFacade;
import com.xyl.mmall.erp.vo.OrderDetailInfoErpVO;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.utils.ERPAccountUtils;

/**
 * ==================================================================
 * OrderController.java created by Harold Deane at Jul 14, 2015 9:28:37 AM
 * 这里对类或者接口作简要描述
 * 
 * @author durianskh@gmail.com
 * @version 1.0
 * ==================================================================
 */
@RestController
public class OrderController {
	private static Logger logger = Logger.getLogger(OrderController.class);

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private ERPOrderFacade erpOrderFacade;

//	@RequestMapping(value = "/getOrdersByParentId", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getOrdersByParentId(
			@RequestParam(value = "parentId", required = true) long parentId,
			@RequestParam(value = "count", required = true) int count) {
		if (count > 100) {
			count = 100;
		}

		JSONObject json = new JSONObject();
		List<JSONObject> parentOrders = new ArrayList<JSONObject>();

		List<Long> parentIds = orderFacade.getParentIds(parentId, count);
		for (Long pId : parentIds) {
			List<OrderFormDTO> subOrders = orderFacade.queryOrderFormListByParentId(pId, true);
			if (subOrders.size() > 0) {
				JSONObject parentOrder = new JSONObject();
				parentOrder.put("parentId", pId);
				parentOrder.put("subOrders", subOrders);
				parentOrders.add(parentOrder);
			} else {
				parentOrders.clear();
				break;
			}
		}

		json.put("total", parentIds.size());
		json.put("list", parentOrders);

		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(json);

		return ret;
	}

	/**
	 * 根据更新时间获取订单id
	 * @param timetag
	 * @param state
	 * @param appid
	 * @return
	 */
	@RequestMapping(value = "/order/getOrderByUpdateTime", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getOrderByUpdateTime(
			@RequestParam(value = "startTime", required = false, defaultValue = "0") long startTime,
			@RequestParam(value = "endTime", required = false, defaultValue = "0") long endTime,
			@RequestParam(value = "state", required = false, defaultValue = "-1") int state,
			@RequestParam(value = "appid") String appid) {
		BaseJsonVO ret = new BaseJsonVO();
		String businessIds = ERPAccountUtils.getERPAccountBusinessIdsByAppId(appid);
		if (StringUtils.isBlank(businessIds)) {
			logger.info("No businessId bind to appid! AppId : " + appid + ".");
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "No businessId bind to appid!");
			return ret;
		}
		JSONObject json = new JSONObject(2);
		long now = System.currentTimeMillis();
		json.put("list", erpOrderFacade.queryOrderFacade(businessIds, startTime, endTime, state));
		json.put("timetag", now);
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(json);
		if (logger.isDebugEnabled()) {
			logger.debug("Get orderIds from erp! AppId : " + appid + ", startTime : " + startTime
					+ ", endTime : " + endTime + ", state : " + state + ".");
		}
		return ret;
	}
	
	/**
	 * 根据orderid获取订单详情
	 * @param orderId
	 * @param appid
	 * @return
	 */
	@RequestMapping(value = "/order/getOrderDetailByOrderId",  method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getOrderDetailByOrderId(
			@RequestParam(value = "orderId", required = true) long orderId, @RequestParam(value = "appid") String appid) {
		BaseJsonVO ret = new BaseJsonVO();
		String businessIds = ERPAccountUtils.getERPAccountBusinessIdsByAppId(appid);
		if (StringUtils.isBlank(businessIds)) {
			logger.info("No businessId bind to appid! AppId : " + appid + ".");
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "No businessId bind to appid!");
			return ret;
		}
		OrderDetailInfoErpVO vo = erpOrderFacade.queryOrderDetailInfoByOrderId(orderId);
		if (vo == null || vo.getBasicInfo() == null) {
			ret.setResult(null);
		} else {
			List<String> bindIds = Arrays.asList(businessIds.split(","));
			if (bindIds.contains(String.valueOf(vo.getBasicInfo().getBusinessId()))) {
				ret.setResult(vo);
			} else {
				ret.setResult(null);
			}
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		if (logger.isDebugEnabled()) {
			logger.debug("Get orderDetail from erp! AppId : " + appid + ", orderId : " + orderId + ".");
		}
		return ret;
	}

	/**
	 * 更新订单状态
	 * @param orderId
	 * @param order
	 * @param appid
	 * @return
	 */
	@BILog(action = "click", type = "skuSellStatisticsERP", clientType = "order")
	@RequestMapping(value = "/order/updateOrderStateByOrderId", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO updateOrderStateByOrderId(
			@RequestParam(value = "orderId", required = true) long orderId,
			@RequestParam(value = "order", required = true) String order, 
			@RequestParam(value = "appid") String appid) {
		BaseJsonVO ret = new BaseJsonVO();
		if (orderId < 1l || StringUtils.isBlank(order)) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "Bad request!");
			return ret;
		}
		String businessIds = ERPAccountUtils.getERPAccountBusinessIdsByAppId(appid);
		if (StringUtils.isBlank(businessIds)) {
			logger.info("No businessId bind to appid! AppId : " + appid + ".");
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "No businessId bind to appid!");
			return ret;
		}
		JSONObject orderJson = null;
		try {
			orderJson = JSONObject.parseObject(order);
		} catch (Exception e) {
			logger.error("Json error! order : " + order);
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "Bad resquest! Param [order] to json error!");
			return ret;
		}
		int orderFromState = orderJson.getIntValue("orderFormState");
		// OrderFromState can be change to CANCEL_ED, ALL_DELIVE or WAITING_DELIVE only.
		if (orderFromState != OrderFormState.ALL_DELIVE.getIntValue()
				&& orderFromState != OrderFormState.FINISH_TRADE.getIntValue()
				&& orderFromState != OrderFormState.CANCEL_ED.getIntValue()) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "Bad request!");
			return ret;
		}
		long userId = orderJson.getLongValue("userId");
		if (userId < 1l) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "Bad request!");
			return ret;
		}
		OrderFormState orderState = OrderFormState.genEnumByIntValueSt(orderFromState);
		OrderFormDTO orderForm = erpOrderFacade.getOrderFormByOrderId(orderId, userId);
		if (orderForm == null) {
			ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "Order doesn't exist!");
			return ret;
		} else {
			List<String> bindIds = Arrays.asList(businessIds.split(","));
			// businessId is binded.
			if (!bindIds.contains(String.valueOf(orderForm.getBusinessId()))) {
				ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "Order doesn't exist!");
				return ret;
			}
			// order state is equal.
			if (orderForm.getOrderFormState() == orderState) {
				logger.info("Orderstate is equal with modified, return ok! OrderId : " + orderId 
						+ ", AppId : " + appid + ", OrderStatus : " + orderState.getIntValue() + ".");
				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "success!");
				return ret;
			}
		}
		int res = 0;
		if (orderFromState == OrderFormState.CANCEL_ED.getIntValue()) {
			String comment = orderJson.getString("comment");
			List<OrderCancelInfoDTO> list = new ArrayList<OrderCancelInfoDTO>(1);
			OrderCancelInfoDTO cancelDTO = new OrderCancelInfoDTO();
			cancelDTO.setCancelSource(OrderCancelSource.ERP);
			cancelDTO.setOrderId(orderId);
			cancelDTO.setReason(comment == null ? "erp cancel!" : comment);
			cancelDTO.setRtype(OrderCancelRType.genEnumByIntValueSt(0));
			cancelDTO.setUserId(userId);
			cancelDTO.setOperateUserType(OperateUserType.ERP);
			cancelDTO.setBusinessId(orderForm.getBusinessId());
			list.add(cancelDTO);
			RetArg retArg = orderFacade.cancelOrders(list);
			// 1.调用取消订单的服务
			// 2.读取调用结果
			Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
			res = isSucc ? 1 : 0;
		} else {
			OrderOperateParam param = new OrderOperateParam();
			param.setOrderId(orderId);
			param.setOperateUserType(OperateUserType.ERP);
			param.setNewState(orderState);
			param.setUserId(userId);
			res = erpOrderFacade.modifyOrderState(param);
		}
		if (res > 0) {
			logger.info("Updating orderState succeeded from erp! OrderId : " + orderId + ", AppId : " + appid
					+ ", OrderStatus : " + orderState.getIntValue() + ".");
			ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "success!");
		} else {
			logger.info("Updating orderState failed from erp! OrderId : " + orderId + ", AppId : " + appid
					+ ", OrderStatus : " + orderState.getIntValue() + ".");
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "failure!");
		}
		return ret;
	}
}
