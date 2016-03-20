package com.xyl.mmall.mobile.web.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.rpc.RpcContext;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.backend.facade.ProductFacade;
import com.xyl.mmall.backend.vo.ProductSKUBackendVO;
import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.cart.service.CartDeleteCacheService;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.common.facade.BrandQueryFacade;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.common.util.PriceCalculateUtil;
import com.xyl.mmall.constant.NkvConstant;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.mobile.web.facade.MobileCalCenterFacade;
import com.xyl.mmall.mobile.web.facade.MobileCartFacade;
import com.xyl.mmall.mobile.web.facade.MobileItemFacade;
import com.xyl.mmall.mobile.web.util.MobileVOConvertUtil;
import com.xyl.mmall.mobile.web.vo.CartActivationVO;
import com.xyl.mmall.mobile.web.vo.CartInfoVO;
import com.xyl.mmall.mobile.web.vo.CartInnervVO;
import com.xyl.mmall.mobile.web.vo.CartPOVO;
import com.xyl.mmall.mobile.web.vo.CartSkuItemVO;
import com.xyl.mmall.mobile.web.vo.CartStoreVO;
import com.xyl.mmall.mobile.web.vo.CartVO;
import com.xyl.mmall.mobile.web.vo.InputParam;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionCartDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * 购物车相关门面工程
 * 
 * @author Yang,Nan
 * 
 */
@Facade("mobileCartFacade")
public class MobileCartFacadeImpl implements MobileCartFacade {
	
	private static Logger logger = LoggerFactory.getLogger(MobileCartFacadeImpl.class);

	@Resource
	private CartService cartService;

	@Resource
	private POProductService poProductService;

	@Resource
	private MobileCalCenterFacade calCenterFacade;

	@Resource
	private ScheduleService scheduleService;

	@Autowired
	private CartDeleteCacheService cartDeleteCacheService;

	@Resource
	private BrandQueryFacade brandQueryFacade;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private MobileItemFacade mainsiteItemFacade;
	
	@Autowired
	private ProductFacade productFacade;
	
	@Autowired
	private ItemProductService itemProductService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ItemCenterCommonFacade commonFacade;

	public CartVO getCart(long userId, int provinceId, List<Long> selectedIds,
			PlatformType platformType) {
		return getCartVOInternal(userId, provinceId, selectedIds, null, 0,
				platformType);
	}

	@Override
	public CartVO getCart(long userId, int provinceId, List<Long> selectedIds,
			RetArg retArg, PlatformType platformType) {
		return getCartVOInternal(userId, provinceId, selectedIds, retArg, 0,
				platformType);
	}

	private CartVO getCartVOInternal(long userId, int provinceId,
			List<Long> selectedIds, RetArg retArg, int operationType,
			PlatformType platformType) {
		if (operationType != MobileCartFacade.OPERATION_DELETE)
			cartService.deleteDeletedReocordFromCart(userId, provinceId);
		
		// TODO Auto-generated method stub
		CartVO vo = new CartVO();
		CartInnervVO cartInnervVO = getFavorCaculateResultDTOBySelected(userId,
				provinceId, null,platformType);
		RetArgUtil.put(retArg, cartInnervVO.getFavorCaculateResultDTO());

		vo.setActivations(convertToCartActivationVO(cartInnervVO
				.getFavorCaculateResultDTO().getActivations(), cartInnervVO
				.getBatchGetScheduleListByIdList().getPoList(), cartInnervVO
				.getSkuDTOList(), CartSkuItemVO.STATE_NORMAL));
		vo.setPoList(convertToCartPOVO(cartInnervVO.getFavorCaculateResultDTO()
				.getNotSatisfySkuList(), cartInnervVO
				.getBatchGetScheduleListByIdList().getPoList(), cartInnervVO
				.getSkuDTOList(), CartSkuItemVO.STATE_NORMAL));
		vo.setUserId(userId);
		CartInfoVO cartInfoVO = new CartInfoVO();
		cartInfoVO.setFree(cartInnervVO.getFavorCaculateResultDTO()
				.isFreeExpPrice());
		cartInfoVO.setLeftTime(cartService.getCartLeftTime(userId, provinceId));
		cartInfoVO.setUpdateTime(cartService.getCartUpdateTime(userId,
				provinceId));

		CartInnervVO deleteAndOvertimeCartInnverVO = MakeCartInnverVO(userId,
				provinceId, null, 0, false, cartInnervVO.getCartDTO()
						.getDeleteAandOverTimeIdOfValid(), cartInnervVO
						.getCartDTO().getDeleteAndOvertimeSkuOfValid(),
				platformType);

		List<CartActivationVO> deleteAndOvertimeCartActivationVOList = convertToCartActivationVO(
				deleteAndOvertimeCartInnverVO.getFavorCaculateResultDTO()
						.getActivations(), deleteAndOvertimeCartInnverVO
						.getBatchGetScheduleListByIdList().getPoList(),
				deleteAndOvertimeCartInnverVO.getSkuDTOList(),
				CartSkuItemVO.STATE_OVERTIME);
		List<CartPOVO> deleteAndOvertimeCartPOVOList = convertToCartPOVO(
				deleteAndOvertimeCartInnverVO.getFavorCaculateResultDTO()
						.getNotSatisfySkuList(), deleteAndOvertimeCartInnverVO
						.getBatchGetScheduleListByIdList().getPoList(),
				deleteAndOvertimeCartInnverVO.getSkuDTOList(),
				CartSkuItemVO.STATE_OVERTIME);

		// 把上面2个list塞入cartvo中对应po下作为skuitem
		addDeleteVo(vo, deleteAndOvertimeCartInnverVO,
				deleteAndOvertimeCartActivationVOList,
				deleteAndOvertimeCartPOVOList);
		processOvertimeItem(vo, cartInnervVO);

		procesInvalidItem(vo, cartInnervVO);
		processParentDelete(vo);
		vo.setCartInfoVO(cartInfoVO);
		vo.setRetCode(cartInnervVO.getRetcode());
		cartInfoVO.setTotalPrice(markSelectForCartVO(vo, selectedIds,
				cartInnervVO));
		if(selectedIds==null){
			selectedIds = new ArrayList<Long>();
			if(vo.getPoList()!=null){
				for(CartPOVO cartPOVO:vo.getPoList()){
					for(CartSkuItemVO cartSkuItemVO :cartPOVO.getSkulist()){
						selectedIds.add(cartSkuItemVO.getProductId());
					}
				}
			}
		}
		if(selectedIds.size()>0){
			Map<String, List<ProductPriceDTO>> map = productService.getProductPriceDTOByProductIds(selectedIds);
			vo.setProductMap(map);
			
			Map<String, String> poUserFavMap = mainsiteItemFacade.getPoProductFavListByUserIdOrPoIds(userId, selectedIds);
			vo.setPoUsrFavMap(poUserFavMap);
		}
		
	/*	Map<Long,Long> skuSupliermap = new HashMap<Long, Long>();
		for(SkuParam skuParam : skuParamList){
			skuSupliermap.put(skuParam.getSupplierId(), skuParam.getSkuId());
		}
		List<Store> stores = storeService.getStoreSByUserIds(new ArrayList<Long>(skuSupliermap.keySet()));
		Map<Long, String>skuidMap = new HashMap<Long, String>();;
		for(Store store:stores){
			skuidMap.put(skuSupliermap.get(store.getUserId()), store.getStoreName());
		}
		
		SkuStoreRelationResult relationResult = new SkuStoreRelationResult();
		relationResult.setSkuStoremap(skuidMap);*/
		reorder(vo);
		return vo;

	}

