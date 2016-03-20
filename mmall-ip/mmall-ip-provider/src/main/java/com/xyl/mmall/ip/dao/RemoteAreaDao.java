package com.xyl.mmall.ip.dao;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.meta.RemoteArea;

public interface RemoteAreaDao extends AbstractDao<RemoteArea> {

	/**
	 * 判断该城市是不是偏远地区，如果返回值是
	 * STATE_TRUE 说明是偏远地区，并且该城市下面的所有的区县街道都是偏远地区
	 * STATE_NOTFULLY_TRUE 说明该城市下面的区县有些是偏远地区，有些不是 需要调用isDistrictRemote来进一步确认
	 * STATE_FALSE 说明不是偏远地区，并且该城市下面的所有的区县街道都不是偏远地区
	 * @param cityCode
	 * @param type
	 * @return
	 */
	public RetArg isCityRemote(long cityCode, ExpressType type);
	/**
	 * 判断City状态是STATE_NOTFULLY_TRUE的时候，用来判断区县是不是偏远地区
	 * 只可能返回STATE_TRUE和STATE_FALSE
	 * 注意必须调用了isCityRemote之后再调用这个，不能只调用isDistrictRemote
	 * @param districtCode
	 * @param type
	 * @return
	 */
	public RetArg isDistrictRemote(long districtCode, ExpressType type);

}
