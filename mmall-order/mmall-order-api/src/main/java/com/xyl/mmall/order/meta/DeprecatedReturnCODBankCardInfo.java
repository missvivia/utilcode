package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@Deprecated
@AnnonOfClass(desc = "货到付款退货场景下的银行卡信息", tableName = "Mmall_Order_ReturnCODBankCardInfo")
public class DeprecatedReturnCODBankCardInfo implements Serializable {

	private static final long serialVersionUID = 22004006511756277L;

	@AnnonOfField(desc = "退货Id", primary = true)
	private long retId;
	
	@AnnonOfField(desc = "订单Id")
	private long orderId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "银行卡卡号")
	private String bankCardNO;
	
	@AnnonOfField(desc = "开户人姓名")
	private String bankCardOwnerName;
	
	@AnnonOfField(desc = "开户行地址")
	private String bankCardAddress;
	
	@AnnonOfField(desc = "银行")
	private String bankType;
	
	@AnnonOfField(desc = "支行")
	private String bankBranch;

	public long getRetId() {
		return retId;
	}

	public void setRetId(long retId) {
		this.retId = retId;
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

	public String getBankCardNO() {
		return bankCardNO;
	}

	public void setBankCardNO(String bankCardNO) {
		this.bankCardNO = bankCardNO;
	}

	public String getBankCardOwnerName() {
		return bankCardOwnerName;
	}

	public void setBankCardOwnerName(String bankCardOwnerName) {
		this.bankCardOwnerName = bankCardOwnerName;
	}

	public String getBankCardAddress() {
		return bankCardAddress;
	}

	public void setBankCardAddress(String bankCardAddress) {
		this.bankCardAddress = bankCardAddress;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

}