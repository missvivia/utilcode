package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

/**
 * 商家平台的尺码模板管理中的尺码模板筛选条件类
 * 
 * @author hzhuangluqian
 *
 */
public class SizeTemplateSearchParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3619447416158988616L;

	/** 用户id */
	private long supplierId;

	/** 选择的最低级类目 */
	private long lowCategoryId;

	/** 尺码模板名称 */
	private String sizeTemplateName;

	/** 最近修改的开始时间 */
	private long stime;

	/** 最近修改的结束时间 */
	private long etime;

	/** 尺码模板id */
	private long sizeTemplateId;

	/** 搜索个数 */
	private int limit;

	/** 上一页最后一个id */
	private long lastId;

	private int offset;
	
	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
	}

	public String getSizeTemplateName() {
		return sizeTemplateName;
	}

	public void setSizeTemplateName(String sizeTemplateName) {
		this.sizeTemplateName = sizeTemplateName;
	}

	public long getStime() {
		return stime;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public long getEtime() {
		return etime;
	}

	public void setEtime(long etime) {
		this.etime = etime;
	}

	public long getSizeTemplateId() {
		return sizeTemplateId;
	}

	public void setSizeTemplateId(long templateId) {
		this.sizeTemplateId = templateId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public long getLastId() {
		return lastId;
	}

	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
