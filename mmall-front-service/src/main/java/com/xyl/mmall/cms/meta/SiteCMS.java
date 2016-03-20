/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.meta;

import java.io.Serializable;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * SiteCMS.java created by yydx811 at 2015年7月16日 下午3:35:54
 * 站点
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_CMS_Site", desc = "站点表", dbCreateTimeName = "CreateTime")
public class SiteCMS implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 6597138270021574056L;

	@AnnonOfField(desc = "站点id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "站点名")
	private String name;

	@AnnonOfField(desc = "创建人Id")
	private long createOperator;

	@AnnonOfField(desc = "修改人id")
	private long updateOperator;

	@AnnonOfField(desc = "创建时间")
	private Date createTime;

	@AnnonOfField(desc = "更新时间")
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreateOperator() {
		return createOperator;
	}

	public void setCreateOperator(long createOperator) {
		this.createOperator = createOperator;
	}

	public long getUpdateOperator() {
		return updateOperator;
	}

	public void setUpdateOperator(long updateOperator) {
		this.updateOperator = updateOperator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
