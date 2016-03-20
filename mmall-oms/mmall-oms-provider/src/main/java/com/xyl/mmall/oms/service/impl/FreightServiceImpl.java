package com.xyl.mmall.oms.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.FreightCacheOrderDao;
import com.xyl.mmall.oms.dao.FreightCodDao;
import com.xyl.mmall.oms.dao.FreightDao;
import com.xyl.mmall.oms.dao.FreightReverseDao;
import com.xyl.mmall.oms.dao.FreightUserReturnDao;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.OmsOrderPackageDao;
import com.xyl.mmall.oms.dao.OmsOrderPackageSkuDao;
import com.xyl.mmall.oms.dao.OmsReturnOrderFormDao;
import com.xyl.mmall.oms.dao.RejectPackageDao;
import com.xyl.mmall.oms.dto.FreightCodDTO;
import com.xyl.mmall.oms.dto.FreightDTO;
import com.xyl.mmall.oms.dto.FreightReverseDTO;
import com.xyl.mmall.oms.dto.FreightUserReturnDTO;
import com.xyl.mmall.oms.dto.Region;
import com.xyl.mmall.oms.dto.warehouse.WMSPackageDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.enums.RejectPackageState;
import com.xyl.mmall.oms.enums.WMSExpressCompany;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.enums.WMSPackageState;
import com.xyl.mmall.oms.enums.WMSSalesOrderState;
import com.xyl.mmall.oms.freight.FreightCalcMeta;
import com.xyl.mmall.oms.freight.FreightCalcResult;
import com.xyl.mmall.oms.freight.FreightCalculators;
import com.xyl.mmall.oms.meta.Freight;
import com.xyl.mmall.oms.meta.FreightCacheOrder;
import com.xyl.mmall.oms.meta.FreightCod;
import com.xyl.mmall.oms.meta.FreightReverse;
import com.xyl.mmall.oms.meta.FreightTemplet;
import com.xyl.mmall.oms.meta.FreightUserReturn;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.meta.OmsOrderPackageSku;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;
import com.xyl.mmall.oms.meta.RejectPackage;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.FreightService;
import com.xyl.mmall.oms.service.RegionService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.WarehouseReturnOrderCaller;
import com.xyl.mmall.oms.warehouse.WarehouseSalesOrderCaller;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 *
 */
@Service("freightService")
public class FreightServiceImpl implements FreightService, WarehouseSalesOrderCaller, WarehouseReturnOrderCaller {
	private Logger logger = LoggerFactory.getLogger(FreightServiceImpl.class);

	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private FreightDao freightDao;

	@Autowired
	private FreightCodDao freightCodDao;

	@Autowired
	private FreightReverseDao freightReverseDao;

	@Autowired
	private RejectPackageDao rejectPackageDao;

	@Autowired
	private OmsReturnOrderFormDao omsReturnOrderDao;

	@Autowired
	private FreightUserReturnDao freightUserReturnDao;

	@Autowired
	private OmsOrderPackageDao omsOrderPackageDao;

	@Autowired
	private OmsOrderPackageSkuDao omsOrderPackageSkuDao;

	@Autowired
	private FreightCacheOrderDao freightCacheOrderDao;
	
	@Autowired
	private RegionService regionService;

	private int limit = 30;

	@Override
	public boolean onSalesOrderStateChange(WMSSalesOrderUpdateDTO salesOrder) {
		try {
			return doSalesOrderStateChange(salesOrder);
		} catch (Throwable e) {
			logger.error("", e);
			FreightCacheOrder cacheOrder = new FreightCacheOrder();
			cacheOrder.setOrderId(salesOrder.getOrderId());
			cacheOrder.setOrderType(WMSOrderType.SALES.name());
			cacheOrder.setReason(e.getMessage());
			return freightCacheOrderDao.addObject(cacheOrder) != null;
		}
	}

