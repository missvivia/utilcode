/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.dto.ModelSpecificationDTO;

/**
 * ModelSpecification.java created by yydx811 at 2015年5月4日 下午2:54:26
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_Specification", desc = "商品规格表", dbCreateTimeName = "CreatTime")
public class ModelSpecification implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 7809045821728617352L;

	@AnnonOfField(desc = "商品规格id", autoAllocateId = true, primary = true)
	private long id;
	
	@AnnonOfField(desc = "商品模型id", policy = true)
	private long ModelId;

	@AnnonOfField(desc = "规格名称")
	private String Name;

	@AnnonOfField(desc = "显示类型，1文字，2图片")
	private int Type;

	@AnnonOfField(desc = "备注")
	private String remark;

	@AnnonOfField(desc = "是否作为筛选项,1是,2不是")
	private int isShow;
	
	@AnnonOfField(desc = "操作人Id")
	private long agentId;

	public ModelSpecification() {
	}

	public ModelSpecification(ModelSpecificationDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getModelId() {
		return ModelId;
	}

	public void setModelId(long modelId) {
		ModelId = modelId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
