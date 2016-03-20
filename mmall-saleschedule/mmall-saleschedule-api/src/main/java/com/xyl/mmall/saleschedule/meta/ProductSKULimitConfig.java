/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * ProductSKULimitConfig.java created by yydx811 at 2015年11月17日 上午9:34:43
 * 商品限购配置
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_SaleSchedule_SKULimitConfig", desc = "商品限购配置表", dbCreateTimeName = "CreateTime")
public class ProductSKULimitConfig implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -6562796400157968692L;

	@AnnonOfField(primary = true, desc = "商品限量配置id", autoAllocateId = true)
	private long Id;

	@AnnonOfField(policy = true, desc = "商品id")
	private long productSKUId;

	@AnnonOfField(desc = "限购数量")
	private int limitNumber;

	@AnnonOfField(desc = "起始时间")
	private long startTime;

	@AnnonOfField(desc = "结束时间")
	private long endTime;
	
	@AnnonOfField(desc = "限购周期")
	private long period;

	@AnnonOfField(desc = "限购说明")
	private String note;
	
	@AnnonOfField(desc = "创建人")
	private long creatorId;

	@AnnonOfField(desc = "修改人")
	private long lastModifyId;

	@AnnonOfField(desc = "创建时间")
	private Date createTime;

	@AnnonOfField(desc = "更新时间")
	private Date updateTime;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getProductSKUId() {
		return productSKUId;
	}

	public void setProductSKUId(long productSKUId) {
		this.productSKUId = productSKUId;
	}

	public int getLimitNumber() {
		return limitNumber;
	}

	public void setLimitNumber(int limitNumber) {
		this.limitNumber = limitNumber;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public long getLastModifyId() {
		return lastModifyId;
	}

	public void setLastModifyId(long lastModifyId) {
		this.lastModifyId = lastModifyId;
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
