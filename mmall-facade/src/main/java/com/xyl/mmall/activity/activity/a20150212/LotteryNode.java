package com.xyl.mmall.activity.activity.a20150212;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.activity.fw.AbstractAcvitiyNode;
import com.xyl.mmall.activity.fw.ActivityNode;
import com.xyl.mmall.activity.fw.ActivityNodeException;
import com.xyl.mmall.activity.fw.Node;
import com.xyl.mmall.content.dto.LotteryGiftDTO;
import com.xyl.mmall.content.enums.GiftType;
import com.xyl.mmall.content.service.OnlineActivityService;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.UserProfileService;

/**
 * Lottery.
 * 
 * @author hzzhanghui
 *
 */
@Node("lotteryNode")
public class LotteryNode extends AbstractAcvitiyNode implements ActivityNode {

	@Autowired
	private OnlineActivityService onlineActivityService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Override
	protected Object doAction(String activityId, Map<String, Object> params) throws ActivityNodeException {
		System.out.println("Begin execute LotteryNode: " + activityId + ";" + params);
		System.out.println("End execute LotteryNode!!!");
		
		// do lottery
		GiftType giftType = lottery();
		
		// save user lottery result
		long now = System.currentTimeMillis();
		long userId = SecurityContextUtils.getUserId();
		String userName = SecurityContextUtils.getUserName();
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(userId);
		String userMobile = userProfileDTO.getMobile();
		
		LotteryGiftDTO gift = new LotteryGiftDTO();
		gift.setHitTime(now);
		gift.setType(giftType);
		gift.setUserId(userId);
		gift.setUserMobile(userMobile);
		gift.setUserName(userName);
		gift.setUpdateTime(now);
		gift.setName(giftType.getDesc());
		gift.setImage("");
		gift.setUnit("");
		
		onlineActivityService.saveGiftHit(gift);
		
		return gift;
	}
	
	private GiftType lottery() {
		Random r = new Random();
		int i = r.nextInt(4);
		if (i == 0) {
			return GiftType.FIVE;
		}
		if (i == 1) {
			return GiftType.TEN;
		}
		if (i == 2) {
			return GiftType.TWENTY;
		}
		if (i == 3) {
			return GiftType.THIRTY;
		}
		return GiftType.FIVE;
	}
}
