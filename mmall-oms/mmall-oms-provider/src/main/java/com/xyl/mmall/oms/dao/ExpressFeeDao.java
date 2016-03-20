package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.dto.ExpressFeeSearchParam;
import com.xyl.mmall.oms.meta.ExpressFee;

public interface ExpressFeeDao extends AbstractDao<ExpressFee>{
	
	/**
	 * 根据条件查找快递费率
	 * @param param
	 * @return
	 */
	public List<ExpressFee> searchExpressFeeByParam(ExpressFeeSearchParam param);
}
