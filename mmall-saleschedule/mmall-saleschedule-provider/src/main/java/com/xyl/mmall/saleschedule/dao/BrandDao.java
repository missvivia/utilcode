package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.saleschedule.meta.Brand;

/**
 * 品牌Dao
 * @author chengximing
 *
 */
public interface BrandDao extends AbstractDao<Brand> {
	
	String getBrandName(long brandId);
	
	List<Brand> getAllBrand(DDBParam param, long time);
	
	List<Brand> searchBrand(String name, DDBParam param);
	
	boolean isBrandExist(String brandNameZh, String brandNameEn);
	/**
	 * 通过brandId列表返回对应的brand的列表
	 * @param ids
	 * @return
	 */
	List<Brand> getBrandListByBrandIdList(List<Long> ids);
	/**
	 * 通过品牌英文或者拼音的首字母品牌进行分组
	 * @param param
	 * @param time
	 * @param index
	 * @param areaId
	 * @return
	 */
	List<Brand> getBrandListByIndex(DDBParam param, long time, String index, long areaId);
	/**
	 * cms品牌过滤
	 * @param param
	 * @param index
	 * @return
	 */
	List<Brand> getBrandListByIndexCMS(DDBParam param, String index);
	/**
	 * 获得在同一个区域下面的品牌推荐列表
	 * @param areaId
	 * @param count
	 * @return
	 */
	List<Brand> getRecommendBrands(long areaId, int count);
	
	List<Brand> getBrandByName(String name);
	/**
	 * 重新跟新头字母
	 * @param headEn
	 * @param headZh
	 * @param brandId
	 */
	void updateBrandName(String headEn, String headZh, long brandId);
	/**
	 * 更新档期里面的品牌名称的信息
	 * @param brandId
	 * @param brandNameZh
	 * @param brandNameEn
	 */
	void updateBrandNameforSale(long brandId, String brandNameZh, String brandNameEn);
	
	/**
	 * 获取品牌
	 * @param ids
	 * @return List<JSONObject>
	 */
	public List<JSONObject> getBrandListInOrderByIds(List<Long> ids);
	
	List<JSONObject> getBrandListInOrderByCategory(long areaId, List<Long> brandValue);
	
	/**
	 * 按品牌id顺序获取品牌简略信息
	 * @param brandIds
	 * @return Brand[] 不存在的id可能会产生空位
	 */
	public Brand[] getBrandBreifInfoListInListOrder(List<Long> brandIds);
}