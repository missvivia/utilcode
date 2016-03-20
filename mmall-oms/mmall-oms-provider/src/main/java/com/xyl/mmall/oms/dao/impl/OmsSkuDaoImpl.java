package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OmsSkuDao;
import com.xyl.mmall.oms.enums.OmsSkuState;
import com.xyl.mmall.oms.meta.OmsSku;

@Repository("OmsSkuDao")
public class OmsSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsSku> implements OmsSkuDao {

	private String sqlSelectByState = "select * from Mmall_Oms_OmsSku where state=?";

	private String sqlUpdateState = "update Mmall_Oms_OmsSku set state=? where skuId=?";

	@Override
	public List<OmsSku> getOmsSkuListByState(OmsSkuState state, int limit, int offset) {
		StringBuilder sb = new StringBuilder(sqlSelectByState);
		this.appendLimitSql(sb, limit, offset);
		return this.queryObjects(sb.toString(), state.getIntValue());
	}

	@Override
	public boolean updateState(long skuId, OmsSkuState state) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateState, state.getIntValue(), skuId) > 0;
	}

}
