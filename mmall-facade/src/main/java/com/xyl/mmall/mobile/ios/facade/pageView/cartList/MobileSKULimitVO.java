package com.xyl.mmall.mobile.ios.facade.pageView.cartList;


/** 
 * @author lhp 
 * @version 2015年11月18日 下午2:09:44 
 *  
 */
public class MobileSKULimitVO {
	
	/**
	 * 起始时间
	 */
	private long startTime;

	/**
	 * 结束时间
	 */
	private long endTime;
	
	/**
	 * 限购说明
	 */
	private String limitDescrp;
	
	/**
	 * 允许购物的数量
	 */
	private int allowBuyNum;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getLimitDescrp() {
		return limitDescrp;
	}

	public void setLimitDescrp(String limitDescrp) {
		this.limitDescrp = limitDescrp;
	}

	public int getAllowBuyNum() {
		return allowBuyNum;
	}

	public void setAllowBuyNum(int allowBuyNum) {
		this.allowBuyNum = allowBuyNum;
	}
	

}
