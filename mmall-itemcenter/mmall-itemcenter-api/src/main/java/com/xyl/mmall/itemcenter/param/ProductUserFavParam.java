package com.xyl.mmall.itemcenter.param;

import com.netease.print.daojar.meta.base.DDBParam;

/**
 * 收藏商品分页参数
 * @author author:lhp
 *
 * @version date:2015年7月8日下午1:06:05
 */
public class ProductUserFavParam extends DDBParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3861750257117555294L;
	
	/**
	 * 用户Id
	 */
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	

}
