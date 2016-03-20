package com.xyl.mmall.saleschedule.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.dto.SchedulePageDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO.OrderFlag;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.JITMode;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.enums.SupplyMode;
import com.xyl.mmall.saleschedule.enums.DBField.BannerField;
import com.xyl.mmall.saleschedule.enums.DBField.PageField;
import com.xyl.mmall.saleschedule.enums.DBField.ScheduleField;
import com.xyl.mmall.saleschedule.enums.DBField.ViceField;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;
import com.xyl.mmall.saleschedule.meta.SchedulePage;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * 
 * @author hzzhanghui
 * 
 * @param <E>
 */
public abstract class ScheduleBaseDao<E> extends PolicyObjectDaoSqlBaseOfAutowired<E> {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String tableName = this.getTableName();

	protected List<Schedule> queryScheduleListTemplate(StringBuilder sqlAll) throws SQLException {
		return null;
	}

	protected List<ScheduleBanner> queryScheduleBannerListTemplate(StringBuilder sqlAll) throws SQLException {
		return null;
	}

	protected List<SchedulePage> querySchedulePageListTemplate(StringBuilder sqlAll) throws SQLException {
		return null;
	}

	protected POListDTO querySchedulePageListForChlTemplate(StringBuilder sqlAll) throws SQLException {
		return null;
	}

	protected POListDTO querySchedulePageListForPOPageTemplate(StringBuilder sqlAll) throws SQLException {
		return null;
	}

	protected POListDTO queryScheduleBannerListForPOPageTemplate(StringBuilder sqlAll) throws SQLException {
		return null;
	}

	protected Calendar getLastTime() {
		Calendar c = Calendar.getInstance();
		setLast(c);

		return c;
	}

	protected Calendar getFirstTime() {
		Calendar c = Calendar.getInstance();
		setFirst(c);

		return c;
	}

	protected void setFirst(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}

	protected void setLast(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
	}

	protected boolean hasNext(int total, int curPage, int pageSize) {
		if (curPage != 0 && pageSize != 0) {
			return curPage * pageSize < total;
		}
		return false;
	}

	protected POListDTO getPOListResult(String sqlSelectHeader, String sqlMain, String orderBySql, String pageSql,
			boolean pageFlag, int curPage, int pageSize) {
		return _getPOListResult(sqlSelectHeader, sqlMain, orderBySql, pageSql, pageFlag, curPage, pageSize, 1);
	}

	protected POListDTO getPOListResultForBanner(String sqlSelectHeader, String sqlMain, String orderBySql,
			String pageSql, boolean pageFlag, int curPage, int pageSize) {
		return _getPOListResult(sqlSelectHeader, sqlMain, orderBySql, pageSql, pageFlag, curPage, pageSize, 2);
	}

	protected POListDTO getPOListResultForPage(String sqlSelectHeader, String sqlMain, String orderBySql,
			String pageSql, boolean pageFlag, int curPage, int pageSize) {
		return _getPOListResult(sqlSelectHeader, sqlMain, orderBySql, pageSql, pageFlag, curPage, pageSize, 3);
	}

	protected POListDTO getPOListResultForChl(String sqlSelectHeader, String sqlMain, String orderBySql,
			String pageSql, boolean pageFlag, int curPage, int pageSize) {
		return _getPOListResult(sqlSelectHeader, sqlMain, orderBySql, pageSql, pageFlag, curPage, pageSize, 4);
	}

	protected POListDTO getPOListResultForPOPage(String sqlSelectHeader, String sqlMain, String orderBySql,
			String pageSql, boolean pageFlag, int curPage, int pageSize) {
		return _getPOListResult(sqlSelectHeader, sqlMain, orderBySql, pageSql, pageFlag, curPage, pageSize, 5);
	}

	protected POListDTO getPOListResultForPOBanner(String sqlSelectHeader, String sqlMain, String orderBySql,
			String pageSql, boolean pageFlag, int curPage, int pageSize) {
		return _getPOListResult(sqlSelectHeader, sqlMain, orderBySql, pageSql, pageFlag, curPage, pageSize, 6);
	}

	private POListDTO _getPOListResult(String sqlSelectHeader, String sqlMain, String orderBySql, String pageSql,
			boolean pageFlag, int curPage, int pageSize, int flag) {
		POListDTO poList = new POListDTO();
		try {
			int total = 0;
			boolean hasNext = false;
			if (pageFlag) {
				// total
				StringBuilder pageSqlAll = new StringBuilder(256);
				pageSqlAll.append(geneCntSql()).append(sqlMain);
				logger.debug("PageSql: " + pageSqlAll);
				total = getCnt(pageSqlAll.toString());
				hasNext = hasNext(total, curPage, pageSize);
			}

			StringBuilder sqlAll = new StringBuilder(256);
			sqlAll.append(sqlSelectHeader).append(sqlMain).append(orderBySql).append(pageSql);
			logger.debug("Sql: " + sqlAll);
			if (flag == 1) { // PO
				List<Schedule> scheduleList = queryScheduleListTemplate(sqlAll);
				poList = genPOList(scheduleList, total, hasNext);
			} else if (flag == 2) { // banner
				List<ScheduleBanner> bannerList = queryScheduleBannerListTemplate(sqlAll);
				poList = genPOListForBanner(bannerList, total, hasNext);
			} else if (flag == 3) { // pages
				List<SchedulePage> pageList = querySchedulePageListTemplate(sqlAll);
				poList = genPOListForPage(pageList, total, hasNext);
			} else if (flag == 4) { // mobile or mainsite
				poList = querySchedulePageListForChlTemplate(sqlAll);
			} else if (flag == 5) { // backend query PO page list
				poList = querySchedulePageListForPOPageTemplate(sqlAll);
			} else if (flag == 6) {
				poList = queryScheduleBannerListForPOPageTemplate(sqlAll);
			}

			poList.setHasNext(hasNext);
			poList.setTotal(total);
			return poList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new POListDTO();
		}
	}

