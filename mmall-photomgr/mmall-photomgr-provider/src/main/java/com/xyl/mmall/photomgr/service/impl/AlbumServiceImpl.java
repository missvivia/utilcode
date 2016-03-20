package com.xyl.mmall.photomgr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.photomgr.dao.AlbumDirDao;
import com.xyl.mmall.photomgr.dao.AlbumImgDao;
import com.xyl.mmall.photomgr.dao.AlbumUserDao;
import com.xyl.mmall.photomgr.dao.AlbumWaterPrintDao;
import com.xyl.mmall.photomgr.dto.AlbumDTO;
import com.xyl.mmall.photomgr.meta.AlbumDir;
import com.xyl.mmall.photomgr.meta.AlbumImg;
import com.xyl.mmall.photomgr.meta.AlbumUser;
import com.xyl.mmall.photomgr.meta.AlbumWaterPrint;
import com.xyl.mmall.photomgr.service.AlbumService;
import com.xyl.mmall.photomgr.service.Util;

/**
 * 相册后台服务
 * 
 * @author hzzhanghui
 * 
 */
@Service
public class AlbumServiceImpl implements AlbumService {
	private static final Logger logger = LoggerFactory.getLogger(AlbumServiceImpl.class);

	@Autowired
	private AlbumDirDao dirDao;

	@Autowired
	private AlbumUserDao userDao;

	@Autowired
	private AlbumImgDao imgDao;

	@Autowired
	private AlbumWaterPrintDao waterDao;

	/**
	 * 添加N个(>=1)图片.
	 * 
	 * @param albumDTO
	 *            待插入图片DTO。包含一个图片列表。
	 * @return 全部插入成功返回true；否则有任何一个图片插入失败就会返回false
	 */
	@Transaction
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public List<AlbumImg> saveImg(AlbumDTO albumDTO) {
		userDao.saveAlbumUser(albumDTO.getAlbumUser());
		AlbumDir dir = dirDao.saveAlbumDir(albumDTO.getAlbumDir());

		// fill 'dirId' field for all img object
		List<AlbumImg> imgList = albumDTO.getAlbumImgList();
		for (AlbumImg img : imgList) {
			img.setDirId(dir.getId());
		}
		imgDao.saveAlbumImgs(albumDTO.getAlbumImgList());
		return albumDTO.getAlbumImgList();
	}

	/**
	 * 根据图片id获取图片信息
	 * 
	 * @param imgId
	 * @return
	 */
	public AlbumDTO getImgById(long imgId) {
		logger.debug("getImgById:" + imgId);
		AlbumDTO dto = new AlbumDTO();
		try {
			AlbumImg img = imgDao.getAlbumImgById(imgId);
			if (img == null) {
				return dto;
			}

			List<AlbumImg> imgList = new ArrayList<AlbumImg>();
			imgList.add(img);
			dto.setAlbumImgList(imgList);

			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return dto;
		}
	}
	
	/**
	 * 删除N个图片。 保留用户和目录信息。
	 * 
	 * @param albumDTO
	 *            待删除图片DTO。包含一个图片列表。
	 * @return 全部删除成功返回true。否则有一个图片删除失败就会返回false。
	 */
	@Transaction
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public boolean delImg(AlbumDTO albumDTO) {
		// resetNosPathForDefaultCategoryImgs(albumDTO);
		//
		// List<AlbumImg> imgList = albumDTO.getAlbumImgList();
		// List<AlbumImg> tmpList = new ArrayList<AlbumImg>();
		// for (AlbumImg img : imgList) {
		// img = imgDao.getAlbumImgById(img.getId());
		// tmpList.add(img);
		// }
		// albumDTO.getAlbumImgList().clear();
		// albumDTO.setAlbumImgList(tmpList);

		return imgDao.delAlbumImgs(albumDTO.getAlbumImgList());
	}

