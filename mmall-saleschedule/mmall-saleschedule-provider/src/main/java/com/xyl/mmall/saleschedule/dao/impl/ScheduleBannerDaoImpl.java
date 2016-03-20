package com.xyl.mmall.saleschedule.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.saleschedule.dao.ScheduleBannerDao;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.DBField.BannerField;
import com.xyl.mmall.saleschedule.enums.DBField.ScheduleField;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;

/**
 * ScheduleBanner表操作DAO。
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class ScheduleBannerDaoImpl extends ScheduleBaseDao<ScheduleBanner> implements ScheduleBannerDao {

	private String sqlUpdateStatus = "UPDATE " + tableName + " SET " + BannerField.status + "=?, "
			+ BannerField.statusUpdateDate + "=?, " + BannerField.updateDate + "=?, " + BannerField.statusMsg + "=?, "
			+ BannerField.auditUserId + "=?, " + BannerField.auditUserName + "=? WHERE " + BannerField.scheduleId
			+ "=? AND " + BannerField.id + "=?";

	private String sqlUpdatePOFields = "UPDATE " + tableName + " SET " + BannerField.saleAreaId + "=?, "
			+ BannerField.supplierId + "=?, " + BannerField.supplierName + "=?, " + BannerField.brandName + "=?, "
			+ BannerField.brandNameEn + "=?, " + BannerField.brandId + "=?, " + BannerField.updateDate + "=? WHERE "
			+ BannerField.scheduleId + "=?";

	@Override
	public List<ScheduleBanner> queryScheduleBannerListTemplate(StringBuilder sqlAll) {
		return queryObjects(sqlAll);
	}

	@Override
	public boolean saveShceduleBannerList(List<ScheduleBanner> bannerList) {
		logger.debug("saveShceduleBannerList: " + bannerList);
		try {
			return addObjects(bannerList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean saveScheduleBanner(ScheduleBanner banner) {
		logger.debug("saveScheduleBanner: " + banner);
		try {
			addObject(banner);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateScheduleBanner(ScheduleBanner banner) {
		logger.debug("updateScheduleBanner: " + banner);
		try {
			String sql = getUpdateBannerSql(banner);
			getSqlSupport().excuteUpdate(sql);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateScheduleBannerPOField(ScheduleBanner banner) {
		logger.debug("updateScheduleBannerPOField: " + banner);
		try {
			Object[] args = { banner.getSaleAreaId(), banner.getSupplierId(), banner.getSupplierName(),
					banner.getBrandName(), banner.getBrandNameEn(), banner.getBrandId(), banner.getUpdateDate(),
					banner.getScheduleId() };
			getSqlSupport().excuteUpdate(sqlUpdatePOFields, args);
			return true;
			// return affectedRows == 1;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean auditScheduleBanner(ScheduleBanner banner) {
		logger.debug("auditScheduleBanner: " + banner);
		long now = System.currentTimeMillis();
		Object[] args = new Object[] { banner.getStatus().getIntValue(), now, now, banner.getStatusMsg(),
				banner.getAuditUserId(), banner.getAuditUserName(), banner.getScheduleId(), banner.getId() };
		try {
			getSqlSupport().excuteUpdate(sqlUpdateStatus, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteScheduleBannerById(long id) {
		logger.debug("deleteScheduleBannerById: " + id);
		try {
			return deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public ScheduleBanner getScheduleBannerById(long id) {
		logger.debug("getScheduleBannerById: " + id);
		try {
			return getObjectById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public ScheduleBanner getScheduleBannerByScheduleId(long scheduleId) {
		logger.debug("getScheduleBannerByScheduleId: " + scheduleId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, BannerField.scheduleId, scheduleId);
		try {
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<ScheduleBanner> getScheduleBannerList(List<Long> poIdList) {
		logger.debug("getScheduleBannerList: " + poIdList);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(getInClause(BannerField.scheduleId, poIdList));
		try {
			return queryObjects(sql.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<ScheduleBanner>();
		}
	}

	@Override
	public POListDTO getScheduleBannerList(ScheduleCommonParamDTO paramDTO, String supplierName, String brandName) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleBanner.class, "B.", "b_")).append(" FROM ");
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleBanner B WHERE 1=1 ");
		sql.append(" AND A." + ScheduleField.id + "=B." + BannerField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		if (supplierName != null && !"".equals(supplierName)) {
			sql.append(" AND B." + BannerField.supplierName + " = '" + supplierName + "'");
		}
		sql.append(getSubpartSql(brandName, paramDTO.poId, paramDTO.bannerOrPageStatusList, paramDTO.startDate,
				paramDTO.endDate, paramDTO.createWhereFlag));
		sql.append(" ORDER BY B." + BannerField.submitDate + " DESC");
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getBannerListDTO(sql.toString(), paramDTO);
	}

	@Override
	public POListDTO getScheduleBannerListWithSupplierIdList(ScheduleCommonParamDTO paramDTO, String brandName) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(getSelectAllFieldsSql(Schedule.class, "A.", null)).append(",")
				.append(getSelectAllFieldsSql(ScheduleBanner.class, "B.", "b_")).append(" FROM ");
		sql.append(" Mmall_SaleSchedule_Schedule A, Mmall_SaleSchedule_ScheduleBanner B WHERE 1=1 ");
		sql.append(" AND A." + ScheduleField.id + "=B." + BannerField.scheduleId);
		sql.append(getAllowedSiteListSql(paramDTO, "A."));
		
		sql.append(getInClause("B." + BannerField.supplierId, paramDTO.supplierIdList));
		sql.append(getSubpartSql(brandName, paramDTO.poId, paramDTO.bannerOrPageStatusList, paramDTO.startDate,
				paramDTO.endDate, paramDTO.createWhereFlag));
		sql.append(" ORDER BY B." + BannerField.submitDate + " DESC");
		sql.append(genePageSql(paramDTO.curPage, paramDTO.pageSize));
		
		return getBannerListDTO(sql.toString(), paramDTO);
	}

	protected POListDTO getBannerListDTO(String sql, ScheduleCommonParamDTO paramDTO) {
		POListDTO poList = new POListDTO();
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql);
			rs = dbr.getResultSet();
			while (rs.next()) {
				Schedule schedule = (Schedule) getObjFromRS(rs, Schedule.class, null);
				ScheduleBanner banner = (ScheduleBanner) getObjFromRS(rs, ScheduleBanner.class, "b_");
				
				ScheduleDTO scheduleDTO = new ScheduleDTO();
				scheduleDTO.setSchedule(schedule);

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

	private String getSubpartSql(String brandName, long scheduleId, List<CheckState> statusList, long startDate,
			long endDate, boolean createDateFlag) {
		StringBuilder sql = new StringBuilder();
		if (brandName != null && !"".equals(brandName.trim())) {
			sql.append(" AND (B." + BannerField.brandName + " = '" + brandName.trim() + "'");
			sql.append(" OR B." + BannerField.brandNameEn + "= '" + brandName.trim() + "') ");
		}
		if (scheduleId != 0) {
			sql.append(" AND B." + BannerField.scheduleId + "= " + scheduleId);
		}
		if (statusList != null && statusList.size() != 0) {
			sql.append(getInClause("B." + BannerField.status, statusList));
		}
		if (startDate != 0) {
			if (createDateFlag) {
				sql.append(" AND B." + BannerField.createDate + ">=" + startDate);
			} else {
				sql.append(" AND B." + BannerField.submitDate + ">=" + startDate);
			}
		}
		if (endDate != 0) {
			if (createDateFlag) {
				sql.append(" AND B." + BannerField.createDate + "<=" + endDate);
			} else {
				sql.append(" AND B." + BannerField.submitDate + "<=" + endDate);
			}
		}

		return sql.toString();
	}
}
