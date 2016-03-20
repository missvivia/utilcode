/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.OmsSkuDao;
import com.xyl.mmall.oms.dao.PickOrderDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.PoOrderFormDao;
import com.xyl.mmall.oms.dao.PoOrderFormSkuDao;
import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.dao.ShipSkuDao;
import com.xyl.mmall.oms.dto.OrderFormOMSDTO;
import com.xyl.mmall.oms.dto.OrderSkuOMSDTO;
import com.xyl.mmall.oms.dto.PoOrderDTO;
import com.xyl.mmall.oms.dto.PoSkuDetailCountDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.BuHuoType;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.enums.OmsSkuState;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.enums.SupplyType;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.OmsSku;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.meta.PoOrderForm;
import com.xyl.mmall.oms.meta.PoOrderFormSku;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.PoOrderService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.util.OmsOrderFormPriceUtil;
import com.xyl.mmall.oms.util.OmsUtil;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;

/**
 * PoOrderServiceImpl.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-24
 * @since 1.0
 */
@Service("poOrderService")
public class PoOrderServiceImpl implements PoOrderService {

	@Autowired
	private PoOrderFormDao poOrderDao;

	@Autowired
	private PickOrderDao pickOrderDao;

	@Autowired
	private ShipOrderDao shipOrderDao;

	@Autowired
	private ShipSkuDao shipSkuDao;

	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private PickSkuDao pickSkuDao;

	@Autowired
	private WarehouseAdapterBridge warehouseAdapterBridge;

	@Autowired
	private OmsSkuDao omsSkuDao;

	@Autowired
	private PoOrderFormSkuDao poOrderFormSkuDao;

