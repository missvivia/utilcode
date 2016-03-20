/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.AlbumFacade;
import com.xyl.mmall.backend.facade.POItemFacade;
import com.xyl.mmall.backend.vo.CategoryVO;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleBannerFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.ScheduleLikeFacade;
import com.xyl.mmall.cms.facade.util.POBaseUtil;
import com.xyl.mmall.cms.facade.util.ScheduleChecker;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.facade.util.BaseChecker.ErrChecker;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.photomgr.meta.AlbumImg;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.SchedulePage;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author hzlihui2014
 *
 */
@Controller
public class ScheduleController {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

	private static final ScheduleChecker checker = new ScheduleChecker(logger);

	// 上新预告
	private static final String PRE_SCHEDULE = "preSchedule";

	// schedule中首页的tab值
	private static final long INDEX_TAB_OF_SCHEDULE = 1;

	// 在档期模块中，传入0，代表不限制返回条数
	private static final int NO_LIMIT_SIZE_OF_SCHEDULE = 0;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private ScheduleLikeFacade likeFacade;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private AlbumFacade albumFacade;

	@Autowired
	private MainBrandFacade mainBrandFacade;

	@Autowired
	private POItemFacade poItemFacadeImpl;

	@Autowired
	private ScheduleBannerFacade bannerFacade;

	@BILog(action = "page", type = "poPage", clientType="wap")
	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	public String mainSiteScheduleDisplay(Model model, @RequestParam(value = "pageId", required = false) Long pageId,
			@RequestParam(value = "scheduleId", required = false) Long scheduleId) {

		return _viewPageDetail(model, pageId, scheduleId, true);
	}

	private String _viewPageDetail(Model model, Long pageId, Long scheduleId, boolean checkAccess) {
		ErrChecker errChecker = checker.checkPageIdAndScheduleId(pageId, scheduleId);
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg).toJSONString();
		}

		PODTO poDTO = null;
		if (pageId != null) {
			poDTO = scheduleFacade.getSchedulePageById(pageId);
		}

		if (scheduleId != null) {
			poDTO = scheduleFacade.getSchedulePageByScheduleId(scheduleId);
		}

		if (poDTO.getPageDTO() == null || poDTO.getPageDTO().getPage() == null) {
			String msg = "Cannot find decorate page by id '" + pageId + "' or '" + scheduleId + "'!!!";
			logger.error(msg);
			// return generateRespJsonStr(ScheduleUtil.CODE_ERR,
			// ScheduleUtil.RESULT_ERR, msg).toJSONString();
			return "pages/404";
		}
		
		// check brand follow state
		Schedule schedule = poDTO.getScheduleDTO().getSchedule();
		long brandId = poDTO.getPageDTO().getPage().getBrandId();
		long userId = SecurityContextUtils.getUserId();
		if (userId <= 0) {
			model.addAttribute("followed", false);
		} else {
			boolean followed = false;
			try {
				followed = mainBrandFacade.getBrandCollectionState(userId, brandId);
			} catch (Exception e) {
				followed = false;
			}
			model.addAttribute("followed", followed);
		}

		if (checkAccess) {
			// check access
			long curUserSiteId = AreaUtils.getAreaCode();
			List<Long> poSiteIdList = new ArrayList<Long>();
			try {
				poSiteIdList = ProvinceCodeMapUtil.getCodeListByProvinceFmt(poDTO.getScheduleDTO().getSchedule()
						.getSaleSiteFlag());
			} catch (Exception e1) {
				logger.error("Error occured when parse siteflag of po '" + poDTO.getScheduleDTO().getSchedule().getId()
						+ "'s flag '" + poDTO.getScheduleDTO().getSchedule().getSaleSiteFlag() + "'!!!", e1);
			}

			if (!poSiteIdList.contains(curUserSiteId)) {
				String msg = "Sorry, current user cannot access PO (saleSiteList=" + poSiteIdList
						+ ") from his province (id=" + curUserSiteId + ")!!";
				logger.error(msg);
				model.addAttribute("pageTitle", schedule.getPageTitle());
				return "pages/schedule/forbidden";
			}

			// Set PO endTime and check whether PO is expire
			// TODO
			// POBaseUtil.calPOQrqmEndTime(schedule);
			if (schedule.getEndTime() < System.currentTimeMillis()) { // PO over
				// redirect to pages\schedule\over.ftl
				String brandName = poDTO.getPageDTO().getPage().getBrandName();
				String brandLogo = schedule.getBrandLogo();
				JSONObject brandJson = new JSONObject();
				brandJson.put("id", brandId);
				brandJson.put("brandNameZh", brandName);
				brandJson.put("logo", brandLogo);
				model.addAttribute("brand", brandJson);
				model.addAttribute("pageTitle", schedule.getPageTitle());
				return "pages/schedule/over";
			}
		}

		Business supplier = businessFacade.getBusinessById(poDTO.getPageDTO().getPage().getSupplierId());
		if (supplier == null) {
			String msg = "Cannot find supplier  by supplier id '" + poDTO.getPageDTO().getPage().getSupplierId()
					+ "'!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg).toJSONString();
		}

		//long now = System.currentTimeMillis();
		//long leftTime = poDTO.getScheduleDTO().getSchedule().getEndTime() - now;

		// scheduleState
		//int scheduleState = ScheduleUtil.getScheduleState(poDTO.getScheduleDTO().getSchedule());

		// 门店列表
		// List<BrandShopDTO> shopList =
		// mainBrandFacade.getBrandShops(supplier.getAreaId(),
		// supplier.getActingBrandId());
