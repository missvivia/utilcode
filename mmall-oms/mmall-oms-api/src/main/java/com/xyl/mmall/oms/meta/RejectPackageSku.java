package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.enums.RejectPackageState;

/**
 * 用户拒收包裹快件商品明细
 * 
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "用户拒收件明细", tableName = "Mmall_Oms_RejectPackageSku")
public class RejectPackageSku implements Serializable {
	
	/** 序列. */
	private static final long serialVersionUID = -93227604113144052L;

	@AnnonOfField(desc = "主键标识", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "包裹Id", policy = true)
	private long rejectPackageId;

	@AnnonOfField(desc = "sku")
	private long skuId;

	@AnnonOfField(desc = "商品重量")
	private long weight;

	@AnnonOfField(desc = "数量")
	private long count;

	@AnnonOfField(desc = "拒收包裹状态")
	private RejectPackageState state;

	@AnnonOfField(desc = "包裹状态更新时间")
	private long stateUpdateTime;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	public RejectPackageSku() {
		this(null);
	}

	public RejectPackageSku(OmsOrderPackageSku sku) {
		if (sku != null) {
			ReflectUtil.convertObj(this, sku, true);
		}
		setCreateTime(System.currentTimeMillis());
		setStateUpdateTime(System.currentTimeMillis());
		setState(RejectPackageState.SENDWMS);
	}

	public long getId() {
		return id;
	}

	public long getRejectPackageId() {
		return rejectPackageId;
	}

	public long getSkuId() {
		return skuId;
	}

	public long getWeight() {
		return weight;
	}

	public long getCount() {
		return count;
	}

	public RejectPackageState getState() {
		return state;
	}

	public long getStateUpdateTime() {
		return stateUpdateTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setRejectPackageId(long rejectPackageId) {
		this.rejectPackageId = rejectPackageId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	public void setCount(long count) {
		this.count = count;
	}

	/**
	 * 设置包裹商品状态，同时设置状态更新时间为：System.currentTimeMillis()
	 * @param state
	 */
	public void setState(RejectPackageState state) {
		this.state = state;
		setStateUpdateTime(System.currentTimeMillis());
	}

	public void setStateUpdateTime(long packageStateUpdateTime) {
		this.stateUpdateTime = packageStateUpdateTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
