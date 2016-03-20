package com.xyl.mmall.mainsite.facade;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.vo.CartInnervVO;
import com.xyl.mmall.mainsite.vo.CartVO;
import com.xyl.mmall.mainsite.vo.InputParam;
import com.xyl.mmall.promotion.enums.PlatformType;

/**
 * @author Yang,Nan
 *
 */
public interface CartFacade {

	public static int OPERATION_DELETE = 1;

	public CartVO getCart(long userid, int provinceId, List<Long> selectedIds,
			PlatformType platformType);

	public CartVO getCartAfterDelete(long userid, int provinceId,
			List<Long> selectedIds, PlatformType platformType);

	public InputParam recoverCart(long userid, int provinceId);

	public CartVO getCartMini(long userid, int provinceId,
			List<Long> selectedIds);

	public CartVO getCart(long userid, int provinceId, List<Long> selectedIds,
			RetArg retArg, PlatformType platformType);

	public boolean deleteCart(long userid, int provinceId);

	public boolean deleteCartForOrder(long userId, int provinceId,
			String cartIds);

	public int addItemToCart(long userId, int provinceId, long skuId,
			int deltaCount, List<Long> selectedIds);

	public boolean addInventoryCount(List<CartItemDTO> list);
	

	public int addItemToCartAndUpdateTime(long userid, int provinceId,
			long skuId, int deltaCount);

	public boolean setInventoryCount(long skuId, int count);

	public Map<Long, Integer> getInventoryCount(List<Long> ids);

	boolean deleteCartItem(long userId, int provinceId, List<Long> skuId);

	// public CartInnervVO getFavorCaculateResultDTOBySelected(long userId, int
	// provinceId, List<Long> filterList);

	public CartInnervVO getFavorCaculateResultDTOBySelected(long userId,
			int provinceId, List<Long> filterList, long userCouponId,
			boolean caculateCoupon,PlatformType platformType);

	public boolean addUserToRemindWhenStorage(long userId, int provinceId,
			long skuId);

	public boolean removeUserRemind(long userId, int provinceId, long skuId);

	public boolean userExistInRemindStorage(long userId, int provinceId,
			long skuId);

	public long resetTime(long userId, int provinceId);

	public long getCartLeftTime(long userId, int provinceId);

	public int getCartValidCount(long userId, int provinceId);

	int rebuyAndReturnCode(long userid, int provinceId, long skuId,
			int deltaCount);

	public boolean addCartUpdateTimeToCache(long userId, int areaId,
			Date updateTime);

	public int addCartItem(long userId, int provinceId, long skuId,
			int deltaCount);
	
	boolean deleteCartItems(long userId, int provinceId, List<Long> skuId);
	
	/**
	 * 取购物车
	 * @param userId
	 * @param siteId
	 * @param selectedIds
	 * @param platformType
	 * @return
	 */
	public CartVO getCartInfo(long userId, int siteId, List<Long> selectedIds,
			PlatformType platformType,boolean isGetMiniCart);
	
	/**
	 * 更新购物数量
	 * @param list
	 * @return
	 */
	public int updateCartAmount(String type, long userId, int provinceId,List<CartItemDTO> list);
	
	/**
	 * 減庫存
	 * @param list
	 * @return
	 */
	public boolean decreaseInventory(List<CartItemDTO> list);
	
	/**
	 * 取购物车信息
	 * @param userId
	 * @param provinceId
	 * @return
	 */
	public CartDTO getCart(long userId, int areaId);
	
	/**
	 * 加入购物车
	 * @param userid
	 * @param provinceId
	 * @param skuId
	 * @param deltaCount
	 * @return
	 */
	public int addItemToCart(long userid, int provinceId,
			long skuId, int deltaCount);
	
	/**
	 * 判断缓存库存是否充足
	 * @param userId
	 * @param areaId
	 * @param cartIds
	 * @return
	 */
	public boolean isInventoryEnough(long userId, int areaId,String cartIds);
	
	
	/**
	 * 删除区域中的购物信息除了传入的skuids
	 * @param userId
	 * @param provinceId
	 * @param skuId
	 * @return
	 */
	boolean deleteCartItemsExceptBySkuIds(long userId, int areaId, List<Long> skuId);
	
	/**
	 * 再次购买
	 * @param userId
	 * @param areaId
	 * @param obj
	 * @return
	 */
	public BaseJsonVO buyAgain(long userId, int areaId,InputParam obj);
	
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
