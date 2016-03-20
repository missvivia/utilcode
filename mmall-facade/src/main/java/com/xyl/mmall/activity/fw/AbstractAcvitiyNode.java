package com.xyl.mmall.activity.fw;

import java.util.Map;

/**
 * 
 * Contains some basic common operations.
 * 
 * @author hzzhanghui
 *
 */
public abstract class AbstractAcvitiyNode implements ActivityNode {
	
	/**
	 * Execute order.
	 */
	protected int order;
	
	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public void setOrder(int order) {
		this.order = order;
	}
	
	@Override
	public Object execute(String activityId, Map<String, Object> params) throws ActivityNodeException {
		// do something basic.
		
		// then call sub-class
		return doAction(activityId, params);
	}

	protected abstract Object doAction(String activityId, Map<String, Object> params) throws ActivityNodeException;
}
