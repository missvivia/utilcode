package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.vo.CouponVO;
import com.xyl.mmall.cms.facade.OrderQueryFacade;
import com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam;
import com.xyl.mmall.cms.vo.OrderOperateLogVO;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.out.facade.SMSFacade;
import com.xyl.mmall.common.param.SMSParam;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;
import com.xyl.mmall.mainsite.util.MainsiteVOConvertUtil;
import com.xyl.mmall.mainsite.vo.order.OrderForm2VO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderLogisticsDTO;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
import com.xyl.mmall.order.enums.OperateLogType;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.TradeInternalProxyService;
import com.xyl.mmall.promotion.dto.CouponDTO;

/**
 * 订单
 * 
 * @author author:lhp
 *
 * @version date:2015年5月18日上午9:24:09
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private OrderQueryFacade orderQueryFacade;

	@Autowired
	private ItemCenterFacade itemCenterFacade;

	@Autowired
	private ReturnPackageFacade returnPackageFacade;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private TradeInternalProxyService tradeInternalProxyService;

	@Autowired
	private SMSFacade smsFacade;

	@Autowired
	private AgentService agentService;

	// 全部订单页面
	@RequestMapping(value = "/orderlist")
	@RequiresPermissions(value = { "order:orderlist" })
	public String orderListPage(Model model, @RequestParam(value = "pageName", required = false) String pageName) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("expressCompany", ExpressCompany.validValues());
		model.addAttribute("pendingPage", false);
		return "pages/order/orderlist";
	}

	// 待发货页面
	@RequestMapping(value = "/pendingorderlist")
	@RequiresPermissions(value = { "order:orderlist" })
	public String pendingorderListPage(Model model, @RequestParam(value = "pageName", required = false) String pageName) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("expressCompany", ExpressCompany.validValues());
		model.addAttribute("pendingPage", true);
		return "pages/order/orderlist";
	}

	@RequestMapping(value = "/searchOrder")
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO searchProductByBusinessId(OrderSearchParam searchParam) {
		long loginId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(loginId);
		searchParam.setBusinessId(supplierId);
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		baseJsonVO.setResult(orderQueryFacade.searchOrderForm(searchParam));
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

	/**
	 * 我的订单详情
	 * 
	 * @param model
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:orderlist" })
	public String detail(Model model, @RequestParam(value = "orderId") long orderId) {
		// 1.查询指定的订单信息

		RetArg retArg = orderQueryFacade.queryOrderFormByOrderId(orderId);
		OrderFormDTO orderDTO = RetArgUtil.get(retArg, OrderFormDTO.class);
		if (orderDTO == null) {
			return "pages/500";
		}
		// 2.生成VO对象
		OrderForm2VO order2VO = MainsiteVOConvertUtil.convertToOrderForm2VO(orderDTO, null);
		// 转换OrderFormState
		// MainsiteVOConvertUtil.resetOrderFormState(order2VO);
		// 3.赋值优惠券
		CouponDTO couponDTO = RetArgUtil.get(retArg, CouponDTO.class);
		order2VO.setCouponVO(new CouponVO(couponDTO));
		// order2VO.setCouponSPrice(couponSPrice);

		// 订单用户基本信息
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(orderDTO.getUserId());
		order2VO.setUserId(orderDTO.getUserId());
		order2VO.setUserName(userProfileDTO.getUserName());
		order2VO.setPhoneNo(userProfileDTO.getMobile());
		order2VO.setNickName(userProfileDTO.getNickName());
		// 设置代客下单账号
		if (orderDTO.getAgentId() > 0) {
			AgentDTO agentDTO = agentService.findAgentById(orderDTO.getAgentId());
			order2VO.setProxyAccount(agentDTO != null ? agentDTO.getName() : "");
		}
		// 4.返回结果
		model.addAttribute("order2VO", order2VO);
		model.addAttribute("expressCompany", ExpressCompany.validValues());
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/order/orderDetail";
	}

	/**
	 * 发货
	 * 
	 * @param orderId
	 */
	@RequestMapping(value = "/deliver", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO operateOrderdeliver(@RequestBody OrderLogisticsDTO orderLogisticsDTO) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		if (!checkOrderLogisticsDTO(orderLogisticsDTO)) {// 物流业务暂时隐藏掉了，发货不需要填物流
			baseJsonVO.setCode(ResponseCode.RES_EPARAM);
			baseJsonVO.setMessage("参数有误！订单Id,用户Id,快递必须有值！");
			return baseJsonVO;
		}
		long userId = SecurityContextUtils.getUserId();
		orderLogisticsDTO.setCreateBy(userId);
		orderLogisticsDTO.setUpdateBy(userId);
		orderLogisticsDTO.setOperateUserType(OperateUserType.BACKERNDER);
		long businessId = itemCenterFacade.getSupplierId(userId);
		orderLogisticsDTO.setBusinessId(businessId);
		int result = orderService.deliverGoods(orderLogisticsDTO);
		if (result > 0) {
			// 发送短信验证码
			smsFacade.sendGoodsSingle(new SMSParam(orderLogisticsDTO.getOrderId()));// 先修改状态，发送短信有没成功不管
			baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
			baseJsonVO.setMessage("发货成功！");
		} else {
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage("发货失败！");
		}
		return baseJsonVO;
	}

	private boolean checkOrderLogisticsDTO(OrderLogisticsDTO orderLogisticsDTO) {
		// remove StringUtils.isEmpty(orderLogisticsDTO.getMailNO())
		if (orderLogisticsDTO.getOrderId() <= 0 || orderLogisticsDTO.getUserId() <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 关闭订单
	 * 
	 * @param orderId
	 */
	@RequestMapping(value = "/close", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO closeOrder(@RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "userId") long userId, @RequestParam(value = "reason") String reason) {
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		OrderCancelInfoDTO cancelDTO = new OrderCancelInfoDTO();
		cancelDTO.setOrderId(orderId);
		cancelDTO.setUserId(userId);
		cancelDTO.setCancelSource(OrderCancelSource.BUSINESSER);
		cancelDTO.setReason(reason);
		cancelDTO.setRtype(OrderCancelRType.ORI);
		cancelDTO.setBusinessId(businessId);
		RetArg ret = orderFacade.cancelOrder(cancelDTO);
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		if (!RetArgUtil.get(ret, Boolean.class)) {
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage("关闭订单失败");
			logger.error("orderFacade.cancelTrade fail" + " ,orderId=" + orderId + " ,userId=" + userId);
		} else {
			baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
		}
		return baseJsonVO;
	}

	/**
	 * 是否同意取消订单
	 * 
	 * @param orderId
	 */
	// TODO
	@RequestMapping(value = "/isAgreeCancelOrder", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO agreeCancelOrder(@RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "userId") long userId, @RequestParam(value = "reason") String reason,
			@RequestParam(value = "isCancel") boolean isCancel) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		if (!isCancel && StringUtils.isEmpty(reason)) {
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage("不同意取消订单，理由不能为空!");
			return baseJsonVO;
		}
		long businessId = itemCenterFacade.getSupplierId(SecurityContextUtils.getUserId());

		// TODO 涉及退款
		baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
		return baseJsonVO;
	}

	/**
	 * 修改收货人地址
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/changeAddress", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO updateOrderExpInfo(Model model, @RequestBody FrontOrderExpInfoUpdateParam param) {
		BaseJsonVO ret = new BaseJsonVO();
		long loginId = SecurityContextUtils.getUserId();
		param.getChgParam().setOperatorId(loginId);
		param.getChgParam().setOperateUserType(OperateUserType.BACKERNDER);
		int result = orderFacade.updateOrderExpInfo(param);
		if (result > 0) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("修改收货人地址成功！");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("修改收货人地址失败！");
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 修改发票状态
	 * 
	 * @param invoiceDTO
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/updateInvoiceState", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO updateInvoiceState(@RequestBody InvoiceDTO invoiceDTO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (invoiceDTO.getState() == null || invoiceDTO.getId() <= 0) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("参数有误,发票状态为空或者发票ID没传入！");
			return ret;
		}
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		invoiceDTO.setUpdateBy(loginId);
		invoiceDTO.setBusinessId(businessId);
		boolean result = orderQueryFacade.updateInvoiceStateInBusiness(invoiceDTO);
		if (result) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("修改发票状态成功！");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("修改发票状态失败！");
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 修改金额
	 * 
	 * @param cashParam
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/updateCash", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO updateSalePrice(Model model, @RequestBody OrderOperateParam param) {
		BaseJsonVO ret = new BaseJsonVO();
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		param.setAgentId(loginId);
		param.setBusinessId(businessId);
		param.setOperateUserType(OperateUserType.BACKERNDER);
		int result = tradeInternalProxyService.modifyTradeCash(param);
		if (result > 0) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("修改金额成功！");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("修改金额失败！");
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 新增或者编辑备注
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/addOrUpdateComment", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO addOrUpdateComment(OrderOperateParam param) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isEmpty(param.getComment())) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("备注内容不能为空！");
			return ret;
		}
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		param.setAgentId(loginId);
		param.setBusinessId(businessId);
		param.setOperateUserType(OperateUserType.BACKERNDER);
		int result = orderFacade.addOrUpdateOrderFormComment(param);
		if (result > 0) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("新增或者编辑备注成功！");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("新增或者编辑备注失败！");
		}
		return ret;
	}

	/**
	 * 新增发票
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @param title
	 * @param associated
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/addInvoice", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO addInvoiceInOrd(Model model, @RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "userId") long userId, @RequestParam(value = "invoiceNo") String invoiceNo) {
		BaseJsonVO ret = new BaseJsonVO();
		if (orderId <= 0 || userId <= 0 || StringUtils.isEmpty(invoiceNo)) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("参数有误,发票号为空或者订单Id,用户Id没值！");
			return ret;
		}
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		invoiceDTO.setOrderId(orderId);
		invoiceDTO.setUserId(userId);
		invoiceDTO.setInvoiceNo(invoiceNo);
		invoiceDTO.setState(InvoiceInOrdState.KP_ING);
		invoiceDTO.setBusinessId(businessId);
		invoiceDTO.setCreateBy(loginId);
		invoiceDTO.setUpdateBy(loginId);
		invoiceDTO.setOperateUserType(OperateUserType.BACKERNDER);
		int result = orderFacade.addInvoice(invoiceDTO);
		if (result > 0) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("新增发票成功！");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("新增发票失败！");
		}
		return ret;
	}

	@RequestMapping(value = "/orderdetail/queryOperateLog", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO queryOrderOperateLong(@RequestParam(value = "orderId") long orderId) {
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		BaseJsonVO ret = new BaseJsonVO();
		OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
		operateLogDTO.setBusinessId(businessId);
		operateLogDTO.setOrderId(orderId);
		List<OrderOperateLogVO> queryOperateLog = orderQueryFacade.queryOperateLog(operateLogDTO, null, null);
		List<OrderOperateLogVO> filterOperateLog = new ArrayList<OrderOperateLogVO>();
		if (queryOperateLog != null) {
			for (OrderOperateLogVO orderOperateLogVO : queryOperateLog) {
				if (orderOperateLogVO.getOperateType() != OperateLogType.ORDER_PAY.getIntValue()) {
					filterOperateLog.add(orderOperateLogVO);
				}
			}
		}
		ret.setResult(filterOperateLog);
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}

	/**
	 * 新增物流或编辑
	 * 
	 * @param orderId
	 */
	@RequestMapping(value = "/addOrUpdateOrderLogistics", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:orderlist" })
	public @ResponseBody BaseJsonVO addOrUpdateOrderLogistics(@RequestBody OrderLogisticsDTO orderLogisticsDTO) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		if (!checkOrderLogisticsDTO(orderLogisticsDTO)) {
			baseJsonVO.setCode(ResponseCode.RES_EPARAM);
			baseJsonVO.setMessage("参数有误！订单Id,快递号,用户Id,快递必须有值！");
			return baseJsonVO;
		}
		long userId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(userId);
		orderLogisticsDTO.setBusinessId(businessId);
		if (orderLogisticsDTO.getId() == 0) {
			orderLogisticsDTO.setCreateBy(userId);
		}
		orderLogisticsDTO.setUpdateBy(userId);
		orderLogisticsDTO.setOperateUserType(OperateUserType.BACKERNDER);
		int result = orderFacade.addOrUpdateOrderLogistics(orderLogisticsDTO);
		if (result > 0) {
			baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
			baseJsonVO.setMessage(orderLogisticsDTO.getId() > 0 ? "修改物流信息成功！" : "新增物流信息成功");
		} else {
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage(orderLogisticsDTO.getId() > 0 ? "修改物流信息失败！" : "新增物流信息失败");
		}
		return baseJsonVO;
	}

}
