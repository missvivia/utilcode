/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.util.Date;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;

/**
 * OrderOperateLog.java created by yydx811 at 2015年6月10日 下午3:31:40
 * 订单操作表
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "订单操作日志", tableName = "Mmall_Order_OperateLog", dbCreateTimeName = "CreateTime")
public class OrderOperateLog implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 4599990042387600912L;

	@AnnonOfField(desc = "操作日志Id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "订单Id", policy = true)
	private long orderId;

	@AnnonOfField(desc = "商家Id")
	private long businessId;

	@AnnonOfField(desc = "类型：1订单状态，2订单金额 ，3收货地址，4 发票，5.物流  6.备注  7 支付状态")
	private int type;
	
	@AnnonOfField(desc = "原内容")
	private String preContent;

	@AnnonOfField(desc = "当前内容")
	private String curContent;

	@AnnonOfField(desc = "备注", notNull = false)
	private String note;
	
	@AnnonOfField(desc = "操作人类型，0系统，1cms管理员，2backend管理员，3用户")
	private int operatorType;
	
	@AnnonOfField(desc = "操作人id")
	private long operatorId;

	@AnnonOfField(desc = "创建时间")
	private Date createTime;

	public OrderOperateLog() {
	}
	
	public OrderOperateLog(OrderOperateLogDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPreContent() {
		return preContent;
	}

	public void setPreContent(String preContent) {
		this.preContent = preContent;
	}

	public String getCurContent() {
		return curContent;
	}

	public void setCurContent(String curContent) {
		this.curContent = curContent;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public int getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
	}

	public long getOperatorId() {
		return operatorId;
	}
	
	public void setOperatorId(long operatorId) {
		this.operatorId = operatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
