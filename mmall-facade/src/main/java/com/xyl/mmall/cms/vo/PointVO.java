/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;

/**
 * PointVO.java created by yydx811 at 2015年12月24日 下午5:18:25
 * 积分审核vo
 *
 * @author yydx811
 */
public class PointVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 2444604669680674059L;

	/** 积分审核id. */
	private long pointId;

	/** 申请管理员姓名. */
	private String applyAgentName;

	/** 审核管理员姓名. */
	private String auditAgentName;

	/** 审核状态. */
	private int auditState;

	/** 审核时间. */
	private long auditTime;

	/** 站点id. */
	private long siteId;

	/** 站点名. */
	private String siteName;

	/** 积分调整名称. */
	private String pointName;

	/** 积分描述. */
	private String pointDescription;

	/** 审核原因. */
	private String auditReason;

	/** 积分调整量. */
	private int pointDelta;

	/** 用户列表. */
	private String userAccountList;

//	/** 用户列表对应的初始积分. */
//	private int[] point;

	public long getPointId() {
		return pointId;
	}

	public void setPointId(long pointId) {
		this.pointId = pointId;
	}

	public String getApplyAgentName() {
		return applyAgentName;
	}

	public void setApplyAgentName(String applyAgentName) {
		this.applyAgentName = applyAgentName;
	}

	public String getAuditAgentName() {
		return auditAgentName;
	}

	public void setAuditAgentName(String auditAgentName) {
		this.auditAgentName = auditAgentName;
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

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getPointDescription() {
		return pointDescription;
	}

	public void setPointDescription(String pointDescription) {
		this.pointDescription = pointDescription;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

//	public int[] getPoint() {
//		return point;
//	}
//
//	public void setPoint(int[] point) {
//		this.point = point;
//	}

	public int getPointDelta() {
		return pointDelta;
	}

	public void setPointDelta(int pointDelta) {
		this.pointDelta = pointDelta;
	}

	public String getUserAccountList() {
		return userAccountList;
	}

	public void setUserAccountList(String userAccountList) {
		this.userAccountList = userAccountList;
	}
	
//	public Point copyPoint() {
//		Point result  = new Point();
//		result.setApplyUserId(this.);
//		result.setAuditState(auditState);
//		result.setAuditTime(auditTime);
//		result.setAuditUserId(auditUserId);
//		result.setDescription(description);
//		result.setId(id);
//		result.setName(name);
//		result.setPointDelta(pointDelta);
//		result.setReason(reason);
//		result.setSiteId(siteId);
//		result.setUsers(users);
//		return result;
//	}
}
