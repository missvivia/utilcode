package com.xyl.mmall.oms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.OmsOrderPackageDao;
import com.xyl.mmall.oms.dao.OmsOrderPackageSkuDao;
import com.xyl.mmall.oms.dao.OmsSkuDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.PoOrderFormSkuDao;
import com.xyl.mmall.oms.dao.WarehouseReturnDao;
import com.xyl.mmall.oms.dto.warehouse.WMSPackageDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.enums.OmsSkuState;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.enums.WMSPackageState;
import com.xyl.mmall.oms.enums.WMSSalesOrderState;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.BociInfo;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.meta.OmsOrderPackageSku;
import com.xyl.mmall.oms.meta.OmsSku;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.meta.PoOrderFormSku;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.util.OmsUtil;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

@Service("omsOrderFormService")
public class OmsOrderFormServiceImpl implements OmsOrderFormService {

	private Logger logger = Logger.getLogger(OmsOrderFormServiceImpl.class);

	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private WarehouseAdapterBridge warehouseAdapterBridge;

	@Autowired
	private OmsOrderPackageSkuDao omsOrderPackageSkuDao;

	@Autowired
	private OmsOrderPackageDao omsOrderPackageDao;

	@Autowired
	private OmsSkuDao omsSkuDao;

	@Autowired
	private WarehouseReturnDao warehouseReturnDao;

	@Autowired
	private PickSkuDao pickSkuDao;

	@Autowired
	private PoOrderFormSkuDao poOrderFormSkuDao;

	@Autowired
	private WarehouseService warehouseService;

	@Override
	public long getCancelSkuCountInPoId(long poId) {
		return omsOrderFormSkuDao.getCancelSkuCountInPoId(poId);
	}