	private void reorder(CartVO vo) {
		CollectionUtil.sortByHasShowIndex2(vo.getActivations());
		for (CartActivationVO avo : vo.getActivations()) {
			CollectionUtil.sortByHasShowIndex2(avo.getPoList());
			for (CartPOVO po : avo.getPoList())
				CollectionUtil.sortByHasShowIndex2(po.getSkulist());
		}

		CollectionUtil.sortByHasShowIndex2(vo.getPoList());
		for (CartPOVO po : vo.getPoList())
			CollectionUtil.sortByHasShowIndex2(po.getSkulist());
	}

	private void procesInvalidItem(CartVO vo, CartInnervVO cartInnervVO) {
		List<CartSkuItemVO> invalidCartItemList = new ArrayList<CartSkuItemVO>();
		for (CartItemDTO dto : cartInnervVO.getCartDTO().getInvalidSkuOfValid()) {
			CartSkuItemVO sku = new CartSkuItemVO();
			if (dto.getCount() == CartItemDTO.TYPE_POINVALID)
				sku.setStatus(CartSkuItemVO.STATE_OVERPO);
			if (dto.getCount() == CartItemDTO.TYPE_SOLDOUT)
				sku.setStatus(CartSkuItemVO.STATE_SOLDOUT);
			sku.setId(dto.getSkuid());
			invalidCartItemList.add(sku);

		}
		List<POSkuDTO> skuDTOList = poProductService
				.getSkuDTOListBySkuId(cartInnervVO.getCartDTO()
						.getInvalidIdOfValid());

		for (CartSkuItemVO sku : invalidCartItemList) {
			for (POSkuDTO sku2 : skuDTOList) {
				if (sku.getId() == sku2.getId()) {
					sku.setColor(sku2.getColorName());
					sku.setSize(sku2.getSize());
					sku.setUrl(sku2.getProductLinkUrl());
					sku.setProductId(sku2.getProductId());
					sku.setThumb(sku2.getThumb());
					sku.setMarketPrice(sku2.getMarketPrice());
					sku.setName(sku2.getProductName());
				}
			}
		}

		vo.setOvertimeCount(cartInnervVO.getCartDTO().getOverTimeIdOfValid() == null ? 0
				: cartInnervVO.getCartDTO().getOverTimeIdOfValid().size());
		vo.setInvalidCartItemList(invalidCartItemList);

	}

	private void processOvertimeItem(CartVO vo, CartInnervVO cartInnervVO) {
		if (vo.getActivations() != null) {
			for (CartActivationVO avo : vo.getActivations()) {
				for (CartPOVO po : avo.getPoList()) {
					for (CartSkuItemVO skuvo : po.getSkulist()) {
						for (CartItemDTO cartdto : cartInnervVO.getCartDTO()
								.getOvertimeCartItemList())
							if (skuvo.getId() == cartdto.getSkuid()) {
								skuvo.setOverTime(true);
								skuvo.setDeleted(false);
								skuvo.setStatus(CartSkuItemVO.STATE_OVERTIME);
								break;
							}
					}
				}
			}
		}
		for (CartPOVO po : vo.getPoList()) {
			for (CartSkuItemVO skuvo : po.getSkulist()) {
				for (CartItemDTO cartdto : cartInnervVO.getCartDTO()
						.getOvertimeCartItemList())
					if (skuvo.getId() == cartdto.getSkuid()) {
						skuvo.setOverTime(true);
						skuvo.setDeleted(false);
						skuvo.setStatus(CartSkuItemVO.STATE_OVERTIME);
						break;
					}
			}

		}

	}

	private void processParentDelete(CartVO vo) {
		if (vo.getActivations() != null) {
			for (CartActivationVO avo : vo.getActivations()) {
				for (CartPOVO po : avo.getPoList()) {
					boolean isPoDeleted = true;
					int count = 0;
					for (CartSkuItemVO skuvo : po.getSkulist()) {
						if (skuvo.isDeleted() == false
								|| skuvo.getStatus() != CartSkuItemVO.STATE_DELETED) {
							isPoDeleted = false;
							break;
						}
					}
					po.setDelete(isPoDeleted);
				}
				boolean isActDeleted = true;
				for (CartPOVO po : avo.getPoList()) {
					if (po.isDeleted() == false) {
						isActDeleted = false;
						break;
					}
				}
				avo.setDelete(isActDeleted);
			}
		}
		for (CartPOVO po : vo.getPoList()) {

			boolean isPoDeleted = true;
			for (CartSkuItemVO skuvo : po.getSkulist()) {
				if (skuvo.isDeleted() == false
						|| skuvo.getStatus() != CartSkuItemVO.STATE_DELETED) {
					isPoDeleted = false;
					break;
				}
			}
			po.setDelete(isPoDeleted);

		}

	}

	private List<CartSkuItemVO> processItem(List<CartSkuItemVO> list,
			String type) {
		if (type == CartSkuItemVO.STATE_DELETED) {
			for (CartSkuItemVO c : list) {
				c.setDeleted(true);
				c.setStatus(CartSkuItemVO.STATE_DELETED);
			}

		} else if (type == CartSkuItemVO.STATE_OVERTIME) {
			for (CartSkuItemVO c : list) {
				c.setOverTime(true);
				c.setStatus(CartSkuItemVO.STATE_OVERTIME);
			}
		}

		return list;
	}

	private void addToVo(CartVO vo, CartInnervVO innerVO,
			List<CartActivationVO> activationVOList, List<CartPOVO> pOVOList,
			String type) {

		List<CartActivationVO> actionNotFoundList = new ArrayList<CartActivationVO>();
		List<CartPOVO> poNotFoundList = new ArrayList<CartPOVO>();
		// 找找每一个活动在有效购物车里面的活动中，有没有
		for (CartActivationVO avo : activationVOList) {
			boolean actionFound = false;
			for (CartActivationVO avoCart : vo.getActivations()) {
				if (avoCart.getId() == avo.getId()) {
					actionFound = true;

					// 查看每个活动，有没有出现过
					for (CartPOVO po2 : avo.getPoList()) {
						boolean poFound = false;
						for (CartPOVO poCart : avoCart.getPoList()) {
							if (poCart.getPoId() == po2.getPoId()) {
								poFound = true;
								// po购物车已有，那么把sku加进来
								poCart.getSkulist().addAll(
										processItem(po2.getSkulist(), type));
								break;
							}
						}
						// 没找到
						if (poFound == false) {
							processItem(po2.getSkulist(), type);
							poNotFoundList.add(po2);
						}

					}
					// 把不重复的po加进来
					avoCart.getPoList().addAll(poNotFoundList);
					break;
				}
			}
			// 没找到
			if (actionFound == false) {
				for (CartPOVO p : avo.getPoList())
					processItem(p.getSkulist(), type);
				actionNotFoundList.add(avo);
			}
		}

		vo.getActivations().addAll(actionNotFoundList);

		poNotFoundList = new ArrayList<CartPOVO>();
		for (CartPOVO po2 : pOVOList) {
			boolean poFound = false;
			for (CartPOVO poCart : vo.getPoList()) {
				if (poCart.getPoId() == po2.getPoId()) {
					poFound = true;
					// po购物车已有，那么把sku加进来
					poCart.getSkulist().addAll(
							processItem(po2.getSkulist(), type));
					break;
				}
			}
			// 没找到
			if (poFound == false) {
				processItem(po2.getSkulist(), type);
				poNotFoundList.add(po2);
			}

		}
		vo.getPoList().addAll(poNotFoundList);
	}

	private void addDeleteVo(CartVO vo, CartInnervVO innerVO,
			List<CartActivationVO> deleteAndOvertimeCartActivationVOList,
			List<CartPOVO> deleteAndOvertimeCartPOVOList) {
		addToVo(vo, innerVO, deleteAndOvertimeCartActivationVOList,
				deleteAndOvertimeCartPOVOList, CartSkuItemVO.STATE_DELETED);
	}

