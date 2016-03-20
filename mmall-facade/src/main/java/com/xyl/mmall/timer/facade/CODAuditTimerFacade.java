package com.xyl.mmall.timer.facade;

import com.netease.print.common.meta.RetArg;

/**
 * 到付审核定时器
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月29日 上午9:09:15
 *
 */
public interface CODAuditTimerFacade {

	/**
	 * 到付审核：通过
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg passCODAuditBeforeSomeTime();
	
	/**
	 * 到付审核：通过 (白名单)
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg passCODAuditInWhiteList();
	
	/**
	 * 到付审核：取消订单（被拒绝的订单保留24小时，超过24小时未有撤销操作，到付订单自动变为取消状态）
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg cancelCODAuditOfTimeout();
	
	/**
	 * 到付审核：取消失效的审核记录
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg cancelIllegalCODAudit();
}
