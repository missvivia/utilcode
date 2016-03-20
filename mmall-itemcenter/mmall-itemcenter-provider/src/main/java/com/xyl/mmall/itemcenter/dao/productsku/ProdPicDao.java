/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.ProdPic;

/**
 * ProdPicDao.java created by yydx811 at 2015年5月15日 下午2:37:39
 * 商品图片dao
 *
 * @author yydx811
 */
public interface ProdPicDao extends AbstractDao<ProdPic> {
	
	/**
	 * 根据商品Id删除商品图片
	 * @param proId
	 * @return
	 */
	public boolean deleteProdPicByProductId(long proId);
	
	/**
	 * 批量添加商品图片
	 * @param picList
	 * @return
	 */
	public boolean addBulkProdPics(List<ProdPic> picList);
	
	/**
	 * 根据skuid获取图片列表
	 * @param skuId
	 * @return
	 */
	public List<ProdPic> getPicListBySKUId(long skuId);
	
	/**
	 * 根据skuids获取图片列表
	 * @param skuId
	 * @return
	 */
	public List<ProdPic> getPicListBySKUIds(List<Long> skuIds);
	
	/**
	 * 批量删除商品图片
	 * @param proIds
	 * @return
	 */
	public boolean batchDeleteProdPic(List<Long>proIds);

	/**
	 * 删除商品图片
	 * @param productSKUId
	 * @param id
	 * @return
	 */
	public int deleteProdPic(long productSKUId, long id);
}
