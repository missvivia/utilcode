package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author zb<br>
 *
 */
@AnnonOfClass(desc = "oms订单包裹明细", tableName = "Mmall_Oms_OmsOrderPackageSku")
public class OmsOrderPackageSku implements Serializable{

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "主键标识", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "包裹Id")
	private long packageId;

	@AnnonOfField(desc = "sku")
	private long skuId;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "订单id")
	private long omsOrderFormId;

	@AnnonOfField(desc = "商品重量")
	private long weight;
	
	@AnnonOfField(desc = "数量")
	private long count;

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	public long getOmsOrderFormId() {
		return omsOrderFormId;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		this.omsOrderFormId = omsOrderFormId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
