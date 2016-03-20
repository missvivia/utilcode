/**
 * 
 */
package com.xyl.mmall.oms.dto.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * 商品入库单
 * 
 * @author hzzengchengyuan
 */
public class WMSShipOrderDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 仓储入库单号
	 */
	private String orderId;

	/**
	 * 仓库机构编码(WMS提供)
	 */
	private String warehouseCode;

	/**
	 * 货主编码(WMS提供)
	 */
	private String ownerCode;

	/**
	 * 仓库类别
	 */
	private WarehouseType wmsType = WarehouseType.EMS;
	
	/**
	 * 入库单类别
	 */
	private WMSOrderType orderType = WMSOrderType.NULL;

	/**
	 * 发货人编码/ID
	 */
	private String senderCode;

	/**
	 * 发货人名称
	 */
	private String senderName;

	/**
	 * 发货人电话
	 */
	private String senderPhone;

	/**
	 * 发货人联系电话
	 */
	private String senderMobile;

	/**
	 * 发货人地址邮编
	 */
	private String senderrPostCode;

	/**
	 * 发货人详细地址
	 */
	private String senderAddress;

	/**
	 * 发货时间
	 */
	private long sendTime;

	/**
	 * 预计到货时间
	 */
	private long expectedTime;

	/**
	 * 运输公司代码（如：EMS,SF）
	 */
	private String logisticCode;

	/**
	 * 运输公司名称
	 */
	private String logisticCompany;

	/**
	 * 运输公司联系人
	 */
	private String logisticLinkman;

	/**
	 * 运输公司电话
	 */
	private String logisticPhone;

	/**
	 * 运输公司订单号
	 */
	private String logisticNo;

	/**
	 * 司机姓名
	 */
	private String driverName;

	/**
	 * 司机电话
	 */
	private String driverPhone;

	/**
	 * 车牌号
	 */
	private String carNumber;

	/**
	 * 发货箱数
	 */
	private int boxCount;

	/**
	 * 商品总件数。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}方法计算
	 */
	private int count = -1;

	/**
	 * 该入库单对应商品种数。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}方法计算
	 */
	private int skuCount = -1;

	/**
	 * 发货单商品明细
	 */
	private List<WMSSkuDetailDTO> skuDetails;

	/**
	 * 是否有不良品
	 */
	private boolean hasDefective = false;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	 * @return the ownerCode
	 */
	public String getOwnerCode() {
		return ownerCode;
	}

	/**
	 * @param ownerCode
	 *            the ownerCode to set
	 */
	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	/**
	 * @return the senderCode
	 */
	public String getSenderCode() {
		return senderCode;
	}

	/**
	 * @param senderCode
	 *            the senderCode to set
	 */
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName
	 *            the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @return the senderPhone
	 */
	public String getSenderPhone() {
		return senderPhone;
	}

	/**
	 * @param senderPhone
	 *            the senderPhone to set
	 */
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	/**
	 * @return the senderMobile
	 */
	public String getSenderMobile() {
		return senderMobile;
	}

	/**
	 * @param senderMobile
	 *            the senderMobile to set
	 */
	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}

	/**
	 * @return the senderrPostCode
	 */
	public String getSenderrPostCode() {
		return senderrPostCode;
	}

	/**
	 * @param senderrPostCode
	 *            the senderrPostCode to set
	 */
	public void setSenderrPostCode(String senderrPostCode) {
		this.senderrPostCode = senderrPostCode;
	}

	/**
	 * @return the senderAddress
	 */
	public String getSenderAddress() {
		return senderAddress;
	}

	/**
	 * @param senderAddress
	 *            the senderAddress to set
	 */
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	/**
	 * @return the wmsType
	 */
	public WarehouseType getWmsType() {
		return wmsType;
	}

	/**
	 * @param wmsType the wmsType to set
	 */
	public void setWmsType(WarehouseType wmsType) {
		this.wmsType = wmsType;
	}

	/**
	 * @return the orderType
	 */
	public WMSOrderType getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(WMSOrderType orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the sendTime
	 */
	public long getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime
	 *            the sendTime to set
	 */
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the expectedTime
	 */
	public long getExpectedTime() {
		return expectedTime;
	}

	/**
	 * @param expectedTime
	 *            the expectedTime to set
	 */
	public void setExpectedTime(long expectedTime) {
		this.expectedTime = expectedTime;
	}

	/**
	 * @return the logisticCode
	 */
	public String getLogisticCode() {
		return logisticCode;
	}

	/**
	 * @param logisticCode
	 *            the logisticCode to set
	 */
	public void setLogisticCode(String logisticCode) {
		this.logisticCode = logisticCode;
	}

	/**
	 * @return the logisticCompany
	 */
	public String getLogisticCompany() {
		return logisticCompany;
	}

	/**
	 * @param logisticCompany
	 *            the logisticCompany to set
	 */
	public void setLogisticCompany(String logisticCompany) {
		this.logisticCompany = logisticCompany;
	}

	/**
	 * @return the logisticLinkman
	 */
	public String getLogisticLinkman() {
		return logisticLinkman;
	}

	/**
	 * @param logisticLinkman
	 *            the logisticLinkman to set
	 */
	public void setLogisticLinkman(String logisticLinkman) {
		this.logisticLinkman = logisticLinkman;
	}

	/**
	 * @return the logisticPhone
	 */
	public String getLogisticPhone() {
		return logisticPhone;
	}

	/**
	 * @param logisticPhone
	 *            the logisticPhone to set
	 */
	public void setLogisticPhone(String logisticPhone) {
		this.logisticPhone = logisticPhone;
	}

	/**
	 * @return the logisticNo
	 */
	public String getLogisticNo() {
		return logisticNo;
	}

	/**
	 * @param logisticNo
	 *            the logisticNo to set
	 */
	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}

	/**
	 * @return the driverName
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName
	 *            the driverName to set
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return the driverPhone
	 */
	public String getDriverPhone() {
		return driverPhone;
	}

	/**
	 * @param driverPhone
	 *            the driverPhone to set
	 */
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	/**
	 * @return the carNumber
	 */
	public String getCarNumber() {
		return carNumber;
	}

	/**
	 * @param carNumber
	 *            the carNumber to set
	 */
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	/**
	 * @return the boxCount
	 */
	public int getBoxCount() {
		return boxCount;
	}

	/**
	 * @param boxCount
	 *            the boxCount to set
	 */
	public void setBoxCount(int boxCount) {
		this.boxCount = boxCount;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		if(this.count == -1) {
			calculateCount();
		}
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the skuCount
	 */
	public int getSkuCount() {
		if(this.skuCount == -1) {
			calculateCount();
		}
		return skuCount;
	}

	/**
	 * @param skuCount
	 *            the skuCount to set
	 */
	public void setSkuCount(int skuCount) {
		this.skuCount = skuCount;
	}

	/**
	 * @return the skuDetails
	 */
	public List<WMSSkuDetailDTO> getSkuDetails() {
		return skuDetails;
	}

	/**
	 * @param skuDetails
	 *            the skuDetails to set
	 */
	public void setSkuDetails(List<WMSSkuDetailDTO> skuDetails) {
		this.skuDetails = skuDetails;
	}

	public void addSkuDetail(WMSSkuDetailDTO skuDetail) {
		if (this.skuDetails == null) {
			this.skuDetails = new ArrayList<WMSSkuDetailDTO>();
		}
		this.skuDetails.add(skuDetail);
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the hasDefective
	 */
	public boolean isHasDefective() {
		return hasDefective;
	}

	/**
	 * @param hasDefective
	 *            the hasDefective to set
	 */
	public void setHasDefective(boolean hasDefective) {
		this.hasDefective = hasDefective;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	public void calculateCount() {
		int allNum = 0;
		List<Long> skuIdList = new ArrayList<Long>();
		if (this.skuDetails != null) {
			for (WMSSkuDetailDTO skuD : this.skuDetails) {
				skuD.calculateCount();
				if (!skuIdList.contains(skuD.getSkuId())) {
					skuIdList.add(skuD.getSkuId());
				}
				allNum += skuD.getCount();
				if (skuD.getDefectiveCount() > 0) {
					this.hasDefective = true;
				}
			}
		}
		this.count = allNum;
		this.skuCount = skuIdList.size();
	}
	
	//定义入库单属性名常量列表，不全大写主要便于引用时查看
	public static final String field_orderId = "orderId";
	public static final String field_warehouseCode = "warehouseCode";
	public static final String field_ownerCode = "ownerCode";
	public static final String field_orderType = "orderType";
	public static final String field_senderCode = "senderCode";
	public static final String field_senderName = "senderName";
	public static final String field_senderPhone = "senderPhone";
	public static final String field_senderMobile = "senderMobile";
	public static final String field_senderrPostCode = "senderrPostCode";
	public static final String field_senderAddress = "senderAddress";
	public static final String field_sendTime = "sendTime";
	public static final String field_expectedTime = "expectedTime";
	public static final String field_logisticCode = "logisticCode";
	public static final String field_logisticCompany = "logisticCompany";
	public static final String field_logisticLinkman = "logisticLinkman";
	public static final String field_logisticPhone = "logisticPhone";
	public static final String field_logisticNo = "logisticNo";
	public static final String field_driverName = "driverName";
	public static final String field_driverPhone = "driverPhone";
	public static final String field_carNumber = "carNumber";
	public static final String field_boxCount = "boxCount";
	public static final String field_count = "count";
	public static final String field_skuCount = "skuCount";
	public static final String field_skuDetails = "skuDetails";
	public static final String field_hasDefective = "hasDefective";
	public static final String field_note = "note";
	
}
