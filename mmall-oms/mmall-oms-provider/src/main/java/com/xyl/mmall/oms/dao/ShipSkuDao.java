/**
 * 发货单sku的dao
 */
package com.xyl.mmall.oms.dao;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;

/**
 * @author hzliujie
 * @date 2014-09-17
 */
public interface ShipSkuDao extends AbstractDao<ShipSkuItemForm> {

	/**
	 * 根据shipOrderId获取拣货单sku详情
	 */
	public List<ShipSkuItemForm> getShipSkuList(String shipOrderId);

	/**
	 * 根据poOrderId获取发货单明细
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<ShipSkuItemForm> getShipSkuListByPoOrderId(String poOrderId);

	/**
	 * 根据poOrderId获取发货单明细
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<ShipSkuItemForm> getShipSkuListByPoOrderId(long poOrderId, long supplierId);

	/**
	 * 
	 * @param shipOrderId
	 * @return
	 */
	public int getTotalCountByShipOrderId(String shipOrderId, long supplierId);

	/**
	 * 
	 * @param shipOrderId
	 * @return
	 */
	public int getTotalSkuTypeByShipOrderId(String shipOrderId, long supplierId);

	/**
	 * 更新仓库反馈信息，where: shipSku.shipOrderId + shipSku.skuId
	 * 
	 * @param shipOrder
	 * @return
	 */
	public boolean updateWarehoureInfo(ShipSkuItemForm shipSku);

	/**
	 * 根据po单号获取累计到货数
	 * 
	 * @param poOrderId
	 * @return
	 */
	public int getTotalArrivedByPoOrderId(String poOrderId, long supplierId);

	/**
	 * @param poOrderId
	 * @return
	 */
	public Map<Long, Integer> getArrivedNormalCountBySkuInPo(long poOrderId);
}
