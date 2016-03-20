/**
 * 
 */
package com.xyl.mmall.backend.vo;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.dto.RejectPackageDTO;
import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.meta.RejectPackage;

/**
 * @author hzzengchengyuan
 *
 */
public class RejectPackageVO extends RejectPackageDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private String warehouseOrderId;
	
	public RejectPackageVO() {

	}

	public RejectPackageVO(RejectPackage pack) {
		ReflectUtil.convertObj(this, pack, false);
	}
	
	public RejectPackageVO(RejectPackageDTO pack) {
		ReflectUtil.convertObj(this, pack, false);
	}

	public String getWarehouseOrderId() {
		return OmsConstants.EMS_ID_PR_STOCKIN_UNRECEIPT.concat(String.valueOf(getRejectPackageId()));
	}
	
}
