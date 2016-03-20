package com.xyl.mmall.content.service;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.content.dto.ArticleDTO;
import com.xyl.mmall.content.dto.ArticlePublishedDTO;
import com.xyl.mmall.content.enums.ContentType;
import com.xyl.mmall.content.enums.NCSIndexDispatchState;
import com.xyl.mmall.content.meta.Article;
import com.xyl.mmall.content.meta.NCSContentDispatchLog;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public interface HelpContentNCSService {
	
	/**
	 * 工厂方法
	 * 
	 * @param contentType
	 * @param foreignPrimaryId
	 * @return
	 */
	public NCSContentDispatchLog createNCSContentDispatchLog(
			ContentType contentType, 
			long foreignPrimaryId, 
			NCSIndexDispatchState dispatchState
			) ;
	
//	/**
//	 * 工厂方法
//	 * 
//	 * @param article
//	 * @return
//	 */
//	public IndexDocRequest createArticleNCSDoc(ArticleDTO article) ;
//	
//	/**
//	 * 工厂方法
//	 * 
//	 * @param aticlePublished
//	 * @return
//	 */
//	public IndexDocRequest createArticlePublishedNCSDoc(ArticlePublishedDTO aticlePublished) ;
//	
//	/**
//	 * 构造Article的NDirQuery
//	 * 
//	 * @param query
//	 * @param publishType
//	 * @param categoryId
//	 * @return
//	 */
//	public NDirQuery createArticleQuery(String query, int publishType, long categoryId) ;
//	
//	/**
//	 * 构造ArticlePublished的NDirQuery
//	 * 
//	 * @param query
//	 * @return
//	 */
//	public NDirQuery createArticlePublishedQuery(String query, int publishType) ;
//	
//	/**
//	 * 获取NDirIndexClient
//	 * 
//	 * @return
//	 */
//	public NDirIndexClient getNCSIndexClient() ;
//	
//	/**
//	 * 获取NDirSearchClient2
//	 * 
//	 * @return
//	 */
//	public NDirSearchClient2 getNCSSearchClient() ;
	
	/**
	 * 索引Article
	 * 
	 * @param article
	 * @return
	 *     RetArg.Boolean (Boolean.True for Successful, Boolean.False for Failed)
	 *     RetArg.String (Message)
	 */
	public RetArg indexArticle(ArticleDTO article) ;
	
	/**
	 * 索引ArticlePublished
	 * 
	 * @param articlePublished
	 * @return
	 *     RetArg.Boolean (Boolean.True for Successful, Boolean.False for Failed)
	 *     RetArg.String (Message)
	 */
	public RetArg indexArticlePublished(ArticlePublishedDTO articlePublished) ;
	
	/**
	 * 删除Article
	 * 
	 * @param article
	 * @return
	 *     RetArg.Boolean (Boolean.True for Successful, Boolean.False for Failed)
	 *     RetArg.String (Message)
	 */
	public RetArg deleteArticle(ArticleDTO article) ;
	
	/**
	 * 删除ArticlePublished
	 * 
	 * @param articlePublished
	 * @return
	 *     RetArg.Boolean (Boolean.True for Successful, Boolean.False for Failed)
	 *     RetArg.String (Message)
	 */
	public RetArg deleteArticlePublished(ArticlePublishedDTO articlePublished) ;
	
	/**
	 * 检索Article
	 * 
	 * @param publishType
	 * @param categoryId
	 * @param query
	 * @param offset
	 * @param length
	 * @return
	 */
	public List<Long> searchArticle(int publishType, long categoryId, 
			String query, int offset, int length) ;
	
	/**
	 * 检索ArticlePublished
	 * 
	 * @param query
	 * @param offset
	 * @param length
	 * @return
	 */
	public List<Long> searchArticlePublished(int publishType, String query, int offset, int length) ;
	
//	/**
//	 * 从SearchResultView中提取Id
//	 * 
//	 * @param view
//	 * @param idFieldName
//	 * @return
//	 */
//	public List<Long> extractIdListFromSearchResultView(SearchResultView view, String idFieldName) ;
	
	/**
	 * 按照ncs检索到的Id排序
	 * @param sortedIdList
	 * @param articleList
	 * @return
	 */
	public <A extends Article>
	List<A> sortArticle(List<Long> sortedIdList, List<A> articleList) ;
}
