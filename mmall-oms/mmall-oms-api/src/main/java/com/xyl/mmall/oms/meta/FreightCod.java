package com.xyl.mmall.oms.meta;

import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 应收COD收款
 * 
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "Oms COD收款", tableName = "Mmall_Oms_FreightCod")
public class FreightCod extends FreightBase {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "cod手续费")
	private BigDecimal codCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向快递费率")
	private BigDecimal reverseRate = BigDecimal.ZERO;

	@AnnonOfField(desc = "反向手续费")
	private BigDecimal reverseCharge = BigDecimal.ZERO;
	
	@AnnonOfField(desc = "反向服务费，一般为固定的值，按单收取")
	private BigDecimal reverseServiceCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "偏远地区cod服务费")
	private BigDecimal educationCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "COD应收款，可能为负数，表示应付快递公司的money")
	private BigDecimal codCollection = BigDecimal.ZERO;

	public FreightCod() {
		super();
	}

	public FreightCod(FreightBase base) {
		ReflectUtil.convertObj(this, base, false);
		setCreateTime(System.currentTimeMillis());
	}

	public BigDecimal getCodCharge() {
		return codCharge;
	}

	public void setCodCharge(BigDecimal codCharge) {
		this.codCharge = codCharge;
	}

	public BigDecimal getReverseRate() {
		return reverseRate;
	}

	public void setReverseRate(BigDecimal reverseRate) {
		this.reverseRate = reverseRate;
	}

	public void setReverseRate(float value) {
		setReverseRate(new BigDecimal(value));
	}

	public BigDecimal getReverseCharge() {
		return reverseCharge;
	}

	public void setReverseCharge(BigDecimal reverseCharge) {
		this.reverseCharge = reverseCharge;
	}
	
	public BigDecimal getReverseServiceCharge() {
		return reverseServiceCharge;
	}

	public void setReverseServiceCharge(BigDecimal reverseServiceCharge) {
		this.reverseServiceCharge = reverseServiceCharge;
	}

	public BigDecimal getEducationCharge() {
		return educationCharge;
	}

	public void setEducationCharge(BigDecimal educationCharge) {
		this.educationCharge = educationCharge;
	}

	public BigDecimal getCodCollection() {
		return codCollection;
	}

	public void setCodCollection(BigDecimal codCollection) {
		this.codCollection = codCollection;
	}

}
