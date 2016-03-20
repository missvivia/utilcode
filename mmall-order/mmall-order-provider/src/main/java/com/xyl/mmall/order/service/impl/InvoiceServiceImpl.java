package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.order.dao.InvoiceDao;
import com.xyl.mmall.order.dao.InvoiceInOrdDao;
import com.xyl.mmall.order.dao.InvoiceInOrdSupplierDao;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;
import com.xyl.mmall.order.meta.Invoice;
import com.xyl.mmall.order.meta.InvoiceInOrd;
import com.xyl.mmall.order.meta.InvoiceInOrdSupplier;
import com.xyl.mmall.order.service.InvoiceService;
import com.xyl.mmall.order.service.OrderExpInfoService;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * @author dingmingliang
 * 
 */
@Service("invoiceService")
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	private OrderExpInfoService orderExpInfoService;
	
	@Autowired
	private InvoiceDao invoiceDao;

	@Autowired
	private InvoiceInOrdDao invoiceInOrdDao;

	@Autowired
	private InvoiceInOrdSupplierDao invoiceInOrdSupplierDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#getInvoiceInOrdSupplierCountOfInit(long)
	 */
	public int getInvoiceInOrdSupplierCountOfInit(long supplierId) {
		return invoiceInOrdSupplierDao.getCount(supplierId, InvoiceInOrdSupplierState.INIT);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#getInvoiceInOrdSupplierByTitle(long,
	 *      java.lang.String, long[],
	 *      com.xyl.mmall.order.enums.InvoiceInOrdSupplierState)
	 */
	public RetArg getInvoiceInOrdSupplierByTitle(long supplierId, String title, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param) {
		List<InvoiceInOrdSupplier> invoiceInOrdSupplierList = invoiceInOrdSupplierDao.getListByTitle(supplierId, title,
				orderTimeRange, state, param);
		List<InvoiceInOrdSupplierDTO> invoiceInOrdSupplierDTOList = convertTOInvoiceInOrdSupplierDTOList(invoiceInOrdSupplierList);

		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, invoiceInOrdSupplierDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#getInvoiceInOrdSupplierByTimeRangeAndState(long,
	 *      long[], com.xyl.mmall.order.enums.InvoiceInOrdSupplierState)
	 */
	public RetArg getInvoiceInOrdSupplierByTimeRangeAndState(long supplierId, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param) {
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		List<InvoiceInOrdSupplier> invoiceInOrdSupplierList = invoiceInOrdSupplierDao.getListByOrderTimeRangeAndState(
				supplierId, orderTimeRange, state, param);
		List<InvoiceInOrdSupplierDTO> invoiceInOrdSupplierDTOList = convertTOInvoiceInOrdSupplierDTOList(invoiceInOrdSupplierList);

		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, invoiceInOrdSupplierDTOList);
		RetArgUtil.put(retArg, param);
		return retArg;
	}

	/**
	 * @param invoiceInOrdSupplierList
	 * @return
	 */
	private List<InvoiceInOrdSupplierDTO> convertTOInvoiceInOrdSupplierDTOList(
			List<InvoiceInOrdSupplier> invoiceInOrdSupplierList) {
		List<InvoiceInOrdSupplierDTO> invoiceInOrdSupplierDTOList = new ArrayList<>();
		if (CollectionUtil.isNotEmptyOfCollection(invoiceInOrdSupplierList)) {
			for (InvoiceInOrdSupplier invoiceInOrdSupplier : invoiceInOrdSupplierList) {
				InvoiceInOrdSupplierDTO invoiceInOrdSupplierDTO = convertTOInvoiceInOrdSupplierDTO(invoiceInOrdSupplier);
				invoiceInOrdSupplierDTOList.add(invoiceInOrdSupplierDTO);
			}
		}
		return invoiceInOrdSupplierDTOList;
	}

	/**
	 * InvoiceInOrdSupplier->InvoiceInOrdSupplierDTO
	 * 
	 * @param invoiceInOrdSupplier
	 * @return
	 */
	private InvoiceInOrdSupplierDTO convertTOInvoiceInOrdSupplierDTO(InvoiceInOrdSupplier invoiceInOrdSupplier) {
		long orderId = invoiceInOrdSupplier.getOrderId(), userId = invoiceInOrdSupplier.getUserId();
		InvoiceInOrdSupplierDTO invoiceInOrdSupplierDTO = new InvoiceInOrdSupplierDTO(invoiceInOrdSupplier);

		OrderExpInfoDTO orderExpInfoDTO = orderExpInfoService.queryInfoByUserIdAndOrderId(userId, orderId);
		invoiceInOrdSupplierDTO.setOrderExpInfoDTO(orderExpInfoDTO);

		InvoiceInOrd invoiceInOrd = invoiceInOrdDao.getObjectByIdAndUserId(orderId, userId);
		InvoiceInOrdDTO invoiceInOrdDTO = new InvoiceInOrdDTO(invoiceInOrd);
		invoiceInOrdSupplierDTO.setInvoiceInOrdDTO(invoiceInOrdDTO);

		return invoiceInOrdSupplierDTO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#getInvoiceInOrdByOrderTimeRange(long,
	 *      com.xyl.mmall.order.enums.InvoiceInOrdState, long[], int)
	 */
	public List<InvoiceInOrdDTO> getInvoiceInOrdByOrderTimeRange(long minOrderId, InvoiceInOrdState state,
			long[] orderTimeRange, int limit) {
		DDBParam param = DDBParam.genParamX(limit);
		param = OrderUtil.initDDBParamWithMinOrderId(param);
		List<InvoiceInOrd> objList = invoiceInOrdDao.getInvoiceInOrdByOrderTimeRangeWithMinOrderId(minOrderId, state,
				orderTimeRange, param);
		List<InvoiceInOrdDTO> dtoList = convertToInvoiceInOrdDTOList(objList);
		return dtoList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#getInvoiceInOrdByOrderId(long,
	 *      long)
	 */
	public InvoiceInOrdDTO getInvoiceInOrdByOrderId(long orderId, long userId) {
		InvoiceInOrd obj = new InvoiceInOrd();
		obj.setOrderId(orderId);
		obj.setUserId(userId);
		obj = invoiceInOrdDao.getObjectByPrimaryKeyAndPolicyKey(obj);
		return InvoiceInOrdDTO.getInstance(obj);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#getInvoiceInOrdByOrderIdColl(java.util.Collection,
	 *      long)
	 */
	public List<InvoiceInOrdDTO> getInvoiceInOrdByOrderIdColl(Collection<Long> orderIdColl, long userId) {
		List<InvoiceInOrd> objList = invoiceInOrdDao.getListByOrderIdsAndUserId(userId, orderIdColl);
		return convertToInvoiceInOrdDTOList(objList);
	}

	/**
	 * @param objList
	 * @return
	 */
	private List<InvoiceInOrdDTO> convertToInvoiceInOrdDTOList(List<InvoiceInOrd> objList) {
		if (CollectionUtil.isEmptyOfCollection(objList))
			return null;
		List<InvoiceInOrdDTO> dtoList = new ArrayList<>();
		for (InvoiceInOrd obj : objList) {
			InvoiceInOrdDTO dto = InvoiceInOrdDTO.getInstance(obj);
			CollectionUtil.addOfList(dtoList, dto);
		}
		return dtoList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#
	 *      getInvoiceInOrdSupplierByOrderId(long, long, DDBParam param)
	 */
	@Override
	public List<InvoiceInOrdSupplierDTO> getInvoiceInOrdSupplierByOrderId(long orderId, long userId, DDBParam param) {
		Long supplierId = null;
		return getInvoiceInOrdSupplierDTOList(orderId, userId, supplierId, param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#getInvoiceInOrdSupplierByOrderId(long)
	 */
	public List<InvoiceInOrdSupplierDTO> getInvoiceInOrdSupplierByOrderId(long orderId) {
		List<InvoiceInOrdSupplier> invoiceInOrdSupplierList = invoiceInOrdSupplierDao
				.queryInvoiceByOrderIdAndUserId(orderId);
		List<InvoiceInOrdSupplierDTO> invoiceInOrdSupplierDTOList = convertTOInvoiceInOrdSupplierDTOList(invoiceInOrdSupplierList);
		return invoiceInOrdSupplierDTOList;
	}

	/**
	 * 查询发票信息
	 * 
	 * @param orderId
	 * @param userId
	 * @param supplierId
	 * @param param
	 * @return
	 */
	private List<InvoiceInOrdSupplierDTO> getInvoiceInOrdSupplierDTOList(long orderId, long userId, Long supplierId,
			DDBParam param) {
		List<InvoiceInOrdSupplier> invoiceList = invoiceInOrdSupplierDao.queryInvoiceByOrderIdAndUserId(orderId,
				userId, param);
		if (null == invoiceList) {
			return null;
		}

		List<InvoiceInOrdSupplierDTO> invoiceDTOList = new ArrayList<InvoiceInOrdSupplierDTO>(invoiceList.size());
		for (InvoiceInOrdSupplier invoice : invoiceList) {
			if (invoice == null || (supplierId != null && supplierId != invoice.getSupplierId()))
				continue;
			invoiceDTOList.add(convertTOInvoiceInOrdSupplierDTO(invoice));
		}
		return invoiceDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#getInvoiceByOrderId(long,
	 *      long)
	 */
	public RetArg getInvoiceByOrderId(long orderId, long userId) {
		InvoiceInOrdDTO invoiceInOrdDTO = getInvoiceInOrdByOrderId(orderId, userId);
		if (invoiceInOrdDTO == null)
			return null;

		DDBParam param = null;
		List<InvoiceInOrdSupplierDTO> invoiceInOrdSupplierDTOList = getInvoiceInOrdSupplierByOrderId(orderId, userId,
				param);

		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, invoiceInOrdDTO);
		RetArgUtil.put(retArg, invoiceInOrdSupplierDTOList);
		return retArg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#updateExpInfoAndState(com.xyl.mmall.order.meta.InvoiceInOrdSupplier)
	 */
	public boolean updateExpInfoAndState(InvoiceInOrdSupplierDTO invoiceInOrdSupplier) {
		if (StringUtils.isBlank(invoiceInOrdSupplier.getBarCode())
				|| StringUtils.isBlank(invoiceInOrdSupplier.getExpressCompanyName())) {
			logger.info("updateExpInfoAndState fail, param field is blank!");
			return false;
		}
		invoiceInOrdSupplier.setState(InvoiceInOrdSupplierState.KP_ED);
		return invoiceInOrdSupplierDao.updateExpInfoAndState(invoiceInOrdSupplier);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#addInvoiceInOrd(com.xyl.mmall.order.dto.InvoiceInOrdDTO)
	 */
	@Override
	public boolean addInvoiceInOrd(InvoiceInOrdDTO obj) {
		return (null != invoiceInOrdDao.addObject(obj));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#updateInvoiceInOrd(com.xyl.mmall.order.dto.InvoiceInOrdDTO)
	 */
	@Override
	public boolean updateInvoiceInOrd(InvoiceInOrdDTO obj) {
		return invoiceInOrdDao.updateObjectByKey(obj);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#addInvoiceInOrdSupplier(java.util.List)
	 */
	@Transaction
	public boolean addInvoiceInOrdSupplier(List<InvoiceInOrdSupplierDTO> objList) {
		return invoiceInOrdSupplierDao.addObjects(objList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.InvoiceService#saveInvoice(com.xyl.mmall.order.dto.InvoiceInOrdDTO,
	 *      java.util.List)
	 */
	@Transaction
	public boolean saveInvoice(InvoiceInOrdDTO invoiceInOrdDTO,
			List<InvoiceInOrdSupplierDTO> invoiceInOrdSupplierDTOList) {
		boolean isSucc = true;
		long orderId = invoiceInOrdDTO.getOrderId();
		// 1.更新InvoiceInOrdDTO.state
		isSucc = isSucc && invoiceInOrdDao.updateState(invoiceInOrdDTO);

		// 2.保存InvoiceInOrdSupplierDTO对象
		if (CollectionUtil.isNotEmptyOfCollection(invoiceInOrdSupplierDTOList))
			isSucc = isSucc && invoiceInOrdSupplierDao.addObjects(invoiceInOrdSupplierDTOList);

		if (!isSucc) {
			throw new ServiceNoThrowException("orderId=" + orderId);
		}
		return isSucc;
	}

	@Override
	@Transaction
	public boolean updateState(InvoiceDTO obj) {
		return invoiceDao.updateState(obj);
	}

	@Override
	@Transaction
	public boolean addInvoice(InvoiceDTO obj) {
		return (null != invoiceDao.addObject(obj));
	}

	@Override
	public List<InvoiceDTO> getInvoiceByOrderId(long orderId) {
		List<Invoice> invoices = invoiceDao.queryInvoiceByOrderId(orderId);
		if(CollectionUtil.isEmptyOfList(invoices)){
			return null;
		}
		return ReflectUtil.convertList(InvoiceDTO.class, invoices, false);
	}
}
