package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.common.util.DateFormatUtil;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;

/**
 * 供应商档期首付款收入确认单.
 * 
 * @author wangfeng
 *
 */
public class FinanceFirstPayConfirmVO implements Serializable {

	private static final long serialVersionUID = 7018460300204279300L;

	private long poId;

	private String startDate;

	private String endDate;

	private long supplierId;

	private String supplierName;

	/** 平台技术服务费占比(%). */
	private BigDecimal platformSrvFeeRate = BigDecimal.ZERO;

	/** 商品销售总额(零售价). */
	private BigDecimal totalOriRPrice = BigDecimal.ZERO;

	/** 首付款. */
	private BigDecimal firstPayPrice = BigDecimal.ZERO;

	public FinanceFirstPayConfirmVO() {
		super();
	}

	public FinanceFirstPayConfirmVO(ScheduleDTO scheduleDTO) {
		super();
		Schedule schedule = scheduleDTO.getSchedule();
		if (schedule != null) {
			this.poId = schedule.getId();
			this.startDate = DateFormatUtil.getFormatDateType5(schedule.getStartTime());
			this.endDate = DateFormatUtil.getFormatDateType5(schedule.getEndTime());
			this.supplierId = schedule.getSupplierId();
			this.supplierName = schedule.getSupplierName();
		}

		ScheduleVice scheduleVice = scheduleDTO.getScheduleVice();
		if (scheduleVice != null && scheduleVice.getPlatformSrvFeeRate() != null) {
			this.platformSrvFeeRate = scheduleVice.getPlatformSrvFeeRate();
		}
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public BigDecimal getPlatformSrvFeeRate() {
		return platformSrvFeeRate;
	}

	public void setPlatformSrvFeeRate(BigDecimal platformSrvFeeRate) {
		this.platformSrvFeeRate = platformSrvFeeRate;
	}

	public BigDecimal getTotalOriRPrice() {
		return totalOriRPrice;
	}

	public void setTotalOriRPrice(BigDecimal totalOriRPrice) {
		this.totalOriRPrice = totalOriRPrice;
	}

	public BigDecimal getFirstPayPrice() {
		return firstPayPrice;
	}

	public void setFirstPayPrice(BigDecimal firstPayPrice) {
		this.firstPayPrice = firstPayPrice;
	}

}
