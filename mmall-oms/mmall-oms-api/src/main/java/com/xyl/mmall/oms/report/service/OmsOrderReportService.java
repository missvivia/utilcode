package com.xyl.mmall.oms.report.service;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;

public interface OmsOrderReportService {

	/**
	 * 同步数据，用在定时器里面
	 * @return
	 */
	public boolean syncData();
	/**
	 * 同步明日的数据，用在定时器里里面
	 * @return
	 */
	public boolean syncTomorrowData();
	/**
	 * 返回全国订单产生概况数据
	 * @param beginTime
	 * @param endTime
	 * @param param
	 * @return
	 */
	public RetArg getOrderReportList(long beginTime, long endTime, DDBParam param);
	/**
	 * 返回明日全国订单生产状况数据
	 * @param beginTime
	 * @param endTime
	 * @param param
	 * @param warehouseIdList
	 * @return
	 */
	public RetArg getTomorrowOrderReportList(long beginTime, long endTime, DDBParam param, 
			List<Long> warehouseIdList);
}
