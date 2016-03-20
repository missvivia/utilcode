package com.xyl.mmall.mainsite.facade;

import java.util.Map;

import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.task.dto.MessageDTO;
import com.xyl.mmall.task.enums.PlatformType;

/**
 * 消息推送
 * @author hzzhaozhenzuo
 *
 */
public interface MessagePushFacade {
	/**
	 * 私信，发送指定平台消息给特定用户
	 * 同步发送，单个用户发送
	 * @param userKey
	 * @param platform 可以取:PlatformType enum,android代表android平台,ios代表ios平台,web代表web平台
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
	 * @param mobile 手机号
	 * @param content 短信内容
	 * @return
	 */
	public boolean sendSms(String mobile,String content);
	
	/**
	 * 发送邮件
	 * @param mailType
	 * @param toAddress
	 * @param title
	 * @param content
	 * @return
	 */
	public boolean sendMail(MailType mailType,String toAddress, String title, String content);
	
	/**
	 * 根据keyId，type及userId发送push消息,短信或邮件
	 * 不发送push消息
	 * @param userId 用户id
	 * @param bizTypeId 业务类型id
	 * @param bizUniqueId 业务唯一id
	 * @param otherParamMap 其它参数
	 * @return
	 */
	public boolean pushForAll(long userId,int bizTypeId,long bizUniqueId,Map<String, Object> otherParamMap);
	
	/**
	 * 采用事务性通道发送短信
	 * @param mobile
	 * @param content
	 * @return
	 */
	public boolean sendSmsImportant(String mobile, String content);
}
