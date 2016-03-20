package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetail;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;

public interface OmsNoReceiptDetailDao extends AbstractDao<OmsNoReceiptDetail> {

	/**
	 * 获取未签收细节数据
	 * @param day
	 * @return
	 */
	public List<OmsNoReceiptDetail> getNoReceiptDetailBefore(long day);
	
	/**
	 * 获取各省未签收订单归类统计数据
	 * @param day
	 * @return
	 */
	public List<OmsNoReceiptReport> getNoReceiptReportByDay(long day);
	

	/**
	 * 获取未签收明细表
	 * @param day
	 * @return
	 */
	public List<OmsNoReceiptDetailReport> getNoReceiptDetailReportByDay(long day);
	
	/**
	 * 某个仓库发出的订单的包裹的状态
	 * @param day
	 * @param warehouseId
	 * @param OmsOrderFormId
	 * @param OmsOrderPackageState
	 * @return
	 */
	public OmsNoReceiptDetail getNoReceiptDetail(long day,long packageId);
}
