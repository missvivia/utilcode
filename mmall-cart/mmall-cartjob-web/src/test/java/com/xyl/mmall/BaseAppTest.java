package com.xyl.mmall;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PropertyPlaceholderAutoConfiguration.class })
public abstract class BaseAppTest{
	
	static{
		System.setProperty("spring.profiles.active", "dev");
	}
	
}