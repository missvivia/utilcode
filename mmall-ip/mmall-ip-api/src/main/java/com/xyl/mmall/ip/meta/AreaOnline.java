package com.xyl.mmall.ip.meta;

import java.io.Serializable;
import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * AreaOnline.java created by yydx811 at 2016年1月8日 下午1:41:21
 * 上线区域
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "上线的区域", tableName = "Mmall_Area_Online")
public class AreaOnline implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "主键", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "区域代码", policy = true)
	private long code;
	
	@AnnonOfField(desc = "区域名称")
	private String name;
	
	@AnnonOfField(desc="状态,0:未开通,1:已开通")
	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
