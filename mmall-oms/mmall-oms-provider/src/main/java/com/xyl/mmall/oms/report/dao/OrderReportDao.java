package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OrderReport;

public interface OrderReportDao extends AbstractDao<OrderReport> {

	/**
	 * 查询所需要的数据
	 * @param beginTime
	 * @param endTime
	 * @param param
	 * @return
	 */
	public List<OrderReport> getOrderReportList(long beginTime, long endTime, DDBParam param);
	/**
	 * 同步所需要的数据
	 * @param expressList
	 * @return
	 */
	public boolean syncData(List<String> expressList);
}
