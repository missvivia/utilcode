/**
 * 
 */
package com.xyl.mmall.content.service;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.dto.ArticleDTO;
import com.xyl.mmall.content.dto.ArticlePublishedDTO;
import com.xyl.mmall.content.dto.NCSContentDispatchLogDTO;
import com.xyl.mmall.content.enums.NCSIndexDispatchState;
import com.xyl.mmall.content.meta.NCSContentDispatchLog;

/**
 * 帮助中心文章相关服务。
 * 
 * @author lihui
 *
 */
public interface HelpArticleService {

	/**
	 * 根据帮助的类目查找已发布文章列表。
	 * 
	 * @param publishType
	 *            发布类别
	 * @param categoryId
	 *            类目ID
	 * @return 文章列表
	 */
	List<ArticlePublishedDTO> findPublishedArticleByPublishTypeAndCategory(int publishType, long categoryId);

	/**
	 * 根据关键字列表查询文章。
	 * 
	 * @param publishType
	 *            发布类别
	 * @param keywordList
	 *            关键字列表
	 * @param offset
	 *            分页位置
	 * @param limit
	 *            分页大小
	 * @return 文章列表
	 */
	List<ArticlePublishedDTO> findPublishedArticleByPublishTypeAndKeywordList(int publishType, List<String> keywordList,
			int limit, int offset);

	/**
	 * 根据关键字列表查询文章数目。
	 * 
	 * @param publishType
	 *            发布类别
	 * @param keywordList
	 *            关键字列表
	 * @return 文章数目
	 */
	int countPublishedArticleByPublishTypeAndKeywordList(int publishType, List<String> keywordList);

	/**
	 * 根据帮助的类目和关键字查找文章列表的总数。
	 * 
	 * @param publishType
	 *            发布类别
	 * @param categoryId
	 *            类目ID
	 * @param keywordList
	 *            关键字
	 * @return 文章列表的总数。
	 */
	int countArticleByPublishTypeAndCategoryAndKeywords(int publishType, long categoryId, List<String> keywordList);

	/**
	 * 根据帮助的类目和关键字查找文章列表。
	 * 
	 * @param publishType
	 *            发布类别
	 * @param categoryId
	 *            文章类别
	 * @param keywordList
	 *            关键字
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return
	 */
	List<ArticleDTO> findArticleByPublishTypeAndCategoryAndKeywords(int publishType, long categoryId,
			List<String> keywordList, int limit, int offset);

	/**
	 * 根据ID获取文章的详情
	 * 
	 * @param id
	 *            文章ID
	 * @return
	 */
	ArticleDTO getArticleDetail(long id);

	/**
	 * 保存文章
	 * 
	 * @param articleDTO
	 *            文章内容
	 * @return
	 */
	ArticleDTO saveHelpArticle(ArticleDTO articleDTO);

	/**
	 * 保存并发布文章
	 * 
	 * @param articleDTO
	 *            文章内容
	 * @return
	 */
	ArticleDTO saveAndPublishHelpArticle(ArticleDTO articleDTO);

	/**
	 * 发布文章
	 * 
	 * @param id
	 *            文章ID
	 * @param userId
	 *            用户ID
	 * @return
	 */
	ArticleDTO publishHelpArticle(long id, long userId);

	/**
	 * 撤销文章
	 * 
	 * @param id
	 *            文章ID
	 * @param userId
	 *            用户ID
	 * @return
	 */
	ArticleDTO revokeHelpArticle(long id, long userId);

	/**
	 * 删除文章
	 * 
	 * @param id
	 *            文章ID
	 * @param userId
	 *            用户ID
	 */
	void deleteHelpArticle(long id, long userId);
	
	/**
	 * 查询NCSContentDispatchLog (使用场景：定时器ContentNCSTimerFacade)
	 * 
	 * @param minId
	 * @param states
	 * @return
	 *     RetArg.List<NCSContentDispatchLogDTO>
	 *     RetArg.DDBParam
	 */
	public RetArg findNCSContentDispatchLogByDispatchStatesWithMinId(long minId, NCSIndexDispatchState[] states, DDBParam param);
	
	/**
	 * 根据ID获取文章
	 * 
	 * @param id
	 *            文章ID
	 * @return
	 */
	ArticleDTO getArticleById(long id);
	
	/**
	 * 根据ID获取文章
	 * 
	 * @param id
	 *            文章ID
	 * @return
	 */
	List<ArticleDTO> getArticleByIdList(List<Long> idList, DDBParam param);
	
	/**
	 * 根据ID获取已发布文章
	 * 
	 * @param id
	 *            文章ID
	 * @return
	 */
	ArticlePublishedDTO getArticlePublishedById(long id);
	
	/**
	 * 根据ID获取已发布文章
	 * 
	 * @param id
	 *            文章ID
	 * @return
	 */
	List<ArticlePublishedDTO> getArticlePublishedByIdList(List<Long> idList, DDBParam param);

	/**
	 * 添加Article
	 * 
	 * @param article
	 * @param dispatchState
	 * @return
	 */
	public NCSContentDispatchLogDTO addArticelToNCSIndexDispatchLog(ArticleDTO article, NCSIndexDispatchState dispatchState);
	
	/**
	 * 添加ArticlePublished
	 * 
	 * @param article
	 * @param dispatchState
	 * @return
	 */
	public NCSContentDispatchLogDTO addArticelPublishedToNCSIndexDispatchLog(ArticlePublishedDTO articlePublished, NCSIndexDispatchState dispatchState);
	
	/**
	 * 更新NCSContentDispatchLog
	 * 
	 * @param log
	 * @param newState
	 * @param oldStates
	 * @return
	 */
	public boolean updateDispatchLog(NCSContentDispatchLog log, NCSIndexDispatchState newState, NCSIndexDispatchState[] oldStates);
	
	/**
	 * 同步已有数据（临时使用，上线后删除）
	 */
	public void syncNCSDispatchLog();
}
