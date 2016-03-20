/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.promotion.enums.PlatformType;

/**
 * Promotion.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@AnnonOfClass(desc = "活动", tableName = "Mmall_Promotion_Promotion", dbCreateTimeName = "CreateTime")
public class Promotion implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键ID", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "省份站点")
	private String selectedProvince;

	@AnnonOfField(desc = "活动名称")
	private String name;

	@AnnonOfField(desc = "活动描述")
	private String description;

	@AnnonOfField(desc = "有效期开始时间")
	private long startTime;

	@AnnonOfField(desc = "有效期结束时间")
	private long endTime;

	@AnnonOfField(desc = "促销内容", type = "TEXT")
	private String items;

	@AnnonOfField(desc = "审核标记，0：初始状态，1：提交审核，2审核通过, 3拒绝，4删除")
	private int auditState;

	@AnnonOfField(desc = "申请用户id")
	private long applyUserId;

	@AnnonOfField(desc = "活动标签", notNull = false, type = "text")
	private String labels;

	@AnnonOfField(desc = "审核人Id")
	private long auditUserId;

	@AnnonOfField(desc = "审核时间")
	private long auditTime;

	@AnnonOfField(desc = "审核说明", notNull = false, type = "varchar(200)")
	private String reason;

	@AnnonOfField(desc = "参与的PO，多个用逗号分隔", type = "varchar(512)", notNull = false)
	private String declarePO;

	/**
	 * 使用终端，预留字段
	 */
	@AnnonOfField(desc = "使用平台", hasDefault = true)
	private PlatformType platformType = PlatformType.ALL;

	@AnnonOfField(desc = "优惠类型")
	private int favorType;

	@AnnonOfField(desc = "省份权限")
	private long areaPermission;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSelectedProvince() {
		return selectedProvince;
	}

	public void setSelectedProvince(String selectedProvince) {
		this.selectedProvince = selectedProvince;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(long auditTime) {
		this.auditTime = auditTime;
	}

	public String getDeclarePO() {
		return declarePO;
	}

	public void setDeclarePO(String declarePO) {
		this.declarePO = declarePO;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}

	public int getFavorType() {
		return favorType;
	}

	public void setFavorType(int favorType) {
		this.favorType = favorType;
	}

	public long getAreaPermission() {
		return areaPermission;
	}

	public void setAreaPermission(long areaPermission) {
		this.areaPermission = areaPermission;
	}

	public Promotion cloneObject() {
		Promotion p = new Promotion();
		BeanUtils.copyProperties(this, p);
		return p;
	}
}
