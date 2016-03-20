package com.xyl.mmall.cms.facade;

import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.cms.facade.param.FrontTimeRangeSearchTypeParam;
import com.xyl.mmall.cms.vo.order.ReturnPackageBriefInfoListVO;
import com.xyl.mmall.cms.vo.order.ReturnPackageDetailInfoVO;
import com.xyl.mmall.cms.vo.order.ReturnSkuInfoListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.ReturnPackageQueryCategoryListVO;
import com.xyl.mmall.order.param.PassReturnOperationParam;
import com.xyl.mmall.order.param.ReturnOperationParam;

/**
 * 订单管理：退货退款管理 (客服)
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午9:11:18
 *
 */
public interface OrderPackageReturnFacade {

	/**
	 * 搜索列表
	 * 
	 * @return
	 */
	public ReturnPackageQueryCategoryListVO cmsReturnOrderKFSearchTypeList();
	
	/**
	 * 按时间查询
	 * 
	 * @param param
	 * @return
	 */
	public ReturnPackageBriefInfoListVO getReturnPackageBriefInfoListByTime(FrontTimeRangeSearchTypeParam param);
	
	/**
	 * 按条件搜索
	 * 
	 * @param param
	 * @return
	 */
	public ReturnPackageBriefInfoListVO getReturnPackageBriefInfoListBySearch(FrontTimeRangeSearchTypeParam param);
	
	/**
	 * 退货详情
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 */
	public ReturnPackageDetailInfoVO getReturnDetailInfo(long retId, long userId);
	
	/**
	 * 退货商品信息
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 */
	public ReturnSkuInfoListVO getReturnSkuInfoList(long retId, long userId);
	
	/**
	 * 退款
	 * 
	 * @param param
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg passReturn(PassReturnOperationParam param);
	
	/**
	 * 拒绝退款
	 * 
	 * @param param
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg rejectReturn(ReturnOperationParam param);
	
	/**
	 * 撤销拒绝
	 * 
	 * @param param
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg cancelReject(ReturnOperationParam param);
	
	/**
	 * 取消退货申请（异常件拒绝退货后，支持客服取消退货申请）
	 * 
	 * @param retPkgId
	 * @param userId
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg cancelReturnByKf(long retPkgId, long userId);
	
	/**
	 * 财务确认到付订单“已退款”
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 *     RetArg._ReturnPackageDTO
	 *     RetArg.Boolean
	 *     RetArg.String
	 */
	public RetArg cwConfirmReturn(long retId, long userId);
	
	/**
	 * 财务确认到付订单“已退款”
	 * 
	 * @param batchParam: key->retId, long->userId
	 * @return
	 *     RetArg.Boolean
	 *     RetArg.String
	 *     RetArg.Map<Long, Boolean>
	 */
	public RetArg cwBatchConfirmReturn(Map<Long, Long> batchParam);
}
