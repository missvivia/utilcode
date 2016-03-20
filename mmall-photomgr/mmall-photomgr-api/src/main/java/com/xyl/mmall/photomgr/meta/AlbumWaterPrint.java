package com.xyl.mmall.photomgr.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 水印设置。 每个用户对应一个水印设置
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "水印", tableName = "Mmall_PhotoMgr_AlbumWaterPrint", policy = "userId")
public class AlbumWaterPrint implements Serializable {

	private static final long serialVersionUID = -2041033147949396195L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private int id;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	// 基准点 1 左上 2 中间 3 右下
	@AnnonOfField(desc = "基准点", notNull = false)
	private int base;

	@AnnonOfField(desc = "左边距", notNull = false)
	private BigDecimal leftMargin;

	@AnnonOfField(desc = "上边距", notNull = false)
	private BigDecimal topMargin;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public BigDecimal getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(BigDecimal leftMargin) {
		this.leftMargin = leftMargin;
	}

	public BigDecimal getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(BigDecimal topMargin) {
		this.topMargin = topMargin;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