//		List<BrandShopDTO> shopList = mainBrandFacade.getBrandShopListBySupplierId(supplier.getId());
//		JSONArray shops = ScheduleUtil.getBrandShopJsonArr(shopList);

		// 图片列表
//		List<AlbumImg> imgList = new ArrayList<AlbumImg>();
//		String imgIds = poDTO.getPageDTO().getPage().getUdImgIds();
//		List<Long> idList = ScheduleUtil.getItemListByItemListStr(imgIds);
//		long bgImgId = poDTO.getPageDTO().getPage().getBgImgId();
//		if (bgImgId != 0) {
//			idList.add(bgImgId);
//		}
//		long headerImgId = poDTO.getPageDTO().getPage().getHeaderImgId();
//		if (headerImgId != 0) {
//			idList.add(headerImgId);
//		}
//		if (idList != null) {
//			imgList = albumFacade.getImgListByIdList(idList);
//		}
//		JSONObject imgJsonMap = new JSONObject();
//		for (AlbumImg img : imgList) {
//			imgJsonMap.put(img.getId() + "", img);
//		}

		// page layout object
//		JSONObject layout = ScheduleUtil.getPageLayoutJson(poDTO);

		// product list
//		JSONObject prdJsonMap = new JSONObject();
//		String prdIds = poDTO.getPageDTO().getPage().getUdProductIds();
//		List<Long> prdIdList = ScheduleUtil.getItemListByItemListStr(prdIds);
//		if (prdIdList != null) {
//			prdJsonMap = scheduleFacade.getPrdListByPrdIdListForMainSite(prdIdList, poDTO);
//		}

		// PO fav
