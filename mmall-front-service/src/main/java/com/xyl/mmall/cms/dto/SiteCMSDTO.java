/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.meta.SiteCMS;

/**
 * SiteCMSDTO.java created by yydx811 at 2015年7月16日 下午4:06:17
 * 站点dto
 *
 * @author yydx811
 */
public class SiteCMSDTO extends SiteCMS {

	/** 序列化id. */
	private static final long serialVersionUID = -5094320745994678357L;

	private List<SiteAreaDTO> areaList;
	
	public SiteCMSDTO() {
	}
	
	public SiteCMSDTO(SiteCMS obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public List<SiteAreaDTO> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<SiteAreaDTO> areaList) {
		this.areaList = areaList;
	}
}
