package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 *
 */
@XmlRootElement(name = "RequestOrderStatusInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSSalesOrderUpdateDTO.class)
public class WmsOrderStatusUpdate {
	private String warehouse_code = "";

	private String owner_code = "";

	@MapField(WMSSalesOrderUpdateDTO.field_orderId)
	private String order_id = "";

	private String status = "";

	@MapField(WMSSalesOrderUpdateDTO.field_logisticCode)
	@XmlElement(name = "LogisticProviderId")
	private String logisticProviderId = "";

	private String product_code = "";

	@MapField(WMSSalesOrderUpdateDTO.field_note)
	private String remark = "";

	@MapField(value = WMSSalesOrderUpdateDTO.field_operaterTime, isDate = true)
	private String operate_time = "";

	@XmlElementWrapper(name = "ship_details")
	@XmlElement(name = "ship_detail")
	@MapField(WMSSalesOrderUpdateDTO.field_packages)
	private List<ShipDetail> list;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLogisticProviderId() {
		return logisticProviderId;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}

	public List<ShipDetail> getList() {
		return list;
	}

	public void setList(List<ShipDetail> list) {
		this.list = list;
	}

}
