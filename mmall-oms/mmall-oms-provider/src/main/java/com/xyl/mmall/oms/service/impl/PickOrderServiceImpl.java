/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.PickOrderDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.dao.WarehouseDao;
import com.xyl.mmall.oms.dto.PickOrderDTO;
import com.xyl.mmall.oms.dto.PickSkuDTO;
import com.xyl.mmall.oms.dto.ShipSkuDTO;
import com.xyl.mmall.oms.enums.BuHuoType;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.meta.BociInfo;
import com.xyl.mmall.oms.meta.PickOrderForm;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.PickOrderService;
import com.xyl.mmall.oms.service.PickSkuService;
import com.xyl.mmall.oms.service.ShipSkuService;
import com.xyl.mmall.oms.util.OmsUtil;

/**
 * @author zb
 *
 */
@Service("pickOrderService")
public class PickOrderServiceImpl implements PickOrderService {

	@Autowired
	private PickOrderDao pickOrderDao;

	@Autowired
	private PickSkuDao pickSkuDao;

	@Autowired
	private PickSkuService pickSkuService;

	@Autowired
	private ShipSkuService shipSkuService;

	@Autowired
	private WarehouseDao warehouseDao;

	@Autowired
	private ShipOrderDao shipOrderDao;

	@Override
	public PickOrderDTO getPickOrderByPkId(String pickOrderId, long supplierId) {
		PickOrderDTO dto = new PickOrderDTO(pickOrderDao.getPickOrder(pickOrderId, supplierId));
		dto.setWarehouseForm(warehouseDao.getWarehouseById(dto.getStoreAreaId()));
		return dto;
	}

