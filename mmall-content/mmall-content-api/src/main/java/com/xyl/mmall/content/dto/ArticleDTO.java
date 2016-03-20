/**
 * 
 */
package com.xyl.mmall.content.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.meta.Article;

/**
 * @author lihui
 *
 */
public class ArticleDTO extends Article {

	private static final long serialVersionUID = 1L;

	public ArticleDTO() {
	}

	public ArticleDTO(Article obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

}
