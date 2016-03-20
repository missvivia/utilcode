/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.util.List;

import com.xyl.mmall.cms.dto.SiteCMSDTO;

/**
 * SiteCMSVO.java created by yydx811 at 2015年7月16日 下午4:37:46
 * 站点vo
 *
 * @author yydx811
 */
public class SiteCMSVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -831792815292724670L;

	/** 站点id. */
	private long siteId;
	
	/** 站点名. */
	private String siteName;
	
	/** 区域信息. */
	private List<SiteAreaVO> areaList;

	public SiteCMSVO() {
	}
	
	public SiteCMSVO(SiteCMSDTO obj) {
		this.siteId = obj.getId();
		this.siteName = obj.getName();
	}
	
	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public List<SiteAreaVO> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<SiteAreaVO> areaList) {
		this.areaList = areaList;
	}
}
