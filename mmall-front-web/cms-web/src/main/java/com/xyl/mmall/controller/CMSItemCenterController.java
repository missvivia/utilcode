package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.POItemFacade;
import com.xyl.mmall.backend.vo.ExportPoSkuVO;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.backend.vo.ItemReviewPassVO;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.CMSItemCenterFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.vo.ExportMaterialVO;
import com.xyl.mmall.cms.vo.ItemReviewRejectVO;
import com.xyl.mmall.cms.vo.ProductReviewSearchVO;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.common.facade.impl.ItemCenterCommonFacadeImpl;
import com.xyl.mmall.excelparse.XLSExport;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.vo.DetailColorVO;
import com.xyl.mmall.mainsite.vo.DetailProductVO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.service.ControllerUtils;

@Controller
public class CMSItemCenterController {
	@Autowired
	private CMSItemCenterFacade itemCenterFacade;

	@Autowired
	private MainsiteItemFacade mainItemCenterFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;

	@RequestMapping(value = "/audit/productlist", method = RequestMethod.GET)
	@RequiresPermissions(value = { "audit:productlist" })
	public String auditPOList(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:product");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute("province", list);

		List<Map<String, Object>> statusList = new ArrayList<Map<String, Object>>();
		for (StatusType type : StatusType.values()) {
			String name = type.getDesc();
			long id = type.getIntValue();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("id", id);
			statusList.add(map);
		}
		model.addAttribute("statusList", statusList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/audit/polist";
	}

	@RequestMapping(value = "/audit/productlist/detail", method = RequestMethod.GET)
	@RequiresPermissions(value = { "audit:productlist" })
	public String auditProductList(Model model, @RequestParam(value = "poId", defaultValue = "0") long poId,
			@RequestParam(value = "disabled", defaultValue = "0") long disabled,
			@RequestParam(value = "startTime", defaultValue = "0") long startTime) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:productlist");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute("province", list);
		Schedule po = new Schedule();
		po.setStartTime(startTime);
		int isReady = 1;
		if (ScheduleUtil.checkSubmit(po))
			isReady = 0;
		List<Map<String, Object>> statusList = new ArrayList<Map<String, Object>>();
		for (StatusType type : StatusType.values()) {
			String name = type.getDesc();
			long id = type.getIntValue();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("id", id);
			statusList.add(map);
		}
		model.addAttribute("statusList", statusList);
		model.addAttribute("poId", poId);
		model.addAttribute("disabled", disabled);
		model.addAttribute("isReady", isReady);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/audit/productlist";
	}

	@RequestMapping(value = "/audit/product/detail", method = RequestMethod.GET)
	@RequiresPermissions(value = { "audit:product" })
	public String auditProduct(Model model, @RequestParam(value = "poId", defaultValue = "0") long poId,
			@RequestParam(value = "disabled", defaultValue = "0") long disabled,
			@RequestParam(value = "startTime", defaultValue = "0") long startTime) {

		List<Map<String, Object>> statusList = new ArrayList<Map<String, Object>>();
		for (StatusType type : StatusType.values()) {
			String name = type.getDesc();
			long id = type.getIntValue();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("id", id);
			statusList.add(map);
		}
		Schedule po = new Schedule();
		po.setStartTime(startTime);
		int isReady = 1;
		if (ScheduleUtil.checkSubmit(po))
			isReady = 0;
		model.addAttribute("poId", poId);
		model.addAttribute("statusList", statusList);
		model.addAttribute("disabled", disabled);
		model.addAttribute("isReady", isReady);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/audit/product";
	}

	@RequestMapping(value = "/audit/product", method = RequestMethod.GET)
	@RequiresPermissions(value = { "audit:product" })
	public String auditPOList2(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:product");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute("province", list);

		List<Map<String, Object>> statusList = new ArrayList<Map<String, Object>>();
		for (StatusType type : StatusType.values()) {
			String name = type.getDesc();
			long id = type.getIntValue();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("id", id);
			statusList.add(map);
		}
		model.addAttribute("statusList", statusList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/audit/po";
	}

	@RequestMapping(value = "/rest/audit/product/search", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO productReviewSearch(@RequestBody ProductReviewSearchVO param) {
		if (param.getStatus() == 0)
			param.setStatus(-1);
		else if (param.getStatus() == 1)
			param.setStatus(2);
		else if (param.getStatus() == 2)
			param.setStatus(3);
		else if (param.getStatus() == 3)
			param.setStatus(4);
		ControllerUtils.operaSQLParam(param);
		// youhua
		// BaseJsonVO retObj = itemCenterFacade.searchReviewProduct(param);
		BaseJsonVO retObj = itemCenterFacade.searchReviewProductYouhua(param);
		return retObj;
	}

	@RequestMapping(value = "/rest/audit/product/pass", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO productReviewPass(@RequestBody ItemReviewPassVO param) {
		BaseJsonVO retObj = itemCenterFacade.productReviewPass(param);
		return retObj;
	}

	@RequestMapping(value = "/rest/audit/product/reject", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO productReviewReject(@RequestBody ItemReviewRejectVO param) {
		BaseJsonVO retObj = itemCenterFacade.productReviewReject(param);
		return retObj;
	}

	@RequestMapping(value = "/rest/audit/material/search", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO skuReviewSearch(@RequestBody ProductReviewSearchVO param) {
		if (param.getStatus() == 0)
			param.setStatus(-1);
		else if (param.getStatus() == 1)
			param.setStatus(2);
		else if (param.getStatus() == 2)
			param.setStatus(3);
		else if (param.getStatus() == 3)
			param.setStatus(4);
		ControllerUtils.operaSQLParam(param);
		// BaseJsonVO retObj = itemCenterFacade.searchReviewSKU(param);
		BaseJsonVO retObj = itemCenterFacade.searchReviewSKUYouhua(param);
		return retObj;
	}

	@RequestMapping(value = "/rest/audit/material/export", method = RequestMethod.GET)
	public void materialExport(HttpServletResponse response, @RequestParam long poId) {
		List<ExportMaterialVO> result = itemCenterFacade.getExportMaterialVO(poId);
		XLSExport export = new XLSExport(String.valueOf(poId));
		export.createEXCEL(result, ExportMaterialVO.class);
		try {
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename=" + poId + ".xls");
			export.exportXLS(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/rest/audit/material/pass", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO skuReviewPass(@RequestBody ItemReviewPassVO param) {
		BaseJsonVO retObj = itemCenterFacade.skuReviewPass(param);
		return retObj;
	}

	@RequestMapping(value = "/rest/audit/material/reject", method = RequestMethod.POST)
	public @ResponseBody BaseJsonVO skuReviewReject(@RequestBody ItemReviewRejectVO param) {
		BaseJsonVO retObj = itemCenterFacade.skuReviewReject(param);
		return retObj;
	}

	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO productPreview(@RequestParam long id,
			@RequestParam(value = "scheduleId", required = false) Long scheduleId, RedirectAttributes model) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		DetailProductVO productVO = null;
		if (scheduleId != null)
			productVO = commonFacade.getDetailPageProduct(id, scheduleId);
		else
			productVO = commonFacade.getDetailPageProduct(id, true, false);
		List<DetailColorVO> colorList = commonFacade.getDetailPageColorList(Long.valueOf(productVO.getPoId()),
				productVO.getGoodsNo());
		retMap.put("product", productVO);
		retMap.put("colors", colorList);
		BaseJsonVO retObj = new BaseJsonVO(retMap);
		retObj.setCode(ErrorCode.SUCCESS);
		return retObj;
	}
}
