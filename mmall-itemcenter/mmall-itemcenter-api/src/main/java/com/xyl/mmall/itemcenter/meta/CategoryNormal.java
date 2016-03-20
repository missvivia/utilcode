package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;

/**
 * CategoryNormal.java created by yydx811 at 2015年4月27日 上午9:23:57
 * 商品分类
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "商品分类表", tableName = "Mmall_Category_Normal", policy = "id", dbCreateTimeName = "CreateTime")
public class CategoryNormal implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -7296822934117131804L;

	/** 主键id */
	@AnnonOfField(desc = "id", primary = true, primaryIndex = 1, autoAllocateId = true, policy = true)
	private long id;

	/** 目录等级 */
	@AnnonOfField(desc = "类目等级")
	private int level;

	/** 目录名称 */
	@AnnonOfField(desc = "类目名称",type="VARCHAR(32)")
	private String name;

	/** 显示次序 */
	@AnnonOfField(desc = "显示顺序")
	private int showIndex;

	/** 父目录的id */
	@AnnonOfField(desc = "父类目的Id")
	private long superCategoryId;

	@AnnonOfField(desc = "操作人id")
	private long agentId;
	
	public CategoryNormal() {
	}

	public CategoryNormal(CategoryNormalDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}

	public long getSuperCategoryId() {
		return superCategoryId;
	}

	public void setSuperCategoryId(long superCategoryId) {
		this.superCategoryId = superCategoryId;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", level=" + level + ", name=" + name + ", showIndex=" + showIndex
				+ ", superCategoryId=" + superCategoryId + ", agentId=" + agentId + "]";
	}
}
