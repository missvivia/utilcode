package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.facade.OmsReportFacade;
import com.xyl.mmall.cms.vo.ReceiptVO;
import com.xyl.mmall.cms.vo.SendOutVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.oms.dto.OmsReportListDTO;
import com.xyl.mmall.oms.dto.SendOutReportDTO;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.report.meta.OmsDelayReport;
import com.xyl.mmall.oms.report.meta.OmsMakerReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;
import com.xyl.mmall.oms.report.meta.OmsNoReturnReport;
import com.xyl.mmall.oms.report.meta.OmsReceiptReport;
import com.xyl.mmall.oms.report.meta.OmsReturnReport;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutReport;
import com.xyl.mmall.oms.report.service.OmsMakerReportService;
import com.xyl.mmall.oms.report.service.OmsNoReceiptService;
import com.xyl.mmall.oms.report.service.OmsOrderReportService;
import com.xyl.mmall.oms.report.service.OmsReceiptService;
import com.xyl.mmall.oms.report.service.OmsReturnService;
import com.xyl.mmall.oms.report.service.OmsSendOutService;
import com.xyl.mmall.oms.report.service.OmsShipOutReportService;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.OmsOrderPackageService;
import com.xyl.mmall.oms.service.WarehouseService;

@Facade("OmsReportFacade")
public class OmsReportFacadeImpl implements OmsReportFacade {

	@Resource
	private OmsSendOutService omsSendOutService;

	@Resource
	private OmsReceiptService omsReceiptService;

	@Resource
	private OmsNoReceiptService omsNoReceiptService;

	@Resource
	private OmsReturnService omsReturnService;

	@Resource
	private POProductService pOProductService;

	@Resource
	private WarehouseService warehouseService;

	@Resource
	private OmsShipOutReportService omsShipOutReportService;

	@Resource
	private OmsOrderReportService omsOrderReportService;

	@Resource
	private OmsMakerReportService omsMakerReportService;

	/**
	 * 全国发货概括统计和各省发货统计数据入口
	 * 
	 * @param date
	 */
	@Override
	public void processSendOUt(long date) {
		// step1.准备基础数据存入Mmall_Oms_Report_SendOutReport
		omsSendOutService.addData(date);
		// step2.全国发货概括数据存入Mmall_Oms_Report_SendOutCountryForm
		omsSendOutService.saveCountryData(date);
		// step3.各省发货统计数据存入Mmall_Oms_Report_SendOutProvinceForm
		omsSendOutService.saveProvinceData(date);
	}

	/**
	 * 分页查询全国发货统计
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	@Override
	public List<OmsSendOutCountryForm> querySendOutCountryByDay(long startDay, long endDay, DDBParam ddbParam) {
		return omsSendOutService.querySendOutCountryByDate(startDay, endDay, ddbParam);
	}

	/**
	 * 分页查询各省发货统计
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	@Override
	public List<OmsSendOutProvinceForm> querySendOutProvinceByDay(long warehouseId, long startDay,long endDay,List<Long> warehouseIds,
			DDBParam ddbParam) {
		return omsSendOutService.querySendOutProvinceByDate(warehouseId, startDay, endDay,warehouseIds, ddbParam);
	}

	/**
	 * 各省订单签收状态统计
	 * 
	 * @param date
	 */
	@Override
	public boolean processReceipt(long date) {
		// step1.准备基础数据存入Mmall_Oms_Report_ReceiptDaily
		boolean succ = omsReceiptService.saveDataByDay(date);

		// step2.各省订单签收状态统计存入Mmall_Oms_Report_ReceiptForm
		succ = succ && omsReceiptService.saveReportDataByDay(date);
		
		return succ;
	}

