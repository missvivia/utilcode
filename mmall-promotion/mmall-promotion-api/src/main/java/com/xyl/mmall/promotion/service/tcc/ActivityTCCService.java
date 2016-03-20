/*
 * @(#) 2014-9-30
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.tcc;

import java.util.Map;

import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.TCCParamDTO;

/**
 * ActivityTCCFacade.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-30
 * @since      1.0
 */
public interface ActivityTCCService {
	
	/**
	 * 添加活动信息(TCC模型)-try步骤<br>
	 * 
	 * @param param
	 *            以TCC模型,向DB添加订单数据需要的参数
	 * @return
	 */
	public boolean tryAddActivityTCC(long tranId, TCCParamDTO tccParamDTO, FavorCaculateResultDTO resultDTO);
	
	/**
	 * 
	 * @param tranId
	 * @param tccParamDTOMap
	 * @param fcResultDTOMap
	 * @return
	 */
    public boolean tryAddActivityTCC(long tranId, Map<Long, TCCParamDTO> tccParamDTOMap,
            Map<Long, FavorCaculateResultDTO> fcResultDTOMap);

	/**
	 * confirm步骤<br>
	 * CouponOrderTCC->CouponOrder<br>
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean confirmAddActivityTCC(long tranId);

	/**
	 * cancel步骤<br>
	 * 删除OrderFormTCC
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean cancelAddActivityTCC(long tranId);
}
