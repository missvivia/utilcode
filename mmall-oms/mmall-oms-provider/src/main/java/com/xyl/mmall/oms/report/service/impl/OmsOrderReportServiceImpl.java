package com.xyl.mmall.oms.report.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.WarehouseDao;
import com.xyl.mmall.oms.dto.OrderReportDTO;
import com.xyl.mmall.oms.dto.TomorrowOrderReportDTO;
import com.xyl.mmall.oms.report.dao.OrderReportDao;
import com.xyl.mmall.oms.report.dao.TomorrowOrderReportDao;
import com.xyl.mmall.oms.report.meta.OrderReport;
import com.xyl.mmall.oms.report.meta.TomorrowOrderReport;
import com.xyl.mmall.oms.report.service.OmsOrderReportService;

@Service("omsOrderReportService")
public class OmsOrderReportServiceImpl implements OmsOrderReportService {

	//private static Logger LOGGER = LoggerFactory.getLogger(OmsOrderReportServiceImpl.class);
	
	@Autowired
	private OmsOrderFormDao omsOrderFormDao;
	
	@Autowired
	private OrderReportDao orderReportDao;
	
	@Autowired
	private TomorrowOrderReportDao tomorrowOrderReportDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsOrderReportService#syncData()
	 */
	@Override
	public boolean syncData() {
		List<String> expressList = warehouseDao.getExpressCompanyList();
		return orderReportDao.syncData(expressList);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsOrderReportService#syncTomorrowData()
	 */
	@Override
	public boolean syncTomorrowData() {
		List<Long> warehouseList = warehouseDao.getIdList();
		return tomorrowOrderReportDao.syncDataTomorrow(warehouseList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsOrderReportService#getOrderReportList(long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg/*List<OrderReportDTO>*/ getOrderReportList(long beginTime, long endTime, DDBParam param) {
		List<String> expressList = warehouseDao.getExpressCompanyList();
		List<OrderReportDTO> dtoList = new ArrayList<>();
		List<OrderReport> orderReportList = orderReportDao.getOrderReportList(beginTime, endTime, param);
		Map<Long, OrderReportDTO> dataMap = new HashMap<>();
		Map<Long, Map<String, Integer>> expressDataMap = new HashMap<>();
		for (OrderReport orderReport : orderReportList) {
			long time = orderReport.getTime();
			OrderReportDTO dto = dataMap.get(time);
			if (dto != null) {
				dto.setCancelOrderNumber(dto.getCancelOrderNumber() + orderReport.getCancelOrderNumber());
				dto.setCollectiveOrderNumber(dto.getCollectiveOrderNumber() + orderReport.getCollectiveOrderNumber());
				dto.setTotalOrderNumber(dto.getTotalOrderNumber() + orderReport.getTotalOrderNumber());
				Map<String, Integer> expressMap = expressDataMap.get(dto.getTime());
				assert(expressMap != null);
				expressMap.put(orderReport.getExpressCompany(), orderReport.getTotalOrderNumber());
			} else {
				OrderReportDTO newDto = new OrderReportDTO();
				newDto.setTime(time);
				newDto.setCancelOrderNumber(orderReport.getCancelOrderNumber());
				newDto.setCollectiveOrderNumber(orderReport.getCollectiveOrderNumber());
				newDto.setTotalOrderNumber(orderReport.getTotalOrderNumber());
				Map<String, Integer> expressMap = new HashMap<>();
				expressMap.put(orderReport.getExpressCompany(), orderReport.getTotalOrderNumber());
				expressDataMap.put(time, expressMap);
				dataMap.put(time, newDto);
			}
		}
		
		for (Long time : dataMap.keySet()) {
			OrderReportDTO dto = dataMap.get(time);
			Map<String, Integer> expressMap = expressDataMap.get(time);
			List<JSONObject> jsonList = new ArrayList<>();
			int totalCount = dto.getTotalOrderNumber();
			for (String expressCompany : expressList) {
				JSONObject jsonObject = new JSONObject();
				Integer count = expressMap.get(expressCompany);
				if (count != null) {
					BigDecimal percent = BigDecimal.valueOf(count * 100/(double)totalCount);
					Double dPercent = percent.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					String perString = dPercent.toString() + "%";
					jsonObject.put("expressCompany", expressCompany);
					jsonObject.put("percent", perString);
					jsonObject.put("number", count);
				} else {
					jsonObject.put("expressCompany", expressCompany);
					jsonObject.put("percent", "0");
					jsonObject.put("number", 0);
				}
				jsonList.add(jsonObject);
				dto.setExpressOrder(jsonList);
				dtoList.add(dto);
			}
		}
		
		Collections.sort(dtoList, new Comparator<OrderReportDTO>() {
			@Override
			public int compare(OrderReportDTO o1, OrderReportDTO o2) {
				return (o2.getTime() > o1.getTime() ? 1 : -1);
			}
		});
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, dtoList);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsOrderReportService#getTomorrowOrderReportList(long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg getTomorrowOrderReportList(long beginTime, long endTime, DDBParam param, 
			List<Long> warehouseIdList) {
		int count = omsOrderFormDao.getTotalOrderCountByTime(beginTime, endTime, warehouseIdList);
		if (count <= 0) {
			return null;
		}
		List<TomorrowOrderReportDTO> dtoList = new ArrayList<>();
		List<TomorrowOrderReport> tomorrowOrderReportList = tomorrowOrderReportDao.getOrderReportList(beginTime, endTime, param, warehouseIdList);
		for (TomorrowOrderReport tomorrowOrderReport : tomorrowOrderReportList) {
			TomorrowOrderReportDTO dto = new TomorrowOrderReportDTO();
			dto.setCancelOrderNumber(tomorrowOrderReport.getCancelOrderNumber());
			dto.setCollectiveOrderNumber(tomorrowOrderReport.getCollectiveOrderNumber());
			dto.setTotalOrderNumber(tomorrowOrderReport.getTotalOrderNumber());
			dto.setExpressCompany(tomorrowOrderReport.getExpressCompany());
			dto.setWarehouseName(tomorrowOrderReport.getWarehouse());
			dto.setTime(tomorrowOrderReport.getTime());
			BigDecimal cancelPercent = BigDecimal.valueOf(100 * tomorrowOrderReport.getCancelOrderNumber()
					/(double)tomorrowOrderReport.getTotalOrderNumber());
			BigDecimal collectivePercent = BigDecimal.valueOf(100 * tomorrowOrderReport.getCollectiveOrderNumber()
					/(double)tomorrowOrderReport.getTotalOrderNumber());
			BigDecimal totalPercent = BigDecimal.valueOf(100 * tomorrowOrderReport.getTotalOrderNumber()/(double)count);
			Double dCancelPercent = cancelPercent.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			Double dCollectivePercent = collectivePercent.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			Double dTotalPercent = totalPercent.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			String cancelPercentString = dCancelPercent.toString() + "%";
			String collectivePercentString = dCollectivePercent.toString() + "%";
			String totalPercentString = dTotalPercent.toString() + "%";
			dto.setCancelOrderPercent(cancelPercentString);
			dto.setCollectiveOrderPercent(collectivePercentString);
			dto.setTotalOrderPercent(totalPercentString);
			dtoList.add(dto);
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, dtoList);
		return retArg;
	}

}
