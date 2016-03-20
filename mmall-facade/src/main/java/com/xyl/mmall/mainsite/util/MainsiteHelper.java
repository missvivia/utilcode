package com.xyl.mmall.mainsite.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.backend.facade.BrandFacade;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.cms.dto.BusiUserRelationDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.enums.PromotionContentTab;
import com.xyl.mmall.cms.facade.CategoryFacade;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.common.util.ItemCenterUtils;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductVo;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.dto.SkuRecommendationDTO;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.meta.ProdPicMap;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.mainsite.facade.IPServiceFacade;
import com.xyl.mmall.mainsite.facade.MainBrandFacade;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.mainsite.vo.DetailProductVO;
import com.xyl.mmall.mainsite.vo.MainSiteDataVo;
import com.xyl.mmall.mainsite.vo.MainsiteIndexVO;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;
import com.xyl.mmall.mainsite.vo.ProductListProductVO;
import com.xyl.mmall.mainsite.vo.ProductListSkuVO;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.service.BrandService;

@Component
public class MainsiteHelper {

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

	@Resource
	private POProductService poProductService;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;

	@Resource
	private MainsiteItemFacade itemFacade;

	@Resource
	private BrandService brandService;

	@Resource
	private CategoryFacade categoryFacade;

	@Resource
	private ItemProductService itemProductService;

	@Resource
	private ProductService productService;

	@Resource
	private BusinessService businessService;

	// schedule中首页的tab值
	public static final long INDEX_TAB_OF_SCHEDULE = 1;

	// 推广中首页的tab值
	private static final int INDEX_TAB_OF_PROMOTIONCONTENT = PromotionContentTab.INDEX.getIntValue();

	private static final int MAX_SIZE_FOR_BRAND_ENTERED = 60;

	// 在档期模块中，传入0，代表不限制返回条数
	private static final int NO_LIMIT_SIZE_OF_SCHEDULE = 0;

	private static final int PO_OF_PROMOTION_CONTENT_TYPE = 0;

	private static final Logger logger = LoggerFactory.getLogger(MainsiteHelper.class);

	public MainsiteIndexVO getMainsitMainsiteIndexVO(long userId) {
		MainsiteIndexVO mainsiteIndexVO = new MainsiteIndexVO();
		List<BusiUserRelationDTO> busiUserRelationDTOs = businessService.getBusiUserRelationsByUserId(userId);
		if (CollectionUtil.isNotEmptyOfList(busiUserRelationDTOs)) {
			for (BusiUserRelationDTO busiUserRelationDTO : busiUserRelationDTOs) {
				BusinessDTO businessDTO = businessService.getBreifBusinessById(busiUserRelationDTO.getBusinessId(), -1);
				List<SkuRecommendationDTO> skuRecommendationDTOs = itemProductService
						.getSKuRecommendationListByBusinessId(busiUserRelationDTO.getBusinessId());
				if (CollectionUtil.isEmptyOfList(skuRecommendationDTOs)) {
					continue;
				}
				List<Long> skuIds = new ArrayList<Long>();
				for (SkuRecommendationDTO skuRecommendationDTO : skuRecommendationDTOs) {
					if (StringUtils.isEmpty(skuRecommendationDTO.getProductStatusMsg())) {
						skuIds.add(skuRecommendationDTO.getProductSKUId());
					}
				}
				if (skuIds.size() > 0) {
					List<ProductSKUDTO> productSKUDTOs = itemProductService.getProductSKUDTOByProdIds(skuIds);
					Map<Long, ProductSKUDTO> skuDTOMap = new HashMap<Long, ProductSKUDTO>();
					for (ProductSKUDTO productSKUDTO : productSKUDTOs) {
						skuDTOMap.put(productSKUDTO.getId(), productSKUDTO);
					}
					// 排序
					List<ProductSKUDTO> sortProductSKUDTOs = new ArrayList<ProductSKUDTO>();
					for (Long skuId : skuIds) {
						sortProductSKUDTOs.add(skuDTOMap.get(skuId));
					}
					Map<String, List<ProductPriceDTO>> productPriceMap = productService
							.getProductPriceDTOByProductIds(skuIds);
					mainsiteIndexVO.fillMainsiteStoreVOs(sortProductSKUDTOs, productPriceMap, businessDTO);
				}
			}
			Collections.sort(mainsiteIndexVO.getMainsiteStoreVOs());
		}

		return mainsiteIndexVO;
	}

