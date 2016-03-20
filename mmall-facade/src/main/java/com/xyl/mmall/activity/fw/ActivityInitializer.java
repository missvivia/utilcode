package com.xyl.mmall.activity.fw;

import java.util.Map;

import org.springframework.context.ApplicationContext;

/**
 * Initialize the activity.
 * 
 * @author hzzhanghui
 *
 */
public interface ActivityInitializer {
	
	/**
	 * Initialize the configuration of activity.
	 * 
	 * @param activityId  To locate the configuration file.
	 * @param params
	 * @param ctx Spring context
	 * @return {"timeRange":dateRangeObj, "nodeList":[nodeclass1,nodeclass2...], ...}
	 */
	public Map<String, Object> init(String activityId, Map<String, Object> params, ApplicationContext ctx) throws ActivityException;
}
