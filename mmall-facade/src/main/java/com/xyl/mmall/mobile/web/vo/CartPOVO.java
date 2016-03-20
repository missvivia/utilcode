package com.xyl.mmall.mobile.web.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.netease.print.common.meta.HasShowIndex2;
import com.xyl.mmall.saleschedule.meta.Schedule;

/**
 * 购物车里面一个品牌
 * 
 * @author Yang,Nan
 *
 */

public class CartPOVO implements Serializable, HasShowIndex2<Long>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3507068982129030327L;

	private long id;

	public final String type = "po";

	/**
	 * 是否删除
	 */
	private boolean isDelete = false;

	/**
	 * 是否选上
	 */
	private boolean isSelected = false;

	private Schedule po;

	private List<CartSkuItemVO> skulist = new ArrayList<CartSkuItemVO>();

	public void setPo(Schedule po) {
		this.po = po;
	}

	public List<CartSkuItemVO> getSkulist() {
		return skulist;
	}

	public void setSkulist(List<CartSkuItemVO> skulist) {
		this.skulist = skulist;
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

	public long getPoId() {
		return po.getId();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		String brand = "";
		String title = "";
		if(po.getBrandName() != null)
			brand = po.getBrandName();
		if(po.getTitle() != null)
			title = po.getTitle();
		return brand + " " + title;
	}

	public String getBrandName() {
		return po.getBrandName();
	}

	public String getTitle() {
		return po.getTitle();
	}

	@Override
	public Long getShowIndex() {
		// TODO Auto-generated method stub
		return po.getId();
	}

}
