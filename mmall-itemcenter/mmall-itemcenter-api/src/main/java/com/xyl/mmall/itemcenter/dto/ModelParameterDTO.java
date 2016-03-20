/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ModelParameter;

/**
 * ModelParameterDTO.java created by yydx811 at 2015年4月30日 上午10:38:44
 * 商品模型属性DTO
 *
 * @author yydx811
 */
public class ModelParameterDTO extends ModelParameter {

	/** 序列化id. */
	private static final long serialVersionUID = 6868786526100213028L;

	/** 属性值列表. */
	private List<ModelParamOptionDTO> modelParamOptionList;
	
	public ModelParameterDTO() {
	}
	
	public ModelParameterDTO(ModelParameter obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public List<ModelParamOptionDTO> getModelParamOptionList() {
		return modelParamOptionList;
	}

	public void setModelParamOptionList(List<ModelParamOptionDTO> modelParamOptionList) {
		this.modelParamOptionList = modelParamOptionList;
	}
}
