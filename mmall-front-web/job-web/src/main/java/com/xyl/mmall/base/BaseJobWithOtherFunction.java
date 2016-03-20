package com.xyl.mmall.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;

/**
 * 带有邮件发送等功能的job父类
 * @author hzzhaozhenzuo
 *
 */
public abstract class BaseJobWithOtherFunction extends BaseJob{
	
	@Autowired
	private MessagePushFacade messagePushFacade;
	
	/**
	 * 子类调用此方法发送邮件
	 * @param jobParam execute方法传过来的参数，此处是为了获取要发送邮件的列表
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * @return
	 */
	public boolean sendMail(JobParam jobParam,String title,String content){
		boolean successFlag=true;
		String mailParam=(String) jobParam.getParamMap().get(JobCodeInfo.SEND_MAIL);
		String[] mailArr=mailParam.split("\\;");
		for(String mail:mailArr){
			if(!messagePushFacade.sendMail(MailType.NORMAL,mail, title, content)){
				successFlag=false;
			}
		}
		return successFlag;
	}
	
}
