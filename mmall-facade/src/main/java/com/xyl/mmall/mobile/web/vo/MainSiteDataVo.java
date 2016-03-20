package com.xyl.mmall.mobile.web.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.saleschedule.dto.BrandItemDTO;

public class MainSiteDataVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//推广
	private List<PromotionContent> promotionContentList;
	
	//最新特卖
	private JSONArray latestNewSchedule;
	
	//预告档期
	private Map<String, JSONArray> preScheduleMap;
	
	// today's foreshow 
	private JSONArray todayScheduleMap;
	
	//入驻品牌
	private List<BrandItemDTO> brandList;
	
	// 内容类目列表
	private List<CategoryContentDTO> categoryList;

	public List<PromotionContent> getPromotionContentList() {
		return promotionContentList;
	}

	public void setPromotionContentList(List<PromotionContent> promotionContentList) {
		this.promotionContentList = promotionContentList;
	}

	public JSONArray getLatestNewSchedule() {
		return latestNewSchedule;
	}

	public void setLatestNewSchedule(JSONArray latestNewSchedule) {
		this.latestNewSchedule = latestNewSchedule;
	}

	public Map<String, JSONArray> getPreScheduleMap() {
		return preScheduleMap;
	}

	public void setPreScheduleMap(Map<String, JSONArray> preScheduleMap) {
		this.preScheduleMap = preScheduleMap;
	}

	public JSONArray getTodayScheduleMap() {
		return todayScheduleMap;
	}

	public void setTodayScheduleMap(JSONArray todayScheduleMap) {
		this.todayScheduleMap = todayScheduleMap;
	}

	public List<BrandItemDTO> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<BrandItemDTO> brandList) {
		this.brandList = brandList;
	}

	public List<CategoryContentDTO> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryContentDTO> categoryList) {
		this.categoryList = categoryList;
	}

}
