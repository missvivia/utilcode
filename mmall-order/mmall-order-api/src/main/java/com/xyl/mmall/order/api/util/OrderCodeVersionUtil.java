package com.xyl.mmall.order.api.util;

/**
 * 用于查看服务版本，避免部署的服务于已提交的代码版本不符
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public class OrderCodeVersionUtil {

	private static final String ORDER_CODE_PUSH_TIME = "2014-12-22 09:21:00";

	private static final String ORDER_CODE_CHANGE_LOG = "在各个环节下面添加了return.properties配置文件";
	
	public static String getVersion() {
		return ORDER_CODE_PUSH_TIME + " -> " + ORDER_CODE_CHANGE_LOG;
	}
}
