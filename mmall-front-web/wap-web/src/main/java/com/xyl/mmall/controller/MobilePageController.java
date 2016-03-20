/**
 * 
 */
package com.xyl.mmall.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.util.SerialNumberUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.POItemFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.HelpCenterFacade;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.facade.OrderTraceFacade;
import com.xyl.mmall.mainsite.facade.RedPacketDetailFacade;
import com.xyl.mmall.mainsite.vo.DetailColorVO;
import com.xyl.mmall.mainsite.vo.DetailProductVO;
import com.xyl.mmall.mainsite.vo.DetailPromotionVO;
import com.xyl.mmall.mainsite.vo.HelpContentCategoryVO;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;
import com.xyl.mmall.mobile.facade.MobileAuthcFacade;
import com.xyl.mmall.mobile.facade.vo.MobileUserVO;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.meta.RedPacketDetail;
import com.xyl.mmall.promotion.meta.RedPacketShareRecord;
import com.xyl.mmall.promotion.meta.UserReceiveRecord;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.promotion.service.RedPacketShareRecordService;
import com.xyl.mmall.promotion.service.UserReceiveRecordService;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.security.utils.HttpUtils;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author lihui
 *
 */
@Controller
public class MobilePageController extends BaseController {

	@Autowired
	private HelpCenterFacade helpCenterFacade;

	@Resource
	private MainsiteItemFacade itemFacade;

	@Resource
	private POItemFacade poItemFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private RedPacketDetailFacade redPacketDetailFacade;

	@Autowired
	private OrderTraceFacade orderTraceFacade;

	@Autowired
	private MobileAuthcFacade mobileAuthcFacade;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;

	@Autowired
	private RedPacketShareRecordService redPacketShareRecordService;

	@Autowired
	private UserReceiveRecordService userReceiveRecordService;
	
	@Autowired
	private RedPacketService redPacketService;
	
	@Value("${mobile.categoriesToRemove:32,34}")
	private String categoriesToRemove;
	