	private boolean doSalesOrderStateChange(WMSSalesOrderUpdateDTO salesOrder) {
		// 基本检查
		long omsOrderFormId = 0L;
		try {
			omsOrderFormId = Long.parseLong(salesOrder.getOrderId());
		} catch (Exception e) {
			logger.error("错误的销售订单号[".concat(salesOrder.getOrderId()).concat("]."));
			return true;
		}
		OmsOrderForm omsOrderForm = this.omsOrderFormDao.getObjectById(omsOrderFormId);
		if (omsOrderForm == null) {
			logger.error("销售订单[".concat(String.valueOf(omsOrderFormId)).concat("]不存在."));
			return true;
		}

		boolean isSuccess = true;
		// 如果订单为发货状态，则开始计算运费
		if (salesOrder.getState() == WMSSalesOrderState.SHIP) {
			for (PackageInfo info : createPackageInfo(omsOrderForm, salesOrder.getPackages(),
					salesOrder.getOperaterTime())) {
				isSuccess &= calcuateShip(omsOrderForm, info);
			}
		} else if (salesOrder.getState() == WMSSalesOrderState.WAYUPDATE) {
			for (WMSPackageDTO wmsPackageDTO : salesOrder.getPackages()) {
				// 如果订单被签收了，则开始计算cod收款
				if (wmsPackageDTO.getState() == WMSPackageState.SIGNED) {
					Freight freight = freightDao.getByExpressInfo(wmsPackageDTO.getExpressCompany(),
							wmsPackageDTO.getShipNo());
					if (freight == null) {
						FreightCacheOrder o = new FreightCacheOrder();
						o.setOrderId(String.valueOf(omsOrderForm.getOmsOrderFormId()));
						o.setOrderType(WMSOrderType.SALES.name());
						doCalcuateCacheOne(o);
						freight = freightDao.getByExpressInfo(wmsPackageDTO.getExpressCompany(),
								wmsPackageDTO.getShipNo());
					}
					isSuccess &= calcuateDone(omsOrderForm, freight);
				}
			}
		}
		if (!isSuccess) {
			throw new ServiceException("计算订单[" + OmsIdUtils.genEmsOrderId(salesOrder.getOrderId(), WMSOrderType.SALES)
					+ "]运费服务失败.");
		}
		return isSuccess;
	}

	private List<PackageInfo> createPackageInfo(OmsOrderForm omsOrderForm, List<WMSPackageDTO> wmsPackages,
			long shipTime) {
		List<OmsOrderFormSku> omsFormSkus = omsOrderFormSkuDao.queryByOmsOrderFormId(omsOrderForm.getOmsOrderFormId(),
				omsOrderForm.getUserId());
		BigDecimal totalOrderAmount = BigDecimal.ZERO;
		for (OmsOrderFormSku omsFormSku : omsFormSkus) {
			totalOrderAmount = totalOrderAmount.add(omsFormSku.getOriRPrice().multiply(new BigDecimal(omsFormSku.getTotalCount())));
		}
		List<PackageInfo> infos = new ArrayList<PackageInfo>();
		
		for (WMSPackageDTO wmsPackage : wmsPackages) {
			PackageInfo info = new PackageInfo();
			info.expressCompany = wmsPackage.getExpressCompany();
			info.expressNo = wmsPackage.getShipNo();
			info.shipTime = shipTime;
			info.weight = wmsPackage.getWeight();
			for (WMSSkuDetailDTO wmsSku : wmsPackage.getSkuDetails()) {
				OmsOrderFormSku sku = getPackageSku(omsFormSkus, wmsSku.getSkuId());
				info.orderAmount = info.orderAmount.add(sku.getOriRPrice().multiply(new BigDecimal(wmsSku.getCount())));
				info.userPayAmount = info.userPayAmount
						.add(sku.getRprice().multiply(new BigDecimal(wmsSku.getCount())));
			}
			
			// 防止出现金额为0的情况
			if (totalOrderAmount.doubleValue() == 0) {
				info.expUserPrice = omsOrderForm.getExpUserPrice();
			} else {
				info.expUserPrice = omsOrderForm.getExpUserPrice().multiply(info.orderAmount).divide(totalOrderAmount);
			}
			
			if (omsOrderForm.isCashOnDelivery()) {
				info.codAmount = info.codAmount.add(info.userPayAmount).add(info.expUserPrice);
			}
			infos.add(info);
		}

		// 由于乘、除后精度问题，订单金额和快递费和订单总金额、快递费总金额会有部分出入，这里对有出入的部分进行消减以保证和订单金额一致
		// 先判断本次发货是否已经把订单货物都发完了，如果发完了才结算上面的那个金额一致性问题
		if (infos.size() > 0 && isShipDone(omsFormSkus, wmsPackages)) {
			PackageInfo packageChange = infos.get(0);
			BigDecimal userPayOfTotal = BigDecimal.ZERO, expUserPriceOfTotal = BigDecimal.ZERO, orderAmountOfTotal = BigDecimal.ZERO;
			for (PackageInfo info : infos) {
				if (info == packageChange)
					continue;
				userPayOfTotal = userPayOfTotal.add(info.userPayAmount);
				expUserPriceOfTotal = expUserPriceOfTotal.add(info.expUserPrice);
				orderAmountOfTotal = orderAmountOfTotal.add(info.orderAmount);
			}
			packageChange.orderAmount = totalOrderAmount.subtract(orderAmountOfTotal);
			packageChange.userPayAmount = omsOrderForm.getCartRPrice().subtract(userPayOfTotal);
			packageChange.expUserPrice = omsOrderForm.getExpUserPrice().subtract(expUserPriceOfTotal);
			if (omsOrderForm.isCashOnDelivery()) {
				packageChange.codAmount = packageChange.userPayAmount.add(packageChange.expUserPrice);
			}
			// 防止出现负数
			if (packageChange.orderAmount.compareTo(BigDecimal.ZERO) < 1) {
				packageChange.orderAmount = BigDecimal.ZERO;
			}
			if (packageChange.userPayAmount.compareTo(BigDecimal.ZERO) < 1) {
				packageChange.userPayAmount = BigDecimal.ZERO;
			}
			if (packageChange.expUserPrice.compareTo(BigDecimal.ZERO) < 1) {
				packageChange.expUserPrice = BigDecimal.ZERO;
			}
			if (packageChange.codAmount.compareTo(BigDecimal.ZERO) < 1) {
				packageChange.codAmount = BigDecimal.ZERO;
			}
		}
		for (PackageInfo info : infos) {
			info.totalOrderAmount = totalOrderAmount;
			info.formatterRoundDown(2);
		}
		// 对于仓库分多次发货的情况，哎，暂时忽略吧，目前的对接不会出现这样的情况
		return infos;
	}

