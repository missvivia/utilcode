package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 *
 */
@XmlRootElement(name = "RequestASN")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSReturnOrderDTO.class)
public class WmsReturnOrderNotice {
	@MapField(WMSReturnOrderDTO.field_warehouseCode)
	private String warehouse_code = "";

	@MapField(WMSReturnOrderDTO.field_ownerCode)
	private String owner_code = "";

	@MapField(WMSReturnOrderDTO.field_orderId)
	private String asn_id = "";

	/**
	 * 订单类型
	 */
	private String order_type_code = "";

	private String order_type_name = "";

	/**
	 * 退货信息
	 */
	@MapField(WMSReturnOrderDTO.field_originalOrderId)
	private String sale_order_id = "";

	/**
	 * 发货方信息
	 */
	private String sender_code = "";

	@MapField(WMSReturnOrderDTO.field_userName)
	private String sender_name = "";

	private String sender_contact = "";

	@MapField(WMSReturnOrderDTO.field_userPhone)
	private String sender_phone = "";

	/**
	 * 物流信息
	 */
	@MapField(WMSReturnOrderDTO.field_logisticCode)
	private String logisticProviderId = "";

	@MapField(WMSReturnOrderDTO.field_logisticCompany)
	private String logisticProviderName = "";

	@MapField(WMSReturnOrderDTO.field_logisticNo)
	private String tms_order_code = "";

	@MapField(value = WMSReturnOrderDTO.field_returnTime, isDate=true)
	private String send_time = "";

	private String pre_arrive_time = "";

	/**
	 * 商品计数
	 */
	@MapField
	private long count;

	@MapField(WMSReturnOrderDTO.field_skuCount)
	private long sku_count;

	/**
	 * 其他信息
	 */
	@MapField(WMSReturnOrderDTO.field_note)
	private String remark = "";

	@XmlElementWrapper(name = "details")
	@XmlElement(name = "detail")
	@MapField(WMSReturnOrderDTO.field_skuDetails)
	List<SkuDetail> details;

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

	public String getOrder_type_code() {
		return order_type_code;
	}

	public void setOrder_type_code(String order_type_code) {
		this.order_type_code = order_type_code;
	}

	public String getOrder_type_name() {
		return order_type_name;
	}

	public void setOrder_type_name(String order_type_name) {
		this.order_type_name = order_type_name;
	}

	public String getSale_order_id() {
		return sale_order_id;
	}

	public void setSale_order_id(String sale_order_id) {
		this.sale_order_id = sale_order_id;
	}

	public String getSender_code() {
		return sender_code;
	}

	public void setSender_code(String sender_code) {
		this.sender_code = sender_code;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSender_contact() {
		return sender_contact;
	}

	public void setSender_contact(String sender_contact) {
		this.sender_contact = sender_contact;
	}

	public String getSender_phone() {
		return sender_phone;
	}

	public void setSender_phone(String sender_phone) {
		this.sender_phone = sender_phone;
	}


	public String getTms_order_code() {
		return tms_order_code;
	}

	public void setTms_order_code(String tms_order_code) {
		this.tms_order_code = tms_order_code;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public String getPre_arrive_time() {
		return pre_arrive_time;
	}

	public void setPre_arrive_time(String pre_arrive_time) {
		this.pre_arrive_time = pre_arrive_time;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getSku_count() {
		return sku_count;
	}

	public void setSku_count(long sku_count) {
		this.sku_count = sku_count;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<SkuDetail> getDetails() {
		return details;
	}

	public void setDetails(List<SkuDetail> details) {
		this.details = details;
	}

	public String getLogisticProviderId() {
		return logisticProviderId;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public String getLogisticProviderName() {
		return logisticProviderName;
	}

	public void setLogisticProviderName(String logisticProviderName) {
		this.logisticProviderName = logisticProviderName;
	}
	
	
}
