package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.util.List;

import com.xyl.mmall.oms.dto.ReceiptReportDTO;

public class ReceiptVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5302416013235112527L;


	//日期
	private long date;
	
	//当天发货总数
	private int count;
	
	//各时间段的妥投情况
	private List<ReceiptReportDTO> list;

	//妥投率
	private double rate;
	
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ReceiptReportDTO> getList() {
		return list;
	}

	public void setList(List<ReceiptReportDTO> list) {
		this.list = list;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

}
