/**
 * 
 */
package com.xyl.mmall.mobile.web.facade.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.xyl.mmall.content.dto.ArticlePublishedDTO;
import com.xyl.mmall.content.dto.HelpContentCategoryDTO;
import com.xyl.mmall.content.enums.ArticlePublishType;
import com.xyl.mmall.content.service.HelpArticleService;
import com.xyl.mmall.content.service.HelpContentCategoryService;
import com.xyl.mmall.content.service.HelpContentNCSService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.web.facade.MobileHelpCenterFacade;
import com.xyl.mmall.mobile.web.vo.HelpArticleVO;
import com.xyl.mmall.mobile.web.vo.HelpContentCategoryVO;


/**
 * @author lihui
 *
 */
@Facade("mobileHelpCenterFacade")
public class MobileHelpCenterFacadeImpl implements MobileHelpCenterFacade {
	
	@Value("${ncs.enable}")
	private boolean ncsEnable;

	@Autowired
	private HelpArticleService helpArticleService;

	@Autowired
	private HelpContentCategoryService helpContentCategoryService;
	
	@Autowired
	private HelpContentNCSService helpContentNCSService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.findWebPublishedArticleByCategory#findArticleByCategory(long)
	 */
	@Override
	public BaseJsonVO findWebPublishedArticleByCategory(long categoryId) {
		return findPublishedArticleByPublishTypeAndCategory(ArticlePublishType.WEB, categoryId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.HelpCenterFacade#searchWebPublishedArticle(java.lang.String,
	 *      int, int)
	 */
	@Override
	public BaseJsonVO searchWebPublishedArticle(String keywords, int limit, int offset) {
		return searchPublishedArticle(ArticlePublishType.WEB, keywords, limit, offset);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.HelpCenterFacade#findAppPublishedArticleByCategory(long)
	 */
	@Override
	public BaseJsonVO findAppPublishedArticleByCategory(long categoryId) {
		return findPublishedArticleByPublishTypeAndCategory(ArticlePublishType.APP, categoryId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.HelpCenterFacade#searchAppPublishedArticle(java.lang.String,
	 *      int, int)
	 */
	@Override
	public BaseJsonVO searchAppPublishedArticle(String keywords, int limit, int offset) {
		return searchPublishedArticle(ArticlePublishType.APP, keywords, limit, offset);
	}

	/**
	 * 根据类别和发布类型查找文章。
	 * 
	 * @param publishType
	 *            发布的类型
	 * @param categoryId
	 *            类别ID
	 * @return
	 */
	private BaseJsonVO findPublishedArticleByPublishTypeAndCategory(ArticlePublishType publishType, long categoryId) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		if (categoryId == 0) {
			return result;
		}
		List<ArticlePublishedDTO> articleList = helpArticleService.findPublishedArticleByPublishTypeAndCategory(
				publishType.getIntValue(), categoryId);
		if (CollectionUtils.isEmpty(articleList)) {
			return result;
		}
		List<HelpArticleVO> articleVOList = new ArrayList<>();
		for (ArticlePublishedDTO article : articleList) {
			articleVOList.add(new HelpArticleVO(article));
		}
		BaseJsonListResultVO listResult = new BaseJsonListResultVO();
		listResult.setList(articleVOList);
		result.setResult(listResult);
		return result;
	}

	/**
	 * 根据发布的类型和关键字查找文章。
	 * 
	 * @param offset
	 *            分页位置
	 * @param limit
	 *            分页大小
	 * @param keywords
	 *            关键字
	 * @param publishType
	 *            发布的类型
	 * @return
	 */
	private BaseJsonVO searchPublishedArticle(ArticlePublishType publishType, String keywords, int limit, int offset) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		if (StringUtils.isBlank(keywords)) {
			return result;
		}
		String[] keywordsArr = StringUtils.split(keywords, " ");
		List<String> keywordList = Arrays.asList(keywordsArr);
		List<ArticlePublishedDTO> articleList = null;
		if(!ncsEnable) {
			articleList = helpArticleService.findPublishedArticleByPublishTypeAndKeywordList(
				publishType.getIntValue(), keywordList, limit, offset);
		} else {
			List<Long> idList = helpContentNCSService.searchArticlePublished(publishType.getIntValue(), keywords, offset, limit);
			articleList = helpArticleService.getArticlePublishedByIdList(idList, null);
			articleList = helpContentNCSService.sortArticle(idList, articleList);
		}
		if (CollectionUtils.isEmpty(articleList)) {
			return result;
		}
		List<HelpArticleVO> articleVOList = new ArrayList<>();
		for (ArticlePublishedDTO article : articleList) {
			articleVOList.add(new HelpArticleVO(article));
		}
		BaseJsonListResultVO listResult = new BaseJsonListResultVO();
		listResult.setList(articleVOList);
		listResult.setTotal(helpArticleService.countPublishedArticleByPublishTypeAndKeywordList(
				publishType.getIntValue(), keywordList));
		result.setResult(listResult);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.HelpCenterFacade#getHelpCenterLeftNav()
	 */
	@Override
	public BaseJsonVO getHelpCenterLeftNav() {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		BaseJsonListResultVO listResult = new BaseJsonListResultVO();
		List<HelpContentCategoryDTO> categoryList = helpContentCategoryService.getHelpContentCategoryList();
		if (CollectionUtils.isEmpty(categoryList)) {
			return result;
		}
		List<HelpContentCategoryVO> parentCategoryVOList = new ArrayList<>();
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
		listResult.setList(parentCategoryVOList);
		result.setResult(listResult);
		return result;
	}

}
