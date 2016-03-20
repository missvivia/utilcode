/**
 * 物流信息DTO
 */
package com.xyl.mmall.oms.dto;

import java.io.Serializable;

/**
 * @author hzzengdan
 * @date 2014-09-15
 */
public class LogisticInfoDTO implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 6827758928648982904L;

	/**
	 * 时间
	 */
	private long time;

	/**
	 * 操作信息
	 */
	private String operation;

	/**
	 * 操作者
	 */
	private String operator;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
