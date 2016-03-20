package com.xyl.mmall.content.dao;


import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.content.dto.SearchCategoryContentDTO;
import com.xyl.mmall.content.meta.CategoryContent;

/**
 * 类目dao
 * 
 * @author lihongpeng
 *
 */
public interface CategoryContentDao extends AbstractDao<CategoryContent> {
	
	/**
	 * 获取全部类目
	 * 
	 * @return
	 */
	public List<CategoryContent> getCategoryContentList();

	/**
	 * 根据父类目id获取子类目列表
	 * 
	 * @param superId
	 *            父类目id
	 * @return
	 */
	public List<CategoryContent> getCategoryContentListBySuperId(long superId);

	
	/**
	 * 根据rootId和level取内容类目
	 * @param level
	 * @param rootId
	 * @return
	 */
	public List<CategoryContent> getCategoryContentListByLevelAndRootId(int level,long rootId);
	
	/**
	 * 根据rootId取内容类目
	 * @param rootId
	 * @return
	 */
	public List<CategoryContent> getCategoryContentListByRootId(long rootId);

	
	/**
	 * 根据搜索条件查询内容分类
	 * @param searchDto
	 * @return
	 */
	public List<CategoryContent> searchCategoryContentList(SearchCategoryContentDTO searchDto);
	
	/**
	 * 获取最新新增的内容分类 
	 * 
	 * @return
	 */
	public CategoryContent getLastestCategoryContent();
	
	/**
	 * 根据ids获取内容分类列表
	 * @param ids
	 * @return
	 */
	public List<CategoryContent> getCategoryContentListByIds(List<Long>ids);
	
	/**
	 * 根据父类目ids获取子类目列表
	 * 
	 * @param superIds
	 *            父类目ids
	 * @return
	 */
	public List<CategoryContent> getCategoryContentListBySuperIds(List<Long> superIds);

	/**
	 * 获取已绑定商品分类的三级内容分类
	 * @return
	 */
	public List<CategoryContent> queryThirdCategoryContentListBindCategoryNormal();
	
	/**
	 * 删除类目树
	 * @param rootId
	 * @return
	 */
	public boolean deleteCategoryContentByRootId(long rootId);
	
	/**
	 * 更新类目树
	 * @param rootId
	 * @return
	 */
	public boolean updateCategoryContent(CategoryContent categoryContent);
}
