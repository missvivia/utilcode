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
@AnnonOfClass(desc = "商家绑定手机表", tableName = "Mmall_Oms_BusinessPhone")
public class BusinessPhoneForm implements Serializable {
	
	/** 序列. */
	private static final long serialVersionUID = -93227604113144052L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商家帐号")
	private String businessAccount;

	@AnnonOfField(desc = "订单id", policy = true)
	private int orderId;

	@AnnonOfField(desc = "手机号", type = "varchar(64)")
	private String phone;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "修改时间")
	private long modifyTime;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

}
