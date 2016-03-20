/**
 * 
 */
package com.xyl.mmall.backend.vo;

/**
 * @author hzzengchengyuan
 *
 */
public class EnumBean {

	private int value;

	private String code;

	private String desc;

	public EnumBean() {

	}

	/**
	 * @param value
	 * @param code
	 * @param desc
	 */
	public EnumBean(int value, String code, String desc) {
		super();
		this.value = value;
		this.code = code;
		this.desc = desc;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
