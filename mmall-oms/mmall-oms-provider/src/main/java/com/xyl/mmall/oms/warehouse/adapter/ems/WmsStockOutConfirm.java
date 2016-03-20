/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * 出库单确认
 * 
 * @author hzzengchengyuan
 *
 */
@XmlRootElement(name = "RequestConsignInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSShipOutOrderUpdateDTO.class)
public class WmsStockOutConfirm {
	private String owner_code;

	private String warehouse_code;

	@MapField(WMSShipOutOrderUpdateDTO.field_orderId)
	private String order_id;

	/**
	 * 出库完成时间（格式：yyyy-MM-dd HH:mm:ss）
	 */
	@MapField(value = WMSShipOutOrderUpdateDTO.field_operaterTime, isDate = true)
	private String consign_time;

	@MapField(value = WMSShipOutOrderUpdateDTO.field_logisticCode, isDate = true)
	private String logisticProviderId;
	
	@XmlElementWrapper(name = "details")
	@XmlElement(name = "detail")
	@MapField(WMSShipOutOrderUpdateDTO.field_skuDetails)
	private List<SkuDetail> list;

	/**
	 * @return the owner_code
	 */
	public String getOwner_code() {
		return owner_code;
	}

	/**
	 * @param owner_code
	 *            the owner_code to set
	 */
	public void setOwner_code(String owner_code) {
		this.owner_code = owner_code;
	}

	/**
	 * @return the warehouse_code
	 */
	public String getWarehouse_code() {
		return warehouse_code;
	}

	/**
	 * @param warehouse_code
	 *            the warehouse_code to set
	 */
	public void setWarehouse_code(String warehouse_code) {
		this.warehouse_code = warehouse_code;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the consign_time
	 */
	public String getConsign_time() {
		return consign_time;
	}

	/**
	 * @param consign_time
	 *            the consign_time to set
	 */
	public void setConsign_time(String consign_time) {
		this.consign_time = consign_time;
	}

	public List<SkuDetail> getList() {
		return list;
	}

	public void setList(List<SkuDetail> list) {
		this.list = list;
	}

	/**
	 * @return the logisticProviderId
	 */
	public String getLogisticProviderId() {
		return logisticProviderId;
	}

	/**
	 * @param logisticProviderId the logisticProviderId to set
	 */
	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

}
