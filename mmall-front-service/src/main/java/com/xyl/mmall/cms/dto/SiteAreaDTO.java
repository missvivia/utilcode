/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.meta.SiteArea;

/**
 * SiteAreaDTO.java created by yydx811 at 2015年7月16日 下午4:07:58
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class SiteAreaDTO extends SiteArea {

	/** 序列化id. */
	private static final long serialVersionUID = -7141315583158859777L;

	public SiteAreaDTO() {
	}
	
	public SiteAreaDTO(SiteArea obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
