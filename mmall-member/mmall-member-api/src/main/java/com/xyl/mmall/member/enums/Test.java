/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.enums;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * Test.java created by yydx811 at 2015年8月6日 下午1:27:25
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@AnnonOfClass(tableName = "test", desc = "")
public class Test implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1952185042806204822L;
	
	@AnnonOfField(desc = "", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "", policy = true)
	private String userId;

	@AnnonOfField(desc = "")
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
