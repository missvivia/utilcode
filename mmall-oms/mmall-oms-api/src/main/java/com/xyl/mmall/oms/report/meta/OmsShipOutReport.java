package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author zb
 *
 */
@AnnonOfClass(desc = "oms入库报表", tableName = "Mmall_Oms_Report_ShipOutReport")
public class OmsShipOutReport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "日期")
	private long dateTime;
	
	@AnnonOfField(desc = "商家ID")
	private long supplierId;
	
	@AnnonOfField(desc = "商家名字")
	private String supplierName;
	
	@AnnonOfField(desc = "仓库Id", policy = true)
	private long warehouseId;
	
	@AnnonOfField(desc = "仓库名字")
	private String warehouseName;
	
	@AnnonOfField(desc = "总数")
	private int total;
	
	@AnnonOfField(desc = "仓库到货时间")
	private long arrivalTime;
	
	@AnnonOfField(desc = "实际到货-良品数量")
	private int arrivedNormalCount;
	
	/**
	 * 得分
	 */
	@AnnonOfField(desc = "得分")
	private BigDecimal score;
	
	/**
	 * 良品率
	 */
	@AnnonOfField(desc = "良品率")
	private BigDecimal normalRatio;
	
	/**
	 * 到货及时率
	 */
	@AnnonOfField(desc = "到货及时率")
	private BigDecimal arrivalRatio;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getArrivedNormalCount() {
		return arrivedNormalCount;
	}

	public void setArrivedNormalCount(int arrivedNormalCount) {
		this.arrivedNormalCount = arrivedNormalCount;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public BigDecimal getNormalRatio() {
		return normalRatio;
	}

	public void setNormalRatio(BigDecimal normalRatio) {
		this.normalRatio = normalRatio;
	}

	public BigDecimal getArrivalRatio() {
		return arrivalRatio;
	}

	public void setArrivalRatio(BigDecimal arrivalRatio) {
		this.arrivalRatio = arrivalRatio;
	}

	
	
}
