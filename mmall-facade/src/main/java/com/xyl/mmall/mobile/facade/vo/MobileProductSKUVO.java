/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.mobile.facade.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.backend.vo.ProdPicVO;
import com.xyl.mmall.backend.vo.ProductPriceVO;
import com.xyl.mmall.backend.vo.ProductSKUDetailVO;
import com.xyl.mmall.framework.vo.BaseVersionVO;
import com.xyl.mmall.itemcenter.dto.ProdPicDTO;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.mainsite.vo.ProdSpeciMainSiteVO;
import com.xyl.mmall.mobile.ios.facade.pageView.cartList.MobileSKULimitVO;

/** 
 * 商品vo
 */
public class MobileProductSKUVO extends BaseVersionVO {
	/** 序列化id. */
	private static final long serialVersionUID = 6118295138796475574L;

	/** 商品id. */
	private long skuId;

	/** 单品id. */
	private long itemSPUId;

	/** 商品名称. */
	private String productName;
	
	/** 商品副标题. */
	private String productTitle;

	/** 分类全名. */
	private String categoryFullName;

	/** 分类id. */
	private long categoryNormalId;

	/** 品牌名. */
	private String brandName;

	/** 条形码. */
	private String prodBarCode;
	
	/** 销售单位. */
	private String prodUnit;

	/** 保质期. */
	private String expireDate;

	/** 生产日期. */
	private long prodProduceDate;

	/** 退货政策，1允许，2拒绝. */
	private int canReturn;

	/** 起批数量. */
	private int batchNum;
	
	/** 库存 。 */
	private int skuNum;

	/** 建议零售价. */
	private BigDecimal salePrice;

	/** 商品状态，1未审核，2审核中，3审核未通过，4已上架，5已下架. */
	private int prodStatus;
	
	/** 缩略图路径 . */
	private String showPicPath;
	
	/** 品牌ID. */
	private long brandId;
	
	/** 店铺名称. */
	private String storeName;
	
	/** 商家id. */
	private long supplierId;
	
	/** 库存. */
	private int productNum;
	
	/** 商品销量. */
	private int productSaleNum;
	
	/** 商品详情. */
	private ProductSKUDetailVO prodDetail;

	/** 商品图片. */
	private List<ProdPicVO> picList;

	/** 商品规格. */
	private List<ProdSpeciMainSiteVO> speciList;

	/** 商品价格区间. */
	private List<ProductPriceVO> priceList;
	
	private int isLimited;
	
	private MobileSKULimitVO skuLimitVO;
	
	public MobileProductSKUVO() {
	}

	public MobileProductSKUVO(ProductSKUDTO obj) {
		this.skuId = obj.getId();
		this.itemSPUId = obj.getSpuId();
		this.productTitle = obj.getTitle();
		this.prodUnit = obj.getUnit();
		this.expireDate = obj.getExpire();
		this.prodProduceDate = obj.getProduceDate() == null ? 0l : obj.getProduceDate().getTime();
		this.canReturn = obj.getCanReturn();
		this.batchNum = obj.getBatchNum();
		this.salePrice = obj.getSalePrice();
		this.prodStatus = obj.getStatus();
		this.categoryFullName = obj.getCategoryName();
		this.productName = obj.getName();
		this.showPicPath = obj.getShowPicPath();
		this.skuNum = obj.getSkuNum();
		this.brandId = obj.getBrandId();
		this.storeName = obj.getStoreName();
		this.supplierId = obj.getBusinessId();
		this.productSaleNum = obj.getSaleNum();
		this.categoryNormalId = obj.getCategoryNormalId();
		this.isLimited = obj.getIsLimited();
		setUpdateTime(obj.getUpdateTime());
		if (obj.getDetailDTO() != null) {
			this.prodDetail = new ProductSKUDetailVO(obj.getDetailDTO());
		}
		if (!CollectionUtils.isEmpty(obj.getPriceList())) {
			this.priceList = new ArrayList<ProductPriceVO>(obj.getPriceList().size());
			for (int i = 0; i < obj.getPriceList().size(); i++) {
				ProductPriceVO priceVO = new ProductPriceVO(obj.getPriceList().get(i));
				if (i > 0) {
					priceVO.setProdMaxNumber(priceList.get(i - 1).getProdMinNumber());
				}
				priceList.add(priceVO);
			}
		}
		if (!CollectionUtils.isEmpty(obj.getPicList())) {
			this.picList = new ArrayList<ProdPicVO>(obj.getPicList().size());
			for (ProdPicDTO picDTO : obj.getPicList()) {
				picList.add(new ProdPicVO(picDTO));
			}
		}
	}
	