	@Autowired
	private WarehouseService warehouseService;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoOrderService#getPoOrdersList(java.lang.String,
	 *      long, long, long, long, long, long)
	 */
	@Override
	public List<PoOrderDTO> getPoOrdersList(String poOrderId, String createSTime, String startETime, String openSTIME,
			String openEtime, long supplierId) {
		String[] poOrderIdArr = null;
		if (poOrderId != null && !"".equals(poOrderId.trim()))
			poOrderIdArr = new String[] { poOrderId };
		List<PoOrderForm> pof = poOrderDao.getPickOrderListByMultiplePoOrder(poOrderIdArr, createSTime, startETime,
				openSTIME, openEtime, supplierId);
		List<PoOrderDTO> pod = new ArrayList<PoOrderDTO>();
		if (pof != null && pof.size() > 0) {
			for (PoOrderForm k : pof) {
				PoOrderDTO poOrderDto = new PoOrderDTO(k);
				PoSkuDetailCountDTO poSkuDetailCountDTO = new PoSkuDetailCountDTO();
				poSkuDetailCountDTO.setSupplyType(k.getSupplyType());
				poSkuDetailCountDTO.setPoOrderId(poOrderId);
				poSkuDetailCountDTO.setCommodityStartTime(k.getStartTime());
				poSkuDetailCountDTO.setCommodityEndTime(k.getEndTime());
				// 未捡货量
				poSkuDetailCountDTO = appendPoCount(Long.parseLong(k.getPoOrderId()), k.getSupplierId(),
						poSkuDetailCountDTO);
				poOrderDto.setSoldAmount(poSkuDetailCountDTO.getTotalSales());
				poOrderDto.setUnPickAmount(poSkuDetailCountDTO.getUnPickedAmount());
				pod.add(poOrderDto);
			}
		}
		return pod;
	}

	private PoSkuDetailCountDTO appendPoCount(long poid, long supplaierId, PoSkuDetailCountDTO poSkuDetailCountDTO) {
		List<PickSkuItemForm> pickSkuList = pickSkuDao.getPickSkuByPoOrderIdAndOriSupplierId(String.valueOf(poid),
				supplaierId);
		int unPickedAmount = 0;
		int pickAmount = 0;
		int selfStock = 0;
		int backupStock = 0;
		int buHuoAmount = 0;
		if (pickSkuList != null && pickSkuList.size() != 0) {
			for (PickSkuItemForm pickSku : pickSkuList) {

				// 去掉补货的
				if (pickSku.getBuhuo() == BuHuoType.BUHUO)
					continue;

				if (OmsUtil.isPicked(pickSku.getPickOrderId())) {
					pickAmount += pickSku.getSkuQuantity();
					if (OmsIdUtils.isBuhuoOrder(pickSku.getPickOrderId())) {
						buHuoAmount += pickSku.getSkuQuantity();
					}
				} else
					unPickedAmount += pickSku.getSkuQuantity();

				if (pickSku.getSupplierId() == pickSku.getOriSupplierId()) {
					selfStock += pickSku.getSkuQuantity();
				} else {
					backupStock += pickSku.getSkuQuantity();
				}

			}
		}
		poSkuDetailCountDTO.setUnPickedAmount(unPickedAmount);
		poSkuDetailCountDTO.setPickedAmount(pickAmount);
		poSkuDetailCountDTO.setTotalSales(pickAmount + unPickedAmount - buHuoAmount);
		poSkuDetailCountDTO.setSelfStock(selfStock);
		poSkuDetailCountDTO.setBackupStock(backupStock);
		poSkuDetailCountDTO.setTotalStock(selfStock + backupStock);
		return poSkuDetailCountDTO;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoOrderService#getPoOrderDetail(java.lang.String)
	 */
	@Override
	public PoSkuDetailCountDTO getPoOrderDetail(String poOrderId) {
		PoSkuDetailCountDTO poSkuDetailCountDTO = new PoSkuDetailCountDTO();
		PoOrderForm poOrder = poOrderDao.getPoOrderById(poOrderId);
		poSkuDetailCountDTO.setSupplyType(poOrder.getSupplyType());
		poSkuDetailCountDTO.setPoOrderId(poOrderId);
		poSkuDetailCountDTO.setCommodityStartTime(poOrder.getStartTime());
		poSkuDetailCountDTO.setCommodityEndTime(poOrder.getEndTime());
		// 未捡货量
		poSkuDetailCountDTO = appendPoCount(Long.parseLong(poOrder.getPoOrderId()), poOrder.getSupplierId(),
				poSkuDetailCountDTO);
		// 到货数量
		int arrivedAmount = 0;
		List<ShipSkuItemForm> shipSkuItemFormList = shipSkuDao.getShipSkuListByPoOrderId(poOrderId);
		for (ShipSkuItemForm shipSkuItemForm : shipSkuItemFormList) {
			arrivedAmount += shipSkuItemForm.getArrivedQuantity();
		}
		// 退货数量
		poSkuDetailCountDTO.setArrivedAmount(arrivedAmount);

		// 代理商自供货量
		poSkuDetailCountDTO.setSelfStock(poOrderFormSkuDao.getPoOrderSelfStockById(Long.valueOf(poOrderId)));

		// 品牌商参与供货
		poSkuDetailCountDTO.setBackupStock(poOrderFormSkuDao.getPoOrderBackupStockById(Long.valueOf(poOrderId)));

		// 总供货量
		poSkuDetailCountDTO.setTotalStock(poSkuDetailCountDTO.getSelfStock() + poSkuDetailCountDTO.getBackupStock());

		// 供货模式
		poSkuDetailCountDTO.setSupplyType(poOrder.getSupplyType());
		return poSkuDetailCountDTO;
	}

	@Override
	public List<PoOrderForm> getPickOrderListByMultiplePoOrder(String[] poOrderId, String createStartTime,
			String createEndTime, String openSaleStartTime, String openSaleEndTime, long supplierId) {
		return poOrderDao.getPickOrderListByMultiplePoOrder(poOrderId, createStartTime, createEndTime,
				openSaleStartTime, openSaleEndTime, supplierId);
	}

	/**
	 * 获取破
	 * 
	 * @param poOrderId
	 * @return
	 */
	public PoOrderForm getPoOrderById(String poOrderId) {
		return poOrderDao.getPoOrderById(poOrderId);
	}

	/**
	 * @param orderFormOMSDTO
	 * @return
	 */
	private Map<Boolean, List<OmsOrderForm>> saveOmsOrderForm(OrderFormOMSDTO orderFormOMSDTO) {
		// 保存omsOrderForm(A模式下一个用户订单对应一个oms订单，B和C模式下，可能会将用户订单拆分成多个oms订单)
		Map<Boolean, List<OmsOrderForm>> retMap = new LinkedHashMap<Boolean, List<OmsOrderForm>>();
		List<OmsOrderForm> omsOrderFormList = new ArrayList<OmsOrderForm>();
		List<OrderSkuOMSDTO> orderSkuOMSDTOList = orderFormOMSDTO.getOrderSkuOMSDTOList();
		Map<Long, List<OrderSkuOMSDTO>> map = new LinkedHashMap<Long, List<OrderSkuOMSDTO>>();
		for (OrderSkuOMSDTO orderSkuOMSDTO : orderSkuOMSDTOList) {
			PoOrderFormSku poOrderFormSku = this.poOrderFormSkuDao.getObjectById(orderSkuOMSDTO.getSkuId());
			long storeAreaId = 0L;
			long supplierId = 0L;
			if (poOrderFormSku == null || poOrderFormSku.getSupplyType() == SupplyType.SELF) {
				storeAreaId = orderSkuOMSDTO.getStoreAreaId();
				supplierId = orderSkuOMSDTO.getSupplierId();
				orderSkuOMSDTO.setOriSupplierId(supplierId);
			} else {
				// 需要的数量
				int decreaseCount = orderSkuOMSDTO.getTotalCount();
				boolean selfEnough = this.poOrderFormSkuDao.decreaseSelfStock(poOrderFormSku.getPoSkuId(),
						poOrderFormSku.getPoId(), decreaseCount);
				if (selfEnough) {
					storeAreaId = poOrderFormSku.getSelfStoreAreaId();
					supplierId = poOrderFormSku.getSelfSupplierId();
				} else {
					boolean isSucc = this.poOrderFormSkuDao.decreaseBackupStock(poOrderFormSku.getPoSkuId(),
							poOrderFormSku.getPoId(), decreaseCount);
					if (!isSucc) {
						retMap.put(Boolean.FALSE, null);
						return retMap;
					}
					storeAreaId = poOrderFormSku.getBackupStoreAreaId();
					supplierId = poOrderFormSku.getBackupSupplierId();
				}
				orderSkuOMSDTO.setOriSupplierId(poOrderFormSku.getSelfSupplierId());
			}
			// 确定这个sku的storeAreaId
			// 对每一个orderSkuOMSDTO找到对应的
			if (!map.containsKey(storeAreaId))
				map.put(storeAreaId, new ArrayList<OrderSkuOMSDTO>());
			orderSkuOMSDTO.setStoreAreaId(storeAreaId);
			orderSkuOMSDTO.setSupplierId(supplierId);

			map.get(storeAreaId).add(orderSkuOMSDTO);
		}
		// 对按照storeAreaId分组后的sku重新组单并且计算价格
		Collection<Collection<OrderSkuOMSDTO>> orderSkuGroupGroup = new ArrayList<Collection<OrderSkuOMSDTO>>();
		for (long storeAreaId : map.keySet()) {
			Collection<OrderSkuOMSDTO> orderSkuGroup = map.get(storeAreaId);
			orderSkuGroupGroup.add(orderSkuGroup);
		}
		List<OrderFormOMSDTO> reGroupOrderFormList = OmsOrderFormPriceUtil.caculatePrice(orderFormOMSDTO,
				orderSkuGroupGroup);
		boolean addOrderFormSucc = true;
		// 对重组后的订单列表进行转换并且保存到数据库
		for (OrderFormOMSDTO newOrderFormOMSDTO : reGroupOrderFormList) {
			OmsOrderForm omsOrderForm = new OmsOrderForm();
			ReflectUtil.convertObj(omsOrderForm, newOrderFormOMSDTO, false);
			long currentTime = System.currentTimeMillis();
			omsOrderForm.setCreateTime(currentTime);
			omsOrderForm.setOmsOrderFormState(OmsOrderFormState.TOSEND);
			// storeAreaId决定了这个订单给到哪个仓库去
			omsOrderForm.setStoreAreaId(newOrderFormOMSDTO.getOrderSkuOMSDTOList().get(0).getStoreAreaId());
			addOrderFormSucc = addOrderFormSucc && (this.omsOrderFormDao.addObject(omsOrderForm) != null);
			omsOrderForm.setOmsOrdeFormSkuList(new ArrayList<OmsOrderFormSku>());
			for (OrderSkuOMSDTO orderSkuOMSDTO : newOrderFormOMSDTO.getOrderSkuOMSDTOList()) {
				OmsOrderFormSku omsOrderFormSku = new OmsOrderFormSku();
				ReflectUtil.convertObj(omsOrderFormSku, orderSkuOMSDTO, false);
				omsOrderForm.getOmsOrdeFormSkuList().add(omsOrderFormSku);
				omsOrderFormSku.setOmsOrderFormId(omsOrderForm.getOmsOrderFormId());
				addOrderFormSucc = addOrderFormSucc && (this.omsOrderFormSkuDao.addObject(omsOrderFormSku) != null);
			}
			omsOrderFormList.add(omsOrderForm);
		}
		if (!addOrderFormSucc) {
			throw new ServiceException("PoOrderServiceImpl.newOrderFormOMSDTO error");
		}
		retMap.put(Boolean.TRUE, omsOrderFormList);
		return retMap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PoOrderService#savePoOrderForm(com.xyl.mmall.oms.meta.OmsOrderForm,
	 *      java.util.List)
	 */
	@Override
	@Transaction
	public boolean savePoOrderForm(OrderFormOMSDTO orderFormOMSDTO, List<PoOrderForm> toCreatePoOrderFormList) {
		boolean isSucc = true;
		// 0.如果订单已经存在，则返回true
		long userOrderFormId = orderFormOMSDTO.getUserOrderFormId();
		List<OmsOrderForm> dbOmsOrderFormList = this.omsOrderFormDao.getListByUserOrderFormId(userOrderFormId,
				orderFormOMSDTO.getUserId());
		if (dbOmsOrderFormList != null && dbOmsOrderFormList.size() > 0)
			return true;
		// 1.保存toCreatePoOrderFormList
		for (PoOrderForm poOrderForm : toCreatePoOrderFormList) {
			if (this.poOrderDao.getPoOrderById(poOrderForm.getPoOrderId()) == null) {
				isSucc = isSucc && (this.poOrderDao.addObject(poOrderForm) != null);
				isSucc = isSucc && this.poOrderFormSkuDao.addObjects(poOrderForm.getPoOrderFormSkuList());
			}
		}
		// 2.保存omsOrderForm(A模式下一个用户订单对应一个oms订单，B和C模式下，可能会将用户订单拆分成多个oms订单)
		Map<Boolean, List<OmsOrderForm>> resultMap = saveOmsOrderForm(orderFormOMSDTO);
		if (resultMap.containsKey(Boolean.FALSE)) {
			boolean addOrderFormSucc = true;
			orderFormOMSDTO.setOmsOrderFormState(OmsOrderFormState.OMSRECVFAILED);
			addOrderFormSucc = addOrderFormSucc && (this.omsOrderFormDao.addObject(orderFormOMSDTO) != null);
			for (OrderSkuOMSDTO orderSkuOMSDTO : orderFormOMSDTO.getOrderSkuOMSDTOList()) {
				OmsOrderFormSku omsOrderFormSku = new OmsOrderFormSku();
				ReflectUtil.convertObj(omsOrderFormSku, orderSkuOMSDTO, false);
				omsOrderFormSku.setOmsOrderFormId(orderFormOMSDTO.getOmsOrderFormId());
				addOrderFormSucc = addOrderFormSucc && (this.omsOrderFormSkuDao.addObject(omsOrderFormSku) != null);
			}
			return addOrderFormSucc;
		}
		List<OmsOrderForm> omsOrderFormList = resultMap.get(Boolean.TRUE);

		for (OmsOrderForm omsOrderForm : omsOrderFormList) {
			// 3.修改PoOrderForm的数量
			PickMoldType pickMoldType = (omsOrderForm.getOmsOrdeFormSkuList().size() == 1 && (omsOrderForm
					.getOmsOrdeFormSkuList().get(0).getTotalCount() == 1)) ? PickMoldType.SINGLE : PickMoldType.MANY;
			for (OmsOrderFormSku omsOrderSku : omsOrderForm.getOmsOrdeFormSkuList()) {
				// 3.1生成待拣货单明细
				PickSkuItemForm pickSkuItemForm = new PickSkuItemForm();
				pickSkuItemForm.setSkuId(String.valueOf(omsOrderSku.getSkuId()));
				pickSkuItemForm.setPoOrderId(String.valueOf(omsOrderSku.getPoId()));
				pickSkuItemForm.setPickOrderId("0");
				pickSkuItemForm.setSkuQuantity(omsOrderSku.getTotalCount());
				pickSkuItemForm.setPickStates(PickStateType.UNPICK);
				pickSkuItemForm.setPickTime(0L);
				pickSkuItemForm.setPickMoldType(pickMoldType);
				pickSkuItemForm.setOmsOrderFormId(omsOrderForm.getOmsOrderFormId());
				pickSkuItemForm.setBuhuo(BuHuoType.NORMAL);
				pickSkuItemForm.setCreateTime(omsOrderForm.getCreateTime());
				pickSkuItemForm.setCodeNO(omsOrderSku.getBarCode());
				pickSkuItemForm.setColor(omsOrderSku.getColorName());
				pickSkuItemForm.setSize(omsOrderSku.getSize());
				pickSkuItemForm.setProductName(omsOrderSku.getProductName());
				pickSkuItemForm.setJITFlag(JITFlagType.IS_JIT);
				pickSkuItemForm.setSupplierId(omsOrderSku.getSupplierId());
				pickSkuItemForm.setStoreAreaId(omsOrderSku.getStoreAreaId());
				pickSkuItemForm.setOriSupplierId(omsOrderSku.getOriSupplierId());
				isSucc = isSucc && (this.pickSkuDao.addObject(pickSkuItemForm) != null);
				// 2.4如果还未保存过sku信息，则生成omssku信息
				OmsSku omsSku = omsSkuDao.getObjectById(omsOrderSku.getSkuId());
				if (omsSku == null) {
					omsSku = new OmsSku();
					omsSku.setState(OmsSkuState.SYNED);
					omsSku.setSkuId(omsOrderSku.getSkuId());
					omsSku.setSupplierId(omsOrderSku.getSupplierId());
					omsSku.setProductId(omsOrderSku.getProductId());
					omsSku.setPoId(omsOrderSku.getPoId());
					omsSku.setProductName(omsOrderSku.getProductName());
					omsSku.setColorName(omsOrderSku.getColorName());
					omsSku.setSize(omsOrderSku.getSize());
					omsSku.setBarCode(omsOrderSku.getBarCode());
					isSucc = isSucc && (omsSkuDao.addObject(omsSku) != null);
					// 推送至仓库
					WMSSkuDetailDTO wmsSkuDetailDTO = new WMSSkuDetailDTO();
					// todo c模式下会有多个仓库的问题。所以同步商品数据的时候需要向涉及到的每一个仓库同步
					WarehouseForm warehouse = warehouseService.getWarehouseById(omsOrderForm.getStoreAreaId());
					wmsSkuDetailDTO.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
					wmsSkuDetailDTO.setWarehouseCode(warehouse.getWarehouseCode());
					wmsSkuDetailDTO.setSkuId(omsSku.getSkuId());
					wmsSkuDetailDTO.setArtNo(omsSku.getBarCode());
					wmsSkuDetailDTO.setName(omsSku.getProductName());
					wmsSkuDetailDTO.setSize(omsSku.getSize());
					wmsSkuDetailDTO.setColor(omsSku.getColorName());
					isSucc = isSucc && warehouseAdapterBridge.syncSku(wmsSkuDetailDTO).isSucess();
					if (!isSucc) {
						throw new ServiceException("PoOrderServiceImpl.savePoOrderForm error.omsOrderFormId="
								+ omsOrderSku.getOmsOrderFormId());
					}
				}
			}
		}
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.service.PoOrderService#isNotGen2Return()
	 */
	@Override
	public List<PoOrderForm> getNotGen2ReturnAfterPoEnd(long incTime,long compareTime) {
		return poOrderDao.getNotGen2ReturnAfterPoEnd(incTime,compareTime);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.service.PoOrderService#addPoOrderCommand(long)
	 */
	@Override
	public boolean addPoOrderCommand(long poOrderId, long command) {
		return poOrderDao.addPoOrderCommand(poOrderId, command);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.service.PoOrderService#addPoOrderGen2ReturnCommand(long)
	 */
	@Override
	public boolean addPoOrderGen2ReturnCommand(long poOrderId) {
		return addPoOrderCommand(poOrderId,PoOrderForm.COMMAND_GEN2RETURN);
	}
}