	private BigDecimal markSelectForCartVO(CartVO vo, List<Long> selectedIds,
			CartInnervVO cartInnervVO) {
		BigDecimal totalprice = BigDecimal.ZERO;
		if (selectedIds != null) {
			if (vo.getActivations() != null) {
				for (CartActivationVO avo : vo.getActivations()) {
					for (CartPOVO po : avo.getPoList()) {
						for (CartSkuItemVO skuvo : po.getSkulist()) {
							for (Long s : selectedIds) {
								if (skuvo.getId() == s) {
									skuvo.setSelected(true);
									totalprice = totalprice.add(skuvo
											.getCartPrice().multiply(
													new BigDecimal(skuvo
															.getCount())));
								}
							}
						}

						boolean isPoSelected = true;
						for (CartSkuItemVO skuvo : po.getSkulist()) {
							if (skuvo.isSelected() == false
									&& skuvo.getStatus() == CartSkuItemVO.STATE_NORMAL) {
								isPoSelected = false;
								break;
							}
						}
						po.setSelected(isPoSelected);
					}

					boolean isActSelected = true;
					for (CartPOVO po : avo.getPoList()) {
						if (po.isSelected() == false) {
							isActSelected = false;
							break;
						}
					}
					avo.setSelected(isActSelected);
				}
			}
			for (CartPOVO po : vo.getPoList()) {
				for (CartSkuItemVO skuvo : po.getSkulist()) {
					for (Long s : selectedIds) {
						if (skuvo.getId() == s) {
							skuvo.setSelected(true);
							totalprice = totalprice
									.add(skuvo.getCartPrice().multiply(
											new BigDecimal(skuvo.getCount())));
						}
					}
				}

				boolean isPoSelected = true;
				for (CartSkuItemVO skuvo : po.getSkulist()) {
					if (skuvo.isSelected() == false
							&& skuvo.getStatus() == CartSkuItemVO.STATE_NORMAL) {
						isPoSelected = false;
						break;
					}
				}
				po.setSelected(isPoSelected);

			}
		} else {
			if (vo.getActivations() != null) {
				for (CartActivationVO avo : vo.getActivations()) {
					for (CartPOVO po : avo.getPoList()) {
						for (CartSkuItemVO skuvo : po.getSkulist()) {
							skuvo.setSelected(true);
							totalprice = totalprice
									.add(skuvo.getCartPrice().multiply(
											new BigDecimal(skuvo.getCount())));
						}
						po.setSelected(true);
					}
					avo.setSelected(true);
				}
			}
			for (CartPOVO po : vo.getPoList()) {
				for (CartSkuItemVO skuvo : po.getSkulist()) {
					skuvo.setSelected(true);
					totalprice = totalprice.add(skuvo.getCartPrice().multiply(
							new BigDecimal(skuvo.getCount())));
				}
				po.setSelected(true);
			}
		}

		return totalprice;
	}

	/**
	 * @param userId
	 * @param provinceId
	 * @param filterList
	 * @return
	 */
	private CartInnervVO getFavorCaculateResultDTOBySelected(long userId,
			int provinceId, List<Long> filterList,PlatformType platformType) {
		boolean caculateCoupon = false;
		return getFavorCaculateResultDTOBySelected(userId, provinceId,
				filterList, 0, caculateCoupon,platformType);

	}
	
