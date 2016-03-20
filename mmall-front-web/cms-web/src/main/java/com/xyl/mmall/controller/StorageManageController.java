package com.xyl.mmall.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.constant.CalendarConst;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.OmsReportFacade;
import com.xyl.mmall.framework.util.FrameworkExcelUtil;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.oms.dto.OrderFormOMSDTO;
import com.xyl.mmall.oms.dto.OrderReportDTO;
import com.xyl.mmall.oms.dto.RejectPackageDTO;
import com.xyl.mmall.oms.dto.ReturnOrderFormDTO;
import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.dto.TomorrowOrderReportDTO;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;
import com.xyl.mmall.oms.report.meta.OmsNoReturnReport;
import com.xyl.mmall.oms.report.meta.OmsReceiptReport;
import com.xyl.mmall.oms.report.meta.OmsReturnReport;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;
import com.xyl.mmall.oms.report.meta.OmsShipOutReport;
import com.xyl.mmall.oms.service.WarehouseService;

/**
 * 仓储管理
 * 
 * @author zzj
 *
 */
@Controller
@RequestMapping("/storage")
public class StorageManageController extends BaseController {

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private OmsReportFacade omsReportFacade;

	@Autowired
	private AgentService agentService;

	@Autowired
	private JITSupplyManagerFacade facade;

	@Autowired
	private WarehouseService warehouseService;

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exports", method = RequestMethod.GET)
	@RequiresPermissions(value = { "storage:exports" })
	public String exports(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/storage/exports";
	}

