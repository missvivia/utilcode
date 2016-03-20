/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;

/**
 * ModelSpeciOptionVO.java created by yydx811 at 2015年5月4日 下午5:13:22
 * 规格选项VO
 *
 * @author yydx811
 */
public class ModelSpeciOptionVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -6541416193749616081L;

	/** 模型id. */
	private long itemModelId;
	
	/** 规格选项值id. */
	private long speciOptionId;

	/** 商品规格id. */
	private long specificationId;
	
	/** 显示类型，1文字，2图片. */
	private int speciOptionType;

	/** 显示顺序. */
	private int index;

	/** 选项值. */
	private String speciOption;

	/** 操作人. */
	private String operator;

	public ModelSpeciOptionVO() {
	}

	public ModelSpeciOptionVO(ModelSpeciOptionDTO obj) {
		this.speciOptionId = obj.getId();
		this.specificationId = obj.getSpecificationId();
		this.speciOptionType = obj.getType();
		this.index = obj.getShowIndex();
		this.speciOption = obj.getOptionValue();
	}
	
	public ModelSpeciOptionDTO convertToDTO() {
		ModelSpeciOptionDTO optionDTO = new ModelSpeciOptionDTO();
		optionDTO.setId(speciOptionId);
		optionDTO.setSpecificationId(specificationId);
		optionDTO.setType(speciOptionType);
		optionDTO.setShowIndex(index);
		optionDTO.setOptionValue(speciOption);
		return optionDTO;
	}
	
	public long getItemModelId() {
		return itemModelId;
	}

	public void setItemModelId(long itemModelId) {
		this.itemModelId = itemModelId;
	}
	
	public long getSpeciOptionId() {
		return speciOptionId;
	}

	public void setSpeciOptionId(long speciOptionId) {
		this.speciOptionId = speciOptionId;
	}

	public long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(long specificationId) {
		this.specificationId = specificationId;
	}

	public int getSpeciOptionType() {
		return speciOptionType;
	}

	public void setSpeciOptionType(int speciOptionType) {
		this.speciOptionType = speciOptionType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getSpeciOption() {
		return speciOption;
	}

	public void setSpeciOption(String speciOption) {
		this.speciOption = speciOption;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
