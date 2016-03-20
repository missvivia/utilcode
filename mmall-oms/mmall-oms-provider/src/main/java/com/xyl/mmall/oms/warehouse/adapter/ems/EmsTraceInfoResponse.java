/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * ems订单轨迹推送到oms后，oms对所以轨迹响应对象的集合
 * 
 * @author hzzengchengyuan
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmsTraceInfoResponse implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "row")
	private List<EmsTraceResponse> rows;

	/**
	 * @return the rows
	 */
	public List<EmsTraceResponse> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<EmsTraceResponse> rows) {
		this.rows = rows;
	}

	public void addRow(EmsTraceResponse row) {
		if (this.rows == null) {
			this.rows = new ArrayList<EmsTraceResponse>();
		}
		this.rows.add(row);
	}
}
