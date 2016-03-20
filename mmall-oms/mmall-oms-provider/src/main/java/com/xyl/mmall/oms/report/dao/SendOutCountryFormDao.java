package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;

/**
 * 
 * @author hzliujie
 * 2014年12月11日 上午11:39:08
 */
public interface SendOutCountryFormDao extends AbstractDao<OmsSendOutCountryForm> {
	/**
	 * 向报表中塞数据
	 * @param date
	 * @return
	 */
	public List<OmsSendOutCountryForm> getData(long date);
	
	/**
	 * 根据时间范围查看对应数据
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<OmsSendOutCountryForm> getDataByDay(long startDay,long endDay,DDBParam ddbParam);
	
	/**
	 * 根据物流商和时间获取当天数据
	 * @param expressName
	 * @param date
	 * @return
	 */
	public OmsSendOutCountryForm getDataByExpressAndDay(String expressName,long date);
	
}
