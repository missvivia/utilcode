package com.xyl.mmall;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.AopContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.xyl.mmall.CartConfig;
import com.xyl.mmall.cart.dto.SkuParam;
import com.xyl.mmall.cart.service.CartService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CartConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("performance")
@EnableAutoConfiguration
public class AppTest {

	static {
		System.setProperty("spring.profiles.active", "performance");
	}

	@Resource
	private CartService cartservice;

	@Test
	public void saveCartItemTest() {
		cartservice.getCart(1,1);
		
	}
	
	@Test
	public void getInventoryTest() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(2L);
		ids.add(9L);
		ids.add(3L);
		ids.add(7L);
		
		
		cartservice.getInventoryCount(ids);
		
	}
}
