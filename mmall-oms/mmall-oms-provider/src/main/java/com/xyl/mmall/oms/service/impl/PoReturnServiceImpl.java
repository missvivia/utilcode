/**
 * 
 */
package com.xyl.mmall.oms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.IReThrowException;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.OmsSkuDao;
import com.xyl.mmall.oms.dao.ReturnPoOrderFormDao;
import com.xyl.mmall.oms.dao.ReturnPoOrderFormSkuDao;
import com.xyl.mmall.oms.dao.WarehouseReturnDao;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PersonContactInfoDTO;
import com.xyl.mmall.oms.dto.PoRetrunOrderQueryParamDTO;
import com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO;
import com.xyl.mmall.oms.dto.ReturnPoOrderFormDTO;
import com.xyl.mmall.oms.dto.ReturnPoOrderFormSkuDTO;
import com.xyl.mmall.oms.dto.WarehouseReturnDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSResponseDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.enums.WMSShipOutOrderState;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.OmsSku;
import com.xyl.mmall.oms.meta.PoOrderForm;
import com.xyl.mmall.oms.meta.ReturnPoOrderForm;
import com.xyl.mmall.oms.meta.ReturnPoOrderFormSku;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.meta.WarehouseReturn;
import com.xyl.mmall.oms.service.PoOrderService;
import com.xyl.mmall.oms.service.PoReturnService;
import com.xyl.mmall.oms.service.WarehouseReturnService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 *
 */
@Service("poReturnService")
public class PoReturnServiceImpl implements PoReturnService {

	@Autowired
	private WarehouseReturnDao warehouseReturnDao;

	@Autowired
	private ReturnPoOrderFormSkuDao returnPoOrderFormSkuDao;

	@Autowired
	private ReturnPoOrderFormDao returnPoOrderFormDao;

	@Autowired
	private WarehouseAdapterBridge warehouseAdapterBridge;

	@Autowired
	private OmsSkuDao omsSkuDao;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private PoOrderService poOrderService;

	@Autowired
	private WarehouseReturnService warehouseReturnService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#createPoReturnOrder()
	 */
	@Override
	public List<ReturnPoOrderFormDTO> createPoReturnOrder(PoRetrunSkuQueryParamDTO params) {
		// 1. 获取退货明细
		PageableList<WarehouseReturn> pageleList = warehouseReturnDao.querReturn(params);
		List<WarehouseReturn> returnForms = pageleList == null ? null : pageleList.getList();
		Map<String, List<WarehouseReturn>> poReturnMap = new HashMap<String, List<WarehouseReturn>>();
		List<ReturnPoOrderFormDTO> poOrderForms = new ArrayList<ReturnPoOrderFormDTO>();
		if (returnForms == null) {
			return poOrderForms;
		}
		// 2. 根据退货明细的"供应商+仓库"为KEY进行归类
		for (WarehouseReturn wReturn : returnForms) {
			List<WarehouseReturn> returnOrderLists = null;
			long supplierId = wReturn.getSupplierId();
			long warehouseId = wReturn.getWarehouseId();
			String key = "" + supplierId + warehouseId;
			if (!poReturnMap.containsKey(key)) {
				returnOrderLists = new ArrayList<WarehouseReturn>();
				poReturnMap.put(key, returnOrderLists);
			} else {
				returnOrderLists = poReturnMap.get(key);
			}
			returnOrderLists.add(wReturn);
		}
		// 3.将同一仓库的同一商家的退货商品生成一张退供单，并保存到db（增加ReturnPoOrderForm表数据且删除原WarehouseReturn表数据）
		for (String key : poReturnMap.keySet()) {
			List<WarehouseReturn> returnOrderLists = poReturnMap.get(key);
			// 3.1 从同类po退货列表中拿出第一个数据(同类数据的退供单基本信息一样滴)填充退供单的基本信息
			WarehouseReturn temp = returnOrderLists.get(0);
			ReturnPoOrderFormDTO returnPoOrderFormDTO = new ReturnPoOrderFormDTO();
			returnPoOrderFormDTO.setSupplierId(temp.getSupplierId());
			returnPoOrderFormDTO.setWarehouseId(temp.getWarehouseId());
			returnPoOrderFormDTO.setState(PoReturnOrderState.NEW);
			returnPoOrderFormDTO.setUpdateTime(System.currentTimeMillis());
			returnPoOrderFormDTO.setCreateTime(System.currentTimeMillis());
			returnPoOrderFormDTO.setPoOrderId(Long.parseLong(temp.getPoOrderId()));
			// 3.2 将同类po的每一个退货明细(包含一退、二退、三退)添加到该退供单
			int allCount = 0;
			for (WarehouseReturn returnOrder : returnOrderLists) {
				ReturnPoOrderFormSkuDTO poOrderSkuDTO = toReturnPoOrderFormSkuDTO(returnOrder);
				poOrderSkuDTO.setPoReturnOrderId(returnPoOrderFormDTO.getPoReturnOrderId());
				poOrderSkuDTO.setState(PoReturnOrderState.NEW);
				returnPoOrderFormDTO.addSkuDetail(poOrderSkuDTO);
				allCount += returnOrder.getCount();
			}
			returnPoOrderFormDTO.setCount(allCount);
			// 3.3 保存退供单
			saveReturnPoOrderForm(returnPoOrderFormDTO);
			poOrderForms.add(returnPoOrderFormDTO);
		}
		return poOrderForms;
	}

