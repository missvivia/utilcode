/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.ip.meta.LocationCode;

/**
 * SiteAreaVO.java created by yydx811 at 2015年7月16日 下午7:50:32
 * 站点Q区域vo
 *
 * @author yydx811
 */
public class SiteAreaVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -2662971196537572813L;
	
	/** 站点Id. */
	private long siteId;
	
	/** 区域id. */
	private long areaId;

	/** 区域名称. */
	private String areaName;
	
	/** 是否选中，0未选中，1全选，2部分选中. */
	private int isChecked;

	public SiteAreaVO() {
	}
	
	public SiteAreaVO(SiteAreaDTO siteAreaDTO) {
		this.areaId = siteAreaDTO.getAreaId();
		this.siteId = siteAreaDTO.getSiteId();
	}

	public SiteAreaVO(LocationCode obj) {
		this.areaId = obj.getCode();
		this.areaName = obj.getLocationName();
	}
	
	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(int isChecked) {
		this.isChecked = isChecked;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	
	
}
