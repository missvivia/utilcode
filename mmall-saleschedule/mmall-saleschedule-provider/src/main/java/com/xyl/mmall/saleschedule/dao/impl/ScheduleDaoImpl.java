package com.xyl.mmall.saleschedule.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.saleschedule.dao.ScheduleDao;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.dto.SchedulePageDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.enums.DBField.BannerField;
import com.xyl.mmall.saleschedule.enums.DBField.PageField;
import com.xyl.mmall.saleschedule.enums.DBField.ScheduleField;
import com.xyl.mmall.saleschedule.enums.DBField.ViceField;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.meta.SchedulePage;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * Schedule表操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class ScheduleDaoImpl extends ScheduleBaseDao<Schedule> implements ScheduleDao {

	private String sqlUpdateStatus = "UPDATE " + tableName + " SET " + ScheduleField.status + "=?, "
			+ ScheduleField.updateTimeForLogic + "=?, " + ScheduleField.scheduleUpdateDate + "=?, "
			+ ScheduleField.statusMsg + "=? WHERE " + ScheduleField.id + "=?";

	private String sqlUpdatePrdListStatus = "UPDATE " + tableName + " SET " + ScheduleField.flagAuditPrdList
			+ "=?  WHERE " + ScheduleField.id + "=?";

	private String sqlUpdateBannerStatus = "UPDATE Mmall_SaleSchedule_ScheduleVice SET " + ViceField.flagAuditBanner
			+ "=?  WHERE " + ViceField.scheduleId + "=? AND " + ViceField.bannerId + "=?";

	private String sqlUpdatePageStatus = "UPDATE Mmall_SaleSchedule_ScheduleVice SET " + ViceField.flagAuditPage + "=?  WHERE "
			+ ViceField.scheduleId + "=? AND " + ViceField.pageId + "=?";

	private String sqlUpdateDate = "UPDATE " + tableName + " SET " + ScheduleField.startTime + "=?, "
			+ ScheduleField.endTime + "=?, " + ScheduleField.adjustTimeDesc + "=?, " + ScheduleField.scheduleUpdateDate
			+ "=? WHERE " + ScheduleField.id + "=?";

	private static final String SELECT_SCHEDULE_BY_TIME_OF_VALID = "select t.* from Mmall_SaleSchedule_Schedule t where t.StartTime>= ? and t.StartTime<? and t.status=?";
	
	@Override
	public List<Schedule> queryScheduleListTemplate(StringBuilder sqlAll) {
		return queryObjects(sqlAll);
	}

	@Override
	public boolean saveSchedule(Schedule schedule) {
		logger.debug("saveSchedule: " + schedule);
		try {
			schedule = addObject(schedule);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean updateSchedule(Schedule schedule) {
		logger.debug("updateSchedule: " + schedule);
		try {
			String sql = getUpdatePOSql(schedule);
			getSqlSupport().excuteUpdate(sql);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateSchedulePageIdAndBannerId(Schedule schedule) {
		logger.debug("updateSchedulePageIdAndBannerId: " + schedule);
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + tableName + " SET ");
		if (schedule.getPageId() != 0 && schedule.getBannerId() != 0) {
			sql.append(ScheduleField.pageId + "=?, ").append(ScheduleField.bannerId + "=? ");
		}

		if (schedule.getPageId() != 0 && schedule.getBannerId() == 0) {
			sql.append(ScheduleField.pageId + "=? ");
		}

		if (schedule.getPageId() == 0 && schedule.getBannerId() != 0) {
			sql.append(ScheduleField.bannerId + "=? ");
		}

		sql.append(" WHERE " + ScheduleField.id + "=? ");
		try {
			logger.debug(sql.toString());
			return updateObjectByKey(schedule);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public List<Schedule> getSameDayPOList(Schedule comparePO) {
		String sql = "SELECT id, startTime, showOrder FROM Mmall_SaleSchedule_Schedule WHERE StartTime = " 
				+ comparePO.getStartTime(); 

		List<Schedule> poList = new ArrayList<Schedule>();
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql.toString());
			rs = dbr.getResultSet();
			while (rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setId(rs.getLong("id"));
				schedule.setStartTime(rs.getLong("startTime"));
				schedule.setShowOrder(rs.getInt("showOrder"));
				
				poList.add(schedule);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeDBQuitely(dbr, rs);
		}
		
		return poList;
	}

	@Override
	public boolean deleteScheduleById(long id) {
		logger.debug("deleteScheduleById: " + id);
		try {
			return deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean adjustScheduleDate(long id, long newStartTime, long newEndTime, String desc) {
		logger.debug("adjustScheduleDate: id=" + id + "; start=" + newStartTime + "; end=" + newEndTime + "; desc="
				+ desc);

		try {
			Object[] args = new Object[] { newStartTime, newEndTime, desc, System.currentTimeMillis(), id };
			getSqlSupport().excuteUpdate(sqlUpdateDate, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Cacheable(value = "poCache")
	@Override
	public Schedule getScheduleById(long id) {
		logger.debug("getScheduleById: " + id);
		try {
			return getObjectById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Cacheable(value = "poCache")
	@Override
	public ScheduleDTO getScheduleByScheduleId(long scheduleId) {
		System.out.println("Dao.getScheduleByScheduleId("+scheduleId+") called....'");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(" AND A." + ScheduleField.id + "=" + scheduleId);
		
		return getPODTOOnlyWithScheduleDTO(sql.toString());
	}

	// CMS 勇哥查询有效PO, 订单也在用
	@Override
	public POListDTO getScheduleList(ScheduleCommonParamDTO paramDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (paramDTO.supplierId != 0) {
			sql.append(" AND A." + ScheduleField.supplierId + "=" + paramDTO.supplierId);
		}
		if (paramDTO.startDate != 0 && paramDTO.endDate != 0) {
			sql.append(" AND A." + ScheduleField.startTime + "<=" + paramDTO.endDate + " AND A." + ScheduleField.endTime
					+ ">" + paramDTO.startDate);
		}
		sql.append(getInClause("A." + ScheduleField.id, paramDTO.poIdList));
		if (paramDTO.poStatusList != null && paramDTO.poStatusList.size() != 0) {
			sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));
		} else {
			sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());
		}
		sql.append(geneOrderBySql2("A.", paramDTO.orderByFlag));
		//sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	// CMS promotion在用
	@Override
	public POListDTO getScheduleListCommon(ScheduleCommonParamDTO paramDTO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (paramDTO.supplierId != 0) {
			sql.append(" AND A." + ScheduleField.supplierId + "=" + paramDTO.supplierId);
		}
		if (paramDTO.startDate != 0 && paramDTO.endDate != 0) {
			sql.append(" AND A." + ScheduleField.startTime + ">=" + paramDTO.startDate + " AND A."
					+ ScheduleField.startTime + "<" + paramDTO.endDate);
		}
		sql.append(getInClause("A." + ScheduleField.id, paramDTO.poIdList));
		sql.append(getInClause("A." + ScheduleField.brandId, paramDTO.brandIdList));
		sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());
		sql.append(" ORDER BY A." + ScheduleField.id);
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}
	
	// 批量获取POList
	@Override
	public POListDTO getScheduleListByPOIdList(List<Long> poIdList) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(getInClause("A." + ScheduleField.id, poIdList));
		
		// 分页会在内存里面进行
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), null);
	}

	// 首页程曦铭
	@Cacheable(value = "poCache")
	@Override
	public POListDTO getScheduleListFuture(long brandId, int dayAfter) {
		StringBuilder sql = new StringBuilder();
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleBanner B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + " = B." + BannerField.scheduleId);
		if (brandId != 0) {
			sql.append(" AND A." + ScheduleField.brandId + "=" + brandId);
		}
		long now = System.currentTimeMillis();
		long future = now + dayAfter * 24 * 3600 * 1000L;
		sql.append(" AND A." + ScheduleField.startTime + ">" + now);
		sql.append(" AND A." + ScheduleField.startTime + "<=" + future);
		sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());

		StringBuilder sqlSelectHeader = new StringBuilder();
		sqlSelectHeader.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
		.append(getSelectAllFieldsSql(ScheduleBanner.class, "B.", "b_")).append(" FROM ");
		String pageSql = genePageSql(0, 0);
		String orderBySql = geneOrderBySql("A." + ScheduleField.startTime);
		return getPOListResultForPOBanner(sqlSelectHeader.toString(), sql.toString(), orderBySql, pageSql, false, 0, 0);
	}

	// 主站程曦铭获取关注的PO列表
	@Cacheable(value = "poCache")
	@Override
	public POListDTO getScheduleList(long brandId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleBanner B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + " = B." + BannerField.scheduleId);
		if (brandId != 0) {
			sql.append(" AND A." + ScheduleField.brandId + "=" + brandId);
		}
		long now = System.currentTimeMillis();
		sql.append(" AND A." + ScheduleField.startTime + "<=" + now);
		sql.append(" AND A." + ScheduleField.endTime + ">=" + now);
		sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());

		StringBuilder sqlSelectHeader = new StringBuilder();
		sqlSelectHeader.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
		.append(getSelectAllFieldsSql(ScheduleBanner.class, "B.", "b_")).append(" FROM ");
		String pageSql = genePageSql(0, 0);
		String orderBySql = geneOrderBySql("A." + ScheduleField.startTime);
		POListDTO poList = getPOListResultForPOBanner(sqlSelectHeader.toString(), sql.toString(), orderBySql, pageSql, false, 0, 0);
		genSaleSiteListForPOList(poList);
		return poList;
	}

	// 楠哥购物车
	@Override
	public POListDTO batchGetScheduleListByIdList(List<Long> poIdList) {
		
		// optimization
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id, startTime, endTime, supplierId, saleSiteFlag, brandName, title ")
		.append(" FROM Mmall_SaleSchedule_Schedule WHERE 1=1 ");
		if (poIdList != null && poIdList.size() == 1) {
			// optimization
			sql.append(" AND " + ScheduleField.id + "=" + poIdList.get(0));
		} else {
			sql.append(getInClause(ScheduleField.id, poIdList));
		}
		
		logger.debug("SQL: " + sql.toString());
		POListDTO poList = new POListDTO();
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql.toString());
			rs = dbr.getResultSet();
			while (rs.next()) {
				ScheduleDTO scheduleDTO = new ScheduleDTO();

				Schedule schedule = new Schedule();
				schedule.setId(rs.getLong("id"));
				schedule.setStartTime(rs.getLong("startTime"));
				schedule.setEndTime(rs.getLong("endTime"));
				schedule.setSupplierId(rs.getLong("supplierId"));
				schedule.setSaleSiteFlag(rs.getLong("saleSiteFlag"));
				schedule.setBrandName(rs.getString("brandName"));
				schedule.setTitle(rs.getString("title"));
				
				scheduleDTO.setSchedule(schedule);
				scheduleDTO.setScheduleVice(null);

				PODTO poDTO = new PODTO();
				poDTO.setScheduleDTO(scheduleDTO);

				poList.getPoList().add(poDTO);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeDBQuitely(dbr, rs);
		}

		genSaleSiteListForPOList(poList);
		
		return poList;
	}

	// CMS陆谦调用
	@Override
	public POListDTO getScheduleList(long siteFlag, long supplierId, String brandName, long startDate, long endDate) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		
		if (siteFlag != 0 && siteFlag != -1) {
			sql.append(" AND A." + ScheduleField.saleSiteFlag + " & " + siteFlag + "=" + siteFlag);
			//sql.append(" AND C." + SiteRelaField.saleSiteId + "=" + saleAreaId);
		}
		if (supplierId != 0) {
			sql.append(" AND A." + ScheduleField.supplierId + "=" + supplierId);
		}

		// sql.append(" AND " + ScheduleField.status + inClause.toString());
		if (brandName != null && !"".equals(brandName.trim())) {
			sql.append(" AND A." + ScheduleField.brandName + " = '" + brandName + "'");
		}

		if (startDate != 0) {
			sql.append(" AND A." + ScheduleField.updateTimeForLogic + ">=" + startDate);
		}

		if (endDate != 0) {
			sql.append(" AND A." + ScheduleField.updateTimeForLogic + "<=" + endDate);
		}
		sql.append(" ORDER BY A." + ScheduleField.id + " ");

		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), null);
	}
	
	// 曾成元使用
	@Override
	public POListDTO getScheduleListByBrandNameOrSupplierAcct(ScheduleCommonParamDTO paramDTO, String supplierAcct,
			String brandName) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));
		//sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());
		
		if (supplierAcct != null && !"".equals(supplierAcct.trim())) {
			sql.append(" AND B." + ViceField.supplierAcct + "='" + supplierAcct.trim() + "'");
		}
		if (brandName != null && !"".equals(brandName.trim())) {
			sql.append(" AND (A." + ScheduleField.brandName + " = '" + brandName.trim() + "'");
			sql.append(" OR A." + ScheduleField.brandNameEn + " = '" + brandName.trim() + "') ");
		}
		
		sql.append(" ORDER BY A." + ScheduleField.createTimeForLogic + " DESC ");
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	// CMS 档期日历，档期审核
	@Override
	public POListDTO getScheduleList(ScheduleCommonParamDTO paramDTO, List<ScheduleState> statusList, boolean isCheck) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (paramDTO.supplierId != 0) {
			sql.append(" AND A." + ScheduleField.supplierId + "=" + paramDTO.supplierId);
		}
		sql.append(getInClause("A." + ScheduleField.status, statusList));
		if (paramDTO.startDate != 0) {
			if (isCheck) {
				sql.append(" AND A." + ScheduleField.updateTimeForLogic + ">=" + paramDTO.startDate);
			} else {
				sql.append(" AND A." + ScheduleField.startTime + ">=" + paramDTO.startDate);
			}
		}

		if (paramDTO.endDate != 0) {
			if (isCheck) {
				sql.append(" AND A." + ScheduleField.updateTimeForLogic + "<=" + paramDTO.endDate);
			} else {
				sql.append(" AND A." + ScheduleField.startTime + "<=" + paramDTO.endDate);
			}
		}
		sql.append(geneOrderBySql2("A.", paramDTO.orderByFlag));
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	// backend 获取banner列表
	@Override
	public POListDTO getScheduleBannerList(ScheduleCommonParamDTO paramDTO) {
		StringBuilder sql = new StringBuilder(512);
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleBanner B WHERE 1=1 ");
		sql.append(" AND A." + ScheduleField.id + " = B." + BannerField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (paramDTO.supplierId != 0) {
			SqlGenUtil.appendExtParamObject(sql, "A." + ScheduleField.supplierId, paramDTO.supplierId);
		}
		if (paramDTO.startDate != 0) {
			sql.append(" AND A." + ScheduleField.startTime + ">=" + paramDTO.startDate);
		}

		if (paramDTO.endDate != 0) {
			sql.append(" AND A." + ScheduleField.startTime + "<=" + paramDTO.endDate);
		}
		if (paramDTO.bannerOrPageStatusList != null && paramDTO.bannerOrPageStatusList.size() != 0) {
			int status = paramDTO.bannerOrPageStatusList.get(0).getIntValue();
			SqlGenUtil.appendExtParamObject(sql, "B." + BannerField.status, status);
		}
		sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));

		StringBuilder sqlSelectHeader = new StringBuilder();
		sqlSelectHeader.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
		.append(getSelectAllFieldsSql(ScheduleBanner.class, "B.", "b_")).append(" FROM ");
		String pageSql = genePageSql(paramDTO.curPage, paramDTO.pageSize);
		String orderBySql = geneOrderBySql2("A.", paramDTO.orderByFlag);// geneOrderBySql("A."
																		// +
																		// ScheduleField.id);

		return getPOListResultForPOBanner(sqlSelectHeader.toString(), sql.toString(), orderBySql, pageSql, true,
				paramDTO.curPage, paramDTO.pageSize);
	}

	@Override
	public POListDTO queryScheduleBannerListForPOPageTemplate(StringBuilder sqlAll) throws SQLException {
		POListDTO poList = new POListDTO();
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sqlAll.toString());
			rs = dbr.getResultSet();
			while (rs.next()) {
				Schedule schedule = (Schedule) getObjFromRS(rs, Schedule.class, null);
				//Schedule schedule = genScheduleObjFromRS(rs);
				ScheduleDTO scheduleDTO = new ScheduleDTO();
				scheduleDTO.setSchedule(schedule);

				ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
				if (rs.getLong("b_id") == 0) { // banner not exist
					bannerDTO.setBanner(null);
				} else { // banner exist
					ScheduleBanner banner = (ScheduleBanner) getObjFromRS(rs, ScheduleBanner.class, "b_");
					bannerDTO.setBanner(banner);
				}

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

	// CMS档期列表
	@Override
	public POListDTO getScheduleListForCMS(ScheduleCommonParamDTO paramDTO, int type, Object val) {
		
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (val != null && !"".equals(val.toString().trim())) {
			if (type == 1) {
				sql.append(" AND A." + ScheduleField.id + "= " + val);
			} else if (type == 2) {
				sql.append(" AND (A." + ScheduleField.brandName + " = '" + val.toString().trim() + "'");
				sql.append(" OR A." + ScheduleField.brandNameEn + "= '" + val.toString().trim() + "') ");
			} else if (type == 3) {
				if (paramDTO.supplierIdList != null && paramDTO.supplierIdList.size() == 1) {
					sql.append(" AND A." + ScheduleField.supplierId + "= " + paramDTO.supplierIdList.get(0));
				} else {
					sql.append(getInClause("A." + ScheduleField.supplierId, paramDTO.supplierIdList));
				}
			} else if (type == 4) {
				sql.append(" AND A." + ScheduleField.supplierId + "=" + val);
			}
		}

		if (paramDTO.poStatusList != null && paramDTO.poStatusList.size() > 0) {
			sql.append(" AND A." + ScheduleField.status + "= " + paramDTO.poStatusList.get(0).getIntValue());
		}
		//sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));
		sql.append(geneOrderBySql2("A.", paramDTO.orderByFlag));
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	// backend 添加商品的PO列表
	@Override
	public POListDTO getScheduleListForOMS(ScheduleCommonParamDTO paramDTO, long startTimeBegin, long startTimeEnd,
			long endTimeBegin, long endTimeEnd, long createTimeBegin, long createTimeEnd) {
		
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (paramDTO.supplierId != 0) {
			sql.append(" AND A." + ScheduleField.supplierId + "=" + paramDTO.supplierId);
		}
		if (paramDTO.poId != 0) {
			sql.append(" AND A." + ScheduleField.id + " = " + paramDTO.poId);
		}
		if (paramDTO.bannerOrPageStatusList != null && paramDTO.bannerOrPageStatusList.size() != 0) {
			int status = paramDTO.bannerOrPageStatusList.get(0).getIntValue();
			if (status == 1) {
				sql.append(" AND (A." + ScheduleField.flagAuditPrdList + "=0 OR A." + ScheduleField.flagAuditPrdList
						+ "=1) ");
			} else {
				sql.append(" AND A." + ScheduleField.flagAuditPrdList + "=" + status);
			}
		}
		sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));
		if (startTimeBegin != 0) {
			sql.append(" AND A." + ScheduleField.startTime + ">=" + startTimeBegin);
		}
		if (startTimeEnd != 0) {
			sql.append(" AND A." + ScheduleField.startTime + "<=" + startTimeEnd);
		}

		if (endTimeBegin != 0) {
			sql.append(" AND A." + ScheduleField.endTime + ">=" + endTimeBegin);
		}
		if (endTimeEnd != 0) {
			sql.append(" AND A." + ScheduleField.endTime + "<=" + endTimeEnd);
		}

		if (createTimeBegin != 0) {
			sql.append(" AND A." + ScheduleField.createTimeForLogic + ">=" + createTimeBegin);
		}
		if (createTimeEnd != 0) {
			sql.append(" AND A." + ScheduleField.createTimeForLogic + "<=" + createTimeEnd);
		}
		sql.append(geneOrderBySql2("A.", paramDTO.orderByFlag));
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	// backend 查询品购页列表
	@Override
	public POListDTO getScheduleListForPOPages(ScheduleCommonParamDTO paramDTO, int type, Object key) {
		StringBuilder sql = new StringBuilder();
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_SchedulePage B WHERE 1=1 ");
		sql.append(" AND A." + ScheduleField.id + " = B." + PageField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		if (paramDTO.supplierId != 0) {
			sql.append(" AND A." + ScheduleField.supplierId + "=" + paramDTO.supplierId);
		}
		if (key != null && !"".equals(key.toString())) {
			if (type == 0) {
				sql.append(" AND A." + ScheduleField.title + " = '" + key + "'");
			} else if (type == 1) {
				sql.append(" AND A." + ScheduleField.id + " = " + key);
			}
		}
		if (paramDTO.bannerOrPageStatusList != null && paramDTO.bannerOrPageStatusList.size() != 0) {
			int status = paramDTO.bannerOrPageStatusList.get(0).getIntValue();
			sql.append(" AND B." + PageField.status + "=" + status);
		}
		sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));

		String sqlSelectHeader = "SELECT B.id PAGEID, B.status PAGESTATUS, B.statusMsg PAGESTATUSMSG,  A.* FROM ";
		String pageSql = genePageSql(paramDTO.curPage, paramDTO.pageSize);
		String orderBySql = geneOrderBySql2("A.", paramDTO.orderByFlag);

		return getPOListResultForPOPage(sqlSelectHeader, sql.toString(), orderBySql, pageSql, true, paramDTO.curPage,
				paramDTO.pageSize);
	}

	@Override
	public POListDTO querySchedulePageListForPOPageTemplate(StringBuilder sqlAll) throws SQLException {
		POListDTO poList = new POListDTO();

		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sqlAll.toString());
			rs = dbr.getResultSet();

			while (rs.next()) {
				Schedule schedule = (Schedule) getObjFromRS(rs, Schedule.class, null);
				ScheduleDTO scheduleDTO = new ScheduleDTO();
				scheduleDTO.setSchedule(schedule);

				SchedulePageDTO pageDTO = new SchedulePageDTO();
				if (rs.getInt("PAGESTATUS") == 0) { // page not exist
					schedule.setStatus(ScheduleState.DRAFT);
					pageDTO.setPage(null);
				} else { // page exist
					// for web display, we use page's status as PO's status
					// schedule.setStatus(ScheduleState.NULL.genEnumByIntValue(rs.getInt("PAGESTATUS")));
					SchedulePage page = new SchedulePage();
					page.setId(rs.getInt("PAGEID"));
					page.setStatus(CheckState.NULL.genEnumByIntValue(rs.getInt("PAGESTATUS")));
					page.setStatusMsg(rs.getString("PAGESTATUSMSG"));
					pageDTO.setPage(page);
				}

				PODTO poDTO = new PODTO();
				poDTO.setScheduleDTO(scheduleDTO);
				poDTO.setPageDTO(pageDTO);

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
	public boolean updateStatus(long id, ScheduleState status, String desc, Long userId) {
		logger.debug("_updateStatus: " + id + "; " + status + "; " + desc);
		long now = System.currentTimeMillis();
		Object[] args = new Object[] { status.getIntValue(), now, now, desc, id };
		try {
			getSqlSupport().excuteUpdate(sqlUpdateStatus, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updatePrdListAuditStatus(long id, int status) {
		logger.debug("updatePrdListAuditStatus: " + id + "; " + status);
		Object[] args = new Object[] { status, id };
		try {
			getSqlSupport().excuteUpdate(sqlUpdatePrdListStatus, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateBannerAuditStatus(long poId, long bannerId, int status) {
		logger.debug("updateBannerAuditStatus: poId=" + poId + "; bannerId=" + bannerId + "; status=" + status);
		Object[] args = new Object[] { status, poId, bannerId };
		try {
			getSqlSupport().excuteUpdate(sqlUpdateBannerStatus, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updatePageAuditStatus(long poId, long pageId, int status) {
		logger.debug("updatePageAuditStatus: poid=" + poId + "; pageId=" + pageId + "; status=" + status);
		Object[] args = new Object[] { status, poId, pageId };
		try {
			getSqlSupport().excuteUpdate(sqlUpdatePageStatus, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	// 主站
	@Override
	public POListDTO getScheduleListForChl(String sqlPart, long saleSiteCode, long startId, int curPage, int pageSize) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B, Mmall_SaleSchedule_ScheduleBanner C WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(" AND A." + ScheduleField.id + "=C." + BannerField.scheduleId);
		
		if (startId != 0) {
			sql.append(" AND A." + ScheduleField.id + ">" + startId);
		}
		if (saleSiteCode != 0 && saleSiteCode != -1) {
			sql.append(" AND A." + ScheduleField.saleSiteFlag + "&" + saleSiteCode + "=" + saleSiteCode);
		}
		long now = System.currentTimeMillis();
		sql.append(" AND A." + ScheduleField.startTime + "<=" + now + " AND A." + ScheduleField.endTime + ">=" + now);
		sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());
		sql.append(sqlPart);

		String selFieldsSql = getSelectAllFieldsSql(Schedule.class, "A.", null) + ", "
				+ getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_") + ", "
				+ getSelectAllFieldsSql(ScheduleBanner.class, "C.", "c_");
		String sqlSelectHeader = "SELECT " + selFieldsSql + " FROM ";
		String pageSql = genePageSql(curPage, pageSize);
		String orderBySql = " ORDER BY " + ScheduleField.showOrder;//geneOrderBySql("A." + ScheduleField.id);

		return getPOListResultForChl(sqlSelectHeader, sql.toString(), orderBySql, pageSql, true, curPage, pageSize);
	}

	// 主站
	@Override
	public POListDTO getScheduleListForFuture(String sqlPart, long saleSiteCode, int daysAfter, int retSize) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleVice B, Mmall_SaleSchedule_ScheduleBanner C WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(" AND A." + ScheduleField.id + "=C." + BannerField.scheduleId);
		sql.append(" AND A." + ScheduleField.saleSiteFlag + "&" + saleSiteCode + "=" + saleSiteCode);

		Calendar c1 = getFirstTime();
		c1.add(Calendar.DAY_OF_MONTH, daysAfter);
		Calendar c2 = getLastTime();
		c2.add(Calendar.DAY_OF_MONTH, daysAfter);
		sql.append(" AND A." + ScheduleField.startTime + ">=" + c1.getTimeInMillis() + " AND A."
				+ ScheduleField.startTime + "<=" + c2.getTimeInMillis());
		sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());
		sql.append(sqlPart);

		String orderBySql = geneOrderBySql("A." + ScheduleField.startTime);
		String pageSql = geneTopSql(retSize);

		String selFieldsSql = getSelectAllFieldsSql(Schedule.class, "A.", null) + ", "
				+ getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_") + ", "
				+ getSelectAllFieldsSql(ScheduleBanner.class, "C.", "c_");
		String sqlSelectHeader = "SELECT " + selFieldsSql + " FROM ";
		return getPOListResultForChl(sqlSelectHeader, sql.toString(), orderBySql, pageSql, false, 0, 0);
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
				// Schedule schedule = genScheduleObjFromRS(rs);

				ScheduleDTO scheduleDTO = new ScheduleDTO();
				scheduleDTO.setSchedule(schedule);
				scheduleDTO.setScheduleVice(vice);

				ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
				// ScheduleBanner banner = new ScheduleBanner();
				// banner.setHomeBannerImgUrl(rs.getString(BannerField.homeBannerImgUrl));
				// banner.setPreBannerImgUrl(rs.getString(BannerField.preBannerImgUrl));
				// banner.setScheduleId(schedule.getId());
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
	public POListDTO getScheduleListByBrandIdList(ScheduleCommonParamDTO paramDTO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		sql.append(getInClause("A." + ScheduleField.brandId, paramDTO.brandIdList));
		sql.append(" AND A." + ScheduleField.status + "=" + ScheduleState.BACKEND_PASSED.getIntValue());
		sql.append(" ORDER BY A." + ScheduleField.id + " ");
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	// CMS和backend首页
	@Override
	public POListDTO getScheduleListByStartEndTime(ScheduleCommonParamDTO paramDTO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
//		if (paramDTO.curSupplierAreaId != 0) {
//			sql.append(" AND A." + ScheduleField.curSupplierAreaId + "=" + paramDTO.curSupplierAreaId);
//		}
		if (paramDTO.supplierId != 0) {
			sql.append(" AND A." + ScheduleField.supplierId + "=" + paramDTO.supplierId);
		}
		sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));
		if (paramDTO.startDate != 0) {
			if (paramDTO.createWhereFlag) {
				sql.append(" AND A." + ScheduleField.createTimeForLogic + ">=" + paramDTO.startDate);
			} else {
				sql.append(" AND A." + ScheduleField.startTime + ">=" + paramDTO.startDate);
			}
		}
		if (paramDTO.endDate != 0) {
			if (paramDTO.createWhereFlag) {
				sql.append(" AND A." + ScheduleField.createTimeForLogic + "<=" + paramDTO.endDate);
			} else {
				sql.append(" AND A." + ScheduleField.startTime + "<=" + paramDTO.endDate);
			}
		}
		sql.append(" ORDER BY A." + ScheduleField.startTime + " ");
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	// CMS状态页面
	@Override
	public POListDTO getScheduleListByStartEndTimeWithType(ScheduleCommonParamDTO paramDTO, int type) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (paramDTO.supplierId != 0) {
			sql.append(" AND A." + ScheduleField.supplierId + "=" + paramDTO.supplierId);
		}
		if (paramDTO.startDate != 0) {
			sql.append(" AND A." + ScheduleField.startTime + ">=" + paramDTO.startDate);
		}
		if (paramDTO.endDate != 0) {
			sql.append(" AND A." + ScheduleField.startTime + "<=" + paramDTO.endDate);
		}

		sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));

		if (type == 3) {
			sql.append(" AND B." + ScheduleField.flagAuditPage + " < 1 ");
		} else if (type == 4) {
			sql.append(" AND B." + ScheduleField.flagAuditBanner + " < 1 ");
		}
		
		sql.append(" ORDER BY A." + ScheduleField.startTime + " ");
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	public List<Schedule> getScheduleListByTime(long saleAreaId, long startTime, long endTime) {
		String sql = SELECT_SCHEDULE_BY_TIME_OF_VALID;
//		if (saleAreaId > 0) {
//			sql += " and t.saleAreaId=" + saleAreaId;
//		}
		return super.queryObjects(sql, startTime, endTime, ScheduleState.BACKEND_PASSED.getIntValue());
	}
	
	@Override
	public Map<String, Object> getProductById(long productId)  {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id, brandId, productName, marketPrice, salePrice FROM Mmall_ItemCenter_PoProduct WHERE id=" + productId);
		
		Map<String, Object> result = new ConcurrentHashMap<String, Object>();
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql.toString());
			rs = dbr.getResultSet();
			if (rs.next()) {
				result.put("id", rs.getLong("id"));
				result.put("brandId", rs.getLong("brandId"));
				result.put("productName", rs.getString("productName"));
				result.put("marketPrice", rs.getBigDecimal("marketPrice"));
				result.put("salePrice", rs.getBigDecimal("salePrice"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeDBQuitely(dbr, rs);
		}
		
		return result;
	}
	
	@Override
	public POListDTO getScheduleListForPrdOrListAudit(ScheduleCommonParamDTO paramDTO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleVice.class, "B.", "b_")).append(" FROM ");

		sql.append(" Mmall_SaleSchedule_Schedule A,  Mmall_SaleSchedule_ScheduleVice B WHERE 1=1");
		sql.append(" AND A." + ScheduleField.id + "=B." + ViceField.scheduleId);
		
		if (paramDTO.poId != 0) {
			sql.append(" AND A." + ScheduleField.id + "=" + paramDTO.poId);
		}
		sql.append(getInClause("A." + ScheduleField.status, paramDTO.poStatusList));
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		sql.append(geneOrderBySql2("A.", paramDTO.orderByFlag));
		
		return getPOListDTOOnlyWithScheduleDTO(sql.toString(), paramDTO);
	}

	
	public boolean updatePOSaleSiteFlag(long poId, long saleSiteFlag) {
		String sql = "UPDATE Mmall_SaleSchedule_Schedule set saleSiteFlag=" + saleSiteFlag + " where id=" + poId;
		try {
			getSqlSupport().excuteUpdate(sql);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
}
