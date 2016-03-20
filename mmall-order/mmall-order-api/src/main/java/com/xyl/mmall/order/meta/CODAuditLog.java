package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.enums.CODAuditState;

/**
 * 到付申请审核日志
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午9:36:26
 *
 */
@AnnonOfClass(desc = "到付申请审核日志", tableName = "Mmall_Order_CODAuditLog")
public class CODAuditLog implements Serializable {
	
	private static final long serialVersionUID = -5459245384005905196L;
	
	@AnnonOfField(desc = "主键Id", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "审核人员用户Id")
	private long auditUserId;

	@AnnonOfField(desc = "审核日志创建时间")
	private long ctime;
	
	@AnnonOfField(desc = "订单Id")
	private long orderId;
	
	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;
	
	@AnnonOfField(desc = "站点Id")
	private int provinceId;

	@AnnonOfField(desc = "审核状态")
	private CODAuditState auditState;
	
	@AnnonOfField(desc = "备注信息")
	private String extInfo;
	
	@AnnonOfField(desc = "审核日志更新时间")
	private long updateTime;
	
	@AnnonOfField(desc = "是否系统自动审核通过")
	private boolean passedByRobot;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public CODAuditState getAuditState() {
		return auditState;
	}

	public void setAuditState(CODAuditState auditState) {
		this.auditState = auditState;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isPassedByRobot() {
		return passedByRobot;
	}

	public void setPassedByRobot(boolean passedByRobot) {
		this.passedByRobot = passedByRobot;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}