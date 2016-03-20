/**
 * 
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.WarehouseLogState;

/**
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "仓储系统日志详情表", tableName = "Mmall_Oms_WarehouseLog")
public class WarehouseLog implements Serializable {

	private static final long serialVersionUID = 3887608090979357584L;

	@AnnonOfField(desc = "ID", primary = true, primaryIndex = 1, autoAllocateId = true, policy = true)
	private long id;

	@AnnonOfField(desc = "仓储类别")
	private String warehouseCode = "";

	@AnnonOfField(desc = "日志类别")
	private String type = "";

	@AnnonOfField(desc = "日志所处状态")
	private WarehouseLogState state;

	@AnnonOfField(desc = "日志创建时间")
	private String time = "";

	@AnnonOfField(desc = "日志详情")
	private String details = "";

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the warehouseCode
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/**
	 * @param warehouseCode
	 *            the warehouseCode to set
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the state
	 */
	public WarehouseLogState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(WarehouseLogState state) {
		this.state = state;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

}