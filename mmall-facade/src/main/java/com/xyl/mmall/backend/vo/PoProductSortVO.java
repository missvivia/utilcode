package com.xyl.mmall.backend.vo;

import java.util.List;

/**
 * 档期商品排序表单
 * 
 * @author hzhuangluqian
 *
 */
public class PoProductSortVO {
	/** 档期id */
	private long scheduleId;

	/** 排序类型 */
	private int type;

	/** 排序序列 */
	private List<Long> seq;

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Long> getSeq() {
		return seq;
	}

	public void setSeq(List<Long> seq) {
		this.seq = seq;
	}
}
