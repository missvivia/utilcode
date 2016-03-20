package com.xyl.mmall.order.service;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;

/**
 * 收货地址服务类
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午10:48:29
 * 
 */
public interface ConsigneeAddressService {

	/**
	 * 根据用户id获取收获地址列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<ConsigneeAddressDTO> getConsigneeAddressListByUserId(long userId);

	/**
	 * 读取用户某条收货地址信息
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	public ConsigneeAddressDTO getConsigneeAddressByIdAndUserId(long id, long userId);

	/**
	 * 添加一个收货地址信息
	 * 
	 * @param userId
	 * @param consigneeAddress
	 * @return
	 */
	public ConsigneeAddressDTO addConsigneeAddress(long userId, ConsigneeAddressDTO consigneeAddress);

	/**
	 * 根据id和userId删除一个收货地址信息
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	public boolean deleteConsigneeAddressByIdAndUserId(long id, long userId);

	/**
	 * 更新一个收货地址信息
	 * 
	 * @param userId
	 * @param consigneeAddress
	 * @return
	 */
	public boolean updateConsigneeAddress(long userId, ConsigneeAddressDTO consigneeAddress);

	/**
	 * 设置默认的收货地址信息
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	public boolean setDefaultConsigneeAddress(long id, long userId);

	/**
	 * 获取默认的收货地址信息
	 * 
	 * @param userId
	 * @return
	 */
	public ConsigneeAddressDTO getDefaultConsigneeAddress(long userId);

	/**
	 * 通过用户Id查询
	 * 
	 * @param userId
	 * @param ddbParam
	 * @return
	 */
	public List<ConsigneeAddressDTO> queryInfoByUserId(long userId, DDBParam ddbParam);

	public RetArg queryInfoByUserId2(long userId, DDBParam ddbParam);

	/**
	 * 通过收件人电话查询收件人ID列表
	 * 
	 * @param consigneeMobile
	 *            收货手机
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 只包含非重复的用户ID数据
	 */
	public List<ConsigneeAddressDTO> queryUserIdByConsigneeMobile(String consigneeMobile, int limit, int offset);

	/**
	 * 根据用户的收货手机获取非重复的用户ID的数量
	 * 
	 * @param consigneeMobile
	 *            收货手机
	 * @return 只包含非重复的用户ID数量
	 */
	public int countUserIdByConsigneeMobile(String consigneeMobile);
}
