/**
 * 
 */
package com.xyl.mmall.member.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.MobileInfo;

/**
 * @author lihui
 *
 */
public class MobileInfoDTO extends MobileInfo {

	private static final long serialVersionUID = 1L;

	public MobileInfoDTO() {
	}

	public MobileInfoDTO(MobileInfo obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
