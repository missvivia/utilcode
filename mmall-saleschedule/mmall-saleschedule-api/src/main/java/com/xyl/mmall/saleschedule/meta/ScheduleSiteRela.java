package com.xyl.mmall.saleschedule.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 档期销售站点关系表。 一个档期对应多个站点id，会产生多条记录。 修改的操作是先删后插。
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "档期销售站点关系表", tableName = "Mmall_SaleSchedule_SiteRela", policy = "scheduleId")
public class ScheduleSiteRela implements java.io.Serializable {

	private static final long serialVersionUID = -3664514531537352018L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "档期id")
	private long scheduleId;

	@AnnonOfField(desc = "档期在某站点下的排序", notNull = false, defa = "1")
	private int showOrder;

	@AnnonOfField(desc = "档期开始时间", notNull = false)
	private long poStartTime;
	
	@AnnonOfField(desc = "销售站点id")
	private long saleSiteId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public long getPoStartTime() {
		return poStartTime;
	}

	public void setPoStartTime(long poStartTime) {
		this.poStartTime = poStartTime;
	}

	public long getSaleSiteId() {
		return saleSiteId;
	}

	public void setSaleSiteId(long saleSiteId) {
		this.saleSiteId = saleSiteId;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
