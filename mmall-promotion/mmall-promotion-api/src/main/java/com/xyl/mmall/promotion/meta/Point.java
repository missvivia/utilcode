/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * Point.java created by yydx811 at 2015年12月23日 上午10:44:43
 * 积分表
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "积分审核表", tableName = "Mmall_Promotion_Point", dbCreateTimeName = "CreateTime")
public class Point implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 417230829467094653L;

	@AnnonOfField(desc = "id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "申请管理员id")
	private long applyUserId;

	@AnnonOfField(desc = "审核标记，0：初始状态，1：提交审核，2审核通过，3拒绝，4删除")
	private int auditState;

	@AnnonOfField(desc = "审核时间")
	private long auditTime;

	@AnnonOfField(desc = "审核管理员Id")
	private long auditUserId;

	@AnnonOfField(desc = "站点id", policy = true)
	private long siteId;

	@AnnonOfField(desc = "积分调整描述")
	private String description;

	@AnnonOfField(desc = "积分调整名称")
	private String name;

	@AnnonOfField(desc = "审核说明")
	private String reason;

	@AnnonOfField(desc = "积分增减量")
	private int pointDelta;

	@AnnonOfField(desc = "绑定用户", type = "TEXT")
	private String users;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(long auditTime) {
		this.auditTime = auditTime;
	}

	public long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getPointDelta() {
		return pointDelta;
	}

	public void setPointDelta(int pointDelta) {
		this.pointDelta = pointDelta;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}
}
