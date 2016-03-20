/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;

/**
 * ModelSpecificationVO.java created by yydx811 at 2015年5月4日 下午5:12:29
 * 商品模型规格VO
 *
 * @author yydx811
 */
public class ModelSpecificationVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -2338911984249364373L;

	/** 商品规格id. */
	private long specificationId;

	/** 商品模型id. */
	private long itemModelId;

	/** 规格名称. */
	private String specificationName;

	/** 显示类型，1文字，2图片. */
	private int specificationType;

	/** 备注. */
	private String note;

	/** 是否作为筛选项,1是,2不是. */
	private int show;

	/** 操作人. */
	private String operator;

	/** 规格选项列表. */
	private List<ModelSpeciOptionVO> speciOptionList;

	public ModelSpecificationVO() {
	}

	public ModelSpecificationVO(ModelSpecificationDTO obj) {
		this.specificationId = obj.getId();
		this.itemModelId = obj.getModelId();
		this.specificationName = obj.getName();
		this.specificationType = obj.getType();
		this.note = obj.getRemark();
		this.show = obj.getIsShow();
		if (!CollectionUtils.isEmpty(obj.getSpeciOptionList())) {
			this.speciOptionList = new ArrayList<ModelSpeciOptionVO>(obj.getSpeciOptionList().size());
			for (ModelSpeciOptionDTO optionDTO : obj.getSpeciOptionList()) {
				this.speciOptionList.add(new ModelSpeciOptionVO(optionDTO));
			}
		}
	}
	
	public ModelSpecificationDTO convertToDTO() {
		ModelSpecificationDTO specificationDTO = new ModelSpecificationDTO();
		specificationDTO.setId(specificationId);
		specificationDTO.setModelId(itemModelId);
		specificationDTO.setIsShow(show);
		specificationDTO.setName(specificationName);
		specificationDTO.setRemark(note);
		specificationDTO.setType(specificationType);
		return specificationDTO;
	}
	
	public long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(long specificationId) {
		this.specificationId = specificationId;
	}

	public long getItemModelId() {
		return itemModelId;
	}

	public void setItemModelId(long itemModelId) {
		this.itemModelId = itemModelId;
	}

	public String getSpecificationName() {
		return specificationName;
	}

	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}

	public int getSpecificationType() {
		return specificationType;
	}

	public void setSpecificationType(int specificationType) {
		this.specificationType = specificationType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<ModelSpeciOptionVO> getSpeciOptionList() {
		return speciOptionList;
	}

	public void setSpeciOptionList(List<ModelSpeciOptionVO> speciOptionList) {
		this.speciOptionList = speciOptionList;
	}
}
