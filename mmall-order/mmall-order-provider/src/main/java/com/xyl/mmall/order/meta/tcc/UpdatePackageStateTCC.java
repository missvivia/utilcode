package com.xyl.mmall.order.meta.tcc;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.order.enums.OrderPackageState;

/**
 * 更新包裹状态的TCC
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "更新包裹状态的TCC", tableName = "Mmall_Order_UpdatePackageStateTCC")
public class UpdatePackageStateTCC implements TCCMetaInterface {

	@AnnonOfField(desc = "packageId", primary = true, primaryIndex = 1)
	private long packageId;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	@AnnonOfField(desc = "包裹状态-更新前")
	private OrderPackageState oriState;

	@AnnonOfField(desc = "包裹状态-更新后")
	private OrderPackageState newState;

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public OrderPackageState getOriState() {
		return oriState;
	}

	public void setOriState(OrderPackageState oriState) {
		this.oriState = oriState;
	}

	public OrderPackageState getNewState() {
		return newState;
	}

	public void setNewState(OrderPackageState newState) {
		this.newState = newState;
	}

	public long getCtimeOfTCC() {
		return ctimeOfTCC;
	}

	public void setCtimeOfTCC(long ctimeOfTCC) {
		this.ctimeOfTCC = ctimeOfTCC;
	}

	public long getTranId() {
		return tranId;
	}

	public void setTranId(long tranId) {
		this.tranId = tranId;
	}
}
