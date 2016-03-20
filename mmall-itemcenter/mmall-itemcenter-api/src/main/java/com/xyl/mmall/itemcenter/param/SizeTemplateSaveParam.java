package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 商家平台尺码模板管理中的添加尺码模板参数类
 * 
 * @author hzhuangluqian
 *
 */
public class SizeTemplateSaveParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8999913693898961085L;

	private long id;

	/** 所属的商家id */
	private long supplierId;

	/** 最低的类目id */
	private long lowCategoryId;

	/** 尺寸模板名 */
	private String templateName;

	/** 尺码提示 */
	private String remindText;

	/** 最后修改时间 */
	private long lastModifyTime;

	/** 尺码模板详情表格 */
	SizeTmplTable sizeTable;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getRemindText() {
		return remindText;
	}

	public void setRemindText(String remindText) {
		this.remindText = remindText;
	}


	public long getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public SizeTmplTable getSizeTable() {
		return sizeTable;
	}

	public void setSizeTable(SizeTmplTable sizeTable) {
		this.sizeTable = sizeTable;
	}
}
