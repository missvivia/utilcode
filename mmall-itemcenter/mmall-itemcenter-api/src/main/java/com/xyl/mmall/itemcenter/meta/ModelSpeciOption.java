/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.dto.ModelSpeciOptionDTO;

/**
 * ModelSpeciOption.java created by yydx811 at 2015年5月4日 下午5:01:07
 * 规格选项
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_SpeciOption", desc = "规格选项值表", dbCreateTimeName = "CreatTime")
public class ModelSpeciOption implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 3116119018053822139L;

	@AnnonOfField(desc = "规格选项值id", autoAllocateId = true, primary = true)
	private long id;
	
	@AnnonOfField(desc = "商品规格id", policy = true)
	private long specificationId;

	@AnnonOfField(desc = "显示类型，1文字，2图片")
	private int Type;
	
	@AnnonOfField(desc = "显示顺序")
	private int showIndex;

	@AnnonOfField(desc = "选项值")
	private String optionValue;

	@AnnonOfField(desc = "操作人Id")
	private long agentId;

	public ModelSpeciOption() {
	}

	public ModelSpeciOption(ModelSpeciOptionDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(long specificationId) {
		this.specificationId = specificationId;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
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
