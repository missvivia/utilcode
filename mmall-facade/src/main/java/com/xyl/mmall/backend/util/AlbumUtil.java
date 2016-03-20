package com.xyl.mmall.backend.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.util.POBaseUtil;
import com.xyl.mmall.photomgr.enums.AlbumUserState;
import com.xyl.mmall.photomgr.meta.AlbumDir;
import com.xyl.mmall.photomgr.meta.AlbumImg;
import com.xyl.mmall.photomgr.meta.AlbumUser;

/**
 * 
 * @author hzzhanghui
 * 
 */
public final class AlbumUtil {

	public static final int CODE_OK = 200;

	public static final int CODE_ERR = 400;

	public static final boolean RESULT_OK = true;

	public static final boolean RESULT_ERR = false;

	public static final String RESP_CODE = "code";

	public static final String RESP_RESULT = "result";

	public static final String RESP_TOTAL = "total";

	public static final String RESP_MSG = "msg";

	public static final String RESP_LIST = "list";

	public static final String MODE_CATEGORYLIST = "categoryList";

	public static final String MODE_WATERPRINT = "waterPrint";

	public static final String REQ_PARAM_CATEGORYID = "categoryId";

	public static final String REQ_PARAM_STARTTIME = "startTime";

	public static final String REQ_PARAM_ENDTIME = "endTime";

	public static final String REQ_PARAM_NAME = "name";

	public static final String REQ_PARAM_LIMIT = "limit";

	public static final String REQ_PARAM_OFFSET = "offset";

	public static final String REQ_PARAM_ID = "id";
	
	private AlbumUtil() {
	}

	public static long getUserId() {
		return SecurityContextUtils.getUserId();
	}

	public static JSONObject generateRespJsonStr(int code, boolean result, String msg) {
		JSONObject json = new JSONObject();
		json.put(RESP_CODE, code);
		json.put(RESP_RESULT, result);
		if (msg != null) {
			json.put(RESP_MSG, msg);
		}

		return json;
	}

	public static JSONObject generateOKJsonStr(List<AlbumImg> imgList) {
		JSONObject json = new JSONObject();
		json.put(RESP_CODE, AlbumUtil.CODE_OK);
		JSONArray arr = new JSONArray();
		json.put(RESP_RESULT, arr);
		for (AlbumImg img : imgList) {
			JSONObject imgJson = new JSONObject();
			imgJson.put("id", img.getId() + POBaseUtil.NULL_STR);
			imgJson.put("userId", img.getUserId() + POBaseUtil.NULL_STR);
			imgJson.put("createDate", img.getCreateDate());
			imgJson.put("imgName", img.getImgName());
			imgJson.put("imgUrl", img.getImgUrl());
			imgJson.put("key", img.getNosPath());
			imgJson.put("width", img.getWidth());
			imgJson.put("height", img.getHeight());
			imgJson.put("imgType", img.getImgType());

			arr.add(imgJson);
		}
		return json;
	}

	public static JSONObject genImgListJson(List<AlbumImg> imgList) {
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		json.put(RESP_CODE, AlbumUtil.CODE_OK);
		json.put(RESP_RESULT, result);
		result.put(RESP_LIST, getImgJsonArr(imgList));

		return json;
	}

	public static JSONArray getImgJsonArr(List<AlbumImg> imgList) {
		JSONArray arr = new JSONArray();
		for (AlbumImg img : imgList) {
			JSONObject imgJson = new JSONObject();
			imgJson.put("imgName", img.getImgName());
			imgJson.put("userId", img.getUserId() + POBaseUtil.NULL_STR);
			imgJson.put("categoryId", img.getDirId());
			imgJson.put("width", img.getWidth());
			imgJson.put("height", img.getHeight());
			imgJson.put("imgType", img.getImgType());
			imgJson.put("imgUrl", img.getImgUrl());
			imgJson.put("imgId", img.getId() + POBaseUtil.NULL_STR);

			arr.add(imgJson);
		}

		return arr;
	}

	public static List<AlbumImg> genImgList(JSONArray arr, long now, long userId) {
		List<AlbumImg> imgList = new ArrayList<AlbumImg>();
		for (int i = 0, j = arr.size(); i < j; i++) {
			AlbumImg img = new AlbumImg();
			img.setCreateDate(now);
			img.setUserId(userId);
			img.setImgName(arr.getJSONObject(i).getString("imgName"));
			img.setImgUrl(arr.getJSONObject(i).getString("imgUrl"));
			img.setNosPath(arr.getJSONObject(i).getString("key"));
			img.setHeight(arr.getJSONObject(i).getIntValue("height"));
			img.setWidth(arr.getJSONObject(i).getIntValue("width"));
			img.setImgType(arr.getJSONObject(i).getString("imgType"));
			imgList.add(img);
		}
		return imgList;
	}

	public static AlbumUser createUser(long now, long userId) {
		AlbumUser user = new AlbumUser();
		user.setStatus(AlbumUserState.VALID);
		user.setUserCreateDate(now);
		user.setUserId(userId);
		user.setUserName(userId + "");

		return user;
	}

	public static AlbumDir createDir(long now, long userId, String dirName, Long dirId) {
		AlbumDir dir = new AlbumDir();
		if (dirName != null) {
			dir.setDirName(dirName);
		}
		dir.setUserId(userId);
		dir.setDirCreateDate(now);
		if (dirId != null) {
			dir.setId(dirId);
		}

		return dir;
	}

	public static AlbumImg createImg(long now, long userId, long dirId, String fileName, String nosPath, String imgUrl,
			InputStream is) {
		AlbumImg img = new AlbumImg();
		img.setCreateDate(now);
		img.setUserId(userId);
		if (dirId != 0L) {
			img.setDirId(dirId);
		}
		if (fileName != null) {
			img.setImgName(fileName);
		}
		if (nosPath != null) {
			img.setNosPath(nosPath);
		}
		if (imgUrl != null) {
			img.setImgUrl(imgUrl);
		}
		if (is != null) {
			img.setInputStream(is);
		}

		return img;
	}

	public static List<Integer> getImgIdList(JSONObject json) {
		List<Integer> imgIdList = new ArrayList<Integer>();

		JSONArray jsonArr = json.getJSONArray("ids");
		for (int i = 0; i < jsonArr.size(); i++) {
			imgIdList.add(jsonArr.getInteger(i));
		}

		return imgIdList;
	}

}
