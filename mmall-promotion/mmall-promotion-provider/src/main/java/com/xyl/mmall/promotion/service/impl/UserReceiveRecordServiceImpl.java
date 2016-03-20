/*
 * @(#) 2014-11-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.promotion.dao.UserReceiveRecordDao;
import com.xyl.mmall.promotion.meta.UserReceiveRecord;
import com.xyl.mmall.promotion.service.UserReceiveRecordService;

/**
 * UserReceiveRecordServiceImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-19
 * @since      1.0
 */
@Service("userReceiveRecordService")
public class UserReceiveRecordServiceImpl implements UserReceiveRecordService {
	
	@Autowired
	private UserReceiveRecordDao userReceiveRecordDao;

	@Override
	public UserReceiveRecord addReceiveRecord(UserReceiveRecord record) {
		record.setId(userReceiveRecordDao.allocateRecordId());
		return userReceiveRecordDao.addObject(record);
	}

	@Override
	public UserReceiveRecord getUserReceiveRecord(long userId, long redPacketId, long groupId) {
		return userReceiveRecordDao.getUserReceiveRecord(userId, redPacketId, groupId);
	}

	@Override
	public UserReceiveRecord getUserReceiveRecordById(long id) {
		return userReceiveRecordDao.getObjectById(id);
	}
	
}
