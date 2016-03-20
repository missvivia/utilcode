package com.xyl.mmall.oms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.OmsOrderPackageDao;
import com.xyl.mmall.oms.dao.OmsOrderPackageSkuDao;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.meta.OmsOrderPackageSku;
import com.xyl.mmall.oms.service.FreightService;
import com.xyl.mmall.oms.service.OmsOrderPackageService;
import com.xyl.mmall.oms.service.RejectPackageService;

@Service("omsOrderPackageService")
public class OmsOrderPackageServiceImpl implements OmsOrderPackageService {

	@Autowired
	private OmsOrderPackageSkuDao omsOrderPackageSkuDao;

	@Autowired
	private OmsOrderPackageDao omsOrderPackageDao;
	
	@Autowired
	private FreightService freightService;
	
	@Autowired
	private RejectPackageService rejectPackageService;
	
	@Override
	public List<OmsOrderPackageSku> getOmsOrderPackageSkuListByPackageId(long packageId, long userId) {
		return omsOrderPackageSkuDao.getListByPackageId(packageId, userId);
	}

	@Override
	public List<OmsOrderPackage> getOmsOrderPackageListByOmsOrderFormId(long omsOrderFormId, long userId) {
		return omsOrderPackageDao.getListByOmsOrderFormId(omsOrderFormId, userId);
	}

	@Override
	public List<OmsOrderPackage> getUnFeedListByUpdateTime(long updateTime, int limit) {
		return omsOrderPackageDao.getUnFeedListByUpdateTime(updateTime, limit);
	}
	
	@Override
	public OmsOrderPackage getOmsOrderPackageByExpress(String mailNO, String expressCompany) {
		return omsOrderPackageDao.getByMailNOAndExpressCompany(mailNO, expressCompany);
	}

	@Override
	public boolean setPackageStateFeedBackToApp(long packageId) {
		return omsOrderPackageDao.setPackageStateFeedBackToApp(packageId);
	}

	@Override
	@Transaction
	public boolean setRejectPackage(String mailNO, String expressCompany) {
		boolean isSucc = omsOrderPackageDao.updatePackageState(mailNO, expressCompany, OmsOrderPackageState.REJECT, OmsOrderPackageState.SHIP) && rejectPackageService.agreeRejectPackage(expressCompany, mailNO); 
		if (!isSucc) {
			throw new ServiceException("setRejectPackage fail.");
		}
		return isSucc;
	}

	@Override
	@Transaction
	public boolean setLostPackage(String mailNO, String expressCompany) {
		boolean isSucc = omsOrderPackageDao.updatePackageState(mailNO, expressCompany, OmsOrderPackageState.LOST, OmsOrderPackageState.SHIP) && freightService.onLostPackagee(expressCompany, mailNO);
		if (!isSucc) {
			throw new ServiceException("setLostPackage fail.");
		}
		return isSucc;
	}

}
