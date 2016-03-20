package com.xyl.mmall.mainsite.facade;

import com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam;
import com.xyl.mmall.mainsite.facade.param._FrontReturnExpInfoParam;
import com.xyl.mmall.mainsite.vo.order.ReturnApplyVO;
import com.xyl.mmall.mainsite.vo.order.ReturnExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.ReturnOrderSkuInfoListVO;
import com.xyl.mmall.mainsite.vo.order.ReturnPriceVO;
import com.xyl.mmall.mainsite.vo.order.ReturnStatusVO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.PackageReturnJudgement;

/**
 * 退货
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午2:44:07
 *
 */
public interface ReturnPackageFacade {
	
	/**
	 * 根据用户id、包裹id查询退货信息
	 * 
	 * @param ordPkgId
	 * @return
	 */
	public ReturnPackageDTO queryReturnPackageByOrderPackageId(long ordPkgId);
	
	/**
	 * 根据用户id、包裹id查询退货信息
	 * 
	 * @param ordPkgId
	 * @param userId
	 * @return
	 */
	public ReturnPackageDTO queryReturnPackageByOrderPackageId(long ordPkgId, long userId);

	/**
	 * 获取包裹的退货列表
	 * 
	 * @param ordPkgId
	 * @param userId
	 * @return
	 */
	public ReturnOrderSkuInfoListVO getReturnOrderSkuVOList(long ordPkgId);
	
	/**
	 * 获得包裹的退换货状态
	 * 
	 * @param ordPkgDTO
	 * @return
	 */
	public PackageReturnJudgement getPackageReturnJudgement(OrderPackageSimpleDTO ordPkgDTO) ;

	/**
	 * 退全部商品
	 * 
	 * @param param
	 * @return
	 */
	public ReturnPriceVO returnAllOrdSku(_FrontReturnApplyParam param);
	
	/**
	 * 添加退货商品：默认数量
	 * 
	 * @param param
	 * @return
	 */
	public ReturnPriceVO addRetOrdSkuDefault(_FrontReturnApplyParam param);

	/**
	 * 添加退货商品：增加选中数量
	 * 
	 * @param param
	 * @return
	 */
	public ReturnPriceVO increaseRetOrdSku(_FrontReturnApplyParam param);

	/**
	 * 取消单个退货
	 * 
	 * @param param
	 * @return
	 */
	public ReturnPriceVO removeRetOrdSku(_FrontReturnApplyParam param);

	/**
	 * 添加退货商品：减少选中数量
	 * 
	 * @param param
	 * @return
	 */
	public ReturnPriceVO decreaseRetOrdSku(_FrontReturnApplyParam param);

	/**
	 * 提交申请
	 * 
	 * @param ordPkgId
	 * @return
	 */
	public ReturnApplyVO applyReturn(_FrontReturnApplyParam param);
	
	/**
	 * 读取申请信息
	 * 
	 * @param ordPkgId
	 * @return
	 */
	public ReturnApplyVO getApply(long ordPkgId);
	
	/**
	 * 提交申请：填写地址信息
	 * 
	 * @param param
	 * @return
	 */
	public ReturnExpInfoVO completeApplyWithExpInfo(_FrontReturnExpInfoParam param);
	
	/**
	 * 退货状态：退货中
	 * 
	 * @param ordPkgId
	 * @return
	 */
	public ReturnExpInfoVO getReturning(long ordPkgId);
	
	/**
	 * 退货状态：成功/失败
	 * 
	 * @param ordPkgId
	 * @return
	 */
	public ReturnStatusVO returnStatus(long ordPkgId);
	
	/**
	 * 取消退货
	 * 
	 * @param ordPkgId
	 * @return
	 */
	public boolean cancelReturn(long ordPkgId);
	
}
