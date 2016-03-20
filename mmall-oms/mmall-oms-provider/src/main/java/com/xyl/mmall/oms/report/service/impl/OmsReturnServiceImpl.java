package com.xyl.mmall.oms.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.dao.OmsNoReturnDao;
import com.xyl.mmall.oms.report.dao.OmsReturnDao;
import com.xyl.mmall.oms.report.meta.OmsNoReturnReport;
import com.xyl.mmall.oms.report.meta.OmsReturnReport;
import com.xyl.mmall.oms.report.service.OmsReturnService;

@Service("omsReturnService")
public class OmsReturnServiceImpl implements OmsReturnService {
	
	@Autowired
	private OmsReturnDao omsReturnDao;
	
	@Autowired
	private OmsNoReturnDao omsNoReturnDao;


	
	@Override
	public boolean saveReturnData(long date) {
		List<OmsReturnReport> list = omsReturnDao.getReturnList(date);
		if(list!=null && list.size()>0){
			for(OmsReturnReport report:list){
				OmsReturnReport obj = omsReturnDao.getReturnListByWarehouseAndDay(report.getWarehouseId(), report.getDate());
				if(obj==null)
					omsReturnDao.addObject(report);
				else{
					report.setId(obj.getId());
					omsReturnDao.updateObjectByKey(report);
				}
			}
		}
		return true;
	}
	
	/**
	 * 构建未返回单量明细
	 * @param date
	 * @return
	 */
	@Override
	public List<OmsNoReturnReport> getNoReturnDataByDate(long date){
		return omsNoReturnDao.getNoReturnList(date);
	}
	
	/**
	 * 存储未返回单量明细
	 * @param list
	 * @return
	 */
	@Override
	public boolean saveNoReturnData(List<OmsNoReturnReport> list) {
		if(list!=null && list.size()>0){
			for(OmsNoReturnReport report:list){
				omsNoReturnDao.addObject(report);
			}
		}
		return true;
	}

	@Override
	public List<OmsReturnReport> queryReturnReportByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds,DDBParam ddbParam) {
		return omsReturnDao.getReturnListByWarehouseAndDay(warehouseId, startDay, endDay,warehouseIds, ddbParam);
	}

	@Override
	public List<OmsNoReturnReport> queryNoReturnReportByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds,DDBParam ddbParam) {
		return omsNoReturnDao.getNoReturnListByWarehouseAndDay(warehouseId, startDay, endDay, warehouseIds,ddbParam);
	}
	
}
