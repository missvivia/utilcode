/*
 * 2014-9-17
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta.tcc;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;
import com.xyl.mmall.promotion.meta.CouponOrder;

/**
 * CouponOrderTCC.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-17
 * @since      1.0
 */

@AnnonOfClass(desc = "订单交易TCC", tableName = "Mmall_Promotion_CouponOrderTCC")
public class CouponOrderTCC extends CouponOrder implements TCCMetaInterface {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "TCC事务Id", policy = true)
	private long tranId;

	@AnnonOfField(desc = "TCC事务添加时间")
	private long ctimeOfTCC;

	public long getTranId() {
		return tranId;
	}

	public void setTranId(long tranId) {
		this.tranId = tranId;
	}

	public long getCtimeOfTCC() {
		return ctimeOfTCC;
	}

	public void setCtimeOfTCC(long ctimeOfTCC) {
		this.ctimeOfTCC = ctimeOfTCC;
	}
	
	public CouponOrder getCouponOrder() {
		return this.cloneObject();
	}
}
