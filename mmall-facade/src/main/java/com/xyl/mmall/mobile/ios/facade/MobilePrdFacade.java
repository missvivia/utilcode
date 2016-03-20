package com.xyl.mmall.mobile.ios.facade;

import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.mobile.ios.facade.pageView.prodctDetail.SkuDetailVo;

public interface MobilePrdFacade {

	public SkuDetailVo getProductSKUVO(ProductSKUDTO skuDTO);
}
