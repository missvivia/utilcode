/**
 * JIT管理Facade定义
 */
package com.xyl.mmall.backend.facade;

import java.util.List;

import com.xyl.mmall.backend.vo.FreightCodVO;
import com.xyl.mmall.backend.vo.FreightReverseVO;
import com.xyl.mmall.backend.vo.FreightUserReturnVO;
import com.xyl.mmall.backend.vo.FreightVO;
import com.xyl.mmall.backend.vo.PoOrderStatisticVo;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.backend.vo.PoReturnQueryParamVO;
import com.xyl.mmall.backend.vo.PoReturnSkuVO;
import com.xyl.mmall.backend.vo.PoStatisticListVo;
import com.xyl.mmall.backend.vo.ScheduleVo;
import com.xyl.mmall.backend.vo.WebPKSearchForm;
import com.xyl.mmall.backend.vo.WebPoSearchForm;
import com.xyl.mmall.backend.vo.WebPoSkuSearchForm;
import com.xyl.mmall.oms.dto.BusinessPhoneDTO;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PickOrderBatchDTO;
import com.xyl.mmall.oms.dto.PickOrderDTO;
import com.xyl.mmall.oms.dto.PickSkuDTO;
import com.xyl.mmall.oms.dto.PoOrderDTO;
import com.xyl.mmall.oms.dto.PoOrderReportFormDTO;
import com.xyl.mmall.oms.dto.PoSkuDetailCountDTO;
import com.xyl.mmall.oms.dto.RejectPackageDTO;
import com.xyl.mmall.oms.dto.ReturnOrderFormDTO;
import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.dto.ShipSkuDTO;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.meta.BusinessPhoneForm;
import com.xyl.mmall.oms.meta.BusinessPhoneLogForm;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.PickOrderForm;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;

/**
 * @author hzzengdan
 * @date 2014-09-18
 * 
 */
public interface JITSupplyManagerFacade {

	/**
	 * 获取po单列表
	 */
	public List<PoOrderDTO> getPoOrderList(WebPoSearchForm searchVo, long supplierId);

	/**
	 * 获取po单各种统计数据
	 */
	public List<PoStatisticListVo> getPoStatistic(WebPoSkuSearchForm searchVo);

	/**
	 * 获取po单统计数据
	 */
	public List<PoOrderStatisticVo> getPoOrderStatistic(long poorderId);

	/**
	 * 获取po单统计报表
	 */
	public List<PoOrderReportFormDTO> getPoReportList(WebPoSearchForm searchVo, long supplierId);

	/**
	 * 通过PickOrderId获取单个拣货单信息
	 */
	public PickOrderDTO getPickOrderByPkId(String pickId, long supplierId);

	/**
	 * 获取拣货单sku详情
	 */
	public List<PickSkuDTO> getPickSkuInfo(String pickOrderId, long supplierId);

	/**
	 * 获取发货单sku详情
	 */
	public List<ShipSkuDTO> getShipOrderSkuList(String shipOrderId);

	/**
	 * 通过发货单Id获得单条发货单
	 */
	public ShipOrderDTO getShipOrder(String shipOrderId, long supplierId);

	/**
	 * 更新PickOrder
	 * 
	 * @param pickOrderDTO
	 * @return
	 */
	boolean updatePickOrder(PickOrderDTO pickOrderDTO);

	/**
	 * 保存列表
	 * 
	 * @param list
	 * @return
	 */
	boolean saveShipSkuList(List<ShipSkuDTO> list, String shipOrderId, JITFlagType jitFlagType);

	boolean updateShipSkuItem(ShipSkuDTO shipSkuDTO);

	boolean addShipSkuItem(ShipSkuItemForm itemForm);

	public PoSkuDetailCountDTO getPoOrderDetail(String poOrderId);

	/**
	 * 
	 * @param businessAccount
	 * @return
	 */
	public List<BusinessPhoneDTO> getBusinessPhoneFormByBusinessAccount(String businessAccount);

	/**
	 * 
	 * @param businessPhone
	 * @return
	 */
	public BusinessPhoneForm addBusinessPhoneForm(BusinessPhoneForm businessPhone);

	/**
	 * 
	 * @param businessPhone
	 * @return
	 */
	public boolean updateBusinessPhoneForm(BusinessPhoneForm businessPhone);

	/**
	 * 
	 * @param businessPhoneLog
	 * @return
	 */
	public BusinessPhoneLogForm addBusinessPhoneLogForm(BusinessPhoneLogForm businessPhoneLog);

	/**
	 * 
	 * @param businessAccount
	 * @return
	 */
	public List<BusinessPhoneLogForm> getBusinessPhoneLogForm(String businessAccount);

