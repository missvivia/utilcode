package com.xyl.mmall.cart.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.cart.dto.CartItemDTO;

/**
 * @author Yang,Nan
 *
 */
public interface CartService {

	/**
	 * 购物车错误
	 */
	public static long CART_TIME = 20 * 60 * 1000;

	/**
	 * 购物车错误
	 */
	public static int CART_ERROR = Integer.MIN_VALUE;

	/**
	 * sku超过购买数量 2个
	 */
	public static int CART_OVER_SINGLE_COUNT = -1;

	/**
	 * 购物车超过50个sku
	 */
	public static int CART_OVER_ITMESCOUNT = -2;

	/**
	 * 库存不足
	 */
	public static int CART_NO_INVENTORY = -3;

	/**
	 * 档期过期
	 */
	public static int CART_PO_INVALID = -4;

	/**
	 * 根据userid和省获取购物车
	 * 
	 * @param userId
	 * @return
	 */
	public CartDTO getCart(long userId, int provinceId);

	/**
	 * 购物车 sku 加几件，返回true成功，false失败
	 * 
	 * @param skuId
	 * @param userId
	 * @param deltaCount
	 * @return
	 */
	public int addCartItem(long userId, int provinceId, long skuId, int deltaCount);

	/**
	 * 把list的数据，插入对应type的购物车
	 * 
	 * @param type
	 * @param userId
	 * @param provinceId
	 * @param list
	 * @return
	 */
	boolean addCartItems(String type, long userId, int provinceId, List<CartItemDTO> list);

	/**
	 * 根据skuids删除进货单信息
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuIds
	 * @return
	 */
	public boolean deleteCartItems(long userId, int provinceId, List<Long> skuIds);

	/**
	 * 根据userid和省删除
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deleteCart(long userId, int provinceId);

	/**
	 * 进货单是否已加入
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	public boolean isCartContainSku(long userId, int provinceId, long skuId);

	// 以下方法目前都没使用

	/**
	 * 购物车 sku 减少几件，返回true成功，false失败
	 * 
	 * @param skuId
	 * @param userId
	 * @param deltaCount
	 * @return
	 */
	int decreaseCartItem(long userId, int provinceId, long skuId, int deltaCount);

	/**
	 * 设置 skuid 的初始库存
	 * 
	 * @param skuId
	 * @param count
	 * @return
	 */
	public boolean setInventoryCount(long skuId, int count);

	public boolean addInventoryCount(List<CartItemDTO> list);

	/**
	 * 得到skuid的库存
	 * 
	 * @param ids
	 * @return
	 */
	public Map<Long, Integer> getInventoryCount(List<Long> ids);

	/**
	 * 删除购物车中一项
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	boolean deleteCartItem(long userId, int provinceId, long skuId);

	/**
	 * 重置 时间
	 * 
	 * @param userId
	 * @param provinceId
	 * @return
	 */
	public long resetTime(long userId, int provinceId);

	/**
	 * 获取 购物车更新时间
	 * 
	 * @param userId
	 * @param provinceId
	 * @return
	 */
	public long getCartUpdateTime(long userId, int provinceId);

	/**
	 * 获取 购物车剩余时间
	 * 
	 * @param userId
	 * @param provinceId
	 * @return
	 */
	public long getCartLeftTime(long userId, int provinceId);

	/**
	 * 将用户加入库存提醒列表 后续当这个sku有库存时会将此sku加入到这个用户的购物车
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	public boolean addUserToRemindWhenStorage(long userId, int provinceId, long skuId);

	/**
	 * 取消用户对于sku的库存提醒
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	public boolean removeUserRemind(long userId, int provinceId, long skuId);

	public boolean userExistInRemindStorage(long userId, int provinceId, long skuId);

	/**
	 * 拿到购物车目前有效sku数量
	 * 
	 * @param userId
	 * @param provinceId
	 * @return
	 */
	int getCartValidCount(long userId, int provinceId);

	/**
	 * 恢复最后一次删除过的物品到购物车
	 * 
	 * @param userid
	 * @param provinceId
	 * @return
	 */
	public int recoverCart(long userid, int provinceId);

	/**
	 * 删除过期队列中元素
	 * 
	 * @param userid
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	boolean removeOverTimeCartItem(long userid, int provinceId, long skuId);

	/**
	 * 移除 删除队列中元素
	 * 
	 * @param userid
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	boolean removeDeletedCartItem(long userid, int provinceId, long skuId);

	boolean deleteValidCart(long userId, int provinceId);

	boolean deleteDeletedReocordFromCart(long userId, int provinceId);

	/**
	 * 每次用户往购物车放入数据时，都需要往cache中保存用户购物车对应的有效时间
	 * 
	 * @param userId
	 *            用户id
	 * @param areaId
	 *            省区域id
	 * @param updateTime
	 *            当前用户更新时间
	 * @return
	 */
	public boolean addCartUpdateTimeToCache(long userId, int areaId, Date updateTime);

	/**
	 * 得到购物车中有效的某一个sku的数量
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	int getValidCartItemCount(long userId, int provinceId, long skuId);

	/**
	 * 下单后，删除购物车，这个删除，不用恢复库存
	 * 
	 * @param userId
	 * @param provinceId
	 * @param selectedIds
	 * @return
	 */
	boolean deleteCartForOrder(long userId, int provinceId, List<Long> selectedIds);

	/**
	 * 带记忆的删除，删除动作后，会把删除的放入删除池，待恢复
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuIds
	 * @return
	 */
	boolean deleteCartItemsWithRecord(long userId, int provinceId, List<Long> skuIds);

	/**
	 * 删除购物车中有效sku，但是不改库存了
	 * 
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	boolean deleteCartItemsWithoutAdjustInventory(long userId, int provinceId, List<Long> skuIds);

	boolean addInvalidCartItems(long userId, int provinceId, List<CartItemDTO> list);

	boolean clearInvalidCartItems(long userId, int provinceId);

	boolean removeInvalidCartItems(long userid, int provinceId, List<Long> skuIds);

	/**
	 * 减缓存库存，下单时调用
	 * 
	 * @param list
	 * @return
	 */
	public int decreaseInventory(List<CartItemDTO> list);
	
	/**
	 * 保存商品选中状态，并返回选中项selectedSkuIds
	 * @param userId
	 * @param provinceId
	 * @param selectedSkuIds null:删除选中商品；selectedSkuIds.length==0：不更新缓存，返回保存的选中SkuIds；否则，保存selectedSkuIds指定的选中skuids
	 * @return
	 */
	Long[] selectCartItems(long userId, int provinceId, Long[] selectedSkuIds);
	
	/**
	 * 取消商品选中状态，并返回选中项selectedSkuIds
	 * @param userId
	 * @param provinceId
	 * @param unselectSkuIds
	 * @return
	 */
	Long[] unselectCartItems(long userId, int provinceId, Long[] unselectSkuIds);

}
