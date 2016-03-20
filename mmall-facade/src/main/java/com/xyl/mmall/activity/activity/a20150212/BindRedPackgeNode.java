package com.xyl.mmall.activity.activity.a20150212;

import java.util.Map;

import com.xyl.mmall.activity.fw.AbstractAcvitiyNode;
import com.xyl.mmall.activity.fw.ActivityNode;
import com.xyl.mmall.activity.fw.ActivityNodeException;
import com.xyl.mmall.activity.fw.Node;

/**
 * Bind the red package for user.
 * 
 * @author hzzhanghui
 *
 */
@Node("bindRedPackgeNode")
public class BindRedPackgeNode extends AbstractAcvitiyNode implements ActivityNode {

	@Override
	protected Object doAction(String activityId, Map<String, Object> params) throws ActivityNodeException {
		System.out.println("Begin execute BindRedPackgeNode: " + activityId + ";" + params);
		System.out.println("End execute BindRedPackgeNode!!!");
		return "BindRedPackgeNode.doAction() finished!!";
	}


}
