/*
 * @(#) 2015-1-5
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.mainsite.facade.UserRedPacketFacade;

/**
 * ModifyRpValidityJob.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2015-1-5
 * @since      1.0
 */
@JobPath("/push/rp/modify")
@Service
public class ModifyRpValidityJob extends BaseJob {
	
	@Autowired
	private UserRedPacketFacade userRedPacketFacade;
	
	@Override
	public boolean execute(JobParam param) {
		userRedPacketFacade.modifyRpValidity();
		return true;
	}
}
