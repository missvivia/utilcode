package com.xyl.mmall.content.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.netease.ndir.client.NDirIndexClient;
import com.netease.ndir.client.NDirSearchClient2;
import com.netease.ndir.client.exception.NDirClientException;
import com.netease.ndir.common.exception.NDirException;
import com.netease.ndir.common.index.IndexDocRequest;
import com.netease.ndir.common.search.NDirScoreDocView;
import com.netease.ndir.common.search.SearchResultView;
import com.netease.ndir.common.syntax.ClauseQuerySyntax;
import com.netease.ndir.common.syntax.NDirQuery;
import com.netease.ndir.common.syntax.NumberQuerySyntax;
import com.netease.ndir.common.syntax.PhraseQuerySyntax;
import com.netease.ndir.common.syntax.QueryOccur;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.content.dto.ArticleDTO;
import com.xyl.mmall.content.dto.ArticlePublishedDTO;
import com.xyl.mmall.content.enums.ArticleNCSIndexField;
import com.xyl.mmall.content.enums.ArticlePublishType;
import com.xyl.mmall.content.enums.ArticlePublishedNCSIndexField;
import com.xyl.mmall.content.enums.ContentType;
import com.xyl.mmall.content.enums.NCSIndexDispatchState;
import com.xyl.mmall.content.meta.Article;
import com.xyl.mmall.content.meta.NCSContentDispatchLog;
import com.xyl.mmall.content.service.HelpContentNCSService;

@Service("contentNCSService")
public class HelpContentNCSServiceImpl implements HelpContentNCSService {
	
	private static final Logger logger = Logger.getLogger(HelpContentNCSServiceImpl.class);
	
	@Value("${ncs.server.sd}")
	private String ncsSDServer;
	
	@Value("${ncs.server.id}")
	private String ncsIDServer;
	
	@Value("${ncs.index.article}")
	private String indexNameOfArticle;
	
	@Value("${ncs.index.article_published}")
	private String indexNameOfArticlePublished;
	
	@Value("${ncs.field.boost.keywords}")
	private float boostOfKeywords;
	
	@Value("${ncs.field.boost.title}")
	private float boostOfTitle;
	
	@Value("${ncs.field.boost.content}")
	private float boostOfContent;

	private NDirIndexClient ncsIndexClient;
	
	private NDirSearchClient2 ncsSearchClient;

	/**
	 * 工厂方法
	 * 
	 * @param contentType
	 * @param foreignPrimaryId
	 * @return
	 */
	@Override
	public NCSContentDispatchLog createNCSContentDispatchLog(
			ContentType contentType, 
			long foreignPrimaryId, 
			NCSIndexDispatchState dispatchState
			) {
		NCSContentDispatchLog log = new NCSContentDispatchLog();
		log.setContentType(contentType);
		log.setDispatchState(dispatchState);
		log.setForeignPrimaryId(foreignPrimaryId);
		long currTime = System.currentTimeMillis();
		log.setCreateTime(currTime);
		log.setUpdateTime(currTime);
		return log;
	}
	
	/**
	 * 工厂方法
	 * 
	 * @param article
	 * @return
	 */
	private IndexDocRequest createArticleNCSDoc(ArticleDTO article) {
		IndexDocRequest doc = new IndexDocRequest();
		if(null != article) {
			ArticleNCSIndexField field = null;
			field = ArticleNCSIndexField.ID;
			doc.setDataValue(field.getFieldName(), article.getId(), field.getFieldType());
			field = ArticleNCSIndexField.CATEGORY_ID;
			doc.setDataValue(field.getFieldName(), article.getCategoryId(), field.getFieldType());
			field = ArticleNCSIndexField.PARENT_CATEGORY_ID;
			doc.setDataValue(field.getFieldName(), article.getParentCategoryId(), field.getFieldType());
			field = ArticleNCSIndexField.KEYWORDS;
			doc.setDataValue(field.getFieldName(), article.getKeywords(), field.getFieldType());
			field = ArticleNCSIndexField.TITLE;
			doc.setDataValue(field.getFieldName(), article.getTitle(), field.getFieldType());
			field = ArticleNCSIndexField.CONTENT;
			doc.setDataValue(field.getFieldName(), article.getContent(), field.getFieldType());
			field = ArticleNCSIndexField.STATUS;
			doc.setDataValue(field.getFieldName(), article.getStatus().getIntValue(), field.getFieldType());
			field = ArticleNCSIndexField.PUBLISH_TYPE;
			doc.setDataValue(field.getFieldName(), article.getPublishType().getIntValue(), field.getFieldType());
		}
		return doc;
	}
	
