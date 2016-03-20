package com.xyl.mmall.order.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.meta.OrderForm;

/**
 * @author dingmingliang
 * 
 */
public class OrderFormExtInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20150206L;

	/**
	 * 取消失败标记位
	 */
	private boolean cancelFail = false;

	public boolean isCancelFail() {
		return cancelFail;
	}

	public void setCancelFail(boolean cancelFail) {
		this.cancelFail = cancelFail;
	}

	/**
	 * @param ord
	 * @return
	 */
	public static OrderFormExtInfoDTO genOrderFormExtInfoDTOByOrder(OrderForm ord) {
		if (ord == null)
			return null;

		String extInfo = ord.getExtInfo();
		return genOrderFormExtInfoDTOByExtInfo(extInfo);
	}
	
	/**
	 * @param extInfo
	 * @return
	 */
	public static OrderFormExtInfoDTO genOrderFormExtInfoDTOByExtInfo(String extInfo) {		
		OrderFormExtInfoDTO extDTO = StringUtils.isBlank(extInfo) ? null : JsonUtils.fromJson(extInfo,
				OrderFormExtInfoDTO.class);
		if (extDTO == null)
			extDTO = new OrderFormExtInfoDTO();
		return extDTO;
	}
}
