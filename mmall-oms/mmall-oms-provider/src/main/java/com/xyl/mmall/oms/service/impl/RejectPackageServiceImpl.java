/**
 * 
 */
package com.xyl.mmall.oms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.OmsOrderPackageDao;
import com.xyl.mmall.oms.dao.OmsOrderPackageSkuDao;
import com.xyl.mmall.oms.dao.OmsSkuDao;
import com.xyl.mmall.oms.dao.RejectPackageDao;
import com.xyl.mmall.oms.dao.RejectPackageSkuDao;
import com.xyl.mmall.oms.dao.WarehouseReturnDao;
import com.xyl.mmall.oms.dto.RejectPackageDTO;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.RejectPackageState;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.meta.OmsOrderPackageSku;
import com.xyl.mmall.oms.meta.OmsSku;
import com.xyl.mmall.oms.meta.RejectPackage;
import com.xyl.mmall.oms.meta.RejectPackageSku;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.meta.WarehouseReturn;
import com.xyl.mmall.oms.service.RejectPackageService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;
import com.xyl.mmall.oms.warehouse.WarehouseReturnOrderCaller;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 *
 */
@Service("rejectPackageService")
public class RejectPackageServiceImpl implements RejectPackageService, WarehouseReturnOrderCaller {

	@Autowired
	private RejectPackageDao rejectPackageDao;

	@Autowired
	private RejectPackageSkuDao rejectPackageSkuDao;

	@Autowired
	private OmsOrderPackageDao orderPackageDao;

	@Autowired
	private OmsOrderPackageSkuDao orderPackageSkuDao;

	@Autowired
	private WarehouseAdapterBridge bridge;

	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private OmsSkuDao omsSkuDao;

	@Autowired
	private WarehouseReturnDao warehouseReturnDao;

	@Autowired
	private WarehouseService warehouseService;

	@Override
	@Transaction
	public boolean agreeRejectPackage(String expressCompany, String expressNO) {
		// 1.先判断一下这个包裹是否已经为拒收件
		if (rejectPackageDao.getByExpressInfo(expressCompany, expressNO) != null) {
			return true;
		}

		// 2.获取原始包裹数据
		OmsOrderPackage pack = orderPackageDao.getByMailNOAndExpressCompany(expressNO, expressCompany);
		if (pack == null) {
			throw new ServiceException("没有该包裹[".concat(expressCompany).concat(",").concat(expressNO)
					.concat("]，请检查参数是否正确。"));
		}
		OmsOrderForm form = omsOrderFormDao.getObjectById(pack.getOmsOrderFormId());
		List<OmsOrderPackageSku> skus = orderPackageSkuDao.getListByPackageId(pack.getPackageId(), pack.getUserId());

		// 3.存储拒收件，先不存储明细，等仓库反馈之后再存储拒收明细
		boolean success = true;
		List<RejectPackageSku> rejectPackageSkus = new ArrayList<RejectPackageSku>();
		RejectPackage rejectPackage = new RejectPackage(pack);
		rejectPackage.setExpressCompany(pack.getExpressCompany());
		rejectPackage.setExpressNO(pack.getMailNO());
		rejectPackage.setWarehouseId(form.getStoreAreaId());
		rejectPackage.setOmsOrderFormId(pack.getOmsOrderFormId());
		rejectPackage.setUserOrderFormId(form.getUserOrderFormId());
		rejectPackage.setWeight(pack.getWeight());
		rejectPackage.setState(RejectPackageState.SENDWMS);
		rejectPackage.setCreateTime(System.currentTimeMillis());
		success = rejectPackageDao.addObject(rejectPackage) != null;
		for (OmsOrderPackageSku sku : skus) {
			RejectPackageSku rejectSku = new RejectPackageSku(sku);
			rejectSku.setState(RejectPackageState.SENDWMS);
			rejectSku.setRejectPackageId(rejectPackage.getRejectPackageId());
			rejectPackageSkus.add(rejectSku);
		}

		// 4.构建wms未签收退货入库单，并推送给仓库
		if (success) {
			WMSReturnOrderDTO returnOrder = createWMSReturnOrder(rejectPackage, rejectPackageSkus);
			success &= bridge.returnSales(returnOrder).isSucess();
		}

		// 5.如果失败，抛出异常回滚事务
		if (!success) {
			throw new ServiceException("agreeRejectPackage [".concat(expressCompany).concat(",").concat(expressNO)
					.concat("] error. "));
		}
		return success;
	}

