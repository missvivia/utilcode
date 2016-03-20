package com.xyl.mmall.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.exceljar.ExcelExportUtil;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.facade.OmsFacade;
import com.xyl.mmall.backend.util.DateUtils;
import com.xyl.mmall.backend.vo.FreightCodVO;
import com.xyl.mmall.backend.vo.FreightReverseVO;
import com.xyl.mmall.backend.vo.FreightUserReturnVO;
import com.xyl.mmall.backend.vo.FreightVO;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.backend.vo.PoReturnQueryParamVO;
import com.xyl.mmall.backend.vo.ScheduleVo;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.oms.dto.PageableList;

/**
 * @author hzzengchengyuan
 * 
 */
@Controller
@RequestMapping("/oms")
public class OmsController {

	private static Logger LOGGER = LoggerFactory.getLogger(OmsController.class);

	public static final int RESPONSE_STATUS_200 = 200;

	public static final int RESPONSE_STATUS_201 = 201;

	@Autowired
	private OmsFacade facede;

	@Autowired
	private JITSupplyManagerFacade jITSupplyManagerFacade;

	@Autowired
	private AgentService agentService;

	@RequestMapping(value = "/wms/{warehouse}")
	@ResponseBody
	public Object processRpc(@PathVariable("warehouse") String warehouse, @RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		return facede.httpWarehouseCaller(warehouse, params);
	}

