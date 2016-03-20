/**
 * 
 */
package com.xyl.mmall.content.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author lihui
 *
 */
@AnnonOfClass(tableName = "Mmall_Content_ArticlePublished", desc = "内容管理的已发布的文章信息")
public class ArticlePublished extends Article implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "原文章ID", notNull = true, policy = true)
	private long orgArticleId;

	/**
	 * @return the orgArticleId
	 */
	public long getOrgArticleId() {
		return orgArticleId;
	}

	/**
	 * @param orgArticleId
	 *            the orgArticleId to set
	 */
	public void setOrgArticleId(long orgArticleId) {
		this.orgArticleId = orgArticleId;
	}

}
