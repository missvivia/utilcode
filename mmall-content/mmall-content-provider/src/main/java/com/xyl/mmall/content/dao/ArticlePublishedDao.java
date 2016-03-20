/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.meta.ArticlePublished;

/**
 * @author lihui
 *
 */
public interface ArticlePublishedDao extends AbstractDao<ArticlePublished> {

	/**
	 * 
	 * @param publishTypeList
	 * @param keywordList
	 * @param categoryId
	 * @param parentCategoryId
	 * @param param
	 * @return
	 */
	List<ArticlePublished> findByPublishTypeAndKeywordListAndCategoryId(List<Integer> publishTypeList,
			List<String> keywordList, long categoryId, long parentCategoryId, DDBParam param);

	/**
	 * 
	 * @param publishTypeList
	 * @param keywordList
	 * @param categoryId
	 * @param parentCategoryId
	 * @return
	 */
	int countByPublishTypeAndKeywordListAndCategoryId(List<Integer> publishTypeList, List<String> keywordList,
			long categoryId, long parentCategoryId);

	/**
	 * @param id
	 * @return
	 */
	ArticlePublished findByOrgArticleId(long id);
	
	/**
	 * 
	 * @param idList
	 * @param param
	 * @return
	 */
	List<ArticlePublished> findByIdList(List<Long> idList, DDBParam param);
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	List<Long> findAllIdSet(DDBParam param);
}
