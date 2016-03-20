package com.xyl.mmall.itemcenter.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.CategoryNormal;

/**
 * 类目服务接口
 * 
 * @author hzhuangluqian
 *
 */
public interface CategoryService {
	/**
	 * 获取全部类目
	 * 
	 * @return
	 */
	public List<Category> getCategoryList();

	/**
	 * 根据父类目id获取子类目列表
	 * 
	 * @param superCategoryId
	 *            父类目id
	 * @return List<Category>
	 */
	public List<Category> getSubCategoryList(long superCategoryId);

	/**
	 * 获取 从最顶层level类目到当前level类目的列表
	 * 
	 * @param lowCategoryId
	 * @return
	 */
	public List<Category> getCategoryListBylowId(long lowCategoryId);

	/**
	 * 根据类目id获取类目对象
	 * 
	 * @param cid
	 * @return
	 */
	public Category getCategoryById(long cid);

	/**
	 * 根据类目级别获取某个级别下的所有类目列表
	 * 
	 * @param level
	 *            类目级别
	 * @return
	 */
	public List<Category> getCategoryListByLevel(int level);

	/**
	 * 根据最低级类目id获取最高级类目对象
	 * 
	 * @param lowerId
	 *            最低一级类目id
	 * @return
	 */
	public Category getFirstCategoryByLowestId(long lowerId);

	/**
	 * 获取某个类目下的递归子类目列表集合
	 * 
	 * @param id
	 *            类目id
	 * @return
	 */
	public List<Category> getLowestCategoryById(long id);

	/**
	 * 获取商品分类列表
	 * @param basePageParamVO 分页参数
	 * @return List<CategoryNormalVO>
	 */
	public List<CategoryNormalDTO> getCategoryNormalList(BasePageParamVO<?> basePageParamVO);
	
	/**
	 * 获取第一级商品分类
	 * @param basePageParamVO 分页参数
	 * @return List<CategoryNormalVO>
	 */
	public List<CategoryNormalDTO> getFirstCategoryNormalList(BasePageParamVO<?> basePageParamVO);
	
	/**
	 * 根据父id获取子分类
	 * @param superCategoryId
	 * @return List<CategoryNormalVO>
	 */
	public List<CategoryNormalDTO> getSubCategoryNormalList(long superCategoryId);
	
	/**
	 * 根据id获取分类
	 * @param id 分类id
	 * @param isContainSub 是否包含子分类
	 * @return CategoryNormalVO
	 */
	public CategoryNormalDTO getCategoryNormalById(long id, boolean isContainSub);

	/**
	 * 创建商品分类
	 * @param categoryNormalDTO
	 * @return int
	 */
	public int createCategoryNormal(CategoryNormalDTO categoryNormalDTO);
	
	/**
	 * 根据条件查询
	 * @param categoryNormalDTO
	 * @return CategoryNormalDTO
	 */
	public CategoryNormalDTO getCategoryNormal(CategoryNormalDTO categoryNormalDTO);

	/**
	 * 更新商品分类
	 * @param categoryNormalDTO
	 * @return int
	 */
	public int updateCategoryNormal(CategoryNormalDTO categoryNormalDTO);
	
	/**
	 * 获取最大ShowIndex
	 * @param categoryNormal
	 * @return int
	 */
	public int getMaxShowIndex(CategoryNormalDTO categoryNormalDTO);
	
	/**
	 * 更新商品分类排序
	 * @param categoryNormalDTO
	 * @return int
	 */
	public int updateCategoryNormalSort(CategoryNormalDTO categoryNormalDTO, int isUp);
	
	/**
	 * 删除商品分类
	 * @param id
	 * @return int
	 */
	public int deleteCategoryNormal(long id);
	
	/**
	 * 获取类目根据ids
	 * 
	 * @return
	 */
	public List<CategoryNormalDTO> getCategoryListByIds(List<Long>ids);

	/**
	 * 按条件获取分类数量
	 * @param categoryNormalDTO
	 * @return List<CategoryNormal>
	 */
	public int getCategoryNormalCount(CategoryNormalDTO categoryNormalDTO);
	
	/**
	 * 获取商品分类全称
	 * @param categoryNormalId
	 * @return
	 */
	public String getFullCategoryNormalName(long categoryNormalId);
	
	/**
	 * 从缓存获取商品分类Map
	 * @return
	 */
	public Map<Long, CategoryNormalDTO> getCategoryNormalMapFromCache();
	
	/**
	 * 按名字完全匹配获取商品分类
	 * @param categoryNormalName
	 * @param level
	 * @param superId
	 * @return
	 */
	public List<CategoryNormal> getCategoryNormalByName(String categoryNormalName, int level, long superId);
}
