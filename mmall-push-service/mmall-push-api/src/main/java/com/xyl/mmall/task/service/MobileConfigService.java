package com.xyl.mmall.task.service;

import java.util.Map;

/**
 * push 管理接口
 * @author jiangww
 *
 */
public interface MobileConfigService {

	/**
	 * 
	 * @param account
	 * @return
	 */
	public  Map<String, String> getAllConfig();
}
