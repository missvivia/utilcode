package com.xyl.mmall.saleschedule.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.xyl.mmall.saleschedule.dao.SchedulePageDao;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.dto.SchedulePageDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.DBField.PageField;
import com.xyl.mmall.saleschedule.enums.DBField.ScheduleField;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.SchedulePage;

/**
 * Schedule表操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class SchedulePageDaoImpl extends ScheduleBaseDao<SchedulePage> implements SchedulePageDao {

	private String tableName = this.getTableName();

	//
	// private String sqlUpdateStatus = "UPDATE " + tableName + " SET " +
	// PageField.status + "=?, "
	// + PageField.statusUpdateDate + "=?, " + PageField.statusMsg +
	// "=? WHERE 1=1 ";

	private String sqlUpdateOrderType = "UPDATE " + tableName + " SET " + PageField.prdListOrderType + "=? WHERE "
			+ PageField.scheduleId + "=?";

	private String sqlUpdate = "UPDATE " + tableName + " SET " + PageField.allListPartOthers + "=?, "
			+ PageField.allListPartVisiable + "=?, " + PageField.bgImgId + "=?, " + PageField.bgSetting + "=?, "
			+ PageField.headerImgId + "=?, " + PageField.headerSetting + "=?, " + PageField.mapPartOthers + "=?, "
			+ PageField.mapPartVisiable + "=?, " + PageField.udImgIds + "=?, " + PageField.udProductIds + "=?, "
			+ PageField.udSetting + "=?, " + PageField.status + "=?, " + PageField.statusUpdateDate + "=? WHERE "
			+ PageField.scheduleId + "=? AND " + PageField.id + "=?";

	private String sqlUpdatePOFields = "UPDATE " + tableName + " SET " + PageField.saleAreaId + "=?, "
			+ PageField.supplierId + "=?, " + PageField.supplierName + "=?, " + PageField.brandName + "=?, "
			+ PageField.brandNameEn + "=?, " + PageField.brandId + "=?, " + PageField.title + "=? WHERE "
			+ PageField.scheduleId + "=?";

	@Override
	public List<SchedulePage> querySchedulePageListTemplate(StringBuilder sqlAll) {
		return queryObjects(sqlAll);
	}

	@Override
	public boolean saveSchedulePage(SchedulePage page) {
		logger.debug("saveSchedulePage: " + page);
		try {
			page = addObject(page);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean updateSchedulePage(SchedulePage page) {
		logger.debug("updateSchedulePage: " + page);
		try {
			Object[] args = { page.getAllListPartOthers(), page.isAllListPartVisiable() ? 1 : 0, page.getBgImgId(),
					page.getBgSetting(), page.getHeaderImgId(), page.getHeaderSetting(), page.getMapPartOthers(),
					page.isMapPartVisiable() ? 1 : 0, page.getUdImgIds(), page.getUdProductIds(), page.getUdSetting(),
					page.getStatus().getIntValue(), page.getStatusUpdateDate(), page.getScheduleId(), page.getId() };
			getSqlSupport().excuteUpdate(sqlUpdate, args);
			return true;
			// return affectedRows == 1;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateSchedulePagePOField(SchedulePage page) {
		logger.debug("updateSchedulePagePOField: " + page);
		try {
			Object[] args = { page.getSaleAreaId(), page.getSupplierId(), page.getSupplierName(), page.getBrandName(),
					page.getBrandNameEn(), page.getBrandId(), page.getTitle(), page.getScheduleId() };
			getSqlSupport().excuteUpdate(sqlUpdatePOFields, args);
			return true;
			// return affectedRows == 1;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteSchedulePageById(long id) {
		logger.debug("deleteSchedulePageById: " + id);
		try {
			return deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public SchedulePage getSchedulePageById(long id, Long userId) {
		logger.debug("getSchedulePageById: " + id);
		try {
			return getObjectById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public PODTO getPOByPageIdOrScheduleId(long pageId, long scheduleId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT A."
				+ ScheduleField.id
				+ " PO_ID, A."
				+ ScheduleField.startTime
				+ " PO_STARTTIME, A."
				+ ScheduleField.pageTitle
				+ " PO_PAGETITLE, A."
				+ ScheduleField.saleSiteFlag
				+ " PO_SALESITEFLAG, A."
				+ ScheduleField.endTime
				+ " PO_ENDTIME, A."
				+ ScheduleField.brandLogo
				+ " PO_BRANDLOGO, B.* FROM Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_SchedulePage B WHERE A.id = B.scheduleId");
		if (pageId != 0) {
			sql.append(" AND B." + PageField.id + "=" + pageId);
		}

		if (scheduleId != 0) {
			sql.append(" AND B." + PageField.scheduleId + "=" + scheduleId);
		}

		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql.toString());
			rs = dbr.getResultSet();
			PODTO poDTO = new PODTO();
			if (rs.next()) {
				// page
				SchedulePage page = genSchedulePageObjFromRS(rs);

				// schedule
				Schedule schedule = new Schedule();
				schedule.setId(rs.getLong("PO_ID"));
				schedule.setStartTime(rs.getLong("PO_STARTTIME"));
				schedule.setEndTime(rs.getLong("PO_ENDTIME"));
				schedule.setBrandLogo(rs.getString("PO_BRANDLOGO"));
				schedule.setSaleSiteFlag(rs.getLong("PO_SALESITEFLAG"));
				schedule.setPageTitle(rs.getString("PO_PAGETITLE"));

				SchedulePageDTO pageDTO = new SchedulePageDTO();
				pageDTO.setPage(page);

				ScheduleDTO scheduleDTO = new ScheduleDTO();
				scheduleDTO.setSchedule(schedule);

				poDTO.setPageDTO(pageDTO);
				poDTO.setScheduleDTO(scheduleDTO);
			}
			return poDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new PODTO();
		} finally {
			closeDBQuitely(dbr, rs);
		}
	}

	@Override
	public SchedulePage getSchedulePageByScheduleId(long scheduleId, Long userId) {
		logger.debug("getSchedulePageByScheduleId: " + scheduleId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND " + PageField.scheduleId + "=" + scheduleId);
		try {
			return queryObject(sql.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public POListDTO getSchedulePageList(ScheduleCommonParamDTO paramDTO, long pageId, String brandName) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(SchedulePage.class, "B.", "b_")).append(" FROM ");
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_SchedulePage B WHERE 1=1 ");
		sql.append(" AND A." + ScheduleField.id + "=B." + PageField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (pageId != 0) {
			sql.append(" AND B." + PageField.id + "= " + pageId);
		}
		if (paramDTO.poId != 0) {
			sql.append(" AND B." + PageField.scheduleId + "=" + paramDTO.poId);
		}

		sql.append(getInClause("B." + PageField.supplierId, paramDTO.supplierIdList));
		if (brandName != null && !"".equals(brandName.trim())) {
			sql.append(" AND (B." + PageField.brandName + " = '" + brandName.trim() + "'");
			sql.append(" OR B." + PageField.brandNameEn + "= '" + brandName.trim() + "') ");
		}

		if (paramDTO.startDate != 0) {
			sql.append(" AND B." + PageField.statusUpdateDate + ">=" + paramDTO.startDate);
		}
		if (paramDTO.endDate != 0) {
			sql.append(" AND B." + PageField.statusUpdateDate + "<=" + paramDTO.endDate);
		}

		sql.append(getInClause("B." + PageField.status, paramDTO.bannerOrPageStatusList));
		sql.append(" ORDER BY B." + PageField.statusUpdateDate + " DESC");
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		// String sqlSelectHeader = "SELECT * FROM ";
		// String pageSql = genePageSql(paramDTO.curPage, paramDTO.pageSize);
		// String orderBySql = geneOrderBySql(PageField.statusUpdateDate) +
		// " DESC ";
		//
		// return getPOListResultForPage(sqlSelectHeader, sql.toString(),
		// orderBySql, pageSql, true, paramDTO.curPage, paramDTO.pageSize);

		return getPageListDTO(sql.toString(), paramDTO);
	}

	protected POListDTO getPageListDTO(String sql, ScheduleCommonParamDTO paramDTO) {
		POListDTO poList = new POListDTO();
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql);
			rs = dbr.getResultSet();
			while (rs.next()) {
				Schedule schedule = (Schedule) getObjFromRS(rs, Schedule.class, null);
				SchedulePage page = (SchedulePage) getObjFromRS(rs, SchedulePage.class, "b_");
				
				ScheduleDTO scheduleDTO = new ScheduleDTO();
				scheduleDTO.setSchedule(schedule);

				SchedulePageDTO pageDTO = new SchedulePageDTO();
				pageDTO.setPage(page);

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
		
		genSaleSiteListForPOList(poList);
		
		try {
			if (paramDTO != null && sql.indexOf("limit") != -1) {
				int total = getCnt(getCntSql(sql));
				boolean hasNext = hasNext(total, paramDTO.curPage, paramDTO.pageSize);
				poList.setTotal(total);
				poList.setHasNext(hasNext);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		return poList;
	}

	@Override
	public List<SchedulePage> getSchedulePageList(List<Long> poIdList) {
		logger.debug("getSchedulePageList: " + poIdList);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(getInClause(PageField.scheduleId, poIdList));

		try {
			return queryObjects(sql.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<SchedulePage>();
		}

	}

	@Override
	public boolean updateStatus(long id, CheckState status, String desc, Long poId) {
		logger.debug("updateStatus: " + id + "; " + status + "; " + desc + "; " + poId);
		Object[] args = null;
		StringBuilder sql = new StringBuilder();
		if (poId != null && poId != 0) {
			sql.append("UPDATE " + tableName + " SET " + PageField.status + "=?, " + PageField.statusUpdateDate
					+ "=?, " + PageField.statusMsg + "=? WHERE 1=1 " + " AND " + PageField.scheduleId + "=? " + " AND "
					+ PageField.id + "=?");
			args = new Object[] { status.getIntValue(), System.currentTimeMillis(), desc, poId, id };
		} else {
			sql.append("UPDATE " + tableName + " SET " + PageField.status + "=?, " + PageField.statusUpdateDate
					+ "=?, " + PageField.statusMsg + "=? WHERE 1=1 " + " AND " + PageField.id + "=?");
			args = new Object[] { status.getIntValue(), System.currentTimeMillis(), desc, id };
		}

		try {
			getSqlSupport().excuteUpdate(sql.toString(), args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

	}

	@Override
	public boolean updatePOPagePrdListOrderType(long poId, int type) {
		logger.debug("updatePOPagePrdListOrderType: " + poId + "; " + type);
		Object[] args = new Object[] { type, poId };
		try {
			getSqlSupport().excuteUpdate(sqlUpdateOrderType, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
}
