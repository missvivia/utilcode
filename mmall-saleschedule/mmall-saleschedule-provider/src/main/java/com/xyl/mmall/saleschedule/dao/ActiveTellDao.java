package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;

public interface ActiveTellDao extends AbstractDao<ActiveTell> {

	/**
	 * 根据查询对象，查找活动通知
	 * @param paramSo
	 * @return
	 */
	public List<ActiveTell> getActiveTellByParam(ActiveTellCommonParamDTO paramSo);

}
