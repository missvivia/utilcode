package com.xyl.mmall;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.SalescheduleConfig;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SalescheduleConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("test")
public class PO0202 {

	static {
		System.setProperty("spring.profiles.active", "test");
	}

	@Resource
	private ScheduleService scheduleService;
	
	@Resource
	private BrandService brandService;

	
	@Test
	public void testScript0123() {
		scheduleService.test();
	}
	
}
