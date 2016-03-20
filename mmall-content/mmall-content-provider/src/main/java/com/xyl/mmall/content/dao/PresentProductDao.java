/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.meta.PresentProduct;

/**
 * @author hzlihui2014
 *
 */
public interface PresentProductDao extends AbstractDao<PresentProduct> {

	/**
	 * @param saleAreaId
	 * @param param
	 * @return
	 */
	List<PresentProduct> getPresentProductListByAreaId(long saleAreaId, DDBParam param);

}
