/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ProdPic;

/**
 * ProdPicDTO.java created by yydx811 at 2015年5月14日 下午8:45:26
 * 商品图片dto
 *
 * @author yydx811
 */
public class ProdPicDTO extends ProdPic {

	/** 序列化id. */
	private static final long serialVersionUID = 1235603709683944953L;

	public ProdPicDTO() {
	}

	public ProdPicDTO(ProdPic obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
