/*
 * @(#) 2014-12-2
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.cms.vo.finance.FinanceFirstPayConfirmVO;
import com.xyl.mmall.cms.vo.finance.FinanceFirstPayOrderVO;

/**
 * ApplierFinanceFacade.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-2
 * @since 1.0
 */
public interface SupplierFinanceFacade {

	/**
	 * 供应商档期首付款确认单信息.
	 * 
	 * @param poId
	 * @return
	 */
	public FinanceFirstPayConfirmVO getFirstPayConfirm(long poId);

	/**
	 * 供应商档期首付款销售明细.
	 * 
	 * @param poId
	 * @return
	 */
	public List<FinanceFirstPayOrderVO> queryFinanceFirstPayOrderVOList(long poId);

	RetArg getFullPayMetaVOList(long poId);

	RetArg getFullPayDetail(long poId);

	RetArg getInDetails(long poId);

	RetArg getRefundDetails(long poId);
}
