package com.xyl.mmall.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.ScheduleBannerFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.impl.AuthorityFacadeImpl;
import com.xyl.mmall.cms.facade.util.POBaseUtil;
import com.xyl.mmall.cms.facade.util.ScheduleChecker;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.facade.util.BaseChecker.ErrChecker;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.vo.POSortVO;
import com.xyl.mmall.cms.vo.POStatusGetVO;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.common.facade.BrandQueryFacade;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleChannelDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO.OrderFlag;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.enums.SupplyMode;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;

/**
 * 档期管理
 * 
 * @author hzzhanghui
 * 
 */
@Controller
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
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private JITSupplyManagerFacade jITSupplyManagerFacade;

	@Autowired
	private BrandQueryFacade brandQueryFacade;

	@Autowired
	private AuthorityFacadeImpl authorityFacadeImpl;

	// ****************************************
	// 页面相关
	// ****************************************

	/**
	 * 档期管理首页 档期创建成功后跳转的页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/schedule/schedulelist")
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public String schedule(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:schedulelist");
		// List<AreaDTO> areaList = businessFacade.getAreaList();
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);

		//List<ScheduleStatusDTO> statusList = scheduleFacade.getScheduleStateList();
		List<IdNameBean> statusList = scheduleFacade.getPOAduitStatusListForApplier();
		model.addAttribute(ScheduleUtil.MODE_STATUSLIST, statusList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/list";
	}

	/**
	 * PO展示管理
	 */
	@RequestMapping(value = "/schedule/place", method = RequestMethod.GET)
	@RequiresPermissions(value = { "schedule:place" })
	public String schedulePlace(Model model, @RequestParam(value = "id", required = false) Long id) {
		if (id != null && id != 0) {
			ScheduleVO schedule = scheduleFacade.getScheduleById(id);
			List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
			List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
			JSONObject poJson = ScheduleUtil.genScheduleJson(schedule.getPo().getScheduleDTO(), allSiteList, null,
					warehouseList);
			model.addAttribute(ScheduleUtil.MODE_SCHEDULE, poJson);
		}

		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:place");
		// List<AreaDTO> areaList = businessFacade.getAreaList();
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);

		List<ScheduleChannelDTO> categoryList = scheduleFacade.getScheduleChannelList();
		JSONArray chlArr = ScheduleUtil.convertChannelToJSONArray(categoryList);
		model.addAttribute(ScheduleUtil.MODE_CATEGORYLIST, chlArr);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/place";
	}

	/**
	 * 档期日历
	 */
	@RequestMapping(value = "/schedule/calendar", method = RequestMethod.GET)
	@RequiresPermissions(value = { "schedule:audit" })
	public String scheduleCalendar(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:audit");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/calendar";
	}

	@RequestMapping(value = "/schedule/return")
	@RequiresPermissions(value = { "schedule:return" })
	public String scheduleReture(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:return");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);
		model.addAttribute("warehouses", jITSupplyManagerFacade.getAllWarehouse());
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/return";
	}

	@RequestMapping(value = "/schedule/returnnote")
	@RequiresPermissions(value = { "schedule:returnnote" })
	public String scheduleReturelist(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:returnnote");
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);
		model.addAttribute("returnStatusList", PoReturnOrderVO.getCmsStateEnumBean());
		model.addAttribute("warehouses", jITSupplyManagerFacade.getAllWarehouse());
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/returnnote";
	}

	@RequestMapping(value = "/schedule/returndetail/{id}")
	public String scheduleReturndetail(Model model, @PathVariable(value = "id") Long returnOrderId) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		PoReturnOrderVO returnOrder = jITSupplyManagerFacade.getReturnPoOrderForm(returnOrderId);
		returnOrder.toCmsStateDesc();
		model.addAttribute("podetails",returnOrder);
		return "pages/schedule/returndetail";
	}

	/**
	 * 请求档期创建/编辑页面
	 * 
	 * @param model
	 * @param id
	 *            档期id，如果不为空，则表示是编辑页面，需要查询Schedule信息返回给页面
	 * @return
	 */
	@RequestMapping(value = { "/schedule/create" })
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public String scheduleCreateOrUpdatePage(Model model, @RequestParam(value = "id", required = false) Long id) {
		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		if (id != null && id != 0) {
			// 档期编辑请求，返回Schedule信息
			ScheduleVO schedule = scheduleFacade.getScheduleById(id);
			List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
			JSONObject poJson = ScheduleUtil.genScheduleJson(schedule.getPo().getScheduleDTO(), allSiteList, null,
					warehouseList);
			model.addAttribute(ScheduleUtil.MODE_SCHEDULE, poJson);
			model.addAttribute(ScheduleUtil.MODE_SCHEDULE+"_json", poJson.toJSONString());
			
		}

		// TODO to be deleted
		// List<AreaDTO> areaList =
		// scheduleFacade.getAllowedAreaList("schedule:schedulelist");
		// List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		// model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);

		List<ScheduleChannelDTO> categoryList = scheduleFacade.getScheduleChannelList();
		JSONArray chlArr = ScheduleUtil.convertChannelToJSONArray(categoryList);
		model.addAttribute(ScheduleUtil.MODE_CATEGORYLIST, chlArr);

		model.addAttribute("warehouseList", warehouseList);

		IdNameBean poFollower = new IdNameBean();
		poFollower.setId(SecurityContextUtils.getUserId() + POBaseUtil.NULL_STR);
		poFollower.setName(SecurityContextUtils.getUserName());
		model.addAttribute("poFollower", poFollower);
		model.addAttribute("currTime", System.currentTimeMillis());
		// 新建或编辑都会返回该页面
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/create";
	}

	@RequestMapping(value = { "/schedule/view" })
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public String scheduleView(Model model, @RequestParam(value = "id", required = false) Long id) {
		ErrChecker errChecker = checker.checkPOID(id);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg).toJSONString();
		}
		ScheduleVO schedule = scheduleFacade.getScheduleById(id);

		if (schedule.getPo().getScheduleDTO().getSchedule() == null) {
			String msg = "Cannot find any po by id '" + id + "'!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg).toJSONString();
		}

		if (!scheduleFacade.canAccessPO(schedule.getPo().getScheduleDTO(), "schedule:schedulelist")) {
			model.addAttribute(ScheduleUtil.MODE_SCHEDULE, null);
			model.addAttribute("noAccess", false);
		} else {
			List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:schedulelist");
			Map<Long, String> areaMap = new ConcurrentHashMap<Long, String>();
			for (AreaDTO area : areaList) {
				areaMap.put(area.getId(), area.getAreaName());
			}
			List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
			List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
			JSONObject poJson = ScheduleUtil.genScheduleJson(schedule.getPo().getScheduleDTO(), allSiteList, areaMap,
					warehouseList);
			model.addAttribute(ScheduleUtil.MODE_SCHEDULE, poJson);
			model.addAttribute("noAccess", true);
		}

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/view";
	}

	/**
	 * 档期审核 -- 页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/schedule/audit")
	@RequiresPermissions(value = { "schedule:audit" })
	public String scheduleAudit(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:audit");
		// List<AreaDTO> areaList = businessFacade.getAreaList();
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/audit";
	}

	/**
	 * Check supplier account whether valid
	 * 
	 * @param brandId
	 * @return
	 */
	@RequestMapping(value = "/schedule/checksupplieracctvalid")
	@ResponseBody
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public JSONObject checkSupplierAcctValid(@RequestParam(value = "supplierAcct") String supplierAcct, 
			@RequestParam(value = "poSupplierAcct") String poSupplierAcct) {
		ErrChecker errChecker = checker.checkStringEmpty(supplierAcct, "supplierAcct cannot be null!!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}
		
		errChecker = checker.checkStringEmpty(supplierAcct, "poSupplierAcct cannot be null!!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		List<String> permissionList = new ArrayList<String>();
		permissionList.add("audit:productlist");
		permissionList.add("audit:product");
		permissionList.add("audit:banner");
		permissionList.add("audit:decorate");
		
		BusinessDTO supplierDTO = businessFacade.getBusinessDTOByName(poSupplierAcct);
		if (supplierDTO == null) {
			String msg = "Cannot find supplier info by supplierAcct '" + poSupplierAcct + "'!!!";
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		
//		List<AreaDTO> accessSiteList = scheduleFacade.getAllowedAreaList("schedule:schedulelist");
//		List<Long> siteList = new ArrayList<Long>();
//		for (AreaDTO areaDTO : accessSiteList) {
//			siteList.add(areaDTO.getId());
//		}

		JSONObject retObj = new JSONObject();
		boolean result = false;
		try {
			result = authorityFacadeImpl.checkUserAuthorityOfSite(supplierAcct, permissionList, supplierDTO.getAreaIds());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		}

		retObj.put(ScheduleUtil.RESP_CODE, ScheduleUtil.CODE_OK);
		retObj.put(ScheduleUtil.RESP_RESULT, result);

		return retObj;
	}

	/**
	 * 新建PO时根据商家账号获取品牌和销售站点信息
	 * 
	 * @param supplierAcct
	 * @return
	 */
	@RequestMapping(value = "/schedule/getsupplierinfo")
	@ResponseBody
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public JSONObject getSupplierInfo(@RequestParam(value = "supplierAcct") String supplierAcct) {
		ErrChecker errChecker = checker.checkStringEmpty(supplierAcct, "supplierAcct cannot be null!!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		BusinessDTO supplierDTO = businessFacade.getBusinessDTOByName(supplierAcct);
		if (supplierDTO == null) {
			String msg = "Cannot find supplier info by supplierAcct '" + supplierAcct + "'!!!";
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}

		int supplierType = supplierDTO.getType();

		long brandId = supplierDTO.getActingBrandId();
		BrandDTO brandDTO = brandQueryFacade.getBrandByBrandId(brandId);
		String nameZh = brandDTO.getBrand().getBrandNameZh();
		String nameEn = brandDTO.getBrand().getBrandNameEn();
		String brandName = nameZh;
		if (brandName == null || "".equals(brandName.trim())) {
			brandName = nameEn;
		}

		JSONObject retObj = new JSONObject();
		JSONObject msg = new JSONObject();
		msg.put("brandId", brandId + POBaseUtil.NULL_STR);
		msg.put("brandName", brandName);
		msg.put("supplierType", supplierType); // 1-supplier 2-brand agent
		List<IdNameBean> shownSiteList = new ArrayList<IdNameBean>();

		// current user's site list
		List<AreaDTO> curUserSiteList = scheduleFacade.getAllowedAreaList("schedule:schedulelist");
		if (curUserSiteList == null || curUserSiteList.size() == 0) {
			String str = "Current user cannot access any sale site!!!!";
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, str);
		}
		if (supplierType == 1) { // supplier

			// supplier's site list
			List<Long> supplierSiteIdList = supplierDTO.getAreaIds();
			if (supplierSiteIdList == null || supplierSiteIdList.size() == 0) {
				String str = "Supplier don't have any permission to access sale site!!!!";
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, str);
			}

			// get intersection
			for (AreaDTO userSite : curUserSiteList) {
				for (int i = 0; i < supplierSiteIdList.size(); i++) {
					if (userSite.getId() == supplierSiteIdList.get(i)) {
						IdNameBean bean = new IdNameBean();
						bean.setId(supplierSiteIdList.get(i) + POBaseUtil.NULL_STR);
						bean.setName(supplierDTO.getAreaNames().get(i));
						shownSiteList.add(bean);
					}
				}
			}
		} else { // brand agent
			List<Long> tmpList = new ArrayList<Long>();
			for (AreaDTO area : curUserSiteList) {
				tmpList.add(area.getId());
			}

			try {
				if (ProvinceCodeMapUtil.getProvinceFmtByCodeList(tmpList) == ProvinceCodeMapUtil.QUANGUO) {
					shownSiteList = scheduleFacade.getAllProvince();
					IdNameBean allCountry = new IdNameBean();
					allCountry.setId("1");
					allCountry.setName("全国");
					shownSiteList.add(allCountry);
				} else {
					// the intersection is just current mananger's site list
					for (AreaDTO area : curUserSiteList) {
						IdNameBean bean = new IdNameBean();
						bean.setId(area.getId() + POBaseUtil.NULL_STR);
						bean.setName(area.getName());
						shownSiteList.add(bean);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (shownSiteList.size() == 0) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR,
					"Current user doesn't has right to access any sale site!!1");
		}
		msg.put("saleSiteList", shownSiteList);

		boolean result = false;
		try {
			List<Business> list = businessFacade.existBusinessOfBrandId(brandId);
			if (list == null || list.size() == 0) {
				result = false;
			} else {
				for (Business bis : list) {
					if (bis.getType() == 2) {
						result = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			result = false;
		}
		msg.put("brandAgentExist", result);

		//
		// List<IdNameBean> poFollowerList = new ArrayList<IdNameBean>();
		// {
		// // call LiHui's interface to get follower list
		// IdNameBean item1 = new IdNameBean();
		// item1.setId(15153+"");
		// item1.setName("zysupplier01@163.com ");
		//
		// IdNameBean item2 = new IdNameBean();
		// item2.setId(15073+"");
		// item2.setName("maxmax456@163.com");
		// poFollowerList.add(item1);
		// poFollowerList.add(item2);
		// }
		// msg.put("poFollowerList", poFollowerList);

		retObj.put(ScheduleUtil.RESP_CODE, ScheduleUtil.CODE_OK);
		retObj.put(ScheduleUtil.RESP_RESULT, ScheduleUtil.RESULT_OK);
		retObj.put(ScheduleUtil.RESP_MSG, msg);

		return retObj;
	}

	/**
	 * AJAX请求创建档期或修改档期信息
	 */
	@RequestMapping(value = { "/schedule/create/add" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public JSONObject scheduleCreate(@RequestBody JSONObject scheduleJsonParam) {
		return updateOrSavePO(scheduleJsonParam, false);
	}

	/**
	 * AJAX请求创建档期或修改档期信息
	 */
	@RequestMapping(value = { "/schedule/create/save" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public JSONObject scheduleSave(@RequestBody JSONObject scheduleJsonParam) {
		return updateOrSavePO(scheduleJsonParam, true);
	}

	private JSONObject updateOrSavePO(JSONObject scheduleJsonParam, boolean saveFlag) {
		logger.debug("Save PO: " + scheduleJsonParam);
		boolean updateFlag = ScheduleUtil.isPOUpdating(scheduleJsonParam);

		// set status
		if (updateFlag) {
			if (saveFlag) {
				ScheduleVO schedule = scheduleFacade.getScheduleById(scheduleJsonParam.getLong("id"));
				scheduleJsonParam.put("status", schedule.getPo().getScheduleDTO().getSchedule().getStatus()
						.getIntValue());
			} else {
				// directly submit to audit
				scheduleJsonParam.put("status", ScheduleState.CHECKING.getIntValue());
			}
		} else {
			if (saveFlag) { // freshly create an new PO
				scheduleJsonParam.put("status", ScheduleState.DRAFT.getIntValue());
			} else {
				// directly submit to audit
				scheduleJsonParam.put("status", ScheduleState.CHECKING.getIntValue());
			}
		}

		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		ErrChecker errChecker = checker.checkCreateScheduleValidation(scheduleJsonParam);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}
		ScheduleDTO scheduleDTO = ScheduleUtil.parseParams(scheduleJsonParam, updateFlag, saveFlag, allSiteList);
		errChecker = checker.checkCreateSchedule(scheduleDTO);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		// PO follower userId
		try {
			BusinessDTO supplierDTO = businessFacade.getBusinessDTOByName(scheduleDTO.getScheduleVice().getPoFollowerUserName());
			if (supplierDTO != null) {
				scheduleDTO.getScheduleVice().setPoFollowerUserId(supplierDTO.getId());
			}
			logger.warn("Cannot find valid supplier by supplier name '" + scheduleDTO.getScheduleVice().getPoFollowerUserName() + "' !!!");
		} catch (Exception e) {
			logger.warn("Cannot find valid supplier by supplier name '" + scheduleDTO.getScheduleVice().getPoFollowerUserName() + "' !!!");
		}
		
		// PO basic field
		BusinessDTO supplierDTO = businessFacade.getBusinessDTOByName(scheduleDTO.getScheduleVice().getSupplierAcct());
		long brandId = supplierDTO.getActingBrandId();
		BrandDTO brandDTO = brandQueryFacade.getBrandByBrandId(brandId);
		scheduleDTO.getSchedule().setBrandId(brandId);
		scheduleDTO.getSchedule().setBrandLogo(brandDTO.getLogo());
		scheduleDTO.getSchedule().setBrandLogoSmall(brandDTO.getLogo());
		scheduleDTO.getSchedule().setBrandName(brandDTO.getBrand().getBrandNameZh());
		scheduleDTO.getSchedule().setBrandNameEn(brandDTO.getBrand().getBrandNameEn());
		scheduleDTO.getSchedule().setSupplierId(supplierDTO.getId());
		scheduleDTO.getSchedule().setSupplierName(supplierDTO.getCompanyName());
		scheduleDTO.getScheduleVice().setUserId(SecurityContextUtils.getUserId());
		scheduleDTO.getScheduleVice().setCreateUser(SecurityContextUtils.getUserName());

		// po follower user id
		try {
			BusinessDTO tmpSupplier = businessFacade.getBusinessDTOByName(scheduleDTO.getScheduleVice()
					.getPoFollowerUserName());
			if (tmpSupplier != null) {
				scheduleDTO.getScheduleVice().setPoFollowerUserId(tmpSupplier.getId());
			}
		} catch (Exception e) {
			// doesn't matter. Ignore
		}

		// others
		scheduleDTO.getScheduleVice().setSupplierType(supplierDTO.getType());
		if (scheduleDTO.getScheduleVice().getSupplierType() == 1
				&& scheduleDTO.getScheduleVice().getSupplyMode() == SupplyMode.TOGETHER) {
			// 代理商选择共同供货才会设置这个辅助供货人
			List<BusinessDTO> supplierList = businessFacade.getBusinessDTOByBrandId(brandId);
			if (supplierList != null) {
				for (BusinessDTO supplier : supplierList) {
					if (supplier.getType() == 2) { // brand agent
						scheduleDTO.getScheduleVice().setBrandSupplierId(supplier.getId());
						scheduleDTO.getScheduleVice().setBrandSupplierName(supplier.getBusinessAccount());
						break;
					}
				}
			}

			if (scheduleDTO.getScheduleVice().getBrandSupplierName() == null) {
				logger.error("Cannot find any brand agent by brandId '" + brandId + "'!!!");
			}
		}

		// PO type
		if (scheduleDTO.getScheduleVice().getSupplierType() == 2) {
			scheduleDTO.getScheduleVice().setPoType(2);
		} else {
			if (scheduleDTO.getScheduleVice().getSupplyMode() == SupplyMode.SELF) {
				scheduleDTO.getScheduleVice().setPoType(1);
			} else if (scheduleDTO.getScheduleVice().getSupplyMode() == SupplyMode.TOGETHER) {
				scheduleDTO.getScheduleVice().setPoType(3);
			}
		}
		
		// control warehouse value
		if (scheduleDTO.getScheduleVice().getSupplierType() == 2) {
			scheduleDTO.getScheduleVice().setSupplierStoreId(0);
		} else {
			if (scheduleDTO.getScheduleVice().getSupplyMode() == SupplyMode.SELF) {
				scheduleDTO.getScheduleVice().setBrandStoreId(0);
			}
		}
		
		scheduleDTO.getScheduleVice().setUserId(ScheduleUtil.getUserId());
		scheduleDTO.getScheduleVice().setCreateUser(ScheduleUtil.getUserName());

		PODTO poDTO = new PODTO();
		poDTO.setScheduleDTO(scheduleDTO);
		logger.info("Create or Update PO: " + poDTO);

		boolean result = false;

		if (updateFlag) {
			result = scheduleFacade.updateSchedule(poDTO);
		} else {
			ScheduleVO vo = scheduleFacade.saveSchedule(poDTO);
			result = (vo.getPo().getScheduleDTO() != null);
		}

		errChecker = checker.checkResult(result, "Create schedule failure. Please check log!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
	}

	// ****************************************
	// 档期审核相关
	// ****************************************
	@RequestMapping(value = { "/schedule/audit/submit" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public JSONObject scheduleAuditSubmit(@RequestParam("id") Long id) {
		return _scheduleAudit(id, null, 2);
	}

	@RequestMapping(value = { "/schedule/audit/pass" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:audit" })
	public JSONObject scheduleAuditPass(@RequestParam("id") Long id) {
		return _scheduleAudit(id, null, 3);
	}

	@RequestMapping(value = { "/schedule/audit/reject" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:audit" })
	public JSONObject scheduleAuditReject(@RequestParam("id") Long id, @RequestParam("desc") String desc) {
		return _scheduleAudit(id, desc, 4);
	}

	private JSONObject _scheduleAudit(Long id, String desc, int type) {
		ErrChecker errChecker = checker.checkPOID(id);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		boolean result = false;
		if (type == 2) { // submit
			result = scheduleFacade.auditScheduleSubmit(id);
		} else if (type == 3) { // pass
			result = scheduleFacade.auditSchedulePass(id);
		} else if (type == 4) { // reject
			// check whether can reject
			ScheduleVO scheduleVO = scheduleFacade.getScheduleById(id);
			if (!ScheduleUtil.canRejectPO(scheduleVO.getPo().getScheduleDTO())) {
				String msg = "对不起，档期已经开始，不能拒绝!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}
			result = scheduleFacade.auditScheduleReject(id, desc);
		}
		errChecker = checker.checkResult(result, "Audit schedule operation failure. Please check log!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
	}

	@RequestMapping(value = { "/schedule/audit/change" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:audit" })
	public JSONObject changeScheduleDate(@RequestBody JSONObject paramJson) {
		Long id = paramJson.getLong("id");
		Long date = paramJson.getLong("date");
		String desc = paramJson.getString("desc");
		if (desc == null) {
			desc = "";
		}
		String poFollowerName = paramJson.getString("poFollowerUserName");
		if (poFollowerName == null) {
			poFollowerName = "";
		}
		ErrChecker errChecker = checker.checkChangeScheduleDate(id, date);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}
		
		ScheduleVO vo = scheduleFacade.getScheduleById(id);
		// check po follower 
		{
			List<ScheduleSiteRela> siteRelaList = vo.getPo().getScheduleDTO().getSiteRelaList();
			List<Long> saleSiteIdList = new ArrayList<Long>();
			for (ScheduleSiteRela siteRela : siteRelaList) {
				saleSiteIdList.add(siteRela.getSaleSiteId());
			}
	
			List<String> permissionList = new ArrayList<String>();
			permissionList.add("audit:productlist");
			permissionList.add("audit:product");
			permissionList.add("audit:banner");
			permissionList.add("audit:decorate");
			boolean result = false;
			try {
				result = authorityFacadeImpl.checkUserAuthorityOfSite(poFollowerName, permissionList, saleSiteIdList);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result = false;
			}
			result = true;
			if (!result) {
				String msg = "Invalid supplier acct '" + poFollowerName + "'!!!";
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}
		}

		long poFollowerId = 0L;
		try {
			BusinessDTO supplierDTO = businessFacade.getBusinessDTOByName(poFollowerName);
			if (supplierDTO != null) {
				poFollowerId = supplierDTO.getId();
			}
			logger.warn("Cannot find valid supplier by supplier name '" + poFollowerName + "' !!!");
		} catch (Exception e) {
			logger.warn("Cannot find valid supplier by supplier name '" + poFollowerName + "' !!!");
		}
		
		long now = System.currentTimeMillis();
		//ScheduleVO vo = scheduleFacade.getScheduleById(id);
		if (vo.getPo().getScheduleDTO().getSchedule().getStatus() == ScheduleState.BACKEND_PASSED
				&& vo.getPo().getScheduleDTO().getSchedule().getFlagAuditPrdList() == StatusType.APPROVAL.getIntValue()
				&& vo.getPo().getScheduleDTO().getScheduleVice().getFlagAuditBanner() == 1
				&& vo.getPo().getScheduleDTO().getScheduleVice().getFlagAuditPage() == 1
				&& vo.getPo().getScheduleDTO().getSchedule().getStartTime() < now
				&& vo.getPo().getScheduleDTO().getSchedule().getEndTime() > now
	|| (vo.getPo().getScheduleDTO().getSchedule().getStartTime() < now)) {
			String msg = "档期已经开始不可调整档期时间！";
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		
		date = ScheduleUtil.getPOStartTime(date).getTimeInMillis();
		int endDays = vo.getPo().getScheduleDTO().getSchedule().getNormalShowPeriod() + vo.getPo().getScheduleDTO().getSchedule().getExtShowPeriod();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		c.add(Calendar.DAY_OF_YEAR, endDays-1);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		
		//System.out.println(c.getTime());
		boolean result = scheduleFacade.adjustScheduleDate(id, date, c.getTimeInMillis(), desc,
				poFollowerName, poFollowerId);
		errChecker = checker.checkResult(result, "Adjust schedule time failure. Please check log!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		} else {
			return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
		}
	}

	@RequestMapping(value = { "/schedule/manual/change" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public JSONObject changeScheduleDate(@RequestParam("id") Long id, @RequestParam("dateStr") String date) {
		long startTime = 0;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date d = formatter.parse(date);
			startTime = d.getTime();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, e.getMessage());
		}

		boolean result = scheduleFacade.adjustScheduleDate(id, startTime,
				ScheduleUtil.calculateScheduleEndTime(startTime), null, "", 0);
		if (!result) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, "Adjust startTime for PO '" + id
					+ "' failure");
		} else {
			return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
		}
	}

	// ****************************************
	// 档期查询相关
	// ****************************************
//	/**
//	 * 查询档期列表 -- 审核页面初始化查询
//	 * 
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "/schedule/audit/list")
//	@ResponseBody
//	@RequiresPermissions(value = { "schedule:audit" })
//	public JSONObject getAuditScheduleList(@RequestParam(value = "limit", required = false) int pageSize,
//			@RequestParam(value = "offset", required = false) int curPage,
//			@RequestParam(value = "province", required = false) int provinceId,
//			@RequestParam(value = "status", required = false) int status) {
//
//		return _getListJson(pageSize, curPage, provinceId, status);
//	}

	private JSONObject _ajaxGetPOList(int pageSize, int curPage, long provinceId, int status, int type,
			 Object value, String permission) {
		List<ScheduleState> statusList = new ArrayList<ScheduleState>();
		if (status != 0) {
			statusList.add(ScheduleState.NULL.genEnumByIntValue(status));
		}

		ScheduleCommonParamDTO paramDTO = ScheduleUtil.getComParamDTO(0, 0, 0, provinceId, 0, 0, curPage, pageSize);
		paramDTO.orderByFlag = OrderFlag.CREATETIME_DESC;
		paramDTO.poStatusList = statusList;

		if (type == 3 && !StringUtils.isEmpty(value)) {
			List<Business> supplierList = businessFacade.getBusinessByAccount(value.toString());
			if (supplierList == null || supplierList.size() == 0) {
				JSONObject json = new JSONObject();
				json.put("code", ScheduleUtil.CODE_OK);
				json.put("result", new JSONObject());
				json.getJSONObject("result").put("total", 0);
				json.getJSONObject("result").put("list", new JSONArray());
				return json;
			}

			if (supplierList != null) {
				List<Long> supplierIdList = new ArrayList<Long>();
				for (Business supplier : supplierList) {
					supplierIdList.add(supplier.getId());
				}
				paramDTO.supplierIdList = supplierIdList;
			}
		}

		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList(permission);

//		if (provinceId == 0) {
			if (areaList == null || areaList.size() == 0) {
				String msg = "Current user has no right to view any site's data!!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}

			List<Long> allowedAreaIdList = new ArrayList<Long>();
			for (AreaDTO area : areaList) {
				allowedAreaIdList.add(area.getId());
			}
			paramDTO.allowedAreaIdList = allowedAreaIdList;
//		}

		ScheduleListVO vo = scheduleFacade.getScheduleListForCMS(paramDTO, type, value);
		ScheduleUtil.setPOValidFlag(vo.getPoList());
		
		Map<Long, String> supplierIdAccountMap = scheduleFacade.getSupplierIdAccountMap(vo.getPoList(), 1);
		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		return ScheduleUtil.generatePOListStr2(vo, areaList, supplierIdAccountMap, warehouseList, allSiteList);

	}
	
	@RequestMapping(value = "/schedule/audit/list")
	@ResponseBody
	@RequiresPermissions(value = { "schedule:audit" })
	public JSONObject getScheduleListForPOAudit(
			@RequestParam(value = "limit", required = false) int pageSize,
			@RequestParam(value = "offset", required = false) int curPage,
			@RequestParam(value = "province", required = false) long provinceId,
			@RequestParam(value = "status", required = false) int status,
			@RequestParam(value = "type", required = false) int type,
			@RequestParam(value = "key", required = false) Object value) {
		return _ajaxGetPOList(pageSize, curPage, provinceId, status, type, value, "schedule:audit");
	}
	
	/**
	 * AJAX获取档期列表
	 */
	@RequestMapping(value = { "/schedule/list" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:schedulelist" })
	public JSONObject getScheduleList(@RequestParam(value = "limit", required = false) int pageSize,
			@RequestParam(value = "offset", required = false) int curPage,
			@RequestParam(value = "province", required = false) long provinceId,
			@RequestParam(value = "status", required = false) int status,
			@RequestParam(value = "type", required = false) int type,
			@RequestParam(value = "key", required = false) Object value) {

		return _ajaxGetPOList(pageSize, curPage, provinceId, status, type, value, "schedule:schedulelist");
//		// // to be deleted
//		// boolean result = scheduleFacade.test();
//		// if (!result) {
//		// String msg = "Failure migrate data!!";
//		// logger.error(msg);
//		// return generateRespJsonStr(ScheduleUtil.CODE_ERR,
//		// ScheduleUtil.RESULT_ERR, msg);
//		// }
//
//		List<ScheduleState> statusList = new ArrayList<ScheduleState>();
//		if (status != 0) {
//			statusList.add(ScheduleState.NULL.genEnumByIntValue(status));
//		}
//
//		ScheduleCommonParamDTO paramDTO = ScheduleUtil.getComParamDTO(0, 0, 0, provinceId, 0, 0, curPage, pageSize);
//		paramDTO.orderByFlag = OrderFlag.CREATETIME_DESC;
//		paramDTO.poStatusList = statusList;
//
//		if (type == 3 && !StringUtils.isEmpty(value)) {
//			List<Business> supplierList = businessFacade.getBusinessByAccount(value.toString());
//			if (supplierList == null || supplierList.size() == 0) {
//				JSONObject json = new JSONObject();
//				json.put("code", ScheduleUtil.CODE_OK);
//				json.put("result", new JSONObject());
//				json.getJSONObject("result").put("total", 0);
//				json.getJSONObject("result").put("list", new JSONArray());
//				return json;
//			}
//
//			if (supplierList != null) {
//				List<Long> supplierIdList = new ArrayList<Long>();
//				for (Business supplier : supplierList) {
//					supplierIdList.add(supplier.getId());
//				}
//				paramDTO.supplierIdList = supplierIdList;
//			}
//		}
//
//		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:schedulelist");
//
////		if (provinceId == 0) {
//			if (areaList == null || areaList.size() == 0) {
//				String msg = "Current user has no right to view any site's data!!!";
//				logger.error(msg);
//				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
//			}
//
//			List<Long> allowedAreaIdList = new ArrayList<Long>();
//			for (AreaDTO area : areaList) {
//				allowedAreaIdList.add(area.getId());
//			}
//			paramDTO.allowedAreaIdList = allowedAreaIdList;
////		}
//
//		ScheduleListVO vo = scheduleFacade.getScheduleListForCMS(paramDTO, type, value);
//		ScheduleUtil.setPOValidFlag(vo.getPoList());
//		
//		Map<Long, String> supplierIdAccountMap = scheduleFacade.getSupplierIdAccountMap(vo.getPoList(), 1);
//		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
//		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
//		return ScheduleUtil.generatePOListStr2(vo, areaList, supplierIdAccountMap, warehouseList, allSiteList);

	}

	/**
	 * 获取指定月份的档期列表
	 */
	@RequestMapping(value = { "/schedule/list/month" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:audit" })
	public JSONObject getScheduleListByMonthAndProvince(@RequestParam("province") Long provinceId,
			@RequestParam("year") int year, @RequestParam("month") int month) {
		ErrChecker errChecker = checker.checkProvinceId(provinceId);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		// calculate start and end time of the month
		Calendar beginTimeOfMonth = ScheduleUtil.getBeginTimeOfMonth(year, month);
		Calendar endTimeOfMonth = ScheduleUtil.getEndTimeOfMonth(year, month);

		List<ScheduleState> statusList = new ArrayList<ScheduleState>();
		statusList.add(ScheduleState.PASSED);
		statusList.add(ScheduleState.BACKEND_PASSED);
		statusList.add(ScheduleState.OFFLINE);

		ScheduleCommonParamDTO paramDTO = ScheduleUtil.getComParamDTO(0, 0, 0, provinceId,
				beginTimeOfMonth.getTimeInMillis(), endTimeOfMonth.getTimeInMillis(), 0, 0);
		paramDTO.orderByFlag = OrderFlag.SHOWORDER_ASC;

//		if (provinceId == null || provinceId == 0) {
			List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:audit");
			if (areaList == null || areaList.size() == 0) {
				String msg = "Current user has no right to view any site's data!!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}

			List<Long> allowedAreaIdList = new ArrayList<Long>();
			for (AreaDTO area : areaList) {
				allowedAreaIdList.add(area.getId());
			}
			paramDTO.allowedAreaIdList = allowedAreaIdList;
//		}

		ScheduleListVO vo = scheduleFacade.getScheduleList(paramDTO, statusList, false);
		
		
		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		return ScheduleUtil.geneJsonObjForMonthList(vo, warehouseList, allSiteList, provinceId);
	}

	@RequestMapping(value = { "/schedule/sort" })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:audit" })
	public JSONObject sortPO(@RequestBody JSONObject paramJson) {
		long curSupplierAreaId = paramJson.getLongValue("curSupplierAreaId");
		JSONArray poIdArr = paramJson.getJSONArray("poIds");
		List<String> poIdList = new ArrayList<String>();
		if (poIdArr != null) {
			for (int i = 0, j = poIdArr.size(); i < j; i++) {
				poIdList.add(poIdArr.getString(i));
			}
		}
		POSortVO vo = new POSortVO();
		vo.setCurSupplierAreaId(curSupplierAreaId);
		vo.setPoIds(poIdList);

		ErrChecker errChecker = checker.checkListEmpty(vo.getPoIds(), "Parameter 'poIds' cannot be null!!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}
		errChecker = checker.checkLong(vo.getCurSupplierAreaId(), "Parameter 'curSupplierAreaId' cannot be null!!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		boolean result = scheduleFacade.batchUpdatePOOrder(vo);
		if (!result) {
			String msg = "Failure to update order, please check the log!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		} else {
			return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
		}
	}

	@RequestMapping(value = { "/schedule/validlist" })
	@ResponseBody
	@RequiresPermissions(value = { "promotion:activityEdit" })
	public JSONObject validScheduleList(@RequestBody JSONObject paramJson) {
		long startDate = paramJson.getLongValue(ScheduleUtil.REQ_PARAM_STARTDATE);
		long endDate = paramJson.getLongValue(ScheduleUtil.REQ_PARAM_ENDDATE);
		long curSupplierAreaId = paramJson.getLongValue(ScheduleUtil.REQ_PARAM_SUPPLIERAREAID);
		int pageSize = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_LIMIT);
		int curPage = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_OFFSET);

		ScheduleCommonParamDTO paramDTO = ScheduleUtil.getComParamDTO(0, 0, 0, curSupplierAreaId, startDate, endDate,
				curPage, pageSize);
//		if (curSupplierAreaId == 0) {
			List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("promotion:activityEdit");
			if (areaList == null || areaList.size() == 0) {
				String msg = "Current user has no right to view any site's data!!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}

			List<Long> allowedAreaIdList = new ArrayList<Long>();
			for (AreaDTO area : areaList) {
				allowedAreaIdList.add(area.getId());
			}
			paramDTO.allowedAreaIdList = allowedAreaIdList;
//		}

		List<ScheduleState> statusList = new ArrayList<ScheduleState>();
		statusList.add(ScheduleState.PASSED);
		statusList.add(ScheduleState.BACKEND_PASSED);
		paramDTO.poStatusList = statusList;
		
		paramDTO.orderByFlag = OrderFlag.STARTTIME_DESC;

		ScheduleListVO vo = scheduleFacade.getScheduleList(paramDTO);
		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		JSONObject json = ScheduleUtil.geneJsonObjForValidList(vo, warehouseList, allSiteList);

		int total = vo.getPoList().getTotal();
		json.put(ScheduleUtil.RESP_TOTAL, total);
		return json;
	}

	private JSONObject _getListJson(int pageSize, int curPage, long provinceId, int status) {
		List<ScheduleState> statusList = new ArrayList<ScheduleState>();

		if (status == 0) {
			// status = 2; // 默认查询待审核的列表
			// statusList.add(ScheduleState.CHECKING);
		} else if (status == 2) {
			statusList.add(ScheduleState.NULL.genEnumByIntValue(status));
		} else if (status == 3) { // 已审核。需要包括审核拒绝的
			statusList.add(ScheduleState.PASSED);
			statusList.add(ScheduleState.REJECTED);
		}

		ScheduleCommonParamDTO paramDTO = ScheduleUtil.getComParamDTO(0, 0, 0, provinceId, 0, 0, curPage, pageSize);
		paramDTO.orderByFlag = OrderFlag.CREATETIME_DESC;

		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:audit");
		// List<AreaDTO> areaList = businessFacade.getAreaList();

//		if (provinceId == 0) {
			if (areaList == null || areaList.size() == 0) {
				String msg = "Current user has no right to view any site's data!!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}

			List<Long> allowedAreaIdList = new ArrayList<Long>();
			for (AreaDTO area : areaList) {
				allowedAreaIdList.add(area.getId());
			}
			paramDTO.allowedAreaIdList = allowedAreaIdList;
//		}

		ScheduleListVO vo = scheduleFacade.getScheduleList(paramDTO, statusList, false);

		Map<Long, String> supplierIdAccountMap = scheduleFacade.getSupplierIdAccountMap(vo.getPoList(), 1);
		List<IdNameBean> warehouseList = scheduleFacade.getStoreAreaList();
		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		return ScheduleUtil.generatePOListStr2(vo, areaList, supplierIdAccountMap, warehouseList, allSiteList);
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

	// /////////////////////////////////////////////////
	// ScheduleBanner
	// /////////////////////////////////////////////////
	/**
	 * AJAX请求banner列表
	 */
	@RequestMapping(value = { "/schedule/banner/audit/search" })
	@ResponseBody
	@RequiresPermissions(value = { "audit:banner" })
	public JSONObject bannerAuditSearch(@RequestBody JSONObject paramJson) {
		boolean needTotal = true;// ScheduleUtil.needTotal(paramJson);
		Long curSupplierAreaId = paramJson.getLong(ScheduleUtil.REQ_PARAM_SUPPLIERAREAID);
		if (curSupplierAreaId == null) {
			curSupplierAreaId = 0L;
		}
		Long startDate = paramJson.getLong(ScheduleUtil.REQ_PARAM_STARTDATE);
		if (startDate == null) {
			startDate = 0L;
		}
		Long endDate = paramJson.getLong(ScheduleUtil.REQ_PARAM_ENDDATE);
		if (endDate == null) {
			endDate = 0L;
		}
		Integer status = paramJson.getInteger(ScheduleUtil.REQ_PARAM_STATUS);
		ErrChecker errChecker = checker.checkStatus(status);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		int pageSize = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_LIMIT);
		int curPage = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_OFFSET);
		List<CheckState> statusList = new ArrayList<CheckState>();
		if (status == 0) {
			statusList.add(CheckState.CHECKING);
		} else {
			statusList.add(CheckState.PASSED);
			statusList.add(CheckState.REJECTED);
		}

		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.curSupplierAreaId = curSupplierAreaId;
		paramDTO.bannerOrPageStatusList = statusList;
		paramDTO.startDate = startDate;
		paramDTO.endDate = endDate;
		paramDTO.curPage = curPage;
		paramDTO.pageSize = pageSize;
		paramDTO.createWhereFlag = false;

		// area list
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:banner");
		// List<AreaDTO> areaList = businessFacade.getAreaList();

//		if (curSupplierAreaId == 0) {
			if (areaList == null || areaList.size() == 0) {
				String msg = "Current user has no right to view any site's data!!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}

			List<Long> allowedAreaIdList = new ArrayList<Long>();
			for (AreaDTO area : areaList) {
				allowedAreaIdList.add(area.getId());
			}
			paramDTO.allowedAreaIdList = allowedAreaIdList;
//		}

		ScheduleListVO listVO = bannerFacade.getScheduleBannerList(paramDTO, null, null);
		ScheduleUtil.setPOValidFlag(listVO.getPoList());
		int total = 0;
		if (listVO.getPoList().getPoList().size() != 0 && needTotal) {
			total = listVO.getPoList().getTotal();
		}

		Map<Long, String> areaMap = new ConcurrentHashMap<Long, String>();
		for (AreaDTO area : areaList) {
			areaMap.put(area.getId(), area.getAreaName());
		}

		Map<Long, String> supplierIdAccountMap = scheduleFacade.getSupplierIdAccountMap(listVO.getPoList(), 3);

		JSONObject json = ScheduleUtil.geneBannerListJsonObj(listVO, areaMap, supplierIdAccountMap);
		if (needTotal) {
			json.getJSONObject(ScheduleUtil.RESP_RESULT).put(ScheduleUtil.RESP_TOTAL, total);
		}
		return json;
	}

	/**
	 * AJAX请求banner列表
	 */
	@RequestMapping(value = { "/schedule/banner/audit/search_by_key" })
	@ResponseBody
	@RequiresPermissions(value = { "audit:banner" })
	public JSONObject bannerAuditSearchWithKey(@RequestBody JSONObject paramJson) {
		boolean needTotal = true;// ScheduleUtil.needTotal(paramJson);
		Integer status = paramJson.getInteger(ScheduleUtil.REQ_PARAM_STATUS);
		ErrChecker errChecker = checker.checkStatus(status);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		int key = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_KEY);
		long scheduleId = 0L;
		String supplierName = null;
		String brandName = null;
		List<Long> supplierIdList = null;
		if (key == 1) {
			try {
				scheduleId = paramJson.getLongValue(ScheduleUtil.REQ_PARAM_VALUE);
			} catch (Exception e) {
				// type convert error
				scheduleId = -1L;
			}
		} else if (key == 2) {
			supplierName = paramJson.getString(ScheduleUtil.REQ_PARAM_VALUE);
			if (supplierName != null && !"".equals(supplierName.trim())) {
				List<Business> supplierList = businessFacade.getBusinessByAccount(supplierName);
				if (supplierList == null || supplierList.size() == 0) {
					JSONObject json = new JSONObject();
					json.put("code", ScheduleUtil.CODE_OK);
					json.put("result", new JSONObject());
					json.getJSONObject("result").put("total", 0);
					json.getJSONObject("result").put("list", new JSONArray());
					return json;
				}

				if (supplierList != null) {
					supplierIdList = new ArrayList<Long>();
					for (Business supplier : supplierList) {
						supplierIdList.add(supplier.getId());
					}
				}
			}
		} else if (key == 3) {
			brandName = paramJson.getString(ScheduleUtil.REQ_PARAM_VALUE);
		}

		int pageSize = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_LIMIT);
		int curPage = paramJson.getIntValue(ScheduleUtil.REQ_PARAM_OFFSET);
		List<CheckState> statusList = new ArrayList<CheckState>();
		if (status == 0) {
			statusList.add(CheckState.CHECKING);
		} else {
			statusList.add(CheckState.PASSED);
			statusList.add(CheckState.REJECTED);
		}

		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.poId = scheduleId;
		paramDTO.bannerOrPageStatusList = statusList;
		paramDTO.supplierIdList = supplierIdList;
		paramDTO.curPage = curPage;
		paramDTO.pageSize = pageSize;
		paramDTO.createWhereFlag = false;

		// area list
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:banner");
		// List<AreaDTO> areaList = businessFacade.getAreaList();

		if (areaList == null || areaList.size() == 0) {
			String msg = "Current user has no right to view any site's data!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}

		List<Long> allowedAreaIdList = new ArrayList<Long>();
		for (AreaDTO area : areaList) {
			allowedAreaIdList.add(area.getId());
		}
		paramDTO.allowedAreaIdList = allowedAreaIdList;

		ScheduleListVO listVO = null;
		if (key == 2) {
			listVO = bannerFacade.getScheduleBannerListWithSupplierIdList(paramDTO, brandName);
		} else {
			listVO = bannerFacade.getScheduleBannerList(paramDTO, supplierName, brandName);
		}
		ScheduleUtil.setPOValidFlag(listVO.getPoList());
		
		int total = 0;
		if (listVO.getPoList().getPoList().size() != 0 && needTotal) {
			listVO.getPoList().getTotal();
		}

		Map<Long, String> areaMap = new ConcurrentHashMap<Long, String>();
		for (AreaDTO area : areaList) {
			areaMap.put(area.getId(), area.getAreaName());
		}

		Map<Long, String> supplierIdAccountMap = scheduleFacade.getSupplierIdAccountMap(listVO.getPoList(), 3);
		JSONObject json = ScheduleUtil.geneBannerListJsonObj(listVO, areaMap, supplierIdAccountMap);
		if (needTotal) {
			json.getJSONObject(ScheduleUtil.RESP_RESULT).put(ScheduleUtil.RESP_TOTAL, total);
		}

		return json;
	}

	// banner审核不通过
	@RequestMapping(value = { "/schedule/banner/reject" })
	@ResponseBody
	@RequiresPermissions(value = { "audit:banner" })
	public JSONObject bannerAuditReject(@RequestBody JSONObject paramJson) {
		String ids = paramJson.getString(ScheduleUtil.REQ_PARAM_IDS);
		Long scheduleId = paramJson.getLong("scheduleId");
		ErrChecker errChecker = checker.checkBannerIds(ids);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		Long id = Long.parseLong(ids.substring(1, ids.lastIndexOf("]")).split(",")[0]);
		if (scheduleId == null || scheduleId == 0) {
			// Get poId for quick query
			ScheduleVO bannerVO = bannerFacade.getScheduleBannerById(id);
			scheduleId = bannerVO.getPo().getBannerDTO().getBanner().getScheduleId();
		}

		// check whether can reject
		ScheduleVO scheduleVO = scheduleFacade.getScheduleById(scheduleId);
		if (!ScheduleUtil.canRejectPO(scheduleVO.getPo().getScheduleDTO())) {
			String msg = "对不起，档期已经开始，不能拒绝!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
		
		long userId = ScheduleUtil.getUserId();
		String userName = ScheduleUtil.getUserName();
		String desc = paramJson.getString(ScheduleUtil.REQ_PARAM_REASON);

		ScheduleBanner banner = new ScheduleBanner();
		banner.setId(id);
		banner.setScheduleId(scheduleId);
		banner.setStatusMsg(desc);
		banner.setAuditUserId(userId);
		banner.setAuditUserName(userName);

		PODTO poDTO = new PODTO();
		ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
		bannerDTO.setBanner(banner);
		poDTO.setBannerDTO(bannerDTO);

		boolean result = bannerFacade.auditScheduleBannerReject(poDTO);
		if (!result) {
			String msg = "Failure to reject banner, please check the log!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		} else {
			return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
		}
	}

	// banner审核通过
	@RequestMapping(value = { "/schedule/banner/pass" })
	@ResponseBody
	@RequiresPermissions(value = { "audit:banner" })
	public JSONObject bannerAuditPass(@RequestBody List<Long> paramJson) {
		ErrChecker errChecker = checker.checkBannerPass(paramJson);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		Long id = paramJson.get(0);
		// Get poId for quick update
		ScheduleVO bannerVO = bannerFacade.getScheduleBannerById(id);
		long scheduleId = bannerVO.getPo().getBannerDTO().getBanner().getScheduleId();

		long userId = ScheduleUtil.getUserId();
		String userName = ScheduleUtil.getUserName();

		ScheduleBanner banner = new ScheduleBanner();
		banner.setId(id);
		banner.setScheduleId(scheduleId);
		banner.setAuditUserId(userId);
		banner.setAuditUserName(userName);

		PODTO poDTO = new PODTO();
		ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
		bannerDTO.setBanner(banner);
		poDTO.setBannerDTO(bannerDTO);

		boolean result = bannerFacade.auditScheduleBannerPass(poDTO);
		if (!result) {
			String msg = "Failure to pass audit banner, please check the log!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		} else {
			return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
		}
	}

	@RequestMapping(value = "/audit/banner")
	@RequiresPermissions(value = { "audit:banner" })
	public String auditBanner(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:banner");
		// List<AreaDTO> areaList = businessFacade.getAreaList();
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/audit/banner";
	}

	@RequestMapping(value = "/audit/brand")
	@RequiresPermissions(value = { "audit:brand" })
	public String auditBrand(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:brand");
		// List<AreaDTO> areaList = businessFacade.getAreaList();
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);

		List<ScheduleChannelDTO> categoryList = scheduleFacade.getScheduleChannelList();
		model.addAttribute(ScheduleUtil.MODE_CATEGORYLIST, categoryList);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/audit/brand";
	}

	// /////////////////////////////////////////////////
	// SchedulePage
	// /////////////////////////////////////////////////
	// CMS品购页审核通过
	@RequestMapping(value = { "/schedule/pages/pass" })
	@ResponseBody
	@RequiresPermissions(value = { "audit:decorate" })
	public JSONObject pagesPass(@RequestBody List<Long> paramJson) {
		ErrChecker errChecker = checker.checkBannerPass(paramJson);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		Long id = paramJson.get(0);
		// get poId for quick update
		PODTO pageDTO = scheduleFacade.getSchedulePageById(id);
		long scheduleId = pageDTO.getPageDTO().getPage().getScheduleId();

		boolean result = scheduleFacade.auditSchedulePagePass(id, scheduleId);
		errChecker = checker.checkResult(result, "Schedule page audit operation failure. Please check log!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
	}

	// CMS品购页审核不通过
	@RequestMapping(value = { "/schedule/pages/reject" })
	@ResponseBody
	@RequiresPermissions(value = { "audit:decorate" })
	public JSONObject pagesReject(@RequestBody JSONObject paramJson) {
		String ids = paramJson.getString(ScheduleUtil.REQ_PARAM_IDS);
		Long scheduleId = paramJson.getLong("scheduleId");
		ErrChecker errChecker = checker.checkBannerIds(ids);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		Long id = Long.parseLong(ids.substring(1, ids.lastIndexOf("]")).split(",")[0]);
		String desc = paramJson.getString(ScheduleUtil.REQ_PARAM_REASON);
		if (scheduleId == null || scheduleId == 0) {
			// get poId for quick update
			PODTO pageDTO = scheduleFacade.getSchedulePageById(id);
			scheduleId = pageDTO.getPageDTO().getPage().getScheduleId();
		}
		
		// check whether can reject
		ScheduleVO scheduleVO = scheduleFacade.getScheduleById(scheduleId);
		if (!ScheduleUtil.canRejectPO(scheduleVO.getPo().getScheduleDTO())) {
			String msg = "对不起，档期已经开始，不能拒绝!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}

		boolean result = scheduleFacade.auditSchedulePageReject(id, scheduleId, desc);
		errChecker = checker.checkResult(result, "Schedule page audit operation failure. Please check log!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg);
		}

		return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
	}

	// CMS品购页列表异步查询
	@RequestMapping(value = { "/schedule/pages/search" })
	@RequiresPermissions(value = { "audit:decorate" })
	@ResponseBody
	public JSONObject getSchedulePageList(@RequestBody JSONObject scheduleJsonParam) {
		boolean needTotal = true;// ScheduleUtil.needTotal(scheduleJsonParam);
		int pageSize = scheduleJsonParam.getIntValue(ScheduleUtil.REQ_PARAM_LIMIT);
		int curPage = scheduleJsonParam.getIntValue(ScheduleUtil.REQ_PARAM_OFFSET);

		long userId = ScheduleUtil.getUserId();
		Long submitDateBegin = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_STARTDATE);
		if (submitDateBegin == null) {
			submitDateBegin = 0L;
		}
		Long submitDateEnd = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_ENDDATE);
		if (submitDateEnd == null) {
			submitDateEnd = 0L;
		}
		Long pageId = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_PAGEID);
		if (pageId == null) {
			pageId = 0L;
		}
		Long scheduleId = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_SCHEDULEID);
		if (scheduleId == null) {
			scheduleId = 0L;
		}
		Long curSupplierAreaId = scheduleJsonParam.getLong(ScheduleUtil.REQ_PARAM_SUPPLIERAREAID);
		if (curSupplierAreaId == null) {
			curSupplierAreaId = 0L;
		}
		String supplierName = scheduleJsonParam.getString(ScheduleUtil.REQ_PARAM_SUPPLIERNAME);
		String brandName = scheduleJsonParam.getString(ScheduleUtil.REQ_PARAM_BRANDNAME);

		int status = scheduleJsonParam.getIntValue("stauts");
		List<CheckState> statusList = new ArrayList<CheckState>();
		if (status != 0) {
			if (status == 1) {
				// statusList.add(CheckState.DRAFT);
				statusList.add(CheckState.CHECKING);
			} else if (status == 2) {
				statusList.add(CheckState.PASSED);
			} else if (status == 3) {
				statusList.add(CheckState.REJECTED);
			}

		} else {
			statusList.add(CheckState.CHECKING);
			statusList.add(CheckState.PASSED);
			statusList.add(CheckState.REJECTED);
		}

		List<Long> supplierIdList = null;
		if (supplierName != null && !"".equals(supplierName.trim())) {
			List<Business> supplierList = businessFacade.getBusinessByAccount(supplierName);
			if (supplierList == null || supplierList.size() == 0) {
				JSONObject json = new JSONObject();
				json.put("code", ScheduleUtil.CODE_OK);
				json.put("result", new JSONObject());
				json.getJSONObject("result").put("total", 0);
				json.getJSONObject("result").put("list", new JSONArray());
				return json;
			}

			if (supplierList != null) {
				supplierIdList = new ArrayList<Long>();
				for (Business supplier : supplierList) {
					supplierIdList.add(supplier.getId());
				}
			}
		}

		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.poId = scheduleId;
		paramDTO.userId = userId;
		paramDTO.curSupplierAreaId = curSupplierAreaId;
		paramDTO.bannerOrPageStatusList = statusList;
		paramDTO.supplierIdList = supplierIdList;
		paramDTO.startDate = submitDateBegin;
		paramDTO.endDate = submitDateEnd;
		paramDTO.curPage = curPage;
		paramDTO.pageSize = pageSize;

		// area list
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:decorate");
		// List<AreaDTO> areaList = businessFacade.getAreaList();

		if (curSupplierAreaId == 0) {
			if (areaList == null || areaList.size() == 0) {
				String msg = "Current user has no right to view any site's data!!!";
				logger.error(msg);
				return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
			}

			List<Long> allowedAreaIdList = new ArrayList<Long>();
			for (AreaDTO area : areaList) {
				allowedAreaIdList.add(area.getId());
			}
			paramDTO.allowedAreaIdList = allowedAreaIdList;
		}

		ScheduleListVO vo = scheduleFacade.getSchedulePageList(paramDTO, pageId, brandName);
		ScheduleUtil.setPOValidFlag(vo.getPoList());
		int total = 0;
		if (vo.getPoList().getPoList().size() != 0 && needTotal) {
			total = vo.getPoList().getTotal();
		}

		Map<Long, String> areaMap = new ConcurrentHashMap<Long, String>();
		for (AreaDTO area : areaList) {
			areaMap.put(area.getId(), area.getAreaName());
		}

		Map<Long, String> supplierIdAccountMap = scheduleFacade.getSupplierIdAccountMap(vo.getPoList(), 2);

		JSONObject json = ScheduleUtil.generatePOListJsonForCMSPagesMgr(vo, areaMap, supplierIdAccountMap);
		if (needTotal) {
			json.getJSONObject(ScheduleUtil.RESP_RESULT).put(ScheduleUtil.RESP_TOTAL, total);
		}

		return json;
	}

	@RequestMapping(value = "/audit/decorate")
	@RequiresPermissions(value = { "audit:decorate" })
	public String auditDecorate(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("audit:decorate");
		// List<AreaDTO> areaList = businessFacade.getAreaList();
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);

		//List<ScheduleStatusDTO> statusList = scheduleFacade.getSchedulePageStatusList();
		List<IdNameBean> statusList = scheduleFacade.getPOAuditStatusListForApprover();
		model.addAttribute(ScheduleUtil.MODE_STATUSLIST, statusList);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/audit/decorate";
	}

	@RequestMapping(value = "/schedule/status", method = RequestMethod.GET)
	@RequiresPermissions(value = { "schedule:place" })
	public String scheduelStatus(Model model) {
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:place");
		// List<AreaDTO> areaList = businessFacade.getAreaList();
		List<IdNameBean> list = ScheduleUtil.convertAreaList(areaList);
		model.addAttribute(ScheduleUtil.MODE_PROVINCELIST, list);

		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 30);
		long monthLater = c.getTimeInMillis();

		model.addAttribute("images", list);
		model.addAttribute("stime", now);
		model.addAttribute("etime", monthLater);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/schedule/status";
	}

	@RequestMapping(value = "/schedule/status/list", method = { RequestMethod.POST })
	@ResponseBody
	@RequiresPermissions(value = { "schedule:place" })
	public JSONObject scheduelStatusList(@RequestBody JSONObject jsonParam) {
		POStatusGetVO vo = JSON.toJavaObject(jsonParam, POStatusGetVO.class);

		// permission check
		List<AreaDTO> areaList = scheduleFacade.getAllowedAreaList("schedule:place");
		if (areaList == null || areaList.size() == 0) {
			String msg = "Current user has no right to view any site's data!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}

		logger.debug("/schedule/status/list: " + vo);
		return scheduleFacade.getScheduleListByStartEndTime(vo);
	}

	@RequestMapping(value = "/schedule/status/online")
	@ResponseBody
	@RequiresPermissions(value = { "schedule:place" })
	public JSONObject scheduelStatusOnline(@RequestParam(value = "id") Long id) {
		logger.debug("/schedule/online: " + id);
		boolean result = scheduleFacade.auditPOOnline(id);
		if (result) {
			return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
		} else {
			String msg = "Failure to online PO '" + id + "'!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
	}

	@RequestMapping(value = "/schedule/status/offline")
	@ResponseBody
	@RequiresPermissions(value = { "schedule:place" })
	public JSONObject scheduelStatusOffline(@RequestParam(value = "id") Long id) {
		logger.debug("/schedule/online: " + id);
		boolean result = scheduleFacade.auditPOOffline(id);
		if (result) {
			return generateRespJsonStr(ScheduleUtil.CODE_OK, ScheduleUtil.RESULT_OK, null);
		} else {
			String msg = "Failure to offline PO '" + id + "'!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}
	}
	
	
	@RequestMapping(value = "/schedule/rest/productlist")
	@ResponseBody
	@RequiresPermissions(value = { "audit:productlist" })
	public JSONObject scheduleRestProductList(
			@RequestParam(value = "province", required = false) Long provinceId,
			@RequestParam(value = "status", required = false) Integer status, 
			@RequestParam(value = "poId", required = false) Long poId,
			@RequestParam(value = "limit", required = false) int limit,
			@RequestParam(value = "offset", required = false) int offset) {
		return _scheduleRestProductOrList(provinceId, status, poId, true, limit, offset);
	}
	
	@RequestMapping(value = "/schedule/rest/product")
	@ResponseBody
	@RequiresPermissions(value = { "audit:product" })
	public JSONObject scheduleRestProduct(
			@RequestParam(value = "province", required = false) Long provinceId,
			@RequestParam(value = "status", required = false) Integer status, 
			@RequestParam(value = "poId", required = false) Long poId,
			@RequestParam(value = "limit", required = false) int limit,
			@RequestParam(value = "offset", required = false) int offset) {
		
		return _scheduleRestProductOrList(provinceId, status, poId, false, limit, offset);
	}
	
	private JSONObject _scheduleRestProductOrList(Long provinceId, Integer status, Long poId, 
			boolean isPrdList, int limit, int offset) {
		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.pageSize = limit;
		paramDTO.curPage = offset;
		paramDTO.orderByFlag = OrderFlag.ID_DESC;
		
		if (provinceId != null && provinceId != 0) {
			paramDTO.curSupplierAreaId = provinceId;
		}
		
		if (poId != null && poId != 0) {
			paramDTO.poId = poId;
		}

		List<AreaDTO> areaList = null;
		if (isPrdList) {
			areaList = scheduleFacade.getAllowedAreaList("audit:productlist");
		} else {
			areaList = scheduleFacade.getAllowedAreaList("audit:product");
		}
		
		if (areaList == null || areaList.size() == 0) {
			String msg = "Current user has no right to view any site's data!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg);
		}

		List<Long> allowedAreaIdList = new ArrayList<Long>();
		for (AreaDTO area : areaList) {
			allowedAreaIdList.add(area.getId());
		}
		paramDTO.allowedAreaIdList = allowedAreaIdList;
		
		List<ScheduleState> statusList = new ArrayList<ScheduleState>();
		statusList.add(ScheduleState.PASSED);
		statusList.add(ScheduleState.BACKEND_PASSED);
		paramDTO.poStatusList = statusList;
		
		ScheduleListVO vo = null;
		if (isPrdList) {
			vo = scheduleFacade.getScheduleListForPrdOrListAudit(paramDTO, status, true);
		} else {
			vo = scheduleFacade.getScheduleListForPrdOrListAudit(paramDTO, status, false);
		}

		List<IdNameBean> allSiteList = scheduleFacade.getAllProvince();
		return ScheduleUtil.generatePOListStrForPrdAudit(vo, allSiteList);
	}
	
}
