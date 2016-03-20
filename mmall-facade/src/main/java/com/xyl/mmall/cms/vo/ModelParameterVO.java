/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;

/**
 * ModelParameterVO.java created by yydx811 at 2015年4月30日 上午10:40:57
 * 模型属性VO
 *
 * @author yydx811
 */
public class ModelParameterVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 6414247085024034980L;

	/** 扩展属性id. */
	private long parameterId;

	/** 商品模型id. */
	private long itemModelId;

	/** 属性名. */
	private String parameterName;

	/** 操作样式,单选1,多选2. */
	private int single;

	/** 是否作为筛选项,1是,2不是. */
	private int show;

	/** 操作人. */
	private String operator;
	
	/** 选项列表. */
	private List<ModelParamOptionVO> optionList;
	
	public ModelParameterVO() {
	}

	public ModelParameterVO(ModelParameterDTO obj) {
		this.parameterId = obj.getId();
		this.itemModelId = obj.getModelId();
		this.parameterName = obj.getName();
		this.single = obj.getIsSingle();
		this.show = obj.getIsShow();
		if (!CollectionUtils.isEmpty(obj.getModelParamOptionList())) {
			this.optionList = new ArrayList<ModelParamOptionVO>(obj.getModelParamOptionList().size());
			for (ModelParamOptionDTO optionDTO : obj.getModelParamOptionList()) {
				this.optionList.add(new ModelParamOptionVO(optionDTO));
			}
		}
	}
	
	public ModelParameterDTO convertToDTO() {
		ModelParameterDTO parameterDTO = new ModelParameterDTO();
		parameterDTO.setId(parameterId);
		parameterDTO.setModelId(itemModelId);
		parameterDTO.setName(parameterName);
		parameterDTO.setIsShow(show);
		parameterDTO.setIsSingle(single);
		return parameterDTO;
	}
	
	public long getParameterId() {
		return parameterId;
	}

	public void setParameterId(long parameterId) {
		this.parameterId = parameterId;
	}

	public long getItemModelId() {
		return itemModelId;
	}

	public void setItemModelId(long itemModelId) {
		this.itemModelId = itemModelId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public int getSingle() {
		return single;
	}

	public void setSingle(int single) {
		this.single = single;
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

	public List<ModelParamOptionVO> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<ModelParamOptionVO> optionList) {
		this.optionList = optionList;
	}
}
