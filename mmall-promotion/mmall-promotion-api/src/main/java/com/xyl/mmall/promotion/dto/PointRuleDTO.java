/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.PointRule;

/**
 * PointRuleDTO.java created by yydx811 at 2015年12月23日 下午4:01:03
 * 积分规则dto
 *
 * @author yydx811
 */
public class PointRuleDTO extends PointRule {

	/** 序列化id. */
	private static final long serialVersionUID = -609468088096143278L;

	public PointRuleDTO() {
	}
	
	public PointRuleDTO(PointRule obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
