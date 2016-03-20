/*
 * @(#) 2014-10-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import com.netease.print.common.meta.RetArg;


/**
 * RebateTCCService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-10
 * @since      1.0
 */
public interface RebateService {
	/**
	 * 返券和返红包操作
	 * 
	 * @return
	 */
	public boolean rebate(long userId, long orderId);
	
	public RetArg rebateAndReturnObjcet(long userId, long orderId);
}