	@Cacheable(value = "mainSiteCache", key = "#curSupplierAreaId")
	public MainSiteDataVo getMainSiteFromOldCache(Long curSupplierAreaId) {
		logger.info("===get index from oldCache database,curSupplierAreaId:" + curSupplierAreaId);
		Date curDate = new Date();
		return this.getHomeDataInner(curSupplierAreaId, curDate);
	}

	@Cacheable(value = "mainSiteFreshCache", key = "#curSupplierAreaId")
	public MainSiteDataVo getMainSiteFromFreshCache(Long curSupplierAreaId) {
		logger.info("===get index from fresh Cache database,curSupplierAreaId:" + curSupplierAreaId);
		Date curDate = new Date();
		return this.getHomeDataInner(curSupplierAreaId, curDate);
	}

	public MainSiteDataVo getHomeDataInner(long curSupplierAreaId, Date curDate) {
		MainSiteDataVo mainSiteDataVo = new MainSiteDataVo();

		// 目前千人千面没有在用
		UserLoginBean userLoginBean = new UserLoginBean();
		long userId = -1;

		/**
		 * 1.轮翻推广
		 */
		// List<PromotionContent> promotionContentList =
		// this.getPromotionContentByParam(curDate, curSupplierAreaId,
		// INDEX_TAB_OF_PROMOTIONCONTENT);
		//
		// mainSiteDataVo.setPromotionContentList(promotionContentList);

		/**
		 * 2.最新特卖
		 */
		// JSONArray jsonArray = this.getOnlineScheduleForIndex(userLoginBean,
		// curSupplierAreaId, curDate);
		// model.addAttribute(SCHEDULE_TODAY, jsonArray);
		// mainSiteDataVo.setLatestNewSchedule(jsonArray);

		/**
		 * 3.上新预告,分别取后四天数据
		 */
		// model.addAttribute(PRE_SCHEDULE, this.getPreScheduleMap(userId,
		// curSupplierAreaId, curDate));
		// mainSiteDataVo.setPreScheduleMap(this.getPreScheduleMap(userId,
		// curSupplierAreaId, curDate));
		//
		// // today's foreshow
		// Calendar now = Calendar.getInstance();
		// long nowTime = now.getTimeInMillis();
		// now.set(Calendar.HOUR_OF_DAY, 10);
		// now.set(Calendar.MINUTE, 0);
		// now.set(Calendar.SECOND, 0);
		// now.set(Calendar.MILLISECOND, 0);
		// long poStartTime = now.getTimeInMillis();
		// if (nowTime < poStartTime) {
		// mainSiteDataVo.setTodayScheduleMap(this.getTodayScheduleMap(userId,
		// curSupplierAreaId));
		// }

		/**
		 * 4.入驻品牌
		 */
		List<BrandItemDTO> brandList = this.getBrandEntered(curSupplierAreaId);
		// model.addAttribute(BRAND_ENTERED, brandList);
		mainSiteDataVo.setBrandList(brandList);

		// 内容分类
		mainSiteDataVo.setCategoryList(this.getCategoryList(curSupplierAreaId));

		return mainSiteDataVo;
	}

	@Cacheable(value = "categoryDTOCache", key = "#curSupplierAreaId")
	public List<CategoryContentDTO> getCategoryList(long curSupplierAreaId) {
		return categoryFacade.getCategoryContentListByAreaId(curSupplierAreaId);
	}

