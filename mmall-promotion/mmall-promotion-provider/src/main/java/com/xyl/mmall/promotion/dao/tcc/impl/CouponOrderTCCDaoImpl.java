/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.tcc.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.tcc.CouponOrderTCCDao;
import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;

/**
 * CouponOrderServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Repository("couponOrderTCCDao")
public class CouponOrderTCCDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CouponOrderTCC> implements
		CouponOrderTCCDao {
	
	private static final String DELETE_TCC_RECORD = "DELETE FROM Mmall_Promotion_CouponOrderTCC WHERE TranId = ?";
	
	@Override
	public CouponOrderTCC addCouponOrderTCC(CouponOrderTCC couponOrderTCC) {
		if (couponOrderTCC.getId() <= 0) {
			couponOrderTCC.setId(this.allocateRecordId());
		}
		return this.addObject(couponOrderTCC);
	}

	@Override
	public List<CouponOrderTCC> getCouponOrderTCCListByTranId(long tranId) {
		StringBuilder sql = new StringBuilder();

		sql.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "TranId", tranId);
		return this.queryObjects(sql);
	}

	@Override
	public boolean deleteCouponOrderTCC(long tranId) {
		return this.getSqlSupport().excuteUpdate(DELETE_TCC_RECORD, tranId) > 0;
	}
}
