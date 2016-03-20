package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.report.meta.OmsSendOutReport;

/**
 * 
 * @author hzliujie
 * 2014年12月11日 上午11:29:01
 */
public class SendOutReportDTO extends  OmsSendOutReport{

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
	
	public SendOutReportDTO(OmsSendOutReport obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
