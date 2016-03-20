package com.xyl.mmall.mobile.ios.facade.pageView.common;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.dto.BusinessDTO;

public class MobileShopInfo extends BusinessDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8085463599589876001L;

	/**
	 * 是否允许进入店铺浏览
	 */
	private boolean isAllowed = false;

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}
	
	public MobileShopInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public MobileShopInfo(BusinessDTO obj){
		ReflectUtil.convertObj(this, obj, false);
	}
	
}
