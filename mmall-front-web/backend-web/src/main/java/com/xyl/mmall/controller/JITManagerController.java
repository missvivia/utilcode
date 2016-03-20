package com.xyl.mmall.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.netease.print.exceljar.ExcelExportUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.vo.PickOrderVo;
import com.xyl.mmall.backend.vo.PickSkuVo;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.backend.vo.PoStatisticListVo;
import com.xyl.mmall.backend.vo.ShipOrderVo;
import com.xyl.mmall.backend.vo.WebPKSearchForm;
import com.xyl.mmall.backend.vo.WebPoSearchForm;
import com.xyl.mmall.backend.vo.WebPoSkuSearchForm;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.framework.util.FrameworkExcelUtil;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.oms.dto.PickOrderBatchDTO;
import com.xyl.mmall.oms.dto.PickOrderDTO;
import com.xyl.mmall.oms.dto.PickSkuDTO;
import com.xyl.mmall.oms.dto.PoOrderDTO;
import com.xyl.mmall.oms.dto.PoOrderReportFormDTO;
import com.xyl.mmall.oms.dto.PoSkuDetailCountDTO;
import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.dto.ShipSkuDTO;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.service.ShipOrderService;

/**
 * jit管理
 * 
 * @author zzj(hzzhangzhoujie@corp.netease.com)
 * 
 */
@Controller
@RequestMapping("/jit")
public class JITManagerController extends BaseController {

	private static Logger logger = Logger.getLogger(JITManagerController.class);

	@Autowired
	private JITSupplyManagerFacade facade;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private ShipOrderService shipOrderService;

	@Autowired
	private DealerService dealerService;