	/**
	 * 新建拣货单
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<PickOrderForm> addPickOrderDtoForPoOrderId(String poOrderId);

	/**
	 * 保存一个订单信息
	 * 
	 * @param orderDTO
	 * @return
	 */
	public boolean savePoOrderForm(OrderFormDTO orderDTO);

	/**
	 * 保存一个退货订单
	 * 
	 * @param ReturnFormDTO
	 * @return
	 */
	public boolean saveReturnOrderForm(ReturnPackageDTO returnOrderFormDTO);

	/**
	 * 仓库确认收货信息
	 * 
	 * @param wMSShipOrderUpdateDTO
	 * @return
	 */
	public boolean warehouseStockInConfirm(WMSShipOrderUpdateDTO wMSShipOrderUpdateDTO);

	/**
	 * 根据组合条件(站点、商家账号、PO单编号、档期开始时间范围、退货单类型)查询退货单(未确认创建退供单)，如果条件为null，则忽略。
	 * 不为空的条件按取查询交集
	 * 
	 * @param params
	 * @return
	 */
	public PageableList<PoReturnSkuVO> queryPoReturnSku(PoReturnQueryParamVO params);

	/**
	 * 将符合指定条件的退货单创建退供单
	 * 
	 * @param warehouseReturns
	 */
	@Deprecated
	public void createPoReturnOrder(PoReturnQueryParamVO params);

	/**
	 * 
	 * @param poId
	 * @return -1 表示创建失败, 其他数字表示创建退供单的张数
	 */
	public int ceatePoReturnOrderByPoId(long poId);

	/**
	 * 根据条件组合查询退供单，如果条件为null，则忽略。不为空的条件按取查询交集
	 * 
	 * @param params
	 * @return
	 */
	public PageableList<PoReturnOrderVO> queryPoReturnOrder(PoReturnQueryParamVO params);

	public PageableList<ScheduleVo> queryScheduleVo(PoReturnQueryParamVO params);

	public PageableList<PoReturnOrderVO> queryPoReturnOrderFormBackend(PoReturnQueryParamVO params);

	/**
	 * 商家确认退供单
	 * 
	 * @param returnOrderId
	 */
	public boolean confirmPoReturnOrder(long returnOrderId, long supplierId);

	/**
	 * 商家确认收到货
	 * 
	 * @param returnOrderId
	 * @param supplierId
	 * @return
	 */
	public boolean okPoReturnOrder(long returnOrderId, long supplierId);

	/**
	 * 获取退供单详情
	 * 
	 * @param formId
	 * @return
	 */
	public PoReturnOrderVO getReturnPoOrderForm(long formId);

	/**
	 * @param formId
	 * @param supplierId
	 * @return
	 */
	public PoReturnOrderVO getPoReturnOrderByIdAndSupplierId(long poReturnOrderId, long supplierId);

	/**
	 * 
	 * @param supplierId
	 * @return
	 */
	public List<ShipOrderForm> getShipOrderBySupplierId(long supplierId);

	/**
	 * 
	 * @param poOrderId
	 * @return
	 */
	public List<PoReturnOrderVO> getReturnVoByPoOrderId(String poOrderId);

	/**
	 * 
	 * @param searchVo
	 * @param ddbParam
	 * @return
	 */
	public List<PickOrderBatchDTO> getPickOrderBatchDTO(Long supplierId, WebPKSearchForm searchVo);

	/**
	 * 
	 * @param businessAccount
	 * @param phone
	 * @return
	 */
	public BusinessPhoneForm getBusinessPhone(String businessAccount, String phone);

	/**
	 * 获取所有仓库
	 * 
	 * @return
	 */
	public WarehouseDTO[] getAllWarehouse();

	/**
	 * @param param
	 * @return
	 */
	List<FreightVO> queryFreight(String expressCompany, long warehouseId, long startDate, long endDate);

	/**
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FreightCodVO> queryFreightCod(String expressCompany, long warehouseId, long startDate, long endDate);

	/**
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FreightReverseVO> queryFreightReverse(String expressCompany, long warehouseId, long startDate, long endDate);

	/**
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FreightUserReturnVO> queryFreightUserReturn(String expressCompany, long warehouseId, long startDate,
			long endDate);

	boolean genTwoReturnJob();

	List<ShipOrderForm> queryShipOrderFormByTime(long startTime, long endTime);

	List<OmsOrderForm> getOmsOrderFormListByTimeRange(long startTime, long endTime);

	List<ReturnOrderFormDTO> queryReturnOrderForm(OmsReturnOrderFormState state);

	List<RejectPackageDTO> queryRejectPackageByCreateTime(long startTime, long endTime);
}
