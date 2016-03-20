package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.report.meta.OmsReceiptReport;

/**
 * 
 * @author hzliujie
 * 2014年12月11日 上午11:29:01
 */
public class ReceiptReportDTO extends OmsReceiptReport{

	/**
	 * OmsReceiptReport
	 */
	private static final long serialVersionUID = -1011795845154425296L;

	/**
	 * 占比
	 */
	private double rate;

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public ReceiptReportDTO(OmsReceiptReport obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
