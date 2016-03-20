package com.xyl.mmall.saleschedule.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.saleschedule.dao.ScheduleViceDao;
import com.xyl.mmall.saleschedule.enums.DBField.ViceField;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * 
 * @author hzzhanghui
 */
@Repository
public class ScheduleViceDaoImpl extends ScheduleBaseDao<ScheduleVice> implements ScheduleViceDao {
	private String tableName = this.getTableName();

	private String sqlDel = "DELETE FROM " + tableName + " WHERE " + ViceField.scheduleId + "=? ";

	@Override
	public boolean saveScheduleVice(ScheduleVice bean) {
		logger.debug("saveScheduleVice: " + bean);
		try {
			addObject(bean);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateScheduleVice(ScheduleVice bean) {
		logger.debug("updateScheduleVice: " + bean);
		try {
			String sql = getUpdatePOViceSql(bean);
			getSqlSupport().excuteUpdate(sql);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public ScheduleVice getScheduleViceByScheduleId(long scheduleId) {
		logger.debug("getScheduleViceByScheduleId: " + scheduleId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, ViceField.scheduleId, scheduleId);
		try {
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean deleteScheduleViceByScheduleId(long scheduleId) {
		logger.debug("deleteScheduleViceByScheduleId: " + scheduleId);
		try {
			Object[] args = { scheduleId };
			int result = getSqlSupport().excuteUpdate(sqlDel, args);
			if (result == 1) {
				return true;
			} else {
				logger.error("Failured to delete PO vice by scheduleId '" + scheduleId + "'!!!");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateSchedulePageIdAndBannerId(ScheduleVice vice) {
		logger.debug("updateSchedulePageIdAndBannerId: " + vice);
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + tableName + " SET ");
		if (vice.getPageId() != 0 && vice.getBannerId() != 0) {
			sql.append(ViceField.pageId + "= " + vice.getPageId()).append(", ").append(
					ViceField.bannerId + "= " + vice.getBannerId());
		}

		if (vice.getPageId() != 0 && vice.getBannerId() == 0) {
			sql.append(ViceField.pageId + "= " + vice.getPageId());
		}

		if (vice.getPageId() == 0 && vice.getBannerId() != 0) {
			sql.append(ViceField.bannerId + "= " + vice.getBannerId());
		}

		sql.append(" WHERE " + ViceField.scheduleId + "= " + vice.getScheduleId());
		try {
			getSqlSupport().excuteUpdate(sql.toString());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	@Override
	public boolean updatePOFollowerUser(String poFollowerUserName, Long poFollowerUserId, long poId) {
		logger.debug("updatePOFollowerUser: followerNamer=" + poFollowerUserName + ", followerId=" 
				+ poFollowerUserId + ", poId=" + poId);
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + tableName + " SET ");
		sql.append(ViceField.poFollowerUserName + "= '" +poFollowerUserName + "', ");
		sql.append(ViceField.poFollowerUserId + "= " + poFollowerUserId + " ");
		sql.append(" WHERE " + ViceField.scheduleId + "= " + poId);
		try {
			logger.debug("SQL: " + sql.toString());
			getSqlSupport().excuteUpdate(sql.toString());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
}