	/**
	 * 各省订单签收状态统计
	 * 
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	@Override
	public List<OmsReceiptReport> queryReceiptReporttByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds,
			DDBParam ddbParam) {
		return omsReceiptService.queryReceiptReportByWarehouseAndDay(warehouseId, startDay, endDay, warehouseIds,ddbParam);
	}

	/**
	 * 各省未签收订单归类统计以及未签收明细表
	 * 
	 * @param date
	 */
	@Override
	public void processNoReceipt(long date) {
		// 计算7天前的数据
		date = date - 7 * 24 * 3600 * 1000;
		// step1.准备基础数据存入Mmall_Oms_Report_NoReceiptDetail
		// step1.1同省
		omsNoReceiptService.saveData(date);
		// step1.2外省
		// omsNoReceiptService.saveData(date,1);
		// step2.各省未签收订单归类统计
		omsNoReceiptService.saveNoReceiptReport(date);

		// setp3.未签收明细表
		omsNoReceiptService.saveNoReceiptDetailReport(date);
	}

	/**
	 * 分页查询各省未牵手订单归类统计
	 * 
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	@Override
	public List<OmsNoReceiptReport> queryNoReceiptReport(long warehouseId, long startDay, long endDay,List<Long> warehouseIds, DDBParam ddbParam) {
		return omsNoReceiptService.queryNoReceiptReport(warehouseId, startDay, endDay,warehouseIds, ddbParam);
	}

	/**
	 * 查询未签收明细表
	 * 
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	@Override
	public List<OmsNoReceiptDetailReport> queryNoReceiptDetailReport(long warehouseId, long startDay, long endDay,
			List<Long> warehouseIds,DDBParam ddbParam) {
		return omsNoReceiptService.queryNoReceiptDetailReport(warehouseId, startDay, endDay,warehouseIds, ddbParam);
	}

	/**
	 * 返货统计报表和未返回单量明细
	 * 
	 * @param date
	 */
	@Override
	public void processReturn(long date) {
		// 计算7天前的数据
		date = date - 7 * 24 * 3600 * 1000;
		// step1.返货统计报表
		omsReturnService.saveReturnData(date);

		// step2.1未返回单量明细
		List<OmsNoReturnReport> list = omsReturnService.getNoReturnDataByDate(date);
		if (list != null && list.size() > 0) {
			for (OmsNoReturnReport report : list) {
				PoProduct poProduct = pOProductService.getProductBySkuId(report.getSkuId());
				if (poProduct != null) {
					report.setProductName(poProduct.getProductName());
				}
			}
		}
		// step2.2存储未返回单量明细
		omsReturnService.saveNoReturnData(list);
	}

	/**
	 * 根据仓库和时间查询返货数据
	 * 
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	@Override
	public List<OmsReturnReport> queryReturnReportByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds,
			DDBParam ddbParam) {
		return omsReturnService.queryReturnReportByWarehouseAndDay(warehouseId, startDay, endDay, warehouseIds,ddbParam);
	}

	/**
	 * 根据仓库和时间查询未返货数据
	 * 
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	@Override
	public List<OmsNoReturnReport> queryNoReturnReportByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds,
			DDBParam ddbParam) {
		return omsReturnService.queryNoReturnReportByWarehouseAndDay(warehouseId, startDay, endDay,warehouseIds, ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OmsReportFacade#getSendOutVOByTime(long,
	 *      long)
	 */
	@Override
	public List<SendOutVO> getSendOutVOByTime(long startTime, long endTime) {
		List<OmsSendOutReport> list = omsSendOutService.getOmsSendOutReportByDuration(startTime, endTime);
		List<SendOutVO> result = null;
		if (list != null && list.size() > 0) {
			result = new ArrayList<SendOutVO>();
			Map<Long, ArrayList<OmsSendOutReport>> map = new HashMap<Long, ArrayList<OmsSendOutReport>>();
			for (OmsSendOutReport sendOutReport : list) {
				if (map.get(sendOutReport.getDate()) == null) {
					ArrayList<OmsSendOutReport> tempList = new ArrayList<OmsSendOutReport>();
					tempList.add(sendOutReport);
					map.put(sendOutReport.getDate(), tempList);
				} else {
					ArrayList<OmsSendOutReport> tempList = map.get(sendOutReport);
					tempList.add(sendOutReport);
					map.put(sendOutReport.getDate(), tempList);
				}
			}
			Iterator<Long> it = map.keySet().iterator();
			while (it.hasNext()) {
				Long date = it.next();
				ArrayList<OmsSendOutReport> reportList = map.get(date);
				ArrayList<SendOutReportDTO> reportDTOList = new ArrayList<SendOutReportDTO>();
				SendOutVO sendOutVo = new SendOutVO();
				// 获取每天的总数
				int sum = omsSendOutService.getTotalByDay(date);
				for (OmsSendOutReport report : reportList) {
					SendOutReportDTO reportDTO = new SendOutReportDTO(report);
					reportDTO.setRate((report.getCod() + report.getNoncode()) / sum * 1.0);
					reportDTOList.add(reportDTO);
				}
				sendOutVo.setCount(sum);
				sendOutVo.setList(reportDTOList);
				sendOutVo.setDate(date);
			}
		}
		return result;
	}

