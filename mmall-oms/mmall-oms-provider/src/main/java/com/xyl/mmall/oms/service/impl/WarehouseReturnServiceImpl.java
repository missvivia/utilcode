package com.xyl.mmall.oms.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.ShipSkuDao;
import com.xyl.mmall.oms.dao.WarehouseReturnDao;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.WarehouseReturn;
import com.xyl.mmall.oms.service.WarehouseReturnService;

@Service("warehouseReturnService")
public class WarehouseReturnServiceImpl implements WarehouseReturnService {

	@Autowired
	private ShipSkuDao shipSkuDao;

	@Autowired
	private PickSkuDao pickSkuDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private WarehouseReturnDao warehouseReturnDao;

	@Override
	@Transaction
	public boolean generate2WarehouseReturn(long poId) {
		// po下已经到货的sku正品数量
		Map<Long, Integer> arrivedSkuCountMap = shipSkuDao.getArrivedNormalCountBySkuInPo(poId);
		// po下已经发货的sku数量
		Map<Long, Integer> outSkuCountMap = new LinkedHashMap<Long, Integer>();

		Map<Long, OmsOrderFormSku> omsOrderFormSkuMap = new LinkedHashMap<Long, OmsOrderFormSku>();

		// 从Mmall_Oms_PickSkuItem上获取对应的订单，统计已经发货的订单
		List<Long> omsOrderFormIdList = pickSkuDao.getOmsOrderFormIdListInPo(poId);
		for (long omsOrderFormId : omsOrderFormIdList) {
			OmsOrderForm omsOrderForm = this.omsOrderFormDao.getOmsOrderFormByOrderId(omsOrderFormId);

			List<OmsOrderFormSku> omsOrderFormSkuList = omsOrderFormSkuDao.queryByOmsOrderFormId(
					omsOrderForm.getOmsOrderFormId(), omsOrderForm.getUserId());

			// 需要统计仓库反馈已经发货的订单
			boolean needCount = omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.SHIP
					|| omsOrderForm.getOmsOrderFormState() == OmsOrderFormState.NOTICE_SHIP;

			for (OmsOrderFormSku omsOrderFormSku : omsOrderFormSkuList) {
				// backup，下面会用到，实际生成退货明细的时候需要商品名字等信息
				if (!omsOrderFormSkuMap.containsKey(omsOrderFormSku.getSkuId()))
					omsOrderFormSkuMap.put(omsOrderFormSku.getSkuId(), omsOrderFormSku);

				// 统计已经到货的情况
				if (needCount) {
					if (!outSkuCountMap.containsKey(omsOrderFormSku.getSkuId()))
						outSkuCountMap.put(omsOrderFormSku.getSkuId(), 0);
					int newCount = outSkuCountMap.get(omsOrderFormSku.getSkuId()) + omsOrderFormSku.getTotalCount();
					outSkuCountMap.put(omsOrderFormSku.getSkuId(), newCount);
				}
			}

		}
		boolean isSucc = true;
		for (Long skuId : arrivedSkuCountMap.keySet()) {
			int remain = arrivedSkuCountMap.get(skuId)
					- (outSkuCountMap.get(skuId) == null ? 0 : outSkuCountMap.get(skuId));
			if (remain <= 0)
				continue;

			OmsOrderFormSku omsOrderFormSku = omsOrderFormSkuMap.get(skuId);
			if (omsOrderFormSku == null) {
				throw new ServiceNoThrowException(
						"WarehouseReturnServiceImpl.generate2WarehouseReturn error,omsOrderFormSku=null,poid=" + poId
								+ ",skuId=" + skuId);
			}

			// 对比入库数量和出库数量
			WarehouseReturn warehouseReturn = new WarehouseReturn();
			// 这里请将SupplierId设为代理商id，不论实际sku是属于谁
			warehouseReturn.setSupplierId(omsOrderFormSku.getSupplierId());
			warehouseReturn.setPoOrderId(String.valueOf(poId));
			warehouseReturn.setSkuId(skuId);
			warehouseReturn.setProductName(omsOrderFormSku.getProductName());
			warehouseReturn.setCount(remain);
			warehouseReturn.setNormalCount(remain);
			warehouseReturn.setDefectiveCount(0);
			warehouseReturn.setType(ReturnType.TWO);
			warehouseReturn.setCreateTime(System.currentTimeMillis());
			warehouseReturn.setWarehouseId(omsOrderFormSku.getStoreAreaId());
			isSucc = isSucc && (warehouseReturnDao.addObject(warehouseReturn) != null);
		}

		if (!isSucc) {
			throw new ServiceNoThrowException("WarehouseReturnServiceImpl.generate2WarehouseReturn error.poid=" + poId);
		}
		return isSucc;
	}
}
