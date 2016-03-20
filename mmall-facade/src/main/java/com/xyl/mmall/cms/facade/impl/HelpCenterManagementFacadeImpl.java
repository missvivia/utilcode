/**
 * 
 */
package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.xyl.mmall.cms.facade.HelpCenterManagementFacade;
import com.xyl.mmall.cms.vo.HelpArticleVO;
import com.xyl.mmall.cms.vo.HelpContentCategoryVO;
import com.xyl.mmall.content.dto.ArticleDTO;
import com.xyl.mmall.content.dto.HelpContentCategoryDTO;
import com.xyl.mmall.content.service.HelpArticleService;
import com.xyl.mmall.content.service.HelpContentCategoryService;
import com.xyl.mmall.content.service.HelpContentNCSService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * @author lihui
 *
 */
@Facade
public class HelpCenterManagementFacadeImpl implements HelpCenterManagementFacade {
	
	private static final Logger logger = Logger.getLogger(HelpCenterManagementFacadeImpl.class);
	
	@Value("${ncs.enable}")
	private boolean ncsEnable;

	@Autowired
	private HelpArticleService helpArticleService;

	@Autowired
	private HelpContentCategoryService helpContentCategoryService;
	
	@Autowired
	private HelpContentNCSService helpContentNCSService;
	
	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#searchArticleByPublicTypeAndCategoryAndKeywords(int,
	 *      long, java.lang.String, int, int)
	 */
	@Override
	public BaseJsonVO searchArticleByPublicTypeAndCategoryAndKeywords(int publishType, long categoryId,
			String keywords, int limit, int offset) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		List<String> keywordList = new ArrayList<>();
		if (StringUtils.isNotBlank(keywords)) {
			String[] keywordsArr = StringUtils.split(keywords, " ");
			keywordList = Arrays.asList(keywordsArr);
		}
		List<ArticleDTO> articleList = null;
		articleList = helpArticleService.findArticleByPublishTypeAndCategoryAndKeywords(publishType,
				categoryId, keywordList, limit, offset);
//		if(!ncsEnable) {
//			articleList = helpArticleService.findArticleByPublishTypeAndCategoryAndKeywords(publishType,
//				categoryId, keywordList, limit, offset);
//		} else {
//			SearchResultView view = helpContentNCSService.searchArticle(publishType, categoryId, keywords, offset, limit);
//			if(null != view) {
//				List<Long> idList = helpContentNCSService.extractIdListFromSearchResultView(view, ArticleNCSIndexField.ID.getFieldName());
//				articleList = helpArticleService.getArticleByIdList(idList, null);
//				articleList = helpContentNCSService.sortArticle(idList, articleList);
//			}
//		}
		if (CollectionUtils.isEmpty(articleList)) {
			return result;
		}
		List<HelpArticleVO> articleVOList = new ArrayList<>();
		for (ArticleDTO article : articleList) {
			articleVOList.add(new HelpArticleVO(article));
		}
		BaseJsonListResultVO listResult = new BaseJsonListResultVO();
		listResult.setList(articleVOList);
		listResult.setTotal(helpArticleService.countArticleByPublishTypeAndCategoryAndKeywords(publishType, categoryId,
				keywordList));
		result.setResult(listResult);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#getHelpArticleDetail(long)
	 */
	@Override
	public HelpArticleVO getHelpArticleDetail(long id) {
		ArticleDTO article = helpArticleService.getArticleDetail(id);
		if (article != null) {
			return new HelpArticleVO(article);
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#saveHelpArticle(com.xyl.mmall.cms.vo.HelpArticleVO,
	 *      long, java.lang.String)
	 */
	@Override
	public BaseJsonVO saveHelpArticle(HelpArticleVO article, long userId, String userName) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		ArticleDTO articleDTO = article.getArticle();
		articleDTO.setLastModifiedBy(userId);
		articleDTO.setLastModifiedUserName(userName);
		ArticleDTO newArticleDTO = helpArticleService.saveHelpArticle(articleDTO);
		result.setResult(new HelpArticleVO(newArticleDTO));
		try {
			helpContentNCSService.indexArticle(newArticleDTO);
		} catch (Exception e) {
			logger.warn("ncs operation failed for Article " + newArticleDTO.getId(), e);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#saveAndPublishHelpArticle(com.xyl.mmall.cms.vo.HelpArticleVO,
	 *      long, java.lang.String)
	 */
	@Override
	public BaseJsonVO saveAndPublishHelpArticle(HelpArticleVO article, long userId, String userName) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		ArticleDTO articleDTO = article.getArticle();
		articleDTO.setLastModifiedBy(userId);
		articleDTO.setLastModifiedUserName(userName);
		ArticleDTO newArticleDTO = helpArticleService.saveAndPublishHelpArticle(articleDTO);
		result.setResult(new HelpArticleVO(newArticleDTO));
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#publishHelpArticle(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO publishHelpArticle(long id, long userId) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		ArticleDTO newArticleDTO = helpArticleService.publishHelpArticle(id, userId);
		result.setResult(new HelpArticleVO(newArticleDTO));
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#revokeHelpArticle(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO revokeHelpArticle(long id, long userId) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		ArticleDTO newArticleDTO = helpArticleService.revokeHelpArticle(id, userId);
		result.setResult(new HelpArticleVO(newArticleDTO));
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#deleteHelpArticle(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO deleteHelpArticle(long id, long userId) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		helpArticleService.deleteHelpArticle(id, userId);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#getHelpArticleCategory()
	 */
	@Override
	public List<HelpContentCategoryVO> getHelpArticleCategory() {
		List<HelpContentCategoryVO> parentCategoryVOList = new ArrayList<>();
		List<HelpContentCategoryDTO> categoryList = helpContentCategoryService.getHelpContentCategoryList();
		if (CollectionUtils.isEmpty(categoryList)) {
			return parentCategoryVOList;
		}
		for (HelpContentCategoryDTO categoryDTO : categoryList) {
			if (categoryDTO.getParentId() == 0L) {
				parentCategoryVOList.add(new HelpContentCategoryVO(categoryDTO));
			} else {
				for (HelpContentCategoryVO parentVO : parentCategoryVOList) {
					if (parentVO.getId() == categoryDTO.getParentId()) {
						if (parentVO.getChildren() == null) {
							parentVO.setChildren(new ArrayList<HelpContentCategoryVO>());
						}
						parentVO.getChildren().add(new HelpContentCategoryVO(categoryDTO));
					}
				}
			}
		}
		return parentCategoryVOList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.HelpCenterManagementFacade#ncssync()
	 */
	@Override
	public void ncssync() {
		helpArticleService.syncNCSDispatchLog();
	}

}
