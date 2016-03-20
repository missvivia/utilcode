package com.xyl.mmall.oms.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.OmsReturnOrderFormDao;
import com.xyl.mmall.oms.dao.OmsReturnOrderFormSkuDao;
import com.xyl.mmall.oms.dao.PoOrderFormSkuDao;
import com.xyl.mmall.oms.dao.WarehouseReturnDao;
import com.xyl.mmall.oms.dto.ReturnOrderFormDTO;
import com.xyl.mmall.oms.dto.ReturnOrderFormSkuDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.enums.WMSReturnOrderState;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;
import com.xyl.mmall.oms.meta.OmsReturnOrderFormSku;
import com.xyl.mmall.oms.meta.PoOrderFormSku;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.meta.WarehouseReturn;
import com.xyl.mmall.oms.service.OmsReturnOrderFormService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author zb
 *
 */
@Service("omsReturnOrderFormService")
public class OmsReturnOrderFormServiceImpl implements OmsReturnOrderFormService {

	@Autowired
	private OmsReturnOrderFormSkuDao omsReturnOrderFormSkuDao;

	@Autowired
	private OmsReturnOrderFormDao omsReturnOrderFormDao;

	@Autowired
	private WarehouseAdapterBridge warehouseAdapterBridge;

	@Autowired
	private WarehouseReturnDao warehouseReturnDao;

	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private PoOrderFormSkuDao poOrderFormSkuDao;

	@Autowired
	private WarehouseService warehouseService;

	@Override
	@Transaction
	public boolean wmsReturnOrderUpdate(WMSReturnOrderUpdateDTO wmsReturnOrderUpdateDTO) {
		long id = Long.parseLong(wmsReturnOrderUpdateDTO.getOrderId());
		OmsReturnOrderForm omsReturnOrderForm = omsReturnOrderFormDao.getObjectById(id);
		if (omsReturnOrderForm.getOmsReturnOrderFormState() == OmsReturnOrderFormState.CONFIRMED
				|| omsReturnOrderForm.getOmsReturnOrderFormState() == OmsReturnOrderFormState.NOTICE_CONFIRMED)
			return true;

		List<OmsReturnOrderFormSku> omsReturnOrdeFormSkuList = omsReturnOrderFormSkuDao.getListByReturnId(id,
				omsReturnOrderForm.getUserId());
		Map<Long, OmsReturnOrderFormSku> omsReturnOrderFormSkuMap = new LinkedHashMap<Long, OmsReturnOrderFormSku>();
		for (OmsReturnOrderFormSku sku : omsReturnOrdeFormSkuList) {
			omsReturnOrderFormSkuMap.put(sku.getSkuId(), sku);
		}
		WMSReturnOrderState wmsReturnOrderState = wmsReturnOrderUpdateDTO.getState();
		OmsReturnOrderFormState state = OmsReturnOrderFormState.genEnumByStringValue(wmsReturnOrderState.getDesc());

		List<WMSSkuDetailDTO> wmsSkuDetailDTOList = wmsReturnOrderUpdateDTO.getSkuDetails();
		Map<Long, WMSSkuDetailDTO> wmsSkuDetailDTOMap = new LinkedHashMap<>();
		for (WMSSkuDetailDTO wmsSkuDetailDTO : wmsSkuDetailDTOList) {
			wmsSkuDetailDTOMap.put(wmsSkuDetailDTO.getSkuId(), wmsSkuDetailDTO);
		}

		boolean isSucc = true;
		if (state != null) {
			// 1.更新订单状态
			isSucc = omsReturnOrderFormDao.updateOmsReturnOrderFormState(id, state,
					wmsReturnOrderUpdateDTO.getNote() == null ? "" : wmsReturnOrderUpdateDTO.getNote());
		}

		if (wmsReturnOrderState == WMSReturnOrderState.DONE) {
			// 2.生成三退明细
			for (OmsReturnOrderFormSku omsReturnOrderFormSku : omsReturnOrdeFormSkuList) {

				WMSSkuDetailDTO wmsSkuDetailDTO = wmsSkuDetailDTOMap.get(omsReturnOrderFormSku.getSkuId());
				if (wmsSkuDetailDTO == null)
					continue;

				// 2.1 更新omsReturnOrderFormSku里的confirmCount
				// 确认的数量就是总数
				long confirmCount = wmsSkuDetailDTO.getCount();
				long normalCount = wmsSkuDetailDTO.getNormalCount();
				long defectiveCount = wmsSkuDetailDTO.getDefectiveCount();

				// add in 2015/1/14 如果出现反馈的数量比实际的多，则直接抛出异常
				if (confirmCount > omsReturnOrderFormSku.getReturnCount()) {
					throw new WarehouseCallerException(
							"OmsReturnOrderFormServiceImpl.updateReturnOrderFormState confirmCount=" + confirmCount
									+ ";ReturnCount=" + omsReturnOrderFormSku.getReturnCount() + ";orderid=" + id);
				}

				isSucc = isSucc
						&& omsReturnOrderFormSkuDao.updateConfirmCount(omsReturnOrderFormSku.getOrderId(),
								omsReturnOrderFormSku.getOrderSkuId(), confirmCount);
				// 2.2保存warehouseReturn
				WarehouseReturn warehouseReturn = new WarehouseReturn();
				// 这里请将SupplierId设为代理商id，不论实际sku是属于谁
				warehouseReturn.setSupplierId(omsReturnOrderFormSku.getOriSupplierId());
				warehouseReturn.setPoOrderId(String.valueOf(omsReturnOrderFormSku.getPoId()));
				warehouseReturn.setSkuId(omsReturnOrderFormSku.getSkuId());
				warehouseReturn.setProductName(omsReturnOrderFormSku.getProductName());
				warehouseReturn.setCount((int) confirmCount);
				warehouseReturn.setNormalCount((int) normalCount);
				warehouseReturn.setDefectiveCount((int) defectiveCount);
				warehouseReturn.setType(ReturnType.THREE);
				warehouseReturn.setCreateTime(System.currentTimeMillis());
				warehouseReturn.setWarehouseId(omsReturnOrderFormSku.getStoreAreaId());
				isSucc = isSucc && (warehouseReturnDao.addObject(warehouseReturn) != null);
			}
		}
		if (!isSucc) {
			throw new WarehouseCallerException("OmsReturnOrderFormServiceImpl.updateReturnOrderFormState error.id="
					+ id);
		}
		return isSucc;
	}

