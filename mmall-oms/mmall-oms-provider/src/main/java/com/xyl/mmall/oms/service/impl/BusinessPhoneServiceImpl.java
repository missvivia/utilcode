package com.xyl.mmall.oms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.BusinessPhoneDao;
import com.xyl.mmall.oms.dao.BusinessPhoneLogDao;
import com.xyl.mmall.oms.meta.BusinessPhoneForm;
import com.xyl.mmall.oms.meta.BusinessPhoneLogForm;
import com.xyl.mmall.oms.service.BusinessPhoneService;

@Service("businessPhoneService")
public class BusinessPhoneServiceImpl implements BusinessPhoneService {

	
	@Autowired
	private BusinessPhoneDao businessPhoneDao;
	
	@Autowired
	private BusinessPhoneLogDao businessPhoneLogDao;
	
	@Override
	public List<BusinessPhoneForm> getBusinessPhoneFormByBusinessAccount(String businessAccount) {
		return businessPhoneDao.getBusinessPhoneByAccountId(businessAccount);
	}

	@Override
	public BusinessPhoneForm addBusinessPhoneForm(BusinessPhoneForm businessPhone) {
		return businessPhoneDao.addBusinessPhone(businessPhone);
	}

	@Override
	public boolean updateBusinessPhoneForm(BusinessPhoneForm businessPhone) {
		return businessPhoneDao.modifyBusinessPhone(businessPhone);
	}

	@Override
	public BusinessPhoneLogForm addBusinessPhoneLogForm(BusinessPhoneLogForm businessPhoneLog) {
		return businessPhoneLogDao.addBusinessPhoneLog(businessPhoneLog);
	}

	@Override
	public List<BusinessPhoneLogForm> getBusinessPhoneLogForm(String businessAccount) {
		return businessPhoneLogDao.getBusinessPhoneLogByAccountId(businessAccount);
	}

	@Override
	public BusinessPhoneForm getPhoneOfAccount(String businessAccount, String phone) {
		return businessPhoneDao.getBusinessByAccountAndPhone(businessAccount, phone);
	}

}
