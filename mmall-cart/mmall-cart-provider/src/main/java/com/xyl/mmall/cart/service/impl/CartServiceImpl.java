package com.xyl.mmall.cart.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.cart.dao.CartDao;
import com.xyl.mmall.cart.dao.CartInventoryDao;
import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.cart.dto.CartItemDTO;
import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.constant.NkvConstant;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.exception.ServiceException;

/**
 * @author Yang,Nan
 *
 */
@Service("cartService")
public class CartServiceImpl implements CartService {

	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	@Resource
	private CartDao cartDao;

	@Resource
	@Deprecated
	private CartInventoryDao cartInventoryDao;

	@Autowired
	@Deprecated
	private CartCleanCacheOperInf cartCleanCacheOperInf;

	@Override
	public CartDTO getCart(long userId, int provinceId) {
		CartDTO ret = new CartDTO();
		// long letftime = getCartLeftTime(userId, provinceId);
		List<CartItemDTO> valiadcartlist = cartDao.getCart(NkvConstant.NKV_CART_VALID, userId, provinceId);
		ret.setCartItemList(valiadcartlist);
		/*
		 * if (letftime <= 0) { addCartItems(NkvConstant.NKV_CART_OVERTIME,
		 * userId, provinceId, valiadcartlist); for (CartItemDTO c :
		 * valiadcartlist) { cartInventoryDao.addInventory(c.getSkuid(),
		 * c.getCount()); } cartDao.clearCart(NkvConstant.NKV_CART_VALID,
		 * userId, provinceId);
		 * 
		 * } else
		 */

		// ret.setInvalidCartItemList(cartDao.getCart(NkvConstant.NKV_CART_INVALID,
		// userId, provinceId));
		// ret.setOvertimeCartItemList(cartDao.getCart(NkvConstant.NKV_CART_OVERTIME,
		// userId, provinceId));
		// ret.setDeletedCartItemList(cartDao.getCart(NkvConstant.NKV_CART_DELETE,
		// userId, provinceId));
		// ret.setDeletedOvertimeCartItemList(cartDao.getCart(NkvConstant.NKV_CART_DELETE_OVERTIME,
		// userId, provinceId));
		return ret;
	}

	@Override
	public int addCartItem(long userId, int provinceId, long skuId, int deltaCount) {
		logger.debug("begin addCartItem skuid {}, userId {}, deltaCount {}", skuId, userId, deltaCount);
		// 购物车数量>=50 ,且购物车中目前没有这个sku，那么就是多余50的sku，报错
		if (isCartFull(userId, provinceId) && !isCartContainSku(userId, provinceId, skuId))
			return CART_OVER_ITMESCOUNT;
		// 增加购物车数量
		if (deltaCount > 0) {
			// 减购物车的库存
			// int invenroyResutl = cartInventoryDao.decreaseInventory(skuId,
			// deltaCount);
			// if (invenroyResutl == CART_ERROR) {
			// return CART_ERROR;// 失败，库存又异常
			// }else if (invenroyResutl == CART_NO_INVENTORY) {
			// return CART_NO_INVENTORY;// 失败，库存又异常
			// }
			// else if (invenroyResutl < 0) {
			// // 负值，表示并发情况下，减多了，加回去,返回失败
			// cartInventoryDao.addInventory(skuId, deltaCount);
			// return CART_NO_INVENTORY;
			// } else {

			int newvalue = cartDao.changeCartItem(userId, provinceId, skuId, deltaCount);
			// if (newvalue > NkvConstant.CART_ITEM_MAX_COUNT) {
			// // 最多2件,如果多了，库存释放回去，购物车数据也改回去
			// cartInventoryDao.addInventory(skuId, deltaCount);
			// cartDao.changeCartItem(userId, provinceId, skuId, -1 *
			// deltaCount);
			// return CART_OVER_SINGLE_COUNT;
			// } else
			return newvalue;

		} else {
			// 非法，不能添非正整数
			return CART_ERROR;
		}

	}