	/**
	 * 工厂方法
	 * 
	 * @param aticlePublished
	 * @return
	 */
	private IndexDocRequest createArticlePublishedNCSDoc(ArticlePublishedDTO aticlePublished) {
		IndexDocRequest doc = new IndexDocRequest();
		if(null != aticlePublished) {
			ArticlePublishedNCSIndexField field = null;
			field = ArticlePublishedNCSIndexField.ID;
			doc.setDataValue(field.getFieldName(), aticlePublished.getId(), field.getFieldType());
			field = ArticlePublishedNCSIndexField.CATEGORY_ID;
			doc.setDataValue(field.getFieldName(), aticlePublished.getCategoryId(), field.getFieldType());
			field = ArticlePublishedNCSIndexField.PARENT_CATEGORY_ID;
			doc.setDataValue(field.getFieldName(), aticlePublished.getParentCategoryId(), field.getFieldType());
			field = ArticlePublishedNCSIndexField.KEYWORDS;
			doc.setDataValue(field.getFieldName(), aticlePublished.getKeywords(), field.getFieldType());
			field = ArticlePublishedNCSIndexField.TITLE;
			doc.setDataValue(field.getFieldName(), aticlePublished.getTitle(), field.getFieldType());
			field = ArticlePublishedNCSIndexField.CONTENT;
			doc.setDataValue(field.getFieldName(), aticlePublished.getContent(), field.getFieldType());
			field = ArticlePublishedNCSIndexField.STATUS;
			doc.setDataValue(field.getFieldName(), aticlePublished.getStatus().getIntValue(), field.getFieldType());
			field = ArticlePublishedNCSIndexField.PUBLISH_TYPE;
			doc.setDataValue(field.getFieldName(), aticlePublished.getPublishType().getIntValue(), field.getFieldType());
			field = ArticlePublishedNCSIndexField.ORG_ARTICLE_ID;
			doc.setDataValue(field.getFieldName(), aticlePublished.getOrgArticleId(), field.getFieldType());
		}
		return doc;
	}
	
	/**
	 * 构造Article的NDirQuery
	 * 
	 * @param query
	 * @param publishType
	 * @param categoryId
	 * @return
	 */
	private NDirQuery createArticleQuery(String query, int publishType, long categoryId) {
		
		
		List<ClauseQuerySyntax> clauseQuerySyntaxList = null;
		
		// 1. 构造短语组合查询
		clauseQuerySyntaxList = new ArrayList<ClauseQuerySyntax>();
		PhraseQuerySyntax phraseQuerySyntax = null;
		
		phraseQuerySyntax = new PhraseQuerySyntax(ArticleNCSIndexField.KEYWORDS.getFieldName(), query, false);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(phraseQuerySyntax, QueryOccur.SHOULD, boostOfKeywords));
		
