package com.xyl.mmall.oms.dao;


import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.BusinessPhoneLogForm;

/**
 * @author liujie
 * @date 2014-09-28
 */
public interface BusinessPhoneLogDao extends AbstractDao<BusinessPhoneLogForm> {

	/**
	 * 通过商家帐号获取商家绑定电话号码
	 * @param businessAccount
	 * @return
	 */
	List<BusinessPhoneLogForm> getBusinessPhoneLogByAccountId(String businessAccount);

	/**
	 * 添加商家绑定电话号码
	 * @param businessForm
	 * @return
	 */
	BusinessPhoneLogForm addBusinessPhoneLog(BusinessPhoneLogForm businessForm);

	
}
