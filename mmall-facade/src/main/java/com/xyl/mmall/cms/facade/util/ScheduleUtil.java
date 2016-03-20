package com.xyl.mmall.cms.facade.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.netease.push.util.JSONUtils;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleChannelDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.JITMode;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.enums.SupplyMode;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.meta.SchedulePage;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleUtil {

	public static final int CODE_OK = 200;

	public static final int CODE_ERR = 400;

	public static final int CODE_PO_ADD_PRD_ERR = 404;

	public static final int PO_DURATION = 8; // 10 days

	public static final int PO_MAX_LOGIN_INTERVAL = 9; // 9 days

	public static final int PO_NORMAL_DURATION = 5; // 5 days

	public static final int PO_FINAL_SEE_DURATION = 3;

	public static final int PO_EXTENSION_DURATION = 0; // 3 days;

	public static final long UNLIKE_PO_DURATION = 1000 * 60 * 60 * 24 * 15L; // 15
																				// day

	public static final int FORBIDDEN_SUBMIT_QUOTA = 6; // 4 days

	public static final boolean RESULT_OK = true;

	public static final boolean RESULT_ERR = false;

	public static final String RESP_CODE = "code";

	public static final String RESP_RESULT = "result";

	public static final String RESP_TOTAL = "total";

	public static final String RESP_MSG = "msg";

	public static final String RESP_LIST = "list";

	public static final String MODE_CATEGORYLIST = "categoryList";

	public static final String MODE_STATUSLIST = "statusList";

	public static final String MODE_SCHEDULE = "schedule";

	public static final String MODE_PROVINCELIST = "provinceList";

	public static final String MODE_BRANDLIST = "brandList";

	public static final String REQ_PARAM_NAME = "name";

	public static final String REQ_PARAM_LIMIT = "limit";

	public static final String REQ_PARAM_OFFSET = "offset";

	public static final String REQ_PARAM_ID = "id";

	public static final String REQ_PARAM_DESC = "desc";

	public static final String REQ_PARAM_REASON = "reason";

	public static final String REQ_PARAM_PONUM = "ponum";

	public static final String REQ_PARAM_CRATEBEGIN = "createBegin";

	public static final String REQ_PARAM_CREATESTOP = "createStop";

	public static final String REQ_PARAM_STARTBEGIN = "startBegin";

	public static final String REQ_PARAM_STARTEND = "startStop";

	public static final String REQ_PARAM_ENDBEGIN = "endBegin";

	public static final String REQ_PARAM_ENDSTOP = "endStop";

	public static final String REQ_PARAM_STATUS = "status";

	public static final String REQ_PARAM_TYPE = "type";

	public static final String REQ_PARAM_KEY = "key";

	public static final String REQ_PARAM_VALUE = "value";

	public static final String REQ_PARAM_SCHEDULEID = "scheduleId";

	public static final String REQ_PARAM_PAGEID = "pageId";

	public static final String REQ_PARAM_BANNERID = "id";

	public static final String REQ_PARAM_IDS = "ids";

	public static final String REQ_PARAM_BANNERSTARTIMG = "bannerStartImg";

	public static final String REQ_PARAM_BANNERNEWIMG = "bannerNewImg";

	public static final String REQ_PARAM_COMMENT = "comment";

	public static final String REQ_PARAM_STARTDATE = "startDate";

	public static final String REQ_PARAM_ENDDATE = "endDate";

	public static final String REQ_PARAM_SUPPLIERAREAID = "curSupplierAreaId";

	public static final String REQ_PARAM_SUPPLIERNAME = "supplierName";

	public static final String REQ_PARAM_BRANDNAME = "brandName";

	public static final String REQ_PARAM_imgCategory = "imgCategory";

	public static final String REQ_PARAM_productCategory = "productCategory";

	public static final String REQ_PARAM_udProductIds = "udProductIds";

	public static final String REQ_PARAM_udSetting = "udSetting";

	public static final String REQ_PARAM_bgImgId = "bgImgId";

	public static final String REQ_PARAM_bgSetting = "bgSetting";

	public static final String REQ_PARAM_headerImgId = "headerImgId";

	public static final String REQ_PARAM_headerSetting = "headerSetting";

	public static final String REQ_PARAM_allListPartVisiable = "allListPartVisiable";

	public static final String REQ_PARAM_allListPartOthers = "allListPartOthers";

	public static final String REQ_PARAM_mapPartVisiable = "mapPartVisiable";

	public static final String REQ_PARAM_mapPartOthers = "mapPartOthers";

	public static final String REQ_PARAM_udImgIds = "udImgIds";

	public static final String REQ_PARAM_brandId = "brandId";

	public static int getScheduleState(Schedule po) {
		int scheduleState = -2;
		long now = System.currentTimeMillis();
		if (po.getStartTime() > now) {
			scheduleState = -2;
		} else if (po.getEndTime() < now) {
			scheduleState = -1;
		} else {
			scheduleState = 0;
		}
		return scheduleState;
	}

	public static long getUserId() {
		return SecurityContextUtils.getUserId();
	}

	public static String getUserName() {
		return SecurityContextUtils.getUserName();
	}

	public static List<Long> getItemListByItemListStr(String listStr) {
		List<Long> list = new ArrayList<Long>();
		if (StringUtils.isEmpty(listStr)) {
			return list;
		}

		String[] arr = listStr.split(",");
		for (String item : arr) {
			list.add(Long.parseLong(item));
		}

		return list;
	}

	public static JSONObject getPageLayoutJson(PODTO poDTO) {
		JSONObject json = new JSONObject();
		SchedulePage page = poDTO.getPageDTO().getPage();
		json.put("udImgIds", page.getUdImgIds());
		json.put("udProductIds", page.getUdProductIds());
		JSONArray obj = null;
		try {
			obj = JSON.parseArray(page.getUdSetting());
		} catch (Exception e) {
		}
		json.put("udSetting", obj);
		json.put("bgImgId", page.getBgImgId() + POBaseUtil.NULL_STR);
		JSONObject obj2 = null;
		try {
			obj2 = JSON.parseObject(page.getBgSetting());
		} catch (Exception e) {
		}
		json.put("bgSetting", obj2);
		json.put("headerImgId", page.getHeaderImgId() + POBaseUtil.NULL_STR);
		obj2 = null;
		try {
			obj2 = JSONUtils.toJSONObject(page.getHeaderSetting());
		} catch (Exception e) {
		}
		json.put("headerSetting", obj2);
		json.put("allListPartVisiable", page.isAllListPartVisiable());
		obj2 = null;
		try {
			obj2 = JSON.parseObject(page.getAllListPartOthers());
		} catch (Exception e) {
		}
		json.put("allListPartOthers", obj2);
		json.put("mapPartVisiable", page.isMapPartVisiable());
		obj2 = null;
		try {
			obj2 = JSON.parseObject(page.getMapPartOthers());
		} catch (Exception e) {
		}
		json.put("mapPartOthers", obj2);
		json.put("id", page.getId() + POBaseUtil.NULL_STR);
		json.put("scheduleId", page.getScheduleId() + POBaseUtil.NULL_STR);
		json.put("brandId", page.getBrandId() + POBaseUtil.NULL_STR);
		return json;
	}

	public static ScheduleCommonParamDTO getComParamDTO(long poId, long userId, long supplierId,
			long curSupplierAreaId, long startDate, long endDate, int curPage, int pageSize) {
		ScheduleCommonParamDTO dto = new ScheduleCommonParamDTO();
		dto.poId = poId;
		dto.userId = userId;
		dto.supplierId = supplierId;
		dto.curSupplierAreaId = curSupplierAreaId;
		dto.startDate = startDate;
		dto.endDate = endDate;
		dto.curPage = curPage;
		dto.pageSize = pageSize;

		return dto;
	}

	public static List<IdNameBean> convertAreaList(List<AreaDTO> areaList) {
		if (areaList == null) {
			return new ArrayList<IdNameBean>();
		}

		List<IdNameBean> list = new ArrayList<IdNameBean>();
		for (AreaDTO area : areaList) {
			IdNameBean bean = new IdNameBean();
			bean.setId(area.getId() + POBaseUtil.NULL_STR);
			bean.setName(area.getAreaName());
			list.add(bean);
		}

		return list;
	}

	public static boolean needTotal(JSONObject paramJson) {
		return paramJson.getBooleanValue("total");
	}

	public static boolean isLongEmpty(Long val) {
		return val == null || val == 0;
	}

	public static boolean isStringEmpty(String val) {
		return val == null || "".equals(val.trim());
	}

	public static JSONObject geneJsonObjForValidList(ScheduleListVO vo, List<IdNameBean> warehouseList,
			List<IdNameBean> allSiteList) {
		JSONObject json = new JSONObject();
		JSONArray result = new JSONArray();
		List<PODTO> poList = vo.getPoList().getPoList();
		json.put("code", CODE_OK);
		json.put("result", result);

		for (PODTO po : poList) {
			JSONObject poJson = ScheduleUtil.genScheduleJson(po.getScheduleDTO(), allSiteList, null, warehouseList);
			poJson.put("promotionDesc", po.getPromotionDesc());
			poJson.put("curDate", DateUtils.parseDateToString(DateUtils.DATE_FORMAT, Calendar.getInstance().getTime()));
			if (po.getBannerDTO() != null && po.getBannerDTO().getBanner() != null) {
				poJson.put("banner", genJsonObjFromBanner(po.getBannerDTO().getBanner()));
			}
			result.add(poJson);
		}

		return json;
	}

	public static JSONObject geneJsonObjForBannerList(ScheduleListVO vo, List<IdNameBean> warehouseList,
			List<IdNameBean> allSiteList) {
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray list = new JSONArray();

		json.put("code", CODE_OK);
		json.put("result", result);
		result.put("list", list);

		List<PODTO> poList = vo.getPoList().getPoList();
		for (PODTO po : poList) {
			JSONObject poJson = ScheduleUtil.genScheduleJson(po.getScheduleDTO(), allSiteList, null, warehouseList);
			if (po.getBannerDTO() != null && po.getBannerDTO().getBanner() != null) {
				poJson.put("banner", genJsonObjFromBanner(po.getBannerDTO().getBanner()));
			}
			poJson.put("reason", po.getBannerDTO().getBanner().getStatusMsg() != null ? po.getBannerDTO().getBanner()
					.getStatusMsg() : "");
			poJson.put("showFlag", po.getShowFlag());
			list.add(poJson);
		}

		return json;
	}

	public static JSONObject geneBannerListJsonObj(ScheduleListVO vo, Map<Long, String> areaMap,
			Map<Long, String> supplierIdAccountMap) {
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray list = new JSONArray();
		json.put("code", CODE_OK);
		json.put("result", result);
		result.put("list", list);

		List<PODTO> poList = vo.getPoList().getPoList();
		for (PODTO po : poList) {
			JSONObject bannerJson = genJsonObjFromBanner(po.getBannerDTO().getBanner());
			bannerJson.put("supplierName", supplierIdAccountMap.get(po.getBannerDTO().getBanner().getSupplierId()));
			//bannerJson.put("siteName", areaMap.get(po.getBannerDTO().getBanner().getSaleAreaId()));
			bannerJson.put("showFlag", po.getShowFlag());
			bannerJson.put("brandName", getCombinedBrandName(po.getScheduleDTO().getSchedule().getBrandNameEn(), 
					po.getScheduleDTO().getSchedule().getBrandName()));
			
			List<IdNameBean> saleSiteList = new ArrayList<IdNameBean>();
			List<ScheduleSiteRela> siteRelaList = po.getScheduleDTO().getSiteRelaList();
			for (ScheduleSiteRela siteRela : siteRelaList) {
				IdNameBean bean = new IdNameBean();
				bean.setId(siteRela.getSaleSiteId() + POBaseUtil.NULL_STR);
				bean.setName(areaMap.get(siteRela.getSaleSiteId()));
				saleSiteList.add(bean);
			}
			bannerJson.put("saleSiteList", saleSiteList);
			if (po.getScheduleDTO().getSchedule().getStatus() == ScheduleState.NULL) {
				bannerJson.put("status", -1);
			}
			
			bannerJson.put("startTime", po.getScheduleDTO().getSchedule().getStartTime());
			bannerJson.put("endTime", po.getScheduleDTO().getSchedule().getEndTime());
			
			list.add(bannerJson);
		}

		return json;
	}

	public static JSONObject genJsonObjFromBanner(ScheduleBanner banner) {
		JSONObject bannerJson = new JSONObject();
		bannerJson.put("id", banner.getId() + POBaseUtil.NULL_STR);
		bannerJson.put("userId", banner.getUserId() + POBaseUtil.NULL_STR);
		bannerJson.put("userName", banner.getUserName());
		bannerJson.put("scheduleId", banner.getScheduleId() + POBaseUtil.NULL_STR);
		//bannerJson.put("curSupplierAreaId", banner.getSaleAreaId() + POBaseUtil.NULL_STR);
		bannerJson.put("supplierId", banner.getSupplierId() + POBaseUtil.NULL_STR);
		bannerJson.put("supplierName", banner.getSupplierName());
		bannerJson.put("brandName", banner.getBrandName());
		bannerJson.put("brandNameEn", banner.getBrandNameEn());
		bannerJson.put("brandId", banner.getBrandId() + POBaseUtil.NULL_STR);
		bannerJson.put("homeBannerImgUrl", banner.getHomeBannerImgUrl());
		bannerJson.put("preBannerImgUrl", banner.getPreBannerImgUrl());
		bannerJson.put("comment", banner.getComment());
		if (banner.getStatus() != null) {
			bannerJson.put("status", banner.getStatus().getIntValue());
		} else {
			bannerJson.put("status", CheckState.NULL.getIntValue());
		}
		bannerJson.put("submitDate", banner.getSubmitDate());
		bannerJson.put("statusUpdateDate", banner.getStatusUpdateDate());
		bannerJson.put("statusMsg", banner.getStatusMsg());
		bannerJson.put("auditUserId", banner.getAuditUserId() + POBaseUtil.NULL_STR);
		bannerJson.put("auditUserName", banner.getAuditUserName());
		bannerJson.put("createDate", banner.getCreateDate());
		bannerJson.put("updateDate", banner.getUpdateDate());

		return bannerJson;
	}
	
	public static JSONObject geneJsonObjForMonthList(ScheduleListVO vo, List<IdNameBean> warehouseList,
			List<IdNameBean> allSiteList, Long provinceId) {
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		List<PODTO> poList = vo.getPoList().getPoList();
		JSONObject dateList = new JSONObject();
		json.put("code", CODE_OK);
		json.put("result", result);
		result.put("data", dateList);

//		Map<String, JSONArray> map = new ConcurrentHashMap<String, JSONArray>();
//		for (PODTO po : poList) {
//			Schedule schedule = po.getScheduleDTO().getSchedule();
//			List<ScheduleSiteRela> siteRelaList = po.getScheduleDTO().getSiteRelaList();
//			System.out.println("Dealing "+schedule.getId() + "; provinceId=" + provinceId);
//			long startTime = schedule.getStartTime();
//			Calendar tmpC = Calendar.getInstance();
//			tmpC.setTimeInMillis(startTime);
//
//			// int m = tmpC.get(Calendar.MONTH) + 1;
//			int dayOfMonth = tmpC.get(Calendar.DAY_OF_MONTH);
//			JSONObject poJson = ScheduleUtil.genScheduleJson(po.getScheduleDTO(), allSiteList, null, warehouseList);
//			for (ScheduleSiteRela siteRela : siteRelaList) {
//				if (siteRela.getSaleSiteId() == provinceId.longValue()) {
//					poJson.put("showOrder", siteRela.getShowOrder());
//					break;
//				}
//			}
//			poJson.put("poStatus", po.getPoStatus());
//			poJson.put("showFlag", po.getShowFlag());
//			if (map.containsKey(dayOfMonth + "")) {
//				map.get(dayOfMonth + "").add(poJson);
//			} else {
//				JSONArray tmpArr = new JSONArray();
//				tmpArr.add(poJson);
//				map.put(dayOfMonth + "", tmpArr);
//			}
//		}
//
//		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
//			String key = iter.next();
//			dateList.put(key, map.get(key));
//		}
		
		Map<String, List<JSONObject>> map = new ConcurrentHashMap<String, List<JSONObject>>();
		for (PODTO po : poList) {
			Schedule schedule = po.getScheduleDTO().getSchedule();
			List<ScheduleSiteRela> siteRelaList = po.getScheduleDTO().getSiteRelaList();
			long startTime = schedule.getStartTime();
			Calendar tmpC = Calendar.getInstance();
			tmpC.setTimeInMillis(startTime);

			// int m = tmpC.get(Calendar.MONTH) + 1;
			int dayOfMonth = tmpC.get(Calendar.DAY_OF_MONTH);
			JSONObject poJson = ScheduleUtil.genScheduleJson(po.getScheduleDTO(), allSiteList, null, warehouseList);
			for (ScheduleSiteRela siteRela : siteRelaList) {
				if (siteRela.getSaleSiteId() == provinceId.longValue()) {
					poJson.put("showOrder", siteRela.getShowOrder());
					break;
				}
			}
			poJson.put("poStatus", po.getPoStatus());
			poJson.put("showFlag", po.getShowFlag());
			if (map.containsKey(dayOfMonth + "")) {
				map.get(dayOfMonth + "").add(poJson);
			} else {
				List<JSONObject> tmpArr = new ArrayList<JSONObject>();
				tmpArr.add(poJson);
				map.put(dayOfMonth + "", tmpArr);
//				JSONArray tmpArr = new JSONArray();
//				tmpArr.add(poJson);
//				map.put(dayOfMonth + "", tmpArr);
			}
		}

		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			List<JSONObject> list = map.get(key);
			Collections.sort(list, new Comparator<JSONObject>() {

				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					int order1 = o1.getIntValue("showOrder");
					int order2 = o2.getIntValue("showOrder");
					if (order1 < order2) {
						return -1;
					} else if (order1 == order2) {
						return 0;
					} else {
						return 1;
					}
				}
				
			});
			dateList.put(key, list);
		}

		return json;
	}

	public static JSONObject genSchedulePageJson(SchedulePage page) {
		JSONObject json = new JSONObject();
		json.put("id", page.getId() + POBaseUtil.NULL_STR);
		json.put("title", page.getTitle());
		json.put("scheduleId", page.getScheduleId() + POBaseUtil.NULL_STR);
		json.put("supplierName", page.getSupplierName());
		json.put("brandName", page.getBrandName());
		json.put("brandNameEn", page.getBrandNameEn());
		json.put("brandId", page.getBrandId() + POBaseUtil.NULL_STR);
		json.put("statusUpdateDate", page.getStatusUpdateDate());
		json.put("status", page.getStatus().getIntValue());
		json.put("statusMsg", page.getStatusMsg());
		json.put("comment", page.getComment());
		return json;
	}

	public static JSONObject genScheduleJson(ScheduleDTO scheduleDTO, List<IdNameBean> allSiteList,
			Map<Long, String> areaMap, List<IdNameBean> warehouseList) {
		JSONObject scheduleJson = POBaseUtil.toJSON(scheduleDTO.getSchedule());
		JSONObject viceJson = POBaseUtil.toJSON(scheduleDTO.getScheduleVice());
		List<ScheduleSiteRela> siteRelaList = scheduleDTO.getSiteRelaList();
		Set<String> keySet = viceJson.keySet();
		for (String key : keySet) {
			scheduleJson.put(key, viceJson.get(key));
		}
		scheduleJson.put("id", scheduleDTO.getSchedule().getId() + POBaseUtil.NULL_STR);
		scheduleJson.put("brandName", getCombinedBrandName(scheduleDTO.getSchedule().getBrandNameEn(), 
				scheduleDTO.getSchedule().getBrandName()));
		scheduleJson.remove("scheduleId");
		scheduleJson.remove("saleAreaId");
		scheduleJson.remove("storeAreaId");

		if (warehouseList != null && warehouseList.size() > 0) {
			Map<Long, String> storeMap = new ConcurrentHashMap<Long, String>();
			for (IdNameBean bean : warehouseList) {
				storeMap.put(Long.parseLong(bean.getId()), bean.getName());
			}
			long supplierStoreId = scheduleDTO.getScheduleVice().getSupplierStoreId();
			long brandStoreId = scheduleDTO.getScheduleVice().getBrandStoreId();
			scheduleJson.put("supplierStoreName", storeMap.get(supplierStoreId));
			scheduleJson.put("brandStoreName", storeMap.get(brandStoreId));
		}
		
		if (allSiteList != null && allSiteList.size() > 0) {
			List<IdNameBean> saleSiteList = new ArrayList<IdNameBean>();
			for (ScheduleSiteRela siteRela : siteRelaList) {
				for (IdNameBean bean : allSiteList) {
					if (siteRela.getSaleSiteId() == Long.parseLong(bean.getId())) {
						saleSiteList.add(bean);
					}
				}
			}
			scheduleJson.put("saleSiteList", saleSiteList);
		}
		
		String adPosition = scheduleDTO.getScheduleVice().getAdPosition();
		List<String> posList = new ArrayList<String>();
		if (adPosition != null && !"".equals(adPosition)) {
			adPosition = adPosition.substring(1, adPosition.length() - 1);
			String[] arr = adPosition.split(",");
			for (int i = 0; i < arr.length; i++) {
				posList.add(arr[i].trim());
			}
		}
		scheduleJson.put("adPosition", posList);

		if (scheduleDTO.getSchedule().getFlagAuditPrdList() <= 0) {
			scheduleJson.put("flagAuditPrdList", 1);
		} else {
			scheduleJson.put("flagAuditPrdList", scheduleDTO.getSchedule().getFlagAuditPrdList());
		}

//		if (areaMap != null) {
//			scheduleJson.put("curSupplierAreaName", areaMap.get(scheduleDTO.getSchedule().getCurSupplierAreaId()));
//		}

		return scheduleJson;
	}

	public static ScheduleDTO parseParams(JSONObject paramJson, boolean updateFlag, boolean saveflag,
			List<IdNameBean> allSiteList) {
		ScheduleDTO scheduleDTO = new ScheduleDTO();

		Schedule schedule = new Schedule();
		ScheduleVice vice = new ScheduleVice();
		List<ScheduleSiteRela> siteRelaList = new ArrayList<ScheduleSiteRela>();
		scheduleDTO.setSchedule(schedule);
		scheduleDTO.setScheduleVice(vice);
		scheduleDTO.setSiteRelaList(siteRelaList);

		// basic info
		if (updateFlag) {
			schedule.setId(paramJson.getLongValue("id"));
			vice.setScheduleId(schedule.getId());
		}

		if (paramJson.containsKey("title")) {
			schedule.setTitle(paramJson.getString("title"));
		}

		if (paramJson.containsKey("pageTitle")) {
			schedule.setPageTitle(paramJson.getString("pageTitle"));
		}
		
		if (paramJson.containsKey("supplierAcct")) {
			vice.setSupplierAcct(paramJson.getString("supplierAcct"));
		}
		if (paramJson.containsKey("poFollowerUserName")) {
			vice.setPoFollowerUserName(paramJson.getString("poFollowerUserName"));
		}
		if (paramJson.containsKey("supplyMode")) {
			int supplyMode = paramJson.getIntValue("supplyMode");
			if (supplyMode == 1) {
				vice.setSupplyMode(SupplyMode.SELF);
			} else if (supplyMode == 2) {
				vice.setSupplyMode(SupplyMode.TOGETHER);
			} else {
				vice.setSupplyMode(SupplyMode.NULL);
			}
		}
		if (paramJson.containsKey("supplierStoreId")) {
			try {
				vice.setSupplierStoreId(paramJson.getLongValue("supplierStoreId"));
			} catch (Exception e) {
				// ignore
			}
		}
		if (paramJson.containsKey("brandStoreId")) {
			try {
				vice.setBrandStoreId(paramJson.getLongValue("brandStoreId"));
			} catch (Exception e) {
				// ignore
			}
		}

		if (paramJson.containsKey("startTime")) {
			long startTime = paramJson.getLongValue("startTime");
			Calendar start = Calendar.getInstance();
			start.setTimeInMillis(startTime);
			start.set(Calendar.HOUR_OF_DAY, 10);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);
			startTime = start.getTimeInMillis();

			//long endTime = calculateScheduleEndTime(startTime);

			schedule.setStartTime(startTime);
			//schedule.setEndTime(endTime);
		} else {
			schedule.setStartTime(0);
		}
		
		if (paramJson.containsKey("endTime")) {
			long endTime = paramJson.getLongValue("endTime");
			Calendar end = Calendar.getInstance();
			end.setTimeInMillis(endTime);
			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);
			end.set(Calendar.MILLISECOND, 999);
			schedule.setEndTime(end.getTimeInMillis());
		} else {
			schedule.setEndTime(0);
		}
		
		if (paramJson.containsKey("startTime") && paramJson.containsKey("endTime")) {
			int diff = daysBetween(schedule.getStartTime(), schedule.getEndTime());
			schedule.setNormalShowPeriod(diff);
			schedule.setExtShowPeriod(0);
		}
		
		if (paramJson.containsKey("saleSiteIds")) {
			JSONArray idArr = paramJson.getJSONArray("saleSiteIds");
			boolean allCountryFlag = false;
			for (int i = 0; i < idArr.size(); i++) {
				if ("1".equals(idArr.get(i))) {
					allCountryFlag = true;
					break;
				}
			}
			
			if (allCountryFlag) {
				for (IdNameBean site : allSiteList) {
					ScheduleSiteRela siteRela = new ScheduleSiteRela();
					siteRela.setSaleSiteId(Long.parseLong(site.getId()));
					if (updateFlag) {
						siteRela.setScheduleId(schedule.getId());
					} 
					siteRela.setShowOrder(1);
					siteRela.setPoStartTime(schedule.getStartTime());
					siteRelaList.add(siteRela);
				}
			} else {
				for (int i = 0; i < idArr.size(); i++) {
					ScheduleSiteRela siteRela = new ScheduleSiteRela();
					siteRela.setSaleSiteId(Long.parseLong(idArr.getString(i)));
					if (updateFlag) {
						siteRela.setScheduleId(schedule.getId());
					} 
					siteRela.setShowOrder(1);
					siteRela.setPoStartTime(schedule.getStartTime());
					siteRelaList.add(siteRela);
				}
			}
		}
		
		if (paramJson.containsKey("adPosition")) {
			String adPosition = paramJson.getString("adPosition");
			if (adPosition.indexOf("[") == -1 && adPosition.indexOf("]") == -1) {
				adPosition = "[" + adPosition + "]";
			}
			vice.setAdPosition(adPosition);
		}

		long now = System.currentTimeMillis();
		if (!updateFlag) {
			schedule.setCreateTimeForLogic(now);
		}

		schedule.setStatus(ScheduleState.DRAFT.genEnumByIntValue(paramJson.getIntValue("status")));

		schedule.setUpdateTimeForLogic(now);
		schedule.setScheduleUpdateDate(now);

		if (paramJson.containsKey("platformSrvFeeRate")) {
			vice.setPlatformSrvFeeRate(paramJson.getBigDecimal("platformSrvFeeRate"));
		}
		if (paramJson.containsKey("maxPriceAfterDiscount")) {
			vice.setMaxPriceAfterDiscount(paramJson.getBigDecimal("maxPriceAfterDiscount"));
		}
		if (paramJson.containsKey("minPriceAfterDiscount")) {
			vice.setMinPriceAfterDiscount(paramJson.getBigDecimal("minPriceAfterDiscount"));
		}
		if (paramJson.containsKey("productTotalCnt")) {
			vice.setProductTotalCnt(paramJson.getIntValue("productTotalCnt"));
		}
		if (paramJson.containsKey("maxDiscount")) {
			schedule.setMaxDiscount(paramJson.getBigDecimal("maxDiscount"));
		}
		if (paramJson.containsKey("minDiscount")) {
			schedule.setMinDiscount(paramJson.getBigDecimal("minDiscount"));
		}
		if (paramJson.containsKey("unitCnt")) {
			vice.setUnitCnt(paramJson.getIntValue("unitCnt"));
		}
		if (paramJson.containsKey("skuCnt")) {
			vice.setSkuCnt(paramJson.getIntValue("skuCnt"));
		}

		schedule.setJitMode(JITMode.JIT);

		schedule.setFlagAuditPrdList(1);
		vice.setFlagAuditBanner(0);
		vice.setFlagAuditPage(0);

		return scheduleDTO;
	}

	public static String getCombinedBrandName(String brandNameEn, String brandNameZH) {
		brandNameEn = (brandNameEn==null) ? "" : brandNameEn;
		brandNameZH = (brandNameZH==null) ? "" : brandNameZH;
		if ("".equals(brandNameEn.trim())) {
			return brandNameZH;
		} 
		if ("".equals(brandNameZH.trim())) {
			return brandNameEn;
		}
		if (!"".equals(brandNameEn.trim()) && !"".equals(brandNameZH.trim())) {
			if (isChinese(brandNameZH)) {
				return brandNameEn + "/" + brandNameZH;
			} else {
				return brandNameEn;
			}
		}
		
		return brandNameZH;
	}
	
	public static JSONObject generatePOListStr2(ScheduleListVO vo, List<AreaDTO> areaList,
			Map<Long, String> supplierIdAccountMap, List<IdNameBean> warehouseList, List<IdNameBean> allSiteList) {
		Map<Long, String> areaMap = new ConcurrentHashMap<Long, String>();
		if (areaList != null) {
			for (AreaDTO area : areaList) {
				areaMap.put(area.getId(), area.getAreaName());
			}
		}

		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		List<PODTO> poList = vo.getPoList().getPoList();
		JSONArray arr = new JSONArray();
		json.put("code", CODE_OK);
		json.put("result", result);
		result.put("total", vo.getPoList().getTotal());
		result.put("list", arr);

		long now = System.currentTimeMillis();
		for (PODTO po : poList) {
			JSONObject poJson = ScheduleUtil.genScheduleJson(po.getScheduleDTO(), allSiteList, areaMap, warehouseList);
			poJson.put("supplierName", supplierIdAccountMap.get(po.getScheduleDTO().getSchedule().getSupplierId()));
			//poJson.put("siteName", areaMap.get(po.getScheduleDTO().getSchedule().getCurSupplierAreaId()));
			poJson.put("curTime", now);
			poJson.put("showFlag", po.getShowFlag());
			poJson.put("brandName", getCombinedBrandName(po.getScheduleDTO().getSchedule().getBrandNameEn(), 
					po.getScheduleDTO().getSchedule().getBrandName()));
			// add area name field
			// JSONObject jsonStoreSaleInfo =
			// poJson.getJSONObject("storeSaleInfo");
			// long storeId = jsonStoreSaleInfo.getLongValue("storeAreaId");
			// long saleId = jsonStoreSaleInfo.getLongValue("saleAreaId");
			// jsonStoreSaleInfo.put("storeAreaName", areaMap.get(storeId));
			// jsonStoreSaleInfo.put("saleAreaName", areaMap.get(saleId));

			arr.add(poJson);
		}

		return json;
	}

	public static JSONObject generatePOListStr(ScheduleListVO vo, List<AreaDTO> areaList,
			List<IdNameBean> warehouseList, List<IdNameBean> allSiteList) {
		Map<Long, String> areaMap = new ConcurrentHashMap<Long, String>();
		if (areaList != null) {
			for (AreaDTO area : areaList) {
				areaMap.put(area.getId(), area.getAreaName());
			}
		}

		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		List<PODTO> poList = vo.getPoList().getPoList();
		JSONArray arr = new JSONArray();
		json.put("code", CODE_OK);
		json.put("result", result);
		result.put("total", vo.getPoList().getTotal());
		result.put("list", arr);

		long now = System.currentTimeMillis();
		for (PODTO po : poList) {
			JSONObject poJson = ScheduleUtil.genScheduleJson(po.getScheduleDTO(), allSiteList, areaMap, warehouseList);
			//poJson.put("siteName", areaMap.get(po.getScheduleDTO().getSchedule().getCurSupplierAreaId()));
			poJson.put("curTime", now);
			poJson.put("showFlag", po.getShowFlag());

			// add area name field
			// JSONObject jsonStoreSaleInfo =
			// poJson.getJSONObject("storeSaleInfo");
			// long storeId = jsonStoreSaleInfo.getLongValue("storeAreaId");
			// long saleId = jsonStoreSaleInfo.getLongValue("saleAreaId");
			// jsonStoreSaleInfo.put("storeAreaName", areaMap.get(storeId));
			// jsonStoreSaleInfo.put("saleAreaName", areaMap.get(saleId));
			
			arr.add(poJson);
		}

		return json;
	}
	
	public static JSONObject generatePOListStrForPrdAudit(ScheduleListVO vo, List<IdNameBean> allSiteList) {
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		List<PODTO> poList = vo.getPoList().getPoList();
		JSONArray arr = new JSONArray();
		json.put("code", CODE_OK);
		json.put("result", result);
		result.put("total", vo.getPoList().getTotal());
		result.put("list", arr);

		long now = System.currentTimeMillis();
		for (PODTO po : poList) {
			Schedule schedule = po.getScheduleDTO().getSchedule();
			ScheduleVice vice = po.getScheduleDTO().getScheduleVice();
			List<ScheduleSiteRela> siteRelaList = po.getScheduleDTO().getSiteRelaList();
			JSONObject poJson = new JSONObject();
			poJson.put("id", schedule.getId());

			StringBuilder saleSiteNameList = new StringBuilder();
			for (ScheduleSiteRela siteRela : siteRelaList) {
				for (IdNameBean bean : allSiteList) {
					if (siteRela.getSaleSiteId() == Long.parseLong(bean.getId())) {
						saleSiteNameList.append(bean.getName()).append(",");
					}
				}
			}
			String saleSiteNameStr = saleSiteNameList.toString();
			saleSiteNameStr = saleSiteNameStr.substring(0, saleSiteNameStr.length()-1);
			
			poJson.put("siteName", saleSiteNameStr);
			poJson.put("supplierAcct", vice.getSupplierAcct());
			poJson.put("supplierName", schedule.getSupplierName());
			poJson.put("brandName", ScheduleUtil.getCombinedBrandName(schedule.getBrandNameEn(), schedule.getBrandName()));
			poJson.put("startTime", schedule.getStartTime());
			poJson.put("endTime", schedule.getEndTime());
			poJson.put("status", po.getPoStatus()); // 2 or 3 or 4
			if (schedule.getStartTime() < now) {
				poJson.put("isStart", true);
			} else {
				poJson.put("isStart", false);
			}
			poJson.put("isValid", po.isValid());
			
			arr.add(poJson);
		}

		return json;
	}

	public static JSONObject generatePOListJsonForBackendPagesMgr(ScheduleListVO vo, List<IdNameBean> warehouseList,
			List<IdNameBean> allSiteList) {
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray arr = new JSONArray();
		json.put("code", CODE_OK);
		json.put("result", result);
		result.put("list", arr);

		for (PODTO po : vo.getPoList().getPoList()) {
			JSONObject poJson = ScheduleUtil.genScheduleJson(po.getScheduleDTO(), allSiteList, null, warehouseList);
			poJson.put("pageId", po.getPageDTO().getPage().getId() + POBaseUtil.NULL_STR);
			poJson.put("reason", po.getPageDTO().getPage().getStatusMsg() != null ? po.getPageDTO().getPage()
					.getStatusMsg() : "");
			poJson.put("showFlag", po.getShowFlag());
			arr.add(poJson);
		}

		return json;
	}

	public static JSONObject generatePOListJsonForCMSPagesMgr(ScheduleListVO vo, Map<Long, String> areaMap,
			Map<Long, String> supplierIdAccountMap) {
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		List<PODTO> poList = vo.getPoList().getPoList();
		JSONArray arr = new JSONArray();
		json.put("code", CODE_OK);
		json.put("result", result);
		result.put("list", arr);

		for (PODTO po : poList) {
			JSONObject pageJson = ScheduleUtil.genSchedulePageJson(po.getPageDTO().getPage());
			pageJson.put("startTime", po.getScheduleDTO().getSchedule().getStartTime());
			pageJson.put("endTime", po.getScheduleDTO().getSchedule().getEndTime());
			pageJson.put("supplierName", supplierIdAccountMap.get(po.getPageDTO().getPage().getSupplierId()));
			//pageJson.put("siteName", areaMap.get(po.getPageDTO().getPage().getSaleAreaId()));
			pageJson.put("showFlag", po.getShowFlag());
			pageJson.put("brandName", getCombinedBrandName(po.getScheduleDTO().getSchedule().getBrandNameEn(), 
					po.getScheduleDTO().getSchedule().getBrandName()));
			
			List<IdNameBean> saleSiteList = new ArrayList<IdNameBean>();
			List<ScheduleSiteRela> siteRelaList = po.getScheduleDTO().getSiteRelaList();
			for (ScheduleSiteRela siteRela : siteRelaList) {
				IdNameBean bean = new IdNameBean();
				bean.setId(siteRela.getSaleSiteId() + POBaseUtil.NULL_STR);
				bean.setName(areaMap.get(siteRela.getSaleSiteId()));
				saleSiteList.add(bean);
			}
			pageJson.put("saleSiteList", saleSiteList);
			
			if (po.getScheduleDTO().getSchedule().getStatus() == ScheduleState.NULL) {
				pageJson.put("status", -1);
			}
			
			
			arr.add(pageJson);
		}

		return json;
	}

	public static JSONArray convertChannelToJSONArray(List<ScheduleChannelDTO> channelList) {
		JSONArray result = new JSONArray();
		if (channelList == null) {
			return result;
		}

		for (ScheduleChannelDTO chl : channelList) {
			JSONObject item = POBaseUtil.toJSON(chl);
			result.add(item);
		}

		return result;
	}
	
	public static Calendar getPOStartTime(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c;
	}
	
	public static long calculateScheduleEndTime(long startTime) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(startTime);
		c.add(Calendar.DAY_OF_MONTH, PO_DURATION - 1);

		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		return c.getTimeInMillis();
	}

	public static Calendar getBeginTimeOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	public static Calendar getEndTimeOfMonth(int year, int month) {
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR, year);
		c2.set(Calendar.MONTH, month - 1);
		c2.set(Calendar.DAY_OF_MONTH, 1);
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		{
			int m = c2.get(Calendar.MONTH);
			// calculate last day
			c2.add(Calendar.DAY_OF_MONTH, 31);
			if (c2.get(Calendar.MONTH) != m) {
				c2.set(Calendar.YEAR, year);
				c2.set(Calendar.MONTH, month - 1);
				c2.set(Calendar.DAY_OF_MONTH, 1);
				c2.add(Calendar.DAY_OF_MONTH, 30);
				if (c2.get(Calendar.MONTH) != m) {
					c2.set(Calendar.YEAR, year);
					c2.set(Calendar.MONTH, month - 1);
					c2.set(Calendar.DAY_OF_MONTH, 1);
					c2.add(Calendar.DAY_OF_MONTH, 29);
					if (c2.get(Calendar.MONTH) != m) {
						c2.set(Calendar.YEAR, year);
						c2.set(Calendar.MONTH, month - 1);
						c2.set(Calendar.DAY_OF_MONTH, 1);
						c2.add(Calendar.DAY_OF_MONTH, 28);
						if (c2.get(Calendar.MONTH) != m) {
							c2.set(Calendar.YEAR, year);
							c2.set(Calendar.MONTH, month - 1);
							c2.set(Calendar.DAY_OF_MONTH, 1);
							c2.add(Calendar.DAY_OF_MONTH, 27);
						}
					}
				}
			}
		}

		return c2;
	}

	public static void main(String[] args) {
		Long l = null;
		System.out.println(isLongEmpty(l));
		String str = null;
		System.out.println(isStringEmpty(str));

		System.out.println(getBeginTimeOfMonth(2014, 9).getTime());
		System.out.println(getEndTimeOfMonth(2014, 9).getTime());
	}

	public static JSONObject generateRespJsonStr(int code, boolean result, String msg) {
		JSONObject json = new JSONObject();
		json.put(ScheduleUtil.RESP_CODE, code);
		json.put(ScheduleUtil.RESP_RESULT, result);
		if (msg != null) {
			json.put(ScheduleUtil.RESP_MSG, msg);
		}

		return json;
	}

	public static POListDTO filterPOList(POListDTO oldPoList) {
		if (oldPoList.getPoList() != null && oldPoList.getPoList().size() != 0) {
			long now = System.currentTimeMillis();
			for (PODTO poDTO : oldPoList.getPoList()) {
				setPOShowFlag(poDTO, now);
			}
		}

		return oldPoList;
	}

	public static void setPOShowFlag(PODTO poDTO, long now) {
		Schedule po = poDTO.getScheduleDTO().getSchedule();
		setPOShowTime(po);
		long poStart = po.getStartTime();
		long poEnd = po.getEndTime();
		if (poEnd <= now) { // PO over
			poDTO.setShowFlag(1);
		} else if (poStart < now && poEnd > now) { // PO in sale
			poDTO.setShowFlag(2);
		} else {
			if (checkSubmit(po)) {
				poDTO.setShowFlag(4); // out of 4 days
			} else {
				poDTO.setShowFlag(3); // in 4 days
			}
		}
	}

	public static Calendar getTodayLastTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		return c;
	}

	public static Calendar getTodayBeginTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	public static Calendar getSpecificBeginTime(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	public static Calendar getSpecificEndTime(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		return c;
	}

	public static JSONArray getBrandShopJsonArr(List<BrandShopDTO> shopList) {
		JSONArray shops = new JSONArray();
		for (BrandShopDTO shop : shopList) {
			JSONObject shopJson = new JSONObject();
			
			shopJson.put("shopName", shop.getShopName());
			shopJson.put("province", shop.getProvince());
			shopJson.put("city", shop.getCity());
			shopJson.put("district", shop.getDistrict());
			shopJson.put("street", shop.getStreet());
			shopJson.put("shopAddr", shop.getShopAddr());
			shopJson.put("shopZone", shop.getShopZone());
			shopJson.put("shopTel", shop.getShopTel());
			shopJson.put("shopContact", shop.getShopContact());
			shopJson.put("longitude", shop.getLongitude());
			shopJson.put("latitude", shop.getLatitude());

			shops.add(shopJson);
		}

		return shops;
	}

	public static <T> ArrayList<T> convert2ArrayList(List<T> list) {
		ArrayList<T> result = new ArrayList<T>();
		for (T item : list) {
			result.add(item);
		}
		return result;
	}

	public static Map<Long, String> convertAreaList2Map(List<AreaDTO> areaList) {
		Map<Long, String> areaMap = new ConcurrentHashMap<Long, String>();
		for (AreaDTO area : areaList) {
			areaMap.put(area.getId(), area.getAreaName());
		}

		return areaMap;
	}

	/**
	 * Calculate days between specific startTime and endTime. Note that the
	 * result will include the day where startTime in. Such as if startTime and
	 * endTime at the same day, will return 1;
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int daysBetween(long startTime, long endTime) {
		if (startTime > endTime) {
			return -1;
		}

		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(startTime);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(endTime);
		c2.set(Calendar.HOUR_OF_DAY, 23);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.SECOND, 59);
		c2.set(Calendar.MILLISECOND, 999);

		return (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000 * 3600 * 24)) + 1;
	}

	public static boolean checkSubmit(Schedule po) {
		long now = System.currentTimeMillis();
		int diff = daysBetween(now, po.getStartTime());
		if (diff < FORBIDDEN_SUBMIT_QUOTA) {
			return false;
		}
		return true;
	}

	public static void setPOShowTime(Schedule schedule) {
		long startTime = schedule.getStartTime();
		long endTime = schedule.getEndTime();

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(startTime);
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		startTime = c.getTimeInMillis();
		schedule.setStartTime(startTime);

		if (endTime == 0) {
			endTime = calculateScheduleEndTime(startTime);
		}
		c = Calendar.getInstance();
		c.setTimeInMillis(endTime);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		endTime = c.getTimeInMillis();
		schedule.setEndTime(endTime);
	}

	public static void batchSetPOShowTime(POListDTO poList) {
		if (poList == null || poList.getPoList() == null) {
			return;
		}

		for (PODTO po : poList.getPoList()) {
			setPOShowTime(po.getScheduleDTO().getSchedule());
		}
	}

	public static long calculatePONewEndTime(long startTime, int daysAfter) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(startTime);
		c.add(Calendar.DAY_OF_MONTH, daysAfter);
		long newPOEndTime = ScheduleUtil.getSpecificEndTime(c.getTimeInMillis()).getTimeInMillis();

		return newPOEndTime;
	}

	public static void setPOMainStatus(List<PODTO> poList) {
		// set PO status
		long now = System.currentTimeMillis();
		for (PODTO poDTO : poList) {
			Schedule schedule = poDTO.getScheduleDTO().getSchedule();
			ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
			ScheduleUtil.setPOShowTime(schedule);
			boolean isSkuPass = poDTO.isSkuPass();
			boolean isPrdPass = poDTO.isProductPass();
			int pageFlag = vice.getFlagAuditPage();
			int bannerFlag = vice.getFlagAuditBanner();
			if (!isSkuPass || !isPrdPass || pageFlag == 0 || bannerFlag == 0) {
				if (schedule.getStartTime() < now) {
					poDTO.setPoStatus(6);
				} else {
					poDTO.setPoStatus(1);
				}
			}

			if (isSkuPass && isPrdPass && pageFlag == 1 && bannerFlag == 1) { // all
																				// passed
				if (schedule.getStatus() == ScheduleState.PASSED) {
					if (schedule.getStartTime() > now) {
						poDTO.setPoStatus(2); // ready for online
					} else {
						poDTO.setPoStatus(6); // invalid
					}
				} else if (schedule.getStatus() == ScheduleState.BACKEND_PASSED) { // online
					if (schedule.getStartTime() > now) {
						poDTO.setPoStatus(3); // ready to start
					} else {
						if (schedule.getEndTime() < now) {
							poDTO.setPoStatus(4); // PO is over
						} else {
							poDTO.setPoStatus(5); // in sale now
						}
					}
				} else if (schedule.getStatus() == ScheduleState.OFFLINE) { // online
					poDTO.setPoStatus(4);
				} else {
					// others are wrong status
					poDTO.setPoStatus(6); // invalid
				}
			}
		}
	}
	
	public static void setPOPrdStatus(PODTO poDTO) {
		int prdStatus = poDTO.getScheduleDTO().getScheduleVice().getFlagAuditPrdzl();
		int prdListStatus = poDTO.getScheduleDTO().getScheduleVice().getFlagAuditPrdqd();

		if (prdListStatus == StatusType.APPROVAL.getIntValue()) {
			poDTO.setSkuPass(true);
		} else {
			poDTO.setSkuPass(false);
		}

		if (prdStatus == StatusType.APPROVAL.getIntValue()) {
			poDTO.setProductPass(true);
		} else {
			poDTO.setProductPass(false);
		}
	}
	
	public static int getPOMainStatus(PODTO poDTO, long now) {
		Schedule schedule = poDTO.getScheduleDTO().getSchedule();
		ScheduleVice vice = poDTO.getScheduleDTO().getScheduleVice();
		ScheduleUtil.setPOShowTime(schedule);
		boolean isSkuPass = poDTO.isSkuPass();
		boolean isPrdPass = poDTO.isProductPass();
		int pageFlag = vice.getFlagAuditPage();
		int bannerFlag = vice.getFlagAuditBanner();
		if (!isSkuPass || !isPrdPass || pageFlag == 0 || bannerFlag == 0) {
			if (schedule.getStartTime() < now) {
				return 6;
			} else {
				return 1;
			}
		}

		if (isSkuPass && isPrdPass && pageFlag == 1 && bannerFlag == 1) { // all
																			// passed
			if (schedule.getStatus() == ScheduleState.PASSED) {
				if (schedule.getStartTime() > now) {
					return 2; // ready for online
				} else {
					return 6; // invalid
				}
			} else if (schedule.getStatus() == ScheduleState.BACKEND_PASSED) { // online
				if (schedule.getStartTime() > now) {
					return 3; // ready to start
				} else {
					if (schedule.getEndTime() < now) {
						return 4; // PO is over
					} else {
						return 5; // in sale now
					}
				}
			} else if (schedule.getStatus() == ScheduleState.OFFLINE) { // online
				return 4;
			} else {
				// others are wrong status
				return 6; // invalid
			}
		}
		
		return 6;
	}

	public static void sortPOListForMainsite(POListDTO poList) {
		poList.setPoList(convert2ArrayList(poList.getPoList()));
		Collections.sort(poList.getPoList(), new Comparator<PODTO>() {
			@Override
			public int compare(PODTO po1, PODTO po2) {
				long diff = po1.getScheduleDTO().getSchedule().getStartTime()
						- po2.getScheduleDTO().getSchedule().getStartTime();
				if (diff > 0L) {
					return -1;
				} else if (diff == 0L) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		
		Collections.sort(poList.getPoList(), new Comparator<PODTO>() {
			@Override
			public int compare(PODTO po1, PODTO po2) {
				Calendar c1 = Calendar.getInstance();
				c1.setTimeInMillis(po1.getScheduleDTO().getSchedule().getStartTime());

				Calendar c2 = Calendar.getInstance();
				c2.setTimeInMillis(po2.getScheduleDTO().getSchedule().getStartTime());

				// same day
				if ((c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
						&& (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
						&& (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))) {
					Schedule s1 = po1.getScheduleDTO().getSchedule();
					Schedule s2 = po2.getScheduleDTO().getSchedule();
					// first, sort by show order ASC
					int showOrderDiff = s1.getShowOrder() - s2.getShowOrder();
					if (showOrderDiff > 0) {
						return 1;
					} else if (showOrderDiff == 0) {
//						if (s1.getShowOrder() == 0 && s2.getShowOrder() == 0) {
//							// no need sort further
//							return 0;
//						} else {
							// if same order, then compare createTime DESC
							long createTimeDiff = s1.getCreateTimeForLogic() - s2.getCreateTimeForLogic();
							if (createTimeDiff > 1L) {
								return -1;
							} else if (createTimeDiff == 0L) {
								return 0;
							} else {
								return 1;
							}
//						}
					} else {
						return -1;
					}
				} else {
					return 0;
				}
			}
		});

	}
	
	public static boolean isPOUpdating(JSONObject scheduleJsonParam) {
		boolean updateFlag = false;
		if (scheduleJsonParam.getLong("id") != null && scheduleJsonParam.getLong("id") != 0) {
			updateFlag = true;
		}
		
		return updateFlag;
	}

	public static boolean isInSameDay(long t1, long t2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(t1);

		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(t2);

		return ((c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) && (c1
				.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)));
	}
	
	public static boolean canRejectPO (ScheduleDTO scheduleDTO) {
		Schedule schedule = scheduleDTO.getSchedule();
		ScheduleVice vice = scheduleDTO.getScheduleVice();
		long now = System.currentTimeMillis();
		if (schedule.getStatus() == ScheduleState.BACKEND_PASSED
				&& schedule.getFlagAuditPrdList() == StatusType.APPROVAL.getIntValue()
				&& vice.getFlagAuditBanner() == 1
				&& vice.getFlagAuditPage() == 1
				&& schedule.getStartTime() < now
				&& schedule.getEndTime() > now) {
			return false;
		}
		
		if (schedule.getStartTime() < now 
				&& (schedule.getFlagAuditPrdList() == StatusType.APPROVAL.getIntValue()
						|| vice.getFlagAuditBanner() == 1
						|| vice.getFlagAuditPage() == 1)) {
			return false;
		}
		
		return true;
	}
	
	
	public static void setPISPOValidFlag(POListDTO poList) {
		for (PODTO po : poList.getPoList()) {
			if (!isPISPOValid(po)) {
				//po.setShowFlag(5); // invalid
				po.getScheduleDTO().getSchedule().setStatus(ScheduleState.NULL);
			}
		}
	}
	
	private static boolean isPISPOValid(PODTO po) {
		long now = System.currentTimeMillis();
		Schedule schedule = po.getScheduleDTO().getSchedule();
		if (schedule.getEndTime() < now) {  // PO offline
			return false;
		}
		
		if (schedule.getStartTime() < now && (
				schedule.getStatus() == ScheduleState.DRAFT
				|| schedule.getStatus() == ScheduleState.CHECKING
				|| schedule.getStatus() == ScheduleState.REJECTED
				|| schedule.getStatus() == ScheduleState.NULL)) {
			return false;
		}
		
		return true;
	}
	
	public static void setPOValidFlag(POListDTO poList) {
		for (PODTO po : poList.getPoList()) {
			if (!isPOValid(po)) {
				//po.setShowFlag(5); // invalid
				po.getScheduleDTO().getSchedule().setStatus(ScheduleState.NULL);
			}
		}
	}
	
	private static boolean isPOValid(PODTO po) {
		long now = System.currentTimeMillis();
		Schedule schedule = po.getScheduleDTO().getSchedule();
		if (schedule.getEndTime() < now) {  // PO offline
			return false;
		}
		
		ScheduleVice vice = po.getScheduleDTO().getScheduleVice();
		// invalid
		if (schedule.getStartTime() < now &&
				(schedule.getFlagAuditPrdList() != 3 || vice.getFlagAuditBanner() != 1
				|| vice.getFlagAuditPage() != 1)) {
			return false;
		}
		
		// invalid
		if (schedule.getStartTime() < now && schedule.getStatus() == ScheduleState.PASSED 
				&& (schedule.getFlagAuditPrdList() == 3 
				&& vice.getFlagAuditBanner() == 1 && vice.getFlagAuditPage() == 1)) {
			return false;
		}
		
		return true;
	}
	
	public static void POListPager(POListDTO poList, int offset, int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 0) {
			limit = 0;
		}

		poList.setTotal(poList.getPoList().size());
		poList.setHasNext(hasNext(poList.getTotal(), offset, limit));
		
		if (limit != 0) {
			List<PODTO> filteredPOList = new ArrayList<PODTO>();
			for (int i = offset; i < (offset + limit); i++) {
				if (i < poList.getPoList().size()) {
					filteredPOList.add(poList.getPoList().get(i));
				}
			}
			poList.setPoList(filteredPOList);
		}
	}
	
	private static boolean hasNext(int total, int curPage, int pageSize) {
		if (curPage != 0 && pageSize != 0) {
			return curPage * pageSize < total;
		}
		return false;
	}
	
	public static List<Long> getPOSaleSiteCodeList(List<ScheduleSiteRela> siteRelaList) {
		List<Long> siteFlagCodeList = new ArrayList<Long>();
		for (ScheduleSiteRela siteRela : siteRelaList) {
			siteFlagCodeList.add(siteRela.getSaleSiteId());
		}
		
		return siteFlagCodeList;
	}
	
	private static boolean isChinese(String str) {
		if(str == null || "".equals(str.trim())) {
			return false;
		}
		
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
	                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
	                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
	            return true;
	        }
		}
		return false;
    }
}