	// 类目全部返回，只是将字段isvisible设为不可见
	public List<CategoryContentDTO> getCategoryListByBusinessId(long businessId, long curSupplierAreaId) {
		List<Long> categoryIds = itemProductService.getCategoryNormalIdsByBusinessId(businessId);
		List<CategoryContentDTO> categoryContentDTOs = getCategoryList(curSupplierAreaId);
		if (CollectionUtil.isEmptyOfList(categoryContentDTOs)) {
			return null;
		}
		for (CategoryContentDTO categoryContentDTO : categoryContentDTOs) {
			if (CollectionUtil.isEmptyOfList(categoryContentDTO.getSubCategoryContentDTOs())) {
				continue;
			}
			for (CategoryContentDTO secondCategoryContentDTO : categoryContentDTO.getSubCategoryContentDTOs()) {
				if (CollectionUtil.isEmptyOfList(secondCategoryContentDTO.getSubCategoryContentDTOs())) {
					continue;
				}
				for (CategoryContentDTO thirdCategoryContentDTO : secondCategoryContentDTO.getSubCategoryContentDTOs()) {
					String[] categoryArray = StringUtils.split(thirdCategoryContentDTO.getCategoryNormalIds(), ",");
					for (String categoryId : categoryArray) {
						if (categoryIds.contains(Long.parseLong(categoryId))) {
							thirdCategoryContentDTO.setIsvisible(true);
							secondCategoryContentDTO.setIsvisible(true);
							categoryContentDTO.setIsvisible(true);
							break;
						}
					}
				}
			}

		}
		return categoryContentDTOs;
	}

	public List<JSONObject> getBrandByBusinessIdAndcategoryNormalIds(long businessId, List<Long> categoryNormalIds) {
		Set<Long> businessIds = new HashSet<Long>();
		businessIds.add(businessId);
		// 先获取品牌id
		List<Long> brandIds = itemProductService.getBrandIdsByCategoryIds(categoryNormalIds, null, businessIds);
		if (CollectionUtils.isEmpty(brandIds)) {
			return null;
		}
		return brandService.getBrandListOrderBySKUSaleNum(brandIds);
	}

	@Cacheable(value = "detailPageCache")
	public DetailProductVO getProductData(long pid) {
		logger.info("=====getProductData from database");
		ProductFullDTO product = poProductService.getProductFullDTOForMainSite(pid);
		DetailProductVO productVO = commonFacade.loadData(product);
		return productVO;
	}

