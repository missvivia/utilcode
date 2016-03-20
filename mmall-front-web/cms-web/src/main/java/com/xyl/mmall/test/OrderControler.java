package com.xyl.mmall.test;
//package com.xyl.mmall.test;
//
//import java.io.IOException;
//
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.testng.log4testng.Logger;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.netease.print.common.meta.RetArg;
//import com.netease.print.common.util.RetArgUtil;
//import com.netease.print.security.util.SecurityContextUtils;
//import com.xyl.mmall.cms.facade.LeftNavigationFacade;
//import com.xyl.mmall.cms.facade.OrderQueryFacade;
//import com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam;
//import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO;
//import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.OrderCategory;
//import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.TimeRangeCategory;
//import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.UserCategory;
//import com.xyl.mmall.cms.vo.order.OrderDetailInfoVO;
//import com.xyl.mmall.cms.vo.order.OrderPackageExpInfoVO;
//import com.xyl.mmall.cms.vo.order.OrderPackageSkuInfoVO;
//import com.xyl.mmall.cms.vo.order.TradeDetailInfoVO;
//import com.xyl.mmall.controller.BaseController;
//import com.xyl.mmall.framework.vo.BaseJsonVO;
//import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
//import com.xyl.mmall.order.enums.OrderCancelRType;
//
///**
// * 订单管理：订单查询
// * 
// * @author hzwangjianyi@corp.netease.com
// * @create 2014年9月29日 上午11:05:05
// *
// */
//@Controller
//@RequestMapping("/test/order")
//public class OrderControler extends BaseController {
//	
//	private static final Logger logger = Logger.getLogger(OrderControler.class);
//
//	@Autowired
//	private OrderQueryFacade cmsOrderQueryFacade;
//	
//	@Autowired
//	private MobilePushManageFacade mobilePushManageFacade;
//	
//	@Autowired
//	private LeftNavigationFacade leftNavigationFacade;
//	
//	/**
//	 * 请求参数Json模板数据
//	 * 
//	 * @param tag
//	 * @return
//	 */
//	@RequestMapping(value = "/sample/order", method = RequestMethod.GET)
//	@ResponseBody 
//	public Object sampleJson(@RequestParam(value="tag") int tag)  {
//		return OrderJsonDeserializor.smapleJson(tag);
//	}
//	
//	/**
//	 * 订单查询：类型列表
//	 * @return
//	 */
//	@RequestMapping(value = {"/query"}, method = RequestMethod.GET)
//	@RequiresPermissions(value = { "order:query" })
//	public String getTypeList(Model model) {
//		appendStaticMethod(model);
//		OrderQueryCategoryListVO vo = cmsOrderQueryFacade.getCmsOrderQueryTypeList();
//		model.addAttribute("data", vo);
//		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//		return "pages/order/query";
//	}
//	
//	/**
//	 * 订单查询：按订单/交易查询
//	 * @return
//	 */
//	@RequestMapping(value = "/tradedetail", method = RequestMethod.GET)
//	public String queryTrade(
//			Model model, 
//			@RequestParam(value="tradeId") long tradeId, 
//			@RequestParam(value="orderId") long orderId, 
//			@RequestParam(value="userId") long userId, 
//			@RequestParam(value="userName") String userName
//			) {
//		appendStaticMethod(model);
//		TradeDetailInfoVO tdiVO = cmsOrderQueryFacade.queryTrade(tradeId, orderId, userId, userName);
//		model.addAttribute("data", tdiVO);
//		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//		return "pages/order/trade.detail";
//	}
//	
//	/**
//	 * 交易相关的Order
//	 * @return
//	 */
//	@RequestMapping(value = "/query/getTradeList", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO queryTradeOrder(
//			Model model, 
//			@RequestParam(value="tradeId") long tradeId, 
//			@RequestParam(value="orderId") long orderId, 
//			@RequestParam(value="userId") long userId
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		ret.setMessage("successful");
//		ret.setResult(cmsOrderQueryFacade.queryTradeOrderInfo(userId, orderId));
//		return ret;
//	}
//	
//	/**
//	 * 订单查询：按用户信息查询
//	 * @return
//	 */
//	@RequestMapping(value = "/query/getOrderList", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO queryObjectByUserInfo(
//			Model model, 
//			@RequestParam(value="type") int typeId, 
//			@RequestParam(value="key") String value, 
//			@RequestParam(value="limit") int limit, 
//			@RequestParam(value="offset") int offset
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		ret.setMessage("successful");
//		UserCategory ut = UserCategory.USER_ID.genEnumByIntValue(typeId);
//		if(null == ut) {
//			ret.setCode(201);
//			ret.setMessage("illegal type " + typeId);
//			return ret;
//		}
//		ret.setResult(cmsOrderQueryFacade.queryByUserInfo(ut, value, limit, offset));
//		return ret;
//	}
//	
//	/**
//	 * 订单查询：按用时间范围查询
//	 * @return
//	 */
//	@RequestMapping(value = "/query/getOrderList2", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO queryObjectByTimeRange(
//			Model model, 
//			@RequestParam(value="type") int typeId, 
//			@RequestParam(value="start") long start, 
//			@RequestParam(value="end") long end, 
//			@RequestParam(value="limit") int limit, 
//			@RequestParam(value="offset") int offset
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		ret.setMessage("successful");
//		TimeRangeCategory tr = TimeRangeCategory.NOT_LACK.genEnumByIntValue(typeId);
//		if(null == tr) {
//			ret.setCode(201);
//			ret.setMessage("illegal type " + typeId);
//			return ret;
//		}
//		ret.setResult(cmsOrderQueryFacade.queryByTimeRange(tr, start, end, limit, offset));
//		return ret;
//	}
//	
//	/**
//	 * CMS订单查询：订单是否存在
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "/query/check", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO queryCheck(Model model, 
//			@RequestParam(value="type") int typeId, 
//			@RequestParam(value="value") String value
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		OrderCategory ott = OrderCategory.getOrderTradeType(typeId);
//		if(null == ott) {
//			ret.setCode(201);
//			ret.setResult(false);
//			ret.setMessage("illegal type:" + typeId);
//			return ret;
//		}
//		boolean exist = cmsOrderQueryFacade.orderExists(ott, value);
//		if(exist) {
//			ret.setCode(200);
//			ret.setMessage("order exists.");
//		} else {
//			ret.setCode(202);
//			ret.setMessage("order dose not exist.");
//		}
//		ret.setResult(exist);
//		return ret;
//	}
//	
//	/**
//	 * CMS订单详情：基本信息 + 交易信息
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "/orderdetail", method = RequestMethod.GET)
//	@RequiresPermissions(value = { "order:query" })
//	public String queryOrderInfo(Model model, 
//			@RequestParam(value="type") int typeId, 
//			@RequestParam(value="key") String value
//			) {
//		appendStaticMethod(model);
//		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//		OrderCategory ott = OrderCategory.getOrderTradeType(typeId);
//		if(null == ott) {
//			model.addAttribute("data", null);
//			return "pages/order/order.detail";
//		}
//		OrderDetailInfoVO vo = cmsOrderQueryFacade.queryOrderDetailInfo(ott, value);
//		model.addAttribute("data", vo);
//		return "pages/order/order.detail";
//	}
//	
//	/**
//	 * CMS订单详情：配送信息
//	 * 
//	 * @param model
//	 * @param typeId
//	 * @param value
//	 * @return
//	 */
//	@RequestMapping(value = "/query/packlist", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO queryPackageExpInfo(
//			Model model, 
//			@RequestParam(value="type") int typeId, 
//			@RequestParam(value="key") String value
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		ret.setMessage("successful");
//		OrderCategory ott = OrderCategory.getOrderTradeType(typeId);
//		if(null == ott) {
//			ret.setCode(201);
//			ret.setMessage("illegal type " + typeId);
//			return ret;
//		}
//		OrderPackageExpInfoVO vo = cmsOrderQueryFacade.queryOrderPackageExpInfo(ott, value);
//		if(null == vo) {
//			ret.setCode(202);
//			ret.setMessage("no order found for <" + typeId + ", " + value + ">");
//			return ret;
//		}
//		ret.setResult(vo);
//		return ret;
//	}
//
//	/**
//	 * CMS订单详情：商品信息
//	 * 
//	 * @param model
//	 * @param typeId
//	 * @param value
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderDetailList", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO queryPackageSkuInfo(
//			Model model, 
//			@RequestParam(value="type") int typeId, 
//			@RequestParam(value="key") String value
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		ret.setMessage("successful");
//		OrderCategory ott = OrderCategory.getOrderTradeType(typeId);
//		if(null == ott) {
//			ret.setCode(201);
//			ret.setMessage("illegal type " + typeId);
//			return ret;
//		}
//		OrderPackageSkuInfoVO vo = cmsOrderQueryFacade.queryOrderPackageSkuInfo(ott, value);
//		if(null == vo) {
//			ret.setCode(202);
//			ret.setMessage("no order found for <" + typeId + ", " + value + ">");
//			return ret;
//		}
//		ret.setResult(vo);
//		return ret;
//	}
//	
//	/**
//	 * 修改收货人地址
//	 * 
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/query/orderdetail/setAddress", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO updateOrderExpInfo(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontOrderExpInfoUpdateParam param = OrderJsonDeserializor.decodeFrontOrderExpInfoUpdateParam(jsonParam);
//		BaseJsonVO ret = new BaseJsonVO();
//		boolean result = cmsOrderQueryFacade.updateOrderExpInfo(param);
//		if(result) {
//			ret.setCode(200);
//			ret.setMessage("successful");
//		} else {
//			ret.setCode(201);
//			ret.setMessage("failed");
//		}
//		ret.setResult(result);
//		return ret;
//	}
//	
//	/**
//	 * 取消订单
//	 * 
//	 * @param model
//	 * @param orderId
//	 * @param userId
//	 * @param rtype
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderdetail/delete", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO cancelOrder(
//			Model model, 
//			@RequestParam(value="orderId") long orderId, 
//			@RequestParam(value="userId") long userId, 
//			@RequestParam(value="rtype") int rtype
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		OrderCancelRType ocrType = OrderCancelRType.ORI.genEnumByIntValue(rtype);
//		if(null == ocrType) {
//			ret.setCode(201);
//			ret.setMessage("illegal param rtype: " + rtype);
//			ret.setResult(false);
//			return ret;
//		}
//		boolean result = cmsOrderQueryFacade.cancelOrder(orderId, userId, ocrType);
//		if(result) {
//			ret.setCode(200);
//			ret.setMessage("successful");
//		} else {
//			ret.setCode(201);
//			ret.setMessage("failed");
//		}
//		ret.setResult(result);
//		return ret;
//	}
//	
//	/**
//	 * 补开发票
//	 * 
//	 * @param model
//	 * @param orderId
//	 * @param userId
//	 * @param title
//	 * @param associated
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderdetail/addInvoice", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO addInvoiceInOrd(
//			Model model, 
//			@RequestParam(value="orderId") long orderId, 
//			@RequestParam(value="userId") long userId, 
//			@RequestParam(value="title") String title, 
//			@RequestParam(value="associated") boolean associated
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		boolean result = cmsOrderQueryFacade.addInvoice(orderId, userId, title, associated);
//		if(result) {
//			ret.setCode(200);
//			ret.setMessage("successful");
//		} else {
//			ret.setCode(201);
//			ret.setMessage("failed");
//		}
//		ret.setResult(result);
//		return ret;
//	}
//	
//	/**
//	 * 修改发票
//	 * 
//	 * @param model
//	 * @param orderId
//	 * @param userId
//	 * @param title
//	 * @param associated
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderdetail/updateInvoice", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO updateInvoiceInOrd(
//			Model model, 
//			@RequestParam(value="orderId") long orderId, 
//			@RequestParam(value="userId") long userId, 
//			@RequestParam(value="title") String title, 
//			@RequestParam(value="associated") boolean associated
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		boolean result = cmsOrderQueryFacade.updateInvoice(orderId, userId, title, associated);
//		if(result) {
//			ret.setCode(200);
//			ret.setMessage("successful");
//		} else {
//			ret.setCode(201);
//			ret.setMessage("failed");
//		}
//		ret.setResult(result);
//		return ret;
//	}
//	
//	/**
//	 * 发票列表
//	 * 
//	 * @param model
//	 * @param orderId
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderdetail/getInvoiceList", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO getInvoiceList(
//			Model model, 
//			@RequestParam(value="orderId") long orderId, 
//			@RequestParam(value="userId") long userId
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		ret.setMessage("successful");
//		ret.setResult(cmsOrderQueryFacade.getInvoiceList(orderId, userId));
//		return ret;
//	}
//
//	/**
//	 * 客服重新打开退货
//	 * 
//	 * @param model
//	 * @param orderId
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderdetail/reopenreturn", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO kfReopenReturn(
//			Model model, 
//			@RequestParam(value="ordPkgId") long ordPkgId, 
//			@RequestParam(value="userId") long userId
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		RetArg retArg = cmsOrderQueryFacade.kfReopenReturn(ordPkgId, userId);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		boolean result = null != isSucc && Boolean.TRUE == isSucc; 
//		if(result) {
//			ret.setCode(200);
//			ret.setMessage("successful");
//		} else {
//			ret.setCode(201);
//			ret.setMessage(RetArgUtil.get(retArg, String.class));
//		}
//		ret.setResult(result);
//		return ret;
//	}
//	
//	/**
//	 * 设置包裹状态：丢包
//	 * 
//	 * @param model
//	 * @param orderId
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderdetail/setpkglost", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO setPackageLost(
//			Model model, 
//			@RequestParam(value="ordPkgId") long ordPkgId, 
//			@RequestParam(value="userId") long userId
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		RetArg retArg = cmsOrderQueryFacade.setPackageLost(ordPkgId, userId);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		boolean result = null != isSucc && Boolean.TRUE == isSucc; 
//		if(result) {
//			ret.setCode(200);
//			ret.setMessage("successful");
//		} else {
//			ret.setCode(201);
//			ret.setMessage(RetArgUtil.get(retArg, String.class));
//		}
//		ret.setResult(result);
//		return ret;
//	}
//	
//	/**
//	 * 设置包裹状态：拒收
//	 * 
//	 * @param model
//	 * @param orderId
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderdetail/setpkgrefused", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO setPackageRefused(
//			Model model, 
//			@RequestParam(value="ordPkgId") long ordPkgId, 
//			@RequestParam(value="userId") long userId
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		RetArg retArg = cmsOrderQueryFacade.setPackageRefused(ordPkgId, userId);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		boolean result = null != isSucc && Boolean.TRUE == isSucc; 
//		if(result) {
//			ret.setCode(200);
//			ret.setMessage("successful");
//		} else {
//			ret.setCode(201);
//			ret.setMessage(RetArgUtil.get(retArg, String.class));
//		}
//		ret.setResult(result);
//		return ret;
//	}
//	
//	/**
//	 * 设置包裹状态：取消
//	 * 
//	 * @param model
//	 * @param orderId
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping(value = "/query/orderdetail/cancelpkg", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO cancelPackage(
//			Model model, 
//			@RequestParam(value="ordPkgId") long ordPkgId, 
//			@RequestParam(value="userId") long userId
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		RetArg retArg = cmsOrderQueryFacade.cancelPackage(ordPkgId, userId);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		boolean result = null != isSucc && Boolean.TRUE == isSucc; 
//		if(result) {
//			ret.setCode(200);
//			ret.setMessage("successful");
//			try {
//				mobilePushManageFacade.push(userId, 11, "包裹取消", "包裹取消-超时未配送", ordPkgId);
//			} catch (Exception e) {
//				logger.info("mobilePushManageFacade.push(...) throws Exception", e);
//			}
//		} else {
//			ret.setCode(201);
//			ret.setMessage(RetArgUtil.get(retArg, String.class));
//		}
//		ret.setResult(result);
//		return ret;
//	}
//	
//}
