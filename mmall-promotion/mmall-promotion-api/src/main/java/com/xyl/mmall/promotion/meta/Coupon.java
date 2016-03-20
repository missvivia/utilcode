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
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.enums.CodeType;
import com.xyl.mmall.promotion.enums.TimesType;

/**
 * Coupon.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@AnnonOfClass(desc = "优惠券", tableName = "Mmall_Promotion_Coupon", dbCreateTimeName = "CreateTime")
public class Coupon implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键ID", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "优惠券code", uniqueKey = true, policy = true)
	private String couponCode;

	@AnnonOfField(desc = "优惠券简称")
	private String name;

	@AnnonOfField(desc = "优惠券名称")
	private String description;

	@AnnonOfField(desc = "code类型")
	private CodeType codeType;

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

	@AnnonOfField(desc = "审核人Id")
	private long auditUserId;

	@AnnonOfField(desc = "审核时间")
	private long auditTime;

	@AnnonOfField(desc = "次数类型")
	private TimesType timesType;

	@AnnonOfField(desc = "次数")
	private int times;

	@AnnonOfField(desc = "随机数")
	private int randomCount;

	@AnnonOfField(desc = "审核说明", notNull = false, type = "varchar(200)")
	private String reason;

	@AnnonOfField(desc = "绑定用户", notNull = false, type = "text")
	private String users;

	@AnnonOfField(desc = "绑定类型")
	private BinderType binderType;

	@AnnonOfField(desc = "母券，随机券时有值", notNull = false)
	private String rootCode;

	@AnnonOfField(desc = "优惠类型")
	private int favorType;

	@AnnonOfField(desc = "区域ids")
	private String AreaIds;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CodeType getCodeType() {
		return codeType;
	}

	public void setCodeType(CodeType codeType) {
		this.codeType = codeType;
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

	public TimesType getTimesType() {
		return timesType;
	}

	public void setTimesType(TimesType timesType) {
		this.timesType = timesType;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getRandomCount() {
		return randomCount;
	}

	public void setRandomCount(int randomCount) {
		this.randomCount = randomCount;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public BinderType getBinderType() {
		return binderType;
	}

	public void setBinderType(BinderType binderType) {
		this.binderType = binderType;
	}

	public String getRootCode() {
		return rootCode;
	}

	public void setRootCode(String rootCode) {
		this.rootCode = rootCode;
	}

	public int getFavorType() {
		return favorType;
	}

	public void setFavorType(int favorType) {
		this.favorType = favorType;
	}

	public boolean isValid() {
		long time = System.currentTimeMillis();
		if (time >= startTime && time < endTime) {
			return true;
		}
		return false;
	}

	public String getAreaIds() {
		return AreaIds;
	}

	public void setAreaIds(String areaIds) {
		AreaIds = areaIds;
	}

	public Coupon cloneObject() {
		Coupon c = new Coupon();
		BeanUtils.copyProperties(this, c);
		return c;
	}
}
