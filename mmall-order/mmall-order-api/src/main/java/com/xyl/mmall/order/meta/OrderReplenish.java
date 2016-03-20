/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.dto.OrderReplenishDTO;

/**
 * OrderReplenish.java created by yydx811 at 2015年6月5日 下午2:55:52
 * 用户补货
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "Mmall_Order_Replenish", desc = "用户补货", dbCreateTimeName = "CreateTime")
public class OrderReplenish implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -6321731199737659337L;

	@AnnonOfField(desc = "补货单id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "用户id", policy = true)
	private long userId;

	@AnnonOfField(desc = "商家id")
	private long businessId;

	@AnnonOfField(desc = "商品id")
	private long skuId;

	@AnnonOfField(desc = "商品快照")
	private String skuSnapshot;

	@AnnonOfField(desc = "购买数量")
	private int buyNum;

	@AnnonOfField(desc = "购买价格")
	private BigDecimal buyPrice;

	@AnnonOfField(desc = "创建时间")
	private Date createTime;

	@AnnonOfField(desc = "更新时间")
	private Date updateTime;

	public OrderReplenish() {
	}
	
	public OrderReplenish(OrderReplenishDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getSkuSnapshot() {
		return skuSnapshot;
	}

	public void setSkuSnapshot(String skuSnapshot) {
		this.skuSnapshot = skuSnapshot;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
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
