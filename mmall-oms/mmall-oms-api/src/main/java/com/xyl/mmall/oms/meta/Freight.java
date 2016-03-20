package com.xyl.mmall.oms.meta;

import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 正向运费报表，只要包裹出仓就会产生正向运费
 * 
 * @author hzzengchengyuan
 *
 */
@AnnonOfClass(desc = "Oms 正向运费", tableName = "Mmall_Oms_Freight")
public class Freight extends FreightBase {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "cod手续费")
	private BigDecimal codCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "正向快递费：首重费用  + 续重费用 + 保价费用")
	private BigDecimal expressCharge = BigDecimal.ZERO;

	@AnnonOfField(desc = "仓内费用：分拣包装费 + 耗材费  + 拦截费")
	private BigDecimal warehouseInsideCharge = BigDecimal.ZERO;

	public Freight() {
		super();
	}

	public BigDecimal getCodCharge() {
		return codCharge;
	}

	public void setCodCharge(BigDecimal codCharge) {
		this.codCharge = codCharge;
	}

	public BigDecimal getExpressCharge() {
		return expressCharge;
	}

	public void setExpressCharge(BigDecimal expressCharge) {
		this.expressCharge = expressCharge;
	}

	public BigDecimal getWarehouseInsideCharge() {
		return warehouseInsideCharge;
	}

	public void setWarehouseInsideCharge(BigDecimal warehouseInsideCharge) {
		this.warehouseInsideCharge = warehouseInsideCharge;
	}

}
