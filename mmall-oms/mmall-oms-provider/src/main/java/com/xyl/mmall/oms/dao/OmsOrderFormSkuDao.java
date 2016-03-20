package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;

/**
 * @author zb
 *
 */
public interface OmsOrderFormSkuDao extends AbstractDao<OmsOrderFormSku> {
	/**
	 * 查询一个po单下取消的sku数量
	 * 
	 * @param poId
	 * @return
	 */
	public long getCancelSkuCountInPoId(long poId);

	/**
	 * 查询一个订单下的sku明细
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<OmsOrderFormSku> queryByOmsOrderFormId(long omsOrderFormId, long userId);
	
	public OmsOrderFormSku getBySupplierIdAndSkuId(long poId, long skuId);
	
	public OmsOrderFormSku getByOmsOrderFormIdAndSkuId(long omsOrderFormId, long skuId);
	
	/**
	 * 查询一个poId销售总数
	 * @param poId
	 * @return
	 */
	public int getTotalSoldByPoId(long poId);
}
