/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.dao.category;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.meta.CategoryNormal;

/**
 * CategoryNormalDao.java created by yydx811 at 2015年4月27日 下午4:33:13
 * 商品分类dao
 *
 * @author yydx811
 */
public interface CategoryNormalDao extends AbstractDao<CategoryNormal> {
	
	/**
	 * 分页获取全部层级分类列表
	 * @param basePageParamVO
	 * @return List<CategoryNormalDTO>
	 */
	public List<CategoryNormalDTO> getCategoryNormalList(BasePageParamVO<?> basePageParamVO);
	
	/**
	 * 获取全部层级分类列表
	 * @return List<CategoryNormalDTO>
	 */
	public List<CategoryNormalDTO> getCategoryNormalList();
	
	/**
	 * 按条件获取分类数量
	 * @param categoryNormal
	 * @return List<CategoryNormal>
	 */
	public int getCategoryNormalCount(CategoryNormal categoryNormal);
	
	/**
	 * 分页获取一级分类列表
	 * @param basePageParamVO
	 * @return List<CategoryNormal>
	 */
	public List<CategoryNormal> getFirstCategoryNormalList(BasePageParamVO<?> basePageParamVO);
	
	/**
	 * 获取一级分类列表
	 * @return List<CategoryNormal>
	 */
	public List<CategoryNormal> getFirstCategoryNormalList();
	
	/**
	 * 按父id获取分类
	 * @param superCategoryId
	 * @return List<CategoryNormal>
	 */
	public List<CategoryNormal> getSubCategoryNormalList(long superCategoryId);
	
	/**
	 * 按id获取分类
	 * @param id
	 * @return CategoryNormal
	 */
	public CategoryNormal getCategoryNormalById(long id);

	/**
	 * 创建商品分类
	 * @param categoryNormal
	 * @return int
	 */
	public int createCategoryNormal(CategoryNormal categoryNormal);
	
	/**
	 * 根据条件查询
	 * @param categoryNormal
	 * @return CategoryNormal
	 */
	public CategoryNormal getCategoryNormal(CategoryNormal categoryNormal);

	/**
	 * 更新商品分类
	 * @param categoryNormal
	 * @return int
	 */
	public int updateCategoryNormal(CategoryNormal categoryNormal);

	/**
	 * 获取最大ShowIndex
	 * @param categoryNormal
	 * @return
	 */
	public int getMaxShowIndex(CategoryNormal categoryNormal);
	
	/**
	 * 更新商品分类排序
	 * @param categoryNormal
	 * @return int
	 */
	public int updateCategoryNormalSort(CategoryNormal categoryNormal, int isUp);

	/**
	 * 删除商品分类
	 * @param id
	 * @return int
	 */
	public int deleteCategoryNormal(long id);
	
	/**
	 * 根据ids获取商品分类
	 * @param ids
	 * @return
	 */
	public List<CategoryNormal> getCategoryListByIds(List<Long> ids);
	
	/**
	 * 取得全部的商品分类
	 * @return
	 */
	public List<CategoryNormal> getALLCategoryNormalList();
	
	/**
	 * 按名字完全匹配获取商品分类
	 * @param categoryNormalName
	 * @param level
	 * @param superId
	 * @return
	 */
	public List<CategoryNormal> getCategoryNormalByName(String categoryNormalName, int level, long superId);
}