	private boolean isShipDone(List<OmsOrderFormSku> omsFormSkus, List<WMSPackageDTO> packages) {
		Map<Long, Integer> packageSku = new HashMap<Long, Integer>();
		for (WMSPackageDTO pack : packages) {
			for (WMSSkuDetailDTO sku : pack.getSkuDetails()) {
				long skuId = sku.getSkuId();
				if (!packageSku.containsKey(skuId)) {
					packageSku.put(skuId, sku.getCount());
				} else {
					packageSku.put(skuId, packageSku.get(skuId) + sku.getCount());
				}
			}
		}
		for (OmsOrderFormSku sku : omsFormSkus) {
			if (!packageSku.containsKey(sku.getSkuId()) || packageSku.get(sku.getSkuId()) != sku.getTotalCount()) {
				return false;
			}
		}
		return true;
	}

	private OmsOrderFormSku getPackageSku(List<OmsOrderFormSku> omsFormSkus, long skuId) {
		for (OmsOrderFormSku omsFormSku : omsFormSkus) {
			if (omsFormSku.getSkuId() == skuId) {
				return omsFormSku;
			}
		}
		// 如果系统对接正常，不会出现返回null的情况；实在出现这个现象就抛NullPointException吧
		return null;
	}

	/**
	 * 从订单中获取目的地地址（三级行政区）
	 * 
	 * @param omsOrderForm
	 * @return
	 */
	private Region getThreeRegionFromOrder(OmsOrderForm omsOrderForm) {
		String code = String.valueOf(omsOrderForm.getSectionId());
		Region dest = regionService.getThreeRegionByCode(code);
		if (dest == null) {
			throw new ServiceException("订单[" + omsOrderForm.getUserOrderFormId() + ","
					+ omsOrderForm.getOmsOrderFormId() + "]找不到相应的行政区：".concat(omsOrderForm.getFullAddress()));
		}
		return dest;
	}

