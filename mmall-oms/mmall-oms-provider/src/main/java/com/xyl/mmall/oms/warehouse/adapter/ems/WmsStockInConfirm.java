package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

@XmlRootElement(name = "RequestReceiveInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSShipOrderUpdateDTO.class)
public class WmsStockInConfirm {
	private String warehouse_code = "";

	private String owner_code = "";

	/**
	 * 订单类型，目前ems接口返回还没有该字段
	 */
	private String order_type = "";

	@MapField(WMSShipOrderUpdateDTO.field_orderId)
	private String asn_id = "";

	@MapField(value = WMSShipOrderUpdateDTO.field_operaterTime, isDate = true)
	private String receive_time;

	@XmlElementWrapper(name = "details")
	@XmlElement(name = "detail")
	@MapField(WMSShipOrderUpdateDTO.field_skuDetails)
	private List<EmsSku> details;

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

	/**
	 * @return the order_type
	 */
	public String getOrder_type() {
		return order_type;
	}

	/**
	 * @param order_type
	 *            the order_type to set
	 */
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getAsn_id() {
		return asn_id;
	}

	public void setAsn_id(String asn_id) {
		this.asn_id = asn_id;
	}

	public String getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}

	public List<EmsSku> getDetails() {
		return details;
	}

	public void setDetails(List<EmsSku> details) {
		this.details = details;
	}
}
