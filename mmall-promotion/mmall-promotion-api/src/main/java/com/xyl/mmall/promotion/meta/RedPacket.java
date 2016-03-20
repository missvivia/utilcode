/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.enums.DistributeRule;

/**
 * RedPacket.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@AnnonOfClass(desc = "红包", tableName = "Mmall_Promotion_RedPacket", dbCreateTimeName = "CreateTime")
public class RedPacket implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键ID", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "红包名称")
	private String name;

	@AnnonOfField(desc = "红包描述")
	private String description;

	@AnnonOfField(desc = "有效期开始时间")
	private long startTime;

	@AnnonOfField(desc = "有效期结束时间")
	private long endTime;
	
	@AnnonOfField(desc = "有效期天数", defa="1")
	private int validDay;

	@AnnonOfField(desc = "审核标记，0：初始状态，1：提交审核，2审核通过, 3拒绝，4删除")
	private int auditState;

	@AnnonOfField(desc = "申请用户id")
	private long applyUserId;

	@AnnonOfField(desc = "审核人Id")
	private long auditUserId;

	@AnnonOfField(desc = "审核时间")
	private long auditTime;

	@AnnonOfField(desc = "每个金额", defa="0.00")
	private BigDecimal cash;

	@AnnonOfField(desc = "总领个数")
	private int count;

	@AnnonOfField(desc = "分发规则", notNull=false)
	private DistributeRule distributeRule = DistributeRule.NULL;

	@AnnonOfField(desc = "可领份数")
	private int copies;

	@AnnonOfField(desc = "是否生成红包")
	private boolean produce;
	
	@AnnonOfField(desc = "使用平台")
	private int platform;
	
	/**
	 * 分享，用户领红包，否者发给指定用户
	 */
	@AnnonOfField(desc = "是否分享")
	private boolean share;
	
	@AnnonOfField(desc = "绑定类型，非分享使用", notNull=false)
	private BinderType binderType = BinderType.NULL;
	
	@AnnonOfField(desc="绑定用户，非分享使用", notNull=false)
	private String users;
	
	@AnnonOfField(desc = "审核说明", notNull=false)
	private String reason;
	
	@AnnonOfField(desc = "是否使用")
	private boolean used;
	
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

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public DistributeRule getDistributeRule() {
		return distributeRule;
	}

	public void setDistributeRule(DistributeRule distributeRule) {
		this.distributeRule = distributeRule;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public boolean isProduce() {
		return produce;
	}

	public void setProduce(boolean produce) {
		this.produce = produce;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public boolean isShare() {
		return share;
	}

	public void setShare(boolean share) {
		this.share = share;
	}

	public BinderType getBinderType() {
		return binderType;
	}

	public void setBinderType(BinderType binderType) {
		this.binderType = binderType;
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

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public int getValidDay() {
		return validDay;
	}

	public void setValidDay(int validDay) {
		this.validDay = validDay;
	}

	public boolean isValid() {
		long time = System.currentTimeMillis();
		if (time >= startTime && time < endTime) {
			return true;
		}
		return false;
	}
	
	public RedPacket cloneObject() {
		RedPacket rp = new RedPacket();
		BeanUtils.copyProperties(this, rp);
		return rp;
	}
}
