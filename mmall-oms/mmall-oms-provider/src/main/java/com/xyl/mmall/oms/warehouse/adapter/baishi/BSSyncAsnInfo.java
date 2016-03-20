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

import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * 百世入库单
 * 
 * @author hzzengchengyuan
 */
@XmlRootElement(name = "SyncAsnInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(value = WMSShipOrderDTO.class, isAllField = true)
public class BSSyncAsnInfo implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	public static final String ACTIONTYPE_ADD = "ADD";

	public static final String ACTIONTYPE_CANCEL = "CANCEL";

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
	 * 操作类型：ADD-新增, CANCEL-取消. 是否可以为空：N
	 */
	private String actionType;

	/**
	 * 对应订单号（无库存发货用）.是否可以为空：Y
	 */
	private String refOrderCode;

	/**
	 * 客户业务单号.是否可以为空：Y
	 */
	private String extTradeId;

	/**
	 * 入库单外部类型.是否可以为空：Y
	 */
	private String extOrderType;

	/**
	 * 最早到货时间.是否可以为空：Y
	 */
	@MapField(value = "sendTime", isDate = true)
	private String earliestArrivalTime;

	/**
	 * 最晚到货时间.是否可以为空：Y
	 */
	private String latestArrivalTime;

	/**
	 * 运输公司名称.是否可以为空：Y
	 */
	@MapField("logisticCompany")
	private String tmsCompany;

	/**
	 * 运输公司联系人.是否可以为空：Y
	 */
	@MapField("logisticLinkman")
	private String tmsLinkman;

	/**
	 * 运输公司电话.是否可以为空：Y
	 */
	@MapField("logisticPhone")
	private String tmsPhone;

	/**
	 * 运输公司联系人身份证号.是否可以为空：Y
	 */
	private String tmsLinkmanNo;

	/**
	 * 运输公司运单号.是否可以为空：Y
	 */
	@MapField("logisticNo")
	private String tmsShippingNo;

	/**
	 * 备注.是否可以为空：Y
	 */
	@MapField
	private String note;

	/**
	 * 地址编码（百世主动分仓功能使用，作为逻辑仓与物理仓对应）.是否可以为空：Y
	 */
	private String warehouseAddressCode;

	/**
	 * 发货人信息
	 */
	@XmlElement(name = "sender")
	@MapClass
	private BSSender sender;

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
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * @param actionType
	 *            the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the refOrderCode
	 */
	public String getRefOrderCode() {
		return refOrderCode;
	}

	/**
	 * @param refOrderCode
	 *            the refOrderCode to set
	 */
	public void setRefOrderCode(String refOrderCode) {
		this.refOrderCode = refOrderCode;
	}

	/**
	 * @return the extTradeId
	 */
	public String getExtTradeId() {
		return extTradeId;
	}

	/**
	 * @param extTradeId
	 *            the extTradeId to set
	 */
	public void setExtTradeId(String extTradeId) {
		this.extTradeId = extTradeId;
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
	 * @return the earliestArrivalTime
	 */
	public String getEarliestArrivalTime() {
		return earliestArrivalTime;
	}

	/**
	 * @param earliestArrivalTime
	 *            the earliestArrivalTime to set
	 */
	public void setEarliestArrivalTime(String earliestArrivalTime) {
		this.earliestArrivalTime = earliestArrivalTime;
	}

	/**
	 * @return the latestArrivalTime
	 */
	public String getLatestArrivalTime() {
		return latestArrivalTime;
	}

	/**
	 * @param latestArrivalTime
	 *            the latestArrivalTime to set
	 */
	public void setLatestArrivalTime(String latestArrivalTime) {
		this.latestArrivalTime = latestArrivalTime;
	}

	/**
	 * @return the tmsCompany
	 */
	public String getTmsCompany() {
		return tmsCompany;
	}

	/**
	 * @param tmsCompany
	 *            the tmsCompany to set
	 */
	public void setTmsCompany(String tmsCompany) {
		this.tmsCompany = tmsCompany;
	}

	/**
	 * @return the tmsLinkman
	 */
	public String getTmsLinkman() {
		return tmsLinkman;
	}

	/**
	 * @param tmsLinkman
	 *            the tmsLinkman to set
	 */
	public void setTmsLinkman(String tmsLinkman) {
		this.tmsLinkman = tmsLinkman;
	}

	/**
	 * @return the tmsPhone
	 */
	public String getTmsPhone() {
		return tmsPhone;
	}

	/**
	 * @param tmsPhone
	 *            the tmsPhone to set
	 */
	public void setTmsPhone(String tmsPhone) {
		this.tmsPhone = tmsPhone;
	}

	/**
	 * @return the tmsLinkmanNo
	 */
	public String getTmsLinkmanNo() {
		return tmsLinkmanNo;
	}

	/**
	 * @param tmsLinkmanNo
	 *            the tmsLinkmanNo to set
	 */
	public void setTmsLinkmanNo(String tmsLinkmanNo) {
		this.tmsLinkmanNo = tmsLinkmanNo;
	}

	/**
	 * @return the tmsShippingNo
	 */
	public String getTmsShippingNo() {
		return tmsShippingNo;
	}

	/**
	 * @param tmsShippingNo
	 *            the tmsShippingNo to set
	 */
	public void setTmsShippingNo(String tmsShippingNo) {
		this.tmsShippingNo = tmsShippingNo;
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
	 * @return the warehouseAddressCode
	 */
	public String getWarehouseAddressCode() {
		return warehouseAddressCode;
	}

	/**
	 * @param warehouseAddressCode
	 *            the warehouseAddressCode to set
	 */
	public void setWarehouseAddressCode(String warehouseAddressCode) {
		this.warehouseAddressCode = warehouseAddressCode;
	}

	/**
	 * @return the sender
	 */
	public BSSender getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(BSSender sender) {
		this.sender = sender;
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
