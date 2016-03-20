/**
 * 
 */
package com.xyl.mmall.backend.vo;

import com.xyl.mmall.backend.util.DateUtils;
import com.xyl.mmall.oms.dto.FreightUserReturnDTO;

/**
 * @author hzzengchengyuan
 *
 */
public class FreightUserReturnVO extends FreightUserReturnDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 序号
	 */
	private int indexNo;
	
	String wmsReceivedTimeFormat;
	
	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}
	
	public String getWmsReceivedTimeFormat() {
		long time = getWmsReceivedTime();
		if (time == 0) {
			return FreightVO.DEFAULT_NULL_LABLE;
		} else {
			return DateUtils.parseToStringtime(time);
		}
	}

}
