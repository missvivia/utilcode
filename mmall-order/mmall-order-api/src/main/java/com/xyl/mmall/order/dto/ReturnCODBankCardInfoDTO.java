package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.ReturnCODBankCardInfo;

public class ReturnCODBankCardInfoDTO extends ReturnCODBankCardInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1982275153860710465L;

	/**
	 * 
	 */
	public ReturnCODBankCardInfoDTO() {
	}
	
	/**
	 * 
	 * @param obj
	 */
	public ReturnCODBankCardInfoDTO(ReturnCODBankCardInfo obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public String mergeGeneralInfo() {
		StringBuffer strBuf = new StringBuffer(128);
		strBuf.append(this.getBankCardOwnerName()).append("-")
			  .append(this.getBankType()).append("-")
			  .append(this.getBankBranch()).append("-")
			  .append(this.getBankCardNO());
		return strBuf.toString();
	}
}
