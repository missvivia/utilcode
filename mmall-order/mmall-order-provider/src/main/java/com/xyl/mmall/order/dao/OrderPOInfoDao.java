package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.meta.OrderPOInfo;

/**
 * @author dingmingliang
 * 
 */
public interface OrderPOInfoDao extends AbstractDao<OrderPOInfo> {

	/**
	 * 批量读取记录
	 * 
	 * @param minId
	 *            Id的起始值
	 * @param poId
	 * @param param
	 * @return
	 */
	public List<OrderPOInfo> getListByPoId(long minId, long poId, DDBParam param);
}
