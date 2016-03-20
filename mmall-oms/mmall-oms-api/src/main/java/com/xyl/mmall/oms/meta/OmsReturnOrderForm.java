package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;

/**
 * 退货记录
 * 
 */
@AnnonOfClass(desc = "退货记录", tableName = "Mmall_Oms_OmsReturnOrderForm")
public class OmsReturnOrderForm implements Serializable {

	private static final long serialVersionUID = -6927719922584400168L;

	@AnnonOfField(desc = "退货Id(PK)", primary = true)
	private long id;

	@AnnonOfField(desc = "要退的订单的Id")
	private long orderId;
	
	@AnnonOfField(desc = "要退的包裹id")
	private long returnPackageId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "申请时间")
	private long ctime;

	@AnnonOfField(desc = "退货-快递号", type = "VARCHAR(64)")
	private String mailNO = "";

	@AnnonOfField(desc = "退货-快递公司", type = "VARCHAR(64)")
	private String expressCompany = "";

	@AnnonOfField(desc = "仓库确认收货时间")
	private long confirmTime;

	@AnnonOfField(desc = "退货状态")
	private OmsReturnOrderFormState omsReturnOrderFormState;

	@AnnonOfField(desc = "备注信息")
	private String extInfo = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getReturnPackageId() {
		return returnPackageId;
	}

	public void setReturnPackageId(long returnPackageId) {
		this.returnPackageId = returnPackageId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public OmsReturnOrderFormState getOmsReturnOrderFormState() {
		return omsReturnOrderFormState;
	}

	public void setOmsReturnOrderFormState(OmsReturnOrderFormState omsReturnOrderFormState) {
		this.omsReturnOrderFormState = omsReturnOrderFormState;
	}
}