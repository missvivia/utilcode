package com.xyl.mmall.order.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState;
import com.xyl.mmall.order.meta.DeprecatedReturnOrderSku;
import com.xyl.mmall.order.param.ReturnConfirmParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午5:16:35
 *
 */
@Deprecated
@Repository("deprecatedReturnOrderSkuDao")
public class DeprecatedReturnOrderSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<DeprecatedReturnOrderSku>
	implements DeprecatedReturnOrderSkuDao {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#getObjectByReturnId(long)
	 */
	@Override
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByReturnId(long returnId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "returnId", returnId);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#queryRetOrdSkuListByReturnIdWithState(long, com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState[])
	 */
	@Override
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByReturnIdWithState(long returnId, DeprecatedReturnOrderSkuState[] stateArray) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "returnId", returnId);
		SqlGenUtil.appendExtParamArray(sql, "retOrdSkuState", stateArray);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#queryRetOrdSkuListByState(com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByState(DeprecatedReturnOrderSkuState[] stateArray, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "retOrdSkuState", stateArray);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#queryRetOrdSkuListByStateWithUserId(com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState[], long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByStateWithUserId(DeprecatedReturnOrderSkuState[] stateArray, long userId,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamArray(sql, "retOrdSkuState", stateArray);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#queryRetOrdSkuListByStateWithOrderId(com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState[], long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByStateWithOrderId(DeprecatedReturnOrderSkuState[] stateArray, long orderId,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamArray(sql, "retOrdSkuState", stateArray);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#queryRetOrdSkuListByStateWithTimeRange(com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState[], long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByStateWithTimeRange(DeprecatedReturnOrderSkuState[] stateArray, long start,
			long end, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "retOrdSkuState", stateArray);
		String sqlStr = sql.toString().trim();
		if (!(sqlStr.endsWith(" WHERE") || sqlStr.endsWith(" AND") || sqlStr.endsWith(" OR") || sqlStr.endsWith("("))) {
			sql.append(" AND ").append("ctime >= ").append(start)
			   .append(" AND ").append("ctime <= ").append(end);
		}
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#queryRetOrdSkuByOrderIdAndOrderSkuId(long, long)
	 */
	@Override
	public DeprecatedReturnOrderSku queryRetOrdSkuByOrderIdAndOrderSkuId(long orderId, long orderSkuId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "orderSkuId", orderSkuId);
		return queryObject(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#confirmReturnOrderSku(com.xyl.mmall.order.meta.DeprecatedReturnOrderSku, com.xyl.mmall.order.param.ReturnOrderSkuParam)
	 */
	@Override
	public boolean confirmReturnOrderSku(DeprecatedReturnOrderSku retOrdSku, ReturnConfirmParam param) {
		if(null == retOrdSku || null == param) {
			return false;
		}
		long returnCount = retOrdSku.getReturnCount();
		long confirmCount = param.getConfirmCount();
		retOrdSku.setConfirmCount(confirmCount);
		retOrdSku.setExtInfo(param.getExtInfo());
		if(0 == confirmCount) {
			retOrdSku.setRetOrdSkuState(DeprecatedReturnOrderSkuState.NOT_CONFIRMED);
		} else if(confirmCount < returnCount) {
			retOrdSku.setRetOrdSkuState(DeprecatedReturnOrderSkuState.PART_CONFIRMED);
		} else if (confirmCount == returnCount){
			retOrdSku.setRetOrdSkuState(DeprecatedReturnOrderSkuState.ALL_CONFIRMED);
		} else {
			retOrdSku.setRetOrdSkuState(DeprecatedReturnOrderSkuState.UNKNOWN);
		}
		return updateObjectByKey(retOrdSku);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#setConfirmedRetOrdSkuToReturned(com.xyl.mmall.order.meta.DeprecatedReturnOrderSku)
	 */
	@Override
	public boolean setConfirmedRetOrdSkuToReturned(DeprecatedReturnOrderSku retOrdSku) {
		if(null == retOrdSku) {
			return false;
		}
		retOrdSku.setRetOrdSkuState(DeprecatedReturnOrderSkuState.RETURNED);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("orderSkuId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("retOrdSkuState");
		DeprecatedReturnOrderSkuState[] stateArray = new DeprecatedReturnOrderSkuState[] {
				DeprecatedReturnOrderSkuState.PART_CONFIRMED, 
				DeprecatedReturnOrderSkuState.ALL_CONFIRMED
		};
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retOrdSku));
		SqlGenUtil.appendExtParamArray(sql, "retOrdSkuState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao#deprecateRetOrdSku(long, long)
	 */
	@Override
	@Transaction
	public boolean deprecateRetOrdSku(long retId, long userId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "returnId", retId);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

}
