package com.xyl.mmall.mobile.web.facade.impl;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.enums.ProductStatusType;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.mobile.web.facade.MobileFacade;
import com.xyl.mmall.mobile.web.vo.OrderReplenishStoreVO;
import com.xyl.mmall.mobile.web.vo.OrderReplenishVO;
import com.xyl.mmall.order.dto.OrderReplenishDTO;
import com.xyl.mmall.order.dto.OrderReplenishStoreDTO;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.service.OrderReplenishService;
import com.xyl.mmall.order.service.SkuOrderStockService;

@Facade("mobileFacade")
public class MobileFacadeImpl implements MobileFacade {
	
	@Resource
	private OrderReplenishService orderReplenishService;
	
	@Resource
	private ItemProductService itemProductService;

	@Resource
	private SkuOrderStockService skuOrderStockService;
	
	@Resource
	private BusinessService businessService;
	
	@Override
	public List<OrderReplenishStoreVO> getReplenishList(BasePageParamVO<OrderReplenishStoreVO> pageParamVO, 
			OrderReplenishDTO replenishDTO) {
		BasePageParamVO<OrderReplenishStoreDTO> basePageParamVO = null;
		basePageParamVO = new BasePageParamVO<OrderReplenishStoreDTO>();
		basePageParamVO = pageParamVO.copy(basePageParamVO);
		basePageParamVO = orderReplenishService.getReplenishList(basePageParamVO, replenishDTO);
		if (basePageParamVO == null) {
			return null;
		}
		pageParamVO = basePageParamVO.copy(pageParamVO);
		return convertToVOList(basePageParamVO.getList(), pageParamVO);
	}
	
	private List<OrderReplenishStoreVO> convertToVOList(List<OrderReplenishStoreDTO> replenishStoreDTOList,
			BasePageParamVO<OrderReplenishStoreVO> pageParamVO) {
		if (CollectionUtils.isEmpty(replenishStoreDTOList)) {
			return null;
		}
		List<OrderReplenishStoreVO> retList = new ArrayList<OrderReplenishStoreVO>(replenishStoreDTOList.size());
		int i = 0;
		for (OrderReplenishStoreDTO replenishStoreDTO : replenishStoreDTOList) {
			OrderReplenishStoreVO replenishStoreVO = new OrderReplenishStoreVO();
			// 店铺名
			String storeName = businessService.getBusinessNameById(replenishStoreDTO.getBusinessId(), 0);
			if (StringUtils.isBlank(storeName)) {
				++i;
				continue;
			}
			replenishStoreVO.setSupplierId(replenishStoreDTO.getBusinessId());
			replenishStoreVO.setStoreName(storeName);
			// 补货单列表
			List<OrderReplenishVO> replenishVOList = new ArrayList<OrderReplenishVO>(replenishStoreDTO.getReplenishDTOList().size());
			for (OrderReplenishDTO replenishDTO : replenishStoreDTO.getReplenishDTOList()) {
				OrderReplenishVO replenishVO = new OrderReplenishVO(replenishDTO);
				ProductSKUDTO skuDTO = new ProductSKUDTO();
				skuDTO.setId(replenishDTO.getSkuId());
				skuDTO = itemProductService.getProductSKUDTO(skuDTO, false);
				// 商品状态判断
				if (skuDTO == null) {
					replenishVO.setSkuStatus(ProductStatusType.INVAILD.getIntValue());
					replenishVOList.add(replenishVO);
					continue;
				}
				if (skuDTO.getStatus() == ProductStatusType.ONLINE.getIntValue()) {
					replenishVO.setSkuStatus(ProductStatusType.ONLINE.getIntValue());
				} else if (skuDTO.getStatus() == ProductStatusType.OFFLINE.getIntValue()) {
					replenishVO.setSkuStatus(ProductStatusType.OFFLINE.getIntValue());
					replenishVOList.add(replenishVO);
					continue;
				} else {
					replenishVO.setSkuStatus(ProductStatusType.INVAILD.getIntValue());
					replenishVOList.add(replenishVO);
					continue;
				}
				// 商品库存
				SkuOrderStockDTO orderStock = skuOrderStockService.getSkuOrderStockDTOBySkuId(replenishDTO.getSkuId());
				replenishVO.setSkuStockNum(orderStock == null ? 0 : orderStock.getStockCount());
				// 商品价格
				if (replenishDTO.getBuyPrice().signum() != 1) {
					replenishVOList.add(replenishVO);
					continue;
				}
				ProductPriceDTO price = itemProductService.getProductPriceByBuyNum(replenishDTO.getSkuId(), replenishDTO.getBuyNum());
				if (price == null) {
					replenishVOList.add(replenishVO);
					continue;
				}
				StringBuffer priceUpsAndDowns = new StringBuffer("价格");
				int temp = price.getPrice().compareTo(replenishDTO.getBuyPrice());
				if (temp == 0) {
					priceUpsAndDowns.append("没有变化");
				} else if (temp == -1) {
					priceUpsAndDowns.append("下调 ");
					priceUpsAndDowns.append(replenishDTO.getBuyPrice().subtract(price.getPrice())
							.divide(replenishDTO.getBuyPrice(), 4, RoundingMode.HALF_UP).movePointRight(2));
					priceUpsAndDowns.append("%");
				} else {
					priceUpsAndDowns.append("上调 ");
					priceUpsAndDowns.append(price.getPrice().subtract(replenishDTO.getBuyPrice())
							.divide(replenishDTO.getBuyPrice(), 4, RoundingMode.HALF_UP).movePointRight(2));
					priceUpsAndDowns.append("%");
				}
				replenishVO.setPriceUpsAndDowns(priceUpsAndDowns.toString());
				replenishVOList.add(replenishVO);
			}
			replenishStoreVO.setReplenishVOList(replenishVOList);
			retList.add(replenishStoreVO);
		}
		if (pageParamVO != null) {
			int total = pageParamVO.getTotal() - i;
			pageParamVO.setTotal(total < 0 ? 0 : total);
		}
		return retList;
	}

}
