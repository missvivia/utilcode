package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.backend.vo.CouponVO;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.out.facade.PayFacade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.ios.facade.IosOrderFacade;
import com.xyl.mmall.mobile.ios.facade.pageView.orderDetail.OrderDetailComposite;
import com.xyl.mmall.mobile.ios.facade.pageView.util.ConverUtilForMobile;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.service.MobileHeaderProcess;

@Controller
@RequestMapping("/m/order")
public class MobileOrderController {

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private PayFacade payFacade;

	@Resource
	private IosOrderFacade iosOrderFacade;

	/**
	 * 我的订单列表-获取列表数据
	 * 
	 * @param orderSearchParam
	 * @return
	 */
	@RequestMapping(value = "/orderlist", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO listData(OrderSearchParam orderSearchParam) {
		// 0.参数准备
		long userId = MobileHeaderProcess.getUserId();
		orderSearchParam.setUserId(userId);
		orderSearchParam.setOrderColumn("parentId");
		orderSearchParam.setAsc(false);
		orderSearchParam.setVisible(true);

		RetArg retArg = iosOrderFacade.queryNewOrderList(orderSearchParam);
		BaseJsonVO baseJsonVO = Converter.pageBaseJsonVO(RetArgUtil.get(retArg, ArrayList.class),
				RetArgUtil.get(retArg, OrderSearchParam.class));
		return baseJsonVO;
	}

	/**
	 * 我的订单列表-获取列表数据
	 * 
	 * @param orderSearchParam
	 * @return
	 */
	@RequestMapping(value = "/orderCount", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO listDataCount() {
		// 0.参数准备
		long userId = MobileHeaderProcess.getUserId();
		OrderSearchParam orderSearchParam = new OrderSearchParam();
		orderSearchParam.setUserId(userId);
		orderSearchParam.setOrderColumn("parentId");
		orderSearchParam.setAsc(false);
		orderSearchParam.setVisible(true);
		Map<String, Integer> hashMap = new HashMap<>();
		for (int i = 1; i <= 3; i++) {
			orderSearchParam.setQueryType(i);
			int count = iosOrderFacade.queryNewOrderCount(orderSearchParam);
			if (count < 0) {
				count = 0;
			}
			switch (i) {
			case 1:
				hashMap.put("waitingPay", count);
				break;
			case 2:
				hashMap.put("waitingDelive", count);
				break;
			case 3:
				hashMap.put("alreadyDelive", count);
				break;
			}
		}

		BaseJsonVO baseJsonVO = new BaseJsonVO();
		baseJsonVO.setCodeAndMessage(ErrorCode.SUCCESS.getIntValue(), "OK");
		baseJsonVO.setResult(hashMap);
		return baseJsonVO;
	}

	/**
	 * 取消订单
	 * 
	 * @param model
	 * @param orderId
	 * @param reason
	 *            退款原因
	 * @param refundType
	 *            0:原路退回,
	 * @return
	 */
	@BILog(action = "click", type = "cancelOrder", clientType = "app")
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO cancel(@RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "reason") String reason, @RequestParam(value = "type") int rtypeInt) {
		// 0.参数准备
		long userId = MobileHeaderProcess.getUserId(), parentId = 0;
		// 1.先根据orderId取得parentId，然后取消所有关联订单
		OrderFormBriefDTO orderFormBriefDTO = orderFacade.queryOrderFormBriefDTO(userId, orderId, true);
		if (orderFormBriefDTO != null) {
			parentId = orderFormBriefDTO.getParentId();
		} else {
			parentId = orderId;
		}
		List<Long> orderIds = orderFacade.getSubOrderIds(parentId);
		if (CollectionUtil.isEmptyOfList(orderIds)) {
			orderIds = new ArrayList<Long>();
			orderIds.add(orderId);
		}
		List<OrderCancelInfoDTO> list = new ArrayList<OrderCancelInfoDTO>();
		for (Long orderId1 : orderIds) {
			OrderCancelInfoDTO cancelDTO = new OrderCancelInfoDTO();
			cancelDTO.setCancelSource(OrderCancelSource.USER);
			cancelDTO.setOrderId(orderId1);
			cancelDTO.setReason(reason);
			cancelDTO.setRtype(OrderCancelRType.genEnumByIntValueSt(rtypeInt));
			cancelDTO.setUserId(userId);
			cancelDTO.setAgentId(userId);
			cancelDTO.setOperateUserType(OperateUserType.USER);
			list.add(cancelDTO);

		}
		RetArg ret = orderFacade.cancelOrders(list);
		// 1.调用取消订单的服务
		// 2.读取调用结果
		Boolean isSucc = RetArgUtil.get(ret, Boolean.class);
		return isSucc ? new BaseJsonVO(ErrorCode.SUCCESS) : Converter
				.errorBaseJsonVO(MobileErrorCode.ORDER_CANCEL_ERROR);
	}

	/**
	 * 确认收货
	 * 
	 * @param model
	 * @param orderId
	 */
	@BILog(action = "click", type = "confirmOrder", clientType = "app")
	@RequestMapping(value = "/confirm", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO confirm(@RequestParam(value = "orderId") long orderId) {
		int result = 0;
		long userId = MobileHeaderProcess.getUserId();
		// 0.参数准备
		OrderOperateParam param = new OrderOperateParam();
		param.setNewState(OrderFormState.FINISH_TRADE);
		param.setAgentId(userId);
		param.setOperateUserType(OperateUserType.USER);
		param.setOrderId(orderId);
		param.setUserId(userId);
		result = orderFacade.modifyOrderState(param);
		return result > 0 ? new BaseJsonVO(ErrorCode.SUCCESS) : Converter
				.errorBaseJsonVO(MobileErrorCode.ORDER_CONFIRM_ERROR);
	}

	/**
	 * 我的订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@BILog(action = "click", type = "orderDetailPage", clientType = "app")
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO detail(@RequestParam(value = "orderId") long orderId, boolean isParentId) {
		// 0.参数准备
		long userId = MobileHeaderProcess.getUserId();
		BaseJsonVO baseJsonVO = new BaseJsonVO(ErrorCode.SUCCESS);
		RetArg retArg = null;
		List<OrderFormDTO> list = new ArrayList<>();
		if (!isParentId) {
			// 1.查询指定的订单信息
			retArg = iosOrderFacade.queryOrderFormByOrderIdAndUserId(userId, orderId, true);
			OrderFormDTO orderDTO = RetArgUtil.get(retArg, OrderFormDTO.class);
			if (orderDTO == null) {
				return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_EXIST_ERROR);
			}
			list.add(orderDTO);

		} else {
			retArg = iosOrderFacade.queryOrderFormByParentOrderIdAndUserId(userId, orderId, true);
			list = RetArgUtil.get(retArg, ArrayList.class);
		}
		if (CollectionUtils.isEmpty(list)) {
			return Converter.errorBaseJsonVO(MobileErrorCode.ORDER_EXIST_ERROR);
		}
		OrderDetailComposite orderDetailComposite = ConverUtilForMobile.convertToCompositeOrderDetail(list);
		// 3.赋值优惠券
		CouponDTO couponDTO = RetArgUtil.get(retArg, CouponDTO.class);
		if (orderDetailComposite != null) {
			orderDetailComposite.setCouponVO(new CouponVO(couponDTO));
			baseJsonVO.setResult(orderDetailComposite);
		}
		return baseJsonVO;
	}

	/**
	 * 删除订单
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO delete(@RequestParam(value = "orderId") long orderId) {
		// 0.参数准备
		long userId = MobileHeaderProcess.getUserId();
		// 1.设置指定的订单为不可见
		boolean isVisible = false, isSucc = true;
		isSucc = isSucc && orderFacade.updateIsVisible(userId, orderId, isVisible);
		// 2.返回结果
		return isSucc ? new BaseJsonVO(ErrorCode.SUCCESS) : Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR);
	}

}
