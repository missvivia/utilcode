package com.xyl.mmall.order.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.DeprecatedReturnCouponHbRecycleState;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.meta.DeprecatedReturnForm;
import com.xyl.mmall.order.param.KFParam;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnOperationParam;
import com.xyl.mmall.order.param.ReturnPackageExpInfoParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午5:16:35
 * 
 */
@Repository
@Deprecated
public class DeprecatedReturnFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<DeprecatedReturnForm> implements DeprecatedReturnFormDao {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryObjectByOrderIdAndUserId(long,
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryObjectByOrderIdAndUserId(long orderId, long userId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryObjectByOrderIdAndUserIdWithStates(long, long, com.xyl.mmall.order.enums.DeprecatedReturnState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryObjectByOrderIdAndUserIdWithStates(long orderId, long userId,
			DeprecatedReturnState[] stateArray, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryObjectByOrderIdAndUserId(java.util.Collection,
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<DeprecatedReturnForm> queryObjectByOrderIdAndUserId(Collection<Long> orderIdColl, long userId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamColl(sql, "orderId", orderIdColl);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryReturnFormListByState(com.xyl.mmall.order.enums.DeprecatedReturnState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryReturnFormListByState(DeprecatedReturnState[] stateArray, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryReturnFormListByStateWithUserId(com.xyl.mmall.order.enums.DeprecatedReturnState[],
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryReturnFormListByStateWithUserId(DeprecatedReturnState[] stateArray, long userId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryReturnFormListByStateWithOrderId(com.xyl.mmall.order.enums.DeprecatedReturnState[],
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryReturnFormListByStateWithOrderId(DeprecatedReturnState[] stateArray, long orderId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryJITFailedReturnFormListByStateWithMinReturnId(com.xyl.mmall.order.enums.DeprecatedReturnState[], long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryJITFailedReturnFormListByStateWithMinReturnId(DeprecatedReturnState[] stateArray, long minRetId,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "jitSucc", false);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		sql.append(" And Id>").append(minRetId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryWaitingRecycleReturnFormListByStateWithMinReturnId(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryWaitingRecycleReturnFormListByStateWithMinReturnId(long minRetId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "returnState", new DeprecatedReturnState[] {DeprecatedReturnState.FINISH_RETURN});
		SqlGenUtil.appendExtParamArray(sql, "couponHbRecycleState", 
				new DeprecatedReturnCouponHbRecycleState[] {DeprecatedReturnCouponHbRecycleState.WAITING_RETURN});
		sql.append(" And Id>").append(minRetId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryReturnFormListByStateWithTimeRange(com.xyl.mmall.order.enums.DeprecatedReturnState[],
	 *      long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryReturnFormListByStateWithTimeRange(DeprecatedReturnState[] stateArray, long start, long end,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		String sqlStr = sql.toString().trim();
		if (!(sqlStr.endsWith(" WHERE") || sqlStr.endsWith(" AND") || sqlStr.endsWith(" OR") || sqlStr.endsWith("("))) {
			sql.append(" AND ").append("ctime >= ").append(start).append(" AND ").append("ctime <= ").append(end);
		}
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#queryReturnFormListByStateWithMailNO(com.xyl.mmall.order.enums.DeprecatedReturnState[], java.lang.String, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> queryReturnFormListByStateWithMailNO(DeprecatedReturnState[] stateArray, String mailNO, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "mailNO", mailNO);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#setReturnState(com.xyl.mmall.order.meta.DeprecatedReturnForm,
	 *      com.xyl.mmall.order.enums.DeprecatedReturnState[])
	 */
	@Override
	public boolean setReturnState(DeprecatedReturnForm retForm, DeprecatedReturnState[] stateArray) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("returnState");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#setReturnCouponHbRecycleState(com.xyl.mmall.order.meta.DeprecatedReturnForm, com.xyl.mmall.order.enums.DeprecatedReturnCouponHbRecycleState[])
	 */
	@Override
	public boolean setReturnCouponHbRecycleState(DeprecatedReturnForm retForm, DeprecatedReturnCouponHbRecycleState[] stateArray) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("couponHbRecycleState");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		SqlGenUtil.appendExtParamArray(sql, "couponHbRecycleState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#setReturnStateToWaitingConfirmWithExpInfo(com.xyl.mmall.order.meta.DeprecatedReturnForm, com.xyl.mmall.order.param.ReturnPackageExpInfoParam)
	 */
	@Override
	public boolean setReturnStateToWaitingConfirmWithExpInfo(DeprecatedReturnForm retForm, ReturnPackageExpInfoParam param) {
		if (null == retForm || null == param) {
			return false;
		}
		retForm.setReturnState(DeprecatedReturnState.WAITING_CONFIRM);
		retForm.setMailNO(param.getMailNO());
		// retForm.setExpressCompany(param.getExpressCompany());
		retForm.setExpressCompany(ExpressCompany.DEFAULT_EXPRESS);
		retForm.setReturnExpInfoId(param.getReturnExpInfoId());
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("returnState");
		setOfUpdate.add("mailNO");
		setOfUpdate.add("expressCompany");
		setOfUpdate.add("returnExpInfoId");
		DeprecatedReturnState[] stateArray = new DeprecatedReturnState[] { DeprecatedReturnState.APPLY_RETURN };
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#setReturnStateToWaitingReturnAuditWithConfirmTime(com.xyl.mmall.order.meta.DeprecatedReturnForm, boolean)
	 */
	@Override
	public boolean setReturnStateToWaitingReturnAuditWithConfirmTime(DeprecatedReturnForm retForm, boolean abnomal) {
		if (null == retForm) {
			return false;
		}
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("confirmTime");
		retForm.setConfirmTime(System.currentTimeMillis());
		setOfUpdate.add("returnState");
		if(abnomal) {
			retForm.setReturnState(DeprecatedReturnState.ABNOMAL);
		} else {
			retForm.setReturnState(DeprecatedReturnState.FINISH_RETURN);
			setOfUpdate.add("returnTime");
			retForm.setReturnTime(System.currentTimeMillis());
		}
		DeprecatedReturnState[] stateArray = new DeprecatedReturnState[] { DeprecatedReturnState.WAITING_CONFIRM };
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#setReturnStateToRefusedWithParam(com.xyl.mmall.order.meta.DeprecatedReturnForm, com.xyl.mmall.order.param.ReturnOperationParam)
	 */
	@Override
	public boolean setReturnStateToRefusedWithParam(DeprecatedReturnForm retForm, ReturnOperationParam param, KFParam kf) {
		if (null == retForm || null == param) {
			return false;
		}
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		
		Set<String> setOfUpdate = new HashSet<>();
		
		retForm.setReturnState(DeprecatedReturnState.REFUSED);
		setOfUpdate.add("returnState");
		
		retForm.setExtInfo(param.getExtInfo());
		setOfUpdate.add("extInfo");
		
		retForm.setReturnTime(System.currentTimeMillis());
		setOfUpdate.add("returnTime");
		
		retForm.setKfId(kf.getKfId());
		setOfUpdate.add("kfId");
		
		retForm.setKfName(kf.getKfName());
		setOfUpdate.add("kfName");
		
		DeprecatedReturnState[] stateArray = new DeprecatedReturnState[] { DeprecatedReturnState.ABNOMAL };
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#cancelRefuse(com.xyl.mmall.order.meta.DeprecatedReturnForm, com.xyl.mmall.order.param.ReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	public boolean cancelRefuse(DeprecatedReturnForm retForm, ReturnOperationParam param, KFParam kf) {
		if (null == retForm || null == param) {
			return false;
		}
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		
		Set<String> setOfUpdate = new HashSet<>();
		
		retForm.setReturnState(DeprecatedReturnState.ABNOMAL);
		setOfUpdate.add("returnState");
		
		retForm.setExtInfo(param.getExtInfo());
		setOfUpdate.add("extInfo");
		
		retForm.setReturnTime(System.currentTimeMillis());
		setOfUpdate.add("returnTime");
		
		retForm.setKfId(kf.getKfId());
		setOfUpdate.add("kfId");
		
		retForm.setKfName(kf.getKfName());
		setOfUpdate.add("kfName");
		
		DeprecatedReturnState[] stateArray = new DeprecatedReturnState[] { DeprecatedReturnState.REFUSED };
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#setReturnStateToReturnedWithParam(com.xyl.mmall.order.meta.DeprecatedReturnForm,
	 *      com.xyl.mmall.order.param.ReturnOperationParam)
	 */
	@Override
	public boolean setReturnStateToReturnedWithParam(DeprecatedReturnForm retForm, PassReturnOperationParam param, KFParam kf) {
		if (null == retForm || null == param) {
			return false;
		}
		
		BigDecimal kfPrice = param.getPayedReturnCashPrice();
		BigDecimal sysPrice = retForm.getPayedCashPrice();
		double kfPriceDouble = (null == kfPrice) ? 0 : kfPrice.doubleValue();
		double sysPriceDouble = (null == sysPrice) ? 0 : sysPrice.doubleValue();
		if(kfPriceDouble > sysPriceDouble) {
			return false;
		}
		
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		
		Set<String> setOfUpdate = new HashSet<>();
		
		retForm.setReturnState(DeprecatedReturnState.FINISH_RETURN);
		setOfUpdate.add("returnState");
		
		retForm.setPayedCashPrice(param.getPayedReturnCashPrice());
		setOfUpdate.add("payedCashPrice");
		
		retForm.setExpSubsidyPrice(param.getExpCompensatedPrice());
		setOfUpdate.add("expSubsidyPrice");
		
		retForm.setExtInfo(param.getExtInfo());
		setOfUpdate.add("extInfo");
		
		retForm.setReturnTime(System.currentTimeMillis());
		setOfUpdate.add("returnTime");
		
		retForm.setKfId(kf.getKfId());
		setOfUpdate.add("kfId");
		
		retForm.setKfName(kf.getKfName());
		setOfUpdate.add("kfName");
		
		DeprecatedReturnState[] stateArray = new DeprecatedReturnState[] { DeprecatedReturnState.ABNOMAL };
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#getWaitingReturnAuditCount()
	 */
	@Override
	public Map<Integer, Long> getWaitingReturnAuditCount() {
		Map<Integer, Long> retMap = new HashMap<Integer, Long>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT COUNT(A.orderId), A.provinceId FROM Mmall_Order_OrderForm A, Mmall_Order_ReturnForm B WHERE A.userId = B.userId AND A.orderId = B.orderId");
		sql.append(" AND B.returnState = ").append(DeprecatedReturnState.ABNOMAL.getIntValue()).append(" GROUP BY A.provinceId");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				long count = rs.getLong("COUNT(A.orderId)");
				int provinceId = rs.getInt("A.provinceId");
				retMap.put(provinceId, count);
			}
		} catch (SQLException e) {
			return retMap;
		} finally {
			dbr.close();
		}
		return retMap;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#getReturnFormListShouldBeCanceled(long)
	 */
	@SuppressWarnings("resource")
	@Override
	public Map<Long, List<Long>> getReturnFormListShouldBeCanceled(long detainTime) {
		long omsTime = System.currentTimeMillis() - detainTime;
		Map<Long, List<Long>> idList = new HashMap<Long, List<Long>>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT A.Id, A.userId FROM Mmall_Order_ReturnForm A, Mmall_Order_OrderForm B ")
		   .append("WHERE (")
		   .append("A.userId = B.userId").append(" AND ")
		   .append("A.orderId = B.orderId").append(" AND ")
		   .append("B.omsTime < ").append(omsTime).append(" AND ")
		   .append("(")
		   .append("A.returnState = ").append(DeprecatedReturnState.INIT.getIntValue()).append(" OR ")
		   .append("A.returnState = ").append(DeprecatedReturnState.APPLY_RETURN.getIntValue()).append(" OR ")
		   .append("A.returnState = ").append(DeprecatedReturnState.WAITING_CONFIRM.getIntValue())
		   .append(")")
		   .append(")");
		DBResource dbr = getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbr) ? null : dbr.getResultSet();
		if(null == rs) {
			return idList;
		}
		try {
			while (rs.next()) {
				long id = rs.getLong("A.Id");
				long userId = rs.getLong("A.userId");
				if(!idList.containsKey(userId)) {
					idList.put(userId, new ArrayList<Long>());
				}
				idList.get(userId).add(id);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage());
			return idList;
		} finally {
			dbr.close();
		}
		return idList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#getReturnedButNotDistributedReturnFormList(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<DeprecatedReturnForm> getReturnedButNotDistributedReturnFormList(long minRetId, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "returnState", new DeprecatedReturnState[] {DeprecatedReturnState.FINISH_RETURN});
		SqlGenUtil.appendExtParamObject(sql, "useCoupon", false);
		sql.append(" And Id>").append(minRetId);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.DeprecatedReturnFormDao#distributeCoupon(long)
	 */
	@Override
	@Transaction
	public boolean distributeCoupon(DeprecatedReturnForm retForm) {
		if(null == retForm) {
			return false;
		}
		retForm = getLockByKey(retForm);
		
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("useCoupon");
		retForm.setUseCoupon(true);
		
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retForm));
		SqlGenUtil.appendExtParamObject(sql, "useCoupon", false);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
	
}
