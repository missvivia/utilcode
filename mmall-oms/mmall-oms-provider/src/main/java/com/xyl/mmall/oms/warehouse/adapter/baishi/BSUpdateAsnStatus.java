/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.baishi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 * 
 */
@XmlRootElement(name = "UpdateAsnStatus")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(ShipOrderDTO.class)
public class BSUpdateAsnStatus implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	/**
	 * 卖家编码，保证唯一。是否可以为空：N
	 */
	@MapField("ownerCode")
	private String customerCode;

	/**
	 * 仓库代码。是否可以为空：N
	 */
	@MapField
	private String warehouseCode;

	/**
	 * 仓储入库通知单号，保证唯一. 是否可以为空：N
	 */
	@MapField("orderId")
	private String asnCode;

	/**
	 * 补货单状态. 是否可以为空：N
	 */
	private BSAsnStatus asnStatus;

	/**
	 * 入库单外部类型.是否可以为空：Y
	 */
	private String extOrderType;

	/**
	 * 送货配送公司代码.是否可以为空：Y
	 */
	@MapField("logisticCode")
	private String logisticsProviderCode;

	/**
	 * 送货运单号.是否可以为空：Y
	 */
	@MapField("logisticNo")
	private String shippingOrderNo;

	/**
	 * 备注或失败原因.是否可以为空：Y
	 */
	@MapField
	private String note;

	/**
	 * 收货时间.是否可以为空：Y
	 */
	@MapField("receiveTime")
	private String receiveTime;

	/**
	 * 是否使用udf字段.是否可以为空：Y
	 */
	private String udfFlag;

	/**
	 * WMS订单头自定义字段.是否可以为空：Y
	 */
	private String udf1;

	/**
	 * WMS订单头自定义字段.是否可以为空：Y
	 */
	private String udf2;

	/**
	 * WMS订单头自定义字段.是否可以为空：Y
	 */
	private String udf3;

	// 百世中有定义8个自定义字段，这里 暂时忽略

	/**
	 * 是否WMS手工创建.是否可以为空：Y
	 */
	private String wmsCreatedFlag;

	/**
	 * 主波次号. 是否可以为空：Y
	 */
	private String waveCode;

	/**
	 * SKU上去详情
	 */
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	@MapField("skuDetails")
	private List<BSSkuDetail> items;

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	/**
	 * @param customerCode
	 *            the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	/**
	 * @return the warehouseCode
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/**
	 * @param warehouseCode
	 *            the warehouseCode to set
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * @return the asnCode
	 */
	public String getAsnCode() {
		return asnCode;
	}

	/**
	 * @param asnCode
	 *            the asnCode to set
	 */
	public void setAsnCode(String asnCode) {
		this.asnCode = asnCode;
	}

	/**
	 * @return the asnStatus
	 */
	public BSAsnStatus getAsnStatus() {
		return asnStatus;
	}

	/**
	 * @param asnStatus
	 *            the asnStatus to set
	 */
	public void setAsnStatus(BSAsnStatus asnStatus) {
		this.asnStatus = asnStatus;
	}

	/**
	 * @return the extOrderType
	 */
	public String getExtOrderType() {
		return extOrderType;
	}

	/**
	 * @param extOrderType
	 *            the extOrderType to set
	 */
	public void setExtOrderType(String extOrderType) {
		this.extOrderType = extOrderType;
	}

	/**
	 * @return the logisticsProviderCode
	 */
	public String getLogisticsProviderCode() {
		return logisticsProviderCode;
	}

	/**
	 * @param logisticsProviderCode
	 *            the logisticsProviderCode to set
	 */
	public void setLogisticsProviderCode(String logisticsProviderCode) {
		this.logisticsProviderCode = logisticsProviderCode;
	}

	/**
	 * @return the shippingOrderNo
	 */
	public String getShippingOrderNo() {
		return shippingOrderNo;
	}

	/**
	 * @param shippingOrderNo
	 *            the shippingOrderNo to set
	 */
	public void setShippingOrderNo(String shippingOrderNo) {
		this.shippingOrderNo = shippingOrderNo;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the receiveTime
	 */
	public String getReceiveTime() {
		return receiveTime;
	}

	/**
	 * @param receiveTime
	 *            the receiveTime to set
	 */
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	/**
	 * @return the udfFlag
	 */
	public String getUdfFlag() {
		return udfFlag;
	}

	/**
	 * @param udfFlag
	 *            the udfFlag to set
	 */
	public void setUdfFlag(String udfFlag) {
		this.udfFlag = udfFlag;
	}

	/**
	 * @return the udf1
	 */
	public String getUdf1() {
		return udf1;
	}

	/**
	 * @param udf1
	 *            the udf1 to set
	 */
	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}

	/**
	 * @return the udf2
	 */
	public String getUdf2() {
		return udf2;
	}

	/**
	 * @param udf2
	 *            the udf2 to set
	 */
	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}

	/**
	 * @return the udf3
	 */
	public String getUdf3() {
		return udf3;
	}

	/**
	 * @param udf3
	 *            the udf3 to set
	 */
	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}

	/**
	 * @return the wmsCreatedFlag
	 */
	public String getWmsCreatedFlag() {
		return wmsCreatedFlag;
	}

	/**
	 * @param wmsCreatedFlag
	 *            the wmsCreatedFlag to set
	 */
	public void setWmsCreatedFlag(String wmsCreatedFlag) {
		this.wmsCreatedFlag = wmsCreatedFlag;
	}

	/**
	 * @return the waveCode
	 */
	public String getWaveCode() {
		return waveCode;
	}

	/**
	 * @param waveCode
	 *            the waveCode to set
	 */
	public void setWaveCode(String waveCode) {
		this.waveCode = waveCode;
	}

	/**
	 * @return the items
	 */
	public List<BSSkuDetail> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<BSSkuDetail> items) {
		this.items = items;
	}

	public void addItem(BSSkuDetail item) {
		if (this.items == null) {
			this.items = new ArrayList<BSSkuDetail>();
		}
		this.items.add(item);
	}
}
