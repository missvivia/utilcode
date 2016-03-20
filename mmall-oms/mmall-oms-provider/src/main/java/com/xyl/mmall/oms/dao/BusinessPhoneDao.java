package com.xyl.mmall.oms.dao;


import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.BusinessPhoneForm;

/**
 * @author liujie
 * @date 2014-09-28
 */
public interface BusinessPhoneDao extends AbstractDao<BusinessPhoneForm> {

	/**
	 * 通过商家帐号获取商家绑定电话号码
	 * @param businessAccount
	 * @return
	 */
	List<BusinessPhoneForm> getBusinessPhoneByAccountId(String businessAccount);

	/**
	 * 添加商家绑定电话号码
	 * @param businessForm
	 * @return
	 */
	BusinessPhoneForm addBusinessPhone(BusinessPhoneForm businessForm);
	
	/**
	 * 修改商家绑定电话号码
	 * @param businessForm
	 * @return
	 */
	boolean modifyBusinessPhone(BusinessPhoneForm businessForm);
	
	/**
	 * 根据电话和帐号获取商家信息
	 * @param businessAccount
	 * @param phone
	 * @return
	 */
	BusinessPhoneForm getBusinessByAccountAndPhone(String businessAccount,String phone);
	
}
