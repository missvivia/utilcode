package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.AlbumFacade;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleBannerFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.AlbumUtil;
import com.xyl.mmall.cms.facade.util.ScheduleChecker;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.facade.util.BaseChecker.ErrChecker;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.photomgr.meta.AlbumImg;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.SchedulePageDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleStatusDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO.OrderFlag;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.meta.SchedulePage;

/**
 * 档期管理
 * 
 * @author hzzhanghui
 * 
 */
@Controller
// @RequestMapping("/schedule")
public class ScheduleController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

	private static final ScheduleChecker checker = new ScheduleChecker(logger);

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private ScheduleBannerFacade bannerFacade;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private ItemCenterFacade itemCenterFacade;

	@Resource
	private ItemCenterCommonFacade commonFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private AlbumFacade albumFacade;

	@Autowired
	private MainBrandFacade mainBrandFacade;

	@Autowired
	private BrandFacade brandFacade;

	@Autowired
	private JITSupplyManagerFacade jITSupplyManagerFacade;

	// ****************************************
	// 页面相关
	// ****************************************

	@RequestMapping(value = "/schedule/manage")
	@RequiresPermissions(value = { "schedule:manage" })
	public String scheduleManage(Model model) {
//		List<ScheduleStatusDTO> statusList = scheduleFacade.getScheduleStateForBackend();
		List<IdNameBean> statusList = scheduleFacade.getPOAduitStatusListForApplier();
		model.addAttribute("statusList", statusList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/list";
	}

	@RequestMapping(value = "/schedule/return", method = RequestMethod.GET)
	public String scheduleReture(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/return";
	}

	@RequestMapping(value = "/schedule/returnnote")
	public String scheduleReturelist(Model model) {
		model.addAttribute("provinceList", businessFacade.getAreaList());
		model.addAttribute("returnStatusList", PoReturnOrderVO.getSupplierStateEnumBean());
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/returnnote";
	}

	@RequestMapping(value = "/schedule/returndetail/{id}")
	public String scheduleReturndetail(Model model, @PathVariable(value = "id") Long returnOrderId) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		model.addAttribute("podetails", jITSupplyManagerFacade.getReturnPoOrderForm(returnOrderId));
		return "pages/schedule/returndetail";
	}

	@RequestMapping(value = { "/schedule/pages" })
	@RequiresPermissions(value = { "schedule:pages" })
	public String schedulePages(Model model) {
		//List<ScheduleStatusDTO> statusList = scheduleFacade.getSchedulePageStatusList();
		List<IdNameBean> statusList = scheduleFacade.getPOAduitStatusListForApplier();
		model.addAttribute("statusList", statusList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/pages";
	}

	/**
	 * 档期添加商品页
	 */
	@RequestMapping(value = { "/schedule/add" })
	public String add(Model model, @RequestParam(value = "id", required = false) Long id) {
		ErrChecker errChecker = checker.checkPOID(id);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg).toJSONString();
		}

		List<AreaDTO> areaList = businessFacade.getAreaList();
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);

		ScheduleVO scheduleVO = scheduleFacade.getScheduleById(id);
		errChecker = checker.checkFindSchedule(scheduleVO.getPo().getScheduleDTO().getSchedule(), id);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg).toJSONString();
		}

		ScheduleUtil.setPOShowFlag(scheduleVO.getPo(), System.currentTimeMillis());

		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		JSONObject poJson = ScheduleUtil.genScheduleJson(scheduleVO.getPo().getScheduleDTO(), allSiteList, null, warehouseList);
		poJson.put("showFlag", scheduleVO.getPo().getShowFlag());
