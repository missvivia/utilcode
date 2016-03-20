/**
 * 
 */
package com.xyl.mmall.content.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.meta.ArticlePublished;

/**
 * @author lihui
 *
 */
public class ArticlePublishedDTO extends ArticlePublished {

	private static final long serialVersionUID = 1L;

	public ArticlePublishedDTO() {
	}

	public ArticlePublishedDTO(ArticlePublished obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
