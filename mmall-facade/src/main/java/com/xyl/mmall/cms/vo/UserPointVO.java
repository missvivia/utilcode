/**
 * 
 */
package com.xyl.mmall.cms.vo;

import com.xyl.mmall.promotion.dto.UserPointDTO;

/**
 * @author jmy
 *
 */
@SuppressWarnings("serial")
public class UserPointVO extends UserPointDTO {
	private String userName;
	
	public UserPointVO(){}

	public UserPointVO(UserPointDTO pointDto) {
		if (pointDto != null) {
			this.setId(pointDto.getId());
			this.setPoint(pointDto.getPoint());
			this.setUserId(pointDto.getUserId());
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
