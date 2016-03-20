package com.xyl.mmall.oms.report.service.impl;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.oms.report.dao.SendOutCountryFormDao;
import com.xyl.mmall.oms.report.dao.SendOutProvinceFormDao;
import com.xyl.mmall.oms.report.dao.SendOutReportDao;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutReport;
import com.xyl.mmall.oms.report.service.OmsSendOutService;

@Service("omsSendOutService")
public class OmsSendOutServiceImpl implements OmsSendOutService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(OmsSendOutServiceImpl.class);
	
	@Autowired
	private SendOutReportDao sendOutDao;
	
	
	@Autowired
	private SendOutCountryFormDao sendOutCountryFormDao;
	
	
	@Autowired
	private SendOutProvinceFormDao sendOutProvinceFormDao;
	
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#addObject(com.xyl.mmall.oms.report.meta.OmsSendOutReport)
	 */
	@Override
	public OmsSendOutReport addObject(OmsSendOutReport obj) {
		return sendOutDao.addObject(obj);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#getOmsSendOutReportByDay(long)
	 */
	@Override
	public List<OmsSendOutReport> getOmsSendOutReportByDay(long date) {
		return sendOutDao.getSentOutListByDay(date);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#getOmsSendOutReportByDuration(long, long)
	 */
	@Override
	public List<OmsSendOutReport> getOmsSendOutReportByDuration(long startTime, long endTime) {
		return sendOutDao.getSentOutListByDuration(startTime, endTime);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#getTotalByDay(long)
	 */
	@Override
	public int getTotalByDay(long date) {
		return sendOutDao.getSendOutCountByDay(date);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#getSendOutByWarehouseId(long)
	 */
	@Override
	public int getSendOutByWarehouseId(long warehouseId) {
		return 0;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#addData(long)
	 */
	@Override
	@Transaction
	public boolean addData(long date) {
		LOGGER.debug("add data begin");
		List<OmsSendOutReport> list = sendOutDao.getSendOutReport(date);
		if(list!=null){
			for(OmsSendOutReport report:list){
				OmsSendOutReport obj = sendOutDao.getSendOutReportByWarehouseIdAndDay(report.getWarehouseId(), date);
				if(obj==null){
					OmsSendOutReport temp = sendOutDao.addObject(report);
					if(temp==null)
						LOGGER.error("intert OmsSendOutCountryForm type data failed.day="+date+",express:"+report.getExpressName());
				}else{
					report.setId(obj.getId());
					sendOutDao.updateObjectByKey(report);
				}
			}
		}
		LOGGER.debug("add data end");
		return true;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#saveCountryData(long)
	 */
	@Override
	public boolean saveCountryData(long date) {
		List<OmsSendOutCountryForm> list = sendOutDao.getSendOutCountryForm(date);
		if(list!=null && list.size()>0){
			for(OmsSendOutCountryForm report:list){
				//存储数据检查当天数据是否存在，用于数据错误重跑数据时清除错误数据
				OmsSendOutCountryForm obj = sendOutCountryFormDao.getDataByExpressAndDay(report.getExpressName(), report.getDate());
				if(obj==null){
					OmsSendOutCountryForm result = sendOutCountryFormDao.addObject(report);
					if(result==null)
						LOGGER.error("intert OmsSendOutCountryForm type data failed.day="+date+",express:"+report.getExpressName());
				}else{
					report.setId(obj.getId());
					sendOutCountryFormDao.updateObjectByKey(report);
				}

			}
		}
		return true;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#saveProvinceData(long)
	 */
	@Override
	public boolean saveProvinceData(long date) {
		long currTime = new Date().getTime();
		List<OmsSendOutProvinceForm> list = sendOutDao.getSendOutProvinceForm(date);
		if(list!=null && list.size()>0){
			for(OmsSendOutProvinceForm obj:list){
				OmsSendOutProvinceForm form = sendOutProvinceFormDao.getSendOutByWarehouseIdAndDay(obj.getWarehouseId(), obj.getDate());
				if(form!=null){
					form.setCodRate(obj.getCodRate());
					form.setExpressName(obj.getExpressName());
					form.setNum(obj.getNum());
					form.setUpdateTime(currTime);
					form.setWarehouse(obj.getWarehouse());
					sendOutProvinceFormDao.updateObjectByKey(form);
				}else{
					OmsSendOutProvinceForm result = sendOutProvinceFormDao.addObject(obj);
					if(result!=null)
						LOGGER.error("intert OmsSendOutProvinceForm type data failed.day=" + date + ",warehouse:"+obj.getWarehouse());
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#querySendOutCountryByDate(long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OmsSendOutCountryForm> querySendOutCountryByDate(long startDay,long endDay,DDBParam param){
		return sendOutCountryFormDao.getDataByDay(startDay, endDay, param);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsSendOutService#querySendOutProvinceByDate(java.lang.String, long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OmsSendOutProvinceForm> querySendOutProvinceByDate(long warehouseId, long startDay, long endDay,List<Long> warehouseIds,DDBParam param) {
		return sendOutProvinceFormDao.getDataByWarehouseAndDay(warehouseId, startDay, endDay, warehouseIds,param);
	}
	
}
