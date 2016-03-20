package com.xyl.mmall.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.OrderPackageReturnFacade;
import com.xyl.mmall.cms.facade.impl.OrderPackageReturnFacadeImpl.CWBatchConfirmResult;
import com.xyl.mmall.cms.facade.param.FrontCWCODBatchReturnParam;
import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
import com.xyl.mmall.cms.facade.param.FrontCWCODBatchReturnParam.KVPair;
import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam.TimeSearchTag;
import com.xyl.mmall.cms.vo.order.ReturnPackageDetailInfoVO;
import com.xyl.mmall.cms.vo.order.ReturnSkuInfoListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.ReturnPackageQueryCategoryListVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnOperationParam;

/**
 * 订单管理：退货退款管理 (客服)
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午11:05:05
 *
 */
@Controller
@RequestMapping("/order")
public class OrderManageReturnKFController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(OrderManageReturnKFController.class);

	@Autowired
	private OrderPackageReturnFacade cmsOrderReturnKFFacade;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private MobilePushManageFacade mobilePushManageFacade;
	
	/**
	 * 退货退款管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/return", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:return" })
	public String getTypeList(Model model) {
		appendStaticMethod(model);
		ReturnPackageQueryCategoryListVO vo = cmsOrderReturnKFFacade.cmsReturnOrderKFSearchTypeList();
		model.addAttribute("data", vo);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/order/return";
	}

	/**
	 * 退货退款管理页面：按照条件查找退货列表
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/return/getlist", method = RequestMethod.POST)
	@ResponseBody 
	public BaseJsonVO getReturnList(
			Model model, 
			@RequestBody FrontTimeRangeSearchTypeParam param
			) {
		BaseJsonVO retVO = new BaseJsonVO();
		retVO.setCode(200);
		retVO.setMessage("successful");
		if(param.getTag() == TimeSearchTag.TIME.getTag()) {
			retVO.setResult(cmsOrderReturnKFFacade.getReturnPackageBriefInfoListByTime(param));
			return retVO;
		}
		if(param.getTag() == TimeSearchTag.SEARCH.getTag()) {
			retVO.setResult(cmsOrderReturnKFFacade.getReturnPackageBriefInfoListBySearch(param));
			return retVO;
		}
		retVO.setCode(201);
		retVO.setMessage("illegal tag:" + param.getTag());
		return retVO;
	}
	
	/**
	 * 退货详情页面
	 * 
	 * @param model
	 * @param retId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/returndetail", method = RequestMethod.GET)
	public String returnDetailInfo(
			Model model, 
			@RequestParam(value="returnId") long retId, 
			@RequestParam(value="userId") long userId
			) {
		appendStaticMethod(model);
		ReturnPackageDetailInfoVO vo = cmsOrderReturnKFFacade.getReturnDetailInfo(retId, userId);
		model.addAttribute("data", vo);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "/pages/order/return.detail";
	}
	
	/**
	 * 退货详情页面：退货商品列表
	 * 
	 * @param model
	 * @param retId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/return/getProductList", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO returnOrderSkuListInfo(
			Model model, 
			@RequestParam(value="returnId") long retId, 
			@RequestParam(value="userId") long userId
			) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		ReturnSkuInfoListVO resultVO = cmsOrderReturnKFFacade.getReturnSkuInfoList(retId, userId);
		ret.setResult(resultVO);
		return ret;
	}
	
	/**
	 * 退款
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/return/pass", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO passReturn(
			Model model, 
			@RequestBody PassReturnOperationParam param
			) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		RetArg retArg = null;
		try {
			retArg = cmsOrderReturnKFFacade.passReturn(param);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			ret.setCode(201);
			ret.setMessage(e.getMessage());
			ret.setResult(false);
			return ret;
		}
		Boolean result = RetArgUtil.get(retArg, Boolean.class);
		boolean isSucc = null != result && Boolean.TRUE == result;
		ret.setResult(isSucc);
		if(isSucc) {
			try {
				ReturnPackageDTO retPkg = RetArgUtil.get(retArg, ReturnPackageDTO.class);
				if(null != retPkg) {
					mobilePushManageFacade.push(param.getUserId(), 2, null, null, retPkg.getOrderId());
				}
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
			}
		} else {
			ret.setCode(201);
			ret.setMessage(RetArgUtil.get(retArg, String.class));
		}
		return ret;
	}
	
	/**
	 * 拒绝退款
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/return/reject", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO rejectReturn(
			Model model, 
			@RequestBody ReturnOperationParam param
			) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(200);
		RetArg retArg = null;
		try {
			retArg = cmsOrderReturnKFFacade.rejectReturn(param);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			ret.setCode(201);
			ret.setMessage(e.getMessage());
			ret.setResult(false);
			return ret;
		}
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		ret.setResult(result);
		if(result) {
			try {
				ReturnPackageDTO retPkg = RetArgUtil.get(retArg, ReturnPackageDTO.class);
				if(null != retPkg) {
					mobilePushManageFacade.push(param.getUserId(), 3, null, null, retPkg.getOrderId());
				}
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
			}
		} else {
			ret.setCode(201);
			ret.setMessage(RetArgUtil.get(retArg, String.class));
		}
		return ret;
	}
	
	/**
	 * 拒绝退款
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/return/goback", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO cancelReject(
			Model model, 
			@RequestBody ReturnOperationParam param
			) {
		BaseJsonVO ret = new BaseJsonVO();
		RetArg retArg = null;
		try {
			retArg = cmsOrderReturnKFFacade.cancelReject(param);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			ret.setCode(201);
			ret.setMessage(e.getMessage());
			ret.setResult(false);
			return ret;
		}
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		ret.setResult(result);
		if(result) {
			ret.setCode(200);
		} else {
			ret.setCode(201);
		}
		ret.setMessage(RetArgUtil.get(retArg, String.class));
		return ret;
	}
	
	/**
	 * 取消退货申请（异常件拒绝退货后，支持客服取消退货申请）
	 * 
	 * @param model
	 * @param retPkgId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/return/deprecate", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO deprecateReturn(
			Model model, 
			@RequestParam(value="retId") long retPkgId, 
			@RequestParam(value="userId") long userId
			) {
		BaseJsonVO ret = new BaseJsonVO();
		RetArg retArg = null;
		try {
			retArg = cmsOrderReturnKFFacade.cancelReturnByKf(retPkgId, userId);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			ret.setCode(201);
			ret.setMessage(e.getMessage());
			ret.setResult(false);
			return ret;
		}
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		ret.setResult(result);
		if(result) {
			ret.setCode(200);
		} else {
			ret.setCode(201);
		}
		ret.setMessage(RetArgUtil.get(retArg, String.class));
		return ret;
	}
	
	/**
	 * 财务确认退款（COD退货 ）
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/return/cwconfirm", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO finishReturn(
			Model model, 
			@RequestParam(value="returnId") long retId, 
			@RequestParam(value="userId") long userId
			) {
		BaseJsonVO ret = new BaseJsonVO();
		RetArg retArg = null;
		try {
			retArg = cmsOrderReturnKFFacade.cwConfirmReturn(retId, userId);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			ret.setCode(201);
			ret.setMessage(e.getMessage());
			ret.setResult(false);
			return ret;
		}
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE == isSucc;
		ret.setResult(result);
		if(result) {
			ret.setCode(200);
		} else {
			ret.setCode(201);
		}
		ret.setMessage(RetArgUtil.get(retArg, String.class));
		return ret;
	}
	
	/**
	 * 财务确认退款（COD退货 ）
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/return/cwbatchconfirm", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonVO finishCODReturn(
			Model model, 
			@RequestBody FrontCWCODBatchReturnParam batchParam
			) {
		BaseJsonVO ret = new BaseJsonVO();
		Map<Long, Long> batchParamMap = new HashMap<Long, Long>();
		if(null != batchParam) {
			List<KVPair> pairList = batchParam.getBatchParam();
			if(null != pairList) {
				for(KVPair pair : pairList) {
					if(null == pair) {
						continue;
					}
					batchParamMap.put(pair.getRetPkgId(), pair.getUserId());
				}
			}
		}
		RetArg retArg = cmsOrderReturnKFFacade.cwBatchConfirmReturn(batchParamMap);
		Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
		boolean result = null != isSucc && Boolean.TRUE.equals(isSucc);
		if(result) {
			ret.setCode(200);
			ret.setResult(RetArgUtil.get(retArg, CWBatchConfirmResult.class));
		} else {
			ret.setCode(201);
		}
		ret.setMessage(RetArgUtil.get(retArg, String.class));
		return ret;
	}
}
