/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.saleschedule.dto.UserFavListDTO;
import com.xyl.mmall.task.dto.PushManagementDTO;
import com.xyl.mmall.task.meta.PushManagement;

/**
 * @author hzjiangww
 *
 */
public interface MobilePushManageFacade {
	/**
	 * 获取收藏用户数据
	 * @param brandIds
	 * @return
	 */
	public List<UserFavListDTO> getUserFavListByBrandIdList(List<Long> brandIds,int limit, 
			int offset);
	/**
	 * 根据条件筛选
	 * 
	 * @param pushManagementAo
	 * @return
	 */
	public RetArg getPushConfigList(PushManagementDTO pushManagementAo,DDBParam param);
	
	/**
	 * 读取某条具体的记录
	 * 
	 * @param id
	 * @return
	 */
	public PushManagementDTO getPushConfigById(long id);

	/**
	 * 添加一个收货地址信息
	 * 
	 * @param userId
	 * @param pushManagementDTO
	 * @return
	 */
	public PushManagementDTO addPushConfig(PushManagement pushManagement);

	/**
	 * 根据id删除一个条发送记录
	 * 
	 * @param id
	 * @return
	 */
	public boolean deletePushConfigById(long id);

	/**
	 * 更新
	 * 
	 * @param userId
	 * @param pushManagementDTO
	 * @return
	 */
	public boolean updatePushManagement(long id, PushManagementDTO pushManagementDTO);

	
	/**
	 * 启动push 定时任务队列
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean pushRun(long start,long end);
	
	/**
	 * 启动检查order支付即将超时的订单
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean pushOrderTimeOut(long lastStartTime,long startTime);
	
	/**
	 * 立即发送push
	 * 
	 * @param pushManagementAo
	 * appUrl 内部打开URL 转换方法见mmall-framework com.xyl.mmall.framework.mobile.MobileURL
	 * @return
	 */
	public boolean push(long userId,String alertTitle, String title, String message ,String appUrl);
	
	/**
	 * 立即发送push 预订的 类型
	 * 	1 = "订单发货";
		2 = "退货成功";
		3 = "退货拒绝";
		4= "活动红包";
		5= "新的优惠券";
		6 = 货到付款订单被审核不通过;
		7 = 订单倒数5分钟
		8 = 购物车倒数 5分钟
	 * @param pushManagementAo
	 * title 提示标题
	 * message 提示详细描述
	 * keyId id ，对应不同类型
	 * @return
	 */
	public boolean push(long userId,int type,String title , String message, long keyId);
	public boolean push(long userId,int type,String title , String message, long keyId,long areaId);
	
	/**
	 * 根据keyId，type及userId发送短信或邮件
	 * <p>
	 * 不支持push消息
	 * @param userId 用户id
	 * @param bizUniqueId 业务类型id
	 * @param keyId 业务唯一值
	 * @param otherParamMap 其它参数
	 * @return
	 */
	public boolean pushForAll(long userId,int bizTypeId,long bizUniqueId,Map<String, Object> otherParamMap);
	
}