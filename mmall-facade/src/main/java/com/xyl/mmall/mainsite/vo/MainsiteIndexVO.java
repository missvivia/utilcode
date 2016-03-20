package com.xyl.mmall.mainsite.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.framework.util.UrlBaseUtil;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;

/**
 * 
 * MainsiteIndexVO.java created by lhp at 2015年12月30日 下午1:40:00 这里对类或者接口作简要描述
 *
 * @author lhp
 */
public class MainsiteIndexVO {

	private List<MainsiteStoreVO> mainsiteStoreVOs = new ArrayList<MainsiteStoreVO>();

	public List<MainsiteStoreVO> getMainsiteStoreVOs() {
		return mainsiteStoreVOs;
	}

	public void setMainsiteStoreVOs(List<MainsiteStoreVO> mainsiteStoreVOs) {
		this.mainsiteStoreVOs = mainsiteStoreVOs;
	}

	public void fillMainsiteStoreVOs(List<ProductSKUDTO> productSKUDTOs,
			Map<String, List<ProductPriceDTO>> productPriceMap, BusinessDTO businessDTO) {
		if (CollectionUtil.isNotEmptyOfList(productSKUDTOs)) {
			MainsiteStoreVO mainsiteStoreVO = new MainsiteStoreVO();
			mainsiteStoreVO.setStoreName(businessDTO.getStoreName());
			mainsiteStoreVO.setStoreUrl(UrlBaseUtil.buildStoreUrl(businessDTO.getId()));
			mainsiteStoreVO.setIndexWeight(businessDTO.getIndexWeight());
			List<MainsiteProductVO> mainsiteProductVOs = new ArrayList<MainsiteProductVO>();
			for (ProductSKUDTO productSKUDTO : productSKUDTOs) {
				MainsiteProductVO mainsiteProductVO = new MainsiteProductVO();
				mainsiteProductVO.setSkuId(productSKUDTO.getId());
				mainsiteProductVO.setProductName(productSKUDTO.getName());
				mainsiteProductVO.setProductTitle(productSKUDTO.getTitle());
				mainsiteProductVO.setShowPicPath(productSKUDTO.getShowPicPath());
				mainsiteProductVO.setDetailUrl(UrlBaseUtil.buildProductUrl(productSKUDTO.getId()));
				mainsiteProductVO.setProductPriceVOs(convertProductPriceDTOToVO(productPriceMap.get(String
						.valueOf(productSKUDTO.getId()))));
				mainsiteProductVOs.add(mainsiteProductVO);
			}
			mainsiteStoreVO.setMainsiteProductVOs(mainsiteProductVOs);
			mainsiteStoreVOs.add(mainsiteStoreVO);
		}
	}

	private List<ProductPriceVO> convertProductPriceDTOToVO(List<ProductPriceDTO> productPriceDTOs) {
		List<ProductPriceVO> productPriceVOs = new ArrayList<ProductPriceVO>();
		if (CollectionUtil.isEmptyOfList(productPriceDTOs)) {
			return productPriceVOs;
		}
		for (ProductPriceDTO productPriceDTO : productPriceDTOs) {
			ProductPriceVO productPriceVO = new ProductPriceVO();
			productPriceVO.setProdMaxNumber(productPriceDTO.getMaxNumber());
			productPriceVO.setProdMinNumber(productPriceDTO.getMinNumber());
			productPriceVO.setProdPrice(productPriceDTO.getPrice());
			productPriceVOs.add(productPriceVO);
		}
		return productPriceVOs;
	}

	public static class MainsiteStoreVO implements Comparable<MainsiteStoreVO> {

		/**
		 * 店铺名称
		 */
		private String storeName;

		/**
		 * 店铺url
		 */
		private String storeUrl;

		/**
		 * 首页权重
		 */
		private int indexWeight;

		private List<MainsiteProductVO> mainsiteProductVOs;

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}

		public String getStoreUrl() {
			return storeUrl;
		}

		public void setStoreUrl(String storeUrl) {
			this.storeUrl = storeUrl;
		}

		public List<MainsiteProductVO> getMainsiteProductVOs() {
			return mainsiteProductVOs;
		}

		public void setMainsiteProductVOs(List<MainsiteProductVO> mainsiteProductVOs) {
			this.mainsiteProductVOs = mainsiteProductVOs;
		}

		public int getIndexWeight() {
			return indexWeight;
		}

		public void setIndexWeight(int indexWeight) {
			this.indexWeight = indexWeight;
		}

		@Override
		public int compareTo(MainsiteStoreVO another) {
			if (this == another) {
				return 0;
			}
			if (another == null) {
				return 1;
			}
			if (this.getIndexWeight() < another.getIndexWeight()) {
				return 1;
			} else if (this.getIndexWeight() > another.getIndexWeight()) {
				return -1;
			}
			return 0;
		}

	}

	public static class MainsiteProductVO {
		/**
		 * 商品Id
		 */
		private long skuId;

		/**
		 * 商品名称
		 */
		private String productName;

		/**
		 * 商品副标题
		 */
		private String productTitle;

		/**
		 * 缩略图路径
		 */
		private String showPicPath;

		/**
		 * 商品详情url
		 */
		private String detailUrl;

		private List<ProductPriceVO> productPriceVOs;

		public long getSkuId() {
			return skuId;
		}

		public void setSkuId(long skuId) {
			this.skuId = skuId;
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

		public String getDetailUrl() {
			return detailUrl;
		}

		public void setDetailUrl(String detailUrl) {
			this.detailUrl = detailUrl;
		}

		public List<ProductPriceVO> getProductPriceVOs() {
			return productPriceVOs;
		}

		public void setProductPriceVOs(List<ProductPriceVO> productPriceVOs) {
			this.productPriceVOs = productPriceVOs;
		}

		public String getShowPicPath() {
			return showPicPath;
		}

		public void setShowPicPath(String showPicPath) {
			this.showPicPath = showPicPath;
		}

	}

	public static class ProductPriceVO {

		/**
		 * 最小数目
		 */
		private int prodMinNumber;

		/**
		 * 最大数目
		 */
		private int prodMaxNumber;

		/**
		 * 价格
		 */
		private BigDecimal prodPrice;

		public int getProdMinNumber() {
			return prodMinNumber;
		}

		public void setProdMinNumber(int prodMinNumber) {
			this.prodMinNumber = prodMinNumber;
		}

		public int getProdMaxNumber() {
			return prodMaxNumber;
		}

		public void setProdMaxNumber(int prodMaxNumber) {
			this.prodMaxNumber = prodMaxNumber;
		}

		public BigDecimal getProdPrice() {
			return prodPrice;
		}

		public void setProdPrice(BigDecimal prodPrice) {
			this.prodPrice = prodPrice;
		}

	}

}
