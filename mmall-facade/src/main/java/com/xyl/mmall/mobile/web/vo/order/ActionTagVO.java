package com.xyl.mmall.mobile.web.vo.order;

import java.io.Serializable;

/**
 * 活动Tag
 * 
 * @author dingmingliang
 *
 */
public class ActionTagVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 描述
	 */
	private String desc;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}	
}
