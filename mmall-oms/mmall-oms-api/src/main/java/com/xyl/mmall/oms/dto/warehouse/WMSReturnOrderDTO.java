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
 * 退货单，用户销售订单退货
 * 
 * @author hzzengchengyuan
 * 
 */
public class WMSReturnOrderDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int FLAG_FAILURE = 0;

	public static final int FLAG_SUCCESS = 1;

	/**
	 * 要退回的仓库类别
	 */
	private WarehouseType wmsType = WarehouseType.EMS;
	
	/**
	 * 退货类型
	 */
	private WMSOrderType orderType = WMSOrderType.RETURN;

	/**
	 * 退货单id
	 */
	private String orderId;
	
	/**
	 * 要退货的原始用户订单id
	 */
	private String originalOrderId;
	
	/**
	 * 退货用户标识/ID
	 */
	private String userId;
	
	/**
	 * 退货用户姓名
	 */
	private String userName;
	
	/**
	 * 退货用户电话
	 */
	private String userPhone;

	/**
	 * 仓库机构编码(WMS提供)
	 */
	private String warehouseCode;

	/**
	 * 货主编码(WMS提供)
	 */
	private String ownerCode;

	/**
	 * 订单原物流公司代码（如：EMS、SF）
	 */
	private String origLogisticCode;

	/**
	 * 订单原物流公司名称
	 */
	private String origLogisticCompany;

	/**
	 * 订单原物流号
	 */
	private String origLogisticNo;

	/**
	 * 退货物流公司代码
	 */
	private String logisticCode;

	/**
	 * 退货物流公司名称
	 */
	private String logisticCompany;

	/**
	 * 退货物流号
	 */
	private String logisticNo;

	/**
	 * 退货标记，0:退货失败，1:退货成功
	 */
	private int flag;

	/**
	 * 申请退货时间
	 */
	private String returnTime;
	
	/**
	 * 退货商品收货时间
	 */
	private String receiveTime;
	
	/**
	 * 总件数（数量）。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}方法计算
	 */
	private int count;

	/**
	 * 该单对应商品种类数。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}方法计算
	 */
	private int skuCount;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 商品明细
	 */
	private List<WMSSkuDetailDTO> skuDetails;

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

	public WMSOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(WMSOrderType orderType) {
		this.orderType = orderType;
	}

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

	public String getOriginalOrderId() {
		return originalOrderId;
	}

	public void setOriginalOrderId(String originalOrderId) {
		this.originalOrderId = originalOrderId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}

	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
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
	 * @return the origLogisticCode
	 */
	public String getOrigLogisticCode() {
		return origLogisticCode;
	}

	/**
	 * @param origLogisticCode
	 *            the origLogisticCode to set
	 */
	public void setOrigLogisticCode(String origLogisticCode) {
		this.origLogisticCode = origLogisticCode;
	}

	/**
	 * @return the origLogisticCompany
	 */
	public String getOrigLogisticCompany() {
		return origLogisticCompany;
	}

	/**
	 * @param origLogisticCompany
	 *            the origLogisticCompany to set
	 */
	public void setOrigLogisticCompany(String origLogisticCompany) {
		this.origLogisticCompany = origLogisticCompany;
	}

	/**
	 * @return the origLogisticNo
	 */
	public String getOrigLogisticNo() {
		return origLogisticNo;
	}

	/**
	 * @param origLogisticNo
	 *            the origLogisticNo to set
	 */
	public void setOrigLogisticNo(String origLogisticNo) {
		this.origLogisticNo = origLogisticNo;
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
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public boolean isSucess() {
		return getFlag() == FLAG_SUCCESS;
	}

	public boolean isFailure() {
		return getFlag() == FLAG_FAILURE;
	}

	/**
	 * @return the returnTime
	 */
	public String getReturnTime() {
		return returnTime;
	}

	/**
	 * @param returnTime the returnTime to set
	 */
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
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
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the skuCount
	 */
	public int getSkuCount() {
		return skuCount;
	}

	/**
	 * @param skuCount the skuCount to set
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
			}
		}
		this.count = allNum;
		this.skuCount = skuIdList.size();
	}

	// 定义退货单属性名常量列表，不全大写主要便于引用时查看
	public static final String field_orderId = "orderId";
	public static final String field_originalOrderId = "originalOrderId";
	public static final String field_userId = "userId";
	public static final String field_userName = "userName";
	public static final String field_userPhone = "userPhone";
	public static final String field_warehouseCode = "warehouseCode";
	public static final String field_ownerCode = "ownerCode";
	public static final String field_origLogisticCode = "origLogisticCode";
	public static final String field_origLogisticCompany = "origLogisticCompany";
	public static final String field_origLogisticNo = "origLogisticNo";
	public static final String field_logisticCode = "logisticCode";
	public static final String field_logisticCompany = "logisticCompany";
	public static final String field_logisticNo = "logisticNo";
	public static final String field_flag = "flag";
	public static final String field_returnTime = "returnTime";
	public static final String field_receiveTime = "receiveTime";
	public static final String field_count = "count";
	public static final String field_skuCount = "skuCount";
	public static final String field_note = "note";
	public static final String field_skuDetails = "skuDetails";

}
