package com.xyl.mmall.oms.warehouse.adapter.ems;

import javax.xml.bind.annotation.*;

import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

import java.util.List;

@XmlRootElement(name = "RequestOrder")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(WMSShipOutOrderDTO.class)
public class WmsStockOutNotice {

	@MapField(WMSShipOutOrderDTO.field_warehouseCode)
    private String warehouse_code = "";

	@MapField(WMSShipOutOrderDTO.field_ownerCode)
    private String owner_code = "";

	@MapField(WMSShipOutOrderDTO.field_orderId)
    private String order_id = "";

    private String order_type_code = "";

    private String order_type_name = "";

    /**
     * 物流信息
     */
    private String LogisticProviderId = "";

    private String LogisticProviderName = "";

    /**
     * 收货方信息
     */
    private String receiver_code = "";

    private String receiver_name = "";

    @MapField(WMSShipOutOrderDTO.field_receiverName)
    private String consignee = "";

    @MapField(WMSShipOutOrderDTO.field_receiverPostCode)
    private String post_code = "";

    @MapField(WMSShipOutOrderDTO.field_receiverPhone)
    private String phone;

    @MapField(WMSShipOutOrderDTO.field_receiverAddress)
    private String address;

    /**
     * 货物信息
     */
    @MapField
    private long count;
    
    @MapField(WMSShipOutOrderDTO.field_skuCount)
    private long sku_count;

    /**
     * 附加信息
     */
    @MapField(WMSShipOutOrderDTO.field_note)
    private String remark;

    @XmlElementWrapper(name = "details")
    @XmlElement(name = "detail")
    @MapField(WMSShipOutOrderDTO.field_skuDetails)
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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getLogisticProviderId() {
        return LogisticProviderId;
    }

    public void setLogisticProviderId(String logisticProviderId) {
        LogisticProviderId = logisticProviderId;
    }

    public String getLogisticProviderName() {
        return LogisticProviderName;
    }

    public void setLogisticProviderName(String logisticProviderName) {
        LogisticProviderName = logisticProviderName;
    }

    public String getReceiver_code() {
        return receiver_code;
    }

    public void setReceiver_code(String receiver_code) {
        this.receiver_code = receiver_code;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public List<EmsSku> getDetails() {
        return details;
    }

    public void setDetails(List<EmsSku> details) {
        this.details = details;
    }
}
