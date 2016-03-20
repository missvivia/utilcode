package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.TomorrowOrderReport;

public interface TomorrowOrderReportDao extends AbstractDao<TomorrowOrderReport> {

	/**
	 * 查询所需要的数据
	 * @param beginTime
	 * @param endTime
	 * @param param
	 * @param warehouseList
	 * @return
	 */
	public List<TomorrowOrderReport> getOrderReportList(long beginTime, long endTime, 
			DDBParam param, List<Long> warehouseList);
	/**
	 * 从其他的表中同步数据过来，计算从昨天下午四点到今天下午四点的订单状态
	 * @param expressList
	 * @return
	 */
	public boolean syncDataTomorrow(List<Long> warehouseList);
}
