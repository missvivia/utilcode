package com.xyl.mmall.activity.fw;

/**
 * 
 * @author hzzhanghui
 *
 */
public interface ActivityConstants {

	int RET_CODE_OK = 200;
	int RET_CODE_ERR = 400;
	int RET_CODE_EXPIRE = 100;
	int RET_CODE_INIT_ERR = 101;
	String EXEC_CODE = "code";
	String EXEC_RESULT = "result";
	String EXEC_MSG = "msg";
	
	String INIT_RESULT = "init_result";
	String INIT_MSG = "init_msg";
	String INIT_KEY_DATE_RANGE = "dateRange";
	String INIT_KEY_NODE_LIST = "nodeList";
	String INIT_NODE_LIST_SPLITTER = "~";
	
	String CONFIG_DIR = "activity.";
	
	String INIT_KEY_NODE_CLASS = "node.class";
	String INIT_KEY_NODE_STARTTIME = "activity.starttime";
	String INIT_KEY_NODE_ENDTIME = "activity.endtime";
	
	String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	String KEY_EXEC_PRE_NODE_RESULT = "key_pre_node_result";
}
