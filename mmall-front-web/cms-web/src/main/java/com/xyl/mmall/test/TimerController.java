package com.xyl.mmall.test;
//package com.xyl.mmall.test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.netease.print.common.meta.RetArg;
//import com.netease.print.common.util.RetArgUtil;
//import com.xyl.mmall.framework.vo.BaseJsonVO;
//import com.xyl.mmall.timer.facade.CODAuditTimerFacade;
//import com.xyl.mmall.timer.facade.ContentNCSTimerFacade;
//import com.xyl.mmall.timer.facade.OrderTimerFacade;
//import com.xyl.mmall.timer.facade.ReturnTimerFacade;
//
///**
// * 
// * @author hzwangjianyi@corp.netease.com
// *
// */
//@Controller
//@RequestMapping("/test")
//public class TimerController {
//
////-------- Test Start --------
//	@Autowired
//	private ReturnTimerFacade retTimerFacade;
//	
//	@Autowired
//	private OrderTimerFacade orderTimerFacade;
//	
//	@Autowired
//	private CODAuditTimerFacade codAuditTimerFacade;
//	
//	@Autowired
//	private ContentNCSTimerFacade contentNCSTimerFacade;
//	
//	@RequestMapping(value = "/timer", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO timerTest(
//			@RequestParam(value="category") String category, 
//			@RequestParam(value="tag") int tag
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		
//		if(null == category) {
//			return ret;
//		}
//		
//		RetArg retArg = null;
//		
//		if(category.equals("return")) {
//			switch(tag) {
//			case 0:
//				retArg = retTimerFacade.setReturnStateToCanceled();
//				break;
//			case 1:
//				retArg = retTimerFacade.pushReturnPackageToJIT();
//				break;
//			case 2:
//				retArg = retTimerFacade.recycleCoupon();
//				break;
//			case 3:
//				retArg = retTimerFacade.distributeReturnExpHb();
//				break;
//			case 4:
//				retArg = retTimerFacade.returnCash();
//				break;
//			case 5:
//				retArg = retTimerFacade.recycleHb();
//				break;
//			case 6:
//				retArg = orderTimerFacade.recycleCouponForOrderOfAllPackageCancelled();
//			default:
//				break;
//			}
//			if(null != retArg) {
//				ret.setResult(RetArgUtil.get(retArg, Boolean.class));
//				ret.setMessage(RetArgUtil.get(retArg, String.class));
//			}
//			return ret;
//		}
//		
//		if(category.equals("cod")) {
//			switch(tag) {
//			case 0:
//				retArg = codAuditTimerFacade.passCODAuditBeforeSomeTime();
//				break;
//			case 1:
//				retArg = codAuditTimerFacade.passCODAuditInWhiteList();
//				break;
//			case 2:
//				retArg = codAuditTimerFacade.cancelCODAuditOfTimeout();
//				break;
//			case 3:
//				retArg = codAuditTimerFacade.cancelIllegalCODAudit();
//				break;
//			default:
//				break;
//			}
//			if(null != retArg) {
//				ret.setResult(RetArgUtil.get(retArg, Boolean.class));
//				ret.setMessage(RetArgUtil.get(retArg, String.class));
//			}
//		}
//		
//		if(category.equals("content")) {
//			switch(tag) {
//			default:
//				retArg = contentNCSTimerFacade.dispatch();
//				break;
//			}
//			if(null != retArg) {
//				ret.setResult(RetArgUtil.get(retArg, Boolean.class));
//				ret.setMessage(RetArgUtil.get(retArg, String.class));
//			}
//		}
//		
//		return ret;
//	}
////-------- Test End --------
//	
//}
