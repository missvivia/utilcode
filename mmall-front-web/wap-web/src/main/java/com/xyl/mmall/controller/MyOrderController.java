/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatEnum;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.common.util.SerialNumberUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.vo.CouponVO;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.common.enums.OrderQueryType;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.out.facade.PayFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.mainsite.facade.OrderTraceFacade;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.OrderTraceVO;
import com.xyl.mmall.mainsite.vo.order.ExpTrackLogVO;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.enums.ShareChannel;
import com.xyl.mmall.promotion.meta.RedPacketShareRecord;
import com.xyl.mmall.promotion.service.RedPacketShareRecordService;

/**
 * @author hzlihui2014
 *
 */
@Controller
@RequestMapping("/myorder")
public class MyOrderController {

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private OrderTraceFacade orderTraceFacade;

	private Boolean isVisible = true;

	@Autowired
	private ReturnPackageFacade returnPackageFacade;

	@Autowired
	private PayFacade payFacade;

	@Autowired
	private RedPacketShareRecordService redPacketShareRecordService;

	/**
	 * 我的订单列表-显示页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String order(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("name", "order");
		return "pages/order/orderlist";
	}

	/**
	 * 我的订单物流信息-显示页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/express" }, method = RequestMethod.GET)
	public String orderExpress(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "pages/order/express";
	}

	/**
	 * 我的订单列表-获取列表数据
	 * 
	 * @param model
	 * @param queryType
	 * @param limit
	 * @param offset
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Deprecated
	public String listData(Model model, @RequestParam(value = "type") int queryTypeInt,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(value = "search", required = false) String search) {
		// 0.参数准备
		long userId = getUserId();
		limit = limit <= 0 ? 5 : limit;
		DDBParam param = DDBParam.genParamX(limit);
		param.setOffset(offset);
		param.setAsc(false);
		param.setOrderColumn("orderTime");

		// 1.根据type,调用不同的方法(0:全部订单,1:待付款,2:待发货,3:已发货,4:查询 )
		OrderQueryType queryType = OrderQueryType.genEnumByIntValueSt(queryTypeInt);
		RetArg retArg = orderFacade.queryOrderList(userId, queryType, search, param, true);
		List<OrderFormDTO> orderDTOList = RetArgUtil.get(retArg, ArrayList.class);
		param = RetArgUtil.get(retArg, DDBParam.class);

		// 2.生成VO对象
		List<OrderForm2VO> order2VOList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(orderDTOList)) {
			for (OrderFormDTO orderDTO : orderDTOList) {
				OrderForm2VO order2VO = convertToOrderForm2VO(orderDTO);
				// 转换OrderFormState
				// MainsiteVOConvertUtil.resetOrderFormState(order2VO);
				CollectionUtil.addOfListFilterNull(order2VOList, order2VO);
			}
		}

		// 3.输出结果
		Map<String, Object> jsonMap2 = new LinkedHashMap<>();
		jsonMap2.put("total", param != null && param.getTotalCount() != null ? param.getTotalCount() : 0);
		jsonMap2.put("list", order2VOList);

		Map<String, Object> jsonMap1 = new LinkedHashMap<>();
		jsonMap1.put("code", 200);
		jsonMap1.put("result", jsonMap2);
		model.addAllAttributes(jsonMap1);
		return null;
	}

	/**
	 * 我的订单列表-获取列表数据
	 * 
	 * @param model
	 * @param queryType
	 * @param limit
	 * @param offset
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/orderlist", method = RequestMethod.GET)
	public String newlistData(Model model, OrderSearchParam orderSearchParam) {
		// 0.参数准备
		long userId = getUserId();
		orderSearchParam.setOrderColumn("parentId");
		orderSearchParam.setAsc(false);
		orderSearchParam.setUserId(userId);
		orderSearchParam.setVisible(true);

		RetArg retArg = orderFacade.queryNewOrderList(orderSearchParam);
		// 3.输出结果
		Map<String, Object> jsonMap2 = new LinkedHashMap<String, Object>();
		jsonMap2.put("total", RetArgUtil.get(retArg, OrderSearchParam.class).getTotalCount());
		jsonMap2.put("list", RetArgUtil.get(retArg, ArrayList.class));

		Map<String, Object> jsonMap1 = new LinkedHashMap<String, Object>();
		jsonMap1.put("code", 200);
		jsonMap1.put("result", jsonMap2);
		model.addAllAttributes(jsonMap1);
		return null;
	}

	/**
	 * 我的订单列表-获取列表数据计数
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getOrderNumbers", method = RequestMethod.GET)
	public String listData(Model model) {
		long userId = getUserId();
		// 1.生成各个Tab的订单计数
		Map<OrderQueryType, Integer> orderCountMap = orderFacade.queryOrderListCount(userId);
		// 2.转换计数
		List<Integer> countList = new ArrayList<>();
		OrderQueryType queryType = null;
		queryType = OrderQueryType.ALL;
		countList.add(orderCountMap.get(queryType) != null ? orderCountMap.get(queryType) : 0);
		queryType = OrderQueryType.WAITING_PAY;
		countList.add(orderCountMap.get(queryType) != null ? orderCountMap.get(queryType) : 0);
		queryType = OrderQueryType.WAITING_DELIVE;
		countList.add(orderCountMap.get(queryType) != null ? orderCountMap.get(queryType) : 0);
		queryType = OrderQueryType.ALREADY_DELIVE;
		countList.add(orderCountMap.get(queryType) != null ? orderCountMap.get(queryType) : 0);

		// 3.输出结果
		Map<String, Object> jsonMap2 = new LinkedHashMap<>();
		jsonMap2.put("list", countList);

		Map<String, Object> jsonMap1 = new LinkedHashMap<>();
		jsonMap1.put("code", 200);
		jsonMap1.put("result", jsonMap2);
		model.addAllAttributes(jsonMap1);
		return null;
	}

	/**
	 * 取消订单
	 * 
	 * @param model
	 * @param orderId
	 * @param reason
	 *            退款原因
	 * @param refundType
	 *            0:原路退回, 1:网易宝
	 * @return
	 */
	@BILog(action = "click", type = "cancelOrder", clientType = "wap")
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public void cancel(Model model, @RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "reason") String reason, @RequestParam(value = "type") int rtypeInt) {
		// 0.参数准备
		long userId = getUserId(), parentId = 0;
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
		// OrderFormDTO orderDTO = RetArgUtil.get(ret, OrderFormDTO.class);
		// OrderForm2VO order2VO = convertToOrderForm2VO(orderDTO);
		// 转换OrderFormState
		// MainsiteVOConvertUtil.resetOrderFormState(order2VO);

		// 返回结果
		Map<String, Object> jsonMap1 = new LinkedHashMap<>();
		jsonMap1.put("code", isSucc ? 200 : 201);
		// jsonMap1.put("result", order2VO);
		model.addAllAttributes(jsonMap1);
	}

	/**
	 * 确认收货
	 * 
	 * @param model
	 * @param orderId
	 */
	@BILog(action = "click", clientType = "order", type = "skuSellStatisticsMainsite")
	@RequestMapping(value = "/confirm", method = RequestMethod.GET)
	public void confirm(Model model, @RequestParam(value = "orderId") long orderId) {
		int result = 0;
		long userId = getUserId();
		// 0.参数准备
		OrderOperateParam param = new OrderOperateParam();
		param.setNewState(OrderFormState.FINISH_TRADE);
		param.setAgentId(userId);
		param.setOperateUserType(OperateUserType.USER);
		param.setOrderId(orderId);
		param.setUserId(userId);
		result = orderFacade.modifyOrderState(param);
		// 返回结果
		Map<String, Object> jsonMap = new LinkedHashMap<>();
		jsonMap.put("code", result > 0 ? 200 : 201);
		jsonMap.put("message", result > 0 ? null : "确认收货失败");
		model.addAllAttributes(jsonMap);
	}

	/**
	 * 我的订单详情
	 * 
	 * @param model
	 * @param type
	 * @return
	 */
	@BILog(action = "page", type = "orderDetailPage", clientType = "wap")
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(Model model, @RequestParam(value = "orderId") long orderId) {
		// 0.参数准备
		long userId = getUserId();

		// 1.查询指定的订单信息
		RetArg retArg = orderFacade.queryOrderFormByOrderIdAndUserId(userId, orderId, isVisible);
		OrderFormDTO orderDTO = RetArgUtil.get(retArg, OrderFormDTO.class);
		if (orderDTO == null) {
			return "pages/500";
		}
		// 2.生成VO对象
		OrderForm2VO order2VO = convertToOrderForm2VO(orderDTO);

		// 3.赋值优惠券
		CouponDTO couponDTO = RetArgUtil.get(retArg, CouponDTO.class);
		order2VO.setCouponVO(new CouponVO(couponDTO));

		// 转换OrderFormState
		// MainsiteVOConvertUtil.resetOrderFormState(order2VO);
		// 3.读取物流信息
		// List<List<ExpTrackLogVO>> trackVOListList =
		// genExpTrackLogVOListList(orderDTO);

		// 4.返回结果
		// model.addAttribute("trackVOListList", trackVOListList);
		model.addAttribute("order2VO", order2VO);
		return "pages/order/orderDetail";
	}

	/**
	 * 删除订单
	 * 
	 * @param model
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public void delete(Model model, @RequestParam(value = "orderId") long orderId) {
		// 0.参数准备
		long userId = getUserId();
		// 1.设置指定的订单为不可见
		boolean isVisible = false, isSucc = true;
		isSucc = isSucc && orderFacade.updateIsVisible(userId, orderId, isVisible);

		// 2.返回结果
		Map<String, Object> jsonMap1 = new LinkedHashMap<>();
		jsonMap1.put("code", isSucc ? 200 : 201);
		model.addAllAttributes(jsonMap1);
	}

	/**
	 * 根据parentId取订单Ids
	 * 
	 * @param model
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/getSubOrderIds", method = RequestMethod.GET)
	public void querySubOrderIds(Model model, @RequestParam(value = "parentId") long parentId) {
		Map<String, Object> jsonMap = new LinkedHashMap<>();
		jsonMap.put("code", ResponseCode.RES_SUCCESS);
		jsonMap.put("result", orderFacade.getSubOrderIds(parentId));
		model.addAllAttributes(jsonMap);
	}

	private long getUserId() {
		return SecurityContextUtils.getUserId();
	}

	private String getRedPacketShareUrl(long orderId) {
		// 是否可以分享红包
		RedPacketShareRecord record = redPacketShareRecordService.getByTypeAndValue(ShareChannel.BY_ORDER, orderId);
		if (record == null) {
			return null;
		}
		// 构建分享id
		String shareId = SerialNumberUtil.makeupSerial(record.getId(), record.getStartTime(), new String[] { "/" });
		return "/m/share/red/apply?id=" + shareId;
	}

	/**
	 * 读取物流信息
	 * 
	 * @param orderDTO
	 * @return
	 */
	@Deprecated
	private List<List<ExpTrackLogVO>> genExpTrackLogVOListList(OrderFormDTO orderDTO) {
		if (orderDTO == null)
			return null;
		List<List<ExpTrackLogVO>> trackVOListList = new ArrayList<>();
		for (OrderPackageDTO packageDTO : orderDTO.getOrderPackageDTOList()) {
			List<ExpTrackLogVO> trackVOList = queryExpTrackLogVOList(packageDTO);
			trackVOList = queryExpTrackLogVOList(packageDTO);
			trackVOList = trackVOList == null ? new ArrayList<ExpTrackLogVO>() : trackVOList;
			trackVOListList.add(trackVOList);
		}
		return trackVOListList;
	}

	/**
	 * 查询物流信息
	 * 
	 * @param op
	 * @return
	 */
	@Deprecated
	private List<ExpTrackLogVO> queryExpTrackLogVOList(OrderPackage op) {
		if (op == null || op.getOrderPackageState() == OrderPackageState.INIT)
			return new ArrayList<>();

		String mailNO = op.getMailNO(), expressCompany = op.getExpressCompanyReturn();
		ExpTrackLogVO trackVO1 = new ExpTrackLogVO();
		trackVO1.setAcceptAddress(op.getWarehouseName());
		trackVO1.setAcceptDate(DateFormatEnum.TYPE5.getFormatDate(op.getExpSTime()));
		trackVO1.setAcceptTime(op.getExpSTime());
		trackVO1.setRemark("商品已发货");

		List<OrderTraceVO> orderTraceVOList = orderTraceFacade.getTrace(expressCompany, mailNO);
		List<ExpTrackLogVO> trackVOList = MainsiteVOConvertUtil.convertToExpTrackLogVOList(orderTraceVOList);
		trackVOList = trackVOList == null ? new ArrayList<ExpTrackLogVO>() : trackVOList;
		trackVOList.add(0, trackVO1);
		return trackVOList;
	}

	/**
	 * 返回OrderForm2VO,并设置canApplyReturn标记
	 * 
	 * @param orderDTO
	 * @return
	 */
	private OrderForm2VO convertToOrderForm2VO(OrderFormDTO orderDTO) {
		if (orderDTO == null)
			return null;
		OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(orderDTO, returnPackageFacade);
		return order2VO;
	}
}
