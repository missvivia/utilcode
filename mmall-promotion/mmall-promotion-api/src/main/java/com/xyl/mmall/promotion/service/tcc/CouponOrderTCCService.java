/*
 * 2014-9-16
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.tcc;

import java.util.List;

import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;


/**
 * CouponOrderTCCService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-16
 * @since      1.0
 */
public interface CouponOrderTCCService {
	/**
	 * 添加优惠券订单(TCC模型)-try步骤<br>
	 * 添加CouponOrderTCC<br>
	 * 
	 * @param param
	 *            以TCC模型,向DB添加订单数据需要的参数
	 * @return
	 */
	public CouponOrderTCC tryAddCouponOrderTCC(long tranId, CouponOrderTCC couponOrderTCC);

	/**
	 * confirm步骤<br>
	 * CouponOrderTCC->CouponOrder<br>
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean confirmAddCouponOrderTCC(long tranId);

	/**
	 * cancel步骤<br>
	 * 删除CouponOrderTCC
	 * 
	 * @param tranId
	 * @return
	 */
	public boolean cancelAddCouponOrderTCC(long tranId);
	
	/**
	 * 根据事物id获取列表
	 * @param tranId
	 * @return
	 */
	List<CouponOrderTCC> getCouponOrderTCCListByTranId(long tranId);
	
	/**
	 * 删除事务记录
	 * @param tranId
	 * @return
	 */
	boolean deleteCouponOrderTCC(long tranId);
}
