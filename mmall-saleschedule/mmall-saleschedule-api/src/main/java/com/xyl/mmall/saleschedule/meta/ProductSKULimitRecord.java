/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * ProductSKULimitRecord.java created by yydx811 at 2015年11月17日 上午9:42:50
 * 商品限购记录
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_SaleSchedule_SKULimitRecord", desc = "商品限购记录表")
public class ProductSKULimitRecord implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -2735150759543423064L;

	@AnnonOfField(primary = true, desc = "商品限量记录id", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商品id")
	private long productSKUId;

	@AnnonOfField(policy = true, desc = "用户id")
	private long userId;

	@AnnonOfField(desc = "限购数量")
	private int totalNumber;

	@AnnonOfField(desc = "起始时间")
	private long createTime;

	@AnnonOfField(desc = "最后购买时间")
	private long lastBuyTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductSKUId() {
		return productSKUId;
	}

	public void setProductSKUId(long productSKUId) {
		this.productSKUId = productSKUId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(long lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}
}
