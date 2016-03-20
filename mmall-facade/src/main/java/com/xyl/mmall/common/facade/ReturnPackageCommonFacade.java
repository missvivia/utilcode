package com.xyl.mmall.common.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;

/**
 * 退货公共Facade
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月27日 上午9:03:11
 * 
 */
public interface ReturnPackageCommonFacade {

	/**
	 * 包裹退货时，判断是否属于整个订单退货的情形
	 * 
	 * @param ordPkgDTO
	 * @param param
	 * @return
	 */
	public boolean totalOrderApplyedReturn(OrderPackageSimpleDTO ordPkgDTO, _FrontReturnApplyParam param);
	
	/**
	 * 包裹退货时，判断是否属于整个订单退货的情形
	 * 
	 * @param ordPkgDTO
	 * @return
	 */
	public boolean totalOrderApplyedReturn(OrderPackageSimpleDTO ordPkgDTO);
	
	/**
	 * 判断是否属于整个订单退货的情形
	 * 
	 * @param ordPkgDTO
	 * @param param
	 * @return
	 */
	public boolean totalOrderConfirmedReturn(OrderPackageSimpleDTO ordPkgDTO);
	
	/**
	 * 获取PO列表
	 * 
	 * @param ordPkgDTO
	 * @return
	 */
	public List<PODTO> getPOList(OrderPackageSimpleDTO ordPkgDTO);
	
	/**
	 * 最早结束PO的结束时间
	 * 
	 * @param ordPkgDTO
	 * @return
	 */
	public long getEarliestPOEndTime(OrderPackageSimpleDTO ordPkgDTO);

	/**
	 * 获得可以申请退货的最迟时间点<br>
	 * CASE1: 完全发货后7天<BR>
	 * CASE2: 订单里最早结束PO的20天后
	 * 
	 * @param orderDTO
	 * @return -1: 参数错误<br>
	 *         -2: 包裹未完全发货
	 */
	public long getDeadlineOfApplyReturn(OrderFormDTO orderDTO);

	/**
	 * 退货仓库地址
	 * 
	 * @param ordPkgDTO
	 * @return
	 */
	public WarehouseForm getReturnWarehouseForm(OrderPackageSimpleDTO ordPkgDTO);
	
	/**
	 * 订单中的所有包裹是否向客服展示“开放退货入口”
	 * 
	 * @param ordForm
	 * @return
	 */
	public Map<Long, Boolean> canReopenReturnShowToKF(OrderFormDTO ordForm);
}
