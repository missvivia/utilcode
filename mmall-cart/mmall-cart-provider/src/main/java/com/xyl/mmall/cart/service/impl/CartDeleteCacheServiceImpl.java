package com.xyl.mmall.cart.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.cart.clean.CartRDBOperUtil;
import com.xyl.mmall.cart.clean.RDBResult;
import com.xyl.mmall.cart.dao.CartDao;
import com.xyl.mmall.cart.delete.CartDeleteKeyUtil;
import com.xyl.mmall.cart.service.CartDeleteCacheService;
import com.xyl.mmall.constant.NkvConstant;

@Service
public class CartDeleteCacheServiceImpl implements CartDeleteCacheService {

	private static final Logger logger = LoggerFactory.getLogger(CartDeleteCacheServiceImpl.class);

	@Autowired
	private CartDao cartDao;

	@Autowired
	private CartRDBOperUtil cartRDBOperUtil;

	@Override
	public boolean addCartItemToDeleteCache(String type, long userId, int areaId, Date date) {
		String outKey = CartDeleteKeyUtil.getOutKeyByParam(type, areaId);
		if (outKey == null) {
			logger.error("illegal type for addCartItemToDeleteCache,type:" + type);
			return false;
		}
		return cartRDBOperUtil.putOrReplaceForRDBOfMap(outKey, CartDeleteKeyUtil.getDeleteFieldKey(userId), date.getTime());
	}
	
	@Override
	public boolean deleteCartItemShouldRemove(int areaId, String type) {
		String outKey = CartDeleteKeyUtil.getOutKeyByParam(type, areaId);
		if (outKey == null) {
			logger.error("illegal type for deleteCartItemShouldRemove,type:" + type);
			return false;
		}

		RDBResult rdbResult = cartRDBOperUtil.getAllFromRDBOfMap(outKey);
		if (!rdbResult.isSearchSuccess()) {
			logger.error("rdb search error,deleteCartItemShouldRemove,areaId:" + areaId + ",type:" + type);
			return false;
		}

		Map<byte[], byte[]> map = rdbResult.getResult();
		if (map == null || map.isEmpty()) {
			return true;
		}

		// process
		boolean allSuccessFlag = true;
		for (Entry<byte[], byte[]> entry : map.entrySet()) {
			String userIdStr = new String(entry.getKey());
			long userId = Long.valueOf(userIdStr);
			if (!this.deleteCartByType(type, userId, areaId)) {
				logger.error("del cartItem from delete cache err,type:" + type + ",userId:" + userIdStr + ",areaId:"
						+ areaId);
				allSuccessFlag = false;
				continue;
			}

			// 删除delete cache中对应的值
			if (!cartRDBOperUtil.delFieldAndValueForRDBOfMap(outKey, userIdStr)) {
				logger.error("can not remove ele from delete cache,outKey:" + outKey + ",userIdStr:" + userIdStr);
				allSuccessFlag = false;
			}
		}

		return allSuccessFlag;
	}

	private boolean deleteCartByType(String type, long userId, int areaId) {
		if (type != null) {
			return cartDao.deleteCart(type, userId, areaId);
		}

		// 分别处理所有非有效队列
		return cartDao.deleteCart(NkvConstant.NKV_CART_OVERTIME, userId, areaId)
				&& cartDao.deleteCart(NkvConstant.NKV_CART_DELETE, userId, areaId)
				&& cartDao.deleteCart(NkvConstant.NKV_CART_DELETE_OVERTIME, userId, areaId)
				&& cartDao.deleteCart(NkvConstant.NKV_CART_INVALID, userId, areaId);
	}

}
