package com.xyl.mmall.mobile.ios.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.itemcenter.dto.ProdPicDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.mobile.ios.facade.IosCartFacade;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.IosCart;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.IosCartItem;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.IosCartStore;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.MobileSKULimitVO;
import com.xyl.mmall.mobile.ios.facade.pageView.common.IosProcPrice;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;
import com.xyl.mmall.saleschedule.service.ProductSKULimitService;

/**
 * 购物车相关门面工程
 * 
 * @author dingjiang
 * 
 */
@Facade("iosCartFacade")
public class IosCartFacadeImpl implements IosCartFacade {

	private static Logger logger = LoggerFactory.getLogger(IosCartFacadeImpl.class);

	@Resource
	private CartService cartService;

	@Resource
	private ProductService productService;

	@Autowired
	private ItemProductService itemProductService;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private SkuOrderStockService skuOrderStockService;
	@Autowired
	private ProductSKULimitService productSKULimitService;

	/**
	 * 取进货单购物信息
	 */
	@Override
	public IosCart getCartInfo(long userId, int siteId) {
		IosCart iosCart = new IosCart();

		CartDTO cartDTO = cartService.getCart(userId, siteId);
		if (CollectionUtil.isEmptyOfList(cartDTO.getCartItemList())) {
			return iosCart;
		}
		List<Long> skuids = cartDTO.getSkuidOfValid();
		List<IosCartStore> iosCartStores = new ArrayList<>();

		List<ProductSKUDTO> productSKUDTOs = itemProductService.getProductSKUDTOByProdIds(skuids);

		if (CollectionUtil.isEmptyOfList(productSKUDTOs)) {
			return iosCart;
		}
		Map<String, List<ProductPriceDTO>> map = productService.getProductPriceDTOByProductIds(skuids);

		List<SkuOrderStockDTO> skuOrderStockDTOs = skuOrderStockService.getSkuOrderStockDTOListBySkuIds(skuids);

		Map<Long, ProductSKULimitConfigDTO> skuLimitMap = productSKULimitService.getProductSKULimitConfigDTOMap(userId,
				skuids);

		Map<Long, List<IosCartItem>> cartStoreMap = new LinkedHashMap<>();
		IosCartItem cartItem = null;
		List<IosCartItem> iosCartItems = null;
		Map<Long, CartItemDTO> cartItemDTOMap = cartDTO.getValidCartItemMap();
		// 取商家信息
		Set<Long> businessIdSet = new HashSet<Long>();
		for (ProductSKUDTO productSKUDTO : productSKUDTOs) {
			businessIdSet.add(productSKUDTO.getBusinessId());

			cartItem = new IosCartItem();
			cartItem.setSkuId(productSKUDTO.getId());
			cartItem.setName(productSKUDTO.getName());
			cartItem.setStatus(productSKUDTO.getStatus());
			List<ProductPriceDTO> listPrice = map.get(String.valueOf(productSKUDTO.getId()));
			// 设置限购信息
			ProductSKULimitConfigDTO configDTO = skuLimitMap.get(productSKUDTO.getId());
			if (configDTO != null) {
				MobileSKULimitVO skuLimitVO = new MobileSKULimitVO();
				skuLimitVO.setAllowBuyNum(configDTO.getAllowedNum());
				skuLimitVO.setStartTime(configDTO.getStartTime());
				skuLimitVO.setEndTime(configDTO.getEndTime());
				skuLimitVO.setLimitDescrp(configDTO.getNote());
				cartItem.setSkuLimitVO(skuLimitVO);
			}

			IosProcPrice iosProcPrice = null;
			List<IosProcPrice> iosProcPrices = new ArrayList<>();
			if (listPrice != null && !listPrice.isEmpty()) {
				for (ProductPriceDTO productPriceDTO : listPrice) {
					iosProcPrice = new IosProcPrice();
					iosProcPrice.setPriceId(productPriceDTO.getId());
					iosProcPrice.setMinNum(productPriceDTO.getMinNumber());
					iosProcPrice.setMaxNum(productPriceDTO.getMaxNumber());
					iosProcPrice.setPrice(productPriceDTO.getPrice());
					iosProcPrices.add(iosProcPrice);

				}
			}
			cartItem.setCount(cartItemDTOMap.get(productSKUDTO.getId()).getCount());
			cartItem.setPriceList(iosProcPrices);
			cartItem.setCreateTime(cartItemDTOMap.get(productSKUDTO.getId()).getCreateTime());
			cartItem.setUnit(productSKUDTO.getUnit());
			if (skuOrderStockDTOs != null && !skuOrderStockDTOs.isEmpty()) {
				for (SkuOrderStockDTO skuOrderStockDTO : skuOrderStockDTOs) {
					if (skuOrderStockDTO.getSkuId() == productSKUDTO.getId()) {
						cartItem.setStockCount(skuOrderStockDTO.getStockCount());
						break;
					}
				}

			}

			List<ProdPicDTO> prodPicDTOs = productSKUDTO.getPicList();
			if (prodPicDTOs != null && !prodPicDTOs.isEmpty()) {
				cartItem.setThumb(prodPicDTOs.get(0).getPath());
			}

			if (cartStoreMap.get(productSKUDTO.getBusinessId()) == null) {
				iosCartItems = new ArrayList<>();
				iosCartItems.add(cartItem);
				cartStoreMap.put(productSKUDTO.getBusinessId(), iosCartItems);
			} else {
				cartStoreMap.get(productSKUDTO.getBusinessId()).add(cartItem);
			}
		}
		for (Entry<Long, List<IosCartItem>> entry : cartStoreMap.entrySet()) {
			// 商品根据时间排序
			Collections.sort(entry.getValue(), new Comparator<IosCartItem>() {

				@Override
				public int compare(IosCartItem o1, IosCartItem o2) {
					if (o1.getCreateTime() > o2.getCreateTime()) {
						return -1;
					} else {
						return 1;
					}
				}

			});
		}

		List<BusinessDTO> businessDTOs = businessService.getBusinessDTOListByIdList(new ArrayList<Long>(businessIdSet));

		IosCartStore cartStore = null;
		for (BusinessDTO businessDTO : businessDTOs) {
			cartStore = new IosCartStore();
			cartStore.setStoreId(businessDTO.getId());
			cartStore.setStoreName(businessDTO.getStoreName());
			cartStore.setStoreBatchCash(businessDTO.getBatchCash());
			List<IosCartItem> iosCarts = cartStoreMap.get(businessDTO.getId());
			if (!CollectionUtils.isEmpty(iosCartItems)) {
				cartStore.setCartItems(iosCarts);
				iosCartStores.add(cartStore);
			}
		}
		if (!iosCartStores.isEmpty()) {
			Collections.sort(iosCartStores, new Comparator<IosCartStore>() {

				@Override
				public int compare(IosCartStore o1, IosCartStore o2) {
					// TODO Auto-generated method stub
					if (CollectionUtils.isEmpty(o1.getCartItems()) || CollectionUtils.isEmpty(o2.getCartItems())) {
						return 0;
					}
					if (o1.getCartItems().get(0).getCreateTime() > o2.getCartItems().get(0).getCreateTime()) {
						return -1;
					} else {
						return 1;
					}
				}

			});

			iosCart.setCartStores(iosCartStores);
		}

		return iosCart;
	}

}
