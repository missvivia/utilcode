package com.xyl.mmall.saleschedule.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.saleschedule.dao.ScheduleSiteRelaDao;
import com.xyl.mmall.saleschedule.enums.DBField.SiteRelaField;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Repository
public class ScheduleSiteRelaDaoImpl extends ScheduleBaseDao<ScheduleSiteRela> implements ScheduleSiteRelaDao {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleSiteRelaDaoImpl.class);

	private String tableName = this.getTableName();

	private String sqlDel = "DELETE FROM " + tableName + " WHERE " + SiteRelaField.scheduleId + "=? ";

	private String sqlUpdateShowOrder = "UPDATE " + tableName + " SET showOrder=? WHERE " + SiteRelaField.scheduleId + "=? AND "
			+ SiteRelaField.saleSiteId + "=? ";
	
	private String sqlUpdateShowOrder2 = "UPDATE " + tableName + " SET showOrder=? WHERE " + SiteRelaField.scheduleId + "=?";
	
	private String sqlUpdatePOStartTime = "UPDATE " + tableName + " SET poStartTime=? WHERE " + SiteRelaField.scheduleId + "=?";

	@Override
	public boolean saveScheduleSiteRela(ScheduleSiteRela bean) {
		logger.debug("saveScheduleSiteRela: " + bean);
		try {
			addObject(bean);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean batchSaveScheduleSiteRela(List<ScheduleSiteRela> beanList) {
		logger.debug("batchSaveScheduleSiteRela: " + beanList);
		try {
			return addObjects(beanList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteScheduleSiteRelaByScheduleId(long scheduleId) {
		String failLog = "Failured to delete relation by scheduleId '" + scheduleId + "'!!!";
		return update(sqlDel, new Object[]{scheduleId}, failLog);
	}
	
	@Override
	public boolean updateScheduleSiteRelaShowOrder(int showOrder, long scheduleId, long saleSiteId) {
		String failLog = "Failured to update showOrder schedule '" + scheduleId + "' in sale site '" + saleSiteId + "'!!!";
		return update(sqlUpdateShowOrder, new Object[]{showOrder, scheduleId, saleSiteId}, failLog);
	}
	
	@Override
	public boolean updateScheduleSiteRelaShowOrder(int showOrder, long scheduleId) {
		String failLog = "Failured to update showOrder schedule '" + scheduleId + "!!!";
		return update(sqlUpdateShowOrder2, new Object[]{showOrder, scheduleId}, failLog);
	}
	
	@Override
	public boolean updateScheduleSiteRelaPOStartTime(long poStartTime, long scheduleId) {
		String failLog = "Failured to update po '" + scheduleId + "' startTime '" + poStartTime + "'!!!";
		return update(sqlUpdatePOStartTime, new Object[]{poStartTime, scheduleId}, failLog);
	}
	
	@Cacheable(value = "poCache")
	@Override
	public List<ScheduleSiteRela> getScheduleSiteRelaList(long scheduleId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "scheduleId", scheduleId);
		
		logger.debug("SQL: " + sql.toString());
		return queryObjects(sql);
	}
	
	@Override
	public List<ScheduleSiteRela> getScheduleSiteRelaListWithNoCache(List<Long> scheduleIdList) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		sql.append(getInClause(SiteRelaField.scheduleId, scheduleIdList));
		logger.debug("SQL: " + sql.toString());
		return queryObjects(sql);
	}

	@Override
	public List<ScheduleSiteRela> getScheduleSiteRelaList(long saleSiteId, long poStartTime) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "saleSiteId", saleSiteId);
		SqlGenUtil.appendExtParamObject(sql, "poStartTime", poStartTime);
		logger.debug("SQL: " + sql.toString());
		return queryObjects(sql);
	}
	
	@Cacheable(value = "poCache")
	@Override
	public int getScheduleShowOrder(long saleSiteId, long poId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "saleSiteId", saleSiteId);
		SqlGenUtil.appendExtParamObject(sql, "scheduleId", poId);
		logger.debug("SQL: " + sql.toString());
		ScheduleSiteRela siteRela = super.queryObject(sql.toString());
		if (siteRela == null) {
			return 999;
		} else {
			return siteRela.getShowOrder();
		}
	}
	
	@Override
	public List<ScheduleSiteRela> getAllScheduleSiteRelaList() {
		return super.getAll();
	}
}
