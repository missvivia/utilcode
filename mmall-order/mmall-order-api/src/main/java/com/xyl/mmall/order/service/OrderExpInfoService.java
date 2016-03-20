package com.xyl.mmall.order.service;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月15日 上午10:33:08
 *
 */
public interface OrderExpInfoService {
	
	/**
	 * 通过用户Id和订单Id查询
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public OrderExpInfoDTO queryInfoByUserIdAndOrderId(long userId, long orderId);
	
	/**
	 * 通过用户Id查询
	 * @param userId
	 * @param ddbParam
	 * @return
	 */
	public List<OrderExpInfoDTO> queryInfoByUserId(long userId, DDBParam ddbParam);
	
	public RetArg queryInfoByUserId2(long userId, DDBParam ddbParam);
	
	/**
	 * 通过收件人名查询
	 * @param consigneeName
	 * @param ddbParam
	 * @return
	 */
	public List<OrderExpInfoDTO> queryInfoByConsigneeName(String consigneeName, DDBParam ddbParam);
	
	public RetArg queryInfoByConsigneeName2(String consigneeName, DDBParam ddbParam);
	
	/**
	 * 通过收件人电话查询
	 * @param consigneeMobile
	 * @param ddbParam
	 * @return
	 */
	public List<OrderExpInfoDTO> queryInfoByConsigneeMobile(String consigneeMobile, DDBParam ddbParam);
	
	public RetArg queryInfoByConsigneeMobile2(String consigneeMobile, DDBParam ddbParam);
	
	/**
	 * 通过收件人地址查询
	 * @param consigneeAddress
	 * @param ddbParam
	 * @return
	 */
	public List<OrderExpInfoDTO> queryInfoByConsigneeAddress(String consigneeAddress, DDBParam ddbParam);
	
	public RetArg queryInfoByConsigneeAddress2(String consigneeAddress, DDBParam ddbParam);
}