		phraseQuerySyntax = new PhraseQuerySyntax(ArticleNCSIndexField.TITLE.getFieldName(), query, false);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(phraseQuerySyntax, QueryOccur.SHOULD, boostOfTitle));
		
		phraseQuerySyntax = new PhraseQuerySyntax(ArticleNCSIndexField.CONTENT.getFieldName(), query, false);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(phraseQuerySyntax, QueryOccur.SHOULD, boostOfContent));
		
		ClauseQuerySyntax cqsOfQuery = new ClauseQuerySyntax(clauseQuerySyntaxList, QueryOccur.MUST, 1);
		
		// 2. 构造num组合查询
		clauseQuerySyntaxList = new ArrayList<ClauseQuerySyntax>();
		NumberQuerySyntax numberQuerySyntax = null;

		numberQuerySyntax = new NumberQuerySyntax(ArticleNCSIndexField.PUBLISH_TYPE.getFieldName(), publishType);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
		ArticlePublishType apt = ArticlePublishType.getArticlePublishTypeByIntValue(publishType);
		switch(apt) {
		case WEB:
			numberQuerySyntax = new NumberQuerySyntax(ArticleNCSIndexField.PUBLISH_TYPE.getFieldName(), 
					ArticlePublishType.BOTH.getIntValue());
			clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
			break;
		case APP:
			numberQuerySyntax = new NumberQuerySyntax(ArticleNCSIndexField.PUBLISH_TYPE.getFieldName(), 
					ArticlePublishType.BOTH.getIntValue());
			clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
			break;
		case BOTH:
			numberQuerySyntax = new NumberQuerySyntax(ArticleNCSIndexField.PUBLISH_TYPE.getFieldName(), 
					ArticlePublishType.WEB.getIntValue());
			clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
			numberQuerySyntax = new NumberQuerySyntax(ArticleNCSIndexField.PUBLISH_TYPE.getFieldName(), 
					ArticlePublishType.APP.getIntValue());
			clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
			break;
		default:
			break;
		}
		
		ClauseQuerySyntax cqsOfNum = new ClauseQuerySyntax(clauseQuerySyntaxList, QueryOccur.MUST, 1);
		
		// 3. 构造category_Id查询
		numberQuerySyntax = new NumberQuerySyntax(ArticleNCSIndexField.CATEGORY_ID.getFieldName(), categoryId);
		
		// 4. 构造最终查询
		clauseQuerySyntaxList = new ArrayList<ClauseQuerySyntax>();
		clauseQuerySyntaxList.add(cqsOfQuery);
		clauseQuerySyntaxList.add(cqsOfNum);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.MUST, 1));
		
		return new NDirQuery(clauseQuerySyntaxList);
	}
	
	/**
	 * 构造ArticlePublished的NDirQuery
	 * 
	 * @param query
	 * @return
	 */
	private NDirQuery createArticlePublishedQuery(String query, int publishType) {
		List<ClauseQuerySyntax> clauseQuerySyntaxList = null;
		
		// 1. 构造短语组合查询
		clauseQuerySyntaxList = new ArrayList<ClauseQuerySyntax>();
		PhraseQuerySyntax phraseQuerySyntax = null;
		
		phraseQuerySyntax = new PhraseQuerySyntax(ArticlePublishedNCSIndexField.KEYWORDS.getFieldName(), query, false);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(phraseQuerySyntax, QueryOccur.SHOULD, boostOfKeywords));
		
		phraseQuerySyntax = new PhraseQuerySyntax(ArticlePublishedNCSIndexField.TITLE.getFieldName(), query, false);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(phraseQuerySyntax, QueryOccur.SHOULD, boostOfTitle));
		
		phraseQuerySyntax = new PhraseQuerySyntax(ArticlePublishedNCSIndexField.CONTENT.getFieldName(), query, false);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(phraseQuerySyntax, QueryOccur.SHOULD, boostOfContent));
		
		ClauseQuerySyntax cqsOfQuery = new ClauseQuerySyntax(clauseQuerySyntaxList, QueryOccur.MUST, 1);
		
		// 2. 构造num组合查询
		clauseQuerySyntaxList = new ArrayList<ClauseQuerySyntax>();
		NumberQuerySyntax numberQuerySyntax = null;
		
		numberQuerySyntax = new NumberQuerySyntax(ArticlePublishedNCSIndexField.PUBLISH_TYPE.getFieldName(), publishType);
		clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
		ArticlePublishType apt = ArticlePublishType.getArticlePublishTypeByIntValue(publishType);
		switch(apt) {
		case WEB:
			numberQuerySyntax = new NumberQuerySyntax(ArticlePublishedNCSIndexField.PUBLISH_TYPE.getFieldName(), 
					ArticlePublishType.BOTH.getIntValue());
			clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
			break;
		case APP:
			numberQuerySyntax = new NumberQuerySyntax(ArticlePublishedNCSIndexField.PUBLISH_TYPE.getFieldName(), 
					ArticlePublishType.BOTH.getIntValue());
			clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
			break;
		case BOTH:
			numberQuerySyntax = new NumberQuerySyntax(ArticlePublishedNCSIndexField.PUBLISH_TYPE.getFieldName(), 
					ArticlePublishType.WEB.getIntValue());
			clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
			numberQuerySyntax = new NumberQuerySyntax(ArticlePublishedNCSIndexField.PUBLISH_TYPE.getFieldName(), 
					ArticlePublishType.APP.getIntValue());
			clauseQuerySyntaxList.add(new ClauseQuerySyntax(numberQuerySyntax, QueryOccur.SHOULD, 1));
			break;
		default:
			break;
		}
		
		ClauseQuerySyntax cqsOfNum = new ClauseQuerySyntax(clauseQuerySyntaxList, QueryOccur.MUST, 1);
		
		// 3. 构造最终查询
		clauseQuerySyntaxList = new ArrayList<ClauseQuerySyntax>();
		clauseQuerySyntaxList.add(cqsOfQuery);
		clauseQuerySyntaxList.add(cqsOfNum);
		
		return new NDirQuery(clauseQuerySyntaxList);
	}
	
	/**
	 * 获取NDirIndexClient
	 * 
	 * @return
	 */
	private NDirIndexClient getNCSIndexClient() {
		if(null == ncsIndexClient) {
			ncsIndexClient = new NDirIndexClient(ncsIDServer);
		}
		return ncsIndexClient;
	}
	
	/**
	 * 获取NDirSearchClient2
	 * 
	 * @return
	 */
	private NDirSearchClient2 getNCSSearchClient() {
		if(null == ncsSearchClient) {
			ncsSearchClient = new NDirSearchClient2(ncsSDServer);
		}
		return ncsSearchClient;
	}
	
	/**
	 * 索引Article
	 * 
	 * @param article
	 * @return
	 *     RetArg.Boolean (Boolean.True for Successful, Boolean.False for Failed)
	 *     RetArg.String (Message)
	 */
	@Override
	public RetArg indexArticle(ArticleDTO article) {
		RetArg retArg = new RetArg();
		if(null == article) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null article param");
			return retArg;
		}
		NDirIndexClient indexClient = getNCSIndexClient();
		IndexDocRequest indexDocRequest = createArticleNCSDoc(article);
		try {
			indexClient.add(indexNameOfArticle, indexDocRequest, null);
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful to index article for " + article.getId());
		} catch (NDirException e) {
			logger.warn("failed to index article for " + article.getId(), e);
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, e.getMessage());
		}
		return retArg;
	}
	
	/**
	 * 索引ArticlePublished
	 * 
	 * @param articlePublished
	 * @return
	 *     RetArg.Boolean (Boolean.True for Successful, Boolean.False for Failed)
	 *     RetArg.String (Message)
	 */
	@Override
	public RetArg indexArticlePublished(ArticlePublishedDTO articlePublished) {
		RetArg retArg = new RetArg();
		if(null == articlePublished) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null articlePublished param");
			return retArg;
		}
		NDirIndexClient indexClient = getNCSIndexClient();
		IndexDocRequest indexDocRequest = createArticlePublishedNCSDoc(articlePublished);
		try {
			indexClient.add(indexNameOfArticlePublished, indexDocRequest, null);
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful to index articlePublished for " + articlePublished.getId());
		} catch (NDirException e) {
			logger.warn("failed to index articlePublished for " + articlePublished.getId(), e);
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, e.getMessage());
		}
		return retArg;
	}
	
	/**
	 * 删除Article
	 * 
	 * @param article
	 * @return
	 *     RetArg.Boolean (Boolean.True for Successful, Boolean.False for Failed)
	 *     RetArg.String (Message)
	 */
	@Override
	public RetArg deleteArticle(ArticleDTO article) {
		RetArg retArg = new RetArg();
		if(null == article) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null article param");
			return retArg;
		}
		NDirIndexClient indexClient = getNCSIndexClient();
		IndexDocRequest indexDocRequest = createArticleNCSDoc(article);
		try {
			indexClient.delete(indexNameOfArticle, indexDocRequest, null);
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful to delete article for " + article.getId());
		} catch (NDirException e) {
			logger.warn("failed to delete article for " + article.getId(), e);
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, e.getMessage());
		}
		return retArg;
	}
	
	/**
	 * 删除ArticlePublished
	 * 
	 * @param articlePublished
	 * @return
	 *     RetArg.Boolean (Boolean.True for Successful, Boolean.False for Failed)
	 *     RetArg.String (Message)
	 */
	@Override
	public RetArg deleteArticlePublished(ArticlePublishedDTO articlePublished) {
		RetArg retArg = new RetArg();
		if(null == articlePublished) {
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, "null articlePublished param");
			return retArg;
		}
		NDirIndexClient indexClient = getNCSIndexClient();
		IndexDocRequest indexDocRequest = createArticlePublishedNCSDoc(articlePublished);
		try {
			indexClient.delete(indexNameOfArticlePublished, indexDocRequest, null);
			RetArgUtil.put(retArg, Boolean.TRUE);
			RetArgUtil.put(retArg, "successful to delete articlePublished for " + articlePublished.getId());
		} catch (NDirException e) {
			logger.warn("failed to delete articlePublished for " + articlePublished.getId(), e);
			RetArgUtil.put(retArg, Boolean.FALSE);
			RetArgUtil.put(retArg, e.getMessage());
		}
		return retArg;
	}
	
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
	@Override
	public List<Long> searchArticle(int publishType, long categoryId, 
			String query, int offset, int length) {
		NDirQuery q = createArticleQuery(query, publishType, categoryId);
		NDirSearchClient2 searchClient = getNCSSearchClient();
  		SearchResultView view = null;
  		boolean isSucc = false;
  		try {
			view = searchClient.globalSearch(indexNameOfArticle, q, offset, length);
			isSucc = true;
		} catch (IOException e) {
			logger.warn("globalSearch() failed of index " + indexNameOfArticle + " for query " + query, e);
		} catch (NDirException e) {
			logger.warn("globalSearch() failed of index " + indexNameOfArticle + " for query " + query, e);
		} catch (NDirClientException e) {
			logger.warn("globalSearch() failed of index " + indexNameOfArticle + " for query " + query, e);
		} finally {
			if(!isSucc) {
				view = null;
			}
		}
  		return extractIdListFromSearchResultView(view, ArticleNCSIndexField.ID.getFieldName());
	}
	
	/**
	 * 检索ArticlePublished
	 * 
	 * @param query
	 * @param offset
	 * @param length
	 * @return
	 */
	@Override
	public List<Long> searchArticlePublished(int publishType, String query, int offset, int length) {
		NDirQuery q = createArticlePublishedQuery(query, publishType);
		NDirSearchClient2 searchClient = getNCSSearchClient();
  		SearchResultView view = null;
  		boolean isSucc = false;
  		try {
			view = searchClient.globalSearch(indexNameOfArticlePublished, q, offset, length);
			isSucc = true;
		} catch (IOException e) {
			logger.warn("globalSearch() failed of index " + indexNameOfArticlePublished + " for query " + query, e);
		} catch (NDirException e) {
			logger.warn("globalSearch() failed of index " + indexNameOfArticlePublished + " for query " + query, e);
		} catch (NDirClientException e) {
			logger.warn("globalSearch() failed of index " + indexNameOfArticlePublished + " for query " + query, e);
		} finally {
			if(!isSucc) {
				view = null;
			}
		}
  		return extractIdListFromSearchResultView(view, ArticlePublishedNCSIndexField.ID.getFieldName());
	}
	
	/**
	 * 从SearchResultView中提取Id
	 * 
	 * @param view
	 * @param idFieldName
	 * @return
	 */
	private List<Long> extractIdListFromSearchResultView(SearchResultView view, String idFieldName) {
		List<Long> idList = new ArrayList<Long>();
		if(null != view && null != idFieldName) {
			List<NDirScoreDocView> docs = view.getDocs();
			if(!CollectionUtil.isEmptyOfList(docs)) {
				for(NDirScoreDocView doc : docs) {
					if(null == doc) {
						continue;
					}
					Long id = doc.getLong(idFieldName);
					if(null != id) {
						idList.add(id);
					}
				}
			}
		}
		return idList;
	}
	
	/**
	 * 按照ncs检索到的Id排序
	 * @param sortedIdList
	 * @param articleList
	 * @return
	 */
	public <A extends Article>
	List<A> sortArticle(List<Long> sortedIdList, List<A> articleList) {
		List<A> sortedArticleList = new ArrayList<A>();
		if(CollectionUtil.isEmptyOfList(sortedIdList) || CollectionUtil.isEmptyOfList(articleList)) {
			return sortedArticleList;
		}
		Map<Long, A> articleMap = new HashMap<Long, A>(articleList.size());
		for(A article : articleList) {
			if(null == article) {
				continue;
			}
			articleMap.put(article.getId(), article);
		}
		for(Long id : sortedIdList) {
			A article = articleMap.get(id);
			if(null != article) {
				sortedArticleList.add(article);
			}
		}
		return sortedArticleList;
	}
}
