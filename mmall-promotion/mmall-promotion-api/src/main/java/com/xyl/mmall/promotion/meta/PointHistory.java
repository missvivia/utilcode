/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * PointHistory.java created by yydx811 at 2015年12月23日 上午10:23:52
 * 积分记录
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "积分记录表", tableName = "Mmall_Promotion_PointHistory", dbCreateTimeName = "CreateTime")
public class PointHistory implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -4219921191975203519L;

	@AnnonOfField(desc = "记录id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "用户ID", policy = true)
	private long userId;

	@AnnonOfField(desc = "订单id", defa = "0", notNull = true)
	private long orderId;

	@AnnonOfField(desc = "交易商品名或调整名")
	private String name;

	@AnnonOfField(desc = "记录类型，1管理员调整，2交易获取，3交易抵扣，4交易回退")
	private int type;

	@AnnonOfField(desc = "积分增减量", defa = "0", notNull = true)
	private int pointDelta;
	
	@AnnonOfField(desc = "创建时间")
	private Date CreateTime;

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

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPointDelta() {
		return pointDelta;
	}

	public void setPointDelta(int pointDelta) {
		this.pointDelta = pointDelta;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
}
