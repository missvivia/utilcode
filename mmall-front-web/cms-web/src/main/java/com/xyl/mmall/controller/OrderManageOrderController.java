package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.log4testng.Logger;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.facade.AuthorityFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.OrderQueryFacade;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam;
import com.xyl.mmall.cms.vo.OrderOperateLogVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.OrderCategory;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.TimeRangeCategory;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.UserCategory;
import com.xyl.mmall.cms.vo.order.OrderDetailInfoVO;
import com.xyl.mmall.cms.vo.order.OrderPackageExpInfoVO;
import com.xyl.mmall.cms.vo.order.OrderPackageSkuInfoVO;
import com.xyl.mmall.cms.vo.order.TradeDetailInfoVO;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.out.facade.PayFacade;
import com.xyl.mmall.common.out.facade.SMSFacade;
import com.xyl.mmall.common.param.SMSParam;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.AreaCodeUtil;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.order.api.util.OrderCodeVersionUtil;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderLogisticsDTO;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.TradeInternalProxyService;

/**
 * 订单管理：订单查询
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午11:05:05
 *
 */
@Controller
@RequestMapping("/order")
public class OrderManageOrderController extends BaseController {

	private static final Logger logger = Logger.getLogger(OrderManageOrderController.class);

	@Autowired
	private OrderQueryFacade cmsOrderQueryFacade;

	@Autowired
	private OrderPackageSimpleService ordPkgSimpleService;

	@Autowired
	private MobilePushManageFacade mobilePushManageFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private TradeInternalProxyService tradeInternalProxyService;

	@Autowired
	private SiteCMSFacade siteCMSFacade;

	@Autowired
	private AuthorityFacade authorityFacade;

	@Autowired
	private SMSFacade smsFacade;

	@Autowired
	private PayFacade payFacade;

