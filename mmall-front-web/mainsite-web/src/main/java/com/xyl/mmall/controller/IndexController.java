package com.xyl.mmall.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.exceljar.ExcelUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.facade.ContentConfigureFacade;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.common.enums.FileTypeEnum;
import com.xyl.mmall.content.annotation.CollectBubbleActivity;
import com.xyl.mmall.content.annotation.PresentProductActivity;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.framework.config.FileDirConfiguration;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.mainsite.facade.IPServiceFacade;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.util.MainsiteHelper;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.mainsite.vo.MainSiteDataVo;
import com.xyl.mmall.mainsite.vo.MainsiteIndexVO;

@Controller
@RequestMapping("")
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
	private LocationService locationService;

	@Autowired
	private FileDirConfiguration fileDirConfiguration;

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

	// ip地址
	private static final String LOCATION_NAME = "locationCode";

	// 分类列表
	private static final String CATEGORY_LIST = "categoryList";

	// today
	private static final String TODAY_SCHEDULE = "todaySchedule";

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	private static final String MOBILE_USER_AGENT_KEYWORDS = "(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*";

	private static final String MOBILE_USER_AGENT_KEYWORDS_EXT = "(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-";

	private static final String WAP_SITE_URL = "http://m.023.baiwandian.cn";

	/**
	 * mainsite-web 首页
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@CollectBubbleActivity
	@PresentProductActivity
	@BILog(action = "page", type = "homePage")
	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String home(Model model, Long curSupplierAreaId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// wap跳转
		if (fromMobile(request) && !fouceWeb(request)) {
			WebUtils.issueRedirect(request, response, WAP_SITE_URL);
			return "";
		}
		// 预热活动跳转
		// if (inPreheat()) {
		// return "pages/activity/20150113";
		// }
		// long oldtime = System.currentTimeMillis();
		//
		// if (curSupplierAreaId == null || curSupplierAreaId < 1)
		// curSupplierAreaId = AreaUtils.getAreaCode();
		//
		// UserLoginBean userLoginBean =
		// QrqmUtils.getCurIPLastLoginTime(request, response);
		//
		// MainSiteDataVo mainSiteDataVo =
		// this.getHomeDataByStrategy(curSupplierAreaId);
		MainsiteIndexVO mainsiteIndexVO = mainsiteHelper.getMainsitMainsiteIndexVO(SecurityContextUtils.getUserId());
		model.addAttribute("mainsiteIndexVO", mainsiteIndexVO);

		// /**
		// * 1.轮翻推广
		// */
		// List<PromotionContent> promotionContentList =
		// mainsiteHelper.getPromotionContentByParam(new Date(),
		// curSupplierAreaId, PromotionContentTab.INDEX.getIntValue());
		// model.addAttribute(PROMOTION_CONTENT, promotionContentList);
		//
		// /**
		// * 2.最新特卖
		// */
		// // JSONArray jsonArray =
		// this.getOnlineScheduleForIndex(userLoginBean, curSupplierAreaId,
		// curDate);
		// JSONArray jsonArray = mainSiteDataVo.getLatestNewSchedule();
		// model.addAttribute(SCHEDULE_TODAY, jsonArray);
		//
		// /**
		// * 3.上新预告,分别取后四天数据
		// */
		// model.addAttribute(PRE_SCHEDULE, mainSiteDataVo.getPreScheduleMap());
		//
		// // today's foreshow
		// model.addAttribute(TODAY_SCHEDULE,
		// mainSiteDataVo.getTodayScheduleMap());
		//
		// /**
		// * 4.入驻品牌
		// */
		// List<BrandItemDTO> brandList = mainSiteDataVo.getBrandList();
		// model.addAttribute(BRAND_ENTERED, brandList);

		/**
		 * 5.ip地址
		 */
		// JSONObject jsonObject = new JSONObject();
		// jsonObject.put("id", curSupplierAreaId);
		// jsonObject.put("name",
		// locationService.getLocationNameByCode(curSupplierAreaId, true));
		// model.addAttribute(LOCATION_NAME, jsonObject);
		//
		// /**
		// * 6.内容分类
		// */
		// model.addAttribute(CATEGORY_LIST, mainSiteDataVo.getCategoryList());
		//
		// long cost = System.currentTimeMillis() - oldtime;
		// logger.info("===change index cost:" + cost);
		return "pages/index/index";
	}

	private boolean fouceWeb(HttpServletRequest request) {
		String from = request.getParameter("from");
		return "wap".equalsIgnoreCase(from);
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

	/**
	 * @return
	 */
	private boolean inPreheat() {
		return System.currentTimeMillis() < OnlineActivityConstants.PREHEAT_END_TIME;
	}

	private JSONArray getOnlineScheduleForIndex(UserLoginBean bean, long curSupplierAreaId, Date curDate) {
		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForChl(bean,
				MainsiteHelper.INDEX_TAB_OF_SCHEDULE, curSupplierAreaId, 0, 0, 0);
		JSONObject jsonObj = ScheduleUtil.geneJsonObjForValidList(scheduleListVO, null, null);
		return this.getJSONArrayByJSONObject(jsonObj);
	}

	private JSONArray getJSONArrayByJSONObject(JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("result");
		return jsonArray;
	}

	/**
	 * 判断用户请求是否发自手机设备。
	 * 
	 * @param request
	 * @return
	 */
	private boolean fromMobile(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent");
		if (StringUtils.isBlank(ua)) {
			return false;
		}
		ua = ua.toLowerCase();
		return ua.matches(MOBILE_USER_AGENT_KEYWORDS) || ua.substring(0, 4).matches(MOBILE_USER_AGENT_KEYWORDS_EXT);
	}

	/**
	 * 获取买家首页商品内容,用于首页预览
	 * 
	 * @param prodctfileName
	 * @return
	 */
	@RequestMapping(value = "/content/getMainsiteIndexInfoList", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getMainsiteIndexInfoList(Model model) {
		BaseJsonVO ret = new BaseJsonVO();
		List<Sheet> sheets = ExcelUtil.getSheetList(fileDirConfiguration.getIndexDataFileDir()
				+ FileTypeEnum.WEB_INDEX_FILE.getFileName());
		if (CollectionUtil.isEmptyOfList(sheets)) {
			ret.setCode(201);
			ret.setMessage("该文件不存在");
			return ret;
		}
		Map<Integer, String> excelColumnMap = contentConfigureFacade.getMainsiteIndexDataFromSheet(sheets.get(0));
		if (excelColumnMap.get("201") != null) {
			ret.setCode(201);
			ret.setMessage(excelColumnMap.get("201"));
			return ret;
		}
		ret.setCode(200);
		ret.setResult(excelColumnMap);
		ret.setMessage("successful");
		return ret;
	}
}
