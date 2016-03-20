package com.xyl.mmall.cms.facade;

import com.xyl.mmall.cms.vo.order.BlacklistAddressVO;
import com.xyl.mmall.cms.vo.order.BlacklistUserVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.BlacklistQueryCategoryListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.BlacklistQueryCategoryListVO.BlacklistSearchType;

/**
 * 黑名单管理
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public interface OrderBlacklistManageFacade {

	/**
	 * 获取查询列表
	 * 
	 * @return
	 */
	public BlacklistQueryCategoryListVO getBlacklistQueryTypeList();
	
	/**
	 * 查询用户黑名单
	 * 
	 * @param bt
	 * @param value
	 * @param limit
	 * @param offset
	 * @return
	 */
	public BlacklistUserVO queryUserBlacklist(BlacklistSearchType bt, String value, int limit, int offset);
	
	/**
	 * 查询地址黑名单
	 * 
	 * @param bt
	 * @param value
	 * @param limit
	 * @param offset
	 * @return
	 */
	public BlacklistAddressVO queryAddressBlackList(BlacklistSearchType bt, String value, int limit, int offset);
	
	/**
	 * 移出用户黑名单
	 * 
	 * @param userId
	 * @return
	 */
	public boolean removeFromBlacklistUser(long userId);
	
	/**
	 * 移出地址黑名单
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	public boolean removeFromBlacklistAddress(long id, long userId);
}
