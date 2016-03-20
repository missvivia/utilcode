package com.xyl.mmall.mainsite.facade;

import java.util.List;

import com.xyl.mmall.mainsite.facade.param.FrontReturnApplyParam;
import com.xyl.mmall.mainsite.facade.param.FrontReturnExpInfoParam;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnApplyVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnOrderSkuInfoListVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnPriceVO;
import com.xyl.mmall.mainsite.vo.order.DeprecatedReturnStatusVO;
import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.DeprecatedOrderReturnJudgement;

/**
 * 退货
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午2:44:07
 *
 */
@Deprecated
public interface ReturnFacade {
	
	/**
	 * 根据用户id、订单id查询退货信息
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public List<DeprecatedReturnFormDTO> queryReturnFormByUserIdAndOrderId(long userId, long orderId);

	/**
	 * 获取订单的退货列表
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public DeprecatedReturnOrderSkuInfoListVO getReturnOrderSkuVOList(long orderId, long userId);
	
	/**
	 * 获得订单的退换货状态
	 * 
	 * @param orderFormDTO
	 * @return
	 */
	public DeprecatedOrderReturnJudgement getOrderReturnJudgement(OrderFormDTO orderFormDTO) ;

	/**
	 * 退全部商品
	 * 
	 * @param param
	 * @return
	 */
	public DeprecatedReturnPriceVO returnAllOrdSku(FrontReturnApplyParam param);
	
	/**
	 * 添加退货商品：默认数量
	 * 
	 * @param param
	 * @return
	 */
	public DeprecatedReturnPriceVO addRetOrdSkuDefault(FrontReturnApplyParam param);

	/**
	 * 添加退货商品：增加选中数量
	 * 
	 * @param param
	 * @return
	 */
	public DeprecatedReturnPriceVO increaseRetOrdSku(FrontReturnApplyParam param);

	/**
	 * 取消单个退货
	 * 
	 * @param param
	 * @return
	 */
	public DeprecatedReturnPriceVO removeRetOrdSku(FrontReturnApplyParam param);

	/**
	 * 添加退货商品：减少选中数量
	 * 
	 * @param param
	 * @return
	 */
	public DeprecatedReturnPriceVO decreaseRetOrdSku(FrontReturnApplyParam param);

	/**
	 * 提交申请
	 * 
	 * @param orderId
	 * @return
	 */
	public DeprecatedReturnApplyVO applyReturn(FrontReturnApplyParam param);
	
	/**
	 * 读取申请信息
	 * 
	 * @param orderId
	 * @return
	 */
	public DeprecatedReturnApplyVO getApply(long orderId);
	
	/**
	 * 提交申请：填写地址信息
	 * 
	 * @param param
	 * @return
	 */
	public DeprecatedReturnExpInfoVO completeApplyWithExpInfo(FrontReturnExpInfoParam param);
	
	/**
	 * 退货状态：退货中
	 * 
	 * @param orderId
	 * @return
	 */
	public DeprecatedReturnExpInfoVO getReturning(long orderId);
	
	/**
	 * 退货状态：成功/失败
	 * 
	 * @param orderId
	 * @return
	 */
	public DeprecatedReturnStatusVO returnStatus(long orderId);
	
	/**
	 * 取消退货
	 * 
	 * @param orderId
	 * @return
	 */
	public boolean cancelReturn(long orderId);
	
}