	@Transaction
	private boolean calcuateShip(OmsOrderForm omsOrderForm, PackageInfo packageInfo) {
		// 去重，快递公司+快递单号
		if (freightDao.getByExpressInfo(packageInfo.expressCompany, packageInfo.expressNo) != null) {
			return true;
		}
		// 组织计算数据
		WarehouseForm warehouse = warehouseService.getWarehouseById(omsOrderForm.getStoreAreaId());
		WMSExpressCompany settleExpressCompany = WMSExpressCompany.genEnumNameIgnoreCase(warehouse.getExpressCompany());
		FreightCalcMeta calcMeta = new FreightCalcMeta();
		calcMeta.setCod(omsOrderForm.isCashOnDelivery());
		calcMeta.setExpressCompany(settleExpressCompany);
		calcMeta.setWeight(packageInfo.weight);
		calcMeta.setOrigin(regionService.getProvince(String.valueOf(warehouse.getProvinceId())));
		calcMeta.setDest(getThreeRegionFromOrder(omsOrderForm));
		calcMeta.setOrderAmount(packageInfo.orderAmount.doubleValue());
		calcMeta.setCodAmount(packageInfo.codAmount.doubleValue());
		calcMeta.setInsurance(packageInfo.totalOrderAmount.doubleValue() > 1000);
		
		FreightCalcResult result = FreightCalculators.calcuate(calcMeta);
		FreightTemplet templet = result.getTemplate();
		// 保存运费
		Freight freight = new Freight();
		freight.setSettleExpressCompany(settleExpressCompany.name());
		freight.setPackageState(OmsOrderPackageState.SHIP);
		freight.setWarehouseId(omsOrderForm.getStoreAreaId());
		freight.setWarehouseName(warehouse.getWarehouseName());
		freight.setWarehouseType(warehouse.getType());
		freight.setExpressCharge(result.getExpressCharge());
		freight.setWarehouseInsideCharge(result.getWarehouseInsideCharge());
		freight.setStartCost(result.getStartCharge());
		freight.setContinueCost(result.getContinueCharge());
		freight.setOnePrice(result.getOnePrice());
		// 发货后不计算cod手续费，等最终状态后再计算更新
		// freight.setCodCharge(result.getCodCharge());
		freight.setInsuranceCharge(result.getInsuranceCharge());
		freight.setExpressCompany(packageInfo.expressCompany);
		freight.setExpressNo(packageInfo.expressNo);
		freight.setShipTime(packageInfo.shipTime);
		freight.setUserOrderFormId(omsOrderForm.getUserOrderFormId());
		freight.setOmsOrderFormId(omsOrderForm.getOmsOrderFormId());
		freight.setUserPayAmount(packageInfo.userPayAmount);
		freight.setOrderAmount(packageInfo.orderAmount);
		freight.setCodAmount(packageInfo.codAmount);
		freight.setExpUserPrice(packageInfo.expUserPrice);
		freight.setDeliverAddress(omsOrderForm.getFullAddress());
		freight.setCode(calcMeta.isCod());
		freight.setInsurance(calcMeta.isInsurance());
		freight.setWeight(packageInfo.weight);
		freight.setDeliverType(templet.getDeliverType());
		freight.setPickPackageCost(templet.getPickPackageCost());
		freight.setConsumablesCost(templet.getConsumablesCost());
		freight.setInterceptOrderCost(templet.getInterceptOrderCost());
		freight.setCodRate(templet.getRateCod());
		freight.setInsuranceRate(templet.getRateInsurance());
		freight.setOnePrice(templet.isOnePrice());
		freight.setLostCompensate(0);
		return freightDao.addObject(freight) != null;
	}

