/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.dto.ModelParameterDTO;

/**
 * ModelParameter.java created by yydx811 at 2015年4月30日 上午10:32:20
 * 商品模型属性
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_Parameter", desc = "商品模型属性表", dbCreateTimeName = "CreateTime")
public class ModelParameter implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -3618303108045377016L;

	@AnnonOfField(desc = "扩展属性id", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "商品模型id", policy = true)
	private long modelId;

	@AnnonOfField(desc = "属性名")
	private String name;

	@AnnonOfField(desc = "操作样式,单选1,多选2")
	private int isSingle = 1;

	@AnnonOfField(desc = "是否作为筛选项,1是,2不是")
	private int isShow = 1;

	@AnnonOfField(desc = "操作人Id")
	private long agentId;

	public ModelParameter() {
	}

	public ModelParameter(ModelParameterDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getModelId() {
		return modelId;
	}

	public void setModelId(long modelId) {
		this.modelId = modelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIsSingle() {
		return isSingle;
	}

	public void setIsSingle(int isSingle) {
		this.isSingle = isSingle;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}
}
