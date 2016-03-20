/**
 * 
 */
package com.xyl.mmall.oms.warehouse.exception;

import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * @author hzzengchengyuan
 * 
 */
public class WarehouseAdapterNotFoundException extends WarehouseCallerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WarehouseType type;

	public WarehouseAdapterNotFoundException(WarehouseType type) {
		super(type + " warehouse adapter not found.");
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public WarehouseType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(WarehouseType type) {
		this.type = type;
	}
}
