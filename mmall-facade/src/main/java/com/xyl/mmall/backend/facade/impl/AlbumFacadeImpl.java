package com.xyl.mmall.backend.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.backend.facade.AlbumFacade;
import com.xyl.mmall.backend.util.AlbumUtil;
import com.xyl.mmall.backend.vo.AlbumVO;
import com.xyl.mmall.cms.facade.util.POBaseUtil;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.photomgr.dto.AlbumDTO;
import com.xyl.mmall.photomgr.meta.AlbumDir;
import com.xyl.mmall.photomgr.meta.AlbumImg;
import com.xyl.mmall.photomgr.meta.AlbumUser;
import com.xyl.mmall.photomgr.service.AlbumService;
import com.xyl.mmall.photomgr.service.Util;

@Facade
public class AlbumFacadeImpl implements AlbumFacade {
	@Resource
	private AlbumService albumService;

	@Override
	public List<AlbumImg> saveImg(long now, long userId, Long categoryId, List<AlbumImg> imgList) {
		String categoryName = null;
		if (null == categoryId || categoryId == 0) {
			categoryId = Util.DEFAULT_DIR_ID;
			categoryName = Util.DEFAULT_CATEGORY_NAME;
		}

		AlbumDir dir = AlbumUtil.createDir(now, userId, categoryName, categoryId);
		AlbumUser user = AlbumUtil.createUser(now, userId);

		AlbumDTO dto = new AlbumDTO();
		dto.setAlbumUser(user);
		dto.setAlbumDir(dir);
		dto.setAlbumImgList(imgList);

		return albumService.saveImg(dto);
	}

	@Override
	public AlbumVO getImgById(long imgId) {
		AlbumDTO dto = albumService.getImgById(imgId);
		AlbumVO vo = new AlbumVO();
		vo.setDto(dto);
		return vo;
	}

	@Override
	public boolean delImg(Long userId, List<Integer> idList) {
		AlbumDTO dto = new AlbumDTO();
		dto.setAlbumImgList(new ArrayList<AlbumImg>());

		for (Integer id : idList) {
			AlbumImg img = new AlbumImg();
			img.setUserId(userId);
			img.setId(id);
			dto.getAlbumImgList().add(img);
		}

		return albumService.delImg(dto);
	}

	@Override
	public boolean updateDir(AlbumDir dir) {
		return albumService.updateDir(dir);
	}
	
	@Override
	public AlbumVO queryImgList(AlbumDTO albumDTO) {
		AlbumDTO dto = albumService.queryImgList(albumDTO);
		AlbumVO vo = new AlbumVO();
		vo.setDto(dto);
		return vo;
	}

	@Override
	public List<AlbumVO> queryAllImgList(long userId) {
		List<AlbumDTO> dtoList = albumService.queryAllImgList(userId);

		List<AlbumVO> voList = new ArrayList<AlbumVO>();
		for (AlbumDTO dto : dtoList) {
			AlbumVO vo = new AlbumVO();
			vo.setDto(dto);

			voList.add(vo);
		}

		return voList;
	}

	@Override
	public AlbumVO createDir(AlbumDTO albumDTO) {
		AlbumDTO dto = albumService.createDir(albumDTO);
		AlbumVO vo = new AlbumVO();
		vo.setDto(dto);
		return vo;
	}

	@Override
	public JSONArray getDirListByUserId(long userId) {
		AlbumUser user = AlbumUtil.createUser(System.currentTimeMillis(), userId);
		AlbumDTO dto = new AlbumDTO();
		dto.setAlbumUser(user);
		List<AlbumDTO> dirList = albumService.queryDirs(dto);

		JSONArray arr = new JSONArray();
		for (AlbumDTO item : dirList) {
			JSONObject dir = new JSONObject();
			dir.put("id", item.getAlbumDir().getId() + POBaseUtil.NULL_STR);
			dir.put("name", item.getAlbumDir().getDirName());

			arr.add(dir);
		}

		return arr;
	}
	
	@Override
	public List<AlbumImg> getImgListByIdList(List<Long> idList) {
		if (idList == null || idList.size() == 0) {
			return new ArrayList<AlbumImg>();
		}
		return albumService.batchQueryImgList(idList);
	}

	@Override
	public List<String> getDirNameListByUserId(long userId) {
		AlbumUser user = AlbumUtil.createUser(System.currentTimeMillis(), userId);
		AlbumDTO dto = new AlbumDTO();
		dto.setAlbumUser(user);
		List<AlbumDTO> dirList = albumService.queryDirs(dto);
		if (dirList == null) {
			return new ArrayList<String>();
		}
		List<String> dirNameList = new ArrayList<String>();
		for (AlbumDTO album : dirList) {
			dirNameList.add(album.getAlbumDir().getDirName());
		}

		return dirNameList;
	}

	@Override
	public AlbumVO queryImgListByDirAndCond(long now, long userId, Long categoryId, long startTime, long endTime,
			String imgName, int curPage, int pageSize) {
		String categoryName = null;
		if (null == categoryId || categoryId == 0) {
			categoryId = Util.DEFAULT_DIR_ID;
			categoryName = Util.DEFAULT_CATEGORY_NAME;
		}

		AlbumUser user = AlbumUtil.createUser(now, userId);
		AlbumDir dir = AlbumUtil.createDir(now, userId, categoryName, categoryId);

		AlbumDTO dto = new AlbumDTO();
		dto.setAlbumUser(user);
		dto.setAlbumDir(dir);
		dto.setQueryStartTime(startTime);
		dto.setQueryEndTime(endTime);
		dto.setQueryImgName(imgName);
		dto.setCurPage(curPage);
		dto.setPageSize(pageSize);

		dto = albumService.queryImgListByDirAndCond(dto);
		AlbumVO vo = new AlbumVO();
		vo.setDto(dto);
		return vo;
	}

	@Override
	public boolean moveImg(List<AlbumImg> imgList) {
		return albumService.updateImgDir(imgList);
	}

	
	@Override
	public boolean chgDir(String newDirName, long userId, String oldDirName) {
		return albumService.chgDir(newDirName, userId, oldDirName);
	}
	
	@Override
	public boolean delDir(String dirName, long userId) {
		return albumService.delDir(dirName, userId);
	}
	
	@Override
	public boolean delDirById(long dirId) {
		return albumService.delDirById(dirId);
	}
	
	@Override
	public AlbumDir getDirById(long dirId, long userId) {
//		AlbumUser user = new AlbumUser();
//		user.setId(userId);
//		AlbumDTO dto = new AlbumDTO();
//		dto.setAlbumUser(user);
//		List<AlbumDTO> dirList = albumService.queryDirs(dto);
//		
//		for (AlbumDTO album : dirList) {
//			if (album.getAlbumDir().getId() == dirId) {
//				return album.getAlbumDir();
//			}
//		}
//		
//		return null;
		
		return albumService.getDirById(dirId, userId);
	}
	
	// ///////////////////////////////////
	// 水印相关
	// //////////////////////////////////
	@Override
	public AlbumVO getWaterPrintByUserId(long userId) {
		AlbumDTO dto = albumService.getWaterPrintByUserId(userId);
		AlbumVO vo = new AlbumVO();
		vo.setDto(dto);
		return vo;
	}

	@Override
	public AlbumVO saveWaterPrint(AlbumDTO dto) {
		dto = albumService.saveWaterPrint(dto);
		AlbumVO vo = new AlbumVO();
		vo.setDto(dto);
		return vo;
	}

	@Override
	public boolean updateWaterPrint(AlbumDTO dto) {
		return albumService.updateWaterPrint(dto);
	}
}
