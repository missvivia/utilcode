package com.xyl.mmall.mobile.ios.facade.impl;

import javax.annotation.Resource;

import com.xyl.mmall.backend.vo.ProductSKULimitConfigVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ItemSPUService;
import com.xyl.mmall.mobile.ios.facade.MobilePrdFacade;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.MobileSKULimitVO;
import com.xyl.mmall.mobile.ios.facade.pageView.prodctDetail.SkuDetailVo;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.service.SkuOrderStockService;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ProductSKULimitService;

@Facade
public class MobilePrdFacadeImpl implements MobilePrdFacade {

	@Resource
	private ItemProductService itemProductService;
	@Resource
	private ItemSPUService itemSPUService;
	@Resource
	private SkuOrderStockService skuOrderStockService;
	@Resource
	private BrandService brandService;
	@Resource
	private ProductSKULimitService productSKULimitService;

	public SkuDetailVo getProductSKUVO(ProductSKUDTO skuDTO) {

		skuDTO = itemProductService.getProductSKUDTO(skuDTO, true);
		if (skuDTO == null) {
			return null;
		}
		// ItemSPUDTO itemSPUDTO = new ItemSPUDTO();
		// itemSPUDTO.setId(skuDTO.getSpuId());

		// ItemSPUDTO resultSpu = itemSPUService.getItemSPU(itemSPUDTO);
		// skuDTO.setName(resultSpu.getName());

		SkuOrderStockDTO skuOrderStockDTO = skuOrderStockService.getSkuOrderStockDTOBySkuId(skuDTO.getId());
		// 1表示限购
		ProductSKULimitConfigDTO limitConfigDTO = null;
		if (skuDTO.getIsLimited() == 1) {
			limitConfigDTO = productSKULimitService.getProductSKULimitConfigBySkuId(skuDTO.getId());
		}
		SkuDetailVo skuDetailVo = new SkuDetailVo(skuDTO);
		// 设置品牌信息
		setBrandInfo(skuDTO.getSpuId(), skuDetailVo);
		skuDetailVo.setStockCount(skuOrderStockDTO.getStockCount());
		if(limitConfigDTO != null){
			MobileSKULimitVO skuLimitVO = new MobileSKULimitVO();
			skuLimitVO.setAllowBuyNum(limitConfigDTO.getAllowedNum());
			skuLimitVO.setEndTime(limitConfigDTO.getEndTime());
			skuLimitVO.setStartTime(limitConfigDTO.getStartTime());
			skuLimitVO.setLimitDescrp(limitConfigDTO.getNote());
			skuDetailVo.setSkuLimitVO(skuLimitVO );
		}
		
		return skuDetailVo;
	}

	private void setBrandInfo(long spuId, SkuDetailVo skuDetailVo) {
		// TODO Auto-generated method stub
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setId(spuId);
		spuDTO = itemSPUService.getItemSPU(spuDTO);
		if (spuDTO != null) {
			BrandDTO b = brandService.getBrandByBrandId(spuDTO.getBrandId());
			if (b != null && b.getBrand() != null) {
				skuDetailVo.setBrandName(b.getBrand().getBrandNameZh());
			} else {
				skuDetailVo.setBrandName("");
			}

		}

	}

}
