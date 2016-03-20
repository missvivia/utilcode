/**
 * 
 */
package com.xyl.mmall.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.enums.PromotionContentTab;
import com.xyl.mmall.cms.facade.ContentConfigureFacade;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.common.enums.FileTypeEnum;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.framework.config.FileDirConfiguration;
import com.xyl.mmall.framework.poi.ExcelDataExtracter;
import com.xyl.mmall.framework.poi.IExcelDataExtracter;
import com.xyl.mmall.framework.poi.IllegalConfigException;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.mainsite.facade.IPServiceFacade;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.util.MainsiteHelper;
import com.xyl.mmall.mainsite.util.QrqmUtils;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.mainsite.vo.MainSiteDataVo;
import com.xyl.mmall.mainsite.vo.MainsiteIndexVO;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author hzlihui2014
 *
 */
@Controller
public class IndexController {

	@Autowired
	private PromotionContentFacade promotionContentFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private BrandFacade brandFacade;

	@Autowired
	private IPServiceFacade iPServiceFacade;

	@Autowired
	private MainBrandFacade mainBrandFacade;

	@Autowired
	private MainsiteHelper mainsiteHelper;

	@Autowired
	private FileDirConfiguration fileDirConfiguration;

	@Autowired
	private ItemProductService itemProductService;

	@Autowired
	private ContentConfigureFacade contentConfigureFacade;

	// 轮翻推广
	private static final String PROMOTION_CONTENT = "promotionContent";

	// 最新特卖
	private static final String SCHEDULE_TODAY = "scheduleToday";

	// 入驻品牌
	private static final String BRAND_ENTERED = "brandEntered";

	// 上新预告
	private static final String PRE_SCHEDULE = "preSchedule";

	// schedule中首页的tab值
	private static final long INDEX_TAB_OF_SCHEDULE = 1;

	// 推广中首页的tab值
	private static final int INDEX_TAB_OF_PROMOTIONCONTENT = PromotionContentTab.INDEX.getIntValue();

	private static final int MAX_SIZE_FOR_BRAND_ENTERED = 60;

	// 在档期模块中，传入0，代表不限制返回条数
	private static final int NO_LIMIT_SIZE_OF_SCHEDULE = 0;

	private static final int PO_OF_PROMOTION_CONTENT_TYPE = 0;

	private static final String SCHEDULEID_PARAM = "?scheduleId=";

	// 预热结束时间：2015-01-16 10:00:00 - 1421373600000L
	// 测试时间：2015-01-09 00:00:00 - 1420732800000L
	private static final long PREHEAT_END_TIME = 1421373600000L;

	private static final String PREHEAT_WAP_SITE = "http://023.baiwandian.cn/preheat/wap";

	@Value("${wapsite.url}")
	private String wapsiteUrl;

	/**
	 * mainsite-web 首页
	 * 
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@BILog(action = "page", type = "homePage", clientType = "wap")
	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String home(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (inPreheat()) {
			WebUtils.issueRedirect(request, response, PREHEAT_WAP_SITE);
			return "";
		}

		long userId = SecurityContextUtils.getUserId();
		MainsiteIndexVO mainsiteIndexVO = mainsiteHelper.getMainsitMainsiteIndexVO(userId);
		model.addAttribute("mainsiteIndexVO", mainsiteIndexVO);
//		long curSupplierAreaId = AreaUtils.getAreaCode();
//
//		UserLoginBean userLoginBean = QrqmUtils.getCurIPLastLoginTime(request, response);
//
//		Date curDate = new Date();

		/**
		 * 1.轮翻推广
		 */
//		List<PromotionContent> promotionContentList = this.getPromotionContentByParam(curDate, curSupplierAreaId,
//				INDEX_TAB_OF_PROMOTIONCONTENT);
//		model.addAttribute(PROMOTION_CONTENT, promotionContentList);

		/**
		 * 2.最新特卖
		 */
		// JSONArray jsonArray = this.getOnlineScheduleForIndex(userLoginBean,
		// curSupplierAreaId, curDate);
//		model.addAttribute(SCHEDULE_TODAY, new JSONArray(0));

		/**
		 * 3.上新预告,分别取后四天数据
		 */
		// model.addAttribute(PRE_SCHEDULE, this.getPreScheduleMap(userId,
		// curSupplierAreaId, curDate));

