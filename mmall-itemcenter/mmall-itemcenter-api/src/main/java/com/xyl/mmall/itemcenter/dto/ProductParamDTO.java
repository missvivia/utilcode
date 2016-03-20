package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ProductParamOption;
import com.xyl.mmall.itemcenter.meta.ProductParameter;

/**
 * 商品参数详情dto，增加参数值，和下拉选项列表
 * 
 * @author hzhuangluqian
 *
 */
public class ProductParamDTO extends ProductParameter implements Serializable {

	/** 商品参数值 */
	private String paramValue;

	/** 商品参数下拉列表 */
	private List<ProductParamOption> optionList;

	public ProductParamDTO() {

	}

	public ProductParamDTO(ProductParameter obj) {
		this.setId(obj.getId());
		this.setDetailType(obj.getDetailType());
		this.setIsRequired(obj.getIsRequired());
		this.setName(obj.getName());
		this.setText(obj.getText());
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public List<ProductParamOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<ProductParamOption> optionList) {
		this.optionList = optionList;
	}

}