	@Cacheable(value = "prodListCache")
	public BaseJsonListResultVO getProductList(long scheduleId, long categoryId, boolean desc, int order, int offset,
			int limit) {
		PoProductListSearchVO param = new PoProductListSearchVO();
		param.setCategoryId(categoryId);
		param.setDesc(desc);
		param.setLimit(limit);
		param.setOrder(order);
		param.setScheduleId(scheduleId);
		param.setOffset(offset);
		try {
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();

			BaseJsonListResultVO result = itemFacade.getPoProductDTOList(param);

			List<ProductListProductVO> list = new ArrayList<ProductListProductVO>();
			List<POProductDTO> retList = (List<POProductDTO>) result.getList();
			if (retList != null && retList.size() > 0) {
				List<Long> pidList = new ArrayList<Long>();
				for (POProductDTO p : retList) {
					pidList.add(p.getId());
				}
				List<ProdPicMap> picList = poProductService.getProductPicListCache(pidList, PictureType.LIST);
				Map<Long, ProdPicMap> picMap = new HashMap<Long, ProdPicMap>();
				for (ProdPicMap pic : picList) {
					picMap.put(pic.getProductId(), pic);
				}
				BrandDTO brand = null;
				for (POProductDTO product : retList) {
					ProductListProductVO pvo = new ProductListProductVO();
					pvo.setId(String.valueOf(product.getId()));
					pvo.setBrandId(String.valueOf(product.getBrandId()));
					if (brand == null)
						brand = brandService.getBrandByBrandId(product.getBrandId());
					pvo.setBrandName(brand.getBrand().getBrandNameAuto());
					pvo.setSameAsShop(product.getSameAsShop());
					pvo.setColorName(product.getColorName());
					ProdPicMap pic = picMap.get(product.getId());
					List<String> pathList = ItemCenterUtil.genProdPicPath(pic);
					pvo.setListShowPicList(pathList);
					pvo.setMarketPrice(product.getMarketPrice());
					pvo.setProductName(product.getProductName());
					pvo.setSalePrice(product.getSalePrice());
					List<POSkuDTO> skulist = (List<POSkuDTO>) product.getSKUList();
					List<ProductListSkuVO> skuVOList = new ArrayList<ProductListSkuVO>();
					if (skulist != null && skulist.size() > 0) {
						for (POSkuDTO sku : skulist) {
							ProductListSkuVO skuVO = new ProductListSkuVO();
							skuVO.setId(sku.getId());
							String size = sku.getSize();
							int s = size.indexOf("(");
							int e = size.lastIndexOf(")");
							if (s > 0 && e > s) {
								skuVO.setSize(size.substring(0, s));
							} else
								skuVO.setSize(size);
							skuVO.setNum(sku.getSkuNum() + sku.getSupplierSkuNum());
							Integer tmpCartStock = sku.getCartStock();
							if (tmpCartStock == null) {
								skuVO.setState(0);
								skuVOList.add(skuVO);
								continue;
							}
							Integer tmpOrderStock = sku.getOrderStock();
							if (tmpOrderStock == null) {
								skuVO.setState(0);
								skuVOList.add(skuVO);
								continue;
							}
							skuVO.setState(ItemCenterUtils.getStockType(tmpCartStock, tmpOrderStock));
							skuVOList.add(skuVO);
						}
					}
					pvo.setSkuList(skuVOList);
					list.add(pvo);
				}
			}

			retVO.setList(list);
			retVO.setHasNext(result.isHasNext());
			retVO.setTotal(result.getTotal());
			return retVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	// 拼接url
	public List<PromotionContent> getPromotionContentByParam(Date curDate, long curSupplierAreaId,
			int tabOfPromotionContent) {
		List<PromotionContent> resList = promotionContentFacade.getPromotionContentByAreaAndTimeAndPosition(
				curDate.getTime(), curSupplierAreaId, INDEX_TAB_OF_PROMOTIONCONTENT);
		if (resList == null || resList.size() <= 0) {
			return resList;
		}

		for (PromotionContent promotionContent : resList) {
			if (promotionContent.getPromotionType() == PO_OF_PROMOTION_CONTENT_TYPE) {
				String storeURL = CodeInfoUtil.DOMAIN_URL + CodeInfoUtil.STORE_URL + promotionContent.getBusinessId();
				promotionContent.setActivityUrl(storeURL);
			}
		}

		return retListLimit(resList, 5);
	}

	/**
	 * 按数量返回列表
	 * 
	 * @param promotionContentList
	 * @param limit
	 * @return
	 */
	public List<PromotionContent> retListLimit(List<PromotionContent> promotionContentList, int limit) {
		if (CollectionUtils.isNotEmpty(promotionContentList)) {
			int size = promotionContentList.size();
			// 列表数量大于5随机取5条
			if (size > limit) {
				Random random = new Random();
				Set<Integer> indexSet = new HashSet<Integer>();
				int i = 0;
				while (i < 5) {
					int index = random.nextInt(size) % (size + 1);
					if (!indexSet.contains(index)) {
						indexSet.add(index);
						++i;
					}
				}
				List<PromotionContent> retList = new ArrayList<PromotionContent>(limit);
				for (Integer index : indexSet) {
					retList.add(promotionContentList.get(index));
				}
				return retList;
			}
		}
		return promotionContentList;
	}

	/**
	 * 此处最新特卖修改
	 * <p>
	 * 从调用方传入时间参数给档期服务
	 * 
	 * @param bean
	 * @param curSupplierAreaId
	 * @param curDate
	 * @return
	 */
	private JSONArray getOnlineScheduleForIndex(UserLoginBean bean, long curSupplierAreaId, Date curDate) {
		// ScheduleListVO scheduleListVO =
		// scheduleFacade.getScheduleListForChl(bean, INDEX_TAB_OF_SCHEDULE,
		// curSupplierAreaId, 0, 0, 0);
		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForChl(INDEX_TAB_OF_SCHEDULE, curSupplierAreaId,
				curDate.getTime());
		JSONObject jsonObj = ScheduleUtil.geneJsonObjForValidList(scheduleListVO, null, null);
		return this.getJSONArrayByJSONObject(jsonObj);
	}

	/**
	 * 此处修改成外部传入时间参数
	 * 
	 * @param userId
	 * @param curSupplierAreaId
	 * @param curDate
	 *            当前时间
	 * @return
	 */
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

		/**
		 * 分别获取到明天，后天，大后天，第四天的查询开始时间与结束时间
		 */

		// 明天上新预告
		JSONArray jsonArrayAfterOne = this.getPreScheduleResultByParam(curDate, 1, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);

		// 后天上新预告
		JSONArray jsonArrayAfterTwo = this.getPreScheduleResultByParam(curDate, 2, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);

		// 大后天上新预告
		JSONArray jsonArrayAfterThree = this.getPreScheduleResultByParam(curDate, 3, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);

		// 第四天上新预告
		JSONArray jsonArrayAfterFour = this.getPreScheduleResultByParam(curDate, 4, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);

		this.putScheduleJSONToMap(nextOneDateStr, jsonArrayAfterOne, resultMap);
		this.putScheduleJSONToMap(nextTwoDateStr, jsonArrayAfterTwo, resultMap);
		this.putScheduleJSONToMap(nextThreeDateStr, jsonArrayAfterThree, resultMap);
		this.putScheduleJSONToMap(nextFourDateStr, jsonArrayAfterFour, resultMap);

		return resultMap;
	}

	private JSONArray getTodayScheduleMap(long userId, long curSupplierAreaId) {
		// today's foreshow
		return this.getPreScheduleResultByParam(Calendar.getInstance().getTime(), 0, userId, curSupplierAreaId,
				NO_LIMIT_SIZE_OF_SCHEDULE);
	}

	/**
	 * 根据当前时间curDate，与dateAfterParam返回开始时间
	 * <p>
	 * 只会把天数加上dateAfterParam
	 * <p>
	 * 例： 传入: 2015-01-12 10:00:00 且 dateAfterParam为1 返回：2015-01-13 00:00:00
	 * 
	 * @param curDate
	 * @param dateAfterParam
	 * @return
	 */
	private Date getStartDateByAfterParam(Date curDate, int dateAfterParam) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);

