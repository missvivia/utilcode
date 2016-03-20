/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.xyl.mmall.backend.util.DateUtils;
import com.xyl.mmall.oms.dto.FreightCodDTO;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;

/**
 * @author hzzengchengyuan
 *
 */
public class FreightCodVO extends FreightCodDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	private int indexNo;
	
	String shipTimeFormat;
	
	String packageStateDesc;
	
	String isCodDesc;
	
	String isInsuranceDesc;
	
	String isOnePiceDesc;
	
	String insuranceRateFormat;
	
	String codRateFormat;
	
	String reverseRateFormat;
	
	String stateUpdateTimeFormat;
	
	String weightKg;
	

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public String getShipTimeFormat() {
		long time = getShipTime();
		if (time == 0) {
			return FreightVO.DEFAULT_NULL_LABLE;
		} else {
			return DateUtils.parseToStringtime(time);
		}
	}

	public String getPackageStateDesc() {
		OmsOrderPackageState state = getPackageState();
		if (state == null) {
			return FreightVO.DEFAULT_NULL_LABLE;
		} else {
			return state.getDesc();
		}
	}

	public String getIsCodDesc() {
		return isCode() ? "是" : "否";
	}

	public String getIsInsuranceDesc() {
		return isInsurance() ? "是" : "否";
	}

	public String getIsOnePiceDesc() {
		return isOnePrice() ? "是" : "否";
	}

	public String getInsuranceRateFormat() {
		double rate = getInsuranceRate().doubleValue();
		if (rate == 0) {
			return FreightVO.DEFAULT_NULL_LABLE;
		} else {
			NumberFormat fmt = NumberFormat.getPercentInstance();
			fmt.setMaximumFractionDigits(FreightVO.DEFAULT_DECIMAL);
			return fmt.format(rate);
		}
	}

	public String getCodRateFormat() {
		double rate = getCodRate().doubleValue();
		if (rate == 0) {
			return FreightVO.DEFAULT_NULL_LABLE;
		} else {
			NumberFormat fmt = NumberFormat.getPercentInstance();
			fmt.setMaximumFractionDigits(FreightVO.DEFAULT_DECIMAL);
			return fmt.format(rate);
		}
	}

	public String getReverseRateFormat() {
		double rate = getReverseRate().doubleValue();
		if (rate == 0) {
			return FreightVO.DEFAULT_NULL_LABLE;
		} else {
			NumberFormat fmt = NumberFormat.getPercentInstance();
			fmt.setMaximumFractionDigits(FreightVO.DEFAULT_DECIMAL);
			return fmt.format(rate);
		}
	}
	
	public String getStateUpdateTimeFormat() {
		long time = getStateUpdateTime();
		if (time == 0) {
			return FreightVO.DEFAULT_NULL_LABLE;
		} else {
			return DateUtils.parseToStringtime(time);
		}
	}

	public String getWeightKg() {
		return String.valueOf(getWeight().divide(new BigDecimal(1000)));
	}

}
