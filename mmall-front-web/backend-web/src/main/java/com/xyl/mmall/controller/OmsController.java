package com.xyl.mmall.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.exceljar.ExcelExportUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.backend.vo.PoReturnQueryParamVO;
import com.xyl.mmall.backend.vo.PoReturnSkuVO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.service.DealerService;
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
	private JITSupplyManagerFacade jITSupplyManagerFacade;

	@Autowired
	private DealerService dealerService;

	@Autowired
	private BusinessService businessService;

	private long getSupplierId() {
		long userId = SecurityContextUtils.getUserId();
		DealerDTO dealer = dealerService.findDealerById(userId);
		if (dealer == null) {
			return -1;
		}
		return dealer.getSupplierId();
	}

	private String getSupplierAccount() {
		long userId = SecurityContextUtils.getUserId();
		DealerDTO dealer = dealerService.findDealerById(userId);
		if (dealer == null) {
			return null;
		}
		return businessService.getBusinessById(dealer.getSupplierId(), -1).getBusinessAccount();
	}

	@RequestMapping(value = "/returnOrder/list")
	@ResponseBody
	public JSONObject returnOrderList(@RequestBody PoReturnQueryParamVO returnParams) {
		try {
			String account = getSupplierAccount();
			if (account == null) {
				return createOKResponse(null);
			}
			returnParams.setPermission("sell:return");
			returnParams.setSupplierAccount(account);
			PageableList<PoReturnOrderVO> returnOrders = jITSupplyManagerFacade.queryPoReturnOrderFormBackend(returnParams);
			// 重新设置状态描述
			if (returnOrders != null && returnOrders.getList() != null) {
				for (PoReturnOrderVO vo : returnOrders.getList()) {
					vo.toSupplierStateDesc();
				}
			}
			return createOKResponse(returnOrders);
		} catch (Exception e) {
			return create201Response(e);
		}
	}

	@RequestMapping(value = "/returnOrder/ok/{id}")
	@ResponseBody
	public JSONObject returnOrderOk(@PathVariable(value = "id") Long returnOrderId) {
		try {
			long supplierId = getSupplierId();
			return createOKResponse(jITSupplyManagerFacade.okPoReturnOrder(returnOrderId, supplierId));
		} catch (Exception e) {
			return create201Response(e);
		}
	}

	@RequestMapping(value = "/returnOrder/confirm/{id}")
	@ResponseBody
	public JSONObject returnOrderConfirm(@PathVariable(value = "id") Long returnOrderId) {
		try {
			long supplierId = getSupplierId();
			jITSupplyManagerFacade.confirmPoReturnOrder(returnOrderId, supplierId);
			return createOKResponse(new Boolean(true));
		} catch (Exception e) {
			return create201Response(e);
		}
	}

	@RequestMapping(value = "/returnOrder/export/{id}")
	public void returnOrderExport(@PathVariable(value = "id") Long returnOrderId, HttpServletResponse response)
			throws IOException {
		long supplierId = getSupplierId();
		PoReturnOrderVO poReturnOrder = jITSupplyManagerFacade.getPoReturnOrderByIdAndSupplierId(returnOrderId,
				supplierId);
		String excelName = poReturnOrder == null ? "null" : poReturnOrder.getBrandName() + returnOrderId;
		excelName = excelName.replaceAll("/", "_");
		response.reset();
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setHeader("Connection", "keep-alive");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-disposition",
				"attachment;filename=" + java.net.URLEncoder.encode(excelName, "UTF-8") + ".xlsx");
		LinkedHashMap<String, String> ruleMap = new LinkedHashMap<String, String>();
		ruleMap.put("barCode", "条码");
		ruleMap.put("productName", "商品名称");
		ruleMap.put("count", "应退数量");
		ruleMap.put("realCount", "实退数量");
		ruleMap.put("typeDesc", "退货类型");
		MyExcelExportUtil util = new MyExcelExportUtil();
		util.initExcelExportUtil(excelName, ruleMap, PoReturnSkuVO.class);
		if (poReturnOrder != null && poReturnOrder.getSkuDetails() != null && poReturnOrder.getSkuDetails().size() > 0) {
			util.write(poReturnOrder.getSkuDetails());
		}
		util.close(response.getOutputStream());
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

	class MyExcelExportUtil extends ExcelExportUtil {
		private Workbook myWb;

		@Override
		@SuppressWarnings("rawtypes")
		public void initExcelExportUtil(String sheetName, LinkedHashMap<String, String> ruleMap, Class clazz,
				boolean isXlsx) {
			if (myWb == null)
				myWb = isXlsx ? new SXSSFWorkbook(500) : new HSSFWorkbook();
			super.setWb(myWb);
			super.initExcelExportUtil(sheetName, ruleMap, clazz, isXlsx);
		}

		public boolean close(OutputStream stream) {
			try {
				myWb.write(stream);
				stream.flush();
				return true;
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
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