	/**
	 * po单列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/polist")
	@RequiresPermissions(value = { "jit:polist" })
	public String poList(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/polist";
	}

	/**
	 * 异步获取po单列表
	 * 
	 * @return
	 */
	@RequestMapping("/polist/getList")
	@ResponseBody
	public Map<String, Object> ajaxGetPOList(@RequestBody WebPoSearchForm webPoSearchForm) {
		Map<String, Object> map = new HashMap<>();
		List<PoOrderDTO> list = null;
		Map<String, Object> result = new HashMap<>();
		try {
			list = facade.getPoOrderList(webPoSearchForm, getSupplierId());
			result.put("total", list.size());
			result.put("list", list);
			map.put("code", "200");
			map.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "201");
			map.put("result", list);
		}
		return map;
	}

	private long getSupplierId() {
		long userId = SecurityContextUtils.getUserId();
		DealerDTO dealer = dealerService.findDealerById(userId);
		return dealer.getSupplierId();
	}

	/**
	 * PO单详情页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/podetail/{poOrderId}" })
	public String poDetail(@PathVariable String poOrderId, Model model) {
		appendStaticMethod(model);
		// todo:PO单详情 对象
		PoSkuDetailCountDTO poDetail = facade.getPoOrderDetail(poOrderId);
		model.addAttribute("poOrder", poDetail);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/podetail";
	}

	/**
	 * 异步获取po SKU列表
	 * 
	 * @return
	 */
	@RequestMapping("/podetail/getSkuList")
	@ResponseBody
	public Map<String, Object> ajaxGetPOSkuList(@RequestBody WebPoSkuSearchForm webPoSkuSearchForm) {
		Map<String, Object> map = new HashMap<>();
		List<PoStatisticListVo> list = new ArrayList<PoStatisticListVo>();
		Map<String, Object> result = new HashMap<>();
		try {
			list = facade.getPoStatistic(webPoSkuSearchForm);
			result.put("total", list.size());
			result.put("list", list);
			map.put("code", "200");
			map.put("result", result);
		} catch (Exception e) {
			map.put("code", "201");
			map.put("result", result);
		}
		return map;
	}

	/**
	 * po单报表列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/poreport")
	@RequiresPermissions(value = { "jit:poreport" })
	public String poReport(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/poreport";
	}

	/**
	 * 异步获取po单报表列表
	 * 
	 * @return
	 */
	@RequestMapping("/poreport/getReportList")
	@ResponseBody
	public Map<String, Object> ajaxGetPORptList(@RequestBody WebPoSearchForm webPoSearchForm) {
		Map<String, Object> map = new HashMap<>();
		List<PoOrderReportFormDTO> list = null;
		Map<String, Object> result = new HashMap<>();
		try {
			long userId = SecurityContextUtils.getUserId();
			DealerDTO dealer = dealerService.findDealerById(userId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (webPoSearchForm.getSaleEndTime() != null && !"".equals(webPoSearchForm.getSaleEndTime()))
				webPoSearchForm.setSaleEndTime(sdf.format(Long.valueOf(webPoSearchForm.getSaleEndTime())));
			if (webPoSearchForm.getSaleStartTime() != null && !"".equals(webPoSearchForm.getSaleStartTime()))
				webPoSearchForm.setSaleStartTime(sdf.format(Long.valueOf(webPoSearchForm.getSaleStartTime())));
			if (webPoSearchForm.getCreateStartTime() != null && !"".equals(webPoSearchForm.getCreateStartTime()))
				webPoSearchForm.setCreateStartTime(sdf.format(Long.valueOf(webPoSearchForm.getCreateStartTime())));
			if (webPoSearchForm.getCreateEndTime() != null && !"".equals(webPoSearchForm.getCreateEndTime()))
				webPoSearchForm.setCreateEndTime(sdf.format(Long.valueOf(webPoSearchForm.getCreateEndTime())));
			list = facade.getPoReportList(webPoSearchForm, dealer.getSupplierId());
			result.put("total", list.size());
			result.put("list", list);
			map.put("code", "200");
			map.put("result", result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			map.put("code", "201");
			map.put("result", result);
		}
		return map;
	}

	/**
	 * 来货物流信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/posupply/{poOrderId}" })
	public String poSupply(@PathVariable String poOrderId, Model model) {
		appendStaticMethod(model);
		// todo:PO单来货物流信息 对象 数组
		long userId = SecurityContextUtils.getUserId();
		DealerDTO dealer = dealerService.findDealerById(userId);
		List<ShipOrderForm> list = facade.getShipOrderBySupplierId(dealer.getSupplierId());
		model.addAttribute("poSupplyList", list);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/posupply";
	}

	/**
	 * 退供物流信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/poreturn/{poOrderId}" })
	public String poReturn(@PathVariable String poOrderId, Model model) {
		appendStaticMethod(model);
		List<PoReturnOrderVO> list = facade.getReturnVoByPoOrderId(poOrderId);
		if (list != null) {
			for (PoReturnOrderVO vo : list) {
				vo.toSupplierStateDesc();
			}
		}
		model.addAttribute("poReturnList", list);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/poreturn";
	}

	/**
	 * 拣货单列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/pkList")
	@RequiresPermissions(value = { "jit:pkList" })
	public String pkList(@RequestParam(value = "poId", defaultValue = "") String poId, Model model) {
		model.addAttribute("poId", poId);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/picking.list";
	}

	/**
	 * 异步获取拣货单列表
	 * 
	 * @return
	 */
	@RequestMapping("/getPKList")
	@ResponseBody
	public Map<String, Object> ajaxGetPKList(@RequestBody WebPKSearchForm webPKSearchForm) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 获取某个用户的拣货单列表
			Long userId = SecurityContextUtils.getUserId();
			DealerDTO dealer = dealerService.findDealerById(userId);
			List<PickOrderBatchDTO> list = facade.getPickOrderBatchDTO(dealer.getSupplierId(), webPKSearchForm);
			Map<String, Object> result = new HashMap<>();
			if (list != null) {
				map.put("code", 200);
				result.put("total", list.size());
			} else {
				map.put("code", 400);
				result.put("total", 0);
			}
			result.put("list", list);
			map.put("result", result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("code", 400);
		}
		return map;
	}

	/**
	 * 导出某个拣货单
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/exportPK/{pkId}" })
	public void exportPK(HttpServletResponse response, HttpServletRequest request, @PathVariable String pkId,
			Model model) {

		File f = new File(System.currentTimeMillis() + ".xlsx");

		LinkedHashMap<String, String> pickOrderSkuExcel = new LinkedHashMap<String, String>();
		pickOrderSkuExcel.put("pickOrderId", "拣货单号");
		pickOrderSkuExcel.put("mode", "类别");
		pickOrderSkuExcel.put("brandName", "品牌名称");
		pickOrderSkuExcel.put("warehouse", "入库仓库");
		pickOrderSkuExcel.put("exportTime", "导出时间");
		// pickOrderSkuExcel.put("skuId", "SKU");
		pickOrderSkuExcel.put("codeNO", "条形码");
		pickOrderSkuExcel.put("productName", "商品名称");
		pickOrderSkuExcel.put("size", "尺码");
		pickOrderSkuExcel.put("color", "颜色");
		pickOrderSkuExcel.put("skuQuantity", "数量");
		pickOrderSkuExcel.put("poOrderId", "PO单编号");

		ExcelExportUtil util = new ExcelExportUtil();
		util.initExcelExportUtil("拣货单详情", pickOrderSkuExcel, PickSkuVo.class);
		List<PickSkuDTO> tradList = facade.getPickSkuInfo(pkId, getSupplierId());
		util.write("拣货单详情", tradList);
		util.close(f.getAbsolutePath());
		FrameworkExcelUtil.writeExcel("拣货单详情", pickOrderSkuExcel, PickSkuDTO.class, f.getName(), tradList, request,
				response);
	}

	@RequestMapping(value = { "/podetail/export" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportPoDetail(WebPoSkuSearchForm webPoSkuSearchForm, Model model) {
		File f = new File(System.currentTimeMillis() + ".xlsx");
		List<PoStatisticListVo> list = facade.getPoStatistic(webPoSkuSearchForm);
		LinkedHashMap<String, String> pickOrderSkuExcel = new LinkedHashMap<String, String>();
		pickOrderSkuExcel.put("skuId", "SKU");
		pickOrderSkuExcel.put("skuNum", "SKU数量");
		pickOrderSkuExcel.put("pickOrderId", "拣货单号");
		pickOrderSkuExcel.put("shipOrderId", "发货单号");
		pickOrderSkuExcel.put("pickTime", "拣货时间");
		pickOrderSkuExcel.put("shipTime", "发货时间");
		pickOrderSkuExcel.put("pickStates", "拣货状态");
		pickOrderSkuExcel.put("shipStates", "发货状态");

		ExcelExportUtil util = new ExcelExportUtil();
		util.initExcelExportUtil("PO详情", pickOrderSkuExcel, PickSkuVo.class);
		util.write("PO详情", list);
		util.close(f.getAbsolutePath());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", f.getName());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(f), headers, HttpStatus.CREATED);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			FileUtils.deleteQuietly(f);
		}

		return null;
	}

	/**
	 * 导出拣货单（数量加1）
	 * 
	 * @return
	 */
	@RequestMapping("/addexportone")
	@ResponseBody
	public Map<String, Object> ajaxExportAddOne(@RequestParam(value = "id", defaultValue = "") String id) {
		Map<String, Object> map = new HashMap<>();

		PickOrderDTO pickOrderDTO = facade.getPickOrderByPkId(id, getSupplierId());
		if (pickOrderDTO == null) {
			return map;
		}

		if (pickOrderDTO.getFirstExportTime() == 0) {
			pickOrderDTO.setFirstExportTime(System.currentTimeMillis());
		}

		pickOrderDTO.setExportTimes(pickOrderDTO.getExportTimes() + 1);

		boolean result = facade.updatePickOrder(pickOrderDTO);

		map.put("result", result);
		map.put("code", 200);
		map.put("data", pickOrderDTO);
		return map;
	}

	/**
	 * 拣货单详情页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/pkDetail/{pkId}/", "/pkDetail/{pkId}" })
	@RequiresPermissions(value = { "jit:pkList" })
	public String pkDetail(@PathVariable String pkId, Model model) {
		appendStaticMethod(model);
		PickOrderDTO pickOrderDTO = facade.getPickOrderByPkId(pkId, getSupplierId());
		List<PickSkuDTO> pickSkuDTOs = facade.getPickSkuInfo(pkId, getSupplierId());

		PickOrderVo pickOrderVo = new PickOrderVo(pickOrderDTO);
		pickOrderVo.setPickSkuDtoList(pickSkuDTOs);

		ShipOrderVo shipOrderVo = null;
		ShipOrderDTO shipOrderDTO = facade.getShipOrder(pkId, getSupplierId());
		List<ShipSkuDTO> shipSkuDTOs = null;
		if (shipOrderDTO.getShipOrderId() != null) {
			shipOrderVo = new ShipOrderVo();
			pickOrderDTO.setExpressNo(shipOrderDTO.getExpressNO());
			pickOrderDTO.setShipState(shipOrderDTO.getShipState());
			shipSkuDTOs = facade.getShipOrderSkuList(pkId);
			shipOrderVo.setShipOrderDTO(shipOrderDTO);
			shipOrderVo.setShipSkuDTOs(shipSkuDTOs);
		}   

		// todo:发货单对象
		model.addAttribute("pkObject", pickOrderVo);
		model.addAttribute("invoice", shipOrderVo);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/picking.detail";
	}

	/**
	 * 更新拣货单状态
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/pkUpdate/{pkId}")
	public Map<String, Object> setPickOrderPicked(@PathVariable String pkId, Model model) {
		Map<String, Object> map = new HashMap<>();
		PickOrderDTO pickOrderDTO = facade.getPickOrderByPkId(pkId, getSupplierId());
		if (pickOrderDTO == null) {
			return map;
		}
		pickOrderDTO.setPickState(PickStateType.PICKED);
		boolean result = facade.updatePickOrder(pickOrderDTO);
		map.put("result", result);
		map.put("code", 200);
		map.put("data", pickOrderDTO);
		return map;
	}

	/**
	 * 出仓/发货列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/inList")
	@RequiresPermissions(value = { "jit:inList" })
	public String storage(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/invoice.list";
	}

	/**
	 * 出仓/发货详情页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/inDetail/{inId}/", "/inDetail/{inId}" })
	public String InvoiceDetail(@PathVariable String inId, Model model) {

		appendStaticMethod(model);
		buildShipOrderVo(model, inId);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/invoice.detail";
	}

	/**
	 * 导出出仓明细
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/exportIn/{shipId}/", "/exportIn/{shipId}" })
	public ResponseEntity<byte[]> exportIn(@PathVariable String shipId, Model model) {
		File f = new File("c:/var/ship_" + System.currentTimeMillis() + ".xlsx");
		LinkedHashMap<String, String> shipOrderSkuExcel = new LinkedHashMap<String, String>();
		shipOrderSkuExcel.put("skuId", "SKU编号");
		shipOrderSkuExcel.put("productName", "商品名称");
		shipOrderSkuExcel.put("poOrderId", "PO单号");
		shipOrderSkuExcel.put("skuQuantity", "发货数量");
		shipOrderSkuExcel.put("size", "尺码");
		shipOrderSkuExcel.put("color", "颜色");

		ExcelExportUtil util = new ExcelExportUtil();
		util.initExcelExportUtil("出仓明细", shipOrderSkuExcel, PickSkuVo.class);
		List<ShipSkuDTO> tradList = facade.getShipOrderSkuList(shipId);
		util.write("出仓明细", tradList);
		util.close(f.getAbsolutePath());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", f.getName());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(f), headers, HttpStatus.CREATED);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			FileUtils.deleteQuietly(f);
		}
		return null;
	}

	/**
	 * 新建发货单
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/invoice/new")
	public String cInvoice(Model model, @RequestParam(value = "pickId", defaultValue = "") String pickId) {
		model.addAttribute("pickOrderId", pickId);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/invoice.new";
	}

	/**
	 * 根据拣货单号获得sku列表
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/invoice/getPickSkuInfo")
	public Map<String, Object> getPickSkuInfo(@RequestParam(value = "id", defaultValue = "") String id) {
		List<PickSkuDTO> pickSkuDTOs = facade.getPickSkuInfo(id, getSupplierId());
		Map<String, Object> map = new HashMap<>();
		map.put("skuList", pickSkuDTOs);
		return map;
	}

	/**
	 * 新建发货单
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/invoice/savesku")
	public Map<String, Object> saveSku(@RequestBody ShipOrderVo shipOrderVo) {
		Map<String, Object> map = new HashMap<>();
		ShipOrderDTO shipOrderDTO = shipOrderVo.getShipOrderDTO();

		if (shipOrderDTO == null) {
			return map;
		}

		String shipOrderId = shipOrderDTO.getShipOrderId();

		if (StringUtils.isBlank(shipOrderId)) {
			return map;
		}

		List<ShipSkuDTO> list = shipOrderVo.getShipSkuDTOs();
		if (CollectionUtils.isEmpty(list)) {
			return map;
		}

		boolean status = facade.saveShipSkuList(list, shipOrderId, JITFlagType.IS_JIT);

		map.put("data", shipOrderVo);
		map.put("result", status);

		return map;
	}

	/**
	 * 将发货单excel转换成bean
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/invoice/import")
	@ResponseBody
	public Map<String, Object> importInvoice(@RequestParam("Filedata") MultipartFile file) {
		Map<String, Object> map = new HashMap<>();

		Workbook workbook = initWorkbook(file);
		int sheetNum = workbook.getNumberOfSheets();
		List<ShipSkuDTO> list = new ArrayList<>();
		for (int i = 0; i < sheetNum; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			int rowNum = sheet.getLastRowNum();
			for (int j = 1; j <= rowNum; j++) {
				Row row = sheet.getRow(j);
				ShipSkuDTO shipSkuDTO = new ShipSkuDTO();
				// sku
				Cell cell0 = row.getCell(0);
				String shipSkuId = cell0.getStringCellValue();
				if (StringUtils.isBlank(shipSkuId)) {
					continue;
				}
				// 产品名称
				Cell cell1 = row.getCell(1);
				String productName = cell1.getStringCellValue();
				if (StringUtils.isBlank(productName)) {
					continue;
				}
				// po编号
				Cell cell2 = row.getCell(2);
				String poOrderId = cell2.getStringCellValue();

				// 发货数量
				Cell cell3 = row.getCell(3);
				int shipCount = (int) cell3.getNumericCellValue();

				if (row.getLastCellNum() > 3) {
					// 尺寸
					Cell cell4 = row.getCell(4);
					String size = cell4.getStringCellValue();

					// 颜色
					Cell cell5 = row.getCell(5);
					String color = cell5.getStringCellValue();

					shipSkuDTO.setSize(size);
					shipSkuDTO.setColor(color);
				}
				shipSkuDTO.setPoOrderId(poOrderId);
				shipSkuDTO.setSkuId(shipSkuId);
				shipSkuDTO.setProductName(productName);
				shipSkuDTO.setSkuQuantity(Integer.valueOf(shipCount));

				list.add(shipSkuDTO);
			}
		}
		map.put("data", list);
		return map;
	}

	/**
	 * 编辑发货单
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/invoice/edit")
	public String eInvoice(Model model, @RequestParam(value = "id", defaultValue = "") String id) {
		appendStaticMethod(model);
		buildShipOrderVo(model, id);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/invoice.edit";
	}

	@RequestMapping("/polist/createPicking")
	public String createPicking(Model model, @RequestParam(value = "poOrderId", defaultValue = "") String poOrderId) {
		appendStaticMethod(model);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/jit/picking.detail";
	}

	private void buildShipOrderVo(Model model, String id) {
		ShipOrderVo shipOrderVo = new ShipOrderVo();

		ShipOrderDTO shipOrderDTO = facade.getShipOrder(id, this.getSupplierId());
		List<ShipSkuDTO> shipSkuDTOs = facade.getShipOrderSkuList(id);

		shipOrderVo.setShipOrderDTO(shipOrderDTO);
		shipOrderVo.setShipSkuDTOs(shipSkuDTOs);

		// todo:发货单对象
		model.addAttribute("invoice", shipOrderVo);
	}

	/**
	 * 根据excel版本不同 初始化workbook
	 * 
	 * @param file
	 * @return
	 */
	public Workbook initWorkbook(MultipartFile file) {
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(file.getInputStream());
		} catch (Exception e1) {
			try {
				workbook = new XSSFWorkbook(file.getInputStream());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return workbook;
	}
}
