package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.util.List;

import com.xyl.mmall.oms.dto.SendOutReportDTO;

public class SendOutVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5302416013235112527L;


	//日期
	private long date;
	
	//当天发货总数
	private int count;
	
	//各快递公司发货明细
	private List<SendOutReportDTO> list;

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

	public List<SendOutReportDTO> getList() {
		return list;
	}

	public void setList(List<SendOutReportDTO> list) {
		this.list = list;
	}

}
