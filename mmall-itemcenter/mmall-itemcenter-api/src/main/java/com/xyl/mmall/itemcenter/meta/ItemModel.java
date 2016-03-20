/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;
import java.sql.Timestamp;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.dto.ItemModelDTO;

/**
 * ItemModel.java created by yydx811 at 2015年4月29日 下午8:27:45
 * 商品模型
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_Model", desc = "商品模型表", dbCreateTimeName="CreateTime")
public class ItemModel implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 1463592716943826531L;

	@AnnonOfField(desc = "商品模版id", autoAllocateId = true, primary = true, policy = true)
	private long Id;
	
	@AnnonOfField(desc = "商品模版名称")
	private String name;

	@AnnonOfField(desc = "商品分类id")
	private long categoryNormalId;

	@AnnonOfField(desc = "操作人Id")
	private long agentId;

	@AnnonOfField(desc = "创建时间")
	private Timestamp createTime;

	@AnnonOfField(desc = "更新时间")
	private Timestamp updateTime;

	public ItemModel() {
	}

	public ItemModel(ItemModelDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCategoryNormalId() {
		return categoryNormalId;
	}

	public void setCategoryNormalId(long categoryNormalId) {
		this.categoryNormalId = categoryNormalId;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}
