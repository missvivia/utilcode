package com.xyl.mmall.framework.interfaces;

/**
 * 支持TCC服务的Meta需要实现的接口<br>
 * 样例见: TCCMetaExam
 * 
 * @author dingmingliang
 *
 */
public interface TCCMetaInterface {

	/**
	 * 获得TCC创建时间
	 * 
	 * @return
	 */
	public long getCtimeOfTCC();

	/**
	 * 设置TCC创建时间
	 * 
	 * @param ctimeOfTCC
	 */
	public void setCtimeOfTCC(long ctimeOfTCC);

	/**
	 * 获取TCC事务Id
	 * 
	 * @return
	 */
	public long getTranId();

	/**
	 * 设置TCC事务Id
	 * 
	 * @param tranId
	 */
	public void setTranId(long tranId);
}
