package com.xyl.mmall.bi.core.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.core.enums.OpAction;
import com.xyl.mmall.bi.core.meta.WebLog;
import com.xyl.mmall.bi.core.service.NQSBILogMessageProducer;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@ActiveProfiles("dev")
@DirtiesContext
@ContextConfiguration(locations = { "classpath:mmall-bi-msg-test.xml" })
public class BILogMessageProducerTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private NQSBILogMessageProducer nqsBILogMessageProducer;

	@Test
	public void sendBILog() {
		WebLog webLog = new WebLog();
		webLog.setAccountId("5L");
		webLog.setAction(OpAction.PAGE);
		webLog.setBrowser("ie8");
		webLog.setClientType(ClientType.WEB);
		webLog.setCookie("cookie");
		webLog.setDeviceOs("win7");
		webLog.setIp("127.0.0.1");
		webLog.setTime(System.currentTimeMillis());
		webLog.setType(BIType.CARTPAGE);
		String otherKey = "testOtherKey";
		nqsBILogMessageProducer.sendBILog(webLog, otherKey);

		System.out.print("end");
	}

}
