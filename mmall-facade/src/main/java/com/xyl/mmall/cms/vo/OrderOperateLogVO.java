/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.util.Date;

import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;

/**
 * OrderOperateLogVO.java created by yydx811 at 2015年6月10日 下午4:19:53
 * cms订单操作日志
 *
 * @author yydx811
 */
public class OrderOperateLogVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 3394265196119926305L;

	/** 操作日志. */
	private long operateLogId;

	/** 订单id. */
	private long cmsOrderId;

	/** 店铺id. */
	private long supplierId;

	/** 类型：1订单状态，2订单金额 ，3收货地址，4 发票. */
	private int operateType;

	/** 原内容. */
	private String perOperateContent;

	/** 当前内容. */
	private String curOperateContent;

	/** 备注. */
	private String operateNote;
	
	/** 操作人类型，0系统，1cms管理员，2backend管理员，3用户. */
	private int operatorType;

	/** 操作人. */
	private String operator;

	/** 创建时间. */
	private Long createTime;
	
	/** 显示的创建时间。 */
	private String showTime;

	public OrderOperateLogVO() {
	}
	
	public OrderOperateLogVO(OrderOperateLogDTO obj) {
		this.operateLogId = obj.getId();
		this.cmsOrderId = obj.getOrderId();
		this.supplierId = obj.getBusinessId();
		this.operateType = obj.getType();
		this.perOperateContent = obj.getPreContent();
		this.curOperateContent = obj.getCurContent();
		this.operateNote = obj.getNote();
		this.operatorType = obj.getOperatorType();
		if (obj.getCreateTime() != null) {
			this.createTime = obj.getCreateTime().getTime();
			this.showTime = DateUtil.dateToString(obj.getCreateTime(), DateUtil.LONG_PATTERN);
		}
	}
	
	public OrderOperateLogDTO convertToDTO() {
		OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
		operateLogDTO.setId(this.operateLogId);
		operateLogDTO.setOrderId(this.cmsOrderId);
		operateLogDTO.setBusinessId(this.supplierId);
		operateLogDTO.setType(this.operateType);
		operateLogDTO.setPreContent(this.perOperateContent);
		operateLogDTO.setCurContent(this.curOperateContent);
		operateLogDTO.setNote(this.operateNote);
		operateLogDTO.setOperatorType(this.operatorType);
		if (this.createTime != null && this.createTime.longValue() > 0l) {
			operateLogDTO.setCreateTime(new Date(this.createTime.longValue()));
		}
		return operateLogDTO;
	}
	
	public long getOperateLogId() {
		return operateLogId;
	}

	public void setOperateLogId(long operateLogId) {
		this.operateLogId = operateLogId;
	}

	public long getCmsOrderId() {
		return cmsOrderId;
	}

	public void setCmsOrderId(long cmsOrderId) {
		this.cmsOrderId = cmsOrderId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	public String getPerOperateContent() {
		return perOperateContent;
	}

	public void setPerOperateContent(String perOperateContent) {
		this.perOperateContent = perOperateContent;
	}

	public String getCurOperateContent() {
		return curOperateContent;
	}

	public void setCurOperateContent(String curOperateContent) {
		this.curOperateContent = curOperateContent;
	}

	public String getOperateNote() {
		return operateNote;
	}

	public void setOperateNote(String operateNote) {
		this.operateNote = operateNote;
	}

	public int getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	
	
}
