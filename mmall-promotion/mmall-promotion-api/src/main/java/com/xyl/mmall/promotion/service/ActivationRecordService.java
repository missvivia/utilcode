/*
 * @(#) 2015-1-23
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.util.Map;

import com.xyl.mmall.promotion.meta.ActivationRecord;
import com.xyl.mmall.promotion.meta.PromotionLock;

/**
 * ActivationRecordService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2015-1-23
 * @since      1.0
 */
public interface ActivationRecordService {

	ActivationRecord getActivationRecordByUserIdAndState(long userId, boolean free);

	Map<String, Object> dispatchCouponCode(long userId, boolean free, String couponCode, String couponCodes, PromotionLock lock);

}