	/**
	 * order服务版本 (should be removed from production)
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/version" }, method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getVersion(Model model) {
		appendStaticMethod(model);
		BaseJsonVO ret = new BaseJsonVO();
		ret.setMessage(OrderCodeVersionUtil.getVersion());
		return ret;
	}

	/**
	 * 订单查询：类型列表
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/query" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:query" })
	public String getTypeList(Model model) {
		appendStaticMethod(model);
		OrderQueryCategoryListVO vo = cmsOrderQueryFacade.getCmsOrderQueryTypeList();
		model.addAttribute("data", vo);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/order/query";
	}

	/**
	 * 订单查询：按订单/交易查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/tradedetail", method = RequestMethod.GET)
	public String queryTrade(Model model, @RequestParam(value = "tradeId") long tradeId,
			@RequestParam(value = "orderId") long orderId, @RequestParam(value = "userId") long userId,
			@RequestParam(value = "userName") String userName) {
		appendStaticMethod(model);
		TradeDetailInfoVO tdiVO = cmsOrderQueryFacade.queryTrade(tradeId, orderId, userId, userName);
		model.addAttribute("data", tdiVO);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/order/trade.detail";
	}

	/**
	 * 交易相关的Order
	 * 
	 * @return
	 */
	@RequestMapping(value = "/query/getTradeList", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO queryTradeOrder(Model model, @RequestParam(value = "tradeId") long tradeId,
			@RequestParam(value = "orderId") long orderId, @RequestParam(value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ret.setMessage("successful");
		ret.setResult(cmsOrderQueryFacade.queryTradeOrderInfo(userId, orderId));
		return ret;
	}

	/**
	 * 订单查询：按用户信息查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/query/getOrderList", method = RequestMethod.GET)
	@ResponseBody
	@Deprecated
	public BaseJsonVO queryObjectByUserInfo(Model model, @RequestParam(value = "type") int typeId,
			@RequestParam(value = "key") String value, @RequestParam(value = "limit") int limit,
			@RequestParam(value = "offset") int offset) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ret.setMessage("successful");
		UserCategory ut = UserCategory.USER_ID.genEnumByIntValue(typeId);
		if (null == ut) {
			ret.setCode(201);
			ret.setMessage("illegal type " + typeId);
			return ret;
		}
		ret.setResult(cmsOrderQueryFacade.queryByUserInfo(ut, value, limit, offset));
		return ret;
	}

	/**
	 * 订单查询：按用时间范围查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/query/getOrderList2", method = RequestMethod.GET)
	@ResponseBody
	@Deprecated
	public BaseJsonVO queryObjectByTimeRange(Model model, @RequestParam(value = "type") int typeId,
			@RequestParam(value = "start") long start, @RequestParam(value = "end") long end,
			@RequestParam(value = "limit") int limit, @RequestParam(value = "offset") int offset) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ret.setMessage("successful");
		TimeRangeCategory tr = TimeRangeCategory.NOT_LACK.genEnumByIntValue(typeId);
		if (null == tr) {
			ret.setCode(201);
			ret.setMessage("illegal type " + typeId);
			return ret;
		}
		ret.setResult(cmsOrderQueryFacade.queryByTimeRange(tr, start, end, limit, offset));
		return ret;
	}

	/**
	 * CMS订单查询：订单是否存在
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/query/check", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO queryCheck(Model model, @RequestParam(value = "type") int typeId,
			@RequestParam(value = "value") String value) {
		BaseJsonVO ret = new BaseJsonVO();
		OrderCategory ott = OrderCategory.getOrderTradeType(typeId);
		if (null == ott) {
			ret.setCode(201);
			ret.setResult(false);
			ret.setMessage("illegal type:" + typeId);
			return ret;
		}
		boolean exist = cmsOrderQueryFacade.orderExists(ott, value);
		if (exist) {
			ret.setCode(200);
			ret.setMessage("order exists.");
		} else {
			ret.setCode(202);
			ret.setMessage("order dose not exist.");
		}
		ret.setResult(exist);
		return ret;
	}

	/**
	 * CMS订单详情：基本信息 + 交易信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/orderdetail", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:query" })
	public String queryOrderInfo(Model model, @RequestParam(value = "type") int typeId,
			@RequestParam(value = "key") String value) {
		appendStaticMethod(model);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		OrderCategory ott = OrderCategory.getOrderTradeType(typeId);
		if (null == ott) {
			model.addAttribute("data", null);
			return "pages/order/order.detail";
		}
		OrderDetailInfoVO vo = cmsOrderQueryFacade.queryOrderDetailInfo(ott, value);
		model.addAttribute("data", vo);
		return "pages/order/order.detail";
	}

	/**
	 * CMS订单详情：配送信息
	 * 
	 * @param model
	 * @param typeId
	 * @param value
	 * @return
	 */
	@RequestMapping(value = "/query/packlist", method = RequestMethod.GET)
	@ResponseBody
	@Deprecated
	public BaseJsonVO queryPackageExpInfo(Model model, @RequestParam(value = "type") int typeId,
			@RequestParam(value = "key") String value) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ret.setMessage("successful");
		OrderCategory ott = OrderCategory.getOrderTradeType(typeId);
		if (null == ott) {
			ret.setCode(201);
			ret.setMessage("illegal type " + typeId);
			return ret;
		}
		OrderPackageExpInfoVO vo = cmsOrderQueryFacade.queryOrderPackageExpInfo(ott, value);
		if (null == vo) {
			ret.setCode(202);
			ret.setMessage("no order found for <" + typeId + ", " + value + ">");
			return ret;
		}
		ret.setResult(vo);
		return ret;
	}

	/**
	 * CMS订单详情：商品信息
	 * 
	 * @param model
	 * @param typeId
	 * @param value
	 * @return
	 */
	@RequestMapping(value = "/query/orderDetailList", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO queryPackageSkuInfo(Model model, @RequestParam(value = "type") int typeId,
			@RequestParam(value = "key") String value) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ret.setMessage("successful");
		OrderCategory ott = OrderCategory.getOrderTradeType(typeId);
		if (null == ott) {
			ret.setCode(201);
			ret.setMessage("illegal type " + typeId);
			return ret;
		}
		OrderPackageSkuInfoVO vo = cmsOrderQueryFacade.queryOrderPackageSkuInfo(ott, value);
		if (null == vo) {
			ret.setCode(202);
			ret.setMessage("no order found for <" + typeId + ", " + value + ">");
			return ret;
		}
		ret.setResult(vo);
		return ret;
	}

	/**
	 * 修改收货人地址
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/query/orderdetail/setAddress", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO updateOrderExpInfo(Model model, @RequestBody FrontOrderExpInfoUpdateParam param) {
		BaseJsonVO ret = new BaseJsonVO();
		long loginId = SecurityContextUtils.getUserId();
		param.getChgParam().setOperatorId(loginId);
		param.getChgParam().setOperateUserType(OperateUserType.CMSER);
		;
		int result = cmsOrderQueryFacade.updateOrderExpInfo(param);
		if (result > 0) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage("failed");
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 取消订单
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @param rtype
	 * @return
	 */
	@RequestMapping(value = "/query/orderdetail/delete", method = RequestMethod.GET)
	@ResponseBody
	@Deprecated
	public BaseJsonVO cancelOrder(Model model, @RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "userId") long userId, @RequestParam(value = "rtype") int rtype) {
		BaseJsonVO ret = new BaseJsonVO();
		OrderCancelRType ocrType = OrderCancelRType.ORI.genEnumByIntValue(rtype);
		if (null == ocrType) {
			ret.setCode(201);
			ret.setMessage("illegal param rtype: " + rtype);
			ret.setResult(false);
			return ret;
		}
		boolean result = cmsOrderQueryFacade.cancelOrder(orderId, userId, ocrType);
		if (result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage("failed");
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 补开发票
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @param title
	 * @param associated
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/query/orderdetail/addInvoice", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO addInvoiceInOrd(Model model, @RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "userId") long userId, @RequestParam(value = "title") String title,
			@RequestParam(value = "associated") boolean associated) {
		BaseJsonVO ret = new BaseJsonVO();
		boolean result = cmsOrderQueryFacade.addInvoice(orderId, userId, title, associated);
		if (result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage("failed");
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 修改发票
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @param title
	 * @param associated
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/query/orderdetail/updateInvoice", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO updateInvoiceInOrd(Model model, @RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "userId") long userId, @RequestParam(value = "title") String title,
			@RequestParam(value = "associated") boolean associated) {
		BaseJsonVO ret = new BaseJsonVO();
		boolean result = cmsOrderQueryFacade.updateInvoice(orderId, userId, title, associated);
		if (result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage("failed");
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 发票列表
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/query/orderdetail/getInvoiceList", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getInvoiceList(Model model, @RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ret.setMessage("successful");
		ret.setResult(cmsOrderQueryFacade.getInvoiceList(orderId, userId));
		return ret;
	}

	/**
	 * 客服重新打开退货
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/query/orderdetail/reopenreturn", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO kfReopenReturn(Model model, @RequestParam(value = "ordPkgId") long ordPkgId,
			@RequestParam(value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		RetArg retArg = cmsOrderQueryFacade.kfReopenReturn(ordPkgId, userId);
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		if (result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage(RetArgUtil.get(retArg, String.class));
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 设置包裹状态：丢包
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/query/orderdetail/setpkglost", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO setPackageLost(Model model, @RequestParam(value = "ordPkgId") long ordPkgId,
			@RequestParam(value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		RetArg retArg = cmsOrderQueryFacade.setPackageLost(ordPkgId, userId);
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		if (result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage(RetArgUtil.get(retArg, String.class));
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 设置包裹状态：拒收
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/query/orderdetail/setpkgrefused", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO setPackageRefused(Model model, @RequestParam(value = "ordPkgId") long ordPkgId,
			@RequestParam(value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		RetArg retArg = cmsOrderQueryFacade.setPackageRefused(ordPkgId, userId);
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		if (result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage(RetArgUtil.get(retArg, String.class));
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 设置包裹状态：取消
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/query/orderdetail/cancelpkg", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO cancelPackage(Model model, @RequestParam(value = "ordPkgId") long ordPkgId,
			@RequestParam(value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		RetArg retArg = cmsOrderQueryFacade.cancelPackage(ordPkgId, userId);
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		if (result) {
			ret.setCode(200);
			ret.setMessage("successful");
			try {
				OrderPackageSimpleDTO ordPkg = ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
				if (null != ordPkg) {
					mobilePushManageFacade.push(userId, 11, null, null, ordPkg.getOrderId());
				}
			} catch (Exception e) {
				logger.info("mobilePushManageFacade.push(...) throws Exception", e);
			}
		} else {
			ret.setCode(201);
			ret.setMessage(RetArgUtil.get(retArg, String.class));
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 设置包裹状态：已签收
	 * 
	 * @param model
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/query/orderdetail/consignpkg", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO consignPackage(Model model, @RequestParam(value = "ordPkgId") long ordPkgId,
			@RequestParam(value = "userId") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		RetArg retArg = cmsOrderQueryFacade.consignPackage(ordPkgId, userId);
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		if (result) {
			ret.setCode(200);
			ret.setMessage("successful");
		} else {
			ret.setCode(201);
			ret.setMessage(RetArgUtil.get(retArg, String.class));
		}
		ret.setResult(result);
		return ret;
	}

	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param range
	 *            1全部，2一周内，3一个月内
	 * @return
	 */
	@RequestMapping(value = "/operateLog", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:query" })
	public @ResponseBody BaseJsonVO queryOrderOperateLong(OrderOperateLogVO operateLogVO, long startTime, long endTime,
			int range) {
		BaseJsonVO ret = new BaseJsonVO();
		if (operateLogVO.getCmsOrderId() < 1l || startTime < 0l || endTime < 0l) {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		// 判断时间范围
		String startTimeStr = null;
		String endTimeStr = null;
		if (range == 1) {
			// 全部
			if (startTime > 0l) {
				startTimeStr = DateUtil.dateToString(new Date(startTime), DateUtil.LONG_PATTERN);
			}
			if (endTime > 0l) {
				endTimeStr = DateUtil.dateToString(new Date(endTime), DateUtil.LONG_PATTERN);
			}
		} else if (range == 2) {
			// 一周内
			Date now = new Date();
			startTimeStr = DateUtil.getDateBegin(DateUtil.dateToSimpleString(DateUtil.dateAddWeeks(now, -1)));
			endTimeStr = DateUtil.dateToString(now, DateUtil.LONG_PATTERN);
		} else if (range == 3) {
			Date now = new Date();
			startTimeStr = DateUtil.getDateBegin(DateUtil.dateToSimpleString(DateUtil.dateAddMonths(now, -1)));
			endTimeStr = DateUtil.dateToString(now, DateUtil.LONG_PATTERN);

		} else {
			return setCodeAndMessage(ret, ResponseCode.RES_EPARAM, "参数错误！");
		}
		OrderOperateLogDTO operateLogDTO = operateLogVO.convertToDTO();
		ret.setResult(cmsOrderQueryFacade.queryOperateLog(operateLogDTO, startTimeStr, endTimeStr));
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}

	private BaseJsonVO setCodeAndMessage(BaseJsonVO ret, int code, String message) {
		ret.setCode(code);
		ret.setMessage(message);
		return ret;
	}

	/**
	 * 订单查询：按用户名,下单时间，订单Id查询
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query/getOrderListByQueryType", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:query" })
	@ResponseBody
	public BaseJsonVO queryOrder(OrderSearchParam orderSearchParam) {
		BaseJsonVO ret = new BaseJsonVO();
		long userId = SecurityContextUtils.getUserId();
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		if (retArg == null) {
			ret.setCode(ResponseCode.RES_FORBIDDEN);
			ret.setMessage("没有区域或区域查询权限!");
			return ret;
		}
		if (!RetArgUtil.get(retArg, Boolean.class)) {
			orderSearchParam.setSiteAreaList(new ArrayList<Long>(RetArgUtil.get(retArg, HashSet.class)));
		}
		if (StringUtils.isEmpty(orderSearchParam.getOrderColumn())) {
			orderSearchParam.setOrderColumn("CreateTime");
			orderSearchParam.setAsc(false);
		}
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setMessage("successful");
		ret.setResult(cmsOrderQueryFacade.queryOrderByOrderSearchParam(orderSearchParam));
		return ret;
	}

	/**
	 * 根据parentId取订单Ids
	 * 
	 * @param model
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/getSubOrderIds", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:query" })
	public BaseJsonVO querySubOrderIds(@RequestParam(value = "parentId") long parentId) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(orderFacade.getSubOrderIds(parentId));
		return ret;
	}

	/**
	 * CMS订单详情：基本信息 + 交易信息
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getorderdetail", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:query" })
	public String queryOrderInfo(Model model, @RequestParam(value = "orderId") long orderId) {
		appendStaticMethod(model);
		long userId = SecurityContextUtils.getUserId();
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(userId));
		OrderDetailInfoVO vo = cmsOrderQueryFacade.queryOrderDetailInfoByOrderId(orderId);
		if (vo == null || vo.getBasicInfo() == null) {
			return "pages/noSearchResult";// 没查询结果
		}
		boolean isPermission = true;
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		long cityCode = AreaCodeUtil.getCityCode(String.valueOf(vo.getBasicInfo().getDistrictId()));
		if (retArg == null) {
			isPermission = false;
		} else if (!RetArgUtil.get(retArg, Boolean.class)) {
			Set<Long> areaIdSet = RetArgUtil.get(retArg, HashSet.class);
			for (Long areaId : areaIdSet) {
				if (areaId == cityCode) {
					isPermission = true;
					break;
				}
				isPermission = false;
			}
		}
		if (!isPermission) {
			model.addAttribute("username", SecurityContextUtils.getUserName());// username
																				// 403页面显示有用到
			return "error/403";// 没权限
		}
		model.addAttribute("data", vo);
		model.addAttribute("expressCompany", ExpressCompany.validValues());
		return "pages/order/orderdetail";
	}

	/**
	 * 新增发票
	 * 
	 * @param invoiceDTO
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/addInvoice", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:operate" })
	@ResponseBody
	public BaseJsonVO addInvoice(@RequestBody InvoiceDTO invoiceDTO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isEmpty(invoiceDTO.getTitle()) || StringUtils.isEmpty(invoiceDTO.getInvoiceNo())) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("参数有误,抬头或者发票号为空！");
			return ret;
		}
		long loginId = SecurityContextUtils.getUserId();
		invoiceDTO.setCreateBy(loginId);
		invoiceDTO.setUpdateBy(loginId);
		invoiceDTO.setOperateUserType(OperateUserType.CMSER);
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

	/**
	 * 修改发票
	 * 
	 * @param invoiceDTO
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/updateInvoice", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:operate" })
	@ResponseBody
	public BaseJsonVO updateInvoice(@RequestBody InvoiceDTO invoiceDTO) {
		BaseJsonVO ret = new BaseJsonVO();
		if (invoiceDTO.getState() == null || invoiceDTO.getId() <= 0) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("参数有误,发票状态为空或者发票ID没传入！");
			return ret;
		}
		long loginId = SecurityContextUtils.getUserId();
		invoiceDTO.setUpdateBy(loginId);
		invoiceDTO.setOperateUserType(OperateUserType.CMSER);
		;
		int result = orderFacade.updateInvoice(invoiceDTO);
		if (result > 0) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("修改发票成功！");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("修改发票失败！");
		}
		return ret;
	}

	/**
	 * 新增物流或编辑
	 * 
	 * @param orderId
	 */
	@RequestMapping(value = "/addOrUpdateOrderLogistics", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:operate" })
	public @ResponseBody BaseJsonVO addOrUpdateOrderLogistics(@RequestBody OrderLogisticsDTO orderLogisticsDTO) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		if (!checkOrderLogisticsDTO(orderLogisticsDTO)) {
			baseJsonVO.setCode(ResponseCode.RES_EPARAM);
			baseJsonVO.setMessage("参数有误！订单Id,快递号,商家Id,快递必须有值！");
			return baseJsonVO;
		}
		long userId = SecurityContextUtils.getUserId();
		orderLogisticsDTO.setCreateBy(userId);
		orderLogisticsDTO.setUpdateBy(userId);
		orderLogisticsDTO.setOperateUserType(OperateUserType.CMSER);
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

	private boolean checkOrderLogisticsDTO(OrderLogisticsDTO orderLogisticsDTO) {
		if (orderLogisticsDTO.getBusinessId() <= 0 || orderLogisticsDTO.getOrderId() <= 0) {
			return false;
		}
		// 编辑时
		if (orderLogisticsDTO.getId() > 0) {
			if (StringUtils.isEmpty(orderLogisticsDTO.getMailNO()) && orderLogisticsDTO.getExpressCompany() == null) {
				return false;
			}
		} else {
			// 新增时
			if (StringUtils.isEmpty(orderLogisticsDTO.getMailNO()) || orderLogisticsDTO.getExpressCompany() == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 修改金额
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/updateCash", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:operate" })
	public @ResponseBody BaseJsonVO updateSalePrice(Model model, @RequestBody OrderOperateParam param) {
		long loginId = SecurityContextUtils.getUserId();
		param.setAgentId(loginId);
		param.setOperateUserType(OperateUserType.CMSER);
		;
		BaseJsonVO ret = new BaseJsonVO();
		int result = tradeInternalProxyService.modifyTradeCash(param);
		if (result > 0) {
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("修改金额成功！");
		} else {
			ret.setMessage("修改金额失败！");
			ret.setCode(ResponseCode.RES_ERROR);
		}
		return ret;
	}

	/**
	 * 修改订单状态
	 * 
	 * @param orderId
	 */
	@BILog(action = "click", clientType = "order", type = "skuSellStatisticsCMS")
	@RequestMapping(value = "/orderdetail/modifyOrderState", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:operate" })
	public @ResponseBody BaseJsonVO modifyOrderState(@RequestBody OrderOperateParam param) {
		long loginId = SecurityContextUtils.getUserId();
		param.setAgentId(loginId);
		param.setOperateUserType(OperateUserType.CMSER);
		int result = 0;
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		if (param.getNewState().equals(OrderFormState.CANCEL_ED)) {
			List<Long> orderIds = new ArrayList<Long>();
			if (param.getParentId() > 0) {
				orderIds = orderFacade.getSubOrderIds(param.getParentId());
			} else {
				orderIds.add(param.getOrderId());
			}
			List<OrderCancelInfoDTO> list = new ArrayList<OrderCancelInfoDTO>();
			for (Long orderId : orderIds) {
				OrderCancelInfoDTO cancelDTO = new OrderCancelInfoDTO();
				cancelDTO.setCancelSource(OrderCancelSource.KF);
				cancelDTO.setOrderId(orderId);
				cancelDTO.setReason(param.getComment());
				cancelDTO.setRtype(OrderCancelRType.genEnumByIntValueSt(0));
				cancelDTO.setUserId(param.getUserId());
				cancelDTO.setAgentId(param.getAgentId());
				cancelDTO.setOperateUserType(OperateUserType.CMSER);
				list.add(cancelDTO);
			}
			// 1.调用取消订单的服务
			RetArg ret = orderFacade.cancelOrders(list);
			// 2.读取调用结果
			Boolean isSucc = RetArgUtil.get(ret, Boolean.class);
			result = isSucc ? 1 : 0;
		} else {
			result = orderFacade.modifyOrderState(param);
		}
		if (result > 0) {
			// 点发货时
			if (param.getNewState().equals(OrderFormState.ALL_DELIVE)) {
				smsFacade.sendGoodsSingle(new SMSParam(param.getOrderId()));// 先修改状态，发送短信有没成功不管
			}
			baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
			baseJsonVO.setMessage("修改订单状态成功");
		} else {
			baseJsonVO.setCode(ResponseCode.RES_ERROR);
			baseJsonVO.setMessage("修改订单状态失败");
		}
		return baseJsonVO;
	}

	/**
	 * 新增或者编辑备注
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/orderdetail/addOrUpdateComment", method = RequestMethod.POST)
	@RequiresPermissions(value = { "order:operate" })
	public @ResponseBody BaseJsonVO addOrUpdateOrderFormComment(@RequestBody OrderOperateParam param) {
		BaseJsonVO ret = new BaseJsonVO();
		if (StringUtils.isEmpty(param.getComment())) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("备注内容不能为空！");
			return ret;
		}
		long loginId = SecurityContextUtils.getUserId();
		param.setAgentId(loginId);
		param.setOperateUserType(OperateUserType.CMSER);
		;
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

}
