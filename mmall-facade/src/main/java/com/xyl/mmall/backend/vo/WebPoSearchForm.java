/**
 * po单查询条件对象
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.cms.vo.PagerConditionAO;

/**
 * @author hzzengdan
 *
 */
public class WebPoSearchForm extends PagerConditionAO implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = -1698798232398934428L;
	
	/**po单编号*/
	private String poOrderId;
	/**创建开始时间*/
	private String createStartTime;
	/**创建截止时间*/
	private String createEndTime;
	/**开售起始时间*/
	private String saleStartTime;
	/**开售截止时间*/
	private String saleEndTime;
	/**停售开始时间*/
	private String stopSaleStartTime;
	/**停售截止时间*/
	private String stopSaleEndTime;

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public String getCreateStartTime() {
		return createStartTime;
	}

	public void setCreateStartTime(String createStartTime) {
		this.createStartTime = createStartTime;
	}

	public String getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(String createEndTime) {
		this.createEndTime = createEndTime;
	}

	public String getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	public String getSaleEndTime() {
		return saleEndTime;
	}

	public void setSaleEndTime(String saleEndTime) {
		this.saleEndTime = saleEndTime;
	}

	public String getStopSaleStartTime() {
		return stopSaleStartTime;
	}

	public void setStopSaleStartTime(String stopSaleStartTime) {
		this.stopSaleStartTime = stopSaleStartTime;
	}

	public String getStopSaleEndTime() {
		return stopSaleEndTime;
	}

	public void setStopSaleEndTime(String stopSaleEndTime) {
		this.stopSaleEndTime = stopSaleEndTime;
	}

	
}
