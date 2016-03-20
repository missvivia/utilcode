package com.xyl.mmall.order.service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.meta.HBRecycleLog;

/**
 * 退货包裹红包回收服务
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public interface HBRecycleLogService {

	/**
	 * 找出退款成功、红包未回收的退货包裹记录
	 * 
	 * @param minRetPkgId
	 * @param ddbParam
	 * @return
	 *     RetArg.List<HBRecycleLogDTO>
	 *     RetArg.DDBParam
	 */
	RetArg getReturnedButNotRecycledObjects(long minRetPkgId, DDBParam ddbParam);
	
	/**
	 * 更新状态：退款成功、红包未回收的退货包裹记录
	 * 
	 * @param hbRecycleLog
	 * @return
	 */
	boolean recycleHb(HBRecycleLog hbRecycleLog);
}
