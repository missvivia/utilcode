package com.xyl.mmall.mobile.facade.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mobile.facade.MobilePushManageFacade;
import com.xyl.mmall.mobile.facade.TestFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.saleschedule.dto.UserFavListDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.task.enums.PushMessageType;

@Facade
public class TestFacadeImpl implements TestFacade{



	private static final Logger logger = LoggerFactory.getLogger(TestFacadeImpl.class);


	public boolean execute() {
		System.out.println("hello");
		return true;
	}
	

}