	private ReturnPoOrderFormSkuDTO toReturnPoOrderFormSkuDTO(WarehouseReturn returnOrder) {
		ReturnPoOrderFormSkuDTO returnPoOrderFormSkuDTO = new ReturnPoOrderFormSkuDTO(returnOrder);
		// 因为反射将原来WarehouseReturn的主键id映射到ReturnPoOrderFormSkuDTO对象里，这里重置一下
		returnPoOrderFormSkuDTO.setId(0);
		returnPoOrderFormSkuDTO.setWarehouseReturnId(returnOrder.getId());
		return returnPoOrderFormSkuDTO;
	}

	@Transaction
	private void saveReturnPoOrderForm(ReturnPoOrderFormDTO form) {
		ReturnPoOrderForm temp = this.returnPoOrderFormDao.addObject(form);
		if (temp != null && form.getSkuDetails() != null) {
			for (ReturnPoOrderFormSkuDTO sku : form.getSkuDetails()) {
				sku.setPoReturnOrderId(temp.getPoReturnOrderId());
				this.returnPoOrderFormSkuDao.addObject(sku);
				this.warehouseReturnDao.deleteById(sku.getWarehouseReturnId());
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#getPoReturnOrder(long)
	 */
	@Override
	public ReturnPoOrderFormDTO getPoReturnOrder(long id, long supplierId) {
		ReturnPoOrderForm dbOrder = this.returnPoOrderFormDao.getByIdAndSupplierId(id, supplierId);
		if (dbOrder == null) {
			return null;
		}
		ReturnPoOrderFormDTO returnPoOrder = new ReturnPoOrderFormDTO(dbOrder);
		List<ReturnPoOrderFormSku> skus = returnPoOrderFormSkuDao
				.getListByPoReturnOrderId(dbOrder.getPoReturnOrderId());
		List<ReturnPoOrderFormSkuDTO> skuDTOs = ReflectUtil.convertList(ReturnPoOrderFormSkuDTO.class, skus, false);
		returnPoOrder.setSkuDetails(skuDTOs);
		return returnPoOrder;
	}

	@Override
	public ReturnPoOrderFormDTO getPoReturnOrder(long id) {
		ReturnPoOrderForm dbOrder = this.returnPoOrderFormDao.getObjectById(id);
		if (dbOrder == null) {
			return null;
		}
		ReturnPoOrderFormDTO returnPoOrder = new ReturnPoOrderFormDTO(dbOrder);
		List<ReturnPoOrderFormSku> skus = returnPoOrderFormSkuDao
				.getListByPoReturnOrderId(dbOrder.getPoReturnOrderId());
		List<ReturnPoOrderFormSkuDTO> skuDTOs = ReflectUtil.convertList(ReturnPoOrderFormSkuDTO.class, skus, false);
		returnPoOrder.setSkuDetails(skuDTOs);
		return returnPoOrder;
	}

	@Override
	public List<ReturnPoOrderFormDTO> getPoReturnOrderByPoOrderId(long poOrderId) {
		PoRetrunOrderQueryParamDTO params = new PoRetrunOrderQueryParamDTO();
		params.setPoOrderId(poOrderId);
		params.unPageable();
		return queryReturnOrder(params).getList();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#confirmPoReturnOrder()
	 */
	@Override
	@Transaction
	public boolean confirmPoReturnOrder(ReturnPoOrderFormDTO form) {
		ReturnPoOrderForm returnPoOrder = getPoReturnOrder(form.getPoReturnOrderId(), form.getSupplierId());
		// 1. 更新退供单的状态为已确认，如果更新失败则返回
		if (returnPoOrder.getState() != PoReturnOrderState.NEW) {
			return false;
		}
		PersonContactInfoDTO personContact = form.getReturnPerson();
		if (personContact == null || personContact.getName() == null || personContact.getAddress() == null) {
			throw new ServiceException("退货人信息为空或不全，无法生成退供单.");
		}
		boolean isSucess = false;
		returnPoOrder.setState(PoReturnOrderState.CONFIRM);
		returnPoOrder.setUpdateTime(System.currentTimeMillis());
		returnPoOrder.setReceiverAddress(personContact.getFullAddress());
		isSucess = returnPoOrderFormDao.update(PoReturnOrderState.NEW, returnPoOrder);
		isSucess &= returnPoOrderFormSkuDao.updateState(returnPoOrder.getPoReturnOrderId(), PoReturnOrderState.NEW,
				PoReturnOrderState.CONFIRM);
		if (!isSucess) {
			return false;
		}
		// 2. 组织发送数据并通知wms发货
		WMSShipOutOrderDTO shipOutOrder = new WMSShipOutOrderDTO();
		WarehouseForm warehouse = warehouseService.getWarehouseById(returnPoOrder.getWarehouseId());
		shipOutOrder.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
		shipOutOrder.setWarehouseCode(warehouse.getWarehouseCode());
		shipOutOrder.setOrderId(String.valueOf(returnPoOrder.getPoReturnOrderId()));
		shipOutOrder.setReceiverName(personContact.getName());
		shipOutOrder.setReceiverPhone(personContact.getPhone());
		shipOutOrder.setReceiverMobile(personContact.getMobile());
		shipOutOrder.setReceiverProvince(personContact.getProvince());
		shipOutOrder.setReceiverCity(personContact.getCity());
		shipOutOrder.setReceiverDistrict(personContact.getDistrict());
		shipOutOrder.setReceiverAddress(personContact.getAddress());
		shipOutOrder.setReceiverPostCode(personContact.getPostcode());
		if (form.getSkuDetails() != null) {
			for (ReturnPoOrderFormSkuDTO sku : form.getSkuDetails()) {
				OmsSku omsSku = omsSkuDao.getObjectById(sku.getSkuId());
				WMSSkuDetailDTO skuDetail = new WMSSkuDetailDTO();
				skuDetail.setSkuId(sku.getSkuId());
				skuDetail.setName(sku.getProductName());
				skuDetail.setCount(sku.getCount());
				skuDetail.setNormalCount(sku.getNormalCount());
				skuDetail.setDefectiveCount(sku.getDefectiveCount());
				skuDetail.setArtNo(omsSku.getBarCode());
				skuDetail.setColor(omsSku.getColorName());
				skuDetail.setSize(omsSku.getSize());
				shipOutOrder.addSkuDetail(skuDetail);
			}
		}
		WMSResponseDTO response = warehouseAdapterBridge.shipOut(shipOutOrder);

		// 3. 如果通知失败，抛出异常，回滚事务
		if (!response.isSucess()) {
			ServiceException exception;
			if (response.isFailure()) {
				exception = new ServiceException(response.getMessage(), response.getException());
			} else {
				exception = new ServiceException(response.getMessage());
			}
			throw exception;
		}
		return response.isSucess();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#onWmsUpdateShipOutOrder(com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO)
	 */
	@Override
	@Transaction
	public boolean onWmsUpdateShipOutOrder(WMSShipOutOrderUpdateDTO shipOutOrder) {
		if (shipOutOrder.getState() == WMSShipOutOrderState.DONE) {
			try {
				return doConfirmWmsShipOutOrder(shipOutOrder);
			} catch (Exception e) {
				if (e instanceof IReThrowException) {
					throw e;
				} else {
					throw new ServiceException(e);
				}
			}
		} else {
			return true;
		}
	}

	private boolean doConfirmWmsShipOutOrder(WMSShipOutOrderUpdateDTO shipOutOrder) {
		// 1. 基本检查
		ReturnPoOrderFormDTO returnOrder = getPoReturnOrder(Long.parseLong(shipOutOrder.getOrderId()));
		if (returnOrder == null) {
			throw new WarehouseCallerException("订单号[%s]有误，不存在该退供单.", shipOutOrder.getOrderId());
		}
		if (shipOutOrder.getCount() == 0) {
			throw new WarehouseCallerException("订单号[%s]包裹中没有商品.", shipOutOrder.getOrderId());
		}
		if (returnOrder.getState().getIntValue() < PoReturnOrderState.CONFIRM.getIntValue()) {
			throw new WarehouseCallerException("订单号[%s]还未确认，不能发货.", shipOutOrder.getOrderId());
		}
		if (returnOrder.getCount() != shipOutOrder.getCount()) {
			throw new WarehouseCallerException("退货单[%s]退货数量不准确，应为:%s，却为:%s", shipOutOrder.getOrderId(),
					returnOrder.getCount(), shipOutOrder.getCount());
		}
		if (returnOrder.getState().getIntValue() >= PoReturnOrderState.SHIPPED.getIntValue()) {
			return true;
		}
		boolean isSuccess = true;
		// 2. 更新sku信息
		for (WMSSkuDetailDTO wmsSku : shipOutOrder.getSkuDetails()) {
			ReturnPoOrderFormSku sku = new ReturnPoOrderFormSku();
			sku.setSkuId(wmsSku.getSkuId());
			sku.setPoReturnOrderId(returnOrder.getPoReturnOrderId());
			sku.setShipTime(shipOutOrder.getOperaterTime());
			sku.setRealCount(wmsSku.getCount());
			sku.setRealDefectiveCount(wmsSku.getDefectiveCount());
			sku.setRealNormalCount(wmsSku.getNormalCount());
			sku.setState(PoReturnOrderState.SHIPPED);
			isSuccess &= returnPoOrderFormSkuDao.updatePoReturnOrderSku(sku);
		}
		PoReturnOrderState oldState = returnOrder.getState();
		// 3. 更新退供单
		returnOrder.setExpressCompany(shipOutOrder.getLogisticCode());
		returnOrder.setExpressPhone(shipOutOrder.getLogisticPhone());
		returnOrder.setShipBoxQTY(shipOutOrder.getBoxCount());
		returnOrder.setVolume((long) shipOutOrder.getVolume());
		returnOrder.setWeight((long) shipOutOrder.getWeight());
		returnOrder.setShipTime(shipOutOrder.getOperaterTime());
		returnOrder.setRealCount(shipOutOrder.getCount());
		returnOrder.setState(PoReturnOrderState.SHIPPED);
		returnOrder.setUpdateTime(System.currentTimeMillis());
		isSuccess &= returnPoOrderFormDao.update(oldState, returnOrder);
		if (!isSuccess) {
			throw new ServiceException("doConfirmWmsShipOutOrder fail.");
		}
		return isSuccess;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#okPoReturnOrder(java.lang.String)
	 */
	public boolean okPoReturnOrder(long returnOrderId, long supplierId) {
		ReturnPoOrderForm returnPoOrder = getPoReturnOrder(returnOrderId, supplierId);
		if (returnPoOrder.getState() != PoReturnOrderState.SHIPPED) {
			return false;
		}
		returnPoOrder.setState(PoReturnOrderState.RECEIPTED);
		return this.returnPoOrderFormDao.updateState(returnPoOrder.getPoReturnOrderId(), PoReturnOrderState.RECEIPTED,
				PoReturnOrderState.SHIPPED)
				&& this.returnPoOrderFormSkuDao.updateState(returnPoOrder.getPoReturnOrderId(),
						PoReturnOrderState.SHIPPED, PoReturnOrderState.RECEIPTED);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#countPoReturnSku(com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO)
	 */
	@Override
	public long countPoReturnSku(PoRetrunSkuQueryParamDTO params) {
		return warehouseReturnDao.countByParams(params);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#countPoReturnOrder(com.xyl.mmall.oms.dto.PoRetrunOrderQueryParamDTO)
	 */
	public long countPoReturnOrder(PoRetrunOrderQueryParamDTO params) {
		return returnPoOrderFormDao.countByParams(params);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#countPoReturnOrderSku(com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO)
	 */
	@Override
	public long countPoReturnOrderSku(PoRetrunSkuQueryParamDTO params) {
		return returnPoOrderFormSkuDao.countByParams(params);
	}

	@Override
	public long countPoReturnOrderSkuCount(PoRetrunSkuQueryParamDTO params) {
		return returnPoOrderFormSkuDao.countSkuCountByParams(params);
	}

	/**
	 * 根据组合条件查询退货单，如果条件为null，则忽略。不为空的条件按取查询交集
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public PageableList<ReturnPoOrderFormDTO> queryReturnOrder(PoRetrunOrderQueryParamDTO params) {
		PageableList<ReturnPoOrderForm> returnOrder = returnPoOrderFormDao.querReturn(params);
		if (returnOrder == null) {
			return new PageableList<ReturnPoOrderFormDTO>();
		}
		List<ReturnPoOrderFormDTO> data = ReflectUtil.convertList(ReturnPoOrderFormDTO.class, returnOrder.getList(),
				false);
		for (ReturnPoOrderFormDTO returnPoOrder : data) {
			List<ReturnPoOrderFormSku> skus = returnPoOrderFormSkuDao.getListByPoReturnOrderId(returnPoOrder
					.getPoReturnOrderId());
			List<ReturnPoOrderFormSkuDTO> skuDTOs = ReflectUtil.convertList(ReturnPoOrderFormSkuDTO.class, skus, false);
			returnPoOrder.setSkuDetails(skuDTOs);
		}
		return new PageableList<ReturnPoOrderFormDTO>(data, returnOrder.getLimit(), returnOrder.getOffset(),
				returnOrder.getTotal());
	}

	@Override
	public PageableList<ReturnPoOrderFormSkuDTO> queryReturnOrderSku(PoRetrunSkuQueryParamDTO params) {
		PageableList<ReturnPoOrderFormSku> returnOrder = returnPoOrderFormSkuDao.querReturnSku(params);
		if (returnOrder == null) {
			return new PageableList<ReturnPoOrderFormSkuDTO>();
		}
		List<ReturnPoOrderFormSkuDTO> data = ReflectUtil.convertList(ReturnPoOrderFormSkuDTO.class,
				returnOrder.getList(), false);
		return new PageableList<ReturnPoOrderFormSkuDTO>(data, returnOrder.getLimit(), returnOrder.getOffset(),
				returnOrder.getTotal());
	}

	public PageableList<WarehouseReturnDTO> queryReturnSku(PoRetrunSkuQueryParamDTO params) {
		PageableList<WarehouseReturn> wReturns = warehouseReturnDao.querReturn(params);
		if (wReturns == null) {
			return new PageableList<WarehouseReturnDTO>();
		}
		List<WarehouseReturnDTO> data = ReflectUtil.convertList(WarehouseReturnDTO.class, wReturns.getList(), false);
		return new PageableList<WarehouseReturnDTO>(data, wReturns.getLimit(), wReturns.getOffset(),
				wReturns.getTotal());
	}

	public PageableList<Long> queryPoIdFromReturnSku(long limit, long offset) {
		return warehouseReturnDao.queryPoIdFromReturnSku(limit, offset);
	}

	@Override
	public int getNReturnSkuCountByPoOrderId(String poOrderId, ReturnType type) {
		return returnPoOrderFormSkuDao.getTotalNReturnOfPoOrderId(poOrderId, type);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoReturnService#gen2Return()
	 */
	@Override
	public boolean gen2Return() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long nowZeroMilisec = cal.getTimeInMillis();
		List<PoOrderForm> orders = poOrderService.getNotGen2ReturnAfterPoEnd(OmsConstants.MILISEC_4DAY,
				nowZeroMilisec);
		if (orders != null) {
			for (PoOrderForm o : orders) {
				doOnePo2Return(o);
			}
		}
		return true;
	}

	@Transaction
	private void doOnePo2Return(PoOrderForm poOrder) {
		boolean isSucc = true;
		if (warehouseReturnService.generate2WarehouseReturn(Long.parseLong(poOrder.getPoOrderId()))) {
			isSucc &= poOrderService.addPoOrderGen2ReturnCommand(Long.parseLong(poOrder.getPoOrderId()));
		}
		if (!isSucc) {
			throw new ServiceException("doOnePo2Return [" + poOrder.getPoOrderId() + "] fail.");
		}
	}

}
