package com.xyl.mmall.itemcenter.dto;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.CategoryNormal;

/**
 * CategoryNormalDTO.java created by yydx811 at 2015年4月27日 上午9:23:19
 * 商品分类dto
 *
 * @author yydx811
 */
public class CategoryNormalDTO extends CategoryNormal implements Comparable<CategoryNormalDTO>{

	/** 序列化id. */
	private static final long serialVersionUID = 3674644652991063333L;

	/** 子分类. */
	private List<CategoryNormalDTO> sameParentList;
	
	/** 类目全称 。  */
	private String fullCategoryName = StringUtils.EMPTY;
	
	/** 子类第三级IDs。用于 */
	private String subThirdIds = StringUtils.EMPTY;
	
	public String getSubThirdIds() {
		return subThirdIds;
	}

	public void setSubThirdIds(String subThirdIds) {
		this.subThirdIds = subThirdIds;
	}

	public String getFullCategoryName() {
		return fullCategoryName;
	}

	public void setFullCategoryName(String fullCategoryName) {
		this.fullCategoryName = fullCategoryName;
	}

	public CategoryNormalDTO() {
	}
	
	public CategoryNormalDTO(CategoryNormal obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public List<CategoryNormalDTO> getSameParentList() {
		return sameParentList;
	}

	public void setSameParentList(List<CategoryNormalDTO> sameParentList) {
		this.sameParentList = sameParentList;
	}
	
	@Override
	public String toString() {
		return "[id=" + super.getId() + ", level=" + super.getLevel() + ", name=" + super.getName() + ", showIndex=" + super.getShowIndex()
				+ ", superCategoryId=" + super.getSuperCategoryId() + ", agentId=" + super.getAgentId() +", fullCategoryName=" + fullCategoryName +", subThirdIds=" + subThirdIds + "]";
	}
	@Override
	public int compareTo(CategoryNormalDTO o) {
		return this.getLevel()-o.getLevel();
	}
}
