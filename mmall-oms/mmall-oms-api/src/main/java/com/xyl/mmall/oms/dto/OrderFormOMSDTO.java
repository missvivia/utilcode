package com.xyl.mmall.oms.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.xyl.mmall.oms.meta.OmsOrderForm;

/**
 * @author zb<br>
 * 
 */
public class OrderFormOMSDTO extends OmsOrderForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	private List<OrderSkuOMSDTO> orderSkuOMSDTOList;

	private String warehouseName;
	
	private String state;
	
	private String time;
	
	private String warehouseSaleId;

	public List<OrderSkuOMSDTO> getOrderSkuOMSDTOList() {
		return orderSkuOMSDTOList;
	}

	public void setOrderSkuOMSDTOList(List<OrderSkuOMSDTO> orderSkuOMSDTOList) {
		this.orderSkuOMSDTOList = orderSkuOMSDTOList;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseSaleId() {
		return "S_" + this.getOmsOrderFormId();
	}

	public void setWarehouseSaleId(String warehouseSaleId) {
	}

	public String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(this.getCreateTime()));
	}

	public void setTime(String time) {
	}

	public String getState() {
		return this.getOmsOrderFormState().getName();
	}

	public void setState(String state) {
		this.state = state;
	}

	
}
