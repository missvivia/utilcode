package com.xyl.mmall.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.AlbumFacade;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.ScheduleLikeFacade;
import com.xyl.mmall.cms.facade.util.BaseChecker.ErrChecker;
import com.xyl.mmall.cms.facade.util.ScheduleChecker;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.vo.POPreviewVO;
import com.xyl.mmall.cms.vo.ScheduleLikeVO;
import com.xyl.mmall.content.annotation.CollectBubbleActivity;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.itemcenter.dto.BaseSkuDTO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.vo.FeedDataXmlVO;
import com.xyl.mmall.mainsite.vo.FeedItemXmlVO;
import com.xyl.mmall.mainsite.vo.FeedXmlVO;
import com.xyl.mmall.photomgr.meta.AlbumImg;
import com.xyl.mmall.saleschedule.dto.BrandShopDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.ScheduleLikeDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleLikeState;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleLike;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.util.AreaUtils;

@Controller
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {

	@Autowired
	private ScheduleLikeFacade scheduleLikeFacade;

	private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

	private static final ScheduleChecker checker = new ScheduleChecker(logger);

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

	// ////////////////////////////
	// SchedulePage
	// ////////////////////////////
	@RequestMapping(value = "/preview")
	public String mainSiteSchedulePreview(Model model, @RequestParam(value = "pageId") Long pPageId,
			@RequestParam(value = "layout") String pLayout) {
		POPreviewVO vo = new POPreviewVO();
		vo.setLayout(pLayout);
		vo.setPageId(pPageId);
		ErrChecker errChecker = checker.checkStringEmpty(vo.getLayout(), "Parameter 'layout' cannot be null!!!");
		if (!errChecker.check) {
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, errChecker.msg).toJSONString();
		}

		PODTO poDTO = scheduleFacade.getSchedulePageById(vo.getPageId());

		if (poDTO.getPageDTO() == null || poDTO.getPageDTO().getPage() == null) {
			String msg = "Cannot find decorate page by id '" + vo.getPageId() + "'!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg).toJSONString();
		}

		Business supplier = businessFacade.getBusinessById(poDTO.getPageDTO().getPage().getSupplierId());
		if (supplier == null) {
			String msg = "Cannot find supplier  by supplier id '" + poDTO.getPageDTO().getPage().getSupplierId()
					+ "'!!!";
			logger.error(msg);
			return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg).toJSONString();
		}

		long now = System.currentTimeMillis();
		long leftTime = poDTO.getScheduleDTO().getSchedule().getEndTime() - now;

		// shop list
		//List<BrandShopDTO> shopList = mainBrandFacade.getBrandShops(supplier.getAreaId(), supplier.getActingBrandId());
		List<BrandShopDTO> shopList = mainBrandFacade.getBrandShopListBySupplierId(supplier.getId());
		JSONArray shops = ScheduleUtil.getBrandShopJsonArr(shopList);

		// img list
		List<AlbumImg> imgList = albumFacade.getImgListByIdList(vo.getImgIds());

		JSONObject imgJsonMap = new JSONObject();
		for (AlbumImg img : imgList) {
			imgJsonMap.put(img.getId() + "", img);
		}

		// product list
		JSONObject prdJsonMap = scheduleFacade.getPrdListByPrdIdListForMainSite(vo.getPrdIds(), poDTO);

		model.addAttribute("leftTime", leftTime);
		model.addAttribute("startTime", poDTO.getScheduleDTO().getSchedule().getStartTime());
		model.addAttribute("endTime", poDTO.getScheduleDTO().getSchedule().getEndTime());
		model.addAttribute("images", imgJsonMap);
		model.addAttribute("layout", vo.getLayOutJson());
		model.addAttribute("shops", shops);
		model.addAttribute("products", prdJsonMap);
		model.addAttribute("title", poDTO.getPageDTO().getPage().getTitle());
		model.addAttribute("pageTitle", poDTO.getScheduleDTO().getSchedule().getPageTitle());

		return "pages/schedule/schedule";
	}

	public String _viewPageDetail(Model model, Long pageId, Long scheduleId, boolean checkAccess) {
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
			//return generateRespJsonStr(ScheduleUtil.CODE_ERR, ScheduleUtil.RESULT_ERR, msg).toJSONString();
			return "pages/404";
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
				return "pages/504";
			}

			// Set PO endTime and check whether PO is expire
			Schedule schedule = poDTO.getScheduleDTO().getSchedule();
			// TODO
			//POBaseUtil.calPOQrqmEndTime(schedule);
			if (schedule.getEndTime() < System.currentTimeMillis()) { // PO over
				// redirect to pages\schedule\over.ftl
				long brandId = poDTO.getPageDTO().getPage().getBrandId();
				String brandName = poDTO.getPageDTO().getPage().getBrandName();
				String brandLogo = schedule.getBrandLogo();
				JSONObject brandJson = new JSONObject();
				brandJson.put("id", brandId);
				brandJson.put("brandNameZh", brandName);
				brandJson.put("logo", brandLogo);
				model.addAttribute("brand", brandJson);
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

		long now = System.currentTimeMillis();
		long leftTime = poDTO.getScheduleDTO().getSchedule().getEndTime() - now;

		// scheduleState
		int scheduleState = ScheduleUtil.getScheduleState(poDTO.getScheduleDTO().getSchedule());

		// 门店列表
//		List<BrandShopDTO> shopList = mainBrandFacade.getBrandShops(supplier.getAreaId(), supplier.getActingBrandId());
		List<BrandShopDTO> shopList = mainBrandFacade.getBrandShopListBySupplierId(supplier.getId());
		JSONArray shops = ScheduleUtil.getBrandShopJsonArr(shopList);

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
		JSONObject imgJsonMap = new JSONObject();
		for (AlbumImg img : imgList) {
			imgJsonMap.put(img.getId() + "", img);
		}

		// page layout object
		JSONObject layout = ScheduleUtil.getPageLayoutJson(poDTO);

		// product list
		JSONObject prdJsonMap = new JSONObject();
		String prdIds = poDTO.getPageDTO().getPage().getUdProductIds();
		List<Long> prdIdList = ScheduleUtil.getItemListByItemListStr(prdIds);
		if (prdIdList != null) {
			prdJsonMap = scheduleFacade.getPrdListByPrdIdListForMainSite(prdIdList, poDTO);
		}

		// PO fav
		//boolean poFav = likeFacade.isLike(ScheduleUtil.getUserId(), poDTO.getPageDTO().getPage().getBrandId());
		boolean brandFav = mainBrandFacade.getBrandCollectionState(SecurityContextUtils.getUserId(), poDTO.getPageDTO().getPage().getBrandId());

		model.addAttribute("scheduleState", scheduleState);
		model.addAttribute("followed", brandFav);
		model.addAttribute("leftTime", leftTime);
		model.addAttribute("startTime", poDTO.getScheduleDTO().getSchedule().getStartTime());
		model.addAttribute("endTime", poDTO.getScheduleDTO().getSchedule().getEndTime());
		model.addAttribute("images", imgJsonMap);
		model.addAttribute("layout", layout);
		model.addAttribute("shops", shops);
		model.addAttribute("products", prdJsonMap);
		model.addAttribute("title", poDTO.getPageDTO().getPage().getTitle());
		model.addAttribute("pageTitle", poDTO.getScheduleDTO().getSchedule().getPageTitle());

		// for statistic
		model.addAttribute("bi_supplyId", poDTO.getPageDTO().getPage().getSupplierId());
		model.addAttribute("bi_branId", poDTO.getPageDTO().getPage().getBrandId());
		model.addAttribute("bi_poId", poDTO.getPageDTO().getPage().getScheduleId());
		model.addAttribute("bi_branName", poDTO.getPageDTO().getPage().getBrandName());
		long poStartTime = poDTO.getScheduleDTO().getSchedule().getStartTime();
		long poEndTime = poDTO.getScheduleDTO().getSchedule().getEndTime();
		if (poStartTime > now) {
			model.addAttribute("bi_status", 1);
		} else if (poEndTime > now) {
			model.addAttribute("bi_status", 2);
		}

		return "pages/schedule/schedule";
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String mainSiteScheduleDisplayWithoutCheckAccess(Model model,
			@RequestParam(value = "pageId", required = false) Long pageId) {

		return _viewPageDetail(model, pageId, null, false);
	}

	@CollectBubbleActivity
	@BILog(action = "page", type = "poPage")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String mainSiteScheduleDisplay(Model model, @RequestParam(value = "pageId", required = false) Long pageId,
			@RequestParam(value = "scheduleId", required = false) Long scheduleId) {

		return _viewPageDetail(model, pageId, scheduleId, true);
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

	/**
	 * 添加我关注的活动 post
	 * 
	 * @param model
	 * @param scheduleId
	 */
	@RequestMapping(value = "/follow", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addScheduleLikeOfUser(@RequestBody JSONObject param) {
		Date date = new Date();
		long userId = SecurityContextUtils.getUserId();

		long scheduleId = param.getLongValue("scheduleId");

		JSONObject jsonObject = new JSONObject();

		ScheduleLikeDTO scheduleLikeDTO = new ScheduleLikeDTO();

		ScheduleLike scheduleLike = new ScheduleLike();
		scheduleLike.setScheduleId(scheduleId);
		scheduleLike.setStatus(ScheduleLikeState.VALID);
		scheduleLike.setUserId(userId);
		scheduleLike.setCreateDate(date.getTime());
		scheduleLike.setStatusUpdateDate(date.getTime());

		scheduleLikeDTO.setLike(scheduleLike);

		ScheduleLikeVO scheduleLikeVO = scheduleLikeFacade.addLike(scheduleLikeDTO);
		jsonObject.put("code", scheduleLikeVO.getLike().getLike() != null ? 200 : 400);
		return jsonObject;
	}

	/**
	 * 取消我关注的活动 post
	 * 
	 * @param model
	 * @param scheduleId
	 */
	@RequestMapping(value = "/unfollow", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject delScheduleLikeOfUser(@RequestBody JSONObject param) {
		long userId = SecurityContextUtils.getUserId();

		long scheduleId = param.getLongValue("scheduleId");
		JSONObject jsonObject = new JSONObject();

		ScheduleLikeDTO scheduleLikeDTO = new ScheduleLikeDTO();

		ScheduleLike scheduleLike = new ScheduleLike();
		scheduleLike.setScheduleId(scheduleId);
		scheduleLike.setUserId(userId);

		scheduleLikeDTO.setLike(scheduleLike);

		jsonObject.put("code", scheduleLikeFacade.cancelLike(scheduleLikeDTO) ? 200 : 400);
		return jsonObject;
	}

	@RequestMapping(value = "/fullfeed", method = RequestMethod.GET)
	public void getFullFeed(HttpServletResponse response) {
		FeedXmlVO vo = new FeedXmlVO();
		List<FeedItemXmlVO> retList = new ArrayList<>();
		long cur = System.currentTimeMillis();
		long tenDays = 10 * 24 * 60 * 60 * 1000L;
		List<PODTO> poList = scheduleFacade.getOnlineScheduleList();
		List<Long> skuList = new ArrayList<>();
		for (PODTO dto : poList) {
			long startTime = dto.getScheduleDTO().getSchedule().getStartTime();
			if (startTime <= cur && startTime > cur - tenDays) {
				List<POProductDTO> list = scheduleFacade.getPoProductByPo(
						dto.getScheduleDTO().getSchedule().getId());
				for (POProductDTO productDTO : list) {
					List<BaseSkuDTO> skuDTOs = (List<BaseSkuDTO>) productDTO.getSKUList();
					List<Long> skuListforOneProduct = new ArrayList<>();
					for (BaseSkuDTO skuDTO : skuDTOs) {
						skuListforOneProduct.add(skuDTO.getId());
					}
					skuList.addAll(skuListforOneProduct);
					FeedItemXmlVO item = new FeedItemXmlVO();
					item.setClickUrl("http://023.baiwandian.cn/product/detail?id=" + productDTO.getId());
					FeedDataXmlVO data = new FeedDataXmlVO();
					data.setSkuIdList(skuListforOneProduct);
					StringBuilder city = new StringBuilder(100);
					List<ScheduleSiteRela> siteList = dto.getScheduleDTO().getSiteRelaList();
					boolean bValid = true;
					for (ScheduleSiteRela siteRela : siteList) {
						Long cityCode = siteRela.getSaleSiteId();
						if (cityCode.equals(1000L)) { 		// 网易字段 暂时无效
							bValid = false;
							break;
						}
						city.append(cityCode).append(" ");
					}
					if (!bValid) continue;
					if (city.length() > 0) {
						city.deleteCharAt(city.lastIndexOf(" "));
					}
					data.setCity(city.toString());
					data.setcName(productDTO.getCategoryName());
					data.setImgUrl(productDTO.getShowPicPath());
					//data.setInventoryNum(String.valueOf(productDTO.getCartStock()));
					data.setOriginalPrice(String.valueOf(productDTO.getMarketPrice()));
					data.setPid(String.valueOf(productDTO.getId()));
					data.setpName(productDTO.getProductName());
					data.setPrice(String.valueOf(productDTO.getSalePrice()));
					item.setData(data);
					retList.add(item);
				}
			}
		}
		
		List<FeedItemXmlVO> retOutputList = new ArrayList<>();
		Map<Long, Integer> countMap = scheduleFacade.getInventoryCount(skuList);
		for (FeedItemXmlVO itemXmlVO : retList) {
			FeedDataXmlVO data = itemXmlVO.getData();
			List<Long> skuListforOneProduct = data.getSkuIdList();
			int totalCount = 0;
			for (Long skuId : skuListforOneProduct) {
				Integer count = countMap.get(skuId);
				if (count != null && count > 0) {
					totalCount += count;
				}
			}
			data.setInventoryNum(String.valueOf(totalCount));
			data.setSkuIdList(null);
			if (totalCount > 0) {
				retOutputList.add(itemXmlVO);
			}
		}
		vo.setList(retOutputList);
		response.setContentType("application/xml"); 
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(FeedXmlVO.class);
			Marshaller marshaller = context.createMarshaller();
	        marshaller.marshal(vo, response.getOutputStream());
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 增量feed有些复杂 暂时不做
	 * @return
	 */
//	@RequestMapping(value = "/incfeed", method = RequestMethod.GET)
//	public @ResponseBody FeedXml getIncFeed() {
//		
//	}
	
}
