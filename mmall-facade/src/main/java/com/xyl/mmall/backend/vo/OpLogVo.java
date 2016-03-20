/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengdan
 *
 */
public class OpLogVo implements Serializable{
  /**
	 * 序列
	 */
	private static final long serialVersionUID = 1895244179106628006L;
	
	private int sequence;
	
	private String operContent;
	
	private String operator;
	
	private String operTime;

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getOperContent() {
		return operContent;
	}

	public void setOperContent(String operContent) {
		this.operContent = operContent;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperTime() {
		return operTime;
	}

	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
  
  
}
