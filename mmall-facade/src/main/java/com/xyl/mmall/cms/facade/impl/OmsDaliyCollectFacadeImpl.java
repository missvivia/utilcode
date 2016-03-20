package com.xyl.mmall.cms.facade.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.netease.print.common.constant.CalendarConst;
import com.netease.print.common.util.CalendarUtil;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.OmsDaliyCollectFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.oms.enums.BociTime;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.report.meta.OmsShipOutReport;
import com.xyl.mmall.oms.report.service.OmsShipOutReportService;
import com.xyl.mmall.oms.service.ShipOrderService;
import com.xyl.mmall.oms.service.ShipSkuService;
import com.xyl.mmall.oms.service.WarehouseService;

/**
 * @author zb
 *
 */
@Facade("OmsDaliyCollectFacade")
public class OmsDaliyCollectFacadeImpl implements OmsDaliyCollectFacade {

	@Resource
	private ShipOrderService shipOrderService;

	@Resource
	private ShipSkuService shipSkuService;

	@Resource
	private OmsShipOutReportService omsShipOutService;

	@Resource
	private WarehouseService warehouseService;

	@Resource
	private BusinessFacade businessFacade;

	private Map<Long, BusinessDTO> mapCache = new HashMap<Long, BusinessDTO>();

	private Map<Long, WarehouseForm> warehouseCache = new HashMap<Long, WarehouseForm>();

	private BigDecimal normalRatioConstant = new BigDecimal("0.6");

	private BigDecimal arrivalRatioConstant = new BigDecimal("0.4");

	@Override
	public void collectShipData() {
		// 获取需要汇总的发货单
		int limit = 200, offset = 0;
		long[] timeRange = genQueryTimes();
		// 必须今天4点之后才能统计
		long startCalTime = timeRange[1] + CalendarConst.DAY_TIME;
		if (System.currentTimeMillis() < startCalTime)
			return;

		long dateTime = timeRange[1];
		Set<Long> processedSupplierId = new HashSet<Long>();

		List<ShipOrderForm> shipOrderList = shipOrderService.getListByCollectTime(timeRange[0], timeRange[1], limit,
				offset);
		while (CollectionUtils.isNotEmpty(shipOrderList)) {

			//
			for (ShipOrderForm shipOrderForm : shipOrderList) {
				long supplierId = shipOrderForm.getSupplierId();
				if (processedSupplierId.contains(supplierId))
					continue;
				OmsShipOutReport report = omsShipOutService.getOmsShipOutReport(dateTime, supplierId);
				if (report != null)
					continue;

				List<ShipOrderForm> list = this.shipOrderService.getListByCollectTime(supplierId, timeRange[0],
						timeRange[1]);
				// 最晚的入库时间
				long lastArrivalTime = 0L;
				// 到货总数
				int total = 0;
				// 正品数量
				int arrivedNormalCount = 0;

				for (ShipOrderForm dayShipOrderForm : list) {
					if (dayShipOrderForm.getArrivalTime() > lastArrivalTime)
						lastArrivalTime = dayShipOrderForm.getArrivalTime();

					total += dayShipOrderForm.getTotal();

					List<ShipSkuItemForm> shipSkuItemFormList = shipSkuService.getShipSkuList(dayShipOrderForm
							.getShipOrderId());
					for (ShipSkuItemForm shipSkuItemForm : shipSkuItemFormList) {
						arrivedNormalCount += shipSkuItemForm.getArrivedNormalCount();
					}
				}
				MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
				// 良品率
				BigDecimal normalRatio = new BigDecimal(arrivedNormalCount * 100).divide(new BigDecimal(total), mc);
				// "到货及时率"，在下午16点前入库完成的都算合格100分，16点之后的都算不合格0分
				BigDecimal arrivalRatio = (lastArrivalTime != 0 && lastArrivalTime <= timeRange[1]) ? new BigDecimal(
						"1") : new BigDecimal("0");
				// 得分
				BigDecimal score = normalRatio.multiply(normalRatioConstant).add(
						arrivalRatio.multiply(arrivalRatioConstant));

				OmsShipOutReport rp = new OmsShipOutReport();
				rp.setDateTime(dateTime);
				rp.setSupplierId(supplierId);
				rp.setSupplierName(getBusinessDTO(supplierId).getCompanyName());
				rp.setWarehouseId(shipOrderForm.getStoreAreaId());
				rp.setWarehouseName(getWarehouseForm(rp.getWarehouseId()).getWarehouseName());
				rp.setTotal(total);
				rp.setArrivalTime(lastArrivalTime);
				rp.setArrivedNormalCount(arrivedNormalCount);
				rp.setScore(score);
				rp.setNormalRatio(normalRatio);
				rp.setArrivalRatio(arrivalRatio);

				// 保存到数据库
				omsShipOutService.addOmsShipOutReport(rp);
			}

			offset += limit;
			shipOrderList = shipOrderService.getListByCollectTime(timeRange[0], timeRange[1], limit, offset);
		}

	}

	public static long[] genQueryTimes() {
		Calendar dayZero = CalendarUtil.getZeroHour(System.currentTimeMillis());
		Calendar cur = Calendar.getInstance();
		int hour = cur.get(Calendar.HOUR);
		if (hour < BociTime.THIRD_BATCH.getIntValue()) {
			dayZero.add(Calendar.DAY_OF_MONTH, -1);
		}
		long endTime = dayZero.getTimeInMillis() + BociTime.THIRD_BATCH.getIntValue() * CalendarConst.HOUR_TIME;
		dayZero.add(Calendar.DAY_OF_MONTH, -1);
		long startTime = dayZero.getTimeInMillis() + BociTime.THIRD_BATCH.getIntValue() * CalendarConst.HOUR_TIME;
		return new long[] { startTime, endTime };
	}

	public static void main(String[] args) {
		long[] times = genQueryTimes();
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		System.out.println(myFmt.format(new Date(times[0])));
		System.out.println(myFmt.format(new Date(times[1])));
		long startCalTime = times[1] + CalendarConst.DAY_TIME;
		System.out.println(myFmt.format(new Date(startCalTime)));
	}

	private BusinessDTO getBusinessDTO(long supplierId) {
		if (!mapCache.containsKey(supplierId)) {
			BusinessDTO dto = businessFacade.getBusinessById(supplierId);
			if (dto != null) {
				mapCache.put(supplierId, dto);
			}
		}
		return mapCache.get(supplierId);
	}

	private WarehouseForm getWarehouseForm(long warehouseId) {
		if (!warehouseCache.containsKey(warehouseId)) {
			WarehouseForm form = warehouseService.getWarehouseById(warehouseId);
			if (form != null) {
				warehouseCache.put(warehouseId, form);
			}
		}
		return warehouseCache.get(warehouseId);
	}

}