	protected POListDTO genPOList(List<Schedule> scheduleList, int total, boolean hasNext) {
		POListDTO poList = new POListDTO();
		poList.setTotal(total);
		poList.setHasNext(hasNext);

		if (scheduleList == null) {
			return poList;
		}

		List<PODTO> poDTOList = new ArrayList<PODTO>();
		for (Schedule schedule : scheduleList) {
			PODTO poDTO = new PODTO();
			ScheduleDTO sDTO = new ScheduleDTO();
			sDTO.setSchedule(schedule);
			poDTO.setScheduleDTO(sDTO);

			poDTOList.add(poDTO);
		}

		poList.setPoList(poDTOList);
		return poList;
	}

	protected POListDTO genPOListForBanner(List<ScheduleBanner> bannerList, int total, boolean hasNext) {
		POListDTO poList = new POListDTO();
		poList.setTotal(total);
		poList.setHasNext(hasNext);

		if (bannerList == null) {
			return poList;
		}

		for (ScheduleBanner banner : bannerList) {
			PODTO poDTO = new PODTO();

			ScheduleBannerDTO bannerDTO = new ScheduleBannerDTO();
			bannerDTO.setBanner(banner);
			poDTO.setBannerDTO(bannerDTO);

			poList.getPoList().add(poDTO);
		}

		return poList;
	}

	protected POListDTO genPOListForPage(List<SchedulePage> pageList, int total, boolean hasNext) {
		POListDTO poList = new POListDTO();
		poList.setTotal(total);
		poList.setHasNext(hasNext);

		if (pageList == null) {
			return poList;
		}

		for (SchedulePage page : pageList) {
			PODTO poDto = new PODTO();
			SchedulePageDTO pageDto = new SchedulePageDTO();
			pageDto.setPage(page);
			poDto.setPageDTO(pageDto);

			poList.getPoList().add(poDto);
		}

		return poList;
	}

	protected int getCnt(String sql) throws SQLException {
		if (sql == null || "".equals(sql.trim())) {
			return 0;
		}
		int cnt = 0;

		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql);
			rs = dbr.getResultSet();
			if (rs.next()) {
				cnt = rs.getInt("CNT");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeDBQuitely(dbr, rs);
		}

		return cnt;
	}

	protected String geneOrderBySql(String field) {
		return " ORDER BY " + field + " ";
	}

	protected String geneOrderBySql2(String prefix, OrderFlag orderFlag) {
		if (orderFlag == OrderFlag.ID_ASC) {
			return " ORDER BY " + prefix + ScheduleField.id + " ";
		}
		if (orderFlag == OrderFlag.ID_DESC) {
			return " ORDER BY " + prefix + ScheduleField.id + " DESC ";
		}
		if (orderFlag == OrderFlag.STARTTIME_ASC) {
			return " ORDER BY " + prefix + ScheduleField.startTime + " ";
		}
		if (orderFlag == OrderFlag.STARTTIME_DESC) {
			return " ORDER BY " + prefix + ScheduleField.startTime + " DESC ";
		}

		if (orderFlag == OrderFlag.CREATETIME_ASC) {
			return " ORDER BY " + prefix + ScheduleField.createTimeForLogic + " ";
		}
		if (orderFlag == OrderFlag.CREATETIME_DESC) {
			return " ORDER BY " + prefix + ScheduleField.createTimeForLogic + " DESC ";
		}

		if (orderFlag == OrderFlag.SHOWORDER_ASC) {
			return " ORDER BY " + prefix + ScheduleField.showOrder + " ";
		}
		if (orderFlag == OrderFlag.SHOWORDER_DESC) {
			return " ORDER BY " + prefix + ScheduleField.showOrder + " DESC ";
		}
		return null;
	}

	protected String geneCntSql() {
		return " SELECT COUNT(*) CNT FROM ";
	}

	protected String genePageSql(int curPage, int pageSize) {
		StringBuilder sb = new StringBuilder(" ");
		if (curPage < 0) {
			curPage = 0;
		}

		if (pageSize < 0) {
			pageSize = 0;
		}

		if (curPage == 0 && pageSize == 0) {
			return sb.toString();
		}

		sb.append(" limit " + curPage + "," + pageSize);

		return sb.toString();
	}

	protected String geneExpireSql(String field) {
		return " AND " + field + ">" + System.currentTimeMillis();
	}

	protected String geneTopSql(int pageSize) {
		if (pageSize <= 0) {
			return "";
		}
		return " limit " + pageSize;
	}

	protected int getOffset(int curPage, int pageSize) {
		return (curPage - 1) * pageSize;
	}

