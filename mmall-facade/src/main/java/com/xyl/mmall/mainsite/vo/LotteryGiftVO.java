/**
 * 
 */
package com.xyl.mmall.mainsite.vo;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.content.dto.LotteryGiftDTO;

/**
 * @author lihui
 *
 */
public class LotteryGiftVO {

	private LotteryGiftDTO lotteryGiftDTO;

	public LotteryGiftVO(LotteryGiftDTO lotteryGiftDTO) {
		this.lotteryGiftDTO = lotteryGiftDTO;
	}

	public String getUserName() {
		String userName = lotteryGiftDTO.getUserName();
		if (StringUtils.isEmpty(userName)) {
			return "";
		}
		String[] nameArr = StringUtils.split(userName, "@");
		String newPrefix = StringUtils.rightPad(StringUtils.substring(nameArr[0], 0, 2), nameArr[0].length() - 2, "*");
		return nameArr.length > 1 ? newPrefix + "@" +  nameArr[1] : newPrefix;
	}

	public int getType() {
		return lotteryGiftDTO.getType().getIntValue();
	}

	public String getName() {
		return lotteryGiftDTO.getType().getDesc();
	}
}
