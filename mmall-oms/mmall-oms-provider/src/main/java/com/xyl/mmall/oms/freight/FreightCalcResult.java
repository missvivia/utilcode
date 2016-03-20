/**
 * 
 */
package com.xyl.mmall.oms.freight;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xyl.mmall.oms.meta.FreightTemplet;

/**
 * @author hzzengchengyuan
 *
 */
public class FreightCalcResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 首重费用
	 */
	private BigDecimal startCharge = BigDecimal.ZERO;

	/**
	 * 续重费用
	 */
	private BigDecimal continueCharge = BigDecimal.ZERO;

	/**
	 * 保价费
	 */
	private BigDecimal insuranceCharge = BigDecimal.ZERO;

	/**
	 * 仓内费用
	 */
	private BigDecimal warehouseInsideCharge = BigDecimal.ZERO;

	/**
	 * 正向快递费小计
	 */
	private BigDecimal expressCharge = BigDecimal.ZERO;

	/**
	 * cod手续费
	 */
	private BigDecimal codCharge = BigDecimal.ZERO;

	/**
	 * 应收cod款额
	 */
	private BigDecimal codAmount = BigDecimal.ZERO;

	/**
	 * 反向保价费
	 */
	private BigDecimal reverseInsuranceCharge = BigDecimal.ZERO;

	/**
	 * 反向服务费，一般为固定的值，按单收取
	 */
	private BigDecimal reverseServiceCharge = BigDecimal.ZERO;

	/**
	 * 反向快递费
	 */
	private BigDecimal reverseCharge = BigDecimal.ZERO;

	/**
	 * 反向运费小计
	 */
	private BigDecimal reverseTotalCharge = BigDecimal.ZERO;

	/**
	 * COD应收款，可能为负数，表示应付快递公司的money
	 */
	private BigDecimal codCollection = BigDecimal.ZERO;

	/**
	 * 偏远地区服务费
	 */
	private BigDecimal educationCharge = BigDecimal.ZERO;

	/**
	 * 一口价
	 */
	private BigDecimal onePrice = BigDecimal.ZERO;

	/**
	 * 结果计算所采用的运费模板
	 */
	private FreightTemplet template;

	public BigDecimal getStartCharge() {
		return startCharge;
	}

	public void setStartCharge(BigDecimal startCharge) {
		this.startCharge = startCharge;
	}

	public void setStartCharge(double startCharge) {
		setStartCharge(new BigDecimal(startCharge));
	}

	public BigDecimal getContinueCharge() {
		return continueCharge;
	}

	public void setContinueCharge(BigDecimal continueCharge) {
		this.continueCharge = continueCharge;
	}

	public void setContinueCharge(double value) {
		setContinueCharge(new BigDecimal(value));
	}

	public BigDecimal getInsuranceCharge() {
		return insuranceCharge;
	}

	public void setInsuranceCharge(BigDecimal insuranceCharge) {
		this.insuranceCharge = insuranceCharge;
	}

	public void setInsuranceCharge(double value) {
		setInsuranceCharge(new BigDecimal(value));
	}

	public BigDecimal getWarehouseInsideCharge() {
		return warehouseInsideCharge;
	}

	public void setWarehouseInsideCharge(BigDecimal warehouseInsideCharge) {
		this.warehouseInsideCharge = warehouseInsideCharge;
	}

	public void setWarehouseInsideCharge(double value) {
		setWarehouseInsideCharge(new BigDecimal(value));
	}

	public BigDecimal getExpressCharge() {
		return expressCharge;
	}

	public void setExpressCharge(BigDecimal expressCharge) {
		this.expressCharge = expressCharge;
	}

	public void setExpressCharge(double value) {
		setExpressCharge(new BigDecimal(value));
	}

	public BigDecimal getCodCharge() {
		return codCharge;
	}

	public void setCodCharge(BigDecimal codCharge) {
		this.codCharge = codCharge;
	}

	public void setCodCharge(double value) {
		setCodCharge(new BigDecimal(value));
	}

	public BigDecimal getCodAmount() {
		return codAmount;
	}

	public void setCodAmount(BigDecimal codAmount) {
		this.codAmount = codAmount;
	}

	public void setCodAmount(double value) {
		setCodAmount(new BigDecimal(value));
	}

	public BigDecimal getReverseInsuranceCharge() {
		return reverseInsuranceCharge;
	}

	public void setReverseInsuranceCharge(BigDecimal reverseInsuranceCharge) {
		this.reverseInsuranceCharge = reverseInsuranceCharge;
	}

	public void setReverseInsuranceCharge(double value) {
		setReverseInsuranceCharge(new BigDecimal(value));
	}

	public BigDecimal getReverseServiceCharge() {
		return reverseServiceCharge;
	}

	public void setReverseServiceCharge(BigDecimal reverseServiceCharge) {
		this.reverseServiceCharge = reverseServiceCharge;
	}

	public void setReverseServiceCharge(double value) {
		setReverseServiceCharge(new BigDecimal(value));
	}

	public BigDecimal getReverseCharge() {
		return reverseCharge;
	}

	public void setReverseCharge(BigDecimal reverseCharge) {
		this.reverseCharge = reverseCharge;
	}

	public void setReverseCharge(double value) {
		setReverseCharge(new BigDecimal(value));
	}

	public BigDecimal getReverseTotalCharge() {
		return reverseTotalCharge;
	}

	public void setReverseTotalCharge(BigDecimal reverseTotalCharge) {
		this.reverseTotalCharge = reverseTotalCharge;
	}

	public void setReverseTotalCharge(double value) {
		setReverseTotalCharge(new BigDecimal(value));
	}

	public BigDecimal getCodCollection() {
		return codCollection;
	}

	public void setCodCollection(BigDecimal codCollection) {
		this.codCollection = codCollection;
	}

	public void setCodCollection(double value) {
		setCodCollection(new BigDecimal(value));
	}

	public BigDecimal getEducationCharge() {
		return educationCharge;
	}

	public void setEducationCharge(BigDecimal educationCharge) {
		this.educationCharge = educationCharge;
	}

	public void setEducationCharge(double value) {
		setEducationCharge(new BigDecimal(value));
	}

	public BigDecimal getOnePrice() {
		return onePrice;
	}

	public void setOnePrice(BigDecimal onePrice) {
		this.onePrice = onePrice;
	}

	public void setOnePrice(double value) {
		setOnePrice(new BigDecimal(value));
	}

	public FreightTemplet getTemplate() {
		return template;
	}

	public void setTemplate(FreightTemplet template) {
		this.template = template;
	}

	/**
	 * 将结果四舍五入取值
	 * 
	 * @param decimal
	 *            保留小数点位数
	 */
	public void formatterRoundHalfUp(int decimal) {
		setCodAmount(getCodAmount().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setCodCharge(getCodCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setCodCollection(getCodCollection().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setContinueCharge(getContinueCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setEducationCharge(getEducationCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setExpressCharge(getExpressCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setInsuranceCharge(getInsuranceCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setOnePrice(getOnePrice().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setReverseCharge(getReverseCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setReverseInsuranceCharge(getReverseInsuranceCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setReverseServiceCharge(getReverseServiceCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setReverseTotalCharge(getReverseTotalCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setStartCharge(getStartCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
		setWarehouseInsideCharge(getWarehouseInsideCharge().setScale(decimal, BigDecimal.ROUND_HALF_UP));
	}

}
