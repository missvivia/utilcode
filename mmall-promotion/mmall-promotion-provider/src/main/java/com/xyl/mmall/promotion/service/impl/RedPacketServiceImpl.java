/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dao.RedPacketDao;
import com.xyl.mmall.promotion.dao.RedPacketLockDao;
import com.xyl.mmall.promotion.dto.RedPacketDTO;
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.enums.DistributeRule;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.meta.RedPacketDetail;
import com.xyl.mmall.promotion.meta.RedPacketLock;
import com.xyl.mmall.promotion.meta.UserRedPacket;
import com.xyl.mmall.promotion.service.RedPacketDetailService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.promotion.service.UserRedPacketService;

/**
 * RedPacketServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Service("redPacketService")
public class RedPacketServiceImpl implements RedPacketService {

	@Autowired
	private RedPacketDao redPacketDao;

	@Autowired
	private RedPacketDetailService redPacketDetailService;

	@Autowired
	private UserRedPacketService userRedPacketService;

	@Autowired
	private RedPacketLockDao redPacketLockDao;
	
	@Autowired
	private RedPacketOrderService redPacketOrderService;

	@Override
	public List<RedPacket> getRedPacketList(long userId, int state, String qvalue, int limit, int offset) {
		return redPacketDao.getRedPacketList(userId, state, qvalue, limit, offset);
	}

	@Override
	public RedPacket addRedPacket(RedPacket redPacket) {
		if (!redPacket.isShare()) {
			redPacket.setCopies(1);
			redPacket.setCount(1);
		}
		RedPacket packet = redPacketDao.addRedPacket(redPacket);
		return packet;
	}

	@Override
	public int getRedPacketCount(long userId, int state, String qvalue) {
		return redPacketDao.getRedPacketCount(userId, state, qvalue);
	}

	@Override
	@Transaction
	public boolean updateRedPacket(RedPacketDTO redPacketDTO) {
		if (redPacketDTO == null) {
			return false;
		}

		boolean status = true;
		// 加锁
		redPacketDao.getLockByKey(redPacketDTO);
		if (redPacketDTO.getAuditState() == StateConstants.PASS) {
			// 分享，构建裂变红包
			if (redPacketDTO.isShare()) {
				// 添加一个红包的锁记录，只有分享才会添加
				if (!redPacketDTO.isProduce()) {
					status = status && genericRedPacketDetail(redPacketDTO);
					if (status) {
						redPacketDTO.setProduce(true);
					}
				}
			} else {
				// 绑定用户
				status = status && binderRedPacketToUser(redPacketDTO);
			}
		}

		if (!status) {
			throw new ServiceException("生成红包详情失败");
		}

		status = status && redPacketDao.updateRedPacket(redPacketDTO);

		if (!status) {
			throw new ServiceException("红包状态更新失败");
		}

		return status;
	}

	/**
	 * 生成红包详情 随机红包使用 ROUND_HALF_DOWN避免最后出现负值
	 * 
	 * @param redPacketDTO
	 * @return
	 */
	private boolean genericRedPacketDetail(RedPacketDTO redPacket) {
		//如果有更新可见状态
		List<RedPacketDetail> details = redPacketDetailService.getDetailListByPacketId(redPacket.getId());
		if (!CollectionUtils.isEmpty(details)) {
			for (RedPacketDetail redPacketDetail : details) {
				redPacketDetail.setVisible(true);
				redPacketDetailService.updateRedPacketDetail(redPacketDetail);
			}
			return true;
		}
		
		BigDecimal cash = redPacket.getCash();
		int count = redPacket.getCount();
		int copies = redPacket.getCopies();
		DistributeRule rule = redPacket.getDistributeRule();

		if (count <= 0) {
			return false;
		}
		for (int i = 0; i < count; i++) {
			RedPacketLock redPacketLock = new RedPacketLock();
			redPacketLock.setGroupId(i + 1);
			redPacketLock.setRedPacketId(redPacket.getId());
			redPacketLockDao.addObject(redPacketLock);
			
			if (rule == DistributeRule.EQUALLY) {
				BigDecimal copyCash = cash.divide(new BigDecimal(copies), 2).setScale(2, BigDecimal.ROUND_HALF_UP);
				RedPacketDetail detail = new RedPacketDetail();
				detail.setRedPacketId(redPacket.getId());
				detail.setCash(copyCash);
				detail.setCopies(copies);
				detail.setGroupId(i + 1);
				detail.setVisible(true);
				detail.setValidStartTime(redPacket.getStartTime());
				detail.setValidEndTime(redPacket.getEndTime());
				redPacketDetailService.addRedPacketDetail(detail);
			} else if (rule == DistributeRule.RANDOM) {
				// 基数，10%，保证每个人领取到平均数的10%
				BigDecimal base = cash.divide(new BigDecimal(copies), 2).multiply(new BigDecimal(0.1))
						.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				// 剩下的金额，用作随机种子
				BigDecimal remainCash = cash.subtract(base.multiply(new BigDecimal(copies)));

				double[] rates = genericRate(copies);
				BigDecimal lastCash = remainCash;
				int index = 0;
				for (double rate : rates) {
					RedPacketDetail detail = new RedPacketDetail();
					detail.setRedPacketId(redPacket.getId());
					BigDecimal copyCash = BigDecimal.ZERO;
					BigDecimal randomCash = BigDecimal.ZERO;
					// 最后一个
					if (index == rates.length - 1) {
						randomCash = lastCash.setScale(2, BigDecimal.ROUND_HALF_DOWN);
					} else {
						// 获取随机值后先不处理
						randomCash = new BigDecimal(rate).multiply(remainCash);
					}
					copyCash = base.add(randomCash).setScale(2, BigDecimal.ROUND_HALF_DOWN);
					detail.setCash(copyCash);
					detail.setCopies(1);
					detail.setValidStartTime(redPacket.getStartTime());
					detail.setValidEndTime(redPacket.getEndTime());
					detail.setVisible(true);
					// 设置分组id
					detail.setGroupId(i + 1);

					lastCash = lastCash.subtract(copyCash.subtract(base));

					redPacketDetailService.addRedPacketDetail(detail);
					index++;
				}
			}
		}
		return true;
	}

	/**
	 * 非分享模式红包处理
	 * 
	 * @param redPacket
	 * @return
	 */
	private boolean binderRedPacketToUser(RedPacketDTO redPacket) {
		List<RedPacketDetail> details = redPacketDetailService.getDetailListByPacketId(redPacket.getId());
		if (!CollectionUtils.isEmpty(details)) {
			for (RedPacketDetail redPacketDetail : details) {
				redPacketDetail.setVisible(true);
				redPacketDetailService.updateRedPacketDetail(redPacketDetail);
				
				List<UserRedPacket> packets = userRedPacketService.getUserRedPacketListByDetailId(redPacketDetail.getId());
				if (CollectionUtils.isEmpty(packets)) {
					continue;
				}
				
				for (UserRedPacket urp : packets) {
					urp.setVisible(true);
					userRedPacketService.updateUserRedPacket(urp);
				}
			}
			return true;
		}
		
		boolean isSucc = true;
		RedPacketDetail detail = new RedPacketDetail();
		detail.setRedPacketId(redPacket.getId());
		detail.setCash(redPacket.getCash());
		detail.setValidStartTime(redPacket.getStartTime());
		detail.setValidEndTime(redPacket.getEndTime());
		detail.setCopies(redPacket.getCopies());
		detail.setVisible(true);
		isSucc = isSucc && redPacketDetailService.addRedPacketDetail(detail) != null;
		if (redPacket.getBinderType() == BinderType.USER_BINDER) {
			List<Long> binderUserList = redPacket.getBinderUserList();
			if (CollectionUtils.isEmpty(binderUserList)) {
				return true;
			}
			for (long userId : binderUserList) {
				UserRedPacket userRedPacket = new UserRedPacket();
				userRedPacket.setCash(redPacket.getCash());
				userRedPacket.setRedPacketDetailId(detail.getId());
				userRedPacket.setRemainCash(redPacket.getCash());
				userRedPacket.setUsedCash(BigDecimal.ZERO);
				userRedPacket.setUserId(userId);
				userRedPacket.setValidStartTime(redPacket.getStartTime());
				userRedPacket.setValidEndTime(redPacket.getEndTime());
				userRedPacket.setVisible(true);
				isSucc = isSucc && userRedPacketService.addUserRedPacket(userRedPacket) != null;
			}
		}
		if (!isSucc) {
			throw new ServiceException("绑定异常");
		}
		return isSucc;
	}

	@Override
	public RedPacket getRedPacketById(long id) {
		return redPacketDao.getRedPacketById(id);
	}

	/**
	 * 生成比例，先随机生成count个小数，然后做归一化处理
	 * 
	 * @param count
	 * @return
	 */
	private double[] genericRate(int count) {
		double[] rates = new double[count];
		double[] seeds = new double[count];
		double total = 0d;

		// 随机生成count个小数
		for (int i = 0; i < count; i++) {
			double random = Math.random();
			rates[i] = random;
			total += random;
		}

		double totalRate = 0D;

		// 归一化
		for (int j = 0; j < count; j++) {
			double seedRate = rates[j] / total;
			if (totalRate + seedRate > 1) {
				seeds[j] = 1 - totalRate;
			} else {
				seeds[j] = seedRate;
			}
			// seeds[j] = seedRate;
			totalRate += seedRate;
		}

		return seeds;
	}

	@Override
	public RedPacket getRandomOneToShare(BigDecimal cash) {
		RedPacket redPacket = redPacketDao.getRandomOneToShare(cash);
		return redPacket;
	}

	@Override
	@Transaction
	public boolean discardRedPacket(RedPacketDTO redPacketDTO) {
		boolean isSucc = true;
		if (redPacketDTO.isShare()) {
			redPacketDTO.setProduce(false);
		}
		isSucc = isSucc && updateRedPacket(redPacketDTO);
		if (!isSucc) {
			return false;
		}

		// 删除detail
		List<RedPacketDetail> details = redPacketDetailService.getDetailListByPacketId(redPacketDTO.getId());
		if (CollectionUtils.isEmpty(details)) {
			return true;
		}

		// 删除用户下
		for (RedPacketDetail detail : details) {
			detail.setVisible(false);
			//删除详情记录
			isSucc = isSucc && redPacketDetailService.updateRedPacketDetail(detail);
			
			List<UserRedPacket> userRedPackets = userRedPacketService.getUserRedPacketListByDetailId(detail.getId());
			if (!CollectionUtils.isEmpty(userRedPackets)) {
				for (UserRedPacket urp : userRedPackets) {
					urp.setVisible(false);
					userRedPacketService.updateUserRedPacket(urp);
				}
			}
		}
		
		redPacketLockDao.deleteByRedPacketId(redPacketDTO.getId());
		
		return isSucc;
	}

	public static void main(String[] args) {
		RedPacketServiceImpl impl = new RedPacketServiceImpl();
		int count = 1000;

		BigDecimal cash = new BigDecimal(10000);
		int copies = 100;
		// 基数，10%，保证每个人领取到平均数的10%
		BigDecimal base = cash.divide(new BigDecimal(copies), 2).multiply(new BigDecimal(0.1))
				.setScale(2, BigDecimal.ROUND_HALF_DOWN);
		System.out.println(base);
		// 剩下的金额，用作随机种子
		BigDecimal remainCash = cash.subtract(base.multiply(new BigDecimal(copies)));
		System.out.println(remainCash);
		for (int i = 0; i < count; i++) {
			double[] rates = impl.genericRate(copies);
			BigDecimal lastCash = remainCash;
			int index = 0;
			BigDecimal sum = BigDecimal.ZERO;
			for (double rate : rates) {
				BigDecimal copyCash = BigDecimal.ZERO;
				BigDecimal randomCash = BigDecimal.ZERO;
				// 最后一个
				if (index == rates.length - 1) {
					randomCash = lastCash.setScale(2, BigDecimal.ROUND_HALF_DOWN);
				} else {
					randomCash = new BigDecimal(rate).multiply(remainCash);
				}
				copyCash = base.add(randomCash).setScale(2, BigDecimal.ROUND_HALF_DOWN);

				lastCash = lastCash.subtract(copyCash.subtract(base));
				// if (index == rates.length - 1) {
				// System.out.println(copyCash);
				// System.out.println(index);
				// }

				sum = sum.add(copyCash);
				// if (copyCash.compareTo(BigDecimal.ZERO) <= 0) {
				// System.out.println(copyCash);
				// System.out.println(index);
				// }

				index++;
			}
			System.out.println(sum);
		}
	}
}
