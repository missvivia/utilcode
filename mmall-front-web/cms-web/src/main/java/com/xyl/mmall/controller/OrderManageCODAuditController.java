package com.xyl.mmall.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.OrderCODAuditFacade;
import com.xyl.mmall.cms.facade.param.FrontCODAuditOperationParam;
import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam.TimeSearchTag;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.CODAuditQueryCategoryListVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * 订单管理：到付审核
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午11:05:05
 *
 */
@Controller
@RequestMapping("/order")
public class OrderManageCODAuditController extends BaseController {

	@Autowired
	private OrderCODAuditFacade cmsOrderCODFacade;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	/**
	 * 到付审核页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/topay", method = RequestMethod.GET)
	@RequiresPermissions(value = { "order:topay" })
	public String getTypeList(Model model) {
		appendStaticMethod(model);
		CODAuditQueryCategoryListVO vo = cmsOrderCODFacade.getCmsCODSearchTypeList();
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("data", vo);
		return "/pages/order/topay";
	}

	/**
	 * 按照条件查找到付审核列表
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/topay/getlist", method = RequestMethod.POST)
	@ResponseBody 
	public BaseJsonVO getTypeList(
			Model model, 
			@RequestBody FrontTimeRangeSearchTypeParam param
			) {
		BaseJsonVO retVO = new BaseJsonVO();
		retVO.setCode(200);
		retVO.setMessage("successful");
		if(param.getTag() == TimeSearchTag.TIME.getTag()) {
			retVO.setResult(cmsOrderCODFacade.getCODInfoListByTime(param));
			return retVO;
		}
		if(param.getTag() == TimeSearchTag.SEARCH.getTag()) {
			retVO.setResult(cmsOrderCODFacade.getCODInfoListBySearch(param));
			return retVO;
		}
		retVO.setCode(201);
		retVO.setMessage("illegal tag:" + param.getTag());
		return retVO;
	}
	
	/**
	 * 到付审核：通过
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/topay/pass", method = RequestMethod.POST)
	@ResponseBody 
	public BaseJsonVO pass(Model model, @RequestBody FrontCODAuditOperationParam param) {
		return auditOperationExec(cmsOrderCODFacade.passAudit(param));
	}
	
	/**
	 * 到付审核：拒绝
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/topay/reject", method = RequestMethod.POST)
	@ResponseBody 
	public BaseJsonVO reject(Model model, @RequestBody FrontCODAuditOperationParam param) {
		return auditOperationExec(cmsOrderCODFacade.rejectAudit(param));
	}
	
	/**
	 * 到付审核：撤销拒绝
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/topay/goback", method = RequestMethod.POST)
	@ResponseBody 
	public BaseJsonVO cancelReject(Model model, @RequestBody FrontCODAuditOperationParam param) {
		return auditOperationExec(cmsOrderCODFacade.cancelReject(param));
	}
	
	/**
	 * 用户黑名单
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/topay/addblackuser", method = RequestMethod.POST)
	@ResponseBody 
	public BaseJsonVO addBlackUser(Model model, @RequestBody FrontCODAuditOperationParam param) {
		return auditOperationExec(cmsOrderCODFacade.addUserToBlack(param));
	}
	
	/**
	 * 地址黑名单
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/topay/addblackaddress", method = RequestMethod.POST)
	@ResponseBody 
	public BaseJsonVO addBlackAddress(Model model, @RequestBody FrontCODAuditOperationParam param) {
		return auditOperationExec(cmsOrderCODFacade.addAddressToBlack(param));
	}
	
	private BaseJsonVO auditOperationExec(boolean successful) {
		BaseJsonVO retVO = new BaseJsonVO();
		if(successful) {
			retVO.setCode(200);
			retVO.setMessage("successful");
		} else {
			retVO.setCode(201);
			retVO.setMessage("failed");
		}
		return retVO;
	}
}
