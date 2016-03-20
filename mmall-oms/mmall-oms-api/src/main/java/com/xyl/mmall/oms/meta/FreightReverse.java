package com.xyl.mmall.oms.meta;

import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 反向运费报表，在未妥投的情况下会产生反向运费
 * 
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "Oms 反向运费", tableName = "Mmall_Oms_FreightReverse")
public class FreightReverse extends FreightBase {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "反向快递费率")
	private BigDecimal reverseRate = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向快递手续费")
	private BigDecimal reverseCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向保价费")
	private BigDecimal reverseInsuranceCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向运费小计 = 反向保价费 + 反向快递手续费")
	private BigDecimal reverseTotalCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向服务费，一般为固定的值，按单收取")
	private BigDecimal reverseServiceCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向快递公司")
	private String returnExpressCompany;

	@AnnonOfField(desc = "反向快递单号")
	private String returnExpressNo;

	public FreightReverse() {
		super();
	}

	public FreightReverse(FreightBase base) {
		ReflectUtil.convertObj(this, base, false);
		setCreateTime(System.currentTimeMillis());
	}

	public BigDecimal getReverseRate() {
		return reverseRate;
	}

	public BigDecimal getReverseCharge() {
		return reverseCharge;
	}

	public BigDecimal getReverseInsuranceCharge() {
		return reverseInsuranceCharge;
	}

	public BigDecimal getReverseTotalCharge() {
		return reverseTotalCharge;
	}

	public BigDecimal getReverseServiceCharge() {
		return reverseServiceCharge;
	}

	public void setReverseRate(BigDecimal reverseRate) {
		this.reverseRate = reverseRate;
	}

	public void setReverseRate(float value) {
		setReverseRate(new BigDecimal(value));
	}

	public void setReverseCharge(BigDecimal reverseCharge) {
		this.reverseCharge = reverseCharge;
	}

	public void setReverseInsuranceCharge(BigDecimal reverseInsuranceCharge) {
		this.reverseInsuranceCharge = reverseInsuranceCharge;
	}

	public void setReverseTotalCharge(BigDecimal reverseTotalCharge) {
		this.reverseTotalCharge = reverseTotalCharge;
	}

	public void setReverseServiceCharge(BigDecimal reverseServiceCharge) {
		this.reverseServiceCharge = reverseServiceCharge;
	}

	public String getReturnExpressCompany() {
		return returnExpressCompany;
	}

	public void setReturnExpressCompany(String returnExpressCompany) {
		this.returnExpressCompany = returnExpressCompany;
	}

	public String getReturnExpressNo() {
		return returnExpressNo;
	}

	public void setReturnExpressNo(String returnExpressNo) {
		this.returnExpressNo = returnExpressNo;
	}

}
