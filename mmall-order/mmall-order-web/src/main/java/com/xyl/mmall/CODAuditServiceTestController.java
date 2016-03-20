package com.xyl.mmall;
//package com.xyl.mmall;
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
//import com.xyl.mmall.order.param.CODWBlistAddressParam;
//import com.xyl.mmall.order.service.CODAuditService;
//
///**
// * 
// * @author hzwangjianyi@corp.netease.com
// * @create 2014年9月17日 下午1:32:11
// *
// */
//@Controller()
//@RequestMapping("/cod_test")
//public class CODAuditServiceTestController {
//
////	private static final int USER_ID = 921;
////	private static final int ORDER_ID_223 = 223;
////	private static final int AUDIT_User_ID = 327;
////	
////	@Resource(name = "CODAuditService")
////	protected CODAuditService codAuditService;
////	
////	@RequestMapping(value = "/add", method = RequestMethod.GET)
////	public @ResponseBody Object addReturn(Model model, 
////			@RequestParam(value="tag") String tag) {
////		Object ret = null;
////		switch(tag) {
////		case "log":
////			ret = codAuditService.addCODAuditLog(ORDER_ID_223, USER_ID, AUDIT_User_ID);
////			break;
////		case "user":
////			ret = codAuditService.addToBlacklistUser(USER_ID, AUDIT_User_ID);
////			break;
////		case "address":
////			CODWBlistAddressParam param = new CODWBlistAddressParam();
////			param.setProvince("浙江");	param.setCity("杭州");
////			param.setSection("滨江");	param.setAddress("东方郡");
////			ret = codAuditService.addToBlacklistAddress(AUDIT_User_ID, param);
////			break;
////		default:
////			break;
////		}
////		return ret;
////	}
////	
////	@RequestMapping(value = "/update", method = RequestMethod.GET)
////	public @ResponseBody Object setReturn(
////			Model model, 
////			@RequestParam(value="tag") String tag, 
////			@RequestParam(value="auditLogId") int auditLogId, 
////			@RequestParam(value="extInfo") String extInfo
////			) {
////		Object obj = null;
////		switch(tag) {
////		case "pass":
////			return codAuditService.setCODAuditStateToPassed(auditLogId, AUDIT_User_ID);
////		case "refuse":
////			return codAuditService.setCODAuditStateToRefused(auditLogId, AUDIT_User_ID, extInfo);
////		case "cancel":
////			return codAuditService.cancelCODAuditStateToWaiting(auditLogId, AUDIT_User_ID);
////		default:
////			break;
////		}
////		return obj;
////	}
////	
////	@RequestMapping(value = "/get", method = RequestMethod.GET)
////	public @ResponseBody Object getReturn(Model model, 
////			@RequestParam(value="tag") String tag, 
////			@RequestParam(value="start") String start, 
////			@RequestParam(value="end") String end
////			) {
////		Object obj = null;
////		switch(tag) {
////		case "q7":
////			return codAuditService.queryAllCODAuditLog(null);
//////		case "qTime":
//////			return codAuditService.queryCODAuditLogWithTimeRange(start, end, null);
//////		case "qUser":
//////			return codAuditService.queryCODAuditLogWithUserId(USER_ID, null);
//////		case "qWait":
//////			return codAuditService.queryWaitingCODAuditLog(null);
//////		case "qPass":
//////		 	return codAuditService.queryPassedCODAuditLog(null);
//////		case "qRefuse":
//////		 	return codAuditService.queryRefusedCODAuditLog(null);
//////		case "qWaitTime":
//////			return codAuditService.queryWaitingCODAuditLogWithTimeRange(start, end, null);
//////		case "qPassTime":
//////		 	return codAuditService.queryPassedCODAuditLogWithTimeRange(start, end, null);
//////		case "qRefuseTime":
//////		 	return codAuditService.queryRefusedCODAuditLogWithTimeRange(start, end, null);
//////		case "qWaitUser":
//////			return codAuditService.queryWaitingCODAuditLogWithUserId(USER_ID, null);
//////		case "qPassUser":
//////		 	return codAuditService.queryPassedCODAuditLogWithUserId(USER_ID, null);
//////		case "qRefuseUser":
//////		 	return codAuditService.queryRefusedCODAuditLogWithUserId(USER_ID, null);
////		default:
////			break;
////		}
////		return obj;
////	}
//	
//	@Resource(name = "CODAuditService")
//	protected CODAuditService codAuditService;
//	@RequestMapping(value = "/timeout", method = RequestMethod.GET)
//	public @ResponseBody Object getReturn(Model model) {
//		return codAuditService.queryCODAuditLogOfTimeout(System.currentTimeMillis(), null);
//	}
//}
