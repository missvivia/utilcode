/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ProdSpeci;

/**
 * ProdSpeciDTO.java created by yydx811 at 2015年5月14日 下午8:40:19
 * 商品规格dto
 *
 * @author yydx811
 */
public class ProdSpeciDTO extends ProdSpeci {

	/** 序列化id. */
	private static final long serialVersionUID = -7839142781455918658L;

	public ProdSpeciDTO() {
	}

	public ProdSpeciDTO(ProdSpeci obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
