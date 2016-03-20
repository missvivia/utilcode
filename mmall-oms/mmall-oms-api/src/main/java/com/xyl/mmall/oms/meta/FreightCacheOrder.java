/**
 * 
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 未计算运费或计算运费异常时将订单数据先缓冲起来
 * 
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "Oms 被缓存待计算运费的订单", tableName = "Mmall_Oms_FreightCacheOrder")
public class FreightCacheOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "订单Id", policy = true)
	private String orderId;

	@AnnonOfField(desc = "订单类型")
	private String orderType;

	@AnnonOfField(desc = "原因")
	private String reason;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	public FreightCacheOrder() {
		setCreateTime(System.currentTimeMillis());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = cutReason(reason);
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public static String cutReason(String reason) {
		if (reason != null && reason.length() > 128) {
			return reason.substring(0, 128);
		} else {
			return reason;
		}
	}

}
