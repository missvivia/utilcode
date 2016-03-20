/**
 * 
 */
package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PoRetrunOrderQueryParamDTO;
import com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO;
import com.xyl.mmall.oms.dto.ReturnPoOrderFormDTO;
import com.xyl.mmall.oms.dto.ReturnPoOrderFormSkuDTO;
import com.xyl.mmall.oms.dto.WarehouseReturnDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.enums.ReturnType;

/**
 * 商家po退货服务，根据一退、二退、三退明细创建商家退货单等
 * 
 * @author hzzengchengyuan
 *
 */
public interface PoReturnService {
	/**
	 * 将指定的退货单创建退供单
	 * 
	 * @param warehouseReturns
	 * @return
	 */
	List<ReturnPoOrderFormDTO> createPoReturnOrder(PoRetrunSkuQueryParamDTO params);

	/**
	 * @param id
	 * @param supplierId
	 * @return
	 */
	ReturnPoOrderFormDTO getPoReturnOrder(long id, long supplierId);

	/**
	 * 根据退货单id查询退货单
	 * 
	 * @param id
	 * @return
	 */
	ReturnPoOrderFormDTO getPoReturnOrder(long id);

	/**
	 * 获取po下所有的退货单数据
	 * 
	 * @param poOrderId
	 * @return
	 */
	List<ReturnPoOrderFormDTO> getPoReturnOrderByPoOrderId(long poOrderId);

	/**
	 * 商家确认退货，只能确认未确认的退供单。只有在{@link PoReturnOrderState#NEW}
	 * 状态下确认成功才返回true，其他情况返回false。 DTO对象中需包含商家退货地址信息
	 * 
	 * @param form
	 * @return
	 */
	boolean confirmPoReturnOrder(ReturnPoOrderFormDTO form);

	/**
	 * 商家确认收到货物，只有在仓库发出货物之后才可确认收货，以防误操作
	 * 
	 * @param returnOrderId
	 * @param supplierId
	 * @return
	 */
	boolean okPoReturnOrder(long returnOrderId, long supplierId);

	/**
	 * 当wms更新出库单状态时的回调
	 * 
	 * @param shipOutOrder
	 * @return
	 */
	boolean onWmsUpdateShipOutOrder(WMSShipOutOrderUpdateDTO shipOutOrder);

	/**
	 * 根据组合条件查询退货单，如果条件为null，则忽略。不为空的条件按取查询交集
	 * 
	 * @param params
	 * @return 返回查询结果对象，如果没有符合条件的结果则返回total=0的空PageableList对象
	 */
	PageableList<ReturnPoOrderFormDTO> queryReturnOrder(PoRetrunOrderQueryParamDTO params);

	/**
	 * 根据组合条件查询退货单，如果条件为null，则忽略。不为空的条件按取查询交集
	 * 
	 * @param params
	 * @return 返回查询结果对象，如果没有符合条件的结果则返回total=0的空PageableList对象
	 */
	PageableList<ReturnPoOrderFormSkuDTO> queryReturnOrderSku(PoRetrunSkuQueryParamDTO params);

	/**
	 * 根据组合条件查询退货单，如果条件为null，则忽略。不为空的条件按取查询交集
	 * 
	 * @param params
	 * @return 返回查询结果对象，如果没有符合条件的结果则返回total=0的空PageableList对象
	 */
	PageableList<WarehouseReturnDTO> queryReturnSku(PoRetrunSkuQueryParamDTO params);
	
	PageableList<Long> queryPoIdFromReturnSku(long limit, long offset);

	/**
	 * 统计符合条件的未生成退供单的退货需求数量
	 * 
	 * @param params
	 * @return
	 */
	long countPoReturnSku(PoRetrunSkuQueryParamDTO params);

	/**
	 * 统计符合条件的退供单数量
	 * 
	 * @param params
	 * @return
	 */
	long countPoReturnOrder(PoRetrunOrderQueryParamDTO params);
	
	/**
	 * 统计符合条件的已生成退供单的退货需求数量
	 * 
	 * @param params
	 * @return
	 */
	long countPoReturnOrderSku(PoRetrunSkuQueryParamDTO params);

	/**
	 * 统计已创建退货单且符合条件的PO退货中中所有商品的sku总数
	 * @param params
	 * @return
	 */
	long countPoReturnOrderSkuCount(PoRetrunSkuQueryParamDTO params);
	
	/**
	 * 根据poOrderId获取N退数量，N=1,2,3
	 * 
	 * @param poOrderId
	 * @param type
	 * @return
	 */
	int getNReturnSkuCountByPoOrderId(String poOrderId, ReturnType type);
	
	/**
	 * 生成二退数据
	 * @return
	 */
	boolean gen2Return();

}