	@RequestMapping(value = "/freight/exportReceive")
	public void exportFreightReceive(String expressCompany, long warehouseId, long startDate, long endDate,
			HttpServletResponse response) {
		try {
			List<FreightCodVO> result = jITSupplyManagerFacade.queryFreightCod(expressCompany, warehouseId, startDate,
					endDate);
			String warehouseName = "";
			if (result != null && result.size() > 0) {
				warehouseName = "_" + result.get(0).getWarehouseName();
			}
			String dateRang = "_".concat(DateUtils.format6.format(DateUtils.parseToDate(startDate))).concat("_")
					.concat(DateUtils.format6.format(DateUtils.parseToDate(endDate)));

			String excelName = "应收COD_".concat(ExpressCompany.genEnumNameIgnoreCase(expressCompany).getName())
					.concat(warehouseName).concat(dateRang);
			response.reset();
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.setHeader("Connection", "keep-alive");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(excelName, "UTF-8") + ".xlsx");

			LinkedHashMap<String, String> ruleMap = new LinkedHashMap<String, String>();
			ruleMap.put("indexNo", "01-序号");
			ruleMap.put("settleExpressCompany", "02-快递公司");
			ruleMap.put("warehouseName", "03-省份/直辖市/仓库");
			ruleMap.put("omsOrderFormId", "04-OMS订单号");
			ruleMap.put("userOrderFormId", "05-用户订单号");
			ruleMap.put("expressNo", "06-运单号");
			ruleMap.put("codAmount", "07-货到付款金额");
			ruleMap.put("shipTimeFormat", "08-发货日期");
			ruleMap.put("packageStateDesc", "09-快递状态");
			ruleMap.put("isCodDesc", "10-是否COD");
			ruleMap.put("isInsuranceDesc", "11-是否保价");
			ruleMap.put("insuranceRateFormat", "12-保价费率");
			ruleMap.put("insuranceCharge", "13-保价费");
			ruleMap.put("deliverType", "14-配送类型");
			ruleMap.put("weightKg", "15-重量");
			ruleMap.put("startCost", "16-首重费用");
			ruleMap.put("continueCost", "17-续重费用");
			ruleMap.put("codRateFormat", "18-手续费率");
			ruleMap.put("codCharge", "19-手续费");
			ruleMap.put("reverseRateFormat", "20-反向快递费率");
			ruleMap.put("educationCharge", "21-偏远服务费");
			ruleMap.put("reverseCharge", "22-反向快递费");
			ruleMap.put("codCollection", "23-COD收款");
			//增加签收时间/反仓时间，网易接收时间
			ruleMap.put("stateUpdateTimeFormat", "24-签收时间/反仓时间");
			MyExcelExportUtil util = new MyExcelExportUtil();
			util.initExcelExportUtil(excelName, ruleMap, FreightCodVO.class);
			if (result != null) {
				// 设置序号
				for (int i = 0; i < result.size(); i++) {
					FreightCodVO fv = result.get(i);
					fv.setIndexNo(i + 1);
				}
				util.write(result);
			}
			util.pushTo(response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	@RequestMapping(value = "/freight/exportPay")
	public void exportFreightPay(String expressCompany, long warehouseId, long startDate, long endDate,
			HttpServletResponse response) {
		try {
			List<FreightVO> result1 = jITSupplyManagerFacade.queryFreight(expressCompany, warehouseId, startDate,
					endDate);
			List<FreightReverseVO> result2 = jITSupplyManagerFacade.queryFreightReverse(expressCompany, warehouseId,
					startDate, endDate);
			List<FreightUserReturnVO> result3 = jITSupplyManagerFacade.queryFreightUserReturn(expressCompany,
					warehouseId, startDate, endDate);
			String warehouseName = "";
			if (result1 != null && result1.size() > 0) {
				warehouseName = "_" + result1.get(0).getWarehouseName();
			}
			String dateRang = "_".concat(DateUtils.format6.format(DateUtils.parseToDate(startDate))).concat("_")
					.concat(DateUtils.format6.format(DateUtils.parseToDate(endDate)));

			String excelName = "应付物流费_".concat(ExpressCompany.genEnumNameIgnoreCase(expressCompany).getName())
					.concat(warehouseName).concat(dateRang);
			response.reset();
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.setHeader("Connection", "keep-alive");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(excelName, "UTF-8") + ".xlsx");
			MyExcelExportUtil util = new MyExcelExportUtil();
			if (result1 != null) {
				LinkedHashMap<String, String> ruleMap = new LinkedHashMap<String, String>();
				ruleMap.put("indexNo", "01-序号");
				ruleMap.put("settleExpressCompany", "02-快递公司");
				ruleMap.put("warehouseName", "03-省份/直辖市/仓库");
				ruleMap.put("shipTimeFormat", "04-发货日期");
				ruleMap.put("userOrderFormId", "05-用户订单号");
				ruleMap.put("omsOrderFormId", "06-OMS订单号");
				ruleMap.put("orderAmount", "07-订单金额");
				ruleMap.put("codAmount", "08-货到付款金额");
				ruleMap.put("deliverAddress", "09-订单的配送地址");
				ruleMap.put("deliverType", "10-配送类型");
				ruleMap.put("expressNo", "11-运单号");
				ruleMap.put("isCodDesc", "12-是否COD");
				ruleMap.put("packageStateDesc", "13-快递状态");
				ruleMap.put("pickPackageCost", "14-分拣及包装费");
				ruleMap.put("consumablesCost", "15-包装耗材费");
				ruleMap.put("interceptOrderCost", "16-拦截订单费");
				ruleMap.put("weightKg", "17-包裹重量");
				ruleMap.put("startCost", "18-首重费用");
				ruleMap.put("continueCost", "19-续重费用");
				ruleMap.put("codRateFormat", "20-COD费率");
				ruleMap.put("codCharge", "21-COD手续费");
				ruleMap.put("insuranceRateFormat", "22-正向保价率");
				ruleMap.put("insuranceCharge", "23-正向保价费");
				ruleMap.put("expressCharge", "24-正向快递费");
				ruleMap.put("warehouseInsideCharge", "26-仓内费用合计");
				ruleMap.put("isInsuranceDesc", "27-是否保价");
				ruleMap.put("isOnePiceDesc", "28-是否一口价");
				ruleMap.put("onePrice", "29-一口价");
				ruleMap.put("lostCompensate", "30-丢件赔偿");
				util.initExcelExportUtil("正向应付物流费", ruleMap, FreightVO.class);
				// 设置序号
				for (int i = 0; i < result1.size(); i++) {
					FreightVO fv = result1.get(i);
					fv.setIndexNo(i + 1);
				}
				util.write("正向应付物流费", result1);
			}
			if (result2 != null) {
				LinkedHashMap<String, String> ruleMap = new LinkedHashMap<String, String>();
				ruleMap.put("indexNo", "01-序号");
				ruleMap.put("settleExpressCompany", "02-快递公司");
				ruleMap.put("warehouseName", "03-省份/直辖市/仓库");
				ruleMap.put("omsOrderFormId", "04-OMS订单号");
				ruleMap.put("userOrderFormId", "05-用户订单号");
				ruleMap.put("expressNo", "06-正向运单号");
				ruleMap.put("returnExpressNo", "07-反向运单号");
				ruleMap.put("orderAmount", "08-订单金额");
				ruleMap.put("codAmount", "09-货到付款金额");
				ruleMap.put("shipTimeFormat", "10-发货日期");
				ruleMap.put("deliverAddress", "11-订单地址");
				ruleMap.put("deliverType", "12-配送类型");
				ruleMap.put("isCodDesc", "13-是否COD");
				ruleMap.put("packageStateDesc", "14-快递状态");
				ruleMap.put("reverseInsuranceCharge", "15-反向保价费");
				ruleMap.put("reverseRateFormat", "16-反向快递费率");
				ruleMap.put("reverseCharge", "17-反向快递费");
				ruleMap.put("reverseTotalCharge", "18-反向运费小计");
				ruleMap.put("reverseServiceCharge", "19-返货处理费");
				ruleMap.put("stateUpdateTimeFormat", "20-退货入库时间");
				util.initExcelExportUtil("反向应付物流费", ruleMap, FreightReverseVO.class);
				// 设置序号
				for (int i = 0; i < result2.size(); i++) {
					FreightReverseVO fv = result2.get(i);
					fv.setIndexNo(i + 1);
				}
				util.write("反向应付物流费", result2);
			}
			if (result3 != null) {
				LinkedHashMap<String, String> ruleMap = new LinkedHashMap<String, String>();
				ruleMap.put("indexNo", "序号");
				ruleMap.put("warehouseName", "省份/直辖市/仓库");
				ruleMap.put("wmsReceivedTimeFormat", "仓库收货时间");
				ruleMap.put("userOrderFormId", "用户订单号");
				ruleMap.put("omsOrderFormId", "OMS订单号");
				ruleMap.put("expressNo", "原运单号");
				ruleMap.put("returnExpressNo", "退回运单号");
				ruleMap.put("returnAddress", "退回地址");
				ruleMap.put("returnServiceCharge", "客户退货处理费");
				util.initExcelExportUtil("顾客退货应付物流费", ruleMap, FreightUserReturnVO.class);
				// 设置序号
				for (int i = 0; i < result3.size(); i++) {
					FreightUserReturnVO fv = result3.get(i);
					fv.setIndexNo(i + 1);
				}
				util.write("顾客退货应付物流费", result3);
			}
			util.pushTo(response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * 未确认的退货单列表查询
	 * 
	 * @param returnParams
	 * @return
	 */
	@RequestMapping(value = "/return/list")
	@ResponseBody
	public JSONObject returnList(@RequestBody PoReturnQueryParamVO returnParams) {
		try {
			PageableList<ScheduleVo> scheduleViceVos = jITSupplyManagerFacade.queryScheduleVo(returnParams);
			return createOKResponse(scheduleViceVos);
		} catch (Exception e) {
			return create201Response(e);
		}
	}

	@RequestMapping(value = "/returnOrder/create/{poId}")
	@ResponseBody
	public JSONObject returnOrderCreate(@PathVariable long poId) {
		try {
			return createOKResponse(jITSupplyManagerFacade.ceatePoReturnOrderByPoId(poId));
		} catch (Exception e) {
			return create201Response(e);
		}
	}

	@RequestMapping(value = "/returnOrder/list")
	@ResponseBody
	public JSONObject returnOrderList(@RequestBody PoReturnQueryParamVO returnParams) {
		try {
			PageableList<PoReturnOrderVO> returnOrders = jITSupplyManagerFacade.queryPoReturnOrder(returnParams);
			// 重新设置状态描述
			if (returnOrders != null && returnOrders.getList() != null) {
				for (PoReturnOrderVO vo : returnOrders.getList()) {
					vo.toCmsStateDesc();
				}
			}
			return createOKResponse(returnOrders);
		} catch (Exception e) {
			return create201Response(e);
		}
	}

	private JSONObject createOKResponse(Object data) {
		JSONObject result = new JSONObject();
		result.put("code", RESPONSE_STATUS_200);
		if (data != null) {
			result.put("result", data);
		} else {
			result.put("result", NullResult.NULL);
		}
		return result;
	}

	private JSONObject create201Response(Exception exception) {
		return create201Response(exception, null);
	}

	private JSONObject create201Response(Exception exception, Object data) {
		JSONObject result = new JSONObject();
		result.put("code", RESPONSE_STATUS_201);
		if (data != null) {
			result.put("result", data);
		} else {
			result.put("result", NullResult.NULL);
		}
		if (exception != null) {
			LOGGER.error("", exception);
		}
		return result;
	}

}

class MyExcelExportUtil extends ExcelExportUtil {
	private Workbook myWb;

	@Override
	@SuppressWarnings("rawtypes")
	public void initExcelExportUtil(String sheetName, LinkedHashMap<String, String> ruleMap, Class clazz, boolean isXlsx) {
		if (myWb == null)
			myWb = isXlsx ? new SXSSFWorkbook(500) : new HSSFWorkbook();
		super.setWb(myWb);
		super.initExcelExportUtil(sheetName, ruleMap, clazz, isXlsx);
	}

	public boolean pushTo(OutputStream stream) {
		try {
			myWb.write(stream);
			stream.flush();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}

class NullResult {
	static NullResult NULL = new NullResult();

	private int limit, offset, total, size;

	@SuppressWarnings("rawtypes")
	private List<?> list = new ArrayList();

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}
