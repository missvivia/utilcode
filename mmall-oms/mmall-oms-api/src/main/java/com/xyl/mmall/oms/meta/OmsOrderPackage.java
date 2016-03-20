package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;

@AnnonOfClass(desc = "oms订单包裹", tableName = "Mmall_Oms_OmsOrderPackage")
public class OmsOrderPackage implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "包裹Id", primary = true, autoAllocateId = true)
	private long packageId;

	@AnnonOfField(desc = "订单Id")
	private long omsOrderFormId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "快递号", type = "VARCHAR(64)", notNull = false)
	private String mailNO;

	@AnnonOfField(desc = "快递公司", type = "VARCHAR(64)")
	private String expressCompany;

	@AnnonOfField(desc = "包裹重量")
	private long weight;

	@AnnonOfField(desc = "包裹状态")
	private OmsOrderPackageState omsOrderPackageState;

	@AnnonOfField(desc = "包裹状态更新时间")
	private long packageStateUpdateTime;

	@AnnonOfField(desc = "包裹状态是否反馈给app")
	private boolean packageStateFeedBackToApp;

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public long getOmsOrderFormId() {
		return omsOrderFormId;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		this.omsOrderFormId = omsOrderFormId;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	public OmsOrderPackageState getOmsOrderPackageState() {
		return omsOrderPackageState;
	}

	public void setOmsOrderPackageState(OmsOrderPackageState omsOrderPackageState) {
		this.omsOrderPackageState = omsOrderPackageState;
	}

	public long getPackageStateUpdateTime() {
		return packageStateUpdateTime;
	}

	public void setPackageStateUpdateTime(long packageStateUpdateTime) {
		this.packageStateUpdateTime = packageStateUpdateTime;
	}

	public boolean isPackageStateFeedBackToApp() {
		return packageStateFeedBackToApp;
	}

	public void setPackageStateFeedBackToApp(boolean packageStateFeedBackToApp) {
		this.packageStateFeedBackToApp = packageStateFeedBackToApp;
	}

}
