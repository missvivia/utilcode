package com.xyl.mmall.oms.service;

import java.util.List;

import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.meta.OmsOrderPackageSku;

/**
 * @author zb
 *
 */
public interface OmsOrderPackageService {
	public List<OmsOrderPackage> getOmsOrderPackageListByOmsOrderFormId(long OmsOrderFormId, long userId);

	public List<OmsOrderPackageSku> getOmsOrderPackageSkuListByPackageId(long packageId, long userId);

	public List<OmsOrderPackage> getUnFeedListByUpdateTime(long updateTime, int limit);
	
	public OmsOrderPackage getOmsOrderPackageByExpress(String mailNO, String expressCompany);

	public boolean setPackageStateFeedBackToApp(long packageId);
	
	/**
	 * 设为拒收快件
	 * @return
	 */
	public boolean setRejectPackage(String mailNO, String expressCompany);
	
	/**
	 * 设为丢失件
	 * @param mailNO
	 * @param expressCompany
	 * @return
	 */
	public boolean setLostPackage(String mailNO, String expressCompany);
	
}