	@Override
	public boolean addCartItems(String type, long userId, int provinceId, List<CartItemDTO> list) {
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		for (CartItemDTO c : list) {
			map.put(Long.valueOf(c.getSkuid()).toString().getBytes(), Integer.valueOf(c.getCount()).toString()
					.getBytes());
		}
		int result = cartDao.addItems(type, userId, provinceId, map);
		return result != Integer.MIN_VALUE ? true : false;

	}

	@Override
	public boolean deleteCartItems(long userId, int provinceId, List<Long> skuIds) {
		int result = 0;
		for (Long skuid : skuIds) {
			result = cartDao.deleteCartItem(userId, provinceId, skuid);
			if (result != ErrorCode.SUCCESS.getIntValue()) {
				return false;
			}
			// if (delNum != CART_ERROR)
			// cartInventoryDao.addInventory(skuid, delNum);
		}
		return true;
	}

	@Override
	public boolean deleteCart(long userId, int provinceId) {
		return cartDao.deleteCart(NkvConstant.NKV_CART_VALID, userId, provinceId);
		// List<CartItemDTO> cartlist =
		// cartDao.getCart(NkvConstant.NKV_CART_VALID, userId, provinceId);
		//
		// cartDao.deleteCart(NkvConstant.NKV_CART_DELETE, userId, provinceId);
		// cartDao.deleteCart(NkvConstant.NKV_CART_INVALID, userId, provinceId);
		// cartDao.deleteCart(NkvConstant.NKV_CART_OVERTIME, userId,
		// provinceId);
		// cartDao.deleteCart(NkvConstant.NKV_CART_DELETE_OVERTIME, userId,
		// provinceId);
		//
		// if (cartDao.deleteCart(NkvConstant.NKV_CART_VALID, userId,
		// provinceId) == true) {
		// for (CartItemDTO c : cartlist) {
		// cartInventoryDao.addInventory(c.getSkuid(), c.getCount());
		// }
		// return true;
		// } else
		// return false;

	}

	public boolean isCartFull(long userId, int provinceId) {
		List<CartItemDTO> cartdto = cartDao.getCart(NkvConstant.NKV_CART_VALID, userId, provinceId);
		if (cartdto == null)
			return false;
		if (cartdto.size() >= NkvConstant.MAX_SKU_COUNT)
			return true;
		else
			return false;

	}

	@Override
	public boolean isCartContainSku(long userId, int provinceId, long skuId) {
		return cartDao.getCartItemCount(NkvConstant.NKV_CART_VALID, userId, provinceId, skuId) > 0;
	}

	// 以下方法目前没使用到

	@Override
	public int decreaseCartItem(long userId, int provinceId, long skuId, int deltaCount) {
		logger.debug("begin decreaseCartItem skuid {}, userId {}, deltaCount {}", skuId, userId, deltaCount);

		// 减少购物车数量
		if (deltaCount < 0) {// 减少购物车数量

			int ret = cartDao.changeCartItem(userId, provinceId, skuId, deltaCount);
			if (ret == CART_ERROR) {// 失败
				return CART_ERROR;
			} else if (ret < 0) {// 说明并发操作，减多了，加回去
				cartDao.changeCartItem(userId, provinceId, skuId, -1 * deltaCount);
				return CART_ERROR;
			} else if (ret == 0) {
				return deleteCartItem(userId, provinceId, skuId) ? 0 : CART_ERROR;
			} else {
				// 释放库存 到
				// 购物车库存,这一步不管释放库存失败与否，因为如果成功，则没问题，失败的话，也不能怎么样了，库存就会比订单库存少几件，只能等晚上同步了
				cartInventoryDao.addInventory(skuId, -1 * deltaCount);
				return ret;
			}

		} else {
			// 非法，不能非正整数
			return CART_ERROR;
		}

	}

	@Override
	public boolean addInvalidCartItems(long userId, int provinceId, List<CartItemDTO> list) {
		addCartItems(NkvConstant.NKV_CART_INVALID, userId, provinceId, list);
		return true;

	}

