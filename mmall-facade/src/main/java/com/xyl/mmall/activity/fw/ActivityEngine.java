package com.xyl.mmall.activity.fw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * Activity core Engine.
 * 
 * @author hzzhanghui
 * 
 */
public class ActivityEngine {
	private Logger logger = Logger.getLogger(ActivityEngine.class);

	private static final ActivityEngine instance = new ActivityEngine();

	private ActivityEngine() {
	}

	public static ActivityEngine getEngine() {
		return instance;
	}

	/**
	 * Execute the flow of a activity.
	 * 
	 * @param activityId
	 *            Identifier of activity. Mandatory.
	 * @param params
	 *            Parameters that needed to initialize and execute apply the
	 *            activity.
	 * @return Result of the activity.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> execute(String activityId, Map<String, Object> params, ApplicationContext ctx) throws ActivityException {
		
		// load configuration
		Map<String, Object> initResult = FileActivityInitializer.getInstance().init(activityId, params, ctx);
		if (!(boolean) initResult.get(ActivityConstants.INIT_RESULT)) {
			String msg = initResult.get(ActivityConstants.INIT_MSG).toString();
			logger.error(msg);
			return buildReturnObj(ActivityConstants.RET_CODE_INIT_ERR, msg);
		}

		// check time range
		DateRange dateRange = (DateRange) initResult.get(ActivityConstants.INIT_KEY_DATE_RANGE);
		if (!dateRange.isActivityOngoing()) {
			String msg = "Sorry, activity is expired!!!";
			logger.error(msg);
			return buildReturnObj(ActivityConstants.RET_CODE_EXPIRE, msg);
		}

		// Contains execution result of all nodes. key=Node class name, value=execution result of the node
		Map<String, Object> execResultList = new HashMap<String, Object>(); 
		
		// execute all the flow one by one
		List<ActivityNode> nodeList = (List<ActivityNode>) initResult.get(ActivityConstants.INIT_KEY_NODE_LIST);
		Object preNodeExecResult = null;
		for (ActivityNode node : nodeList) {
			params.put(ActivityConstants.KEY_EXEC_PRE_NODE_RESULT, preNodeExecResult);
			try {
				preNodeExecResult = node.execute(activityId, params);
				execResultList.put(node.getClass().getSimpleName(), preNodeExecResult);
			} catch (Exception e) {
				String msg = "Error occured when executing node[" + node.getClass().getName() + "], activityId="
						+ activityId + ", params=" + params;
				logger.error(msg, e);
				return buildReturnObj(ActivityConstants.RET_CODE_ERR, msg);
			}
		}

		return buildReturnObj(ActivityConstants.RET_CODE_OK, execResultList);
	}

	private Map<String, Object> buildReturnObj(int code, Object result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ActivityConstants.EXEC_CODE, code);
		map.put(ActivityConstants.EXEC_RESULT, result);
		return map;
	}

}
