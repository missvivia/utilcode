/**
 * PO单Dao操作
 */
package com.xyl.mmall.oms.dao.impl;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.PoOrderFormSkuDao;
import com.xyl.mmall.oms.meta.PoOrderFormSku;

/**
 * @author hzzengdan
 *
 */
@Repository("poOrderFormSkuDao")
public class PoOrderFormSkuDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<PoOrderFormSku> implements
		PoOrderFormSkuDao {

	private String sqlDecreaseSelfStock = "update Mmall_Oms_PoOrderFormSku set curSelfStock = curSelfStock - ? where  poSkuId=? and poId=? and curSelfStock>=?";

	private String sqlDecreaseBackupStock = "update Mmall_Oms_PoOrderFormSku set curBackupStock = curBackupStock - ? where  poSkuId=? and poId=? and curBackupStock>=?";

	private String sqlIncreaseSelfStock = "update Mmall_Oms_PoOrderFormSku set curSelfStock = curSelfStock + ? where  poSkuId=? and poId=?";

	private String sqlIncreaseBackupStock = "update Mmall_Oms_PoOrderFormSku set curBackupStock = curBackupStock + ? where  poSkuId=? and poId=?";

	private String sqlSelectById = "select * from Mmall_Oms_PoOrderFormSku where poSkuId=? and poId=?";

	private String sqlSelfStockByPoId = "select sum(OriSelfStock) from Mmall_Oms_PoOrderFormSku where poId=?";
	
	private String sqlBackupStockByPoId = "select sum(OriBackupStock) from Mmall_Oms_PoOrderFormSku where poId=?";
	
	@Override
	public boolean decreaseSelfStock(long poSkuId, long poId, int decreaseCount) {
		return this.getSqlSupport().excuteUpdate(sqlDecreaseSelfStock, decreaseCount, poSkuId, poId, decreaseCount) > 0;
	}

	@Override
	public boolean decreaseBackupStock(long poSkuId, long poId, int decreaseCount) {
		return this.getSqlSupport().excuteUpdate(sqlDecreaseBackupStock, decreaseCount, poSkuId, poId, decreaseCount) > 0;
	}

	@Override
	public boolean increaseSelfStock(long poSkuId, long poId, int increaseCount) {
		return this.getSqlSupport().excuteUpdate(sqlIncreaseSelfStock, increaseCount, poSkuId, poId) > 0;
	}

	@Override
	public boolean increaseBackupStock(long poSkuId, long poId, int increaseCount) {
		return this.getSqlSupport().excuteUpdate(sqlIncreaseBackupStock, increaseCount, poSkuId, poId) > 0;
	}

	@Override
	public PoOrderFormSku getPoOrderFormSkuById(long poSkuId, long poId) {
		return this.queryObject(sqlSelectById, poSkuId, poId);
	}
	
	@Override
	public int getPoOrderSelfStockById(long poId){
		return this.getSqlSupport().queryCount(sqlSelfStockByPoId, poId);
	}
	
	@Override
	public int getPoOrderBackupStockById(long poId){
		return this.getSqlSupport().queryCount(sqlBackupStockByPoId, poId);
	}

}
