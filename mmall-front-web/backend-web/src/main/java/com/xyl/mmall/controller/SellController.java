/**
 * 
 */
package com.xyl.mmall.controller;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatEnum;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.util.BackendExcelUtil;
import com.xyl.mmall.backend.util.BackendVOConvertUtil;
import com.xyl.mmall.backend.vo.InvoiceInOrdSupplierVO;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.cms.facade.InvoiceFacade;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;

/**
 * @author hzzengchengyuan
 * 
 */
@Controller
public class SellController {
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private JITSupplyManagerFacade jITSupplyManagerFacade;

	@Autowired
	private InvoiceFacade invoiceFacade;

	@Autowired
	private DealerService dealerService;

	@Value("${export.fold.path}")
	private String tmpFile;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private long getSupplierId() {
		long userId = SecurityContextUtils.getUserId();
		DealerDTO dealer = dealerService.findDealerById(userId);
		if (dealer == null) {
			return -1;
		}
		return dealer.getSupplierId();
	}

	@RequestMapping(value = "/sell/return")
	@RequiresPermissions(value = { "sell:return" })
	public String returnOrder(Model model) {
		model.addAttribute("statusList", PoReturnOrderVO.getSupplierStateEnumBean());
		model.addAttribute("warehouses", jITSupplyManagerFacade.getAllWarehouse());
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/sell/return";
	}

