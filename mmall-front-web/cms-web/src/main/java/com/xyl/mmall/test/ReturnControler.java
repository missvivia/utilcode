package com.xyl.mmall.test;
//package com.xyl.mmall.test;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.netease.print.common.meta.RetArg;
//import com.netease.print.common.util.RetArgUtil;
//import com.netease.print.security.util.SecurityContextUtils;
//import com.xyl.mmall.cms.facade.LeftNavigationFacade;
//import com.xyl.mmall.cms.facade.OrderPackageReturnFacade;
//import com.xyl.mmall.cms.facade.param.FrontCWCODBatchReturnParam;
//import com.xyl.mmall.cms.facade.param.FrontCWCODBatchReturnParam.KVPair;
//import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
//import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam.TimeSearchTag;
//import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.ReturnPackageQueryCategoryListVO;
//import com.xyl.mmall.cms.vo.order.ReturnPackageDetailInfoVO;
//import com.xyl.mmall.cms.vo.order.ReturnSkuInfoListVO;
//import com.xyl.mmall.controller.BaseController;
//import com.xyl.mmall.framework.vo.BaseJsonVO;
//import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
//import com.xyl.mmall.order.dto._ReturnPackageDTO;
//import com.xyl.mmall.order.param.PassReturnOperationParam;
//import com.xyl.mmall.order.param.ReturnConfirmParam;
//import com.xyl.mmall.order.param.ReturnOperationParam;
//import com.xyl.mmall.order.service._ReturnPackageUpdateService;
//import com.xyl.mmall.test.ReturnJsonDeserializor.OmsConfirmParam;
//import com.xyl.mmall.test.ReturnJsonDeserializor.OmsConfirmParam.ConfirmParam;
//
///**
// * 订单管理：退货退款管理 (客服)
// * 
// * @author hzwangjianyi@corp.netease.com
// * @create 2014年9月29日 上午11:05:05
// *
// */
//@Controller
//@RequestMapping("/test/order")
//public class ReturnControler extends BaseController {
//	
//	private static final Logger logger = Logger.getLogger(ReturnControler.class);
//
//	@Autowired
//	private _ReturnPackageUpdateService retPkgUpdateService;
//	
//	@Autowired
//	private OrderPackageReturnFacade cmsOrderReturnKFFacade;
//	
//	@Autowired
//	private LeftNavigationFacade leftNavigationFacade;
//	
//	@Autowired
//	private MobilePushManageFacade mobilePushManageFacade;
//	
//	/**
//	 * 请求参数Json模板数据
//	 * 
//	 * @param tag
//	 * @return
//	 */
//	@RequestMapping(value = "/sample/return", method = RequestMethod.GET)
//	@ResponseBody 
//	public Object sampleJson(@RequestParam(value="tag") int tag)  {
//		return ReturnJsonDeserializor.smapleJson(tag);
//	}
//	
//	/**
//	 * OMS退货确认
//	 * 
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/return/omsconfirm", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO omsConfirm(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		OmsConfirmParam param = ReturnJsonDeserializor.decodeOmsConfirmParam(jsonParam);
//		BaseJsonVO retVO = new BaseJsonVO();
//		long retPkgId = param.getRetPkgId();
//		long userId = param.getUserId();
//		List<ConfirmParam> confirms = param.getReceivedRetOrdSku();
//		Map<Long, ReturnConfirmParam> confirm = new HashMap<Long, ReturnConfirmParam>();
//		for(ConfirmParam cp : confirms) {
//			confirm.put(cp.getOrderSkuId(), cp.getConfirmInfo());
//		}
//		RetArg retArg = retPkgUpdateService.confirmReturnedOrderSku(retPkgId, userId, confirm);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		if(null != isSucc && Boolean.TRUE == isSucc) {
//			retVO.setCode(200);
//		} else {
//			retVO.setCode(201);
//		}
//		retVO.setResult(RetArgUtil.get(retArg, _ReturnPackageDTO.class));
//		retVO.setMessage(RetArgUtil.get(retArg, String.class));
//		return retVO;
//	}
//	
//	/**
//	 * 退货退款管理页面
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "/return", method = RequestMethod.GET)
//	@RequiresPermissions(value = { "order:return" })
//	public String getTypeList(Model model) {
//		appendStaticMethod(model);
//		ReturnPackageQueryCategoryListVO vo = cmsOrderReturnKFFacade.cmsReturnOrderKFSearchTypeList();
//		model.addAttribute("data", vo);
//		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//		return "/pages/order/return";
//	}
//
//	/**
//	 * 退货退款管理页面：按照条件查找退货列表
//	 * 
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/return/getlist", method = RequestMethod.GET)
//	@ResponseBody 
//	public BaseJsonVO getReturnList(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontTimeRangeSearchTypeParam param = ReturnJsonDeserializor.decodeFrontTimeRangeSearchTypeParam(jsonParam);
//		BaseJsonVO retVO = new BaseJsonVO();
//		retVO.setCode(200);
//		retVO.setMessage("successful");
//		if(param.getTag() == TimeSearchTag.TIME.getTag()) {
//			retVO.setResult(cmsOrderReturnKFFacade.getReturnPackageBriefInfoListByTime(param));
//			return retVO;
//		}
//		if(param.getTag() == TimeSearchTag.SEARCH.getTag()) {
//			retVO.setResult(cmsOrderReturnKFFacade.getReturnPackageBriefInfoListBySearch(param));
//			return retVO;
//		}
//		retVO.setCode(201);
//		retVO.setMessage("illegal tag:" + param.getTag());
//		return retVO;
//	}
//	
//	/**
//	 * 退货详情页面
//	 * 
//	 * @param model
//	 * @param retId
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping(value = "/returndetail", method = RequestMethod.GET)
//	public String returnDetailInfo(
//			Model model, 
//			@RequestParam(value="returnId") long retId, 
//			@RequestParam(value="userId") long userId
//			) {
//		appendStaticMethod(model);
//		ReturnPackageDetailInfoVO vo = cmsOrderReturnKFFacade.getReturnDetailInfo(retId, userId);
//		model.addAttribute("data", vo);
//		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//		return "/pages/order/return.detail";
//	}
//	
//	/**
//	 * 退货详情页面：退货商品列表
//	 * 
//	 * @param model
//	 * @param retId
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping(value = "/return/getProductList", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseJsonVO returnOrderSkuListInfo(
//			Model model, 
//			@RequestParam(value="returnId") long retId, 
//			@RequestParam(value="userId") long userId
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		ReturnSkuInfoListVO resultVO = cmsOrderReturnKFFacade.getReturnSkuInfoList(retId, userId);
//		ret.setResult(resultVO);
//		return ret;
//	}
//	
//	/**
//	 * 退款
//	 * 
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/return/pass", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseJsonVO passReturn(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		PassReturnOperationParam param = ReturnJsonDeserializor.decodePassReturnOperationParam(jsonParam);
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		RetArg retArg = cmsOrderReturnKFFacade.passReturn(param);
//		Boolean result = RetArgUtil.get(retArg, Boolean.class);
//		boolean isSucc = null != result && Boolean.TRUE == result;
//		ret.setResult(isSucc);
//		if(isSucc) {
//			try {
//				mobilePushManageFacade.push(param.getUserId(), 2, "退货退款结果", "客服已通过退货退款请求", param.getRetId());
//			} catch (Exception e) {
//				logger.info(e.getMessage(), e);
//			}
//		} else {
//			ret.setCode(201);
//			ret.setMessage(RetArgUtil.get(retArg, String.class));
//		}
//		return ret;
//	}
//	
//	/**
//	 * 拒绝退款
//	 * 
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/return/reject", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseJsonVO rejectReturn(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		ReturnOperationParam param = ReturnJsonDeserializor.decodeReturnOperationParam(jsonParam);
//		BaseJsonVO ret = new BaseJsonVO();
//		ret.setCode(200);
//		RetArg retArg = cmsOrderReturnKFFacade.rejectReturn(param);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		boolean result = null != isSucc && Boolean.TRUE == isSucc;
//		ret.setResult(result);
//		if(result) {
//			try {
//				mobilePushManageFacade.push(param.getUserId(), 3, "退货退款结果", "客服已拒绝退货退款请求", param.getRetId());
//			} catch (Exception e) {
//				logger.info(e.getMessage(), e);
//			}
//		} else {
//			ret.setCode(201);
//			ret.setMessage(RetArgUtil.get(retArg, String.class));
//		}
//		return ret;
//	}
//	
//	/**
//	 * 拒绝退款
//	 * 
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/return/goback", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseJsonVO cancelReject(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		ReturnOperationParam param = ReturnJsonDeserializor.decodeReturnOperationParam(jsonParam);
//		BaseJsonVO ret = new BaseJsonVO();
//		RetArg retArg = cmsOrderReturnKFFacade.cancelReject(param);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		boolean result = null != isSucc && Boolean.TRUE == isSucc;
//		ret.setResult(result);
//		if(result) {
//			ret.setCode(200);
//		} else {
//			ret.setCode(201);
//		}
//		ret.setMessage(RetArgUtil.get(retArg, String.class));
//		return ret;
//	}
//	
//	/**
//	 * 财务确认退款（COD退货 ）
//	 * 
//	 * @param model
//	 * @param param
//	 * @return
//	 */
//	@RequestMapping(value = "/return/cwconfirm", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseJsonVO finishReturn(
//			Model model, 
//			@RequestParam(value="returnId") long retId, 
//			@RequestParam(value="userId") long userId
//			) {
//		BaseJsonVO ret = new BaseJsonVO();
//		RetArg retArg = cmsOrderReturnKFFacade.cwConfirmReturn(retId, userId);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		boolean result = null != isSucc && Boolean.TRUE == isSucc;
//		ret.setResult(result);
//		if(result) {
//			ret.setCode(200);
//		} else {
//			ret.setCode(201);
//		}
//		ret.setMessage(RetArgUtil.get(retArg, String.class));
//		return ret;
//	}
//	
//	/**
//	 * 财务确认退款（COD退货 ）
//	 * 
//	 * @param model
//	 * @param param
//	 * @return
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 */
//	@RequestMapping(value = "/return/cwbatchconfirm", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseJsonVO finishReturn(@RequestParam(value="jsonParam") String jsonParam) 
//			throws JsonParseException, JsonMappingException, IOException {
//		FrontCWCODBatchReturnParam batchParam = ReturnJsonDeserializor.decodeFrontCWCODBatchReturnParam(jsonParam);
//		BaseJsonVO ret = new BaseJsonVO();
//		Map<Long, Long> batchParamMap = new HashMap<Long, Long>();
//		if(null != batchParam) {
//			List<KVPair> pairList = batchParam.getBatchParam();
//			if(null != pairList) {
//				for(KVPair pair : pairList) {
//					if(null == pair) {
//						continue;
//					}
//					batchParamMap.put(pair.getRetPkgId(), pair.getUserId());
//				}
//			}
//		}
//		RetArg retArg = cmsOrderReturnKFFacade.cwBatchConfirmReturn(batchParamMap);
//		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
//		boolean result = null != isSucc && Boolean.TRUE == isSucc;
//		ret.setResult(result);
//		if(result) {
//			ret.setCode(200);
//		} else {
//			ret.setCode(201);
//		}
//		ret.setMessage(RetArgUtil.get(retArg, String.class));
//		return ret;
//	}
//}
