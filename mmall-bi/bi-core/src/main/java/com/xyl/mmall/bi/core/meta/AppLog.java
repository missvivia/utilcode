package com.xyl.mmall.bi.core.meta;

import java.util.HashMap;

/**
 * app日志.
 * 
 * @author wangfeng
 * 
 */
public class AppLog extends BasicLog {

	private static final long serialVersionUID = -7243360179913967481L;
	
	private HashMap<String,Object> log;
	
	public AppLog(){
		super();
	}

	public HashMap<String,Object> getLog() {
		return log;
	}

	public void setLog(HashMap<String,Object> log) {
		this.log = log;
	}
	
	
}
