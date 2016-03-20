/**
 * 
 */
package com.xyl.mmall.oms.service;

import com.xyl.mmall.oms.meta.WarehouseLog;

/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseLogService {
	/**
	 * 添加一条日志，增加异步的支持主要是为了摆脱嵌套DB操作时，里层记录日志的操作因事务回滚而导致无法记录日志到db
	 * 
	 * @param log
	 * @param isAsyn
	 *            是否异步，如果异步会在新线程中记录日志
	 */
	void log(WarehouseLog log, boolean isAsyn);

}
