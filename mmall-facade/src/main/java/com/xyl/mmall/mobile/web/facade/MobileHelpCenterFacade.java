/**
 * 
 */
package com.xyl.mmall.mobile.web.facade;

import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * 帮助中心相关Facade。
 * 
 * @author lihui
 *
 */
public interface MobileHelpCenterFacade {

	/**
	 * 根据帮助类目查找已发布到WEB文章的列表。
	 * 
	 * @param categoryId
	 *            帮助类目的ID
	 * @return 文章列表
	 */
	BaseJsonVO findWebPublishedArticleByCategory(long categoryId);

	/**
	 * 获取帮助中心左边导航条的数据。
	 * 
	 * @return 左导航条数据
	 */
	BaseJsonVO getHelpCenterLeftNav();

	/**
	 * 根据关键字查询发布到WEB的帮助文章。
	 * 
	 * @param keywords
	 *            关键字
	 * @param offset
	 *            分页位置
	 * @param limit
	 *            分页大小
	 * @return 文章列表
	 */
	BaseJsonVO searchWebPublishedArticle(String keywords, int limit, int offset);

	/**
	 * 根据帮助类目查找已发布到APP的文章的列表。
	 * 
	 * @param categoryId
	 *            帮助类目的ID
	 * @return 文章列表
	 */
	BaseJsonVO findAppPublishedArticleByCategory(long categoryId);

	/**
	 * 根据关键字查询发布到APP的帮助文章。
	 * 
	 * @param keywords
	 *            关键字
	 * @param offset
	 *            分页位置
	 * @param limit
	 *            分页大小
	 * @return 文章列表
	 */
	BaseJsonVO searchAppPublishedArticle(String keywords, int limit, int offset);

}
