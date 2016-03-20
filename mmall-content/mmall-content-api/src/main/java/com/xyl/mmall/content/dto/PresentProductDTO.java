/**
 * 
 */
package com.xyl.mmall.content.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.meta.PresentProduct;

/**
 * @author hzlihui2014
 *
 */
public class PresentProductDTO extends PresentProduct {

	private static final long serialVersionUID = 1L;

	public PresentProductDTO() {
	}

	public PresentProductDTO(PresentProduct obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
