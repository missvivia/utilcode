package com.xyl.mmall.activity.fw;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;

import com.xyl.mmall.jms.service.util.ResourceTextUtil;

/**
 * Initialize the activity.
 * 
 * @author hzzhanghui
 * 
 */
public class FileActivityInitializer implements ActivityInitializer {
	private static final FileActivityInitializer instance = new FileActivityInitializer();

	private FileActivityInitializer() {
	}

	public static FileActivityInitializer getInstance() {
		return instance;
	}
	
	@Override
	public Map<String, Object> init(String activityId, Map<String, Object> params, ApplicationContext ctx) throws ActivityException {
		Map<String, Object> result = new HashMap<String, Object>();

		// locate the configuration file
		ResourceBundle rb = null;
		try {
			rb = ResourceTextUtil.getResourceBundleByName(ActivityConstants.CONFIG_DIR + activityId);
		} catch (Exception e) {
			result.put(ActivityConstants.INIT_RESULT, false);
			result.put(ActivityConstants.INIT_MSG, "Error occured when locatate the file :"
					+ ActivityConstants.CONFIG_DIR + activityId + ".properties!");
			return result;
		}

		// parse time range of activity
		String activityStartTime = rb.getString(ActivityConstants.INIT_KEY_NODE_STARTTIME);
		String activityEndTime = rb.getString(ActivityConstants.INIT_KEY_NODE_ENDTIME);
		SimpleDateFormat sdf = new SimpleDateFormat(ActivityConstants.DATE_FORMAT);
		try {
			result.put(ActivityConstants.INIT_KEY_DATE_RANGE, new DateRange(sdf.parse(activityStartTime).getTime(), sdf
					.parse(activityEndTime).getTime()));
		} catch (ParseException e) {
			result.put(ActivityConstants.INIT_RESULT, false);
			result.put(ActivityConstants.INIT_MSG, "Error occured when parse time range of activity!!["
					+ activityStartTime + "," + activityEndTime + "]");
			return result;
		}

		// parse node list for the flow of activity
		String nodeClassListStr = rb.getString(ActivityConstants.INIT_KEY_NODE_CLASS);
		String[] nodeClassNameArr = nodeClassListStr.split(ActivityConstants.INIT_NODE_LIST_SPLITTER);
		if (nodeClassNameArr == null || nodeClassNameArr.length == 0) {
			result.put(ActivityConstants.INIT_RESULT, false);
			result.put(ActivityConstants.INIT_MSG, "Error occured when parse node list:" + nodeClassNameArr);
			return result;
		}
//		String[] nodeClassNameArr = new String[]{"lotteryNode#1",
//				"bindRedPackgeNode#2",
//				"freeShippingNode#3"};
		List<ActivityNode> nodeList = new ArrayList<ActivityNode>();
		for (String nodeClassName : nodeClassNameArr) {
			try {
				String[] tmpArr = nodeClassName.split("#");
				ActivityNode node = (ActivityNode) ctx.getBean(tmpArr[0]);;
				node.setOrder(Integer.parseInt(tmpArr[1]));
				nodeList.add(node);
			} catch (Exception e) {
				result.put(ActivityConstants.INIT_RESULT, false);
				result.put(ActivityConstants.INIT_MSG, "Error occured when instaniate of class:" + nodeClassName);
				return result;
			}
		}

		Collections.sort(nodeList, new Comparator<ActivityNode>() {
			@Override
			public int compare(ActivityNode o1, ActivityNode o2) {
				int order1 = o1.getOrder();
				int order2 = o2.getOrder();

				if (order1 > order2) {
					return 1;
				} else if (order1 == order2) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		
		result.put(ActivityConstants.INIT_KEY_NODE_LIST, nodeList);
		result.put(ActivityConstants.INIT_RESULT, true);
		result.put(ActivityConstants.INIT_MSG, "Init success!");

		return result;
	}
}
