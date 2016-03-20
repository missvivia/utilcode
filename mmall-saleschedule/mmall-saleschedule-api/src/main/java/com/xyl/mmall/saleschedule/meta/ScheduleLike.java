package com.xyl.mmall.saleschedule.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.enums.ScheduleLikeState;

/**
 * PO收藏
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "档期频道常量表", tableName = "Mmall_SaleSchedule_ScheduleLike", policy = "userId")
public class ScheduleLike implements java.io.Serializable {
	
	private static final long serialVersionUID = -1020864327915413661L;

	@AnnonOfField(desc = "主键id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户id")
	private long userId;

	@AnnonOfField(desc = "档期id")
	private long scheduleId;

	@AnnonOfField(desc = "创建时间")
	private long createDate;

	@AnnonOfField(desc = "状态")
	private ScheduleLikeState status;

	@AnnonOfField(desc = "状态更新时间")
	private long statusUpdateDate;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public ScheduleLikeState getStatus() {
		return status;
	}

	public void setStatus(ScheduleLikeState status) {
		this.status = status;
	}

	public long getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(long statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
