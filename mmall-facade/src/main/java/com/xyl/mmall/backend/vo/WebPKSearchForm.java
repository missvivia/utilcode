package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.oms.enums.JITFlagType;

/**
 * 拣货单查询表单对象
 * 
 * @author zzj(hzzhangzhoujie@corp.netease.com)
 * 
 */
public class WebPKSearchForm implements Serializable{
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 8725300658002945477L;
	/**
	 * 拣货单编号
	 */
	private String pickOrderId;
	/**
	 * po单编号
	 */
	private String poOrderId;
	/**
	 * 拣货单创建开始时间
	 */
	private String createStartTime;
	/**
	 * 拣货单创建结束时间
	 */
	private String createEndTime;
	/**
	 * 导出状态（1：未导出，2:已导出）
	 */
	private int exportStatus;
	/**
	 * 档期开始时间（起始）
	 */
	private String commodityStartTimeOfStart;
	/**
	 * 档期开始时间（结束）
	 */
	private String commodityStartTimeOfEnd;
	/**
	 * 档期结束时间（起始）
	 */
	private String commodityEndTimeOfStart;
	/**
	 * 档期结束时间（结束）
	 */
	private String commodityEndTimeOfEnd;
	
	/**
	 * 是否jit
	 */
	private JITFlagType jitFlagType;
	
	private int limit;
	
	private int offset;
	
	public String getPickOrderId() {
		return pickOrderId;
	}
	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}
	public String getPoOrderId() {
		return poOrderId;
	}
	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}
	public String getCreateStartTime() {
		return createStartTime;
	}
	public void setCreateStartTime(String createStartTime) {
		this.createStartTime = createStartTime;
	}
	public String getCreateEndTime() {
		return createEndTime;
	}
	public void setCreateEndTime(String createEndTime) {
		this.createEndTime = createEndTime;
	}
	public int getExportStatus() {
		return exportStatus;
	}
	public void setExportStatus(int exportStatus) {
		this.exportStatus = exportStatus;
	}
	public String getCommodityStartTimeOfStart() {
		return commodityStartTimeOfStart;
	}
	public void setCommodityStartTimeOfStart(String commodityStartTimeOfStart) {
		this.commodityStartTimeOfStart = commodityStartTimeOfStart;
	}
	public String getCommodityStartTimeOfEnd() {
		return commodityStartTimeOfEnd;
	}
	public void setCommodityStartTimeOfEnd(String commodityStartTimeOfEnd) {
		this.commodityStartTimeOfEnd = commodityStartTimeOfEnd;
	}
	public String getCommodityEndTimeOfStart() {
		return commodityEndTimeOfStart;
	}
	public void setCommodityEndTimeOfStart(String commodityEndTimeOfStart) {
		this.commodityEndTimeOfStart = commodityEndTimeOfStart;
	}
	public String getCommodityEndTimeOfEnd() {
		return commodityEndTimeOfEnd;
	}
	public void setCommodityEndTimeOfEnd(String commodityEndTimeOfEnd) {
		this.commodityEndTimeOfEnd = commodityEndTimeOfEnd;
	}
	public JITFlagType getJitFlagType() {
		return jitFlagType;
	}
	public void setJitFlagType(JITFlagType jitFlagType) {
		this.jitFlagType = jitFlagType;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