	@Transaction
	private boolean calcuateReject(OmsOrderForm omsOrderForm, RejectPackage pack, Freight freight) {
		if (freight.getPackageState() != OmsOrderPackageState.SHIP) {
			return true;
		}
		WarehouseForm warehouse = warehouseService.getWarehouseById(freight.getWarehouseId());
		FreightCalcMeta calcMeta = new FreightCalcMeta();
		calcMeta.setCod(freight.isCode());
		calcMeta.setInsurance(freight.isInsurance());
		calcMeta.setReverse(true);
		calcMeta.setExpressCompany(WMSExpressCompany.genEnumNameIgnoreCase(freight.getSettleExpressCompany()));
		calcMeta.setWeight(freight.getWeight().doubleValue());
		calcMeta.setOrigin(regionService.getProvince(String.valueOf(warehouse.getProvinceId())));
		calcMeta.setDest(getThreeRegionFromOrder(omsOrderForm));
		calcMeta.setOrderAmount(freight.getOrderAmount().doubleValue());
		calcMeta.setCodAmount(freight.getCodAmount().doubleValue());
		FreightCalcResult result = FreightCalculators.calcuate(calcMeta);
		if (result == null) {
			// 无运费产生
			return true;
		}
		FreightReverse reverseFreight = new FreightReverse(freight);
		FreightTemplet templet = result.getTemplate();
		reverseFreight.setReverseRate(templet.getRateReverse());
		reverseFreight.setReverseCharge(result.getReverseCharge());
		reverseFreight.setReverseInsuranceCharge(result.getReverseInsuranceCharge());
		reverseFreight.setReverseServiceCharge(result.getReverseServiceCharge());
		reverseFreight.setReverseTotalCharge(result.getReverseTotalCharge());
		reverseFreight.setReturnExpressCompany(pack.getReturnExpressCompany());
		reverseFreight.setReturnExpressNo(pack.getReturnExpressNO());
		reverseFreight.setPackageState(OmsOrderPackageState.REJECT);

		boolean success = freightDao.updatePackageStateAndCodCharge(OmsOrderPackageState.REJECT, result.getCodCharge()
				.doubleValue(), freight.getId(), OmsOrderPackageState.SHIP);
		success &= freightReverseDao.addObject(reverseFreight) != null;
		if (calcMeta.isCod()) {
			FreightCod codFreight = new FreightCod(freight);
			codFreight.setCodCharge(result.getCodCharge());
			codFreight.setReverseRate(templet.getRateReverse());
			codFreight.setReverseCharge(result.getReverseCharge());
			codFreight.setReverseServiceCharge(result.getReverseServiceCharge());
			codFreight.setEducationCharge(result.getEducationCharge());
			codFreight.setCodCollection(result.getCodCollection());
			codFreight.setPackageState(OmsOrderPackageState.REJECT);
			success &= freightCodDao.addObject(codFreight) != null;
		}
		if (!success) {
			throw new ServiceException("calcuateReject[" + freight.getExpressCompany() + "," + freight.getExpressNo()
					+ "] fail.");
		}
		return success;
	}

	@Transaction
	private boolean calcuateDone(OmsOrderForm omsOrderForm, Freight freight) {
		if (freight.getPackageState() != OmsOrderPackageState.SHIP) {
			return true;
		}
		WarehouseForm warehouse = warehouseService.getWarehouseById(omsOrderForm.getStoreAreaId());
		FreightCalcMeta calcMeta = new FreightCalcMeta();
		calcMeta.setCod(freight.isCode());
		calcMeta.setInsurance(freight.isInsurance());
		calcMeta.setExpressCompany(WMSExpressCompany.genEnumNameIgnoreCase(freight.getSettleExpressCompany()));
		calcMeta.setWeight(freight.getWeight().doubleValue());
		calcMeta.setOrigin(regionService.getProvince(String.valueOf(warehouse.getProvinceId())));
		calcMeta.setDest(getThreeRegionFromOrder(omsOrderForm));
		calcMeta.setOrderAmount(freight.getOrderAmount().doubleValue());
		calcMeta.setCodAmount(freight.getCodAmount().doubleValue());
		FreightCalcResult result = FreightCalculators.calcuate(calcMeta);
		if (result == null) {
			// 无运费产生
			return true;
		}
		boolean success = freightDao.updatePackageStateAndCodCharge(OmsOrderPackageState.DONE, result.getCodCharge()
				.doubleValue(), freight.getId(), OmsOrderPackageState.SHIP);
		if (calcMeta.isCod()) {
			// 去重，快递公司 + 快递单号
			FreightTemplet templet = result.getTemplate();
			FreightCod codFreight = new FreightCod(freight);
			codFreight.setPackageState(OmsOrderPackageState.DONE);
			codFreight.setCodCharge(result.getCodCharge());
			codFreight.setReverseRate(templet.getRateReverse());
			codFreight.setReverseCharge(result.getReverseCharge());
			codFreight.setReverseServiceCharge(result.getReverseServiceCharge());
			codFreight.setEducationCharge(result.getEducationCharge());
			codFreight.setCodCollection(result.getCodCollection());
			success &= freightCodDao.addObject(codFreight) != null;
		}
		if (!success) {
			throw new ServiceException("calcuateReject[" + freight.getExpressCompany() + "," + freight.getExpressNo()
					+ "] fail.");
		}
		return success;
	}

