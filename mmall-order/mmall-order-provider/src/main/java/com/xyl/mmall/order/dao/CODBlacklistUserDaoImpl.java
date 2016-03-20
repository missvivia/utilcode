package com.xyl.mmall.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.CODBlacklistUser;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午11:03:53
 *
 */
@Repository(value = "CODBlacklistUserDao")
public class CODBlacklistUserDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CODBlacklistUser> 
	implements CODBlacklistUserDao {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODBlacklistUserDao#queryObjectsByUserIdList(java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODBlacklistUser> queryObjectsByUserIdList(List<Long> userIdList, DDBParam ddbParam) {
		if(CollectionUtil.isEmptyOfList(userIdList)) {
			return new ArrayList<CODBlacklistUser>();
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "userId", userIdList.toArray());
		return getListByDDBParam(sql.toString(), ddbParam);
	}

}
