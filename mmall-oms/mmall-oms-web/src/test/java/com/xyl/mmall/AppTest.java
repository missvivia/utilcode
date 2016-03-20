package com.xyl.mmall;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.OmsConfig;
import com.xyl.mmall.oms.dao.ExpressFeeDao;
import com.xyl.mmall.oms.dto.OmsReportListDTO;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.report.meta.OmsDelayReport;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;
import com.xyl.mmall.oms.report.service.OmsMakerReportService;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.OmsOrderPackageService;
import com.xyl.mmall.oms.service.WarehouseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {OmsConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("dev")
@EnableAutoConfiguration
public class AppTest {

	static {
		System.setProperty("spring.profiles.active", "dev");
	}

	@Autowired
	private ExpressFeeDao expressFeeDao;
	
	@Test
	public void testByZhanghui() {
		//testJob();
		testMakerReport();
		testDelayReport();
	}
	
	private void testDelayReport() {
		JSONObject result = new JSONObject();
		result.put("result", true);
		result.put("code", 200);

		JSONArray list = new JSONArray();
		result.put("list", list);

		OmsReportListDTO dto = omsMakerReportService.getDelayReportList(11, 1410300800000L, 1430300800000L, 0, 10);
		if (dto.getList() == null || dto.getList().size() == 0) {
			result.put("total", 0);
			result.put("hasNext", false);
			System.out.println("total: " + result.getIntValue("total"));
			System.out.println("list: " + result.getJSONArray("list"));
			return;
		}

		List<OmsDelayReport> reportList = (List<OmsDelayReport>) dto.getList();
		result.put("total", dto.getTotal());
		result.put("hasNext", dto.isHasNext());

		for (OmsDelayReport report : reportList) {
			JSONObject json = new JSONObject();
			json.put("id", report.getId() + "");
			json.put("warehouseId", report.getWarehouseId() + "");
			json.put("makeTime", report.getMakeTime());
			json.put("warehouseOwner", report.getWarehouseOwner());
			json.put("warehouseName", report.getWarehouseName());
			json.put("omsOrderId", report.getOmsOrderId());
			json.put("createTime", report.getCreateTime());
			json.put("omsOrderFormState", report.getOmsOrderFormState());
			json.put("shipTime", report.getShipTime());
			json.put("consigneeName", report.getConsigneeName());
			json.put("consigneeMobile", report.getConsigneeMobile());
			json.put("mailNO", report.getMailNO());
			list.add(json);
		}

		System.out.println("total: " + result.getIntValue("total"));
		System.out.println("list: " + result.getJSONArray("list"));
	}
	
	
	
	private void testMakerReport() {
		JSONObject result = new JSONObject();
		result.put("result", true);
		result.put("code", 200);

		JSONArray list = new JSONArray();
		result.put("list", list);

		OmsReportListDTO dto = omsMakerReportService.getReportList(11, 1410300800000L, 1430300800000L, 0, 10);
		if (dto.getList() == null || dto.getList().size() == 0) {
			result.put("total", 0);
			result.put("hasNext", false);
			System.out.println("total: " + result.getIntValue("total"));
			System.out.println("list: " + result.getJSONArray("list"));
			return;
		}

		List<OmsMakerReport> reportList = (List<OmsMakerReport>) dto.getList();
		result.put("total", dto.getTotal());
		result.put("hasNext", dto.isHasNext());

		for (OmsMakerReport report : reportList) {
			JSONObject json = new JSONObject();
			json.put("id", report.getId() + "");
			json.put("warehouseId", report.getWarehouseId() + "");
			json.put("makeTime", report.getMakeTime());
			json.put("warehouseOwner", report.getWarehouseOwner());
			json.put("warehouseName", report.getWarehouseName());
			json.put("preBalance", report.getPreBalance());
			json.put("gatherCnt", report.getGatherCnt());
			json.put("cancelCnt", report.getCancelCnt());
			json.put("sentCnt", report.getSentCnt());
			json.put("lackCnt", report.getLackCnt());
			json.put("delayCnt", report.getDelayCnt());
			json.put("curBalance", report.getCurBalance());
			json.put("delayRate", report.getDelayRate());
			list.add(json);
		}
		
		System.out.println("total: " + result.getIntValue("total"));
		System.out.println("list: " + result.getJSONArray("list"));

	}
	
	@Test
	public void testSearchExpressFee() {
//		ExpressFeeSearchParam param=new ExpressFeeSearchParam();
//		param.setSiteId(33L);
//		List<ExpressFee> expressFeeList=expressFeeDao.searchExpressFeeByParam(param);
//		
//		Assert.assertNotNull(expressFeeList);
//		
//		List<ExpressFee> expressFeeList2=expressFeeDao.searchExpressFeeByParam(param);
//		
//		Assert.assertNotNull(expressFeeList2);
		
	}
	
	///////////////////////////////////
	// by hzzhanghui
	//////////////////////////////////
	private static final Logger logger = LoggerFactory.getLogger(AppTest.class);
	
	@Resource
	private OmsOrderFormService omsOrderFormService;
	
	@Resource
	private WarehouseService warehouseService;
	
	@Resource
	private OmsMakerReportService omsMakerReportService;
	
	@Resource
	private OmsOrderPackageService omsOrderPackageService;
	
	
	
	private void testJob() {
		long startTime = getQueryStartTime().getTimeInMillis();
		long endTime = getQueryEndTime().getTimeInMillis();
		
		
		
		try {
			
			List<OmsOrderForm> orderFormList = omsOrderFormService.getOmsOrderFormListByTimeRange(startTime, endTime);
			if (orderFormList != null && orderFormList.size() > 0) {
				
				//List<Long> warehouseIdList = new ArrayList<Long>();
				Set<Long> warehouseIdList = new TreeSet<Long>();
				for (OmsOrderForm orderForm : orderFormList) {
					warehouseIdList.add(orderForm.getStoreAreaId());
				}

				
				for (long warehouseId : warehouseIdList) {
					OmsMakerReport report = new OmsMakerReport();

					
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_YEAR, -1);
					long makeTime = getSpecificBeginTime(c.getTimeInMillis()).getTimeInMillis();
					report.setMakeTime(makeTime);

					
					report.setWarehouseId(warehouseId);

					WarehouseDTO warehouseDTO = warehouseService.getWarehouseById(warehouseId);

				
					String warehouseOwner = "";
					String type = warehouseDTO.getType();
					try {
						int typeVal = Integer.parseInt(type);
						warehouseOwner = WarehouseType.NULL.genEnumByIntValue(typeVal).getDesc();
					} catch (Exception e) {
						warehouseOwner = type;
					}
					report.setWarehouseOwner(warehouseOwner);

					
					String warehouseName = warehouseDTO.getWarehouseName();
					report.setWarehouseName(warehouseName);

					
					int preBalance = 0;
					Calendar c2 = Calendar.getInstance();
					c2.add(Calendar.DAY_OF_YEAR, -2);
					long date = getSpecificBeginTime(c2.getTimeInMillis()).getTimeInMillis();
					OmsMakerReport preReport = omsMakerReportService.getReportList(warehouseId, date);
					if (preReport != null) {
						preBalance = preReport.getCurBalance();
					}
					report.setPreBalance(preBalance);

					
					int gatherCnt = 0;
					for (OmsOrderForm orderForm : orderFormList) {
						if (OmsOrderFormState.isCollectWarehouse(orderForm.getOmsOrderFormState())) {
							gatherCnt++;
						}
					}
					report.setGatherCnt(gatherCnt);

					
					int delayCnt = 0;
					List<Long> delayIdList = new ArrayList<Long>(); 
					List<OmsOrderForm> delayOrderList = new ArrayList<OmsOrderForm>();
					for (OmsOrderForm orderForm : orderFormList) {
						if (!OmsOrderFormState.isShippedUser(orderForm.getOmsOrderFormState())) {
							delayIdList.add(orderForm.getOmsOrderFormId());
							delayOrderList.add(orderForm);
							delayCnt++;
						}
					}
					
					
					int cancelCnt = 0;
					for (OmsOrderForm orderForm : orderFormList) {
						if (OmsOrderFormState.isPickCancel(orderForm.getOmsOrderFormState())) {
							cancelCnt++;
						}
					}
					if (preReport != null) {
						String preDelayOrderIds = preReport.getDelayOrderIds();//
						if (preDelayOrderIds != null && preDelayOrderIds.length() > 0) {
							String[] preDelayOrderIdArr = preDelayOrderIds.split(",");
							for (String orderId : preDelayOrderIdArr) {
								try {
									OmsOrderForm order = omsOrderFormService.getOmsOrderFormByOrderId(Long.parseLong(orderId));
									if (OmsOrderFormState.isPickCancel(order.getOmsOrderFormState())) {
										cancelCnt++;
									}
									if (!OmsOrderFormState.isShippedUser(order.getOmsOrderFormState())) {
										delayIdList.add(order.getOmsOrderFormId());
										delayOrderList.add(order);
										delayCnt++;
									}
								} catch (Exception e) {
									// ignore
								}
							}
						}
					}
					
					report.setCancelCnt(cancelCnt);
					report.setDelayCnt(delayCnt);

					//
					int sentCnt = 0;
					for (OmsOrderForm orderForm : orderFormList) {
						if (OmsOrderFormState.isShippedUser(orderForm.getOmsOrderFormState())) {
							sentCnt++;
						}
					}
					report.setSentCnt(sentCnt);

					//
					int lackCnt = 0;
					report.setLackCnt(lackCnt);

					//
					int curBalance = lackCnt + delayCnt;
					report.setCurBalance(curBalance);

					//
					BigDecimal delayRate = new BigDecimal(0.0);
					try {
						double tmp = delayCnt / (preBalance + gatherCnt);
						delayRate = new BigDecimal(tmp);
					} catch (Exception e) {
						delayRate = new BigDecimal(0.0);
					}
					report.setDelayRate(delayRate);

					//
					StringBuilder delayOrderIds = new StringBuilder();
					for (int i = 0; i < delayIdList.size(); i++) {
						if (i != (delayIdList.size() - 1)) {
							delayOrderIds.append(delayIdList.get(i)).append(",");
						} else {
							delayOrderIds.append(delayIdList.get(i));
						}
					}
					report.setDelayOrderIds(delayOrderIds.toString());

					omsMakerReportService.insert(report);
					logger.info("Successfully generate warehouse production report: " + report);
					
					//
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
						
						String mailNO = "";
						try {
							List<OmsOrderPackage> packageList = omsOrderPackageService.
									getOmsOrderPackageListByOmsOrderFormId(order.getOmsOrderFormId(), order.getUserId());
							if (packageList != null && packageList.size() > 0) {
								mailNO = packageList.get(0).getMailNO();
							}
						} catch (Exception e) {
							// ignore
						}
						delayReport.setMailNO(mailNO);
						
						omsMakerReportService.insertDelayReport(delayReport);
						logger.info("Successfully generate warehouse delayed detail report: " + delayReport);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
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
		c.add(Calendar.DAY_OF_YEAR, -5);
		c.set(Calendar.HOUR_OF_DAY, 16);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	private Calendar getQueryEndTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -4);
		c.set(Calendar.HOUR_OF_DAY, 15);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		return c;
	}
}
