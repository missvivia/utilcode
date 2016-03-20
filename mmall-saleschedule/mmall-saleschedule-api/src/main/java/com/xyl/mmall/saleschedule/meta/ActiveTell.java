package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 活动通知表
 * 
 * @author hzzhaozhenzuo
 *
 */
@AnnonOfClass(desc = "活动通知表", tableName = "Mmall_Active_Tell", policy = "areaId")
public class ActiveTell implements Serializable {

	private static final long serialVersionUID = 20141018L;

	@AnnonOfField(desc = "活动通知表主键", primary = true, autoAllocateId = true)
	private long id;

	// 活动通知业务类型,0:品牌通知,1:活动通知
	@AnnonOfField(desc = "当前通知业务类型")
	private int tellActiveType;

	// 对于品牌通知为品牌id，对于活动通知则为用户选择想要通知的哪一天
	@AnnonOfField(desc = "需要通知的业务id")
	private long tellActiveId;

	@AnnonOfField(desc = "对于品牌通知为品牌名称", notNull = false)
	private String tellActiveTitle;

	// 对于活动类型通知为用户关心的某一天的全局活动开始时间,品牌通知不填
	@AnnonOfField(desc = "用户关心的某一天的活动开始时间")
	private long activeBeginTime;

	@AnnonOfField(desc = "用户在填写通知时所在的区域id", unsigned=false, policy = true)
	private long areaId;

	// 手机通知或邮件通知,0:手机,1邮件
	@AnnonOfField(desc = "手机通知或邮件通知")
	private int tellTargetType;

	@AnnonOfField(desc = "要发送的手机或者邮箱")
	private String tellTargetValue;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "预留字段1",notNull=false)
	private String udf1;

	@AnnonOfField(desc = "预留字段2",notNull=false)
	private long udf2;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTellActiveType() {
		return tellActiveType;
	}

	public void setTellActiveType(int tellActiveType) {
		this.tellActiveType = tellActiveType;
	}

	public long getTellActiveId() {
		return tellActiveId;
	}

	public void setTellActiveId(long tellActiveId) {
		this.tellActiveId = tellActiveId;
	}

	public String getTellActiveTitle() {
		return tellActiveTitle;
	}

	public void setTellActiveTitle(String tellActiveTitle) {
		this.tellActiveTitle = tellActiveTitle;
	}

	public long getActiveBeginTime() {
		return activeBeginTime;
	}

	public void setActiveBeginTime(long activeBeginTime) {
		this.activeBeginTime = activeBeginTime;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public int getTellTargetType() {
		return tellTargetType;
	}

	public void setTellTargetType(int tellTargetType) {
		this.tellTargetType = tellTargetType;
	}

	public String getTellTargetValue() {
		return tellTargetValue;
	}

	public void setTellTargetValue(String tellTargetValue) {
		this.tellTargetValue = tellTargetValue;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getUdf1() {
		return udf1;
	}

	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}

	public long getUdf2() {
		return udf2;
	}

	public void setUdf2(long udf2) {
		this.udf2 = udf2;
	}
}