//	protected ScheduleBanner genBannerObjFromRS(ResultSet rs) throws SQLException {
//		ScheduleBanner banner = new ScheduleBanner();
//		banner.setAuditUserId(rs.getLong("b_" + BannerField.auditUserId));
//		banner.setAuditUserName(rs.getString("b_" + BannerField.auditUserName));
//		banner.setBrandName(rs.getString("b_" + BannerField.brandName));
//		banner.setBrandId(rs.getLong("b_" + BannerField.brandId));
//		banner.setBrandNameEn(rs.getString("b_" + BannerField.brandNameEn));
//		banner.setComment(rs.getString("b_" + BannerField.comment));
//		banner.setCreateDate(rs.getLong("b_" + BannerField.createDate));
//		//banner.setSaleAreaId(rs.getLong("b_" + BannerField.saleAreaId));
//		banner.setHomeBannerImgId(rs.getLong("b_" + BannerField.homeBannerImgId));
//		banner.setHomeBannerImgUrl(rs.getString("b_" + BannerField.homeBannerImgUrl));
//		banner.setId(rs.getLong("b_" + BannerField.id));
//		banner.setPreBannerImgId(rs.getLong("b_" + BannerField.preBannerImgId));
//		banner.setPreBannerImgUrl(rs.getString("b_" + BannerField.preBannerImgUrl));
//		banner.setScheduleId(rs.getLong("b_" + BannerField.scheduleId));
//		banner.setStatus(CheckState.NULL.genEnumByIntValue(rs.getInt("b_" + BannerField.status)));
//		banner.setStatusMsg(rs.getString("b_" + BannerField.statusMsg));
//		banner.setStatusUpdateDate(rs.getLong("b_" + BannerField.statusUpdateDate));
//		banner.setSubmitDate(rs.getLong("b_" + BannerField.submitDate));
//		banner.setSupplierId(rs.getLong("b_" + BannerField.supplierId));
//		banner.setSupplierName(rs.getString("b_" + BannerField.supplierName));
//		banner.setUpdateDate(rs.getLong("b_" + BannerField.updateDate));
//		banner.setUserId(rs.getLong("b_" + BannerField.userId));
//		banner.setUserName(rs.getString("b_" + BannerField.userName));
//		return banner;
//	}

	protected SchedulePage genSchedulePageObjFromRS(ResultSet rs) throws SQLException {
		SchedulePage page = new SchedulePage();
		page.setAllListPartOthers(rs.getString(PageField.allListPartOthers));
		page.setAllListPartVisiable(rs.getBoolean(PageField.allListPartVisiable));
		page.setBgImgId(rs.getLong(PageField.bgImgId));
		page.setBgSetting(rs.getString(PageField.bgSetting));
		page.setBrandId(rs.getLong(PageField.brandId));
		page.setBrandName(rs.getString(PageField.brandName));
		page.setBrandNameEn(rs.getString(PageField.brandNameEn));
		page.setComment(rs.getString(PageField.comment));
		page.setCreateDate(rs.getLong(PageField.createDate));
		page.setCreatePerson(rs.getString(PageField.createPerson));
		page.setSaleAreaId(rs.getLong(PageField.saleAreaId));
		page.setHeaderImgId(rs.getLong(PageField.headerImgId));
		page.setHeaderSetting(rs.getString(PageField.headerSetting));
		page.setId(rs.getLong(PageField.id));
		page.setMapPartOthers(rs.getString(PageField.mapPartOthers));
		page.setMapPartVisiable(rs.getBoolean(PageField.mapPartVisiable));
		page.setPartDisplayOrderList(rs.getString(PageField.partDisplayOrderList));
		page.setPrdListOrderType(rs.getInt(PageField.prdListOrderType));
		page.setScheduleId(rs.getLong(PageField.scheduleId));
		page.setStatus(CheckState.NULL.genEnumByIntValue(rs.getInt(PageField.status)));
		page.setStatusMsg(rs.getString(PageField.statusMsg));
		page.setStatusUpdateDate(rs.getLong(PageField.statusUpdateDate));
		page.setSupplierId(rs.getLong(PageField.supplierId));
		page.setSupplierName(rs.getString(PageField.supplierName));
		page.setTitle(rs.getString(PageField.title));
		page.setUdImgIds(rs.getString(PageField.udImgIds));
		page.setUdProductIds(rs.getString(PageField.udProductIds));
		page.setUdSetting(rs.getString(PageField.udSetting));
		page.setUpdateCnt(rs.getInt(PageField.updateCnt));
		page.setUserId(rs.getLong(PageField.userId));

		return page;
	}

