package com.xyl.mmall.bi.core.aop;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 
 * @author wangfeng
 * 
 */
@ActiveProfiles("dev")
@DirtiesContext
@ContextConfiguration(locations = { "classpath:mmall-bi-msg-test.xml" })
public class BILogAspectTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private BILogAspectTestService biLogAspectTestService;

	@Test
	public void biLog() {
		boolean isSucc = biLogAspectTestService.biLog();
		assertTrue(isSucc);
	}

	@Test
	public void noBI() {
		boolean isSucc = biLogAspectTestService.noBI();
		assertTrue(isSucc);
	}

	@Test
	public void clientType() {
		boolean isSucc = biLogAspectTestService.clientType();
		assertTrue(isSucc);
	}

	@Test
	public void sendMessage() {
		String accountId = "3";
		boolean isSucc = biLogAspectTestService.sendMessage(accountId);
		assertTrue(isSucc);
	}

}
