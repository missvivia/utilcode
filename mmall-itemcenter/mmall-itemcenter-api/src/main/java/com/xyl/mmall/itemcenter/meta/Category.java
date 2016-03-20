package com.xyl.mmall.itemcenter.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 目录 meta类<br>
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_Category", desc = "商品类目", dbCreateTimeName = "CreateTime")
public class Category implements java.io.Serializable {

	private static final long serialVersionUID = -378453065109283134L;

	/** 主键id */
	@AnnonOfField(desc = "id", primary = true, primaryIndex = 1, autoAllocateId = true, policy = true)
	private long id;

	/** 父目录的id */
	@AnnonOfField(desc = "父类目的Id")
	private long superCategoryId;

	/** 显示次序 */
	@AnnonOfField(desc = "显示顺序")
	private int showIndex;

	/** 目录等级 */
	@AnnonOfField(desc = "类目等级")
	private int level;

	/** 目录名称 */
	@AnnonOfField(desc = "类目名称", type="VARCHAR(32)")
	private String name;

	/** 该类目下的商品参数 */
	@AnnonOfField(desc = "该类目下的商品参数", notNull = false)
	private String parameter;

	@AnnonOfField(desc = "分类下品牌", notNull = false)
	private String brand;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSuperCategoryId() {
		return superCategoryId;
	}

	public void setSuperCategoryId(long superCategoryId) {
		this.superCategoryId = superCategoryId;
	}

	public int getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
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

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
