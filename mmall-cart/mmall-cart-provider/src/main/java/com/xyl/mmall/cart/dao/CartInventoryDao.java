package com.xyl.mmall.cart.dao;

import java.util.List;
import java.util.Map;

public interface CartInventoryDao {
	public int decreaseInventory(long skuId,int deltaCount);
	public int addInventory(long skuId,int deltaCount);
	public boolean setInventoryCount(long skuId, int count);
	public Map<Long,Integer> getInventoryCount(List<Long> ids);
}
