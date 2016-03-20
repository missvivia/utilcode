package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.AlbumFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.util.AlbumChecker;
import com.xyl.mmall.backend.util.AlbumUtil;
import com.xyl.mmall.backend.util.ScheduleWebConfigBean;
import com.xyl.mmall.backend.vo.AlbumVO;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.facade.util.BaseChecker.ErrChecker;
import com.xyl.mmall.photomgr.dto.AlbumDTO;
import com.xyl.mmall.photomgr.meta.AlbumDir;
import com.xyl.mmall.photomgr.meta.AlbumImg;
import com.xyl.mmall.photomgr.meta.AlbumUser;

/**
 * 图片管理
 * 
 * @author hzzhanghui
 * 
 */
@Controller
public class AlbumController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);

	private static final AlbumChecker checker = new AlbumChecker(logger);

	
	@Autowired
	private AlbumFacade albumFacade;
	
	@Autowired
	private ScheduleWebConfigBean configBean;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	// /////////////////////////////
	// 页面相关
	// /////////////////////////////
	@RequestMapping(value = "/image/index")
	@RequiresPermissions(value = { "image:index" })
	public String image(Model model) {
		long userId = AlbumUtil.getUserId();
		JSONArray categoryList = albumFacade.getDirListByUserId(userId);
		model.addAttribute(AlbumUtil.MODE_CATEGORYLIST, categoryList);

		AlbumVO wp = albumFacade.getWaterPrintByUserId(userId);
		model.addAttribute(AlbumUtil.MODE_WATERPRINT, JSONObject.toJSONString(wp.getDto().getAlbumWaterPrint()));
		
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/image/upload";
	}

	@RequestMapping(value = "/image/category")
	@RequiresPermissions(value = { "image:category" })
	public String imageCagegory(Model model) {
		long userId = AlbumUtil.getUserId();
		JSONArray categoryList = albumFacade.getDirListByUserId(userId);
		model.addAttribute(AlbumUtil.MODE_CATEGORYLIST, categoryList);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/image/category";
	}
	
	@RequestMapping(value = "/rest/image/category")
	@ResponseBody
	public JSONObject ajaxImageCagegory() {
		long userId = AlbumUtil.getUserId();
		JSONArray categoryList = albumFacade.getDirListByUserId(userId);
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		json.put("code", AlbumUtil.CODE_OK);
		json.put("result", result);
		result.put("list", categoryList);
		
		return json;
	}

	@RequestMapping(value = "/image/manage")
	@RequiresPermissions(value = { "image:manage" })
	public String imageManage(Model model) {
		long userId = AlbumUtil.getUserId();
		JSONArray categoryList = albumFacade.getDirListByUserId(userId);
		model.addAttribute(AlbumUtil.MODE_CATEGORYLIST, categoryList);

		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/image/manage";
	}

	// /////////////////////////////
	// 接口相关
	// /////////////////////////////
	// 到NOS上删除需要传key，到图片管理删除需要传id
	@RequestMapping(value = "/image/manage/remove")
	@ResponseBody
	public JSONObject delImages(@RequestBody JSONObject paramJson) {
		Long userId = AlbumUtil.getUserId();
		List<Integer> idList = AlbumUtil.getImgIdList(paramJson);

		boolean result = albumFacade.delImg(userId, idList);
		if (result) {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_OK, AlbumUtil.RESULT_OK, null);
		} else {
			String msg = "Failure delete images, please check log!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
	}

	@RequestMapping(value = "/image/manage/move")
	@ResponseBody
	public JSONObject moveImg(@RequestBody JSONObject paramJson) {
		Long userId = AlbumUtil.getUserId();
		Long toCategoryId = paramJson.getLong(AlbumUtil.REQ_PARAM_CATEGORYID);

		ErrChecker errChecker = checker.checkMoveImg(toCategoryId);
		if (!errChecker.check) {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, errChecker.msg);
		}

		List<Integer> idList = AlbumUtil.getImgIdList(paramJson);
		List<AlbumImg> imgList = new ArrayList<AlbumImg>();
		for (Integer id : idList) {
			AlbumImg img = new AlbumImg();
			img.setUserId(userId);
			img.setId(id);
			img.setDirId(toCategoryId);
			imgList.add(img);
		}

		boolean result = albumFacade.moveImg(imgList);
		if (!result) {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR,
					"Failure to change category of these images " + idList);
		} else {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_OK, AlbumUtil.RESULT_OK, null);
		}
	}

	@RequestMapping(value = "/image/manage/list", method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject getUserImgListByUserId(@RequestBody JSONObject paramJson) {
		boolean needTotal = ScheduleUtil.needTotal(paramJson);
		long userId = AlbumUtil.getUserId();
		Long categoryId = paramJson.getLong(AlbumUtil.REQ_PARAM_CATEGORYID);
		Long startTime = paramJson.getLong(AlbumUtil.REQ_PARAM_STARTTIME);
		if (startTime == null) {
			startTime = 0L;
		}
		Long endTime = paramJson.getLong(AlbumUtil.REQ_PARAM_ENDTIME);
		if (endTime == null) {
			endTime = 0L;
		}

		String imgName = paramJson.getString(AlbumUtil.REQ_PARAM_NAME);

		int pageSize = paramJson.getIntValue(AlbumUtil.REQ_PARAM_LIMIT);
		int curPage = paramJson.getIntValue(AlbumUtil.REQ_PARAM_OFFSET);

		long now = System.currentTimeMillis();

		AlbumVO vo = albumFacade.queryImgListByDirAndCond(now, userId, categoryId, startTime, endTime, imgName,
				curPage, pageSize);
		List<AlbumImg> imgList = new ArrayList<AlbumImg>();
		for (AlbumImg img : vo.getDto().getAlbumImgList()) {
			imgList.add(img);
		}

		int total = 0;
		if (imgList.size() != 0 && needTotal) {
			AlbumVO vo2 = albumFacade.queryImgListByDirAndCond(now, userId, categoryId, startTime, endTime, imgName, 0,
					0);
			total = vo2.getDto().getAlbumImgList().size();
		}
		
		Collections.sort(imgList, new Comparator<AlbumImg>(){
			@Override
			public int compare(AlbumImg o1, AlbumImg o2) {
				long uploadTime1 = o1.getCreateDate();
				long uploadTime2 = o2.getCreateDate();
				long diff = uploadTime1 - uploadTime2;
				
				if (diff > 0) {
					return 1;
				} else if (diff == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		JSONObject json = AlbumUtil.genImgListJson(imgList);
		if (needTotal) {
			json.getJSONObject(AlbumUtil.RESP_RESULT).put(AlbumUtil.RESP_TOTAL, total);
		}

		return json;
	}

	@RequestMapping(value = "/image/manage/category")
	@ResponseBody
	public JSONObject addDir(@RequestBody JSONArray paramJson) {
		ErrChecker errChecker = checker.checkAddDir(paramJson);
		if (!errChecker.check) {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, errChecker.msg);
		}

		long userId = AlbumUtil.getUserId();

		List<String> paramBeanList = new ArrayList<String>();
		boolean result = true;
		for (int i = 0, j = paramJson.size(); i < j; i++) {
			JSONObject categoryJson = paramJson.getJSONObject(i);
			Long categoryId = categoryJson.getLong(AlbumUtil.REQ_PARAM_ID);
			if (categoryId == null) {
				categoryId = 0L;
			}

			String categoryName = categoryJson.getString(AlbumUtil.REQ_PARAM_NAME);
			errChecker = checker.checkAddDirName(categoryName);
			if (!errChecker.check) {
				return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, errChecker.msg);
			}
			
			paramBeanList.add(categoryName);
			
			long now = System.currentTimeMillis();
			AlbumUser user = AlbumUtil.createUser(now, userId);
			AlbumDir dir = AlbumUtil.createDir(now, userId, categoryName, categoryId);

			AlbumDTO dto = new AlbumDTO();
			dto.setAlbumUser(user);
			dto.setAlbumDir(dir);

			AlbumVO vo = albumFacade.createDir(dto);
			if (vo.getDto().getAlbumDir().getId() == 0) {
				result = false;
				break;
			}
		}
		
		List<String> dirNameList = albumFacade.getDirNameListByUserId(userId);
		for (String dirName : dirNameList) {
			if (!paramBeanList.contains(dirName)) {  // need to be deleted
				albumFacade.chgDir(configBean.getDefaultDirName(), userId, dirName);
				albumFacade.delDir(dirName, userId);
			}
		}

		if (!result) {
			String msg = "Failure to create category, please check the log!!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		} else {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_OK, AlbumUtil.RESULT_OK, null);
		}
	}

	// 这里实际上是在图片管理中建立对应记录。而非真正上传到NOS
	@RequestMapping(value = "/image/upload", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject imageUpload(@RequestBody JSONObject paramJson) {
		return _genSavedJson(paramJson);
	}

	// 这里实际上是在图片管理中建立对应记录。而非真正操作NOS
	@RequestMapping(value = "/image/manage/cropImg")
	@ResponseBody
	public JSONObject cropImg(@RequestBody JSONObject paramJson) {
		return _genSavedJson(paramJson);
	}

	// 这里实际上是在图片管理中建立对应记录。而非真正操作NOS
	@RequestMapping(value = "/image/manage/zoomImg")
	@ResponseBody
	public JSONObject zoomImg(@RequestBody JSONObject paramJson) {
		return _genSavedJson(paramJson);
	}

	private JSONObject _genSavedJson(JSONObject paramJson) {
		long userId = AlbumUtil.getUserId();
		Long categoryId = paramJson.getLong(AlbumUtil.REQ_PARAM_CATEGORYID);

		long now = System.currentTimeMillis();
		List<AlbumImg> imgList = AlbumUtil.genImgList(paramJson.getJSONArray(AlbumUtil.RESP_LIST), now, userId);

		imgList = albumFacade.saveImg(now, userId, categoryId, imgList);
		if (imgList != null && imgList.size() != 0) {
			logger.debug("You have successfully uploaded " + imgList);
			return AlbumUtil.generateOKJsonStr(imgList);
		} else {
			String msg = "Failure to upload files, please check log!!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
	}

	@RequestMapping(value = "/image/{id}")
	@ResponseBody
	public JSONObject getImgById(@PathVariable("id") Long imgId) {
		ErrChecker errChecker = checker.checkGetImgById(imgId);
		if (!errChecker.check) {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, errChecker.msg);
		}

		AlbumVO vo = albumFacade.getImgById(imgId);
		errChecker = checker.checkGetImgByIdResults(vo, imgId);
		if (!errChecker.check) {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, errChecker.msg);
		}

		AlbumImg img = vo.getDto().getAlbumImgList().get(0);
		return (JSONObject) JSONObject.toJSON(img);
	}

	@RequestMapping(value = "/image/category/list")
	@ResponseBody
	public JSONObject queryDirList() {
		Long userId = AlbumUtil.getUserId();

		JSONArray dirList = albumFacade.getDirListByUserId(userId);
		JSONObject json = new JSONObject();
		json.put(AlbumUtil.RESP_CODE, AlbumUtil.CODE_OK);
		JSONObject result = new JSONObject();
		result.put("list", dirList);
		json.put(AlbumUtil.RESP_RESULT, result);
		return json;
	}

	@RequestMapping(value = "/image/category/add")
	@ResponseBody
	public JSONObject categoryAdd(@RequestBody JSONObject paramJson) {
		Long userId = AlbumUtil.getUserId();
		String categoryName = paramJson.getString("name");
		if (categoryName == null || "".equals(categoryName.trim())) {
			String msg = "CategoryName cannot be null!!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
		
		List<String> dirNameList = albumFacade.getDirNameListByUserId(userId);
		if (dirNameList != null && dirNameList.size() != 0) {
			if (dirNameList.contains(categoryName)) {
				String msg = "Current user has already created the category '" + categoryName + "'!!!";
				logger.error(msg);
				return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
			}
		}
		
		long now = System.currentTimeMillis();
		AlbumUser user = AlbumUtil.createUser(now, userId);
		AlbumDir dir = AlbumUtil.createDir(now, userId, categoryName, 0L);

		AlbumDTO dto = new AlbumDTO();
		dto.setAlbumUser(user);
		dto.setAlbumDir(dir);

		AlbumVO vo = albumFacade.createDir(dto);
		if (vo.getDto().getAlbumDir().getId() == 0) {
			String msg = "Failure to create category, please check the log!!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		} else {
			JSONObject json = AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_OK, AlbumUtil.RESULT_OK, null);
			json.put(ScheduleUtil.RESP_RESULT, vo.getDto().getAlbumDir());
			return json;
		}
	}
	
	@RequestMapping(value = "/image/category/remove")
	@ResponseBody
	public JSONObject categoryRemove(@RequestBody JSONObject paramJson) {
		Long userId = AlbumUtil.getUserId();
		Long categoryId = paramJson.getLong("id");
		if (categoryId == null || categoryId == 0) {
			String msg = "Parameter 'categoryId' cannot be null!!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
		
		AlbumDir dir = albumFacade.getDirById(categoryId, userId);
		if (dir == null) {
			String msg = "Cannot find category by categoryId '" + categoryId + "' and userId '" + userId + "'!!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
		
		albumFacade.chgDir(configBean.getDefaultDirName(), userId, dir.getDirName());
		boolean result = albumFacade.delDir(dir.getDirName(), userId);
		if (result) {
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_OK, AlbumUtil.RESULT_OK, null);
		} else {
			String msg = "Failure delete category '" + dir.getDirName() + "', please check log!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
	}
	
	@RequestMapping(value = "/image/category/update")
	@ResponseBody
	public JSONObject categoryUpdate(@RequestBody JSONObject paramJson) {
		Long userId = AlbumUtil.getUserId();

		Long categoryId = paramJson.getLong("id");
		if (categoryId == null || categoryId == 0) {
			String msg = "Parameter 'categoryId' cannot be null!!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
		String categoryName = paramJson.getString("name");
		if (categoryName == null || "".equals(categoryName.trim())) {
			String msg = "Parameter 'categoryName' cannot be null!!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
		
		AlbumDir dir = AlbumUtil.createDir(System.currentTimeMillis(), userId, categoryName, categoryId);
		boolean result = albumFacade.updateDir(dir);
		if (result) {
			JSONObject json = AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_OK, AlbumUtil.RESULT_OK, null);
			json.put(ScheduleUtil.RESP_RESULT, dir);
			return json;
		} else {
			String msg = "Failure update category '" + categoryName + "', please check log!!";
			logger.error(msg);
			return AlbumUtil.generateRespJsonStr(AlbumUtil.CODE_ERR, AlbumUtil.RESULT_ERR, msg);
		}
	}
}