	@Override
	@Transaction
	public boolean saveReturnOrderForm(ReturnOrderFormDTO returnOrderFormDTO) {
		// 0.check
		long id = returnOrderFormDTO.getId();
		OmsReturnOrderForm omsReturnOrderForm = this.omsReturnOrderFormDao.getObjectById(id);
		if (omsReturnOrderForm != null)
			return true;
		// 1.保存原始的退单数据
		// 获取该退货单对应的oms原销售订单号
		long salesOrderId = 0;
		List<OmsOrderForm> orders = omsOrderFormDao.getListByUserOrderFormId(returnOrderFormDTO.getOrderId(),
				returnOrderFormDTO.getUserId());
		for (OmsOrderForm order : orders) {
			List<OmsOrderFormSku> skus = omsOrderFormSkuDao.queryByOmsOrderFormId(order.getOmsOrderFormId(),
					order.getUserId());
			for (OmsOrderFormSku sku : skus) {
				for (ReturnOrderFormSkuDTO returnOrderFormSkuDTO : returnOrderFormDTO.getReturnOrderFormSkuDTOList()) {
					if (sku.getSkuId() == returnOrderFormSkuDTO.getSkuId()) {
						salesOrderId = order.getOmsOrderFormId();
						// 设置returnOrderFormSkuDTO上的StoreAreaId和SupplierId
						PoOrderFormSku poOrderFormSku = poOrderFormSkuDao.getObjectById(sku.getSkuId());
						returnOrderFormSkuDTO.setStoreAreaId(poOrderFormSku.getSelfStoreAreaId());
						returnOrderFormSkuDTO.setSupplierId(sku.getSupplierId());
						returnOrderFormSkuDTO.setOriSupplierId(sku.getOriSupplierId());
						// 不存在一个sku属于多个omsorderformid的情况
						break;
					}
				}
			}
		}
		boolean isSucc = omsReturnOrderFormDao.addObject(returnOrderFormDTO) != null;
		WMSReturnOrderDTO wmsReturnOrderDTO = new WMSReturnOrderDTO();

		WarehouseForm warehouse = warehouseService.getWarehouseById(returnOrderFormDTO.getReturnOrderFormSkuDTOList()
				.get(0).getStoreAreaId());
		wmsReturnOrderDTO.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
		wmsReturnOrderDTO.setWarehouseCode(warehouse.getWarehouseCode());

		wmsReturnOrderDTO.setOrderId(String.valueOf(returnOrderFormDTO.getId()));
		wmsReturnOrderDTO
				.setOriginalOrderId(OmsIdUtils.genEmsOrderId(String.valueOf(salesOrderId), WMSOrderType.SALES));
		wmsReturnOrderDTO.setUserId(String.valueOf(returnOrderFormDTO.getUserId()));
		wmsReturnOrderDTO.setLogisticCompany(returnOrderFormDTO.getExpressCompany());
		wmsReturnOrderDTO.setLogisticNo(returnOrderFormDTO.getMailNO());
		wmsReturnOrderDTO.setNote("原始销售单号:"
				+ OmsIdUtils.genEmsOrderId(String.valueOf(salesOrderId), WMSOrderType.SALES));

		List<WMSSkuDetailDTO> skuDetails = new ArrayList<WMSSkuDetailDTO>();
		for (ReturnOrderFormSkuDTO returnOrderFormSkuDTO : returnOrderFormDTO.getReturnOrderFormSkuDTOList()) {
			WMSSkuDetailDTO wmsSkuDetailDTO = new WMSSkuDetailDTO();
			wmsSkuDetailDTO.setSkuId(returnOrderFormSkuDTO.getSkuId());
			wmsSkuDetailDTO.setArtNo(returnOrderFormSkuDTO.getBarCode());
			wmsSkuDetailDTO.setName(returnOrderFormSkuDTO.getProductName());
			wmsSkuDetailDTO.setSize(returnOrderFormSkuDTO.getSize());
			wmsSkuDetailDTO.setColor(returnOrderFormSkuDTO.getColorName());
			wmsSkuDetailDTO.setCount((int) returnOrderFormSkuDTO.getReturnCount());
			wmsSkuDetailDTO.setNormalCount((int) returnOrderFormSkuDTO.getReturnCount());
			skuDetails.add(wmsSkuDetailDTO);
			isSucc = isSucc && omsReturnOrderFormSkuDao.addObject(returnOrderFormSkuDTO) != null;
		}
		// 2.将订单push给仓库
		wmsReturnOrderDTO.setSkuDetails(skuDetails);
		wmsReturnOrderDTO.calculateCount();
		isSucc = isSucc && warehouseAdapterBridge.returnSales(wmsReturnOrderDTO).isSucess();
		if (!isSucc) {
			throw new ServiceNoThrowException("ReturnPoOrderFormServiceImpl.saveReturnOrderForm error.orderid="
					+ returnOrderFormDTO.getOrderId());
		}
		return isSucc;
	}

	@Override
	public List<OmsReturnOrderForm> getListByConfirmTimeAndState(long confirmTime, OmsReturnOrderFormState returnState,
			int limit) {
		return this.omsReturnOrderFormDao.getListByConfirmTimeAndState(confirmTime, returnState, limit);
	}

	@Override
	public boolean updateOmsReturnOrderFormState(long orderId, OmsReturnOrderFormState state, String extInfo) {
		return this.omsReturnOrderFormDao.updateOmsReturnOrderFormState(orderId, state, extInfo);
	}

	@Override
	public List<OmsReturnOrderFormSku> getOmsReturnOrderFormSkuListByReturnId(long returnId, long userId) {
		return this.omsReturnOrderFormSkuDao.getListByReturnId(returnId, userId);
	}

}
