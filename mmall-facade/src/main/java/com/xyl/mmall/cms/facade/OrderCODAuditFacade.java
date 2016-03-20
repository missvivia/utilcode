package com.xyl.mmall.cms.facade;

import com.xyl.mmall.cms.facade.param.FrontCODAuditOperationParam;
import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
import com.xyl.mmall.cms.vo.order.CODAuditInfoListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.CODAuditQueryCategoryListVO;

/**
 * 订单管理：到付审核
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午9:11:18
 *
 */
public interface OrderCODAuditFacade {

	/**
	 * 获取订单COD查询类型列表
	 * @return
	 */
	public CODAuditQueryCategoryListVO getCmsCODSearchTypeList();
	
	/**
	 * 根据审核状态和时间查询
	 * @param param
	 * @return
	 */
	public CODAuditInfoListVO getCODInfoListByTime(FrontTimeRangeSearchTypeParam param);
	
	/**
	 * 根据审核状态和搜索关键字查询
	 * @param param
	 * @return
	 */
	public CODAuditInfoListVO getCODInfoListBySearch(FrontTimeRangeSearchTypeParam param);
	
	/**
	 * 到付审核：通过
	 * @param param
	 * @return
	 */
	public boolean passAudit(FrontCODAuditOperationParam param);
	
	/**
	 * 到付审核：拒绝
	 * @param param
	 * @return
	 */
	public boolean rejectAudit(FrontCODAuditOperationParam param);
	
	/**
	 * 到付审核：撤销拒绝
	 * @param param
	 * @return
	 */
	public boolean cancelReject(FrontCODAuditOperationParam param);
	
	/**
	 * 到付审核：用户黑名单
	 * @param param
	 * @return
	 */
	public boolean addUserToBlack(FrontCODAuditOperationParam param);
	
	/**
	 * 到付审核：地址黑名单
	 * @param param
	 * @return
	 */
	public boolean addAddressToBlack(FrontCODAuditOperationParam param);
}
