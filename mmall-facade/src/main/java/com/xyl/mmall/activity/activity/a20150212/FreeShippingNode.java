package com.xyl.mmall.activity.activity.a20150212;

import java.util.Map;

import com.xyl.mmall.activity.fw.AbstractAcvitiyNode;
import com.xyl.mmall.activity.fw.ActivityNode;
import com.xyl.mmall.activity.fw.ActivityNodeException;
import com.xyl.mmall.activity.fw.Node;

/**
 * Free shipping for user.
 * 
 * @author hzzhanghui
 *
 */
@Node("freeShippingNode")
public class FreeShippingNode extends AbstractAcvitiyNode implements ActivityNode {

	@Override
	protected Object doAction(String activityId, Map<String, Object> params) throws ActivityNodeException {
		System.out.println("Begin execute FreeShippingNode: " + activityId + ";" + params);
		System.out.println("End execute FreeShippingNode!!!");
		return "FreeShippingNode.doAction() finished!!";
	}


}
