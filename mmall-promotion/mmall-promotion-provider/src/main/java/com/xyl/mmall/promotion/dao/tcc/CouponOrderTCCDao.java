/*
 * 2014-9-16
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.tcc;

import java.util.List;

import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;



/**
 * CouponOrderTCCService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-16
 * @since      1.0
 */
public interface CouponOrderTCCDao {
	
	CouponOrderTCC addCouponOrderTCC(CouponOrderTCC couponOrderTCC);

	List<CouponOrderTCC> getCouponOrderTCCListByTranId(long tranId);

	boolean deleteCouponOrderTCC(long tranId);
	
}
