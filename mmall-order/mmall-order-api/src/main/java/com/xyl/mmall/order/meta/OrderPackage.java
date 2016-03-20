package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.enums.OrderPackageState;

/**
 * 订单包裹
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "订单包裹", tableName = "Mmall_Order_OrderPackage", dbCreateTimeName = "CreateTime")
public class OrderPackage implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "包裹Id", primary = true, autoAllocateId = true)
	private long packageId;

	@AnnonOfField(desc = "订单Id")
	private long orderId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "快递号", type = "VARCHAR(32)", notNull = false)
	private String mailNO;

	@Deprecated
	@AnnonOfField(desc = "快递公司-用户选择的")
	private ExpressCompany expressCompany;

	@AnnonOfField(desc = "快递公司-ERP回传", type = "VARCHAR(16)", notNull = false)
	private String expressCompanyReturn;

	@AnnonOfField(desc = "包裹的状态")
	private OrderPackageState orderPackageState;

	@AnnonOfField(desc = "下单时间")
	private long orderTime;

	@AnnonOfField(desc = "OMS接单时间/拆分包裹时间")
	private long omsTime;

	@AnnonOfField(desc = "快递开始时间")
	private long expSTime;

	@AnnonOfField(desc = "快递签收时间")
	private long confirmTime;

	@AnnonOfField(desc = "取消时间")
	private long cancelTime;

	@AnnonOfField(desc = "仓库Id")
	private long warehouseId;

	@AnnonOfField(desc = "仓库名称", type = "VARCHAR(32)")
	private String warehouseName = "";

	@AnnonOfField(desc = "客服重新开启退货申请入口")
	private boolean kfReopenReturn = false;

	@AnnonOfField(desc = "客服重新开启退货申请入口的时间(kfReopenReturn=true时有效)")
	private long reopenReturnTime;

	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getExpressCompanyReturn() {
		return expressCompanyReturn;
	}

	public void setExpressCompanyReturn(String expressCompanyReturn) {
		this.expressCompanyReturn = expressCompanyReturn;
	}

	public long getOmsTime() {
		return omsTime;
	}

	public void setOmsTime(long omsTime) {
		this.omsTime = omsTime;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public OrderPackageState getOrderPackageState() {
		return orderPackageState;
	}

	public void setOrderPackageState(OrderPackageState orderPackageState) {
		this.orderPackageState = orderPackageState;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
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

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	@Deprecated
	public ExpressCompany getExpressCompany() {
		return expressCompany;
	}

	@Deprecated
	public void setExpressCompany(ExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}

	public long getExpSTime() {
		return expSTime;
	}

	public void setExpSTime(long expSTime) {
		this.expSTime = expSTime;
	}

	public boolean isKfReopenReturn() {
		return kfReopenReturn;
	}

	public void setKfReopenReturn(boolean kfReopenReturn) {
		this.kfReopenReturn = kfReopenReturn;
	}

	public long getReopenReturnTime() {
		return reopenReturnTime;
	}

	public void setReopenReturnTime(long reopenReturnTime) {
		this.reopenReturnTime = reopenReturnTime;
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