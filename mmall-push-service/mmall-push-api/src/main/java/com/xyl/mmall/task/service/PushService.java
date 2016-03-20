package com.xyl.mmall.task.service;

import java.util.Map;

import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.task.dto.MessageDTO;
import com.xyl.mmall.task.enums.PlatformType;



/**
 * push 管理接口
 * @author jiangww
 *
 */
public interface PushService {
	
	/**
	 * 立即发送push
	 * 
	 * @param pushManagementAo
	 * appUrl 内部打开URL 转换方法见mmall-framework com.xyl.mmall.framework.mobile.MobileURL
	 * @return
	 */
	public boolean push(long userId,String alertTitle, String title, String message ,String appUrl);
	
	/**
	 * 业务push总入口，内部根据业务类型会发送相应的短信，push或邮件
	 * 	1 = "订单发货";
		2 = "退货成功";
		3 = "退货拒绝";
		4= "活动红包";
		5= "新的优惠券";
		6 = 货到付款订单被审核不通过;
		7 = 订单倒数5分钟
		8 = 购物车倒数 5分钟
		9 = 订单支付后取消
		11 = 包裹被取消
	 * @param pushManagementAo
	 * title 提示标题
	 * message 提示详细描述
	 * keyId id ，对应不同类型
	 * @return
	 */
	public boolean push(long userId,int type,String title , String message, long keyId);
	
	
	public boolean pushByArea(long userId,int type,String title , String message, long keyId,long areaCode);
	
	/**
	 * 私信，发送指定平台消息给特定用户
	 * 同步发送，单个用户发送
	 * @param userKey
	 * @param platform 可以取:PushConst类的静态变量,android代表android平台,ios代表ios平台,web代表web平台
	 * @param content
	 * @return 后台直接调用消息网关，直接返回是否成功
	 */
	public boolean pushMessageForPrivate(String userKey,PlatformType platform,String content);
	
	/**
	 * 使用定制消息对象发送
	 * 同步发送，单个用户发送
	 * @param userKey
	 * @param message
	 * @return
	 */
	public boolean putMessageForPrivate(String userKey,MessageDTO message);
	
	/**
	 * 客户端向服务器获取唯一的的签名信息
	 * @param userUniqueKey 用户唯一值
	 * @return
	 */
	public Map<String,Object> getSignatureByUserKey(String userUniqueKey);
	
	/**
	 * 发送短信接口
	 * <p>
	 * 只支持订阅类短信
	 * @param mobile 手机号
	 * @param content 短信内容
	 * @return
	 */
	public boolean sendSms(String mobile,String content);
	
	/**
	 * 发送邮件
	 * @param toAddress
	 * @param title
	 * @param content
	 * @param fromName
	 * @return
	 */
	public boolean sendMail(MailType mailType, String toAddress, String title,String content);
	
	/**TestCompilerMojo.java:161
	 * 发送比较重要的短信，如订单短信
	 * <p>
	 * 根据keyId，type及userId发送push消息,短信或邮件
	 * @param userId 用户id
	 * @param bizTypeId 业务类型id
	 * @param keyId 业务唯一值
	 * @param otherParamMap 其它参数
	 * @return
	 */
	public boolean pushForAll(long userId,int bizTypeId,long keyId,Map<String, Object> otherParamMap);
	
	/**
	 * 发送短信接口
	 * <p>
	 * 采用事务性通道
	 * @param mobile 手机号
	 * @param content 短信内容
	 * @return
	 */
	public boolean sendSmsImportant(String mobile,String content);
}
