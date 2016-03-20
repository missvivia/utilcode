package com.xyl.mmall.mainsite.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.backend.vo.ProductSKUBackendVO;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.common.util.ItemCenterUtils;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.ItemCenterExceptionHandler;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductUserFavDTO;
import com.xyl.mmall.itemcenter.dto.PoProductVo;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.enums.SortType;
import com.xyl.mmall.itemcenter.meta.ProdPicMap;
import com.xyl.mmall.itemcenter.param.PoProductSo;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.POSizeService;
import com.xyl.mmall.itemcenter.service.PoProductUserFavService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;
import com.xyl.mmall.itemcenter.util.ProductComparator;
import com.xyl.mmall.mainsite.facade.MainsiteItemFacade;
import com.xyl.mmall.mainsite.util.SkuSpecComparator;
import com.xyl.mmall.mainsite.vo.DetailPromotionVO;
import com.xyl.mmall.mainsite.vo.PoProductListSearchVO;
import com.xyl.mmall.mainsite.vo.ProductListProductVO;
import com.xyl.mmall.mainsite.vo.ProductListSkuVO;
import com.xyl.mmall.mainsite.vo.SizeSpecVO;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.promotion.activity.Label;
import com.xyl.mmall.promotion.meta.Promotion;
import com.xyl.mmall.promotion.service.PromotionService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.SchedulePageService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@Facade
public class MainsiteItemFacadeImpl implements MainsiteItemFacade {
	private static final Logger logger = LoggerFactory.getLogger(MainsiteItemFacadeImpl.class);

	public final static String desp = "desp";

	public final static String name = "name";

	@Resource
	private POProductService poProductService;

	@Resource
	private ProductService productService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private SizeTemplateService sizeTemplateService;

	@Resource
	private POSizeService poSizeService;

	@Resource
	private PromotionService promotionService;

	@Resource
	private BrandService brandService;

	@Resource
	private SchedulePageService schedulePageService;

	@Resource
	private CartService cartService;

	@Resource
	private SkuOrderStockService skuOrderStockService;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;
	
	@Resource
	private PoProductUserFavService poProductUserFavService;
	
	@Resource
	private ItemProductService itemProductService;
	
	@Resource
	private BusinessService businessService;

	@Override
	public int isFollowBrand(long loginId, long brandId) {
		if (brandService.isBrandInFavList(loginId, brandId))
			return 1;
		else
			return 0;
	}

	@Override
	public boolean isIPAllowed(int areaCode, long poId) {
		PODTO po = scheduleService.getScheduleById(poId);
		boolean isIn = false;
		List<ScheduleSiteRela> saleCodeList = po.getScheduleDTO().getSiteRelaList();
		for (ScheduleSiteRela siteMap : saleCodeList) {
			if (siteMap.getSaleSiteId() == areaCode) {
				isIn = true;
				break;
			}
		}
		return isIn;
	}

