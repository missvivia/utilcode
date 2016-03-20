package com.xyl.mmall.cms.vo;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class POPreviewVO {
	private long pageId;

	private String layout;

	private JSONObject layoutJson;

	public long getPageId() {
		return pageId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public JSONObject getLayOutJson() {
		if (layoutJson == null) {
			JSONObject result = JSON.parseObject(layout);
			
			JSONArray arr = null;
			try {
				arr = JSON.parseArray(result.getString("udSetting"));
			} catch (Exception e) {
			}
			result.put("udSetting", arr);
			
			JSONObject tmp = null;
			try {
				tmp = JSON.parseObject(result.getString("bgSetting"));
			} catch (Exception e) {
			}
			result.put("bgSetting", tmp);
			
			tmp = null;
			try {
				tmp = JSON.parseObject(result.getString("headerSetting"));
			} catch (Exception e) {
			}
			result.put("headerSetting", tmp);
			
			tmp = null;
			try {
				tmp = JSON.parseObject(result.getString("allListPartOthers"));
			} catch (Exception e) {
			}
			result.put("allListPartOthers", tmp);
			
			tmp = null;
			try {
				tmp = JSON.parseObject(result.getString("mapPartOthers"));
			} catch (Exception e) {
			}
			result.put("mapPartOthers", tmp);
			
			return result;
		}

		return layoutJson;
	}

	public List<Long> getImgIds() {
		JSONObject json = getLayOutJson();
		String imgIds = json.getString("udImgIds");
		List<Long> idList = ScheduleUtil.getItemListByItemListStr(imgIds);
		long bgImgId = json.getLongValue("bgImgId");
		if (bgImgId != 0) {
			idList.add(bgImgId);
		}
		long headerImgId = json.getLongValue("headerImgId");
		if (headerImgId != 0) {
			idList.add(headerImgId);
		}

		return idList;
	}

	public List<Long> getPrdIds() {
		JSONObject json = getLayOutJson();
		String prdIds = json.getString("udProductIds");
		List<Long> prdIdList = ScheduleUtil.getItemListByItemListStr(prdIds);
		return prdIdList;
	}
}
