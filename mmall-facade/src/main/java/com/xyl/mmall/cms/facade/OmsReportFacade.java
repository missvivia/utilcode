package com.xyl.mmall.cms.facade;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.vo.ReceiptVO;
import com.xyl.mmall.cms.vo.SendOutVO;
import com.xyl.mmall.oms.dto.OmsReportListDTO;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.report.meta.OmsDelayReport;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;
import com.xyl.mmall.oms.report.meta.OmsNoReturnReport;
import com.xyl.mmall.oms.report.meta.OmsReceiptReport;
import com.xyl.mmall.oms.report.meta.OmsReturnReport;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;

public interface OmsReportFacade {


	/**
	 * 根据开始时间和结束时间获取发货情况
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<SendOutVO> getSendOutVOByTime(long startTime,long endTime);
	
	/**
	 * 根据开始时间和结束时间获取签收状态统计
	 */
	public List<ReceiptVO> getReceiptVOByTime(long startDay,long endDay);

	/**
	 * 全国发货概括统计和各省发货统计数据入口
	 * @param date
	 */
	public void processSendOUt(long date);

	/**
	 * 分页查询全国发货统计
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<OmsSendOutCountryForm> querySendOutCountryByDay(long startDay,long endDay, DDBParam ddbParam);

	/**
	 * 分页查询各省发货统计
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<OmsSendOutProvinceForm> querySendOutProvinceByDay(long warehouseId,long startDay, long endDay, List<Long> warehouseIds,DDBParam ddbParam);
	
	/**
	 * 各省订单签收状态统计
	 * @param date
	 */
	public boolean processReceipt(long date);

	/**
	 * 各省订单签收状态统计
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	public List<OmsReceiptReport> queryReceiptReporttByWarehouseAndDay(long warehouseId, long startDay, long endDay, List<Long> warehouseIds,DDBParam ddbParam);

	/**
	 * 各省未签收订单归类统计以及未签收明细表
	 * @param date
	 */
	public void processNoReceipt(long date);

	/**
	 * 分页查询各省未牵手订单归类统计
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	public List<OmsNoReceiptReport> queryNoReceiptReport(long warehouseId,long startDay, long endDay,List<Long> warehouseIds, DDBParam ddbParam);

	/**
	 * 查询未签收明细表
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	public List<OmsNoReceiptDetailReport> queryNoReceiptDetailReport(long warehouseId, long startDay, long endDay,List<Long> warehouseIds, DDBParam ddbParam);

	/**
	 * 返货统计报表和未返回单量明细
	 * @param date
	 */
	public void processReturn(long date);

	/**
	 * 根据仓库和时间查询返货数据
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<OmsReturnReport> queryReturnReportByWarehouseAndDay(long warehouseId,long startDay, long endDay, List<Long> warehouseIds,DDBParam ddbParam);

	/**
	 * 根据仓库和时间查询未返货数据
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<OmsNoReturnReport> queryNoReturnReportByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds, DDBParam ddbParam);
	
	
	/**
	 * 获取仓库列表
	 * @return
	 */
	public WarehouseDTO[] getWarehouseList(List<Long> areaLists);
	
	/**
	 * 分页查询发货报表
	 * @param warehouseId
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	public RetArg queryOmsShipOutReport(long warehouseId, long startDay, long endDay, DDBParam ddbParam);
	
	/**
	 * 全国订单产生概况数据汇总
	 * @return
	 */
	public boolean syncDataforOrder();
	
	/**
	 * 明日生产任务数据汇总
	 * @return
	 */
	public boolean syncDataforTomorrowOrder();
	
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
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	boolean insertReport(OmsMakerReport report);
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	boolean insertDelayReport(OmsDelayReport report);
	
	/**
	 * @param id
	 * @return
	 */
	OmsMakerReport getReportById(long id);

	/**
	 * Query all
	 * 
	 * @return
	 */
	OmsReportListDTO getAllReport();

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
	OmsMakerReport getMakerReport(long warehouseId, long date);
	
	/**
	 * Get warehouse by warehouseId
	 * 
	 * @param warehouseId
	 * @return
	 */
	WarehouseDTO getWarehouseById(long warehouseId);
	
	/**
	 * 
	 * @param omsOrderFormId
	 * @param userId
	 * @return
	 */
	List<OmsOrderPackage> getOmsOrderPackageListByOmsOrderFormId(long omsOrderFormId, long userId);
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<OmsOrderForm> getOmsOrderFormListByTimeRange(long startTime, long endTime);
	
	/**
	 * 
	 * @param omsOrderId
	 * @return
	 */
	OmsOrderForm getOmsOrderFormByOrderId(long omsOrderId);
	
	/**
	 * 
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @param offset
	 * @param limit
	 * @return
	 */
	JSONObject getMakerOrderReport(long warehouseId, long startTime, long endTime, int offset, int limit);
	
	/**
	 * 
	 * @param warehouseId
	 * @param date
	 * @return
	 */
	List<OmsDelayReport> getDelayReportList(long warehouseId, long date);
	
	/**
	 * 
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @param offset
	 * @param limit
	 * @return
	 */
	JSONObject getDelayOrderReport(long warehouseId, long startTime, long endTime, int offset, int limit);
}
