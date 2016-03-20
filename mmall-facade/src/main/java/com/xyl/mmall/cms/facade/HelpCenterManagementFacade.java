/**
 * 
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.cms.vo.HelpArticleVO;
import com.xyl.mmall.cms.vo.HelpContentCategoryVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * @author lihui
 *
 */
public interface HelpCenterManagementFacade {

	/**
	 * 根据类别和关键字查找文章。
	 * 
	 * @param publishType
	 *            发布类型
	 * @param categoryId
	 *            文章类别
	 * @param keywords
	 *            关键字
	 * @param offset
	 *            分页位置
	 * @param limit
	 *            分页大小
	 * @return 文章列表
	 */
	BaseJsonVO searchArticleByPublicTypeAndCategoryAndKeywords(int publishType, long categoryId, String keywords,
			int limit, int offset);

	/**
	 * 根据ID获取文章的详情。
	 * 
	 * @param id
	 *            文章ID
	 * @return 文章详情
	 */
	HelpArticleVO getHelpArticleDetail(long id);

	/**
	 * 保存或者更新帮助文章
	 * 
	 * @param article
	 *            文章内容
	 * @param userId
	 *            操作人ID
	 * @param userName
	 *            操作人账号
	 * @return
	 */
	BaseJsonVO saveHelpArticle(HelpArticleVO article, long userId, String userName);

	/**
	 * 保存并发布帮助文章
	 * 
	 * @param article
	 *            文章内容
	 * @param userId
	 *            操作人ID
	 * @param userName
	 *            操作人账号
	 * @return
	 */
	BaseJsonVO saveAndPublishHelpArticle(HelpArticleVO article, long userId, String userName);

	/**
	 * 发布指定文章
	 * 
	 * @param id
	 *            文章ID
	 * @param userId
	 *            操作人ID
	 * @return
	 */
	BaseJsonVO publishHelpArticle(long id, long userId);

	/**
	 * 撤销指定文章
	 * 
	 * @param id
	 *            文章ID
	 * @param userId
	 *            操作人ID
	 * @return
	 */
	BaseJsonVO revokeHelpArticle(long id, long userId);

	/**
	 * 删除指定文章
	 * 
	 * @param id
	 *            文章ID
	 * @param userId
	 *            操作人ID
	 * @return
	 */
	BaseJsonVO deleteHelpArticle(long id, long userId);

	/**
	 * 获取文章的类目
	 * 
	 * @return
	 */
	List<HelpContentCategoryVO> getHelpArticleCategory();
	
	/**
	 * 同步ncs数据
	 */
	void ncssync();

}
