/**
 * 
 */
package com.xyl.mmall.mainsite.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.content.dto.ArticlePublishedDTO;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class HelpArticleVO {

	private ArticlePublishedDTO article;

	public HelpArticleVO(ArticlePublishedDTO article) {
		this.setArticle(article);
	}

	public HelpArticleVO() {
		this.setArticle(new ArticlePublishedDTO());
	}

	/**
	 * @return the article
	 */
	@JsonIgnore
	public ArticlePublishedDTO getArticle() {
		return article;
	}

	/**
	 * @param article
	 *            the article to set
	 */
	public void setArticle(ArticlePublishedDTO article) {
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
