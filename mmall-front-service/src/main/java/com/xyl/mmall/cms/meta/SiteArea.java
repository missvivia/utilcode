/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * SiteArea.java created by yydx811 at 2015年7月16日 下午3:53:35
 * 站点区域
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_CMS_SiteArea", desc = "站点区域表")
public class SiteArea implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 1766191979116508590L;

	@AnnonOfField(desc = "id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "站点id")
	private long siteId;

	@AnnonOfField(desc = "区域id")
	private long areaId;

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

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
}
