/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.itemcenter.dto.ItemModelDTO;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;

/**
 * ItemModelVO.java created by yydx811 at 2015年4月29日 下午8:16:07
 * 商品模型
 *
 * @author yydx811
 */
public class ItemModelVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -3291225700559537999L;

	/** 商品模版id. */
	private long modelId;
	
	/** 商品模版名称. */
	private String modelName;
	
	/** 商品分类id. */
	private long categoryNormalId;
	
	/** 商品分类名. */
	private String categoryNormalName;

	/** 操作人. */
	private String operator;
	
	/** 更新时间. */
	private Timestamp updateTime;
	
	/** 属性列表. */
	private List<ModelParameterVO> parameterList;
	
	/** 规格列表. */
	private List<ModelSpecificationVO> specificationList;
	
	public ItemModelVO() {
	}

	public ItemModelVO(ItemModelDTO obj) {
		this.modelId = obj.getId();
		this.modelName = obj.getName();
		this.categoryNormalId = obj.getCategoryNormalId();
		this.updateTime = obj.getUpdateTime();
		if (!CollectionUtils.isEmpty(obj.getParameterList())) {
			this.parameterList = new ArrayList<ModelParameterVO>(obj.getParameterList().size());
			for (ModelParameterDTO parameterDTO : obj.getParameterList()) {
				this.parameterList.add(new ModelParameterVO(parameterDTO));
			}
		}
		if (!CollectionUtils.isEmpty(obj.getSpecificationList())) {
			this.specificationList = new ArrayList<ModelSpecificationVO>(obj.getParameterList().size());
			for (ModelSpecificationDTO specificationDTO : obj.getSpecificationList()) {
				this.specificationList.add(new ModelSpecificationVO(specificationDTO));
			}
		}
	}
	
	public ItemModelDTO convertToDTO() {
		ItemModelDTO modelDTO = new ItemModelDTO();
		modelDTO.setId(modelId);
		modelDTO.setName(modelName);
		modelDTO.setCategoryNormalId(categoryNormalId);
		return modelDTO;
	}
	
	public long getModelId() {
		return modelId;
	}

	public void setModelId(long modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public long getCategoryNormalId() {
		return categoryNormalId;
	}

	public void setCategoryNormalId(long categoryNormalId) {
		this.categoryNormalId = categoryNormalId;
	}

	public String getCategoryNormalName() {
		return categoryNormalName;
	}

	public void setCategoryNormalName(String categoryNormalName) {
		this.categoryNormalName = categoryNormalName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<ModelParameterVO> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<ModelParameterVO> parameterList) {
		this.parameterList = parameterList;
	}

	public List<ModelSpecificationVO> getSpecificationList() {
		return specificationList;
	}

	public void setSpecificationList(List<ModelSpecificationVO> specificationList) {
		this.specificationList = specificationList;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}
