package com.xyl.mmall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.SalescheduleConfig;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SalescheduleConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("production")
public class LotteryTest {

	static {
		System.setProperty("spring.profiles.active", "production");
	}

	@Resource
	private ScheduleService scheduleService;
	
	@Test
	public void test() throws Exception {
//		List<LotteryGift> giftList = scheduleService.getGiftHitList();
//		
//		List<LotteryGift> firstList = new ArrayList<LotteryGift>();
//		List<LotteryGift> secondList = new ArrayList<LotteryGift>();
//		List<LotteryGift> thirdList = new ArrayList<LotteryGift>();
//		List<LotteryGift> fourthList = new ArrayList<LotteryGift>();
//		
//		for (LotteryGift gift : giftList) {
//			if (gift.getType() == GiftType.FIRST) {
//				firstList.add(gift);
//			} else if (gift.getType() == GiftType.SECOND) {
//				secondList.add(gift);
//			} else if (gift.getType() == GiftType.THIRD) {
//				thirdList.add(gift);
//			} else if (gift.getType() == GiftType.FOURTH) {
//				fourthList.add(gift);
//			}
//		}
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		
//		StringBuilder sb = new StringBuilder();
//		sb.append("一等奖(" + GiftType.FIRST.getDesc() + "): ").append(firstList.size()).append("个").append("\n");
//		sb.append("\t").append("用户ID\t").append("用户账号 \t\t\t").append("中奖时间\n");
//		for (LotteryGift gift : firstList) {
//			sb.append("\t");
//			Calendar c = Calendar.getInstance();
//			c.setTimeInMillis(gift.getUpdateTime());
//			sb.append(gift.getUserId()).append("\t").append(gift.getUserName()).append("\t\t\t").append(sdf.format(c.getTime())).append("\n");
//		}
//		
//		
//		sb.append("\n二等奖(" + GiftType.SECOND.getDesc() + "): ").append(secondList.size()).append("个").append("\n");
//		sb.append("\t").append("用户ID\t").append("用户账号 \t\t\t").append("中奖时间\n");
//		for (LotteryGift gift : secondList) {
//			sb.append("\t");
//			Calendar c = Calendar.getInstance();
//			c.setTimeInMillis(gift.getUpdateTime());
//			sb.append(gift.getUserId()).append("\t").append(gift.getUserName()).append("\t\t\t").append(sdf.format(c.getTime())).append("\n");
//		}
//		
//		
//		sb.append("\n三等奖(" + GiftType.THIRD.getDesc() + "): ").append(thirdList.size()).append("个").append("\n");
//		sb.append("\t").append("用户ID\t").append("用户账号 \t\t\t").append("中奖时间\n");
//		for (LotteryGift gift : thirdList) {
//			sb.append("\t");
//			Calendar c = Calendar.getInstance();
//			c.setTimeInMillis(gift.getUpdateTime());
//			sb.append(gift.getUserId()).append("\t").append(gift.getUserName()).append("\t\t\t").append(sdf.format(c.getTime())).append("\n");
//		}
//		
//		sb.append("\n四等奖(" + GiftType.FOURTH.getDesc() + "): ").append(fourthList.size()).append("个").append("\n");
//		sb.append("\t").append("用户ID\t").append("用户账号 \t\t\t").append("中奖时间\n");
//		for (LotteryGift gift : fourthList) {
//			sb.append("\t");
//			Calendar c = Calendar.getInstance();
//			c.setTimeInMillis(gift.getUpdateTime());
//			sb.append(gift.getUserId()).append("\t").append(gift.getUserName()).append("\t\t\t").append(sdf.format(c.getTime())).append("\n");
//		}
//		
//		System.out.println(sb.toString());
	}
	
	
}
