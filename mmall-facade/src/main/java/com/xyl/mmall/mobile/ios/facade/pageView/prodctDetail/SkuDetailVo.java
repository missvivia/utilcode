package com.xyl.mmall.mobile.ios.facade.pageView.prodctDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.itemcenter.dto.ProdParamDTO;
import com.xyl.mmall.itemcenter.dto.ProdPicDTO;
import com.xyl.mmall.itemcenter.dto.ProdSpeciDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.MobileSKULimitVO;
import com.xyl.mmall.mobile.ios.facade.pageView.common.IosProcPrice;
import com.xyl.mmall.mobile.ios.facade.pageView.common.MobileSKUDetailVO;

public class SkuDetailVo {

	/** 商品id. */
	private long skuId;

	private String name;

	private String title;

	private BigDecimal salePrice;

	private int batchNum;

	private int canReturn;

	private String unit;

	private long storeId;

	private String storeName;

	private BigDecimal batchCash;

	private int stockCount;

	private boolean isCOD = true;

	/** 保质期. */
	private String expireDate;

	/** 生产日期. */
	private long prodProduceDate;

	/** 品牌名. */
	private String brandName;

	/** 价格列表. */
	private List<IosProcPrice> priceList;

	/** 商品属性列表. */
	private List<ProdParamDTO> paramList;

	/** 商品规格列表. */
	private List<ProdSpeciDTO> speciList;

	private List<ProdPicDTO> picList;

	/** 商品详情. */
	private MobileSKUDetailVO prodDetail;

	private MobileSKULimitVO skuLimitVO;

	private int status;

	private String updateTime;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public SkuDetailVo() {

	}

	public SkuDetailVo(ProductSKUDTO pro) {
		this.skuId = pro.getId();
		this.picList = pro.getPicList();
		this.name = pro.getName();
		this.title = pro.getTitle();
		this.salePrice = pro.getSalePrice();
		this.batchNum = pro.getBatchNum();
		this.canReturn = pro.getCanReturn();
		this.unit = pro.getUnit();
		this.storeId = pro.getBusinessId();
		this.storeName = pro.getStoreName();
		this.batchCash = pro.getBatchCash();
		this.setPriceList(pro.getPriceList());
		this.paramList = pro.getParamList();
		this.speciList = pro.getSpeciList();
		this.expireDate = pro.getExpire();
		this.prodProduceDate = pro.getProduceDate() == null ? 0l : pro.getProduceDate().getTime();
		this.updateTime = pro.getUpdateTime() == null ? "" :String.valueOf(pro.getUpdateTime().getTime());
		this.status = pro.getStatus();
		if (pro.getDetailDTO() != null) {
			this.prodDetail = new MobileSKUDetailVO(pro.getDetailDTO());
		}
	}


	public MobileSKULimitVO getSkuLimitVO() {
		return skuLimitVO;
	}

	public void setSkuLimitVO(MobileSKULimitVO skuLimitVO) {
		this.skuLimitVO = skuLimitVO;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public long getProdProduceDate() {
		return prodProduceDate;
	}

	public void setProdProduceDate(long prodProduceDate) {
		this.prodProduceDate = prodProduceDate;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	public MobileSKUDetailVO getProdDetail() {
		return prodDetail;
	}

	public void setProdDetail(MobileSKUDetailVO prodDetail) {
		this.prodDetail = prodDetail;
	}

	public List<ProdPicDTO> getPicList() {
		return picList;
	}

	public void setPicList(List<ProdPicDTO> picList) {
		this.picList = picList;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public int getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}

	public int getCanReturn() {
		return canReturn;
	}

	public void setCanReturn(int canReturn) {
		this.canReturn = canReturn;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public BigDecimal getBatchCash() {
		return batchCash;
	}

	public void setBatchCash(BigDecimal batchCash) {
		this.batchCash = batchCash;
	}

	public boolean isCOD() {
		return isCOD;
	}

	public void setCOD(boolean isCOD) {
		this.isCOD = isCOD;
	}

	public List<IosProcPrice> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ProductPriceDTO> priceList) {
		if (priceList != null && !priceList.isEmpty()) {
			this.priceList = new ArrayList<>();
			IosProcPrice iosProcPrice = null;
			for (ProductPriceDTO priceDTO : priceList) {
				iosProcPrice = new IosProcPrice();
				iosProcPrice.setMaxNum(priceDTO.getMaxNumber());
				iosProcPrice.setMinNum(priceDTO.getMinNumber());
				iosProcPrice.setPrice(priceDTO.getPrice());
				iosProcPrice.setPriceId(priceDTO.getId());
				this.priceList.add(iosProcPrice);
			}
		}

	}

	public List<ProdParamDTO> getParamList() {
		return paramList;
	}

	public void setParamList(List<ProdParamDTO> paramList) {
		this.paramList = paramList;
	}

	public List<ProdSpeciDTO> getSpeciList() {
		return speciList;
	}

	public void setSpeciList(List<ProdSpeciDTO> speciList) {
		this.speciList = speciList;
	}

}