	private WMSReturnOrderDTO createWMSReturnOrder(RejectPackage pack, List<RejectPackageSku> skus) {
		OmsOrderForm orderForm = omsOrderFormDao.getObjectById(pack.getOmsOrderFormId());
		String salesOrderId = OmsIdUtils.genEmsOrderId(String.valueOf(orderForm.getOmsOrderFormId()),
				WMSOrderType.SALES);
		WMSReturnOrderDTO returnOrder = new WMSReturnOrderDTO();
		WarehouseForm warehouse = warehouseService.getWarehouseById(pack.getWarehouseId());
		returnOrder.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
		returnOrder.setWarehouseCode(warehouse.getWarehouseCode());
		returnOrder.setOrderType(WMSOrderType.R_UA);
		returnOrder.setOrderId(String.valueOf(pack.getRejectPackageId()));
		returnOrder.setOriginalOrderId(salesOrderId);
		returnOrder.setUserId(String.valueOf(orderForm.getUserId()));
		returnOrder.setNote("原始销售单号:".concat(salesOrderId));
		returnOrder.setOrigLogisticCode(pack.getExpressCompany());
		returnOrder.setOrigLogisticCompany(pack.getExpressCompany());
		returnOrder.setOrigLogisticNo(pack.getExpressNO());
		returnOrder.setLogisticCode(pack.getExpressCompany());
		returnOrder.setLogisticCompany(pack.getExpressCompany());
		returnOrder.setLogisticNo(pack.getExpressNO());
		List<WMSSkuDetailDTO> skuDetails = new ArrayList<WMSSkuDetailDTO>();
		for (RejectPackageSku sku : skus) {
			OmsSku omsSku = omsSkuDao.getObjectById(sku.getSkuId());
			WMSSkuDetailDTO wmsSkuDetailDTO = new WMSSkuDetailDTO();
			wmsSkuDetailDTO.setSkuId(sku.getSkuId());
			wmsSkuDetailDTO.setArtNo(omsSku.getBarCode());
			wmsSkuDetailDTO.setName(omsSku.getProductName());
			wmsSkuDetailDTO.setSize(omsSku.getSize());
			wmsSkuDetailDTO.setColor(omsSku.getColorName());
			wmsSkuDetailDTO.setCount(new Float(sku.getCount()).intValue());
			wmsSkuDetailDTO.setNormalCount(wmsSkuDetailDTO.getCount());
			skuDetails.add(wmsSkuDetailDTO);
		}
		returnOrder.setSkuDetails(skuDetails);
		returnOrder.calculateCount();
		return returnOrder;
	}

