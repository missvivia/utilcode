package com.xyl.mmall.cart.dao;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.cart.dto.CartItemDTO;

public interface CartDao {

	/**
	 * 新增时加入进货单,包含新增创建时间缓存
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @param deltaCount
	 * @return
	 */
	public int changeCartItem(long userId, int provinceId, long skuId, int deltaCount);

	/**
	 * 获取进货单列表
	 * 
	 * @param type
	 * @param userId
	 * @param provinceId
	 * @return
	 */
	public List<CartItemDTO> getCart(String type, long userId, int provinceId);

	/**
	 * 进货单时批量更新，不包含创建时间缓存
	 * 
	 * @param type
	 * @param userId
	 * @param provinceId
	 * @param map
	 * @return
	 */
	public int addItems(String type, long userId, int provinceId, Map<byte[], byte[]> map);

	/**
	 * 单个删除进货单信息
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	public int deleteCartItem(long userId, int provinceId, long skuId);

	public int getCartItemCount(String type, long userId, int provinceId, long skuId);

	public boolean deleteCart(String type, long userId, int provinceId);

	public long resetTime(long userId, int provinceId);

	public long getCartTime(long userId, int provinceId);

	public int getLastClearBucketPosition();

	/**
	 * 将某个无库存的sku与某个用户关联 之后这个sku有库存时会选择某个关联用户并将这个sku放入这个用户的购物车
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	public boolean addUserToRemindWhenStorage(long userId, int provinceId, long skuId);

	/**
	 * 移除用户对于某个sku的库存关注
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	public boolean remvoeUserRemind(long userId, int provinceId, long skuId);

	/**
	 * 查询用户是否已经存在于sku库存提醒列表
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	public boolean userExistInRemindStorage(long userId, int provinceId, long skuId);

	/**
	 * 清空这个类型的购物车
	 * 
	 * @param type
	 *            3类：有效 ，超时，无效
	 * @param userId
	 * @param provinceId
	 * @return
	 */
	boolean clearCart(String type, long userId, int provinceId);

	int changeCartItemOnType(String type, long userId, int provinceId, long skuId, int deltaCount);

	int deleteCartItem(String type, long userId, int provinceId, long skuId);

	boolean clearDeletePool(long userId, int provinceId);

	boolean clearDeleteOvertimePool(long userId, int provinceId);

	/**
	 * 保存商品选中状态，并返回选中项selectedSkuIds
	 * @param userId
	 * @param provinceId
	 * @param selectedSkuIds null:删除选中商品；selectedSkuIds.length==0：不更新缓存，返回保存的选中SkuIds；否则，保存selectedSkuIds指定的选中skuids
	 * @return
	 */
	Long[] selectCartItems(long userId, int provinceId, Long[] selectedSkuIds);

}
