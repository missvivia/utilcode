package com.xyl.mmall.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.CODBlacklistAddress;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午11:03:58
 *
 */
@Repository(value = "CODBlacklistAddressDao")
public class CODBlacklistAddressDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CODBlacklistAddress> 
	implements CODBlacklistAddressDao{

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODBlacklistAddressDao#queryObjectsByUserIdList(java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODBlacklistAddress> queryObjectsByUserIdList(List<Long> userIdList, DDBParam ddbParam) {
		if(CollectionUtil.isEmptyOfList(userIdList)) {
			return new ArrayList<CODBlacklistAddress>();
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "userId", userIdList.toArray());
		return getListByDDBParam(sql.toString(), ddbParam);
	}

}