	@Override
	public int getCartValidCount(long userId, int provinceId) {
		// int count = 0;
		List<CartItemDTO> cartlist = cartDao.getCart(NkvConstant.NKV_CART_VALID, userId, provinceId);
		// for (CartItemDTO dto : cartlist) {
		// count = count + dto.getCount();
		// }
		return cartlist != null ? cartlist.size() : 0;
	}

	@Override
	public boolean setInventoryCount(long skuId, int count) {
		// TODO Auto-generated method stub
		return cartInventoryDao.setInventoryCount(skuId, count);
	}

	@Override
	public boolean deleteCartForOrder(long userId, int provinceId, List<Long> selectedIds) {
		// TODO Auto-generated method stub
		List<CartItemDTO> cartlist = cartDao.getCart(NkvConstant.NKV_CART_VALID, userId, provinceId);

		cartDao.deleteCart(NkvConstant.NKV_CART_DELETE, userId, provinceId);
		cartDao.deleteCart(NkvConstant.NKV_CART_DELETE_OVERTIME, userId, provinceId);
		// cartDao.deleteCart(NkvConstant.NKV_CART_INVALID, userId, provinceId);
		// cartDao.deleteCart(NkvConstant.NKV_CART_OVERTIME, userId,
		// provinceId);

		if (selectedIds == null) {
			cartDao.deleteCart(NkvConstant.NKV_CART_VALID, userId, provinceId);
			return true;
		}

		for (CartItemDTO c : cartlist) {
			if (selectedIds.contains(c.getSkuid()))
				cartDao.deleteCartItem(NkvConstant.NKV_CART_VALID, userId, provinceId, c.getSkuid());
		}

		return true;

	}

	@Override
	public boolean deleteValidCart(long userId, int provinceId) {
		// TODO Auto-generated method stub
		List<CartItemDTO> cartlist = cartDao.getCart(NkvConstant.NKV_CART_VALID, userId, provinceId);

		if (cartDao.deleteCart(NkvConstant.NKV_CART_VALID, userId, provinceId) == true) {
			for (CartItemDTO c : cartlist) {
				cartInventoryDao.addInventory(c.getSkuid(), c.getCount());
			}
			return true;
		} else
			return false;

	}

	@Override
	public boolean deleteDeletedReocordFromCart(long userId, int provinceId) {
		return cartDao.deleteCart(NkvConstant.NKV_CART_DELETE, userId, provinceId)
				&& cartDao.deleteCart(NkvConstant.NKV_CART_DELETE_OVERTIME, userId, provinceId);
	}

	@Override
	public boolean deleteCartItem(long userId, int provinceId, long skuId) {
		// TODO Auto-generated method stub
		int delNum = cartDao.deleteCartItem(userId, provinceId, skuId);
		if (delNum != CART_ERROR) {
			cartInventoryDao.addInventory(skuId, delNum);
			return true;
		} else
			return false;

	}

	@Override
	public boolean deleteCartItemsWithoutAdjustInventory(long userId, int provinceId, List<Long> skuIds) {
		for (Long skuid : skuIds) {
			int delNum = cartDao.deleteCartItem(userId, provinceId, skuid);
		}
		return true;
	}

	@Override
	public boolean deleteCartItemsWithRecord(long userId, int provinceId, List<Long> skuIds) {
		// TODO Auto-generated method stub
		List<CartItemDTO> list = new ArrayList<CartItemDTO>();
		List<CartItemDTO> validList = new ArrayList<CartItemDTO>();
		List<Long> overtimeList = new ArrayList<Long>();
		List<CartItemDTO> cartvalidList = cartDao.getCart(NkvConstant.NKV_CART_VALID, userId, provinceId);
		List<CartItemDTO> cartovertimeList = cartDao.getCart(NkvConstant.NKV_CART_OVERTIME, userId, provinceId);
		for (CartItemDTO dto : cartvalidList) {
			if (skuIds.contains(dto.getSkuid()))
				validList.add(dto);
		}
		for (CartItemDTO dto : cartovertimeList) {
			if (skuIds.contains(dto.getSkuid()))
				overtimeList.add(dto.getSkuid());
		}
		for (CartItemDTO sku : validList) {
			int delNum = cartDao.deleteCartItem(NkvConstant.NKV_CART_VALID, userId, provinceId, sku.getSkuid());
			if (delNum != CART_ERROR)
				cartInventoryDao.addInventory(sku.getSkuid(), sku.getCount());
			CartItemDTO dto = new CartItemDTO();
			dto.setSkuid(sku.getSkuid());
			dto.setCount(sku.getCount());
			list.add(dto);
		}
		cartDao.clearDeletePool(userId, provinceId);
		boolean ret = addCartItems(NkvConstant.NKV_CART_DELETE, userId, provinceId, list);

		list = new ArrayList<CartItemDTO>();
		for (long skuId : overtimeList) {
			int delNum = cartDao.deleteCartItem(NkvConstant.NKV_CART_OVERTIME, userId, provinceId, skuId);
			CartItemDTO dto = new CartItemDTO();
			dto.setSkuid(skuId);
			dto.setCount(delNum);
			list.add(dto);
		}
		cartDao.clearDeleteOvertimePool(userId, provinceId);
		return addCartItems(NkvConstant.NKV_CART_DELETE_OVERTIME, userId, provinceId, list);
	}

