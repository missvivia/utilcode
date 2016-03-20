package com.xyl.mmall.base;


/**
 * job父类
 * 保证同种类型的job同一时刻只能串行执行
 * @author hzzhaozhenzuo
 *
 */
public abstract class BaseJob {
	
	public boolean entrance(JobParam param){
		return execute(param);
	}
	
	public abstract boolean execute(JobParam param);
	
	
	/**
	 * new job by lhp,采用spring schedule跑定时器
	 * @param param
	 */
	public void executeJob(){
		
	};
}
