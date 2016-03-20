package com.xyl.mmall.itemcenter.dao.product;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.PoProductUserFav;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;

public interface PoProductUserFavDao extends AbstractDao<PoProductUserFav> {

	/**
	 * 判断用户是否收藏了对应的商品
	 * @param userId
	 * @param poId
	 * @return
	 */
	public boolean isPoProductInFavList(long userId, long poId);
	/**
	 * 添加商品收藏
	 * @param userId
	 * @param poId
	 * @return
	 */
	public boolean addPoProductIntoFavList(long userId, long poId);
	/**
	 * 删除商品收藏
	 * @param userId
	 * @param poId
	 * @return
	 */
	public boolean removePoProductFromFavList(long userId, long poId);
	
	
	/**
	 * 根据商品Id删除商品收藏
	 * @param productId
	 * @return
	 */
	public boolean removeProductFromFavListByProId(long productId);
	
	
	/**
	 * 根据商品Ids批量删除商品收藏
	 * @param productIds
	 * @return
	 */
	public boolean batchRemoveProductFromFavListByProIds(List<Long> productIds);
	
	
	/**
	 * 计算商品被多少人收藏
	 * @param poId
	 * @return
	 */
	public int getPoProductFavCount(long poId);
	
	/**
	 * 根据userid或者商品Ids取收藏商品
	 * @param userId
	 * @param poIds
	 * @return
	 */
	public List<PoProductUserFav> getPoProductFavListByUserIdOrPoIds(Long userId, List<Long>poIds);
	
	
	/**
	 * 分页取收藏商品
	 * @param param
	 * @return
	 */
	public List<PoProductUserFav> getPageProductUserFavDTOByUserId(ProductUserFavParam param);
	
}