	@Transaction
	private boolean calcuateReturnWmsReceived(OmsReturnOrderForm returnOrder, long receiveTime) {
		if (freightUserReturnDao.getByExpressInfo(returnOrder.getExpressCompany(), returnOrder.getMailNO()) != null) {
			return true;
		}
		FreightUserReturn userReturnOrder = new FreightUserReturn();
		// 根据报告查询oms订单，然后查询出所在仓库
		OmsOrderPackage pack = omsOrderPackageDao.getObjectById(returnOrder.getReturnPackageId());
		OmsOrderForm form = omsOrderFormDao.getObjectById(pack.getOmsOrderFormId());
		userReturnOrder.setWarehouseId(form.getStoreAreaId());
		WarehouseForm warehouse = warehouseService.getWarehouseById(form.getStoreAreaId());
		WMSExpressCompany expressCompany = WMSExpressCompany.genEnumNameIgnoreCase(warehouse.getExpressCompany());
		userReturnOrder.setUserReturnOrderFormId(returnOrder.getId());
		userReturnOrder.setSettleExpressCompany(expressCompany.name());
		userReturnOrder.setWarehouseName(warehouse.getWarehouseName());
		userReturnOrder.setOmsOrderFormId(form.getOmsOrderFormId());
		userReturnOrder.setUserOrderFormId(form.getUserOrderFormId());
		userReturnOrder.setPayAmount(form.getCartRPrice().add(form.getExpUserPrice()));
		userReturnOrder.setReturnExpressCompany(returnOrder.getExpressCompany());
		userReturnOrder.setReturnExpressNo(returnOrder.getMailNO());
		userReturnOrder.setExpressCompany(pack.getExpressCompany());
		userReturnOrder.setExpressNo(pack.getMailNO());
		userReturnOrder.setReturnAddress(warehouse.getReturnAddress());
		userReturnOrder.setWmsReceivedTime(receiveTime);
		userReturnOrder.setReturnServiceCharge(new BigDecimal(FreightCalculators
				.getUserReturnServieCharge(expressCompany)));
		userReturnOrder.setState(OmsReturnOrderFormState.CONFIRMED);
		return freightUserReturnDao.addObject(userReturnOrder) != null;
	}

	@Override
	public boolean onReturnOrderStateChange(WMSReturnOrderUpdateDTO returnOrderUpdate) throws WarehouseCallerException {
		try {
			return doReturnOrderStateChange(returnOrderUpdate);
		} catch (Throwable e) {
			logger.error("", e);
			FreightCacheOrder cacheOrder = new FreightCacheOrder();
			cacheOrder.setOrderId(returnOrderUpdate.getOrderId());
			cacheOrder.setOrderType(returnOrderUpdate.getOrderType().name());
			cacheOrder.setReason(e.getMessage());
			return freightCacheOrderDao.addObject(cacheOrder) != null;
		}
	}

