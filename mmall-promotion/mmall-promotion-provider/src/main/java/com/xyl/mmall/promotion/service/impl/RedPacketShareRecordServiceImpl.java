/*
 * @(#) 2014-11-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.util.SerialNumberUtil;
import com.xyl.mmall.promotion.dao.RedPacketShareRecordDao;
import com.xyl.mmall.promotion.enums.ShareChannel;
import com.xyl.mmall.promotion.meta.RedPacketShareRecord;
import com.xyl.mmall.promotion.service.RedPacketShareRecordService;

/**
 * RedPacketShareRecordServiceImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-14
 * @since      1.0
 */
@Service("redPacketShareRecordService")
public class RedPacketShareRecordServiceImpl implements RedPacketShareRecordService {
	
	@Autowired
	private RedPacketShareRecordDao redPacketShareRecordDao;
	
	@Override
	public RedPacketShareRecord getByIdAndValue(long redPacketId, long orderId) {
		return redPacketShareRecordDao.getByIdAndValue(redPacketId, orderId);
	}

	@Override
	public boolean addRedPacketShareRecord(RedPacketShareRecord record) {
		//设置id
		record.setId(redPacketShareRecordDao.allocateRecordId());
		return redPacketShareRecordDao.addObject(record) != null;
	}

	@Override
	public RedPacketShareRecord getByTypeAndValue(ShareChannel shareChannel, long orderId) {
		return redPacketShareRecordDao.getByTypeAndValue(shareChannel, orderId);
	}

	@Override
	public RedPacketShareRecord getById(long id) {
		return redPacketShareRecordDao.getObjectById(id);
	}

	@Override
	public String getRedPacketShareId(ShareChannel shareChannel, long orderId) {
		RedPacketShareRecord record = getByTypeAndValue(shareChannel, orderId);
		if (record == null) {
			return null;
		}
		
		return SerialNumberUtil.makeupSerial(record.getId(), record.getStartTime(), new String[]{"/"});
	}

}
