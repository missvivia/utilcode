package com.xyl.mmall.oms.report.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.oms.report.dao.OmsNoReceiptDetailDao;
import com.xyl.mmall.oms.report.dao.OmsNoReceiptDetailReportDao;
import com.xyl.mmall.oms.report.dao.OmsNoReceiptReportDao;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetail;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;
import com.xyl.mmall.oms.report.service.OmsNoReceiptService;

@Service("omsNoReceiptService")
public class OmsNoReceiptServiceImpl implements OmsNoReceiptService {

	private static Logger LOGGER = LoggerFactory.getLogger(OmsNoReceiptServiceImpl.class);
	
	@Autowired
	private OmsNoReceiptDetailDao omsNoReceiptDetailDao; 
	
	@Autowired
	private OmsNoReceiptReportDao omsNoReceiptReportDao; 
	
	@Autowired
	private OmsNoReceiptDetailReportDao omsNoReceiptDetailReportDao; 
	
		
	
	@Override
	@Transaction
	public boolean saveData(long date) {
		List<OmsNoReceiptDetail> list = omsNoReceiptDetailDao.getNoReceiptDetailBefore(date);
		if(list!=null && list.size()>0){
			for(OmsNoReceiptDetail detail:list){
				OmsNoReceiptDetail obj = omsNoReceiptDetailDao.getNoReceiptDetail(date, detail.getPackageId());
				if(obj==null){
					OmsNoReceiptDetail result = omsNoReceiptDetailDao.addObject(detail);
					if(result==null)
						LOGGER.error("save data into Mmall_Oms_Report_NoReceiptDetail failed,day="+detail.getDay()+",warehouseId="+detail.getWarehouseId());
				}else{
					detail.setId(obj.getId());
					omsNoReceiptDetailDao.updateObjectByKey(detail);
				}
			}
		}
		return true;
	}

	@Override
	public boolean saveNoReceiptReport(long date){
		List<OmsNoReceiptReport> list = omsNoReceiptDetailDao.getNoReceiptReportByDay(date);
		if(list!=null && list.size()>0){
			for(OmsNoReceiptReport report:list){
				OmsNoReceiptReport obj = omsNoReceiptReportDao.getNoReceiptReportByWarehouseIdAndDay(report.getWarehouseId(), date);
				if(obj==null){
					OmsNoReceiptReport result = omsNoReceiptReportDao.addObject(report);
					if(result==null)
						LOGGER.error("save data into Mmall_Oms_Report_NoReceiptReport failed,day="+report.getDate()+",warehouseId="+report.getWarehouseId());
				}else{
					report.setId(obj.getId());
					omsNoReceiptReportDao.updateObjectByKey(report);
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean saveNoReceiptDetailReport(long date){
		List<OmsNoReceiptDetailReport> list = omsNoReceiptDetailDao.getNoReceiptDetailReportByDay(date);
		if(list!=null && list.size()>0){
			for(OmsNoReceiptDetailReport report:list){
				OmsNoReceiptDetailReport result = omsNoReceiptDetailReportDao.addObject(report);
				if(result==null)
					LOGGER.error("save data into Mmall_Oms_Report_NoReceiptDetailReport failed,day="+report.getDate()+",warehouseId="+report.getWarehouseName());
			}
		}
		return true;
	}

	@Override
	public List<OmsNoReceiptReport> queryNoReceiptReport(
			long warehouseId, long startDay, long endDay,List<Long> warehouseIds, DDBParam ddbParam) {
		return omsNoReceiptReportDao.getNoReceiptReport(warehouseId, startDay, endDay,warehouseIds, ddbParam);
	}

	@Override
	public List<OmsNoReceiptDetailReport> queryNoReceiptDetailReport(
			long warehouseId, long startDay, long endDay,List<Long> warehouseIds, DDBParam ddbParam) {
		return omsNoReceiptDetailReportDao.getNoReceiptDetailReport(warehouseId, startDay, endDay, warehouseIds,ddbParam);
	}
}
