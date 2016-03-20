/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * ems物流运输轨迹信息，多个轨迹集合对象
 * 
 * @author hzzengchengyuan
 *
 */
@XmlRootElement(name = "RequestEmsInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmsTraceInfo implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "row")
	private List<EmsTrace> rows;

	/**
	 * @return the rows
	 */
	public List<EmsTrace> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<EmsTrace> rows) {
		this.rows = rows;
	}

}
