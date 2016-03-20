package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderExpInfo;

/**
 * @author dingmingliang
 *
 */
public class OrderExpInfoDTO extends OrderExpInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderExpInfoDTO(OrderExpInfo obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	/**
	 * 构造函数
	 */
	public OrderExpInfoDTO(){		
	}
	
	public String fullAddress() {
		StringBuffer strBuf = new StringBuffer(1024);
		strBuf.append(this.getProvince())
			  .append(this.getCity())
			  .append(this.getSection())
			  .append(this.getStreet())
			  .append(this.getAddress());
		return strBuf.toString();
	}

	
	
}
