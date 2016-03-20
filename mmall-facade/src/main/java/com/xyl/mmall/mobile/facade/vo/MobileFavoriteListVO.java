package com.xyl.mmall.mobile.facade.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.framework.vo.BaseJsonListVO;
/**
 * 第一级类
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileFavoriteListVO  extends BaseJsonListVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 160100338436486513L;
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
