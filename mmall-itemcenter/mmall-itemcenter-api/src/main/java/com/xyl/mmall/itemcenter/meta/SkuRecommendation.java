package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(tableName = "Mmall_ItemCenter_SkuRecommendation", desc = "首页商品推荐表")
public class SkuRecommendation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8566340132297176165L;

	/**
	 * 主键Id
	 */
	@AnnonOfField(desc = "主键id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/**
	 * 商家id
	 */
	@AnnonOfField(desc = "商家id", policy = true)
	private long businessId;

	/**
	 * 商品Id
	 */
	@AnnonOfField(desc = "商品id")
	private long productSKUId;

	/**
	 * 商品顺序
	 */
	@AnnonOfField(desc = "商品顺序")
	private int showIndex;

	@AnnonOfField(desc = "创建时间")
	private Date createTime;

	@AnnonOfField(desc = "修改时间")
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public long getProductSKUId() {
		return productSKUId;
	}

	public void setProductSKUId(long productSKUId) {
		this.productSKUId = productSKUId;
	}

	public int getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
