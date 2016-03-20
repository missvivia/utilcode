package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;

/**
 * @author zb
 *
 */
public interface OmsReturnOrderFormDao extends AbstractDao<OmsReturnOrderForm> {

	public boolean updateConfirmTime(long id, long confirmTime);

	public List<OmsReturnOrderForm> getListByConfirmTimeAndState(long confirmTime, OmsReturnOrderFormState returnState,
			int limit);

	public boolean updateOmsReturnOrderFormState(long id, OmsReturnOrderFormState state, String extInfo);

}
