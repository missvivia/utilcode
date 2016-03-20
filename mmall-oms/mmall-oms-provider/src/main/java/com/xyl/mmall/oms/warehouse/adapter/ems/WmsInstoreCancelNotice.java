/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 *
 */
@XmlRootElement(name = "RequestCancelOrder")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSShipOrderDTO.class)
public class WmsInstoreCancelNotice implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	@MapField(WMSShipOrderDTO.field_warehouseCode)
	private String warehouse_code = "";

	@MapField(WMSShipOrderDTO.field_ownerCode)
	private String owner_code = "";

	@MapField(WMSShipOrderDTO.field_orderId)
	private String asn_id = "";

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

	public String getAsn_id() {
		return asn_id;
	}

	public void setAsn_id(String asn_id) {
		this.asn_id = asn_id;
	}

}
