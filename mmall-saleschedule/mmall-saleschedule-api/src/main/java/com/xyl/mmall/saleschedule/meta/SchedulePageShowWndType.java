package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 品购页商品橱窗样式。 橱窗=1个banner+N个坑位。banner可以添加图片。坑位可以加sku。 估计是常量表。即预定义一些样式供用户使用。
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "品购表", tableName = "Mmall_SaleSchedule_SchedulePageShowWndType")
public class SchedulePageShowWndType implements Serializable {

	private static final long serialVersionUID = 7219398482291821687L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	// 1-上；2-下；3-做；4-右
	@AnnonOfField(desc = "banner位置")
	private int bannerPos;

	@AnnonOfField(desc = "商品坑位数量")
	private int prdPosCnt;

	@AnnonOfField(desc = "橱窗名称")
	private String showWndName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBannerPos() {
		return bannerPos;
	}

	public void setBannerPos(int bannerPos) {
		this.bannerPos = bannerPos;
	}

	public int getPrdPosCnt() {
		return prdPosCnt;
	}

	public void setPrdPosCnt(int prdPosCnt) {
		this.prdPosCnt = prdPosCnt;
	}

	public String getShowWndName() {
		return showWndName;
	}

	public void setShowWndName(String showWndName) {
		this.showWndName = showWndName;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
