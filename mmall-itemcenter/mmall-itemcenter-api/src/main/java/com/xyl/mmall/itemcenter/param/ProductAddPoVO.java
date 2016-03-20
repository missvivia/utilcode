package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

/**
 * 档期添加商品的商品信息接收类
 * 
 * @author hzhuangluqian
 *
 */
public class ProductAddPoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6156913363033787838L;

	/** 商品id */
	private long productId;

	private String goodsNo;

	private String colorNum;

	/** 添加的sku信息 */
	private List<SkuAddPoVO> skuList;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public List<SkuAddPoVO> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<SkuAddPoVO> skuList) {
		this.skuList = skuList;
	}

	public String getColorNum() {
		return colorNum;
	}

	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
	}

	@Override
	public int hashCode() {
		int elm1 = goodsNo.hashCode();
		int result = 1;
		result = 31 * result + elm1;
		int elm2 = colorNum.hashCode();
		result = 31 * result + elm2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductAddPoVO other = (ProductAddPoVO) obj;
		if (!goodsNo.equals(other.getGoodsNo()))
			return false;
		if (!colorNum.equals(other.getColorNum()))
			return false;
		return true;
	}
}
