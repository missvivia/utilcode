package com.xyl.mmall.oms.report.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dto.OmsReportListDTO;
import com.xyl.mmall.oms.dto.PagerParam;
import com.xyl.mmall.oms.report.dao.OmsDelayReportDao;
import com.xyl.mmall.oms.report.dao.OmsMakerReportDao;
import com.xyl.mmall.oms.report.meta.OmsDelayReport;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;
import com.xyl.mmall.oms.report.service.OmsMakerReportService;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Service
public class OmsMakerReportServiceImpl implements OmsMakerReportService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OmsMakerReportDao makerDao;

	@Autowired
	private OmsDelayReportDao delayDao;

	@Override
	public boolean insert(OmsMakerReport report) {
		logger.info("insert(" + report + ")");
		return makerDao.insert(report);
	}

	@Override
	public OmsMakerReport getReportById(long id) {
		logger.info("getReportById: " + id);
		return makerDao.getReportById(id);
	}

	@Override
	public OmsReportListDTO getAllReport() {
		List<OmsMakerReport> list = makerDao.getAllReport();
		if (list == null) {
			list = new ArrayList<OmsMakerReport>();
		}

		OmsReportListDTO dto = new OmsReportListDTO();
		dto.setList(list);

		logger.info("Returned: " + dto);
		return dto;
	}

	@Override
	public OmsReportListDTO getReportList(long warehouseId, long startTime, long endTime, int offset, int pageSize) {
		PagerParam pagerParam = new PagerParam();
		pagerParam.offset = offset;
		pagerParam.pageSize = pageSize;

		List<OmsMakerReport> list = makerDao.getReportList(warehouseId, startTime, endTime, pagerParam);
		if (list == null) {
			list = new ArrayList<OmsMakerReport>();
		}

		OmsReportListDTO dto = new OmsReportListDTO();
		dto.setList(list);
		dto.setTotal(pagerParam.total);
		dto.setHasNext(pagerParam.hasNext);

		logger.info("Returned: " + dto);
		return dto;
	}

	@Override
	public OmsMakerReport getReportList(long warehouseId, long date) {
		return makerDao.getReportList(warehouseId, date);
	}
	
	@Override
	public boolean insertDelayReport(OmsDelayReport report) {
		return delayDao.insert(report);
	}
	
	public List<OmsDelayReport> getDelayReportList(long warehouseId, long date) {
		return delayDao.getDelayReportList(warehouseId, date);
	}

	@Override
	public OmsReportListDTO getAllDelayReport() {
		List<OmsDelayReport> list = delayDao.getAllReport();
		if (list == null) {
			list = new ArrayList<OmsDelayReport>();
		}

		OmsReportListDTO dto = new OmsReportListDTO();
		dto.setList(list);

		logger.info("Returned: " + dto);
		return dto;
	}

	@Override
	public OmsDelayReport getDelayReportById(long id) {
		return delayDao.getReportById(id);
	}

	@Override
	public OmsReportListDTO getDelayReportList(long warehouseId, long startTime, long endTime, int offset, int pageSize) {
		PagerParam pagerParam = new PagerParam();
		pagerParam.offset = offset;
		pagerParam.pageSize = pageSize;

		List<OmsDelayReport> list = delayDao.getReportList(warehouseId, startTime, endTime, pagerParam);
		if (list == null) {
			list = new ArrayList<OmsDelayReport>();
		}

		OmsReportListDTO dto = new OmsReportListDTO();
		dto.setList(list);
		dto.setTotal(pagerParam.total);
		dto.setHasNext(pagerParam.hasNext);

		logger.info("Returned: " + dto);
		return dto;
	}

}
