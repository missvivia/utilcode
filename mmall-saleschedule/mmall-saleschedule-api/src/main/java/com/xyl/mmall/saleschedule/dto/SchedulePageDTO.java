package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.meta.SchedulePage;

/**
 * 品购页DTO
 * 
 * @author hzzhanghui
 * 
 */
public class SchedulePageDTO implements Serializable {

	private static final long serialVersionUID = 4707221223487158574L;

	private SchedulePage page;

	public SchedulePage getPage() {
		return page;
	}

	public void setPage(SchedulePage page) {
		this.page = page;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}

}
