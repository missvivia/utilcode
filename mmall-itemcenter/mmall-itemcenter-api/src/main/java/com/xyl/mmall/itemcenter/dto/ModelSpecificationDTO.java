/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ModelSpecification;

/**
 * ModelSpecificationDTO.java created by yydx811 at 2015年5月4日 下午5:06:38
 * 商品规格DTO
 *
 * @author yydx811
 */
public class ModelSpecificationDTO extends ModelSpecification {

	/** 序列化id. */
	private static final long serialVersionUID = 5054512905939136265L;

	/** 规格选项列表. */
	private List<ModelSpeciOptionDTO> speciOptionList;
	
	public ModelSpecificationDTO() {
	}
	
	public ModelSpecificationDTO(ModelSpecification obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public List<ModelSpeciOptionDTO> getSpeciOptionList() {
		return speciOptionList;
	}

	public void setSpeciOptionList(List<ModelSpeciOptionDTO> speciOptionList) {
		this.speciOptionList = speciOptionList;
	}
}
