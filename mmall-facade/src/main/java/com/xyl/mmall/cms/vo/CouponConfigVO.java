/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.xyl.mmall.promotion.dto.CouponConfigDTO;

/**
 * CouponConfigVO.java created by yydx811 at 2015年12月30日 下午4:28:32
 * 优惠券配置vo
 *
 * @author yydx811
 */
public class CouponConfigVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 6197682488246909076L;

	/** 优惠券配置id. */
	private long couponConfigId;

	/** 站点id. */
	private long siteId;

	/** 优惠券code，逗号分割. */
	private String couponCodes;

	/** 优惠券配置类型，1新手券. */
	private int couponConfigType;
	
	/** 使用相对时间，1使用，0不使用. */
	private int isRelativeTime;

	/** 是否生效，1生效，0无效. */
	private int isValid;
	
	public CouponConfigVO() {
	}

	public CouponConfigVO(CouponConfigDTO obj) {
		this.couponConfigId = obj.getId();
		this.couponCodes = obj.getCouponCodes();
		this.siteId = obj.getSiteId();
		this.isRelativeTime = obj.getIsRelativeTime();
		this.isValid = obj.getValidFlag();
		this.couponConfigType = obj.getType();
	}
	
	public CouponConfigDTO convertToDTO() {
		CouponConfigDTO couponConfigDTO = new CouponConfigDTO();
		couponConfigDTO.setId(couponConfigId);
		couponConfigDTO.setSiteId(siteId);
		couponConfigDTO.setType(couponConfigType);
		couponConfigDTO.setCouponCodes(couponCodes);
		couponConfigDTO.setIsRelativeTime(isRelativeTime);
		couponConfigDTO.setValidFlag(isValid);
		return couponConfigDTO;
	}
	
	public long getCouponConfigId() {
		return couponConfigId;
	}

	public void setCouponConfigId(long couponConfigId) {
		this.couponConfigId = couponConfigId;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public String getCouponCodes() {
		return couponCodes;
	}

	public void setCouponCodes(String couponCodes) {
		this.couponCodes = couponCodes;
	}

	public int getCouponConfigType() {
		return couponConfigType;
	}

	public void setCouponConfigType(int couponConfigType) {
		this.couponConfigType = couponConfigType;
	}

	public int getIsRelativeTime() {
		return isRelativeTime;
	}

	public void setIsRelativeTime(int isRelativeTime) {
		this.isRelativeTime = isRelativeTime;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
}
