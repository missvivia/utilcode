/**
 * 
 */
package com.xyl.mmall.mobile.facade.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.MobileInfoDTO;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileInfoVO {

	private MobileInfoDTO mobileInfo;

	public MobileInfoVO() {

	}

	public MobileInfoVO(MobileInfoDTO mobileInfo) {
		this.mobileInfo = mobileInfo;
	}

	/**
	 * @return the mobileInfo
	 */
	public MobileInfoDTO getMobileInfo() {
		return mobileInfo;
	}

	/**
	 * @param mobileInfo
	 *            the mobileInfo to set
	 */
	public void setMobileInfo(MobileInfoDTO mobileInfo) {
		this.mobileInfo = mobileInfo;
	}
}
