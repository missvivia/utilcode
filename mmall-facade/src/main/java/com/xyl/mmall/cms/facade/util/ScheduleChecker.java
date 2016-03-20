package com.xyl.mmall.cms.facade.util;

import java.util.List;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.enums.SupplyMode;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * 
 * 
 * @author hzzhanghui
 * 
 */
public final class ScheduleChecker extends BaseChecker {

	public ScheduleChecker(Logger logger) {
		super(logger);
	}

	public ErrChecker checkPOID(Long id) {
		return checkLong(id, "PO id cannot be null!!!");
	}

	public ErrChecker checkPageID(Long id) {
		return checkLong(id, "Decorate page id cannot be null!!!");
	}

	public ErrChecker checkSavePOPage(Long pageId, Long poId, Long brandId) {
		ErrChecker checker = new ErrChecker();
		checker = checkLong(pageId, "Decorate page id cannot be null!!!");
		if (!checker.check) {
			return checker;
		}
		checker = checkLong(poId, "PO id cannot be null!!!");
		if (!checker.check) {
			return checker;
		}
		checker = checkLong(brandId, "Brand id cannot be null!!!");
		if (!checker.check) {
			return checker;
		}

		return checker;
	}

	public ErrChecker checkChangeScheduleDate(Long poId, Long date) {
		ErrChecker checker = new ErrChecker();
		checker = checkLong(poId, "PO id cannot be null!!!");
		if (!checker.check) {
			return checker;
		}
		checker = checkLong(date, "Schedule's new date cannot be null when adjust date!!");
		if (!checker.check) {
			return checker;
		}
		
		

		return checker;
	}

	public ErrChecker checkProvinceId(Long id) {
		return checkLong(id, "Province id cannot be null!!");
	}

	public ErrChecker checkPageId(Long id) {
		return checkLong(id, "Schedule page id cannot be null when audit!!");
	}

	public ErrChecker checkPageIdAndScheduleId(Long pageId, Long scheduleId) {
		ErrChecker checker = new ErrChecker();
		if ((pageId == null || pageId == 0) && (scheduleId == null || scheduleId == 0)) {
			return buildErrChecker(checker, "Id cannot be null!!!");
		}

		return checker;
	}

	public ErrChecker checkResult(boolean result, String msg) {
		return checkBoolean(result, msg);
	}

	public ErrChecker checkFindSchedule(Schedule schedule, long id) {
		return checkObjectNull(schedule, "Cannot find any po with id '" + id + "'!!!");
	}

	public ErrChecker checkSupplierAreaId(Long id) {
		return checkLong(id, "curSupplierAreaId cannot be null!!!");
	}

