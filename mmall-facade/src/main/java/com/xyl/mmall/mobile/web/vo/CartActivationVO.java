package com.xyl.mmall.mobile.web.vo;

import java.io.Serializable;
import java.util.List;

import com.netease.print.common.meta.HasShowIndex2;
import com.xyl.mmall.mobile.web.vo.order.ActionTagVO;

/**
 * @author Yang,Nan
 *
 */
public class CartActivationVO implements Serializable,HasShowIndex2<Long> {
	
	private long id;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4640308561745153319L;

	public final String type = "active";

	

	/**
	 * 活动Tag列表
	 */
	private List<ActionTagVO> tagList;

	/**
	 * 活动效果描述
	 */
	private String effectDesc;

	/**
	 * 凑单页面 url
	 */
	private String discountUrl;

	/**
	 * 满足条件的po列表
	 */
	private List<CartPOVO> poList;
	
	
	private String activatonName;
	/**
	 * 是否删除
	 */
	private boolean isDelete = false;

	/**
	 * 是否选上
	 */
	private boolean isSelected = false;




	public List<CartPOVO> getPoList() {
		return poList;
	}

	public void setPoList(List<CartPOVO> poList) {
		this.poList = poList;
	}

	

	public List<ActionTagVO> getTagList() {
		return tagList;
	}

	public void setTagList(List<ActionTagVO> tagList) {
		this.tagList = tagList;
	}

	public String getEffectDesc() {
		return effectDesc;
	}

	public void setEffectDesc(String effectDesc) {
		this.effectDesc = effectDesc;
	}

	public String getDiscountUrl() {
		return discountUrl;
	}

	public void setDiscountUrl(String discountUrl) {
		this.discountUrl = discountUrl;
	}

	public boolean isDeleted() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getActivatonName() {
		return activatonName;
	}

	public void setActivatonName(String activatonName) {
		this.activatonName = activatonName;
	}

	@Override
	public Long getShowIndex() {
		// TODO Auto-generated method stub
		return Long.valueOf(id);
	}

}