	@RequestMapping(value = "/sell/returndetail/{id}")
	@RequiresPermissions(value = { "sell:return" })
	public String returnOrderDetails(Model model, @PathVariable(value = "id") Long returnOrderId) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		PoReturnOrderVO returnOrder = jITSupplyManagerFacade.getPoReturnOrderByIdAndSupplierId(returnOrderId,
				getSupplierId());
		returnOrder.toSupplierStateDesc();
		model.addAttribute("podetails", returnOrder);
		return "pages/sell/returndetail";
	}

	/**
	 * 发票初始化页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sell/invoice", method = RequestMethod.GET)
	@RequiresPermissions(value = { "sell:invoice" })
	public String invoice(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/sell/invoice";
	}

	/**
	 * 发票查询结果页面
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param sdate
	 * @param edate
	 * @param stateInt
	 *            0:未开票;1:已开票
	 * @param queryType
	 *            0: 不启用;1: 订单号查询;2:抬头查询
	 * @param queryValue
	 *            查询框里的值
	 * @param offset
	 * @param isExport
	 *            是否是导出excel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sell/invoiceSearch", method = RequestMethod.GET)
	@RequiresPermissions(value = { "sell:invoice" })
	public ModelAndView invoiceSearch(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "sdate", required = false) Long stime,
			@RequestParam(value = "edate", required = false) Long etime,
			@RequestParam(value = "stateInt") int stateInt, @RequestParam(value = "queryType") int queryType,
			@RequestParam(value = "queryValue", required = false) String queryValue,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(value = "limit", required = false, defaultValue = "50") int limit,
			@RequestParam(value = "isExport", required = false, defaultValue = "false") boolean isExport) {
		long supplierId = getSupplierId();

		List<InvoiceInOrdSupplierVO> invoiceVOList = null;
		// 1.根据查询条件做不同的数据处理
		InvoiceInOrdSupplierState state = InvoiceInOrdSupplierState.genEnumByIntValueSt(stateInt);
		DDBParam param = DDBParam.genParamX(limit);
		param.setOffset(offset);
		// 解析时间条件
		stime = stime != null ? stime : 0;
		etime = etime != null ? etime : System.currentTimeMillis();
		long[] orderTimeRange = new long[] { stime, etime };
		int totalCount = 0;
		if (queryType == 0) {
			// CASE1: 根据时间段查询
			RetArg retArg = invoiceFacade.getInvoiceInOrdSupplierByTimeRangeAndState(supplierId, orderTimeRange, state,
					param);
			List<InvoiceInOrdSupplierDTO> invoiceDTOList = RetArgUtil.get(retArg, ArrayList.class);
			param = RetArgUtil.get(retArg, DDBParam.class);
			totalCount = param.getTotalCount();
			invoiceVOList = BackendVOConvertUtil.convertToInvoiceInOrdSupplierVOList(invoiceDTOList);
		} else if (queryType == 1 && NumberUtils.isNumber(queryValue)) {
			// CASE2: 查询订单号
			long orderId = Long.valueOf(queryValue);
			List<InvoiceInOrdSupplierDTO> invoiceDTOList = invoiceFacade.getInvoiceInOrdSupplierByOrderId(supplierId,
					orderId, state);
			totalCount = CollectionUtil.isNotEmptyOfCollection(invoiceDTOList) ? invoiceDTOList.size() : 0;
			invoiceVOList = BackendVOConvertUtil.convertToInvoiceInOrdSupplierVOList(invoiceDTOList);
		} else if (queryType == 2) {
			// CASE3: 查询抬头
			RetArg retArg = invoiceFacade.getInvoiceInOrdSupplierByTitle(supplierId, queryValue, orderTimeRange, state,
					param);
			List<InvoiceInOrdSupplierDTO> invoiceDTOList = RetArgUtil.get(retArg, ArrayList.class);
			param = RetArgUtil.get(retArg, DDBParam.class);
			totalCount = param.getTotalCount();
			invoiceVOList = BackendVOConvertUtil.convertToInvoiceInOrdSupplierVOList(invoiceDTOList);
		}
		// 2.判断是否要导出为Excel
		if (isExport) {
			String filePath = tmpFile + File.separator + "invoice_e_" + supplierId + "_" + System.currentTimeMillis()
					+ ".xlsx";
			BackendExcelUtil.writeInvoiceInOrdSupplierVOToExcel(filePath, invoiceVOList, request, response);
			return null;
		}

		// 3.输出结果
		Map<String, Object> jsonMap2 = new LinkedHashMap<>();
		jsonMap2.put("list", invoiceVOList);
		jsonMap2.put("total", totalCount);
		Map<String, Object> jsonMap1 = new LinkedHashMap<>();
		jsonMap1.put("code", 200);
		jsonMap1.put("result", jsonMap2);
		model.addAllAttributes(jsonMap1);

		return new ModelAndView();
	}

	/**
	 * 批量更新发票的快递信息
	 * 
	 * @param model
	 * @param updateJsons
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sell/invoice/batchSubmit")
	@RequiresPermissions(value = { "sell:invoice" })
	public ModelAndView invoiceSave(Model model, @RequestBody List<InvoiceInOrdSupplierDTO> dtoList) {
		// 1.更新发票的快递信息+发票状态(设置为已开票)
		boolean isSucc = invoiceSaveByBatch(dtoList);
		model.addAttribute("code", isSucc ? "200" : "201");
		model.addAttribute("result", isSucc);
		return new ModelAndView();
	}

	/**
	 * 批量更新发票的快递信息(上传Excel更新)
	 * 
	 * @param model
	 * @param myfile
	 * @return
	 */
	@RequestMapping(value = "/sell/invoiceUpload", method = RequestMethod.POST)
	@RequiresPermissions(value = { "sell:invoice" })
	public ModelAndView invoiceFileSave(Model model, @RequestParam MultipartFile myfile) {
		boolean isSucc = false;
		try {
			// 1.解析Excel,并转换为List<InvoiceInOrdSupplierDTO>
			List<InvoiceInOrdSupplierDTO> dtoList = parseExcelToInvoiceDTOList(myfile);
			// 2.更新发票的快递信息+发票状态(设置为已开票)
			isSucc = invoiceSaveByBatch(dtoList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Map<String, String> jsonMap = new HashMap<>();
		jsonMap.put("code", isSucc ? "200" : "201");
		jsonMap.put("result", isSucc ? "true" : "false");
		String json = JsonUtils.toJson(jsonMap);
		ModelAndView mv = new ModelAndView("pages/json");
		mv.addObject("json", json);
		return mv;
	}

	/**
	 * 更新发票的快递信息+发票状态(设置为已开票)
	 * 
	 * @param dtoList
	 * @return
	 */
	private boolean invoiceSaveByBatch(List<InvoiceInOrdSupplierDTO> dtoList) {
		// 参数判断
		long supplierId = getSupplierId();
		if (CollectionUtil.isEmptyOfCollection(dtoList)) {
			logger.error("List<InvoiceInOrdSupplierDTO> is empty, supplierId=" + supplierId);
			return false;
		}

		// 更新发票的快递信息+发票状态(设置为已开票)
		boolean isSucc = false;
		for (InvoiceInOrdSupplierDTO dto : dtoList) {
			try {
				dto.setSupplierId(supplierId);
				if (invoiceFacade.updateExpInfoAndState(dto))
					isSucc = true;
			} catch (Exception ex) {
			}
		}
		return isSucc;
	}

	/**
	 * @param myfile
	 * @return
	 */
	private List<InvoiceInOrdSupplierDTO> parseExcelToInvoiceDTOList(MultipartFile myfile) {
		List<InvoiceInOrdSupplierDTO> invoiceDTOList = new ArrayList<>();
		try {
			// 1.获取工作表，兼容03及以上版本
			Workbook book = WorkbookFactory.create(myfile.getInputStream());
			Sheet sheet = book != null ? book.getSheetAt(0) : null;
			Iterator<Row> iter = sheet != null ? sheet.rowIterator() : null;
			if (iter == null || !iter.hasNext())
				return null;

			// 2.生成字段名和列明的关系
			Row rowOfFirst = iter.next();
			if (!iter.hasNext()) {
				return null;
			}
			Map<Integer, String> idxAndFNameMap = genIdxAndFNameMapForInvoice(rowOfFirst);
			if (CollectionUtil.isEmptyOfMap(idxAndFNameMap))
				return null;

			// 3.处理数据
			while (iter.hasNext()) {
				Row row = iter.next();
				InvoiceInOrdSupplierDTO invoiceDTO = parseExcelToInvoiceDTO(row, idxAndFNameMap);
				CollectionUtil.addOfList(invoiceDTOList, invoiceDTO);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return invoiceDTOList;
	}

	/**
	 * @param row
	 * @return
	 */
	private Map<Integer, String> genIdxAndFNameMapForInvoice(Row row) {
		Map<Integer, String> idxAndFNameMap = new TreeMap<Integer, String>();
		Map<String, String> cnameAndFNameMap = new HashMap<String, String>();
		cnameAndFNameMap.put("so编号", "orderId");
		cnameAndFNameMap.put("物流单号", "barCode");
		cnameAndFNameMap.put("物流公司", "expressCompanyName");

		int idx = -1;
		Iterator<Cell> iter = row.cellIterator();
		while (iter.hasNext()) {
			idx++;
			Cell cell = iter.next();
			String cellValue = getValueOfCell(cell);
			String fname = cnameAndFNameMap.get(cellValue);
			if (StringUtils.isBlank(fname))
				continue;
			idxAndFNameMap.put(idx, fname);
		}
		return idxAndFNameMap;
	}

	/**
	 * @param row
	 * @param idxAndFNameMap
	 * @return
	 */
	private InvoiceInOrdSupplierDTO parseExcelToInvoiceDTO(Row row, Map<Integer, String> idxAndFNameMap) {
		InvoiceInOrdSupplierDTO dto = new InvoiceInOrdSupplierDTO();
		boolean hasContCell = false;
		try {
			for (Entry<Integer, String> entry : idxAndFNameMap.entrySet()) {
				int idx = entry.getKey();
				String propertyName = entry.getValue();
				Cell cell = row.getCell(idx);
				if (cell == null) {
					continue;
				}
				hasContCell = true;
				String value = getValueOfCell(cell);

				// 将值设置到对象上
				BeanWrapper beanWrapper = new BeanWrapperImpl(dto);
				beanWrapper.setPropertyValue(propertyName, value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return hasContCell ? dto : null;
	}

	/**
	 * @param cell
	 * @return
	 */
	private String getValueOfCell(Cell cell) {
		String value = null;
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			// value = String.valueOf(cell.getNumericCellValue());
			DecimalFormat df = new DecimalFormat("0");
			value = df.format(cell.getNumericCellValue());
		} else
			value = cell.toString();

		return value.trim();
	}
}
