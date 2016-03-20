package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.cms.vo.CategoryContentVO;
import com.xyl.mmall.cms.vo.CategoryNormalVO;
import com.xyl.mmall.content.dto.CategoryContentDTO;
import com.xyl.mmall.content.dto.SearchCategoryContentDTO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;
import com.xyl.mmall.itemcenter.meta.CategoryNormal;

/**
 * CategoryFacade.java created by yydx811 at 2015年4月27日 上午10:22:48
 * 分类facade
 *
 * @author yydx811
 */
public interface CategoryFacade {

	/**
	 * 获取商品分类列表
	 * @param basePageParamVO 分页参数
	 * @return List<CategoryNormalVO>
	 */
	public List<CategoryNormalVO> getCategoryNormalList(BasePageParamVO<CategoryNormalVO> basePageParamVO);
	
	/**
	 * 获取第一级商品分类
	 * @param basePageParamVO 分页参数
	 * @return List<CategoryNormalVO>
	 */
	public List<CategoryNormalVO> getFirstCategoryNormalList(BasePageParamVO<CategoryNormalVO> basePageParamVO);
	
	/**
	 * 根据父id获取子商品分类
	 * @param superCategoryId
	 * @return List<CategoryNormalVO>
	 */
	public List<CategoryNormalVO> getSubCategoryNormalList(long superCategoryId);
	
	/**
	 * 根据id获取商品分类
	 * @param id 分类id
	 * @param isContainSub 是否包含子分类
	 * @return CategoryNormalVO
	 */
	public CategoryNormalVO getCategoryNormalById(long id, boolean isContainSub);
	
	/**
	 * 创建商品分类
	 * @param categoryNormalDTO
	 * @return int
	 */
	public int createCategoryNormal(CategoryNormalDTO categoryNormalDTO);
	
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
	 * 新增内容类目
	 * @param dto
	 */
	public int saveCategoryContent(CategoryContentDTO dto);
	
	/**
	 * 更新内容类目
	 * @param dto
	 */
	public int updateCategoryContent(CategoryContentDTO dto);
	
	/**
	 * 删除内容类目
	 * @param dto
	 */
	public boolean deleteCategoryContent(long categoryId);
	
	/**
	 * 根据父id获取内容子分类
	 * @param superCategoryId
	 * @return List<CategoryNormalVO>
	 */
	public List<CategoryContentVO> getSubCategoryContentList(long superCategoryId);
	
	
	/**
	 * 根据search条件获取内容分类
	 * @param searchDto
	 * @return
	 */
	public BasePageParamVO<CategoryContentDTO> searchCategoryContentList(SearchCategoryContentDTO searchDto);
	
	/**
	 * 根据id获取内容分类
	 * @param id 分类id
	 * @return CategoryContentVO
	 */
	public CategoryContentVO getCategoryContentById(long id);
	
	/**
	 * 获取分类全名
	 * @param categoryId
	 * @return
	 */
	public String getCategoryFullName(long categoryId);
	
	/**
	 * 是否绑定商品分类
	 * @param categoryId
	 * @return
	 */
	public String isContainNormalCategory(long categoryId);
	
	/**
	 * 按名字完全匹配获取商品分类
	 * @param categoryNormalName
	 * @param level
	 * @param superId
	 * @return
	 */
	public List<CategoryNormal> getCategoryNormalByName(String categoryNormalName, int level, long superId);
	
	/**
	 * 根据areaId获取所有类目列表
	 * @param areaId
	 * @return
	 */
	public List<CategoryContentDTO> getCategoryContentListByAreaId(long areaId);
	
	/**
	 * 
	 * @param areaId
	 * @return
	 */
	public List<CategoryContentDTO> getCategoryContentListByAreaId(long areaId,Boolean hasAllDate,Long categoryId)throws Exception;
	
	/**
	 * 删除内容分类树
	 * @param rootId 即level=0时的ID
	 * @return
	 */
	public boolean deleteCategoryContentTree(long rootId);
	
	
	/**
	 * 根据rootId或level取内容类目，从db取
	 * @param level -1时不选
	 * @param rootId -1时不选
	 * @return
	 */
	public List<CategoryContentDTO> getCategoryContentListByLevelAndRootId(int level,long rootId);
	
	/**
	 * 根据rootId取内容类目树   走缓存，已经构建树结构
	 * @param rootId
	 * @return
	 */
	public List<CategoryContentDTO> getCategoryContentListByRootId(long rootId);
	
	/**
	 * 获取简略内容分类信息
	 * @param categoryContentId
	 * @return
	 */
	public CategoryContentVO getBriefCategoryContentById(long categoryContentId);
}
