/**
 * 
 */
package com.xyl.mmall.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.dto.ArticleDTO;
import com.xyl.mmall.content.dto.ArticlePublishedDTO;
import com.xyl.mmall.content.dto.NCSContentDispatchLogDTO;
import com.xyl.mmall.content.enums.ArticlePublishType;
import com.xyl.mmall.content.enums.ArticleStatus;
import com.xyl.mmall.content.enums.ContentType;
import com.xyl.mmall.content.enums.NCSIndexDispatchState;
import com.xyl.mmall.content.meta.Article;
import com.xyl.mmall.content.meta.ArticlePublished;
import com.xyl.mmall.content.meta.HelpContentCategory;
import com.xyl.mmall.content.meta.NCSContentDispatchLog;
import com.xyl.mmall.content.service.HelpArticleService;
import com.xyl.mmall.content.service.HelpContentNCSService;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.content.dao.ArticleDao;
import com.xyl.mmall.content.dao.ArticlePublishedDao;
import com.xyl.mmall.content.dao.HelpContentCategoryDao;
import com.xyl.mmall.content.dao.NCSContentDispatchLogDao;

/**
 * @author lihui
 *
 */
public class HelpArticleServiceImpl implements HelpArticleService {
	
	private static final Logger logger = Logger.getLogger(HelpArticleServiceImpl.class);
	
	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private ArticlePublishedDao articlePublishedDao;

	@Autowired
	private HelpContentCategoryDao helpContentCategoryDao;
	
	@Autowired
	private NCSContentDispatchLogDao ncsContentDispatchLogDao;
	
