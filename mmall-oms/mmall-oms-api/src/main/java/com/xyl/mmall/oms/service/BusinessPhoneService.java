/**
 * 
 */
package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.meta.BusinessPhoneForm;
import com.xyl.mmall.oms.meta.BusinessPhoneLogForm;

/**
 * @author liujie
 * 
 */
public interface BusinessPhoneService {

	/**
	 * 
	 * @param businessAccount
	 * @return
	 */
	public List<BusinessPhoneForm> getBusinessPhoneFormByBusinessAccount(String businessAccount);

	/**
	 * 
	 * @param businessPhone
	 * @return
	 */
	public BusinessPhoneForm addBusinessPhoneForm(BusinessPhoneForm businessPhone);
	
	/**
	 * 
	 * @param businessPhone
	 * @return
	 */
	public boolean updateBusinessPhoneForm(BusinessPhoneForm businessPhone);
	
	/**
	 * 
	 * @param businessPhoneLog
	 * @return
	 */
	public BusinessPhoneLogForm addBusinessPhoneLogForm(BusinessPhoneLogForm businessPhoneLog);
	
	/**
	 * 
	 * @param businessAccount
	 * @return
	 */
	public List<BusinessPhoneLogForm> getBusinessPhoneLogForm(String businessAccount);
	
	/**
	 * 
	 * @param businessAccount
	 * @param phone
	 * @return
	 */
	public BusinessPhoneForm getPhoneOfAccount(String businessAccount,String phone);
}
