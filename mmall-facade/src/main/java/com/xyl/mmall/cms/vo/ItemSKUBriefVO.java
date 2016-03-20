/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.backend.vo.ProductPriceVO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;

/**
 * ItemSKUBriefVO.java created by yydx811 at 2015年11月20日 上午12:02:58
 * 商品简略信息vo
 *
 * @author yydx811
 */
public class ItemSKUBriefVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 6605007623719802740L;

	/** 商品id. */
	private long skuId;

	/** 店铺id. */
	private long storeId;

	/** 是否限购，1限购，其他不限购. */
	private long isLimited;

	/** 商品名. */
	private String skuName;

	/** 商品副标题. */
	private String skuTitle;

	/** 店铺名. */
	private String storeName;

	/** 图片url. */
	private String picUrl;

	/** 价格列表. */
	private List<ProductPriceVO> priceList;

	public ItemSKUBriefVO() {
	}

	public ItemSKUBriefVO(ProductSKUDTO obj) {
		this.skuId = obj.getId();
		this.skuName = obj.getName();
		this.skuTitle = obj.getTitle();
		this.storeId = obj.getBusinessId();
		this.isLimited = obj.getIsLimited();
		this.picUrl = obj.getShowPicPath();
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
	}
	
	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public long getIsLimited() {
		return isLimited;
	}

	public void setIsLimited(long isLimited) {
		this.isLimited = isLimited;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuTitle() {
		return skuTitle;
	}

	public void setSkuTitle(String skuTitle) {
		this.skuTitle = skuTitle;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public List<ProductPriceVO> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ProductPriceVO> priceList) {
		this.priceList = priceList;
	}
}
