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
import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam;
import com.xyl.mmall.order.enums.JITPushState;
import com.xyl.mmall.order.enums.ReturnExpHbState;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.meta.ReturnPackage;
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
@Repository("returnPackageDao")
public class ReturnPackageDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ReturnPackage> implements ReturnPackageDao {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByOrderIdAndUserId(long, long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByOrderIdAndUserId(long orderId, long userId, boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByStateWithOrderIdAndUserId(long, long, boolean, com.xyl.mmall.order.enums.ReturnPackageState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByStateWithOrderIdAndUserId(long orderId, long userId, boolean deprecated, 
			ReturnPackageState[] stateArray, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByOrderIdAndUserId(java.util.Collection, long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<ReturnPackage> queryObjectsByOrderIdAndUserId(Collection<Long> orderIdColl, long userId,
			boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamColl(sql, "orderId", orderIdColl);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByOrderPackageIdAndUserId(long, long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByOrderPackageIdAndUserId(
			long orderPackageId, long userId, boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderPkgId", orderPackageId);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByState(com.xyl.mmall.order.enums.ReturnPackageState[], boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByState(ReturnPackageState[] stateArray, boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByStateWithUserId(com.xyl.mmall.order.enums.ReturnPackageState[], long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByStateWithUserId(ReturnPackageState[] stateArray, long userId, 
			boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByStateWithOrderId(com.xyl.mmall.order.enums.ReturnPackageState[], long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByStateWithOrderId(ReturnPackageState[] stateArray, long orderId, 
			boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByStateWithOrderPackageId(com.xyl.mmall.order.enums.ReturnPackageState[], long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByStateWithOrderPackageId(ReturnPackageState[] stateArray, long ordPkgId,
			boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderPkgId", ordPkgId);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryWaitingJITPushObjectsByStateWithMinRetPkgId(long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryWaitingJITPushObjectsByStateWithMinRetPkgId(long minRetPkgId, boolean deprecated, DDBParam param) {
		ReturnPackageState[] stateArray = new ReturnPackageState[] {ReturnPackageState.WAITING_CONFIRM};
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "jitPushState", JITPushState.WAITING_PUSH);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		sql.append(" And retPkgId > ").append(minRetPkgId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByStateWithTimeRange(com.xyl.mmall.order.enums.ReturnPackageState[], long, long, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByStateWithTimeRange(ReturnPackageState[] stateArray, long start, long end,
			boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		String sqlStr = sql.toString().trim();
		if (!(sqlStr.endsWith(" WHERE") || sqlStr.endsWith(" AND") || sqlStr.endsWith(" OR") || sqlStr.endsWith("("))) {
			sql.append(" AND ").append("ctime >= ").append(start).append(" AND ").append("ctime <= ").append(end);
		}
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#queryObjectsByStateWithMailNO(com.xyl.mmall.order.enums.ReturnPackageState[], java.lang.String, boolean, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> queryObjectsByStateWithMailNO(ReturnPackageState[] stateArray, String mailNO, 
			boolean deprecated, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "mailNO", mailNO);
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "deprecated", deprecated);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#updateReturnPackageState(com.xyl.mmall.order.meta.ReturnPackage, com.xyl.mmall.order.enums.ReturnPackageState, com.xyl.mmall.order.enums.ReturnPackageState[], java.lang.String)
	 */
	@Override
	@Transaction
	public boolean updateReturnPackageState(ReturnPackage retPkg, ReturnPackageState newState, 
			ReturnPackageState[] stateArray, String extInfo) {
		if(null == retPkg) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("returnState");
		retPkg.setReturnState(newState);
		if(null != extInfo) {
			setOfUpdate.add("extInfo");
			retPkg.setExtInfo(extInfo);
		}
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#updateReturnPackageStateToWaitingConfirmWithExpInfo(com.xyl.mmall.order.meta.ReturnPackage, com.xyl.mmall.order.param.ReturnPackageExpInfoParam)
	 */
	@Override
	@Transaction
	public boolean updateReturnPackageStateToWaitingConfirmWithExpInfo(ReturnPackage retPkg, ReturnPackageExpInfoParam param) {
		if (null == retPkg || null == param) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("returnExpInfoId");
		retPkg.setReturnExpInfoId(param.getReturnExpInfoId());
		setOfUpdate.add("mailNO");
		retPkg.setMailNO(param.getMailNO());
		setOfUpdate.add("expressCompany");
		retPkg.setExpressCompany(param.getExpressCompany());
		setOfUpdate.add("returnState");
		retPkg.setReturnState(ReturnPackageState.WAITING_CONFIRM);
		setOfUpdate.add("jitPushState");
		retPkg.setJitPushState(JITPushState.WAITING_PUSH);
		ReturnPackageState[] stateArray = new ReturnPackageState[] { ReturnPackageState.APPLY_RETURN };
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "jitPushState", JITPushState.INIT);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#updateReturnPackageStateToWaitingAuditWithConfirmTime(com.xyl.mmall.order.meta.ReturnPackage, boolean, boolean, com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam)
	 */
	@Override
	@Transaction
	public boolean updateReturnPackageStateToWaitingAuditWithConfirmTime(ReturnPackage retPkg, boolean abnormal, 
			boolean isCOD, _ReturnPackagePriceParam param) {
		if (null == retPkg) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("confirmTime");
		retPkg.setConfirmTime(System.currentTimeMillis());
		setOfUpdate.add("returnState");
		ReturnPackageState state = null;
		if(abnormal) {
			state = isCOD ? ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT : ReturnPackageState.ABNORMAL_WAITING_AUDIT;
			retPkg.setReturnState(state);
		} else {
			if(isCOD) {
				retPkg.setReturnState(ReturnPackageState.CW_WAITING_RETURN);
			} else {
				retPkg.setReturnState(ReturnPackageState.FINISH_RETURN);
				setOfUpdate.add("returnOperationTime");
				retPkg.setReturnOperationTime(System.currentTimeMillis());
			}
		}
		if(null != param) {
			setOfUpdate.add("payedTotalPriceToUser");
			retPkg.setPayedTotalPriceToUser(param.getReturnTotalPrice());
			setOfUpdate.add("payedHbPriceToUser");
			retPkg.setPayedHbPriceToUser(param.getReturnHbPrice());
			setOfUpdate.add("payedCashPriceToUser");
			retPkg.setPayedCashPriceToUser(param.getReturnCashPrice());
		}
		ReturnPackageState[] stateArray = new ReturnPackageState[] { ReturnPackageState.WAITING_CONFIRM };
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#updateReturnPackageStateToRefusedWithParam(com.xyl.mmall.order.meta.ReturnPackage, com.xyl.mmall.order.param.ReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public boolean updateReturnPackageStateToRefusedWithParam(ReturnPackage retPkg, ReturnOperationParam param, KFParam kf) {
		if (null == retPkg || null == param) {
			return false;
		}
		ReturnPackageState state = retPkg.getReturnState();
		if(null == state) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("returnState");
		if(state == ReturnPackageState.ABNORMAL_WAITING_AUDIT) {
			retPkg.setReturnState(ReturnPackageState.ABNORMAL_REFUSED);
		}else if(state == ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT) {
			retPkg.setReturnState(ReturnPackageState.ABNORMAL_COD_REFUSED);
		} else {
			return false;
		}
		setOfUpdate.add("returnOperationTime");
		retPkg.setReturnOperationTime(System.currentTimeMillis());
		setOfUpdate.add("extInfo");
		retPkg.setExtInfo(param.getExtInfo());
		if(null != kf) {
			setOfUpdate.add("kfId");
			retPkg.setKfId(kf.getKfId());
			setOfUpdate.add("kfName");
			retPkg.setKfName(kf.getKfName());
		}
		ReturnPackageState[] stateArray = new ReturnPackageState[] { 
				ReturnPackageState.ABNORMAL_WAITING_AUDIT, ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT
			};
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#cancelRefuse(com.xyl.mmall.order.meta.ReturnPackage, com.xyl.mmall.order.param.ReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public boolean cancelRefuse(ReturnPackage retPkg, ReturnOperationParam param, KFParam kf) {
		if (null == retPkg || null == param) {
			return false;
		}
		ReturnPackageState state = retPkg.getReturnState();
		if(null == state || (state != ReturnPackageState.ABNORMAL_REFUSED && state != ReturnPackageState.ABNORMAL_COD_REFUSED)) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("returnState");
		if(state == ReturnPackageState.ABNORMAL_REFUSED) {
			retPkg.setReturnState(ReturnPackageState.ABNORMAL_WAITING_AUDIT);
		} else {
			retPkg.setReturnState(ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT);
		}
		setOfUpdate.add("returnOperationTime");
		retPkg.setReturnOperationTime(System.currentTimeMillis());
		setOfUpdate.add("extInfo");
		retPkg.setExtInfo(param.getExtInfo());
		if(null != kf) {
			setOfUpdate.add("kfId");
			retPkg.setKfId(kf.getKfId());
			setOfUpdate.add("kfName");
			retPkg.setKfName(kf.getKfName());
		}
		ReturnPackageState[] stateArray = new ReturnPackageState[] { 
				ReturnPackageState.ABNORMAL_REFUSED, ReturnPackageState.ABNORMAL_COD_REFUSED
			};
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#updateReturnPackageStateToAuditPassedWithParam(com.xyl.mmall.order.meta.ReturnPackage, com.xyl.mmall.order.param.PassReturnOperationParam, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	@Transaction
	public boolean updateReturnPackageStateToAuditPassedWithParam(ReturnPackage retPkg, PassReturnOperationParam param, KFParam kf) {
		if (null == retPkg || null == param) {
			return false;
		}
		ReturnPackageState state = retPkg.getReturnState();
		if(null == state) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("returnState");
		if(state == ReturnPackageState.ABNORMAL_WAITING_AUDIT) {
			retPkg.setReturnState(ReturnPackageState.FINISH_RETURN);
		} else if (state == ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT) {
			retPkg.setReturnState(ReturnPackageState.CW_WAITING_RETURN);
		} else {
			return false;
		}
		setOfUpdate.add("payedTotalPriceToUser");
		BigDecimal payedHbPrice = retPkg.getPayedHbPriceToUser();
		if(null == payedHbPrice) {
			payedHbPrice = BigDecimal.ZERO;
		}
		retPkg.setPayedTotalPriceToUser(payedHbPrice.add(param.getCashPriceToUser()));
		setOfUpdate.add("payedCashPriceToUser");
		retPkg.setPayedCashPriceToUser(param.getCashPriceToUser());
		setOfUpdate.add("returnOperationTime");
		retPkg.setReturnOperationTime(System.currentTimeMillis());
		setOfUpdate.add("extInfo");
		retPkg.setExtInfo(param.getExtInfo());
		if(null != kf) {
			setOfUpdate.add("kfId");
			retPkg.setKfId(kf.getKfId());
			setOfUpdate.add("kfName");
			retPkg.setKfName(kf.getKfName());
		}
		ReturnPackageState[] stateArray = new ReturnPackageState[] { 
				ReturnPackageState.ABNORMAL_WAITING_AUDIT, ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT
			};
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamArray(sql, "returnState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#updateReturnPackageStateToCashReturned(com.xyl.mmall.order.meta.ReturnPackage, com.xyl.mmall.order.param.KFParam, boolean)
	 */
	@Override
	@Transaction
	public boolean updateReturnPackageStateToCashReturned(ReturnPackage retPkg, KFParam kf, boolean isCOD) {
		if (null == retPkg) {
			return false;
		}
		ReturnPackageState state = retPkg.getReturnState();
		if(null == state) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("returnState");
		if(isCOD) {
			retPkg.setReturnState(ReturnPackageState.FINALLY_COD_RETURNED_TO_USER);
		} else {
			retPkg.setReturnState(ReturnPackageState.FINALLY_RETURNED_TO_USER);
		}
		setOfUpdate.add("returnOperationTime");
		retPkg.setReturnOperationTime(System.currentTimeMillis());
		setOfUpdate.add("retExpCouponState");
		retPkg.setRetExpCouponState(ReturnExpHbState.WAITING_DISTRIBUTING);
		if(null != kf) {
			setOfUpdate.add("kfId");
			retPkg.setKfId(kf.getKfId());
			setOfUpdate.add("kfName");
			retPkg.setKfName(kf.getKfName());
		}
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		if(isCOD) {
			SqlGenUtil.appendExtParamObject(sql, "returnState", ReturnPackageState.CW_WAITING_RETURN);
		} else {
			SqlGenUtil.appendExtParamObject(sql, "returnState", ReturnPackageState.FINISH_RETURN);
		}
		SqlGenUtil.appendExtParamObject(sql, "retExpCouponState", ReturnExpHbState.INIT);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#getWaitingReturnAuditCount()
	 */
	@Override
	public Map<Integer, Long> getWaitingReturnAuditCount() {
		Map<Integer, Long> retMap = new HashMap<Integer, Long>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT COUNT(B.retPkgId), A.provinceId FROM Mmall_Order_OrderForm A, Mmall_Order_ReturnPackage B")
		   .append(" WHERE A.userId = B.userId AND A.orderId = B.orderId AND B.deprecated = False")
		   .append(" AND (B.returnState = ").append(ReturnPackageState.ABNORMAL_WAITING_AUDIT.getIntValue())
		   .append(" OR B.returnState = ").append(ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT.getIntValue())
		   .append(") GROUP BY A.provinceId");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				long count = rs.getLong("COUNT(B.retPkgId)");
				// int provinceId = rs.getInt("A.provinceId");
				// for DDB 
				int provinceId = rs.getInt("provinceId");
				retMap.put(provinceId, count);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage());
			return retMap;
		} finally {
			dbr.close();
		}
		return retMap;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#getWaitingReturnCountOfCOD()
	 */
	@Override
	public Map<Integer, Long> getWaitingReturnCountOfCOD() {
		Map<Integer, Long> retMap = new HashMap<Integer, Long>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT COUNT(B.retPkgId), A.provinceId FROM Mmall_Order_OrderForm A, Mmall_Order_ReturnPackage B")
		   .append(" WHERE A.userId = B.userId AND A.orderId = B.orderId AND B.deprecated = False")
		   .append(" AND B.returnState = ").append(ReturnPackageState.CW_WAITING_RETURN.getIntValue())
		   .append(" GROUP BY A.provinceId");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				long count = rs.getLong("COUNT(B.retPkgId)");
				// int provinceId = rs.getInt("A.provinceId");
				// for DDBS
				int provinceId = rs.getInt("provinceId");
				retMap.put(provinceId, count);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage());
			return retMap;
		} finally {
			dbr.close();
		}
		return retMap;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#getObjectsShouldBeCanceled(long)
	 */
	@SuppressWarnings("resource")
	@Override
	public Map<Long, List<Long>> getObjectsShouldBeCanceled(long detainTime) {
		long omsTime = System.currentTimeMillis() - detainTime;
		Map<Long, List<Long>> idList = new HashMap<Long, List<Long>>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT A.retPkgId, A.userId FROM Mmall_Order_ReturnPackage A, Mmall_Order_OrderPackage B ")
		   .append("WHERE (")
		   .append("A.userId = B.userId").append(" AND ")
		   .append("A.orderPkgId = B.packageId").append(" AND ")
		   .append("A.deprecated = False").append(" AND ")
		   .append("B.expSTime < ").append(omsTime).append(" AND ")
		   .append("(")
		   .append("A.returnState = ").append(ReturnPackageState.INIT.getIntValue()).append(" OR ")
		   .append("A.returnState = ").append(ReturnPackageState.APPLY_RETURN.getIntValue()).append(" OR ")
		   .append("A.returnState = ").append(ReturnPackageState.WAITING_CONFIRM.getIntValue())
		   .append(")")
		   .append(")");
		DBResource dbr = getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbr) ? null : dbr.getResultSet();
		if(null == rs) {
			return idList;
		}
		try {
			while (rs.next()) {
				// long id = rs.getLong("A.retPkgId");
				// long userId = rs.getLong("A.userId");
				// for DDB
				long id = rs.getLong("retPkgId");
				long userId = rs.getLong("userId");
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
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#getReturnPackageShouldReturnCashByMinRetPkgId(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> getReturnPackageShouldReturnCashByMinRetPkgId(long minRetPkgId, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "returnState", ReturnPackageState.FINISH_RETURN);
		sql.append(" And retPkgId > ").append(minRetPkgId);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#getReturnedButNotDistributedObjects(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ReturnPackage> getReturnedButNotDistributedObjects(long minRetPkgId, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "returnState", new ReturnPackageState[] {
				ReturnPackageState.FINALLY_RETURNED_TO_USER, 
				ReturnPackageState.FINALLY_COD_RETURNED_TO_USER
			});
		SqlGenUtil.appendExtParamObject(sql, "retExpCouponState", ReturnExpHbState.WAITING_DISTRIBUTING);
		sql.append(" And retPkgId > ").append(minRetPkgId);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#distributeHb(com.xyl.mmall.order.meta.ReturnPackage)
	 */
	@Override
	@Transaction
	public boolean distributeHb(ReturnPackage retPkg) {
		if(null == retPkg) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("retExpCouponState");
		retPkg.setRetExpCouponState(ReturnExpHbState.DISTRIBUTED);
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamObject(sql, "retExpCouponState", ReturnExpHbState.WAITING_DISTRIBUTING);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#deprecateRecord(com.xyl.mmall.order.meta.ReturnPackage)
	 */
	@Override
	@Transaction
	public boolean deprecateRecord(ReturnPackage retPkg) {
		if(null == retPkg) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("deprecated");
		retPkg.setDeprecated(true);
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamObject(sql, "returnState", ReturnPackageState.APPLY_RETURN);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#deprecateRecordByKf(com.xyl.mmall.order.meta.ReturnPackage, com.xyl.mmall.order.param.KFParam)
	 */
	@Override
	public boolean deprecateRecordByKf(ReturnPackage retPkg, KFParam kf) {
		if(null == retPkg) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("deprecated");
		retPkg.setDeprecated(true);
		setOfUpdate.add("returnOperationTime");
		retPkg.setReturnOperationTime(System.currentTimeMillis());
		if(null != kf) {
			setOfUpdate.add("kfId");
			retPkg.setKfId(kf.getKfId());
			setOfUpdate.add("kfName");
			retPkg.setKfName(kf.getKfName());
		}
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamArray(sql, "returnState", new ReturnPackageState[] {
				ReturnPackageState.ABNORMAL_REFUSED, ReturnPackageState.ABNORMAL_COD_REFUSED
				});
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.ReturnPackageDao#updateJITPushState(com.xyl.mmall.order.meta.ReturnPackage, com.xyl.mmall.order.enums.JITPushState, com.xyl.mmall.order.enums.JITPushState[])
	 */
	@Override
	@Transaction
	public boolean updateJITPushState(ReturnPackage retPkg, JITPushState newState, JITPushState[] oldStates) {
		if(null == retPkg || null == newState || null == oldStates) {
			return false;
		}
		retPkg = getLockByKey(retPkg);
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("jitPushState");
		retPkg.setJitPushState(newState);
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, retPkg));
		SqlGenUtil.appendExtParamArray(sql, "jitPushState", oldStates);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

}
