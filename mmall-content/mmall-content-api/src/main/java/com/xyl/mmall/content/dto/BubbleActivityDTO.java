/**
 * 
 */
package com.xyl.mmall.content.dto;

import java.io.Serializable;

/**
 * @author hzlihui2014
 *
 */
public class BubbleActivityDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	// 0 普通，1彩色
	private int type;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}
