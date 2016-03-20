package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.enums.PictureType;
import com.xyl.mmall.itemcenter.meta.ProdPicMap;

/**
 * 商品图片dao
 * 
 * @author hzhuangluqian
 *
 */
public interface ProductPicDao extends AbstractDao<ProdPicMap> {
	/**
	 * 插入新的商品图片
	 * 
	 * @param prodPicMap
	 * @return
	 */
	public ProdPicMap addNewProdPicMap(ProdPicMap prodPicMap);

	/**
	 * 根据商品id，图片类型，是否是档期商品来查询商品图片类
	 * 
	 * @param pid
	 * @param type
	 * @param isInPo
	 * @return
	 */
	public ProdPicMap getProdPicMap(long pid, PictureType type, int isInPo);

	/**
	 * 更新商品图片类
	 * 
	 * @param map
	 * @return
	 */
	public boolean updateProdPicMap(ProdPicMap map);

	/**
	 * 根据商品id和是否是档期商品删除商品图片
	 * 
	 * @param productId
	 * @param isInPo
	 * @return
	 */
	public boolean deleteProdPicMap(long productId, int isInPo);

	/**
	 * 根据商品id，是否是档期商品来查询商品图片类
	 * 
	 * @param pid
	 * @param isInPo
	 * @return
	 */
	public List<ProdPicMap> getProdPicMap(long pid, int isInPo);

	public ProdPicMap getProdPicMapNoCache(long pid, PictureType type, int isInPo);

	public List<ProdPicMap> getProdPicMap(PictureType type, int isInPo);

}
