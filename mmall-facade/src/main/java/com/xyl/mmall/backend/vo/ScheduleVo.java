/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengchengyuan
 *
 */
public class ScheduleVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long scheduleId;
	
	private String supplierAcct;
	
	private String brandName;
	
	private String companyName;
	
	private String supplierWarehouseName;

	private String brandWarehouseName;
	
	private long startTime;
	
	private long endTime;
	
	/**
	 * 该PO是否可以创建退供单
	 */
	private boolean ableCreate = false;

	public ScheduleVo() {

	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getSupplierAcct() {
		return supplierAcct;
	}

	public void setSupplierAcct(String supplierAcct) {
		this.supplierAcct = supplierAcct;
	}

	public String getSupplierWarehouseName() {
		return supplierWarehouseName;
	}

	public void setSupplierWarehouseName(String supplierWarehouseName) {
		this.supplierWarehouseName = supplierWarehouseName;
	}

	public String getBrandWarehouseName() {
		return brandWarehouseName;
	}

	public void setBrandWarehouseName(String brandWarehouseName) {
		this.brandWarehouseName = brandWarehouseName;
	}

	public String getWarehouseNames() {
		String[] addressItem = new String[] { getSupplierWarehouseName(), getBrandWarehouseName() };
		StringBuilder sb = new StringBuilder();
		boolean preEmpty = true;
		for (String item : addressItem) {
			if (!preEmpty) {
				sb.append(",");
			}
			boolean currentEmpty = isBlank(item);
			if (!currentEmpty) {
				sb.append(item);
			}
			preEmpty = currentEmpty;
		}
		return sb.toString();
	}

	private boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isAbleCreate() {
		return ableCreate;
	}

	public void setAbleCreate(boolean ableCreate) {
		this.ableCreate = ableCreate;
	}
	
}