	@RequestMapping(value = "/exports/excel", method = RequestMethod.GET)
	@RequiresPermissions(value = { "storage:exports" })
	public void exportsExcel(@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "type", defaultValue = "-1") int type, Model model, HttpServletResponse response,
			HttpServletRequest request) throws ParseException {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if (type == 1) {
			// 导出出库单
			long startTime1 = sd.parse(startTime).getTime();
			long endTime1 = sd.parse(endTime).getTime() + CalendarConst.DAY_TIME - 1L;
			List<ShipOrderForm> shipOrderFormList = this.facade.queryShipOrderFormByTime(startTime1, endTime1);
			List<ShipOrderDTO> shipDTOList = new ArrayList<ShipOrderDTO>();
			for (ShipOrderForm shipOrderForm : shipOrderFormList) {
				ShipOrderDTO dto = new ShipOrderDTO(shipOrderForm);
				WarehouseForm warehouseForm = warehouseService.getWarehouseById(dto.getStoreAreaId());
				dto.setWarehouseName(warehouseForm.getWarehouseName());
				shipDTOList.add(dto);
			}
			FrameworkExcelUtil.writeExcel("入库单列表", shipOrderFormExcel, ShipOrderDTO.class, startTime
					+ "_shiporderlist.xls", shipDTOList, request, response);
		} else if (type == 2) {
			// 导出入库单
			long startTime1 = sd.parse(startTime).getTime() + 16 * CalendarConst.HOUR_TIME;
			long endTime1 = sd.parse(endTime).getTime() + 16 * CalendarConst.HOUR_TIME + CalendarConst.DAY_TIME - 1L;
			// 根据订单时间查询
			List<OmsOrderForm> omsOrderFormList = facade.getOmsOrderFormListByTimeRange(startTime1, endTime1);
			List<OrderFormOMSDTO> dtoList = new ArrayList<OrderFormOMSDTO>();
			for (OmsOrderForm omsOrderForm : omsOrderFormList) {
				OrderFormOMSDTO dto = new OrderFormOMSDTO();
				ReflectUtil.convertObj(dto, omsOrderForm, false);
				WarehouseForm warehouseForm = warehouseService.getWarehouseById(dto.getStoreAreaId());
				dto.setWarehouseName(warehouseForm.getWarehouseName());
				dtoList.add(dto);
			}
			// 导出excel
			FrameworkExcelUtil.writeExcel("订单列表", omsOrderFormExcel, OrderFormOMSDTO.class, startTime
					+ "_orderlist.xls", dtoList, request, response);
		} else if (type == 3) {
			// 导出退货单
			List<ReturnOrderFormDTO> dtoList = facade.queryReturnOrderForm(OmsReturnOrderFormState.SENDED);
			for (ReturnOrderFormDTO dto : dtoList) {
				if (dto.getWarehouseId() > 0) {
					WarehouseForm warehouseForm = warehouseService.getWarehouseById(dto.getWarehouseId());
					dto.setWarehouseName(warehouseForm.getWarehouseName());
				}
			}
			FrameworkExcelUtil.writeExcel("销退列表", returnOrderFormExcel, ReturnOrderFormDTO.class, startTime
					+ "_returnorderlist.xls", dtoList, request, response);
		} else if (type == 4) {
			// 导出拒退单
			long startTime1 = sd.parse(startTime).getTime();
			long endTime1 = sd.parse(endTime).getTime() + CalendarConst.DAY_TIME - 1L;
			List<RejectPackageDTO> rpacks = facade.queryRejectPackageByCreateTime(startTime1, endTime1);
			FrameworkExcelUtil.writeExcel("拒件列表", rejectPackageExcel, RejectPackageDTO.class, startTime
					+ "_rejectpacklist.xls", rpacks, request, response);
		}

	}

	/**
	 * 物流统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logistics", method = RequestMethod.GET)
	@RequiresPermissions(value = { "storage:logistics" })
	public String helperList(Model model) {
		// 报表类型
		List<String> typeListOfLog = new ArrayList<String>();
		typeListOfLog.add("报表1-全国物流概况");
		typeListOfLog.add("报表2-各省发货统计");
		typeListOfLog.add("报表3-各省订单签收状态统计");
		typeListOfLog.add("报表4-各省未签收订单归类统计");
		typeListOfLog.add("报表5-未签收明细表");
		typeListOfLog.add("报表6-返货统计报表");
		typeListOfLog.add("报表7-未返回单量明细");

		long userId = SecurityContextUtils.getUserId();
		List<Long> areaLists = agentService.findAgentSiteIdsByPermission(userId, "storage:logistics");
		// 仓库列表
		WarehouseDTO[] warehouseList = omsReportFacade.getWarehouseList(areaLists);
		model.addAttribute("typeListOfLog", typeListOfLog);
		model.addAttribute("warehouseList", warehouseList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/storage/logistics";
	}

	/**
	 * 物流统计-获取列表数据
	 * 
	 * @param type
	 * @param warehouse
	 * @param startTime
	 * @param endTime
	 * @param offset
	 * @param limit
	 * @param model
	 * @return
	 */
	@RequestMapping("/logistics/list")
	public void logList(@RequestParam(value = "type", defaultValue = "-1") int type,
			@RequestParam(value = "warehouse", defaultValue = "-1") int warehouseId,
			@RequestParam(value = "startTime", defaultValue = "-28800000L") long startTime,
			@RequestParam(value = "endTime", defaultValue = "2524579200000") long endTime,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "10") int limit, Model model) {
		long userId = SecurityContextUtils.getUserId();
		List<Long> areaLists = agentService.findAgentSiteIdsByPermission(userId, "storage:logistics");
		WarehouseDTO[] warehouseList = omsReportFacade.getWarehouseList(areaLists);
		List<Long> warehouseIds = new ArrayList<Long>();
		for (WarehouseDTO warehouseDTO : warehouseList) {
			warehouseIds.add(warehouseDTO.getWarehouseId());
		}
		// todo:列表数据
		List<String> typeListOfLog = new ArrayList<String>();
		typeListOfLog.add("报表1-全国物流概况");
		typeListOfLog.add("报表2-各省发货统计");
		typeListOfLog.add("报表3-各省订单签收状态统计");
		typeListOfLog.add("报表4-各省未签收订单归类统计");
		typeListOfLog.add("报表5-未签收明细表");
		typeListOfLog.add("报表6-返货统计报表");
		typeListOfLog.add("报表7-未返回单量明细");
		Map<String, Object> result = new HashMap<String, Object>();
		if (type == 1) {
			DDBParam ddbParam = new DDBParam("id", true, limit, offset);
			List<OmsSendOutCountryForm> list = omsReportFacade.querySendOutCountryByDay(startTime, endTime, ddbParam);
			result.put("list", list);
			ddbParam = new DDBParam("id", true, 0, 0);
			list = omsReportFacade.querySendOutCountryByDay(startTime, endTime, ddbParam);
			if (list != null)
				result.put("total", list.size());
			else
				result.put("total", 0);
			model.addAttribute("result", result);
		} else if (type == 2) {
			DDBParam ddbParam = new DDBParam("id", true, limit, offset);
			List<OmsSendOutProvinceForm> list = omsReportFacade.querySendOutProvinceByDay(warehouseId, startTime,
					endTime, warehouseIds, ddbParam);
			result.put("list", list);
			ddbParam = new DDBParam("id", true, 0, 0);
			list = omsReportFacade.querySendOutProvinceByDay(warehouseId, startTime, endTime, warehouseIds, ddbParam);
			if (list != null)
				result.put("total", list.size());
			else
				result.put("total", 0);
			model.addAttribute("result", result);
		} else if (type == 3) {
			DDBParam ddbParam = new DDBParam("id", true, limit, offset);
			List<OmsReceiptReport> list = omsReportFacade.queryReceiptReporttByWarehouseAndDay(warehouseId, startTime,
					endTime, warehouseIds, ddbParam);
			result.put("list", list);
			ddbParam = new DDBParam("id", true, 0, 0);
			list = omsReportFacade.queryReceiptReporttByWarehouseAndDay(warehouseId, startTime, endTime, warehouseIds,
					ddbParam);
			if (list != null)
				result.put("total", list.size());
			else
				result.put("total", 0);
			model.addAttribute("result", result);
		} else if (type == 4) {
			DDBParam ddbParam = new DDBParam("id", true, limit, offset);
			List<OmsNoReceiptReport> list = omsReportFacade.queryNoReceiptReport(warehouseId, startTime, endTime,
					warehouseIds, ddbParam);
			result.put("list", list);
			ddbParam = new DDBParam("id", true, 0, 0);
			list = omsReportFacade.queryNoReceiptReport(warehouseId, startTime, endTime, warehouseIds, ddbParam);
			if (list != null)
				result.put("total", list.size());
			else
				result.put("total", 0);
			model.addAttribute("result", result);
		} else if (type == 5) {
			DDBParam ddbParam = new DDBParam("id", true, limit, offset);
			List<OmsNoReceiptDetailReport> list = omsReportFacade.queryNoReceiptDetailReport(warehouseId, startTime,
					endTime, warehouseIds, ddbParam);
			result.put("list", list);
			ddbParam = new DDBParam("id", true, 0, 0);
			list = omsReportFacade.queryNoReceiptDetailReport(warehouseId, startTime, endTime, warehouseIds, ddbParam);
			if (list != null)
				result.put("total", list.size());
			else
				result.put("total", 0);
			model.addAttribute("result", result);
		} else if (type == 6) {
			DDBParam ddbParam = new DDBParam("id", true, limit, offset);
			List<OmsReturnReport> list = omsReportFacade.queryReturnReportByWarehouseAndDay(warehouseId, startTime,
					endTime, warehouseIds, ddbParam);
			result.put("list", list);
			ddbParam = new DDBParam("id", true, 0, 0);
			list = omsReportFacade.queryReturnReportByWarehouseAndDay(warehouseId, startTime, endTime, warehouseIds,
					ddbParam);
			if (list != null)
				result.put("total", list.size());
			else
				result.put("total", 0);
			model.addAttribute("result", result);
		} else if (type == 7) {
			DDBParam ddbParam = new DDBParam("id", true, limit, offset);
			List<OmsNoReturnReport> list = omsReportFacade.queryNoReturnReportByWarehouseAndDay(warehouseId, startTime,
					endTime, warehouseIds, ddbParam);
			result.put("list", list);
			ddbParam = new DDBParam("id", true, 0, 0);
			list = omsReportFacade.queryNoReturnReportByWarehouseAndDay(warehouseId, startTime, endTime, warehouseIds,
					ddbParam);
			if (list != null)
				result.put("total", list.size());
			else
				result.put("total", 0);
			model.addAttribute("result", result);
		}
		model.addAttribute("typeListOfLog", typeListOfLog);
		model.addAttribute("code", 200);
	}

	/**
	 * 物流统计-导出报表数据
	 * 
	 * @param type
	 * @param warehouse
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/logistics/export")
	public void logExport(@RequestParam(value = "type", defaultValue = "-1") int type,
			@RequestParam(value = "warehouse", defaultValue = "-1") int warehouseId,
			@RequestParam(value = "startTime", defaultValue = "0") long startTime,
			@RequestParam(value = "endTime", defaultValue = "2524579200000") long endTime,
			HttpServletResponse response, HttpServletRequest request) {
		long userId = SecurityContextUtils.getUserId();
		List<Long> areaLists = agentService.findAgentSiteIdsByPermission(userId, "storage:logistics");
		WarehouseDTO[] warehouseList = omsReportFacade.getWarehouseList(areaLists);
		List<Long> warehouseIds = new ArrayList<Long>();
		for (WarehouseDTO warehouseDTO : warehouseList) {
			warehouseIds.add(warehouseDTO.getWarehouseId());
		}
		File f = new File(System.currentTimeMillis() + ".xlsx");
		if (type == 5) {
			DDBParam ddbParam = new DDBParam("id", true, 0, 0);
			List<OmsNoReceiptDetailReport> list = omsReportFacade.queryNoReceiptDetailReport(warehouseId, startTime,
					endTime, warehouseIds, ddbParam);
			FrameworkExcelUtil.writeExcel("拣货单详情", noReceiptDetailExcel, OmsNoReceiptDetailReport.class, f.getName(),
					list, request, response);
		} else if (type == 7) {
			DDBParam ddbParam = new DDBParam("id", true, 0, 0);
			List<OmsNoReturnReport> list = omsReportFacade.queryNoReturnReportByWarehouseAndDay(warehouseId, startTime,
					endTime, warehouseIds, ddbParam);
			FrameworkExcelUtil.writeExcel("拣货单详情", noReturnDetailExcel, OmsNoReturnReport.class, f.getName(), list,
					request, response);

		}

	}

	/**
	 * 仓储统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/warehouse", method = RequestMethod.GET)
	@RequiresPermissions(value = { "storage:warehouse" })
	public String viewHelper(Model model) {
		// 报表类型
		List<String> typeListOfWare = new ArrayList<String>();
		Subject subject = SecurityUtils.getSubject();
		if (subject.isPermitted("storage:wareManager")) {
			typeListOfWare.add("报表1-全国订单生产概况");
			typeListOfWare.add("报表2-明日生产任务");
			typeListOfWare.add("报表3-仓库到货统计排名");
			typeListOfWare.add("报表4-仓库生产统计");
			typeListOfWare.add("报表5-延迟订单明细");
		} else {
			typeListOfWare.add("报表2-明日生产任务");
			typeListOfWare.add("报表3-仓库到货统计排名");
		}

		long userId = SecurityContextUtils.getUserId();
		List<Long> areaLists = agentService.findAgentSiteIdsByPermission(userId, "storage:warehouse");
		// 仓库列表
		WarehouseDTO[] warehouseList = omsReportFacade.getWarehouseList(areaLists);
		model.addAttribute("typeListOfWare", typeListOfWare);
		model.addAttribute("warehouseList", warehouseList);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));

		return "pages/storage/warehouse";
	}

	/**
	 * 仓储统计-获取列表数据
	 * 
	 * @param type
	 * @param warehouse
	 * @param startTime
	 * @param endTime
	 * @param offset
	 * @param limit
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/warehouse/list")
	public void wareList(@RequestParam(value = "type", defaultValue = "-1") int type,
			@RequestParam(value = "warehouse", defaultValue = "-1") int warehouseId,
			@RequestParam(value = "startTime", defaultValue = "0") long startTime,
			@RequestParam(value = "endTime", defaultValue = "2524579200000") long endTime,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "10") int limit, Model model) {

		// todo:列表数据
		List<String> typeListOfWare = new ArrayList<String>();
		typeListOfWare.add("报表1-全国订单生产概况");
		typeListOfWare.add("报表2-明日生产任务");
		typeListOfWare.add("报表3-仓库到货统计排名");
		typeListOfWare.add("报表4-仓库生产统计");
		typeListOfWare.add("报表5-延迟订单明细");
		typeListOfWare.add("报表6-供应商缺货统计");
		typeListOfWare.add("报表7-退供统计");
		Map<String, Object> result = new HashMap<String, Object>();
		long userId = SecurityContextUtils.getUserId();
		List<Long> areaLists = agentService.findAgentSiteIdsByPermission(userId, "storage:warehouse");
		WarehouseDTO[] warehouseList = omsReportFacade.getWarehouseList(areaLists);
		RetArg retArg = null;
		DDBParam param = null;
		switch (type) {
		case 1:
			param = new DDBParam("time", false, limit, offset);
			retArg = omsReportFacade.getOrderReportList(startTime, endTime, param);
			List<OrderReportDTO> orderReportDTOList = RetArgUtil.get(retArg, ArrayList.class);
			param = RetArgUtil.get(retArg, DDBParam.class);
			result.put("total", param.getTotalCount());
			result.put("list", orderReportDTOList);
			break;
		case 2:
			param = new DDBParam("time", false, limit, offset);
			List<Long> warehouseIdList = new ArrayList<>();
			if (warehouseId > 0) {
				warehouseIdList.add((long) warehouseId);
			} else {
				for (WarehouseDTO dto : warehouseList) {
					warehouseIdList.add(dto.getWarehouseId());
				}
			}
			retArg = omsReportFacade.getTomorrowOrderReportList(startTime, endTime, param, warehouseIdList);
			List<TomorrowOrderReportDTO> tomorrowOrderReportDTOlist = RetArgUtil.get(retArg, ArrayList.class);
			param = RetArgUtil.get(retArg, DDBParam.class);
			result.put("total", param.getTotalCount());
			result.put("list", tomorrowOrderReportDTOlist);
			break;

		case 3:
			param = new DDBParam("dateTime", false, limit, offset);
			retArg = omsReportFacade.queryOmsShipOutReport(warehouseId, startTime, endTime, param);
			List<OmsShipOutReport> omsShipOutReportList = RetArgUtil.get(retArg, ArrayList.class);
			param = RetArgUtil.get(retArg, DDBParam.class);
			result.put("total", param.getTotalCount());
			result.put("list", omsShipOutReportList);
			break;

		case 4:
			startTime = getSpecificBeginTime(startTime).getTimeInMillis();
			endTime = getSpecificBeginTime(endTime).getTimeInMillis();
			JSONObject json = omsReportFacade.getMakerOrderReport(warehouseId, startTime, endTime, offset, limit);
			result.put("total", json.getIntValue("total"));
			result.put("list", json.getJSONArray("list"));
			break;
		case 5:
			startTime = getSpecificBeginTime(startTime).getTimeInMillis();
			endTime = getSpecificBeginTime(endTime).getTimeInMillis();
			JSONObject json2 = omsReportFacade.getDelayOrderReport(warehouseId, startTime, endTime, offset, limit);
			result.put("total", json2.getIntValue("total"));
			result.put("list", json2.getJSONArray("list"));
			break;

		default:
			break;
		}
		model.addAttribute("result", result);
		model.addAttribute("code", 200);
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

	/**
	 * 仓储统计-导出报表数据
	 * 
	 * @param type
	 * @param warehouse
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/warehouse/export")
	public void wareExport(@RequestParam(value = "type", defaultValue = "-1") int type,
			@RequestParam(value = "warehouse", defaultValue = "-1") int warehouseId,
			@RequestParam(value = "startTime", defaultValue = "0") long startTime,
			@RequestParam(value = "endTime", defaultValue = "2524579200000") long endTime) {

		// todo:导出报表

	}

	// 未签收明细报表
	public static LinkedHashMap<String, String> noReceiptDetailExcel = new LinkedHashMap<String, String>();

	static {
		noReceiptDetailExcel.put("date", "发货日期");
		noReceiptDetailExcel.put("expressCompany", "物流商");
		noReceiptDetailExcel.put("warehouseName", "仓库");
		noReceiptDetailExcel.put("OmsOrderFormId", "订单号");
		noReceiptDetailExcel.put("mailNO", "正向运单号");
		noReceiptDetailExcel.put("mailNOReturn", "反向运单号");
		noReceiptDetailExcel.put("rejectTime", "拒收时间");
		noReceiptDetailExcel.put("rejectReason", "拒收原因");
		noReceiptDetailExcel.put("consigneeName", "收货人");
		noReceiptDetailExcel.put("consigneeAddress", "联系方式");
		noReceiptDetailExcel.put("consigneeAddress", "收货人地址");
	}

	// 未返回单量明细
	public static LinkedHashMap<String, String> noReturnDetailExcel = new LinkedHashMap<String, String>();

	static {
		noReturnDetailExcel.put("expressCompany", "物流商");
		noReturnDetailExcel.put("warehouseName", "仓库");
		noReturnDetailExcel.put("date", "发货日期");
		noReturnDetailExcel.put("OmsOrderFormId", "订单号");
		noReturnDetailExcel.put("price", "订单金额");
		noReturnDetailExcel.put("productName", "商品名称");
		noReturnDetailExcel.put("productName", "商品数量");
		noReturnDetailExcel.put("consigneeAddress", "订单配送地址");
		noReturnDetailExcel.put("mailType", "配送类型");
		noReturnDetailExcel.put("cashOnDelivery", "是否COD");
		noReturnDetailExcel.put("mailType", "配送类型");
		noReturnDetailExcel.put("mailNO", "运单号");
		noReturnDetailExcel.put("omsOrderPackageState", "快递状态");
		noReturnDetailExcel.put("mailNOReturn", "返回运单号");
	}

	public static LinkedHashMap<String, String> omsOrderFormExcel = new LinkedHashMap<String, String>();
	static {
		omsOrderFormExcel.put("warehouseSaleId", "仓库订单id");
		omsOrderFormExcel.put("userOrderFormId", "用户订单id");
		omsOrderFormExcel.put("consigneeName", "收件人姓名");
		omsOrderFormExcel.put("consigneeMobile", "收件人电话");
		omsOrderFormExcel.put("cashOnDelivery", "是否货到付款");
		omsOrderFormExcel.put("time", "时间");
		omsOrderFormExcel.put("state", "订单状态");
		omsOrderFormExcel.put("warehouseName", "仓库");
	}

	public static LinkedHashMap<String, String> shipOrderFormExcel = new LinkedHashMap<String, String>();
	static {
		shipOrderFormExcel.put("shipOrderId", "入库单id");
		shipOrderFormExcel.put("time", "时间");
		shipOrderFormExcel.put("total", "总数");
		shipOrderFormExcel.put("arrivedCount", "到达数量");
		shipOrderFormExcel.put("arrivedTimeStr", "到达时间");
		shipOrderFormExcel.put("state", "入库单状态");
		shipOrderFormExcel.put("warehouseName", "仓库");
	}

	public static LinkedHashMap<String, String> returnOrderFormExcel = new LinkedHashMap<String, String>();
	static {
		returnOrderFormExcel.put("returnId", "退货入库单id");
		returnOrderFormExcel.put("mailNO", "快递单号");
		returnOrderFormExcel.put("orderId", "用户订单号");
		returnOrderFormExcel.put("consigneeName", "联系人");
		returnOrderFormExcel.put("consigneeMobile", "联系方式");
		returnOrderFormExcel.put("time", "时间");
		returnOrderFormExcel.put("warehouseSaleId", "仓库订单id");
		returnOrderFormExcel.put("warehouseName", "仓库");
	}
	
	public static LinkedHashMap<String, String> rejectPackageExcel = new LinkedHashMap<String, String>();
	static {
		rejectPackageExcel.put("rejectPackageId", "oms拒件入库单id");
		rejectPackageExcel.put("expressNO", "快递单号");
		rejectPackageExcel.put("userOrderFormId", "用户订单号");
		rejectPackageExcel.put("userName", "联系人");
		rejectPackageExcel.put("userPhone", "联系方式");
		rejectPackageExcel.put("createTimeFormat", "时间");
		rejectPackageExcel.put("warehouseOrderId", "仓库订单id");
		rejectPackageExcel.put("warehouseName", "仓库");
		rejectPackageExcel.put("stateDesc", "订单状态");
	}

}
