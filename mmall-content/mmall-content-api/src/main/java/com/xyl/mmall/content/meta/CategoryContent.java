package com.xyl.mmall.content.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.common.meta.BaseVersion;

/**
 * 目录 meta类<br>
 * 
 * @author lihongpeng
 *
 */
@AnnonOfClass(tableName = "Mmall_Category_Content", desc = "内容类目", dbCreateTimeName = "CreateTime")
public class CategoryContent extends BaseVersion {
	
	private static final long serialVersionUID = 5588816251263090552L;

	/** 主键id */
	@AnnonOfField(desc = "id", primary = true, primaryIndex = 1, autoAllocateId = true, policy = true)
	private long id;

	/** 父目录的id */
	@AnnonOfField(desc = "父类目的Id")
	private long superCategoryId;
	
	/** 商品类目Ids */
	@AnnonOfField(desc = "商品类目Ids")
	private String categoryNormalIds;
	
	/** 显示次序 */
	@AnnonOfField(desc = "显示顺序")
	private int showIndex;

	/** 目录等级 */
	@AnnonOfField(desc = "类目等级")
	private int level;

	/** 目录名称 */
	@AnnonOfField(desc = "类目名称",type="VARCHAR(32)")
	private String name;
	
	/** 站点 */
	@AnnonOfField(desc = "站点",type="VARCHAR(255)")
	private String districtIds;
	
	/** 站点 名称*/
	@AnnonOfField(desc = "站点名称",type="VARCHAR(512)")
	private String districtNames;
	
	/** 类目树根id */
	@AnnonOfField(desc = "类目树根id")
	private long rootId;
	
	
	public CategoryContent(){
		
	}
	
	public String getDistrictNames() {
		return districtNames;
	}

	public void setDistrictNames(String districtNames) {
		this.districtNames = districtNames;
	}

	public String getCategoryNormalIds() {
		return categoryNormalIds;
	}

	public void setCategoryNormalIds(String categoryNormalIds) {
		this.categoryNormalIds = categoryNormalIds;
	}

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

	public String getDistrictIds() {
		return districtIds;
	}

	public void setDistrictIds(String districtIds) {
		this.districtIds = districtIds;
	}

	public long getRootId() {
		return rootId;
	}

	public void setRootId(long rootId) {
		this.rootId = rootId;
	}

	
}
