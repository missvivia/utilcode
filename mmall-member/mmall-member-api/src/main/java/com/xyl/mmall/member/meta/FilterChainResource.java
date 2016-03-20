/**
 * 
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.member.enums.AuthzCategory;

/**
 * @author lihui
 *
 */
@AnnonOfClass(tableName = "Mmall_Member_FilterChainResource", desc = "权限过滤器定义信息")
public class FilterChainResource implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "访问路径定义", type = "VARCHAR(64)")
	private String url;

	@AnnonOfField(desc = "访问权限定义", type = "VARCHAR(64)")
	private String permission;

	@AnnonOfField(desc = "权限系统类别,0:商家后台管理角色, 1:运维平台管理角色, 2:主站系统, 3:手机后台系统")
	private AuthzCategory category;

	@AnnonOfField(desc = "排序顺序")
	private int orderBy;

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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * @return the category
	 */
	public AuthzCategory getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(AuthzCategory category) {
		this.category = category;
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