	private boolean doReturnOrderStateChange(WMSReturnOrderUpdateDTO returnOrderUpdate) {
		boolean isSuccess = true;
		WMSOrderType orderType = returnOrderUpdate.getOrderType();
		if (orderType == WMSOrderType.R_UA) {
			// 基本检查
			RejectPackage pack = rejectPackageDao.getObjectById(Long.parseLong(returnOrderUpdate.getOrderId()));
			if (pack == null) {
				throw new WarehouseCallerException("未签收退货入库单号[%s]有误， 不存在该入库单.", OmsIdUtils.genEmsOrderId(
						returnOrderUpdate.getOrderId(), orderType));
			}
			Freight freight = freightDao.getByExpressInfo(pack.getExpressCompany(), pack.getExpressNO());
			if (freight == null) {
				FreightCacheOrder o = new FreightCacheOrder();
				o.setOrderId(returnOrderUpdate.getOrderId());
				o.setOrderType(orderType.name());
				doCalcuateCacheOne(o);
				freight = freightDao.getByExpressInfo(pack.getExpressCompany(), pack.getExpressNO());
			}
			OmsOrderForm omsOrderForm = this.omsOrderFormDao.getObjectById(pack.getOmsOrderFormId());
			isSuccess &= calcuateReject(omsOrderForm, pack, freight);
		} else if (orderType == WMSOrderType.RETURN) {
			// 基本检查
			OmsReturnOrderForm returnOrder = omsReturnOrderDao.getObjectById(Long.parseLong(returnOrderUpdate
					.getOrderId()));
			if (returnOrder == null) {
				throw new WarehouseCallerException("%s号[%s]有误， 不存在该入库单.", orderType.getDesc(),
						OmsIdUtils.genEmsOrderId(returnOrderUpdate.getOrderId(), WMSOrderType.RETURN));
			}
			isSuccess &= calcuateReturnWmsReceived(returnOrder, returnOrderUpdate.getReceiveTime());
		}
		if (!isSuccess) {
			throw new ServiceException("计算订单[" + OmsIdUtils.genEmsOrderId(returnOrderUpdate.getOrderId(), orderType)
					+ "]运费服务失败.");
		}
		return isSuccess;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.FreightService#onLostPackagee(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean onLostPackagee(String expressCompany, String expressNo) {
		Freight freight = freightDao.getByExpressInfo(expressCompany, expressNo);
		return freightDao.updatePackageStateAndCodCharge(OmsOrderPackageState.LOST, freight.getCodCharge()
				.doubleValue(), freight.getId(), OmsOrderPackageState.SHIP);
	}

	@Override
	public boolean doCalcuateCache() {
		long startMinCacheId = 0L;
		List<FreightCacheOrder> orders = freightCacheOrderDao.getByMinCacheId(startMinCacheId, limit);
		while (orders != null && orders.size() > 0) {
			for (FreightCacheOrder o : orders) {
				try {
					doCalcuateCacheOne(o);
				} catch (Throwable e) {
					logger.error("", e);
					o.setReason(e.getMessage());
					freightCacheOrderDao.updateReason(o);
				}
				startMinCacheId = o.getId();
			}
			orders = freightCacheOrderDao.getByMinCacheId(startMinCacheId, limit);
		}
		return true;
	}

