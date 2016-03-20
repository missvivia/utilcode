package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.meta.CODBlacklistAddress;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午11:03:43
 *
 */
public interface CODBlacklistAddressDao extends AbstractDao<CODBlacklistAddress> {

	public List<CODBlacklistAddress> queryObjectsByUserIdList(List<Long> userIdList, DDBParam ddbParam);
	
}