//		if (scheduleVO.getPo().getScheduleDTO().getSchedule().getFlagAuditPrdList() <= 0) {
//			poJson.put("status", 1);
//		} else {
//			poJson.put("status", scheduleVO.getPo().getScheduleDTO().getSchedule().getFlagAuditPrdList());
//		}
		// use flagAuditPrdList as PO's status
		int flagAuditPrdList = scheduleVO.getPo().getScheduleDTO().getSchedule().getFlagAuditPrdList();
		if (flagAuditPrdList <= 1) {
			scheduleVO.getPo().getScheduleDTO().getSchedule().setStatus(ScheduleState.DRAFT);
		} else {
			scheduleVO.getPo().getScheduleDTO().getSchedule()
					.setStatus(ScheduleState.NULL.genEnumByIntValue(flagAuditPrdList));
		}
		POListDTO poList = new POListDTO();
		poList.getPoList().add(scheduleVO.getPo());
		ScheduleUtil.setPISPOValidFlag(poList);
		poJson.put("status", scheduleVO.getPo().getScheduleDTO().getSchedule().getStatus().getIntValue());
		
		model.addAttribute(ScheduleUtil.MODE_SCHEDULE, poJson);

		//JSONArray statusList = scheduleFacade.getProdDetailAuditStatusList();
		List<IdNameBean> statusList = scheduleFacade.getPOAduitStatusListForApplier();
		model.addAttribute(ScheduleUtil.MODE_STATUSLIST, statusList);

		List<CategoryArchitect> categoryList = commonFacade.getCategoryArchitect();
		model.addAttribute(ScheduleUtil.MODE_CATEGORYLIST, categoryList);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/add";
	}

	// ****************************************
	// 审核相关
	// ****************************************
	@RequestMapping(value = { "/schedule/pages/submit" })
	@ResponseBody
	public JSONObject pagesSubmit(@RequestBody JSONObject paramJson) {
		Long id = paramJson.getLong(ScheduleUtil.REQ_PARAM_ID);
		Long poId = paramJson.getLong("scheduleId");
		ErrChecker errChecker = checker.checkPOID(id);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		// check
		PODTO po = scheduleFacade.getSchedulePageById(id);
		if (po.getPageDTO() == null || po.getPageDTO().getPage() == null) { // PO
																			// id?
			ScheduleVO vo = scheduleFacade.getScheduleById(id);
			if (vo.getPo().getScheduleDTO() == null || vo.getPo().getScheduleDTO().getSchedule() == null) {
				String msg = "Cannot find any schedule page with id '" + id + "'!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			} else {
				id = vo.getPo().getScheduleDTO().getSchedule().getPageId();
				po = vo.getPo();
			}
		}
		if (!ScheduleUtil.checkSubmit(po.getScheduleDTO().getSchedule())) {
			String msg = "对不起，上线前4天不能再提交审核，请联系相关运维人员！";
			logger.error(msg);
			JSONObject json = generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			json.put("message", msg);
			return json;
		}

		boolean result = scheduleFacade.auditSchedulePageSubmit(id, poId);
		if (!result) {
			String msg = "Schedule page audit operation failure. Please check log!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}

		return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
	}

	/**
	 * PO添加商品后提交审核
	 */
	@RequestMapping(value = { "/schedule/productlist/submit" })
	@ResponseBody
	public JSONObject scheduleProductListSubmit(@RequestBody JSONObject paramJson) {
		//long loginId = SecurityContextUtils.getUserId();
		//long supplierId = itemCenterFacade.getSupplierId(loginId);
		Long id = paramJson.getLong(ScheduleUtil.REQ_PARAM_ID);
		ErrChecker errChecker = checker.checkPOID(id);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		// check
		ScheduleVO vo = scheduleFacade.getScheduleById(id);
		if (vo.getPo().getScheduleDTO().getSchedule() == null) {
			String msg = "Cannot find any PO with id '" + id + "'!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		if (!ScheduleUtil.checkSubmit(vo.getPo().getScheduleDTO().getSchedule())) {
			String msg = "对不起，上线前4天不能再提交审核，请联系相关运维人员！";
			logger.error(msg);
			JSONObject json = generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			json.put("message", msg);
			return json;
		}

		// check whether PO has product first before submit
		boolean poHasPrdFlag = scheduleFacade.isProductInPO(id);
		if (!poHasPrdFlag) {
			JSONObject json = new JSONObject();
			json.put(ScheduleUtil.RESP_CODE, ScheduleUtil.CODE_ERR);
			json.put(ScheduleUtil.RESP_RESULT, ScheduleUtil.RESULT_ERR);
			JSONObject msg = new JSONObject();
			msg.put("errcode", ScheduleUtil.CODE_PO_ADD_PRD_ERR);
			msg.put("errMsg", "PO adding product list operation failure when submit. Please check log!!");
			json.put(ScheduleUtil.RESP_MSG, msg);

			return json;
		}

		// update SKU's audit status after submit PO
		scheduleFacade.updatePrdListSubmitStatus(id);

		return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
	}

	// ****************************************
	// 查询相关
	// ****************************************
	// Backend “商务管理”查询档期列表
	@RequestMapping(value = { "/schedule/search" })
	@ResponseBody
	public JSONObject getScheduleList(@RequestBody JSONObject scheduleJsonParam) {
		int pageSize = scheduleJsonParam.getIntValue(ScheduleUtil.REQ_PARAM_LIMIT);
		int curPage = scheduleJsonParam.getIntValue(ScheduleUtil.REQ_PARAM_OFFSET);
		Long poId = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_PONUM);
		if (poId == null) {
			poId = 0L;
		}
		Long createTimeBegin = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_CRATEBEGIN);
		if (createTimeBegin == null) {
			createTimeBegin = 0L;
		}
		Long createTimeEnd = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_CREATESTOP);
		if (createTimeEnd == null) {
			createTimeEnd = 0L;
		}
		Long startTimeBegin = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_STARTBEGIN);
		if (startTimeBegin == null) {
			startTimeBegin = 0L;
		}
		Long startTimeEnd = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_STARTEND);
		if (startTimeEnd == null) {
			startTimeEnd = 0L;
		}
		Long endTimeBegin = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_ENDBEGIN);
		if (endTimeBegin == null) {
			endTimeBegin = 0L;
		}
		Long endTimeEnd = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_ENDSTOP);
		if (endTimeEnd == null) {
			endTimeEnd = 0L;
		}

		BusinessDTO supplier = getSupplier();
		if (supplier == null) {
			String msg = "Cannot find supplier, please check login session !!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}

		ScheduleCommonParamDTO paramDTO = ScheduleUtil.getComParamDTO(poId, 0, supplier.getId(), 0,
				0, 0, curPage, pageSize);
		if (supplier.getType() == 2) { 
			 // brand agent can see all site's PO
		} else if (supplier.getType() == 1) { // supplier
			try {
				paramDTO.allowedAreaIdList = supplier.getAreaIds();
				paramDTO.allowedAreaIdListFlag = ProvinceCodeMapUtil.getProvinceFmtByCodeList(supplier.getAreaIds());
			} catch (Exception e) {
				logger.error("Parse province fmt error for supplier '" + supplier.getId() 
						+ "'s siteList: " + supplier.getAreaIds() , e);
			}
		} else {
			String msg = "Supplier '" + supplier.getId() + "'s type '" + supplier.getType() + "' is wrong!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		
		
		_fillStatusListForPOManagement(paramDTO, scheduleJsonParam);
		ScheduleListVO vo = scheduleFacade.getScheduleListForOMS(paramDTO, startTimeBegin, startTimeEnd, endTimeBegin,
				endTimeEnd, createTimeBegin, createTimeEnd);
		ScheduleUtil.setPISPOValidFlag(vo.getPoList());
		
		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		return ScheduleUtil.generatePOListStr(vo, null, warehouseList, allSiteList);
	}

	// 商务管理->品购页列表
	@RequestMapping(value = { "/schedule/pages/search" })
	@ResponseBody
	public JSONObject getSchedulePageList(@RequestBody JSONObject paramJson) {
		boolean needTotal = true;// ScheduleUtil.needTotal(scheduleJsonParam);
		int pageSize = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_LIMIT);
		int curPage = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_OFFSET);
		Integer type = paramJson.getInteger(ScheduleUtil.REQ_PARAM_TYPE);
		Object key = paramJson.get(ScheduleUtil.REQ_PARAM_KEY);

		BusinessDTO supplier = getSupplier();
		if (supplier == null) {
			String msg = "Cannot find supplier, please check login session!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		ScheduleCommonParamDTO paramDTO = ScheduleUtil.getComParamDTO(0, 0, supplier.getId(), 0, 0,
				0, curPage, pageSize);
		if (supplier.getType() == 2) { 
			 // brand agent can see all site's PO
		} else if (supplier.getType() == 1) { // supplier
			try {
				paramDTO.allowedAreaIdList = supplier.getAreaIds();
				paramDTO.allowedAreaIdListFlag = ProvinceCodeMapUtil.getProvinceFmtByCodeList(supplier.getAreaIds());
			} catch (Exception e) {
				logger.error("Parse province fmt error for supplier '" + supplier.getId() 
						+ "'s siteList: " + supplier.getAreaIds() , e);
			}
		} else {
			String msg = "Supplier '" + supplier.getId() + "'s type '" + supplier.getType() + "' is wrong!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		
		_fillStatusListForPOManagement(paramDTO, paramJson);
		ScheduleListVO vo = scheduleFacade.getScheduleListForPOPages(paramDTO, type, key);
		ScheduleUtil.setPISPOValidFlag(vo.getPoList());
		int total = 0;
		if (vo.getPoList().getPoList().size() != 0 && needTotal) {
			total = vo.getPoList().getTotal();
		}

		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		JSONObject json = ScheduleUtil.generatePOListJsonForBackendPagesMgr(vo,warehouseList, allSiteList);
		if (needTotal) {
			json.getJSONObject(ScheduleUtil.RESP_RESULT).put(ScheduleUtil.RESP_TOTAL, total);
		}

		return json;
	}

	private JSONObject generateRespJsonStr(int code, boolean result, String msg) {
		JSONObject json = new JSONObject();
		json.put(ScheduleUtil.RESP_CODE, code);
		json.put(ScheduleUtil.RESP_RESULT, result);
		if (msg != null) {
			json.put(ScheduleUtil.RESP_MSG, msg);
		}

		return json;
	}

	@RequestMapping(value = { "/schedule/blist", "/schedule/banner" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "schedule:blist" })
	public String bannerManager(Model model) {
		model.addAttribute("name", "aaa");
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/banner/list";
	}

	@RequestMapping(value = "/schedule/download", method = RequestMethod.GET)
	public String bannerDownload(Model model) {
		long userId = SecurityContextUtils.getUserId();
		long supplierId = itemCenterFacade.getSupplierId(userId);
		Business supplier = businessFacade.getBusinessById(supplierId);
		model.addAttribute("name", "aaa");
		BrandDTO brandDTO = brandFacade.getBrandByBrandId(supplier.getActingBrandId());
		model.addAttribute("brand", brandDTO);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/banner/download";
	}

	// ////////////////////////////////////////
	// Banner
	// ///////////////////////////////////////

	private void _fillStatusListForPOManagement(ScheduleCommonParamDTO paramDTO, JSONObject paramJson) {
		Integer status = paramJson.getInteger(ScheduleUtil.REQ_PARAM_STATUS);
		List<CheckState> checkStatusList = null;
		if (status != null && status != 0) {
			checkStatusList = new ArrayList<CheckState>();
			checkStatusList.add(CheckState.NULL.genEnumByIntValue(status));
		} // else // query all

		// PO status
		List<ScheduleState> statusList = new ArrayList<ScheduleState>();
		statusList.add(ScheduleState.PASSED);
		statusList.add(ScheduleState.BACKEND_PASSED);
		statusList.add(ScheduleState.OFFLINE);

		paramDTO.poStatusList = statusList;
		paramDTO.bannerOrPageStatusList = checkStatusList;
		paramDTO.orderByFlag = OrderFlag.CREATETIME_DESC;
	}

	/**
	 * AJAX请求banner列表
	 */
	// // 商务管理->banner列表
	@RequestMapping(value = { "/schedule/banner/getlist" })
	@ResponseBody
	public JSONObject bannerGetList(@RequestBody JSONObject paramJson) {
		int pageSize = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_LIMIT);
		int curPage = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_OFFSET);
		boolean needTotal = true;// ScheduleUtil.needTotal(paramJson);

		BusinessDTO supplier = getSupplier();
		if (supplier == null) {
			String msg = "Cannot find supplier, please check login session!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}

		ScheduleCommonParamDTO paramDTO = ScheduleUtil.getComParamDTO(0, 0, supplier.getId(), 0, 0,
				0, curPage, pageSize);
		if (supplier.getType() == 2) { 
			 // brand agent can see all site's PO
		} else if (supplier.getType() == 1) { // supplier
			try {
				paramDTO.allowedAreaIdList = supplier.getAreaIds();
				paramDTO.allowedAreaIdListFlag = ProvinceCodeMapUtil.getProvinceFmtByCodeList(supplier.getAreaIds());
			} catch (Exception e) {
				logger.error("Parse province fmt error for supplier '" + supplier.getId() 
						+ "'s siteList: " + supplier.getAreaIds() , e);
			}
		} else {
			String msg = "Supplier '" + supplier.getId() + "'s type '" + supplier.getType() + "' is wrong!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		
		_fillStatusListForPOManagement(paramDTO, paramJson);
		ScheduleListVO vo = scheduleFacade.getScheduleBannerList(paramDTO);
		ScheduleUtil.setPISPOValidFlag(vo.getPoList());
		int total = 0;
		if (vo.getPoList().getPoList().size() != 0 && needTotal) {
			total = vo.getPoList().getTotal();
		}

		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		JSONObject json = ScheduleUtil.geneJsonObjForBannerList(vo, warehouseList, allSiteList);
		if (needTotal) {
			json.getJSONObject(ScheduleUtil.RESP_RESULT).put(ScheduleUtil.RESP_TOTAL, total);
		}

		return json;
	}

	/**
	 * banner提交审核
	 */
	@RequestMapping(value = { "/schedule/banner/submit" })
	@ResponseBody
	public JSONObject bannerAuditSubmit(@RequestBody JSONObject paramJson) {
		Long scheduleId = paramJson.getLong(ScheduleUtil.REQ_PARAM_SCHEDULEID);
		ErrChecker errChecker = checker.checkPOID(scheduleId);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		Long bannerId = paramJson.getLong(ScheduleUtil.REQ_PARAM_BANNERID);
		if (bannerId == null || bannerId == 0) {
			ScheduleVO banner = bannerFacade.getScheduleBannerByScheduleId(scheduleId);
			if (banner.getPo().getBannerDTO().getBanner() != null) {
				bannerId = banner.getPo().getBannerDTO().getBanner().getId();
			}
		}

		// check
		ScheduleVO po = scheduleFacade.getScheduleById(scheduleId);
		if (po.getPo().getScheduleDTO().getSchedule() == null) {
			String msg = "Cannot find any PO with id '" + scheduleId + "'!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		if (!ScheduleUtil.checkSubmit(po.getPo().getScheduleDTO().getSchedule())) {
			String msg = "对不起，上线前4天不能再提交审核，请联系相关运维人员！";
			logger.error(msg);
			JSONObject json = generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			json.put("message", msg);
			return json;
		}

		long userId = ScheduleUtil.getUserId();
		String userName = ScheduleUtil.getUserName();

		// 1. get relevant PO info
		ScheduleVO poVO = scheduleFacade.getScheduleById(scheduleId);
		Schedule schedule = poVO.getPo().getScheduleDTO().getSchedule();
		errChecker = checker.checkFindSchedule(schedule, scheduleId);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		// 2. create or update banner
		long now = System.currentTimeMillis();
		ScheduleBanner banner = new ScheduleBanner();
		banner.setUserId(userId);
		banner.setUserName(userName);
		banner.setScheduleId(scheduleId);
		//banner.setSaleAreaId(schedule.getCurSupplierAreaId());
		banner.setSupplierId(schedule.getSupplierId());
		banner.setSupplierName(schedule.getSupplierName());
		banner.setBrandName(schedule.getBrandName());
		banner.setBrandId(schedule.getBrandId());
		banner.setBrandNameEn(schedule.getBrandNameEn());
		banner.setHomeBannerImgUrl(paramJson.getString(ScheduleUtil.REQ_PARAM_BANNERSTARTIMG));
		banner.setPreBannerImgUrl(paramJson.getString(ScheduleUtil.REQ_PARAM_BANNERNEWIMG));
		if (paramJson.containsKey(ScheduleUtil.REQ_PARAM_COMMENT)) {
			banner.setComment(paramJson.getString(ScheduleUtil.REQ_PARAM_COMMENT));
		}
		banner.setStatus(CheckState.CHECKING);
		banner.setSubmitDate(now);
		banner.setStatusUpdateDate(now);
		banner.setUpdateDate(now);

		PODTO poDTO = new PODTO();
		ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
		bannerDTO.setBanner(banner);
		poDTO.setBannerDTO(bannerDTO);
		if (bannerId == null || bannerId == 0) { // create
			banner.setCreateDate(now);
			ScheduleVO dbVO = bannerFacade.saveScheduleBanner(poDTO);
			if (dbVO.getPo().getBannerDTO().getBanner().getId() == 0) {
				String msg = "Failure to create banner, please check the log!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			} else {
				JSONObject json = new JSONObject();
				json.put(ScheduleUtil.RESP_CODE, ScheduleUtil.CODE_OK);
				json.put(ScheduleUtil.RESP_RESULT,
						ScheduleUtil.genJsonObjFromBanner(dbVO.getPo().getBannerDTO().getBanner()));
				return json;
			}
		} else { // update
			banner.setId(bannerId);
			if (bannerFacade.updateScheduleBanner(poDTO)) {
				return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
			} else {
				String msg = "Failure to update banner, please check the log!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}
		}
	}

	// ////////////////////////////
	// SchedulePage
	// ////////////////////////////
	@RequestMapping(value = "/schedule/decorate", method = RequestMethod.GET)
	public String scheduleDecorate(Model model, @RequestParam(value = "id", required = false) Long pageId) {
		// Long pageId = paramJson.getLong(ScheduleUtil.REQ_PARAM_ID);
		ErrChecker errChecker = checker.checkPageId(pageId);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg).toJSONString();
		}

		PODTO poDTO = scheduleFacade.getSchedulePageById(pageId);
		if (poDTO.getPageDTO() == null || poDTO.getPageDTO().getPage() == null) {
			String msg = "Cannot find decorate page by page id '" + pageId + "'!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg).toJSONString();
		}

		long userId = ScheduleUtil.getUserId();
		BusinessDTO supplier = getSupplier();
		if (supplier == null) {
			String msg = "Cannot find supplier  by user id '" + userId + "'!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg).toJSONString();
		}

		// 门店列表
		//List<BrandShopDTO> shopList = mainBrandFacade.getBrandShops(supplier.getAreaId(), supplier.getActingBrandId());
		List<BrandShopDTO> shopList = mainBrandFacade.getBrandShopListBySupplierId(supplier.getId());
		
		// 图片列表
		List<AlbumImg> imgList = new ArrayList<AlbumImg>();
		String imgIds = poDTO.getPageDTO().getPage().getUdImgIds();
		List<Long> idList = ScheduleUtil.getItemListByItemListStr(imgIds);
		long bgImgId = poDTO.getPageDTO().getPage().getBgImgId();
		if (bgImgId != 0) {
			idList.add(bgImgId);
		}
		long headerImgId = poDTO.getPageDTO().getPage().getHeaderImgId();
		if (headerImgId != 0) {
			idList.add(headerImgId);
		}

		if (idList != null) {
			imgList = albumFacade.getImgListByIdList(idList);
		}

		// product category list
		List<CategoryArchitect> productCategoryList = commonFacade.getCategoryArchitect();

		// image category list
		JSONArray imgCategoryList = albumFacade.getDirListByUserId(userId);

		// page layout object
		JSONObject layout = ScheduleUtil.getPageLayoutJson(poDTO);

		// product list
		JSONArray products = new JSONArray();
		String prdIds = poDTO.getPageDTO().getPage().getUdProductIds();
		List<Long> prdIdList = ScheduleUtil.getItemListByItemListStr(prdIds);
		if (prdIdList != null) {
			products = scheduleFacade.getPrdListByPrdIdList(prdIdList, poDTO);
		}

		JSONArray imgListJSONArr = AlbumUtil.convertImgListToJSONArray(imgList);
		model.addAttribute("images", imgListJSONArr);
		model.addAttribute("imgCategory", imgCategoryList);
		model.addAttribute("productCategory", productCategoryList);
		model.addAttribute("layout", layout);
		model.addAttribute("brandShopList", shopList);
		model.addAttribute("products", products);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/decorate";
	}

	@RequestMapping(value = { "/schedule/decorate/save" })
	@ResponseBody
	public JSONObject scheduleDecorateSave(@RequestBody JSONObject paramJson) {
		return _editSchedulePage(paramJson, false);
	}

	@RequestMapping(value = { "/schedule/decorate/publish" })
	@ResponseBody
	public JSONObject scheduleDecoratePublish(@RequestBody JSONObject paramJson) {
		return _editSchedulePage(paramJson, true);
	}

	private JSONObject _editSchedulePage(JSONObject paramJson, boolean publishFlag) {
		Long pageId = paramJson.getLong(ScheduleUtil.REQ_PARAM_ID);
		String udProductIds = paramJson.getString(ScheduleUtil.REQ_PARAM_udProductIds);
		String udSetting = paramJson.getString(ScheduleUtil.REQ_PARAM_udSetting);
		Long bgImgId = paramJson.getLongValue(ScheduleUtil.REQ_PARAM_bgImgId);
		String bgSetting = paramJson.getString(ScheduleUtil.REQ_PARAM_bgSetting);
		Long headerImgId = paramJson.getLong(ScheduleUtil.REQ_PARAM_headerImgId);
		String headerSetting = paramJson.getString(ScheduleUtil.REQ_PARAM_headerSetting);
		boolean allListPartVisiable = paramJson.getBoolean(ScheduleUtil.REQ_PARAM_allListPartVisiable);
		String allListPartOthers = paramJson.getString(ScheduleUtil.REQ_PARAM_allListPartOthers);
		boolean mapPartVisiable = paramJson.getBoolean(ScheduleUtil.REQ_PARAM_mapPartVisiable);
		String mapPartOthers = paramJson.getString(ScheduleUtil.REQ_PARAM_mapPartOthers);
		String udImgIds = paramJson.getString(ScheduleUtil.REQ_PARAM_udImgIds);
		Long scheduleId = paramJson.getLong("scheduleId");

		ErrChecker errChecker = checker.checkPageID(pageId);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		if (scheduleId == null || scheduleId == 0) {
			PODTO tmpDTO = scheduleFacade.getSchedulePageById(pageId);
			scheduleId = tmpDTO.getPageDTO().getPage().getScheduleId();
		}

		SchedulePage page = new SchedulePage();

		page.setId(pageId);
		page.setScheduleId(scheduleId);
		page.setAllListPartOthers(allListPartOthers);
		page.setAllListPartVisiable(allListPartVisiable);
		page.setBgImgId(bgImgId);
		page.setBgSetting(bgSetting);
		page.setHeaderImgId(headerImgId);
		page.setHeaderSetting(headerSetting);
		page.setMapPartOthers(mapPartOthers);
		page.setMapPartVisiable(mapPartVisiable);
		page.setUdImgIds(udImgIds);
		page.setUdProductIds(udProductIds);
		page.setUdSetting(udSetting);

		if (publishFlag) {
			page.setStatus(CheckState.CHECKING);
			page.setStatusUpdateDate(System.currentTimeMillis());
		} else {
			page.setStatus(CheckState.DRAFT);
		}

		PODTO poDTO = new PODTO();
		SchedulePageDTO pageDTO = new SchedulePageDTO();
		pageDTO.setPage(page);
		poDTO.setPageDTO(pageDTO);

		boolean result = scheduleFacade.updateSchedulePage(poDTO);
		if (result) {
			return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
		} else {
			String msg = "Failure to update decorate page, please check the log!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
	}

	private long getSupplierId(long userId) {
		return itemCenterFacade.getSupplierId(userId);
	}

	private BusinessDTO getSupplier() {
		long userId = ScheduleUtil.getUserId();
		long supplierId = getSupplierId(userId);
		BusinessDTO supplier = businessFacade.getBusinessById(supplierId);
		return supplier;
	}
}