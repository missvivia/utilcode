package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.intf.Size;

/**
 * 自定义尺码的字段集合类
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_CustomizedSize", desc = "自定义尺码字段表")
public class CustomizedSize implements Serializable, Size {

	private static final long serialVersionUID = -4132784170884724183L;

	@AnnonOfField(desc = "主键", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long Id;

	/** 商品id */
	@AnnonOfField(desc = "商品id", policy = true)
	private long productId;

	/** 字段id */
	@AnnonOfField(desc = "尺码字段id")
	private long columnId;

	/** 是否是挂在PO下，只读的 */
	@AnnonOfField(desc = "是否是挂在PO下")
	private int isInPo = 0;

	/** 字段次序 */
	@AnnonOfField(desc = "字段次序")
	private long colIndex;

	/** 是否是必填项 */
	@AnnonOfField(desc = "是否是必填项 ")
	private boolean isRequired;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getColumnId() {
		return columnId;
	}

	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}

	public int getIsInPo() {
		return isInPo;
	}

	public void setIsInPo(int isInPo) {
		this.isInPo = isInPo;
	}

	public long getColIndex() {
		return colIndex;
	}

	public void setColIndex(long colIndex) {
		this.colIndex = colIndex;
	}

	public boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean isRequired() {
		return isRequired;
	}

	@Override
	public long getTemplateKey() {
		return productId;
	}

	@Override
	public void setTemplateKey(long templateKey) {
		this.productId = templateKey;
	}

}
