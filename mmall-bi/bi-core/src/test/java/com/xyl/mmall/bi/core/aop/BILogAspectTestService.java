package com.xyl.mmall.bi.core.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.core.enums.OpAction;
import com.xyl.mmall.bi.core.meta.WebLog;
import com.xyl.mmall.bi.core.service.BILogNotifyMessageProducer;

/**
 * 
 * @author wangfeng
 * 
 */
public class BILogAspectTestService {

	@Autowired
	private BILogNotifyMessageProducer biLogNotifyMessageProducer;

	private static Logger logger = LoggerFactory.getLogger(BILogAspectTestService.class);

	@BILog(action = "page", type = "newPage")
	public boolean biLog() {
		logger.info("biLog");
		return true;
	}

	@BILog(clientType = "test clientType value", action = "page", type = "findPage")
	public boolean clientType() {
		logger.info("biLog clientType");
		return true;
	}

	public boolean noBI() {
		logger.info("bo bi");
		return true;
	}

	@BILog(action = "click", type = "homePage")
	public boolean sendMessage(String accountId) {
		sendBILog(accountId);
		return true;
	}

	private void sendBILog(String accountId) {
		WebLog webLog = new WebLog();
		webLog.setAccountId(accountId);
		webLog.setAction(OpAction.PAGE);
		webLog.setBrowser("ie8");
		webLog.setClientType(ClientType.WEB);
		webLog.setCookie("cookie");
		webLog.setDeviceOs("win7");
		webLog.setIp("127.0.0.1");
		webLog.setTime(System.currentTimeMillis());
		webLog.setType(BIType.CARTPAGE);
		String otherKey = "testOtherKey";
		biLogNotifyMessageProducer.sendBILog(webLog, otherKey);

		System.out.print("end");
	}

}
