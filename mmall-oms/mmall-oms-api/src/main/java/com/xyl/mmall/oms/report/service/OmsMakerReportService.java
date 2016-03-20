package com.xyl.mmall.oms.report.service;

import java.util.List;

import com.xyl.mmall.oms.dto.OmsReportListDTO;
import com.xyl.mmall.oms.report.meta.OmsDelayReport;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;

/**
 * 
 * @author hzzhanghui
 * 
 */
public interface OmsMakerReportService {

	/**
	 * Add
	 * 
	 * @param report
	 * @return
	 */
	boolean insert(OmsMakerReport report);

	/**
	 * Query all
	 * 
	 * @return
	 */
	OmsReportListDTO getAllReport();
	
	/**
	 * Query by id.
	 * 
	 * @param id
	 * @return
	 */
	OmsMakerReport getReportById(long id);

	/**
	 * Query by condition.
	 * 
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	OmsReportListDTO getReportList(long warehouseId, long startTime, long endTime, int offset, int pageSize);
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	OmsMakerReport getReportList(long warehouseId, long date);
	
	/////////////////////////////////////////////
	/**
	 * Add
	 * 
	 * @param report
	 * @return
	 */
	boolean insertDelayReport(OmsDelayReport report);

	/**
	 * Get delayed report list for special warehouse and special date.
	 * 
	 * @param warehouseId
	 * @param date
	 * @return
	 */
	List<OmsDelayReport> getDelayReportList(long warehouseId, long date);
	
	/**
	 * Query all
	 * 
	 * @return
	 */
	OmsReportListDTO getAllDelayReport();
	
	/**
	 * Query by id.
	 * 
	 * @param id
	 * @return
	 */
	OmsDelayReport getDelayReportById(long id);

	/**
	 * Query by condition.
	 * 
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	OmsReportListDTO getDelayReportList(long warehouseId, long startTime, long endTime, int offset, int pageSize);
	
}