//		boolean poFav = likeFacade.isLike(ScheduleUtil.getUserId(), poDTO.getPageDTO().getPage().getBrandId());
//
//		model.addAttribute("scheduleState", scheduleState);
//		model.addAttribute("followed", poFav);
//		model.addAttribute("leftTime", leftTime);
//		model.addAttribute("startTime", poDTO.getScheduleDTO().getSchedule().getStartTime());
//		model.addAttribute("endTime", poDTO.getScheduleDTO().getSchedule().getEndTime());
//		model.addAttribute("images", imgJsonMap);
//		model.addAttribute("layout", layout);
//		model.addAttribute("shops", shops);
//		model.addAttribute("shops", new JSONArray());
//		model.addAttribute("products", prdJsonMap);
//		model.addAttribute("title", poDTO.getPageDTO().getPage().getTitle());
//		model.addAttribute("pageTitle", poDTO.getScheduleDTO().getSchedule().getPageTitle());

		// for statistic
		model.addAttribute("bi_supplyId", poDTO.getPageDTO().getPage().getSupplierId());
		model.addAttribute("bi_branId", poDTO.getPageDTO().getPage().getBrandId());
		model.addAttribute("bi_poId", poDTO.getPageDTO().getPage().getScheduleId());
		model.addAttribute("bi_branName", poDTO.getPageDTO().getPage().getBrandName());
		long now = System.currentTimeMillis();
		long poStartTime = poDTO.getScheduleDTO().getSchedule().getStartTime();
		long poEndTime = poDTO.getScheduleDTO().getSchedule().getEndTime();
		if (poStartTime > now) {
			model.addAttribute("bi_status", 1);
		} else if (poEndTime > now) {
			model.addAttribute("bi_status", 2);
		}

		//fill ftl
		SchedulePage page = poDTO.getPageDTO().getPage();
		JSONObject scheduleJson = new JSONObject();
		scheduleJson.put("id", schedule.getId());
		scheduleJson.put("startTime", schedule.getStartTime());
		scheduleJson.put("endTime", schedule.getEndTime());
		scheduleJson.put("brandId", page.getBrandId());
		scheduleJson.put("pageTitle", schedule.getPageTitle());
		model.addAttribute("schedule", scheduleJson);
		
		BaseJsonVO baseJsonVO = poItemFacadeImpl.getPoCategory(schedule.getId(), true);
		BaseJsonListResultVO baseJsonListResultVO = (BaseJsonListResultVO)baseJsonVO.getResult();
		@SuppressWarnings("unchecked")
		List<CategoryVO> clist = (List<CategoryVO>)baseJsonListResultVO.getList();
		List<IdNameBean> categoryList = new ArrayList<IdNameBean>();
		for (CategoryVO vo : clist) {
			IdNameBean bean = new IdNameBean();
			bean.setId(vo.getId() + POBaseUtil.NULL_STR);
			bean.setName(vo.getName());
			categoryList.add(bean);
		}
		model.addAttribute("category", categoryList);
		
		List<String> activityList = scheduleFacade.getPromotionByPO(schedule.getId());
		model.addAttribute("activities", activityList);
		
		ScheduleVO vo = bannerFacade.getScheduleBannerByScheduleId(schedule.getId());
		model.addAttribute("homeBanner", vo.getPo().getBannerDTO().getBanner().getHomeBannerImgUrl());
		model.addAttribute("preBanner", vo.getPo().getBannerDTO().getBanner().getPreBannerImgUrl());
		
		return "pages/schedule/schedule";
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

	@RequestMapping(value = "/trailer", method = RequestMethod.GET)
	public String treeGTrailer(HttpServletRequest request, HttpServletResponse response, Model model) {
		long userId = SecurityContextUtils.getUserId();
		long curSupplierAreaId = AreaUtils.getAreaCode();

		Date curDate = new Date();

		/**
		 * 上新预告,分别取后四天数据
		 */
		model.addAttribute(PRE_SCHEDULE, this.getPreScheduleMap(userId, curSupplierAreaId, curDate));
		return "/pages/schedule/trailer";
	}

	private Map<String, JSONArray> getPreScheduleMap(long userId, long curSupplierAreaId, Date curDate) {
		Date nextOneDate = DateUtils.addDay(curDate, 1);
		Date nextTwoDate = DateUtils.addDay(curDate, 2);
		Date nextThreeDate = DateUtils.addDay(curDate, 3);
		Date nextFourDate = DateUtils.addDay(curDate, 4);

		String nextOneDateStr = DateUtils.parseDateToString(DateUtils.DATE_FORMAT, nextOneDate);
		String nextTwoDateStr = DateUtils.parseDateToString(DateUtils.DATE_FORMAT, nextTwoDate);
		String nextThreeDateStr = DateUtils.parseDateToString(DateUtils.DATE_FORMAT, nextThreeDate);
		String nextFourDateStr = DateUtils.parseDateToString(DateUtils.DATE_FORMAT, nextFourDate);

		Map<String, JSONArray> resultMap = new LinkedHashMap<String, JSONArray>();

		// 明天上新预告
		JSONArray jsonArrayAfterOne = this.getPreScheduleResultByParam(1, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);

		// 后天上新预告
		JSONArray jsonArrayAfterTwo = this.getPreScheduleResultByParam(2, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);

		// 大后天上新预告
		JSONArray jsonArrayAfterThree = this.getPreScheduleResultByParam(3, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);

		// 第四天上新预告
		JSONArray jsonArrayAfterFour = this.getPreScheduleResultByParam(4, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);

		this.putScheduleJSONToMap(nextOneDateStr, jsonArrayAfterOne, resultMap);
		this.putScheduleJSONToMap(nextTwoDateStr, jsonArrayAfterTwo, resultMap);
		this.putScheduleJSONToMap(nextThreeDateStr, jsonArrayAfterThree, resultMap);
		this.putScheduleJSONToMap(nextFourDateStr, jsonArrayAfterFour, resultMap);

		return resultMap;
	}

	private void putScheduleJSONToMap(String dateStr, JSONArray jsonArray, Map<String, JSONArray> map) {
		map.put(dateStr, jsonArray);
	}

	private JSONArray getPreScheduleResultByParam(int dayAfterNum, long userId, long curSupplierAreaId, int retSize) {
		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForFuture(userId, INDEX_TAB_OF_SCHEDULE,
				curSupplierAreaId, dayAfterNum, retSize);
		JSONObject jsonObject = ScheduleUtil.geneJsonObjForValidList(scheduleListVO, null, null);
		return this.getJSONArrayByJSONObject(jsonObject);
	}

	private JSONArray getJSONArrayByJSONObject(JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("result");
		return jsonArray;
	}
}
