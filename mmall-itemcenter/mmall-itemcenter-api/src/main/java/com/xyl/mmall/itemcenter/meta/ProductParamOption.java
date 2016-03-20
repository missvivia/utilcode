package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(tableName = "Mmall_ItemCenter_ProductParamOption", desc = "商品参数选项表")
public class ProductParamOption implements Serializable {

	private static final long serialVersionUID = 6123892524413016883L;

	/** 主键，商品参数下拉菜单选项id */
	@AnnonOfField(desc = "主键，商品参数下拉菜单选项id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/** 外键键，所属商品参数id */
	@AnnonOfField(desc = "外键键，所属商品参数id", policy = true)
	private long prodParamId;

	/** 该选项显示的内容 */
	@AnnonOfField(desc = "该选项显示的内容")
	private String value;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProdParamId() {
		return prodParamId;
	}

	public void setProdParamId(long prodParamId) {
		this.prodParamId = prodParamId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
