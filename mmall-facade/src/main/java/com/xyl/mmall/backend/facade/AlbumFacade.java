package com.xyl.mmall.backend.facade;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.xyl.mmall.backend.vo.AlbumVO;
import com.xyl.mmall.photomgr.dto.AlbumDTO;
import com.xyl.mmall.photomgr.meta.AlbumDir;
import com.xyl.mmall.photomgr.meta.AlbumImg;

public interface AlbumFacade {
	/**
	 * 保存N张图片
	 * 
	 * @param now
	 *            时间戳
	 * @param userId
	 *            用户id
	 * @param categoryId
	 *            目录id
	 * @param imgList
	 *            图片列表。从NOS返回得到。
	 * @return
	 */
	List<AlbumImg> saveImg(long now, long userId, Long categoryId, List<AlbumImg> imgList);

	/**
	 * 根据图片id获取图片信息
	 * 
	 * @param imgId
	 * @return
	 */
	AlbumVO getImgById(long imgId);

	/**
	 * 删除图片
	 * 
	 * @param userId
	 *            用户id
	 * @param idList
	 *            待上传的图片id列表
	 * @return
	 */
	boolean delImg(Long userId, List<Integer> idList);

	/**
	 * 查询用户在指定目录下的所有图片。
	 * 
	 * @param albumDTO
	 *            包含用户id和指定目录id信息的DTO
	 * @return 包含该用户在指定目录下的所有图片DTO。
	 */
	AlbumVO queryImgList(AlbumDTO albumDTO);

	/**
	 * 查询用户的所有图片。 包括该用户所有目录下的图片。
	 * 
	 * @param userId
	 *            用户标识
	 * @return 该用户所有图片
	 */
	List<AlbumVO> queryAllImgList(long userId);

	/**
	 * 给用户创建一个目录
	 * 
	 * @param albumDTO
	 * @return 返回带id的目录对象
	 */
	AlbumVO createDir(AlbumDTO albumDTO);

	/**
	 * 查询用户下所有目录列表
	 * 
	 * @return
	 */
	JSONArray getDirListByUserId(long userId);

	/**
	 * 根据一组image id查询对应image列表
	 * 
	 * @param idList
	 * @return
	 */
	List<AlbumImg> getImgListByIdList(List<Long> idList);

	/**
	 * 查询用户下所有目录名称列表
	 * 
	 * @param userId
	 * @return
	 */
	List<String> getDirNameListByUserId(long userId);

	/**
	 * 查询图片
	 * 
	 * @param now
	 *            时间戳
	 * @param userId
	 *            用户id
	 * @param categoryId
	 *            目录id
	 * @param startTime
	 *            创建开始时间
	 * @param endTime
	 *            创建结束时间
	 * @param imgName
	 *            图片名称。模糊查询
	 * @return
	 */
	AlbumVO queryImgListByDirAndCond(long now, long userId, Long categoryId, long startTime, long endTime,
			String imgName, int curPage, int pageSize);

	/**
	 * 把图片从一个目录移动到另外一个目录
	 * 
	 * @param src
	 *            原目录
	 * @param dest
	 *            目的目录
	 * @return 移动成功返回true。否则返回false。
	 */
	boolean moveImg(List<AlbumImg> imgList);

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
	 * Get dir by id
	 * @param dirId
	 * @return
	 */
	AlbumDir getDirById(long dirId, long userId);
	
	/**
	 * Update name of dir
	 * 
	 * @param dir
	 * @return True if update success, False otherwise
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
	AlbumVO getWaterPrintByUserId(long userId);

	/**
	 * 新增水印设置
	 * 
	 * @param dto
	 * @return
	 */
	AlbumVO saveWaterPrint(AlbumDTO dto);

	/**
	 * 更新水印设置
	 * 
	 * @param dto
	 * @return
	 */
	boolean updateWaterPrint(AlbumDTO dto);
}
