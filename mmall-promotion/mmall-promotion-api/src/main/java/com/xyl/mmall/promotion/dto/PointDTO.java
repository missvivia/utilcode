/**
 * 
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;

import com.xyl.mmall.promotion.enums.AuditState;
import com.xyl.mmall.promotion.meta.Point;

/**
 * @author jmy
 *
 */
public class PointDTO implements Serializable {

	/**
	 * 
	 */
	private static final Long serialVersionUID = -7793715256622215893L;

	private Long id;

	private Long applyUserId;

	private Integer auditState;

	private Long auditTime;

	private Long auditUserId;

	private Long siteId;

	private String description;

	private String name;

	private String reason;

	private Integer pointDelta;

	private String users;
	
	//以下是不同于Point类的域
	
	/**
	 * 搜索关键字
	 */
	private String searchKey;
	/**
	 * 1：申请调整；0：审核
	 */
	private int apply = 1;
	
	public PointDTO() {
		
	}
	
	public PointDTO(Point point) {
//		ReflectUtil.convertObj(this, point, false);//低效
		this.id = point.getId();
		this.applyUserId = point.getApplyUserId();
		this.auditState = point.getAuditState();
		this.auditTime = point.getAuditTime();
		this.auditUserId = point.getAuditUserId();
		this.siteId = point.getSiteId();
		this.description = point.getDescription();
		this.name = point.getName();
		this.reason = point.getReason();
		this.pointDelta = point.getPointDelta();
		this.users = point.getUsers();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Long auditTime) {
		this.auditTime = auditTime;
	}

	public Long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(Long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
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

	public Integer getPointDelta() {
		return pointDelta;
	}

	public void setPointDelta(Integer pointDelta) {
		this.pointDelta = pointDelta;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public int getApply() {
		return apply;
	}

	public void setApply(int apply) {
		this.apply = apply;
	}

}