	public ErrChecker checkCreateScheduleValidation(JSONObject param) {
		ErrChecker checker = new ErrChecker();
		if (param.containsKey("title") && param.getString("title").length() > 24) {
			return buildErrChecker(checker, "PO名称 不能超过12个汉字！！");
		}
		if (param.containsKey("pageTitle") && param.getString("pageTitle").length() > 180) {
			return buildErrChecker(checker, "品购页title不能超过90个汉字！！");
		}
		if (param.containsKey("platformSrvFeeRate")) {
			if (!POBaseUtil.isInteger(param.getString("platformSrvFeeRate"))) {
				return buildErrChecker(checker, "平台服务费率 只能输入整数！！");
			}
			int platformSrvFeeRate = Integer.parseInt(param.getString("platformSrvFeeRate"));
			if (platformSrvFeeRate > 100) {
				return buildErrChecker(checker, "服务费率必须小于100%！！");
			}
		}
		if (param.containsKey("maxPriceAfterDiscount")) {
			if (!POBaseUtil.isDecimalOrInteger(param.getString("maxPriceAfterDiscount"))) {
				return buildErrChecker(checker, "销售价格区间 只运行输入整数和小数！！");
			}
		}
		if (param.containsKey("minPriceAfterDiscount")) {
			if (!POBaseUtil.isDecimalOrInteger(param.getString("minPriceAfterDiscount"))) {
				return buildErrChecker(checker, "销售价格区间 只运行输入整数和小数！！");
			}
		}
		if (param.containsKey("maxDiscount")) {
			if (!POBaseUtil.isInteger(param.getString("maxDiscount"))) {
				return buildErrChecker(checker, "折扣区间 只能输入整数！！");
			}
		}
		if (param.containsKey("minDiscount")) {
			if (!POBaseUtil.isInteger(param.getString("minDiscount"))) {
				return buildErrChecker(checker, "折扣区间 只能输入整数！！");
			}
		}
		if (param.containsKey("unitCnt")) {
			if (!POBaseUtil.isInteger(param.getString("unitCnt"))) {
				return buildErrChecker(checker, "款数 只能输入整数！！");
			}
		}
		if (param.containsKey("skuCnt")) {
			if (!POBaseUtil.isInteger(param.getString("skuCnt"))) {
				return buildErrChecker(checker, "SKU数 只能输入整数！！");
			}
		}
		if (param.containsKey("productTotalCnt")) {
			if (!POBaseUtil.isInteger(param.getString("productTotalCnt"))) {
				return buildErrChecker(checker, "总件数 只能输入整数！！");
			}
		}
		
		return checker;
	}
	public ErrChecker checkCreateSchedule(ScheduleDTO scheduleDTO) {
		ErrChecker checker = new ErrChecker();

		Schedule s = scheduleDTO.getSchedule();
		ScheduleVice sv = scheduleDTO.getScheduleVice();
		List<ScheduleSiteRela> srList = scheduleDTO.getSiteRelaList();

//		if (s.getCurSupplierAreaId() == 0) {
//			return buildErrChecker(checker, "curSupplierAreaId cannot be null!!!");
//		}

//		if (s.getBrandId() == 0) {
//			return buildErrChecker(checker, "brandId cannot be null!!");
//		}

		if (s.getStartTime() == 0) {
			return buildErrChecker(checker, "startTime cannot be null!!");
		}
		
		if (s.getEndTime() == 0) {
			return buildErrChecker(checker, "endTime cannot be null!!");
		}
		
		if (!ScheduleUtil.checkSubmit(s)) {
			return buildErrChecker(checker, "PO startTime should be at least 4 days later from today!!!");
		}

		if (s.getStatus() == ScheduleState.NULL) {
			return buildErrChecker(checker, "status is -1, please check!!");
		}

		if (sv.getAdPosition() == null || "".equals(sv.getAdPosition().trim())) {
			return buildErrChecker(checker, "Please select at least one channel!!!");
		}

		if (s.getTitle() == null || "".equals(s.getTitle().trim())) {
			return buildErrChecker(checker, "PO title cannot be null!!");
		}
		if (s.getPageTitle() == null || "".equals(s.getPageTitle().trim())) {
			return buildErrChecker(checker, "PO page title cannot be null!!");
		}
		if (srList == null || srList.size() == 0) {
			return buildErrChecker(checker, "Please select at least one sale site!!!");
		}
		if (sv.getSupplierAcct() == null || "".equals(sv.getSupplierAcct().trim())) {
			return buildErrChecker(checker, "supplierAcct cannot be null!!!");
		}
		if (sv.getPoFollowerUserName() == null || "".equals(sv.getPoFollowerUserName().trim())) {
			return buildErrChecker(checker, "poFollowerUserName cannot be null!!!");
		}
//		if (sv.getPoFollowerUserId() == 0) {
//			return buildErrChecker(checker, "poFollowerUserId cannot be null!!!");
//		}
		if (sv.getSupplyMode() == null || sv.getSupplyMode() == SupplyMode.NULL) {
			return buildErrChecker(checker, "supplyMode cannot be null!!!");
		}
		return checker;
	}

	public ErrChecker checkStatus(Integer status) {
		ErrChecker checker = new ErrChecker();
		if (status == null) {
			return buildErrChecker(checker, "Status cannot be null!!!");
		}
		if (status < 0 || status > 1) {
			return buildErrChecker(checker, "Status should be 0 or 1, but current value is '" + status + "'!!!");
		}
		return checker;
	}

	public ErrChecker checkBannerPass(List<Long> paramJson) {
		return checkListEmpty(paramJson, "Id cannot be null!!!");
	}

	public ErrChecker checkBannerIds(String bannerIds) {
		ErrChecker checker = new ErrChecker();
		if (bannerIds == null || "".equals(bannerIds.trim()) || "[]".equals(bannerIds.trim())) {
			return buildErrChecker(checker, "Id list cannot be null or empty!!!");
		}
		return checker;
	}

}
