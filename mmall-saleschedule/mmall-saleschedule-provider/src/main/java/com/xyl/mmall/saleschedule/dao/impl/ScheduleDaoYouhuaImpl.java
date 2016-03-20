package com.xyl.mmall.saleschedule.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.xyl.mmall.saleschedule.dao.ScheduleDaoYouhua;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.enums.DBField.BannerField;
import com.xyl.mmall.saleschedule.enums.DBField.ScheduleField;
import com.xyl.mmall.saleschedule.enums.DBField.ViceField;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class ScheduleDaoYouhuaImpl extends ScheduleBaseDao<Schedule> implements ScheduleDaoYouhua {

	private String sqlUpdatePrdzlStatus = "UPDATE Mmall_SaleSchedule_ScheduleVice SET " + ViceField.flagAuditPrdzl
			+ "=?  WHERE " + ViceField.scheduleId + "=?";
	
	private String sqlUpdatePrdqdStatus = "UPDATE Mmall_SaleSchedule_ScheduleVice SET " + ViceField.flagAuditPrdqd
			+ "=?  WHERE " + ViceField.scheduleId + "=?";
	
	@Override
	public ScheduleDTO getScheduleByScheduleIdForCMSWithNoCache(long scheduleId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(" AND A." + ScheduleField.id + "=" + scheduleId);

		return getPODTOOnlyWithScheduleDTO(sql.toString());
	}

	@Cacheable(value = "poCache")
	@Override
	public Schedule getScheduleByIdWithLimitedFields(long id) {

		// optimization
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id, startTime, endTime, supplierId, saleSiteFlag, brandName, title ")
				.append(" FROM Mmall_SaleSchedule_Schedule WHERE 1=1 ").append(" AND " + ScheduleField.id + "=" + id);

		logger.debug("SQL for getScheduleByIdWithLimitedFields: " + sql.toString());
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql.toString());
			rs = dbr.getResultSet();
			if (rs.next()) {

				Schedule schedule = new Schedule();
				schedule.setId(rs.getLong("id"));
				schedule.setStartTime(rs.getLong("startTime"));
				schedule.setEndTime(rs.getLong("endTime"));
				schedule.setSupplierId(rs.getLong("supplierId"));
				schedule.setSaleSiteFlag(rs.getLong("saleSiteFlag"));
				schedule.setBrandName(rs.getString("brandName"));
				schedule.setTitle(rs.getString("title"));

				return schedule;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeDBQuitely(dbr, rs);
		}

		return null;
	}

	@Override
	public POListDTO getScheduleListForChl(String sqlPart, long saleSiteCode, long curDate) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B, Mmall_SaleSchedule_ScheduleBanner C WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(" AND A." + ScheduleField.id + "=C." + BannerField.scheduleId);
		
		if (saleSiteCode != 0 && saleSiteCode != -1) {
			sql.append(" AND A." + ScheduleField.saleSiteFlag + "&" + saleSiteCode + "=" + saleSiteCode);
		}
		sql.append(" AND A." + ScheduleField.startTime + "<=" + curDate + " AND A." + ScheduleField.endTime + ">=" + curDate);
		sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());
		sql.append(sqlPart);

		String selFieldsSql = getSelectAllFieldsSql(Schedule.class, "A.", null) + ", "
				+ getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_") + ", "
				+ getSelectAllFieldsSql(ScheduleBanner.class, "C.", "c_");
		String sqlSelectHeader = "SELECT " + selFieldsSql + " FROM ";
		String orderBySql = " ORDER BY " + ScheduleField.showOrder;

		return getPOListResultForChl(sqlSelectHeader, sql.toString(), orderBySql, " ", true, 0, 0);
	}

	// 主站
	@Override
	public POListDTO getScheduleListForFuture(String sqlPart, long saleSiteCode, long startTime, long endTime) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleVice B, Mmall_SaleSchedule_ScheduleBanner C WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(" AND A." + ScheduleField.id + "=C." + BannerField.scheduleId);
		sql.append(" AND A." + ScheduleField.saleSiteFlag + "&" + saleSiteCode + "=" + saleSiteCode);

		sql.append(" AND A." + ScheduleField.startTime + ">=" + startTime + " AND A."
				+ ScheduleField.startTime + "<=" + endTime);
		sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());
		sql.append(sqlPart);

		String orderBySql = geneOrderBySql("A." + ScheduleField.startTime);

		String selFieldsSql = getSelectAllFieldsSql(Schedule.class, "A.", null) + ", "
				+ getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_") + ", "
				+ getSelectAllFieldsSql(ScheduleBanner.class, "C.", "c_");
		String sqlSelectHeader = "SELECT " + selFieldsSql + " FROM ";
		return getPOListResultForChl(sqlSelectHeader, sql.toString(), orderBySql, " ", false, 0, 0);
	}
	
	@Override
	public POListDTO querySchedulePageListForChlTemplate(StringBuilder sqlAll) throws SQLException {
		POListDTO poList = new POListDTO();

		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sqlAll.toString());
			rs = dbr.getResultSet();
			while (rs.next()) {
				Schedule schedule = (Schedule) getObjFromRS(rs, Schedule.class, null);
				ScheduleVice vice = (ScheduleVice) getObjFromRS(rs, ScheduleVice.class, "b_");
				ScheduleBanner banner = (ScheduleBanner) getObjFromRS(rs, ScheduleBanner.class, "c_");

				ScheduleDTO scheduleDTO = new ScheduleDTO();
				scheduleDTO.setSchedule(schedule);
				scheduleDTO.setScheduleVice(vice);

				ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
				bannerDTO.setBanner(banner);

				PODTO poDTO = new PODTO();
				poDTO.setScheduleDTO(scheduleDTO);
				poDTO.setBannerDTO(bannerDTO);

				poList.getPoList().add(poDTO);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeDBQuitely(dbr, rs);
		}

		return poList;
	}
	
	@Override
	public boolean updatePrdzlStatus(long id, int status) {
		logger.debug("updatePrdqdStatus({}, {})", id, status);
		return _updateStatus(sqlUpdatePrdzlStatus, new Object[] { status, id });
	}
	
	@Override
	public boolean updatePrdqdStatus(long id, int status) {
		logger.debug("updatePrdqdStatus({}, {})", id, status);
		return _updateStatus(sqlUpdatePrdqdStatus, new Object[] { status, id });
	}
}
