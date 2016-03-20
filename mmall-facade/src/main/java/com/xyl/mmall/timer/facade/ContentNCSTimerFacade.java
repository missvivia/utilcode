package com.xyl.mmall.timer.facade;

import com.netease.print.common.meta.RetArg;

/**
 * NCS索引定时器
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public interface ContentNCSTimerFacade {

	/**
	 * 
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg dispatch();
	
}
