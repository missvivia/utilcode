/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ItemSPU;

/**
 * ItemSPUDTO.java created by yydx811 at 2015年5月6日 下午6:52:27
 * 单品dto
 *
 * @author yydx811
 */
public class ItemSPUDTO extends ItemSPU {

	/** 序列化id. */
	private static final long serialVersionUID = -7519950476694741607L;
	
	/**
	 * 商品分类Ids，以,分割
	 */
	private String categoryNormalIds;

	public ItemSPUDTO() {
	}

	public ItemSPUDTO(ItemSPU obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public String getCategoryNormalIds() {
		return categoryNormalIds;
	}

	public void setCategoryNormalIds(String categoryNormalIds) {
		this.categoryNormalIds = categoryNormalIds;
	}
	
	
}
