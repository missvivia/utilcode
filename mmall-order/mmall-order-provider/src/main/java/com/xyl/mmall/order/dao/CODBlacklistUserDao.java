package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.meta.CODBlacklistUser;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午11:03:37
 *
 */
public interface CODBlacklistUserDao extends AbstractDao<CODBlacklistUser> {

	public List<CODBlacklistUser> queryObjectsByUserIdList(List<Long> userIdList, DDBParam ddbParam);
	
}
