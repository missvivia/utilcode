/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ProdParam;

/**
 * ProdParamDTO.java created by yydx811 at 2015年5月14日 下午8:42:50
 * 商品属性dto
 *
 * @author yydx811
 */
public class ProdParamDTO extends ProdParam {

	/** 序列化id. */
	private static final long serialVersionUID = -6820583454982755761L;

	public ProdParamDTO() {
	}

	public ProdParamDTO(ProdParam obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
