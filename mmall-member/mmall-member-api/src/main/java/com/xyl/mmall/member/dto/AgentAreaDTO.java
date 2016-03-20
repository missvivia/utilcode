/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.AgentArea;

/**
 * AgentAreaDTO.java created by yydx811 at 2015年7月21日 下午5:02:27
 * 管理员区域dto
 *
 * @author yydx811
 */
public class AgentAreaDTO extends AgentArea {

	/** 序列化id. */
	private static final long serialVersionUID = -6255400939723785847L;
	
	public AgentAreaDTO() {
	}

	public AgentAreaDTO(AgentArea obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
