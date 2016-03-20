package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.meta.OrderExpInfo;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午9:40:53
 *
 */
public interface OrderExpInfoDao extends AbstractDao<OrderExpInfo> {

	/**
	 * @param userId
	 * @param ddbParam
	 * @return
	 */
	public List<OrderExpInfo> queryInfoListByUserId(long userId, DDBParam ddbParam);
	
	/**
	 * @param consigneeName
	 * @param ddbParam
	 * @return
	 */
	public List<OrderExpInfo> queryInfoByConsigneeName(String consigneeName, DDBParam ddbParam);
	
	/**
	 * @param consigneeMobile
	 * @param ddbParam
	 * @return
	 */
	public List<OrderExpInfo> queryInfoByConsigneeMobile(String consigneeMobile, DDBParam ddbParam);
	
	/**
	 * @param consigneeAddress
	 * @param ddbParam
	 * @return
	 */
	public List<OrderExpInfo> queryInfoByConsigneeAddress(String consigneeAddress, DDBParam ddbParam);
	
	/**
	 * @param userId
	 * @param orderIdColl
	 * @return
	 */
	public List<OrderExpInfo> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl);
}