	@Override
	public long resetTime(long userId, int provinceId) {
		// TODO Auto-generated method stub
		long time = cartDao.resetTime(userId, provinceId);
		if (time == CART_ERROR)
			throw new ServiceException("cart : resetTime time error !");

		return time;
	}

	@Override
	public long getCartUpdateTime(long userId, int provinceId) {
		// TODO Auto-generated method stub
		long time = cartDao.getCartTime(userId, provinceId);
		return time;
	}

	@Override
	public Map<Long, Integer> getInventoryCount(List<Long> ids) {
		// TODO Auto-generated method stub
		return cartInventoryDao.getInventoryCount(ids);
	}

	@Override
	public boolean addUserToRemindWhenStorage(long userId, int provinceId, long skuId) {
		return cartDao.addUserToRemindWhenStorage(userId, provinceId, skuId);
	}

	@Override
	public boolean userExistInRemindStorage(long userId, int provinceId, long skuId) {
		return cartDao.userExistInRemindStorage(userId, provinceId, skuId);
	}

	@Override
	public long getCartLeftTime(long userId, int provinceId) {
		// TODO Auto-generated method stub
		long lefttime = CART_TIME - ((new Date()).getTime() - getCartUpdateTime(userId, provinceId));
		if (lefttime < 0)
			return 0;
		else
			return lefttime;
	}

	@Override
	public int recoverCart(long userid, int provinceId) {
		// TODO Auto-generated method stub
		int ret = 0;
		List<CartItemDTO> soldOutskuList = new ArrayList<CartItemDTO>();
		List<CartItemDTO> deletedlist = cartDao.getCart(NkvConstant.NKV_CART_DELETE, userid, provinceId);
		for (CartItemDTO c : deletedlist) {
			// 如果失败，代表可能原来的数量恢复不回去了，那么试试一件、、
			int newret = addCartItem(userid, provinceId, c.getSkuid(), c.getCount());
			ret = processReturnCode(ret, newret);
			if (newret < 0 && c.getCount() > 1) {
				ret = processReturnCode(ret, addCartItem(userid, provinceId, c.getSkuid(), 1));
			}
			removeOverTimeCartItem(userid, provinceId, c.getSkuid());
			if (ret == CART_NO_INVENTORY) {
				CartItemDTO dto = new CartItemDTO();
				dto.setSkuid(c.getSkuid());
				dto.setCount(CartItemDTO.TYPE_SOLDOUT);
				soldOutskuList.add(dto);
			}
		}
		addInvalidCartItems(userid, provinceId, soldOutskuList);
		// 情况 删除记忆池
		cartDao.clearDeletePool(userid, provinceId);

		List<CartItemDTO> deletedovertimelist = cartDao.getCart(NkvConstant.NKV_CART_DELETE_OVERTIME, userid,
				provinceId);
		addCartItems(NkvConstant.NKV_CART_OVERTIME, userid, provinceId, deletedovertimelist);
		// 情况 删除记忆池
		cartDao.clearDeleteOvertimePool(userid, provinceId);
		return ret;
	}