	@Autowired
	private HelpContentNCSService helpContentNCSService;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#findPublishedArticleByPublishTypeAndCategory(int,
	 *      long)
	 */
	@Override
	@Cacheable(value = "publishedArticle")
	public List<ArticlePublishedDTO> findPublishedArticleByPublishTypeAndCategory(int publishType, long categoryId) {
		return findPublishedArticleByPublishTypeAndCategoryAndKeywords(publishType, categoryId, null, false,
				DDBParam.genParam500());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#findPublishedArticleByPublishTypeAndKeywordList(int,
	 *      java.util.List, int, int)
	 */
	@Override
	public List<ArticlePublishedDTO> findPublishedArticleByPublishTypeAndKeywordList(int publishType,
			List<String> keywordList, int limit, int offset) {
		if (CollectionUtils.isEmpty(keywordList)) {
			return null;
		}
		DDBParam param = DDBParam.genParam1();
		param.setLimit(limit);
		param.setOffset(offset);
		return findPublishedArticleByPublishTypeAndCategoryAndKeywords(publishType, -1L, keywordList, true, param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#countPublishedArticleByPublishTypeAndKeywordList(int,
	 *      java.util.List)
	 */
	@Override
	public int countPublishedArticleByPublishTypeAndKeywordList(int publishType, List<String> keywordList) {
		if (CollectionUtils.isEmpty(keywordList)) {
			return 0;
		}
		return countPublishedArticleByPublishTypeAndCategoryAndKeywords(publishType, -1L, keywordList, true);
	}

	/**
	 * 
	 * @param publishType
	 * @param categoryId
	 * @param keywordList
	 * @param allowEmptyCategory
	 * @return
	 */
	private int countArticleByPublishTypeAndCategoryAndKeywords(int publishType, long categoryId,
			List<String> keywordList, boolean allowEmptyCategory) {
		if (categoryId == -1L && !allowEmptyCategory) {
			return 0;
		}
		// TODO:关键字查询暂时不错，将使用搜索引擎检索。
		List<String> searchKeywordList = new ArrayList<>();
		HelpContentCategory category = helpContentCategoryDao.getObjectById(categoryId);
		// 如果类别的父类别ID为0，则查询父类，否则查询子类。
		if (category != null && category.getParentId() == 0L) {
			return articlePublishedDao.countByPublishTypeAndKeywordListAndCategoryId(buildPublishTypeList(publishType),
					searchKeywordList, -1L, categoryId);
		} else {
			return articlePublishedDao.countByPublishTypeAndKeywordListAndCategoryId(buildPublishTypeList(publishType),
					searchKeywordList, categoryId, -1L);
		}

	}

	/**
	 * @param publishType
	 * @return
	 */
	private List<Integer> buildPublishTypeList(int publishType) {
		List<Integer> publishTypeList = new ArrayList<>();
		if (publishType == -1L) {
			return publishTypeList;
		}
		publishTypeList.add(publishType);
		if (ArticlePublishType.WEB.getIntValue() == publishType || ArticlePublishType.APP.getIntValue() == publishType) {
			publishTypeList.add(ArticlePublishType.BOTH.getIntValue());
		} else if (ArticlePublishType.BOTH.getIntValue() == publishType) {
			publishTypeList.add(ArticlePublishType.APP.getIntValue());
			publishTypeList.add(ArticlePublishType.WEB.getIntValue());
		}
		return publishTypeList;
	}

	/**
	 * 
	 * @param publishType
	 * @param categoryId
	 * @param keywordList
	 * @param allowEmptyCategory
	 * @param param
	 * @return
	 */
	private List<ArticleDTO> findArticleByPublishTypeAndCategoryAndKeywords(int publishType, long categoryId,
			List<String> keywordList, boolean allowEmptyCategory, DDBParam param) {
		if (categoryId == -1L && !allowEmptyCategory) {
			return null;
		}
		// TODO:关键字查询暂时不错，将使用搜索引擎检索。
		List<String> searchKeywordList = new ArrayList<>();
		HelpContentCategory category = helpContentCategoryDao.getObjectById(categoryId);
		// 如果类别的父类别ID为0，则查询父类，否则查询子类。
		List<Article> articleList = null;
		if (category != null && category.getParentId() == 0L) {
			articleList = articleDao.findByPublishTypeAndKeywordListAndCategoryId(buildPublishTypeList(publishType),
					searchKeywordList, -1L, categoryId, param);
		} else {
			articleList = articleDao.findByPublishTypeAndKeywordListAndCategoryId(buildPublishTypeList(publishType),
					searchKeywordList, categoryId, -1L, param);
		}
		if (CollectionUtils.isEmpty(articleList)) {
			return null;
		}
		List<ArticleDTO> articleDTOList = new ArrayList<>();
		for (Article article : articleList) {
			// 列表不需要文章的内容，清空
			article.setContent(null);
			ArticleDTO articleDTO = new ArticleDTO(article);
			articleDTOList.add(articleDTO);
		}
		return articleDTOList;
	}

	/**
	 * 
	 * @param publishType
	 * @param categoryId
	 * @param keywordList
	 * @param allowEmptyCategory
	 * @return
	 */
	private int countPublishedArticleByPublishTypeAndCategoryAndKeywords(int publishType, long categoryId,
			List<String> keywordList, boolean allowEmptyCategory) {
		if (categoryId == -1L && !allowEmptyCategory) {
			return 0;
		}
		// TODO:关键字查询暂时不错，将使用搜索引擎检索。
		List<String> searchKeywordList = new ArrayList<>();
		HelpContentCategory category = helpContentCategoryDao.getObjectById(categoryId);
		// 如果类别的父类别ID为0，则查询父类，否则查询子类。
		if (category != null && category.getParentId() == 0L) {
			return articleDao.countByPublishTypeAndKeywordListAndCategoryId(buildPublishTypeList(publishType),
					searchKeywordList, -1L, categoryId);
		} else {
			return articleDao.countByPublishTypeAndKeywordListAndCategoryId(buildPublishTypeList(publishType),
					searchKeywordList, categoryId, -1L);
		}
	}

	/**
	 * 
	 * @param publishType
	 * @param categoryId
	 * @param keywordList
	 * @param allowEmptyCategory
	 * @param param
	 * @return
	 */
	private List<ArticlePublishedDTO> findPublishedArticleByPublishTypeAndCategoryAndKeywords(int publishType,
			long categoryId, List<String> keywordList, boolean allowEmptyCategory, DDBParam param) {
		if (categoryId == -1L && !allowEmptyCategory) {
			return null;
		}
		// TODO:关键字查询暂时不错，将使用搜索引擎检索。
		List<String> searchKeywordList = new ArrayList<>();
		HelpContentCategory category = helpContentCategoryDao.getObjectById(categoryId);
		// 如果类别的父类别ID为0，则查询父类，否则查询子类。
		List<ArticlePublished> articleList = null;
		if (category != null && category.getParentId() == 0L) {
			articleList = articlePublishedDao.findByPublishTypeAndKeywordListAndCategoryId(
					buildPublishTypeList(publishType), searchKeywordList, -1L, categoryId, param);
		} else {
			articleList = articlePublishedDao.findByPublishTypeAndKeywordListAndCategoryId(
					buildPublishTypeList(publishType), searchKeywordList, categoryId, -1L, param);
		}
		if (CollectionUtils.isEmpty(articleList)) {
			return null;
		}
		List<ArticlePublishedDTO> articleDTOList = new ArrayList<>();
		for (ArticlePublished article : articleList) {
			ArticlePublishedDTO articleDTO = new ArticlePublishedDTO(article);
			articleDTOList.add(articleDTO);
		}
		return articleDTOList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#countArticleByPublishTypeAndCategoryAndKeywords(int,
	 *      long, java.util.List)
	 */
	@Override
	public int countArticleByPublishTypeAndCategoryAndKeywords(int publishType, long categoryId,
			List<String> keywordList) {
		return countArticleByPublishTypeAndCategoryAndKeywords(publishType, categoryId, keywordList, true);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#findArticleByPublishTypeAndCategoryAndKeywords(int,
	 *      long, java.util.List, int, int)
	 */
	@Override
	public List<ArticleDTO> findArticleByPublishTypeAndCategoryAndKeywords(int publishType, long categoryId,
			List<String> keywordList, int limit, int offset) {
		DDBParam param = DDBParam.genParam1();
		param.setOffset(offset);
		param.setLimit(limit);
		return findArticleByPublishTypeAndCategoryAndKeywords(publishType, categoryId, keywordList, true, param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#getArticleDetail(long)
	 */
	@Override
	public ArticleDTO getArticleDetail(long id) {
		Article article = articleDao.findDetailWithParentCategoryById(id);
		if (article == null) {
			return null;
		}
		return new ArticleDTO(article);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#saveHelpArticle(com.xyl.mmall.content.dto.ArticleDTO)
	 */
	@Override
	public ArticleDTO saveHelpArticle(ArticleDTO articleDTO) {
		ArticleDTO ret = null;
		HelpContentCategory category = helpContentCategoryDao.getObjectById(articleDTO.getCategoryId());
		boolean isParent = category != null && category.getParentId() == 0L;
		if (articleDTO.getId() == 0L) {
			Article newArticle = new Article();
			ReflectUtil.convertObj(newArticle, articleDTO, false);
			newArticle.setLastSaveTime(System.currentTimeMillis());
			newArticle.setStatus(ArticleStatus.SAVED);
			if (isParent) {
				newArticle.setParentCategoryId(articleDTO.getCategoryId());
				newArticle.setCategoryId(0);
			} else {
				newArticle.setParentCategoryId(category.getParentId());
			}
			newArticle = articleDao.addObject(newArticle);
			ret = new ArticleDTO(newArticle);
		} else {
			Article savedArticle = articleDao.findDetailWithParentCategoryById(articleDTO.getId());
			ArticleStatus savedArticleStatus = savedArticle.getStatus();
			long orgPublishTime = savedArticle.getPublishTime();
			ReflectUtil.convertObj(savedArticle, articleDTO, false);
			savedArticle.setLastSaveTime(System.currentTimeMillis());
			savedArticle.setPublishTime(orgPublishTime);
			if (isParent) {
				savedArticle.setParentCategoryId(articleDTO.getCategoryId());
				savedArticle.setCategoryId(0);
			} else {
				savedArticle.setParentCategoryId(category.getParentId());
			}
			// 保持原来的状态
			savedArticle.setStatus(savedArticleStatus);
			articleDao.updateObjectByKey(savedArticle);
			ret =  new ArticleDTO(savedArticle);
		}
		try {
			RetArg retArg = helpContentNCSService.indexArticle(ret);
			Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
			if(Boolean.TRUE != isSucc) {
				addArticelToNCSIndexDispatchLog(ret, NCSIndexDispatchState.WAITING_TO_DISPATCH);
			} else {
				addArticelToNCSIndexDispatchLog(ret, NCSIndexDispatchState.DISPATCHED);
			}
		} catch (Exception e) {
			logger.warn("ncs 异常", e);
		}
		
		return ret;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#saveAndPublishHelpArticle(com.xyl.mmall.content.dto.ArticleDTO)
	 */
	@Override
	@Transaction
	public ArticleDTO saveAndPublishHelpArticle(ArticleDTO articleDTO) {
		ArticleDTO ret = null;
		HelpContentCategory category = helpContentCategoryDao.getObjectById(articleDTO.getCategoryId());
		boolean isParent = category != null && category.getParentId() == 0L;
		if (articleDTO.getId() == 0L) {
			Article newArticle = new Article();
			ReflectUtil.convertObj(newArticle, articleDTO, false);
			newArticle.setLastSaveTime(System.currentTimeMillis());
			if (isParent) {
				newArticle.setParentCategoryId(articleDTO.getCategoryId());
				newArticle.setCategoryId(0);
			} else {
				newArticle.setParentCategoryId(category.getParentId());
			}
			// 更新状态为已发布
			newArticle.setPublishTime(System.currentTimeMillis());
			newArticle.setStatus(ArticleStatus.PUBLISHED);
			newArticle = articleDao.addObject(newArticle);
			// 发布文章
			updatePublishedHelpArticle(newArticle);
			ret = new ArticleDTO(newArticle);
		} else {
			Article savedArticle = articleDao.findDetailWithParentCategoryById(articleDTO.getId());
			ReflectUtil.convertObj(savedArticle, articleDTO, false);
			savedArticle.setLastSaveTime(System.currentTimeMillis());
			if (isParent) {
				savedArticle.setParentCategoryId(articleDTO.getCategoryId());
				savedArticle.setCategoryId(0);
			} else {
				savedArticle.setParentCategoryId(category.getParentId());
			}
			// 更新状态为已发布
			savedArticle.setPublishTime(System.currentTimeMillis());
			savedArticle.setStatus(ArticleStatus.PUBLISHED);
			articleDao.updateObjectByKey(savedArticle);
			// 发布文章
			updatePublishedHelpArticle(savedArticle);
			ret = new ArticleDTO(savedArticle);
		}
		try {
			RetArg retArg = helpContentNCSService.indexArticle(ret);
			Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
			if(Boolean.TRUE != isSucc) {
				addArticelToNCSIndexDispatchLog(ret, NCSIndexDispatchState.WAITING_TO_DISPATCH);
			} else {
				addArticelToNCSIndexDispatchLog(ret, NCSIndexDispatchState.DISPATCHED);
			}
		} catch (Exception e) {
			logger.warn("ncs 异常", e);
		}
		return ret;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#publishHelpArticle(long,
	 *      long)
	 */
	@Override
	@Transaction
	public ArticleDTO publishHelpArticle(long id, long userId) {
		Article savedArticle = articleDao.findDetailWithParentCategoryById(id);
		if (savedArticle == null) {
			return null;
		}
		// 更新原始文章的状态
		savedArticle.setPublishTime(System.currentTimeMillis());
		savedArticle.setStatus(ArticleStatus.PUBLISHED);
		articleDao.updateObjectByKey(savedArticle);
		// 发布文章
		updatePublishedHelpArticle(savedArticle);
		ArticleDTO ret = new ArticleDTO(savedArticle);
		try {
			RetArg retArg = helpContentNCSService.indexArticle(ret);
			Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
			if(Boolean.TRUE != isSucc) {
				addArticelToNCSIndexDispatchLog(ret, NCSIndexDispatchState.WAITING_TO_DISPATCH);
			} else {
				addArticelToNCSIndexDispatchLog(ret, NCSIndexDispatchState.DISPATCHED);
			}
		} catch (Exception e) {
			logger.warn("ncs 异常", e);
		}
		return ret;
	}

	/**
	 * 根据已经保存好的文章发布副本。
	 * 
	 * @param savedArticle
	 *            已保存的文章
	 * 
	 */
	private void updatePublishedHelpArticle(Article savedArticle) {
		ArticlePublished ncsPublishedArticle = null;
		// 更新发布文章副本
		ArticlePublished publishedArticle = articlePublishedDao.findByOrgArticleId(savedArticle.getId());
		if (publishedArticle == null) {
			publishedArticle = new ArticlePublished();
			ReflectUtil.convertObj(publishedArticle, savedArticle, false);
			// 保存初始文章的ID
			publishedArticle.setOrgArticleId(savedArticle.getId());
			publishedArticle.setId(0);
			ncsPublishedArticle = articlePublishedDao.addObject(publishedArticle);
		} else {
			long publishedArticleId = publishedArticle.getId();
			ReflectUtil.convertObj(publishedArticle, savedArticle, false);
			publishedArticle.setId(publishedArticleId);
			articlePublishedDao.updateObjectByKey(publishedArticle);
			ncsPublishedArticle = publishedArticle;
		}
		if(null != ncsPublishedArticle) {
			try {
				ArticlePublishedDTO apDTO = new ArticlePublishedDTO(ncsPublishedArticle);
				RetArg retArg = helpContentNCSService.indexArticlePublished(apDTO);
				Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
				if(Boolean.TRUE != isSucc) {
					addArticelPublishedToNCSIndexDispatchLog(apDTO, NCSIndexDispatchState.WAITING_TO_DISPATCH);
				} else {
					addArticelPublishedToNCSIndexDispatchLog(apDTO, NCSIndexDispatchState.DISPATCHED);
				}
			} catch (Exception e) {
				logger.warn("ncs 异常", e);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#revokeHelpArticle(long,
	 *      long)
	 */
	@Override
	@Transaction
	public ArticleDTO revokeHelpArticle(long id, long userId) {
		Article savedArticle = articleDao.findDetailWithParentCategoryById(id);
		if (savedArticle == null) {
			// 原始文章不存在，返回空
			return null;
		}
		ArticlePublished publishedArticle = articlePublishedDao.findByOrgArticleId(id);
		if (publishedArticle != null) {
			// 发布的文章存在，删除
			articlePublishedDao.deleteObjectByKey(publishedArticle);
			try {
				ArticlePublishedDTO publishedArticleDTO = new ArticlePublishedDTO(publishedArticle);
				RetArg retArg = helpContentNCSService.deleteArticlePublished(publishedArticleDTO);
				Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
				if(Boolean.TRUE != isSucc) {
					addArticelPublishedToNCSIndexDispatchLog(publishedArticleDTO, NCSIndexDispatchState.WAITING_TO_DELETE);
				} else {
					addArticelPublishedToNCSIndexDispatchLog(publishedArticleDTO, NCSIndexDispatchState.DELETED);
				}
			} catch (Exception e) {
				logger.warn("ncs 异常", e);
			}
		}
		savedArticle.setStatus(ArticleStatus.SAVED);
		articleDao.updateObjectByKey(savedArticle);
		ArticleDTO ret = new ArticleDTO(savedArticle);
		try {
			RetArg retArg = helpContentNCSService.indexArticle(ret);
			Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
			if(Boolean.TRUE != isSucc) {
				addArticelToNCSIndexDispatchLog(ret, NCSIndexDispatchState.WAITING_TO_DISPATCH);
			} else {
				addArticelToNCSIndexDispatchLog(ret, NCSIndexDispatchState.DISPATCHED);
			}
		} catch (Exception e) {
			logger.warn("ncs 异常", e);
		}
		return ret;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.HelpArticleService#deleteHelpArticle(long,
	 *      long)
	 */
	@Override
	public void deleteHelpArticle(long id, long userId) {
		// 检查是否有已发布的文章副本
		ArticlePublished publishedArticle = articlePublishedDao.findByOrgArticleId(id);
		if (publishedArticle != null) {
			// 发布的文章存在，删除
			articlePublishedDao.deleteObjectByKey(publishedArticle);
			try {
				ArticlePublishedDTO publishedArticleDTO = new ArticlePublishedDTO(publishedArticle);
				RetArg retArg = helpContentNCSService.deleteArticlePublished(publishedArticleDTO);
				Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
				if(Boolean.TRUE != isSucc) {
					addArticelPublishedToNCSIndexDispatchLog(publishedArticleDTO, NCSIndexDispatchState.WAITING_TO_DELETE);
				} else {
					addArticelPublishedToNCSIndexDispatchLog(publishedArticleDTO, NCSIndexDispatchState.DELETED);
				}
			} catch (Exception e) {
				logger.warn("ncs 异常", e);
			}
		}
		// 删除原始文章
		Article deleteArticle = articleDao.findDetailWithParentCategoryById(id);
		articleDao.deleteById(id);
		if(null != deleteArticle) {
			try {
				ArticleDTO deleteArticleDTO = new ArticleDTO(deleteArticle);
				RetArg retArg = helpContentNCSService.deleteArticle(deleteArticleDTO);
				Boolean isSucc = RetArgUtil.get(retArg, Boolean.class);
				if(Boolean.TRUE != isSucc) {
					addArticelToNCSIndexDispatchLog(deleteArticleDTO, NCSIndexDispatchState.WAITING_TO_DELETE);
				} else {
					addArticelToNCSIndexDispatchLog(deleteArticleDTO, NCSIndexDispatchState.DELETED);
				}
			} catch (Exception e) {
				logger.warn("ncs 异常", e);
			}
		}
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#findNCSContentDispatchLogByDispatchStatesWithMinId(long, com.xyl.mmall.content.enums.NCSIndexDispatchState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg findNCSContentDispatchLogByDispatchStatesWithMinId(long minId, NCSIndexDispatchState[] states, DDBParam param) {
		RetArg retArg = new RetArg();
		List<NCSContentDispatchLog> logList = ncsContentDispatchLogDao.getDispatchLogByTypeAndStateWithMinId(minId, 
				new ContentType[] {ContentType.ARTICLE, ContentType.ARTICLE_PUBLISHED}, states, param);
		List<NCSContentDispatchLogDTO> logDTOList = new ArrayList<NCSContentDispatchLogDTO>();
		if(!CollectionUtil.isEmptyOfList(logList)) {
			for(NCSContentDispatchLog log : logList) {
				if(null == log) {
					continue;
				}
				logDTOList.add(new NCSContentDispatchLogDTO(log));
			}
		}
		RetArgUtil.put(retArg, logDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#getArticleById(long)
	 */
	@Override
	public ArticleDTO getArticleById(long id) {
		Article article = articleDao.getObjectById(id);
		return null == article ? null : new ArticleDTO(article);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#getArticleByIdList(java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ArticleDTO> getArticleByIdList(List<Long> idList, DDBParam param) {
		List<Article> list = articleDao.findByIdList(idList, param);
		List<ArticleDTO> dtoList = new ArrayList<ArticleDTO>();
		if(!CollectionUtil.isEmptyOfList(list)) {
			for(Article articel : list) {
				if(null == articel) {
					continue;
				}
				dtoList.add(new ArticleDTO(articel));
			}
		}
		return dtoList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#getArticlePublishedById(long)
	 */
	@Override
	public ArticlePublishedDTO getArticlePublishedById(long id) {
		ArticlePublished articlePublished = articlePublishedDao.getObjectById(id);
		return null == articlePublished ? null : new ArticlePublishedDTO(articlePublished);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#getArticlePublishedByIdList(java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<ArticlePublishedDTO> getArticlePublishedByIdList(List<Long> idList, DDBParam param) {
		List<ArticlePublished> list = articlePublishedDao.findByIdList(idList, param);
		List<ArticlePublishedDTO> dtoList = new ArrayList<ArticlePublishedDTO>();
		if(!CollectionUtil.isEmptyOfList(list)) {
			for(ArticlePublished ap : list) {
				if(null == ap) {
					continue;
				}
				dtoList.add(new ArticlePublishedDTO(ap));
			}
		}
		return dtoList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#addArticelToNCSIndexDispatchLog(com.xyl.mmall.content.dto.ArticleDTO, com.xyl.mmall.content.enums.NCSIndexDispatchState)
	 */
	@Override
	public NCSContentDispatchLogDTO addArticelToNCSIndexDispatchLog(ArticleDTO article, NCSIndexDispatchState dispatchState) {
		if(null == article || null == dispatchState) {
			return null;
		}
		NCSContentDispatchLog log = helpContentNCSService.createNCSContentDispatchLog(ContentType.ARTICLE, article.getId(), dispatchState);
		log = ncsContentDispatchLogDao.addObject(log);
		return null == log ? null : new NCSContentDispatchLogDTO(log);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#addArticelPublishedToNCSIndexDispatchLog(com.xyl.mmall.content.dto.ArticlePublishedDTO, com.xyl.mmall.content.enums.NCSIndexDispatchState)
	 */
	@Override
	public NCSContentDispatchLogDTO addArticelPublishedToNCSIndexDispatchLog(ArticlePublishedDTO articlePublished, NCSIndexDispatchState dispatchState) {
		if(null == articlePublished || null == dispatchState) {
			return null;
		}
		NCSContentDispatchLog log = helpContentNCSService.createNCSContentDispatchLog(ContentType.ARTICLE_PUBLISHED, articlePublished.getId(), dispatchState);
		log = ncsContentDispatchLogDao.addObject(log);
		return null == log ? null : new NCSContentDispatchLogDTO(log);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#updateDispatchLog(com.xyl.mmall.content.meta.NCSContentDispatchLog, com.xyl.mmall.content.enums.NCSIndexDispatchState, com.xyl.mmall.content.enums.NCSIndexDispatchState[])
	 */
	@Override
	public boolean updateDispatchLog(NCSContentDispatchLog log, NCSIndexDispatchState newState, NCSIndexDispatchState[] oldStates) {
		if(null == log || null == newState || null == oldStates) {
			return false;
		}
		return ncsContentDispatchLogDao.updateDispatchLog(log, newState, oldStates);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.service.HelpArticleService#syncNCSDispatchLog()
	 */
	@Override
	public void syncNCSDispatchLog() {
		List<Long> idList = null;
		
		/* Article */
		idList = articleDao.findAllIdSet(null);
		if(!CollectionUtil.isEmptyOfList(idList)) {
			for(Long id : idList) {
				if(null == id) {
					continue;
				}
				NCSContentDispatchLog log = new NCSContentDispatchLog();
				log.setContentType(ContentType.ARTICLE);
				log.setDispatchState(NCSIndexDispatchState.WAITING_TO_DISPATCH);
				log.setForeignPrimaryId(id);
				log.setCreateTime(System.currentTimeMillis());
				log.setUpdateTime(System.currentTimeMillis());
				ncsContentDispatchLogDao.addObject(log);
			}
		}
		
		/* ArticlePublished */
		idList = articlePublishedDao.findAllIdSet(null);
		if(!CollectionUtil.isEmptyOfList(idList)) {
			for(Long id : idList) {
				if(null == id) {
					continue;
				}
				NCSContentDispatchLog log = new NCSContentDispatchLog();
				log.setContentType(ContentType.ARTICLE_PUBLISHED);
				log.setDispatchState(NCSIndexDispatchState.WAITING_TO_DISPATCH);
				log.setForeignPrimaryId(id);
				log.setCreateTime(System.currentTimeMillis());
				log.setUpdateTime(System.currentTimeMillis());
				ncsContentDispatchLogDao.addObject(log);
			}
		}
	}
	
}