	@Override
	@Transaction
	public boolean send(OmsOrderForm omsOrderForm) {
		long omsOrderFormId = omsOrderForm.getOmsOrderFormId();
		List<OmsOrderFormSku> list = omsOrderFormSkuDao.queryByOmsOrderFormId(omsOrderFormId, omsOrderForm.getUserId());
		omsOrderForm.setOmsOrdeFormSkuList(list);
		// 1.拼装WMSSalesOrderDTO数据
		WMSSalesOrderDTO wmsSalesOrderDTO = new WMSSalesOrderDTO();
		WarehouseForm warehouse = warehouseService.getWarehouseById(omsOrderForm.getStoreAreaId());
		wmsSalesOrderDTO.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
		wmsSalesOrderDTO.setWarehouseCode(warehouse.getWarehouseCode());

		wmsSalesOrderDTO.setOrderId(String.valueOf(omsOrderForm.getOmsOrderFormId()));
		wmsSalesOrderDTO.setReceiverName(omsOrderForm.getConsigneeName());
		wmsSalesOrderDTO.setReceiverPostCode(omsOrderForm.getZipcode());
		wmsSalesOrderDTO.setReceiverPhone(omsOrderForm.getConsigneeTel());
		wmsSalesOrderDTO.setReceiverMobile(omsOrderForm.getConsigneeMobile());
		wmsSalesOrderDTO.setReceiverProvince(omsOrderForm.getProvince());
		wmsSalesOrderDTO.setReceiverCity(omsOrderForm.getCity());
		wmsSalesOrderDTO.setReceiverDistrict(omsOrderForm.getSection());
		wmsSalesOrderDTO.setReceiverAddress(omsOrderForm.getStreet() + " " + omsOrderForm.getAddress());
		wmsSalesOrderDTO.setOrderTime(omsOrderForm.getOrderTime());
		wmsSalesOrderDTO.setLogisticCode(wmsSalesOrderDTO.getWmsType().getDesc());

		double totalPrice = omsOrderForm.getCartRPrice().doubleValue();
		double shipAmount = omsOrderForm.getExpUserPrice().doubleValue();
		boolean isCod = omsOrderForm.isCashOnDelivery();
		double codAmount = 0.0;
		if (isCod) {
			codAmount = (omsOrderForm.getCartRPrice().add(omsOrderForm.getExpUserPrice())).doubleValue();
		}
		List<WMSSkuDetailDTO> skuDetails = new ArrayList<WMSSkuDetailDTO>();
		for (OmsOrderFormSku sku : list) {
			WMSSkuDetailDTO wmsSkuDetailDTO = new WMSSkuDetailDTO();
			wmsSkuDetailDTO.setSkuId(sku.getSkuId());
			wmsSkuDetailDTO.setArtNo(sku.getBarCode());
			wmsSkuDetailDTO.setName(sku.getProductName());
			wmsSkuDetailDTO.setSize(sku.getSize());
			wmsSkuDetailDTO.setColor(sku.getColorName());
			wmsSkuDetailDTO.setCount(sku.getTotalCount());
			wmsSkuDetailDTO.setNormalCount(sku.getTotalCount());
			wmsSkuDetailDTO.setPrice(sku.getRprice().doubleValue());
			skuDetails.add(wmsSkuDetailDTO);
		}
		wmsSalesOrderDTO.setTotalPrice(totalPrice);
		wmsSalesOrderDTO.setShipAmount(shipAmount);
		wmsSalesOrderDTO.setCod(isCod);
		wmsSalesOrderDTO.setCodAmount(codAmount);
		wmsSalesOrderDTO.setSkuDetails(skuDetails);
		wmsSalesOrderDTO.calculateCount();

		PickMoldType pickMoldType = (omsOrderForm.getOmsOrdeFormSkuList().size() == 1 && (omsOrderForm
				.getOmsOrdeFormSkuList().get(0).getTotalCount() == 1)) ? PickMoldType.SINGLE : PickMoldType.MANY;

		BociInfo boci = OmsUtil.getBoci(omsOrderForm.getCreateTime(), pickMoldType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String lot = sdf.format(new Date(boci.getBociDeadLine())) + OmsConstants.DEFAULT_SPLIT_CHAR1
				+ boci.getBociType().getIntValue();

		wmsSalesOrderDTO.setBoci("boci" + lot);

		// 2.修改订单状态
		boolean isSucc = omsOrderFormDao.updateOrderFormState(omsOrderFormId, OmsOrderFormState.TOSEND,
				OmsOrderFormState.SENT);
		isSucc = isSucc && warehouseAdapterBridge.send(wmsSalesOrderDTO).isSucess();

		if (!isSucc) {
			// 抛出异常，强制让事务回滚
			logger.error("OmsOrderFormServiceImpl.send error.omsOrderFormId=" + omsOrderFormId);
			throw new ServiceNoThrowException("OmsOrderFormServiceImpl.send error.omsOrderFormId=" + omsOrderFormId);
		}
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.OmsOrderFormService#canCancelOrder(long,
	 *      long)
	 */
	@Override
	public int canCancelOrder(long userOrderFormId, long userId) {
		List<OmsOrderForm> omsOrderFormList = this.omsOrderFormDao.getListByUserOrderFormId(userOrderFormId, userId);
		int type = 0;
		// 为了更安全，先判断一下是否可以执行取消操作
		for (int i = 0; i < omsOrderFormList.size();) {
			OmsOrderForm omsOrderForm = omsOrderFormList.get(i);
			if (omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.CANCEL) {
				return 2;
			} else if (omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.UNPICK_CANCEL) {
				return 1;
			} else {
				return 0;
			}
		}
		return type;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.OmsOrderFormService#cancelOrderForm(long,
	 *      long)
	 */
	@Override
	@Transaction
	public int cancelOrderForm(long userOrderFormId, long userId) {
		List<OmsOrderForm> omsOrderFormList = this.omsOrderFormDao.getListByUserOrderFormId(userOrderFormId, userId);
		int type = 0;
		boolean isSucc = true;
		// 为了更安全，先判断一下是否可以执行取消操作
		for (int i = 0; i < omsOrderFormList.size(); i++) {
			OmsOrderForm omsOrderForm = omsOrderFormList.get(i);
			if (omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.CANCEL
					|| omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.UNPICK_CANCEL) {
				return 3;
			}
			if (omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.SHIP
					|| omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.NOTICE_SHIP) {
				return 0;
			}
		}
		for (int i = 0; i < omsOrderFormList.size(); i++) {
			OmsOrderForm omsOrderForm = omsOrderFormList.get(i);
			long omsOrderFormId = omsOrderForm.getOmsOrderFormId();
			List<PickSkuItemForm> pickSkuItemList = this.pickSkuDao.getByOmsOrderFormId(omsOrderFormId);

			for (PickSkuItemForm pickSkuItem : pickSkuItemList) {
				// 如果未拣货，则删除unpicksku
				if (pickSkuItem.getPickOrderId().equals("0")) {
					// 0.更新状态
					isSucc = omsOrderFormDao.updateOrderFormState(omsOrderFormId, OmsOrderFormState.UNPICK_CANCEL);
					// 1.删除
					isSucc = isSucc && pickSkuDao.deleteUnPickSkuByOmsOrderFormId(omsOrderFormId);
					// 2.如果订单已经推送，则取消
					if (omsOrderForm.getOmsOrderFormState() != OmsOrderFormState.TOSEND) {
						isSucc = isSucc && sendCancelCommandToWarehouse(omsOrderForm);
					}
					// 3.回收对应的库存
					for (PickSkuItemForm pickSku : pickSkuItemList) {
						// 查询对应的posku属于哪个storeareaid
						PoOrderFormSku poOrderFormSku = this.poOrderFormSkuDao.getPoOrderFormSkuById(
								Long.valueOf(pickSku.getSkuId()), Long.valueOf(pickSku.getPoOrderId()));

						if (pickSku.getStoreAreaId() == poOrderFormSku.getSelfStoreAreaId()) {
							isSucc = isSucc
									&& poOrderFormSkuDao.increaseSelfStock(Long.valueOf(pickSku.getSkuId()),
											Long.valueOf(pickSku.getPoOrderId()), pickSku.getSkuQuantity());
						} else if (pickSku.getStoreAreaId() == poOrderFormSku.getSelfStoreAreaId()) {
							isSucc = isSucc
									&& poOrderFormSkuDao.increaseBackupStock(Long.valueOf(pickSku.getSkuId()),
											Long.valueOf(pickSku.getPoOrderId()), pickSku.getSkuQuantity());
						}
					}
					type = isSucc ? 1 : type;
				} else {
					// 如果已经生成拣货单，则拣货单不用调整。直接给仓库取消
					isSucc = omsOrderFormDao.updateOrderFormState(omsOrderFormId, OmsOrderFormState.CANCEL);
					isSucc = isSucc && sendCancelCommandToWarehouse(omsOrderForm);
					type = isSucc ? 2 : type;
				}

				if (!isSucc) {
					throw new ServiceNoThrowException("cancelOrderForm error.omsorderformid="
							+ omsOrderForm.getOmsOrderFormId());
				}

				// 根据omsorderform的维度只做一次即可
				break;
			}

		}
		return type;
	}

	@Override
	public boolean sendCancelCommandToWarehouse(OmsOrderForm omsOrderForm) {
		WMSSalesOrderDTO wmsSalesOrderDTO = new WMSSalesOrderDTO();
		WarehouseForm warehouse = warehouseService.getWarehouseById(omsOrderForm.getStoreAreaId());
		if (warehouse != null) {
			wmsSalesOrderDTO.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
			wmsSalesOrderDTO.setWarehouseCode(warehouse.getWarehouseCode());
		}
		wmsSalesOrderDTO.setOrderId(String.valueOf(omsOrderForm.getOmsOrderFormId()));
		// 3.向仓库发送指令
		return warehouseAdapterBridge.cancelSend(wmsSalesOrderDTO).isSucess();
	}

	@Override
	@Transaction
	public boolean salesOrderUpdate(WMSSalesOrderUpdateDTO wmsSalesOrderUpdateDTO) {
		long omsOrderFormId = 0L;
		try {
			omsOrderFormId = Long.parseLong(wmsSalesOrderUpdateDTO.getOrderId());
		} catch (Exception e) {
			logger.error("error orderid:" + wmsSalesOrderUpdateDTO.getOrderId());
			return true;
		}
		OmsOrderForm omsOrderForm = this.omsOrderFormDao.getObjectById(omsOrderFormId);
		if (omsOrderForm == null) {
			logger.error("null orderform,orderid:" + omsOrderFormId);
			return true;
		}
		// 根据WMSSalesOrderState区分不同的情况
		WMSSalesOrderState state = wmsSalesOrderUpdateDTO.getState();
		if (state == WMSSalesOrderState.SHIP) {
			boolean isSucc = true;
			List<WMSPackageDTO> wmsPackageList = wmsSalesOrderUpdateDTO.getPackages();
			List<OmsOrderPackage> omsOrderPackageList = new ArrayList<OmsOrderPackage>();
			for (WMSPackageDTO wmsPackageDTO : wmsPackageList) {
				OmsOrderPackage orderPackage = new OmsOrderPackage();
				orderPackage.setOmsOrderFormId(omsOrderFormId);
				orderPackage.setMailNO(wmsPackageDTO.getShipNo());
				orderPackage.setUserId(omsOrderForm.getUserId());
				orderPackage.setExpressCompany(wmsSalesOrderUpdateDTO.getLogisticCode());
				orderPackage.setWeight(wmsPackageDTO.getWeight());
				orderPackage.setOmsOrderPackageState(OmsOrderPackageState.SHIP);
				orderPackage.setPackageStateFeedBackToApp(false);
				isSucc = isSucc && (this.omsOrderPackageDao.addObject(orderPackage) != null);
				List<WMSSkuDetailDTO> wmsSkuDetailList = wmsPackageDTO.getSkuDetails();
				List<OmsOrderPackageSku> omsPackageSkuList = new ArrayList<OmsOrderPackageSku>();
				for (WMSSkuDetailDTO wmsSkuDetailDTO : wmsSkuDetailList) {
					OmsOrderPackageSku omsOrderPackageSku = new OmsOrderPackageSku();
					omsOrderPackageSku.setOmsOrderFormId(omsOrderFormId);
					omsOrderPackageSku.setPackageId(orderPackage.getPackageId());
					omsOrderPackageSku.setSkuId(wmsSkuDetailDTO.getSkuId());
					omsOrderPackageSku.setCount(wmsSkuDetailDTO.getCount());
					omsOrderPackageSku.setUserId(omsOrderForm.getUserId());
					omsOrderPackageSku.setWeight(wmsSkuDetailDTO.getWeight());
					omsPackageSkuList.add(omsOrderPackageSku);
					isSucc = isSucc && (this.omsOrderPackageSkuDao.addObject(omsOrderPackageSku) != null);
				}
				omsOrderPackageList.add(orderPackage);
			}
			// 更新订单状态并计算运费
			long currentTime = System.currentTimeMillis();
			isSucc = isSucc
					&& this.omsOrderFormDao.updateShipTimeAndState(omsOrderFormId, currentTime, OmsOrderFormState.SHIP);
			if (!isSucc) {
				throw new WarehouseCallerException(
						"OmsOrderFormServiceImpl.salesOrderUpdate.SHIP error.omsOrderFormId=" + omsOrderFormId);
			}
			return isSucc;
		} else if (state == WMSSalesOrderState.WAYUPDATE) {
			boolean isSucc = true;
			List<WMSPackageDTO> wmsPackageList = wmsSalesOrderUpdateDTO.getPackages();
			for (WMSPackageDTO wmsPackageDTO : wmsPackageList) {
				WMSPackageState wmsPackageState = wmsPackageDTO.getState();
				String expressCompany = wmsSalesOrderUpdateDTO.getLogisticCode();
				if (wmsPackageState == WMSPackageState.SIGNED) {
					String mailNo = wmsPackageDTO.getShipNo();
					OmsOrderPackage orderPackage = this.omsOrderPackageDao.getByMailNOAndExpressCompany(mailNo,
							expressCompany);
					orderPackage.setPackageStateUpdateTime(System.currentTimeMillis());

					if (orderPackage.getOmsOrderPackageState() == OmsOrderPackageState.DONE)
						return true;

					if (wmsPackageState == WMSPackageState.SIGNED) {
						orderPackage.setOmsOrderPackageState(OmsOrderPackageState.DONE);
						// 更新到数据库中去
						isSucc = isSucc
								&& this.omsOrderPackageDao.updatePackageState(orderPackage.getPackageId(),
										orderPackage.getOmsOrderPackageState(), OmsOrderPackageState.SHIP,
										orderPackage.getPackageStateUpdateTime());
					}
				}
			}
			if (!isSucc) {
				throw new WarehouseCallerException(
						"OmsOrderFormServiceImpl.salesOrderUpdate.WAYUPDATE error.omsOrderFormId=" + omsOrderFormId);
			}
			return isSucc;
		} else {
			// 直接更新OmsOrderFormState的状态
			// 如果已经是ship状态，则不做任何处理
			if (omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.SHIP
					|| omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.NOTICE_SHIP
					|| omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.CANCEL
					|| omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.UNPICK_CANCEL
					|| omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.TIMEOUT_CANCEL
					|| omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.OMSRECVFAILED_CANCEL)
				return true;

			OmsOrderFormState omsOrderFormState = OmsOrderFormState.genEnumByDesc(state.toString());
			if (omsOrderFormState == null) {
				// 不可识别的订单状态
				logger.error("invalid WMSSalesOrderState.state=" + state.toString());
			}
			return omsOrderFormDao.updateOrderFormState(omsOrderFormId, omsOrderFormState);
		}
	}

	@Override
	public void pushSkuToWarehose() {
		// 按状态获取未同步的sku列表
		int limit = 20, offset = 0;
		List<OmsSku> omsSkuList = new ArrayList<OmsSku>();
		do {
			omsSkuList = this.omsSkuDao.getOmsSkuListByState(OmsSkuState.WAITSYN, limit, offset);
			for (OmsSku omsSku : omsSkuList) {
				WMSSkuDetailDTO wmsSkuDetailDTO = new WMSSkuDetailDTO();
				wmsSkuDetailDTO.setSkuId(omsSku.getSkuId());
				wmsSkuDetailDTO.setArtNo(omsSku.getBarCode());
				wmsSkuDetailDTO.setName(omsSku.getProductName());
				wmsSkuDetailDTO.setSize(omsSku.getSize());
				wmsSkuDetailDTO.setColor(omsSku.getColorName());
				try {
					boolean isSucc = this.warehouseAdapterBridge.syncSku(wmsSkuDetailDTO).isSucess();
					if (isSucc) {
						this.omsSkuDao.updateState(omsSku.getSkuId(), OmsSkuState.SYNED);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			offset += omsSkuList.size();
		} while (omsSkuList.size() == limit);
	}

	@Override
	public boolean updateOmsOrderFormState(long orderId, OmsOrderFormState state) {
		return this.omsOrderFormDao.updateOrderFormState(orderId, state);
	}

	@Override
	public int getTotalSoldByPoId(long poId) {
		return omsOrderFormSkuDao.getTotalSoldByPoId(poId);
	}

	@Override
	public List<OmsOrderForm> getOmsOrderFormListByStateWithMinOrderId(long minOrderId,
			OmsOrderFormState omsOrderFormState, int limit) {

		return omsOrderFormDao.getOmsOrderFormListByStateWithMinOrderId(minOrderId, omsOrderFormState, limit);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.OmsOrderFormService#getOmsOrderFormListByStateWithMinOrderId(long,
	 *      com.xyl.mmall.oms.enums.OmsOrderFormState[], long[], int)
	 */
	@Override
	public List<OmsOrderForm> getOmsOrderFormListByStateWithMinOrderId(long minOrderId,
			OmsOrderFormState[] omsOrderFormStateArray, long[] orderTimeRange, int limit) {
		List<OmsOrderForm> list = this.omsOrderFormDao.getOmsOrderFormListByStateWithMinOrderId(minOrderId,
				omsOrderFormStateArray, orderTimeRange, limit);
		if (CollectionUtil.isNotEmptyOfList(list)) {
			for (OmsOrderForm form : list) {
				form.setOmsOrdeFormSkuList(this.omsOrderFormSkuDao.queryByOmsOrderFormId(form.getOmsOrderFormId(),
						form.getUserId()));
			}
		}
		return list;
	}

	@Override
	public List<OmsOrderForm> getOmsOrderFormListByTimeRange(long startTime, long endTime) {
		List<OmsOrderForm> list = this.omsOrderFormDao.getOmsOrderFormListByTimeRange(startTime, endTime);
		return list;
	}

	@Override
	public OmsOrderForm getOmsOrderFormByOrderId(long omsOrderId) {
		return omsOrderFormDao.getOmsOrderFormByOrderId(omsOrderId);
	}

	@Override
	public List<OmsOrderForm> getOmsOrderFormByUserOrderFormId(long userOrderFormId, long userId) {
		return omsOrderFormDao.getListByUserOrderFormId(userOrderFormId, userId);
	}
}
