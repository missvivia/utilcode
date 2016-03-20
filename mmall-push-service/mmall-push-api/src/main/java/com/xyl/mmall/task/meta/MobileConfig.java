package com.xyl.mmall.task.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 
 * 
 * @author hzjiangww
 * 
 */
@AnnonOfClass(desc = "移动端配置文件", tableName = "Mmall_Mobile_Config")
public class MobileConfig implements Serializable {

	private static final long serialVersionUID = -4914353226193236544L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true, policy = true)
	private long id;

	@AnnonOfField(desc = "配置文件名", type = "varchar(32)", uniqueKey = true)
	private String name;

	@AnnonOfField(desc = "配置文件")
	private String config;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

}
