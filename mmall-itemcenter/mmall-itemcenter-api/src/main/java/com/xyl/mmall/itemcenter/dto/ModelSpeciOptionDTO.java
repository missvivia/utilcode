/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ModelSpeciOption;

/**
 * ModelSpeciOptionDTO.java created by yydx811 at 2015年5月4日 下午5:08:38
 * 规格选项DTO
 *
 * @author yydx811
 */
public class ModelSpeciOptionDTO extends ModelSpeciOption {

	/** 序列化id. */
	private static final long serialVersionUID = -3802199458488651098L;

	public ModelSpeciOptionDTO() {
	}

	public ModelSpeciOptionDTO(ModelSpeciOption obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
