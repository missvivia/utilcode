package com.xyl.mmall.oms.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.facade.OmsReportFacade;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.report.meta.OmsDelayReport;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;

/**
 * 生成 仓库生产统计报表和仓库延迟订单统计报表 job
 * 
 * cron: 0 30 * * * ?
 * 
 * @return
 * @author hzzhanghui
 * 
 */
@Service
@JobPath("/oms/makerreport")
public class OmsMakerReportJob extends BaseJob {
	
	private static final Logger logger = LoggerFactory.getLogger(OmsMakerReportJob.class);
	
	@Resource
	private OmsReportFacade omsReportFacade;

	@Override
	public boolean execute(JobParam param) {
		long startTime = getQueryStartTime().getTimeInMillis();
		long endTime = getQueryEndTime().getTimeInMillis();
		
		// 上期结存=上期的"本期"结存
		// 本期结存=缺货订单+延迟订单，因为目前缺货没法统计，统一为0，所以，本期结存也就等于延迟订单数量
		try {
			// 获取本期要统计的订单列表
			List<OmsOrderForm> orderFormList = omsReportFacade.getOmsOrderFormListByTimeRange(startTime, endTime);
			if (orderFormList != null && orderFormList.size() > 0) {
				// 获取所有的仓库
				Set<Long> warehouseIdList = new TreeSet<Long>();
				for (OmsOrderForm orderForm : orderFormList) {
					warehouseIdList.add(orderForm.getStoreAreaId());
				}
				
				// 循环为每个仓库生成报表
				for (long warehouseId : warehouseIdList) {
					// filter all orders for this warehouse
					int gatherCnt = 0;
					List<OmsOrderForm> wsOrderList = new ArrayList<OmsOrderForm>();
					for (OmsOrderForm orderForm : orderFormList) {
						if (orderForm.getStoreAreaId() == warehouseId
								&& OmsOrderFormState.isCollectWarehouse(orderForm.getOmsOrderFormState())) {
							wsOrderList.add(orderForm);
							gatherCnt++;
						}
					}
					
					// get all previous delay orders
					Calendar c2 = Calendar.getInstance();
					c2.add(Calendar.DAY_OF_YEAR, -2);
					long date = getSpecificBeginTime(c2.getTimeInMillis()).getTimeInMillis();
					OmsMakerReport preReport = omsReportFacade.getMakerReport(warehouseId, date);
					if (preReport != null && preReport.getDelayOrderIds() != null) {
						String[] arr = preReport.getDelayOrderIds().trim().split(",");
						for (String orderId : arr) {
							try {
								OmsOrderForm preDelayOrder = omsReportFacade.getOmsOrderFormByOrderId(Long.parseLong(orderId));
								if (preDelayOrder != null) {
									wsOrderList.add(preDelayOrder);
								}
							} catch (Exception e) {
								continue;
							}
						}
					}
					// for now, we get all data source 'wsOrderList' for this time statistic
					
					
					OmsMakerReport report = new OmsMakerReport();

					// 日期
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_YEAR, -1);
					long makeTime = getSpecificBeginTime(c.getTimeInMillis()).getTimeInMillis();
					report.setMakeTime(makeTime);

					// 仓库id
					report.setWarehouseId(warehouseId);

					WarehouseDTO warehouseDTO = omsReportFacade.getWarehouseById(warehouseId);

					// 仓储服务商
					String warehouseOwner = "";
					String type = warehouseDTO.getType();
					try {
						int typeVal = Integer.parseInt(type);
						warehouseOwner = WarehouseType.NULL.genEnumByIntValue(typeVal).getDesc();
					} catch (Exception e) {
						warehouseOwner = type;
					}
					report.setWarehouseOwner(warehouseOwner);

					// 仓库
					String warehouseName = warehouseDTO.getWarehouseName();
					report.setWarehouseName(warehouseName);

					// 上期结存
					int preBalance = 0;
					if (preReport != null) {
						preBalance = preReport.getCurBalance();
					}
					report.setPreBalance(preBalance);
					
					// 已汇总订单
					report.setGatherCnt(gatherCnt);

					// 延迟订单 = 本期延迟+上期结余延迟
					int delayCnt = 0;
					List<Long> delayIdList = new ArrayList<Long>(); // 记录延迟订单列表
					List<OmsOrderForm> delayOrderList = new ArrayList<OmsOrderForm>();
					for (OmsOrderForm orderForm : wsOrderList) {
						if (OmsOrderFormState.isCollectWarehouse(orderForm.getOmsOrderFormState())
								&& !OmsOrderFormState.isShippedUser(orderForm.getOmsOrderFormState())
								&& !OmsOrderFormState.isPickCancel(orderForm.getOmsOrderFormState())) {
							delayIdList.add(orderForm.getOmsOrderFormId());
							delayOrderList.add(orderForm);
							delayCnt++;
						}
					}
					report.setDelayCnt(delayCnt);
					
					// 取消订单 = 本期取消+上期结余取消的
					int cancelCnt = 0;
					for (OmsOrderForm orderForm : wsOrderList) {
						if (OmsOrderFormState.isPickCancel(orderForm.getOmsOrderFormState())) {
							cancelCnt++;
						}
					}
					report.setCancelCnt(cancelCnt);

					// 已发货订单
					int sentCnt = 0;
					for (OmsOrderForm orderForm : wsOrderList) {
						if (OmsOrderFormState.isShippedUser(orderForm.getOmsOrderFormState())) {
							sentCnt++;
						}
					}
					report.setSentCnt(sentCnt);

					// 缺货订单，暂时不统计
					int lackCnt = 0;
					report.setLackCnt(lackCnt);

					// 本期结存
					int curBalance = lackCnt + delayCnt;
					report.setCurBalance(curBalance);

					// 延迟占比: 延迟/（上期结存+已汇总）
					BigDecimal delayRate = new BigDecimal(0.0);
					try {
						double tmp = delayCnt / (preBalance + gatherCnt);
						delayRate = new BigDecimal(tmp);
					} catch (Exception e) {
						delayRate = new BigDecimal(0.0);
					}
					report.setDelayRate(delayRate);

					// 延迟订单列表
					StringBuilder delayOrderIds = new StringBuilder();
					for (int i = 0; i < delayIdList.size(); i++) {
						if (i != (delayIdList.size() - 1)) {
							delayOrderIds.append(delayIdList.get(i)).append(",");
						} else {
							delayOrderIds.append(delayIdList.get(i));
						}
					}
					report.setDelayOrderIds(delayOrderIds.toString());
					
					// check whether repeated record
					OmsMakerReport dbReport = omsReportFacade.getMakerReport(report.getWarehouseId(), report.getMakeTime());
					if (dbReport != null && report.equals(dbReport)) {
						logger.info("Already existed report for (warehouse=" + warehouseId + ", date=" + date + ")!!!");
					} else {
					omsReportFacade.insertReport(report);
					logger.info("Successfully generate warehouse production report: " + report);
					
					// 生成当前仓库的延迟订单列表
					for (OmsOrderForm order : delayOrderList) {
						OmsDelayReport delayReport = new OmsDelayReport();
						delayReport.setWarehouseId(warehouseId);
						delayReport.setMakeTime(makeTime);
						delayReport.setWarehouseOwner(warehouseOwner);
						delayReport.setWarehouseName(warehouseName);
						delayReport.setOmsOrderId(order.getOmsOrderFormId());
						delayReport.setCreateTime(order.getCreateTime());
						delayReport.setOmsOrderFormState(order.getOmsOrderFormState().getName());
						delayReport.setShipTime(order.getShipTime());
						delayReport.setConsigneeName(order.getConsigneeName());
						delayReport.setConsigneeMobile(order.getConsigneeMobile());
						// 快递单号
						String mailNO = "";
						try {
							List<OmsOrderPackage> packageList = omsReportFacade.
									getOmsOrderPackageListByOmsOrderFormId(order.getOmsOrderFormId(), order.getUserId());
							if (packageList != null && packageList.size() > 0) {
								mailNO = packageList.get(0).getMailNO();
							}
						} catch (Exception e) {
							// ignore
						}
						delayReport.setMailNO(mailNO);
						
							List<OmsDelayReport> dbReportList = omsReportFacade.getDelayReportList(delayReport.getWarehouseId(), delayReport.getMakeTime());
							boolean repeatFlag = false;
							if (dbReportList != null) {
								for (OmsDelayReport tmp : dbReportList) {
									if (delayReport.equals(tmp)) {
										repeatFlag = true;
										break;
									}
								}
							}
							
							if (repeatFlag) {
								logger.info("Already existed report for (warehouse=" + warehouseId + ", date=" + date + ", orderId=" + delayReport.getOmsOrderId() + ")!!!");
							} else {
								omsReportFacade.insertDelayReport(delayReport);
								logger.info("Successfully generate warehouse delayed detail report: " + delayReport);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		
		return true;
	}

	private Calendar getSpecificBeginTime(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	private Calendar getQueryStartTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -3);
		c.set(Calendar.HOUR_OF_DAY, 16);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	private Calendar getQueryEndTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -2);
		c.set(Calendar.HOUR_OF_DAY, 15);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		return c;
	}

}
