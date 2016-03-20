package com.xyl.mmall.itemcenter.dao.category;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.Category;

/**
 * 类目dao
 * 
 * @author hzhuangluqian
 *
 */
public interface CategoryDao extends AbstractDao<Category> {
	/**
	 * 获取全部类目
	 * 
	 * @return
	 */
	public List<Category> getCategoryList();

	/**
	 * 根据父类目id获取子类目列表
	 * 
	 * @param superId
	 *            父类目id
	 * @return
	 */
	public List<Category> getCategoryListBySuperId(long superId);

	/**
	 * 根据主键id获取类目对象
	 * 
	 * @param id
	 * @return
	 */
	public Category getCategoryById(long id);

	/**
	 * 获取某id下的所有最底层的类目列表
	 * 
	 * @param id
	 * @return
	 */
	public List<Category> getLowestCategoryById(List<Category> retList, long id);

	/**
	 * 根据最低一级的类目获取 顶级到最低级的类目列表
	 * 
	 * @param retList
	 * @param lowCategoryId
	 */
	public void getCategoryListByLowestId(List<Category> retList, long lowCategoryId);

	public Category getFirstCategoryByLowestId(long lowerId);

	public List<Category> getCategoryListByLevel(int level);

	public List<Category> getCategoryList(List<Long> ids);
}
