package com.xyl.mmall.backend.util;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.exceljar.ExcelExportUtil;
import com.xyl.mmall.backend.vo.InvoiceInOrdSupplierVO;
import com.xyl.mmall.framework.util.FrameworkExcelUtil;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;

/**
 * @author dingmingliang
 * 
 */
public final class BackendExcelUtil {

	/**
	 * 导出InvoiceInOrdSupplierVO明细
	 * 
	 * @param filePath
	 * @param cpsList
	 * @param request
	 * @param response
	 */
	public static void writeInvoiceInOrdSupplierVOToExcel(String filePath, List<InvoiceInOrdSupplierVO> objList,
			HttpServletRequest request, HttpServletResponse response) {
		if (CollectionUtil.isEmptyOfCollection(objList))
			return;

		LinkedHashMap<String, String> ruleMap = new LinkedHashMap<String, String>();
		ruleMap.put("orderId", "so编号");
		ruleMap.put("consigneeName", "收件人");
		ruleMap.put("consigneePhone", "联系电话");
		ruleMap.put("title", "抬头");
		ruleMap.put("fullAddress", "收票地址");
		ruleMap.put("cash", "发票金额");
		ruleMap.put("goods", "商品");
		ruleMap.put("orderDate", "下单时间");
		ruleMap.put("barCode", "物流单号");
		ruleMap.put("expressCompanyName", "物流公司");

		InvoiceInOrdSupplierVO obj = CollectionUtil.getFirstObjectOfCollection(objList);
		ExcelExportUtil util = new ExcelExportUtil();
		util.initExcelExportUtil("发票明细-" + (obj.getState() == InvoiceInOrdSupplierState.KP_ED ? "已开票" : "待开票"),
				ruleMap, InvoiceInOrdSupplierVO.class);

		util.write(objList);
		if (StringUtils.isNotBlank(filePath))
			util.close(filePath);

		// 将Excel回写到Response里
		FrameworkExcelUtil.writeToResponse(request, response, filePath);
	}
}
