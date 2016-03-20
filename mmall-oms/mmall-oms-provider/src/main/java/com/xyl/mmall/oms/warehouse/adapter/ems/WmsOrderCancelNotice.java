package com.xyl.mmall.oms.warehouse.adapter.ems;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

@XmlRootElement(name = "RequestCancelOrder")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSSalesOrderDTO.class)
public class WmsOrderCancelNotice {
	@MapField(WMSSalesOrderDTO.field_warehouseCode)
	private String warehouse_code = "";

	@MapField(WMSSalesOrderDTO.field_ownerCode)
	private String owner_code = "";
	
	@MapField(WMSSalesOrderDTO.field_orderId)
	private String order_id = "";

	public String getWarehouse_code() {
		return warehouse_code;
	}

	public void setWarehouse_code(String warehouse_code) {
		this.warehouse_code = warehouse_code;
	}

	public String getOwner_code() {
		return owner_code;
	}

	public void setOwner_code(String owner_code) {
		this.owner_code = owner_code;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	
	
}
