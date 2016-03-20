package com.xyl.mmall.saleschedule.dao;

import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.saleschedule.meta.BrandUserFav;

/**
 * 品牌收藏的数据库操作接口
 * @author chengximing
 *
 */
public interface BrandUserFavDao extends AbstractDao<BrandUserFav> {
	/**
	 * 判断用户是否收藏了对应的品牌
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean isBrandInFavList(long userId, long brandId);
	/**
	 * 添加品牌收藏
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean addBrandIntoFavList(long userId, long brandId);
	/**
	 * 删除品牌收藏
	 * @param userId
	 * @param brandId
	 * @return
	 */
	public boolean removeBrandFromFavList(long userId, long brandId);
	/**
	 * 计算品牌被多少人收藏
	 * @param brandId
	 * @return
	 */
	public int getBrandFavCount(long brandId);
	/**
	 * 通过userId返回对应的BrandUserFav的列表
	 * @param param
	 * @param userId
	 * @param time
	 * @return
	 */
	public List<BrandUserFav> getBrandUserFavListByUserId(DDBParam param, long userId, long time);
	/**
	 * 通过brandId的列表得到用户关注的状态
	 * @param brandIdList
	 * @param userId
	 * @return
	 */
	public Map<Long, Boolean> getBrandFavStateByIds(List<Long> brandIdList, long userId);
	/**
	 * 通过brandId的列表得到用户关注的数量
	 * @param brandIdList
	 * @return
	 */
	public Map<Long, Integer> getBrandFavCountByIds(List<Long> brandIdList);
	
	/***
	 * 用户在某个区域
	 * @param userId
	 * @param areaId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public boolean hasActiveForBrandLike(long userId, long areaId, long startTime, long endTime);
	/**
	 * 返回用户品牌对应收藏的用户的相关信息，用于相关的提醒
	 * @param brandIdList
	 * @param timeAfter
	 * @param offset
	 * @return
	 */
	public RetArg/*List<UserFavListDTO>*/ getUserFavListByBrandIdList(List<Long> brandIdList, 
			long timeAfter, int limit, int offset);
	/**
	 * 输入brandId列表 返回用户实际上收藏的brandId列表
	 * @param userId
	 * @param brandIdListString
	 * @return
	 */
	public List<Long> getBrandFavListByUser(long userId, String brandIdListString);

}