		calendar.add(Calendar.DAY_OF_MONTH, dateAfterParam);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 返回结束时间
	 * <p>
	 * 与getStartDateByAfterParam类似 只是返回结果： 2015-01-12 23:59:59
	 * 
	 * @param curDate
	 * @param dateAfterParam
	 * @return
	 */
	private Date getEndDateByAfterParam(Date curDate, int dateAfterParam) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);

		calendar.add(Calendar.DAY_OF_MONTH, dateAfterParam);

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}

	private void putScheduleJSONToMap(String dateStr, JSONArray jsonArray, Map<String, JSONArray> map) {
		map.put(dateStr, jsonArray);
	}

	private JSONArray getPreScheduleResultByParam(Date curDate, int dayAfterNum, long userId, long curSupplierAreaId,
			int retSize) {
		Date startTime = this.getStartDateByAfterParam(curDate, dayAfterNum);
		Date endTime = this.getEndDateByAfterParam(curDate, dayAfterNum);

		ScheduleListVO scheduleListVO = scheduleFacade.getScheduleListForFuture(INDEX_TAB_OF_SCHEDULE,
				curSupplierAreaId, startTime.getTime(), endTime.getTime());

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

	@Cacheable(value = "prodListCache")
	public BaseJsonListResultVO getProductListByCategory(PoProductListSearchVO param) {
		try {
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();
			BaseJsonListResultVO result = itemFacade.getProductDTOListByCategory(param);

			List<ProductListProductVO> list = new ArrayList<ProductListProductVO>();
			List<POProductDTO> retList = (List<POProductDTO>) result.getList();
			if (retList != null && retList.size() > 0) {
				List<Long> pidList = new ArrayList<Long>();
				for (POProductDTO p : retList) {
					pidList.add(p.getId());
				}
				List<ProdPicMap> picList = poProductService.getProductPicListCache(pidList, PictureType.LIST);
				Map<Long, ProdPicMap> picMap = new HashMap<Long, ProdPicMap>();
				for (ProdPicMap pic : picList) {
					picMap.put(pic.getProductId(), pic);
				}
				BrandDTO brand = null;
				for (POProductDTO product : retList) {
					ProductListProductVO pvo = new ProductListProductVO();
					pvo.setId(String.valueOf(product.getId()));
					pvo.setBrandId(String.valueOf(product.getBrandId()));
					if (brand == null)
						brand = brandService.getBrandByBrandId(product.getBrandId());
					pvo.setBrandName(brand.getBrand().getBrandNameAuto());
					pvo.setSameAsShop(product.getSameAsShop());
					pvo.setColorName(product.getColorName());
					ProdPicMap pic = picMap.get(product.getId());
					List<String> pathList = ItemCenterUtil.genProdPicPath(pic);
					pvo.setListShowPicList(pathList);
					pvo.setMarketPrice(product.getMarketPrice());
					pvo.setProductName(product.getProductName());
					pvo.setSalePrice(product.getSalePrice());
					List<POSkuDTO> skulist = (List<POSkuDTO>) product.getSKUList();
					List<ProductListSkuVO> skuVOList = new ArrayList<ProductListSkuVO>();
					if (skulist != null && skulist.size() > 0) {
						for (POSkuDTO sku : skulist) {
							ProductListSkuVO skuVO = new ProductListSkuVO();
							skuVO.setId(sku.getId());
							String size = sku.getSize();
							int s = size.indexOf("(");
							int e = size.lastIndexOf(")");
							if (s > 0 && e > s) {
								skuVO.setSize(size.substring(0, s));
							} else
								skuVO.setSize(size);
							skuVO.setNum(sku.getSkuNum() + sku.getSupplierSkuNum());
							Integer tmpCartStock = sku.getCartStock();
							if (tmpCartStock == null) {
								skuVO.setState(0);
								skuVOList.add(skuVO);
								continue;
							}
							Integer tmpOrderStock = sku.getOrderStock();
							if (tmpOrderStock == null) {
								skuVO.setState(0);
								skuVOList.add(skuVO);
								continue;
							}
							skuVO.setState(ItemCenterUtils.getStockType(tmpCartStock, tmpOrderStock));
							skuVOList.add(skuVO);
						}
					}
					// 获取店铺名
					Schedule schedule = scheduleFacade.getScheduleByScheduleId(product.getPoId());
					pvo.setShopName(schedule == null ? "" : schedule.getSupplierName());
					pvo.setSkuList(skuVOList);
					// 获取价格列表
					pvo.setPriceList(itemFacade.getProductPriceDTOByProductId(product.getId()));
					list.add(pvo);
				}
			}

			retVO.setList(list);
			retVO.setHasNext(result.isHasNext());
			retVO.setTotal(result.getTotal());
			return retVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	public BaseJsonListResultVO getProductListBySearchParam(PoProductListSearchVO param) {
		BaseJsonListResultVO result = itemFacade.getProductDTOListBySearchParam(param);
		List<ProductListProductVO> list = new ArrayList<ProductListProductVO>();
		List<PoProductVo> retList = (List<PoProductVo>) result.getList();
		if (retList != null && retList.size() > 0) {
			List<Long> pidList = new ArrayList<Long>();
			for (PoProductVo p : retList) {
				pidList.add(p.getId());
			}
			List<ProdPicMap> picList = poProductService.getProductPicListCache(pidList, PictureType.LIST);
			Map<Long, ProdPicMap> picMap = new HashMap<Long, ProdPicMap>();
			for (ProdPicMap pic : picList) {
				picMap.put(pic.getProductId(), pic);
			}
			BrandDTO brand = null;
			for (PoProductVo product : retList) {
				ProductListProductVO pvo = new ProductListProductVO();
				pvo.setId(String.valueOf(product.getId()));
				pvo.setBrandId(String.valueOf(product.getBrandId()));
				if (brand == null) {
					brand = brandService.getBrandByBrandId(product.getBrandId());
				}
				pvo.setBrandName(brand.getBrand().getBrandNameAuto());
				pvo.setSameAsShop(product.getSameAsShop());
				pvo.setColorName(product.getColorName());
				ProdPicMap pic = picMap.get(product.getId());
				List<String> pathList = ItemCenterUtil.genProdPicPath(pic);
				pvo.setListShowPicList(pathList);
				pvo.setMarketPrice(product.getMarketPrice());
				pvo.setProductName(product.getProductName());
				pvo.setSalePrice(product.getSalePrice());
				list.add(pvo);
			}
		}
		BaseJsonListResultVO retVO = new BaseJsonListResultVO();
		retVO.setList(list);
		retVO.setHasNext(result.isHasNext());
		retVO.setTotal(result.getTotal());
		return retVO;
	}

}