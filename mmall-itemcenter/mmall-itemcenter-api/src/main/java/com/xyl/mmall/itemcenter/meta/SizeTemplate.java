package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 尺码模板 meta类<br>
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_SizeTemplate", desc = "尺码模板表")
public class SizeTemplate implements Serializable {

	private static final long serialVersionUID = -8441122712792466350L;

	/** 主键id */
	@AnnonOfField(desc = "主键id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/** 所属的商家id */
	@AnnonOfField(desc = "所属的商家id", policy = true)
	private long supplierId;

	/** 最低的类目id */
	@AnnonOfField(desc = "最低层类目id")
	private long lowCategoryId;

	/** 尺寸模板名 */
	@AnnonOfField(desc = "尺寸模板名")
	private String templateName;

	/** 尺码提示 */
	@AnnonOfField(desc = "尺码提示")
	private String remindText;

	/** 最后修改时间 */
	@AnnonOfField(desc = "最后修改时间")
	private long lastModifyTime;

	@AnnonOfField(desc = "是否在PO")
	private int isInPo = 0;

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

	public long getLowCategoryId() {
		return lowCategoryId;
	}

	public void setLowCategoryId(long lowCategoryId) {
		this.lowCategoryId = lowCategoryId;
	}

	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public int getIsInPo() {
		return isInPo;
	}

	public void setIsInPo(int isInPo) {
		this.isInPo = isInPo;
	}

}
