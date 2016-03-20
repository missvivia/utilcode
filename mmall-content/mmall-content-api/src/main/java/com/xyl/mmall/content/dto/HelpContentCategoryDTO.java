/**
 * 
 */
package com.xyl.mmall.content.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.meta.HelpContentCategory;

/**
 * @author lihui
 *
 */
public class HelpContentCategoryDTO extends HelpContentCategory {

	private static final long serialVersionUID = 1L;

	public HelpContentCategoryDTO() {
	}

	public HelpContentCategoryDTO(HelpContentCategory obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

}
