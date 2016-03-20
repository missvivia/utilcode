/**
 * 
 */
package com.xyl.mmall.oms.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.meta.RejectPackage;
import com.xyl.mmall.oms.util.OmsIdUtils;

/**
 * @author hzzengchengyuan
 *
 */
public class RejectPackageDTO extends RejectPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	private String userPhone;

	private String warehouseName;
	
	@SuppressWarnings("unused")
	private String warehouseOrderId;
	
	@SuppressWarnings("unused")
	private String stateDesc;
	
	@SuppressWarnings("unused")
	private String createTimeFormat;

	public RejectPackageDTO() {

	}

	public RejectPackageDTO(RejectPackage pack) {
		ReflectUtil.convertObj(this, pack, false);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseOrderId() {
		return OmsIdUtils.genEmsOrderId(String.valueOf(super.getRejectPackageId()), WMSOrderType.R_UA);
	}

	public String getStateDesc() {
		return super.getState().getDesc();
	}
	
	public static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String getCreateTimeFormat() {
		return format.format(new Date(super.getCreateTime()));
	}


}
