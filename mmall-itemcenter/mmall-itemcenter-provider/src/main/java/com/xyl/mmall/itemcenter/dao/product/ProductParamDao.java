package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ProductParameter;

/**
 * 商品参数类dao
 * 
 * @author hzhuangluqian
 *
 */
public interface ProductParamDao extends AbstractDao<ProductParameter> {
	/**
	 * 根据类目id查询该类目的商品参数列表
	 * 
	 * @param productId
	 * @return
	 */
	public List<ProductParameter> getParamList(List<Long> ids);
	
	public ProductParameter getObjectById(long id);
}
