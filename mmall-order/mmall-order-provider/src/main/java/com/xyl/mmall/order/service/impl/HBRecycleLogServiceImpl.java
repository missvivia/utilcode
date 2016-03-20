package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dao.HBRecycleLogDao;
import com.xyl.mmall.order.dto.HBRecycleLogDTO;
import com.xyl.mmall.order.meta.HBRecycleLog;
import com.xyl.mmall.order.service.HBRecycleLogService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
@Service("hbRecycleLogService")
public class HBRecycleLogServiceImpl implements HBRecycleLogService {
	
	@Autowired
	private  HBRecycleLogDao hbRecycleLogDao;

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.HBRecycleLogService#getReturnedButNotRecycledObjects(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg getReturnedButNotRecycledObjects(long minRetPkgId, DDBParam ddbParam) {
		if(null == ddbParam) {
			ddbParam = DDBParam.genParamX(100);
			ddbParam.setOrderColumn("retPkgId");
			ddbParam.setAsc(true);
		}
		List<HBRecycleLogDTO> logDTOList = new ArrayList<HBRecycleLogDTO>();
		List<HBRecycleLog> logList = hbRecycleLogDao.getReturnedButNotRecycledObjects(minRetPkgId, ddbParam);
		if(!CollectionUtil.isEmptyOfList(logList)) {
			for(HBRecycleLog log : logList) {
				logDTOList.add(new HBRecycleLogDTO(log));
			}
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, logDTOList);
		RetArgUtil.put(retArg, ddbParam);
		return retArg;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.service.HBRecycleLogService#recycleHb(com.xyl.mmall.order.meta.HBRecycleLog)
	 */
	@Override
	public boolean recycleHb(HBRecycleLog hbRecycleLog) {
		return hbRecycleLogDao.recycleHb(hbRecycleLog);
	}

}