		/**
		 * 4.入驻品牌
		 */
		// List<BrandItemDTO> brandList =
		// this.getBrandEntered(curSupplierAreaId);
		// model.addAttribute(BRAND_ENTERED, brandList);
		
//		try {
//			IExcelDataExtracter excel = new ExcelDataExtracter(new FileInputStream(new File("src/main/resources/public/data/wapIndex.xlsx")));
//			Map<String, Object> data = excel.getAllData();
//			final String keyOfGoodsList = "goodsList";
//			@SuppressWarnings({ "unchecked", "rawtypes" })
//			List<Long> goodsList = (List) data.get(keyOfGoodsList);
//			Set<Long> nonExists = contentConfigureFacade.validateSkuExists(goodsList);
//			if (!nonExists.isEmpty()) {
//				System.out.println("不存在商品SKU" + Arrays.toString(nonExists.toArray(new Long[0])));
//			}
//			data.put(keyOfGoodsList, contentConfigureFacade.getSKUBy(goodsList));//itemProductService.getProductSKUDTOByProdIds(goodsList)
//			model.addAllAttributes(data);
//			System.out.println(JSON.toJSONString(data));
//		} catch (EncryptedDocumentException | InvalidFormatException | IOException | IllegalConfigException e) {
//			model.addAttribute("code", ResponseCode.RES_EUNKNOWN);
//			model.addAttribute("message", e.getMessage());
//		}

		return "pages/index/index";
	}

	@RequestMapping("/page/category")
	public String toCategoryPage() {
		return "pages/category/index";
	}

	@RequestMapping("/rest/category")
	public @ResponseBody BaseJsonVO getCategoryList(Long curSupplierAreaId) {
		if (curSupplierAreaId == null || curSupplierAreaId < 1) {
			curSupplierAreaId = AreaUtils.getAreaCode();
		}
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ResponseCode.RES_SUCCESS);
		MainSiteDataVo vo = this.getHomeDataByStrategy(curSupplierAreaId);
		result.setResult(vo);

		return result;
	}

	private MainSiteDataVo getHomeDataByStrategy(long curSupplierAreaId) {
		Calendar calendar = Calendar.getInstance();
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		MainSiteDataVo mainSiteDataVo;
		if (currentHour >= CodeInfoUtil.PO_BEGIN_HOUR) {
			/**
			 * 新数据
			 */
			mainSiteDataVo = mainsiteHelper.getMainSiteFromFreshCache(curSupplierAreaId);
		} else {
			/**
			 * 旧数据
			 */
			mainSiteDataVo = mainsiteHelper.getMainSiteFromOldCache(curSupplierAreaId);
		}
		return mainSiteDataVo;
	}

	// 拼接url
	private List<PromotionContent> getPromotionContentByParam(Date curDate, long curSupplierAreaId,
			int tabOfPromotionContent) {
		List<PromotionContent> resList = promotionContentFacade.getMobilePCByProvTimeDevice(curDate.getTime(),
				curSupplierAreaId);
		if (resList == null || resList.size() <= 0) {
			return resList;
		}

		for (PromotionContent promotionContent : resList) {
			if (!promotionContent.getPlatformType().toLowerCase().contains("ios") || promotionContent.getOnline() == 1) {
				continue;
			}
			if (promotionContent.getPromotionType() == PO_OF_PROMOTION_CONTENT_TYPE) {
				String poUrl = wapsiteUrl + CodeInfoUtil.PO_URL + SCHEDULEID_PARAM + promotionContent.getBusinessId();
				promotionContent.setActivityUrl(poUrl);
			}
		}
		return resList;
	}

	private JSONArray getOnlineScheduleForIndex(UserLoginBean bean, long curSupplierAreaId, Date curDate) {
		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForChl(bean, INDEX_TAB_OF_SCHEDULE,
				curSupplierAreaId, 0, 0, 0);
		JSONObject jsonObj = ScheduleUtil.geneJsonObjForValidList(scheduleListVO, null, null);
		return this.getJSONArrayByJSONObject(jsonObj);
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

	/**
	 * brand search
	 * 
	 * @return
	 */
	private List<BrandItemDTO> getBrandEntered(long areaId) {
		return mainBrandFacade.getRecommendBrandItemList(areaId, 0, MAX_SIZE_FOR_BRAND_ENTERED, false);
	}

	private boolean inPreheat() {
		return System.currentTimeMillis() < PREHEAT_END_TIME;
	}

	/**
	 * 活动
	 * 
	 * @return
	 */
	@RequestMapping(value = "/activity/rule")
	public String getActivityRulePage() {
		if (System.currentTimeMillis() < OnlineActivityConstants.ACTIVITY_END_TIME) {
			return "pages/activity/rule";
		}
		return "pages/activity/rule0228";
	}

	@RequestMapping(value = "/activity/cooperate/coupon")
	public String getActivityCooperate() {
		return "pages/activity/coupon";
	}

	@RequestMapping("/index/data")
	@ResponseBody
	public BaseJsonVO getExcelData4IndexPage() {
		BaseJsonVO result = new BaseJsonVO();
		try {
			IExcelDataExtracter excel = new ExcelDataExtracter(new File(
					fileDirConfiguration.getIndexDataFileDir() + FileTypeEnum.WAP_INDEX_FILE.getFileName()));
			result.setCode(ResponseCode.RES_SUCCESS);
			result.setResult(excel.getAllData());
		} catch (EncryptedDocumentException | InvalidFormatException | IOException | IllegalConfigException e) {
			result.setCode(ResponseCode.RES_EUNKNOWN);
			result.setMessage(e.getMessage());
		}
		return result;
	}
}