	/**
	 * 查询用户在指定目录下的所有图片。
	 * 
	 * @param albumDTO
	 *            包含用户id和指定目录id信息的DTO
	 * @return 包含该用户在指定目录下的所有图片DTO。
	 */
	public AlbumDTO queryImgList(AlbumDTO albumDTO) {
		AlbumDir dir = dirDao.getAlbumDirById(albumDTO.getAlbumDir().getId());
		if (dir == null) {
			logger.debug("cannot find category by userId '" + albumDTO.getAlbumDir().getUserId()
					+ "' and categoryId ' " + albumDTO.getAlbumDir().getId() + " '!");
			return albumDTO;
		}

		List<AlbumImg> imgList = imgDao.getAlbumImgListByDirId(dir.getUserId(), dir.getId());
		albumDTO.setAlbumImgList(imgList);
		return albumDTO;
	}

	/**
	 * 查询用户的所有图片。 包括该用户所有目录下的图片。
	 * 
	 * @param userId
	 *            用户标识
	 * @return 该用户所有图片
	 */
	public List<AlbumDTO> queryAllImgList(long userId) {
		List<AlbumDTO> list = new ArrayList<AlbumDTO>();

		AlbumUser user = userDao.getAlbumUserByUserId(userId);
		if (user == null) {
			logger.debug("cannot find category by userId '" + userId + "'!");
			return new ArrayList<AlbumDTO>();
		}
		List<AlbumDir> dirList = dirDao.getAlbumDirListByUserId(userId);
		for (AlbumDir dir : dirList) {
			AlbumDTO dto = new AlbumDTO();
			dto.setAlbumUser(user);
			dto.setAlbumDir(dir);
			dto.setAlbumImgList(imgDao.getAlbumImgListByDirId(userId, dir.getId()));

			list.add(dto);
		}

		return list;
	}

	@Override
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public AlbumDTO createDir(AlbumDTO albumDTO) {
		userDao.saveAlbumUser(albumDTO.getAlbumUser());
		dirDao.saveAlbumDir(albumDTO.getAlbumDir());
		return albumDTO;
	}

	@Override
	public List<AlbumDTO> queryDirs(AlbumDTO albumDTO) {
		List<AlbumDir> dirList = dirDao.getAlbumDirListByUserId(albumDTO.getAlbumUser().getUserId());

		List<AlbumDTO> list = new ArrayList<AlbumDTO>();
		for (AlbumDir dir : dirList) {
			AlbumDTO dto = new AlbumDTO();
			dto.setAlbumDir(dir);
			dto.setAlbumUser(albumDTO.getAlbumUser());

			list.add(dto);
		}

		return list;
	}
	
	@Override
	public AlbumDir getDirById(long dirId, long userId) {
		logger.debug("getDirById() : dirId=" + dirId + ", userId=" + userId);
		return dirDao.getDirById(dirId, userId);
	}
	
//	@Override
//	public List<AlbumDir> getDirListByUserId(long userId) {
//		logger.debug("getDirListByUserId(): userId=" + userId);
//		return dirDao.getDirListByUserId(userId);
//	}

	@Override
	public List<AlbumImg> batchQueryImgList(List<Long> idList) {
		List<AlbumImg> imgList = new ArrayList<AlbumImg>();
		for (Long id : idList) {
			AlbumImg img = imgDao.getAlbumImgById(id);
			if (img != null) {
				imgList.add(img);
			}
		}
		
		return imgList;
		//return imgDao.batchQueryImgList(idList);
	}
	
	/**
	 * 查询用户在指定目录下的符合条件所有图片。
	 * 
	 * @param albumDTO
	 *            包含用户id和指定目录id信息，以及各种查询条件的DTO
	 * @return 包含该用户在指定目录下的符合条件所有图片DTO。
	 */
	public AlbumDTO queryImgListByDirAndCond(AlbumDTO albumDTO) {
		AlbumDir dir = null;
		if (albumDTO.getAlbumDir().getId() == Util.DEFAULT_DIR_ID) {
			dir = dirDao.getAlbumDirByName(albumDTO.getAlbumDir());
		} else {
			dir = dirDao.getAlbumDirById(albumDTO.getAlbumDir().getId());
		}
		if (dir == null) {
			logger.debug("cannot find category by userId '" + albumDTO.getAlbumDir().getUserId()
					+ "' and categoryId ' " + albumDTO.getAlbumDir().getDirName() + " '!");
			return albumDTO;
		}

		int curPage = adjustCurPage(albumDTO.getCurPage());
		List<AlbumImg> imgList = imgDao.getAlbumImgListByCond(dir.getUserId(), dir.getId(),
				albumDTO.getQueryStartTime(), albumDTO.getQueryEndTime(), albumDTO.getQueryImgName(), curPage,
				albumDTO.getPageSize());
		albumDTO.setAlbumImgList(imgList);
		return albumDTO;
	}

