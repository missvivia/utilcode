/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.xyl.mmall.itemcenter.dto.ItemSPUDTO;

/**
 * ItemSPUVO.java created by yydx811 at 2015年5月6日 下午6:56:29
 * 单品vo
 *
 * @author yydx811
 */
public class ItemSPUVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -8895756096768894412L;
	
	/** 单品id. */
	private long spuId;

	/** 条形码. */
	private String spuBarCode;

	/** 单品名称. */
	private String spuName;

	/** 分类id. */
	private long categoryNormalId;

	/** 分类名称. */
	private String categoryNormalName;
	
	/** 品牌id. */
	private long brandId;

	/** 品牌名称. */
	private String brandName;

	/** 操作人. */
	private String operator;
	
	/** 一级分类名. */
	private String firstCategoryName;

	/** 二级分类名. */
	private String secondCategoryName;

	/** 三级分类名. */
	private String thirdCategoryName;

	public ItemSPUVO() {
	}

	public ItemSPUVO(ItemSPUDTO obj) {
		this.spuId = obj.getId();
		this.spuBarCode = obj.getBarCode();
		this.spuName = obj.getName();
		this.categoryNormalId = obj.getCategoryNormalId();
		this.brandId = obj.getBrandId();
	}
	
	public ItemSPUDTO convert() {
		ItemSPUDTO spuDTO = new ItemSPUDTO();
		spuDTO.setId(spuId);
		spuDTO.setBarCode(spuBarCode);
		spuDTO.setName(spuName);
		spuDTO.setCategoryNormalId(categoryNormalId);
		spuDTO.setBrandId(brandId);
		return spuDTO;
	}

	public long getSpuId() {
		return spuId;
	}

	public void setSpuId(long spuId) {
		this.spuId = spuId;
	}

	public String getSpuBarCode() {
		return spuBarCode;
	}

	public void setSpuBarCode(String spuBarCode) {
		this.spuBarCode = spuBarCode;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public long getCategoryNormalId() {
		return categoryNormalId;
	}

	public void setCategoryNormalId(long categoryNormalId) {
		this.categoryNormalId = categoryNormalId;
	}

	public String getCategoryNormalName() {
		return categoryNormalName;
	}

	public void setCategoryNormalName(String categoryNormalName) {
		this.categoryNormalName = categoryNormalName;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getFirstCategoryName() {
		return firstCategoryName;
	}

	public void setFirstCategoryName(String firstCategoryName) {
		this.firstCategoryName = firstCategoryName;
	}

	public String getSecondCategoryName() {
		return secondCategoryName;
	}

	public void setSecondCategoryName(String secondCategoryName) {
		this.secondCategoryName = secondCategoryName;
	}

	public String getThirdCategoryName() {
		return thirdCategoryName;
	}

	public void setThirdCategoryName(String thirdCategoryName) {
		this.thirdCategoryName = thirdCategoryName;
	}
}
