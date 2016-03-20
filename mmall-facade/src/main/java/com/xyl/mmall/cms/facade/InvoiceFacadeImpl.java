package com.xyl.mmall.cms.facade;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;
import com.xyl.mmall.order.service.InvoiceService;

/**
 * @author dingmingliang
 * 
 */
@Facade
public class InvoiceFacadeImpl implements InvoiceFacade {

	@Autowired
	private InvoiceService invoiceService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.InvoiceFacade#getInvoiceInOrdSupplierByTimeRangeAndState(long,
	 *      long[], com.xyl.mmall.order.enums.InvoiceInOrdSupplierState,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public RetArg getInvoiceInOrdSupplierByTimeRangeAndState(long supplierId, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param) {
		return invoiceService.getInvoiceInOrdSupplierByTimeRangeAndState(supplierId, orderTimeRange, state, param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.InvoiceFacade#getInvoiceInOrdSupplierByTitle(long,
	 *      java.lang.String, long[],
	 *      com.xyl.mmall.order.enums.InvoiceInOrdSupplierState)
	 */
	public RetArg getInvoiceInOrdSupplierByTitle(long supplierId, String title,
			long[] orderTimeRange, InvoiceInOrdSupplierState state, DDBParam param) {
		return invoiceService.getInvoiceInOrdSupplierByTitle(supplierId, title, orderTimeRange, state, param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.InvoiceFacade#getInvoiceInOrdSupplierByOrderId(long,
	 *      long)
	 */
	public List<InvoiceInOrdSupplierDTO> getInvoiceInOrdSupplierByOrderId(long supplierId, long orderId,
			InvoiceInOrdSupplierState state) {
		List<InvoiceInOrdSupplierDTO> dtoList = invoiceService.getInvoiceInOrdSupplierByOrderId(orderId);
		Map<Long, List<InvoiceInOrdSupplierDTO>> dtoMap = CollectionUtil.convertCollToListMap(dtoList, "supplierId");
		List<InvoiceInOrdSupplierDTO> dtoListOfSupplier = CollectionUtil.getValueOfMap(dtoMap, supplierId);
		if (state == null)
			return dtoListOfSupplier;

		Map<InvoiceInOrdSupplierState, List<InvoiceInOrdSupplierDTO>> dtoMapOfSupplier = CollectionUtil
				.convertCollToListMap(dtoList, "state");
		return CollectionUtil.getValueOfMap(dtoMapOfSupplier, state);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.InvoiceFacade#updateExpInfoAndState(com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO)
	 */
	public boolean updateExpInfoAndState(InvoiceInOrdSupplierDTO obj) {
		return invoiceService.updateExpInfoAndState(obj);
	}
}
