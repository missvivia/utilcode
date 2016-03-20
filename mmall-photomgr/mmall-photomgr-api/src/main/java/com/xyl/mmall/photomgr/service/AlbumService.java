package com.xyl.mmall.photomgr.service;

import java.util.List;

import com.xyl.mmall.photomgr.dto.AlbumDTO;
import com.xyl.mmall.photomgr.meta.AlbumDir;
import com.xyl.mmall.photomgr.meta.AlbumImg;

/**
 * 相冊後台服務
 * 
 * @author hzzhanghui
 * 
 */
public interface AlbumService {

	/**
	 * 添加N个(>=1)图片. 保存到NOS，同时写入相关库表
	 * 
	 * @param albumDTO
	 *            待插入图片DTO。包含一个图片列表。
	 * @return 全部插入成功返回true；否则有任何一个图片插入失败就会返回false
	 */
	List<AlbumImg> saveImg(AlbumDTO albumDTO);

	/**
	 * 根据图片id获取图片信息
	 * 
	 * @param imgId
	 * @return
	 */
	AlbumDTO getImgById(long imgId);

	/**
	 * 删除N个图片。 从NOS删除，同时删除相关库表。
	 * 
	 * @param albumDTO
	 *            待删除图片DTO。包含一个图片列表。
	 * @return 全部删除成功返回true。否则有一个图片删除失败就会返回false。
	 */
	boolean delImg(AlbumDTO albumDTO);

	/**
	 * 查询用户在指定目录下的所有图片。
	 * 
	 * @param albumDTO
	 *            包含用户id和指定目录id信息的DTO
	 * @return 包含该用户在指定目录下的所有图片DTO。
	 */
	AlbumDTO queryImgList(AlbumDTO albumDTO);

	/**
	 * 查询用户的所有图片。 包括该用户所有目录下的图片。
	 * 
	 * @param userId
	 *            用户标识
	 * @return 该用户所有图片
	 */
	List<AlbumDTO> queryAllImgList(long userId);

	/**
	 * 给用户创建一个目录
	 * 
	 * @param albumDTO
	 * @return 返回带id的目录对象
	 */
	AlbumDTO createDir(AlbumDTO albumDTO);

	/**
	 * 查询用户下所有目录列表
	 * 
	 * @param albumDTO
	 * @return
	 */
	List<AlbumDTO> queryDirs(AlbumDTO albumDTO);
	
	/**
	 * Get dir by dirId and userId
	 * @param dirId
	 * @param userId For balance purpose
	 * @return
	 */
	AlbumDir getDirById(long dirId, long userId);
	
//	/**
//	 * Get dir list by userId
//	 * @param userId
//	 * @return
//	 */
//	List<AlbumDir> getDirListByUserId(long userId);

	/**
	 * 根据一组img id获取对应图片列表
	 * 
	 * @param idList
	 * @return
	 */
	List<AlbumImg> batchQueryImgList(List<Long> idList);

	/**
	 * 查询用户在指定目录下的符合条件所有图片。
	 * 
	 * @param albumDTO
	 *            包含用户id和指定目录id信息，以及各种查询条件的DTO
	 * @return 包含该用户在指定目录下的符合条件所有图片DTO。
	 */
	AlbumDTO queryImgListByDirAndCond(AlbumDTO albumDTO);

	/**
	 * 把图片从一个目录移动到另外一个目录
	 * 
	 * @param src
	 *            原目录
	 * @param dest
	 *            目的目录
	 * @return 移动成功返回true。否则返回false。
	 */
	boolean moveImg(AlbumDTO src, AlbumDTO dest);

	/**
	 * 批量更新图片目录
	 * 
	 * @param imgList
	 * @return
	 */
	boolean updateImgDir(List<AlbumImg> imgList);

	/**
	 * 修改图片的目录
	 * 
	 * @param newDirName
	 * @param userId
	 * @param oldDirName
	 * @return
	 */
	boolean chgDir(String newDirName, long userId, String oldDirName);

	/**
	 * 删除目录
	 * 
	 * @param dirName
	 * @param userId
	 * @return
	 */
	boolean delDir(String dirName, long userId);
	
	/**
	 * Delete dir by id
	 * 
	 * @param dirId
	 * @return
	 */
	boolean delDirById(long dirId);

	/**
	 * Update dir name
	 * 
	 * @param dir
	 * @return True if update success, false otherwise
	 */
	boolean updateDir(AlbumDir dir);
	
	// ///////////////////////////////////
	// 水印相关
	// //////////////////////////////////

	/**
	 * 查询用户对应的水印配置
	 * 
	 * @param userId
	 * @return 不存在的情况下返回null
	 */
	AlbumDTO getWaterPrintByUserId(long userId);

	/**
	 * 新增水印设置
	 * 
	 * @param dto
	 * @return
	 */
	AlbumDTO saveWaterPrint(AlbumDTO dto);

	/**
	 * 更新水印设置
	 * 
	 * @param dto
	 * @return
	 */
	boolean updateWaterPrint(AlbumDTO dto);
}
