package com.xyl.mmall.oms.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.xyl.mmall.oms.meta.OmsReturnOrderForm;

/**
 * 退货记录
 * 
 */
public class ReturnOrderFormDTO extends OmsReturnOrderForm {

	private String consigneeName;

	private String consigneeMobile;

	private String time;

	private String warehouseSaleId;
	
	private long warehouseId;
	
	private String warehouseName;
	
	private String returnId;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5884591176628384569L;

	private List<ReturnOrderFormSkuDTO> returnOrderFormSkuDTOList;

	public List<ReturnOrderFormSkuDTO> getReturnOrderFormSkuDTOList() {
		return returnOrderFormSkuDTOList;
	}

	public void setReturnOrderFormSkuDTOList(List<ReturnOrderFormSkuDTO> returnOrderFormSkuDTOList) {
		this.returnOrderFormSkuDTOList = returnOrderFormSkuDTOList;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(this.getCtime()));
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWarehouseSaleId() {
		return warehouseSaleId;
	}

	public void setWarehouseSaleId(String warehouseSaleId) {
		this.warehouseSaleId = warehouseSaleId;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getReturnId() {
		return "R_"+this.getId();
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	
	

}