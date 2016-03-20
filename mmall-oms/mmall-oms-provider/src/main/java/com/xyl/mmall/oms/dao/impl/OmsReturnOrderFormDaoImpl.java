package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OmsReturnOrderFormDao;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;

@Repository("OmsReturnOrderFormDao")
public class OmsReturnOrderFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsReturnOrderForm> implements
		OmsReturnOrderFormDao {

	private String sqlUpdateReturnState = "update Mmall_Oms_OmsReturnOrderForm set omsReturnOrderFormState = ?,extinfo=? where id=?";

	private String sqlUpdateConfirmTime = "update Mmall_Oms_OmsReturnOrderForm set confirmTime=? where id=?";

	private String sqlSelectByConfirmTimeAndState = "select * from Mmall_Oms_OmsReturnOrderForm where confirmTime<? and omsReturnOrderFormState =? order by confirmTime desc";

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.OmsReturnOrderFormDao#updateOmsReturnOrderFormState(long,
	 *      com.xyl.mmall.oms.enums.OmsReturnOrderFormState)
	 */
	@Override
	public boolean updateOmsReturnOrderFormState(long id, OmsReturnOrderFormState state, String extInfo) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateReturnState, state.getIntValue(), extInfo, id) > 0;
	}

	@Override
	public boolean updateConfirmTime(long id, long confirmTime) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateConfirmTime, confirmTime, id) > 0;
	}

	@Override
	public List<OmsReturnOrderForm> getListByConfirmTimeAndState(long confirmTime, OmsReturnOrderFormState returnState,
			int limit) {
		StringBuilder sb = new StringBuilder(sqlSelectByConfirmTimeAndState);
		this.appendLimitSql(sb, limit, 0);
		return this.queryObjects(sb.toString(), confirmTime, returnState.getIntValue());
	}

}
