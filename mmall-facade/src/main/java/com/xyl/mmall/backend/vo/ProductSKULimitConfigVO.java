/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;

/**
 * ProductSKULimitConfigVO.java created by yydx811 at 2015年11月18日 下午2:11:48
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class ProductSKULimitConfigVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 8987623504632000906L;

	/** 商品限量配置id. */
	private long limitConfigId;
	
	/** 商品id. */
	private long skuId;

	/** 限购数量. */
	private int skuLimitNum;

	/** 起始时间. */
	private long limitStartTime;

	/** 结束时间. */
	private long limitEndTime;

	/** 限购周期. */
	private long limitPeriod;

	/** 限购说明. */
	private String limitComment;
	
	/** 允许购物的数量. */
	private int allowBuyNum;

	public ProductSKULimitConfigVO() {
	}

	public ProductSKULimitConfigVO(ProductSKULimitConfigDTO obj) {
		this.limitConfigId = obj.getId();
		this.skuId = obj.getProductSKUId();
		this.skuLimitNum = obj.getLimitNumber();
		this.limitStartTime = obj.getStartTime();
		this.limitEndTime = obj.getEndTime();
		this.limitPeriod = obj.getPeriod();
		this.limitComment = obj.getNote();
		this.allowBuyNum = obj.getAllowedNum();
	}
	
	public ProductSKULimitConfigDTO convertToDTO() {
		ProductSKULimitConfigDTO skuLimitConfigDTO = new ProductSKULimitConfigDTO();
		skuLimitConfigDTO.setId(limitConfigId);
		skuLimitConfigDTO.setProductSKUId(skuId);
		skuLimitConfigDTO.setLimitNumber(skuLimitNum);
		skuLimitConfigDTO.setStartTime(limitStartTime);
		skuLimitConfigDTO.setEndTime(limitEndTime);
		skuLimitConfigDTO.setPeriod(limitPeriod);
		skuLimitConfigDTO.setNote(limitComment);
		return skuLimitConfigDTO;
	}
	
	public long getLimitConfigId() {
		return limitConfigId;
	}

	public void setLimitConfigId(long limitConfigId) {
		this.limitConfigId = limitConfigId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public int getSkuLimitNum() {
		return skuLimitNum;
	}

	public void setSkuLimitNum(int skuLimitNum) {
		this.skuLimitNum = skuLimitNum;
	}

	public long getLimitStartTime() {
		return limitStartTime;
	}

	public void setLimitStartTime(long limitStartTime) {
		this.limitStartTime = limitStartTime;
	}

	public long getLimitEndTime() {
		return limitEndTime;
	}

	public void setLimitEndTime(long limitEndTime) {
		this.limitEndTime = limitEndTime;
	}

	public long getLimitPeriod() {
		return limitPeriod;
	}

	public void setLimitPeriod(long limitPeriod) {
		this.limitPeriod = limitPeriod;
	}

	public String getLimitComment() {
		return limitComment;
	}

	public void setLimitComment(String limitComment) {
		this.limitComment = limitComment;
	}

	public int getAllowBuyNum() {
		return allowBuyNum;
	}

	public void setAllowBuyNum(int allowBuyNum) {
		this.allowBuyNum = allowBuyNum;
	}
}
