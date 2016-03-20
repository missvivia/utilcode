/*
 * @(#) 2014-12-2
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller.finance;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.DateFormatUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.SupplierFinanceFacade;
import com.xyl.mmall.cms.vo.finance.FinanceFirstPayConfirmVO;
import com.xyl.mmall.cms.vo.finance.FinanceFirstPayOrderVO;
import com.xyl.mmall.cms.vo.finance.FinanceFullPayDetailMetaVO;
import com.xyl.mmall.cms.vo.finance.FinanceFullPayMetaVO;
import com.xyl.mmall.cms.vo.finance.FinanceInDetailMetaVO;
import com.xyl.mmall.cms.vo.finance.FinanceRefundDetailMetaVO;
import com.xyl.mmall.controller.BaseController;
import com.xyl.mmall.framework.util.FrameworkExcelUtil;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.meta.Schedule;

/**
 * SupplierFinanceController.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-2
 * @since 1.0
 */
@Controller
@RequestMapping("/finance")
public class SupplierFinanceController extends BaseController {

	@Autowired
	private SupplierFinanceFacade supplierFinanceFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Autowired
	private POProductService poProductService;

	@Value("${file.tmp}")
	private String tmpFile;

	/**
	 * 首付款-确认单.
	 * 
	 * @param poId
	 * @return
	 */
	@RequestMapping("/firstpay")
	public String firstPayIndex(@RequestParam(value = "poId") long poId, Model model) {
		FinanceFirstPayConfirmVO financeFirstPayConfirmVO = supplierFinanceFacade.getFirstPayConfirm(poId);
		model.addAttribute("result", financeFirstPayConfirmVO);
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/finance/first.pay";
	}

	/**
	 * 首付款-商品销售明细.
	 * 
	 * @param poId
	 * @param request
	 * @param response
	 */
	@RequestMapping("/firstpay/detail")
	public void firstPayDetail(@RequestParam(value = "poId") long poId, HttpServletRequest request,
			HttpServletResponse response) {
		List<FinanceFirstPayOrderVO> financeFirstPayOrderVOList = supplierFinanceFacade
				.queryFinanceFirstPayOrderVOList(poId);

		LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
		columnMap.put("poId", "PO单号");
		columnMap.put("supplierId", "供应商编号");
		columnMap.put("supplierName", "公司名称");
		columnMap.put("brandName", "品牌名称");
		columnMap.put("startDate", "上线日期");
		columnMap.put("endDate", "下线日期");
		columnMap.put("orderId", "订单号");
		columnMap.put("barCode", "条码");
		columnMap.put("goodsNo", "货号");
		columnMap.put("oriRPrice", "销售价(零售价)");
		columnMap.put("totalCount", "销售数量");
		columnMap.put("totalRPrice", "销售价总额");

		String filePath = tmpFile + File.separator + "firstpay_" + poId + "_"
				+ DateFormatUtil.getFormatDateType1(System.currentTimeMillis()) + ".xls";
		FrameworkExcelUtil.writeExcel("商品销售明细", columnMap, FinanceFirstPayOrderVO.class, filePath,
				financeFirstPayOrderVOList, request, response);
	}

	/**
	 * 根据po查询
	 * 
	 * @param poId
	 * @return
	 */
	@RequestMapping(value = "/fullpay")
	// @RequiresPermissions(value = { "finance:salequery" })
	public String querySupplier(@RequestParam(value = "poId") long poId, Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		RetArg retArg = supplierFinanceFacade.getFullPayMetaVOList(poId);

		FinanceFullPayMetaVO vo = null;
		PODTO podto = null;

		if (retArg != null) {
			vo = RetArgUtil.get(retArg, FinanceFullPayMetaVO.class);
			podto = RetArgUtil.get(retArg, PODTO.class);
		}

		model.addAttribute("vo", vo);
		model.addAttribute("podto", podto);

		return "pages/finance/full.pay";
	}

