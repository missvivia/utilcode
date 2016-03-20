/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * CouponConfig.java created by yydx811 at 2015年12月30日 下午4:20:59
 * 优惠券配置
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_Promotion_CouponConfig", desc = "优惠券配置表", dbCreateTimeName = "CreateTime")
public class CouponConfig implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 6146891541874061308L;

	@AnnonOfField(desc = "优惠券配置id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "站点id", policy = true)
	private long siteId;

	@AnnonOfField(desc = "优惠券code，逗号分割")
	private String couponCodes;

	@AnnonOfField(desc = "优惠券配置类型，1新手券")
	private int type;
	
	@AnnonOfField(desc = "使用相对时间，1使用，0不使用")
	private int isRelativeTime;

	@AnnonOfField(desc = "是否生效，1生效，0无效")
	private int validFlag;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsRelativeTime() {
		return isRelativeTime;
	}

	public void setIsRelativeTime(int isRelativeTime) {
		this.isRelativeTime = isRelativeTime;
	}

	public int getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(int validFlag) {
		this.validFlag = validFlag;
	}
}
