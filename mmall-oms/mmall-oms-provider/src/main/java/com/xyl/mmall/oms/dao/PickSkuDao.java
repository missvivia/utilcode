package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.meta.PickSkuItemForm;

/**
 * @author zb
 *
 */
public interface PickSkuDao extends AbstractDao<PickSkuItemForm> {
	/**
	 * 通过pickid获取拣货单sku详情列表
	 */
	public List<PickSkuItemForm> getPickSkuList(String pickOrderId, long supplierId);

	/**
	 * @param skuId
	 * @param poId
	 * @return
	 */
	public PickSkuItemForm getShipSkuBySkuIdAndPoId(String skuId, String poId);

	/**
	 * 通过poOrderId获取sku列表
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<PickSkuItemForm> getPickSkuListByPoOrder(String poOrderId, String pickOrderId, String pickStartTime,
			String pickEndTime);

	/**
	 * 通过poOrderId获取sku详细列表
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<PickSkuItemForm> getPickSkuByPoOrderId(String poOrderId, long supplierId);

	/**
	 * 根据poid和原始供应商获取详情
	 * 
	 * @param poOrderId
	 * @param supplierId
	 * @return
	 */
	public List<PickSkuItemForm> getPickSkuByPoOrderIdAndOriSupplierId(String poOrderId, long supplierId);

	/**
	 * 通过poOrderId获取未拣货sku详细列表
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<PickSkuItemForm> getUnPickSkuByPoOrderId(String poOrderId);

	/**
	 * 更新某个拣货单对应的sku列表的状态
	 * 
	 * @param pickOrderId
	 * @param type
	 * @return
	 */
	public boolean updatePickSkuState(String pickOrderId, PickStateType type);

	/**
	 * 根据oms订单id删除未拣货的sku
	 * 
	 * @param omsOrderFormId
	 * @return
	 */
	public boolean deleteUnPickSkuByOmsOrderFormId(long omsOrderFormId);

	/**
	 * 获取商家下未拣货的明细
	 * 
	 * @param supplierId
	 * @return
	 */
	public List<PickSkuItemForm> getUnPickSkuByPoSupplierId(long supplierId);

	/**
	 * @param pickSkuId
	 * @return
	 */
	public boolean updateUnPickSkuStateById(long id, String pickOrderId, long pickTime);

	/**
	 * @param createTime
	 * @param limit
	 * @return
	 */
	public List<PickSkuItemForm> getUnPickListByCreateTime(long createTime, int limit);

	/**
	 * @param omsOrderFormId
	 * @return
	 */
	public List<PickSkuItemForm> getByOmsOrderFormId(long omsOrderFormId);
	
	/**
	 * @param poId
	 * @return
	 */
	public List<Long> getOmsOrderFormIdListInPo(long poId);
}
