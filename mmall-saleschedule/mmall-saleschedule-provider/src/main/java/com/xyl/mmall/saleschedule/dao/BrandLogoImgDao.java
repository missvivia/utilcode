package com.xyl.mmall.saleschedule.dao;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.enums.BrandImgSize;
import com.xyl.mmall.saleschedule.meta.BrandLogoImg;

/**
 * 品牌图片Dao
 * @author chengximing
 *
 */
public interface BrandLogoImgDao extends AbstractDao<BrandLogoImg> {
	/**
	 * 根据BrandId返回brand对应的logo
	 * @param brandId
	 * @param brandImgSize
	 * @return
	 */
	public BrandLogoImg getBrandLogoByBrandId(long brandId, BrandImgSize brandImgSize);
	/**
	 * 根据brandId返回图像的logo的list
	 * @param brandId
	 * @return
	 */
	public List<BrandLogoImg> getBrandLogoListByBrandId(long brandId);
	/**
	 * 根据brandId的列表返回对应的logo的数据
	 * @param brandIdList
	 * @param brandImgSize
	 * @return
	 */
	public Map<Long, BrandLogoImg> getBrandLogoListByBrandIdList(List<Long> brandIdList, BrandImgSize brandImgSize);
	/**
	 * 根据图片尺寸设置图片url
	 * @param brandId
	 * @param brandImgSize
	 * @param url
	 * @return
	 */
	public boolean setBrandLogoImg(long brandId, BrandImgSize brandImgSize, String url);
	
	public boolean delBrandLogoListByBrandId(long brandId);
	/**
	 * 添加新的品牌logo
	 * @param brandId
	 * @param logo
	 * @param visualWeb
	 * @param visualApp
	 */
	void addNewBrandLogo(long brandId, String logo, String visualWeb, String visualApp);
	/**
	 * 设置新的品牌logo
	 * @param brandId
	 * @param logo
	 * @param visualWeb
	 * @param visualApp
	 */
	void setNewBrandLogo(long brandId, String logo, String visualWeb, String visualApp);
}