	private CartInnervVO buildCartInnverVO(long userId, int provinceId,List<Long> filterList, long userCouponId, boolean caculateCoupon,
			PlatformType platformType){
		CartInnervVO vo = new CartInnervVO();
		CartDTO cartDTO = cartService.getCart(userId, provinceId);
		List<Long> paramList = cartDTO.getSkuidOfValid();
		List<CartItemDTO> skulistOfValid = cartDTO.getSkuOfValid();
		if (CollectionUtil.isNotEmptyOfCollection(filterList)) {
			vo.setCartIdsValid(isChildOfList(filterList,
					cartDTO.getSkuidOfValid()));
			//vo.setCartIdsValid(true);
			paramList = intersect(paramList, filterList);
			skulistOfValid = intersectCartItemDTO(skulistOfValid, filterList);
		}
		Map<Long, CartItemDTO> cartItemMap = CollectionUtil.convertCollToMap(skulistOfValid, "skuid");
		if(CollectionUtil.isEmptyOfMap(cartItemMap)){
			return null;
		}
		//取商品信息
		List<ProductSKUBackendVO> productSKUBackendVOs = productFacade.getProductSKUVO(paramList);
		//取商品区间价格
		Map<String, List<ProductPriceDTO>> productPricemap = productService.getProductPriceDTOByProductIds(paramList);
		// 返回有活动信息的VO
		FavorCaculateParamDTO paramDTO = new FavorCaculateParamDTO();
		paramDTO.setUserId(userId);
		paramDTO.setProvince(provinceId);
		paramDTO.setUserCouponId(userCouponId);
		paramDTO.setCaculateCoupon(caculateCoupon);
		paramDTO.setPlatformType(platformType);
		vo.setParamDTO(paramDTO);
		
		List<PromotionSkuItemDTO> promotionSkuItemDTOs = new ArrayList<PromotionSkuItemDTO>();
		for (ProductSKUBackendVO productSKUBackendVO:productSKUBackendVOs) {
				PromotionSkuItemDTO dto = new PromotionSkuItemDTO();
				CartItemDTO cartItemDTO = cartItemMap.get(productSKUBackendVO.getSkuId());
				BigDecimal cartPrice = PriceCalculateUtil.getCartPrice(cartItemDTO.getCount(), productPricemap.get(String.valueOf(productSKUBackendVO.getSkuId())),productSKUBackendVO.getBatchNum());
				BigDecimal oriRetailPrice = PriceCalculateUtil.getOriRetailPrice(cartItemDTO.getCount(), productPricemap.get(String.valueOf(productSKUBackendVO.getSkuId())));
				dto.setOriRetailPrice(oriRetailPrice);
				dto.setRetailPrice(cartPrice);
				dto.setCartPrice(cartPrice);
				dto.setSkuId(productSKUBackendVO.getSkuId());
				dto.setBusinessId(productSKUBackendVO.getSupplierId());
				dto.setUserId(userId);
				dto.setCount(cartItemDTO.getCount());
				dto.setBatchCash(productSKUBackendVO.getBatchCash());
				//dto.setSpeciList(productSKUBackendVO.getSpeciList());
				promotionSkuItemDTOs.add(dto);
				if(ProductStatusType.ONLINE.getIntValue()!=productSKUBackendVO.getProdStatus()){
					promotionSkuItemDTOs.clear();
					break;
				}
		} 
//        FavorCaculateResultDTO caculateActivation = calCenterFacade.caculateActivation(
//                skuPoListMap, paramDTO);

		FavorCaculateResultDTO caculateActivation = new FavorCaculateResultDTO();
		caculateActivation.setSkuList(promotionSkuItemDTOs);
		caculateActivation.setFreeExpPrice(true);
		// 添加优惠券相关信息
//		if(userCouponId > 0)
//		{
//		    calCenterFacade.caculateMmallCoupon(paramDTO, caculateActivation);
//		}
		
		vo.setFavorCaculateResultDTO(caculateActivation);
		vo.setCartDTO(cartDTO);
		return vo;
	}
	@Deprecated
	private CartInnervVO MakeCartInnverVO(long userId, int provinceId,
			List<Long> filterList, long userCouponId, boolean caculateCoupon,
			List<Long> paramList, List<CartItemDTO> skulistOfValid,
			PlatformType platformType) {

		CartInnervVO vo = new CartInnervVO();
		CartDTO cartDTO = cartService.getCart(userId, provinceId);

		if (CollectionUtil.isNotEmptyOfCollection(filterList)) {

			vo.setCartIdsValid(isChildOfList(filterList,
					cartDTO.getSkuidOfValid()));
			paramList = intersect(paramList, filterList);
			skulistOfValid = intersectCartItemDTO(skulistOfValid, filterList);
		}

		List<ProductSKUBackendVO> productSKUBackendVOs = productFacade.getProductSKUVO(paramList);
		List<POSkuDTO> skuDTOList = poProductService
				.getSkuDTOListBySkuId(paramList);
		List<Long> poIdList = new ArrayList<Long>();
		for(POSkuDTO p: skuDTOList){
			poIdList.add(p.getPoId());
		}

		final List<Long> poIdList2 = poIdList;
		POListDTO batchGetScheduleListByIdList = scheduleService
		 .batchGetScheduleListByIdList(poIdList);

		Future<POListDTO> asyncCall = RpcContext.getContext().asyncCall(
				new Callable<POListDTO>() {
					@Override
					public POListDTO call() throws Exception {
						// TODO Auto-generated method stub
						return scheduleService
								.batchGetScheduleListByIdList(poIdList2);
					}

				});
		// 调用阮洪勇接口，返回有活动信息的VO
		FavorCaculateParamDTO paramDTO = new FavorCaculateParamDTO();
		paramDTO.setUserId(userId);
		paramDTO.setProvince(provinceId);
		paramDTO.setUserCouponId(userCouponId);
		paramDTO.setCaculateCoupon(caculateCoupon);
		paramDTO.setPlatformType(platformType);
		Map<Long, List<PromotionSkuItemDTO>> skuPoListMap = new HashMap<Long, List<PromotionSkuItemDTO>>();

		vo.setParamDTO(paramDTO);

		for (int i = 0; i < skuDTOList.size(); i++) {
			POSkuDTO poSkuDTO = skuDTOList.get(i);
			if (skuPoListMap.containsKey(poSkuDTO.getPoId())) {
				List<PromotionSkuItemDTO> list1 = skuPoListMap.get(poSkuDTO
						.getPoId());
				PromotionSkuItemDTO e = new PromotionSkuItemDTO();
				e.setOriRetailPrice(poSkuDTO.getSalePrice());
				e.setRetailPrice(poSkuDTO.getSalePrice());
				e.setCartPrice(poSkuDTO.getSalePrice());
				e.setSkuId(poSkuDTO.getId());
				e.setUserId(userId);
				e.setCount(getCartItemDTOcount(skulistOfValid, poSkuDTO.getId()));
				list1.add(e);
				skuPoListMap.put(poSkuDTO.getPoId(), list1);
			} else {
				List<PromotionSkuItemDTO> list2 = new ArrayList<PromotionSkuItemDTO>();
				PromotionSkuItemDTO e = new PromotionSkuItemDTO();
				e.setOriRetailPrice(poSkuDTO.getSalePrice());
				e.setRetailPrice(poSkuDTO.getSalePrice());
				e.setCartPrice(poSkuDTO.getSalePrice());
				e.setSkuId(poSkuDTO.getId());
				e.setUserId(userId);
				e.setCount(getCartItemDTOcount(skulistOfValid, poSkuDTO.getId()));
				list2.add(e);
				skuPoListMap.put(poSkuDTO.getPoId(), list2);
			}
		}
		FavorCaculateResultDTO caculateActivation = calCenterFacade
				.caculateActivation(skuPoListMap, paramDTO);

		vo.setFavorCaculateResultDTO(caculateActivation);
		try {
			vo.setBatchGetScheduleListByIdList(asyncCall.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vo.setSkuDTOList(skuDTOList);
		vo.setCartDTO(cartDTO);
		return vo;
	}

	private int removeInvalidSku(final long userId, final int provinceId,
			final CartDTO cartDTO) {
		List<Long> expirePoIdList = new ArrayList<Long>();
		List<CartItemDTO> expireskuList = new ArrayList<CartItemDTO>();
		List<CartItemDTO> soldOutskuList = new ArrayList<CartItemDTO>();
		List<Long> expireskuIdList = new ArrayList<Long>();
		List<Long> soldOutskuIdList = new ArrayList<Long>();

		Future<Map<Long, Integer>> inventoryCountValidFuture = RpcContext
				.getContext().asyncCall(new Callable<Map<Long, Integer>>() {
					@Override
					public Map<Long, Integer> call() throws Exception {
						// TODO Auto-generated method stub
						return cartService.getInventoryCount(cartDTO
								.getSkuidOfValid());
					}

				});
		Future<Map<Long, Integer>> inventoryCountOvertimeFuture = RpcContext
				.getContext().asyncCall(new Callable<Map<Long, Integer>>() {
					@Override
					public Map<Long, Integer> call() throws Exception {
						// TODO Auto-generated method stub
						return cartService.getInventoryCount(cartDTO
								.getOverTimeIdOfValid());
					}

				});

		List<POSkuDTO> skuDTOList = poProductService
				.getSkuDTOListBySkuId(cartDTO.getSkuidOfValid());
		List<Long> poIdList = new ArrayList<Long>();
		for (POSkuDTO p : skuDTOList) {
			poIdList.add(p.getPoId());
		}
		POListDTO batchGetScheduleListByIdList = scheduleService
				.batchGetScheduleListByIdList(poIdList);
		for (PODTO po : batchGetScheduleListByIdList.getPoList()) {
			if (!po.isValid())
				expirePoIdList.add(po.getScheduleDTO().getSchedule().getId());
		}
		// 找出PO过期的sku
		for (POSkuDTO sku : skuDTOList) {
			if (expirePoIdList.contains(sku.getPoId()) == true) {
				CartItemDTO dto = new CartItemDTO();
				dto.setSkuid(sku.getId());
				dto.setCount(CartItemDTO.TYPE_POINVALID);
				expireskuList.add(dto);
				expireskuIdList.add(sku.getId());
			}
		}

		try {
			Map<Long, Integer> inventoryCountValid = inventoryCountValidFuture
					.get();
			for (Long id : cartDTO.getSkuidOfValid()) {
				if (inventoryCountValid.containsKey(id)
						&& inventoryCountValid.get(id) == 0
						&& !cartService
								.isCartContainSku(userId, provinceId, id)) {
					CartItemDTO dto = new CartItemDTO();
					dto.setSkuid(id);
					dto.setCount(CartItemDTO.TYPE_SOLDOUT);
					soldOutskuList.add(dto);
					soldOutskuIdList.add(id);
				}

			}

			Map<Long, Integer> inventoryCountOvertime = inventoryCountOvertimeFuture
					.get();
			for (Long id : cartDTO.getOverTimeIdOfValid()) {
				if (inventoryCountOvertime.containsKey(id)
						&& inventoryCountOvertime.get(id) == 0
						&& !cartService
								.isCartContainSku(userId, provinceId, id)) {
					CartItemDTO dto = new CartItemDTO();
					dto.setSkuid(id);
					dto.setCount(CartItemDTO.TYPE_SOLDOUT);
					soldOutskuList.add(dto);
					soldOutskuIdList.add(id);
				}

			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final List<Long> expirePoIdList2 = expirePoIdList;
		final List<CartItemDTO> expireskuList2 = expireskuList;
		final List<CartItemDTO> soldOutskuList2 = soldOutskuList;
		final List<Long> expireskuIdList2 = expireskuIdList;
		final List<Long> soldOutskuIdList2 = soldOutskuIdList;

		if (expireskuIdList.size() > 0) {
			RpcContext.getContext().asyncCall(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return cartService.deleteCartItems(userId, provinceId,
							expireskuIdList2);
				}

			});
		}

		if (soldOutskuIdList.size() > 0)
			RpcContext.getContext().asyncCall(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return cartService.deleteCartItemsWithoutAdjustInventory(
							userId, provinceId, soldOutskuIdList2);
				}

			});

		for (CartItemDTO overdto : soldOutskuList) {
			if (overdto.getCount() == CartItemDTO.TYPE_SOLDOUT)
				cartService.removeOverTimeCartItem(userId, provinceId,
						overdto.getSkuid());
		}

		if (cartDTO.getSkuidOfValid().size() > 0)
			RpcContext.getContext().asyncCall(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return cartService.removeInvalidCartItems(userId,
							provinceId, cartDTO.getSkuidOfValid());
				}

			});

		if (expireskuList.size() > 0)
			RpcContext.getContext().asyncCall(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return cartService.addInvalidCartItems(userId, provinceId,
							expireskuList2);
				}

			});

		if (soldOutskuList.size() > 0)
			RpcContext.getContext().asyncCall(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return cartService.addInvalidCartItems(userId, provinceId,
							soldOutskuList2);
				}

			});

		if (expireskuList.size() > 0)
			return ErrorCode.CART_PO_INVALID.getIntValue();
		else
			return 0;

	}

	public CartInnervVO getFavorCaculateResultDTOBySelected(long userId,
			int provinceId, List<Long> filterList, long userCouponId,
			boolean caculateCoupon, PlatformType platformType) {

//		CartDTO cartDTO = cartService.getCart(userId, provinceId);//contain addCartItem
	    //int ret = removeInvalidSku(userId, provinceId, cartDTO);
		//cartDTO = cartService.getCart(userId, provinceId);
//		List<Long> paramList = cartDTO.getSkuidOfValid();
//		List<CartItemDTO> skulistOfValid = cartDTO.getSkuOfValid();
		CartInnervVO vo = buildCartInnverVO(userId, provinceId, filterList,
				userCouponId, caculateCoupon, platformType);
	//	vo.setCartDTO(cartDTO);
	//	vo.setRetcode(ret);
		return vo;
	}

	private int getCartItemDTOcount(List<CartItemDTO> list, long skuId) {
		for (CartItemDTO dto : list) {
			if (dto.getSkuid() == skuId)
				return dto.getCount();
		}
		return 0;
	}

	private List<CartActivationVO> convertToCartActivationVO(
			List<PromotionCartDTO> activations, List<PODTO> poDTOList,
			List<POSkuDTO> skuDTOlist, String type) {
		List<CartActivationVO> list = new ArrayList<CartActivationVO>();
		if (activations == null)
			return list;
		for (PromotionCartDTO pcd : activations) {
			CartActivationVO cartActivationVO = new CartActivationVO();
			cartActivationVO.setPoList(convertToCartPOVO(pcd.getPoSkuMap(),
					poDTOList, skuDTOlist, type));
			cartActivationVO.setId(pcd.getPromotionDTO().getId());
			cartActivationVO.setTagList(MobileVOConvertUtil
					.convertToActionTagVOList(pcd.getPromotionDTO()));
			cartActivationVO.setActivatonName(pcd.getPromotionDTO().getName());
			cartActivationVO.setEffectDesc(pcd.getStepTip());
			cartActivationVO.setDiscountUrl(pcd.getCoudanUrl());
			if (type == CartSkuItemVO.STATE_OVERTIME) {
				cartActivationVO.setEffectDesc(null);
				cartActivationVO.setDiscountUrl(null);
			}
			list.add(cartActivationVO);
		}

		return list;
	}

	private List<CartPOVO> convertToCartPOVO(
			Map<Long, List<PromotionSkuItemDTO>> map, List<PODTO> poDTOList,
			List<POSkuDTO> skuDTOlist, String type) {
		List<CartPOVO> list = new ArrayList<CartPOVO>();
		if (map == null)
			return list;
		for (Map.Entry<Long, List<PromotionSkuItemDTO>> entry : map.entrySet()) {
			CartPOVO cartPOVO = new CartPOVO();
			cartPOVO.setSkulist(convertToCartSkuItemVO(entry.getValue(),
					skuDTOlist, type));
			cartPOVO.setPo(filterPODTOFromList(entry.getKey(), poDTOList)
					.getScheduleDTO().getSchedule());
			cartPOVO.setId(cartPOVO.getPoId());
			list.add(cartPOVO);
		}
		return list;
	}

	private PODTO filterPODTOFromList(Long id, List<PODTO> poDTOList) {
		for (PODTO p : poDTOList) {
			if (p.getScheduleDTO().getSchedule().getId() == id) {
				return p;
			}
		}
		return null;

	}

	private List<CartSkuItemVO> convertToCartSkuItemVO(
			List<PromotionSkuItemDTO> list, List<POSkuDTO> skuDTOlist,
			String type) {
		List<CartSkuItemVO> newlist = new ArrayList<CartSkuItemVO>();
		Map<Long, Future<BrandDTO>> map = new HashMap<Long, Future<BrandDTO>>();
		for (POSkuDTO sku : skuDTOlist) {
			Future<BrandDTO> brandDTO = brandQueryFacade
					.getBrandByBrandIdAsync(sku.getBrandId());
			map.put(sku.getId(), brandDTO);
		}
		for (PromotionSkuItemDTO from : list) {
			CartSkuItemVO newValue = new CartSkuItemVO();
			newValue.setCartPrice(from.getCartPrice());
			newValue.setId(from.getSkuId());
			newValue.setCount(from.getCount());
			newValue.setOriginalPrice(from.getOriRetailPrice());
			newValue.setRetailPrice(from.getRetailPrice());
			newValue.setPromotionDiscountAmount(from
					.getPromotionDiscountAmount());

			if (type == CartSkuItemVO.STATE_OVERTIME) {
				newValue.setRetailPrice(from.getOriRetailPrice());
				newValue.setPromotionDiscountAmount(new BigDecimal(0));

			}

			for (POSkuDTO sku : skuDTOlist) {
				if (sku.getId() == newValue.getId()) {
					newValue.setColor(sku.getColorName());
					newValue.setSize(sku.getSize());
					newValue.setUrl(sku.getProductLinkUrl());
					newValue.setProductId(sku.getProductId());
					newValue.setThumb(sku.getThumb());
					newValue.setMarketPrice(sku.getMarketPrice());
					newValue.setName(sku.getProductName());
					newValue.setStoreName(sku.getStoreName());

					try {
						BrandDTO brandDTO = map.get(sku.getId()).get();
						String brandNameEn = brandDTO.getBrand()
								.getBrandNameEn(), brandNameZh = brandDTO
								.getBrand().getBrandNameZh();
						brandNameEn = StringUtils.isBlank(brandNameEn) ? ""
								: brandNameEn.trim();
						brandNameZh = StringUtils.isBlank(brandNameZh) ? ""
								: brandNameZh.trim();
						newValue.setBrandName(brandNameEn + " " + brandNameZh);
						newValue.setBrandLinkUrl("/mainbrand/story?id="
								+ sku.getBrandId());
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			newlist.add(newValue);
		}
		return newlist;
	}

	@Override
	public int addItemToCart(long userId, int provinceId, long skuId,
			int deltaCount, List<Long> selectedIds) {
		int ret = 0;
		if (deltaCount > 0) {
			ret = cartService
					.addCartItem(userId, provinceId, skuId, deltaCount);
		} else if (deltaCount < 0)
			ret = cartService.decreaseCartItem(userId, provinceId, skuId,
					deltaCount);
		else
			ret = CartService.CART_ERROR;

		return ret;
	}

	public boolean addInventoryCount(List<CartItemDTO> list) {
		return cartService.addInventoryCount(list);
	}

	@Override
	public int addItemToCartAndUpdateTime(long userid, int provinceId,
			long skuId, int deltaCount) {
		// TODO Auto-generated method stub
		int ret = addCartItem(userid, provinceId, skuId, deltaCount);
		if (ret >= 0) {// 正常值的时候，更新购物车update时间，0是删除了最后一条记录
			cartService.removeOverTimeCartItem(userid, provinceId, skuId);
			cartService.deleteDeletedReocordFromCart(userid, provinceId);
			long updatetime = cartService.resetTime(userid, provinceId);
			Date update = new Date();
			update.setTime(updatetime);
			cartService.addCartUpdateTimeToCache(userid, provinceId, update);
			cartDeleteCacheService.addCartItemToDeleteCache(null, userid,
					provinceId, update);
		}

		return ret;

	}

	@Override
	public int rebuyAndReturnCode(long userid, int provinceId, long skuId,
			int deltaCount) {
		// TODO Auto-generated method stub
		int ret = 0;

		long lefttime = cartService.getCartLeftTime(userid, provinceId);
		// 当重新购买的时候，已经没有时间了，就重置吧

		if (deltaCount > 0) {
			ret = cartService
					.addCartItem(userid, provinceId, skuId, deltaCount);
		} else if (deltaCount < 0)
			ret = cartService.decreaseCartItem(userid, provinceId, skuId,
					deltaCount);
		else
			ret = CartService.CART_ERROR;

		if (lefttime <= 0 && ret > 0) {
			cartService.resetTime(userid, provinceId);
		}

		if (ret >= 0) {// 正常值的时候，更新购物车update时间，0是删除了最后一条记录
			cartService.removeOverTimeCartItem(userid, provinceId, skuId);
			cartService.deleteDeletedReocordFromCart(userid, provinceId);
		}

		return ret;

	}

	@Override
	public boolean setInventoryCount(long skuId, int count) {
		// TODO Auto-generated method stub
		return cartService.setInventoryCount(skuId, count);
	}

	@Override
	public boolean deleteCart(long userid, int provinceId) {
		return cartService.deleteCart(userid, provinceId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.web.facade.MobileCartFacade#deleteCartForOrder(long,
	 *      int, java.util.List)
	 */
	public boolean deleteCartForOrder(long userId, int provinceId,
			String cartIds) {
		List<Long> selectedIds = new ArrayList<>();
		if (StringUtils.isNotBlank(cartIds)) {
			for (String cartId : cartIds.split(",")) {
				if (NumberUtils.isNumber(cartId))
					selectedIds.add(Long.valueOf(cartId));
			}
		}
		if (CollectionUtil.isEmptyOfCollection(selectedIds))
			return false;
		return cartService.deleteCartForOrder(userId, provinceId, selectedIds);
	}

	@Override
	public boolean deleteCartItem(long userId, int provinceId, List<Long> skuIds) {
		return cartService
				.deleteCartItemsWithRecord(userId, provinceId, skuIds);
	}

	private List<Long> intersect(List<Long> listA, List<Long> filterList) {
		List<Long> rtnList = new ArrayList<Long>();
		for (Long v : listA) {
			if (filterList.contains(v)) {
				rtnList.add(v);
			}
		}
		return rtnList;
	}

	private boolean isChildOfList(List<Long> listA, List<Long> listB) {
		if (listA == null)
			return true;
		if (listB == null)
			return false;
		boolean isChild = true;
		for (Long a : listA) {
			if (listB.contains(a) == false) {
				isChild = false;
				break;
			}

		}
		return isChild;
	}

	private List<CartItemDTO> intersectCartItemDTO(List<CartItemDTO> listA,
			List<Long> filterList) {
		List<CartItemDTO> rtnList = new ArrayList<CartItemDTO>();
		for (CartItemDTO p : listA) {
			if (filterList.contains(p.getSkuid())) {
				rtnList.add(p);
			}
		}
		return rtnList;
	}

	@Override
	public Map<Long, Integer> getInventoryCount(List<Long> ids) {
		// TODO Auto-generated method stub
		return cartService.getInventoryCount(ids);
	}

	@Override
	public boolean addUserToRemindWhenStorage(long userId, int provinceId,
			long skuId) {
		return cartService
				.addUserToRemindWhenStorage(userId, provinceId, skuId);
	}

	@Override
	public boolean userExistInRemindStorage(long userId, int provinceId,
			long skuId) {
		return cartService.userExistInRemindStorage(userId, provinceId, skuId);
	}

	@Override
	public long resetTime(long userId, int provinceId) {
		// TODO Auto-generated method stub
		return cartService.resetTime(userId, provinceId);
	}

	@Override
	public long getCartLeftTime(long userId, int provinceId) {
		return cartService.getCartLeftTime(userId, provinceId);
	}

	@Override
	public int getCartValidCount(long userId, int provinceId) {
		return cartService.getCartValidCount(userId, provinceId);
	}

	@Override
	public CartVO getCartMini(long userid, int provinceId,
			List<Long> selectedIds) {
		// TODO Auto-generated method stub
		CartVO c = getCart(userid, provinceId, selectedIds,PlatformType.PC);
		c.setInvalidatedCount(c.getInvalidCartItemList() == null ? 0 : c
				.getInvalidCartItemList().size() + c.getOvertimeCount());
		c.setInvalidCartItemList(null);
		List<CartActivationVO> newActList = new ArrayList<CartActivationVO>();
		List<CartPOVO> newPoList = new ArrayList<CartPOVO>();
		List<CartSkuItemVO> newSkuList = new ArrayList<CartSkuItemVO>();

		for (CartActivationVO avo : c.getActivations()) {
			newPoList = new ArrayList<CartPOVO>();
			for (CartPOVO po : avo.getPoList()) {
				newSkuList = new ArrayList<CartSkuItemVO>();
				for (CartSkuItemVO sku : po.getSkulist()) {
					if (sku.getStatus() == sku.STATE_NORMAL)
						newSkuList.add(sku);
				}
				po.setSkulist(newSkuList);

				if (po.getSkulist().size() > 0)
					newPoList.add(po);
			}
			avo.setPoList(newPoList);

			if (newPoList.size() > 0)
				newActList.add(avo);
		}
		c.setActivations(newActList);

		newPoList = new ArrayList<CartPOVO>();
		for (CartPOVO po : c.getPoList()) {
			newSkuList = new ArrayList<CartSkuItemVO>();
			for (CartSkuItemVO sku : po.getSkulist()) {
				if (sku.getStatus() == sku.STATE_NORMAL)
					newSkuList.add(sku);
			}
			po.setSkulist(newSkuList);

			if (po.getSkulist().size() > 0)
				newPoList.add(po);
		}
		c.setPoList(newPoList);

		return c;
	}

	@Override
	public InputParam recoverCart(long userid, int provinceId) {
		// TODO Auto-generated method stub
		InputParam ret = new InputParam();
		ret.selectedIds = cartService.getCart(userid, provinceId)
				.getDeletedIdOfValid();

		ret.diff = cartService.recoverCart(userid, provinceId);
		return ret;
	}

	@Override
	public boolean removeUserRemind(long userId, int provinceId, long skuId) {
		return cartService.removeUserRemind(userId, provinceId, skuId);
	}

	@Override
	public boolean addCartUpdateTimeToCache(long userId, int areaId,
			Date updateTime) {
		return cartService.addCartUpdateTimeToCache(userId, areaId, updateTime);
	}

	@Override
	public int addCartItem(long userId, int provinceId, long skuId,
			int deltaCount) {
		int ret = 0;
		if (deltaCount > 0) {
			ret = cartService
					.addCartItem(userId, provinceId, skuId, deltaCount);
		} else if (deltaCount < 0)
			ret = cartService.decreaseCartItem(userId, provinceId, skuId,
					deltaCount);
		else
			ret = CartService.CART_ERROR;

		return ret;
	}

	@Override
	public CartVO getCartAfterDelete(long userid, int provinceId,
			List<Long> selectedIds,PlatformType platformType) {
		// TODO Auto-generated method stub
		return getCartVOInternal(userid, provinceId, selectedIds, null,
				MobileCartFacade.OPERATION_DELETE,platformType);
	}

	@Override
	public boolean deleteCartItems(long userId, int provinceId, List<Long> skuIds) {
		return 	cartService.deleteCartItems(userId, provinceId, skuIds);
	}

	/**
	 * 取进货单购物信息
	 */
	@Override
	public CartVO getCartInfo(long userId, int siteId, List<Long> selectedIds,
			PlatformType platformType,boolean isGetMiniCart) {
		CartDTO cartDTO = cartService.getCart(userId, siteId);
		if(CollectionUtil.isEmptyOfList(cartDTO.getCartItemList())){
			return new CartVO();
		}
		List<CartItemDTO> cartItemDTOs = cartDTO.getSkuOfValid();
		List<Long>skuids = cartDTO.getSkuidOfValid();
		if(CollectionUtil.isNotEmptyOfList(selectedIds)){
			 cartItemDTOs = intersectCartItemDTO(cartItemDTOs,selectedIds);
			 skuids = selectedIds;
		}	
		
		
		List<ProductSKUBackendVO>skuDTOList = productFacade.getProductSKUVO(skuids);
		if(CollectionUtil.isEmptyOfList(skuDTOList)){
			return new CartVO();
		}
		Map<String, List<ProductPriceDTO>> map = productService.getProductPriceDTOByProductIds(skuids);
		Map<Long, Integer> inventoryMap = null;
		CartVO cartVO = new CartVO();
		if(!isGetMiniCart){
			Map<String, String> poUserFavMap = mainsiteItemFacade.getPoProductFavListByUserIdOrPoIds(userId, skuids);
			cartVO.setPoUsrFavMap(poUserFavMap);
			inventoryMap = commonFacade.getOrderSkuStock(skuids);
			//inventoryMap = cartService.getInventoryCount(skuids);//性能有问题时可以切换从nkv中取
		}
		cartVO.setProductMap(map);
	    cartVO.setCartStoreList(convertToCartStoreVO(skuDTOList,cartDTO.getValidCartItemMap(),map,inventoryMap));
	    cartVO.setCartInfoVO(buildCartInfoVO(cartVO.getCartStoreList()));
		return cartVO;
	}
	
	/**
	 * 
	 * @param skuDTOlist
	 * @param cartItemMap
	 * @param productPriceMap
	 * @return
	 */
	private List<CartStoreVO> convertToCartStoreVO(List<ProductSKUBackendVO> skulist,Map<Long, CartItemDTO>cartItemMap,Map<String, List<ProductPriceDTO>> productPriceMap,Map<Long, Integer> inventoryMap) {
		Map<Long, Future<BrandDTO>> map = new HashMap<Long, Future<BrandDTO>>();
		for (ProductSKUBackendVO sku : skulist) {
			Future<BrandDTO> brandDTO = brandQueryFacade
					.getBrandByBrandIdAsync(sku.getBrandId());
			map.put(sku.getSkuId(), brandDTO);
		}
		Map<String, List<CartSkuItemVO>>storeNameMap = new HashMap<String, List<CartSkuItemVO>>();
		List<CartSkuItemVO> newlist = null;
		for (ProductSKUBackendVO sku : skulist) {
			if(cartItemMap.get(sku.getSkuId())==null){
				continue;
			}
			CartSkuItemVO cartSkuItemVO = new CartSkuItemVO();
			if(sku.getProdStatus()!=ProductStatusType.ONLINE.getIntValue()){
				//continue;
				cartSkuItemVO.setOffline(true);
			}
		    cartSkuItemVO.setId(sku.getSkuId());
		    cartSkuItemVO.setCount(cartItemMap.get(sku.getSkuId()).getCount());
		    cartSkuItemVO.setUnit(sku.getProdUnit());
		    cartSkuItemVO.setSpeciList(sku.getSpeciList());
			cartSkuItemVO.setUrl("/detail?skuId=" + sku.getSkuId());
			cartSkuItemVO.setProductId(sku.getSkuId());
			cartSkuItemVO.setThumb(sku.getShowPicPath());
			cartSkuItemVO.setMarketPrice(sku.getSalePrice());
			cartSkuItemVO.setName(sku.getProductName());
			cartSkuItemVO.setStoreName(sku.getStoreName());
			cartSkuItemVO.setStoreUrl("/store/"+sku.getSupplierId()+"/");
			cartSkuItemVO.setStoreBatchCash(sku.getBatchCash());
			cartSkuItemVO.setStoreId(sku.getSupplierId());
			if(inventoryMap!=null){
				cartSkuItemVO.setInventroyCount(inventoryMap.get(sku.getSkuId())==null?0:inventoryMap.get(sku.getSkuId()));
				////性能有问题时可以切换从nkv中取
//				if(inventoryMap.get(sku.getSkuId())==null){
//					SkuOrderStockDTO skuOrderStockDTO = skuOrderStockService.getSkuOrderStockDTOBySkuId(sku.getSkuId());
//					cartSkuItemVO.setInventroyCount(skuOrderStockDTO == null? 0:skuOrderStockDTO.getStockCount());
//				}else{
//					cartSkuItemVO.setInventroyCount(inventoryMap.get(sku.getSkuId()));
//				}
			}
			BigDecimal retailPrice =  PriceCalculateUtil.getOriRetailPrice(cartSkuItemVO.getCount(), productPriceMap.get(String.valueOf(cartSkuItemVO.getProductId())));
			cartSkuItemVO.setCartPrice(retailPrice);
			cartSkuItemVO.setRetailPrice(retailPrice);
			try {
				BrandDTO brandDTO = map.get(sku.getSkuId()).get();
				if(brandDTO!=null&&brandDTO.getBrand()!=null){
					String brandNameEn = brandDTO.getBrand()
							.getBrandNameEn(), brandNameZh = brandDTO
							.getBrand().getBrandNameZh();
					brandNameEn = StringUtils.isBlank(brandNameEn) ? ""
							: brandNameEn.trim();
					brandNameZh = StringUtils.isBlank(brandNameZh) ? ""
							: brandNameZh.trim();
					cartSkuItemVO.setBrandName(brandNameEn + " " + brandNameZh);
					cartSkuItemVO.setBrandLinkUrl("/mainbrand/story?id="
							+ sku.getBrandId());
				}
			} catch (InterruptedException | ExecutionException e) {
				logger.error("bulid brand name error",e);
				e.printStackTrace();
			}
			
			if(storeNameMap.get(sku.getStoreName())==null){
				newlist = new ArrayList<CartSkuItemVO>();
				newlist.add(cartSkuItemVO);
				storeNameMap.put(sku.getStoreName(), newlist);
			}else{
				storeNameMap.get(sku.getStoreName()).add(cartSkuItemVO);
			}
		
		}
		List<CartStoreVO> cartStoreVOs = new ArrayList<CartStoreVO>();
		for(Map.Entry<String, List<CartSkuItemVO>> entry:storeNameMap.entrySet()){
			BigDecimal totalPrice = BigDecimal.ZERO;
			CartStoreVO cartStoreVO = new CartStoreVO();
			cartStoreVO.setStoreName(entry.getKey());
			cartStoreVO.setSkulist(entry.getValue());
			for(CartSkuItemVO skuItemVO:entry.getValue()){
				if(skuItemVO.getCartPrice().equals(BigDecimal.ZERO)){
					totalPrice = totalPrice.add(skuItemVO.getMarketPrice().multiply(new BigDecimal(skuItemVO.getCount()))) ;
				}else{
					totalPrice = totalPrice.add(skuItemVO.getCartPrice().multiply(new BigDecimal(skuItemVO.getCount()))) ;
				}
				cartStoreVO.setStoreUrl(skuItemVO.getStoreUrl());
				cartStoreVO.setStoreBatchCash(skuItemVO.getStoreBatchCash());
				cartStoreVO.setStoreId(skuItemVO.getStoreId());
			}
			cartStoreVO.setTotalPrice(totalPrice);
			cartStoreVO.setTotalCount(entry.getValue().size());
			cartStoreVOs.add(cartStoreVO);
		}
		return cartStoreVOs;
	}
	
	/**
	 * 构建购物车信息，主要购物总价
	 * @param cartStoreVOs
	 * @return
	 */
	private CartInfoVO buildCartInfoVO(List<CartStoreVO> cartStoreVOs){
		if(CollectionUtil.isEmptyOfList(cartStoreVOs)){
			return null;
		}
		BigDecimal totalPrice = BigDecimal.ZERO;
		int productNum = 0;
		for(CartStoreVO cartStoreVO:cartStoreVOs){
			totalPrice = totalPrice.add(cartStoreVO.getTotalPrice()) ;
			productNum+=cartStoreVO.getTotalCount();
		}
		CartInfoVO cartInfoVO = new CartInfoVO();
		cartInfoVO.setTotalPrice(totalPrice);
		cartInfoVO.setStoreNum(cartStoreVOs.size());
		cartInfoVO.setProductNum(productNum);
		return cartInfoVO;
	}
	

	@Override
	public int updateCartAmount(String type, long userId, int provinceId,List<CartItemDTO> list) {
		if(CollectionUtil.isEmptyOfList(list)){
			return -4;
		}
		Function<CartItemDTO, Long> idFunction = new Function<CartItemDTO,Long>() {
			@Override
			public Long apply(CartItemDTO arg0) {
				return arg0.getSkuid();
			}
		};
		List<Long>skuidList = Lists.transform(list, idFunction);
		Map<Long, Boolean> skuStatusMap = itemProductService.getProductStatusIsOnline(skuidList);
		if(skuStatusMap == null || skuStatusMap.size() != skuidList.size() || skuStatusMap.containsValue(false)){
			return -5;
		}
		//if (isCartFull(userId, provinceId) && !isCartContainSku(userId, provinceId, skuId))
		//判断购物车是否超过容量(50)
		List<Long> incontainSkuIds = new ArrayList<>();
		for(Long skuid : skuidList){
			if(!cartService.isCartContainSku(userId, provinceId, skuid)){
				incontainSkuIds.add(skuid);
			}
		}
		
		CartDTO cartDTO = cartService.getCart(userId,provinceId);
		int cartConut = cartDTO.getCartItemList().size();
		if(cartConut+incontainSkuIds.size() > NkvConstant.MAX_SKU_COUNT){
			return -6;
		}
		return cartService.addCartItems(type, userId, provinceId, list)?1:-1;
	}

	@Override
	public boolean decreaseInventory(List<CartItemDTO> list) {
		if(CollectionUtil.isEmptyOfList(list)){
			return false;
		}
		return cartService.decreaseInventory(list)>0;
	}

	@Override
	public CartDTO getCart(long userId, int areaId) {
		return cartService.getCart(userId, areaId);
	}

	@Override
	public int addItemToCart(long userid, int provinceId, long skuId,
			int deltaCount) {
		if(!itemProductService.isProductStatusOnline(skuId)){
			return -5;
		}
		int result = 0,count = deltaCount;
		CartDTO cartDTO = cartService.getCart(userid, provinceId);
		if(cartDTO!=null&&CollectionUtil.isNotEmptyOfList(cartDTO.getInvalidCartItemList())){
			for(CartItemDTO cartItemDTO:cartDTO.getInvalidCartItemList()){
				if(cartItemDTO.getSkuid()==skuId){
					count = cartItemDTO.getCount()+deltaCount;
					break;
				}
			}
		}
		result = cartService.addCartItem(userid, provinceId, skuId, count);
		return result;
	}

	@Override
	public boolean isInventoryEnough(long userId, int areaId,String cartIds) {
		Map<Long,CartItemDTO> cartMap = null;
		List<Long>cartIdList = new ArrayList<Long>();
		CartDTO cartDTO = cartService.getCart(userId, areaId);
		if(cartDTO==null||cartDTO.getValidCartItemMap()==null){
			return false;
		}
		cartMap = cartDTO.getValidCartItemMap();
		if(StringUtils.isEmpty(cartIds)){
			cartIdList = new ArrayList<Long>(cartMap.keySet());
		}else{
			for(String cartId:cartIds.split(",")){
				cartIdList.add(Long.parseLong(cartId));
			}
		}
		Map<Long, Integer> itemMap = cartService.getInventoryCount(cartIdList);
		for(Entry<Long, Integer> entry:itemMap.entrySet()){
			if(cartMap.get(entry.getKey())!=null&&entry.getValue()<cartMap.get(entry.getKey()).getCount()){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean deleteCartItemsExceptBySkuIds(long userId, int areaId,
			List<Long> skuIds) {
		if(CollectionUtil.isEmptyOfList(skuIds)){
			cartService.deleteCart(userId, areaId);
		}
		CartDTO cartDTO = cartService.getCart(userId, areaId);
		if(cartDTO!=null&&CollectionUtil.isNotEmptyOfList(cartDTO.getCartItemList())){
			List<Long>deleteSkuIdList = new ArrayList<Long>();
			for(CartItemDTO cartItemDTO:cartDTO.getCartItemList()){
				if(!skuIds.contains(cartItemDTO.getSkuid())){
					deleteSkuIdList.add(cartItemDTO.getSkuid());
				}
			}
			cartService.deleteCartItems(userId, areaId, deleteSkuIdList );	
		}
		return true;
	}
	
	@Override
	public BaseJsonVO buyAgain(long userId, int areaId, InputParam obj) {
		BaseJsonVO retJsonVO = new BaseJsonVO();
		retJsonVO.setCode(ResponseCode.RES_ERROR);
		if (CollectionUtil.isEmptyOfList(obj.getCartItemDTOs())) {
			return retJsonVO;
		}
		Function<CartItemDTO, Long> idFunction = new Function<CartItemDTO, Long>() {
			@Override
			public Long apply(CartItemDTO arg0) {
				return arg0.getSkuid();
			}
		};
		List<Long> skuidList = Lists.transform(obj.getCartItemDTOs(), idFunction);
		Map<Long, Boolean> skuStatusMap = itemProductService.getProductStatusIsOnline(skuidList);
		
		StringBuilder sb = new StringBuilder();
		long skuId = 0l;
		OrderSkuDTO orderSkuDTO = null;
		for (CartItemDTO cartItemDTO : obj.getCartItemDTOs()) {
			skuId = cartItemDTO.getSkuid();
			if (skuStatusMap.get(skuId) == null || !skuStatusMap.get(skuId)) {
				orderSkuDTO = orderService.getOrderSku(userId, obj.getOrderId(), skuId);
				if (orderSkuDTO == null) {
					continue;
				}
				sb.append(orderSkuDTO.getSkuSPDTO().getProductName() + " 已经下架或者删除 ! \n");
			}
			// 商品删除时不加
			if (skuStatusMap.get(skuId) != null) {
				int ret = cartService.addCartItem(userId, areaId, cartItemDTO.getSkuid(), cartItemDTO.getCount());
				if (ret == CartService.CART_OVER_ITMESCOUNT) {
					sb.append("进货单最多加50个商品! \n");
					break;
				}
			}
		}
		if (sb.length() > 0) {
			retJsonVO.setMessage(sb.toString());
		} else {
			retJsonVO.setCode(ResponseCode.RES_SUCCESS);
			retJsonVO.setMessage("再次购买成功");
		}
		return retJsonVO;
	}

}
