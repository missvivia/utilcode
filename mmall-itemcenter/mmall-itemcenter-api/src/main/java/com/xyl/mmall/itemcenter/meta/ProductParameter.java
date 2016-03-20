package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.enums.ProdDetailType;

/**
 * 商品参数表
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_ProductParameter", desc = "商品参数表")
public class ProductParameter implements Serializable {

	private static final long serialVersionUID = 4100908207552550163L;

	/** 主键，商品参数id */
	@AnnonOfField(desc = "主键，商品参数id", primary = true, primaryIndex = 1, autoAllocateId = true, policy = true)
	private long id;

	/** 商品参数名称 */
	@AnnonOfField(desc = "商品参数名称")
	private String name;

	/** 显示次序 */
	@AnnonOfField(desc = "填写内容提示文字")
	private String text;

	@AnnonOfField(desc = "参数类别")
	private ProdDetailType detailType;

	@AnnonOfField(desc = "是否必填")
	private boolean isRequired;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProdDetailType getDetailType() {
		return detailType;
	}

	public void setDetailType(ProdDetailType detailType) {
		this.detailType = detailType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean getIsRequired() {
		return isRequired;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

}
