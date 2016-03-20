package com.xyl.mmall.oms.report.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dto.PagerParam;
import com.xyl.mmall.oms.report.dao.OmsMakerReportDao;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;
import com.xyl.mmall.oms.util.OmsUtil;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class OmsMakerReportImplDao extends PolicyObjectDaoSqlBaseOfAutowired<OmsMakerReport> implements
		OmsMakerReportDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean insert(OmsMakerReport report) {
		logger.info("insert: " + report);
		try {
			addObject(report);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

		return true;
	}
	
	@Override
	public OmsMakerReport getReportById(long id) {
		logger.info("getReportById(" + id + ")");
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "id", id);
		
		try {
			logger.info("SQL: " + sql.toString());
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<OmsMakerReport> getAllReport() {
		return getAll();
	}

	@Override
	public List<OmsMakerReport> getReportList(long warehouseId, long startTime, long endTime, PagerParam pagerParam) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		if (warehouseId != 0) {
			SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		}
		if (startTime != 0) {
			sql.append(" AND makeTime >= " + startTime);
		}
		if (endTime != 0) {
			sql.append(" AND makeTime <= " + endTime);
		}
		sql.append(" ORDER BY makeTime DESC ");
		sql.append(OmsUtil.genePageSql(pagerParam.offset, pagerParam.pageSize));

		List<OmsMakerReport> list = new ArrayList<OmsMakerReport>();
		try {
			logger.info("SQL: " + sql.toString());
			list = queryObjects(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			list = null;
		}

		try {
			if (sql.indexOf("limit") != -1) {
				int total = getCnt(OmsUtil.getCntSql(sql.toString()));
				boolean hasNext = OmsUtil.hasNext(total, pagerParam.offset, pagerParam.pageSize);
				pagerParam.hasNext = hasNext;
				pagerParam.total = total;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}

		return list;
	}
	
	@Override
	public OmsMakerReport getReportList(long warehouseId, long date) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		SqlGenUtil.appendExtParamObject(sql, "makeTime", date);
		sql.append(" ORDER BY makeTime DESC ");

		try {
			logger.info("SQL: " + sql.toString());
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private int getCnt(String sql) throws SQLException {
		if (sql == null || "".equals(sql.trim())) {
			return 0;
		}
		int cnt = 0;

		DBResource dbr = null;
		ResultSet rs = null;
		try {
			logger.info("CNT SQL: " + sql);
			dbr = getSqlSupport().excuteQuery(sql);
			rs = dbr.getResultSet();
			if (rs.next()) {
				cnt = rs.getInt("CNT");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			OmsUtil.closeDBQuitely(dbr, rs);
		}

		return cnt;
	}
}
