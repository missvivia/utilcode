/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ItemModel;

/**
 * ItemModelDTO.java created by yydx811 at 2015年4月30日 上午9:23:58
 * 商品模型DTO
 *
 * @author yydx811
 */
public class ItemModelDTO extends ItemModel {

	/** 序列化id. */
	private static final long serialVersionUID = -3255693334117807120L;
	
	/** 扩展属性. */
	private List<ModelParameterDTO> parameterList;
	
	/** 规格参数. */
	private List<ModelSpecificationDTO> specificationList;

	public ItemModelDTO() {
	}
	
	public ItemModelDTO(ItemModel obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public List<ModelParameterDTO> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<ModelParameterDTO> parameterList) {
		this.parameterList = parameterList;
	}

	public List<ModelSpecificationDTO> getSpecificationList() {
		return specificationList;
	}

	public void setSpecificationList(List<ModelSpecificationDTO> specificationList) {
		this.specificationList = specificationList;
	}
}
