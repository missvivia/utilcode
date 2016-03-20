package com.xyl.mmall.common.out.facade;

import com.xyl.mmall.common.param.SMSParam;


/**
 * 调用短信平台接口
 * @author author:lhp
 *
 * @version date:2015年8月11日下午6:32:05
 */
public interface SMSFacade {
	
	/**
	 * 发货短信
	 * @param smsParam
	 * @return
	 */
	public int sendGoodsSingle(SMSParam smsParam);

	/**
	 * 发送短信验证码
	 * @param mobile
	 * @param type
	 */
	public void sendCode(String mobile, String type);
	
	/**
	 * 校验验证码
	 * @param mobile
	 * @param code
	 * @return
	 */
	public boolean checkCode(String mobile, String code, String type);
}
