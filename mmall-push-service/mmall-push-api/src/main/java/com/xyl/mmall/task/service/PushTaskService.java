package com.xyl.mmall.task.service;




/**
 * push 管理接口
 * @author jiangww
 *
 */
public interface PushTaskService {
	
	/**
	 * 定时任务扫描
	 * 
	 * 
	 */
	public boolean push(long start,long end);
	
}
