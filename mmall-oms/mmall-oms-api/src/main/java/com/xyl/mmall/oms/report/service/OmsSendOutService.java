package com.xyl.mmall.oms.report.service;

import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutReport;

public interface OmsSendOutService {
	/**
	 * 输入数据
	 * @param obj
	 * @return
	 */
	public OmsSendOutReport addObject(OmsSendOutReport obj);
	
	/**
	 * 根据日期获取当天的发货情况
	 * @param date
	 * @return
	 */
	public List<OmsSendOutReport> getOmsSendOutReportByDay(long date);
	
	/**
	 * 根据日期获取当天的发货情况
	 * @param date
	 * @return
	 */
	public List<OmsSendOutReport> getOmsSendOutReportByDuration(long startTime,long endTime);
	
	/**
	 * 根据日期获取发货总数
	 * @param date
	 * @return
	 */
	public int getTotalByDay(long date);
	
	/**
	 * 根据仓库获取发货量情况
	 * @param warehouseId
	 * @return
	 */
	public int getSendOutByWarehouseId(long warehouseId);
	
	/**
	 * 添加每日发货数据
	 * @param date
	 * @return
	 */
	public boolean addData(long date);
	
	/**
	 * 添加全国发货报表数据
	 * @param date
	 * @return
	 */
	public boolean saveCountryData(long date);
	
	/**
	 * 添加各省发货统计
	 * @param date
	 * @return
	 */
	public boolean saveProvinceData(long date);
	
	/**
	 * 报表查询全国发货数据
	 * @param startDay
	 * @param endDay
	 * @param param
	 * @return
	 */
	public List<OmsSendOutCountryForm> querySendOutCountryByDate(long startDay, long endDay, DDBParam param);
	
	/**
	 * 报表查询各省发货数据,仓库名字，开始日期，结束日期
	 * @param startDay
	 * @param endDay
	 * @param param
	 * @return
	 */
	public List<OmsSendOutProvinceForm> querySendOutProvinceByDate(long warehouseId,long startDay, long endDay,List<Long> warehouseIds, DDBParam param);
}
