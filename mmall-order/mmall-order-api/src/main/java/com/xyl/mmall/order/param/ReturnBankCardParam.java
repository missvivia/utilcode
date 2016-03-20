package com.xyl.mmall.order.param;

import java.io.Serializable;

/**
 * 货到付款退货场景下的银行卡信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月30日 下午1:49:09
 *
 */
public class ReturnBankCardParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1278716056585061121L;

	// @AnnonOfField(desc = "银行卡卡号")
	private String bankCardNO;
	
	// @AnnonOfField(desc = "开户人姓名")
	private String bankCardOwnerName;
	
	// @AnnonOfField(desc = "开户行地址")
	private String bankCardAddress;
	
	// @AnnonOfField(desc = "银行")
	private String bankType;
	
	// @AnnonOfField(desc = "支行")
	private String bankBranch;

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
