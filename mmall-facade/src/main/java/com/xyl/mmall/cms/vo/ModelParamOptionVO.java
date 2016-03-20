/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;

/**
 * ModelParamOptionVO.java created by yydx811 at 2015年5月4日 上午9:57:47
 * 模型属性选项VO
 *
 * @author yydx811
 */
public class ModelParamOptionVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -7208880030512325499L;

	/** 模型id. */
	private long itemModelId;

	/** 选项id. */
	private long paramOptionId;
	
	/** 属性id. */
	private long modelParameterId;
	
	/** 选项值. */
	private String paramOption;
	
	/** 操作人. */
	private String operator;

	public long getParamOptionId() {
		return paramOptionId;
	}
	
	public ModelParamOptionVO() {
	}

	public ModelParamOptionVO(ModelParamOptionDTO obj) {
		this.paramOptionId = obj.getId();
		this.modelParameterId = obj.getParameterId();
		this.paramOption = obj.getOptionValue();
	}
	
	public ModelParamOptionDTO convertToDTO() {
		ModelParamOptionDTO optionDTO = new ModelParamOptionDTO();
		optionDTO.setId(paramOptionId);
		optionDTO.setParameterId(modelParameterId);
		optionDTO.setOptionValue(paramOption);
		return optionDTO;
	}
	
	public long getItemModelId() {
		return itemModelId;
	}

	public void setItemModelId(long itemModelId) {
		this.itemModelId = itemModelId;
	}

	public void setParamOptionId(long paramOptionId) {
		this.paramOptionId = paramOptionId;
	}

	public long getModelParameterId() {
		return modelParameterId;
	}

	public void setModelParameterId(long modelParameterId) {
		this.modelParameterId = modelParameterId;
	}

	public String getParamOption() {
		return paramOption;
	}

	public void setParamOption(String paramOption) {
		this.paramOption = paramOption;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
