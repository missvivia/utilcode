package com.xyl.mmall.order.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午10:33:34
 * 
 */
@Repository("orderFormDao")
public class OrderFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderForm> implements OrderFormDao {

	private static Logger logger = Logger.getLogger(OrderFormDaoImpl.class);

	public List<OrderForm> queryOrderFormList(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "isVisible", isVisible);
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}

	public List<OrderForm> queryOrderFormListByUserIdAndParentId(long userId, long parentId) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("SELECT * FROM Mmall_Order_OrderForm WHERE userId=" + userId + " AND parentId=" + parentId);
		return queryObjects(sb);
	}

	public List<OrderForm> queryOrderFormListByParentId(long parentId, boolean isVisible) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT * FROM Mmall_Order_OrderForm WHERE ParentId=").append(parentId).append(" and IsVisible=")
				.append(isVisible ? 1 : 0).append(";");
		return queryObjects(sql);
	}

	public List<OrderForm> queryOrderFormListByParentId(long parentId) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("SELECT * FROM Mmall_Order_OrderForm WHERE parentId=" + parentId);
		return queryObjects(sb);
	}

	public List<OrderForm> queryOrderFormListByUserIdAndParentId(long userId, long parentId, Boolean isVisible) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("SELECT * FROM Mmall_Order_OrderForm WHERE userId=" + userId + " AND parentId=" + parentId
				+ " AND isVisible=" + isVisible);
		return queryObjects(sb);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormListCount(long,
	 *      java.lang.Boolean, com.xyl.mmall.order.enums.OrderFormState[],
	 *      long[])
	 */
	public int queryOrderFormListCount(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, Boolean isOnPay) {
		StringBuilder sql = new StringBuilder(256);
		if (stateArray != null && stateArray.length == 1) {
			if (stateArray[0].equals(OrderFormState.WAITING_PAY)) {
				sql.append("select count(DISTINCT ParentId) from Mmall_Order_OrderForm where OrderFormPayMethod = 2 ");// 在线支付未付款时订单合并
			} else if (stateArray[0].equals(OrderFormState.WAITING_DELIVE) && isOnPay != null) {
				if (isOnPay) {
					sql.append(" select count(OrderId) from Mmall_Order_OrderForm where OrderFormPayMethod = 2 ");
				} else {
					sql.append(" select count(DISTINCT ParentId) from Mmall_Order_OrderForm where OrderFormPayMethod != 2 ");// 货到付款待发货时订单合并
				}
			}
		}
		if (sql.length() == 0) {
			sql.append(genCountSql());
		}
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "isVisible", isVisible);
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormList(java.lang.Boolean,
	 *      com.xyl.mmall.order.enums.OrderFormState[], long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderForm> queryOrderFormList(Boolean isVisible, OrderFormState[] stateArray, long[] orderTimeRange,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "isVisible", isVisible);
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormListWithLackPackage(java.lang.Boolean,
	 *      com.xyl.mmall.order.enums.OrderFormState[], long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderForm> queryOrderFormListWithLackPackage(Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(
				"SELECT A.* FROM Mmall_Order_OrderForm A, Mmall_Order_OrderPackage B where A.userId = B.userId AND A.orderId = B.orderId ")
				.append("AND A.orderTime >= ").append(orderTimeRange[0]).append(" AND A.orderTime <= ")
				.append(orderTimeRange[1]);
		SqlGenUtil.appendExtParamObject(sql, "A.isVisible", isVisible);
		SqlGenUtil.appendExtParamArray(sql, "A.orderFormState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "B.orderPackageState", OrderPackageState.OUT_TIME);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormList(java.util.List,
	 *      java.lang.Boolean, com.xyl.mmall.order.enums.OrderFormState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderForm> queryOrderFormList(List<Long> userIdList, Boolean isVisible, OrderFormState[] stateArray,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "userId", userIdList.toArray());
		SqlGenUtil.appendExtParamObject(sql, "isVisible", isVisible);
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormList(long,
	 *      java.lang.Boolean, java.lang.Long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderForm> queryOrderFormList(long userId, Boolean isVisible, Long orderIdOfPart,
			long[] orderTimeRange, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "isVisible", isVisible);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		if (orderIdOfPart != null)
			sql.append(" AND OrderId LIKE '%").append(orderIdOfPart).append("%'");

		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormListWithMinOrderId(long,
	 *      com.xyl.mmall.order.enums.OrderFormState[], long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderForm> queryOrderFormListWithMinOrderId(long minOrderId, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		sql.append(" And OrderId>").append(minOrderId);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormDTOListWithMinOrderId(long,
	 *      com.xyl.mmall.order.enums.OrderFormState[], long[],
	 *      com.xyl.mmall.framework.enums.PayState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderForm> queryOrderFormDTOListWithMinOrderId(long minOrderId, OrderFormState[] orderFormStateArray,
			long[] orderTimeRange, PayState[] payStateArray, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", orderFormStateArray);
		SqlGenUtil.appendExtParamArray(sql, "payState", payStateArray);
		sql.append(" And OrderId>").append(minOrderId);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormListByStateWithMinOrderId(long,
	 *      com.xyl.mmall.order.enums.OrderFormState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderForm> queryOrderFormListByStateWithMinOrderId(long minOrderId, OrderFormState[] stateArray,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		sql.append(" And OrderId>").append(minOrderId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#queryOrderFormCount(long,
	 *      java.lang.Boolean, com.xyl.mmall.order.enums.OrderFormState[])
	 */
	public int queryOrderFormCount(long userId, Boolean isVisible, OrderFormState[] stateArray, long[] orderTimeRange) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "isVisible", isVisible);
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		if (orderTimeRange != null && orderTimeRange.length > 1)
			OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.dao.PolicyObjectDaoSqlBaseOfPrint#getObjectFromRs(java.sql.ResultSet)
	 */
	public OrderForm getObjectFromRs(ResultSet rs) throws SQLException {
		OrderForm obj = super.getObjectFromRs(rs);
		return obj;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateOrdState(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.order.enums.OrderFormState[])
	 */
	public boolean updateOrdState(OrderForm obj, OrderFormState[] oldStateArray) {
		if (CollectionUtil.isEmptyOfArray(oldStateArray))
			return false;
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("orderFormState");
		switch (obj.getOrderFormState()) {
		case WAITING_DELIVE:
			if (obj.getPayTime() <= 0 && OrderFormPayMethod.isOnlinePayMethod(obj.getOrderFormPayMethod())) {
				obj.setPayTime(System.currentTimeMillis());
				obj.setPayState(PayState.ONLINE_PAYED);
				setOfUpdate.add("payTime");
				setOfUpdate.add("payState");
			}
			break;
		case ALL_DELIVE:
			// 充当发货时间
			if (obj.getOmsTime() <= 0) {
				obj.setOmsTime(System.currentTimeMillis());
				setOfUpdate.add("omsTime");
			}
			break;
		case FINISH_TRADE:
			if (obj.getPayTime() <= 0 && !OrderFormPayMethod.isOnlinePayMethod(obj.getOrderFormPayMethod())) {
				obj.setPayTime(System.currentTimeMillis());
				obj.setPayState(PayState.COD_PAYED);
				setOfUpdate.add("payTime");
				setOfUpdate.add("payState");
			}
			// 结束时间
			if (obj.getConfirmTime() <= 0) {
				obj.setConfirmTime(System.currentTimeMillis());
				setOfUpdate.add("confirmTime");
			}
			break;
		default:
			break;
		}

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", oldStateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updatePayState(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.framework.enums.PayState[])
	 */
	@Override
	public boolean updatePayState(OrderForm obj, PayState[] oldStateArray) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("payState");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamArray(sql, "payState", oldStateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updatePayStateWithPayTime(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.framework.enums.PayState[])
	 */
	@Override
	public boolean updatePayStateWithPayTime(OrderForm obj, PayState[] oldStateArray) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("payState");
		if (obj.getPayTime() > 0)
			setOfUpdate.add("payTime");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamArray(sql, "payState", oldStateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateOrdStateWithConfirmTime(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.order.enums.OrderFormState[])
	 */
	public boolean updateOrdStateToFinishDelive(OrderForm obj) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("orderFormState");
		setOfUpdate.add("confirmTime");

		if (obj.getConfirmTime() <= 0)
			obj.setConfirmTime(System.currentTimeMillis());
		obj.setOrderFormState(OrderFormState.FINISH_TRADE);
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", new OrderFormState[] { OrderFormState.ALL_DELIVE,
				OrderFormState.PART_DELIVE });
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateOrdStateWithOMSTime(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.order.enums.OrderFormState[])
	 */
	public boolean updateOrdStateWithOMSTime(OrderForm obj, OrderFormState[] oldStateArray) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), new String[] { "orderFormState", "omsTime" }, new String[] {
				"orderId", "userId" }, obj));
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", oldStateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateOrdStateAndPayState(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.order.enums.OrderFormState[],
	 *      com.xyl.mmall.order.enums.PayState[])
	 */
	public boolean updateOrdStateAndPayState(OrderForm obj, OrderFormState[] oldOrderFormStateArray,
			PayState[] oldPayStateArray) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("orderFormState");
		setOfUpdate.add("payState");
		if (obj.getPayTime() > 0)
			setOfUpdate.add("payTime");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", oldOrderFormStateArray);
		SqlGenUtil.appendExtParamArray(sql, "payState", oldPayStateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateIsVisible(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.order.enums.OrderFormState[])
	 */
	public boolean updateIsVisible(OrderForm obj, OrderFormState[] oldStateArray) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("isVisible");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", oldStateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateOrdByType1(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.order.meta.OrderForm)
	 */
	public boolean updateOrdByType1(OrderForm objOfOri, OrderForm objOfNew) {
		Collection<String> fieldNameCollOfUpdate = Arrays.asList(new String[] { "orderFormState", "orderFormPayMethod",
				"payState" });
		Collection<String> fieldNameCollOfWhere = Arrays.asList(new String[] { "orderFormState", "orderFormPayMethod",
				"payState" });
		return PrintDaoUtil.updateObjectByKey(objOfOri, objOfNew, fieldNameCollOfUpdate, fieldNameCollOfWhere, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#getLockByKeys(java.util.Collection)
	 */
	public List<OrderForm> getLockByKeys(Collection<OrderForm> objList) {
		if (CollectionUtil.isEmptyOfCollection(objList))
			return null;
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql()).append(" AND ( 1=0 ");
		for (OrderForm order : objList) {
			sql.append(" OR ( orderId=").append(order.getOrderId()).append(" AND ").append("userId=")
					.append(order.getUserId()).append(" )");
		}
		sql.append(" ) ");
		SqlGenUtil.appendForUpdate(sql);
		return queryObjects(sql);
	}

	@Override
	public int queryCountAfterOmsTime(long userId, long omsTime, OrderFormState[] stateArray) {
		return queryCountExec("omsTime", userId, omsTime, stateArray);
	}

	private int queryCountExec(String column, long userId, long time, OrderFormState[] stateArray) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		sql.append(" And " + column + " > " + time);
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateCODAuditPayTime(com.xyl.mmall.order.meta.OrderForm)
	 */
	@Override
	@Transaction
	public boolean updateCODAuditPayTime(OrderForm ordForm) {
		if (null == ordForm) {
			return false;
		}
		ordForm = getLockByKey(ordForm);
		ordForm.setPayTime(System.currentTimeMillis());
		return updateObjectByKey(ordForm);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateOrdState(java.util.Collection,
	 *      com.xyl.mmall.order.enums.OrderFormState,
	 *      com.xyl.mmall.order.enums.OrderFormState[])
	 */
	public int updateOrdState(Collection<? extends OrderForm> orderColl, OrderFormState newState,
			OrderFormState[] oldStateArray) {
		if (CollectionUtil.isEmptyOfCollection(orderColl))
			return 0;

		StringBuilder sql = new StringBuilder(1024);
		sql.append("UPDATE ").append(getTableName()).append(" SET orderFormState=").append(newState.getIntValue());
		sql.append(" WHERE ( 1=2 ");
		for (OrderForm order : orderColl) {
			sql.append(" OR ( OrderId=").append(order.getOrderId()).append(" AND UserId=").append(order.getUserId())
					.append(" )");
		}
		sql.append(" ) ");
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", oldStateArray);
		return getSqlSupport().excuteUpdate(sql.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderFormDao#updateOrdStateAndExtInfo(com.xyl.mmall.order.meta.OrderForm,
	 *      com.xyl.mmall.order.enums.OrderFormState[], java.lang.String)
	 */
	public boolean updateOrdStateAndExtInfo(OrderForm obj, OrderFormState[] oldStateArray, String oldExtInfo) {
		if (oldExtInfo == null || CollectionUtil.isEmptyOfArray(oldStateArray))
			return false;

		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("orderFormState");
		setOfUpdate.add("extInfo");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", oldStateArray);
		SqlGenUtil.appendExtParamObject(sql, "extInfo", oldExtInfo);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	public boolean updateComment(OrderForm obj) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), new String[] { "comment" }, new String[] { "orderId",
				"userId" }, obj));
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}


	private String genWhereSql(OrderSearchParam param) {
		StringBuilder sql = new StringBuilder(256);
		if (param.getOrderId() > 0) {
			SqlGenUtil.appendExtParamObject(sql, "oo.orderId", param.getOrderId());
		}
		if (param.getUserId() > 0) {
			SqlGenUtil.appendExtParamObject(sql, "oo.userId", param.getUserId());
		}
		if (param.getBusinessId() > 0) {
			SqlGenUtil.appendExtParamObject(sql, "BusinessId", param.getBusinessId());
		}
		if (param.getOrderStatus() >= 0) {
			SqlGenUtil.appendExtParamObject(sql, "OrderFormState", param.getOrderStatus());
		}
		if (param.getPayMethod() > 0) {
			SqlGenUtil.appendExtParamObject(sql, "OrderFormPayMethod", param.getPayMethod());
		}
		if (param.getPayStatus() >= 0) {
			SqlGenUtil.appendExtParamObject(sql, "PayState", param.getPayStatus());
		}
		if (param.getStime() > 0) {
			sql.append(" AND oo.CreateTime >= '").append(
					DateUtil.dateToString(new Date(param.getStime()), DateUtil.LONG_PATTERN) + "'");
		}
		if (param.getEtime() > 0) {
			sql.append(" AND oo.CreateTime <= '").append(
					DateUtil.dateToString(new Date(param.getEtime()), DateUtil.LONG_PATTERN) + "'");
		}
		if(param.isUseCoupon()){
			sql.append(" AND CouponDiscount > 0 ");
		}
		if (CollectionUtil.isNotEmptyOfList(param.getUserIdList())) {
			SqlGenUtil.appendExtParamArray(sql, "userId", param.getUserIdList().toArray());
		}
		if (CollectionUtil.isNotEmptyOfList(param.getBusinessIdList())) {
			SqlGenUtil.appendExtParamArray(sql, "BusinessId", param.getBusinessIdList().toArray());
		}
		if (CollectionUtil.isNotEmptyOfList(param.getSiteAreaList())) {
			sql.append(" AND FLOOR(ProvinceId/100) in ");
			sql.append("(");
			for (Long id : param.getSiteAreaList()) {
				sql.append(Math.abs(id)).append(",");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			sql.append(") ");
		}
		return sql.toString();
	}

	@Override
	public int queryBusiOrderFormListCount(long businessId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "businessId", businessId);
		SqlGenUtil.appendExtParamObject(sql, "isVisible", isVisible);
		SqlGenUtil.appendExtParamArray(sql, "orderFormState", stateArray);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public List<Long> getSubOrderIdsByParentId(long parentId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append("SELECT OrderId FROM Mmall_Order_OrderForm WHERE 1=1 ");
		SqlGenUtil.appendExtParamObject(sql, "ParentId", parentId);
		return Arrays.asList(super.queryIds(sql.toString()));
	}

	public List<Long> getParentIds(long parentId, int count) {
		List<Long> list = new ArrayList<Long>(0);
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT DISTINCT ParentId FROM Mmall_Order_OrderForm where ParentId > ").append(parentId)
				.append(" AND isVisible = 1 ORDER BY ParentId ASC LIMIT ").append(count).append(";");

		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet result = dbResource == null ? null : dbResource.getResultSet();
		try {
			if (result != null) {
				while (result.next()) {
					list.add(result.getLong(1));
				}
				result.close();
			}
		} catch (Exception e) {
			list.clear();
			logger.error(e);
			return list;
		} finally {
			if (dbResource != null) {
				dbResource.close();
			}
		}

		return list;
	}

	@Override
	public List<OrderForm> queryOrderFormListByOrderSearchParam(OrderSearchParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT * FROM Mmall_Order_OrderForm oo WHERE 1=1 ");
		sql.append(genWhereSql(param));
		return getListByDDBParam(sql.toString(), param);
	}

	@Override
	public List<OrderForm> queryOrderFormList(String businessIds, long startTime, long endTime, int state) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		sql.append("AND BusinessId IN (").append(businessIds).append(")");
		if (state >= 0) {
			SqlGenUtil.appendExtParamObject(sql, "OrderFormState", state);
		}
		if (startTime > 0l) {
			String dateTime = DateUtil.dateToString(new Date(startTime), DateUtil.LONG_PATTERN);
			sql.append(" AND UpdateTime >= '").append(dateTime).append("'");
		}
		if (endTime > 0l) {
			String dateTime = DateUtil.dateToString(new Date(endTime), DateUtil.LONG_PATTERN);
			sql.append(" AND UpdateTime < '").append(dateTime).append("'");
		}
		this.appendOrderSql(sql, "OrderId", true);
		return queryObjects(sql.toString());
	}
}