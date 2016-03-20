package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.HBRecycleLog;

public class HBRecycleLogDTO extends HBRecycleLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4232388157129932179L;
	
	public HBRecycleLogDTO() {
	}

	public HBRecycleLogDTO(HBRecycleLog obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
}
