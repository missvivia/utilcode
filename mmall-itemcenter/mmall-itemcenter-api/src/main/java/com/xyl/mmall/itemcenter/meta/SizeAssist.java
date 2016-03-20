package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 尺码助手meta对象
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_SizeAssist", desc = "尺码助手数据库")
public class SizeAssist implements Serializable {

	private static final long serialVersionUID = 6867522844685639078L;

	/** 尺码助手id */
	@AnnonOfField(desc = "主键，尺码助手id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/** 所属商家id */
	@AnnonOfField(desc = "所属商家id", policy = true)
	private long supplierId;

	/** 尺码助手名称 */
	@AnnonOfField(desc = "尺码助手名称")
	private String name;

	/** 横轴名称 */
	@AnnonOfField(desc = "横轴名称")
	private String haxisName;

	/** 纵轴名称 */
	@AnnonOfField(desc = "纵轴名称")
	private String vaxisName;

	/** 尺码助手横轴值 */
	@AnnonOfField(desc = "尺码助手横轴值")
	private String haxisValue;

	/** 尺码助手纵轴值 */
	@AnnonOfField(desc = "尺码助手纵轴值")
	private String vaxisValue;

	/** 尺码助手内容 */
	@AnnonOfField(desc = "尺码助手内容")
	private String body;

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

	public String getHaxisName() {
		return haxisName;
	}

	public void setHaxisName(String haxisName) {
		this.haxisName = haxisName;
	}

	public String getVaxisName() {
		return vaxisName;
	}

	public void setVaxisName(String vaxisName) {
		this.vaxisName = vaxisName;
	}

	public String getHaxisValue() {
		return haxisValue;
	}

	public void setHaxisValue(String haxisValue) {
		this.haxisValue = haxisValue;
	}

	public String getVaxisValue() {
		return vaxisValue;
	}

	public void setVaxisValue(String vaxisValue) {
		this.vaxisValue = vaxisValue;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
}
