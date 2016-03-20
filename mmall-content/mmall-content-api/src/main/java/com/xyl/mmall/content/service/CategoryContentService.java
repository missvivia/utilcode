package com.xyl.mmall.content.service;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.dto.CategoryContentDTO;


/**
 * 内容类目服务接口
 * 
 * @author lihongpeng
 *
 */
public interface CategoryContentService {
	
	/**
	 * 分页获取全部类目
	 * 
	 * @return
	 */
	public List<CategoryContentDTO> getPageCategoryContentList(DDBParam ddbParam);
	

	/**
	 * 根据父类目id获取子类目列表
	 * 
	 * @param superCategoryContentId
	 *            父类目id
	 * @return List<CategoryContent>
	 */
	public List<CategoryContentDTO> getSubCategoryContentList(long superCategoryContentId);
	


	/**
	 * 根据类目id获取类目对象
	 * 
	 * @param cid
	 * @return
	 */
	public CategoryContentDTO getCategoryContentById(long cid);

	
	/**
	 * 新增内容分类
	 * @param categoryContentDTO
	 */
	public boolean saveCategoryContent(CategoryContentDTO categoryContentDTO);
	
	/**
	 * 跟新内容分类
	 * @param categoryContentDTO
	 */
	public boolean updateCategoryContent(CategoryContentDTO categoryContentDTO);
	
	/**
	 * 根据Id删除内容分类
	 * @param categoryId
	 */
	public boolean deleteCategoryContent(long categoryId);
	
	
	/**
	 * 获取已绑定商品分类的三级内容分类
	 * @return
	 */
	public List<CategoryContentDTO> queryThirdCategoryContentListBindCategoryNormal();
	
	
	/**
	 * 根据rootId返回所有内容类目Map
	 */
	public Map<Long, CategoryContentDTO> getCategoryContentDTOMapByRootId(long rootId);
	
	
	/**
	 * 根据rootId返回所有内容类目Map  without cache
	 */	
	public Map<Long, CategoryContentDTO> getNoCacheCategoryContentDTOMapByRootId(long rootId);
	
	/**
	 * 删除分类树
	 * @param rootId 即level=0时的ID
	 * @return
	 */
	public boolean deleteCategoryContentTree(long rootId);
	
	/**
	 * 根据rootId和level取内容类目
	 * @param level
	 * @param rootId
	 * @return
	 */
	public List<CategoryContentDTO> getCategoryContentListByLevelAndRootId(int level,long rootId);
	
	/**
	 * 根据rootId取内容类目树
	 * @param rootId
	 * @return
	 */
	public List<CategoryContentDTO> getCategoryContentListByRootId(long rootId);
	
	
	public List<CategoryContentDTO> getCategoryContentListByRootId(long rootId,Boolean hasAllData,Long categoryId) throws Exception;
	
}