	@Override
	public boolean updatePickOrder(PickOrderDTO pickOrderDTO) {
		return pickOrderDao.updatePickOrder(pickOrderDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.PickOrderService#createPickOrderFormAndShipOrder(long)
	 */
	@Override
	@Transaction
	public List<PickOrderForm> createPickOrderFormAndShipOrder(long supplierId, long bociDeadLine) {
		List<PickOrderForm> pickOrderFormList = new ArrayList<PickOrderForm>();
		// 获取当前商家下所有未拣货的明细
		List<PickSkuItemForm> pickSkuList = pickSkuDao.getUnPickSkuByPoSupplierId(supplierId);
		// key是拣货单号
		Map<String, List<PickSkuItemForm>> pickSkuListMap = new LinkedHashMap<String, List<PickSkuItemForm>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		long pickTime = System.currentTimeMillis();
		// 将未拣货的sku明细按照拣货单号分组
		boolean isSucc = true;
		for (PickSkuItemForm pickSkuItem : pickSkuList) {
			// 判断是否到拣货时间
			if (pickSkuItem.getCreateTime() > bociDeadLine || pickSkuItem.getBuhuo() == BuHuoType.BUHUO)
				continue;

			BociInfo bociInfo = OmsUtil.getBoci(pickSkuItem.getCreateTime(), pickSkuItem.getPickMoldType());

			String prefix = pickSkuItem.getPickMoldType() == PickMoldType.SINGLE ? "A" : "B";

			String pickOrderId = prefix + "-" + sdf.format(new Date(bociInfo.getBociDeadLine())) + "-"
					+ bociInfo.getBociType().getIntValue() + "-" + pickSkuItem.getSupplierId() + "-"
					+ pickSkuItem.getStoreAreaId();

			long collectTime = bociDeadLine;
			int boci = bociInfo.getBociType().getIntValue();

			if (!pickSkuListMap.containsKey(pickOrderId)) {
				pickSkuListMap.put(pickOrderId, new ArrayList<PickSkuItemForm>());
			}
			pickSkuListMap.get(pickOrderId).add(pickSkuItem);
			pickSkuItem.setPickOrderId(pickOrderId);
			pickSkuItem.setPickStates(PickStateType.UNPICK);
			pickSkuItem.setPickTime(System.currentTimeMillis());

			pickSkuItem.setCollectTime(collectTime);
			pickSkuItem.setBoci(boci);

			isSucc = isSucc && this.pickSkuDao.updateUnPickSkuStateById(pickSkuItem.getId(), pickOrderId, pickTime);
		}
		// 遍历各个分组，生成对应的拣货单
		for (String pickOrderId : pickSkuListMap.keySet()) {
			int pickSkuTotal = 0;
			List<PickSkuItemForm> pickSkuInPickOrder = pickSkuListMap.get(pickOrderId);
			for (PickSkuItemForm pickSkuItemForm : pickSkuInPickOrder) {
				pickSkuTotal += pickSkuItemForm.getSkuQuantity();
			}
			// 创建一张拣货单
			PickOrderForm pickOrderForm = new PickOrderForm();
			pickOrderForm.setPickOrderId(pickOrderId);
			pickOrderForm.setJITFlag(JITFlagType.IS_JIT);
			pickOrderForm.setPickState(PickStateType.UNPICK);
			pickOrderForm.setPickTotalQuantity(pickSkuTotal);
			pickOrderForm.setCreateTime(pickTime);
			pickOrderForm.setModifyTime(pickTime);
			pickOrderForm.setSupplierId(supplierId);
			// 设置仓库
			pickOrderForm.setStoreAreaId(pickSkuInPickOrder.get(0).getStoreAreaId());
			isSucc = isSucc && (pickOrderDao.addObject(pickOrderForm) != null);
			pickOrderForm.setPickSkuList(pickSkuInPickOrder);
			pickOrderFormList.add(pickOrderForm);

			// 创建一张发货单数据
			ShipOrderForm shipOrderForm = new ShipOrderForm();
			shipOrderForm.setSupplierId(pickOrderForm.getSupplierId());
			shipOrderForm.setStoreAreaId(pickOrderForm.getStoreAreaId());
			shipOrderForm.setPickOrderId(pickOrderForm.getPickOrderId());
			shipOrderForm.setShipOrderId(pickOrderForm.getPickOrderId());
			shipOrderForm.setShipState(ShipStateType.UNSEDN);
			shipOrderForm.setJITFlag(JITFlagType.IS_JIT);
			shipOrderForm.setCreateTime(System.currentTimeMillis());
			// 设置汇总时间，拨次，拣货类型等信息
			shipOrderForm.setCollectTime(pickSkuInPickOrder.get(0).getCollectTime());
			shipOrderForm.setBoci(pickSkuInPickOrder.get(0).getBoci());
			shipOrderForm.setPickMoldType(pickSkuInPickOrder.get(0).getPickMoldType());

			List<PickSkuDTO> pickSkuDTOs = pickSkuService.getPickSkuDetail(pickOrderForm.getPickOrderId(),
					pickOrderForm.getSupplierId());
			Map<Long, ShipSkuDTO> map = new LinkedHashMap<Long, ShipSkuDTO>();
			List<ShipSkuDTO> list = new ArrayList<ShipSkuDTO>();
			long totalCount = 0;
			if (pickSkuDTOs != null && pickSkuDTOs.size() > 0) {
				for (PickSkuDTO pickSku : pickSkuDTOs) {
					long skuId = Long.parseLong(pickSku.getSkuId());
					ShipSkuDTO shipSkuDTO = map.get(skuId);
					if (shipSkuDTO == null) {
						shipSkuDTO = new ShipSkuDTO();
						shipSkuDTO.setSkuId(pickSku.getSkuId());
						shipSkuDTO.setCodeNO(pickSku.getCodeNO());
						shipSkuDTO.setColor(pickSku.getColor());
						shipSkuDTO.setJITFlag(pickSku.getJITFlag());
						shipSkuDTO.setPickMoldType(pickSku.getPickMoldType());
						shipSkuDTO.setPoOrderId(pickSku.getPoOrderId());
						shipSkuDTO.setProductName(pickSku.getProductName());
						shipSkuDTO.setShipOrderId(pickSku.getPickOrderId());
						shipSkuDTO.setSize(pickSku.getSize());
						shipSkuDTO.setShipStates(ShipStateType.UNSEDN);
						shipSkuDTO.setShipTime(new Date().getTime());
						shipSkuDTO.setSkuQuantity(pickSku.getSkuQuantity());
						shipSkuDTO.setSupplierId(pickSku.getSupplierId());
						shipSkuDTO.setStoreAreaId(pickSku.getStoreAreaId());
						map.put(skuId, shipSkuDTO);
						list.add(shipSkuDTO);
					} else {
						shipSkuDTO.setSkuQuantity(shipSkuDTO.getSkuQuantity() + pickSku.getSkuQuantity());
					}
					totalCount = totalCount + pickSku.getSkuQuantity();
				}
				isSucc = isSucc
						&& shipSkuService.saveShipSkuList(list, pickOrderForm.getPickOrderId(), JITFlagType.IS_JIT);
			}
			shipOrderForm.setTotal((int) totalCount);
			shipOrderForm.setCreateTime(System.currentTimeMillis());
			// 创建发货单
			isSucc = isSucc && shipOrderDao.addObject(shipOrderForm) != null;

		}
		if (!isSucc) {
			throw new ServiceException("PickOrderService.createPickOrderForm,supplierId=" + supplierId);
		}
		return pickOrderFormList;
	}

	@Override
	public List<PickOrderDTO> getPickListBySupplierIdAndTime(long supplierId, long startTime, long endTime) {
		if (startTime == 0)
			startTime = OmsConstants.LONG_BEFORE_LONG;
		if (endTime == 0)
			endTime = OmsConstants.LONG_AFTER_LONG;
		List<PickOrderForm> pof = pickOrderDao.getPickListBySupplierIdAndTime(supplierId, startTime, endTime);
		if (CollectionUtils.isEmpty(pof)) {
			return null;
		}
		List<PickOrderDTO> pod = new ArrayList<>(pof.size());
		for (PickOrderForm k : pof) {
			PickOrderDTO pickOrderDTO = new PickOrderDTO(k);
			ShipOrderForm shipOrderForm = this.shipOrderDao.getShipOrderByShipId(k.getPickOrderId(), k.getSupplierId());
			// 增加仓库数据
			WarehouseForm warehouseForm = warehouseDao.getWarehouseById(k.getStoreAreaId());
			pickOrderDTO.setWarehouseForm(warehouseForm);
			pickOrderDTO.setShipState(shipOrderForm.getShipState());
			pod.add(pickOrderDTO);
		}
		return pod;
	}
}
