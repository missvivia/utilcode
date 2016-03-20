package com.xyl.mmall.oms.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.PoOrderFormSku;

/**
 * @author zb
 *
 */
public interface PoOrderFormSkuDao extends AbstractDao<PoOrderFormSku> {
	
	public PoOrderFormSku getPoOrderFormSkuById(long poSkuId,long poId);
	
	public boolean decreaseSelfStock(long poSkuId, long poId, int decreaseCount);

	public boolean decreaseBackupStock(long poSkuId, long poId, int decreaseCount);

	public boolean increaseSelfStock(long poSkuId, long poId, int increaseCount);

	public boolean increaseBackupStock(long poSkuId, long poId, int increaseCount);

	public int getPoOrderSelfStockById(long poId);

	public int getPoOrderBackupStockById(long poId);
}
