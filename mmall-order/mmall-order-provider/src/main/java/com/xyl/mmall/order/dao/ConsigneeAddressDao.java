package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.meta.ConsigneeAddress;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月11日 下午4:58:10
 *
 */
public interface ConsigneeAddressDao extends AbstractDao<ConsigneeAddress> {

	/**
	 * 删除收货地址
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	public boolean deleteConsigneeAddress(long id, long userId);

	/**
	 * 根据userId获取收获地址列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<ConsigneeAddress> getConsigneeAddressListByUserId(long userId);

	/**
	 * 更新收获地址信息
	 * 
	 * @param clientExpInfo
	 * @return
	 */
	public boolean updateConsigneeAddress(ConsigneeAddress consigneeAddress);

	/**
	 * 根据userId获取默认收获地址
	 * 
	 * @param userId
	 * @return
	 */
	public ConsigneeAddress getDefaultByUserId(long userId);

	/**
	 * 更新默认状态
	 * 
	 * @param id
	 * @param userId
	 * @param bDef
	 * @return
	 */
	public boolean updateDefault(long id, long userId, boolean bDef);

	public List<ConsigneeAddress> queryInfoListByUserId(long userId, DDBParam ddbParam);

	/**
	 * 根据用户ID和收货地址ID获取收获地址的内容。
	 * 
	 * @param id
	 *            收货地址ID
	 * @param userId
	 *            用户ID
	 * @return 收货地址内容
	 */
	public ConsigneeAddress getConsigneeAddressByIdAndUserId(long id, long userId);

	/**
	 * 根据用户的收货手机获取非重复的用户ID的列表
	 * 
	 * @param consigneeMobile
	 *            收货手机
	 * @param ddbParam
	 *            分页参数
	 * @return 只包含非重复的用户ID数据
	 */
	public List<ConsigneeAddress> queryUserIdByConsigneeMobile(String consigneeMobile, DDBParam ddbParam);

	/**
	 * 根据用户的收货手机获取非重复的用户ID的数量
	 * 
	 * @param consigneeMobile
	 *            收货手机
	 * @return 只包含非重复的用户ID数量
	 */
	public int countUserIdByConsigneeMobile(String consigneeMobile);

	/**
	 * 更新用户的所有地址的默认状态
	 * 
	 * @param userId
	 *            用户Id
	 * @param isDefault
	 *            是否为默认地址
	 * @return
	 */
	public boolean updateAllDefault(long userId, boolean isDefault);
}
