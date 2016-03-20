package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;

/**
 * 
 * @author hzliujie
 * 2014年12月11日 上午11:39:08
 */
public interface SendOutProvinceFormDao extends AbstractDao<OmsSendOutProvinceForm> {
	
	/**
	 * 查询各省发货统计，根据仓库，开始时间，结束时间
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<OmsSendOutProvinceForm> getDataByWarehouseAndDay(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam);

	/**
	 * 根据仓库id和日期获取发货情况
	 * @param warehouseId
	 * @param date
	 * @return
	 */
	public int getSendOutByWarehouseId(long warehouseId, long date);
	
	/**
	 * 根据仓库id和日期获取数据
	 * @param warehouseId
	 * @param date
	 * @return
	 */
	public OmsSendOutProvinceForm getSendOutByWarehouseIdAndDay(long warehouseId, long date);
}
