/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.promotion.dao.RedPacketDetailDao;
import com.xyl.mmall.promotion.dao.RedPacketLockDao;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.meta.RedPacketDetail;
import com.xyl.mmall.promotion.meta.RedPacketLock;
import com.xyl.mmall.promotion.meta.UserReceiveRecord;
import com.xyl.mmall.promotion.meta.UserRedPacket;
import com.xyl.mmall.promotion.service.RedPacketDetailService;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.promotion.service.UserReceiveRecordService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.utils.DateUtils;

/**
 * RedPacketDetailServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-10
 * @since 1.0
 */
@Service("redPacketDetailService")
public class RedPacketDetailServiceImpl implements RedPacketDetailService {

	@Autowired
	private RedPacketDetailDao redPacketDetailDao;

	@Autowired
	private UserRedPacketService userRedPacketService;
	
	@Autowired
	private RedPacketLockDao redPacketLockDao;
	
	@Autowired
	private UserReceiveRecordService userReceiveRecordService;
	
	@Autowired
	private RedPacketService redPacketService;

	@Override
	public RedPacketDetail addRedPacketDetail(RedPacketDetail detail) {
		return redPacketDetailDao.addRedPacketDetail(detail);
	}

	@Override
	public RedPacketDetail getRamdomDetailByRedPacketId(long redPacketId, int groupId, Boolean visible) {
		return redPacketDetailDao.getRamdomDetailByRedPacketId(redPacketId, groupId, visible);
	}

	@Override
	@Transaction
	public RedPacketDetail takeRedPacketDetail(long userId, long redPacketId, int groupId) {
		//行级锁
		RedPacketLock lock = new RedPacketLock();
		lock.setRedPacketId(redPacketId);
		lock.setGroupId(groupId);
		lock = redPacketLockDao.getLockByKey(lock);
		if (lock == null) {
			return null;
		}
		
		RedPacketDetail detail = getRamdomDetailByRedPacketId(redPacketId, groupId, Boolean.TRUE);
		// 红包已经领取完
		if (detail == null || detail.getCopies() <= 0) {
			return null;
		}
		
		boolean isSucc = true;
		
		RedPacket redPacket = redPacketService.getRedPacketById(redPacketId);
		if (redPacket == null || !redPacket.isShare() || redPacket.getValidDay() <= 0) {
			return null;
		}
		
		int validDay = redPacket.getValidDay();
		long current = DateUtils.formatDate(new Date(), DateUtils.DATE_FORMAT);
		//减掉1毫秒
		long end = current + validDay * 1000 * 60 * 60 * 24L - 1;
		
		UserRedPacket userRedPacket = new UserRedPacket();
		userRedPacket.setCash(detail.getCash());
		userRedPacket.setRemainCash(detail.getCash());
		userRedPacket.setRedPacketDetailId(detail.getId());
		userRedPacket.setUsedCash(BigDecimal.ZERO);
		userRedPacket.setUserId(userId);
		userRedPacket.setValidStartTime(current);
		userRedPacket.setValidEndTime(end);
		userRedPacket.setVisible(true);

		isSucc = isSucc && userRedPacketService.addUserRedPacket(userRedPacket) != null;
		
		if (!isSucc) {
			throw new ServiceException("绑定用户红包异常");
		}
		
		isSucc = isSucc && redPacketDetailDao.setCopiesMinusOne(detail);
		
		if (!isSucc) {
			throw new ServiceException("更新红包详情异常");
		}
		
		UserReceiveRecord receiveRecord = new UserReceiveRecord();
		
		receiveRecord.setGroupId(groupId);
		receiveRecord.setRedPacketId(redPacketId);
		receiveRecord.setUserId(userId);
		receiveRecord.setCash(detail.getCash());
		
		isSucc = isSucc && userReceiveRecordService.addReceiveRecord(receiveRecord) != null;
		
		if (!isSucc) {
			throw new ServiceException("添加领取记录异常");
		}
		
		return detail;
	}

	@Override
	public RedPacketDetail getDetailById(long redPacketDetailId) {
		return redPacketDetailDao.getDetailById(redPacketDetailId);
	}

	@Override
	public List<RedPacketDetail> getDetailListByPacketId(long redPacketId) {
		return redPacketDetailDao.getDetailListByPacketId(redPacketId);
	}

	@Override
	public boolean deleteDetailById(long id) {
		return redPacketDetailDao.deleteDetailById(id);
	}

	@Override
	public boolean updateRedPacketDetail(RedPacketDetail detail) {
		return redPacketDetailDao.updateRedPacketDetail(detail);
	}
}
