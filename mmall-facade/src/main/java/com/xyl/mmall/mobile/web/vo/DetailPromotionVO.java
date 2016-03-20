package com.xyl.mmall.mobile.web.vo;

import java.util.List;
import java.util.Map;

/**
 * 详情页的活动信息VO
 * 
 * @author hzhuangluqian
 *
 */
public class DetailPromotionVO {
	/** 活动描述 */
	private String desp;

	/** 活动标签列表 */
	private List<Map<String, String>> tipList;

	/** 活动结束倒计时 */
	private long countDown;

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public List<Map<String, String>> getTipList() {
		return tipList;
	}

	public void setTipList(List<Map<String, String>> tipList) {
		this.tipList = tipList;
	}

	public long getCountDown() {
		return countDown;
	}

	public void setCountDown(long countDown) {
		this.countDown = countDown;
	}

}
