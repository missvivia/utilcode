package com.xyl.mmall.mobile.ios.facade.pageView.orderList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.mobile.ios.facade.pageView.common.IosProcPrice;

public class IosOrderSku {
	private long skuId;
	
	private String name;
	
	private String thumb;
	
	private List<IosProcPrice> priceList;
	
	public List<IosProcPrice> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ProductPriceDTO> productPriceDTOs) {
		if(CollectionUtils.isEmpty(productPriceDTOs)){
			return;
		}
		List<IosProcPrice> priceList = new ArrayList<>();
		IosProcPrice iosProcPrice = null;
		for(ProductPriceDTO priceDTO:productPriceDTOs){
			iosProcPrice = new IosProcPrice();
			iosProcPrice.setPriceId(priceDTO.getId());
			iosProcPrice.setPrice(priceDTO.getPrice());
			iosProcPrice.setMaxNum(priceDTO.getMaxNumber());
			iosProcPrice.setMinNum(priceDTO.getMinNumber());
			priceList.add(iosProcPrice);
		}
		
		this.priceList = priceList;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}


}
