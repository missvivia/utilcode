/**
 * 
 */
package com.xyl.mmall.oms.warehouse.exception;

import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * @author hzzengchengyuan
 * 
 */
public class WarehouseAdapterCallbackNotFoundException extends WarehouseCallerException {
	private WarehouseType type;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WarehouseAdapterCallbackNotFoundException(WarehouseType type) {
		super(type + " warehouse adapter callback not found.");
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public WarehouseType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(WarehouseType type) {
		this.type = type;
	}

}
