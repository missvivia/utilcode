package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 
 * @author hzliujie 2014年12月18日 下午3:18:36
 */
@AnnonOfClass(desc = "全国发货概括", tableName = "Mmall_Oms_Report_SendOutCountryForm")
public class OmsSendOutCountryForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "全国发货单量")
	private int total;

	@AnnonOfField(desc = "快递公司", type = "varchar(255)")
	private String expressName;

	@AnnonOfField(desc = "COD数量")
	private int cod;

	@AnnonOfField(desc = "非COD数量")
	private int noncode;

	@AnnonOfField(desc = "COD占比")
	private BigDecimal codRate;

	@AnnonOfField(desc = "占比")
	private BigDecimal rate;

	@AnnonOfField(desc = "合计")
	private int num;

	@AnnonOfField(desc = "日期")
	private long date;

	@AnnonOfField(desc = "创建日期")
	private long createTime;

	@AnnonOfField(desc = "更新日期")
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public int getNoncode() {
		return noncode;
	}

	public void setNoncode(int noncode) {
		this.noncode = noncode;
	}

	public BigDecimal getCodRate() {
		if (codRate != null)
			codRate = codRate.setScale(4, RoundingMode.HALF_UP);
		return codRate;
	}

	public void setCodRate(BigDecimal codRate) {
		this.codRate = codRate;
	}

	public BigDecimal getRate() {
		if (rate != null)
			rate = rate.setScale(4, RoundingMode.HALF_UP);
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
