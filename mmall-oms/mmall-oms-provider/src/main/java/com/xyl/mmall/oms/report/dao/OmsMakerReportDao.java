package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.dto.PagerParam;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;

/**
 * 
 * @author hzzhanghui
 * 
 */
public interface OmsMakerReportDao extends AbstractDao<OmsMakerReport> {

	boolean insert(OmsMakerReport report);
	
	OmsMakerReport getReportById(long id);

	List<OmsMakerReport> getAllReport();

	List<OmsMakerReport> getReportList(long warehouseId, long startTime, long endTime, PagerParam pagerParam);

	OmsMakerReport getReportList(long warehouseId, long date);
}
