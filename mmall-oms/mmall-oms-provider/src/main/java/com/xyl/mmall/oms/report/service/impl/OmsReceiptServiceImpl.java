package com.xyl.mmall.oms.report.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.dao.WarehouseDao;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.report.dao.OmsReceiptDailyDao;
import com.xyl.mmall.oms.report.dao.ReceiptDao;
import com.xyl.mmall.oms.report.dao.SendOutProvinceFormDao;
import com.xyl.mmall.oms.report.meta.OmsReceiptDaily;
import com.xyl.mmall.oms.report.meta.OmsReceiptReport;
import com.xyl.mmall.oms.report.service.OmsReceiptService;

@Service("omsReceiptService")
public class OmsReceiptServiceImpl implements OmsReceiptService {

	private static Logger LOGGER = LoggerFactory.getLogger(OmsReceiptServiceImpl.class);
	
	@Autowired
	private OmsReceiptDailyDao omsReceiptDailyDao;
	
	@Autowired
	private ReceiptDao receiptDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private SendOutProvinceFormDao sendOutProvinceFormDao; 
	
	@Override
	public OmsReceiptReport addObject(OmsReceiptReport obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OmsReceiptReport> getOmsRecepitReportByDuration(long startTime, long endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 构建OmsReceiptReport表的数据
	 * @param day
	 * @return
	 */
	@Override
	public List<OmsReceiptReport> getReceiptReportByDay(long day){
		LOGGER.info("getReceiptReportByDay begin");
		List<OmsReceiptDaily> list = omsReceiptDailyDao.getListByDate(day);
		List<OmsReceiptReport> reportList = null;
		long currTime = new Date().getTime();
		if(list!=null&&list.size()>0){
			reportList = new ArrayList<OmsReceiptReport>();
			//构建仓库与24,48,72,》72数据对
			Map<Long,List<OmsReceiptDaily>> map = new HashMap<Long,List<OmsReceiptDaily>>();
			for(OmsReceiptDaily omsReceiptDaily:list){
				if(map.get(omsReceiptDaily.getWarehouseId())!=null){
					List<OmsReceiptDaily> result = map.get(omsReceiptDaily.getWarehouseId());
					result.add(omsReceiptDaily);
					map.put(omsReceiptDaily.getWarehouseId(), result);
				}else{
					List<OmsReceiptDaily> result = new ArrayList<OmsReceiptDaily>();
					result.add(omsReceiptDaily);
					map.put(omsReceiptDaily.getWarehouseId(), result);
				}
			}
			Iterator<Long> it = map.keySet().iterator();
			while(it.hasNext()){
				Long warehouseId = it.next();
				List<OmsReceiptDaily> result = map.get(warehouseId);
				OmsReceiptReport receiptReport = new OmsReceiptReport();
				receiptReport.setWarehouseId(warehouseId);
				receiptReport.setDate(day);
				receiptReport.setCreateTime(currTime);
				receiptReport.setUpdateTime(currTime);
				WarehouseForm warehouseFrom = warehouseDao.getWarehouseById(warehouseId);
				//仓库
				if(warehouseFrom!=null){
					receiptReport.setExpress(warehouseFrom.getExpressCompany());
					receiptReport.setWarehouse(warehouseFrom.getWarehouseName());
				}
				//发货量
				int total = sendOutProvinceFormDao.getSendOutByWarehouseId(warehouseId, day);
				receiptReport.setTotal(total);
				for(OmsReceiptDaily daily:result){
					if(daily.getType()==24){
						receiptReport.setTotal_24(daily.getTotal());
						if(total!=0)
							receiptReport.setRate_24(new BigDecimal(1.0*daily.getTotal()/total));
						else
							receiptReport.setRate_24(BigDecimal.ZERO);
					}else if(daily.getType()==48){
						receiptReport.setTotal_48(daily.getTotal());
						if(total!=0)
							receiptReport.setRate_48(new BigDecimal(1.0*daily.getTotal()/total));
						else
							receiptReport.setRate_48(BigDecimal.ZERO);
					}else if(daily.getType()==72){
						receiptReport.setTotal_72(daily.getTotal());
						if(total!=0)
							receiptReport.setRate_72(new BigDecimal(1.0*daily.getTotal()/total));
						else
							receiptReport.setRate_72(BigDecimal.ZERO);
					}
				}
				receiptReport.setNo_receipt(total-receiptReport.getTotal_24()-receiptReport.getTotal_48()-receiptReport.getTotal_72());
				if(total!=0)
					receiptReport.setReturnOrder(new BigDecimal(1.0*receiptReport.getNo_receipt()/total));
				else
					receiptReport.setReturnOrder(BigDecimal.ZERO);
				reportList.add(receiptReport);
			}
			
		}
		return reportList;
	}
	
	@Override
	public boolean saveDataByDay(long date) {
		boolean succ = true;
		long currTime = new Date().getTime();
		//1.昨天发货的数据的24小时内签收情况
		List<OmsReceiptDaily> list = omsReceiptDailyDao.getReceiptReportDaily(date, 0, 24, 24);
		if(list!=null && list.size()>0){
			for(OmsReceiptDaily omsReceiptDaily:list){
				OmsReceiptDaily obj = omsReceiptDailyDao.getReceiptDailyByWarehouseAnd(omsReceiptDaily.getWarehouseId(), omsReceiptDaily.getDate(), 24);
				if(obj!=null){
					obj.setNo_receipt(omsReceiptDaily.getNo_receipt());
					obj.setTotal(omsReceiptDaily.getTotal());
					obj.setUpdateTime(currTime);
					omsReceiptDailyDao.updateObjectByKey(obj);
				}else{
					OmsReceiptDaily result = omsReceiptDailyDao.saveObject(omsReceiptDaily);
					if(result == null){
						LOGGER.error("save data into Mmall_Oms_Report_ReceiptForm failed,warehouseId="+omsReceiptDaily.getWarehouseId() + ",date="+omsReceiptDaily.getDate());
						succ = false;
					}
				}
			}
		}
		//2.前天发货的数据48小时内的签收情况
		list = omsReceiptDailyDao.getReceiptReportDaily(date-24*3600*1000, 24, 48, 48);
		if(list!=null && list.size()>0){
			for(OmsReceiptDaily omsReceiptDaily:list){
				OmsReceiptDaily obj = omsReceiptDailyDao.getReceiptDailyByWarehouseAnd(omsReceiptDaily.getWarehouseId(), omsReceiptDaily.getDate(), 48);
				if(obj!=null){
					obj.setNo_receipt(omsReceiptDaily.getNo_receipt());
					obj.setTotal(omsReceiptDaily.getTotal());
					obj.setUpdateTime(currTime);
					omsReceiptDailyDao.updateObjectByKey(obj);
				}else{
					OmsReceiptDaily result = omsReceiptDailyDao.saveObject(omsReceiptDaily);
					if(result == null)
						LOGGER.error("save data into Mmall_Oms_Report_ReceiptForm failed,warehouseId="+omsReceiptDaily.getWarehouseId() + ",date="+omsReceiptDaily.getDate());
				}
			}
		}
		//3.大前天发货的数据72小时内的签收情况
		list = omsReceiptDailyDao.getReceiptReportDaily(date-2*24*3600*1000, 48, 72, 72);
		if(list!=null && list.size()>0){
			for(OmsReceiptDaily omsReceiptDaily:list){
				OmsReceiptDaily obj = omsReceiptDailyDao.getReceiptDailyByWarehouseAnd(omsReceiptDaily.getWarehouseId(), omsReceiptDaily.getDate(), 72);
				if(obj!=null){
					obj.setNo_receipt(omsReceiptDaily.getNo_receipt());
					obj.setTotal(omsReceiptDaily.getTotal());
					obj.setUpdateTime(currTime);
					omsReceiptDailyDao.updateObjectByKey(obj);
				}else{
					OmsReceiptDaily result = omsReceiptDailyDao.saveObject(omsReceiptDaily);
					if(result == null)
						LOGGER.error("save data into Mmall_Oms_Report_ReceiptForm failed,warehouseId="+omsReceiptDaily.getWarehouseId() + ",date="+omsReceiptDaily.getDate());
				}
			}
		}
		//4.7前天发货的数据超过72小时的签收情况
		list = omsReceiptDailyDao.getReceiptReportDaily(date-6*24*3600*1000, 72, 132, 96);
		if(list!=null && list.size()>0){
			for(OmsReceiptDaily omsReceiptDaily:list){
				OmsReceiptDaily obj = omsReceiptDailyDao.getReceiptDailyByWarehouseAnd(omsReceiptDaily.getWarehouseId(), omsReceiptDaily.getDate(), 96);
				if(obj!=null){
					obj.setNo_receipt(omsReceiptDaily.getNo_receipt());
					obj.setTotal(omsReceiptDaily.getTotal());
					obj.setUpdateTime(currTime);
					omsReceiptDailyDao.updateObjectByKey(obj);
				}else{
					OmsReceiptDaily result = omsReceiptDailyDao.saveObject(omsReceiptDaily);
					if(result == null)
						LOGGER.error("save data into Mmall_Oms_Report_ReceiptForm failed,warehouseId="+omsReceiptDaily.getWarehouseId() + ",date="+omsReceiptDaily.getDate());
				}
			}
		}
		return succ;
	}

	@Override
	public boolean saveReportData(List<OmsReceiptReport> list) {
		LOGGER.info("saveReceiptForm");
		boolean succ = true;
		long currTime = new Date().getTime();
		if(list!=null){
			for(OmsReceiptReport report:list){
				OmsReceiptReport result = receiptDao.getOmsReceiptDailyByWarehouseIdAndDay(report.getWarehouseId(),report.getDate());
				if(result==null){
					OmsReceiptReport obj = receiptDao.addObject(report);
					if(obj==null){
						LOGGER.error("save data into Mmall_Oms_Report_ReceiptForm failed,warehouseId="+report.getWarehouseId() + ",date="+report.getDate());
						succ = false;
					}
				}else{
					result.setExpress(report.getExpress());
					result.setNo_receipt(report.getNo_receipt());
					result.setRate_24(report.getRate_24());
					result.setRate_48(report.getRate_48());
					result.setRate_72(report.getRate_72());
					result.setRate_after_72(report.getRate_after_72());
					result.setTotal(report.getTotal());
					result.setTotal_24(report.getTotal_24());
					result.setTotal_48(report.getTotal_48());
					result.setTotal_72(report.getTotal_72());
					result.setTotal_after_72(report.getTotal_after_72());
					result.setUpdateTime(currTime);
					result.setWarehouse(report.getWarehouse());
					result.setReturnOrder(report.getReturnOrder());
					if(!receiptDao.updateObjectByKey(result)){
						LOGGER.error("update data into Mmall_Oms_Report_ReceiptForm failed,warehouseId="+report.getWarehouseId() + ",date="+report.getDate());
						succ = false;
					}
				}
			}
		}
		return succ;
	}

	@Override
	public boolean saveReportDataByDay(long date) {
		boolean succ = false;
		//1.存入24h内发货的数据
		List<OmsReceiptReport> list = this.getReceiptReportByDay(date);
		succ = saveReportData(list);
		
		
		//2.存入48h内发货数据
		list = this.getReceiptReportByDay(date-24*3600*1000);
		succ = succ && saveReportData(list);
		
		
		//3.存入72h内发货数据
		list = this.getReceiptReportByDay(date-2*24*3600*1000);
		succ = succ && saveReportData(list);
		
		
		//4.存入大于72小时发货数据
		list = this.getReceiptReportByDay(date-6*24*3600*1000);
		succ = succ && saveReportData(list);
		
		
		return succ;
	}

	@Override
	public List<OmsReceiptReport> queryReceiptReportByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds,DDBParam param) {
		return receiptDao.getReceiptReportByDate(warehouseId, startDay, endDay,warehouseIds, param);
	}

}
