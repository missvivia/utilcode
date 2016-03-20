package com.xyl.mmall.saleschedule.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.saleschedule.dao.ScheduleLikeDao;
import com.xyl.mmall.saleschedule.enums.ScheduleLikeState;
import com.xyl.mmall.saleschedule.enums.DBField.LikeField;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleLike;

/**
 * ScheduleLike表操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class ScheduleLikeDaoImpl extends ScheduleBaseDao<ScheduleLike> implements ScheduleLikeDao {

	private String tableName = this.getTableName();

	private String delSql = "DELETE FROM " + tableName + " WHERE " + LikeField.scheduleId + "=? AND "
			+ LikeField.userId + "=?";

	private String delByScheduleIdSql = "DELETE FROM " + tableName + " WHERE " + LikeField.scheduleId + "=?";

	private String delByUserIdSql = "DELETE FROM " + tableName + " WHERE " + LikeField.userId + "=?";

	@Override
	public boolean saveScheduleLike(ScheduleLike like) {
		logger.debug("saveScheduleLike: " + like);
		try {
			ScheduleLike dbObj = getScheduleLikeByScheduleIdAndUserId(like.getScheduleId(), like.getUserId());
			if (dbObj == null || dbObj.getId() == 0) {
				like = addObject(like);
			} else {
				logger.debug("User '" + like.getUserId() + "' already liked PO '" + like.getScheduleId() + "'!!");
				like.setId(dbObj.getId());
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateScheduleLike(ScheduleLike like) {
		logger.debug("updateScheduleLike: " + like);
		try {
			return updateObjectByKey(like);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteScheduleLikeById(long id) {
		logger.debug("deleteScheduleLikeById: " + id);
		try {
			return deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteScheduleLikeByScheduleIdAndUserId(long scheduleId, long userId) {
		logger.debug("deleteScheduleLikeByScheduleIdAndUserId: " + scheduleId + " -- " + userId);
		try {
			Object[] args = { scheduleId, userId };
			getSqlSupport().excuteUpdate(delSql, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteScheduleLikeByScheduleId(long scheduleId) {
		logger.debug("deleteScheduleLikeByScheduleId: " + scheduleId);
		try {
			Object[] args = { scheduleId };
			getSqlSupport().excuteUpdate(delByScheduleIdSql, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteScheduleLikeByUserId(long userId) {
		logger.debug("deleteScheduleLikeByUserId: " + userId);
		try {
			Object[] args = { userId };
			getSqlSupport().excuteUpdate(delByUserIdSql, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public ScheduleLike getScheduleLikeById(long id) {
		logger.debug("getScheduleLikeById: " + id);
		try {
			return getObjectById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public ScheduleLike getScheduleLikeByScheduleIdAndUserId(long scheduleId, long userId) {
		logger.debug("getScheduleLikeByScheduleIdAndUserId: " + scheduleId + " -- " + userId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, LikeField.scheduleId, scheduleId);
		SqlGenUtil.appendExtParamObject(sql, LikeField.userId, userId);
		try {
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean saveShceduleLikeList(List<ScheduleLike> likeList) {
		logger.debug("saveShceduleLikeList: " + likeList);
		try {
			return this.addObjects(likeList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public List<ScheduleLike> getScheduleLikeListByStatus(ScheduleLikeState status) {
		logger.debug("getScheduleLikeListByStatus: " + status);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, LikeField.status, status.getIntValue());
		try {
			return queryObjects(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<ScheduleLike>();
		}
	}

	@Override
	public List<ScheduleLike> getScheduleLikeListByUserId(long userId) {
		logger.debug("getScheduleLikeListByUserId: " + userId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, LikeField.userId, userId);
		try {
			return queryObjects(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<ScheduleLike>();
		}
	}

	public List<Schedule> queryScheduleListTemplate(StringBuilder sqlAll) throws SQLException {
		List<Schedule> scheduleList = new ArrayList<Schedule>();

		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sqlAll.toString());
			rs = dbr.getResultSet();
			while (rs.next()) {
				//Schedule schedule = super.genScheduleObjFromRS(rs);
				Schedule schedule = (Schedule) getObjFromRS(rs, Schedule.class, null);
				scheduleList.add(schedule);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeDBQuitely(dbr, rs);
		}

		return scheduleList;
	}

//	@Override
//	public POListDTO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long timestamp, int limit) {
//		logger.debug("getLikedPOListByUserIdAndSiteId: " + userId + "; " + supplierAreaId + "; " + timestamp + "; "
//				+ limit);
//		StringBuilder sql = new StringBuilder(512);
//		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleLike B WHERE 1=1 ");
//		sql.append(" AND A." + ScheduleField.id + " = B." + LikeField.scheduleId).append(" AND B.userId = " + userId)
//				.append(" AND A.status = " + ScheduleState.BACKEND_PASSED.getIntValue());
//
//		if (supplierAreaId > 0) {
//			sql.append(" AND A." + ScheduleField.curSupplierAreaId + "=" + supplierAreaId);
//		}
//
//		if (timestamp > 0) {
//			Calendar c = Calendar.getInstance();
//			c.setTimeInMillis(timestamp);
//			setFirst(c);
//			sql.append(" AND A." + ScheduleField.startTime + " >= " + c.getTimeInMillis());
//		}
//
//		String sqlSelectHeader = "SELECT A.* FROM ";
//		String pageSql = geneTopSql(limit);
//		String orderBySql = geneOrderBySql("A." + ScheduleField.startTime);
//
//		return getPOListResult(sqlSelectHeader, sql.toString(), orderBySql, pageSql, false, 0, 0);
//	}
//
//	@Override
//	public POListDTO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long startId, int curPage,
//			int pageSize) {
//		logger.debug("getLikedPOListByUserIdAndSiteId: " + userId + "; " + supplierAreaId + "; " + startId);
//		StringBuilder sql = new StringBuilder(512);
//		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleLike B WHERE 1=1 ");
//		sql.append(" AND A." + ScheduleField.id + " = B." + LikeField.scheduleId).append(" AND B.userId = " + userId)
//				.append(" AND A.status = " + ScheduleState.BACKEND_PASSED.getIntValue());
//		if (startId > 0) {
//			sql.append(" AND A." + ScheduleField.id + " > " + startId);
//		}
//		if (supplierAreaId != 0) {
//			sql.append(" AND A." + ScheduleField.curSupplierAreaId + "=" + supplierAreaId);
//		}
//
//		String sqlSelectHeader = "SELECT A.* FROM ";
//		String pageSql = genePageSql(curPage, pageSize);
//		String orderBySql = geneOrderBySql("A." + ScheduleField.id);
//
//		return getPOListResult(sqlSelectHeader, sql.toString(), orderBySql, pageSql, true, curPage, pageSize);
//	}

	@Override
	public List<ScheduleLike> getScheduleLikeListByScheduleId(long scheduleId) {
		logger.debug("getScheduleLikeListByScheduleId: " + scheduleId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, LikeField.scheduleId, scheduleId);
		try {
			return queryObjects(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<ScheduleLike>();
		}
	}

	@Override
	public long getLikeCntByPOId(long poId) {
		logger.debug("getLikeCntByPOId: " + poId);
		String sql = "SELECT COUNT(" + LikeField.id + ")  CNT FROM " + tableName + " WHERE " + LikeField.scheduleId
				+ "=" + poId;

		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql);
			rs = dbr.getResultSet();
			long CNT = 0l;
			if (rs.next()) {
				CNT = rs.getLong("CNT");
			}
			return CNT;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return 0L;
		} finally {
			closeDBQuitely(dbr, rs);
		}
	}
}
