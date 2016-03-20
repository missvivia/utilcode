package com.xyl.mmall.photomgr.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.photomgr.meta.AlbumImg;

/**
 * 相册图片DAO。
 * 
 * @author hzzhanghui
 * 
 */
public interface AlbumImgDao extends AbstractDao<AlbumImg> {
	/**
	 * 新增或更新图片信息。
	 * 
	 * @param img
	 *            待添加或更新图片对象。
	 * @return 返回待id的图片对象
	 */
	AlbumImg saveAlbumImg(AlbumImg img);

	/**
	 * 更新图片所属目录
	 * 
	 * @param img
	 * @return
	 */
	boolean updateImgDir(AlbumImg img);

	/**
	 * 批量修噶图片目录
	 * 
	 * @param newDirId
	 * @param userId
	 * @param oldDirId
	 * @return
	 */
	boolean chgDir(long newDirId, long userId, long oldDirId);

	/**
	 * 批量新增或更新图片信息。
	 * 
	 * @param imgList
	 *            待添加或更新的图片列表
	 * @return 返回更新后的带id的图片列表
	 */
	List<AlbumImg> saveAlbumImgs(List<AlbumImg> imgList);
	
	/**
	 * 删除图片。
	 * 
	 * @param img
	 *            待删除图片对象。
	 * @return 删除成功返回true。否则返回false。
	 */
	boolean delAlbumImg(AlbumImg img);

	/**
	 * 批量删除图片。
	 * 
	 * @param imgList
	 *            待删除的图片列表。
	 * @return 全部删除成功返回true。任何一个删除失败返回false。
	 */
	boolean delAlbumImgs(List<AlbumImg> imgList);

	/**
	 * 根据主键id查询图片信息。
	 * 
	 * @param id
	 *            主键
	 * @return 返回DB中的图片信息
	 */
	AlbumImg getAlbumImgById(long id);

	/**
	 * 根据userId查询图片信息。
	 * 
	 * @param userId
	 * @return 返回DB中的图片信息
	 */
	AlbumImg getAlbumImgByUserId(long userId);

//	/**
//	 * 根据图片nos路径查询图片信息。
//	 * 
//	 * @param nosPath
//	 *            图片在nos上的路径
//	 * @return 返回DB中的图片信息
//	 */
//	AlbumImg getAlbumImgByNosPath(String nosPath);

//	/**
//	 * 根据图片URL查询图片信息。
//	 * 
//	 * @param imgUrl
//	 *            图片URL
//	 * @return 返回DB中的图片信息
//	 */
//	AlbumImg getAlbumImgByUrl(String imgUrl);

	/**
	 * 查询所有用户所有目录下的所有图片。 包括所有用户所有目录下的图片。
	 * 
	 * @return 返回所欲用户所有目录下的所有图片列表。
	 */
	List<AlbumImg> getAllImgs();

	/**
	 * 查询某个用户指定目录下的图片列表。
	 * 
	 * @param dirId
	 * @return
	 */
	List<AlbumImg> getAlbumImgListByDirId(long userId, long dirId);
	
	/**
	 * 根据一组img id获取对应图片列表
	 * 
	 * @param idList
	 * @return
	 */
	List<AlbumImg> batchQueryImgList(List<Long> idList);

	/**
	 * 查询符合条件的图片列表。
	 * 
	 * @return
	 */
	List<AlbumImg> getAlbumImgListByCond(long userId, long dirId, long queryStartTime, long queryEndTime,
			String imgName, int curPage, int pageSize);

	/**
	 * 查询用户所有目录下的所有图片列表。
	 * 
	 * @param userId
	 * @return 用户所有目录下的图片列表。
	 */
	List<AlbumImg> getAlbumImgListByUserId(long userId);

	/**
	 * 删除所有图片
	 * 
	 * @return 要么返回true，要么抛出异常。
	 */
	boolean deleteAllImg();
}
