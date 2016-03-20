package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * 尺码模板管理中的尺码搜索VO
 * 
 * @author hzhuangluqian
 *
 */
public class SizeTmplSearchVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971880031075524796L;

	private long supplierId;
	
	/** 最低类目id */
	private long lowCategoryId;

	/** 尺码模板名 */
	private String sizeTemplateName;

	/** 尺码模板id */
	private long sizeTemplateId;

	/** 最后编辑开始时间 */
	private long etime;

	/** 最后编辑结束时间 */
	private long stime;

	private int limit;
	
	private long lastId;
	
	private int offset;
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

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

	public long getSizeTemplateId() {
		return sizeTemplateId;
	}

	public void setSizeTemplateId(long sizeTemplateId) {
		this.sizeTemplateId = sizeTemplateId;
	}

	public long getEtime() {
		return etime;
	}

	public void setEtime(long etime) {
		this.etime = etime;
	}

	public long getStime() {
		return stime;
	}

	public void setStime(long stime) {
		this.stime = stime;
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

}