	@Override
	@Transaction
	public boolean onReturnOrderStateChange(WMSReturnOrderUpdateDTO returnOrderUpdate) throws WarehouseCallerException {
		// 1.基本检查
		if (returnOrderUpdate.getOrderType() != WMSOrderType.R_UA) {
			return true;
		}
		String emsOrderId = OmsIdUtils.genEmsOrderId(returnOrderUpdate.getOrderId(), WMSOrderType.R_UA);
		if (returnOrderUpdate.getSkuDetails() == null) {
			throw new WarehouseCallerException("未签收退货入库单[%s]， 没有SKU明细.", emsOrderId);
		}
		RejectPackage pack = rejectPackageDao.getObjectById(Long.parseLong(returnOrderUpdate.getOrderId()));
		if (pack == null) {
			throw new WarehouseCallerException("未签收退货入库单[%s]有误， 不存在该入库单.", emsOrderId);
		}
		if (pack.getState() == RejectPackageState.RECEIVED) {
			return true;
		}
		boolean success = true;

		// 2.更新拒收件状态
		success &= rejectPackageDao.updateState(pack.getRejectPackageId(), RejectPackageState.RECEIVED,
				RejectPackageState.SENDWMS);

		// 3.数据正确性校验
		OmsOrderPackage omsOrderPackage = orderPackageDao.getByMailNOAndExpressCompany(pack.getExpressNO(),
				pack.getExpressCompany());
		List<OmsOrderPackageSku> oriOmsOrderPackageSku = orderPackageSkuDao.getListByPackageId(
				omsOrderPackage.getPackageId(), omsOrderPackage.getUserId());
		List<Long> wmsSkus = new ArrayList<Long>();
		for (WMSSkuDetailDTO wmsSku : returnOrderUpdate.getSkuDetails()) {
			OmsOrderPackageSku exists = seekOmsOrderPackageSku(oriOmsOrderPackageSku, wmsSku.getSkuId());
			if (exists == null) {
				throw new WarehouseCallerException("未妥投入库单[%s]不需要该sku[%s].", emsOrderId, wmsSku.getSkuId());
			}
			if (exists.getCount() != wmsSku.getCount()) {
				throw new WarehouseCallerException("未妥投入库单[%s]的sku[%s]数量不准确，应为:[%s],却为:[%s]", emsOrderId,
						wmsSku.getSkuId(), exists.getCount(), wmsSku.getCount());
			}
			if (wmsSkus.contains(wmsSku.getSkuId())) {
				throw new WarehouseCallerException("未妥投入库单[%s]sku[%s]重复.", emsOrderId, wmsSku.getSkuId());
			} else {
				wmsSkus.add(wmsSku.getSkuId());
			}
		}

		// 4.存储拒收件的明细并创建二退数据
		OmsOrderForm orderForm = omsOrderFormDao.getObjectById(pack.getOmsOrderFormId());
		List<OmsOrderFormSku> omsOrderFormSku = omsOrderFormSkuDao.queryByOmsOrderFormId(orderForm.getOmsOrderFormId(),
				orderForm.getUserId());
		Map<Long, OmsOrderFormSku> mapOrderFormSku = new HashMap<Long, OmsOrderFormSku>();
		for (OmsOrderFormSku temp : omsOrderFormSku) {
			mapOrderFormSku.put(temp.getSkuId(), temp);
		}

		for (WMSSkuDetailDTO wmsSku : returnOrderUpdate.getSkuDetails()) {
			RejectPackageSku rejectSku = new RejectPackageSku();
			rejectSku.setSkuId(wmsSku.getSkuId());
			rejectSku.setWeight(wmsSku.getWeight());
			rejectSku.setCreateTime(pack.getCreateTime());
			rejectSku.setState(RejectPackageState.RECEIVED);
			rejectSku.setRejectPackageId(pack.getRejectPackageId());
			rejectSku.setCount(wmsSku.getCount());
			success &= rejectPackageSkuDao.addObject(rejectSku) != null;

			OmsSku omsSku = omsSkuDao.getObjectById(wmsSku.getSkuId());
			WarehouseReturn warehouseReturn = new WarehouseReturn();
			warehouseReturn.setWarehouseId(orderForm.getStoreAreaId());
			warehouseReturn.setSkuId(rejectSku.getSkuId());
			warehouseReturn.setPoOrderId(String.valueOf(mapOrderFormSku.get(rejectSku.getSkuId()).getPoId()));
			warehouseReturn.setProductName(omsSku.getProductName());
			warehouseReturn.setCount(new Float(rejectSku.getCount()).intValue());
			warehouseReturn.setDefectiveCount(wmsSku.getDefectiveCount());
			warehouseReturn.setNormalCount(warehouseReturn.getCount());
			warehouseReturn.setType(ReturnType.TWO);
			warehouseReturn.setSupplierId(mapOrderFormSku.get(rejectSku.getSkuId()).getSupplierId());
			warehouseReturn.setCreateTime(System.currentTimeMillis());
			success &= warehouseReturnDao.addObject(warehouseReturn) != null;
		}
		if (!success) {
			throw new ServiceException("onWmsReceivedRejectPackage [".concat(returnOrderUpdate.getOrderId()).concat(
					"] error. "));
		}
		return success;
	}

	private OmsOrderPackageSku seekOmsOrderPackageSku(List<OmsOrderPackageSku> packageSkus, Long skuId) {
		for (OmsOrderPackageSku packageSku : packageSkus) {
			if (packageSku.getSkuId() == skuId) {
				return packageSku;
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.RejectPackageService#queryByCreateTime(long,
	 *      long)
	 */
	@Override
	public List<RejectPackageDTO> queryByCreateTime(long startTime, long endTime) {
		List<RejectPackage> packs = rejectPackageDao.queryByCreateTime(startTime, endTime);
		List<RejectPackageDTO> result = null;
		if (packs != null) {
			result = new ArrayList<RejectPackageDTO>();
			for (RejectPackage pack : packs) {
				RejectPackageDTO dto = new RejectPackageDTO(pack);
				appendRejectPackageDTO(dto);
				result.add(dto);
			}
		}
		return result;
	}
	
	private void appendRejectPackageDTO (RejectPackageDTO dto) {
		WarehouseDTO warehouse = warehouseService.getWarehouseById(dto.getWarehouseId());
		OmsOrderForm order = omsOrderFormDao.getObjectById(dto.getOmsOrderFormId());
		dto.setWarehouseName(warehouse.getWarehouseName());
		dto.setUserName(order.getConsigneeName());
		dto.setUserPhone(StringUtils.isEmpty(order.getConsigneeMobile()) ? order.getConsigneeTel() : order.getConsigneeMobile());
	}

}