	private int adjustCurPage(int curPage) {
		//return curPage + 1;
		return curPage;
	}

	/**
	 * 把图片从一个目录移动到另外一个目录
	 * 
	 * @param src
	 *            原目录
	 * @param dest
	 *            目的目录
	 * @return 移动成功返回true。否则返回false。
	 */
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public boolean moveImg(AlbumDTO src, AlbumDTO dest) {
		AlbumImg srcImg = src.getAlbumImgList().get(0);
		if (srcImg == null) {
			logger.debug("Src img cannot be null!");
			return false;
		}
		srcImg.setDirId(dest.getAlbumDir().getId());

		imgDao.saveAlbumImg(srcImg);

		return true;
	}

	@Override
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public boolean updateImgDir(List<AlbumImg> imgList) {
		for (AlbumImg img : imgList) {
			if (!imgDao.updateImgDir(img)) {
				logger.error("Error update dir for image '" + img.getId() + "'!!!");
			}
		}
		return true;
	}

	@Override
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public boolean chgDir(String newDirName, long userId, String oldDirName) {
		AlbumDir queryDir = new AlbumDir();
		queryDir.setUserId(userId);
		queryDir.setDirName(newDirName);
		AlbumDir dbNewDir = dirDao.getAlbumDirByName(queryDir);

		queryDir.setDirName(oldDirName);

		AlbumDir dbOldDir = dirDao.getAlbumDirByName(queryDir);

		return imgDao.chgDir(dbNewDir.getId(), userId, dbOldDir.getId());
	}

	@Override
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public boolean delDir(String dirName, long userId) {
		AlbumDir queryDir = new AlbumDir();
		queryDir.setUserId(userId);
		queryDir.setDirName(dirName);
		AlbumDir dbDir = dirDao.getAlbumDirByName(queryDir);

		return dirDao.delAlbumDir(dbDir);
	}
	
	@Override
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public boolean delDirById(long dirId) {
		AlbumDir dir = new AlbumDir();
		dir.setId(dirId);
		return dirDao.delAlbumDir(dir);
	}
	
	@Override
	@CacheEvict(value = { "albumCache" }, allEntries = true)
	public boolean updateDir(AlbumDir dir) {
		return dirDao.updateDir(dir);
	}

	// ///////////////////////////////////
	// 水印相关
	// //////////////////////////////////

	/**
	 * 查询用户对应的水印配置
	 * 
	 * @param userId
	 * @return 不存在的情况下返回null
	 */
	@Override
	public AlbumDTO getWaterPrintByUserId(long userId) {
		AlbumWaterPrint wp = waterDao.getAlbumWaterPrintByUserId(userId);
		AlbumDTO dto = new AlbumDTO();
		dto.setAlbumWaterPrint(wp);

		return dto;
	}

	/**
	 * 新增水印设置
	 * 
	 * @param dto
	 * @return
	 */
	@Override
	public AlbumDTO saveWaterPrint(AlbumDTO dto) {
		waterDao.saveAlbumWaterPrint(dto.getAlbumWaterPrint());
		return dto;
	}

	/**
	 * 更新水印设置
	 * 
	 * @param dto
	 * @return
	 */
	@Override
	public boolean updateWaterPrint(AlbumDTO dto) {
		return waterDao.updateAlbumWaterPrint(dto.getAlbumWaterPrint());
	}

	// private void resetNosPathForDefaultCategoryImgs(AlbumDTO albumDTO) {
	// if (albumDTO.getAlbumDir().getId() == Util.DEFAULT_DIR_ID) {
	// AlbumDir dir = dirDao.getAlbumDirByName(albumDTO.getAlbumDir());
	// albumDTO.setAlbumDir(dir);
	// }
	// }
}
