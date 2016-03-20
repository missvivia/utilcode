/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.ems;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * ems订单轨迹推送到oms后，oms针对每个轨迹处理后的响应对象
 * 
 * @author hzzengchengyuan
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EmsTraceResponse extends Response implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 233999305589874648L;

	/**
	 * 事务ID
	 */
	private String transaction_id;

	/**
	 * @return the transaction_id
	 */
	public String getTransaction_id() {
		return transaction_id;
	}

	/**
	 * @param transaction_id
	 *            the transaction_id to set
	 */
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

}