//	protected Schedule genScheduleObjFromRS(ResultSet rs) throws SQLException {
//		Schedule schedule = new Schedule();
//		schedule.setId(rs.getLong(ScheduleField.id));
//		schedule.setAdjustTimeDesc(rs.getString(ScheduleField.adjustTimeDesc));
//		schedule.setAdPosition(rs.getString(ScheduleField.adPosition));
//		schedule.setBrandId(rs.getLong(ScheduleField.brandId));
//		schedule.setBrandName(rs.getString(ScheduleField.brandName));
//		schedule.setBrandNameEn(rs.getString(ScheduleField.brandNameEn));
//		schedule.setBrandLogo(rs.getString(ScheduleField.brandLogo));
//		schedule.setBrandLogoSmall(rs.getString(ScheduleField.brandLogoSmall));
//		schedule.setCPrice(rs.getBigDecimal(ScheduleField.cPrice));
//		schedule.setCreateTimeForLogic(rs.getLong(ScheduleField.createTimeForLogic));
//		schedule.setCreateUser(rs.getString(ScheduleField.createUser));
//		schedule.setCurSupplierAreaId(rs.getLong(ScheduleField.curSupplierAreaId));
//		schedule.setDeposit(rs.getBigDecimal(ScheduleField.deposit));
//		schedule.setEndTime(rs.getLong(ScheduleField.endTime));
//		schedule.setGrossProfitRate(rs.getBigDecimal(ScheduleField.grossProfitRate));
//		schedule.setJitMode(JITMode.NULL.genEnumByIntValue(rs.getInt(ScheduleField.jitMode)));
//		schedule.setMaxDiscount(rs.getBigDecimal(ScheduleField.maxDiscount));
//		schedule.setMaxPriceAfterDiscount(rs.getBigDecimal(ScheduleField.maxPriceAfterDiscount));
//		schedule.setMinDiscount(rs.getBigDecimal(ScheduleField.minDiscount));
//		schedule.setMinPriceAfterDiscount(rs.getBigDecimal(ScheduleField.minPriceAfterDiscount));
//		schedule.setMortgageRate(rs.getBigDecimal(ScheduleField.mortgageRate));
//		schedule.setPlatformSrvFeeRate(rs.getBigDecimal(ScheduleField.platformSrvFeeRate));
//		schedule.setProductTotalCnt(rs.getInt(ScheduleField.productTotalCnt));
//		schedule.setSaleAreaId(rs.getLong(ScheduleField.saleAreaId));
//		schedule.setScheduleUpdateDate(rs.getLong(ScheduleField.scheduleUpdateDate));
//		schedule.setShowOrder(rs.getInt(ScheduleField.showOrder));
//		schedule.setSkuCnt(rs.getInt(ScheduleField.skuCnt));
//		schedule.setStartTime(rs.getLong(ScheduleField.startTime));
//		schedule.setStatus(ScheduleState.NULL.genEnumByIntValue(rs.getInt(ScheduleField.status)));
//		schedule.setStatusMsg(rs.getString(ScheduleField.statusMsg));
//		schedule.setUpdateTimeForLogic(rs.getLong(ScheduleField.updateTimeForLogic));
//		schedule.setStoreAreaId(rs.getLong(ScheduleField.storeAreaId));
//		schedule.setSupplierId(rs.getLong(ScheduleField.supplierId));
//		schedule.setSupplierName(rs.getString(ScheduleField.supplierName));
//		schedule.setTitle(rs.getString(ScheduleField.title));
//		schedule.setUnitCnt(rs.getInt(ScheduleField.unitCnt));
//		schedule.setUnlossFlag(rs.getBoolean(ScheduleField.unlossFlag));
//		schedule.setUserId(rs.getLong(ScheduleField.userId));
//		schedule.setPageId(rs.getLong(ScheduleField.pageId));
//		schedule.setBannerId(rs.getLong(ScheduleField.bannerId));
//		schedule.setChlFlag(rs.getLong(ScheduleField.chlFlag));
//
//		return schedule;
//	}

	protected String getUpdateBannerSql(ScheduleBanner banner) {
		StringBuffer sb = new StringBuffer();
		sb.append(" UPDATE Mmall_SaleSchedule_ScheduleBanner SET ");
		if (banner.getUserId() != 0) {
			sb.append(" " + BannerField.userId + "=" + banner.getUserId() + ",");
		}
		if (banner.getUserName() != null) {
			sb.append(" " + BannerField.userName + "='" + banner.getUserName() + "',");
		}
		if (banner.getHomeBannerImgId() != 0) {
			sb.append(" " + BannerField.homeBannerImgId + "=" + banner.getHomeBannerImgId() + ",");
		}
		if (banner.getHomeBannerImgUrl() != null) {
			sb.append(" " + BannerField.homeBannerImgUrl + "='" + banner.getHomeBannerImgUrl() + "',");
		}
		if (banner.getPreBannerImgId() != 0) {
			sb.append(" " + BannerField.preBannerImgId + "=" + banner.getPreBannerImgId() + ",");
		}
		if (banner.getPreBannerImgUrl() != null) {
			sb.append(" " + BannerField.preBannerImgUrl + "='" + banner.getPreBannerImgUrl() + "',");
		}
		if (banner.getComment() != null) {
			sb.append(" " + BannerField.comment + "='" + banner.getComment() + "',");
		}
		if (banner.getStatus() != null && banner.getStatus() != CheckState.NULL) {
			sb.append(" " + BannerField.status + "=" + banner.getStatus().getIntValue() + ",");
		}
		if (banner.getStatusMsg() != null) {
			sb.append(" " + BannerField.statusMsg + "='" + banner.getStatusMsg() + "',");
		}
		if (banner.getSubmitDate() != 0) {
			sb.append(" " + BannerField.submitDate + "=" + banner.getSubmitDate() + ",");
		}
		if (banner.getStatusUpdateDate() != 0) {
			sb.append(" " + BannerField.statusUpdateDate + "=" + banner.getStatusUpdateDate() + ",");
		}
		if (banner.getAuditUserId() != 0) {
			sb.append(" " + BannerField.auditUserId + "=" + banner.getAuditUserId() + ",");
		}
		if (banner.getAuditUserName() != null) {
			sb.append(" " + BannerField.auditUserName + "='" + banner.getAuditUserName() + "',");
		}
		if (banner.getUpdateDate() != 0) {
			sb.append(" " + BannerField.updateDate + "=" + banner.getUpdateDate() + ",");
		}
		sb.replace(sb.length() - 1, sb.length(), " ");

		sb.append(" WHERE " + BannerField.id + "=" + banner.getId());
		sb.append(" AND " + BannerField.scheduleId + "=" + banner.getScheduleId());
		logger.debug("Update po sql: " + sb.toString());
		return sb.toString();
	}

	protected String getUpdatePOSql(Schedule schedule) {
		StringBuffer sb = new StringBuffer();
		sb.append(" UPDATE Mmall_SaleSchedule_Schedule SET ");
//		if (schedule.getUserId() != 0) {
//			sb.append(" " + ScheduleField.userId + "=" + schedule.getUserId() + ",");
//		}
		if (schedule.getStartTime() != 0) {
			sb.append(" " + ScheduleField.startTime + "=" + schedule.getStartTime() + ",");
		}
		if (schedule.getEndTime() != 0) {
			sb.append(" " + ScheduleField.endTime + "=" + schedule.getEndTime() + ",");
		}
		if (schedule.getAdjustTimeDesc() != null) {
			sb.append(" " + ScheduleField.adjustTimeDesc + "='" + schedule.getAdjustTimeDesc() + "',");
		}
//		if (schedule.getCurSupplierAreaId() != 0) {
//			sb.append(" " + ScheduleField.curSupplierAreaId + "=" + schedule.getCurSupplierAreaId() + ",");
//		}
		if (schedule.getSupplierId() != 0) {
			sb.append(" " + ScheduleField.supplierId + "=" + schedule.getSupplierId() + ",");
		}
		if (schedule.getSupplierName() != null) {
			sb.append(" " + ScheduleField.supplierName + "='" + schedule.getSupplierName() + "',");
		}
		if (schedule.getBrandId() != 0) {
			sb.append(" " + ScheduleField.brandId + "=" + schedule.getBrandId() + ",");
		}
		if (schedule.getBrandName() != null) {
			sb.append(" " + ScheduleField.brandName + "='" + schedule.getBrandName() + "',");
		}
		if (schedule.getBrandNameEn() != null) {
			sb.append(" " + ScheduleField.brandNameEn + "='" + schedule.getBrandNameEn() + "',");
		}
		if (schedule.getBrandLogo() != null) {
			sb.append(" " + ScheduleField.brandLogo + "='" + schedule.getBrandLogo() + "',");
		}
		if (schedule.getBrandLogoSmall() != null) {
			sb.append(" " + ScheduleField.brandLogoSmall + "='" + schedule.getBrandLogoSmall() + "',");
		}
		if (schedule.getTitle() != null) {
			sb.append(" " + ScheduleField.title + "='" + schedule.getTitle() + "',");
		}
//		sb.append(" " + ScheduleField.grossProfitRate + "=" + schedule.getGrossProfitRate() + ",");
//		sb.append(" " + ScheduleField.cPrice + "=" + schedule.getCPrice() + ",");
//		sb.append(" " + ScheduleField.unlossFlag + "=" + (schedule.isUnlossFlag() ? 1 : 0) + ",");
//		sb.append(" " + ScheduleField.platformSrvFeeRate + "=" + schedule.getPlatformSrvFeeRate() + ",");
//		sb.append(" " + ScheduleField.mortgageRate + "=" + schedule.getMortgageRate() + ",");
//		sb.append(" " + ScheduleField.deposit + "=" + schedule.getDeposit() + ",");
//		sb.append(" " + ScheduleField.maxPriceAfterDiscount + "=" + schedule.getMaxPriceAfterDiscount() + ",");
//		sb.append(" " + ScheduleField.minPriceAfterDiscount + "=" + schedule.getMinPriceAfterDiscount() + ",");
//		sb.append(" " + ScheduleField.productTotalCnt + "=" + schedule.getProductTotalCnt() + ",");
		sb.append(" " + ScheduleField.maxDiscount + "=" + schedule.getMaxDiscount() + ",");
		sb.append(" " + ScheduleField.minDiscount + "=" + schedule.getMinDiscount() + ",");
//		sb.append(" " + ScheduleField.unitCnt + "=" + schedule.getUnitCnt() + ",");
//		sb.append(" " + ScheduleField.skuCnt + "=" + schedule.getSkuCnt() + ",");
		//sb.append(" " + ScheduleField.saleAreaId + "=" + schedule.getSaleAreaId() + ",");
		//sb.append(" " + ScheduleField.storeAreaId + "=" + schedule.getStoreAreaId() + ",");
		sb.append(" " + ScheduleField.jitMode + "=" + schedule.getJitMode().getIntValue() + ",");
		if (schedule.getShowOrder() != 0) {
			sb.append(" " + ScheduleField.showOrder + "=" + schedule.getShowOrder() + ",");
		}
		if (schedule.getAdPosition() != null) {
			sb.append(" " + ScheduleField.adPosition + "='" + schedule.getAdPosition() + "',");
		}
		if (schedule.getUpdateTimeForLogic() != 0) {
			sb.append(" " + ScheduleField.updateTimeForLogic + "=" + schedule.getUpdateTimeForLogic() + ",");
		}
		if (schedule.getScheduleUpdateDate() != 0) {
			sb.append(" " + ScheduleField.scheduleUpdateDate + "=" + schedule.getScheduleUpdateDate() + ",");
		}
		if (schedule.getStatus() != null && schedule.getStatus() != ScheduleState.NULL) {
			sb.append(" " + ScheduleField.status + "=" + schedule.getStatus().getIntValue() + ",");
		}
		if (schedule.getChlFlag() != 0) {
			sb.append(" " + ScheduleField.chlFlag + "=" + schedule.getChlFlag() + ",");
		}
		if (schedule.getPageTitle() != null) {
			sb.append(" " + ScheduleField.pageTitle + "='" + schedule.getPageTitle() + "',");
		}
		if (schedule.getNormalShowPeriod() != 0) {
			sb.append(" " + ScheduleField.normalShowPeriod + "=" + schedule.getNormalShowPeriod() + ",");
		}
		if (schedule.getExtShowPeriod() != 0) {
			sb.append(" " + ScheduleField.extShowPeriod + "=" + schedule.getExtShowPeriod() + ",");
		}
		if (schedule.getSaleSiteFlag() != 0) {
			sb.append(" " + ScheduleField.saleSiteFlag + "=" + schedule.getSaleSiteFlag() + ",");
		}
		
		sb.replace(sb.length() - 1, sb.length(), " ");
		// skip id, createTime, createUser, statusMsg, flagAuditPrdList,
		// flagAuditPage, pageId, flagAuditBanner, bannerId,

		sb.append(" WHERE id=" + schedule.getId());
		logger.debug("Update po sql: " + sb.toString());
		return sb.toString();
	}

	protected String getUpdatePOViceSql(ScheduleVice vice) {
		StringBuffer sb = new StringBuffer();
		sb.append(" UPDATE Mmall_SaleSchedule_ScheduleVice SET ");
//		sb.append(" " + ViceField.grossProfitRate + "=" + vice.getGrossProfitRate() + ",");
//		sb.append(" " + ViceField.cPrice + "=" + vice.getCPrice() + ",");
//		sb.append(" " + ViceField.unlossFlag + "=" + (vice.isUnlossFlag() ? 1 : 0) + ",");
		sb.append(" " + ViceField.platformSrvFeeRate + "=" + vice.getPlatformSrvFeeRate() + ",");
//		sb.append(" " + ViceField.mortgageRate + "=" + vice.getMortgageRate() + ",");
//		sb.append(" " + ViceField.deposit + "=" + vice.getDeposit() + ",");
		sb.append(" " + ViceField.maxPriceAfterDiscount + "=" + vice.getMaxPriceAfterDiscount() + ",");
		sb.append(" " + ViceField.minPriceAfterDiscount + "=" + vice.getMinPriceAfterDiscount() + ",");
		sb.append(" " + ViceField.productTotalCnt + "=" + vice.getProductTotalCnt() + ",");
		sb.append(" " + ViceField.unitCnt + "=" + vice.getUnitCnt() + ",");
		sb.append(" " + ViceField.skuCnt + "=" + vice.getSkuCnt() + ",");
		if (vice.getAdPosition() != null) {
			sb.append(" " + ViceField.adPosition + "='" + vice.getAdPosition() + "',");
		}
		if (vice.getPoFollowerUserId() != 0) {
			sb.append(" " + ViceField.poFollowerUserId + "=" + vice.getPoFollowerUserId() + ",");
		}
		if (vice.getPoFollowerUserName() != null) {
			sb.append(" " + ViceField.poFollowerUserName + "='" + vice.getPoFollowerUserName() + "',");
		}
		if (vice.getSupplierAcct() != null) {
			sb.append(" " + ViceField.supplierAcct + "='" + vice.getSupplierAcct() + "',");
		}
		if (vice.getSupplyMode() != null && vice.getSupplyMode() != SupplyMode.NULL) {
			sb.append(" " + ViceField.supplyMode + "=" + vice.getSupplyMode().getIntValue() + ",");
		}
		//if (vice.getSupplierStoreId() != 0) {
			sb.append(" " + ViceField.supplierStoreId + "=" + vice.getSupplierStoreId() + ",");
		//}
		//if (vice.getBrandStoreId() != 0) {
			sb.append(" " + ViceField.brandStoreId + "=" + vice.getBrandStoreId() + ",");
		//}
		if (vice.getUserId() != 0) {
			sb.append(" " + ViceField.userId + "=" + vice.getUserId() + ",");
		}
		if (vice.getCreateUser() != null) {
			sb.append(" " + ViceField.createUser + "='" + vice.getCreateUser() + "',");
		}
		if (vice.getBrandSupplierName() != null) {
			sb.append(" " + ViceField.brandSupplierName + "='" + vice.getBrandSupplierName() + "',");
		}
		
		if (vice.getBrandSupplierId() != 0) {
			sb.append(" " + ViceField.brandSupplierId + "=" + vice.getBrandSupplierId() + ",");
		}
		if (vice.getSupplierType() != 0) {
			sb.append(" " + ViceField.supplierType + "=" + vice.getSupplierType() + ",");
		}
		if (vice.getPoType() != 0) {
			sb.append(" " + ViceField.poType + "=" + vice.getPoType() + ",");
		}
		
		sb.replace(sb.length() - 1, sb.length(), " ");
		// skip id, scheduleId, userId, createUser, statusMsg, flagAuditPrdList,
		// flagAuditPage, pageId, flagAuditBanner, bannerId,

		sb.append(" WHERE " + ViceField.scheduleId + " = " + vice.getScheduleId());
		logger.debug("Update po vice sql: " + sb.toString());
		return sb.toString();
	}

	protected boolean getPageFlag(ScheduleCommonParamDTO paramDTO) {
		return paramDTO.curPage != 0 || paramDTO.pageSize != 0;
	}

	protected void closeDBQuitely(DBResource dbr, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

		try {
			if (dbr != null) {
				dbr.close();
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	/**
	 * eg. List<Long> list = new ArrayList<Long>(); list.add(100L);
	 * list.add(200L); list.add(300L); System.out.println(getInClause("A.id",
	 * list)); // output: AND A.id IN (100,200,300)
	 * 
	 * List<ScheduleState> list3 = new ArrayList<ScheduleState>();
	 * list3.add(ScheduleState.PASSED); list3.add(ScheduleState.BACKEND_PASSED);
	 * System.out.println(getInClause("A.status", list3)); // output: AND
	 * A.status IN (3,202)
	 * 
	 * @param filed
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected <T> String getInClause(String filed, List<T> list) {
		StringBuffer sb = new StringBuffer(" ");

		if (list != null && list.size() > 0) {
			StringBuilder inClause = new StringBuilder();
			inClause.append(" IN (");
			for (int i = 0; i < list.size(); i++) {
				if (i != (list.size() - 1)) {
					if (list.get(i) instanceof AbstractEnumInterface) {
						inClause.append(((AbstractEnumInterface) list.get(i)).getIntValue()).append(",");
					} else {
						inClause.append(list.get(i).toString()).append(",");
					}
				} else {
					if (list.get(i) instanceof AbstractEnumInterface) {
						inClause.append(((AbstractEnumInterface) list.get(i)).getIntValue());
					} else {
						inClause.append(list.get(i).toString());
					}
				}
			}

			inClause.append(")");

			sb.append(" AND " + filed + inClause.toString());
		}

		return sb.toString();
	}

	public static Object getObjFromRS(ResultSet rs, Class<?> clazz, String aliasPrefix) {
		if (aliasPrefix == null) {
			aliasPrefix = "";
		}
		aliasPrefix = aliasPrefix.trim();

		try {
			Object obj = clazz.newInstance();
			List<FieldInfo> getters = TypeUtils.computeGetters(clazz, null);
			for (FieldInfo field : getters) {
				AnnonOfField anno = field.getAnnotation(AnnonOfField.class);
				if (anno == null) {
					continue;
				}

				String fieldName = field.getName();
				if (anno.dbFieldName() != null && !"".equals(anno.dbFieldName())) {
					fieldName = anno.dbFieldName();
				}
				fieldName = aliasPrefix + fieldName;

				Class<?> fieldClz = field.getFieldClass();
				try {
					// if (isAbstractEnumInterface(fieldClz)) {
					// System.out.println(fieldName +
					// " is from AbstractEnumInterface!!!");
					// field.getField().set(obj, rs.getInt(fieldName));
					// }

					if (fieldClz == CheckState.class) {
						field.getField().set(obj, CheckState.NULL.genEnumByIntValue(rs.getInt(fieldName)));
						continue;
					}
					if (fieldClz == ScheduleState.class) {
						field.getField().set(obj, ScheduleState.NULL.genEnumByIntValue(rs.getInt(fieldName)));
						continue;
					}
					if (fieldClz == JITMode.class) {
						field.getField().set(obj, JITMode.NULL.genEnumByIntValue(rs.getInt(fieldName)));
						continue;
					}
					if (fieldClz == SupplyMode.class) {
						field.getField().set(obj, SupplyMode.NULL.genEnumByIntValue(rs.getInt(fieldName)));
						continue;
					}
					if (fieldClz == long.class) {
						field.getField().set(obj, rs.getLong(fieldName));
						continue;
					}
					if (fieldClz == boolean.class) {
						field.getField().set(obj, rs.getBoolean(fieldName));
						continue;
					}
					if (fieldClz == int.class) {
						field.getField().set(obj, rs.getInt(fieldName));
						continue;
					}

					if (fieldClz == BigDecimal.class) {
						field.getField().set(obj, rs.getBigDecimal(fieldName));
						continue;
					}

					if (fieldClz == String.class) {
						field.getField().set(obj, rs.getString(fieldName));
						continue;
					}

					field.getField().set(obj, rs.getObject(fieldName));
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getSelectAllFieldsSql(Class<?> clz, String prefix, String aliasPrefix) {
		if (clz == null) {
			throw new IllegalArgumentException("Parameter 'clz' cannot be null!!!");
		}
		if (prefix == null) {
			prefix = "";
		}
		prefix = prefix.trim();

		if (aliasPrefix == null) {
			aliasPrefix = "";
		}
		aliasPrefix = aliasPrefix.trim();

		StringBuilder sb = new StringBuilder(" ");
		List<FieldInfo> getters = TypeUtils.computeGetters(clz, null);
		for (int i = 0, j = getters.size(); i < j; i++) {
			FieldInfo field = getters.get(i);

			AnnonOfField anno = field.getAnnotation(AnnonOfField.class);
			if (anno == null) {
				continue;
			}

			String fieldName = field.getName();
			if (anno.dbFieldName() != null && !"".equals(anno.dbFieldName())) {
				fieldName = anno.dbFieldName();
			}

			if (i == (j - 1)) {
				if (!"".equals(aliasPrefix)) {
					sb.append(prefix).append(fieldName).append(" ").append(aliasPrefix).append(fieldName);
				} else {
					sb.append(prefix).append(fieldName);
				}
			} else {
				if (!"".equals(aliasPrefix)) {
					sb.append(prefix).append(fieldName).append(" ").append(aliasPrefix).append(fieldName).append(",");
				} else {
					sb.append(prefix).append(fieldName).append(",");
				}
			}
		}
		sb.append(" ");
		return sb.toString();
	}
	
	protected String getCntSql(String sql) {
		String cntSql = " ";
		if (sql.indexOf("limit") != -1) {
			cntSql = "SELECT COUNT(*) CNT " + (sql.substring(sql.indexOf("FROM"), sql.indexOf("limit")));
		}
		logger.debug("COUNT SQL: " + cntSql);
		return cntSql;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 */
	protected POListDTO getPOListDTOOnlyWithScheduleDTO(String sql, ScheduleCommonParamDTO paramDTO) {
		logger.debug("SQL: " + sql);
		
		POListDTO poList = new POListDTO();
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql);
			rs = dbr.getResultSet();
			while (rs.next()) {
				ScheduleDTO scheduleDTO = new ScheduleDTO();
				Schedule schedule = (Schedule) getObjFromRS(rs, Schedule.class, null);
				ScheduleVice scheduleVice = (ScheduleVice) getObjFromRS(rs, ScheduleVice.class, "b_");
				scheduleDTO.setSchedule(schedule);
				scheduleDTO.setScheduleVice(scheduleVice);

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

	/**
	 * 
	 * @param sql
	 * @return
	 */
	protected ScheduleDTO getPODTOOnlyWithScheduleDTO(String sql) {
		logger.debug("SQL: " + sql);
		ScheduleDTO scheduleDTO = new ScheduleDTO();
		DBResource dbr = null;
		ResultSet rs = null;
		try {
			dbr = getSqlSupport().excuteQuery(sql.toString());
			rs = dbr.getResultSet();
			Schedule schedule = null;
			ScheduleVice vice = null;
			while (rs.next()) {
				if (schedule == null) {
					schedule = (Schedule) getObjFromRS(rs, Schedule.class, null);
				}
				if (vice == null) {
					vice = (ScheduleVice) getObjFromRS(rs, ScheduleVice.class, "b_");
				}
			}
			scheduleDTO.setSchedule(schedule);
			scheduleDTO.setScheduleVice(vice);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeDBQuitely(dbr, rs);
		}

		genSaleSiteListForPO(scheduleDTO);
		return scheduleDTO;
	}

	protected String getAllowedSiteListSql(ScheduleCommonParamDTO paramDTO, String prefix) {
		StringBuilder sql = new StringBuilder(" ");
		if (paramDTO.allowedAreaIdList != null && paramDTO.allowedAreaIdList.size() != 0) {
			if (paramDTO.allowedAreaIdListFlag != -1 && paramDTO.allowedAreaIdListFlag != 0) {
				sql.append(" AND " + prefix + ScheduleField.saleSiteFlag + " & " + paramDTO.allowedAreaIdListFlag
						+ "=" + prefix + ScheduleField.saleSiteFlag);
			}
		}
		if (paramDTO.curSupplierAreaId != 0) {
			if (paramDTO.saleSiteFlag != -1 && paramDTO.saleSiteFlag != 0) {
				sql.append(" AND " + prefix + ScheduleField.saleSiteFlag + " & " + paramDTO.saleSiteFlag + "="
						+ paramDTO.saleSiteFlag);
			}
		}

		return sql.toString();
	}

	protected void genSaleSiteListForPOList(POListDTO poList) {
		for (PODTO poDTO : poList.getPoList()) {
			genSaleSiteListForPO(poDTO.getScheduleDTO());
		}
	}

	protected void genSaleSiteListForPO(ScheduleDTO scheduleDTO) {
		if (scheduleDTO == null || scheduleDTO.getSchedule() == null) {
			return;
		}
		
		long scheduleId = scheduleDTO.getSchedule().getId();
		long saleSiteFlag = scheduleDTO.getSchedule().getSaleSiteFlag();

		List<Long> saleSiteIdList = new ArrayList<Long>();
		try {
			saleSiteIdList = ProvinceCodeMapUtil.getCodeListByProvinceFmt(saleSiteFlag);
		} catch (Exception e) {
			logger.error("Cannot parse saleSiteFlag '" + saleSiteFlag + "' for PO '" + scheduleId + "'!!!");
			return;
		}

		List<ScheduleSiteRela> siteRelaList = new ArrayList<ScheduleSiteRela>();
		for (Long saleSiteId : saleSiteIdList) {
			ScheduleSiteRela siteRela = new ScheduleSiteRela();
			siteRela.setSaleSiteId(saleSiteId);
			siteRela.setScheduleId(scheduleId);
			siteRelaList.add(siteRela);
		}
		scheduleDTO.setSiteRelaList(siteRelaList);
	}
	
	protected boolean _updateStatus(String sql, Object[] args) {
		try {
			getSqlSupport().excuteUpdate(sql, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	protected boolean update(String sql, Object[] args, String failLog) {
		try {
			int result = getSqlSupport().excuteUpdate(sql, args);
			if (result >= 1) {
				return true;
			} else {
				logger.error(failLog);
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
}
