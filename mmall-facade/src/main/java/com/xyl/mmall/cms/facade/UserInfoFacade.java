/**
 * 
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.vo.ConsigneeAddressVO;
import com.xyl.mmall.cms.vo.UserProfileVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.member.dto.AccountDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;

/**
 * 主站用户相关Facade。
 * 
 * @author lihui
 *
 */
public interface UserInfoFacade {

	/**
	 * 根据查询的类型和值查询主站用户的列表。
	 * 
	 * @param type
	 *            查询类型
	 * @param value
	 *            查询取值
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 主站用户列表
	 */
	BaseJsonVO getUserList(Integer type, String value, int limit, int offset);

	/**
	 * 获取指定用户的基本信息。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户基本信息
	 */
	BaseJsonVO getUserInfo(long userId);

	/**
	 * 更新指定用户的绑定手机和邮箱地址。
	 * 
	 * @param userId
	 *            用户ID
	 * @param type
	 *            更新类型
	 * @param value
	 *            更新数据
	 * @return
	 */
	BaseJsonVO updateUserBindMobileEmail(long userId, String type, String value);

	/**
	 * 根据用户ID获取用户的收货地址。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 收货地址
	 */
	BaseJsonVO getUserConsigneeAddress(long userId);

	/**
	 * 根据用户ID获取用户的优惠券信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param offset
	 *            分页位置
	 * @param limit
	 *            分页大小
	 * @return 优惠券信息
	 */
	BaseJsonVO getUserCoupon(long userId, int limit, int offset);

	/**
	 * 根据用户ID获取用户的订单信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param offset
	 *            分页位置
	 * @param limit
	 *            分页大小
	 * @return 订单信息
	 */
	BaseJsonVO getUserOrder(long userId, int limit, int offset);

	/**
	 * 根据用户ID获取用户的红包信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param offset
	 *            分页位置
	 * @param limit
	 *            分页大小
	 * @return 红包信息
	 */
	BaseJsonVO getUserRedPacket(long userId, int limit, int offset);

	/**
	 * 搜索用户信息
	 * @param basePageParamVO
	 * @param searchValue
	 * @return
	 */
	public List<UserProfileVO> queryUserList(BasePageParamVO<UserProfileVO> basePageParamVO, String searchValue);
	
	/**
	 * 获取用户基本信息
	 * @param userId
	 * @return
	 */
	public UserProfileVO getUserDetailInfo(long userId);
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public long getUserIdByUserName(String userName);
	
	/**
	 * 获取用户收货地址
	 * @param userId
	 * @return
	 */
	public List<ConsigneeAddressVO> getUserConsigneeAddressList(long userId);
	
	/**
	 * 获取用户订单列表
	 * @param userId
	 * @param ddbParam
	 * @return
	 */
	public JSONObject getUserOrderList(long userId, DDBParam ddbParam);
	
	/**
	 * 更新用户基本信息
	 * @param accountDTO
	 * @param userProfile
	 * @return
	 */
	public int updateUserBaseInfo(AccountDTO accountDTO, UserProfileDTO userProfile) throws Exception;
	
	/**
	 * 获取用户优惠券
	 * @param userId
	 * @param state -1 全部
	 * @param limit
	 * @param offset
	 * @return {@link JSONObject}
	 */
	public JSONObject getUserCoupons(long userId, int state, int limit, int offset);
	
	/**
	 * 新增用户
	 * @param accountDTO
	 * @param userProfileDTO
	 * @return long userprofileid
	 */
	public long addUser(AccountDTO accountDTO, UserProfileDTO userProfileDTO);
	
	/**
	 * 获取用户基本信息
	 * @param userId
	 * @return
	 */
	public UserProfileDTO getUserBaseInfo(long userId);
}
