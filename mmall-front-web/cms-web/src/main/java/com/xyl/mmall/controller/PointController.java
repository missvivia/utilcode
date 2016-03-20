/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.PointFacade;
import com.xyl.mmall.cms.vo.PointVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.cms.vo.UserPointVO;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.promotion.dto.PointDTO;
import com.xyl.mmall.promotion.enums.AuditState;
import com.xyl.mmall.promotion.meta.Point;

/**
 * PointController.java created by yydx811 at 2015年12月24日 下午4:57:19
 * 积分申请审核controller
 *
 * @author yydx811
 */
@Controller
@RequestMapping("/promotion/point")
public class PointController {

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private PointFacade pointFacade;
	
	/**
	 * 积分调整申请页面
	 * @param basePageParamVO
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@RequiresPermissions("promotion:point")
	public String list(BasePageParamVO<PointVO> basePageParamVO, Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 0);
		
		PointDTO dto = new PointDTO();
		dto.setAuditState(-1);
		SiteCMSVO currentSite = pointFacade.getCurrentSite();
		if(currentSite !=null){
			dto.setSiteId(currentSite.getSiteId());
			dto.setApply(1);//申请页面
			model.addAttribute("list", pointFacade.getPointList(dto, basePageParamVO));
		}
		
		return "pages/promotion/point";
	}
	
	/**
	 * 积分调整审核页面
	 * @param basePageParamVO
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listaudit", method = RequestMethod.GET)
	@RequiresPermissions("promotion:pointaudit")
	public String listaudit(BasePageParamVO<PointVO> basePageParamVO, Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 1);
		
		PointDTO dto = new PointDTO();
		dto.setAuditState(-1);
		SiteCMSVO currentSite = pointFacade.getCurrentSite();
		if(currentSite !=null){
			dto.setSiteId(currentSite.getSiteId());
			dto.setApply(0);//审核页面
			model.addAttribute("list", pointFacade.getPointList(dto, basePageParamVO));
		}
		
		return "pages/promotion/point";
	}
	
	/**
	 * 积分申请/审核调整列表
	 * @param state 审核状态
	 * @param searchValue 搜索关键字
	 * @param apply 1：申请调整；0：审核
	 * @param basePageParamVO
	 * @return
	 */
	@RequestMapping(value = "/pointList", method = RequestMethod.GET)
	@RequiresPermissions(value = {"promotion:point", "promotion:pointaudit"}, logical = Logical.OR)
	public @ResponseBody BaseJsonVO pointList(
			@RequestParam(value = "state", defaultValue = "-1") int state,
			@RequestParam(value = "searchValue", defaultValue = "") String searchValue,
			@RequestParam(value = "apply", defaultValue = "1") int apply, 
			BasePageParamVO<PointVO> basePageParamVO) {
		BaseJsonVO ret = new BaseJsonVO();
		PointDTO dto = new PointDTO();
		dto.setAuditState(state);
		dto.setSearchKey(searchValue);
		SiteCMSVO currentSite = pointFacade.getCurrentSite();
		dto.setSiteId(currentSite.getSiteId());
		dto.setApply(apply);
		ret.setResult(pointFacade.getPointList(dto, basePageParamVO));
		ret.setCode(ErrorCode.SUCCESS);
		return ret;
	}	
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@RequiresPermissions(value = {"promotion:point"})
	public String editPoint(Model model, @RequestParam(value = "id", defaultValue = "-1") long id) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", 0);
		
		PointVO point = null;
		if (id > 0) {//编辑积分申请
			point = pointFacade.getPoint(id);
			if (point != null) {
				model.addAttribute("userPoint", pointFacade.getUserPoint(Long.valueOf(point.getUserAccountList())));
			}
		} else {//新建积分申请
			point = new PointVO();
			SiteCMSVO currentSite = pointFacade.getCurrentSite();
			point.setSiteId(currentSite.getSiteId());
			point.setSiteName(currentSite.getSiteName());
		}
		model.addAttribute("point", point);
		return "pages/promotion/pointEdit";
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@RequiresPermissions(value = {"promotion:point", "promotion:pointaudit"}, logical = Logical.OR)
	public String detailPoint(Model model, @RequestParam(value = "id") long id, @RequestParam(value = "audit", defaultValue="0") int audit) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("audit", audit);
		
		PointVO point = null;
		if (id > 0) {//编辑积分申请
			point = pointFacade.getPoint(id);
		} else {//新建积分申请
			point = new PointVO();
			SiteCMSVO currentSite = pointFacade.getCurrentSite();
			point.setSiteId(currentSite.getSiteId());
			point.setSiteName(currentSite.getSiteName());
		}
		model.addAttribute("point", point);
		return "pages/promotion/pointDetail";
	}

	@RequestMapping(value = "/queryUserPoint", method = RequestMethod.GET)
	@RequiresPermissions({ "promotion:point" })
	public @ResponseBody BaseJsonVO queryUserList(BasePageParamVO<UserPointVO> basePageParamVO, String searchValue) {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(pointFacade.getUserPointList(basePageParamVO, searchValue));
		return ret;
	}

	/**
	 * 保存申请
	 * @param id 调整申请id
	 * @return
	 */
	@RequestMapping(value = "/save")
	@RequiresPermissions({ "promotion:point" })
	public @ResponseBody BaseJsonVO save(Point point) {
		BaseJsonVO ret = new BaseJsonVO();
		try {
			pointFacade.savePoint(point);
			ret.setCode(ResponseCode.RES_SUCCESS);
			return ret; 
		} catch(Exception e) {
			ret.setMessage(e.getMessage());
			ret.setCode(ResponseCode.RES_ERROR);
			return ret;
		}
	}

	/**
	 * 提交申请
	 * @param id 调整申请id
	 * @return
	 */
	@RequestMapping(value = "/submit")
	@RequiresPermissions({ "promotion:point" })
	public @ResponseBody BaseJsonVO submit(@RequestParam(value = "id") long id) {
		BaseJsonVO ret = new BaseJsonVO();
		try {
			if (pointFacade.changeState(id, AuditState.SUBMITTED.getType())) {
				ret.setCode(ResponseCode.RES_SUCCESS);
				return ret; 
			} else {
				ret.setMessage("提交失败");
			}
		} catch(Exception e) {
			ret.setMessage(e.getMessage());
		}
		ret.setCode(ResponseCode.RES_ERROR);
		return ret;
	}

	/**
	 * 审核申请
	 * @param id 调整申请id
	 * @param auditState 审核状态，2：通过，3：拒绝
	 * @return
	 */
	@RequestMapping(value = "/audit")
	@RequiresPermissions({ "promotion:pointaudit" })
	public @ResponseBody BaseJsonVO audit(@RequestParam(value = "id") long id, @RequestParam(value = "state") int auditState) {
		BaseJsonVO ret = new BaseJsonVO();
		try {
			if ((auditState == AuditState.PASSED.getType() || auditState == AuditState.REJECTED.getType())) {
				if (pointFacade.changeState(id, auditState)) {
					ret.setCode(ResponseCode.RES_SUCCESS);
					return ret; 
				} else {
					ret.setMessage("审核失败");
				}
			} else {
				ret.setMessage("审核状态只能是通过或拒绝");
			}
		} catch(Exception e) {
			ret.setMessage(e.getMessage());
		}
		ret.setCode(ResponseCode.RES_ERROR);
		return ret;
	}
}