	@Transaction
	private void doCalcuateCacheOne(FreightCacheOrder o) {
		boolean isSuccess = true;
		WMSOrderType type = WMSOrderType.genEnumNameIgnoreCase(o.getOrderType());
		switch (type) {
		case SALES:
			OmsOrderForm omsOrderForm = this.omsOrderFormDao.getObjectById(Long.parseLong(o.getOrderId()));
			// 获取该订单的所有包裹
			List<OmsOrderPackage> packages = this.omsOrderPackageDao.getListByOmsOrderFormId(
					omsOrderForm.getOmsOrderFormId(), omsOrderForm.getUserId());
			Map<String, OmsOrderPackageState> packageStateMap = new HashMap<String, OmsOrderPackageState>();
			List<WMSPackageDTO> wmsPackages = new ArrayList<WMSPackageDTO>();
			for (OmsOrderPackage pack : packages) {
				packageStateMap.put(pack.getMailNO(), pack.getOmsOrderPackageState());
				WMSPackageDTO packDto = new WMSPackageDTO();
				packDto.setExpressCompany(pack.getExpressCompany());
				packDto.setShipNo(pack.getMailNO());
				packDto.setWeight(pack.getWeight());
				List<OmsOrderPackageSku> skus = omsOrderPackageSkuDao.getListByPackageId(pack.getPackageId(),
						pack.getUserId());
				for (OmsOrderPackageSku sku : skus) {
					WMSSkuDetailDTO wmsSku = new WMSSkuDetailDTO();
					wmsSku.setSkuId(sku.getSkuId());
					wmsSku.setCount((int) sku.getCount());
					wmsSku.setWeight(sku.getWeight());
					packDto.addSkuDetail(wmsSku);
				}
				packDto.calculateCount();
				wmsPackages.add(packDto);
			}
			for (PackageInfo info : createPackageInfo(omsOrderForm, wmsPackages, omsOrderForm.getShipTime())) {
				isSuccess &= calcuateShip(omsOrderForm, info);
				Freight freight = freightDao.getByExpressInfo(info.expressCompany, info.expressNo);
				if (packageStateMap.get(info.expressNo) == OmsOrderPackageState.DONE) {
					isSuccess &= this.calcuateDone(omsOrderForm, freight);
				}
			}
			break;
		case RETURN:
			OmsReturnOrderForm returnOrder = omsReturnOrderDao.getObjectById(Long.parseLong(o.getOrderId()));
			if (returnOrder.getConfirmTime() > 0) {
				isSuccess &= calcuateReturnWmsReceived(returnOrder, returnOrder.getConfirmTime());
			}
			break;
		case R_UA:
			RejectPackage pack = rejectPackageDao.getObjectById(Long.parseLong(o.getOrderId()));
			if (pack.getState() != RejectPackageState.RECEIVED) {
				break;
			}
			omsOrderForm = this.omsOrderFormDao.getObjectById(pack.getOmsOrderFormId());
			Freight freight = freightDao.getByExpressInfo(pack.getExpressCompany(), pack.getExpressNO());
			if (freight == null) {
				FreightCacheOrder tmp = new FreightCacheOrder();
				tmp.setOrderId(String.valueOf(omsOrderForm.getOmsOrderFormId()));
				tmp.setOrderType(WMSOrderType.SALES.name());
				doCalcuateCacheOne(tmp);
				freight = freightDao.getByExpressInfo(pack.getExpressCompany(), pack.getExpressNO());
			}
			isSuccess &= calcuateReject(omsOrderForm, pack, freight);
			break;
		default:
			isSuccess = false;
			throw new ServiceException("该订单[".concat(o.getOrderId()).concat(",").concat(type.getDesc())
					.concat("]无运费处理。"));
		}
		freightCacheOrderDao.deleteById(o.getId());
		if (!isSuccess) {
			throw new ServiceException("计算运费失败.");
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.FreightService#queryFreight(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<FreightDTO> queryFreight(String expressCompany, long warehouseId, long startTime, long endTime) {
		List<Freight> freights = freightDao.queryFreight(expressCompany, warehouseId, startTime, endTime);
		if (freights != null) {
			List<FreightDTO> result = ReflectUtil.convertList(FreightDTO.class, freights, false);
			return result;
		} else {
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.FreightService#queryFreightCod(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<FreightCodDTO> queryFreightCod(String expressCompany, long warehouseId, long startTime, long endTime) {
		List<FreightCod> freights = freightCodDao.queryFreight(expressCompany, warehouseId, startTime, endTime);
		if (freights != null) {
			List<FreightCodDTO> result = ReflectUtil.convertList(FreightCodDTO.class, freights, false);
			return result;
		} else {
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.FreightService#queryFreightReverse(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<FreightReverseDTO> queryFreightReverse(String expressCompany, long warehouseId, long startTime,
			long endTime) {
		List<FreightReverse> freights = freightReverseDao.queryFreight(expressCompany, warehouseId, startTime, endTime);
		if (freights != null) {
			List<FreightReverseDTO> result = ReflectUtil.convertList(FreightReverseDTO.class, freights, false);
			return result;
		} else {
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.FreightService#queryFreightUserReturn(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<FreightUserReturnDTO> queryFreightUserReturn(String expressCompany, long warehouseId, long startTime,
			long endTime) {
		List<FreightUserReturn> freights = freightUserReturnDao.queryFreight(expressCompany, warehouseId, startTime,
				endTime);
		if (freights != null) {
			List<FreightUserReturnDTO> result = ReflectUtil.convertList(FreightUserReturnDTO.class, freights, false);
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 包裹数据
	 * 
	 * @author hzzengchengyuan
	 *
	 */
	class PackageInfo {

		String expressCompany;

		String expressNo;

		double weight;

		long shipTime;

		BigDecimal orderAmount = BigDecimal.ZERO;

		BigDecimal expUserPrice = BigDecimal.ZERO;

		BigDecimal userPayAmount = BigDecimal.ZERO;

		BigDecimal codAmount = BigDecimal.ZERO;
		
		/* 该包裹对应的订单总额 */
		BigDecimal totalOrderAmount = BigDecimal.ZERO;

		void formatterRoundDown(int decimal) {
			orderAmount = orderAmount.setScale(decimal, RoundingMode.DOWN);
			expUserPrice = expUserPrice.setScale(decimal, RoundingMode.DOWN);
			userPayAmount = userPayAmount.setScale(decimal, RoundingMode.DOWN);
			codAmount = codAmount.setScale(decimal, RoundingMode.DOWN);
		}
	}
}
