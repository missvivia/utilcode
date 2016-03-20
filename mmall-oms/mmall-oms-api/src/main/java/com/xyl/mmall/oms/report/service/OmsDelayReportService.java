package com.xyl.mmall.oms.report.service;

import com.xyl.mmall.oms.dto.OmsReportListDTO;
import com.xyl.mmall.oms.report.meta.OmsDelayReport;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;

/**
 * 
 * @author hzzhanghui
 * 
 */
public interface OmsDelayReportService {

	/**
	 * Add
	 * 
	 * @param report
	 * @return
	 */
	boolean insert(OmsDelayReport report);

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
	OmsDelayReport getReportById(long id);

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
}
