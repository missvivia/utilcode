package com.xyl.mmall.common.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.param.RetOrdSkuPriceCalParam;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;

/**
 * 退货公共Facade
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月27日 上午9:03:11
 * 
 */
@Deprecated
public interface ReturnCommonFacade {

	/**
	 * 从订单中获取Promotion，填充PromotionDTO.index字段
	 * 
	 * @param ordFormDTO
	 * @return
	 */
	public Map<Long, PromotionDTO> extractPromotion(OrderFormDTO ordFormDTO);
	
	/**
	 * 从订单的所有OrderSku中提取出退货申请中的数据
	 * 
	 * @param retOrdSkuList
	 * @param ordFormDTO
	 * @return
	 */
	public Map<Long, List<PromotionSkuItemDTO>> extractRefindItems(List<RetOrdSkuPriceCalParam> retOrdSkuList,
			OrderFormDTO ordFormDTO);

	/**
	 * 从订单的所有OrderSku中过滤掉退货申请中的数据
	 * 
	 * @param retOrdSkuList
	 * @param ordFormDTO
	 * @return
	 */
	public Map<Long, List<PromotionSkuItemDTO>> filterRefindItems(List<RetOrdSkuPriceCalParam> retOrdSkuList,
			OrderFormDTO ordFormDTO);

	/**
	 * 构造PromotionSkuItemDTO
	 * 
	 * @param ordSkuDTO
	 * @param adjustCount
	 * @return
	 */
	public PromotionSkuItemDTO orderSkuToPromotionSkuItem(OrderSkuDTO ordSkuDTO, int adjustCount);

	/**
	 * 构造FavorCaculateParamDTO
	 * 
	 * @param ordFormDTO
	 * @return
	 */
	public FavorCaculateParamDTO extractFavorCaculateParam(OrderFormDTO ordFormDTO);

	/**
	 * 最早结束PO的结束时间
	 * 
	 * @param ordFormDTO
	 * @return
	 */
	public long getEarliestPOEndTime(OrderFormDTO ordFormDTO);

	/**
	 * 获得可以申请退货的最迟时间点<br>
	 * CASE1: 完全发货后7天<BR>
	 * CASE2: 订单里最早结束PO的20天后
	 * 
	 * @param orderDTO
	 * @return
	 */
	public long getDeadlineOfApplyReturn(OrderFormDTO orderDTO);

	/**
	 * 退货仓库地址
	 * 
	 * @param ordFormDTO
	 * @return
	 */
	public WarehouseForm getReturnWarehouseForm(OrderFormDTO ordFormDTO);
}
