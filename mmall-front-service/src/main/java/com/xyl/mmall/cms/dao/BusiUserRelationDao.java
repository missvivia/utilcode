package com.xyl.mmall.cms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.meta.BusiUserRelation;

public interface BusiUserRelationDao extends AbstractDao<BusiUserRelation> {

	/**
	 * 根据商家Id取得指定用户
	 * @param businessId
	 * @return
	 */
	public List<BusiUserRelation> getBusiUserRelationsByBusinessId(long businessId);
	
	/**
	 * 根据userId取得商家指定用户
	 * @param userId
	 * @return
	 */
	public List<BusiUserRelation> getBusiUserRelationsByUserId(long userId);
	
	/**
	 * 根据商家Id删除指定用户
	 * @param businessId
	 * @return
	 */
	public boolean deleteByBusinessId(long businessId);
	
	
	/**
	 * 根据商家Ids批量删除指定用户
	 * @param businessIds
	 * @return
	 */
	public boolean batchDeleteByBusinessIds(List<Long> businessIds);
	
	/**
	 * 获取关系
	 * @param relation
	 * @return
	 */
	public BusiUserRelation getBusiUserRelation(BusiUserRelation relation);
	
	/**
	 * 分页取得商家指定用户
	 * @param businessId
	 * @param userName
	 * @param ddbParam
	 * @return
	 */
	public List<BusiUserRelation> getPageBusiUserRelationBybusinessId(long businessId,String userName,
			DDBParam ddbParam);
}
