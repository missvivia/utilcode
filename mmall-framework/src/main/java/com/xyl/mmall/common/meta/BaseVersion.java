package com.xyl.mmall.common.meta;

import java.io.Serializable;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfField;

public class BaseVersion implements Serializable{
	
	/** . */
	private static final long serialVersionUID = -6947710965520789120L;

	@AnnonOfField(desc = "创建时间")
	private Date createTime;

	@AnnonOfField(desc = "创建人")
	private long createBy;
	
	@AnnonOfField(desc = "修改时间")
	private Date updateTime;

	@AnnonOfField(desc = "修改人")
	private long updateBy;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}

	
}
