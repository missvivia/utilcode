/**
 * 
 */
package com.xyl.mmall.content.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.meta.SuitcaseGift;

/**
 * @author hzlihui2014
 *
 */
public class SuitcaseGiftDTO extends SuitcaseGift {

	private static final long serialVersionUID = 1L;

	public SuitcaseGiftDTO() {
	}

	public SuitcaseGiftDTO(SuitcaseGift obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
