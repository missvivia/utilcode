/**
 * 
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author liujie
 * @date 2014-09-29
 */
@AnnonOfClass(desc = "商家绑定手机修改日志表", tableName = "Mmall_Oms_BusinessPhoneLog")
public class BusinessPhoneLogForm implements Serializable {
	
	/** 序列. */
	private static final long serialVersionUID = -93227604113144052L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商家帐号")
	private String businessAccount;

	@AnnonOfField(desc = "订单id", policy = true)
	private int orderId;

	@AnnonOfField(desc = "旧手机号", type = "varchar(64)")
	private String oldphone;

	@AnnonOfField(desc = "新手机号", type = "varchar(64)")
	private String newphone;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBusinessAccount() {
		return businessAccount;
	}

	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOldphone() {
		return oldphone;
	}

	public void setOldphone(String oldphone) {
		this.oldphone = oldphone;
	}

	public String getNewphone() {
		return newphone;
	}

	public void setNewphone(String newphone) {
		this.newphone = newphone;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
