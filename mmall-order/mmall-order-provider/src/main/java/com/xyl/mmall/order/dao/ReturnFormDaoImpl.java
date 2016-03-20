package com.xyl.mmall.order.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.ReturnCouponRecycleState;
import com.xyl.mmall.order.enums.ReturnOrderSkuNumState;
import com.xyl.mmall.order.meta.ReturnForm;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午5:16:35
 * 
 */
@Repository("returnFormDao")
public class ReturnFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ReturnForm> implements ReturnFormDao {
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnFormDao#queryObjectByOrderIdAndUserId(long, long)
	 */
	@Override
	public ReturnForm queryObjectByOrderIdAndUserId(long orderId, long userId) {
		return getObjectByIdAndUserId(orderId, userId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnFormDao#updateApplyedNumState(com.xyl.mmall.order.meta.ReturnForm, com.xyl.mmall.order.enums.ReturnOrderSkuNumState, com.xyl.mmall.order.enums.ReturnOrderSkuNumState[])
	 */
	@Override
	public boolean updateApplyedNumState(ReturnForm retForm, ReturnOrderSkuNumState newState, ReturnOrderSkuNumState[] oldStates) {
		if(null == retForm) {
			return false;
		}
		
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("applyedNumState");
		setOfUpdate.add("version");

		long version = retForm.getVersion();
		retForm.setApplyedNumState(newState);
		retForm.setVersion(version + 1);
		
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		
		SqlGenUtil.appendExtParamArray(sql, "applyedNumState", oldStates);
		SqlGenUtil.appendExtParamObject(sql, "version", version);
		
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnFormDao#updateConfirmNumState(com.xyl.mmall.order.meta.ReturnForm, com.xyl.mmall.order.enums.ReturnOrderSkuNumState, com.xyl.mmall.order.enums.ReturnOrderSkuNumState[])
	 */
	@Override
	public boolean updateConfirmNumState(ReturnForm retForm, ReturnOrderSkuNumState newState, ReturnOrderSkuNumState[] oldStates) {
		if(null == retForm) {
			return false;
		}
		
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("confirmedNumState");
		setOfUpdate.add("version");

		long version = retForm.getVersion();
		retForm.setConfirmedNumState(newState);
		retForm.setVersion(version + 1);
		
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		
		SqlGenUtil.appendExtParamArray(sql, "confirmedNumState", oldStates);
		SqlGenUtil.appendExtParamObject(sql, "version", version);
		
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnFormDao#queryWaitingRecycleReturnFormListWithMinOrderId(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnForm> queryWaitingRecycleReturnFormListWithMinOrderId(long minOrdId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "applyedNumState", ReturnOrderSkuNumState.APPLY_ORDER_FULL_RETURN);
		SqlGenUtil.appendExtParamObject(sql, "confirmedNumState", ReturnOrderSkuNumState.CONFIRM_ORDER_FULL_RETURN);
		SqlGenUtil.appendExtParamObject(sql, "couponHbRecycleState", ReturnCouponRecycleState.WAITING_RECYCEL);
		sql.append(" And orderId > ").append(minOrdId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnFormDao#updateReturnCouponHbRecycleState(com.xyl.mmall.order.meta.ReturnForm, com.xyl.mmall.order.enums.ReturnCouponRecycleState, com.xyl.mmall.order.enums.ReturnCouponRecycleState[])
	 */
	@Override
	public boolean updateReturnCouponHbRecycleState(ReturnForm retForm, ReturnCouponRecycleState newState,
			 ReturnCouponRecycleState[] oldStates) {
		if(null == retForm) {
			return false;
		}
		
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("couponHbRecycleState");
		setOfUpdate.add("version");
		
		long version = retForm.getVersion();
		retForm.setCouponHbRecycleState(newState);
		retForm.setVersion(version + 1);

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		
		SqlGenUtil.appendExtParamArray(sql, "couponHbRecycleState", oldStates);
		SqlGenUtil.appendExtParamObject(sql, "version", version);
		
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

}
