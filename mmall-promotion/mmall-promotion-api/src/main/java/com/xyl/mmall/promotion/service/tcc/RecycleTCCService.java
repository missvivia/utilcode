/*
 * @(#) 2014-10-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.tcc;

import com.xyl.mmall.promotion.dto.TCCParamDTO;

/**
 * RecycleTCCService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-10
 * @since      1.0
 */
public interface RecycleTCCService {
	/**
	 * 添加回收信息(TCC模型)-try步骤<br>
	 * 
	 * @param param
	 *            以TCC模型,向DB添加订单数据需要的参数
	 * @return
	 */
	public boolean tryAddRecycleTCC(long tranId, TCCParamDTO tccParamDTO);

	/**
	 * confirm步骤<br>
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean confirmAddRecycleTCC(long tranId);

	/**
	 * cancel步骤<br>
	 * 删除RecycleTCC
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean cancelAddRecycleTCC(long tranId);
}
