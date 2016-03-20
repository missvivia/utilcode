/**
 * 来货物流信息
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengdan
 *
 */
public class ComeCargoLogisticVo implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = -5689469931422387625L;
	/**时间*/
	private String time;
	/**操作描述*/
	private String operDesc;
	/**操作者*/
	private String operator;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOperDesc() {
		return operDesc;
	}

	public void setOperDesc(String operDesc) {
		this.operDesc = operDesc;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	
}
