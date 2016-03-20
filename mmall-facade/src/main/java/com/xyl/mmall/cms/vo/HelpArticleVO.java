/**
 * 
 */
package com.xyl.mmall.cms.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.content.dto.ArticleDTO;
import com.xyl.mmall.content.enums.ArticlePublishType;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class HelpArticleVO {

	private ArticleDTO article;

	public HelpArticleVO(ArticleDTO article) {
		this.setArticle(article);
	}

	public HelpArticleVO() {
		this.setArticle(new ArticleDTO());
	}

	/**
	 * @return the article
	 */
	@JsonIgnore
	public ArticleDTO getArticle() {
		return article;
	}

	/**
	 * @param article
	 *            the article to set
	 */
	public void setArticle(ArticleDTO article) {
		this.article = article;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return article.getId();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		article.setId(id);
	}

	/**
	 * @return the categoryId
	 */
	public long getCategoryId() {
		return article.getCategoryId();
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(long categoryId) {
		article.setCategoryId(categoryId);
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return article.getCategoryName();
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return article.getKeywords();
	}

	/**
	 * @param keyWords
	 *            the keyWords to set
	 */
	public void setKeywords(String keywords) {
		article.setKeywords(keywords);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return article.getTitle();
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		article.setTitle(title);
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return article.getContent();
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		article.setContent(content);
	}

	/**
	 * @return the lastSaveTime
	 */
	public long getLastSaveTime() {
		return article.getLastSaveTime();
	}

	/**
	 * @return the publishTime
	 */
	public long getPublishTime() {
		return article.getPublishTime();
	}

	/**
	 * @return the lastModifiedUser
	 */
	public String getLastModifiedUser() {
		return article.getLastModifiedUserName();
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return article.getStatus() == null ? null : article.getStatus().getDesc();
	}

	/**
	 * @return the publishType
	 */
	public int getPublishType() {
		return article.getPublishType().getIntValue();
	}

	/**
	 * @return the publishTypeName
	 */
	public String getPublishTypeName() {
		return article.getPublishType().getDesc();
	}

	/**
	 * @param publishType
	 *            the publishType to set
	 */
	public void setPublishType(int publishType) {
		ArticlePublishType type = ArticlePublishType.getArticlePublishTypeByIntValue(publishType);
		if (type != ArticlePublishType.NULL) {
			article.setPublishType(type);
		}
	}

	/**
	 * @return the parentCategoryId
	 */
	public long getParentCategoryId() {
		return article.getParentCategoryId();
	}

	/**
	 * @return the parentCategoryName
	 */
	public String getParentCategoryName() {
		return article.getParentCategoryName();
	}

}
