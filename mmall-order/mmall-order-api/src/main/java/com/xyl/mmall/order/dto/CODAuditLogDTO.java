package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.CODAuditLog;

/**
 * 到付申请审核日志DTO
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午9:53:02
 *
 */
public class CODAuditLogDTO extends CODAuditLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1345487835177270273L;

	/**
	 * 
	 */
	public CODAuditLogDTO() {
	}
	
	/**
	 * 
	 * @param obj
	 */
	public CODAuditLogDTO(CODAuditLog obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
}
