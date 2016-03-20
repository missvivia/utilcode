/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.meta.WarehouseForm;

/**
 * @author zb
 *
 */
public class PoOrderStatisticVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "实际到货-总数量")
	private int arrivedQuantity;

	@AnnonOfField(desc = "实际到货-不良品数量")
	private int arrivedDefectiveCount;

	@AnnonOfField(desc = "实际到货-退货数量")
	private int arrivedRefundCount;

	/**
	 * 供应商id
	 */
	private long supplierId;
	
	private long skuId;

	/**
	 * 仓库
	 */
	private WarehouseForm warehouseForm;

	public int getArrivedQuantity() {
		return arrivedQuantity;
	}

	public void setArrivedQuantity(int arrivedQuantity) {
		this.arrivedQuantity = arrivedQuantity;
	}

	public int getArrivedDefectiveCount() {
		return arrivedDefectiveCount;
	}

	public void setArrivedDefectiveCount(int arrivedDefectiveCount) {
		this.arrivedDefectiveCount = arrivedDefectiveCount;
	}


	public int getArrivedRefundCount() {
		return arrivedRefundCount;
	}

	public void setArrivedRefundCount(int arrivedRefundCount) {
		this.arrivedRefundCount = arrivedRefundCount;
	}

	public WarehouseForm getWarehouseForm() {
		return warehouseForm;
	}

	public void setWarehouseForm(WarehouseForm warehouseForm) {
		this.warehouseForm = warehouseForm;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

}
