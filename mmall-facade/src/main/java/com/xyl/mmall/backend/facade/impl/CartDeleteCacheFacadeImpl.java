package com.xyl.mmall.backend.facade.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.backend.facade.CartDeleteCacheFacade;
import com.xyl.mmall.cart.service.CartDeleteCacheService;
import com.xyl.mmall.framework.annotation.Facade;

@Facade
public class CartDeleteCacheFacadeImpl implements CartDeleteCacheFacade{
	
	@Autowired
	private CartDeleteCacheService cartDeleteCacheFacade;

	@Override
	public boolean addCartItemToDeleteCache(String type, long userId, int areaId, Date addDate) {
		return cartDeleteCacheFacade.addCartItemToDeleteCache(type, userId, areaId, addDate);
	}

	@Override
	public boolean deleteCartItemShouldRemove(int areaId, String type) {
		return cartDeleteCacheFacade.deleteCartItemShouldRemove(areaId, type);
	}

}
