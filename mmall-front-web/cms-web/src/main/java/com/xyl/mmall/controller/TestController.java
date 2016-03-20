package com.xyl.mmall.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.framework.config.NkvConfiguration;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * Hello world!
 *
 */
@Controller
public class TestController
{

//	@Autowired
//	private DefaultExtendNkvClient defaultExtendNkvClient;
//	
//	@RequestMapping(value = "/nkv/get")
//	public @ResponseBody BaseJsonVO getFromNKV(@RequestParam(value = "uid") long uid,
//			@RequestParam(value = "skuId") long skuId) {
//		BaseJsonVO ret = new BaseJsonVO();
//		try {
////			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
////					("Test_" + key).getBytes(), new NkvOption(5000l, (short) 0));
//			Result<byte[]> result = defaultExtendNkvClient.hget(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE, 
//					("Test_" + skuId).getBytes(), Long.toString(uid).getBytes("UTF-8"), new NkvOption(5000l));
//			if (result != null && ResultCode.OK.equals(result.getCode())) {
//				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "ok!");
//				ret.setResult(Integer.parseInt(new String(result.getResult())));
//			} else {
//				ret.setCodeAndMessage(ResponseCode.RES_ERROR, "fail!");
//			}
//		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
//			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "error!");
//			e.printStackTrace();
//		}
//		return ret;
//	}
//	
//	@RequestMapping(value = "/nkv/add")
//	public @ResponseBody BaseJsonVO addToNKV(@RequestParam(value = "uid") long uid, 
//			@RequestParam(value = "skuId") long skuId, 
//			@RequestParam(value = "value") int value) {
//		BaseJsonVO ret = new BaseJsonVO();
//		try {
//			Result<Long> result = defaultExtendNkvClient.hincrby(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE, 
//					("Test_" + skuId).getBytes(), Long.toString(uid).getBytes("UTF-8"), value,
//					new NkvOption(5000l, (short)0, 15));
////			Result<Integer> result = defaultExtendNkvClient.incr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
////					("Test_" + key).getBytes(), value, 0, new NkvOption(5000l, (short) 0, 10));
//			if (result != null && ResultCode.OK.equals(result.getCode())) {
//				ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "ok!");
//			} else {
//				ret.setCodeAndMessage(ResponseCode.RES_ERROR, "fail!");
//			}
//		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "error!");
//		}
//		return ret;
//	}
	
//	@Autowired
//	private LeftNavigationFacade leftNavigationFacade;
//	
//	@Autowired
//	private BusinessFacade businessFacade;
//	
//	@RequestMapping(value ="/businessDao", method = RequestMethod.GET)
//	public String content(Model model){
//		List<Business> list = businessFacade.getBusinessByAccount("vt");
//		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//      return "pages/content/spread";
//  }
//	
////	@RequestMapping(value ="/content", method = RequestMethod.GET)
////    public String content(Model model){
////        return "pages/content/spread";
////    }
////	@RequestMapping(value ="/content/spread", method = RequestMethod.GET)
////    public String contentSpread(Model model){
////        return "pages/content/spread";
////    }
////    @RequestMapping(value ="/content/note", method = RequestMethod.GET)
////    public String contentNote(Model model){
////        return "pages/content/note";
////    }
////    @RequestMapping(value ="/schedule", method = RequestMethod.GET)
////    public String schedule(Model model){
////        return "pages/schedule/list";
////    }
////    @RequestMapping(value ="/schedule/create", method = RequestMethod.GET)
////    public String scheduleCreate(Model model){
////        return "pages/schedule/create";
////    }
////    @RequestMapping(value ="/schedule/calendar", method = RequestMethod.GET)
////    public String scheduleCalendar(Model model){
////        return "pages/schedule/calendar";
////    }
////
////    @RequestMapping(value ="/schedule/audit", method = RequestMethod.GET)
////    public String scheduleAudit(Model model){
////        return "pages/schedule/audit";
////    }
////    @RequestMapping(value ="/schedule/passed", method = RequestMethod.GET)
////    public String schedulePassed(Model model){
////        return "pages/schedule/passed";
////    }
////    @RequestMapping(value ="/schedule/exhibition", method = RequestMethod.GET)
////    public String scheduleExhibition(Model model){
////        return "pages/schedule/exhibition";
////    }
////    @RequestMapping(value ="/schedule/place", method = RequestMethod.GET)
////    public String schedulePlace(Model model){
////        return "pages/schedule/place";
////    }
////    @RequestMapping(value ="/schedule/return", method = RequestMethod.GET)
////    public String scheduleReture(Model model){
////        return "pages/schedule/return";
////    }
////    @RequestMapping(value ="/schedule/returnlist", method = RequestMethod.GET)
////    public String scheduleReturelist(Model model){
////        return "pages/schedule/returnlist";
////    }
////    @RequestMapping(value ="/schedule/returndetail", method = RequestMethod.GET)
////    public String scheduleReturndetail(Model model){
////        return "pages/schedule/returndetail";
////    }
////    @RequestMapping(value ="/promotion", method = RequestMethod.GET)
////    public String promotion(Model model){
////        return "pages/promotion/activity";
////    }
////    @RequestMapping(value ="/promotion/activity", method = RequestMethod.GET)
////    public String promotionActivity(Model model){
////        return "pages/promotion/activity";
////    }
////    @RequestMapping(value ="/promotion/coupon", method = RequestMethod.GET)
////    public String promotionCoupon(Model model){
////        return "pages/promotion/coupon";
////    }
////    @RequestMapping(value ="/promotion/packet", method = RequestMethod.GET)
////    public String promotionPacket(Model model){
////        return "pages/promotion/packet";
////    }
////    @RequestMapping(value ="/promotion/activityEdit", method = RequestMethod.GET)
////    public String promotionActivityEdit(Model model){
////        return "pages/promotion/activityEdit";
////    }
////    @RequestMapping(value ="/promotion/couponEdit", method = RequestMethod.GET)
////    public String promotionCouponEdit(Model model){
////        return "pages/promotion/couponEdit";
////    }
////    @RequestMapping(value ="/promotion/packetEdit", method = RequestMethod.GET)
////    public String promotionPacketEdit(Model model){
////        return "pages/promotion/packetEdit";
////    }
////    @RequestMapping(value ="/order", method = RequestMethod.GET)
////    public String order(Model model){
////        return "pages/order/query";
////    }
//    @RequestMapping(value ="/order/orderlist", method = RequestMethod.GET)
//    public String orderList(Model model){
//    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//        return "pages/order/order.list";
//    }
////    @RequestMapping(value ="/order/orderdetail", method = RequestMethod.GET)
////    public String orderDetail(Model model){
////        return "pages/order/order.detail";
////    }
////    @RequestMapping(value ="/order/tradedetail", method = RequestMethod.GET)
////    public String tradeDetail(Model model){
////        return "pages/order/trade.detail";
////    }
////    @RequestMapping(value ="/order/query", method = RequestMethod.GET)
////    public String orderQuery(Model model){
////        return "pages/order/query";
////    }
////    @RequestMapping(value ="/order/topay", method = RequestMethod.GET)
////    public String orderTopay(Model model){
////        return "pages/order/topay";
////    }
////    @RequestMapping(value ="/order/refund", method = RequestMethod.GET)
////    @RequiresPermissions(value = { "order:refund" })
////    public String orderRefund(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/order/refund";
////
////    }
////    @RequestMapping(value ="/order/return", method = RequestMethod.GET)
////    @RequiresPermissions(value = { "order:return" })
////    public String orderReturn(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/order/return";
////    }
////    @RequestMapping(value ="/order/returndetail", method = RequestMethod.GET)
////    public String orderReturnDetail(Model model){
////        return "pages/order/return.detail";
////    }
////    @RequestMapping(value ="/order/returnstore", method = RequestMethod.GET)
////    @RequiresPermissions(value = { "order:returnstore" })
////    public String orderReturnStore(Model model){
////        return "pages/order/return.store";
////    }
//    @RequestMapping(value ="/order/sell", method = RequestMethod.GET)
//    @RequiresPermissions(value = { "order:sell" })
//    public String orderSell(Model model){
//    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//        return "pages/order/sell";
//    }
////    @RequestMapping(value ="/finance", method = RequestMethod.GET)
////    public String finance(Model model){
////        return "pages/finance/salequery";
////    }
////    @RequestMapping(value ="/finance/salequery", method = RequestMethod.GET)
////	@RequiresPermissions(value = { "finance:salequery" })
////    public String financeSalequery(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/finance/salequery";
////    }
////    @RequestMapping(value ="/finance/firstpay", method = RequestMethod.GET)
////	@RequiresPermissions(value = { "finance:salequery" })
////    public String financeFirstPay(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/finance/first.pay";
////    }
////    @RequestMapping(value ="/finance/fullpay", method = RequestMethod.GET)
////	@RequiresPermissions(value = { "finance:salequery" })
////    public String financeFullPay(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/finance/full.pay";
////    }
//    @RequestMapping(value ="/finance/return", method = RequestMethod.GET)
//	@RequiresPermissions(value = { "finance:return" })
//    public String financeReturn(Model model){
//    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//        return "pages/finance/return";
//    }
//
////    @RequestMapping(value ="/user", method = RequestMethod.GET)
////    public String user(Model model){
////        return "pages/user/user";
////    }
////    @RequestMapping(value ="/user/info", method = RequestMethod.GET)
////	@RequiresPermissions(value = { "user:info" })
////    public String userInfo(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/user/info";
////    }
////    @RequestMapping(value ="/user/userlist", method = RequestMethod.GET)
////    public String userList(Model model){
////        return "pages/user/userlist";
////    }
////    @RequestMapping(value ="/business", method = RequestMethod.GET)
////    public String business(Model model){
////        return "pages/business/account";
////    }
//   
////    @RequestMapping(value ="/business/brand", method = RequestMethod.GET)
////    public String businessBrand(Model model){
////        return "pages/business/brand";
////    }
////    @RequestMapping(value ="/business/detail", method = RequestMethod.GET)
////    public String businessDetail(Model model){
////        return "pages/business/detail";
////    }
////    @RequestMapping(value ="/audit", method = RequestMethod.GET)
////    public String audit(Model model){
////        return "pages/audit/product";
////    }
////    @RequestMapping(value ="/audit/productlist", method = RequestMethod.GET)
////    public String auditProductList(Model model){
////        return "pages/audit/productlist";
////    }
////    @RequestMapping(value ="/audit/product", method = RequestMethod.GET)
////    public String auditProduct(Model model){
////        return "pages/audit/product";
////    }
////    @RequestMapping(value ="/audit/decorate", method = RequestMethod.GET)
////    public String auditDecorate(Model model){
////        return "pages/audit/decorate";
////    }
//    
//    @RequestMapping(value ="/audit/promotion", method = RequestMethod.GET)
//    public String auditPromotion(Model model){
//    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//        return "pages/audit/promotion";
//    }
////    @RequestMapping(value ="/app", method = RequestMethod.GET)
////    public String app(Model model){
////        return "pages/app/list";
////    }
////    @RequestMapping(value ="/app/list", method = RequestMethod.GET)
////	@RequiresPermissions(value = { "app:list" })
////    public String appList(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/app/list";
////    }
////    @RequestMapping(value ="/app/add", method = RequestMethod.GET)
////    @RequiresPermissions(value = { "app:add" })
////    public String appAdd(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/app/add";
////    }
////    @RequestMapping(value ="/message", method = RequestMethod.GET)
////    public String message(Model model){
////        return "pages/message";
////    }
//    @RequestMapping(value ="/message/list", method = RequestMethod.GET)
//    @RequiresPermissions(value = { "message:list" })
//    public String messageList(Model model){
//    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//        return "pages/message/list";
//    }
//    @RequestMapping(value ="/message/detail", method = RequestMethod.GET)
//    public String messageDetail(Model model){
//    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//        return "pages/message/detail";
//    }
////    @RequestMapping(value ="/focuspicture", method = RequestMethod.GET)
////    public String focusPicture(Model model){
////        return "pages/focuspicture/manage";
////    }
////    @RequestMapping(value ="/focuspicture/manage", method = RequestMethod.GET)
////    @RequiresPermissions(value = { "focuspicture:manage" })
////    public String focusPictureMnage(Model model){
////    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
////        return "pages/focuspicture/manage";
////    }
//    
}