	/**
	 * 来货明细
	 * 
	 * @param poId
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("inDetail")
	public void inDetail(@RequestParam(value = "poId") long poId, HttpServletRequest request,
			HttpServletResponse response) {
		RetArg retArg = supplierFinanceFacade.getInDetails(poId);
		List<FinanceInDetailMetaVO> vos = RetArgUtil.get(retArg, ArrayList.class);

		vos = CollectionUtils.isEmpty(vos) ? new ArrayList<FinanceInDetailMetaVO>() : vos;

		LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
		columnMap.put("poId", "PO单号");
		columnMap.put("onlineDate", "上线日期");
		columnMap.put("supplierId", "供应商编号");
		columnMap.put("companyName", "公司名称");
		columnMap.put("brandName", "品牌名称");
		columnMap.put("poFollowerUserName", "商助邮箱");
		columnMap.put("barcode", "条码");
		columnMap.put("goodsNo", "货号");
		columnMap.put("goodsName", "商品名称");
		columnMap.put("marketPrice", "正品价");
		columnMap.put("retailPrice", "零售价");
		columnMap.put("receivableCount", "计划数量");
		columnMap.put("arrivedCount", "收货数量");
		columnMap.put("notNormalCount", "退货数量");
		columnMap.put("realReceiveCount", "实收数量");
		columnMap.put("warehouse", "接收仓库");

		String fileName = "inDetails_" + poId + "_" + DateUtils.parseDateToString(DateUtils.DATE_FORMAT, new Date())
				+ ".xls";

		FrameworkExcelUtil.writeExcel("来货明细", columnMap, FinanceInDetailMetaVO.class, tmpFile + File.separator
				+ fileName, vos, request, response);
	}

	/**
	 * 导出销售明细
	 * 
	 * @param poId
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("exportDetail")
	public void exportDetailXls(@RequestParam(value = "poId") long poId, HttpServletRequest request,
			HttpServletResponse response) {

		RetArg retArg = supplierFinanceFacade.getFullPayDetail(poId);

		Schedule schedule = RetArgUtil.get(retArg, Schedule.class);
		List<OrderSkuDTO> dtos = RetArgUtil.get(retArg, ArrayList.class);
		Map<String, String> descMap = RetArgUtil.get(retArg, HashMap.class);
		PODTO podto = RetArgUtil.get(retArg, PODTO.class);
		String companyName = RetArgUtil.get(retArg, String.class);

		dtos = CollectionUtils.isEmpty(dtos) ? new ArrayList<OrderSkuDTO>() : dtos;
		descMap = CollectionUtils.isEmpty(descMap) ? new HashMap<String, String>() : descMap;

		LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
		columnMap.put("poId", "PO单号");
		columnMap.put("supplierNo", "供应商编号");
		columnMap.put("companyName", "公司名称");
		columnMap.put("brandName", "品牌名称");
		columnMap.put("onlineDate", "上线日期");
		columnMap.put("offlineDate", "下线日期");
		columnMap.put("orderId", "销售订单号");
		columnMap.put("barcode", "条码");
		columnMap.put("goodsNo", "货号");
		columnMap.put("retailPrice", "销售价（零售价）");
		columnMap.put("count", "销售数量");
		columnMap.put("retailTotalPrice", "销售总额");
		columnMap.put("promotionDiscountAmount", "单件活动优惠金额");
		columnMap.put("promotionTotalDiscountAmount", "活动优惠总额");
		columnMap.put("supplierPromotionShareRatio", "活动-供应商承担比(%)");
		columnMap.put("platformPromotionShareRatio", "活动-平台承担比(%)");
		columnMap.put("redPacketDiscountAmount", "单件红包抵扣金额");
		columnMap.put("redPacketTotalDiscountAmount", "红包抵扣总额");
		columnMap.put("supplierRedPacketShareRatio", "红包-供应商承担比(%)");
		columnMap.put("platformRedPacketShareRatio", "红包-平台承担比(%)");
		columnMap.put("couponDiscountAmount", "单件优惠券抵扣金额");
		columnMap.put("couponTotalDiscountAmount", "优惠券抵扣总额");
		columnMap.put("supplierCouponShareRatio", "优惠券-供应商承担比(%)");
		columnMap.put("platformCouponShareRatio", "优惠券-平台承担比(%)");
		columnMap.put("platformDiscountAmount", "平台承担折扣总金额");
		columnMap.put("supplierDiscountAmount", "应扣供应商总金额");
		columnMap.put("desc", "优惠名称");

		BigDecimal platformRate = podto.getScheduleDTO().getScheduleVice().getPlatformSrvFeeRate() == null ? BigDecimal.ZERO
				: podto.getScheduleDTO().getScheduleVice().getPlatformSrvFeeRate();

		List<FinanceFullPayDetailMetaVO> datas = new ArrayList<>(dtos.size());
		for (OrderSkuDTO dto : dtos) {
			if (dto.getPoId() != poId) {
				continue;
			}
			FinanceFullPayDetailMetaVO vo = new FinanceFullPayDetailMetaVO();
			int totalCount = dto.getTotalCount();

			vo.setPoId(schedule.getId());
			vo.setSupplierNo(schedule.getSupplierId());
			vo.setCompanyName(companyName);
			vo.setBrandName(schedule.getBrandName());
			vo.setOnlineDate(DateUtils.parseLongToString(DateUtils.DATE_FORMAT, schedule.getStartTime()));
			vo.setOfflineDate(DateUtils.parseLongToString(DateUtils.DATE_FORMAT, schedule.getEndTime()));
			vo.setOrderId(dto.getOrderId());
			POSkuDTO poSkuDTO = poProductService.getPOSkuDTO(dto.getSkuId());
			if (poSkuDTO == null) {
				continue;
			}
			vo.setBarcode(poSkuDTO.getBarCode());
			vo.setGoodsNo(poSkuDTO.getGoodsNo());
			vo.setRetailPrice(dto.getOriRPrice());
			vo.setCount(totalCount);
			vo.setRetailTotalPrice(dto.getOriRPrice().multiply(new BigDecimal(totalCount)));
			vo.setPromotionDiscountAmount(dto.getHdSPrice());
			vo.setPromotionTotalDiscountAmount(dto.getHdSPrice().multiply(new BigDecimal(totalCount)));
			vo.setSupplierPromotionShareRatio(new BigDecimal(100).subtract(platformRate));
			vo.setPlatformPromotionShareRatio(platformRate);
			// 设置红包
			vo.setRedPacketDiscountAmount(dto.getRedSPrice());
			vo.setRedPacketTotalDiscountAmount(dto.getRedSPrice().multiply(new BigDecimal(totalCount)));
			vo.setCouponDiscountAmount(dto.getCouponSPrice());
			vo.setCouponTotalDiscountAmount(dto.getCouponSPrice().multiply(new BigDecimal(totalCount)));
			// 平台加上红包价格
			BigDecimal platformDiscount = dto.getHdSPrice().multiply(new BigDecimal(totalCount)).multiply(platformRate)
					.divide(new BigDecimal(100)).add(vo.getRedPacketTotalDiscountAmount())
					.add(vo.getCouponTotalDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal supplierDiscount = (dto.getHdSPrice().add(dto.getCouponSPrice()).add(dto.getRedSPrice()))
					.multiply(new BigDecimal(totalCount)).subtract(platformDiscount)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			vo.setPlatformDiscountAmount(platformDiscount);
			vo.setSupplierDiscountAmount(supplierDiscount);
			String key = dto.getOrderId() + "-" + dto.getPackageId();
			if (descMap.containsKey(key)) {
				vo.setDesc(descMap.get(key));
			} else {
				vo.setDesc("");
			}

			datas.add(vo);
		}

		String fileName = "saleDetail_" + poId + "_" + DateUtils.parseDateToString(DateUtils.DATE_FORMAT, new Date())
				+ ".xls";

		FrameworkExcelUtil.writeExcel("商品销售明细（含优惠明细）", columnMap, FinanceFullPayDetailMetaVO.class, tmpFile
				+ File.separator + fileName, datas, request, response);
	}

	/**
	 * 退货明细
	 * 
	 * @param poId
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("refundDetail")
	public void refundDetail(@RequestParam(value = "poId") long poId, HttpServletRequest request,
			HttpServletResponse response) {
		RetArg retArg = supplierFinanceFacade.getRefundDetails(poId);

		List<FinanceRefundDetailMetaVO> list = RetArgUtil.get(retArg, ArrayList.class);

		list = CollectionUtils.isEmpty(list) ? new ArrayList<FinanceRefundDetailMetaVO>() : list;

		LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
		columnMap.put("poId", "PO单号");
		columnMap.put("onlineDate", "上线日期");
		columnMap.put("supplierNo", "供应商编号");
		columnMap.put("companyName", "公司名称");
		columnMap.put("brandName", "品牌名称");
		columnMap.put("assistant", "商助邮箱");
		columnMap.put("barcode", "条码");
		columnMap.put("goodsNo", "货号");
		columnMap.put("goodsName", "商品名称");
		columnMap.put("marketPrice", "正品价");
		columnMap.put("retailPrice", "零售价");
		columnMap.put("refundCount", "退货数量");
		columnMap.put("signRefundDate", "退货签收时间");
		columnMap.put("warehouse", "接收仓库");
		columnMap.put("refundType", "退货类型");

		String fileName = "refundDetail_" + poId + "_" + DateUtils.parseDateToString(DateUtils.DATE_FORMAT, new Date())
				+ ".xls";

		FrameworkExcelUtil.writeExcel("退货明细", columnMap, FinanceRefundDetailMetaVO.class, tmpFile + File.separator
				+ fileName, list, request, response);
	}
}
