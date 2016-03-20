package com.xyl.mmall.content.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.meta.NCSContentDispatchLog;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public class NCSContentDispatchLogDTO extends NCSContentDispatchLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NCSContentDispatchLogDTO() {
	}
	
	public NCSContentDispatchLogDTO(NCSContentDispatchLog obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

}
