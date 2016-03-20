/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.CouponConfig;

/**
 * CouponConfigDTO.java created by yydx811 at 2015年12月30日 下午4:26:35
 * 优惠券配置dto
 *
 * @author yydx811
 */
public class CouponConfigDTO extends CouponConfig {

	/** 序列化id. */
	private static final long serialVersionUID = -1932433615905616996L;

	public CouponConfigDTO() {
	}
	
	public CouponConfigDTO(CouponConfig obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
