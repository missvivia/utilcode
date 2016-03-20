/*
 * @(#) 2014-12-2
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.cms.vo.finance.FinanceOrderRefundVO;
import com.xyl.mmall.cms.vo.finance.FinanceOrderVO;
import com.xyl.mmall.cms.vo.finance.FinanceTradeVO;

/**
 * OrderFinanceFacade.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-2
 * @since 1.0
 */
public interface OrderFinanceFacade {

	/**
	 * 
	 * 
	 * @param timeRange
	 * @return
	 */
	public List<FinanceOrderVO> queryOrderList(long[] timeRange);

	/**
	 * 
	 * 
	 * @param timeRange
	 * @return
	 */
	public List<FinanceTradeVO> queryTradeList(long[] timeRange);
	
	/**
	 * 获取指定时间区间的退款记录
	 * @param timeRange
	 * @return
	 */
	List<FinanceOrderRefundVO> queryOrderRefundList(long[] timeRange);

}
