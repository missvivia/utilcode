package com.xyl.mmall.activity.fw;

import java.util.Map;

import org.springframework.context.ApplicationContext;

/**
 * Initialize the activity.
 * 
 * @author hzzhanghui
 *
 */
public class DBActivityInitializer implements ActivityInitializer {
	private static final DBActivityInitializer instance = new DBActivityInitializer();
	private DBActivityInitializer(){}

	public static DBActivityInitializer getInstance() {
		return instance;
	}

	@Override
	public Map<String, Object> init(String activityId, Map<String, Object> params, ApplicationContext ctx)
			throws ActivityException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
