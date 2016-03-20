package com.xyl.mmall.activity.fw;

import java.util.Map;

/**
 * Abstract of one step for activity. Sub class has responsibility to implement
 * business logic.
 * 
 * @author hzzhanghui
 * 
 */
public interface ActivityNode {

	/**
	 * Execute the special node of activity.
	 * 
	 * @param activityId
	 *            Identifier of activity. Mandatory.
	 * @param params
	 *            Parameters that needed to execute apply the activity.
	 * @return Result of this node.
	 */
	Object execute(String activityId, Map<String, Object> params) throws ActivityNodeException;
	
	int getOrder();
	
	void setOrder(int order);
}