	@Override
	public DetailPromotionVO getDetailPagePromotionInfo(long poId) {
		try {
			DetailPromotionVO promotionVO = null;
			Promotion promotion = promotionService.getPromotionByPO(poId, 0, 0, 0, true);
			if (promotion != null) {
				promotionVO = new DetailPromotionVO();
				String labels = promotion.getLabels();
				List<Map<String, String>> labelVOList = new ArrayList<Map<String, String>>();
				List<Label> labelList = JsonUtils.parseArray(labels, Label.class);
				for (Label label : labelList) {
					if (label.isSelect()) {
						Map<String, String> map = new HashMap<String, String>();
						map.put(name, label.getName());
						map.put(desp, label.getDesc());
						labelVOList.add(map);
					}
				}
				promotionVO.setTipList(labelVOList);
				promotionVO.setDesp(promotion.getDescription());
				long endTime = promotion.getEndTime();
				promotionVO.setCountDown(getCountDownTime(endTime));
			}
			return promotionVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	private long getCountDownTime(long endTime) {
		long now = new Date().getTime();
		return endTime - now;
	}

	@Override
	public BaseJsonListResultVO getProductList(PoProductListSearchVO param) {
		logger.info("=====getProductListData from database");
		long oldTime = System.currentTimeMillis();
		try {
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();

			long oldTime2 = System.currentTimeMillis();
			BaseJsonListResultVO result = getPoProductDTOList(param);
			long cost2 = System.currentTimeMillis() - oldTime2;
			logger.info("===cost2 for getPoProductDTOList:" + cost2);

			long oldTimeOther = System.currentTimeMillis();
			List<ProductListProductVO> list = new ArrayList<ProductListProductVO>();
			List<POProductDTO> retList = (List<POProductDTO>) result.getList();
			if (retList != null && retList.size() > 0) {
				List<Long> pidList = new ArrayList<Long>();
				for (POProductDTO p : retList) {
					pidList.add(p.getId());
				}
				List<ProdPicMap> picList = null;
				if (!param.isPreview())
					picList = poProductService.getProductPicListCache(pidList, PictureType.LIST);
				else
					picList = poProductService.getProductPicList(pidList, PictureType.LIST);
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
			long costOther = System.currentTimeMillis() - oldTimeOther;
			logger.info("===costOther,getProductList:" + costOther);

			retVO.setList(list);
			retVO.setHasNext(result.isHasNext());
			retVO.setTotal(result.getTotal());
			long allCost = System.currentTimeMillis() - oldTime;
			logger.info("===getProductList allCost:" + allCost);
			return retVO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw ItemCenterExceptionHandler.getThrowAppException(e);
		}
	}

	@Override
	public BaseJsonListResultVO getPoProductDTOList(PoProductListSearchVO param) {
		BaseJsonListResultVO retVO = new BaseJsonListResultVO();
		List<POProductDTO> retList = new ArrayList<POProductDTO>();
		long poId = param.getScheduleId();

		List<POProductDTO> filterList = null;
		if (!param.isPreview())
			filterList = poProductService.getProductDTOListByPoCache(poId);
		else
			filterList = poProductService.getProductDTOListByPo(poId);
		BigDecimal priceFrom = param.getPriceFrom();

		BigDecimal priceTo = param.getPriceTo();

		long catgoryId = param.getCategoryId();
		List<POProductDTO> pList = new ArrayList<POProductDTO>();
		for (POProductDTO p : filterList) {
			if (catgoryId == 0 || p.getLowCategoryId() == catgoryId) {
				BigDecimal salePrice = p.getSalePrice();
				if (priceFrom == null && priceTo == null)
					pList.add(p);
				else if (priceFrom != null && priceTo != null) {
					if (priceFrom.compareTo(salePrice) <= 0 && priceTo.compareTo(salePrice) >= 0)
						pList.add(p);
				} else if (priceFrom == null) {
					if (priceTo.compareTo(salePrice) >= 0)
						pList.add(p);
				} else {
					if (priceFrom.compareTo(salePrice) <= 0)
						pList.add(p);
				}
			}
		}
		boolean hasNext = false;
		if (pList.size() > 0) {
			List<Long> skuIdList = getSkuIdList(pList);
			if (skuIdList != null && skuIdList.size() > 0) {
				Map<Long, Integer> cartStock = null;
				Map<Long, Integer> orderStock = null;
				try {
					cartStock = cartService.getInventoryCount(skuIdList);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					cartStock = new HashMap<Long, Integer>();
				}
				try {
					orderStock = commonFacade.getOrderSkuStock(skuIdList);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					orderStock = new HashMap<Long, Integer>();
				}

				for (POProductDTO product : pList) {
					List<POSkuDTO> skulist = (List<POSkuDTO>) product.getSKUList();
					int stock = 0;
					int cart = 0;
					int total = 0;
					if (skulist != null && skulist.size() > 0) {
						for (POSkuDTO sku : skulist) {
							Integer orderStk = orderStock.get(sku.getId());
							if (orderStk == null)
								orderStk = 0;
							Integer cartStk = cartStock.get(sku.getId());
							if (cartStk == null)
								cartStk = 0;
							sku.setCartStock(cartStk);
							sku.setOrderStock(orderStk);
							int skuTotal = sku.getSkuNum();
							total = total + skuTotal;
							stock = stock + orderStk;
							cart = cart + cartStk;
						}
					}
					product.setStock(stock);
					product.setCartStock(cart);
					product.setSaleTotal(total - stock);
				}
			}
			for (POProductDTO p : pList) {
				double sale = p.getSalePrice().doubleValue();
				double market = p.getMarketPrice().doubleValue();
				double discount = 0;
				if (sale != 0)
					discount = sale / market;
				p.setDiscount(new BigDecimal(ItemCenterUtils.formatDouble1(discount)));
			}
			boolean isAsc = !param.isDesc();
			int sort = param.getOrder();
			if (sort == 0) {
				SortType sortType = SortType.DEFAULT;
				int defaultSortType = schedulePageService.getPOPagePrdListOrderType(poId);
				Collections.sort(pList, new ProductComparator(isAsc, sortType, defaultSortType));
			} else if (sort == 1) {
				SortType sortType = SortType.PRICE;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			} else if (sort == 2) {
				SortType sortType = SortType.SALE;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			} else if (sort == 3) {
				SortType sortType = SortType.DISCOUNT;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			}
			int limit = param.getLimit();
			int copyLimit = limit;
			int offset = param.getOffset();
			long lastId = param.getLastId();
			if (lastId > 0) {
				for (int i = 0; i < pList.size(); i++) {
					POProductDTO product = pList.get(i);
					if (product.getId() == lastId) {
						offset = i + 1;
						break;
					}
				}
			}
			if (offset <= pList.size() - 1) {
				for (int i = offset; i < pList.size(); i++) {
					POProductDTO product = pList.get(i);
					retList.add(product);
					copyLimit--;
					if (copyLimit == 0)
						break;
				}
			}
			if ((limit + offset) < pList.size())
				hasNext = true;
		}
		retVO.setList(retList);
		retVO.setHasNext(hasNext);
		retVO.setTotal(pList.size());
		return retVO;
	}

	private List<Long> getSkuIdList(List<POProductDTO> list) {
		List<Long> retList = new ArrayList<Long>();
		for (POProductDTO product : list) {
			List<POSkuDTO> skulist = (List<POSkuDTO>) product.getSKUList();
			if (skulist != null && skulist.size() > 0) {
				retList.addAll(ItemCenterUtils.getSkuList(skulist));
			}
		}
		return retList;
	}

	@Override
	public int getCartSkuNum(long usrId, int areaId, long skuId) {
		return cartService.getValidCartItemCount(usrId, areaId, skuId);
	}

	/**
	 * 对于detail/check请求的优化
	 * <p>
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.MainsiteItemFacade#detailSizeCheckForYouhua(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO detailSizeCheckForYouhua(long poId, long pid) {
		try {
			BaseJsonListResultVO retVO = new BaseJsonListResultVO();
			List<Long> skuList = poProductService.getSkuIds(poId, pid);
			List<SizeSpecVO> retList = getSkuSpecList(skuList);
			commonFacade.getStock(retList);
			retVO.setHasNext(false);
			retVO.setList(retList);
			BaseJsonVO retObj = new BaseJsonVO(retVO);
			retObj.setCode(ErrorCode.SUCCESS);
			return retObj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ItemCenterExceptionHandler.getAjaxExceptionJsonVO(e);
		}
	}

	public List<SizeSpecVO> getSkuSpecList(List<Long> skuList) {
		List<SizeSpecVO> specList = new ArrayList<SizeSpecVO>();
		for (long sku : skuList) {
			SizeSpecVO sizeSpec = new SizeSpecVO();
			sizeSpec.setSkuId(String.valueOf(sku));
			specList.add(sizeSpec);
		}
		Collections.sort(specList, new SkuSpecComparator());
		commonFacade.getStock(specList);
		return specList;
	}

	@Override
	public BaseJsonListResultVO getProductDTOListByCategory(PoProductListSearchVO param) {
		BaseJsonListResultVO retVO = new BaseJsonListResultVO();
		List<POProductDTO> retList = new ArrayList<POProductDTO>();

		long catgoryId = param.getCategoryId();
		List<POProductDTO> filterList = null;
		if (!param.isPreview())
			filterList = poProductService.getProductDTOListByCategoryAndCache(catgoryId);
		else
			filterList = poProductService.getProductDTOListByCatgory(catgoryId);
		BigDecimal priceFrom = param.getPriceFrom();

		BigDecimal priceTo = param.getPriceTo();

		List<POProductDTO> pList = new ArrayList<POProductDTO>();
		String brandIds = param.getBrandIds();
		List<Long> list = JsonUtils.parseArray(brandIds, Long.class);
		
		// 价格,品牌过滤
		for (POProductDTO p : filterList) {
			BigDecimal salePrice = p.getSalePrice();
			// brand
			if (list.size() > 0) {
				if (!list.contains(p.getBrandId()))
					continue;
			}
			// price
			if (priceFrom == null && priceTo == null)
				pList.add(p);
			else if (priceFrom != null && priceTo != null) {
				if (priceFrom.compareTo(salePrice) <= 0 && priceTo.compareTo(salePrice) >= 0)
					pList.add(p);
			} else if (priceFrom == null) {
				if (priceTo.compareTo(salePrice) >= 0)
					pList.add(p);
			} else {
				if (priceFrom.compareTo(salePrice) <= 0)
					pList.add(p);
			}
		}
		boolean hasNext = false;
		if (pList.size() > 0) {
			List<Long> skuIdList = getSkuIdList(pList);
			if (skuIdList != null && skuIdList.size() > 0) {
				Map<Long, Integer> cartStock = null;
				Map<Long, Integer> orderStock = null;
				// 库存
				try {
					cartStock = cartService.getInventoryCount(skuIdList);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					cartStock = new HashMap<Long, Integer>();
				}
				try {
					orderStock = commonFacade.getOrderSkuStock(skuIdList);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					orderStock = new HashMap<Long, Integer>();
				}

				for (POProductDTO product : pList) {
					List<POSkuDTO> skulist = (List<POSkuDTO>) product.getSKUList();
					int stock = 0;
					int cart = 0;
					int total = 0;
					if (skulist != null && skulist.size() > 0) {
						for (POSkuDTO sku : skulist) {
							Integer orderStk = orderStock.get(sku.getId());
							if (orderStk == null)
								orderStk = 0;
							Integer cartStk = cartStock.get(sku.getId());
							if (cartStk == null)
								cartStk = 0;
							sku.setCartStock(cartStk);
							sku.setOrderStock(orderStk);
							int skuTotal = sku.getSkuNum();
							total = total + skuTotal;
							stock = stock + orderStk;
							cart = cart + cartStk;
						}
					}
					product.setStock(stock);
					product.setCartStock(cart);
					product.setSaleTotal(total - stock);
				}
			}
			for (POProductDTO p : pList) {
				double sale = p.getSalePrice().doubleValue();
				double market = p.getMarketPrice().doubleValue();
				double discount = 0;
				if (sale != 0)
					discount = sale / market;
				p.setDiscount(new BigDecimal(ItemCenterUtils.formatDouble1(discount)));
			}
			boolean isAsc = !param.isDesc();
			int sort = param.getOrder();
			if (sort == 0) {
				SortType sortType = SortType.DEFAULT;
//				int defaultSortType = schedulePageService.getPOPagePrdListOrderType(poId);
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			} else if (sort == 1) {
				SortType sortType = SortType.PRICE;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			} else if (sort == 2) {
				SortType sortType = SortType.SALE;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			} else if (sort == 3) {
				SortType sortType = SortType.DISCOUNT;
				Collections.sort(pList, new ProductComparator(isAsc, sortType));
			}
			int limit = param.getLimit();
			int copyLimit = limit;
			int offset = param.getOffset();
			long lastId = param.getLastId();
			if (lastId > 0) {
				for (int i = 0; i < pList.size(); i++) {
					POProductDTO product = pList.get(i);
					if (product.getId() == lastId) {
						offset = i + 1;
						break;
					}
				}
			}
			if (offset <= pList.size() - 1) {
				for (int i = offset; i < pList.size(); i++) {
					POProductDTO product = pList.get(i);
					retList.add(product);
					copyLimit--;
					if (copyLimit == 0)
						break;
				}
			}
			if ((limit + offset) < pList.size())
				hasNext = true;
		}
		retVO.setList(retList);
		retVO.setHasNext(hasNext);
		retVO.setTotal(pList.size());
		return retVO;
	}

	@Override
	public BaseJsonListResultVO getProductDTOListBySearchParam(
			PoProductListSearchVO param) {
		PoProductSo poProductSo = new PoProductSo();
		poProductSo.setBarCode(param.getBarcode());
		poProductSo.setProductIdList(JsonUtils.parseArray (param.getProductIds(), Long. class));
		poProductSo.setStime(param.getReplenishTime());
		BaseJsonListResultVO retVO = new BaseJsonListResultVO();
		List<PoProductVo> poProductVos = poProductService.getPoProductosByParam(poProductSo);
		retVO.setList(poProductVos);
		return retVO;
	}

	@Override
	public List<ProductPriceDTO> getProductPriceDTOByProductId(long productId) {
		return productService.getProductPriceDTOByProductId(productId);
	}

	@Override
	public int addPoProductIntoFavList(long userId, long productId) {
		List<Long> skuidList = new ArrayList<Long>();
		skuidList.add(productId);
		Map<Long, Boolean> skuStatusMap = itemProductService.getProductStatusIsOnline(skuidList);
		if(skuStatusMap.get(productId)==null){
			return -1;//商品已删除
		}
		if(!skuStatusMap.get(productId)){
			return -2;//商品已下架
		}
		if(!poProductUserFavService.addPoProductIntoFavList(userId, productId)){
			return -3;//收藏商品失败
		}
		return 1;
	}

	@Override
	public boolean removePoProductFromFavList(long userId, long poId) {
		return poProductUserFavService.removePoProductFromFavList(userId, poId);
	}

	@Override
	public Map<String, String> getPoProductFavListByUserIdOrPoIds(
			long userId, List<Long> poIds) {
		Map<String, String>map = new HashMap<String, String>();
		List<PoProductUserFavDTO>dtos = poProductUserFavService.getPoProductFavListByUserIdOrPoIds(userId, poIds);
		for(PoProductUserFavDTO dto:dtos){
			map.put(String.valueOf(dto.getPoId()), "Y");
		}
		return map;
	}

	@Override
	public BaseJsonListResultVO getProductDTOListByProductUserFavParam(
			ProductUserFavParam productUserFavParam) {
		BaseJsonListResultVO baseJsonListResultVO = new BaseJsonListResultVO();
		BasePageParamVO<PoProductUserFavDTO> basePageParamVO = poProductUserFavService.getPageProductUserFavDTOByUserId(productUserFavParam);
		if(CollectionUtil.isEmptyOfList(basePageParamVO.getList())){
			return baseJsonListResultVO;
		}
		//取商品Ids
		Function< PoProductUserFavDTO, Long> getidFunc = new Function<PoProductUserFavDTO, Long>() {
			public Long apply(PoProductUserFavDTO productUserFavDTO) {
				return productUserFavDTO.getPoId();
			}
		};
		List<Long> productIdlist = Lists.transform( basePageParamVO.getList(), getidFunc );
		List<ProductSKUDTO> productSKUDTOs = itemProductService.getProductSKUDTOByProdIds(productIdlist);
	
		//取商家信息
		Set<Long>businessIdSet = new HashSet<Long>();
		if(CollectionUtil.isEmptyOfList(productSKUDTOs)){
			return baseJsonListResultVO;
		}
		Map<Long, ProductSKUDTO> productSKUDTOMap = new HashMap<Long, ProductSKUDTO>();
		for(ProductSKUDTO productSKUDTO:productSKUDTOs){
			businessIdSet.add(productSKUDTO.getBusinessId());
			productSKUDTOMap.put(productSKUDTO.getId(), productSKUDTO);
		}

		List<BusinessDTO> businessDTOs = businessService.getBusinessDTOListByIdList(new ArrayList<Long>(businessIdSet));
		Map<Long, BusinessDTO> businessMap = Maps.uniqueIndex(businessDTOs,new Function<BusinessDTO,Long>(){
			  public Long apply(BusinessDTO businessDTO) {
                  return businessDTO.getId();
           }});

		Map<String, List<ProductPriceDTO>> productPriceMap = productService.getProductPriceDTOByProductIds(productIdlist);
		BusinessDTO businessDTO = null;
		
		//商品DTO转VO
		List<ProductSKUBackendVO> productSKUBackendVOs = new ArrayList<ProductSKUBackendVO>();
		//skuId是有排序的
		for(Long productSKUId:productIdlist){
			ProductSKUDTO productSKUDTO = productSKUDTOMap.get(productSKUId);
			if(productSKUDTO==null){
				continue;
			}
			businessDTO = businessMap.get(productSKUDTO.getBusinessId());
			if(businessDTO==null){
				continue;
			}
			productSKUDTO.setStoreName(businessDTO.getStoreName());
			productSKUDTO.setBatchCash(productSKUDTO.getBatchCash());
			productSKUDTO.setPriceList(productPriceMap.get(String.valueOf(productSKUDTO.getId())));
			productSKUBackendVOs.add(new ProductSKUBackendVO(productSKUDTO));
		}
					
		baseJsonListResultVO.setList(productSKUBackendVOs);
		baseJsonListResultVO.setTotal(basePageParamVO.getTotal());
		return baseJsonListResultVO;
	}

}
