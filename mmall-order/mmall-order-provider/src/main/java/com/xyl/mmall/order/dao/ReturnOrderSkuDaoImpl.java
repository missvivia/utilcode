package com.xyl.mmall.order.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState;
import com.xyl.mmall.order.meta.ReturnOrderSku;
import com.xyl.mmall.order.param.ReturnConfirmParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午5:16:35
 *
 */
@Repository("returnOrderSkuDao")
public class ReturnOrderSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ReturnOrderSku>
	implements ReturnOrderSkuDao {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnOrderSkuDao#queryObjectsByRetPkgId(long, long)
	 */
	@Override
	public List<ReturnOrderSku> queryObjectsByRetPkgId(long retPkgId, long userId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "retPkgId", retPkgId);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnOrderSkuDao#queryObjectsByRetPkgIdWithState(long, long, com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState[])
	 */
	@Override
	public List<ReturnOrderSku> queryObjectsByRetPkgIdWithState(long retPkgId, long userId, 
			ReturnOrderSkuConfirmState[] stateArray) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "retPkgId", retPkgId);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamArray(sql, "retOrdSkuState", stateArray);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnOrderSkuDao#confirmReturnOrderSku(com.xyl.mmall.order.meta.ReturnOrderSku, com.xyl.mmall.order.param.ReturnConfirmParam)
	 */
	@Override
	@Transaction
	public boolean confirmReturnOrderSku(ReturnOrderSku retOrdSku, ReturnConfirmParam param) {
		if(null == retOrdSku || null == param) {
			return false;
		}
		retOrdSku = getLockByKey(retOrdSku);
		int applyedReturnCount = retOrdSku.getApplyedReturnCount();
		int confirmCount = param.getConfirmCount();
		retOrdSku.setConfirmCount(confirmCount);
		retOrdSku.setConfirmInfo(param.getExtInfo());
		if(0 == confirmCount) {
			retOrdSku.setRetOrdSkuState(ReturnOrderSkuConfirmState.NOT_CONFIRMED);
		} else if(confirmCount < applyedReturnCount) {
			retOrdSku.setRetOrdSkuState(ReturnOrderSkuConfirmState.PART_CONFIRMED);
		} else if (confirmCount == applyedReturnCount){
			retOrdSku.setRetOrdSkuState(ReturnOrderSkuConfirmState.ALL_CONFIRMED);
		} else {
			retOrdSku.setRetOrdSkuState(ReturnOrderSkuConfirmState.UNKNOWN);
		}
		return updateObjectByKey(retOrdSku);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnOrderSkuDao#updateReturnOrderSkuConfirmState(com.xyl.mmall.order.meta.ReturnOrderSku, com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState, com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState[])
	 */
	@Override
	@Transaction
	public boolean updateReturnOrderSkuConfirmState(ReturnOrderSku retOrdSku, ReturnOrderSkuConfirmState newState, 
			ReturnOrderSkuConfirmState[] stateArray) {
		if(null == retOrdSku) {
			return false;
		}
		retOrdSku = getLockByKey(retOrdSku);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("retOrdSkuState");
		retOrdSku.setRetOrdSkuState(newState);
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retOrdSku));
		SqlGenUtil.appendExtParamArray(sql, "retOrdSkuState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

}
