package com.xyl.mmall.ip.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.ip.dao.UserFeedbackDao;
import com.xyl.mmall.ip.meta.UserFeedback;

@Repository
public class UserFeedbackDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<UserFeedback> 
	implements UserFeedbackDao {

	@Override
	public boolean addNewFeedback(UserFeedback feedback) {
		if (feedback != null) {
			feedback.setId(-1);
			feedback.setSubmitTime(System.currentTimeMillis());
		} else {
			return false;
		}
		if (addObject(feedback) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<UserFeedback> getFeedbackList(long startTime, long endTime,
			long areaId, String system, String version, String key, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (startTime > 0) {
			sql.append(" AND submitTime >= ").append(startTime);
		}
		if (endTime > 0) {
			sql.append(" AND submitTime < ").append(endTime + 24 * 60 * 60 * 1000);
		}
		if (areaId > 0) {
			sql.append(" AND areaId = ").append(areaId);
		}
		if (system != null && system.trim().length() != 0) {
			system = system.trim().toUpperCase();
			sql.append(" AND UPPER(system) = '").append(system).append("'");
		}
		if (version != null && version.trim().length() != 0) {
			version = version.trim();
			sql.append(" AND version = '").append(version).append("'");
		}
		if (key != null && key.trim().length() != 0) {
			key = key.trim();
			sql.append(" AND feedBackContent = '").append(key).append("'");
		}
		return getListByDDBParam(sql.toString(), param);
	}

	@Override
	public List<String> getAllSystems() {
		StringBuilder sql = new StringBuilder(256);
		List<String> list = new ArrayList<>();
		sql.append("SELECT DISTINCT system FROM Mmall_IP_UserFeedback GROUP BY system");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				String sys = rs.getString("system");
				list.add(sys);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return list;
	}

	@Override
	public List<String> getAllVersions() {
		StringBuilder sql = new StringBuilder(256);
		List<String> list = new ArrayList<>();
		sql.append("SELECT DISTINCT version FROM Mmall_IP_UserFeedback GROUP BY version");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				String ver = rs.getString("version");
				list.add(ver);
			}
		} catch (SQLException e) {
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return list;
	}

}