	@Override
	public List<ReceiptVO> getReceiptVOByTime(long startDay, long endDay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WarehouseDTO[] getWarehouseList(List<Long> areaLists) {
		return warehouseService.getAllWarehouseByIdList(areaLists);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OmsReportFacade#queryOmsShipOutReport(long,
	 *      long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg queryOmsShipOutReport(long warehouseId, long startDay, long endDay, DDBParam ddbParam) {
		if (warehouseId > 0)
			return omsShipOutReportService.getOmsShipOutReport(startDay, endDay, warehouseId, ddbParam);
		else
			return omsShipOutReportService.getOmsShipOutReport(startDay, endDay, ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OmsReportFacade#getOrderReportList(long,
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg getOrderReportList(long beginTime, long endTime, DDBParam param) {
		return omsOrderReportService.getOrderReportList(beginTime, endTime, param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OmsReportFacade#getTomorrowOrderReportList(long,
	 *      long, com.netease.print.daojar.meta.base.DDBParam, java.lang.String)
	 */
	@Override
	public RetArg getTomorrowOrderReportList(long beginTime, long endTime, DDBParam param, 
			List<Long> warehouseIdList) {
		return omsOrderReportService.getTomorrowOrderReportList(beginTime, endTime, param, warehouseIdList);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OmsReportFacade#syncDataforOrder()
	 */
	@Override
	public boolean syncDataforOrder() {
		return omsOrderReportService.syncData();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.OmsReportFacade#syncDataforTomorrowOrder()
	 */
	@Override
	public boolean syncDataforTomorrowOrder() {
		return omsOrderReportService.syncTomorrowData();
	}

	@Override
	public boolean insertReport(OmsMakerReport report) {
		return omsMakerReportService.insert(report);
	}

	@Override
	public OmsMakerReport getReportById(long id) {
		return omsMakerReportService.getReportById(id);
	}

	@Override
	public OmsReportListDTO getAllReport() {
		return omsMakerReportService.getAllReport();
	}

	@Override
	public OmsReportListDTO getReportList(long warehouseId, long startTime, long endTime, int offset, int pageSize) {
		return omsMakerReportService.getReportList(warehouseId, startTime, endTime, offset, pageSize);
	}

	@Override
	public OmsMakerReport getMakerReport(long warehouseId, long date) {
		return omsMakerReportService.getReportList(warehouseId, date);
	}

	@Override
	public boolean insertDelayReport(OmsDelayReport report) {
		return omsMakerReportService.insertDelayReport(report);
	}

	@Resource
	private OmsOrderFormService omsOrderFormService;

	@Resource
	private OmsOrderPackageService omsOrderPackageService;

	@Override
	public List<OmsOrderForm> getOmsOrderFormListByTimeRange(long startTime, long endTime) {
		return omsOrderFormService.getOmsOrderFormListByTimeRange(startTime, endTime);
	}

	@Override
	public OmsOrderForm getOmsOrderFormByOrderId(long omsOrderId) {
		return omsOrderFormService.getOmsOrderFormByOrderId(omsOrderId);
	}

	@Override
	public WarehouseDTO getWarehouseById(long warehouseId) {
		return warehouseService.getWarehouseById(warehouseId);
	}

	@Override
	public List<OmsOrderPackage> getOmsOrderPackageListByOmsOrderFormId(long omsOrderFormId, long userId) {
		return omsOrderPackageService.getOmsOrderPackageListByOmsOrderFormId(omsOrderFormId, userId);
	}

	@Override
	public JSONObject getMakerOrderReport(long warehouseId, long startTime, long endTime, int offset, int limit) {
		JSONObject result = new JSONObject();
		result.put("result", true);
		result.put("code", 200);

		JSONArray list = new JSONArray();
		result.put("list", list);

		OmsReportListDTO dto = omsMakerReportService.getReportList(warehouseId, startTime, endTime, offset, limit);
		if (dto.getList() == null || dto.getList().size() == 0) {
			result.put("total", 0);
			result.put("hasNext", false);
			return result;
		}

		List<OmsMakerReport> reportList = (List<OmsMakerReport>) dto.getList();
		result.put("total", dto.getTotal());
		result.put("hasNext", dto.isHasNext());

		for (OmsMakerReport report : reportList) {
			JSONObject json = new JSONObject();
			json.put("id", report.getId() + "");
			json.put("warehouseId", report.getWarehouseId() + "");
			json.put("makeTime", report.getMakeTime());
			json.put("warehouseOwner", report.getWarehouseOwner());
			json.put("warehouseName", report.getWarehouseName());
			json.put("preBalance", report.getPreBalance());
			json.put("gatherCnt", report.getGatherCnt());
			json.put("cancelCnt", report.getCancelCnt());
			json.put("sentCnt", report.getSentCnt());
			json.put("lackCnt", report.getLackCnt());
			json.put("delayCnt", report.getDelayCnt());
			json.put("curBalance", report.getCurBalance());
			json.put("delayRate", report.getDelayRate());
			list.add(json);
		}

		return result;
	}

	@Override
	public List<OmsDelayReport> getDelayReportList(long warehouseId, long date) {
		return omsMakerReportService.getDelayReportList(warehouseId, date);
	}
	
	@Override
	public JSONObject getDelayOrderReport(long warehouseId, long startTime, long endTime, int offset, int limit) {
		JSONObject result = new JSONObject();
		result.put("result", true);
		result.put("code", 200);

		JSONArray list = new JSONArray();
		result.put("list", list);

		OmsReportListDTO dto = omsMakerReportService.getDelayReportList(warehouseId, startTime, endTime, offset, limit);
		if (dto.getList() == null || dto.getList().size() == 0) {
			result.put("total", 0);
			result.put("hasNext", false);
			return result;
		}

		List<OmsDelayReport> reportList = (List<OmsDelayReport>) dto.getList();
		result.put("total", dto.getTotal());
		result.put("hasNext", dto.isHasNext());

		for (OmsDelayReport report : reportList) {
			JSONObject json = new JSONObject();
			json.put("id", report.getId() + "");
			json.put("warehouseId", report.getWarehouseId() + "");
			json.put("makeTime", report.getMakeTime());
			json.put("warehouseOwner", report.getWarehouseOwner());
			json.put("warehouseName", report.getWarehouseName());
			json.put("omsOrderId", report.getOmsOrderId());
			json.put("createTime", report.getCreateTime());
			json.put("omsOrderFormState", report.getOmsOrderFormState());
			json.put("shipTime", report.getShipTime());
			json.put("consigneeName", report.getConsigneeName());
			json.put("consigneeMobile", report.getConsigneeMobile());
			json.put("mailNO", report.getMailNO());
			list.add(json);
		}

		return result;
	}
}
