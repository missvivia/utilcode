package com.xyl.mmall.saleschedule.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 档期频道信息 初始化： DELETE FROM Mmall_SaleSchedule_Channel; INSERT INTO
 * Mmall_SaleSchedule_Channel(id,name) values(1, "首页"); INSERT INTO
 * Mmall_SaleSchedule_Channel(id,name) values(2, "女装"); INSERT INTO
 * Mmall_SaleSchedule_Channel(id,name) values(3, "男装"); INSERT INTO
 * Mmall_SaleSchedule_Channel(id,name) values(4, "鞋包"); INSERT INTO
 * Mmall_SaleSchedule_Channel(id,name) values(5, "童装"); INSERT INTO
 * Mmall_SaleSchedule_Channel(id,name) values(6, "家纺"); COMMIT;
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "档期频道常量表", tableName = "Mmall_SaleSchedule_Channel")
public class ScheduleChannel implements java.io.Serializable {

	private static final long serialVersionUID = 7103128263226489695L;

	@AnnonOfField(desc = "主键id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "名称")
	private String name;

	@AnnonOfField(desc = "频道图标URL", notNull = false)
	private String iconUrl;

	@AnnonOfField(desc = "频道图标id", notNull = false)
	private long iconId;
	
	@AnnonOfField(desc = "异或标记值，勿改", notNull = false)
	private long flag;

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

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public long getIconId() {
		return iconId;
	}

	public void setIconId(long iconId) {
		this.iconId = iconId;
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
