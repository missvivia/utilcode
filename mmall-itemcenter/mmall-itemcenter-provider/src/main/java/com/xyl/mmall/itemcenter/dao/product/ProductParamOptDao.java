package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ProductParamOption;

/**
 * 商品参数下拉框选项dao
 * 
 * @author hzhuangluqian
 *
 */
public interface ProductParamOptDao extends AbstractDao<ProductParamOption> {
	/**
	 * 根据商品参数id获取商品参数选项表
	 * 
	 * @param prodParamId
	 * @return
	 */
	List<ProductParamOption> getOptionList(long prodParamId);
}
