/**
 * 
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.PoOrderForm;

/**
 * @author hzzengdan
 * @date 2014-09-15
 */
public interface PoOrderFormDao extends AbstractDao<PoOrderForm> {

	/**
	 * 通过poorderId获取PoOrder Bean
	 * 
	 * @param poOrderId
	 * @return
	 */
	public PoOrderForm getPoOrderById(String poOrderId);

	/**
	 * 查询多个poOrderId及时间范围内的
	 * 
	 * @param poOrderId
	 * @param createStartTime
	 * @param createEndTime
	 * @param openSaleStartTime
	 * @param openSaleEndTime
	 * @param stopSaleStartTime
	 * @param stopSaleEndTime
	 * @return
	 */
	public List<PoOrderForm> getPickOrderListByMultiplePoOrder(String[] poOrderId, String createStartTime,
			String createEndTime, String openSaleStartTime, String openSaleEndTime, long supplierId);

	public List<PoOrderForm> getPoOrderList(String poOrderId, long createStartTime, long createEndTime,
			long openSaleStartTime, long openSaleEndTime, long stopSaleStartTime, long stopSaleEndTime);

	public List<PoOrderForm> getNotGen2ReturnAfterPoEnd(long incTime,long compareTime);
	
	/**
	 * 
	 * @return
	 */
	public boolean addPoOrderCommand(long poOrderId, long command);
	
}
