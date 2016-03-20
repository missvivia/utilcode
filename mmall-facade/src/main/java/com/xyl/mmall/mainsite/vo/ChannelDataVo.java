package com.xyl.mmall.mainsite.vo;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.xyl.mmall.cms.meta.PromotionContent;

public class ChannelDataVo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	//推广
	List<PromotionContent> promotionContentList;
	
	//最新特卖
	JSONArray latestSchedule;
	
	//title
	private String title;

	public List<PromotionContent> getPromotionContentList() {
		return promotionContentList;
	}

	public void setPromotionContentList(List<PromotionContent> promotionContentList) {
		this.promotionContentList = promotionContentList;
	}

	public JSONArray getLatestSchedule() {
		return latestSchedule;
	}

	public void setLatestSchedule(JSONArray latestSchedule) {
		this.latestSchedule = latestSchedule;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