	private int processReturnCode(int oldRet, int newRet) {
		if (oldRet < newRet)
			return oldRet;
		else
			return newRet;
	}

	@Override
	public boolean removeOverTimeCartItem(long userid, int provinceId, long skuId) {
		// TODO Auto-generated method stub
		return cartDao.deleteCartItem(NkvConstant.NKV_CART_OVERTIME, userid, provinceId, skuId) == 1 ? true : false;
	}

	@Override
	public boolean removeDeletedCartItem(long userid, int provinceId, long skuId) {
		// TODO Auto-generated method stub
		return cartDao.deleteCartItem(NkvConstant.NKV_CART_DELETE, userid, provinceId, skuId) == 1 ? true : false;
	}

	@Override
	public boolean removeUserRemind(long userId, int provinceId, long skuId) {
		return cartDao.remvoeUserRemind(userId, provinceId, skuId);
	}

	@Override
	public boolean addCartUpdateTimeToCache(long userId, int areaId, Date updateTime) {
		return cartCleanCacheOperInf.addCartUpdateTimeToCache(userId, areaId, updateTime);
	}

	@Override
	public int getValidCartItemCount(long userId, int provinceId, long skuId) {
		return cartDao.getCartItemCount(NkvConstant.NKV_CART_VALID, userId, provinceId, skuId);
	}

	@Override
	public boolean addInventoryCount(List<CartItemDTO> list) {
		// TODO Auto-generated method stub
		for (CartItemDTO sku : list) {
			cartInventoryDao.addInventory(sku.getSkuid(), sku.getCount());
		}
		return true;
	}

	@Override
	public boolean clearInvalidCartItems(long userId, int provinceId) {
		// TODO Auto-generated method stub
		return cartDao.deleteCart(NkvConstant.NKV_CART_INVALID, userId, provinceId);
	}

	@Override
	public boolean removeInvalidCartItems(long userid, int provinceId, List<Long> skuIds) {
		// TODO Auto-generated method stub
		for (Long id : skuIds)
			cartDao.deleteCartItem(NkvConstant.NKV_CART_INVALID, userid, provinceId, id);
		return true;
	}

	@Override
	public int decreaseInventory(List<CartItemDTO> list) {
		for (CartItemDTO sku : list) {
			// 减购物车的库存
			int invenroyResutl = cartInventoryDao.decreaseInventory(sku.getSkuid(), sku.getCount());
			if (invenroyResutl == CART_ERROR) {
				return CART_ERROR;// 失败，库存又异常
			} else if (invenroyResutl == CART_NO_INVENTORY) {
				return CART_NO_INVENTORY;// 失败，库存又异常
			} else if (invenroyResutl < 0) {
				// 负值，表示并发情况下，减多了，加回去,返回失败
				cartInventoryDao.addInventory(sku.getSkuid(), sku.getCount());
				return CART_NO_INVENTORY;
			}
			return invenroyResutl;
		}
		// 非法，不能添非正整数
		return CART_ERROR;
	}

	@Override
	public Long[] selectCartItems(long userId, int provinceId, Long[] selectedSkuIds) {
		
		return cartDao.selectCartItems(userId, provinceId, selectedSkuIds);
	}

	@Override
	public Long[] unselectCartItems(long userId, int provinceId, Long[] unselectSkuIds) {
		//当前缓存中的选中状态
		Long[] selected = cartDao.selectCartItems(userId, provinceId, new Long[0]);
		if (unselectSkuIds == null && unselectSkuIds.length == 0) {
			return selected;
		}
		
		List<Long> lstSelected = new ArrayList<>(Arrays.asList(selected));
		if (lstSelected.removeAll(Arrays.asList(unselectSkuIds))) {//如果当前缓存中存在入参unselectSkuIds
			if (lstSelected.isEmpty()) {
				return cartDao.selectCartItems(userId, provinceId, null);//删除缓存
			} else {
				return cartDao.selectCartItems(userId, provinceId, lstSelected.toArray(new Long[0]));//设置剔除后缓存
			}
		}
		
		return selected;
	}
}
