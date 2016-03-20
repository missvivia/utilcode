/**
 * 
 */
package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.meta.ReturnPoOrderFormSku;
import com.xyl.mmall.oms.meta.WarehouseReturn;

/**
 * @author hzzengchengyuan
 *
 */
public class ReturnPoOrderFormSkuDTO extends ReturnPoOrderFormSku {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 原始退货明细id，{@link WarehouseReturn#getId()}
	 */
	private long warehouseReturnId;

	public ReturnPoOrderFormSkuDTO() {

	}

	public ReturnPoOrderFormSkuDTO(ReturnPoOrderFormSku formSku) {
		ReflectUtil.convertObj(this, formSku, false);
	}

	public ReturnPoOrderFormSkuDTO(WarehouseReturn warehouseReturn) {
		super(warehouseReturn);
		this.setWarehouseReturnId(warehouseReturn.getId());
	}

	/**
	 * @return the warehouseReturnId
	 */
	public long getWarehouseReturnId() {
		return warehouseReturnId;
	}

	/**
	 * @param warehouseReturnId
	 *            the warehouseReturnId to set
	 */
	public void setWarehouseReturnId(long warehouseReturnId) {
		this.warehouseReturnId = warehouseReturnId;
	}

}
