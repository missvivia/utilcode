package com.xyl.mmall.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(desc = "任务执行历史记录", tableName = "Mmall_Job_History")
public class JobHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "主键", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "每次定时器产生的job任务的唯一值")
	private String jobUniqueId;

	@AnnonOfField(desc = "job类型id")
	private long jobTypeId;

	@AnnonOfField(desc = "job类型名称", notNull = false)
	private String jobTypeName;

	@AnnonOfField(desc = "job处理状态,0:处理中,1:处理成功,2:处理失败")
	private int processStatus;

	@AnnonOfField(desc = "job产生时间")
	private long generateTime;

	@AnnonOfField(desc = "job处理时间")
	private long processTime;

	@AnnonOfField(desc = "job运行时间", notNull = false)
	private long costTime;

	@AnnonOfField(desc = "记录创建时间")
	private long createTime;

	@AnnonOfField(desc = "记录更新时间", notNull = false)
	private long updateTime;

	@AnnonOfField(desc = "错误描述", notNull = false)
	private String errorDesc;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getJobUniqueId() {
		return jobUniqueId;
	}

	public void setJobUniqueId(String jobUniqueId) {
		this.jobUniqueId = jobUniqueId;
	}

	public long getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(long jobTypeId) {
		this.jobTypeId = jobTypeId;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public int getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}

	public long getGenerateTime() {
		return generateTime;
	}

	public void setGenerateTime(long generateTime) {
		this.generateTime = generateTime;
	}

	public long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(long processTime) {
		this.processTime = processTime;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
}
