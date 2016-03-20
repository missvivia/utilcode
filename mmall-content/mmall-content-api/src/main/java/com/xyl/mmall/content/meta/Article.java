/**
 * 
 */
package com.xyl.mmall.content.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.content.enums.ArticlePublishType;
import com.xyl.mmall.content.enums.ArticleStatus;

/**
 * @author lihui
 *
 */
@AnnonOfClass(tableName = "Mmall_Content_Article", desc = "内容管理的文章信息")
public class Article implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "文章类别ID", notNull = false)
	private long categoryId;

	@AnnonOfField(desc = "文章父类别ID")
	private long parentCategoryId;

	@AnnonOfField(desc = "文章关键字", notNull = false, safeHtml = true, type = "VARCHAR(32)")
	private String keywords;

	@AnnonOfField(desc = "文章标题", notNull = false, safeHtml = true, type = "VARCHAR(64)")
	private String title;

	@AnnonOfField(desc = "文章内容", notNull = false, type = "TEXT")
	private String content;

	@AnnonOfField(desc = "最后保存时间")
	private long lastSaveTime;

	@AnnonOfField(desc = "发布时间")
	private long publishTime;

	@AnnonOfField(desc = "最后修改人Id")
	private long lastModifiedBy;

	@AnnonOfField(desc = "最后修改人用户名", type = "VARCHAR(32)")
	private String lastModifiedUserName;

	@AnnonOfField(desc = "文章状态(0:未发布,1:已发布,2:已删除)")
	private ArticleStatus status;

	@AnnonOfField(desc = "文章发布类型(0:未发布,1:Web,2:App,3:Web和App)")
	private ArticlePublishType publishType;

	@AnnonOfField(desc = "类别名称", inDB = false)
	private String categoryName;

	@AnnonOfField(desc = "父类别名称", inDB = false)
	private String parentCategoryName;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the categoryId
	 */
	public long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the lastSaveTime
	 */
	public long getLastSaveTime() {
		return lastSaveTime;
	}

	/**
	 * @param lastSaveTime
	 *            the lastSaveTime to set
	 */
	public void setLastSaveTime(long lastSaveTime) {
		this.lastSaveTime = lastSaveTime;
	}

	/**
	 * @return the publishTime
	 */
	public long getPublishTime() {
		return publishTime;
	}

	/**
	 * @param publishTime
	 *            the publishTime to set
	 */
	public void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public long getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy
	 *            the lastModifiedBy to set
	 */
	public void setLastModifiedBy(long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @return the status
	 */
	public ArticleStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(ArticleStatus status) {
		this.status = status;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the parentCategoryName
	 */
	public String getParentCategoryName() {
		return parentCategoryName;
	}

	/**
	 * @param parentCategoryName
	 *            the parentCategoryName to set
	 */
	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	/**
	 * @return the lastModifiedUserName
	 */
	public String getLastModifiedUserName() {
		return lastModifiedUserName;
	}

	/**
	 * @param lastModifiedUserName
	 *            the lastModifiedUserName to set
	 */
	public void setLastModifiedUserName(String lastModifiedUserName) {
		this.lastModifiedUserName = lastModifiedUserName;
	}

	/**
	 * @return the publishType
	 */
	public ArticlePublishType getPublishType() {
		return publishType;
	}

	/**
	 * @param publishType
	 *            the publishType to set
	 */
	public void setPublishType(ArticlePublishType publishType) {
		this.publishType = publishType;
	}

	/**
	 * @return the parentCategoryId
	 */
	public long getParentCategoryId() {
		return parentCategoryId;
	}

	/**
	 * @param parentCategoryId
	 *            the parentCategoryId to set
	 */
	public void setParentCategoryId(long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

}
