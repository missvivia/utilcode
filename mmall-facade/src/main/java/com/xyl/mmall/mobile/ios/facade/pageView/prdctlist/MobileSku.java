package com.xyl.mmall.mobile.ios.facade.pageView.prdctlist;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.backend.vo.ProductPriceVO;
import com.xyl.mmall.mobile.facade.vo.MobileProductSKUVO;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.MobileSKULimitVO;
import com.xyl.mmall.mobile.ios.facade.pageView.common.IosProcPrice;

public class MobileSku {

	/** 商品id. */
	private long skuId;

	private String thumb;

	private String name;

	private String title;

	private List<IosProcPrice> priceList;

	private String unit;
	
	private long stockCount;
	
	private MobileSKULimitVO skuLimitVO;
	
	private String storeName;
	
	
	public MobileSku() {

	}

	
	public String getStoreName() {
		return storeName;
	}


	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}


	public MobileSKULimitVO getSkuLimitVO() {
		return skuLimitVO;
	}


	public void setSkuLimitVO(MobileSKULimitVO skuLimitVO) {
		this.skuLimitVO = skuLimitVO;
	}


	public MobileSku(MobileProductSKUVO vo) {
		if (vo != null) {
			this.skuId = vo.getSkuId();
			this.name = vo.getProductName();
			this.title = vo.getProductTitle();
			this.thumb = vo.getPicList() != null ? vo.getPicList().get(0).getPicPath() : "";
			this.unit = vo.getProdUnit();
			this.priceList = converToProcPrice(vo.getPriceList());
			this.stockCount = vo.getSkuNum();
			this.setSkuLimitVO(vo.getSkuLimitVO());
			this.storeName = vo.getStoreName();
		}
	}

	public List<IosProcPrice> converToProcPrice(List<ProductPriceVO> priceVOs) {
		List<IosProcPrice> iosProcPrices = new ArrayList<>();
		if (priceVOs != null && !priceVOs.isEmpty()) {
			
			IosProcPrice iosProcPrice = null;
			
			for(ProductPriceVO priceVO:priceVOs){
				iosProcPrice = new IosProcPrice();
				iosProcPrice.setPriceId(priceVO.getProdPriceId());
				iosProcPrice.setPrice(priceVO.getProdPrice());
				iosProcPrice.setMinNum(priceVO.getProdMinNumber());
				iosProcPrice.setMaxNum(priceVO.getProdMaxNumber());
				iosProcPrices.add(iosProcPrice);
			}
		}
		return iosProcPrices;
	}

	public long getSkuId() {
		return skuId;
	}

	public long getStockCount() {
		return stockCount;
	}

	public void setStockCount(long stockCount) {
		this.stockCount = stockCount;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public List<IosProcPrice> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<IosProcPrice> priceList) {
		this.priceList = priceList;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
