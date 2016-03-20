package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.intf.SizeValue;

@AnnonOfClass(tableName = "Mmall_ItemCenter_CustomizedSizeValue", desc = "自定义尺码字段值表")
public class CustomizedSizeValue implements Serializable, SizeValue {

	private static final long serialVersionUID = -7448807202921557806L;

	@AnnonOfField(desc = "主键", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/** 商品id */
	@AnnonOfField(desc = "商品id", policy = true)
	private long productId;

	/** 字段id */
	@AnnonOfField(desc = "尺码字段id")
	private long columnId;

	/** 表示第几条记录 */
	@AnnonOfField(desc = "表示第几条记录")
	private long recordIndex;

	/** 是否是挂在PO下 */
	@AnnonOfField(desc = "是否是挂在PO下")
	private int isInPo = 0;

	/** 字段值 */
	@AnnonOfField(desc = "尺码字段的值", type = "VARCHAR(12)")
	private String value;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public long getTemplateKey() {
		return productId;
	}

	@Override
	public void setTemplateKey(long templatekey) {
		this.productId = templatekey;

	}

	@Override
	public long getRecordIndex() {
		return recordIndex;
	}

	@Override
	public void setRecordIndex(long recordIndex) {
		this.recordIndex = recordIndex;
	}

	public int getIsInPo() {
		return isInPo;
	}

	public void setIsInPo(int isInPo) {
		this.isInPo = isInPo;
	}

}
