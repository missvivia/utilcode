/*
 * @(#) 2014-12-2
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller.finance;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.vo.EnumBean;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.OrderFinanceFacade;
import com.xyl.mmall.cms.vo.finance.FinanceOrderRefundVO;
import com.xyl.mmall.cms.vo.finance.FinanceOrderVO;
import com.xyl.mmall.cms.vo.finance.FinanceTradeVO;
import com.xyl.mmall.controller.BaseController;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.framework.util.FrameworkExcelUtil;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.promotion.utils.DateUtils;

/**
 * FinanceController.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-2
 * @since 1.0
 */
@Controller
@RequestMapping("/finance")
public class OrderFinanceController extends BaseController {

	@Autowired
	private OrderFinanceFacade orderFinanceFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private JITSupplyManagerFacade jITSupplyManagerFacade;

	@Value("${file.tmp}")
	private String tmpFile;

	@RequestMapping("/index")
	public String index() {
		return "";
	}

	/**
	 * 平台-订单数据.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param request
	 * @param response
	 */
	@RequestMapping("/order/list")
	public void queryOrder(@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate, HttpServletRequest request, HttpServletResponse response) {
		long[] timeRange = getTimeRange(startDate, endDate);
		List<FinanceOrderVO> financeOrderList = orderFinanceFacade.queryOrderList(timeRange);

		LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
		columnMap.put("orderId", "订单号");
		columnMap.put("orderFormState", "订单状态");
		columnMap.put("qttk", "七天无理由退货期已满");
		columnMap.put("orderProvince", "订单省份");
		columnMap.put("financeOrderPackageList.packageId", "包裹号");
		columnMap.put("financeOrderPackageList.packageState", "包裹状态");
		columnMap.put("financeOrderPackageList.expressCompanyName", "快递公司");
		columnMap.put("financeOrderPackageList.expSDate", "发货时间");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.productName", "商品名称");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.poId", "PO单号");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.poStartDate", "PO开始时间");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.poEndDate", "PO结束时间");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.categoryName", "商品类目");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.oriRPrice", "商品销售价(零售价)");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.totalCount", "购买数量");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.totalOriRPrice", "商品销售总价");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.yhPrice", "商品优惠金额");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.yhTotalPrice", "商品优惠总金额");
		columnMap.put("couponDesc", "优惠信息");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.rprice", "商品结算价(优惠后)");
		columnMap.put("financeOrderPackageList.financeOrderSkuList.totalRprice", "商品结算总价(优惠后)");
		columnMap.put("expUserPrice", "用户支付运费");
		columnMap.put("userTotalPrice", "订单实付金额");
		columnMap.put("orderDate", "下单时间");
		columnMap.put("tradeId", "交易号");
		columnMap.put("payMethodArr", "收款渠道");

		FrameworkExcelUtil.writeExcel("订单数据", columnMap, FinanceOrderVO.class, tmpFile + File.separator + "order_"
				+ startDate + "-" + endDate + ".xls", financeOrderList, request, response);
	}

	/**
	 * 平台-交易数据.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param request
	 * @param response
	 */
	@RequestMapping("/trade/list")
	public void queryTrade(@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate, HttpServletRequest request, HttpServletResponse response) {
		long[] timeRange = getTimeRange(startDate, endDate);
		List<FinanceTradeVO> financeTradeList = orderFinanceFacade.queryTradeList(timeRange);

		LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
		columnMap.put("tradeId", "交易号");
		columnMap.put("payMethod", "交易平台类型");
		columnMap.put("payDate", "交易时间");
		columnMap.put("cash", "交易金额");
		columnMap.put("orderId", "订单号");

		FrameworkExcelUtil.writeExcel("交易数据", columnMap, FinanceOrderVO.class, tmpFile + File.separator + "trade_"
				+ startDate + "-" + endDate + ".xls", financeTradeList, request, response);
	}

	@RequestMapping(value = "/queryOrderRefund", method = RequestMethod.GET)
	public void queryOrderRefund(@RequestParam(value = "startDate") long startDate,
			@RequestParam(value = "endDate") long endDate, HttpServletRequest request, HttpServletResponse response) {
		List<FinanceOrderRefundVO> list = orderFinanceFacade.queryOrderRefundList(new long[] { startDate, endDate });

		LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
		columnMap.put("orderId", "订单号");
		columnMap.put("packageVOs.packageId", "包裹号");
		columnMap.put("packageVOs.skuVOs.productName", "商品名称");
		columnMap.put("packageVOs.skuVOs.poId", "po单号");
		columnMap.put("packageVOs.skuVOs.categoryName", "商品类目");
		columnMap.put("packageVOs.skuVOs.retailPrice", "商品销售价");
		columnMap.put("packageVOs.skuVOs.salePrice", "商品结算价");
		columnMap.put("packageVOs.skuVOs.refundCount", "退货数量");
		columnMap.put("packageVOs.skuVOs.refundCash", "商品实退金额");
		columnMap.put("expressRefundCash", "运费退款");
		columnMap.put("packageVOs.refundReason", "退款原因");
		columnMap.put("packageVOs.refundChannel", "退款渠道");
		columnMap.put("packageVOs.refundDate", "退款时间");
		columnMap.put("packageVOs.tradeSerial", "交易号");

		String fileName = "orderRefund_" + DateUtils.parseLongToString(DateUtils.DATE_FORMAT, startDate) + "-"
				+ DateUtils.parseLongToString(DateUtils.DATE_FORMAT, endDate) + ".xls";

		FrameworkExcelUtil.writeExcel("订单取消(退货)", columnMap, FinanceOrderRefundVO.class, tmpFile + File.separator
				+ fileName, list, request, response);
	}

	@RequestMapping(value = "/salequery", method = RequestMethod.GET)
	@RequiresPermissions(value = { "finance:salequery" })
	public String financeSalequery(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		ExpressCompany[] es = ExpressCompany.validValues();
		EnumBean[] express = new EnumBean[es.length];
		for (int i = 0; i < ExpressCompany.validValues().length; i++) {
			express[i] = new EnumBean(es[i].getIntValue(), es[i].getCode(), es[i].getName());
		}
		model.addAttribute("express", express);
		WarehouseDTO[] ws = jITSupplyManagerFacade.getAllWarehouse();
		IdNameBean[] warehouses = new IdNameBean[ws == null ? 0 : ws.length];
		if (ws != null) {
			for (int i = 0; i < ws.length; i++) {
				warehouses[i] = new IdNameBean();
				warehouses[i].setId(String.valueOf(ws[i].getWarehouseId()));
				warehouses[i].setName(ws[i].getWarehouseName());
			}
		}
		model.addAttribute("warehouses", warehouses);
		model.addAttribute("orderPermitted", SecurityUtils.getSubject().isPermitted("finance:order"));
		model.addAttribute("freightPermitted", SecurityUtils.getSubject().isPermitted("finance:freight"));
		model.addAttribute("payPermitted", SecurityUtils.getSubject().isPermitted("finance:pay"));
		return "pages/finance/salequery";
	}
}
