/**
 * 
 */
package com.xyl.mmall.content.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.meta.LotteryGift;

/**
 * @author hzlihui2014
 *
 */
public class LotteryGiftDTO extends LotteryGift {

	private static final long serialVersionUID = 1L;

	public LotteryGiftDTO() {
	}

	public LotteryGiftDTO(LotteryGift obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
