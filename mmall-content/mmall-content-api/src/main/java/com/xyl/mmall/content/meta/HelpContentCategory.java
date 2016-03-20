/**
 * 
 */
package com.xyl.mmall.content.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author lihui
 *
 */
@AnnonOfClass(tableName = "Mmall_Content_HelpCategory", desc = "内容管理的帮助中心类目信息")
public class HelpContentCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "类目标题", notNull = false, safeHtml = true, type = "VARCHAR(32)")
	private String name;

	@AnnonOfField(desc = "父类目的ID,根类目的父类目ID为0")
	private long parentId;

	@AnnonOfField(desc = "排序顺序")
	private int orderBy;

	@AnnonOfField(desc = "最后保存时间")
	private long lastModifiedTime;

	@AnnonOfField(desc = "最后修改人Id")
	private long lastModifiedBy;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the lastModifiedTime
	 */
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * @param lastModifiedTime
	 *            the lastModifiedTime to set
	 */
	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public long getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy
	 *            the lastModifiedBy to set
	 */
	public void setLastModifiedBy(long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @return the orderBy
	 */
	public int getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

}
