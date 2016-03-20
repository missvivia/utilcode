package com.xyl.mmall;
//package com.xyl.mmall;
//
//import java.util.ArrayList;
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
//import com.xyl.mmall.order.dto.OrderFormDTO;
//import com.xyl.mmall.order.param.OrderExpInfoChangeParam;
//import com.xyl.mmall.order.service.OrderService;
//
///**
// * 
// * @author hzwangjianyi@corp.netease.com
// * @create 2014年9月17日 下午1:32:11
// *
// */
//@Controller()
//@RequestMapping("/order_test")
//public class OrderServiceTestController {
//
//	private static final int USER_ID = 921;
//	
//	@Resource(name = "orderService")
//	protected OrderService orderService;
//	
//	@RequestMapping(value = "/get", method = RequestMethod.GET)
//	public @ResponseBody Object getOrder(
//			Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId, 
//			@RequestParam(value="orderSkuId") int orderSkuId, 
//			@RequestParam(value="mail") String mail
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "all":
//			obj = orderService.queryAllOrderFormList(USER_ID, true, null);
//			break;
//		case "waitpay":
//			obj = orderService.queryWaitingPayOrderFormList(USER_ID, true, null);
//			break;
//		case "waitsend":
//			obj = orderService.queryWaitingSendOrderFormList(USER_ID, true, null);
//			break;
//		case "sent":
//			obj = orderService.querySentOrderFormList(USER_ID, true, null);
//			break;
//		case "by_2Ids":
//		 	obj = orderService.queryOrderForm(USER_ID, orderId, true);
//		 	break;
//		case "by_orderId":
//			obj = orderService.queryOrderFormByOrderId(orderId);
//			 break;
//		case "by_userId":
//			obj = orderService.queryAllOrderFormList(USER_ID, true, null);
//			 break;
//		case "by_mail":
//			obj = orderService.queryOrderFormListByMailNO(mail, true, null);
//			 break;
//		case "can_ret_order":
//			obj = orderService.canOrderBeReturned(USER_ID, orderId);
//			break;
//		case "can_ret_sku":
//			obj = orderService.canOrderSkuBeReturned(USER_ID, orderId, orderSkuId);
//			break;
//		case "can_kf_openret":
//			obj = orderService.canReopenReturnShowToKF(USER_ID, orderId);
//			break;
//		default:
//			obj = new ArrayList<OrderFormDTO>();
//			break;
//		}
//		return obj;
//	}
//	
//	@RequestMapping(value = "/update", method = RequestMethod.GET)
//	public @ResponseBody Object setOrder(
//			Model model, 
//			@RequestParam(value="tag") String tag, 
//			@RequestParam(value="orderId") int orderId
//			) {
//		Object obj = null;
//		switch(tag) {
//		case "epay_payed":
//			obj = orderService.setPayStateToEPayedWithPayTime(USER_ID, orderId);
//			break;
//		case "codpay_payed":
//			obj = orderService.setPayStateToCODPayedWithPayTime(USER_ID, orderId);
//			break;
//		case "pay_refund":
//			obj = orderService.setPayStateToRefunded(USER_ID, orderId);
//			break;
//		case "pay_close":
//			obj = orderService.setPayStateToClose(USER_ID, orderId);
//			break;
//		case "ord_consigned":
//			obj = orderService.setOrderFormStateToConsigned(USER_ID, orderId);
//			break;
//		case "expInfo_change":
//			OrderExpInfoChangeParam param = new OrderExpInfoChangeParam();
//			param.setProvince("辽宁");  param.setCity("普兰店");  param.setSection("城子坦");
//			param.setAddress("郑沟村姜屯");  param.setConsigneeMobile("mobile");
//			param.setConsigneeTel("tel");  param.setConsigneeName("BB");
//			obj = orderService.updateOrderExpInfo(orderId, param);
//			break;
//		case "kf_reopen_ret":
//			obj = orderService.reOpenReturn(USER_ID, orderId);
//			break;
//		default:
//			break;
//		}
//		return obj;
//	}
//	
//}
