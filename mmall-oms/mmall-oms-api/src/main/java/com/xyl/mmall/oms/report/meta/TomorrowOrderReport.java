package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 
 * @author chengximing
 *
 */
@AnnonOfClass(desc = "明日生产任务", tableName = "Mmall_Oms_Report_TomorrowOrderReport")
public class TomorrowOrderReport implements Serializable {

	private static final long serialVersionUID = -3499367153119666406L;

	@AnnonOfField(desc = "明日生产任务主键id", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "仓库id")
	private long warehouseId;
	
	@AnnonOfField(desc = "时间戳")
	private long time;
	
	@AnnonOfField(desc = "产生总订单")
	private int totalOrderNumber;
	
	@AnnonOfField(desc = "汇总订单")
	private int collectiveOrderNumber;
	
	@AnnonOfField(desc = "取消订单")
	private int cancelOrderNumber;
	
	@AnnonOfField(desc = "快递公司", type = "VARCHAR(25)")
	private String expressCompany;
	
	@AnnonOfField(desc = "仓库名字", type = "VARCHAR(25)")
	private String warehouse;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getTotalOrderNumber() {
		return totalOrderNumber;
	}

	public void setTotalOrderNumber(int totalOrderNumber) {
		this.totalOrderNumber = totalOrderNumber;
	}

	public int getCollectiveOrderNumber() {
		return collectiveOrderNumber;
	}

	public void setCollectiveOrderNumber(int collectiveOrderNumber) {
		this.collectiveOrderNumber = collectiveOrderNumber;
	}

	public int getCancelOrderNumber() {
		return cancelOrderNumber;
	}

	public void setCancelOrderNumber(int cancelOrderNumber) {
		this.cancelOrderNumber = cancelOrderNumber;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}
	
}
