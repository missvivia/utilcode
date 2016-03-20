/**
 * 
 */
package com.xyl.mmall.oms.dto.warehouse;

/**
 * 订单的物流运输轨迹
 * 
 * @author hzzengchengyuan
 *
 */
public class WMSOrderTrace {
	/**
	 * 
	 */
	private String logisticCompany;
	/**
	 * 物流运单号
	 */
	private String logisticNo;

	/**
	 * 订单号
	 */
	private String orderId;

	/**
	 * 时间戳，格式为：yyyy-MM-dd HH:mm:ss
	 */
	private String timestamp;

	/**
	 * 当前操作人员
	 */
	private String operater;
	
	/**
	 * 操作人员电话
	 */
	private String operaterPhone;

	/**
	 * 操作类型
	 */
	private String operate;
	
	/**
	 * 操作描述
	 */
	private String operateDesc;
	
	/**
	 * 子操作类型
	 */
	private String childOperate;
	
	/**
	 * 子操作描述
	 */
	private String childOperateDesc;
	
	/**
	 * 当前操作机构(地址)名称
	 */
	private String operateOrg;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 签收信息
	 */
	private String signedInfo;

	/**
	 * 未签收信息
	 */
	private String unreceiptedInfo;

	/**
	 * @return the logisticCompany
	 */
	public String getLogisticCompany() {
		return logisticCompany;
	}

	/**
	 * @param logisticCompany the logisticCompany to set
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
	 * @param logisticNo the logisticNo to set
	 */
	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the operater
	 */
	public String getOperater() {
		return operater;
	}

	/**
	 * @param operater the operater to set
	 */
	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	/**
	 * @return the operateDesc
	 */
	public String getOperateDesc() {
		return operateDesc;
	}

	/**
	 * @param operateDesc the operateDesc to set
	 */
	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public String getChildOperate() {
		return childOperate;
	}

	public String getChildOperateDesc() {
		return childOperateDesc;
	}

	public void setChildOperate(String childOperate) {
		this.childOperate = childOperate;
	}

	public void setChildOperateDesc(String childOperateDesc) {
		this.childOperateDesc = childOperateDesc;
	}

	/**
	 * @return the operaterPhone
	 */
	public String getOperaterPhone() {
		return operaterPhone;
	}

	/**
	 * @param operaterPhone the operaterPhone to set
	 */
	public void setOperaterPhone(String operaterPhone) {
		this.operaterPhone = operaterPhone;
	}

	/**
	 * @return the operateOrg
	 */
	public String getOperateOrg() {
		return operateOrg;
	}

	/**
	 * @param operateOrg the operateOrg to set
	 */
	public void setOperateOrg(String operateOrg) {
		this.operateOrg = operateOrg;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the signedInfo
	 */
	public String getSignedInfo() {
		return signedInfo;
	}

	/**
	 * @param signedInfo the signedInfo to set
	 */
	public void setSignedInfo(String signedInfo) {
		this.signedInfo = signedInfo;
	}

	/**
	 * @return the unreceiptedInfo
	 */
	public String getUnreceiptedInfo() {
		return unreceiptedInfo;
	}

	/**
	 * @param unreceiptedInfo the unreceiptedInfo to set
	 */
	public void setUnreceiptedInfo(String unreceiptedInfo) {
		this.unreceiptedInfo = unreceiptedInfo;
	}

	public static final String field_logisticCompany = "logisticCompany";
	public static final String field_logisticNo = "logisticNo";
	public static final String field_orderId = "orderId";
	public static final String field_timestamp = "timestamp";
	public static final String field_operater = "operater";
	public static final String field_operaterPhone = "operaterPhone";
	public static final String field_operate = "operate";
	public static final String field_operateDesc = "operateDesc";
	public static final String field_childOperate = "childOperate";
	public static final String field_childOperateDesc = "childOperateDesc";
	public static final String field_operateOrg = "operateOrg";
	public static final String field_desc = "desc";
	public static final String field_signedInfo = "signedInfo";
	public static final String field_unreceiptedInfo = "unreceiptedInfo";

}
