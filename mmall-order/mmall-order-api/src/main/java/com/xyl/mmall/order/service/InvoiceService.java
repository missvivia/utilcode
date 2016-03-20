package com.xyl.mmall.order.service;

import java.util.Collection;
import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;

/**
 * @author dingmingliang
 * 
 */
public interface InvoiceService {

	/**
	 * 根据供应商Id和poId,获得待开票的数量
	 * 
	 * @param supplierId
	 * @return
	 */
	public int getInvoiceInOrdSupplierCountOfInit(long supplierId);

	/**
	 * 根据发票的抬头查询
	 * 
	 * @param supplierId
	 * @param title
	 * @param orderTimeRange
	 * @param state
	 * @param param
	 * @return RetArg.ArrayList: List(InvoiceInOrdSupplierDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg getInvoiceInOrdSupplierByTitle(long supplierId, String title, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param);

	/**
	 * 查询商家的订单发票信息
	 * 
	 * @param supplierId
	 * @param orderTimeRange
	 * @param state
	 * @param param
	 * @return RetArg.ArrayList: List(InvoiceInOrdSupplierDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg getInvoiceInOrdSupplierByTimeRangeAndState(long supplierId, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param);

	/**
	 * 查询订单发票信息
	 * 
	 * @param minOrderId
	 *            起始的订单Id
	 * @param state
	 * @param orderTimeRange
	 * @param limit
	 * @return 结果按照订单Id升序
	 */
	public List<InvoiceInOrdDTO> getInvoiceInOrdByOrderTimeRange(long minOrderId, InvoiceInOrdState state,
			long[] orderTimeRange, int limit);

	/**
	 * 查询订单发票信息
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public InvoiceInOrdDTO getInvoiceInOrdByOrderId(long orderId, long userId);
	
	/**
	 * 查询订单发票信息
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<InvoiceInOrdDTO> getInvoiceInOrdByOrderIdColl(Collection<Long> orderIdColl, long userId);

	/**
	 * 查询供应商订单发票信息
	 * 
	 * @param orderId
	 * @param userId
	 * @param param
	 * @return
	 */
	public List<InvoiceInOrdSupplierDTO> getInvoiceInOrdSupplierByOrderId(long orderId, long userId, DDBParam param);

	/**
	 * 查询供应商订单发票信息
	 * 
	 * @param orderId
	 * @return
	 */
	public List<InvoiceInOrdSupplierDTO> getInvoiceInOrdSupplierByOrderId(long orderId);

	/**
	 * 查询某个订单的发票信息(订单发票+供应商订单发票)
	 * 
	 * @param orderId
	 * @param userId
	 * @return RetArg.List(InvoiceInOrdSupplierDTO)<br>
	 *         RetArg.InvoiceInOrdDTO
	 */
	public RetArg getInvoiceByOrderId(long orderId, long userId);

	/**
	 * 更新发票的快递信息
	 * 
	 * @param obj
	 * @return
	 */
	public boolean updateExpInfoAndState(InvoiceInOrdSupplierDTO obj);

	/**
	 * 添加订单发票信息
	 * 
	 * @param obj
	 * @return
	 */
	public boolean addInvoiceInOrd(InvoiceInOrdDTO obj);
	
	/**
	 * 添加发票信息---商家操作
	 * 
	 * @param obj
	 * @return
	 */
	public boolean addInvoice(InvoiceDTO obj);

	/**
	 * 修改订单发票信息
	 * 
	 * @param obj
	 * @return
	 */
	public boolean updateInvoiceInOrd(InvoiceInOrdDTO obj);

	/**
	 * 批量添加商家订单发票
	 * 
	 * @param objList
	 * @return
	 */
	public boolean addInvoiceInOrdSupplier(List<InvoiceInOrdSupplierDTO> objList);

	/**
	 * 保存发票<br>
	 * 1.更新InvoiceInOrdDTO.state<br>
	 * 2.保存InvoiceInOrdSupplierDTO对象
	 * 
	 * @param invoiceInOrdDTO
	 * @param invoiceInOrdSupplierDTOList
	 * @return
	 */
	public boolean saveInvoice(InvoiceInOrdDTO invoiceInOrdDTO,
			List<InvoiceInOrdSupplierDTO> invoiceInOrdSupplierDTOList);
	
	/**
	 * 更新发票状态---商家
	 * @param obj
	 * @return
	 */
	public boolean updateState(InvoiceDTO obj);
	
	/**
	 * 根据订单ID查发票信息
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<InvoiceDTO> getInvoiceByOrderId(long orderId);
	
}
