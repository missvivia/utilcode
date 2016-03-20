package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.SizeTemplate;

/**
 * 商家平台的尺码模板管理中的尺码模板查询结果类
 * 
 * @author hzhuangluqian
 *
 */
public class SizeTemplateDTO extends SizeTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -680431718004188270L;
	/** 所属类目名称 */
	private String lowCategoryName;

	public SizeTemplateDTO(SizeTemplate obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public String getLowCategoryName() {
		return lowCategoryName;
	}

	public void setLowCategoryName(String lowCategoryName) {
		this.lowCategoryName = lowCategoryName;
	}

	public String toString() {
		return super.getId() + "\t" + super.getTemplateName() + "\t" + getLowCategoryName() + "\t"
				+ super.getLastModifyTime();
	}
}