	public MobileSKULimitVO getSkuLimitVO() {
		return skuLimitVO;
	}

	public void setSkuLimitVO(MobileSKULimitVO skuLimitVO) {
		this.skuLimitVO = skuLimitVO;
	}

	public int getIsLimited() {
		return isLimited;
	}

	public void setIsLimited(int isLimited) {
		this.isLimited = isLimited;
	}

	public ProductSKUDTO convertToDTO() {
		ProductSKUDTO skuDTO = new ProductSKUDTO();
		skuDTO.setId(skuId);
		skuDTO.setSpuId(itemSPUId);
		skuDTO.setTitle(productTitle);
		skuDTO.setUnit(prodUnit);
		skuDTO.setExpire(expireDate);
		skuDTO.setProduceDate(prodProduceDate < 0l ? null : new Date(prodProduceDate));
		skuDTO.setCanReturn(canReturn);
		skuDTO.setBatchNum(batchNum);
		skuDTO.setSalePrice(salePrice);
		skuDTO.setStatus(prodStatus);
		if (prodDetail != null) {
			skuDTO.setDetailDTO(prodDetail.convertToDTO());
		}
		if (!CollectionUtils.isEmpty(priceList)) {
			List<ProductPriceDTO> priceDTOList = new ArrayList<ProductPriceDTO>(priceList.size());
			for (ProductPriceVO priceVO : priceList) {
				priceDTOList.add(priceVO.converToDTO());
			}
			skuDTO.setPriceList(priceDTOList);
		}
		if (!CollectionUtils.isEmpty(picList)) {
			List<ProdPicDTO> picDTOList = new ArrayList<ProdPicDTO>(picList.size());
			for (ProdPicVO picVO : picList) {
				picDTOList.add(picVO.convertToDTO());
			}
			skuDTO.setPicList(picDTOList);
		}
		return skuDTO;
	}
	
	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getItemSPUId() {
		return itemSPUId;
	}

	public void setItemSPUId(long itemSPUId) {
		this.itemSPUId = itemSPUId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getCategoryFullName() {
		return categoryFullName;
	}

	public void setCategoryFullName(String categoryFullName) {
		this.categoryFullName = categoryFullName;
	}

	public long getCategoryNormalId() {
		return categoryNormalId;
	}

	public void setCategoryNormalId(long categoryNormalId) {
		this.categoryNormalId = categoryNormalId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProdBarCode() {
		return prodBarCode;
	}

	public void setProdBarCode(String prodBarCode) {
		this.prodBarCode = prodBarCode;
	}

	public String getProdUnit() {
		return prodUnit;
	}

	public void setProdUnit(String prodUnit) {
		this.prodUnit = prodUnit;
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

	public int getCanReturn() {
		return canReturn;
	}

	public void setCanReturn(int canReturn) {
		this.canReturn = canReturn;
	}

	public int getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}
	
	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public int getProdStatus() {
		return prodStatus;
	}

	public void setProdStatus(int prodStatus) {
		this.prodStatus = prodStatus;
	}

	
	public String getShowPicPath() {
		return showPicPath;
	}

	public void setShowPicPath(String showPicPath) {
		this.showPicPath = showPicPath;
	}
	
	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}

	public ProductSKUDetailVO getProdDetail() {
		return prodDetail;
	}

	public void setProdDetail(ProductSKUDetailVO prodDetail) {
		this.prodDetail = prodDetail;
	}

	public List<ProdPicVO> getPicList() {
		return picList;
	}

	public void setPicList(List<ProdPicVO> picList) {
		this.picList = picList;
	}

	public List<ProdSpeciMainSiteVO> getSpeciList() {
		return speciList;
	}

	public void setSpeciList(List<ProdSpeciMainSiteVO> speciList) {
		this.speciList = speciList;
	}

	public List<ProductPriceVO> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ProductPriceVO> priceList) {
		this.priceList = priceList;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public int getProductSaleNum() {
		return productSaleNum;
	}

	public void setProductSaleNum(int productSaleNum) {
		this.productSaleNum = productSaleNum;
	}
}
