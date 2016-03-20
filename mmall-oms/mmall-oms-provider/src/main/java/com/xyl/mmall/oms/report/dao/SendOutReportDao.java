package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutReport;

/**
 * 
 * @author hzliujie
 * 2014年12月11日 上午11:39:08
 */
public interface SendOutReportDao extends AbstractDao<OmsSendOutReport> {

	/**
	 *  根据日期获取发货数据
	 * @param date
	 * @return
	 */
	public List<OmsSendOutReport> getSentOutListByDay(long date);
	
	/**
	 *  根据开始和结束日期获取发货数据
	 * @param date
	 * @return
	 */
	public List<OmsSendOutReport> getSentOutListByDuration(long startTime,long endTime);
	
	
	/**
	 * 根据日期获取当日发货量
	 * @param date
	 * @return
	 */
	public int getSendOutCountByDay(long date);
	
	
	/**
	 * 根据仓库id获取发货数量
	 * @param warehouseId
	 * @return
	 */
	public int getSendOutByWarehouseId(long warehouseId);

	/**
	 * 存储每日统计数据
	 * @param report
	 * @return
	 */
	public OmsSendOutReport save(OmsSendOutReport report);

	/**
	 * 构建每日统计数据
	 * @param day
	 * @return
	 */
	public List<OmsSendOutReport> getSendOutReport(long day);
	
	/**
	 * 
	 * @param day
	 * @return
	 */
	public List<OmsSendOutProvinceForm> getSendOutProvinceForm(long day);

	/**
	 * 组装全国发货报表数据
	 * @param day
	 * @return
	 */
	public List<OmsSendOutCountryForm> getSendOutCountryForm(long day);
	
	/**
	 * 根据仓库和日期获取每日基础数据
	 */
	public OmsSendOutReport getSendOutReportByWarehouseIdAndDay(long warehouseId,long day);
}
