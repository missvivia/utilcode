package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.dto.PagerParam;
import com.xyl.mmall.oms.report.meta.OmsDelayReport;

/**
 * 
 * @author hzzhanghui
 * 
 */
public interface OmsDelayReportDao extends AbstractDao<OmsDelayReport> {

	boolean insert(OmsDelayReport report);
	
	OmsDelayReport getReportById(long id);

	List<OmsDelayReport> getAllReport();
	
	List<OmsDelayReport> getDelayReportList(long warehouseId, long date);

	List<OmsDelayReport> getReportList(long warehouseId, long startTime, long endTime, PagerParam pagerParam);

}
