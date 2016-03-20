/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ModelParamOption;

/**
 * ModelParamOptionDTO.java created by yydx811 at 2015年5月4日 上午9:54:10
 * 模型属性选项DTO
 *
 * @author yydx811
 */
public class ModelParamOptionDTO extends ModelParamOption {

	/** 序列化id. */
	private static final long serialVersionUID = 2070577003205070036L;

	public ModelParamOptionDTO() {
	}
	
	public ModelParamOptionDTO(ModelParamOption obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
