package com.xyl.mmall.cms.facade.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.photomgr.meta.AlbumImg;

/**
 * 图片空间实用类
 * 
 * @author hzzhanghui
 *
 */
public final class AlbumUtil {

	private AlbumUtil() {
		
	}
	
	public static JSONArray convertImgListToJSONArray(List<AlbumImg> imgList) {
		JSONArray result = new JSONArray();
		
		if (imgList == null) {
			return result;
		}
		
		for (AlbumImg img : imgList) {
			JSONObject item = POBaseUtil.toJSON(img);
			result.add(item);
		}
		
		return result;
	}
	
}
