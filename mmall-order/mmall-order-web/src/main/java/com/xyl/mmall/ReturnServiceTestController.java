package com.xyl.mmall;
//package com.xyl.mmall;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.netease.print.common.util.DateFormatEnum;
//import com.xyl.mmall.order.dto.ReturnFormDTO;
//import com.xyl.mmall.order.enums.ExpressCompany;
//import com.xyl.mmall.order.enums.RefundType;
//import com.xyl.mmall.order.enums.ReturnOrderSkuState;
//import com.xyl.mmall.order.param.ReturnConfirmParam;
//import com.xyl.mmall.order.param.ReturnFormApplyParam;
//import com.xyl.mmall.order.param.ReturnFormExpInfoParam;
//import com.xyl.mmall.order.param.ReturnFormQueryParam;
//import com.xyl.mmall.order.param.ReturnOrderSkuParam;
//import com.xyl.mmall.order.param.ReturnPassedParam;
//import com.xyl.mmall.order.param.ReturnRefusedParam;
//import com.xyl.mmall.order.service.OrderService;
//import com.xyl.mmall.order.service.ReturnQueryService;
//import com.xyl.mmall.order.service.ReturnUpdateService;
//
///**
// * 
// * @author hzwangjianyi@corp.netease.com
// * @create 2014年9月17日 下午1:32:11
// *
// */
//@Controller()
//@RequestMapping("/return_test")
//public class ReturnServiceTestController {
//
//	private static final int USER_ID = 921;
//	private static final int ORDER_ID_223 = 223;
//	private static final int ORDER_ID_322 = 322;
//	
//	@Resource(name = "orderService")
//	protected OrderService orderService;
//	
//	@Resource(name = "returnQueryService")
//	protected ReturnQueryService retQueryService;
//	
//	@Resource(name = "returnUpdateService")
//	protected ReturnUpdateService retUpdateService;
//	
//	@RequestMapping(value = "/add", method = RequestMethod.GET)
//	public @ResponseBody Object addReturn(
//			Model model, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="returnId") int retId) {
//		List<Object> ret = new ArrayList<Object>();
//		ReturnFormDTO rfDTO = null;
//		rfDTO = retUpdateService.applyReturn(USER_ID, orderId, true, createApplyParam(orderId));
//		ret.add(rfDTO);
//		ReturnFormExpInfoParam persistParam = new ReturnFormExpInfoParam();
//		persistParam.setReturnExpInfoId(520);
//		persistParam.setExpressCompany(ExpressCompany.SHUNFENG);
//		persistParam.setMailNO("999999999");
//		rfDTO = retUpdateService.updateReturnExpInfo(rfDTO.getId(), persistParam);
//		ret.add(rfDTO);
//		return ret;
//	}
//	
//	private ReturnOrderSkuParam createReturnOrderSkuParam(long orderId) {
//		ReturnOrderSkuParam param = new ReturnOrderSkuParam();
//		if(ORDER_ID_223 == orderId) {
//			param.setOrderSkuId(555);
//			param.setSkuId(5);
//		} else if(ORDER_ID_322 == orderId) {
//			param.setOrderSkuId(777);
//			param.setSkuId(7);
//		} else {
//			
//		}
//		param.setCount(3);
//		param.setReason("Just do it!");
//		param.setTotalReturnPrice(new BigDecimal(100));
//		return param;
//	}
//	
//	private ReturnFormApplyParam createApplyParam(long orderId) {
//		ReturnFormApplyParam applyParam = new ReturnFormApplyParam();
//		BigDecimal bd = new BigDecimal(921);
//		applyParam.setGoodsTotalRPrice(bd);
//		applyParam.setHdYPrice(bd);
//		applyParam.setCouponYPrice(bd);
//		applyParam.setExpPrice(bd);
//		applyParam.setPayedCashPrice(bd);
//		applyParam.setHbPrice(bd);
//		applyParam.setReturnCashPrice(bd);
//		applyParam.setExpSubsidyPrice(bd);
//		applyParam.setRefundType(RefundType.WANGYIBAO);
//		List<ReturnOrderSkuParam> retOrderSkuParamList = new ArrayList<ReturnOrderSkuParam>();
//		retOrderSkuParamList.add(createReturnOrderSkuParam(orderId));
//		applyParam.setRetOrderSkuParamList(retOrderSkuParamList);
//		return applyParam;
//	}
//	
//	@RequestMapping(value = "/get_all", method = RequestMethod.GET)
//	public @ResponseBody Object getAll(Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="start") String start, 
//			@RequestParam(value="end") String end
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "All":
//			obj = retQueryService.queryAll(null);
//			break;
//		case "Order":
//			obj = retQueryService.queryAllWithOrderId(orderId, null);
//			break;
//		case "Time":
//			obj = retQueryService.queryAllWithTimeRange(start, end, null);
//			break;
//		case "User":
//			obj = retQueryService.queryAllWithUserId(USER_ID, null);
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/get_waiting_confirm", method = RequestMethod.GET)
//	public @ResponseBody Object getWaitingConfrim(Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="start") String start, 
//			@RequestParam(value="end") String end
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "All":
//			obj = retQueryService.queryWaitingConfirm(null);
//			break;
//		case "Order":
//			obj = retQueryService.queryWaitingConfirmWithOrderId(orderId, null);
//			break;
//		case "Time":
//			obj = retQueryService.queryWaitingConfirmWithTimeRange(start, end, null);
//			break;
//		case "User":
//			obj = retQueryService.queryWaitingConfirmWithUserId(USER_ID, null);
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/get_waiting_return_audit", method = RequestMethod.GET)
//	public @ResponseBody Object getWaitingReturnAudit(Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="start") String start, 
//			@RequestParam(value="end") String end
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "All":
//			obj = retQueryService.queryWaitingReturnAudit(null);
//			break;
//		case "Order":
//			obj = retQueryService.queryWaitingReturnAuditWithOrderId(orderId, null);
//			break;
//		case "Time":
//			obj = retQueryService.queryWaitingReturnAuditWithTimeRange(start, end, null);
//			break;
//		case "User":
//			obj = retQueryService.queryWaitingReturnAuditWithUserId(USER_ID, null);
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/get_refused", method = RequestMethod.GET)
//	public @ResponseBody Object getRefused(Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="start") String start, 
//			@RequestParam(value="end") String end
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "All":
//			obj = retQueryService.queryRefused(null);
//			break;
//		case "Order":
//			obj = retQueryService.queryRefusedWithOrderId(orderId, null);
//			break;
//		case "Time":
//			obj = retQueryService.queryRefusedWithTimeRange(start, end, null);
//			break;
//		case "User":
//			obj = retQueryService.queryRefusedWithUserId(USER_ID, null);
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/get_finished", method = RequestMethod.GET)
//	public @ResponseBody Object getFinished(Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="start") String start, 
//			@RequestParam(value="end") String end
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "All":
//			obj = retQueryService.queryFinished(null);
//			break;
//		case "Order":
//			obj = retQueryService.queryFinishedWithOrderId(orderId, null);
//			break;
//		case "Time":
//			obj = retQueryService.queryFinishedWithTimeRange(start, end, null);
//			break;
//		case "User":
//			obj = retQueryService.queryFinishedWithUserId(USER_ID, null);
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/get_canceled", method = RequestMethod.GET)
//	public @ResponseBody Object getCanceled(Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="start") String start, 
//			@RequestParam(value="end") String end
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "All":
//			obj = retQueryService.queryCanceled(null);
//			break;
//		case "Order":
//			obj = retQueryService.queryCanceledWithOrderId(orderId, null);
//			break;
//		case "Time":
//			obj = retQueryService.queryCanceledWithTimeRange(start, end, null);
//			break;
//		case "User":
//			obj = retQueryService.queryCanceledWithUserId(USER_ID, null);
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/get_combined", method = RequestMethod.GET)
//	public @ResponseBody Object getCombined(Model model, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="mailNO") String mailNO, 
//			@RequestParam(value="expEnum") int expEnum, 
//			@RequestParam(value="start") String start, 
//			@RequestParam(value="end") String end, 
//			@RequestParam(value="skuId") int skuId, 
//			@RequestParam(value="tag") String tag
//			) {
//		Object obj = null;
//		ReturnFormQueryParam rcqParam = new ReturnFormQueryParam();
//		if(orderId > 0) {
//			rcqParam.setOrderId(orderId);
//		}
//		if(null != mailNO && mailNO.length() > 0) {
//			rcqParam.setMailNO(mailNO);
//		}
//		if(expEnum > 0) {
//			rcqParam.setExpCompany(ExpressCompany.SHUNFENG.genEnumByIntValue(expEnum));
//		}
//		if(null != start && start.length() > 0 && null != end && end.length() > 0) {
//			rcqParam.setStartTime(DateFormatEnum.TYPE6.getTimeOfDate(start));
//			rcqParam.setEndTime(DateFormatEnum.TYPE6.getTimeOfDate(end));
//		}
//		switch(tag) {
//		case "all_state":
//			obj = retQueryService.queryReturnWithCombinedParam(rcqParam, null, skuId, ReturnOrderSkuState.values());
//			break;
//		case "no":
//			obj = retQueryService.queryReturnWithCombinedParam(rcqParam, null, skuId, 
//					new ReturnOrderSkuState[] {ReturnOrderSkuState.NOT_CONFIRMED});
//			break;
//		case "part":
//			obj = retQueryService.queryReturnWithCombinedParam(rcqParam, null, skuId, 
//					new ReturnOrderSkuState[] {ReturnOrderSkuState.PART_CONFIRMED});
//			break;
//		case "all":
//			obj = retQueryService.queryReturnWithCombinedParam(rcqParam, null, skuId, 
//					new ReturnOrderSkuState[] {ReturnOrderSkuState.ALL_CONFIRMED});
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/get_is", method = RequestMethod.GET)
//	public @ResponseBody Object getCombined(Model model, 
//			@RequestParam(value="retId") int retId, 
//			@RequestParam(value="tag") String tag
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "no":
//			obj = retQueryService.isNoRetOrdSkuConfirmed(retId);
//			break;
//		case "part":
//			obj = retQueryService.isPartRetOrdSkuConfirmed(retId);
//			break;
//		case "all":
//			obj = retQueryService.isAllRetOrdSkuConfirmed(retId);
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/update", method = RequestMethod.GET)
//	public @ResponseBody Object setReturn(
//			Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="retId") int retId, 
//			@RequestParam(value="orderSkuId") int orderSkuId, 
//			@RequestParam(value="confirmCount") int confirmCount, 
//			@RequestParam(value="extInfo") String extInfo
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "confirm":
//			ReturnConfirmParam rcParam = new ReturnConfirmParam();
//			rcParam.setConfirmCount(confirmCount);
//			rcParam.setExtInfo(extInfo);
//			obj = retUpdateService.confirmReturnedOrderSku(orderId, orderSkuId, rcParam);
//			break;
//		case "finish":
//			ReturnPassedParam rpParam = new ReturnPassedParam();
//			rpParam.setReturnMoney(new BigDecimal(921));
//			rpParam.setExtInfo(extInfo);
//			obj = retUpdateService.finishReturn(retId, rpParam);
//			break;
//		case "refuse":
//			ReturnRefusedParam rrParam = new ReturnRefusedParam();
//			rrParam.setExtInfo(extInfo);
//			obj = retUpdateService.refuseReturn(retId, rrParam);
//			break;
//		case "cancel_refuse":
//			obj = retUpdateService.cancelRefuse(retId);
//		case "set_state_cancled":
//			obj = retUpdateService.setReturnStateToCanceled(retId);
//		default:
//			break;
//		}
//		return obj;
//	}
//}