	/**
	 * 显示帮助中心页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public String test(Model model,HttpServletRequest request) {
		String agent=request.getHeader("User-Agent");
		String androidPage="pages/webview/down.android";
		String iosPage="pages/webview/down.ios";
		if(StringUtils.isEmpty(agent)){
			return androidPage;
		}
		
		if(agent.indexOf("iPhone")>=0 ||agent.indexOf("iPad")>=0){
			return iosPage;
		}
		return androidPage;
	}

	/**
	 * 显示帮助中心页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/help", method = RequestMethod.GET)
	public String getHelpCenter(Model model) {
		model.addAttribute("navigation", removeUnnecessaryCategory(helpCenterFacade.getHelpCenterLeftNav()));
		return "pages/webview/help.center";
	}

	/**
	 * 关于我们
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/abouts", method = RequestMethod.GET)
	public String abouts(Model model) {
		return "pages/abouts";
	}

	/**
	 * 微信招商页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/wbusiness", method = RequestMethod.GET)
	public String wbusiness(Model model) {
		return "pages/weixin.business";
	}

	/**
	 * 去掉手机端不需要的类目。
	 * 
	 * @param result
	 * @return
	 */
	private BaseJsonVO removeUnnecessaryCategory(BaseJsonVO result) {
		if (null == result.getResult()) {
			return result;
		}
		String categories[] = StringUtils.split(categoriesToRemove, ",");
		BaseJsonListResultVO listResult = (BaseJsonListResultVO) result.getResult();
		@SuppressWarnings("unchecked")
		List<HelpContentCategoryVO> categoryList = (List<HelpContentCategoryVO>) listResult.getList();
		List<HelpContentCategoryVO> newList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(categoryList)) {
			for (HelpContentCategoryVO vo : categoryList) {
				if (!ArrayUtils.contains(categories, String.valueOf(vo.getId()))) {
					if (CollectionUtils.isNotEmpty(vo.getChildren())) {
						List<HelpContentCategoryVO> newChildList = new ArrayList<>();
						for (HelpContentCategoryVO childVO : vo.getChildren()) {
							if (!ArrayUtils.contains(categories, String.valueOf(childVO.getId()))) {
								newChildList.add(childVO);
							}
						}
						vo.setChildren(newChildList);
					}
					newList.add(vo);
				}
			}
		}
		listResult.setList(newList);
		return result;
	}


	/**
	 * 显示商品详情页面
	 * 
	 * @param model
	 * @param id
	 * @param skuId
	 * @return
	 */
	@RequestMapping(value = "/m/product", method = RequestMethod.GET)
	public String getProductInfo(Model model, @RequestParam long id) {
		long loginId = SecurityContextUtils.getUserId();
		DetailProductVO productVO = commonFacade.getDetailPageProduct(id, true, true);
		long brandId = productVO.getBrand().getId();
		int isFollow = itemFacade.isFollowBrand(loginId, brandId);
		productVO.setIsFollow(isFollow);

		JSONObject jsonObject = new JSONObject();
		transForSizeTable(productVO);

		jsonObject.put("product", productVO);
		long poId = Long.valueOf(productVO.getPoId());
		DetailPromotionVO promotionVO = itemFacade.getDetailPagePromotionInfo(poId);
		jsonObject.put("activity", promotionVO);
		List<DetailColorVO> colorList = commonFacade.getDetailPageColorList(poId, productVO.getGoodsNo());
		jsonObject.put("colors", colorList);

		model.addAttribute(ErrorCode.SUCCESS);
		model.addAttribute("data", jsonObject);
		return "pages/webview/product";
	}

	private static void transForSizeTable(DetailProductVO productVO) {
		if (productVO.getHelper() == null || productVO.getHelper().getBody() == null
				|| productVO.getHelper().getBody().size() <= 0) {
			return;
		}
		List<List<?>> sizeProductList = new ArrayList();
		initList(sizeProductList, productVO.getHelper().getBody().get(0).size());
		transferArr(productVO.getHelper().getBody(), sizeProductList);
		productVO.getHelper().setBody(sizeProductList);
	}

	private static List<List<?>> transferArr(List<List<?>> orginalArr, List<List<?>> targetArr) {
		if (orginalArr == null || orginalArr.size() <= 0 || orginalArr.size() == orginalArr.get(0).size()
				|| targetArr == null) {
			return orginalArr;
		}

		int rowNum = orginalArr.size();
		int colNum = orginalArr.get(0).size();

		for (int i = 0; i < rowNum; i++) {
			List<?> orgRowList = orginalArr.get(i);
			for (int j = 0; j < colNum; j++) {
				List targetRowTempList = getListByPos(j, targetArr);
				targetRowTempList.add(i, orgRowList.get(j));
			}

		}

		return targetArr;
	}

	private static List getListByPos(int pos, List<List<?>> paramList) {
		List resList = paramList.get(pos);
		if (resList == null) {
			resList = new ArrayList();
			paramList.add(resList);
		}
		return resList;
	}

	private static void initList(List list, int num) {
		if (num <= 0) {
			return;
		}
		for (int i = 0; i < num; i++) {
			list.add(new ArrayList());
		}
	}

	private boolean getAreaIdOfPo(long poId,long areaId) {
		boolean flag=false;
		ScheduleVO scheduleVo = scheduleFacade.getScheduleById(poId);
		if (scheduleVo == null || scheduleVo.getPo().getScheduleDTO().getSiteRelaList()==null ||
				scheduleVo.getPo().getScheduleDTO().getSiteRelaList().size()<=0) {
			return false;
		}
		List<ScheduleSiteRela> siteRelaList=scheduleVo.getPo().getScheduleDTO().getSiteRelaList();
		for(ScheduleSiteRela rela:siteRelaList){
			if(areaId==rela.getSaleSiteId()){
				flag=true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 手机浏览器的登录页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "pages/login";
	}

	/**
	 * 使用红包。如果非登录用户的话，将需要跳转至登录页面。
	 * 
	 * @param id
	 *            红包的ID
	 * @return
	 */
	@RequestMapping(value = "/m/share/red/apply", method = RequestMethod.GET)
	public String applyRedPacket(@RequestParam("id") String id, Model model) {
		long userId = SecurityContextUtils.getUserId();
		
		BigDecimal amount = BigDecimal.ZERO;
		Long[] ids = SerialNumberUtil.spliteSerial(id, new String[] { "/" });
		if (ids != null && ids.length >= 2) {
			RedPacketShareRecord record = redPacketShareRecordService.getById(ids[0]);
			// 验证
			if (record != null && record.getStartTime() == ids[1]) {
				
				RedPacket redPacket = redPacketService.getRedPacketById(record.getRedPacketId());
				if (redPacket == null || redPacket.getAuditState() != StateConstants.PASS || !redPacket.isValid()) {
					model.addAttribute("state", 0);
					model.addAttribute("amount", amount);
					return "pages/webview/share/red/result";
				}
				
				// 判断是否已经领取
				UserReceiveRecord receiveRecord = userReceiveRecordService.getUserReceiveRecord(userId,
						record.getRedPacketId(), record.getGroupId());
				// 不为空已经领取过
				if (receiveRecord != null) {
					model.addAttribute("state", 1);
					model.addAttribute("receive", 1);
					model.addAttribute("amount", receiveRecord.getCash());
					return "pages/webview/share/red/result";
				}
				
				// 领取一个红包
				RedPacketDetail detail = redPacketDetailFacade.takeRedPacketDetail(userId, record.getRedPacketId(),
						record.getGroupId());
				if (detail != null) {
					// 红包未领完
					amount = detail.getCash();
				}
			}
		}
		model.addAttribute("receive", 0);
		model.addAttribute("state", 1);
		model.addAttribute("amount", amount);
		return "pages/webview/share/red/result";
	}

	/**
	 * 显示分享单品页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/share/product", method = RequestMethod.GET)
	public String getShareProductInfo(Model model, @RequestParam long id) {
		appendStaticMethod(model);
		long loginId = SecurityContextUtils.getUserId();
		DetailProductVO productVO = commonFacade.getDetailPageProduct(id, true, true);
		long brandId = productVO.getBrand().getId();
		int isFollow = itemFacade.isFollowBrand(loginId, brandId);
		productVO.setIsFollow(isFollow);

		transForSizeTable(productVO);

		model.addAttribute("product", productVO);
		long poId = Long.valueOf(productVO.getPoId());
		DetailPromotionVO promotionVO = itemFacade.getDetailPagePromotionInfo(poId);
		model.addAttribute("activity", promotionVO);
		List<DetailColorVO> colorList = commonFacade.getDetailPageColorList(poId, productVO.getGoodsNo());
		model.addAttribute("colors", colorList);

		model.addAttribute(ErrorCode.SUCCESS);

		// po startTime and endTime
		Schedule schedule = this.getScheduleById(poId);
		if (schedule != null) {
			model.addAttribute("activeFlag", this.getActiveFlagforPoShare(schedule));
			model.addAttribute("startTime", schedule.getStartTime());
			model.addAttribute("endTime", schedule.getEndTime());
		}

		long curSupplierAreaId = AreaUtils.getAreaCode();
		boolean flag = this.getAreaIdOfPo(poId,curSupplierAreaId);
		model.addAttribute("canAccess", flag ? 1 : 0);

		return "pages/webview/share/product";
	}

	/**
	 * 显示物流信息页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/logistics", method = RequestMethod.GET)
	public String getLogisticsInfo(@RequestParam("expressCompany") String expressCompany,
			@RequestParam("expressNO") String expressNO, Model model, HttpServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		Cookie token = HttpUtils.getCookieByName(httpServletRequest, "token");
		if (token != null && StringUtils.isNotBlank(token.getValue())) {
			MobileUserVO userInfo = mobileAuthcFacade.findAuthencatedUser(token.getValue());
			if (userInfo != null) {
				model.addAttribute("orderTrace", orderTraceFacade.getTrace(expressCompany, expressNO));
				model.addAttribute("expressCompany", expressCompany);
				model.addAttribute("expressNO", expressNO);
			} else {
				return "redirect:/m/logistics/unauthrized";
			}
		}
		return "pages/webview/logistics";
	}

	@RequestMapping(value = "/m/logistics_all", method = RequestMethod.GET)
	public String getLogisticsInfo(@RequestParam("orderId") long orderId, Model model, HttpServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		Cookie token = HttpUtils.getCookieByName(httpServletRequest, "token");
		if (token != null && StringUtils.isNotBlank(token.getValue())) {
			MobileUserVO userInfo = mobileAuthcFacade.findAuthencatedUser(token.getValue());
			if (userInfo != null) {
				model.addAttribute("orderTrace", orderTraceFacade.getTraceByOrderId(userInfo.getUserId(), orderId));
			} else {
				return "redirect:/m/logistics/unauthrized";
			}
		}
		return "pages/webview/logistics_all";
	}
	/**
	 * 获取物流信息时token过期的页面，返回空白页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/logistics/unauthrized", method = RequestMethod.GET)
	public @ResponseBody String getLogisticsInfoUnauthrized(Model model, HttpServletRequest request) {
		return null;
	}

	/**
	 * 根据帮助类目查找已发布到APP的文章的列表。
	 * 
	 * @param categoryId
	 *            帮助类目ID
	 * @return 文章列表
	 */
	@RequestMapping(value = "/m/help/article", method = RequestMethod.GET)
	public String findAppPublishedArticleByCategory(@RequestParam("categoryId") long categoryId, Model model) {
		model.addAttribute("detail", helpCenterFacade.findAppPublishedArticleByCategory(categoryId));
		return "pages/webview/help.detail";
	}

	/**
	 * 获取帮助中心导航的数据。
	 * 
	 * @return 左导航条数据
	 */
	@RequestMapping(value = "/m/help/nav", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getHelpCenterLeftNav() {
		return helpCenterFacade.getHelpCenterLeftNav();
	}

	/**
	 * get page of po share
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/m/share/po/page", method = RequestMethod.GET)
	public String getSharePOPage(Model model, PoProductListSearchVO param) {
		appendStaticMethod(model);
		param.setLastId(0);
		param.setLimit(20);
		String resPage = "pages/webview/share/po";
		long curSupplierAreaId = AreaUtils.getAreaCode();
		boolean flag = this.getAreaIdOfPo(param.getScheduleId(),curSupplierAreaId);

		model.addAttribute("canAccess", flag ? 1 : 0);

		if (!flag) {
			model.addAttribute("code", ErrorCode.NO_MATCH);
			return resPage;
		}

		BaseJsonListResultVO res = itemFacade.getProductList(param);
		model.addAttribute("result", res);

		// activity info
		DetailPromotionVO promotionVO = itemFacade.getDetailPagePromotionInfo(param.getScheduleId());
		model.addAttribute("activity", promotionVO);

		// po startTime and endTime
		Schedule schedule = this.getScheduleById(param.getScheduleId());
		if (schedule != null) {
			model.addAttribute("startTime", schedule.getStartTime());
			model.addAttribute("endTime", schedule.getEndTime());
			model.addAttribute("activeFlag", this.getActiveFlagforPoShare(schedule));
		}

		model.addAttribute("code", ErrorCode.SUCCESS);
		return resPage;
	}

	private Schedule getScheduleById(long scheduleId) {
		List<Schedule> scheduleList = scheduleFacade.getScheduleByIdList(Arrays.asList(scheduleId));
		if (scheduleList != null && scheduleList.size() == 1) {
			return scheduleList.get(0);
		}
		return null;
	}

	/**
	 * get data of po share
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/m/share/po/data", method = RequestMethod.GET)
	public @ResponseBody JSONObject getSharePOInfo(PoProductListSearchVO param) {
		JSONObject jsonObject = new JSONObject();
		long curSupplierAreaId = AreaUtils.getAreaCode();
		boolean flag = this.getAreaIdOfPo(param.getScheduleId(),curSupplierAreaId);
		jsonObject.put("canAccess", flag ? 1 : 0);

		if (!flag) {
			jsonObject.put("code", ErrorCode.NO_MATCH);
			return jsonObject;
		}

		BaseJsonListResultVO res = itemFacade.getProductList(param);
		jsonObject.put("result", res);

		// po startTime and endTime
		Schedule schedule = this.getScheduleById(param.getScheduleId());
		if (schedule != null) {
			jsonObject.put("startTime", schedule.getStartTime());
			jsonObject.put("endTime", schedule.getEndTime());
			jsonObject.put("activeFlag", this.getActiveFlagforPoShare(schedule));
		}

		jsonObject.put("code", ErrorCode.SUCCESS);
		return jsonObject;
	}

	@RequestMapping(value = "/m/sizetable", method = RequestMethod.GET)
	public String getSizeTableByProductId(Model model, @RequestParam long productId) {
		DetailProductVO productVO = commonFacade.getDetailPageProduct(productId, true, true);
		transForSizeTable(productVO);

		model.addAttribute("product", productVO);
		return "pages/webview/sizetable";
	}

	@RequestMapping(value = "/m/agreement", method = RequestMethod.GET)
	public String agreement(Model module) {
		module.addAttribute("result", helpCenterFacade.findWebPublishedArticleByCategory(82));
		return "pages/webview/agreement";
	}

	@RequestMapping(value = "/m/share/home", method = RequestMethod.GET)
	public String shareHome() {
		return "pages/webview/share/home";
	}

	private int getActiveFlagforPoShare(Schedule schedule) {
		int activeFlag;
		if (System.currentTimeMillis() < schedule.getStartTime()) {
			activeFlag = 0;// not begin
		} else if (System.currentTimeMillis() > schedule.getEndTime()) {
			activeFlag = 2;// finish
		} else {
			activeFlag = 1;
		}
		return activeFlag;
	}

}
