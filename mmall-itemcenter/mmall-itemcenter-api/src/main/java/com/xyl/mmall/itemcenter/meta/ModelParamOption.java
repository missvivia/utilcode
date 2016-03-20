/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.dto.ModelParamOptionDTO;

/**
 * ModelParamOption.java created by yydx811 at 2015年4月30日 上午11:04:12
 * 商品模型选项数据
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_ParamOption", desc = "属性选项数据表", dbCreateTimeName = "CreateTime")
public class ModelParamOption implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 4850668117677772094L;

	@AnnonOfField(desc = "选项id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "属性id", policy = true)
	private long parameterId;

	@AnnonOfField(desc = "选项数据")
	private String optionValue;

	@AnnonOfField(desc = "操作人Id")
	private long agentId;

	public ModelParamOption() {
	}

	public ModelParamOption(ModelParamOptionDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParameterId() {
		return parameterId;
	}

	public void setParameterId(long parameterId) {
		this.parameterId = parameterId;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}
}
