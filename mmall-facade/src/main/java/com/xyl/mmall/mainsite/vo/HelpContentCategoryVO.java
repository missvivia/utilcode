/**
 * 
 */
package com.xyl.mmall.mainsite.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.content.dto.HelpContentCategoryDTO;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class HelpContentCategoryVO {

	private HelpContentCategoryDTO category;

	private List<HelpContentCategoryVO> children;

	public HelpContentCategoryVO(HelpContentCategoryDTO category) {
		this.setCategory(category);
	}

	public HelpContentCategoryVO() {
		category = new HelpContentCategoryDTO();
	}

	/**
	 * @return the category
	 */
	@JsonIgnore
	public HelpContentCategoryDTO getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(HelpContentCategoryDTO category) {
		this.category = category;
	}

	/**
	 * @return the children
	 */
	public List<HelpContentCategoryVO> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<HelpContentCategoryVO> children) {
		this.children = children;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return category.getId();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		category.setId(id);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return category.getName();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		category.setName(name);
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return category.getParentId();
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(long parentId) {
		category.setParentId(parentId);
	}

}
