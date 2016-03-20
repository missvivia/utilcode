/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * AgentArea.java created by yydx811 at 2015年7月21日 下午5:02:27
 * 管理员区域
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_Member_AgentArea", desc = "管理员站点区域表")
public class AgentArea implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -1068635455121609808L;

	@AnnonOfField(desc = "id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "管理员id", policy = true)
	private long agentId;
	
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

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
}
